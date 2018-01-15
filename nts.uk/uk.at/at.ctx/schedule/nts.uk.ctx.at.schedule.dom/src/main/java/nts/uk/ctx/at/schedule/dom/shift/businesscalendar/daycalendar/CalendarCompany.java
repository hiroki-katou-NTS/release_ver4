/**
 * 9:16:26 AM Jul 12, 2017
 */
package nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;

/**
 * domain : 会社営業日カレンダー日次
 * @author tutk
 *
 */
@Getter
public class CalendarCompany {
	
	private String companyId;
	
	private GeneralDate date;
	
	private UseSet workingDayAtr;

	public CalendarCompany(String companyId, GeneralDate date, UseSet workingDayAtr) {
		super();
		this.companyId = companyId;
		this.date = date;
		this.workingDayAtr = workingDayAtr;
	}
	
	
	public static CalendarCompany createFromJavaType(String companyId, GeneralDate date, int workingDayAtr){
		return new CalendarCompany(companyId, date, EnumAdaptor.valueOf(workingDayAtr, UseSet.class));
	}
	
	
}
