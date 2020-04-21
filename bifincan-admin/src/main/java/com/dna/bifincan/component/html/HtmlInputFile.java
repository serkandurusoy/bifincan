package com.dna.bifincan.component.html;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;

/**
 * Faces component for <code>input type="file"</code> field.
 * 
 * @author BalusC
 * @link http://balusc.blogspot.com/2009/12/uploading-files-with-jsf-20-and-servlet.html
 */
@FacesComponent(value = "HtmlInputFile")
public class HtmlInputFile extends HtmlInputText {

    // Getters ------------------------------------------------------------------------------------
    
    @Override
    public String getRendererType() {
        return "javax.faces.File";
    }

}
