/** Simple helper class to read a file and construct fields for indexing
 *  with an IndexWriter (can be memory of file-based).  Called by
 *  MemoryIndexBuilder and FileIndexBuilder.
 * 
 * @author Scott Sanner, Paul Thomas, Debashish Chakraborty
 */

package search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
No change required in this class
 */

public class DocAdder {
	
	public static void AddDoc(IndexWriter w, File f) {
		BufferedReader br = null;
		try {
		    Document doc = new Document();
		    br = new BufferedReader(new FileReader(f));
		    StringBuilder content = new StringBuilder();
		    String line = null;
		    String first_line = null;
		    while ((line = br.readLine()) != null) {
		    	if (first_line == null)
		    		first_line = line;
		    	content.append(line + "\n");
		    }
		    doc.add(new StoredField("PATH", f.getPath()));

			// Comment out if Stemmer class is being used
//		    doc.add(new TextField("FIRST_LINE", first_line, Field.Store.YES));
//		    doc.add(new TextField("CONTENT", content.toString(), Field.Store.YES));

			// Comment out if Stemmer class is NOT being used
			// Implementing stemming following Stemmer.java
			// perform stemming on the first line and content respectively
			doc.add(new TextField("FIRST_LINE", Stemmer.stem(first_line), Field.Store.YES));
			doc.add(new TextField("CONTENT", Stemmer.stem(content.toString()), Field.Store.YES));
		    
		    w.addDocument(doc);
		} catch (IOException e) {
			System.err.println("Could not add file '" + f + "': " + e);
			e.printStackTrace(System.err);
		} finally {
			try {
				if (br != null) {
					br.close();
				} 
			} catch (IOException e) {
					System.err.println("Couldn't close reader for file '" + f + "': " + e);
			}
		}

	}

}
