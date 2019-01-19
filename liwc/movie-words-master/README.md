# Movie Words

This project provides a suite of tools suitable for analyzing the linguistic content of movie scripts by subtitle files for a collection of movies.

Inputs:
* A list of movie info (title, year, IMDB numbers, subtitle filename)
* Subtitle files
* Dictionary files for analysis

The overall goals of the tools are the following:
* Retrieve additional information about movies from IMDB that may help detect trends in linguistic content, for instance, Budget and Gross Box Office Income.
* Create histograms of words used in subtitle files (ignoring metadata like timestamps contained in the subtitle files).
* Analyze the histograms against dictionary files which categorize words in the histograms.

## Compiling & Building

The project uses [Maven](http://maven.apache.org/). To build the `.jar` file using Maven, navigate to the root project directory and run:

```bash
mvn clean compile assembly:single
```

Specifying the `clean` goal is optional, if you would like to do an incremental build instead:

```bash
mvn compile assembly:single
```

## Running

You can run _MovieWords_ from the `.jar` built above by changing to the `target` directory and running a command like the following:

```bash
java -jar movie-words.jar <action> <options...>
```

Or if you add the `.jar` to the `CLASSPATH` you can run it as follows:

```bash
java com.github.dilijev.moviewords.Main <action> <options...>
```

### Requirements

_MovieWords_ depends on Java 1.8 to compile and run.

### Dependencies

We depend on the following packages:

* [jsoup](http://jsoup.org/)

For details about which versions we currently depend on, see [pom.xml](https://github.com/dilijev/movie-words/blob/master/pom.xml).
