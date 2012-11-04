package com.axelby.riasel;

import java.io.IOException;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AtomParser {

	final static String NS_ATOM = "http://www.w3.org/2005/Atom";

	public static Feed process(XmlPullParser parser) throws XmlPullParserException, IOException {

		Feed feed = new Feed();

		for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
			if (eventType == XmlPullParser.START_TAG) {
				if (isAtomElement(parser, "title"))
					feed.setTitle(parser.nextText());
				else if (isAtomElement(parser, "updated"))
					feed.setLastBuildDate(Utils.parseDate(parser.nextText()));
				else if (isAtomElement(parser, "entry"))
					break;
			}
		}

		feed.setItems(parseEntries(parser).toArray(new FeedItem[0]));

		return feed;
	}

	private static Vector<FeedItem> parseEntries(XmlPullParser parser) throws XmlPullParserException, IOException {
		Vector<FeedItem> items = new Vector<FeedItem>();
		FeedItem item = new FeedItem();

		// grab podcasts from item tags
		for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
			if (eventType == XmlPullParser.START_TAG) {
				if (isAtomElement(parser, "entry")) {
					item = new FeedItem();
				} else if (isAtomElement(parser, "title")) {
					item.setTitle(parser.nextText());
				} else if (isAtomElement(parser, "link")) {
					String rel = parser.getAttributeValue(null, "rel");
					if (rel == null)
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
				else if (isAtomElement(parser, "published"))
					item.setPublicationDate(Utils.parseDate(parser.nextText()));
				else if (isAtomElement(parser, "updated") && item.getPublicationDate() == null)
					item.setPublicationDate(Utils.parseDate(parser.nextText()));
			} else if (eventType == XmlPullParser.END_TAG) {
				if (isAtomElement(parser, "entry")) {
					items.add(item);
					item = null;
				}
			}
		}

		return items;
	}

	private static boolean isAtomElement(XmlPullParser parser, String name) {
		return parser.getName().equals(name) && parser.getNamespace().equals(NS_ATOM);
	}
}
