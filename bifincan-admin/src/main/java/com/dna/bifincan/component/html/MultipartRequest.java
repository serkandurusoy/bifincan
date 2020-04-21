package com.dna.bifincan.component.html;

/*
 * net/balusc/http/multipart/MultipartRequest.java
 * 
 * Copyright (C) 2009 BalusC
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 * If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * This class represents a multipart request. It not only abstracts the <code>{@link Part}</code>
 * away, but it also provides direct access to the <code>{@link MultipartMap}</code>, so that one 
 * can get the uploaded files out of it.
 * 
 * @author BalusC
 * @link http://balusc.blogspot.com/2009/12/uploading-files-in-servlet-30.html
 */
public class MultipartRequest extends HttpServletRequestWrapper {
	private static final Logger logger = Logger.getLogger(MultipartRequest.class.getName());
	
    private static final String DEFAULT_ENCODING = "UTF-8";
    
	private Map<String, List<String>> formParams;
	
	private Map<String, List<FileItem>> fileParams;

    private Map<String, String[]> parameterMap;
	
	public MultipartRequest(HttpServletRequest request, ServletFileUpload servletFileUpload) throws IOException {
		super(request);
		formParams = new LinkedHashMap<String, List<String>>();
		fileParams = new LinkedHashMap<String, List<FileItem>>();
		
		parseRequest(request, servletFileUpload);
	}

	@SuppressWarnings("unchecked")
	private void parseRequest(HttpServletRequest request, ServletFileUpload servletFileUpload) throws IOException {
		try {
			List<FileItem> fileItems = servletFileUpload.parseRequest(request);
			
			for(FileItem item : fileItems) {                
				if(item.isFormField())
					addFormParam(item);
				else
					addFileParam(item);
			}
			
		} catch (FileUploadException e) {
			logger.severe("Error in parsing fileupload request");
			
			throw new IOException(e.getMessage());
		}
	}
	
	private void addFileParam(FileItem item) {
		if(fileParams.containsKey(item.getFieldName())) {
			fileParams.get(item.getFieldName()).add(item);
		} else {
			List<FileItem> items = new ArrayList<FileItem>();
			items.add(item);
			fileParams.put(item.getFieldName(), items);
		}
	}
	
	private void addFormParam(FileItem item) {
		// Vnik addition
		String str = null;
		String charset = this.getRequest().getCharacterEncoding();		

		if (charset == null) {
			charset = DEFAULT_ENCODING;
		} 

		try {
			str = item.getString(charset);
		} catch (UnsupportedEncodingException ex) {
			str = item.getString();
			logger.log(Level.SEVERE, null, ex);
		}
		
		if (formParams.containsKey(item.getFieldName())) {
			formParams.get(item.getFieldName()).add(str);
		} else {
			List<String> items = new ArrayList<String>();
			items.add(str);
			formParams.put(item.getFieldName(), items);
		}
		// end of addition
		
		/*	if(formParams.containsKey(item.getFieldName())) {
			formParams.get(item.getFieldName()).add(item.getString());
		} else {
			List<String> items = new ArrayList<String>();
			items.add(item.getString());
			formParams.put(item.getFieldName(), items);
		}*/
	
	}
	
	@Override
	public String getParameter(String name) {
		if(formParams.containsKey(name)) {
			List<String> values = formParams.get(name);
			if(values.isEmpty())
				return "";
			else
				return values.get(0);
		}
		else {
			return super.getParameter(name);
		}
	}

	@Override
	public Map getParameterMap() {
        if(parameterMap == null) {
            Map<String,String[]> map = new LinkedHashMap<String, String[]>();

            for(String formParam : formParams.keySet()) {
                map.put(formParam, formParams.get(formParam).toArray(new String[0]));
            }
            
            map.putAll(super.getParameterMap());
            
            parameterMap = Collections.unmodifiableMap(map);
        }

		return parameterMap;
	}

	@Override
	public Enumeration getParameterNames() {
		Set<String> paramNames = new LinkedHashSet<String>();
		paramNames.addAll(formParams.keySet());
        
        Enumeration<String> original = super.getParameterNames();
        while(original.hasMoreElements()) {
            paramNames.add(original.nextElement());
        }
		
		return Collections.enumeration(paramNames);
	}

	@Override
	public String[] getParameterValues(String name) {
		if(formParams.containsKey(name)) {
			List<String> values = formParams.get(name);
			if(values.isEmpty())
				return new String[0];
			else
				return values.toArray(new String[values.size()]);
		}
		else {
			return super.getParameterValues(name);
		}
	}
	
	public FileItem getFileItem(String name) {
		if(fileParams.containsKey(name)) {
			List<FileItem> items = fileParams.get(name);
			
			return items.get(0);
		} else {
			return null;
		}	
	}

}
