package search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entries.InverseConstructionMap;
import entries.InverseIndexEntry;
import entries.InverseIndexMap;
import index.InverseIndexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by vlad on 09.03.2017.
 */
public class SearchWorker {
    public static Map<String, String> indexDictionary;

    public SearchWorker() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            indexDictionary = objectMapper.readValue(new File(InverseIndexer.INVERSED_INDEX_DICITIONARY_PATH),
                    new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> parseInterogation(String interogation) {
        List<String> tokens = tokenizeInterogation(interogation);
        List<String> files = new ArrayList<String>();

        for (String token : tokens) {
            files = processToken(files, token);
        }

        return files;
    }

    public List<String> tokenizeInterogation(String interogation) {
        StringBuilder token = new StringBuilder();
        List<String> tokens = new ArrayList<>();

        for (int i = 0; i < interogation.length(); ++i) {
            char currentChar = interogation.charAt(i);
            if (currentChar != ' ') {
                token.append(currentChar);
            } else {
                tokens.add(token.toString());
                token.replace(0, token.length(), "");
            }
        }

        tokens.add(token.toString());
        return tokens;
    }

    private List<String> processToken(List<String> currentFilesList, String token) {
        switch(token.charAt(0)) {
            case '^': //OR
                return reunion(currentFilesList, getFilesList(token.substring(1, token.length())));
            case '+': //AND
                return intersection(currentFilesList, getFilesList(token.substring(1, token.length())));
            case '-': //NOT
                return difference(currentFilesList, getFilesList(token.substring(1, token.length())));
            default:
                return getFilesList(token);
        }
    }

    public List<String> getFilesList(String word) {
        String indexFilePath = indexDictionary.get(word);

        if (indexFilePath == null) {
            return new ArrayList<>();
        }
        InverseIndexMap indexMap = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            indexMap = objectMapper.readValue(new File(indexFilePath), InverseIndexMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (InverseIndexEntry inverseEntry : indexMap.getIndexMap()) {
            if (inverseEntry.getWord().equals(word)){
                return inverseEntry.getFiles();
            }
        }
        return null;
    }


    private List<String> reunion(List<String> l1, List<String> l2) {
        if (l1.size() < l2.size()) {
            List<String> reunionList = new ArrayList<>(l2);

            for (String element : l1) {
                if (!reunionList.contains(element)) {
                    reunionList.add(element);
                }
            }

            return reunionList;
        } else {
            List<String> reunionList = new ArrayList<>(l1);

            for (String element : l2) {
                if (!reunionList.contains(element)) {
                    reunionList.add(element);
                }
            }

            return reunionList;
        }
    }

    //to do - choose the smallest list
    private List<String> intersection(List<String> l1, List<String> l2) {
        List<String> intersectionList = new ArrayList<>();

        if (l1.size() < l2.size()) {
            for (String element : l1) {
                if (l2.contains(element)) {
                    intersectionList.add(element);
                }
            }
        } else {
            for (String element : l2) {
                if (l1.contains(element)) {
                    intersectionList.add(element);
                }
            }
        }

        return intersectionList;
    }

    private List<String> difference(List<String> l1, List<String> l2) {
        List<String> differenceList = new ArrayList<>();

        for (String element : l1) {
            if (!l2.contains(element)) {
                differenceList.add(element);
            }
        }

        return differenceList;
    }
}
