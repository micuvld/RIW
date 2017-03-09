package index;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vlad on 28.02.2017.
 */
public class IndexEntry {
    private Map<String, Integer> filesCounters;

    public IndexEntry(String file, int count) {
        this.filesCounters = new HashMap<String, Integer>();
        addFile(file, count);
    }

    public void addFile(String file, int count) {
        filesCounters.put(file, count);
    }

//    public String toStringAsDictionaryForm() {
//        return token + " -> " + index;
//    }
//
//    public String toStringAsIndexToFileForm() {
//        StringBuilder toReturn = new StringBuilder(index + ": ");
//
//        for (Map.Entry<String, Integer> counter : filesCounters.entrySet()) {
//            toReturn.append("<" + counter.getKey() + ", " + counter.getValue() + "> ");
//        }
//
//        return toReturn.toString();
//    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();

        for (Map.Entry<String, Integer> counter : filesCounters.entrySet()) {
            toReturn.append("<" + counter.getKey() + ", " + counter.getValue() + "> ");
        }

        return toReturn.toString();
    }

}