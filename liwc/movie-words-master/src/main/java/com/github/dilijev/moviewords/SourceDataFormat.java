/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords;

import java.util.Arrays;

public class SourceDataFormat {
	public String movieID, idSubtitleFile, subLanguageID, idSubtitle,
			subActualCD, subSumCD, subFormat, movieName, movieYear,
			movieImdbID, userRank, subDownloadsCnt;

	public final String titleRow = "MovieID,IDSubtitleFile,SubLanguageID,IDSubtitle,SubActualCD,SubSumCD,SubFormat,"
			+ "MovieName,MovieYear,MovieImdbID,UserRank,SubDownloadsCnt";

	public SourceDataFormat() {
	}

	public SourceDataFormat(String data) {
		String[] fields = CSVParser.parse(data);
//		System.out.println(Arrays.toString(fields));

		movieID = fields[0];
		idSubtitleFile = fields[1];
		subLanguageID = fields[2];
		idSubtitle = fields[3];
		subActualCD = fields[4];
		subSumCD = fields[5];
		subFormat = fields[6];
		movieName = fields[7].trim();
		movieYear = fields[8];
		movieImdbID = fields[9];
		userRank = fields[10];
		subDownloadsCnt = fields[11];
	}

	@Override
	public String toString() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", movieID,
				idSubtitleFile, subLanguageID, idSubtitle, subActualCD,
				subSumCD, subFormat, movieName, movieYear, movieImdbID,
				userRank, subDownloadsCnt);
	}
}
