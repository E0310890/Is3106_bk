package com.is3106.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeHelper {
	
	public static Date getCurrentDate() {
		return new Date();
	}
	
	public static Timestamp getCurrentTimestamp() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
		Timestamp timestamp = Timestamp.valueOf(now.toLocalDateTime());
		return timestamp;
	}
	
	public static long getCurrentEpoch() {
		return Instant.now().toEpochMilli();
	}
	
	//return number of milliseconds since the epoch of 1970-01-01T00:00:00Z
	public static long TimestampToEpoch(Timestamp timestamp) {
		Instant instant = timestamp.toInstant();
		return instant.toEpochMilli();
	}
	
	// Convert instant to timestamp
	public static Timestamp EpochToTimestamp(long epoch) {
		Instant instant = Instant.ofEpochSecond(TimeUnit.MILLISECONDS.toSeconds(epoch));
		return Timestamp.from(instant);
	}
	
	// Date Converter
	public static String dateFormatConverter(String fromDateFormat, String toDateFormat, String FromDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fromDateFormat);
			Date date = sdf.parse(FromDate);
			sdf = new SimpleDateFormat(toDateFormat);
			return sdf.format(date);
		}catch(ParseException e) {
			e.printStackTrace();
			return null;
		}
	};

}
