package com.jb.test;

import static org.junit.Assert.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import com.jb.beans.RSSNews;
import com.jb.system.RSSSystem;
import com.jb.util.RSSFetcher;

public class RSSSystemTest {
	private String strUrl = "https://www.ynet.co.il/3rdparty/mobile/rss/ynetnews/3082/";
	private ArrayList<RSSNews> testArrayList;
	private RSSSystem rssSystem;

	@Before
	public void setup() {
		testArrayList = null;
		RSSFetcher rssFetcher = new RSSFetcher(strUrl);
		rssSystem = new RSSSystem();
		try {
			testArrayList = rssFetcher.fetchFromXmlToRSSNews();
		} catch (IOException e) {
			System.err.println("Error creating HTTP connection");
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println("Error on parse String to Calander");
			e.printStackTrace();
		}
	}

	@Test
	public void testValidityURL() throws Exception {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.connect();

			assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
		} catch (IOException e) {
			System.err.println("Error creating HTTP connection");
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void conversionTest() {

		assertFalse("ArrayList is not null", testArrayList == null);
		assertFalse("ArrayList is not empty", testArrayList.isEmpty());
		assertTrue("ArrayList size is 50", testArrayList.size() == 50);

		assertTrue("Title is not null", testArrayList.get(0).getTitle() != null);
		assertTrue("Author is not null", testArrayList.get(0).getAuthor() != null);
		assertTrue("Link is not null", testArrayList.get(0).getLink() != null);
		assertTrue("Description is not null", testArrayList.get(0).getDescription() != null);
		assertTrue("PubDate is not null", testArrayList.get(0).getPubDate() != null);
	}

	@Test
	public void authorConversionTest() {
		String StrBeforeFilterByAuthor = rssSystem.displayRssFetcher();
		String StrAfterFilterByAuthor = rssSystem.displayRssFetcher("Ynet");
		String AUTHOR_REGEX = "(Author:)(.*)";
		Pattern AUTHOR_PATTERN = Pattern.compile(AUTHOR_REGEX);
		String[] linesBeforeFilter = StrBeforeFilterByAuthor.split("\\n");
		String[] linesAfterFilter = StrAfterFilterByAuthor.split("\\n");

		StringBuilder allAuthors = new StringBuilder();

		for (String line : linesBeforeFilter) {
			Matcher m = AUTHOR_PATTERN.matcher(line);
			while (m.find()) {
				if (m.group(2).contains("Ynet"))
					allAuthors.append(m.group(2));
			}
		}
		StringBuilder onlyYnetAuthors = new StringBuilder();
		for (String line : linesAfterFilter) {
			Matcher m = AUTHOR_PATTERN.matcher(line);
			while (m.find()) {
				onlyYnetAuthors.append(m.group(2));
			}
		}

		assertEquals("if allAuthors String after filter it for \"Ynet\" is equal to String onlyYnetAuthors - Test Pass",
				allAuthors.toString(), onlyYnetAuthors.toString());
	}

	@Test
	public void menuTest() {
		rssSystem.startApp();
		assertEquals("thread should be \"RUNNABLE\" state", Thread.State.RUNNABLE, RSSSystem.timerThread.getState());

		RSSSystem.rssTask.startRssTask();
		assertTrue("thread state should be alive", RSSSystem.timerThread.isAlive());
		RSSSystem.rssTask.startRssTask();
		assertTrue("thread state still should be alive", RSSSystem.timerThread.isAlive());

//		RSSSystem.rssTask.stopRssTask();
//		assertEquals("thread state still should be not alive", !RSSSystem.timerThread.isAlive(), false);

//		RSSSystem.rssTask.stopRssTask();
//		assertEquals("thread state still should be not alive", !RSSSystem.timerThread.isAlive(), true);

		System.out.println(" ");
	}

}
