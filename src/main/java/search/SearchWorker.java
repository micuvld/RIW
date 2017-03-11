package search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlad on 09.03.2017.
 */
public class SearchWorker {
    public void parseInterogation(String interogation) {
        List<String> tokens = tokenizeInterogation(interogation);
        List<String> files = new ArrayList<String>();

        for (String token : tokens) {
            processToken(files, token);
        }

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
        List<String> toReturn = new ArrayList<>();
        switch(token.charAt(0)) {
            case '^': //OR
                break;
            case '+': //AND
                break;
            case '-': //NOT
                break;
            default:
                toReturn = getFilesList(token);
        }

        return toReturn;
    }


    private List<String> reunion(List<String> l1, List<String> l2) {
        for (String element : l1) {
            if (l2.contains(element)) {
                l2.remove(element);
            }
        }

        l1.addAll(l2);
        return l1;
    }

    private List<String> intersection(List<String> l1, List<String> l2) {
        List<String> intersectionList = new ArrayList<>();

        for (String element : l1) {
            if (l2.contains(element)) {
                intersectionList.add(element);
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

    private List<String> getFilesList(String word) {
        return null;
    }
}
