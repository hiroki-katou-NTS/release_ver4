package nts.uk.shr.com.time.japanese;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.gul.util.Range;

/**
 * Name of Japanese Era (元号)
 */
public class JapaneseEraName {
	
	/** name (ex. "平成") */
	@Getter
	private String name;
	
	/** symbol (ex. "H") */ 
	@Getter
	private String symbol;
	
	/** span */
	private Range<GeneralDate> span;
	
	public JapaneseEraName(String name, String symbol, Range<GeneralDate> span) {
		this.name = name;
		this.symbol = symbol;
		this.span = span;
	}
	
	public JapaneseEraName(String name, String symbol, GeneralDate startDate, GeneralDate endDate) {
		this(name, symbol, Range.between(startDate, endDate));
	}
	
	public GeneralDate startDate() {
		return this.span.min();
	}
	
	public GeneralDate endDate() {
		return this.span.max();
	}
	
	public boolean contains(GeneralDate date) {
		return this.span.contains(date);
	}
}
