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
package net.sourceforge.plantuml.tim.expression;

import java.util.ArrayDeque;
import java.util.Deque;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;

// https://en.wikipedia.org/wiki/Shunting-yard_algorithm
// https://en.cppreference.com/w/c/language/operator_precedence
public class ShuntingYard {

	final private TokenStack ouputQueue = new TokenStack();
	final private Deque<Token> operatorStack = new ArrayDeque<>();

	private static final boolean TRACE = false;

	private void traceMe() {
		if (TRACE == false)
			return;
		System.err.println("-------------------");
		System.err.println("operatorStack=" + operatorStack);
		System.err.println("ouputQueue=" + ouputQueue);
		System.err.println("");
	}

	public ShuntingYard(TokenIterator it, Knowledge knowledge) throws EaterException, EaterExceptionLocated {

		while (it.hasMoreTokens()) {
			final Token token = it.nextToken();
			traceMe();
			if (TRACE)
				System.err.println("token=" + token);
			if (token.getTokenType() == TokenType.NUMBER || token.getTokenType() == TokenType.QUOTED_STRING) {
				ouputQueue.add(token);
			} else if (token.getTokenType() == TokenType.FUNCTION_NAME) {
				operatorStack.addFirst(token);
			} else if (token.getTokenType() == TokenType.PLAIN_TEXT) {
				final String name = token.getSurface();
				final TValue variable = knowledge.getVariable(name);
				if (variable == null) {
					if (isVariableName(name) == false) {
						throw EaterException.unlocated("Parsing syntax error about " + name);
					}
					ouputQueue.add(new Token(name, TokenType.QUOTED_STRING, null));
				} else {
					ouputQueue.add(variable.toToken());
				}
			} else if (token.getTokenType() == TokenType.OPERATOR) {
				while ((thereIsAFunctionAtTheTopOfTheOperatorStack(token) //
						|| thereIsAnOperatorAtTheTopOfTheOperatorStackWithGreaterPrecedence(token) //
						|| theOperatorAtTheTopOfTheOperatorStackHasEqualPrecedenceAndIsLeftAssociative(token)) //
						&& theOperatorAtTheTopOfTheOperatorStackIsNotALeftParenthesis(token)) {
					ouputQueue.add(operatorStack.removeFirst());
				}
				// push it onto the operator stack.
				operatorStack.addFirst(token);
			} else if (token.getTokenType() == TokenType.OPEN_PAREN_FUNC) {
				operatorStack.addFirst(token);
			} else if (token.getTokenType() == TokenType.OPEN_PAREN_MATH) {
				operatorStack.addFirst(token);
			} else if (token.getTokenType() == TokenType.CLOSE_PAREN_FUNC) {
				final Token first = operatorStack.removeFirst();
				ouputQueue.add(first);
			} else if (token.getTokenType() == TokenType.CLOSE_PAREN_MATH) {
				while (operatorStack.peekFirst().getTokenType() != TokenType.OPEN_PAREN_MATH) {
					ouputQueue.add(operatorStack.removeFirst());
				}
				if (operatorStack.peekFirst().getTokenType() == TokenType.OPEN_PAREN_MATH) {
					operatorStack.removeFirst();
				}
			} else if (token.getTokenType() == TokenType.COMMA) {
				while (operatorStack.peekFirst() != null
						&& operatorStack.peekFirst().getTokenType() != TokenType.OPEN_PAREN_FUNC) {
					ouputQueue.add(operatorStack.removeFirst());
				}
			} else {
				throw new UnsupportedOperationException(token.toString());
			}
		}

		while (operatorStack.isEmpty() == false) {
			final Token token = operatorStack.removeFirst();
			ouputQueue.add(token);
		}

		// System.err.println("ouputQueue=" + ouputQueue);
	}

	private boolean isVariableName(String name) {
		return name.matches("[a-zA-Z0-9.$_]+");
	}

	private boolean thereIsAFunctionAtTheTopOfTheOperatorStack(Token token) {
		final Token top = operatorStack.peekFirst();
		return top != null && top.getTokenType() == TokenType.FUNCTION_NAME;
	}

	private boolean thereIsAnOperatorAtTheTopOfTheOperatorStackWithGreaterPrecedence(Token token) {
		final Token top = operatorStack.peekFirst();
		if (top != null && top.getTokenType() == TokenType.OPERATOR
				&& top.getTokenOperator().getPrecedence() > token.getTokenOperator().getPrecedence()) {
			return true;
		}
		return false;
	}

	private boolean theOperatorAtTheTopOfTheOperatorStackHasEqualPrecedenceAndIsLeftAssociative(Token token) {
		final Token top = operatorStack.peekFirst();
		if (top != null && top.getTokenType() == TokenType.OPERATOR && top.getTokenOperator().isLeftAssociativity()
				&& top.getTokenOperator().getPrecedence() == token.getTokenOperator().getPrecedence()) {
			return true;
		}
		return false;
	}

	private boolean theOperatorAtTheTopOfTheOperatorStackIsNotALeftParenthesis(Token token) {
		final Token top = operatorStack.peekFirst();
		if (top != null && top.getTokenType() == TokenType.OPEN_PAREN_MATH) {
			return true;
		}
		return true;
	}

	public TokenStack getQueue() {
		return this.ouputQueue;
	}

}
