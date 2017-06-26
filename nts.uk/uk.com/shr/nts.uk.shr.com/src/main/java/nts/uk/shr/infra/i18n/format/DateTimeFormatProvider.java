package nts.uk.shr.infra.i18n.format;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class provides date time formatter based on locale.
 *
 */
public class DateTimeFormatProvider {
	
	@SuppressWarnings("serial")
	public static final Map<Locale, DateTimeTranscoder> LocaleTranscoderMap = new HashMap<Locale, DateTimeTranscoder>() {
		{
			put(Locale.JAPAN, new DateTimeTranscoderJP());
			put(Locale.US, new DateTimeTranscoderUS());
		}
	};
}
