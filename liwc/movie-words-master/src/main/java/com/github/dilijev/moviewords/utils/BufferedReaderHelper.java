/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Predicate;

public class BufferedReaderHelper {
	BufferedReader reader;

	public BufferedReaderHelper(BufferedReader reader) {
		this.reader = reader;
	}

	public String nextLine() throws IOException {
		return reader.readLine();
	}

	public String nextLineMatching(Predicate<String> pred) throws IOException {
		String line = null;
		do {
			line = reader.readLine();
		} while (line != null && !pred.test(line));
		return line;
	}

	public String nextNonEmptyLine() throws IOException {
		return nextLineMatching(line -> !(line.trim().isEmpty()));
	}
}
