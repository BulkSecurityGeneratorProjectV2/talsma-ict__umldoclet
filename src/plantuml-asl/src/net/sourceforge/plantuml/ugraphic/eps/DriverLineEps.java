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
package net.sourceforge.plantuml.ugraphic.eps;

import java.awt.geom.Line2D;

import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

public class DriverLineEps implements UDriver<ULine, EpsGraphics> {

	private final ClipContainer clipContainer;

	public DriverLineEps(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(ULine shape, double x, double y, ColorMapper mapper, UParam param, EpsGraphics eps) {
		double x2 = x + shape.getDX();
		double y2 = y + shape.getDY();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Line2D.Double line = clip.getClippedLine(new Line2D.Double(x, y, x2, y2));
			if (line == null) {
				return;
			}
			x = line.x1;
			y = line.y1;
			x2 = line.x2;
			y2 = line.y2;
		}

		eps.setStrokeColor(mapper.toColor(param.getColor()));
		eps.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDashVisible(), param.getStroke()
				.getDashSpace());
		eps.epsLine(x, y, x2, y2);
	}
}
