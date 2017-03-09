package index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vlad on 09.03.2017.
 */
public class BSBIPartition {
    Map<String, String> files = new HashMap<>();

    public void addFile(String inputFilePath, String indexFilePath) {
        files.put(inputFilePath, indexFilePath);
    }

    public Map<String, String> getFiles() {
        return files;
    }
}
