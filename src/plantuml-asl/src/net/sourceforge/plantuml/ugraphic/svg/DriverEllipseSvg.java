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
package net.sourceforge.plantuml.ugraphic.svg;

import net.sourceforge.plantuml.svg.SvgGraphics;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

public class DriverEllipseSvg implements UDriver<UEllipse, SvgGraphics> {

	private final ClipContainer clipContainer;

	public DriverEllipseSvg(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UEllipse shape, double x, double y, ColorMapper mapper, UParam param, SvgGraphics svg) {
		final double width = shape.getWidth();
		final double height = shape.getHeight();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			if (clip.isInside(x, y) == false) {
				return;
			}
			if (clip.isInside(x + width, y + height) == false) {
				return;
			}
		}

		DriverRectangleSvg.applyStrokeColor(svg, mapper, param);
		DriverRectangleSvg.applyFillColor(svg, mapper, param);

		svg.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		double start = shape.getStart();
		final double extend = shape.getExtend();
		final double cx = x + width / 2;
		final double cy = y + height / 2;
		if (start == 0 && extend == 0) {
			svg.svgEllipse(cx, cy, width / 2, height / 2, shape.getDeltaShadow());
		} else {
			start = start + 90;
			if (extend > 0) {
				// http://www.itk.ilstu.edu/faculty/javila/SVG/SVG_drawing1/elliptical_curve.htm
				final double x1 = cx + Math.sin(start * Math.PI / 180.) * width / 2;
				final double y1 = cy + Math.cos(start * Math.PI / 180.) * height / 2;
				final double x2 = cx + Math.sin((start + extend) * Math.PI / 180.) * width / 2;
				final double y2 = cy + Math.cos((start + extend) * Math.PI / 180.) * height / 2;
				svg.svgArcEllipse(width / 2, height / 2, x1, y1, x2, y2);
			} else {
				final double x1 = cx + Math.sin((start + extend) * Math.PI / 180.) * width / 2;
				final double y1 = cy + Math.cos((start + extend) * Math.PI / 180.) * height / 2;
				final double x2 = cx + Math.sin(start * Math.PI / 180.) * width / 2;
				final double y2 = cy + Math.cos(start * Math.PI / 180.) * height / 2;
				svg.svgArcEllipse(width / 2, height / 2, x1, y1, x2, y2);

			}
		}
	}

}
