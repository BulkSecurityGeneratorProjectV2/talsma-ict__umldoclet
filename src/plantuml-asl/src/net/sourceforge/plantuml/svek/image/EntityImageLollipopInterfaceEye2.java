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

import java.util.Objects;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.awt.geom.Dimension2D;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class EntityImageLollipopInterfaceEye2 extends AbstractEntityImage {

	public static final double SIZE = 14;
	private final TextBlock desc;
	private final TextBlock stereo;
	private final SymbolContext ctx;
	final private Url url;

	public EntityImageLollipopInterfaceEye2(ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();

//		final USymbol symbol = Objects.requireNonNull(
//				entity.getUSymbol() == null ? skinParam.componentStyle().toUSymbol() : entity.getUSymbol());

		// final FontParam fontParam = symbol.getFontParam();
		final FontParam fontParam = FontParam.COMPONENT;

		this.desc = BodyFactory.create2(skinParam.getDefaultTextAlignment(HorizontalAlignment.CENTER),
				entity.getDisplay(), skinParam, stereotype, entity, getStyle(fontParam));

		this.url = entity.getUrl99();

		HColor backcolor = getEntity().getColors().getColor(ColorType.BACK);
//		if (backcolor == null)
//			backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), symbol.getColorParamBack());

		final HColor forecolor = HColorUtils.BLACK;
		// final HColor forecolor = SkinParamUtils.getColor(getSkinParam(), getStereo(),
		// symbol.getColorParamBorder());
		this.ctx = new SymbolContext(backcolor, forecolor).withStroke(new UStroke(1.5))
				.withShadow(getSkinParam().shadowing(getEntity().getStereotype()) ? 3 : 0);

		if (stereotype != null && stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) != null
				&& portionShower.showPortion(EntityPortion.STEREOTYPE, entity)) {
//			final FontParam fontParam = symbol.getFontParamStereotype();
//			stereo = Display.getWithNewlines(stereotype.getLabel(getSkinParam().guillemet())).create(
//					FontConfiguration.create(getSkinParam(), fontParam, stereotype), HorizontalAlignment.CENTER,
//					skinParam);
			stereo = TextBlockUtils.empty(0, 0);
		} else {
			stereo = TextBlockUtils.empty(0, 0);
		}

	}

	private Style getStyle(FontParam fontParam) {
		return fontParam.getStyleDefinition(SName.componentDiagram)
				.getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE, SIZE);
	}

	final public void drawU(UGraphic ug) {
		if (url != null) {
			ug.startUrl(url);
		}
		final UEllipse circle = new UEllipse(SIZE, SIZE);
		if (getSkinParam().shadowing(getEntity().getStereotype())) {
			circle.setDeltaShadow(4);
		}
		ctx.apply(ug).draw(circle);

		final Dimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		final double x1 = SIZE / 2 - dimDesc.getWidth() / 2;
		final double y1 = SIZE * 1.4;
		desc.drawU(ug.apply(new UTranslate(x1, y1)));

		final Dimension2D dimStereo = stereo.calculateDimension(ug.getStringBounder());
		final double x2 = SIZE / 2 - dimStereo.getWidth() / 2;
		final double y2 = -dimStereo.getHeight();
		stereo.drawU(ug.apply(new UTranslate(x2, y2)));

		if (url != null) {
			ug.closeUrl();
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

}
