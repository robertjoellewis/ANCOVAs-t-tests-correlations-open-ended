/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords.imdb;

import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImdbScraper {
	private static final String template = "http://www.imdb.com/title/%s";

	public static ImdbInfo byImdbId(String id) {
		return byImdbId(Integer.parseInt(id));
	}

	public static ImdbInfo byImdbId(int id) {
		ImdbInfo info = new ImdbInfo();
		
		String code = getImdbCodeString(id);
		info.id = code;
		
		String url = getImdbUrlString(code);
		
		try {
			
			Document document = Jsoup.connect(url).get();
			
			//
			// collect title and year info
			//
			
			Elements elems = document.select("h1 > span");
			String title = elems.get(0).html();
			String year = elems.get(1).select("a").get(0).html();
			
			info.title = title;
			info.year = Integer.parseInt(year);
			
			//
			// collect box office info
			//
			
			Elements titleDetails = document.select("[id=titleDetails]");
			Elements boxOffice = titleDetails.select("h3");
			Element boxOfficeHeading = boxOffice.get(0);
			if (boxOfficeHeading.text().equals("Box Office")) {
				
				Element budget = boxOfficeHeading.nextElementSibling();
				Element gross = budget.nextElementSibling().nextElementSibling();
				
				String budgetText = budget.text();
				String budgetString = extractMoney(budgetText);
				info.budget = extractMoneyInteger(budgetString);
				
				String grossText = gross.text();
				String grossString = extractMoney(grossText);
				info.gross = extractMoneyInteger(grossString);
				
			} else {
				System.err.println("Could not find 'Box Office' section");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return info;
	}

	private static int extractMoneyInteger(String money) {
		money = money.replaceAll("\\D","");
		return Integer.parseInt(money);
	}

	private static String extractMoney(String text) {
		String start = text.substring(text.indexOf("$"));
		String money = start.substring(0, start.indexOf(" "));
		return money;
	}

	private static String getImdbUrlString(String code) {
		return String.format(template, code);
	}

	public static String getImdbCodeString(int id) {
		String imdbIdCode = String.format("tt%07d", id);
		// if it's more than 7 digits long then it's an invalid IMDB code
		// TODO check for this

		return imdbIdCode;
	}
}
