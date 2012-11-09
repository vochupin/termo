package com.example.crashreport;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndPerson;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

public class RssParser {
	public SyndFeed parseFeed(String url) throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
		return new SyndFeedInput().build(new XmlReader(new URL(url)));
	}

	public String toString(SyndFeed feed){
		
		StringBuffer buf = new StringBuffer();

		buf.append("About feed:\n");

		buf.append("Author: " + feed.getAuthor() + "\n");

		buf.append("Authors:\n");

		if (feed.getAuthors() != null){

			for (Object author : feed.getAuthors())	{

				buf.append(((SyndPerson) author).getName() + "\n");
				buf.append(((SyndPerson) author).getEmail() + "\n");
				buf.append(((SyndPerson) author).getUri() + "\n");
				buf.append("\n");
			}
		}

		buf.append("Title: " + feed.getTitle() + "\n");
		buf.append("Title Ex: " + feed.getTitleEx() + "\n");
		buf.append("Description: " + feed.getDescription() + "\n");
		buf.append("Description Ex: " + feed.getDescriptionEx() + "\n");
		buf.append("Date" + feed.getPublishedDate() + "\n");
		buf.append("Type: " + feed.getFeedType() + "\n");
		buf.append("Encoding: " + feed.getEncoding() + "\n");
		buf.append("(C) " + feed.getCopyright() + "\n");
		buf.append("\n");
		for (Object object : feed.getEntries()){
			SyndEntry entry = (SyndEntry) object;
			buf.append(entry.getTitle() + " - " + entry.getAuthor() + "\n");
			buf.append(entry.getLink() + "\n");
			for (Object contobj : entry.getContents()){
				SyndContent content = (SyndContent) contobj;
				buf.append(content.getType() + "\n");
				buf.append(content.getValue() + "\n");
			}

			SyndContent content = entry.getDescription();
			if (content != null) buf.append(content.getValue());
			buf.append(entry.getPublishedDate() + "\n");
			buf.append("\n");
		}
		
		return buf.toString();
	}
}
