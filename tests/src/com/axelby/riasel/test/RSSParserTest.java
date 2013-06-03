package com.axelby.riasel.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.axelby.riasel.Feed;
import com.axelby.riasel.FeedParser;

import android.test.ActivityTestCase;
import android.util.Xml;

public class RSSParserTest extends ActivityTestCase {

	public void testBBCFeed() throws UnsupportedEncodingException, XmlPullParserException {
		String xml = getInstrumentation().getContext().getResources().getString(R.string.rss_bbc);
		XmlPullParser parser = Xml.newPullParser();
		InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
		parser.setInput(is, "utf-8");

		FeedParser feedParser = new FeedParser();
		feedParser.setOnFeedInfoHandler(new FeedParser.FeedInfoHandler() {
			@Override
			public void OnFeedInfo(FeedParser parser, Feed feed) {
				assertEquals("Title", "6 Minute English", feed.getTitle());

				Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
				cal.set(2013, 05, 02, 12, 00, 03);
				assertEquals("Updated time difference", 0, cal.getTimeInMillis() / 1000 * 1000 - feed.getLastBuildDate().getTime());
			}
		});
	}
}
