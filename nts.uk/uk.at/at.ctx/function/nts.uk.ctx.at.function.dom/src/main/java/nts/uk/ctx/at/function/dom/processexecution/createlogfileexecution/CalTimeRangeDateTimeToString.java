package nts.uk.ctx.at.function.dom.processexecution.createlogfileexecution;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ejb.Stateless;

import nts.arc.time.GeneralDateTime;
/**
 * 
 * @author tutk
 *
 */
public class CalTimeRangeDateTimeToString {
	
	public static String calTimeExec(GeneralDateTime startDateTime, GeneralDateTime endDateTime) {
		try {
			long time = Timestamp.valueOf(endDateTime.localDateTime()).getTime()
					- Timestamp.valueOf(startDateTime.localDateTime()).getTime();
			Date date = new Date(time);
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			String dateFormatted = formatter.format(date);
			return dateFormatted;
		} catch (Exception e) {
			return "";
		}
	}

}
