package nts.uk.ctx.bs.employee.dom.holidaysetting.common;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;

@Getter
@Setter
/**
 * The Class PublicHolidayMonthSetting.
 */
//月間公休日数設定
public class PublicHolidayMonthSetting extends DomainObject{

	/** The public hd management year. */
	// 公休管理年
	private Year publicHdManagementYear;
	
	/** The month. */
	// 月度
	private Integer month;
	
	/** The in legal holiday. */
	// 法定内休日日数
	private MonthlyNumberOfDays inLegalHoliday;
	
	/** The out legal holiday. */
	// 法定外休日日数
	private MonthlyNumberOfDays outLegalHoliday;
	
	/**
	 * Instantiates a new public holiday month setting.
	 *
	 * @param publicHdManagementYear the public hd management year
	 * @param month the month
	 * @param inLegalHoliday the in legal holiday
	 * @param outLegalHoliday the out legal holiday
	 */
	public PublicHolidayMonthSetting(Year publicHdManagementYear, Integer month, 
						MonthlyNumberOfDays inLegalHoliday, MonthlyNumberOfDays outLegalHoliday){
		this.publicHdManagementYear = publicHdManagementYear;
		this.month = month;
		this.inLegalHoliday = inLegalHoliday;
		this.outLegalHoliday = outLegalHoliday;
	}
	
}
