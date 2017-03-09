package index;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by vlad on 08.03.2017.
 */
public interface IIndexer {
    public void indexFiles(Path path) throws IOException;
}
