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
package net.sourceforge.plantuml.tim.stdlib;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunction;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TFunctionType;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;

public class InvokeProcedure implements TFunction {

	public TFunctionSignature getSignature() {
		return new TFunctionSignature("%invoke_procedure", 1);
	}

	public boolean canCover(int nbArg, Set<String> namedArgument) {
		return nbArg > 0;
	}

	public TFunctionType getFunctionType() {
		return TFunctionType.PROCEDURE;
	}

	public void executeProcedure(TContext context, TMemory memory, LineLocation location, String s)
			throws EaterException, EaterExceptionLocated {
		throw new UnsupportedOperationException();
	}

	public void executeProcedureInternal(TContext context, TMemory memory, List<TValue> args, Map<String, TValue> named)
			throws EaterException, EaterExceptionLocated {
		final String fname = args.get(0).toString();
		final List<TValue> sublist = args.subList(1, args.size());
		final TFunctionSignature signature = new TFunctionSignature(fname, sublist.size());
		final TFunction func = context.getFunctionSmart(signature);
		if (func == null) {
			throw EaterException.located("Cannot find void function " + fname);
		}
		func.executeProcedureInternal(context, memory, sublist, named);
	}

	public TValue executeReturnFunction(TContext context, TMemory memory, LineLocation location, List<TValue> values,
			Map<String, TValue> named) {
		throw new UnsupportedOperationException();
	}

	public boolean isUnquoted() {
		return false;
	}

}
