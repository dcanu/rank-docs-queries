package search;

// PorterStemmer class
import org.tartarus.snowball.ext.PorterStemmer;
import java.util.StringTokenizer;

/**
 * @author Debashish Chakraborty
 * @uniId u4610248
 */
public class Stemmer {

    public static String stem(String content) {

        PorterStemmer porterStemmer = new PorterStemmer();
        // tokenize the content
        StringTokenizer stringTokenizer = new StringTokenizer(content);
        String string;
        String stemmed_content="";

        /*
         * As long as there are more words, stem the word,
         * add it to the stemmed string
         * and convert the word to lower case.
         */
        while (stringTokenizer.hasMoreTokens()){
            string = stringTokenizer.nextToken();
            porterStemmer.setCurrent(string);
            porterStemmer.stem();
            string = porterStemmer.getCurrent();
            stemmed_content +=  string.toLowerCase() + " ";
        }
        return stemmed_content;
    }

//    public static void main(String[] args) {
//        System.out.println(Stemmer.stem("Gtda dasd"));
//    }
}
