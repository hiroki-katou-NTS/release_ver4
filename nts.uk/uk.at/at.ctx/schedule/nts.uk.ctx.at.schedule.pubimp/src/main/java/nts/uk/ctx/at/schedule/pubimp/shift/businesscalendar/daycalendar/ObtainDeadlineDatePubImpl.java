package nts.uk.ctx.at.schedule.pubimp.shift.businesscalendar.daycalendar;

import java.util.Optional;

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
	private CalendarCompanyRepository calendarCompanyRepository;
	
	@Inject
	private CalendarWorkPlaceRepository calendarWorkPlaceRepository;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public GeneralDate obtainDeadlineDate(GeneralDate targetDate, Integer specDayNo, String workplaceID,
			String companyID) {
		int workingDayNo = 0;
		// 年月日でループ
		GeneralDate loopDate = targetDate;
		do {
			// 次の日へ
			loopDate = loopDate.addDays(1);
			// ドメインモデル「職場営業日カレンダー日次」を取得する
			Optional<CalendarWorkplace> opCalendarWorkplace = calendarWorkPlaceRepository
					.findCalendarWorkplaceByDate(workplaceID, loopDate);
			// 取得したドメインモデル「職場営業日カレンダー日次」の件数をチェック
			if(opCalendarWorkplace.isPresent()){
				// ドメインモデル「職場営業日カレンダー日次」.稼働日区分をチェック
				if(!opCalendarWorkplace.get().getWorkingDayAtr().equals(UseSet.workingDay)){
					continue;
				}
			} else {
				// ドメインモデル「会社営業日カレンダー日次」を取得する
				Optional<CalendarCompany> opCalendarCompany = calendarCompanyRepository
						.findCalendarCompanyByDate(companyID, loopDate);
				// 取得したドメインモデル「会社営業日カレンダー日次」の件数をチェック
				if(!opCalendarCompany.isPresent()){
					continue;
				}
				// ドメインモデル「会社営業日カレンダー日次」.稼働日区分をチェック
				if(!opCalendarCompany.get().getWorkingDayAtr().equals(UseSet.workingDay)){
					continue;
				}
			}
			// 稼働日数　=　稼働日数　＋１
			workingDayNo += 1;
		// 指定日数に達したかチェック
		} while (workingDayNo < specDayNo);
		return loopDate;
	}

}
