package org.staff.utils;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.swt.widgets.DateTime;

public final class Convertor {

	private static final Date PURE_GREGORIAN_CHANGE = new Date(Long.MIN_VALUE);
	private static final Date PURE_JULIAN_CHANGE = new Date(Long.MAX_VALUE);
	private static final Date DEFAULT_GREGORIAN_CHANGE = new GregorianCalendar().getGregorianChange(); // year 1582
	
	public static String getUTF8Representation(String usernameCP251) {
		String utf8username = null;
		
		try {
			utf8username= new String(usernameCP251.getBytes("UTF-8"), "windows-1251");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return utf8username;
	}
	
	public static String getBase64AuthString(String authString) {
		return new String(Base64.encodeBase64(authString.getBytes()));
	}
	
	public static XMLGregorianCalendar gregorianCalenderToXMLGregorianCalender(GregorianCalendar gDate) {
		
		GregorianCalendar sDate = (GregorianCalendar) gDate.clone();
		XMLGregorianCalendar xmlGDate = null;
		try {
			xmlGDate = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(sDate.get(1), sDate.get(2) + 1, sDate.get(5),
					DatatypeConstants.FIELD_UNDEFINED, // hours
					DatatypeConstants.FIELD_UNDEFINED, // minutes
					DatatypeConstants.FIELD_UNDEFINED, // seconds
					DatatypeConstants.FIELD_UNDEFINED, // millis
					DatatypeConstants.FIELD_UNDEFINED);
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // timezone - offset in minutes
		
		
		
//		try {
//			xmlGDate = DatatypeFactory.newInstance()
//					.newXMLGregorianCalendar(sDate);
//		} catch (DatatypeConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return xmlGDate;
	}
	
	public static GregorianCalendar xmlGregorianCalenderToGregorianCalender(XMLGregorianCalendar xmlGDate) {
		
		XMLGregorianCalendar sDate = (XMLGregorianCalendar) xmlGDate.clone();
		
		//GregorianCalendar gDate = sDate.toGregorianCalendar();
		
		GregorianCalendar gDate = new GregorianCalendar();
		gDate.clear();
		gDate.setGregorianChange(PURE_GREGORIAN_CHANGE);

		gDate.set(Calendar.YEAR, sDate.getYear());
		gDate.set(Calendar.MONTH, sDate.getMonth());
		gDate.set(Calendar.DAY_OF_MONTH, sDate.getDay());
		
		return gDate;
	}

	public static GregorianCalendar getGregorianDateFromSWTDateTime(DateTime dDate) {
		return new GregorianCalendar(dDate.getYear(), dDate.getMonth(), dDate.getDay());
	}

	/**
	 * @param gregorianCalendar
	 * @return
	 */
	public static String gregorianCalendarToString(GregorianCalendar gregorianCalendar) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
   		
    	simpleDateFormat.setCalendar(gregorianCalendar);
        String dateFormatted = simpleDateFormat.format(gregorianCalendar.getTime());
		return dateFormatted;
	}

	/**
	 * @param par
	 * @return
	 */
	public static GregorianCalendar stringToGregorianCalendar(String par) {
		String[] splitDate = par.split("[.]"); //assuming "MM.dd.yyyy" stored value
    	
    	int days = Integer.parseInt(splitDate[0]);
    	int month = Integer.parseInt(splitDate[1]) - 1;
    	int year = Integer.parseInt(splitDate[2]);

    	GregorianCalendar dateConverted = new GregorianCalendar(year, month, days);
		return dateConverted;
	}
	
	
//	TimeZone cet = TimeZone.getTimeZone("CET");
//	TimeZone utc = TimeZone.getTimeZone("UTC");
//	GregorianCalendar gc = new GregorianCalendar();
//	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
//
//	@Test
//	public void testNow() throws DatatypeConfigurationException {
//	  df.setTimeZone(gc.getTimeZone());
//	  log.info(" - Gregorian LOCAL [" + df.format(gc.getTime()) + "]");
//	  df.setTimeZone(cet);
//	  log.info(" - Gregorian CET [" + df.format(gc.getTime()) + "]");
//	  df.setTimeZone(utc);
//	  String gcs = df.format(gc.getTime());
//	  log.info(" - Gregorian UTC [" + df.format(gc.getTime()) + "]");
//	  XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
//	  df.setTimeZone(xc.getTimeZone(0));
//	  log.info(" - XML RAW [" + df.format(xc.toGregorianCalendar().getTime()) + "]");
//	  df.setTimeZone(cet);
//	  log.info(" - XML CET [" + df.format(xc.toGregorianCalendar().getTime()) + "]");
//	  df.setTimeZone(utc);
//	  String xcs = df.format(xc.toGregorianCalendar().getTime());
//	  log.info(" - XML UTC [" + df.format(xc.toGregorianCalendar().getTime()) + "]");
//	  assertEquals(gcs, xcs);
//	}
	
}
