/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords;

import java.util.ArrayList;

public class CSVParser {
	private enum ParserState {
		NORMAL, QUOTE, EXPECT_COMMA
	}

	private static int lineNumber = 1;

	public static String[] parse(String data) {
		ArrayList<String> fields = new ArrayList<>();

		int begin = 0;
		int end = 0;
		String temp = "";

		ParserState state = ParserState.NORMAL;
		for (int i = 0; i < data.length(); i++) {
			char c = data.charAt(i);

			switch (state) {
			case NORMAL:
				switch (c) {
				case ',':
					String field = data.substring(begin, end);
					fields.add(field);
					begin = end = i + 1;
					state = ParserState.NORMAL;
					break;
				case '"':
					begin = end = i + 1;
					state = ParserState.QUOTE;
					break;
				default:
					end++;
					state = ParserState.NORMAL;
					break;
				}
				break;
			case QUOTE:
				switch (c) {
				case '"':
					state = ParserState.EXPECT_COMMA;
					break;
				default:
					temp += c;
					state = ParserState.QUOTE;
					break;
				}
				break;
			case EXPECT_COMMA:
				switch (c) {
				case ',':
					String field = data.substring(begin, end);
					fields.add(field);
					begin = end = i + 1;
					state = ParserState.NORMAL;
					break;
				case '"': // a second quote means a literal quote character
					temp += c;
					state = ParserState.QUOTE;
					break;
				default:
					System.err.println("Expected comma in CSV line "
							+ lineNumber + " at column " + (i + 1));
					System.exit(1);
					break;
				}
				break;
			}
		}

		String field = data.substring(begin, end);
		fields.add(field);

		lineNumber++;
		return fields.toArray(new String[] {});
	}
}
