package com.maor.system;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import com.maor.beans.RSSNews;
import com.maor.tasks.RSSTask;
import com.maor.ui.RSSMenu;
import com.maor.util.RSSFetcher;

public class RSSSystem {
	public static RSSTask rssTask;
	public static Thread timerThread;
	private final RSSMenu rssMenu;

	private static ArrayList<RSSNews> rssNewsArrayList;

	public RSSSystem() {
		rssMenu = new RSSMenu(this);
	}

	public void startApp() {
		rssTask = new RSSTask(rssMenu);
		runTimerThread();
		rssMenu.showMenu();
	}

	public static void runTimerThread() {
		timerThread = new Thread(rssTask);
		timerThread.setDaemon(true);
		timerThread.start();
	}

	public String displayRssFetcher(String... AuthorNameArgs) {
		StringBuilder sb = null;
		String result = null;
		int index = 1;
		try {
			runRssFetcher();
			sb = new StringBuilder();
			sb.append(
					"------------------------------------------------------------------------------------------------------------");

			for (RSSNews rssNews : rssNewsArrayList) {
				if (AuthorNameArgs.length <= 0) {
					sb.append("[News #" + index++ + "]");
					sb.append(rssNews.toString());
					sb.append(
							"\n\n------------------------------------------------------------------------------------------------------------");
				} else {
					if (rssNews.getAuthor().contains(AuthorNameArgs[0])) {
						sb.append("[News #" + index++ + "]");
						sb.append(rssNews.toString());
						sb.append(
								"\n\n------------------------------------------------------------------------------------------------------------");
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}

		if (!(rssNewsArrayList == null)) {
			result = sb.toString();
		} else {
			result = "Error: Check Internet connection..";
		}
		return result;
	}

	private void runRssFetcher() throws IOException, ParseException {
		String url = "https://www.ynet.co.il/3rdparty/mobile/rss/ynetnews/3082/";
		RSSFetcher rssFetcher = new RSSFetcher(url);

		rssNewsArrayList = rssFetcher.fetchFromXmlToRSSNews();
	}

}
