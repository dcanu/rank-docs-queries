package search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Debashish Chakraborty
 * Read topics from the topics file with a given file path
 */
public class TopicsReader {

    public static ArrayList<String> topics = new ArrayList<>();

//    String file_path = "../gov-test-collection/documents/";

    /**
     * @param file_path
     * @return
     */

    public static ArrayList<String> readTopics(String file_path)
    {
        // Read the topics from the designated file path
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file_path)))
        {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null){
//                topics.add(currentLine.split(" ", 2)[1]);
//                topics.toString().replaceAll("[a-zA-Z]","");
                topics.add(currentLine);
            }

//            topics.forEach(System.out::println);

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return topics;

    }

//    public static void main(String[] args){
//
////        String file_path = "../lab1-q1-test-collection/topics/air.topics";
//        String file_path = "../gov-test-collection/topics/gov.topics";
//
//
////        TopicsReader.readTopics(file_path);
//    }
}
