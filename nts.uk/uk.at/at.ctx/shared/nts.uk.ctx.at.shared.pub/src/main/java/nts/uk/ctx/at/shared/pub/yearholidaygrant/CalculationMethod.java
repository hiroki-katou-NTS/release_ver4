package nts.uk.ctx.at.shared.pub.yearholidaygrant;

/**
 * 付与日数の計算対象
 *	0:労働日数
 *	1:出勤率
 * @author TanLV
 *
 */
public enum CalculationMethod {
	/** 出勤率 */
	ATTENDENCE_RATE(0),
	/** 労働日数 */
	WORKING_DAY(1);
	
	public final int value;
	
	CalculationMethod(int value) {
		this.value = value;
	}
}
