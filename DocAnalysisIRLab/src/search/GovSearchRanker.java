/** Modified from SimpleSearchRanker
 * Exhibits standard Lucene searches for ranking documents.
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GovSearchRanker {

    String        _indexPath;
    StandardQueryParser   _parser;
    IndexReader   _reader;
    IndexSearcher _searcher;
    DecimalFormat _df = new DecimalFormat("#.####");

    public GovSearchRanker(String index_path, String default_field, Analyzer a)
            throws IOException {
        try {
            _indexPath = index_path;
            Directory d = new SimpleFSDirectory(Paths.get(_indexPath));
            DirectoryReader dr = DirectoryReader.open(d);
            _searcher = new IndexSearcher(dr);
            _parser = new StandardQueryParser(a);
        } catch (IOException iox) {
            throw new IOException("Index Files for gov-test-collection do not exist..."+ "\n" +
                    " Please compile FileIndexBuilder with gov-test-collection before compiling SearchRanker.");
        }
    }

    public void doSearch(String query, int num_hits, PrintStream ps)
            throws Exception {

        Query q = _parser.parse(query.replaceAll("\\d",""), "CONTENT");
        TopScoreDocCollector collector = TopScoreDocCollector.create(num_hits);
        _searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // create "retrieved.txt" file if it doesn't exist already
        File file = new File("src/gov_retrieved_stemmed"+num_hits+".txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file,true)){
            if (!file.exists()){
                file.createNewFile();

            }

            // Printing out topic hits in the terminal removing the topic index
            ps.println("Found " + hits.length + " hits " + "for query" + query.replaceAll("\\d","") + ":");


            for (int i = 0; i < hits.length; i++) {
                int docId = hits[i].doc;
                Document d = _searcher.doc(docId);


                String filename = (new File(d.get("PATH"))).getName();
                String printLine;
                if (Integer.parseInt((query.toString()).replaceAll("[^\\d]", "").trim()) <10){
                    query = query.replaceAll("[^0-9]", "").trim();
                    printLine = "0"+ query + " Q0 " + filename + " " + i + " " + _df.format(hits[i].score) + " dc";
                }
                else {
                    query = query.replaceAll("[^0-9]", "").trim();
                    printLine =  query + " Q0 " + filename + " " + i + " " + _df.format(hits[i].score) + " dc";
                }

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

    public static void main(String[] args) throws Exception, IOException {

        Integer num_hits = 20;

//        String index_path = "src/search/lucene.index";
        String index_path = "src/gov/lucene.index";
        String default_field = "CONTENT";


//        String file_path = "../lab1-q1-test-collection/topics/air.topics";
        String file_path = "../gov-test-collection/topics/gov.topics";

        FileIndexBuilder b = new FileIndexBuilder(index_path);
//        SimpleSearchRanker r = new SimpleSearchRanker(b._indexPath, default_field, b._analyzer);

        GovSearchRanker r = new GovSearchRanker(b._indexPath, default_field, b._analyzer);
        ArrayList<String> topics = TopicsReader.readTopics(file_path);

        // Comment out if not using Stemmer class
        // Stem the topics before search is conducted
        for (String topic : topics){
            topic = Stemmer.stem(topic);
            r.doSearch(topic, num_hits, System.out);
        }

        // comment out if using Stemmer class
//        for (String topic : topics){
//            r.doSearch(topic, num_hits, System.out);
//        }


    }

}