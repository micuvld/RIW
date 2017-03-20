package entries;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by vlad on 14.03.2017.
 */
public class AbstractInverseIndexMap {
    @JsonIgnore
    public static String fileNameForNextMap;
    @JsonProperty("indexMap")
    public static List<InverseIndexEntry> indexMap = new ArrayList<>();

    public List<InverseIndexEntry> getIndexMap() {
        return indexMap;
    }

    public static void setFileNameForNextMap(String fileName) {
        fileNameForNextMap = fileName;
    }
}
