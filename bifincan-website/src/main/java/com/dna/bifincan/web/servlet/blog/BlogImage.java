package com.dna.bifincan.web.servlet.blog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dna.bifincan.model.blog.BlogPost;
import com.dna.bifincan.model.products.Image;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.service.BlogService;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.util.WebConstants;

public class BlogImage extends HttpServlet {
	private static final long serialVersionUID = -7920529090285882046L;

	private static ApplicationContext appContext = null;

	private BlogService blogService;
        private ConfigurationService configurationService;
	
	public BlogImage() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {	
		appContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        if (appContext != null) {
        	blogService = (BlogService)appContext.getBean("blogService");
                configurationService = (ConfigurationService)appContext.getBean("configurationService");
        }
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String blogSlug = request.getParameter("blogslug");  // find the slug indicator

		String size = request.getParameter("size");  // find the size indicator

		byte[] thumb = null;
		if(blogSlug != null && !blogSlug.isEmpty()) {  
			//Long blogPostID = blogService.findBlogPostIdBySlug(blogSlug);  // find the blog post id
			BlogPost post = blogService.findBySlug(blogSlug);  // then the blog post 

			if(WebConstants.SMALL_IMG.equals(size)) {
				if(post.getImageSmall() != null) {
					Image image = blogService.findBlogPostImage(post.getImageSmall());
					if(image != null) thumb = image.getContents(); 
				}				
			} else if(WebConstants.LARGE_IMG.equals(size)) {
				if(post.getImageLarge() != null) {
					Image image = blogService.findBlogPostImage(post.getImageLarge());
					if(image != null) thumb = image.getContents(); 
				}					
			}
		} 
	
		if(thumb != null && thumb.length > 0) {  // if the thumb has been set
			response.setContentLength(thumb.length);

		} else { // otherwise load the 'not-available' image;
			String file = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey())).getValue() + 
						request.getContextPath() + "/resources/images/global/noimage/" + ((WebConstants.SMALL_IMG.equals(size)) ? 
							WebConstants.NOT_AVAILABLE_BLOG_SMALL_IMG_NAME : 
							WebConstants.NOT_AVAILABLE_BLOG_LARGE_IMG_NAME);

			thumb = getBytesFromFile(new URL(file));
			response.setContentType(WebConstants.PNG_MIME);
		}		
		
		response.setContentType(WebConstants.JPEG_MIME);   // for blog post images (default)
		
		if(thumb != null) {  // load the proper image anyway (either product image, brand logo or the default image)
			BufferedInputStream input = null;
			BufferedOutputStream output = null;

			// write the bytes into the response stream
			try {
				input = new BufferedInputStream(new ByteArrayInputStream(thumb));
				output = new BufferedOutputStream(response.getOutputStream());
				byte[] buffer = new byte[8192];
				int length;
				while ((length = input.read(buffer)) > 0) {
					output.write(buffer, 0, length);
				}
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException logOrIgnore) {
						//TODO
					}
				}	
				if (input != null) {
					try {
						input.close();
					} catch (IOException logOrIgnore) {
						//TODO 
					}
				}		
			}
		}
		
	}
	
	private static byte[] getBytesFromFile(URL file) throws IOException {
		ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();

        InputStream in = file.openStream();
        byte[] buf = new byte[512];
        int len;
        while (true) {
            len = in.read(buf);
            if (len == -1) {
                break;
            }
            tmpOut.write(buf, 0, len);
        }
        tmpOut.close();

        return tmpOut.toByteArray();		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("Error: not allows request method [post]");
	}
	
	public void destroy() {
		blogService = null;
		configurationService = null;
	}
}
