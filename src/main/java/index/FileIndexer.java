package index;


import parse.DirectoryParser;
import parse.HtmlObject;

import java.io.*;
import java.util.*;

/**
 * Created by vlad on 22.02.2017.
 */
public class FileIndexer {
    private final String STOP_WORDS_FILE = "resources/stop_words.txt";
    private final String EXCEPTION_WORDS_FILE = "resources/exception_words.txt";

    private final List<String> stopWords = new ArrayList<String>();
    private final List<String> exceptionWords = new ArrayList<String>();

    private String indexDictionaryPath;
    private Map<String, Integer> indexList;
    private Integer currentIndex = 0;

    public Map<String, Integer> getIndexList() {
        return indexList;
    }

    public FileIndexer(String indexDictionaryPath) {
        this.indexDictionaryPath = indexDictionaryPath;
        populateExceptionAndStopLists();
    }

    /* USE MERGE SORT OF INDEX OF BLOCS
    public void inverseIndex() {
    }
    */

    public void indexFiles(List<HtmlObject> htmlObjects) {
        for (HtmlObject htmlObject : htmlObjects) {
            indexFile(htmlObject);
        }
    }

    public void indexFile(HtmlObject htmlObject) {
        indexList = new TreeMap<String, Integer>();
        StringBuilder word = new StringBuilder();

        for (char c : htmlObject.getText().toCharArray()) {
            if (Character.isLetter(c)) {
                word.append(c);
            } else {
                processWord(word.toString().toLowerCase(), htmlObject.getBaseUrl());
                word.replace(0, word.length(), "");
            }
        }

        String idxdFileName = DirectoryParser.changeFileExtension(htmlObject.getFileName(), "idxd");//htmlObject.getFileName().split("\\.")[0] + ".idxd";
        writeIndexFile(htmlObject.getBaseUrl() + "/" + idxdFileName);
        writeToIndexDictionary(htmlObject.getBaseUrl() + "/" + htmlObject.getFileName(), htmlObject.getBaseUrl() + "/" + idxdFileName);
    }

    private void processWord(String word, String filePath) {
        if (!isException(word)) {
            if (isStopWord(word)) {
                return;
            }

            word = toCanonicalForm(word);
        }

        if (!word.equals("")) {
            if (indexList.containsKey(word)) {
                indexList.put(word, indexList.get(word) + 1);
            } else {
                indexList.put(word, 1);
            }
        }

        currentIndex++;
    }

//    public void indexFile(HtmlObject htmlObject) {
//        StringBuilder word = new StringBuilder();
//        String wordIndex;
//
//        for (char c : htmlObject.getText().toCharArray()) {
//            if (Character.isLetter(c)) {
//                word.append(c);
//            } else {
//                wordIndex = addWordIndex(word.toString());
//                addFileForIndex(wordIndex, htmlObject.getBaseUrl());
//                word.replace(0, word.length(), "");
//            }
//        }
//
//    }
//
//    private String addToDictionary(String word, String fileName) {
//        // TO DO!!!
//    }
//
//    private String addWordIndex(String word) {
//        if (indexDictionary.containsKey(word)) {
//            return indexDictionary.get(word);
//        } else {
//            indexDictionary.put(word, (currentIndex).toString());
//            return (currentIndex++).toString();
//        }
//    }
//
//    private void addFileForIndex(String index, String filePath) {
//        if (fileDictionary.containsKey(index)) {
//            fileDictionary.put(index, fileDictionary.get(index) + ", " + filePath);
//        } else {
//            fileDictionary.put(index, filePath);
//        }
//    }

    private boolean isException(String word) {
        return exceptionWords.contains(word);
    }

    private boolean isStopWord(String word) {
        return stopWords.contains(word);
    }

    private String toCanonicalForm(String word) {
        return word;
    }

    private void populateExceptionAndStopLists() {
        BufferedReader bufferedReader = null;
        String currentLine;

        try {
            bufferedReader = new BufferedReader(new FileReader(STOP_WORDS_FILE));
            while ((currentLine = bufferedReader.readLine()) != null) {
                stopWords.add(currentLine);
            }

            bufferedReader = new BufferedReader(new FileReader(EXCEPTION_WORDS_FILE));
            while ((currentLine = bufferedReader.readLine()) != null) {
                exceptionWords.add(currentLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        StringBuilder stringToReturn = new StringBuilder();

        for (Map.Entry<String, Integer> entry : indexList.entrySet()) {
            stringToReturn.append(entry.getKey() + " " + entry.getValue() + "\n");
        }

        return stringToReturn.toString();
    }

    public void writeIndexFile(String indexFilePath) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(indexFilePath, "UTF-8");

            for (Map.Entry<String, Integer> entry : indexList.entrySet()) {
                writer.write(entry.getKey() + " : " + entry.getValue() + "\n");
            }

            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void writeToIndexDictionary(String inputFilePath, String indexFilePath) {
        try {
            FileWriter fw = new FileWriter(indexDictionaryPath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter outWriter = new PrintWriter(bw);

            outWriter.write(inputFilePath + " -> " + indexFilePath + "\n");

            outWriter.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
