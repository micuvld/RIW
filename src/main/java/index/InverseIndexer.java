package index;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entries.InverseIndexEntry;
import entries.InverseConstructionMap;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by vlad on 08.03.2017.
 */
public class InverseIndexer {
    public static final String INVERSED_INDEX_DICITIONARY_PATH = "/home/vlad/workspace/RIW/outdir/inversed_index.txt";
    private final String INDEX_FILES_PATH = "/home/vlad/workspace/RIW/outdir/temp_invers_indexes";
    private final int PARTITION_SIZE = 2;
    private int index_file_count = 0;

    private InverseConstructionMap inverseConstructionMap;

    public InverseIndexer(String inverseIndexDictionaryPath) {
    }

    public void indexFiles(Path directIndexDictionaryPath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directIndexDictionaryPath.toString()),
                Charset.forName("UTF-8")));
        StringBuilder token = new StringBuilder();
        String filePath = "";
        int fileCount = 0;

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
        writeToIndexDictionary(INVERSED_INDEX_DICITIONARY_PATH, idxiFilePath);
    }

    private void indexFile(String filePath, String directIndexFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directIndexFilePath),
                Charset.forName("UTF-8")));
        StringBuilder token = new StringBuilder();
        String word = "";

        ObjectMapper objectMapper = new ObjectMapper();
        InverseConstructionMap.setFileNameForNextMap(filePath);
        inverseConstructionMap = objectMapper.readValue(new File(directIndexFilePath), InverseConstructionMap.class);
    }

    private void writeIndexFile(String indexFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(new File(indexFilePath), inverseConstructionMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToIndexDictionary(String dictionaryPath, String indexFilePath) {
        TreeMap<String, String> indexDictionary = new TreeMap<>();

        for (InverseIndexEntry entry : inverseConstructionMap.getIndexMap()) {
            indexDictionary.put(entry.getWord(), indexFilePath);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(new File(dictionaryPath), indexDictionary);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void BSBI(Path directIndexDictionaryPath) throws IOException {
//        List<BSBIPartition> partitionsList = generatePartitions(directIndexDictionaryPath);
//        List<String> partitionsIndexFiles = new ArrayList<>();
//
//        for (BSBIPartition partition : partitionsList) {
//            partitionsIndexFiles.add(indexPartition(partition));
//        }
//    }

//    public List<BSBIPartition> generatePartitions(Path directIndexDictionaryPath) throws IOException{
//        List<BSBIPartition> partitionsList = new ArrayList<>();
//        BSBIPartition partition = new BSBIPartition();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directIndexDictionaryPath.toString()),
//                Charset.forName("UTF-8")));
//        StringBuilder token = new StringBuilder();
//        String filePath = "";
//        int fileCount = 0;
//
//        indexList = new TreeMap<>();
//        int c;
//        while((c = reader.read()) != -1) {
//            char charC = (char)c;
//            switch (charC) {
//                case ':':
//                    filePath = token.toString();
//                    token.replace(0, token.length(), "");
//                    break;
//                case '\n':
//                    fileCount++;
//                    partition.addFile(filePath, token.toString());
//                    if (fileCount == PARTITION_SIZE) {
//                        fileCount = 0;
//                        partitionsList.add(partition);
//                        partition = new BSBIPartition();
//                    }
//                    token.replace(0, token.length(), "");
//                    break;
//                default:
//                    token.append(charC);
//            }
//
//        }
//
//        if (fileCount > 0 && fileCount < PARTITION_SIZE) {
//            partitionsList.add(partition);
//        }
//
//        return partitionsList;
//    }
//
//    public String indexPartition(BSBIPartition partition) throws IOException {
//        indexList = new TreeMap<>();
//        for (Map.Entry<String, String> fileEntry : partition.getFiles().entrySet()) {
//            indexFile(fileEntry.getKey(), fileEntry.getValue());
//        }
//
//        String indexFilePath = INDEX_FILES_PATH + "/block" + (index_file_count++);
//        writeIndexFile(indexFilePath);
//
//        return indexFilePath;
//    }
//
//    public void mergePartitions(List<String> partitionsIndexFiles) {
//        indexList = new TreeMap<>();
//
//        for (String indexPath : partitionsIndexFiles) {
//
//        }
//    }
}
