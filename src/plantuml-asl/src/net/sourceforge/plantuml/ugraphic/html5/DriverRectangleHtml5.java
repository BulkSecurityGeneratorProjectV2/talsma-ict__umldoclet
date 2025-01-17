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
package net.sourceforge.plantuml.ugraphic.html5;

import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;

public class DriverRectangleHtml5 implements UDriver<URectangle, Html5Drawer> {

	private final ClipContainer clipContainer;

	public DriverRectangleHtml5(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(URectangle rect, double x, double y, ColorMapper mapper, UParam param, Html5Drawer html) {
		double width = rect.getWidth();
		double height = rect.getHeight();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Rectangle2D.Double r = clip.getClippedRectangle(new Rectangle2D.Double(x, y, width, height));
			x = r.x;
			y = r.y;
			width = r.width;
			height = r.height;
		}

		final double rx = rect.getRx();
		final double ry = rect.getRy();

//		// Shadow
//		if (rect.getDeltaShadow() != 0) {
//			eps.epsRectangleShadow(x, y, width, height, rx / 2, ry / 2, rect.getDeltaShadow());
//		}

		final HColor back = param.getBackcolor();
		if (back instanceof HColorGradient) {
//			eps.setStrokeColor(mapper.getMappedColor(param.getColor()));
//			eps.epsRectangle(x, y, width, height, rx / 2, ry / 2, (HtmlColorGradient) back, mapper);
		} else {
			final String color = param.getColor() == null ? null : mapper.toRGB(param.getColor());
			final String backcolor = param.getColor() == null ? null : mapper.toRGB(param.getBackcolor());

			html.setStrokeColor(color);
			html.setFillColor(backcolor);
//			eps.setStrokeWidth("" + param.getStroke().getThickness(), param.getStroke().getDashVisible(), param
//					.getStroke().getDashSpace());
			html.htmlRectangle(x, y, width, height, rx / 2, ry / 2);
		}
	}
}
