package com.axelby.riasel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

	static Date parseDate(String date) {
		final SimpleDateFormat dateFormats[] = new SimpleDateFormat[] {
				new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"),
				new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
				new SimpleDateFormat("EEE, d MMM yy HH:mm:ss z"),
				new SimpleDateFormat("EEE, d MMM yy HH:mm z"),
				new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z"),
				new SimpleDateFormat("EEE, d MMM yyyy HH:mm z"),
				new SimpleDateFormat("d MMM yy HH:mm z"),
				new SimpleDateFormat("d MMM yy HH:mm:ss z"),
				new SimpleDateFormat("d MMM yyyy HH:mm z"),
				new SimpleDateFormat("d MMM yyyy HH:mm:ss z"),
		};
	
		for (SimpleDateFormat format : dateFormats) {
			format.setTimeZone(TimeZone.getTimeZone("UTC"));
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
