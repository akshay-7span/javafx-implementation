package webcrawler.crawller;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface Analyzer {
    void analyze(HtmlPage page, String url, boolean isIndexed, boolean crawlingBlocked);
}