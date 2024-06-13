package webcrawler.crawller;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import webcrawler.utils.DataStore;

import static webcrawler.utils.CrawlerUtils.*;


public class SEOAnalyzer implements Analyzer {
    @Override
    public void analyze(HtmlPage page, String url, boolean isIndexed, boolean crawlingBlocked) {
        String title = page.getTitleText();
        DomNodeList<HtmlElement> metaTags = page.getHead().getElementsByTagName("meta");
        String contentType = page.getWebResponse().getContentType();

        StringBuilder metaTagsBuilder = new StringBuilder();
        for (HtmlElement metaTag : metaTags) {
            String tagName = metaTag.getAttribute("name");
            String tagContent = metaTag.getAttribute("content");
            metaTagsBuilder.append("meta: ").append(tagName).append(": ").append(tagContent).append("\n");
        }

        StringBuilder headerTagsBuilder = new StringBuilder();
        for (DomElement element : page.getHtmlElementDescendants()) {
            String tagName = element.getTagName();
            if (tagName.matches("h[1]")) {
                String tagContent = element.getTextContent();
                headerTagsBuilder.append(tagName).append(": ").append(tagContent).append("\n");
            }
        }

        String pageLoadTime = getPageLoadTime(url);
        String repeatedWords = countRepeatedWords(url);

        DataStore.insertSEOData(url, title, metaTagsBuilder.toString(), headerTagsBuilder.toString(), contentType, pageLoadTime, repeatedWords, isIndexed, crawlingBlocked);
    }

}
