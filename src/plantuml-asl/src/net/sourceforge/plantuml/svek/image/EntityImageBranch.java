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
package net.sourceforge.plantuml.svek.image;

import java.util.EnumMap;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.awt.geom.Dimension2D;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGroupType;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageBranch extends AbstractEntityImage {

	final private static int SIZE = 12;

	public EntityImageBranch(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
	}

	public StyleSignatureBasic getDefaultStyleDefinition() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE * 2, SIZE * 2);
	}

	final public void drawU(UGraphic ug) {
		final UPolygon diams = new UPolygon();
		diams.addPoint(SIZE, 0);
		diams.addPoint(SIZE * 2, SIZE);
		diams.addPoint(SIZE, SIZE * 2);
		diams.addPoint(0, SIZE);
		diams.addPoint(SIZE, 0);

		final Style style = getDefaultStyleDefinition().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
		final HColor border = style.value(PName.LineColor).asColor(getSkinParam().getThemeStyle(),
				getSkinParam().getIHtmlColorSet());
		final HColor back = style.value(PName.BackGroundColor).asColor(getSkinParam().getThemeStyle(),
				getSkinParam().getIHtmlColorSet());
		final UStroke stroke = style.getStroke();
		final double shadowing = style.value(PName.Shadowing).asDouble();

		diams.setDeltaShadow(shadowing);
		final Map<UGroupType, String> typeIDent = new EnumMap<>(UGroupType.class);
		typeIDent.put(UGroupType.CLASS, "elem " + getEntity().getCode() + " selected");
		typeIDent.put(UGroupType.ID, "elem_" + getEntity().getCode());
		ug.startGroup(typeIDent);
		ug.apply(border).apply(back.bg()).apply(stroke).draw(diams);
		ug.closeGroup();
	}

	public ShapeType getShapeType() {
		return ShapeType.DIAMOND;
	}

}
