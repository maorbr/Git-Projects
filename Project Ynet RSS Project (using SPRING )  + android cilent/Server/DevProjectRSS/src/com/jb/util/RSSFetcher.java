package com.jb.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import com.jb.beans.RSSNews;

public class RSSFetcher {

	private final String ID = "id";
	private final String TITLE = "title";
	private final String DESCRIPTION = "description";
	private final String AUTHOR = "author";
	private final String LINK = "link";
	private final String PUB_DATE = "pubDate";
	private final String ITEM = "item";
	private final URL url;

	public RSSFetcher(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public ArrayList<RSSNews> fetchFromXmlToRSSNews() throws IOException, ParseException {
		ArrayList<RSSNews> rssNewsArrayList = new ArrayList<>();
		RSSNews rssFeed = null;
		try {
			boolean isFeedHeader = true;
			String id = "";
			String title = "";
			String description = "";
			String author = "";
			String link = "";
			String pubdate = "";

			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = createConnectionToUrl();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			while (eventReader.hasNext()) { // read the XML document.
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					String localPart = event.asStartElement().getName().getLocalPart();
					switch (localPart) {
					case ITEM:
						if (isFeedHeader) {
							isFeedHeader = false;
							rssFeed = new RSSNews();
						}
						event = eventReader.nextEvent();
						break;
					case ID:
						id = getCharacterData(event, eventReader);
						break;
					case TITLE:
						title = getCharacterData(event, eventReader);
						break;
					case DESCRIPTION:
						description = getCharacterData(event, eventReader);
						break;
					case AUTHOR:
						author = getCharacterData(event, eventReader);
						break;
					case LINK:
						link = getCharacterData(event, eventReader);
						break;
					case PUB_DATE:
						pubdate = getCharacterData(event, eventReader);
						break;
					}

				} else if (event.isEndElement()) {
					if (event.asEndElement().getName().getLocalPart() == ITEM) {

						initializeRSSNews(rssFeed, id, title, description, author, link, pubdate);
						rssNewsArrayList.add(rssFeed);
						event = eventReader.nextEvent();
						isFeedHeader = true;
					}
				}
			}
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
		return rssNewsArrayList;
	}

	private InputStream createConnectionToUrl() throws IOException {
		return url.openConnection().getInputStream();
	}

	private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
		String result = "";
		event = eventReader.nextEvent();
		if (event instanceof Characters) {
			result = event.asCharacters().getData();
		}
		return result;
	}

	private Calendar convertPubDateToCalander(String pubDate) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss X");
		Date date = simpleDateFormat.parse(pubDate);
		calendar.setTime(date);
		return calendar;
	}

	private void initializeRSSNews(RSSNews rssFeed, String id, String title, String description, String author,
			String link, String pubdate) throws ParseException {
		rssFeed.setId(Long.parseLong(id));
		rssFeed.setTitle(title);
		rssFeed.setDescription(description);
		rssFeed.setAuthor(author);
		rssFeed.setLink(link);
		rssFeed.setPubDate(convertPubDateToCalander(pubdate));
	}

}
