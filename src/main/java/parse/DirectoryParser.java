package parse;


import util.Utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by vlad on 23.02.2017.
 */
public class DirectoryParser {
    private HtmlParser parser;

    public DirectoryParser(HtmlParser parser) {
        this.parser = parser;
    }

    public void parseDirectoryAndGenerateHtmlObjects(Path path, List<HtmlObject> htmlObjects) throws IOException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(path);
        for (Path entry : stream) {
            if (Files.isDirectory(entry)) {
                parseDirectoryAndGenerateHtmlObjects(entry, htmlObjects);
            } else {
                if (Utils.getFileExtension(entry.getFileName().toString()).equals("html")) {
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
}
