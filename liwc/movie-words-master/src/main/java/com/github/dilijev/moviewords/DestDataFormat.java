/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords;

import java.util.Arrays;

public class DestDataFormat extends SourceDataFormat {
	public String tvShowName, episodeName;
	public boolean isTVShow;

	public final String titleRow = "MovieID,IDSubtitleFile,SubLanguageID,IDSubtitle,SubActualCD,SubSumCD,SubFormat,"
			+ "MovieName,IsTVShow,TVShowName,EpisodeName,MovieYear,MovieImdbID,UserRank,SubDownloadsCnt";

	private DestDataFormat(String data) {
		// TODO implement and make public
	}

	public DestDataFormat(SourceDataFormat sdf) {
		this.movieID = sdf.movieID;
		this.idSubtitleFile = sdf.idSubtitleFile;
		this.subLanguageID = sdf.subLanguageID;
		this.idSubtitle = sdf.idSubtitle;
		this.subActualCD = sdf.subActualCD;
		this.subSumCD = sdf.subSumCD;
		this.subFormat = sdf.subFormat;
		this.movieName = sdf.movieName;

		this.isTVShow = false;
		this.tvShowName = null;
		this.episodeName = null;

		this.movieYear = sdf.movieYear;
		this.movieImdbID = sdf.movieImdbID;
		this.userRank = sdf.userRank;
		this.subDownloadsCnt = sdf.subDownloadsCnt;
	}

	@Override
	public String toString() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%b,%s,%s,%s,%s,%s,%s",
				movieID, idSubtitleFile, subLanguageID, idSubtitle,
				subActualCD, subSumCD, subFormat, movieName, isTVShow,
				tvShowName, episodeName, movieYear, movieImdbID, userRank,
				subDownloadsCnt);
	}
}
