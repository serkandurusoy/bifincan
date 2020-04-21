package com.dna.bifincan.util;

import com.sun.faces.renderkit.html_basic.OutputLinkRenderer;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

public class ExtendedLinkRendererForPinterest extends OutputLinkRenderer { 
 
    @Override 
    protected void writeCommonLinkAttributes(ResponseWriter writer, UIComponent component) throws IOException {
        super.writeCommonLinkAttributes(writer, component);
        writer.writeAttribute("count-layout", component.getAttributes().get("count-layout"), null);
    }

}
