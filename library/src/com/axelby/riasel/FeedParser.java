package com.axelby.riasel;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FeedParser {

	private FeedParser() {
	}

	public static Feed parseFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		// make sure this is an RSS document
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.START_TAG)
			eventType = parser.next();
		if (parser.getName().equals("rss")) {
			return RSSParser.process(parser);
		} else if (parser.getName().equals("feed")) {
			return AtomParser.process(parser);
		} else {
			return null;
		}
	}

}
