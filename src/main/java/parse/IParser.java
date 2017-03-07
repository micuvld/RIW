package parse;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by vlad on 23.02.2017.
 */
public interface IParser {
    HtmlObject parseLocalDocument(Path path) throws IOException;
    HtmlObject parseFromUrl(String url) throws IOException;
}
