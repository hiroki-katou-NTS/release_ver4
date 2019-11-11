package nts.uk.ctx.at.schedule.pubimp.shift.businesscalendar.daycalendar;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarCompany;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarCompanyRepository;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarWorkPlaceRepository;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarWorkplace;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.UseSet;
import nts.uk.ctx.at.schedule.pub.shift.businesscalendar.daycalendar.ObtainDeadlineDatePub;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class ObtainDeadlineDatePubImpl implements ObtainDeadlineDatePub {
	
	@Inject
	private CalendarCompanyRepository repoCalendarCom;
	
	@Inject
	private CalendarWorkPlaceRepository repoCalendarWkp;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public GeneralDate obtainDeadlineDate(GeneralDate targetDate, Integer specDayNo, String workplaceID,
			String companyID) {
		if(specDayNo == 0) return targetDate;
		GeneralDate deadlineDate = targetDate;
		//営業日カレンダー日次　＝　ドメインモデル「職場営業日カレンダー日次」を取得する
		List<CalendarWorkplace> lstWkp = repoCalendarWkp.getLstByDateWorkAtr(workplaceID, targetDate, UseSet.workingDay.value);
		List<GeneralDate> lstDate = lstWkp.stream().map(c -> c.getDate()).collect(Collectors.toList());
		if (lstDate.isEmpty()){
			//営業日カレンダー日次　＝　ドメインモデル「会社営業日カレンダー日次」を取得する
			List<CalendarCompany> lstCom = repoCalendarCom.getLstByDateWorkAtr(companyID, targetDate, UseSet.workingDay.value);
			if(lstCom.isEmpty()) return targetDate;
			lstDate = lstCom.stream().map(c -> c.getDate()).collect(Collectors.toList());
		}
		for (GeneralDate date : lstDate) {
			//締切日　＝　ループ中の営業日カレンダー日次．年月日
			deadlineDate = date;
			//Input．指定日数　-＝　1
			specDayNo -= 1;
			if(specDayNo == 0){
				return date;
			}
		}
		return deadlineDate;
	}

}
