package nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.overtime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.onemonth.AgreementOneMonthTime;
/**
 * 36協定上限月間時間
 * @author thanhnx
 *
 */
@AllArgsConstructor
@Getter
public class Time36AgreeUpperLimitMonthShare extends DomainObject {
	
	/*
	 * 時間外時間
	 */
	private AttendanceTimeMonth overTime;
	
	/*
	 * 上限時間
	 */
	private AgreementOneMonthTime upperLimitTime;
	
	public Time36AgreeUpperLimitMonthShare(){
		this.overTime = new AttendanceTimeMonth(0);
		this.upperLimitTime = new AgreementOneMonthTime(0);
	}
	
	public void updateOverTime(Integer overTime){
		this.overTime = new AttendanceTimeMonth(overTime);
	}
	
	public void updateUpperLimitTime(Integer upperLimitTime){
		this.upperLimitTime = new AgreementOneMonthTime(upperLimitTime);
	}
}
