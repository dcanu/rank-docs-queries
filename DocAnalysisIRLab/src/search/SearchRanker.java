/** Exhibits standard Lucene searches for ranking documents.
 *
 * @author Scott Sanner, Paul Thomas, Debashish Chakraborty
 */

package search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchRanker {

    String        _indexPath;
    StandardQueryParser   _parser;
    IndexReader   _reader;
    IndexSearcher _searcher;
    DecimalFormat _df = new DecimalFormat("#.####");

    public SearchRanker(String index_path, String default_field, Analyzer a)
            throws IOException {
        try {
            _indexPath = index_path;
            Directory d = new SimpleFSDirectory(Paths.get(_indexPath));
            DirectoryReader dr = DirectoryReader.open(d);
            _searcher = new IndexSearcher(dr);
            _parser = new StandardQueryParser(a);
        } catch (IOException iox) {
            throw new IOException("Index Files do not exist..."+ "\n" +
                    " Please compile FileIndexBuilder with lab1-test-collection before compiling SearchRanker.");
        }
    }

    public void doSearch(String query, int num_hits, PrintStream ps)
            throws Exception {

        Query q = _parser.parse(query.replaceAll("\\d",""), "CONTENT");
        TopScoreDocCollector collector = TopScoreDocCollector.create(num_hits);
        _searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // create "retrieved.txt" file if it doesn't exist already
        File file = new File("src/retrieved"+num_hits+".txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file,true)){
            if (!file.exists()) file.createNewFile();
//            file.createNewFile();

        // Printing out topic hits in the terminal removing the topic index
        ps.println("Found " + hits.length + " hits " + "for query" + query.replaceAll("\\d","") + ":");


            for (int i = 0; i < hits.length; i++) {
                int docId = hits[i].doc;
                Document d = _searcher.doc(docId);

                query = query.replaceAll("[a-zA-Z]","").trim();
                String filename = (new File(d.get("PATH"))).getName();
                String printLine = query + " Q0 " + filename + " " + i + " " + _df.format(hits[i].score) + " dc";
                ps.println(printLine);
                printLine += "\n";

                // get the content in bytes
                byte[] contentInBytes = printLine.getBytes();

                fileOutputStream.write(contentInBytes);
                fileOutputStream.flush();
//                fileOutputStream.close();

            }
        }

    }

    public static void main(String[] args) throws Exception {

        String index_path = "src/search/lucene.index";
        String default_field = "CONTENT";
        Integer num_hits = 20;

        /* File path for topics for lab test*/
        String file_path = "../lab1-q1-test-collection/topics/air.topics";

        FileIndexBuilder b = new FileIndexBuilder(index_path);

        // Rank search based on index and analyzer
        SearchRanker r = new SearchRanker(b._indexPath, default_field, b._analyzer);
        TopicsReader topicsReader = new TopicsReader();
        ArrayList<String> topics = topicsReader.readTopics(file_path);

        for (String topic : topics){
            r.doSearch(topic, num_hits , System.out);
//            r.doSearch(Stemmer.stem(topic), 20, System.out);
        }


    }

}
