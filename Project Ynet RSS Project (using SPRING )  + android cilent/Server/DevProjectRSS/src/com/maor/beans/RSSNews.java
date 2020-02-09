package com.maor.beans;

import java.util.Calendar;

public class RSSNews {

	private long id;
	private String title;
	private String description;
	private String author;
	private String link;
	private Calendar pubDate;

	public RSSNews() {
		this(0, "", "", "", "", Calendar.getInstance());
	}

	public RSSNews(long id, String title, String description, String author, String link, Calendar pubDate) {
		setId(id);
		setTitle(title);
		setAuthor(author);
		setLink(link);
		setPubDate(pubDate);
		setDescription(description);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Calendar getPubDate() {
		return pubDate;
	}

	public void setPubDate(Calendar pubDate) {
		this.pubDate = pubDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "\nID: " + getId() + "\nTitle: " + getTitle() + "\nDescription: " + getDescription()
				+ "\nAuthor: " + getAuthor() + "\nLink: " + getLink() + "\nPublish Date: " + getPubDate().getTime();
	}

}
