package parse;

import index.FileIndexer;
import util.Utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by vlad on 07.03.2017.
 */
public class DirectoryIndexer {
    FileIndexer fileIndexer;

    public DirectoryIndexer(FileIndexer fileIndexer) {
        this.fileIndexer = fileIndexer;
    }

    public void indexDirectory(Path path, String fileExtension) throws IOException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(path);
        for (Path entry : stream) {
            if (Files.isDirectory(entry)) {
                indexDirectory(entry, fileExtension);
            } else {
                if (Utils.getFileExtension(entry.getFileName().toString()).equals(fileExtension)) {
                    fileIndexer.indexFile(entry);
                }
            }
        }
    }
}
