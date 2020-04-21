/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import java.util.Date;

/**
 * 
 * @author hantsy
 */
public class Item implements Comparable<Item> {
	private String title;

	private String url;

	private String content;

	private Date pubDate;

	private String imageUrl;

	private int imageSize;

	public int getImageSize() {
		return imageSize;
	}

	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int compareTo(Item o) {
		return (-(this.pubDate.compareTo(o.getPubDate())));
	}

}
