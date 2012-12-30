package com.axelby.riasel;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.axelby.riasel.FeedParser.FeedInfoHandler;
import com.axelby.riasel.FeedParser.FeedItemHandler;

public class AtomParser {

	final static String NS_ATOM = "http://www.w3.org/2005/Atom";

	public static void process(XmlPullParser parser, FeedInfoHandler feedInfoHandler, FeedItemHandler feedItemHandler) throws XmlPullParserException, IOException {

		Feed feed = new Feed();

		for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
			if (eventType == XmlPullParser.START_TAG) {
				if (isAtomElement(parser, "title"))
					feed.setTitle(parser.nextText());
				else if (isAtomElement(parser, "icon"))
					feed.setThumbnail(parser.nextText());
				else if (isAtomElement(parser, "updated"))
					feed.setLastBuildDate(Utils.parseDate(parser.nextText()));
				else if (isAtomElement(parser, "entry"))
					break;
			}
		}

		if (feedInfoHandler != null)
			feedInfoHandler.OnFeedInfo(feed);

		parseEntries(parser, feedItemHandler);
	}

	private static void parseEntries(XmlPullParser parser, FeedItemHandler feedItemHandler) throws XmlPullParserException, IOException {
		FeedItem item = null;

		// grab podcasts from item tags
		for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
			if (eventType == XmlPullParser.START_TAG) {
				if (isAtomElement(parser, "entry")) {
					item = new FeedItem();
				} else if (isAtomElement(parser, "title")) {
					item.setTitle(parser.nextText());
				} else if (isAtomElement(parser, "link")) {
					String rel = parser.getAttributeValue(null, "rel");
					if (rel == null || rel.equals("alternate"))
						item.setLink(parser.getAttributeValue(null, "href"));
					else if (rel.equals("payment"))
						item.setPaymentURL(parser.getAttributeValue(null, "href"));
					else if (rel.equals("enclosure")) {
						if (parser.getAttributeValue(null, "length") != null)
						item.setMediaSize(Long.valueOf(parser.getAttributeValue(null, "length")));
						item.setMediaURL(parser.getAttributeValue(null, "href"));
					}
				} else if (isAtomElement(parser, "summary") && item.getDescription() == null)
					item.setDescription(parser.nextText());
				else if (isAtomElement(parser, "content"))
					item.setDescription(parser.nextText());
				else if (isAtomElement(parser, "published"))
					item.setPublicationDate(Utils.parseDate(parser.nextText()));
				else if (isAtomElement(parser, "updated") && item.getPublicationDate() == null)
					item.setPublicationDate(Utils.parseDate(parser.nextText()));
			} else if (eventType == XmlPullParser.END_TAG) {
				if (isAtomElement(parser, "entry")) {
					if (feedItemHandler != null)
						feedItemHandler.OnFeedItem(item);
					item = null;
				}
			}
		}
	}

	private static boolean isAtomElement(XmlPullParser parser, String name) {
		return parser.getName().equals(name) && parser.getNamespace().equals(NS_ATOM);
	}
}
