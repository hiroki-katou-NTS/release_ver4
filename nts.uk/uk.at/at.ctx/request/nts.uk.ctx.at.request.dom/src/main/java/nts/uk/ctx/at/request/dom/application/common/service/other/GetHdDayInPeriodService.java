package nts.uk.ctx.at.request.dom.application.common.service.other;


import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.shared.dom.worktype.algorithm.JudgeHdSystemOneDayService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class GetHdDayInPeriodService {
	
	@Inject
	private CollectAchievement collectAchievement;
	@Inject
	private JudgeHdSystemOneDayService judgeHdSysOneDaySv;
	/**
	 * 19.指定する期間での休日日数を取得する
	 * @author hoatt
	 * @param 社員ID //employeeID
	 * @param 期間//period
	 * @return 休日日数 //holidayNumber
	 */
	public int getHolidayDayInPeriod(String employeeID, DatePeriod period){
		int hdDays = 0;
		String companyID = AppContexts.user().companyId();
		//休日日数=0(初期化)-(holidayNumber = 0(Khởi tạo ban đầu))
		//INPUT．期間．開始日から期間．終了日までループする-(Loop từ INPUT．period．startDate đến period．endDate)
		for (GeneralDate date = period.start(); date.after(period.end()); date.addDays(1)) {
			//実績の取得-(lấy Performance-kết quả thực tế) 13.実績を取得する
			AchievementOutput ach = collectAchievement.getAchievement(companyID, employeeID, date);
			if(Strings.isBlank(ach.getWorkType().getWorkTypeCode())){
				continue;
			}
			//1日休暇系か判定する-(kiểm tra xem có phải hệ thống nghỉ cả ngày ko)
			boolean checkOneDay = judgeHdSysOneDaySv.judgeHdSysOneDay(ach.getWorkType().getWorkTypeCode());
			//休日日数+=1-(holidayNumber + =1)
			hdDays = checkOneDay ? hdDays + 1 : hdDays;
		}
		return hdDays;
	};
}
