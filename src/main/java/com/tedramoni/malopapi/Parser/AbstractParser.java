package com.tedramoni.malopapi.Parser;

/**
 * Created by Ted on 22/03/2016.
 */

import com.tedramoni.malopapi.Model.Anime;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Random;

import static org.apache.commons.lang3.StringUtils.countMatches;


public abstract class AbstractParser {

    public static final String DOMAIN = "http://myanimelist.net/anime/";
    public static final String USER_AGENT = "iMAL-iOS";
    static Logger log = Logger.getLogger(AbstractParser.class.getName());

    public abstract Anime crawl(Integer id) throws IOException;

    protected Integer getIdFromLink(String link) {
        try {
            if (link.startsWith("http") || link.startsWith("https"))
                return Integer.parseInt(link.split("/")[4].split("/")[0]);
            else
                return Integer.parseInt(link.split("/")[2].split("/")[0]);
        } catch (Exception e) {
            log.debug(String.format("Error parsing id from link : %s", link));
        }

        return null;
    }

    protected String getInfoFromLink(String link) {
        try {
            if (link.startsWith("http") || link.startsWith("https"))
                return link.split("/")[3].split("/")[2];
            else
                return link.split("/")[1].split("/")[2];
        } catch (Exception e) {
            log.debug(String.format("Error parsing type from link : %s", link));
        }

        return null;
    }

    protected String createEntryURL(Integer id) {

        if (id == null) {
            throw new IllegalArgumentException("Id or Type cannot be null");
        }

        return DOMAIN + id.toString() + "/";
    }

    public Document getResultFromJSoup(String url) throws IOException {
        log.debug("Trying to get result from " + url);

        try {
            //delay between 0 and 5s
            long delay = (0 + new Random().nextInt(5)) * 1000;
            Integer seconds = (int) (delay / 1000) % 60;
            log.debug(String.format("Delay : %ss", seconds.toString()));
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final Connection.Response response = Jsoup.connect(url).userAgent(USER_AGENT).execute();

        Document doc = null;

        if (response.url().toString().startsWith(DOMAIN) && countMatches(response.url().toString(), "/") == 5) {
            doc = response.parse();
        }

        log.debug(String.format("Response url %s", response.url()));

        return doc;
    }
}