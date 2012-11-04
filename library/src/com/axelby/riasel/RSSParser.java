package com.axelby.riasel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class RSSParser {
	private RSSParser() {
	}

	static Feed process(XmlPullParser parser) throws XmlPullParserException, IOException {
		Feed feed = new Feed();
		boolean in_image = false;

		// look for subscription details, stop at item tag
		for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
			// check for an ending image tag
			if (in_image && eventType == XmlPullParser.END_TAG && parser.getName().equals("image")) {
				in_image = false;
				continue;
			}
			if (eventType != XmlPullParser.START_TAG)
				continue;

			String name = parser.getName();
			// these are elements about the thumbnail
			if (in_image) {
				if (name.equals("url"))
					feed.setThumbnail(parser.nextText());
				continue;
			}

			// if we're starting an item, move past the subscription details
			// section
			if (name.equals("item")) {
				break;
			} else if (name.equals("image")) {
				in_image = true;
				continue;
			} else if (name.equalsIgnoreCase("lastBuildDate")) {
				Date date = parseRFC822(parser.nextText());
				if (date != null)
					feed.setLastBuildDate(date);
			} else if (name.equalsIgnoreCase("title") && parser.getNamespace().equals("")) {
				feed.setTitle(parser.nextText());
			}
		}

		feed.setItems(parseRSSItems(parser).toArray(new FeedItem[0]));

		return feed;
	}

	private static Vector<FeedItem> parseRSSItems(XmlPullParser parser) throws XmlPullParserException, IOException {
		Vector<FeedItem> items = new Vector<FeedItem>();
		FeedItem item = null;

		// grab podcasts from item tags
		for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
			if (eventType == XmlPullParser.START_TAG) {
				String name = parser.getName();
				String namespace = parser.getNamespace();
				if (name.equalsIgnoreCase("item")) {
					item = new FeedItem();
				} else if (name.equalsIgnoreCase("title") && parser.getNamespace().equals("")) {
					item.setTitle(parser.nextText());
				} else if (name.equalsIgnoreCase("link")) {
					String rel = parser.getAttributeValue(null, "rel");
					if (rel != null && rel.equalsIgnoreCase("payment")) {
						item.setPaymentURL(parser.getAttributeValue(null, "href"));
					} else {
						item.setLink(parser.nextText());
					}
				} else if (namespace.equals("") && name.equalsIgnoreCase("description")) {
					item.setDescription(parser.nextText());
				} else if (name.equalsIgnoreCase("pubDate")) {
					item.setPublicationDate(parseRFC822(parser.nextText()));
				} else if (name.equalsIgnoreCase("enclosure")) {
					item.setMediaURL(parser.getAttributeValue(null, "url"));
					try {
						item.setMediaSize(Long.valueOf(parser.getAttributeValue(null, "length")));
					} catch (Exception e) {
						item.setMediaSize(0L);
					}
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				String name = parser.getName();
				if (name.equalsIgnoreCase("item")) {
					items.add(item);
					item = null;
				}
			}
		}

		return items;
	}

	private static Date parseRFC822(String date) {
		final SimpleDateFormat rfc822DateFormats[] = new SimpleDateFormat[] {
				new SimpleDateFormat("EEE, d MMM yy HH:mm:ss z"), new SimpleDateFormat("EEE, d MMM yy HH:mm z"),
				new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z"), new SimpleDateFormat("EEE, d MMM yyyy HH:mm z"),
				new SimpleDateFormat("d MMM yy HH:mm z"), new SimpleDateFormat("d MMM yy HH:mm:ss z"),
				new SimpleDateFormat("d MMM yyyy HH:mm z"), new SimpleDateFormat("d MMM yyyy HH:mm:ss z"), };

		for (SimpleDateFormat format : rfc822DateFormats) {
			try {
				return format.parse(date);
			} catch (ParseException e) {
			}

			// try it again in english
			try {
				SimpleDateFormat enUSFormat = new SimpleDateFormat(format.toPattern(), Locale.US);
				return enUSFormat.parse(date);
			} catch (ParseException e) {
			}
		}

		return null;
	}
}
