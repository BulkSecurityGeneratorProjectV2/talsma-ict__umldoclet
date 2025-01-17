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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class CommandPackageEmpty extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackageEmpty() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPackageEmpty.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("package"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("DISPLAY", "([%g][^%g]+[%g]|[^#%s{}]*)"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("CODE", "([%pLN_.]+)") //
						)), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("COLOR", "(#[0-9a-fA-F]{6}|#?\\w+)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\}"), //
				RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String idShort;
		final String display;
		if (arg.get("CODE", 0) == null) {
			if (StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0)).length() == 0) {
				idShort = "##" + UniqueSequence.getValue();
				display = null;
			} else {
				idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0));
				display = idShort;
			}
		} else {
			display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0));
			idShort = arg.get("CODE", 0);
		}
		final IGroup currentPackage = diagram.getCurrentGroup();
		final Ident ident = diagram.buildLeafIdent(idShort);
		final Code code = diagram.V1972() ? ident : diagram.buildCode(idShort);
		diagram.gotoGroup(ident, code, Display.getWithNewlines(display), GroupType.PACKAGE, currentPackage,
				NamespaceStrategy.SINGLE);
		final IEntity p = diagram.getCurrentGroup();
		final String color = arg.get("COLOR", 0);
		if (color != null) {
			p.setSpecificColorTOBEREMOVED(ColorType.BACK,
					diagram.getSkinParam().getIHtmlColorSet().getColor(diagram.getSkinParam().getThemeStyle(), color));
		}
		diagram.endGroup();
		return CommandExecutionResult.ok();
	}

}
