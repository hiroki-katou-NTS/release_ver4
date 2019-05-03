package nts.uk.ctx.at.function.dom.adapter.agreement;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.MessageDisp;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.Number;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.OverTime;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Builder
@Setter
@Getter
public class CheckedOvertimeImport {
	private String employeeId;
	private DatePeriod datePeriod;
	private String alarmCheckId;
	private boolean error;	
	/** no */
	private int no;
	/** 36超過時間 */
	private OverTime ot36;
	/** 36超過回数 */
	private Number excessNum;
	/** 表示するメッセージ */
	private MessageDisp messageDisp;
	
	private int countAgreementOneEmp ; 
}
