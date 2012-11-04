package com.axelby.riasel;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AtomParser {

	public static Feed process(XmlPullParser parser) throws XmlPullParserException, IOException {
		final String NS_ATOM = "http://www.w3.org/2005/Atom";

		Feed feed = new Feed();

		for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
			if (eventType == XmlPullParser.START_TAG) {
				String name = parser.getName();
				String namespace = parser.getNamespace();
				if (name.equals("title") && namespace.equals(NS_ATOM))
					feed.setTitle(parser.nextText());
				if (name.equals("entry") && namespace.equals(NS_ATOM))
					break;
			}
		}

		return feed;
	}

}
