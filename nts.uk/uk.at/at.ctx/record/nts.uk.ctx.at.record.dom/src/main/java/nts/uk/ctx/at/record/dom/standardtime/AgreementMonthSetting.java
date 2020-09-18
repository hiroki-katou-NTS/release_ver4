package nts.uk.ctx.at.record.dom.standardtime;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmOneMonth;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorOneMonth;
import nts.uk.ctx.at.shared.dom.monthlyattdcal.agreementresult.hourspermonth.ErrorTimeInMonth;

/**
 * 
 * @author nampt
 *
 */
@Getter
public class AgreementMonthSetting extends AggregateRoot {

	private String employeeId;

	private YearMonth yearMonthValue;

	//TODO #30161
	private ErrorOneMonth errorOneMonth;

	private AlarmOneMonth alarmOneMonth;

	/** １ヶ月時間 */
	private ErrorTimeInMonth oneMonthTime;

	public AgreementMonthSetting(String employeeId, YearMonth yearMonthValue, ErrorOneMonth errorOneMonth,
			AlarmOneMonth alarmOneMonth) {
		super();
		this.employeeId = employeeId;
		this.yearMonthValue = yearMonthValue;
		this.errorOneMonth = errorOneMonth;
		this.alarmOneMonth = alarmOneMonth;
	}

	public static AgreementMonthSetting createFromJavaType(String employeeId, BigDecimal yearMonthValue,
			int errorOneMonth, int alarmOneMonth) {
		return new AgreementMonthSetting(employeeId, new YearMonth(yearMonthValue.intValue()),
				new ErrorOneMonth(errorOneMonth), new AlarmOneMonth(alarmOneMonth));
	}
	
	public void validate(){
		if(alarmOneMonth.v().compareTo(errorOneMonth.v()) > 0){
			throw new BusinessException("Msg_59", "KMK008_43", "KMK008_42");
		}
	}

}
