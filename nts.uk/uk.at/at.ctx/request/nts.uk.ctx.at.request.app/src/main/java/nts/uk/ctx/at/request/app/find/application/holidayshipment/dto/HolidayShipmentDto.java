package nts.uk.ctx.at.request.app.find.application.holidayshipment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.common.dto.AppEmploymentSettingDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationSettingDto;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.absenceleaveapp.AbsenceLeaveAppDto;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.recruitmentapp.RecruitmentAppDto;
import nts.uk.ctx.at.request.app.find.setting.applicationreason.ApplicationReasonDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.withdrawalrequestset.WithDrawalReqSetDto;
import nts.uk.ctx.at.request.app.find.setting.workplace.ApprovalFunctionSettingDto;
import nts.uk.ctx.at.shared.app.find.worktype.WorkTypeDto;

/**
 * @author sonnlb
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayShipmentDto {

	/**
	 * 申請承認設定
	 */
	private ApplicationSettingDto applicationSetting;

	/**
	 * 雇用別申請承認設定
	 */
	private List<AppEmploymentSettingDto> appEmploymentSettings;
	/**
	 * 申請承認機能設定
	 */
	private ApprovalFunctionSettingDto approvalFunctionSetting;
	/**
	 * 基準日
	 */
	private GeneralDate refDate;

	/**
	 * 振出用勤務種類
	 */
	private List<WorkTypeDto> recWkTypes;
	/**
	 * 振休用勤務種類
	 */
	private List<WorkTypeDto> absWkTypes;
	/**
	 * 申請表示設定
	 */
	int preOrPostType;

	/**
	 * 申請者社員ID
	 */
	private String employeeID;

	/**
	 * 社員名
	 */
	private String employeeName;

	private boolean manualSendMailAtr;

	private List<ApplicationReasonDto> appReasons;

	private ChangeWorkTypeDto changeWkType;

	/**
	 * 振出用就業時間帯 振休用就業時間帯 Mặc định null
	 */

	/**
	 * 振休振出申請設定
	 */
	private WithDrawalReqSetDto drawalReqSet;

	/**
	 * 振休申請
	 * 
	 */
	private AbsenceLeaveAppDto absApp;

	/**
	 * 振出申請
	 */
	private RecruitmentAppDto recApp;

	/**
	 * 申請
	 */
	private ApplicationDto_New application;
}
