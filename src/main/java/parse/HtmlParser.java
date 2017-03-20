package parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlad on 16.02.2017.
 */
public class HtmlParser {
    public HtmlObject parseLocalDocument(Path path) throws IOException {
        HtmlObject htmlObject = new HtmlObject(path.getParent().toString(), path.getFileName().toString());
        File inputFile = new File(path.toString());

        Document document = Jsoup.parse(inputFile, "UTF-8");
        setHtmlObjectFields(document, htmlObject);

        return htmlObject;
    }

    public HtmlObject parseFromUrl(String url) throws IOException {
        HtmlObject htmlObject = new HtmlObject(url);

        Document document = Jsoup.parse(new URL(url), 1000);
        setHtmlObjectFields(document, htmlObject);

        return htmlObject;
    }

    private void setHtmlObjectFields(Document document, HtmlObject htmlObject) {
        htmlObject.setTitle(document.title());
        htmlObject.setDescription(getMetaTag(document, "description"));
        htmlObject.setKeywords(getMetaTag(document, "keywords"));
        htmlObject.setRobots(getMetaTag(document, "robots"));
        htmlObject.setAnchorHrefs(getAllAnchors(document, htmlObject));
        htmlObject.setText(getAllText(document));
    }

    private List<String> getAllAnchors(Document document, HtmlObject referencedObject) {
        List<String> allAnchors = new ArrayList<String>();
        Elements anchors = document.getElementsByTag("a");

        for (Element anchor : anchors) {
            String href = anchor.attr("href");

            if (href != null && isExternalReference(anchor, document, referencedObject)) {
                allAnchors.add(anchor.absUrl("href"));
            }

        }

        return allAnchors;
    }

    private String getAllText(Document document) {
        return document.body().text();
    }

    private String getMetaTag(Document document, String metaTag) {
        Elements metaTags = document.getElementsByTag("meta");
        for (Element tag : metaTags) {
            String nameTag = tag.attr("name");

            if (nameTag.equals(metaTag)) {
                return tag.attr("content");
            }
        }

        return "*missing*";
    }

    boolean isExternalReference(Element anchor, Document document, HtmlObject referencedObject) {
        String href = anchor.attr("href");
        String absHref;

        int indexOfHashtag = href.indexOf('#');
        int indexOfAbsHashtag;
        String idToCheck;

        if (indexOfHashtag == -1) {
            return true;
        }

        if (indexOfHashtag == 0 || href.substring(0,2).equals("..")) {
            idToCheck = href.substring(1, href.length());
        } else {
            absHref = referencedObject.getBaseUrl() + href;
            indexOfAbsHashtag = absHref.indexOf('#');
            if (absHref.substring(0,indexOfAbsHashtag).equals(document.baseUri())) {
                idToCheck = absHref.substring(indexOfAbsHashtag, absHref.length());
            } else {
                return true;
            }
        }


        return documentContainsReferencedId(idToCheck, document);
    }

    boolean documentContainsReferencedId(String id, Document document) {
        return !id.equals("") && document.getElementById(id) == null;
    }
}
