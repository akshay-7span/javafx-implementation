package webcrawler.crawller;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static webcrawler.utils.CrawlerUtils.*;
import static webcrawler.utils.DBUtils.insertCrawledData;


public class LinkCrawler implements Crawler {

    private final ExecutorService executor;

    public LinkCrawler(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void crawl(String url, ProgressBar progressBar) {
        progressBar.setVisible(true);
        executor.submit(() -> crawlLinks(url));
        decrementActiveTasks(progressBar);
    }

    private void crawlLinks(String url) {
        List<String> crawledUrls = new ArrayList<>();
        String domain = extractDomain(url);
        crawlLinksRecursive(url, domain, crawledUrls);
    }

    private void crawlLinksRecursive(String url, String domain, List<String> crawledUrls) {
        if (!crawledUrls.contains(url)) {
            crawledUrls.add(url);
            try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
                webClient.getOptions().setCssEnabled(false);
                webClient.getOptions().setJavaScriptEnabled(false);

                final HtmlPage page = webClient.getPage(url);
                final List<HtmlAnchor> anchors = page.getAnchors();

                for (HtmlAnchor anchor : anchors) {
                    try {
                        String href = anchor.getHrefAttribute();
                        String absoluteUrl = getAbsoluteUrl(url, href);

                        if (!crawledUrls.contains(absoluteUrl)) {
                            int status = getLinkStatus(absoluteUrl);
                            String pageName = page.getTitleText();
                            if (isSameDomain(absoluteUrl, domain)) {
                                insertCrawledData(pageName, url, absoluteUrl, "Internal Link", status);
                                crawlLinksRecursive(absoluteUrl, domain, crawledUrls);
                            } else {
                                insertCrawledData(pageName, url, absoluteUrl, "External Link", status);
                            }
                        }
                    } catch (FailingHttpStatusCodeException e) {
                        System.err.println("Failed to fetch URL: " + e.getStatusMessage() + ", Status Code: " + e.getStatusCode());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}