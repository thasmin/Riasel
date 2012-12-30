package com.axelby.riasel;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FeedParser {

	public interface FeedInfoHandler {
		public void OnFeedInfo(Feed feed);
	}
	public interface FeedItemHandler {
		public void OnFeedItem(FeedItem item);
	}

	public class UnknownFeedException extends Exception {
		private static final long serialVersionUID = -4953090101978301549L;
		public UnknownFeedException() {
			super("This is not a RSS or Atom feed and is unsupported by Riasel.");
		}
		public UnknownFeedException(Throwable throwable) {
			super("This is not a RSS or Atom feed and is unsupported by Riasel.", throwable);
		}
	}

	private FeedInfoHandler _feedInfoHandler;
	private FeedItemHandler _feedItemHandler;

	public FeedParser() {
	}

	public void setOnFeedInfoHandler(FeedInfoHandler handler) {
		_feedInfoHandler = handler;
	}
	public void setOnFeedItemHandler(FeedItemHandler handler) {
		_feedItemHandler = handler;
	}

	public void parseFeed(XmlPullParser parser) throws XmlPullParserException, IOException, UnknownFeedException {
		// make sure this is an RSS document
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.START_TAG)
			eventType = parser.next();
		if (parser.getName().equals("rss")) {
			RSSParser.process(parser, _feedInfoHandler, _feedItemHandler);
		} else if (parser.getName().equals("feed")) {
			AtomParser.process(parser, _feedInfoHandler, _feedItemHandler);
		} else {
			throw new UnknownFeedException();
		}
	}

}
