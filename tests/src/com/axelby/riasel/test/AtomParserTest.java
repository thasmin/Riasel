package com.axelby.riasel.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
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
	
	public void testSimpleFeed() throws XmlPullParserException, IOException {
		String xml = getInstrumentation().getContext().getResources().getString(R.string.atom_simple);
		XmlPullParser parser = Xml.newPullParser();
		InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
		parser.setInput(is, "utf-8");
		Feed feed = FeedParser.parseFeed(parser);

		assertEquals("Title", "Example Feed", feed.getTitle());

		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		cal.set(2003, 11, 13, 18, 30, 02);
		assertEquals("Updated time difference", 0, cal.getTimeInMillis() / 1000 * 1000 - feed.getLastBuildDate().getTime());

		assertEquals("Entry count", 1, feed.getItems().length);

		FeedItem item = feed.getItems()[0];
		assertEquals("Entry title", "Atom-Powered Robots Run Amok", item.getTitle());
		assertEquals("Entry link", "http://example.org/2003/12/13/atom03", item.getLink());
		cal.set(2003, 11, 13, 18, 30, 02);
		assertEquals("Entry publish date difference", 0, cal.getTimeInMillis() / 1000 * 1000 - item.getPublicationDate().getTime());
		assertEquals("Entry description", "Some text.", item.getDescription());
	}
}
