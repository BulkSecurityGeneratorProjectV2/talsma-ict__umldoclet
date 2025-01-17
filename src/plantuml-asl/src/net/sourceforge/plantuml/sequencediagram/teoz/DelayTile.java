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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class DelayTile extends AbstractTile implements Tile {

	private final Delay delay;
	private final TileArguments tileArguments;
	// private Real first;
	// private Real last;
	private Real middle;

	public Event getEvent() {
		return delay;
	}

	public DelayTile(Delay delay, TileArguments tileArguments) {
		super(tileArguments.getStringBounder());
		this.delay = delay;
		this.tileArguments = tileArguments;
	}

	private void init(StringBounder stringBounder) {
		if (middle != null) {
			return;
		}
		final Real first = tileArguments.getFirstLivingSpace().getPosC(stringBounder);
		final Component comp = getComponent(stringBounder);
		final Real last = tileArguments.getLastLivingSpace().getPosC(stringBounder);
		this.middle = RealUtils.middle(first, last);

	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = tileArguments.getSkin().createComponent(delay.getUsedStyles(), ComponentType.DELAY_TEXT,
				null, tileArguments.getSkinParam(), delay.getText());
		return comp;
	}

	private double getPreferredWidth(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim.getWidth();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		init(stringBounder);
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final Area area = Area.create(getPreferredWidth(stringBounder), dim.getHeight());
		tileArguments.getLivingSpaces().delayOn(getY(), dim.getHeight());

		ug = ug.apply(UTranslate.dx(getMinX().getCurrentValue()));
		comp.drawU(ug, area, (Context2D) ug);
	}

	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final Dimension2D dim = comp.getPreferredDimension(getStringBounder());
		return dim.getHeight();
	}

	public void addConstraints() {
	}

	public Real getMinX() {
		init(getStringBounder());
		return this.middle.addFixed(-getPreferredWidth(getStringBounder()) / 2);
	}

	public Real getMaxX() {
		init(getStringBounder());
		return this.middle.addFixed(getPreferredWidth(getStringBounder()) / 2);
	}

	// private double startingY;
	//
	// public void setStartingY(double startingY) {
	// this.startingY = startingY;
	// }
	//
	// public double getStartingY() {
	// return startingY;
	// }

}
