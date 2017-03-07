package toRun;

import index.FileIndexer;
import parse.DirectoryParser;
import parse.HtmlObject;
import parse.HtmlParser;
import parse.IParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by vlad on 16.02.2017.
 */
public class Worker {
    public static void main(String[] args) {
        Properties properties = loadProperties();

        IParser parser = new HtmlParser();
        DirectoryParser directoryParser = new DirectoryParser(parser);

        FileIndexer fileIndexer = new FileIndexer(properties.getProperty("index_dictionary_path"));
        List<HtmlObject> htmlObjects = new ArrayList<HtmlObject>();

        try {
            directoryParser.parseDirectory(Paths.get(properties.getProperty("input_directory_path")), htmlObjects);
            for (HtmlObject htmlObject : htmlObjects) {
                htmlObject.writeTextToFile(properties.getProperty("raw_output_directory_path"));
            }

            fileIndexer.indexFiles(htmlObjects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties loadProperties() {
        Properties properties = new Properties();
        FileInputStream propertiesFileStream = null;

        try {
            propertiesFileStream = new FileInputStream("configuration/configuration.properties");
            properties.load(propertiesFileStream);
            propertiesFileStream.close();
            return properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
