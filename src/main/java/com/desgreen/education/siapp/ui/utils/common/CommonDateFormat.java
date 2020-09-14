package com.desgreen.education.siapp.ui.utils.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CommonDateFormat {
	
	private final long serialVersionUID = 1L;

	public CommonDateFormat() {
	}
	
	public Date fromLocalDate_(LocalDate localDate) {
		
		//Asia/Kuala_Lumpur +8
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay(defaultZoneId).toInstant();
		Date date = Date.from(instant);
        
		return date;
	}

	public static Date fromLocalDate(LocalDate localDate) {
		
		//Asia/Kuala_Lumpur +8
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay(defaultZoneId).toInstant();
		Date date = Date.from(instant);
        
		return date;
	}
	
	public Date fromLocalDateTime_(LocalDateTime localDateTime) {
		
	     //Asia/Kuala_Lumpur +8
	     ZoneId defaultZoneId = ZoneId.systemDefault();
	     Instant instant = localDateTime.atZone(defaultZoneId).toInstant();
	     Date date = Date.from(instant);
    
		return date;
	 }
	public static Date fromLocalDateTime(LocalDateTime localDateTime) {
		
	     //Asia/Kuala_Lumpur +8
	     ZoneId defaultZoneId = ZoneId.systemDefault();
	     Instant instant = localDateTime.atZone(defaultZoneId).toInstant();
	     Date date = Date.from(instant);
     
		return date;
	 }
	
	public LocalDate fromDate_(Date date) {
		//Asia/Kuala_Lumpur +8
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //1. Convert Date -> Instant
        Instant instant = date.toInstant();
        //2. Instant + system default time zone + toLocalDate() = LocalDate
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
		
        return localDate;
	}
	public static LocalDate fromDate(Date date) {
		//Asia/Kuala_Lumpur +8
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //1. Convert Date -> Instant
        Instant instant = date.toInstant();
        //2. Instant + system default time zone + toLocalDate() = LocalDate
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
		
        return localDate;
	}
	
	public LocalDateTime fromDateTime_(Date date) {
		//Asia/Kuala_Lumpur +8
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //1. Convert Date -> Instant
        Instant instant = date.toInstant();
        //3. Instant + system default time zone + toLocalDateTime() = LocalDateTime
        LocalDateTime localDateTime = instant.atZone(defaultZoneId).toLocalDateTime();
		
        return localDateTime;
	}
	public static LocalDateTime fromDateTime(Date date) {
		//Asia/Kuala_Lumpur +8
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //1. Convert Date -> Instant
        Instant instant = date.toInstant();
        //3. Instant + system default time zone + toLocalDateTime() = LocalDateTime
        LocalDateTime localDateTime = instant.atZone(defaultZoneId).toLocalDateTime();
		
        return localDateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (serialVersionUID ^ (serialVersionUID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CommonDateFormat)) {
			return false;
		}
		CommonDateFormat other = (CommonDateFormat) obj;
		if (serialVersionUID != other.serialVersionUID) {
			return false;
		}
		return true;
	}
	
	
}
