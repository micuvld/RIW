package entries;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vlad on 14.03.2017.
 */
public class InverseIndexEntry {
    @JsonProperty("word")
    String word;
    @JsonProperty("indexes")
    private Map<String, Integer> filesCounters;

    public InverseIndexEntry(String word, String file, int count) {
        this.word = word;
        this.filesCounters = new HashMap<>();
        filesCounters.put(file, count);
    }

    public InverseIndexEntry() {

    }

    public String getWord() {
        return this.word;
    }
    @JsonIgnore
    public List<String> getFiles() {
        return new ArrayList<>(filesCounters.keySet());
    }

    public void addFile(String file, int count) {
        filesCounters.put(file,count);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof String) {
            if (word.equals(o.toString())) {
                return true;
            }
        } else if (o instanceof InverseIndexEntry) {
            if (word.equals(((InverseIndexEntry)o).word)) {
                return true;
            }
        }

        return false;
    }
}
