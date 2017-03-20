package entries;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by vlad on 14.03.2017.
 */
public class InverseIndexMap extends AbstractInverseIndexMap{
   // @JsonCreator
   // public InverseIndexMap(@JsonProperty("indexMap") ArrayList<InverseIndexEntry> inverseIndexList) {
    //    System.out.println();
//        if (indexMap.isEmpty()) {
//            for (Map.Entry<String, Integer> entry : directIndexEntry.entrySet()) {
//                indexMap.add(new InverseIndexEntry(entry.getKey(), fileNameForNextMap, entry.getValue()));
//            }
//        } else {
//            for (Map.Entry<String, Integer> entry : directIndexEntry.entrySet()) {
//                InverseIndexEntry foundEntry = null;
//                for (InverseIndexEntry inverseEntry : indexMap) {
//                    if (inverseEntry.word.equals(entry.getKey())) {
//                        foundEntry = inverseEntry;
//                        break; // optional
//                    }
//                }
//
//                if (foundEntry == null) {
//                    indexMap.add(new InverseIndexEntry(entry.getKey(), fileNameForNextMap, entry.getValue()));
//                } else {
//                    foundEntry.addFile(fileNameForNextMap, entry.getValue());
//                }
//            }
//        }

    //}
}
