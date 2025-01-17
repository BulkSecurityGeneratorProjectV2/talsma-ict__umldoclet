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
package net.sourceforge.plantuml.ugraphic;

import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperTransparentWrapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public abstract class AbstractCommonUGraphic implements UGraphic {

	private UStroke stroke = new UStroke();
	private UPattern pattern = UPattern.FULL;
	private boolean hidden = false;
	private HColor backColor = null;
	private HColor color = null;
	private boolean enlargeClip = false;

	private final StringBounder stringBounder;
	private UTranslate translate = new UTranslate();

	private final ColorMapper colorMapper;
	private UClip clip;

	private final HColor defaultBackground;

	@Override
	public HColor getDefaultBackground() {
		return defaultBackground;
	}

	public double dpiFactor() {
		return 1;
	}

	public UGraphic apply(UChange change) {
		Objects.requireNonNull(change);
		final AbstractCommonUGraphic copy = copyUGraphic();
		if (change instanceof UTranslate) {
			copy.translate = ((UTranslate) change).compose(copy.translate);
		} else if (change instanceof UClip) {
			copy.clip = (UClip) change;
			copy.clip = copy.clip.translate(getTranslateX(), getTranslateY());
		} else if (change instanceof UStroke) {
			copy.stroke = (UStroke) change;
		} else if (change instanceof UPattern) {
			copy.pattern = (UPattern) change;
		} else if (change instanceof UHidden) {
			copy.hidden = change == UHidden.HIDDEN;
		} else if (change instanceof UBackground) {
			copy.backColor = ((UBackground) change).getBackColor();
		} else if (change instanceof HColorNone) {
			copy.color = null;
		} else if (change instanceof HColor) {
			copy.color = (HColor) change;
		}
		return copy;
	}

	final public UClip getClip() {
		if (enlargeClip && clip != null) {
			return clip.enlarge(1);
		}
		return clip;
	}

	final public void enlargeClip() {
		this.enlargeClip = true;
	}

	public AbstractCommonUGraphic(HColor defaultBackground, ColorMapper colorMapper, StringBounder stringBounder) {
		this.colorMapper = colorMapper;
		this.defaultBackground = defaultBackground;
		this.stringBounder = stringBounder;
	}

	protected AbstractCommonUGraphic(AbstractCommonUGraphic other) {
		this.defaultBackground = other.defaultBackground;
		this.enlargeClip = other.enlargeClip;
		this.colorMapper = other.colorMapper;
		this.stringBounder = other.stringBounder;
		this.translate = other.translate;
		this.clip = other.clip;

		this.stroke = other.stroke;
		this.pattern = other.pattern;
		this.hidden = other.hidden;
		this.color = other.color;
		this.backColor = other.backColor;
	}

	protected abstract AbstractCommonUGraphic copyUGraphic();

	final public UParam getParam() {
		return new UParam() {

			public boolean isHidden() {
				return hidden;
			}

			public UStroke getStroke() {
				return stroke;
			}

			public HColor getColor() {
				return color;
			}

			public HColor getBackcolor() {
				return backColor;
			}

			public UPattern getPattern() {
				return pattern;
			}
		};
	}

	@Override
	public StringBounder getStringBounder() {
		return stringBounder;
	}

	final protected double getTranslateX() {
		return translate.getDx();
	}

	final protected double getTranslateY() {
		return translate.getDy();
	}

	final public ColorMapper getColorMapper() {
		return new ColorMapperTransparentWrapper(colorMapper);
	}

	final public void flushUg() {
	}

	@Override
	public void startUrl(Url url) {
	}

	@Override
	public void closeUrl() {
	}

	public void startGroup(Map<UGroupType, String> typeIdents) {
	}

	public void closeGroup() {
	}

	public boolean matchesProperty(String propertyName) {
		return false;
	}

}
