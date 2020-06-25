package nts.uk.ctx.at.shared.dom.dailyattdcal.dailycalprocess.calculation.other;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * 法定労働控除時間
 * @author keisuke_hoshina
 *
 */
@Getter
public class StatutoryDeductionForFlex {
	//実働用
	private AttendanceTime forActualWork;
	//割増用
	private AttendanceTime forPremium;
	
	/**
	 * Constructor 
	 */
	public StatutoryDeductionForFlex(AttendanceTime forActualWork, AttendanceTime forPremium) {
		super();
		this.forActualWork = forActualWork;
		this.forPremium = forPremium;
	}
}
