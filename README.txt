DocAnalysisIRLab contains the src/search folder which the following classes:

DocAdder.java - Simple helper class to read a file and construct fields for indexing
                with an IndexWriter (can be memory of file-based).  Called by
                MemoryIndexBuilder and FileIndexBuilder.
FileFinder.java - Simple helper class to recursively return files in a directory
                with a given extension.
FileIndexBuilder.java - Build a file-based Lucene inverted index. Modified with
                Stemmer class.
GovSearchRanker.java - Modified SimpleSearchRanker for ranking gov-test-collection.
IndexDisplay.java - Dump index contents to output stream.
SearchRanker.java - Modified SimpleSearchRanker for ranking lab1-test-collection.
SimpleSearchRanker.java - Exhibits standard Lucene searches for ranking documents.
Stemmer.java - Calls on PorterStemmer class to stem strings and standardize them
                to lower cases
TopicsReader.java - Read topics from the topics file with a given file path

------------------------------------------------------------------------------

NOTE: Before compiling any of the classes, please provide a copy of
lab1-test-collection and gov-test-collection in the same directory as this
README.txt file and DocAnalysisIRLab folder.

------------------------------------------------------------------------------
Usage:

In order to run the lab1-test-collection search engine, please compile
FileIndexBuilder class initially followed by SearchRanker. Please follow
the instructions provided in the comments to determine which lines to comment out
and the ones that need to be un-commented.

------------------------------------------------------------------------------

The instructions are quite similar for gov-test-collection, the only difference
being you would need to compile GovSearchRanker instead of SearchRanker. 
