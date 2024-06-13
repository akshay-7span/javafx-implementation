package webcrawler.crawller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static webcrawler.utils.CrawlerUtils.*;


public class SEOCrawler implements Crawler {

    private final Analyzer analyzer;

    public SEOCrawler(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public void crawl(String url, ProgressBar progressBar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws IOException {
                progressBar.setVisible(true);
                boolean isIndexed = isURLIndexed(url);
                boolean crawlingBlocked = isCrawlingBlocked(url);

                try (final WebClient webClient = createWebClient()) {
                    HtmlPage page = webClient.getPage(url);
                    webClient.waitForBackgroundJavaScript(10000);
                    analyzer.analyze(page, url, isIndexed, crawlingBlocked);

                    List<String> links = extractLinks(page);

                    for (String link : links) {
                        if (!isTelephoneLink(link)) {
                            try {
                                HtmlPage attachedPage = webClient.getPage(link);
                                webClient.waitForBackgroundJavaScript(10000);
                                if (hasContentToAnalyze(attachedPage)) {
                                    analyzer.analyze(attachedPage, link, isIndexed, crawlingBlocked);
                                }
                            } catch (Exception e) {
                                System.err.println("Error processing URL: " + link + " - " + e.getMessage());
                            }
                        }
                    }
                    decrementActiveTasks(progressBar);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

}