package index;

import util.Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vlad on 08.03.2017.
 */
public class InverseIndexer implements IIndexer {
    private final String INDEX_FILES_PATH = "/home/vlad/workspace/RIW/outdir/temp_invers_indexes";
    private final String INVERSED_INDEX_DICITIONARY_PATH = "/home/vlad/workspace/RIW/outdir";
    private final int PARTITION_SIZE = 2;
    private int index_file_count = 0;

    private TreeMap<String, IndexEntry> indexList;

    public InverseIndexer(String inverseIndexDictionaryPath) {
    }

    @Override
    public void indexFiles(Path directIndexDictionaryPath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directIndexDictionaryPath.toString()),
                Charset.forName("UTF-8")));
        StringBuilder token = new StringBuilder();
        String filePath = "";
        int fileCount = 0;

        indexList = new TreeMap<>();
        int c;
        while((c = reader.read()) != -1) {
            char charC = (char)c;
            switch (charC) {
                case ':':
                    filePath = token.toString();
                    token.replace(0, token.length(), "");
                    break;
                case '\n':
                    indexFile(filePath, token.toString());
                    token.replace(0, token.length(), "");
                    break;
                default:
                    token.append(charC);
            }
        }

        String idxiFilePath = INDEX_FILES_PATH + "/block.idxi";

        writeIndexFile(idxiFilePath);
        writeToIndexDictionary(INVERSED_INDEX_DICITIONARY_PATH + "/" + "inversed_index.txt", idxiFilePath);
    }



    private void indexFile(String filePath, String directIndexFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directIndexFilePath),
                Charset.forName("UTF-8")));
        StringBuilder token = new StringBuilder();
        String word = "";

        int c;
        while((c = reader.read()) != -1) {
            char charC = (char)c;
            switch (charC) {
                case ':':
                    word = token.toString();
                    token.replace(0, token.length(), "");
                    break;
                case '\n':
                    addToIndexMap(word, filePath, Integer.parseInt(token.toString()));
                    token.replace(0, token.length(), "");
                    break;
                default:
                    token.append(charC);
            }
        }
    }

    private void addToIndexMap(String word, String filePath, int count) {
        if (indexList.containsKey(word)) {
            indexList.get(word).addFile(filePath, count);
        } else {
            indexList.put(word, new IndexEntry(filePath, count));
        }
    }

    private void writeIndexFile(String indexFilePath) throws IOException {
        PrintWriter outWriter = new PrintWriter(new BufferedWriter(
                new FileWriter(indexFilePath, true)));

        for (Map.Entry<String, IndexEntry> entry : indexList.entrySet()) {
            outWriter.write(entry.getKey() + ":" + entry.getValue() + "\n");
        }

        outWriter.close();
    }

    private void writeToIndexDictionary(String dictionaryPath, String indexFilePath) {
        try {
            PrintWriter outWriter = new PrintWriter(new BufferedWriter(new FileWriter(dictionaryPath)));

            for (Map.Entry<String, IndexEntry> indexEntry : indexList.entrySet()) {
                outWriter.write(indexEntry.getKey() + ":" + indexFilePath + "\n");
            }

            outWriter.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BSBI(Path directIndexDictionaryPath) throws IOException {
        List<BSBIPartition> partitionsList = generatePartitions(directIndexDictionaryPath);
        List<String> partitionsIndexFiles = new ArrayList<>();

        for (BSBIPartition partition : partitionsList) {
            partitionsIndexFiles.add(indexPartition(partition));
        }
    }

    public List<BSBIPartition> generatePartitions(Path directIndexDictionaryPath) throws IOException{
        List<BSBIPartition> partitionsList = new ArrayList<>();
        BSBIPartition partition = new BSBIPartition();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directIndexDictionaryPath.toString()),
                Charset.forName("UTF-8")));
        StringBuilder token = new StringBuilder();
        String filePath = "";
        int fileCount = 0;

        indexList = new TreeMap<>();
        int c;
        while((c = reader.read()) != -1) {
            char charC = (char)c;
            switch (charC) {
                case ':':
                    filePath = token.toString();
                    token.replace(0, token.length(), "");
                    break;
                case '\n':
                    fileCount++;
                    partition.addFile(filePath, token.toString());
                    if (fileCount == PARTITION_SIZE) {
                        fileCount = 0;
                        partitionsList.add(partition);
                        partition = new BSBIPartition();
                    }
                    token.replace(0, token.length(), "");
                    break;
                default:
                    token.append(charC);
            }

        }

        if (fileCount > 0 && fileCount < PARTITION_SIZE) {
            partitionsList.add(partition);
        }

        return partitionsList;
    }

    public String indexPartition(BSBIPartition partition) throws IOException {
        indexList = new TreeMap<>();
        for (Map.Entry<String, String> fileEntry : partition.getFiles().entrySet()) {
            indexFile(fileEntry.getKey(), fileEntry.getValue());
        }

        String indexFilePath = INDEX_FILES_PATH + "/block" + (index_file_count++);
        writeIndexFile(indexFilePath);

        return indexFilePath;
    }

    public void mergePartitions(List<String> partitionsIndexFiles) {
        indexList = new TreeMap<>();

        for (String indexPath : partitionsIndexFiles) {

        }
    }
}
