/*
 * Author: Doug Ilijev
 * Copyright (c) 2015: Doug Ilijev
 */

package com.github.dilijev.moviewords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.github.dilijev.moviewords.dictionary.Dictionary;
import com.github.dilijev.moviewords.imdb.ImdbScraper;
import com.github.dilijev.moviewords.subtitles.Histogram;
import com.github.dilijev.moviewords.subtitles.SubtitleReader;

public class Main {
	private static void testCSV() throws IOException {
		Scanner sc = new Scanner(new File("E:\\dev\\movie-words\\source.csv"));
		sc.nextLine(); // throw away title line

		ArrayList<DestDataFormat> list = new ArrayList<>();

		int num = 0;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			DestDataFormat ddf = new DestDataFormat(new SourceDataFormat(line));

			// System.out.println(ddf);
			list.add(ddf);

			num++;
			if (num >= 100)
				break;

			// System.out.println(num);
		}

		System.out.println(num + " rows read from file");

		Collections.sort(list, SourceDataTitleOrder.INSTANCE);

		for (DestDataFormat f : list) {
			System.out.println(f.movieName);
		}
	}

	private static void testImdbScraper() {
		System.out.println(ImdbScraper.byImdbId(137523)); // Fight Club
		System.out.println(ImdbScraper.byImdbId(120737)); // LOTR: Fellowship
		System.out.println(ImdbScraper.byImdbId(2702698)); // Sherlock (TV) // TODO not working (TV mode)
	}

	private static void dictionaryMode(String[] args) throws IOException {
		Map<String, String> opts = new HashMap<>();

		if (!(args.length >= 3)) {
			System.err.println("Incorrect usage");
			System.err.println("    dictionary -d DICTFILE -s SOURCEFILE [-h HISTO_DIRECTORY|histo] "
					+ "[-o OUTFILE|dict.csv] [-b BEGIN|0] [-e END|0]");
			System.exit(1);
		}

		// 0 was the verb that got us here, start at 1
		for (int i = 1; i < args.length; i += 2) {
			if (args[i].startsWith("-")) {
				String key = args[i];

				if (i < args.length + 1) {
					String value = args[i + 1];
					opts.put(key, value);
				} else {
					System.err.println("No value given for option: " + key);
				}
			} else {
				System.err.println("Invalid argument: " + args[i]);
				System.exit(1);
			}
		}

		String dictFile = null; // -d
		String sourceFile = null; // -s
		String histoDirectory = "histo"; // -h
		String outFile = "dict.csv"; // -o
		int begin = 0; // -b : default 0 start at the beginning
		int end = 0; // -e : default 0 do the whole file, otherwise stop on this line

		if (opts.containsKey("-d")) {
			dictFile = opts.get("-d");
		}
		if (opts.containsKey("-s")) {
			sourceFile = opts.get("-s");
		}
		if (opts.containsKey("-h")) {
			histoDirectory = opts.get("-h");
		}
		if (opts.containsKey("-o")) {
			outFile = opts.get("-o");
		}
		if (opts.containsKey("-b")) {
			begin = Integer.parseInt(opts.get("-b"));
		}
		if (opts.containsKey("-e")) {
			end = Integer.parseInt(opts.get("-e"));
		}

		System.out.println("--Options--");
		System.out.println("Dictionary: " + dictFile);
		System.out.println("Source file: " + sourceFile);
		System.out.println("Histogram directory: " + histoDirectory);
		System.out.println("Output file: " + outFile);
		System.out.println("Begin: " + begin);
		System.out.println("End: " + end);
		System.out.println();

		if (dictFile == null || sourceFile == null) {
			System.err.println("Must specify both -d and -s in dictionary analysis mode.");
			System.exit(1);
		}

		Dictionary dict = new Dictionary(dictFile);

		// use the rest of the settings for a batch job
		BufferedReader sourceFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
		FileWriter outputFileWriter = new FileWriter(outFile);

		String headers = sourceFileReader.readLine();

		// write all headers to file
		outputFileWriter.write(headers);
		outputFileWriter.write(","); // extra comma
		outputFileWriter.write(dict.getHeaderString());
		outputFileWriter.write("\n");

		// just read up to the lines we care about (starting at line <begin>)
		for (int i = 0; i < begin; i++) {
			String line = sourceFileReader.readLine();
			if (line == null) {
				break;
			}
		}

		// read lines [begin..end)
		for (int i = begin; (end == 0) ? true : (i < end); i++) {
			String row = sourceFileReader.readLine();
			if (row == null) {
				break;
			}

			// TODO set the relative path for input
			dictionaryHelper(dict, i, row, histoDirectory, outputFileWriter);
		}

		sourceFileReader.close();
		outputFileWriter.close();
	}

	private static void dictionaryHelper(Dictionary dict, int index, String row, String histoDirectory,
			FileWriter outputFileWriter) throws IOException {
		dict.resetCounts();

		String[] entries = row.split(",");

		String baseFilename = entries[1]; // IDSubtitleFile

		System.out.print(index + ". Base filename: ");
		System.out.println(baseFilename);

		// write the data currently in the row to the file
		outputFileWriter.write(row);
		outputFileWriter.write(",");

		String histoPath = histoDirectory + File.separator + baseFilename + ".histo.csv";

		InputStream s = new FileInputStream(histoPath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(s));

		reader.readLine(); // ignore the first line (headers)

		String totalsRow = reader.readLine();
		String[] totals = totalsRow.split(",");
		int totalWords = Integer.parseInt(totals[1]);
		dict.setTotalWords(totalWords);

		reader.readLine(); // ignore the third line (headers for histogram)

		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}

			String[] a = line.split(",");
			String word = a[0];
			int count = Integer.parseInt(a[1]);

			dict.analyzeWord(word, count);
		}

		// write the values from the analysis to the output file
		outputFileWriter.write(dict.getValueString());
		outputFileWriter.write("\n");

		reader.close();
	}

	private static void histogramMode(String[] args) throws IOException {
		Map<String, String> opts = new HashMap<>();

		if (!(args.length >= 3)) {
			System.err.println("Incorrect usage");
			System.err.println("    histogram (-f FILENAME | -s SOURCEFILE) [-b BEGIN|0] [-e END|0]");
			System.exit(1);
		}

		// 0 was the verb that got us here, start at 1
		for (int i = 1; i < args.length; i += 2) {
			if (args[i].startsWith("-")) {
				String key = args[i];

				if (i < args.length + 1) {
					String value = args[i + 1];
					opts.put(key, value);
				} else {
					System.err.println("No value given for option: " + key);
				}
			} else {
				System.err.println("Invalid argument: " + args[i]);
				System.exit(1);
			}
		}

		String filename = null; // -f
		String sourceFile = null; // -s
		int begin = 0; // -b : default 0 start at the beginning
		int end = 0; // -e : default 0 do the whole file, otherwise stop on this line

		if (opts.containsKey("-s")) {
			sourceFile = opts.get("-s");
		}
		if (opts.containsKey("-f")) {
			filename = opts.get("-f");
		}
		if (opts.containsKey("-b")) {
			begin = Integer.parseInt(opts.get("-b"));
		}
		if (opts.containsKey("-e")) {
			end = Integer.parseInt(opts.get("-e"));
		}

		System.out.println("--Options--");
		System.out.println("Filename: " + filename);
		System.out.println("Source file: " + sourceFile);
		System.out.println("Begin: " + begin);
		System.out.println("End: " + end);

		if (filename == null && sourceFile == null) {
			System.err.println("Must specify either -s or -f with histogram mode.");
			System.exit(1);
		}

		if (filename != null) {
			// then do just this file
			histogramHelper(filename);
		} else {
			// use the rest of the settings for a batch job
			InputStream s = new FileInputStream(sourceFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(s));

			reader.readLine(); // skip the headers

			// just read up to the lines we care about (starting at line
			// <begin>)
			for (int i = 0; i < begin; i++) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
			}

			// read lines [begin..end)
			for (int i = begin; (end == 0) ? true : (i < end); i++) {
				String row = reader.readLine();
				if (row == null) {
					break;
				}

				String[] entries = row.split(",");

				// IDSubtitleFile
				String subtitleFilename = entries[1];

				System.out.print(i + ". Subtitle file: ");
				System.out.println(subtitleFilename);

				// TODO set the relative path for input and output from a
				// command line argument
				histogramHelper(subtitleFilename, "eng", "histo");
			}

			reader.close();
		}
	}

	private static void histogramHelper(String filename, String inDir, String outDir) throws IOException {
		if (inDir == null) {
			inDir = "";
		} else {
			inDir += File.separatorChar;
		}

		if (outDir == null) {
			outDir = "";
		} else {
			new File(outDir).mkdirs();
			outDir += File.separatorChar;
		}

		Histogram h = new Histogram();
		InputStream s = new FileInputStream(inDir + filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(s));

		SubtitleReader subReader = new SubtitleReader(br);
		subReader.readAllCues(h);

		FileWriter cues = new FileWriter(outDir + filename + ".cues.csv");
		cues.write(h.makeTimestampTable());
		cues.close();

		FileWriter words = new FileWriter(outDir + filename + ".histo.csv");
		words.write(h.makeHistogramTable(false));
		words.close();

		FileWriter wordsCues = new FileWriter(outDir + filename + ".histo.cuelist.csv");
		wordsCues.write(h.makeHistogramTable(true));
		wordsCues.close();

		// FileWriter all = new FileWriter(outDir + filename + ".all.csv");
		// all.write(h.toString());
		// all.close();
	}

	private static void histogramHelper(String filename) throws IOException {
		histogramHelper(filename, null, null);
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			usage();
			return;
		}

		if (args.length >= 1) {
			if (args[0].equals("histogram")) {
				histogramMode(args);
			}
			if (args[0].equals("dictionary")) {
				dictionaryMode(args);
			} else {
				System.err.println("Unknown mode.");
				System.exit(1);
			}
		}
	}

	public static void usage() {
		System.err.println("Wrong number of arguments. Specify a verb (histogram, dictionary) and arguments.");
		System.exit(1);
	}
}
