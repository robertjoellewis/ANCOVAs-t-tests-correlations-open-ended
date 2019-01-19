/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords.subtitles;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class Histogram {
	private int totalWords;

	private TreeMap<Integer, String> timeStampIndexMap;
	private TreeMap<String, HistogramData> histo;

	public Histogram() {
		totalWords = 0;
		timeStampIndexMap = new TreeMap<>();
		histo = new TreeMap<>();
	}

	public void insertWordCountAtTimestampIndex(String word, int index) {
		if (histo.containsKey(word)) {
			HistogramData data = histo.get(word);
			data.addTimeIndex(index);
		} else {
			HistogramData data = new HistogramData();
			data.addTimeIndex(index);
			histo.put(word, data);
		}

		++totalWords;
	}

	public void addTimeStamp(int index, String timeStamp) {
		timeStampIndexMap.put(index, timeStamp);
	}

	public long getTotalWords() {
		return totalWords;
	}

	public int getUniqueWords() {
		return histo.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		makeTimestampTable(sb);

		sb.append("\n");

		makeHistogramTable(sb, true);

		return sb.toString();
	}

	public String makeTimestampTable() {
		StringBuilder sb = new StringBuilder();
		makeTimestampTable(sb);
		return sb.toString();
	}
	
	public String makeHistogramTable(boolean withCueNumbers) {
		StringBuilder sb = new StringBuilder();
		makeHistogramTable(sb, withCueNumbers);
		return sb.toString();
	}
	
	private void makeTimestampTable(StringBuilder sb) {
		sb.append("Index,Timestamp\n");

		Set<Integer> keySet = timeStampIndexMap.keySet();
		for (Integer i : keySet) {
			String timeStamp = timeStampIndexMap.get(i);
			sb.append(String.format("%d,\"%s\"\n", i, timeStamp));
		}
	}
	
	private void makeHistogramTable(StringBuilder sb, boolean withCueNumbers) {
		sb.append("UNIQUE WORDS,TOTAL WORDS");
		if (withCueNumbers) {
			sb.append(",TOTAL CUES");
		}
		sb.append("\n");
		
		sb.append(String.format("%d,%d", histo.size(), totalWords));
		if (withCueNumbers) {
			sb.append("," + timeStampIndexMap.size());
		}
		sb.append("\n");
		
		sb.append("Word,Count");
		if (withCueNumbers) {
			sb.append(",Cue Numbers");
		}
		sb.append("\n");

		Set<String> wordSet = histo.keySet();
		for (String word : wordSet) {
			HistogramData hd = histo.get(word);
			int count = hd.getCount();
			sb.append(String.format("%s,%d,", word, count));

			if (withCueNumbers) {
				ArrayList<Integer> timeIndexes = hd.getTimeIndexes();
				for (Integer index : timeIndexes) {
					sb.append(String.format("%d,", index));
				}
			}
			
			sb.append("\n");
		}
	}
}
