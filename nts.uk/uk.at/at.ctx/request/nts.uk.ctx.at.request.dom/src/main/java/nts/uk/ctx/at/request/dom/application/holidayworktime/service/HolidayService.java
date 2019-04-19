package nts.uk.ctx.at.request.dom.application.holidayworktime.service;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTimeHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTypeHolidayWork;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;

public interface HolidayService {
	/**
	 * 4_a.勤務種類を取得する（法定内外休日）
	 * @param companyID
	 * @param employeeID
	 * @param appEmploymentSettings
	 * @param appDate
	 * @param personalLablorCodition
	 * @return
	 */
	public WorkTypeHolidayWork getWorkTypeForLeaverApp(String companyID, String employeeID,List<AppEmploymentSetting> appEmploymentSettings,GeneralDate appDate,Optional<WorkingConditionItem> personalLablorCodition,Integer paramholidayCls);
	
	 /**
	 * 4.勤務種類を取得する
	 * @param companyID
	 * @param employeeID
	 * @param approvalFunctionSetting
	 * @param appEmploymentSettings
	 * @param appDate
	 * @return
	 */
	public WorkTypeHolidayWork getWorkTypes(String companyID, String employeeID,List<AppEmploymentSetting> appEmploymentSettings,GeneralDate appDate,Optional<WorkingConditionItem> personalLablorCodition,boolean isChangeDate);
	/**
	 * 4_b.勤務種類を取得する（詳細）
	 * @param companyID
	 * @param employeeID
	 * @param appEmploymentSettings
	 * @param appDate
	 * @param personalLablorCodition
	 * @return
	 */
	public WorkTypeHolidayWork getListWorkType(String companyID,
			String employeeID,List<AppEmploymentSetting> appEmploymentSettings,GeneralDate appDate,Optional<WorkingConditionItem> personalLablorCodition);
	/**
	 * 4_c.初期選択
	 * @param workType
	 * @param appDate
	 */
	public void getWorkType(String companyID,WorkTypeHolidayWork workType,GeneralDate appDate, String employeeID,Optional<WorkingConditionItem> personalLablorCodition,boolean isChangeDate);
	/**
	 * 5.就業時間帯を取得する
	 * @param companyID
	 * @param employeeID
	 * @param appEmploymentSettings
	 * @param baseDate
	 * @param isChangeDate 
	 * @return
	 */
	public WorkTimeHolidayWork getWorkTimeHolidayWork(String companyID, String employeeID,GeneralDate baseDate,Optional<WorkingConditionItem> personalLablorCodition,boolean isChangeDate);
	
	/**
	 * insert HolidayWork
	 * @param domain
	 * @param newApp
	 */
	void createHolidayWork(AppHolidayWork domain, Application_New newApp);
	
	/**
	 * 11.休出申請（振休変更）削除
	 * @param appID
	 */
	public void delHdWorkByAbsLeaveChange(String appID);
}
	
