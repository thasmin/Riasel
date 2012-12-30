package com.axelby.riasel.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.test.ActivityTestCase;
import android.util.Xml;

import com.axelby.riasel.Feed;
import com.axelby.riasel.FeedItem;
import com.axelby.riasel.FeedParser;

public class AtomParserTest extends ActivityTestCase {

	public void testSimpleFeed() throws XmlPullParserException, IOException, FeedParser.UnknownFeedException {
		String xml = getInstrumentation().getContext().getResources().getString(R.string.atom_simple);
		XmlPullParser parser = Xml.newPullParser();
		InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
		parser.setInput(is, "utf-8");

		FeedParser feedParser = new FeedParser();
		feedParser.setOnFeedInfoHandler(new FeedParser.FeedInfoHandler() {
			@Override
			public void OnFeedInfo(Feed feed) {
				assertEquals("Title", "Example Feed", feed.getTitle());

				Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
				cal.set(2003, 11, 13, 18, 30, 02);
				assertEquals("Updated time difference", 0, cal.getTimeInMillis() / 1000 * 1000 - feed.getLastBuildDate().getTime());
			}
		});

		final Integer itemCount[] = new Integer[] { 0 };
		feedParser.setOnFeedItemHandler(new FeedParser.FeedItemHandler() {
			@Override
			public void OnFeedItem(FeedItem item) {
				assertEquals("There should be only one item.", 0, itemCount[0].intValue());
				itemCount[0]++;

				assertEquals("Entry title", "Atom-Powered Robots Run Amok", item.getTitle());
				assertEquals("Entry link", "http://example.org/2003/12/13/atom03", item.getLink());

				Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
				cal.set(2003, 11, 13, 18, 30, 02);
				assertEquals("Entry publish date difference", 0, cal.getTimeInMillis() / 1000 * 1000 - item.getPublicationDate().getTime());

				assertEquals("Entry description", "Some text.", item.getDescription());
			}
		});

		feedParser.parseFeed(parser);
	}

	public void testShortFeed() throws XmlPullParserException, IOException, FeedParser.UnknownFeedException {
		String xml = getInstrumentation().getContext().getResources().getString(R.string.atom_short);
		XmlPullParser parser = Xml.newPullParser();
		InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
		parser.setInput(is, "utf-8");

		FeedParser feedParser = new FeedParser();
		feedParser.setOnFeedInfoHandler(new FeedParser.FeedInfoHandler() {
			@Override
			public void OnFeedInfo(Feed feed) {
				assertEquals("Title", "dive into mark", feed.getTitle());
				assertEquals("Icon", "http://example.com/icon.png", feed.getThumbnail());
				//assertEquals("Link", "http://example.org/feed.atom", feed.getLink());

				Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
				cal.set(2005, 6, 31, 12, 29, 29);
				assertEquals("Updated time difference", 0, cal.getTimeInMillis() / 1000 * 1000 - feed.getLastBuildDate().getTime());
			}
		});

		final Integer itemCount[] = new Integer[] { 0 };
		feedParser.setOnFeedItemHandler(new FeedParser.FeedItemHandler() {
			@Override
			public void OnFeedItem(FeedItem item) {
				assertEquals("There should be only one item.", 0, itemCount[0].intValue());
				itemCount[0]++;

				assertEquals("Entry title", "Atom draft-07 snapshot", item.getTitle());
				assertEquals("Entry link", "http://example.org/2005/04/02/atom", item.getLink());
				assertEquals("Entry media url", "http://example.org/audio/ph34r_my_podcast.mp3", item.getMediaURL());
				assertEquals("Entry media length", 1337L, item.getMediaSize().longValue());

				Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
				cal.set(2003, 11, 13, 12, 29, 29);
				assertEquals("Entry publish date difference", 0, cal.getTimeInMillis() / 1000 * 1000 - item.getPublicationDate().getTime());

				String content = " <p>Such high volume would no doubt provide a dream platform for advertisers & ";
				assertEquals("Entry description", content, item.getDescription());
			}
		});
		feedParser.parseFeed(parser);
	}
}
