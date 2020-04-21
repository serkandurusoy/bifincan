package com.dna.bifincan.web.servlet.product;

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

import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.Image;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.service.BrandService;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.util.WebConstants;

public class ProductImage extends HttpServlet {
	private static final long serialVersionUID = 4859931320337306863L;
	private static ApplicationContext appContext = null;

	private ProductService productService;
	private BrandService brandService;
        private ConfigurationService configurationService;
	
	public ProductImage() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {	
		appContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        if (appContext != null) {
        	productService = (ProductService)appContext.getBean("productService");
        	brandService = (BrandService)appContext.getBean("brandService");
        	configurationService = (ConfigurationService)appContext.getBean("configurationService");
        }
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// find the slug indicators
		String productSlug = request.getParameter("productslug");
		String brandSlug = request.getParameter("brandslug");
		
		boolean productRequested = true;
		byte[] thumb = null;
		if (productSlug != null && !productSlug.isEmpty()) {  // if it is product image
			Long productID = productService.findProductIdBySlug(productSlug);  // find the product id
			
			if(productID != null) {  // to avoid any id not found case
				Product product = productService.findById(productID); // then the product (don't check also for valid age)
	
				String size = request.getParameter("size");  // find the size indicator
	
				if(WebConstants.SMALL_IMG.equals(size)) {
					if(product.getImageSmall() != null) {
						Image image = brandService.findBrandLogo(product.getImageSmall());
						if(image != null) thumb = image.getContents(); 
					}				
				} else if(WebConstants.LARGE_IMG.equals(size)) {
					if(product.getImageLarge() != null) {
						Image image = brandService.findBrandLogo(product.getImageLarge());
						if(image != null) thumb = image.getContents(); 
					}					
				} else if(WebConstants.DETAIL_IMG.equals(size)) {
					if(product.getImageDetail() != null) {
						Image image = brandService.findBrandLogo(product.getImageDetail());
						if(image != null) thumb = image.getContents(); 
					}		
				}	
			}
				
		} else if(brandSlug != null && !brandSlug.isEmpty()) {  // if it is brand logo
			Long brandID = brandService.findBrandIdBySlug(brandSlug);  // find the brand id
                        if (brandID != null) {
                            Brand brand = brandService.findBrandById(brandID);  // then the brand 
                            if(brand.getLogo() != null) {
                                    Image image = brandService.findBrandLogo(brand.getLogo());
                                    if(image != null) thumb = image.getContents(); 
                            }
                            
                        } productRequested = false;
		}
	
		if(thumb != null && thumb.length > 0) {  // if the thumb has been set
			response.setContentLength(thumb.length);
			
			if(productRequested) {
				response.setContentType(WebConstants.JPEG_MIME);   // for product images (default)
			} else {
				response.setContentType(WebConstants.PNG_MIME);  // for brand logos (default)
			}
		} else { // otherwise load the 'not-available' image;
			if (productRequested) {
                            String size = request.getParameter("size");
                            if (WebConstants.DETAIL_IMG.equals(size)) {
                                String file = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey())).getValue() + 
                                            request.getContextPath() + "/resources/images/global/noimage/" + WebConstants.NOT_AVAILABLE_PRODUCT_DETAIL_IMG_NAME ;

                                thumb = getBytesFromFile(new URL(file));
                                response.setContentType(WebConstants.JPEG_MIME);
                            } else if (WebConstants.LARGE_IMG.equals(size)) {
                                String file = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey())).getValue() + 
                                            request.getContextPath() + "/resources/images/global/noimage/" + WebConstants.NOT_AVAILABLE_PRODUCT_LARGE_IMG_NAME ;

                                thumb = getBytesFromFile(new URL(file));
                                response.setContentType(WebConstants.JPEG_MIME);
                            } else {
                                String file = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey())).getValue() + 
                                            request.getContextPath() + "/resources/images/global/noimage/" + WebConstants.NOT_AVAILABLE_PRODUCT_SMALL_IMG_NAME ;

                                thumb = getBytesFromFile(new URL(file));
                                response.setContentType(WebConstants.JPEG_MIME);
                            }
                        
                        } else {
                            String file = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey())).getValue() + 
                                        request.getContextPath() + "/resources/images/global/noimage/" + WebConstants.NOT_AVAILABLE_BRAND_LOGO_NAME ;

                            thumb = getBytesFromFile(new URL(file));
                            response.setContentType(WebConstants.PNG_MIME);
                        }
                        
		}		
		
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
		productService = null;
		brandService = null;
                configurationService = null;
	}
}
