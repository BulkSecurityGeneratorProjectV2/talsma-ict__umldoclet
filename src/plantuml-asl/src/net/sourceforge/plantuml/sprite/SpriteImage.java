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
package net.sourceforge.plantuml.sprite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.awt.geom.Dimension2D;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperMonochrome;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class SpriteImage implements Sprite {

	private final UImage img;

	public SpriteImage(BufferedImage img) {
		this.img = new UImage(new PixelImage(Objects.requireNonNull(img), AffineTransformType.TYPE_BILINEAR));
	}

	public TextBlock asTextBlock(final HColor color, final double scale, final ColorMapper colorMapper) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				if (colorMapper instanceof ColorMapperMonochrome) {
					ug.draw(img.monochrome().scale(scale));
				} else if (color == null)
					ug.draw(img.scale(scale));
				else
					ug.draw(img.muteColor(colorMapper.toColor(color)).scale(scale));

//				ug.draw(img.muteColor(((HColorSimple) color).getColor999()).scale(scale));

			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(img.getWidth() * scale, img.getHeight() * scale);
			}
		};
	}

	public static Sprite fromInternal(String name) {
		if (name.endsWith(".png"))
			throw new IllegalArgumentException();

		final InputStream is = getInternalSprite(name + ".png");
		if (is == null)
			return null;

		try {
			return new SpriteImage(SImageIO.read(is));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static InputStream getInternalSprite(final String inner) {
		final String path = "/sprites/" + inner;
		final InputStream is = SpriteImage.class.getResourceAsStream(path);
		return is;
	}

}
