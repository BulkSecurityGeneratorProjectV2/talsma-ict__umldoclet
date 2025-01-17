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
package net.sourceforge.plantuml.graphic;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGroupType;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public abstract class UGraphicDelegator implements UGraphic {

	final private UGraphic ug;

	@Override
	public String toString() {
		return super.toString() + " " + getUg().toString();
	}

	public final boolean matchesProperty(String propertyName) {
		return ug.matchesProperty(propertyName);
	}

	public UGraphicDelegator(UGraphic ug) {
		this.ug = ug;
	}

	public StringBounder getStringBounder() {
		return ug.getStringBounder();
	}

	public UParam getParam() {
		return ug.getParam();
	}

	public void draw(UShape shape) {
		ug.draw(shape);
	}

	public ColorMapper getColorMapper() {
		return ug.getColorMapper();
	}

	@Override
	public void startUrl(Url url) {
		ug.startUrl(url);
	}

	@Override
	public void closeUrl() {
		ug.closeUrl();
	}

	public void startGroup(Map<UGroupType, String> typeIdents) {
		ug.startGroup(typeIdents);
	}

	public void closeGroup() {
		ug.closeGroup();
	}

	protected UGraphic getUg() {
		return ug;
	}

	public void flushUg() {
		ug.flushUg();
	}

	@Override
	public HColor getDefaultBackground() {
		return ug.getDefaultBackground();
	}

	@Override
	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		ug.writeToStream(os, metadata, dpi);
	}
}
