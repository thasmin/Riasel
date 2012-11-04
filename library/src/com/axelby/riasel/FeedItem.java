package com.axelby.riasel;

import java.util.Date;

import android.content.ContentValues;

public class FeedItem {

	private String _title;
	private String _paymentURL;
	private String _link;
	private String _description;
	private Date _publicationDate;
	private String _mediaURL;
	private Long _mediaSize;

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		if (getTitle() != null)
			values.put("title", getTitle());
		if (getPaymentURL() != null)
			values.put("paymentURL", getPaymentURL());
		if (getLink() != null)
			values.put("link", getLink());
		if (getDescription() != null)
			values.put("description", getDescription());
		if (getPublicationDate() != null)
			values.put("publicationDate", getPublicationDate().getTime());
		if (getMediaURL() != null)
			values.put("mediaURL", getMediaURL());
		if (getMediaSize() != null)
			values.put("mediaSize", getMediaSize());
		return values;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String _title) {
		this._title = _title;
	}

	public String getPaymentURL() {
		return _paymentURL;
	}

	public void setPaymentURL(String _paymentURL) {
		this._paymentURL = _paymentURL;
	}

	public String getLink() {
		return _link;
	}

	public void setLink(String _link) {
		this._link = _link;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String _description) {
		this._description = _description;
	}

	public Date getPublicationDate() {
		return _publicationDate;
	}

	public void setPublicationDate(Date _publicationDate) {
		this._publicationDate = _publicationDate;
	}

	public String getMediaURL() {
		return _mediaURL;
	}

	public void setMediaURL(String _mediaURL) {
		this._mediaURL = _mediaURL;
	}

	public Long getMediaSize() {
		return _mediaSize;
	}

	public void setMediaSize(Long _mediaSize) {
		this._mediaSize = _mediaSize;
	}

}
