package nts.uk.ctx.at.request.dom.application.holidayworktime.service;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTimeHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTypeHolidayWork;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborCondition;

public interface HolidayService {
	
	 /**
	 * 4.勤務種類を取得する
	 * @param companyID
	 * @param employeeID
	 * @param approvalFunctionSetting
	 * @param appEmploymentSettings
	 * @param appDate
	 * @return
	 */
	public WorkTypeHolidayWork getWorkTypes(String companyID, String employeeID,List<AppEmploymentSetting> appEmploymentSettings,GeneralDate appDate,Optional<PersonalLaborCondition> personalLablorCodition);
	/**
	 * 4_b.勤務種類を取得する（詳細）
	 * @param companyID
	 * @param employeeID
	 * @param appEmploymentSettings
	 * @param appDate
	 * @param personalLablorCodition
	 * @return
	 */
	public WorkTypeHolidayWork getListWorkType(String companyID, String employeeID,List<AppEmploymentSetting> appEmploymentSettings,GeneralDate appDate,Optional<PersonalLaborCondition> personalLablorCodition);
	/**
	 * 4_c.初期選択
	 * @param workType
	 * @param appDate
	 */
	public void getWorkType(String companyID,WorkTypeHolidayWork workType,GeneralDate appDate, String employeeID,Optional<PersonalLaborCondition> personalLablorCodition);
	/**
	 * 5.就業時間帯を取得する
	 * @param companyID
	 * @param employeeID
	 * @param appEmploymentSettings
	 * @param baseDate
	 * @return
	 */
	public WorkTimeHolidayWork getWorkTimeHolidayWork(String companyID, String employeeID,GeneralDate baseDate,Optional<PersonalLaborCondition> personalLablorCodition);
	
	/**
	 * insert HolidayWork
	 * @param domain
	 * @param newApp
	 */
	void createHolidayWork(AppHolidayWork domain, Application_New newApp);
}
	
