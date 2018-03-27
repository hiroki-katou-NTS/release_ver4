/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums;

/**
 * @author danpv
 *
 */
public enum ErrorPresent {

	/** 0 : エラーあり */
	HAS_ERROR(0, "エラーあり"),

	/** 1 : エラーなし */
	NO_ERROR(1, "エラーなし");

	public final int value;
	
	public final String nameId;

	private ErrorPresent(int value, String nameId) {
		this.value = value;
		this.nameId = nameId;
	}

}
