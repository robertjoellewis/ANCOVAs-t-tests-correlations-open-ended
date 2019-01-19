/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords;

import java.util.Comparator;

public class SourceDataTitleOrder implements Comparator<SourceDataFormat> {
	static final SourceDataTitleOrder INSTANCE = new SourceDataTitleOrder();
	
	private SourceDataTitleOrder() {
		// do nothing
	}
	
	@Override
	public int compare(SourceDataFormat a, SourceDataFormat b) {
		if (a == null) {
			System.out.println("a is null");
		}
		
		if (b == null) {
			System.out.println("b is null");			
		}
		
		return a.movieName.compareTo(b.movieName);
	}
}
