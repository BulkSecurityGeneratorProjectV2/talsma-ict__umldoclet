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
package net.sourceforge.plantuml.creole.legacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Parser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UFont;

public class PSystemCreole extends PlainDiagram {

	private final List<String> lines = new ArrayList<>();

	public PSystemCreole(UmlSource source) {
		super(source);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Creole)");
	}

	public void doCommandLine(String line) {
		lines.add(line);
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) {
		final Display display = Display.create(lines);
		final UFont font = UFont.serif(14);
		final FontConfiguration fontConfiguration = FontConfiguration.blackBlueTrue(font);
		final Sheet sheet = Parser.build(fontConfiguration, HorizontalAlignment.LEFT,
				SkinParam.create(UmlDiagramType.SEQUENCE, fileFormatOption.getStyle()), CreoleMode.FULL).createSheet(display);
		return new SheetBlock1(sheet, LineBreakStrategy.NONE, 0);

		// final Dimension2D dim = TextBlockUtils.getDimension(sheetBlock);
		// final UGraphic2 ug = fileFormat.createUGraphic(new ColorMapperIdentity(), 1,
		// dim, null, false);
		// // sheetBlock.drawU(ug.apply(UTranslate.dy(10)));
		// sheetBlock.drawU(ug);
		// ug.writeImageTOBEMOVED(os, null, 96);
		// return new ImageDataSimple(dim);
	}
}
