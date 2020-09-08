package nts.uk.screen.at.app.kaf022.find;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.hdworkapplicationsetting.HolidayWorkAppSetDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.optionalitemappsetting.OptionalItemAppSetDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.vacationapplicationsetting.HolidayApplicationSettingDto;
import nts.uk.ctx.at.request.app.find.setting.company.emailset.AppEmailSetDto;
import nts.uk.ctx.at.request.app.find.setting.request.application.businesstrip.BusinessTripSetDto;
import nts.uk.ctx.at.request.app.find.application.gobackdirectly.GoBackReflectDto;
import nts.uk.ctx.at.request.app.find.application.stamp.dto.AppStampSettingDto;
import nts.uk.ctx.at.request.app.find.application.workchange.AppWorkChangeSetDto;
import nts.uk.ctx.at.request.app.find.applicationreflect.AppReflectExeConditionDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.appovertime.OvertimeAppSetDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.approvallistsetting.ApprovalListDispSettingDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.applicationsetting.ApplicationSettingDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.applicationsetting.DisplayReasonDto;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.othdwork.hdworkapply.HdWorkAppReflectDto;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.othdwork.otworkapply.OtWorkAppReflectDto;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.stampapplication.StampAppReflectDto;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.timeleaveapplication.TimeLeaveAppReflectDto;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.vacationapplication.leaveapplication.HolidayApplicationReflectDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosureHistoryFindDto;
import nts.uk.ctx.sys.portal.pub.standardmenu.StandardMenuNameExport;
import nts.uk.ctx.workflow.app.find.approvermanagement.setting.ApprovalSettingDto;
import nts.uk.ctx.workflow.app.find.approvermanagement.setting.JobAssignSettingDto;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class DtoKaf022 {
	public List<ClosureHistoryFindDto> allClosure;
	public ApplicationSettingDto applicationSetting;
	public List<DisplayReasonDto> reasonDisplaySettings;
	public List<StandardMenuNameExport> menus;
	public JobAssignSettingDto jobAssign;
	public ApprovalSettingDto approvalSettingDto;
	public AppReflectExeConditionDto appReflectCondition;
	public Integer nightOvertimeReflectAtr;

	public OvertimeAppSetDto overtimeAppSetting;
	public OtWorkAppReflectDto overtimeAppReflect;

	public HolidayApplicationSettingDto holidayApplicationSetting;
	public HolidayApplicationReflectDto holidayApplicationReflect;

	public AppWorkChangeSetDto appChange;
	public Integer workTimeReflectAtr;

	public BusinessTripSetDto tripRequestSetting;

	public GoBackReflectDto goBackReflect;

	public HolidayWorkAppSetDto holidayWorkApplicationSetting;
	public HdWorkAppReflectDto holidayWorkApplicationReflect;

	public TimeLeaveAppReflectDto timeLeaveApplicationReflect;

	public int lateEarlyCancelAtr;
	public int lateEarlyClearAlarmAtr;

	public AppStampSettingDto appStampSetting;
	public StampAppReflectDto appStampReflect;

	public List<OptionalItemAppSetDto> optionalItemApplicationSettings;

	public ApprovalListDispSettingDto approvalListDisplaySetting;

	public AppEmailSetDto appMailSetting;
}
