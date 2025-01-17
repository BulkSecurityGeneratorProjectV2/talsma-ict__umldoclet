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
package net.sourceforge.plantuml.ugraphic.html5;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class UGraphicHtml5 extends AbstractUGraphic<Html5Drawer> implements ClipContainer {

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return this;
	}

	public UGraphicHtml5(HColor defaultBackground, ColorMapper colorMapper, StringBounder stringBounder) {
		super(defaultBackground, colorMapper, stringBounder, new Html5Drawer());
		registerDriver(URectangle.class, new DriverRectangleHtml5(this));
		// registerDriver(UText.class, new DriverTextEps(imDummy, this, strategy));
		ignoreShape(UText.class);
		registerDriver(ULine.class, new DriverLineHtml5(this));
		// registerDriver(UPolygon.class, new DriverPolygonEps(this));
		ignoreShape(UPolygon.class);
		// registerDriver(UEllipse.class, new DriverEllipseEps());
		// registerDriver(UImage.class, new DriverImageEps());
		// registerDriver(UPath.class, new DriverPathEps());
		// registerDriver(DotPath.class, new DriverDotPathEps());
	}

	// public void close() {
	// getEpsGraphics().close();
	// }

	public String generateHtmlCode() {
		return getGraphicObject().generateHtmlCode();
	}

	@Override
	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		os.write(generateHtmlCode().getBytes());
	}

	// public void centerChar(double x, double y, char c, UFont font) {
	// final UnusedSpace unusedSpace = UnusedSpace.getUnusedSpace(font, c);
	//
	// final double xpos = x - unusedSpace.getCenterX() - 0.5;
	// final double ypos = y - unusedSpace.getCenterY() - 0.5;
	//
	// final TextLayout t = new TextLayout("" + c, font.getFont(), imDummy.getFontRenderContext());
	// getGraphicObject().setStrokeColor(getColorMapper().getMappedColor(getParam().getColor()));
	// DriverTextEps.drawPathIterator(getGraphicObject(), xpos + getTranslateX(), ypos + getTranslateY(), t
	// .getOutline(null).getPathIterator(null));
	//
	// }
	//
	// static public String getEpsString(ColorMapper colorMapper, EpsStrategy epsStrategy, UDrawable udrawable)
	// throws IOException {
	// final UGraphicHtml5 ug = new UGraphicHtml5(colorMapper, epsStrategy);
	// udrawable.drawU(ug);
	// return ug.getEPSCode();
	// }
	//
	// static public void copyEpsToFile(ColorMapper colorMapper, UDrawable udrawable, File f) throws IOException {
	// final PrintWriter pw = SecurityUtils.PrintWriter(f);
	// final EpsStrategy epsStrategy = EpsStrategy.getDefault2();
	// pw.print(UGraphicHtml5.getEpsString(colorMapper, epsStrategy, udrawable));
	// pw.close();
	// }
	//
	// public void setAntiAliasing(boolean trueForOn) {
	// }
	//
	// public void startUrl(String url, String tooltip) {
	// getGraphicObject().openLink(url);
	// }
	//
	// public void closeAction() {
	// getGraphicObject().closeLink();
	// }

}
