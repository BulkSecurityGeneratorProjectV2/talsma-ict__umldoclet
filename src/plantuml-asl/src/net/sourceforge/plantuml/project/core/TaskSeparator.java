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
package net.sourceforge.plantuml.project.core;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.style.StyleBuilder;

public class TaskSeparator extends AbstractTask implements Task {

	private final String comment;

	public TaskSeparator(StyleBuilder styleBuilder, String comment, int id) {
		super(styleBuilder, new TaskCode("##" + id));
		this.comment = comment;
	}

	public Day getStart() {
		throw new UnsupportedOperationException();
	}

	public Day getEnd() {
		throw new UnsupportedOperationException();
	}

	public void setStart(Day start) {
		throw new UnsupportedOperationException();
	}

	public void setEnd(Day end) {
		throw new UnsupportedOperationException();
	}

	public void setColors(CenterBorderColor... colors) {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return comment;
	}

	public void addResource(Resource resource, int percentage) {
		throw new UnsupportedOperationException();
	}

	public Load getLoad() {
		throw new UnsupportedOperationException();
	}

	public void setLoad(Load load) {
		throw new UnsupportedOperationException();
	}

	public void setDiamond(boolean diamond) {
		throw new UnsupportedOperationException();
	}

	public boolean isDiamond() {
		throw new UnsupportedOperationException();
	}

	public void setCompletion(int completion) {
		throw new UnsupportedOperationException();
	}

	public void setUrl(Url url) {
		throw new UnsupportedOperationException();
	}

	public void addPause(Day pause) {
		throw new UnsupportedOperationException();
	}

	public void addPause(DayOfWeek pause) {
		throw new UnsupportedOperationException();
	}

	public void setNote(Display note) {
	}

}
