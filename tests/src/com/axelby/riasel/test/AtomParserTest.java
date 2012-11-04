package com.axelby.riasel.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.test.AndroidTestCase;
import android.util.Xml;

import com.axelby.riasel.Feed;
import com.axelby.riasel.FeedParser;

public class AtomParserTest extends AndroidTestCase {
	
	public void testSimpleFeed() throws XmlPullParserException, IOException {
		String xml = this.getContext().getResources().getString(R.string.atom_simple);
		XmlPullParser parser = Xml.newPullParser();
		InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
		parser.setInput(is, "utf-8");
		Feed feed = FeedParser.parseFeed(parser);

		assertEquals("Example Feed", feed.getTitle());
	}
}
