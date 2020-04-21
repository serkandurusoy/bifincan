/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.util;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author hantsy
 */
public class StringUtils {
	private static final Logger log = LoggerFactory
			.getLogger(StringUtils.class);

	private final static Locale LOC = new Locale("tr");
	
	public static String escapeHTML(String htmlString) {
		// remove all html tags.
		String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");
		return noHTMLString;
	}

	public static String getCleanHTML(String htmlString) {
		// replace new line to html br tag
		String noHTMLString = htmlString.replaceAll("\r\n", "<br/>");
		noHTMLString = noHTMLString.replaceAll("\n", "<br/>");
		noHTMLString = noHTMLString.replaceAll("\r", "<br/>");

		// replace the quote char
		noHTMLString = noHTMLString.replaceAll("\'", "&#39;");
		noHTMLString = noHTMLString.replaceAll("\"", "&#34;");
		return noHTMLString;
	}

	public static String trimRepeatedText(String original) {
		if (original == null || original.trim().length() == 0) {
			return "";
		}
		String[] words = original.split(" ");

		// trim chars in word firstly.
		for (int i = 0; i < words.length; i++) {
			words[i] = trimRepeatedCharactersInWord(words[i]);
		}

		StringBuilder sb = new StringBuilder("");

		boolean trimFlag = false;
		if (words.length > 2) {
			for (int i = 0; i < words.length - 2; i++) {
				if (words[i + 1].equalsIgnoreCase(words[i])
						&& words[i + 2].equalsIgnoreCase(words[i])) {
					trimFlag = true;
					break;
				}

			}
		}

		if (trimFlag) {
			String lastWord = "";
			int count = 1;
			for (int i = 0; i < words.length; i++) {
				if (lastWord.equalsIgnoreCase(words[i])) {
					count++;
				} else {
					lastWord = words[i];
					count = 1;
				}

				if (count < 3) {
					sb.append(words[i]);
					sb.append(" ");
				}
			}
			return trimRepeatedText(sb.toString().trim());

		} else {
			for (int i = 0; i < words.length; i++) {
				sb.append(words[i]);
				if (i < words.length - 1) {
					sb.append(" ");
				}
			}

			return sb.toString();

		}
	}

	public static String trimRepeatedCharactersInWord(String words) {
		if (words == null || words.trim().length() == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder("");

		String lcword = words.toLowerCase(LOC);
		
		if (lcword.length() > 2) {
			char lastChar = 0;
			int count = 1;

			for (int i = 0; i < lcword.length(); i++) {

				if (log.isDebugEnabled()) {
					log.debug("char i@" + words.charAt(i));
					log.debug("lowcase char i" + lcword.charAt(i));
				}

				if (lastChar == lcword.charAt(i)) {
					count++;
				} else {
					lastChar = lcword.charAt(i);
					count = 1;
				}

				if (count < 3) {
					sb.append(words.charAt(i));
				}
			}

			return sb.toString();
		} else {
			return words;
		}
	}
}
