package nts.uk.shr.com.time.japanese;

import nts.arc.time.GeneralDate;

public class JapaneseDate {
	
	private final GeneralDate date;
	
	private final JapaneseEraName era; 
	
	public JapaneseDate(GeneralDate date, JapaneseEraName era) {
		this.date = date;
		this.era = era;
	}
	
	/** sample result 平成 10年2月3日 */
	public String toString () {
		StringBuilder result = new StringBuilder(era.getName()); 
		result.append((this.date.year() - this.era.startDate().year()) + "年");		
		result.append(this.date.month() + "月");
		result.append(this.date.day() + "日");
		return result.toString();
	}
	
	/** sample result 100203 */
	public int toFullDateInt () {
		return (this.date.year() - this.era.startDate().year())*10000 + this.date.month()*100 + this.date.day();
	}
	
	/** sample result 100203 */
	public int toYearMonthInt () {
		return (this.date.year() - this.era.startDate().year())*100 + this.date.month();
	}
	
	/** get Japanese era 元号 */
	public String era() {
		return this.era.getName();
	}

	/** get Japanese era symbol 元号 */
	public String eraSymbol() {
		return this.era.getSymbol();
	}
	
	/** get Japanese year */
	public int year() {
		return this.date.year() - this.era.startDate().year();
	}
	
	/** get Japanese month */
	public int month() {
		return this.date.month();
	}

	/** get Japanese day */
	public int day() {
		return this.date.day();
	}
}
