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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RacorderFollowTangeante extends RacorderAbstract implements Racorder {

	public DotPath getRacordIn(Rectangle2D rect, Line2D tangeante) {

// Log.println("rect x=" + rect.getX() + " y=" + rect.getY() + " w=" + rect.getWidth() + " h="
//				+ rect.getHeight());
// Log.println("tangeante (" + tangeante.getX1() + "," + tangeante.getY1() + ") (" + tangeante.getX2()
//				+ "," + tangeante.getY2() + ")");

		final DotPath result = new DotPath();

		// final Point2D inter = BezierUtils.intersect((Line2D.Double)
		// tangeante, rect);
		Point2D inter = new LineRectIntersection(tangeante, rect).getIntersection();
// Log.println("inter=" + inter);

		if (inter == null) {
			final Point2D p1 = new Point2D.Double(rect.getMinX(), rect.getMinY());
			final Point2D p2 = new Point2D.Double(rect.getMaxX(), rect.getMinY());
			final Point2D p3 = new Point2D.Double(rect.getMaxX(), rect.getMaxY());
			final Point2D p4 = new Point2D.Double(rect.getMinX(), rect.getMaxY());

			inter = LineRectIntersection.getCloser(tangeante.getP1(), p1, p2, p3, p4);
		}

		final CubicCurve2D.Double curv = new CubicCurve2D.Double(tangeante.getX1(), tangeante.getY1(),
				tangeante.getX1(), tangeante.getY1(), inter.getX(), inter.getY(), inter.getX(), inter.getY());
		return result.addAfter(curv);
	}

}
