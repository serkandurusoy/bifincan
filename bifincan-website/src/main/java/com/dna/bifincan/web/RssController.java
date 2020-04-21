/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import com.dna.bifincan.model.blog.BlogPost;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.service.BlogService;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.ImageService;
import com.dna.bifincan.service.ProductService;
import java.io.Serializable;
import java.util.*;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RssController implements Serializable {
	private static final long serialVersionUID = -9215161622812527577L;

	private static final Logger log = LoggerFactory.getLogger(RssController.class);

    @Inject
    BlogService blogService;

    @Inject
    ProductService productService;

    @Inject
    ImageService imageService;

    @Inject
    private ConfigurationService configurationService;

    public String getBaseUrl() {
        String applicationCanonicalURL = "https://www.bifincan.com";
        Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.APPLICATION_CANONICAL_URL.getKey()));
        if (canonicalUrlConfig != null) {
            applicationCanonicalURL = canonicalUrlConfig.getValue();
        }
        return applicationCanonicalURL;
    }

    @RequestMapping(value = "/rss", method = RequestMethod.GET)
    public ModelAndView rss(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/rss+xml");
        response.setCharacterEncoding("UTF-8");
        
        Locale locale=new Locale("en");
        TimeZone timezone=TimeZone.getTimeZone("GMT");

        ModelAndView model = new ModelAndView();
        model.setViewName("rss");
        model.addObject("feed", buildRssItems());
        model.addObject("locale", locale);
        model.addObject("timezone", timezone);
        model.addObject("base", getBaseUrl() + request.getContextPath());
        model.addObject("rsstitle", getEscapedHtml("bifincan, tüketicinin sesini markaya duyuran bi icat"));
        model.addObject("rssdescription", getEscapedHtml("Daha kaliteli ürün ve hizmetler almak için biraraya gelerek markalara ve ürünlerine sesini duyuran bilinçli tüketicilerin, tüketici dostu markalar ile buluşma noktası."));

        return model;
    }

    @RequestMapping(value = "/sitemap", method = RequestMethod.GET)
    public ModelAndView sitemap(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        ModelAndView model = new ModelAndView();
        model.setViewName("sitemap");
        model.addObject("items", buildSitemapItems());
        model.addObject("base", getBaseUrl() + request.getContextPath());

        return model;
    }

    private List<Item> buildSitemapItems() {
        log.debug("build sitemap itmes");
        List<BlogPost> _posts = blogService.findAllPosts();
        List<Product> _products = productService.findAllActiveProducts();

        List<Item> items = new ArrayList<Item>();
        for (BlogPost _post : _posts) {
            Item _item = new Item();
            _item.setUrl("bilog/" + _post.getSlug());
            _item.setPubDate(_post.getPostedAt());
            items.add(_item);
        }

        for (Product _product : _products) {
            Item _item = new Item();
            _item.setUrl("hediyeler/" + _product.getSlug());
            _item.setPubDate(_product.getEntryDate());
            items.add(_item);
        }


        Collections.sort(items);

        return items;
    }

    private Feed buildRssItems() {
        log.debug("build rss itmes");
        List<BlogPost> _posts = blogService.findLastPosts(25);
        List<Product> _products = productService.findActiveProducts(25);

        List<Item> items = new ArrayList<Item>();
        for (BlogPost _post : _posts) {
            Item _item = new Item();
            _item.setTitle(getEscapedHtml(_post.getTitle()));
            _item.setContent(getEscapedHtml(_post.getTeaser()));
            _item.setPubDate(_post.getPostedAt());
            _item.setUrl("bilog/" + _post.getSlug());
            _item.setImageUrl("gorseller/bilog-yazilari/" + _post.getSlug() + "-buyuk-resim.jpg");
            if (_post.getImageLarge() != null) {
                _item.setImageSize(imageService.findById(_post.getImageLarge()).getMediaLengthInBytes());
            } else {
                _item.setImageSize(0);
            }

            items.add(_item);
        }


        for (Product _product : _products) {
            Item _item = new Item();
            _item.setTitle(getEscapedHtml(_product.getBrand().getName() + " " + _product.getName()));
            _item.setContent(getEscapedHtml(_product.getShortDescription()));
            _item.setPubDate(_product.getEntryDate());
            _item.setUrl("hediyeler/" + _product.getSlug());
            _item.setImageUrl("gorseller/hediye-resimleri/" + _product.getSlug() + "-detay-resim.jpg");
            if (_product.getImageDetail() != null) {
                _item.setImageSize(imageService.findById(_product.getImageDetail()).getMediaLengthInBytes());
            } else {
                _item.setImageSize(0);
            }

            items.add(_item);
        }


        Collections.sort(items);

        Feed feed = new Feed();

        Date lastDate = new Date();
        if (!items.isEmpty()) {
            lastDate = items.get(0).getPubDate();
        }

        feed.setLastBuildDate(lastDate);

        if (items.size() > 25) {
            feed.setItems(items.subList(0, 25));
        } else {
            feed.setItems(items);
        }


        return feed;
    }

    public String getEscapedHtml(String htmlString) {

        /*
         * ı => &#305; => &#x131; ş => &#351; => &#x15F; ç => &#231; => &ccedil;
         * ö => &#246; => &ouml; ü => &#252; => &uuml; ğ => &#287; => &#x11F; İ
         * => &#304; => &#x130; Ş => &#350; => &#x15E; Ç => &#199; => &Ccedil; Ö
         * => &#214; => &Ouml; Ğ => &#286; => &#x11E; Ü => &#220; => &Uuml;
         */

        //replace new line to whitespace
        String noHTMLString = htmlString.replaceAll("\r\n", " ");
        noHTMLString = noHTMLString.replaceAll("\n", " ");
        noHTMLString = noHTMLString.replaceAll("\r", " ");

        //replace the & char
        noHTMLString = noHTMLString.replaceAll("&", "&amp;");

        //replace the quote char
        noHTMLString = noHTMLString.replaceAll("\'", "&#39;");
        noHTMLString = noHTMLString.replaceAll("\"", "&#34;");

        //replace < > chars
        noHTMLString = noHTMLString.replaceAll("<", "&lt;");
        noHTMLString = noHTMLString.replaceAll(">", "&gt;");

        //replace turkish chars
        noHTMLString = noHTMLString.replaceAll("ı", "&#305;");
        noHTMLString = noHTMLString.replaceAll("ş", "&#351;");
        noHTMLString = noHTMLString.replaceAll("ç", "&#231;");
        noHTMLString = noHTMLString.replaceAll("ö", "&#246;");
        noHTMLString = noHTMLString.replaceAll("ü", "&#252;");
        noHTMLString = noHTMLString.replaceAll("ğ", "&#287;");
        noHTMLString = noHTMLString.replaceAll("İ", "&#304;");
        noHTMLString = noHTMLString.replaceAll("Ş", "&#350;");
        noHTMLString = noHTMLString.replaceAll("Ç", "&#199;");
        noHTMLString = noHTMLString.replaceAll("Ö", "&#214;");
        noHTMLString = noHTMLString.replaceAll("Ğ", "&#286;");
        noHTMLString = noHTMLString.replaceAll("Ü", "&#220;");

        return noHTMLString;
    }
}
