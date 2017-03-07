package index;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vlad on 28.02.2017.
 */
public class IndexEntry {
    private String index;
    private String token;
    private Map<String, Integer> filesCounters;

    public IndexEntry(String index, String token, String file) {
        this.index = index;
        this.token = token;
        this.filesCounters = new HashMap<String, Integer>();
        addFile(file);
    }

    public void addFile(String file) {
        filesCounters.put(file, 1);
    }

    public void incrementCountForFile(String file) {
        if (filesCounters.containsKey(file)) {
            filesCounters.put(file, filesCounters.get(file) + 1);
        } else {
            addFile(file);
        }
    }

    public String toStringAsDictionaryForm() {
        return token + " -> " + index;
    }

    public String toStringAsIndexToFileForm() {
        StringBuilder toReturn = new StringBuilder(index + ": ");

        for (Map.Entry<String, Integer> counter : filesCounters.entrySet()) {
            toReturn.append("<" + counter.getKey() + ", " + counter.getValue() + "> ");
        }

        return toReturn.toString();
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder(index + " -> " + token + ": ");

        for (Map.Entry<String, Integer> counter : filesCounters.entrySet()) {
            toReturn.append("<" + counter.getKey() + ", " + counter.getValue() + "> ");
        }

        return toReturn.toString();
    }

}