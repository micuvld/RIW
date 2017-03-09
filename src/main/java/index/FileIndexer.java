package index;


import parse.HtmlObject;
import util.Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by vlad on 22.02.2017.
 */
public class FileIndexer implements IIndexer {
    private final String STOP_WORDS_FILE = "resources/stop_words.txt";
    private final String EXCEPTION_WORDS_FILE = "resources/exception_words.txt";
    private final String INDEX_DIRECTORY_PATH = "/home/vlad/workspace/RIW/outdir/";

    private final List<String> stopWords = new ArrayList<String>();
    private final List<String> exceptionWords = new ArrayList<String>();

    //private String indexDictionaryPath;
    private Map<String, Integer> indexList;

    public Map<String, Integer> getIndexList() {
        return indexList;
    }

    public FileIndexer(String indexDictionaryPath) {
        //this.indexDictionaryPath = indexDictionaryPath;
        populateExceptionAndStopLists();
    }

    /* USE MERGE SORT OF INDEX OF BLOCS
    public void inverseIndex() {
    }
    */

    public void indexHtmlObjects(List<HtmlObject> htmlObjects) {
        for (HtmlObject htmlObject : htmlObjects) {
            indexHtmlObject(htmlObject);
        }
    }

    public void indexHtmlObject(HtmlObject htmlObject) {
        indexList = new TreeMap<String, Integer>();
        StringBuilder word = new StringBuilder();

        for (char c : htmlObject.getText().toCharArray()) {
            if (Character.isLetter(c)) {
                word.append(c);
            } else {
                processWord(word.toString().toLowerCase());
                word.replace(0, word.length(), "");
            }
        }

        String idxdFileName = Utils.changeFileExtension(htmlObject.getFileName(), "idxd");
        writeIndexFile(htmlObject.getBaseUrl() + "/" + idxdFileName);
        writeToIndexDictionary(htmlObject.getBaseUrl() + "/" + htmlObject.getFileName(), htmlObject.getBaseUrl() + "/" + idxdFileName);
    }

    public void indexFile(Path path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString()),
                        Charset.forName("UTF-8")));
        indexList = new TreeMap<String, Integer>();
        StringBuilder word = new StringBuilder();

        int c;
        while((c = reader.read()) != -1) {
            char charC = (char)c;
            if (Character.isLetter(charC)) {
                word.append(charC);
            } else {
                processWord(word.toString().toLowerCase());
                word.replace(0, word.length(), "");
            }
        }

        String parentPath = path.getParent().toString();
        String fileName = path.getFileName().toString();
        String idxdFileName = Utils.changeFileExtension(fileName, "idxd");

        writeIndexFile(path.getParent().toString() + "/" + idxdFileName);
        writeToIndexDictionary(parentPath + "/" + fileName, parentPath + "/" + idxdFileName);

    }

    private void processWord(String word) {
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
    }

//    public void indexHtmlObject(HtmlObject htmlObject) {
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
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
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
            FileWriter fw = new FileWriter(INDEX_DIRECTORY_PATH + "direct_index.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter outWriter = new PrintWriter(bw);

            outWriter.write(Utils.getRelativePath(inputFilePath, "/home/vlad/workspace/RIW/outdir/raw_output/") + ":" + indexFilePath + "\n");

            outWriter.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void indexFiles(Path path) throws IOException {

    }
}
