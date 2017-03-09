package parse;

import util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by vlad on 16.02.2017.
 */
public class HtmlObject {
    private String baseUrl = "";
    private String fileName = "";
    private String title = "";
    private String keywords = "";
    private String description = "";
    private String robots = "";
    private List<String> anchorHrefs;
    private String text = "";

    public HtmlObject() {
        anchorHrefs = new ArrayList<String>();
        text = "";
    }

    public HtmlObject(String baseUrl) {
        this();
        this.baseUrl = baseUrl;
    }

    public HtmlObject(String baseUrl, String fileName) {
        this();
        this.baseUrl = baseUrl;
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRobots() {
        return robots;
    }

    public void setRobots(String robots) {
        this.robots = robots;
    }

    public String getText() {
        return text;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getAnchorHrefs() {
        return anchorHrefs;
    }

    public void setAnchorHrefs(List<String> anchorHrefs) {
        this.anchorHrefs = anchorHrefs;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * PRETTY PRINT +
     * TO DO - WRITE TO FILE
     * @return
     */
    @Override
    public String toString() {
        return "Base URL:" + baseUrl +
                "\nTitle: " + title +
                "\nMeta keywords: " + keywords +
                "\nMeta description: " + description +
                "\nMeta robots: " + robots +
                "\nAnchors: " + anchorHrefs.toString() +
                "\nText: " + text.toString();
    }

    public void writeTextToFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(path + Utils.changeFileExtension(fileName, "txt"));
        file.getParentFile().mkdirs();

        PrintWriter writer = new PrintWriter(file);
        writer.println(text.toString());
        writer.close();
    }
}
