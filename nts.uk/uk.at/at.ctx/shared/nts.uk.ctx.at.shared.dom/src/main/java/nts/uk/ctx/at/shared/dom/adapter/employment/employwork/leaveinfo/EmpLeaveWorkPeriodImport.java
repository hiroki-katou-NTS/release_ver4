package nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo;

import lombok.Getter;
import nts.arc.time.calendar.period.DatePeriod;

/**
 *社員の休業期間
 * @author HieuLt
 * 
 */

@Getter
public class EmpLeaveWorkPeriodImport {
	
	/** 社員ID**/
	private final	String empID;
	
	/** 休職休業枠NO */
	//http://192.168.50.4:3000/issues/110682
	private final int tempAbsenceFrNo; 
	
	/** 期間 **/ 
	private  final DatePeriod datePeriod;
	

	//[C-0] 社員の休業期間Imported( 社員ID, 期間, 休職休業枠NO )																	
	public EmpLeaveWorkPeriodImport(String empID, int tempAbsenceFrNo, DatePeriod datePeriod) {
		super();
		this.empID = empID;
		this.tempAbsenceFrNo = tempAbsenceFrNo;
		this.datePeriod = datePeriod;
	}
	
}
