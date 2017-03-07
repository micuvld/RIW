package parse;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by vlad on 23.02.2017.
 */
public class DirectoryParser {
    private IParser parser;

    public DirectoryParser(IParser parser) {
        this.parser = parser;
    }

    public void parseDirectory(Path path, List<HtmlObject> htmlObjects) throws IOException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(path);
        for (Path entry : stream) {
            if (Files.isDirectory(entry)) {
                parseDirectory(entry, htmlObjects);
            } else {
                if (getFileExtension(entry.getFileName().toString()).equals("html")) {
                    htmlObjects.add(parseFile(entry));
                }
            }
        }

    }

    private HtmlObject parseFile(Path entry) {
        try {
            return parser.parseLocalDocument(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');

        if (i > 0) {
            return fileName.substring(i+1);
        } else {
            return "";
        }
    }

    public static String changeFileExtension(String fileName, String newExtension) {
        int i = fileName.lastIndexOf('.');

        if (i > 0) {
            return fileName.substring(0, i + 1) + newExtension;
        } else {
            return "";
        }
    }
}
