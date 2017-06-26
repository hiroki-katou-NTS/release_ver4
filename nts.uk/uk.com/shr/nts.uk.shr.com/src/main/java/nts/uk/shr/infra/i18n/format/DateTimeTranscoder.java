package nts.uk.shr.infra.i18n.format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import javax.enterprise.inject.spi.CDI;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.japanese.JapaneseDate;
import nts.uk.shr.com.time.japanese.JapaneseErasProvider;

/**
 * Base class parses date time parameters in message.
 *
 */
public abstract class DateTimeTranscoder {
	
	/**
	 * Default locale
	 */
	public static final Locale DEFAULT_LOCALE = Locale.JAPANESE;
	
	/**
	 * Splash pattern
	 */
	public static final Pattern SLASH_PTN = Pattern.compile("\\/");
	
	/**
	 * Hyphen
	 */
	public static final String HYPHEN = "-";
	
	/**
	 * Short ymd pattern.
	 * @return pattern
	 */
	protected Pattern shortYmdPattern() {
		return Pattern.compile("\\d{4}(\\/|-)\\d{1,2}(\\/|-)\\d{1,2}");
	}
	
	/**
	 * Short ymdw pattern.
	 * @return pattern
	 */
	protected Pattern shortYmdwPattern() {
		return Pattern.compile("\\d{4}(\\/|-)\\d{1,2}(\\/|-)\\d{1,2}\\(\\w+\\)");
	}
	
	/**
	 * Short ym pattern.
	 * @return pattern
	 */
	protected Pattern shortYmPattern() {
		return Pattern.compile("\\d{4}(\\/|-)\\d{1,2}");
	}
	
	/**
	 * Short md pattern.
	 * @return pattern
	 */
	protected Pattern shortMdPattern() {
		return Pattern.compile("\\d{1,2}(\\/|-)\\d{1,2}");
	}
	
	/**
	 * Long ymd pattern.
	 * @return pattern
	 */
	protected Pattern longYmdPattern() {
		return Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}日");
	}
	
	/**
	 * Long ymdw pattern.
	 * @return pattern
	 */
	protected Pattern longYmdwPattern() {
		return Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}日\\(\\w+\\)");
	}
	
	/**
	 * Long f pattern.
	 * @return pattern
	 */
	protected Pattern longFPattern() {
		return Pattern.compile("\\d{4}年度");
	}
	
	/**
	 * Long jmd pattern.
	 * @return pattern
	 */
	protected Pattern longJmdPattern() {
		return Pattern.compile("\\w{2}\\d{1,3}年\\d{1,2}月\\d{1,2}日");
	}
	
	/**
	 * Long jm pattern.
	 * @return pattern
	 */
	protected Pattern longJmPattern() {
		return Pattern.compile("\\w{2}\\d{1,3}年\\d{1,2}月");
	}
	
	/**
	 * Time short hm pattern.
	 * @return pattern
	 */
	protected Pattern timeShortHmPattern() {
		return Pattern.compile("\\d+:\\d{2}");
	}
	
	/**
	 * Time short hms pattern.
	 * @return pattern
	 */
	protected Pattern timeShortHmsPattern() {
		return Pattern.compile("\\d+:\\d{2}:\\d{2}");
	}
	
	/**
	 * Full date time short pattern.
	 * @return pattern
	 */
	protected Pattern fullDateTimeShortPattern() {
		return Pattern.compile("\\d{4}(\\/|-)\\d{1,2}(\\/|-)\\d{1,2} \\d+:\\d{2}:\\d{2}");
	}
	
	/**
	 * Format short ymd.
	 * @param date 
	 * @return format date
	 */
	protected String shortYmd(String date) {
		if (!shortYmdPattern().matcher(date).matches()) 
			throw new IllegalDateTimeFormatException();
		return dateOf(date);
	}
	
	/**
	 * Format short ymdw.
	 * @param date
	 * @return format date
	 */
	protected String shortYmdw(String date) {
		if (shortYmdwPattern().matcher(date).matches()) return date;
		return formatYmdOfPattern(dateOf(date), DateTimeFormatDef.SHORT_YMDW_PATTERN)
				.orElseThrow(() -> new IllegalDateTimeFormatException());
	}
	
	/**
	 * Format short ym.
	 * @param date
	 * @return format date
	 */
	protected String shortYm(String date) {
		if (shortYmPattern().matcher(date).matches()) return date;
		return formatYmdOfPattern(dateOf(date), DateTimeFormatDef.SHORT_YM_PATTERN)
				.orElseThrow(() -> new IllegalDateTimeFormatException());
	}
	
	/**
	 * Format short md.
	 * @param date 
	 * @return format date
	 */
	protected String shortMd(String date) {
		if (shortMdPattern().matcher(date).matches()) return date;
		return formatYmdOfPattern(dateOf(date), DateTimeFormatDef.SHORT_MD_PATTERN)
				.orElseThrow(() -> new IllegalDateTimeFormatException());
	}
	
	/**
	 * Format long ymd.
	 * @param date
	 * @return format date
	 */
	protected String longYmd(String date) {
		if (longYmdPattern().matcher(date).matches()) return date;
		return formatYmdOfPattern(dateOf(date), DateTimeFormatDef.LONG_YMD_PATTERN)
				.orElseThrow(() -> new IllegalDateTimeFormatException());
	}
	
	/**
	 * Format long ymdw.
	 * @param date
	 * @return format date
	 */
	protected String longYmdw(String date) {
		if (longYmdwPattern().matcher(date).matches()) return date;
		return formatYmdOfPattern(dateOf(date), DateTimeFormatDef.LONG_YMDW_PATTERN)
				.orElseThrow(() -> new IllegalDateTimeFormatException());
	}
	
	/**
	 * Format long f.
	 * @param date
	 * @return format date
	 */
	protected String longF(String date) {
		if (longFPattern().matcher(date).matches()) return date;
		return formatFiscalYmdOfPattern(dateOf(date), DateTimeFormatDef.LONG_F_PATTERN, DEFAULT_LOCALE)
				.orElseThrow(() -> new IllegalDateTimeFormatException());
	}
	
	/**
	 * Format long jmd.
	 * @param date
	 * @return format date
	 */
	protected String longJmd(String date) {
		if (longJmdPattern().matcher(date).matches()) return date;
		return formatJpDateOfPattern(dateOf(date), DateTimeFormatDef.LONG_JMD_PATTERN)
				.orElseThrow(() -> new IllegalDateTimeFormatException());
	}
	
	/**
	 * Format long jmd.
	 * @param date
	 * @return format date
	 */
	protected String longJmdSys(String date) {
		if (longJmdPattern().matcher(date).matches()) return date;
		return formatSysJpDateOfPattern(dateOf(date));
	}
	
	/**
	 * Format long jm.
	 * @param date
	 * @return format date
	 */
	protected String longJm(String date) {
		if (longJmPattern().matcher(date).matches()) return date;
		return formatJpDateOfPattern(dateOf(date), DateTimeFormatDef.LONG_JM_PATTERN)
				.orElseThrow(() -> new IllegalDateTimeFormatException());
	}
	
	/**
	 * Format long jm.
	 * @param date
	 * @return format date
	 */
	protected String longJmSys(String date) {
		if (longJmPattern().matcher(date).matches()) return date;
		String jpDate = formatSysJpDateOfPattern(dateOf(date));
		int endIdx = jpDate.indexOf("月");
		if (endIdx == -1) throw new IllegalDateTimeFormatException();
		return jpDate.substring(0, endIdx + 1);
	}
	
	/**
	 * Format time short hm.
	 * @param time
	 * @return format time
	 */
	protected String timeShortHm(String time) {
		String t = timeOf(time);
		if (timeShortHmPattern().matcher(t).matches()) return t;
		if (timeShortHmsPattern().matcher(t).matches()) {
			return t.substring(0, t.lastIndexOf(":"));
		}
		throw new IllegalDateTimeFormatException();
	}
	
	/**
	 * Format time short hms.
	 * @param time
	 * @return format time
	 */
	protected String timeShortHms(String time) {
		String t = timeOf(time);
		if (timeShortHmsPattern().matcher(t).matches()) return t;
		throw new IllegalDateTimeFormatException();
	}
	
	/**
	 * Format clock short hm.
	 * @param time
	 * @return format time
	 */
	protected String clockShortHm(String time) {
		return timeShortHm(time);
	}
	
	/**
	 * Format full date time short.
	 * @param dateTime
	 * @return format date time
	 */
	protected String fullDateTimeShort(String dateTime) {
		if (fullDateTimeShortPattern().matcher(dateTime).matches()) 
			return Pattern.compile(HYPHEN).matcher(dateTime).replaceAll(SLASH_PTN.toString());
		throw new IllegalDateTimeFormatException();
	}
	
	/**
	 * Create date formatter map.
	 * @param transcoder
	 * @return date formatter map
	 */
	protected Map<String, Function<String, String>> createFormatterMap(DateTimeTranscoder transcoder) {
		@SuppressWarnings("serial")
		Map<String, Function<String, String>> dateFormatters = new HashMap<String, Function<String, String>>() {
			{
				put(DateTimeFormatDef.SHORT_YMD, (date) -> transcoder.shortYmd(date));
				put(DateTimeFormatDef.SHORT_YMDW, (date) -> transcoder.shortYmdw(date));
				put(DateTimeFormatDef.SHORT_YM, (date) -> transcoder.shortYm(date));
				put(DateTimeFormatDef.SHORT_MD, (date) -> transcoder.shortMd(date));
				put(DateTimeFormatDef.LONG_YMD, (date) -> transcoder.longYmd(date));
				put(DateTimeFormatDef.LONG_YMDW, (date) -> transcoder.longYmdw(date));
				put(DateTimeFormatDef.LONG_F, (date) -> transcoder.longF(date));
				put(DateTimeFormatDef.LONG_JMD, (date) -> transcoder.longJmdSys(date)); 
				put(DateTimeFormatDef.LONG_JM, (date) -> transcoder.longJmSys(date));
				put(DateTimeFormatDef.TIME_SHORT_HM, (time) -> transcoder.timeShortHm(time));
				put(DateTimeFormatDef.TIME_SHORT_HMS, (time) -> transcoder.timeShortHms(time));
				put(DateTimeFormatDef.CLOCK_SHORT_HM, (time) -> transcoder.clockShortHm(time));
				put(DateTimeFormatDef.DATETIME_SHORT_YMDHMS, (dateTime) -> transcoder.fullDateTimeShort(dateTime));
			}
		};
		return dateFormatters;
	}
	
	/**
	 * Create date formatter map.
	 * @return date formatter map
	 */
	public abstract Map<String, Function<String, String>> create();
	
	/**
	 * Format ymd with pattern.
	 * @param date
	 * @param pattern
	 * @return optional date string
	 */
	private Optional<String> formatYmdOfPattern(String date, String pattern) {
		return this.formatYmdOfPattern(date, pattern, DEFAULT_LOCALE);
	}
	
	/**
	 * Format ymd with pattern.
	 * @param date
	 * @param pattern
	 * @param locale
	 * @return optional date string
	 */
	private Optional<String> formatYmdOfPattern(String date, String pattern, Locale locale) {
		if (shortYmdPattern().matcher(date).matches()) {
			String convertedDate = SLASH_PTN.matcher(date).replaceAll(HYPHEN);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
			return Optional.of(formatter.format(LocalDate.parse(convertedDate, 
												DateTimeFormatter.ofPattern(DateTimeFormatDef.YMD_PATTERN))));
		}
		return Optional.empty();
	}
	
	/**
	 * Format fiscal ymd with pattern.
	 * @param date
	 * @param pattern
	 * @param locale
	 * @return optional date string
	 */
	private Optional<String> formatFiscalYmdOfPattern(String date, String pattern, Locale locale) {
		if (shortYmdPattern().matcher(date).matches()) {
			String convertedDate = SLASH_PTN.matcher(date).replaceAll(HYPHEN);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
			LocalDate d = LocalDate.parse(convertedDate, DateTimeFormatter.ofPattern(DateTimeFormatDef.YMD_PATTERN));
			if (d.compareTo(LocalDate.of(d.getYear(), 4, 1)) < 0) {
				d = d.minusYears(1);
			}
			return Optional.of(formatter.format(d));
		}
		return Optional.empty();
	}
	
	/**
	 * Format Japanese date with pattern.
	 * @param date
	 * @param pattern
	 * @return optional date string
	 */
	private Optional<String> formatJpDateOfPattern(String date, String pattern) {
		if (shortYmdPattern().matcher(date).matches()) {
			String convertedDate = SLASH_PTN.matcher(date).replaceAll(HYPHEN);
			java.time.chrono.JapaneseDate jpDate = java.time.chrono.JapaneseDate.from(LocalDate.parse(convertedDate,
														DateTimeFormatter.ofPattern(DateTimeFormatDef.YMD_PATTERN)));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, DEFAULT_LOCALE);
			return Optional.of(formatter.format(jpDate));
		}
		return Optional.empty();
	}
	
	/**
	 * Format Japanese date.
	 * @param date
	 * @return Japanese date
	 */
	private String formatSysJpDateOfPattern(String date) {
		String convertedDate = SLASH_PTN.matcher(date).replaceAll(HYPHEN);
		JapaneseDate jDate = CDI.current().select(JapaneseErasProvider.class).get()
								.toJapaneseDate(GeneralDate.localDate(LocalDate.parse(convertedDate, 
												DateTimeFormatter.ofPattern(DateTimeFormatDef.YMD_PATTERN))));
		return jDate.toString();
	}
	
	/**
	 * Get date from date time.
	 * @param dateTime
	 * @return date
	 */
	private String dateOf(String dateTime) {
		if (fullDateTimeShortPattern().matcher(dateTime).matches()) {
			return dateTime.substring(0, dateTime.indexOf(" "));
		}
		return dateTime;
	}
	
	/**
	 * Get time from date time.
	 * @param dateTime
	 * @return time
	 */
	private String timeOf(String dateTime) {
		if (fullDateTimeShortPattern().matcher(dateTime).matches()) {
			return dateTime.substring(dateTime.indexOf(" ") + 1);
		}
		return dateTime;
	}
}
