/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords.dictionary;

public class CategoryInfo {
	private String name;
	private int count;

	public CategoryInfo(String name) {
		this.name = name;
		this.count = 0;
	}

	public void incrementCount(int n) {
		count += n;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public void resetCount() {
		this.count = 0;
	}

	public String toString() {
		return String.format("{%s,%d}", name, count);
	}
}
