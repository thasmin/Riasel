package com.axelby.riasel;

import java.util.Date;

import android.content.ContentValues;

public class Feed {

	private String _title;
	private String _thumbnail;
	private Date _lastBuildDate;
	private FeedItem[] _items;

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		if (getTitle() != null)
			values.put("title", getTitle());
		if (getThumbnail() != null)
			values.put("thumbnail", getThumbnail());
		if (getLastBuildDate() != null)
			values.put("lastBuildDate", getLastBuildDate().getTime());
		return values;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String _title) {
		this._title = _title;
	}

	public String getThumbnail() {
		return _thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		_thumbnail = thumbnail;
	}

	public Date getLastBuildDate() {
		return _lastBuildDate;
	}

	public void setLastBuildDate(Date mLastBuildDate) {
		this._lastBuildDate = mLastBuildDate;
	}

	public FeedItem[] getItems() {
		return _items;
	}

	public void setItems(FeedItem[] _items) {
		this._items = _items;
	}

}
