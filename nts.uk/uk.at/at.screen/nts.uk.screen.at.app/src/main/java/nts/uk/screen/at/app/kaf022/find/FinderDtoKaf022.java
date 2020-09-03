package nts.uk.screen.at.app.kaf022.find;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.app.find.application.applicationlist.AppTypeBfFinder;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.hdworkapplicationsetting.HolidayWorkAppSetDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.vacationapplicationsetting.HolidayApplicationSettingDto;
import nts.uk.ctx.at.request.app.find.setting.company.emailset.AppEmailSetDto;
import nts.uk.ctx.at.request.app.find.setting.request.application.businesstrip.BusinessTripSetDto;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationSettingFinder;
import nts.uk.ctx.at.request.app.find.application.gobackdirectly.GoBackReflectDto;
import nts.uk.ctx.at.request.app.find.application.requestofearch.GetDataAppCfDetailFinder;
import nts.uk.ctx.at.request.app.find.application.stamp.dto.AppStampSettingDto;
import nts.uk.ctx.at.request.app.find.application.triprequestsetting.TripRequestSetFinder;
import nts.uk.ctx.at.request.app.find.application.workchange.AppWorkChangeSetDto;
import nts.uk.ctx.at.request.app.find.applicationreflect.AppReflectExeConditionDto;
import nts.uk.ctx.at.request.app.find.setting.applicationapprovalsetting.appovertime.AppOvertimeSettingFinder;
import nts.uk.ctx.at.request.app.find.setting.applicationapprovalsetting.hdapplicationsetting.TimeHdAppSetFinder;
import nts.uk.ctx.at.request.app.find.setting.applicationapprovalsetting.hdworkapplicationsetting.WithdrawalAppSetFinder;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.applicationlatearrival.LateEarlyRequestFinder;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.applicationsetting.ApplicationSettingDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.applicationsetting.DisplayReasonDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.appovertime.OvertimeAppSetDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.approvallistsetting.ApprovalListDispSettingDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.withdrawalrequestset.WithDrawalReqSetFinder;
import nts.uk.ctx.at.request.app.find.setting.company.applicationcommonsetting.AppCommonSetFinder;
import nts.uk.ctx.at.request.app.find.setting.company.applicationsetting.ProxyAppSetFinder;
import nts.uk.ctx.at.request.app.find.setting.company.displayname.AppDispNameFinder;
import nts.uk.ctx.at.request.app.find.setting.company.mailsetting.mailapplicationapproval.ApprovalTempFinder;
import nts.uk.ctx.at.request.app.find.setting.company.mailsetting.mailcontenturlsetting.UrlEmbeddedFinder;
import nts.uk.ctx.at.request.app.find.setting.company.mailsetting.mailholidayinstruction.MailHdInstructionFinder;
import nts.uk.ctx.at.request.app.find.setting.company.mailsetting.overtimeworkinstructionmail.MailOtInstructionFinder;
import nts.uk.ctx.at.request.app.find.setting.company.mailsetting.remandsetting.ContentOfRemandMailFinder;
import nts.uk.ctx.at.request.app.find.setting.company.otrestappcommon.OvertimeRestAppCommonSetFinder;
import nts.uk.ctx.at.request.app.find.setting.company.request.applicationsetting.apptypesetting.DisplayReasonFinder;
import nts.uk.ctx.at.request.app.find.setting.company.request.stamp.StampRequestSettingFinder;
import nts.uk.ctx.at.request.app.find.setting.company.vacationapplicationsetting.HdAppSetFinder;
import nts.uk.ctx.at.request.app.find.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSettingFinder;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChangeSetRepository;
import nts.uk.ctx.at.request.dom.applicationreflect.AppReflectExeConditionRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationlatearrival.LateEarlyCancelAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationlatearrival.LateEarlyCancelAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationsetting.DisplayReasonRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.OvertimeAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.approvallistsetting.ApprovalListDispSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.HolidayWorkAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.stampsetting.ApplicationStampSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HolidayApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.emailset.AppEmailSet;
import nts.uk.ctx.at.request.dom.setting.company.emailset.AppEmailSetRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.businesstrip.AppTripRequestSetRepository;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackReflectRepository;
import nts.uk.ctx.at.shared.app.find.ot.frame.OvertimeWorkFrameFinder;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.othdwork.hdworkapply.HdWorkAppReflectDto;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.othdwork.otworkapply.OtWorkAppReflectDto;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.stampapplication.StampAppReflectDto;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.timeleaveapplication.TimeLeaveAppReflectDto;
import nts.uk.ctx.at.shared.app.find.workcheduleworkrecord.appreflectprocess.appreflectcondition.vacationapplication.leaveapplication.HolidayApplicationReflectDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.ClosureHistoryFinder;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.lateearlycancellation.LateEarlyCancelReflect;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.timeleaveapplication.TimeLeaveAppReflectRepository;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.workchangeapp.ReflectWorkChangeApp;
import nts.uk.ctx.sys.portal.pub.standardmenu.StandardMenuNameExport;
import nts.uk.ctx.sys.portal.pub.standardmenu.StandardMenuNameQuery;
import nts.uk.ctx.sys.portal.pub.standardmenu.StandardMenuPub;
import nts.uk.ctx.workflow.app.find.approvermanagement.setting.ApprovalSettingFinder;
import nts.uk.ctx.workflow.app.find.approvermanagement.setting.JobAssignSettingFinder;
import nts.uk.shr.com.context.AppContexts;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class FinderDtoKaf022 {
	
	@Inject
	private ClosureHistoryFinder closureHistoryFinder;
	
    @Inject
    private AppWorkChangeSetRepository appWorkChangeSetRepo;
	
	@Inject
	private JobAssignSettingFinder jobFinder;
	
	@Inject
	private ApprovalSettingFinder approvalSettingFinder;

	@Inject
	private StandardMenuPub menuPub;

	@Inject
	private ApplicationSettingRepository appSettingRepo;

	@Inject
	private DisplayReasonRepository displayReasonRepo;

	@Inject
	private OvertimeAppSetRepository overtimeAppSetRepo;

	@Inject
    private AppReflectExeConditionRepository appReflectConditionRepo;

	@Inject
	private GoBackReflectRepository goBackReflectRepo;

	@Inject
    private LateEarlyCancelAppSetRepository lateEarlyCancelRepo;

	@Inject
    private ApplicationStampSettingRepository appStampSettingRepo;

    @Inject
    private ApprovalListDispSetRepository approvalListDispSetRepo;

    @Inject
	private AppTripRequestSetRepository appTripRequestSetRepo;

    @Inject
	private AppEmailSetRepository appEmailSetRepo;

	@Inject
	private HolidayApplicationSettingRepository holidayApplicationSettingRepo;

	@Inject
	private HolidayWorkAppSetRepository holidayWorkAppSetRepo;

	@Inject
	private TimeLeaveAppReflectRepository timeLeaveAppReflectRepo;

	public DtoKaf022 findDtoKaf022() {
		String companyId = AppContexts.user().companyId();
		// TODO: get Application Settings
		ApplicationSettingDto applicationSetting = appSettingRepo.findByCompanyId(companyId).map(ApplicationSettingDto::fromDomain).orElse(null);
		List<DisplayReasonDto> displayReasons = displayReasonRepo.findByCompanyId(companyId).stream().map(DisplayReasonDto::fromDomain).collect(Collectors.toList());
		OvertimeAppSetDto overTimeAppSetting = overtimeAppSetRepo.findSettingByCompanyId(companyId).map(OvertimeAppSetDto::fromDomain).orElse(null);
        AppWorkChangeSetDto appWorkChangeSetDto = appWorkChangeSetRepo.findByCompanyId(companyId).map(AppWorkChangeSetDto::fromDomain).orElse(null);
        LateEarlyCancelAppSet lateEarlyCancelAppSet = lateEarlyCancelRepo.getByCId(companyId);
        AppStampSettingDto appStampSetting = appStampSettingRepo.findSettingByCompanyId(companyId).map(AppStampSettingDto::fromDomain).orElse(null);
        ApprovalListDispSettingDto approvalListDispSetting = approvalListDispSetRepo.findByCID(companyId).map(ApprovalListDispSettingDto::fromDomain).orElse(null);
		AppEmailSet appEmailSet = appEmailSetRepo.findByCID(companyId);
		HolidayApplicationSettingDto holidayApplicationSetting = holidayApplicationSettingRepo.findSettingByCompanyId(companyId).map(HolidayApplicationSettingDto::fromDomain).orElse(null);
		HolidayWorkAppSetDto holidayWorkAppSet = holidayWorkAppSetRepo.findSettingByCompany(companyId).map(HolidayWorkAppSetDto::fromDomain).orElse(null);

		// TODO: get Reflection Settings
        AppReflectExeConditionDto appReflectCondition = appReflectConditionRepo.findByCompanyId(companyId).map(AppReflectExeConditionDto::fromDomain).orElse(null);
        OtWorkAppReflectDto overtimeAppReflect = overtimeAppSetRepo.findReflectByCompanyId(companyId).map(OtWorkAppReflectDto::fromDomain).orElse(null);
        ReflectWorkChangeApp reflectWorkChangeApp = appWorkChangeSetRepo.findByCompanyIdReflect(companyId).orElse(null);
		BusinessTripSetDto tripRequestSet = appTripRequestSetRepo.findById(companyId).map(BusinessTripSetDto::fromDomain).orElse(null);
		GoBackReflectDto goBackReflect = goBackReflectRepo.findByCompany(companyId).map(GoBackReflectDto::fromDomain).orElse(null);
        LateEarlyCancelReflect lateEarlyCancelReflect = lateEarlyCancelRepo.getByCompanyId(companyId);
		StampAppReflectDto stampAppReflectDto = appStampSettingRepo.findReflectByCompanyId(companyId).map(StampAppReflectDto::fromDomain).orElse(null);
		HolidayApplicationReflectDto holidayApplicationReflect = holidayApplicationSettingRepo.findReflectByCompanyId(companyId).map(HolidayApplicationReflectDto::fromDomain).orElse(null);
		HdWorkAppReflectDto hdWorkAppReflectDto = holidayWorkAppSetRepo.findReflectByCompany(companyId).map(HdWorkAppReflectDto::fromDomain).orElse(null);
		TimeLeaveAppReflectDto timeLeaveAppReflectDto = timeLeaveAppReflectRepo.findByCompany(companyId).map(TimeLeaveAppReflectDto::fromDomain).orElse(null);

		// TODO: get menu
		List<StandardMenuNameQuery> queries = new ArrayList<>();
		queries.add(new StandardMenuNameQuery("KAF005", "A", Optional.of("overworkatr=0")));
		queries.add(new StandardMenuNameQuery("KAF005", "A", Optional.of("overworkatr=1")));
		queries.add(new StandardMenuNameQuery("KAF005", "A", Optional.of("overworkatr=2")));
		queries.add(new StandardMenuNameQuery("KAF006", "A", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF007", "A", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF008", "A", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF009", "A", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF010", "A", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF012", "A", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF004", "A", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF002", "A", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF002", "C", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF011", "A", Optional.empty()));
		queries.add(new StandardMenuNameQuery("KAF020", "A", Optional.empty()));
		List<StandardMenuNameExport> menuList = menuPub.getMenuDisplayName(companyId, queries);

		DtoKaf022 result = new DtoKaf022();
		result.allClosure = closureHistoryFinder.findAll();

		// refactor 4
		// A
		result.setApplicationSetting(applicationSetting);
		result.setReasonDisplaySettings(displayReasons);
		result.setMenus(menuList);
		result.setAppReflectCondition(appReflectCondition);
		result.setNightOvertimeReflectAtr(appSettingRepo.getNightOvertimeReflectAtr(companyId));
		result.jobAssign = jobFinder.findApp();
		result.approvalSettingDto = approvalSettingFinder.findApproSet();

		// B
        result.setOvertimeAppSetting(overTimeAppSetting);
        result.setOvertimeAppReflect(overtimeAppReflect);

        // C
		result.setHolidayApplicationSetting(holidayApplicationSetting);
		result.setHolidayApplicationReflect(holidayApplicationReflect);

        // D
        result.appChange = appWorkChangeSetDto;
        result.workTimeReflectAtr = reflectWorkChangeApp != null ? reflectWorkChangeApp.getWhetherReflectAttendance().value : 0;

        // E
		result.tripRequestSetting = tripRequestSet;

        // F
		result.goBackReflect = goBackReflect;

		// G
		result.setHolidayWorkApplicationSetting(holidayWorkAppSet);
		result.setHolidayWorkApplicationReflect(hdWorkAppReflectDto);

		// H
		result.setTimeLeaveApplicationReflect(timeLeaveAppReflectDto);

		// I
        result.lateEarlyCancelAtr = lateEarlyCancelAppSet != null ? lateEarlyCancelAppSet.getCancelAtr().value : 0;
        result.lateEarlyClearAlarmAtr = lateEarlyCancelReflect != null ? BooleanUtils.toInteger(lateEarlyCancelReflect.isClearLateReportWarning()) : 0;

        // J
        result.appStampSetting = appStampSetting;
        result.appStampReflect = stampAppReflectDto;

        // Q
        result.approvalListDisplaySetting = approvalListDispSetting;

        // Y
		result.appMailSetting = appEmailSet == null ? null : new AppEmailSetDto(appEmailSet);

		return result;
	}
}
