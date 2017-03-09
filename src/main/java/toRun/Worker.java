package toRun;

import index.FileIndexer;
import index.InverseIndexer;
import parse.*;

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
        DirectoryIndexer directoryIndexer = new DirectoryIndexer(fileIndexer);
        List<HtmlObject> htmlObjects = new ArrayList<HtmlObject>();
        String relativePath;

        try {
            directoryParser.parseDirectoryAndGenerateHtmlObjects(Paths.get(properties.getProperty("input_directory_path")), htmlObjects);
            for (HtmlObject htmlObject : htmlObjects) {
                relativePath = getPathRelativeToRootDirectory(htmlObject.getBaseUrl(),
                        properties.getProperty("input_directory_path")) + "/";
                htmlObject.writeTextToFile(properties.getProperty("raw_output_directory_path") +
                        relativePath);
            }

            directoryIndexer.indexDirectory(Paths.get(properties.getProperty("raw_output_directory_path")), "txt");
            InverseIndexer inverseIndexer = new InverseIndexer("");
            inverseIndexer.indexFiles(Paths.get("/home/vlad/workspace/RIW/outdir/direct_index.txt"));
            //inverseIndexer.BSBI(Paths.get("/home/vlad/workspace/RIW/outdir/direct_index.txt"));
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


    public static String getPathRelativeToRootDirectory(String absolutePath, String rootPath) {
        return absolutePath.substring(rootPath.length());
    }
}
