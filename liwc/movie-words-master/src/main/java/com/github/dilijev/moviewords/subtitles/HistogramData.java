/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords.subtitles;

import java.util.ArrayList;

public class HistogramData {
	private int count;
	private ArrayList<Integer> timeIndexes;
	
	public HistogramData() {
		this.count = 0;
		this.timeIndexes = new ArrayList<>();
	}
	
	private void incrementCount() {
		count += 1;
	}
	
	public void addTimeIndex(int index) {
		timeIndexes.add(index);
		incrementCount();
	}
	
	public int getCount() {
		return count;
	}
	
	public ArrayList<Integer> getTimeIndexes() {
		return timeIndexes;
	}
}
