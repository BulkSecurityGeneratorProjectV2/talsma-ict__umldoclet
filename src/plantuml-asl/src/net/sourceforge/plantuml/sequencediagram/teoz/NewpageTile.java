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

import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class NewpageTile extends AbstractTile {

	private final Newpage newpage;
	private final TileArguments tileArguments;

	@Override
	public double getContactPointRelative() {
		return 0;
	}

	public NewpageTile(Newpage newpage, TileArguments tileArguments) {
		super(tileArguments.getStringBounder());
		this.newpage = newpage;
		this.tileArguments = tileArguments;
	}

	public void drawU(UGraphic ug) {
	}

	public double getPreferredHeight() {
		return 0;
	}

	public void addConstraints() {
	}

	public Real getMinX() {
		return tileArguments.getOrigin();
	}

	public Real getMaxX() {
		return tileArguments.getOrigin();
	}

	public Event getEvent() {
		return newpage;
	}

}
