/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords.imdb;

import java.text.NumberFormat;

public class ImdbInfo {
	private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
	
	// basics
	public String title;
	public int year;
	public String id;
	
	// box office
	public int budget;
	public int gross;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{\n");
		sb.append("  Title: \"" + title + "\"\n");
		sb.append("  Year: " + year + "\n");
		sb.append("  IMDB ID: " + id + "\n");
		sb.append(String.format("  Budget: %s (%d)\n", currencyFormatter.format(budget), budget));
		sb.append(String.format("  Gross: %s (%d)\n", currencyFormatter.format(gross), gross));
		sb.append("}\n");
		
		return sb.toString();
	}
}
