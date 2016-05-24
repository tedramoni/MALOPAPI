package com.tedramoni.malopapi.Parser;

/**
 * Created by Ted on 22/03/2016.
 */

import com.tedramoni.malopapi.Model.Anime;
import com.tedramoni.malopapi.Model.Opening;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

public class AnimeParser extends AbstractParser {

    @Override
    public Anime crawl(Integer id) throws IOException {
        if (id == null) {
            throw new IllegalArgumentException("Both type and id argument cannot be null" );
        }

        if (id <= 0) {
            throw new IllegalArgumentException("Both type and id argument cannot be empty" );
        }

        final String url = createEntryURL(id);
        final Document doc = this.getResultFromJSoup(url);
        if (doc == null) {
            throw new IOException(String.format("No data fetched for url %s", url));
        }

        return scrap(doc, url);
    }

    public Anime scrap(Document doc, String url) {

        String pattern;
        Integer id = this.getIdFromLink(url);

        if (doc == null || doc.text().isEmpty()) {
            throw new IllegalArgumentException("Document cannot be null or empty" );
        }

        Anime anime = new Anime(id);
        anime.setId(id);
        log.debug("[myanimelist-api] Id has been set" );


        try {
            anime.setTitre(getMainTitle(doc));
            log.debug("[myanimelist-api] Title has been set" );
        } catch (Exception e) {
            log.debug("[myanimelist-api] Cannot parse main title", e);
        }

        pattern = "Edit Opening Theme";

        Elements h2s = doc.select("h2");
        for (Element h2 : h2s) {
            if (h2.text().equals(pattern)) {
                Elements openings = doc.select("div.theme-songs.js-theme-songs.opnening");
                if (openings != null && openings.hasText()) {
                    log.debug("Openings have been found" );
                    anime = setOpenings(anime, openings);
                }
                Elements moreOpenings = doc.select("div[id=opTheme]");
                if (moreOpenings != null && moreOpenings.hasText()) {
                    log.debug("More Openings have been found" );
                    anime = setMoreOpenings(anime, moreOpenings);
                }

            }
        }
        return anime;
    }

    private String getMainTitle(Document doc) {
        Element firstH1 = doc.select("h1" ).first();
        return firstH1.childNodes().get(0).childNodes().get(0).toString();
    }

    private Anime setOpenings(Anime anime, Elements text) {

        // OLD MAL TEMPLATE FOR OPENING *DEPRECATED*
        /*Integer index = 1;
        Node current = h2.nextSibling();
        log.debug("NEXT SIBLING"+current.nextSibling().toString());
        do {
            String line = current.toString();
            log.debug(line);
            if (line.contains("<br>" ) || index > 10 || line.equals(" " ) || line.isEmpty()) {
                current = current.nextSibling();
            } else {
                if (line.contains("No opening themes have been added to this title. Help improve our database by adding an opening theme" )) {
                    break;
                }
                log.debug("title added :" + line.toString());
                Opening opening = new Opening(index);
                opening.setOpening(line);
                anime.addOpening(index, opening);
                ++index;
                current = current.nextSibling();
            }
        } while (!current.toString().contains("h2" ));*/
        Integer index = 1;
        String divContent = text.toString();
        String[] divContentCutted = divContent.split("#" );
        if(divContentCutted.length==1){
            divContentCutted = divContent.split("<span class=\"theme-song\">" );
            for (String line : divContentCutted) {
                if (line.contains("No opening themes have been added to this title. Help improve our database by adding an opening theme" )) {
                    break;
                }
                if(line.contains("by") && index<=10){
                    log.debug("title added :" + line.toString());
                    Opening opening = new Opening(index);
                    opening.setOpening(line);
                    anime.addOpening(index, opening);
                    ++index;
                }
            }
        }
        else{
            for (String line : divContentCutted) {
                if (line.contains("No opening themes have been added to this title. Help improve our database by adding an opening theme" )) {
                    break;
                }
                if(line.contains(":") && index<=10){
                    log.debug("title added :" + line.toString());
                    Opening opening = new Opening(index);
                    opening.setOpening(line);
                    anime.addOpening(index, opening);
                    ++index;
                }
            }
        }

        return anime;
    }

    private Anime setMoreOpenings(Anime anime, Elements text) {
        Integer index = 11;
        String divContent = text.text();
        String[] divContentCutted = divContent.split(" #" );
        for (String line : divContentCutted) {
            log.debug("title added :" + line.toString());
            Opening opening = new Opening(index);
            opening.setOpening(line);
            anime.addOpening(index, opening);
            ++index;
        }
        return anime;
    }

    public void call() {
        throw new NotImplementedException();
    }
}
