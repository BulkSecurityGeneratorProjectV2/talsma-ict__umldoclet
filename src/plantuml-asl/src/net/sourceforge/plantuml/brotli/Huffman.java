/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Original Author:  Arnaud Roques
 */
/* Copyright 2015 Google Inc. All Rights Reserved.

   https://github.com/google/brotli/blob/master/LICENSE

   Distributed under MIT license.
   See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
*/

package net.sourceforge.plantuml.brotli;

/**
 * Utilities for building Huffman decoding tables.
 */
final class Huffman {

  private static final int MAX_LENGTH = 15;

  /**
   * Returns reverse(reverse(key, len) + 1, len).
   *
   * <p> reverse(key, len) is the bit-wise reversal of the len least significant bits of key.
   */
  private static int getNextKey(int key, int len) {
    int step = 1 << (len - 1);
    while ((key & step) != 0) {
      step >>= 1;
    }
    return (key & (step - 1)) + step;
  }

  /**
   * Stores {@code item} in {@code table[0], table[step], table[2 * step] .., table[end]}.
   *
   * <p> Assumes that end is an integer multiple of step.
   */
  private static void replicateValue(int[] table, int offset, int step, int end, int item) {
    do {
      end -= step;
      table[offset + end] = item;
    } while (end > 0);
  }

  /**
   * @param count histogram of bit lengths for the remaining symbols,
   * @param len code length of the next processed symbol.
   * @return table width of the next 2nd level table.
   */
  private static int nextTableBitSize(int[] count, int len, int rootBits) {
    int left = 1 << (len - rootBits);
    while (len < MAX_LENGTH) {
      left -= count[len];
      if (left <= 0) {
        break;
      }
      len++;
      left <<= 1;
    }
    return len - rootBits;
  }

  /**
   * Builds Huffman lookup table assuming code lengths are in symbol order.
   */
  static void buildHuffmanTable(int[] rootTable, int tableOffset, int rootBits, int[] codeLengths,
      int codeLengthsSize) {
    int key; // Reversed prefix code.
    int[] sorted = new int[codeLengthsSize]; // Symbols sorted by code length.
    // TODO: fill with zeroes?
    int[] count = new int[MAX_LENGTH + 1]; // Number of codes of each length.
    int[] offset = new int[MAX_LENGTH + 1]; // Offsets in sorted table for each length.
    int symbol;

    // Build histogram of code lengths.
    for (symbol = 0; symbol < codeLengthsSize; symbol++) {
      count[codeLengths[symbol]]++;
    }

    // Generate offsets into sorted symbol table by code length.
    offset[1] = 0;
    for (int len = 1; len < MAX_LENGTH; len++) {
      offset[len + 1] = offset[len] + count[len];
    }

    // Sort symbols by length, by symbol order within each length.
    for (symbol = 0; symbol < codeLengthsSize; symbol++) {
      if (codeLengths[symbol] != 0) {
        sorted[offset[codeLengths[symbol]]++] = symbol;
      }
    }

    int tableBits = rootBits;
    int tableSize = 1 << tableBits;
    int totalSize = tableSize;

    // Special case code with only one value.
    if (offset[MAX_LENGTH] == 1) {
      for (key = 0; key < totalSize; key++) {
        rootTable[tableOffset + key] = sorted[0];
      }
      return;
    }

    // Fill in root table.
    key = 0;
    symbol = 0;
    for (int len = 1, step = 2; len <= rootBits; len++, step <<= 1) {
      for (; count[len] > 0; count[len]--) {
        replicateValue(rootTable, tableOffset + key, step, tableSize, len << 16 | sorted[symbol++]);
        key = getNextKey(key, len);
      }
    }

    // Fill in 2nd level tables and add pointers to root table.
    int mask = totalSize - 1;
    int low = -1;
    int currentOffset = tableOffset;
    for (int len = rootBits + 1, step = 2; len <= MAX_LENGTH; len++, step <<= 1) {
      for (; count[len] > 0; count[len]--) {
        if ((key & mask) != low) {
          currentOffset += tableSize;
          tableBits = nextTableBitSize(count, len, rootBits);
          tableSize = 1 << tableBits;
          totalSize += tableSize;
          low = key & mask;
          rootTable[tableOffset + low] =
              (tableBits + rootBits) << 16 | (currentOffset - tableOffset - low);
        }
        replicateValue(rootTable, currentOffset + (key >> rootBits), step, tableSize,
            (len - rootBits) << 16 | sorted[symbol++]);
        key = getNextKey(key, len);
      }
    }
  }
}
