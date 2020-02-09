package com.jb.tasks;

import java.util.Calendar;
import com.jb.system.RSSSystem;
import com.jb.ui.RSSMenu;

public class RSSTask implements Runnable {
	private boolean quit = false;
	private final RSSMenu rssMenu;

	public RSSTask(RSSMenu rssMenu) {
		this.rssMenu = rssMenu;
	}

	@Override
	public void run() {
		while (!quit) {
			if (isTimeToWork()) {
				runRssTask();
			}
		}
	}

	private boolean isTimeToWork() {
		Calendar now = Calendar.getInstance();
		int minutes = now.get(Calendar.MINUTE);// (Calendar.SECOND);
		return minutes % 30 == 0;
	}

	public void runRssTask() {
		this.getRssMenu().displayRssFetcherOnGui();
	}

	public void startRssTask() {
		quit = false;

		if (RSSSystem.timerThread != null && !(RSSSystem.timerThread.isAlive())) {
			RSSSystem.runTimerThread();
		}
	}

	public void stopRssTask() {
		quit = true;

		if (RSSSystem.timerThread != null && RSSSystem.timerThread.isAlive()) {
			RSSSystem.timerThread.interrupt();
		}
	}

	public RSSMenu getRssMenu() {
		return rssMenu;
	}

}
