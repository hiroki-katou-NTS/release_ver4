package nts.uk.shr.infra.i18n.format;

import java.util.Map;
import java.util.function.Function;

/**
 * Date time parser for Japanese locale.
 *
 */
public class DateTimeTranscoderJP extends DateTimeTranscoder {
	
	/* (non-Javadoc)
	 * @see nts.uk.shr.infra.i18n.format.DateTimeTranscoder#create()
	 */
	public Map<String, Function<String, String>> create() {
		return this.createFormatterMap(new DateTimeTranscoderJP());
	}
}
