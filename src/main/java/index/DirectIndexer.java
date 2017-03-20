package index;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import util.Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by vlad on 22.02.2017.
 */
public class DirectIndexer {
    private final String STOP_WORDS_FILE = "resources/stop_words.txt";
    private final String EXCEPTION_WORDS_FILE = "resources/exception_words.txt";
    private final String INDEX_DIRECTORY_PATH = "/home/vlad/workspace/RIW/outdir/";

    private final List<String> stopWords = new ArrayList<String>();
    private final List<String> exceptionWords = new ArrayList<String>();

    private TreeMap<String, Integer> indexList;

    public DirectIndexer(String indexDictionaryPath) {
        populateExceptionAndStopLists();
    }

    public void indexFile(Path path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString()),
                        Charset.forName("UTF-8")));
        indexList = new TreeMap<>();
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
                indexList.replace(word, indexList.get(word) + 1);
            } else {
                indexList.put(word, 1);
            }
        }
    }

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

    public void writeIndexFile(String indexFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(new File(indexFilePath), indexList);
        } catch (IOException e) {
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
    
}
