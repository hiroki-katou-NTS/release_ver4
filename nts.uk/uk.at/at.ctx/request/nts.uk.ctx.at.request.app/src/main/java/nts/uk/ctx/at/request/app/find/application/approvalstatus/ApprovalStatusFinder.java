package nts.uk.ctx.at.request.app.find.application.approvalstatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.i18n.I18NText;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.setting.company.request.approvallistsetting.ApprovalListDisplaySetDto;
import nts.uk.ctx.at.request.app.find.setting.company.vacationapplicationsetting.HdAppSetDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.appabsence.AllDayHalfDayLeaveAtr;
import nts.uk.ctx.at.request.dom.application.applist.service.AppCompltLeaveSync;
import nts.uk.ctx.at.request.dom.application.applist.service.OverTimeFrame;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppAbsenceFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppDetailInfoRepository;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppGoBackInfoFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppHolidayWorkFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppOverTimeInfoFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppWorkChangeFull;
import nts.uk.ctx.at.request.dom.application.approvalstatus.ApprovalStatusMailTemp;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.ApprovalStatusService;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApplicationsListOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalStatusEmployeeOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttAppDetail;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttAppOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttByEmpListOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApproverOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.SendMailResultOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.UnApprovalSendMail;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.WorkplaceInfor;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalBehaviorAtrImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.HolidayAppType;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.ApprovalListDisplaySetting;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ApprovalComfirmDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosureHistoryForComDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosuresDto;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
/**
 * 承認状況メールテンプレート
 * 
 * @author dat.lh
 */
public class ApprovalStatusFinder {

	@Inject
	private ClosureService closureService;

	/** The repository. */
	@Inject
	private ClosureRepository repository;

	@Inject
	ClosureEmploymentRepository closureEmpRepo;

	@Inject
	private ApprovalStatusService appSttService;

	@Inject
	private AppDetailInfoRepository appDetailInfoRepo;

	@Inject
	private RequestSettingRepository repoRequestSet;

	/**
	 * アルゴリズム「承認状況本文起動」を実行する
	 */
	public List<ApprovalStatusMailTempDto> getMailTemp() {
		// 会社ID
		String cid = AppContexts.user().companyId();
		List<ApprovalStatusMailTempDto> listMail = new ArrayList<ApprovalStatusMailTempDto>();

		listMail.add(this.getApprovalStatusMailTemp(cid, 0));
		listMail.add(this.getApprovalStatusMailTemp(cid, 1));
		listMail.add(this.getApprovalStatusMailTemp(cid, 2));
		listMail.add(this.getApprovalStatusMailTemp(cid, 3));
		listMail.add(this.getApprovalStatusMailTemp(cid, 4));
		return listMail;
	}

	private ApprovalStatusMailTempDto getApprovalStatusMailTemp(String cid, int mailType) {
		// アルゴリズム「承認状況メール本文取得」を実行する
		ApprovalStatusMailTemp domain = appSttService.getApprovalStatusMailTemp(mailType);
		// ドメインの取得
		if (Objects.isNull(domain)) {
			// ドメインが取得できなかった場合
			// 画面モード ＝ 新規
			return new ApprovalStatusMailTempDto(mailType, 1, 1, 1, "", "", 0);
		}
		// ドメインが取得できた場合(lấy được)
		// 画面モード ＝ 更新
		return ApprovalStatusMailTempDto.fromDomain(domain);
	}

	/**
	 * 承認状況メールテスト送信
	 */
	public String confirmSenderMail() {
		// アルゴリズム「承認状況送信者メール確認」を実行する
		return appSttService.confirmApprovalStatusMailSender();
	}

	public SendMailResultOutput sendTestMail(int mailType) {
		// アルゴリズム「承認状況メールテスト送信実行」を実行する
		return appSttService.sendTestMail(mailType);
	}

	/**
	 * アルゴリズム「承認状況指定締め日取得」を実行する Acquire approval situation designated closing
	 * date
	 * 
	 * @return approval situation
	 */
	public ApprovalComfirmDto findAllClosure() {
		// Get companyID.
		String companyId = AppContexts.user().companyId();
		GeneralDate startDate = null;
		GeneralDate endDate = null;
		int processingYm = 0;
		// ドメインモデル「就業締め日」を取得する <shared>
		List<Closure> closureList = this.repository.findAllUse(companyId);
		int selectedClosureId = 0;
		List<ClosuresDto> closureDto = closureList.stream().map(x -> {
			int closureId = x.getClosureId().value;
			List<ClosureHistoryForComDto> closureHistoriesList = x.getClosureHistories().stream().map(x1 -> {
				return new ClosureHistoryForComDto(x1.getClosureName().v(), x1.getClosureId().value,
						x1.getEndYearMonth().v().intValue(), x1.getClosureDate().getClosureDay().v().intValue(),
						x1.getStartYearMonth().v().intValue());
			}).collect(Collectors.toList());
			ClosureHistoryForComDto closureHistories = closureHistoriesList.stream()
					.filter(x2 -> x2.getClosureId() == closureId).findFirst().orElse(null);
			return new ClosuresDto(closureId, closureHistories.getCloseName(), closureHistories.getClosureDate());
		}).collect(Collectors.toList());

		// 就業締め日（リスト）の先頭の締めIDを選択
		List<String> listEmpCode = new ArrayList<>();
		Optional<ClosuresDto> closure = closureDto.stream().findFirst();
		if (closure.isPresent()) {
			val closureId = closure.get().getClosureId();
			selectedClosureId = closureId;
			val closureOpt = this.repository.findById(companyId, closureId);
			if (closureOpt.isPresent()) {
				val closureItem = closureOpt.get();
				// 当月の期間を算出する
				val yearMonth = closureItem.getClosureMonth().getProcessingYm();
				processingYm = yearMonth.v();
				// アルゴリズム「承認状況指定締め期間設定」を実行する
				// アルゴリズム「当月の期間を算出する」を実行する
				DatePeriod closurePeriod = this.closureService.getClosurePeriod(closureId, yearMonth);
				startDate = closurePeriod.start();
				endDate = closurePeriod.end();
				// ドメインモデル「雇用に紐づく就業締め」より、雇用コードと締めIDを取得する
				List<ClosureEmployment> listEmployee = closureEmpRepo.findByClosureId(companyId, closureId);
				for (ClosureEmployment emp : listEmployee) {
					listEmpCode.add(emp.getEmploymentCD());
				}
			} else {
				throw new RuntimeException("Could not find closure");
			}
		}
		return new ApprovalComfirmDto(selectedClosureId, closureDto, startDate, endDate, processingYm, listEmpCode);
	}

	/**
	 * アルゴリズム「承認状況指定締め期間設定」を実行する
	 * 
	 * @param closureId
	 * @param closureDate
	 * @return
	 */
	public ApprovalStatusPeriorDto getApprovalStatusPerior(int closureId, int closureDate) {
		// Get companyID.
		String companyId = AppContexts.user().companyId();
		GeneralDate startDate = null;
		GeneralDate endDate = null;
		int processingYmNew = 0;
		// 当月の期間を算出する
		YearMonth processingYm = new YearMonth(closureDate);

		List<ClosureEmployment> listEmployee = new ArrayList<>();
		Optional<Closure> closure = repository.findById(companyId, closureId);
		if (!closure.isPresent()) {
			throw new RuntimeException("Could not find closure");
		}

		val yearMonth = closure.get().getClosureMonth().getProcessingYm();
		processingYmNew = yearMonth.v();
		// アルゴリズム「当月の期間を算出する」を実行する
		DatePeriod closurePeriod = this.closureService.getClosurePeriod(closureId, processingYm);
		startDate = closurePeriod.start();
		endDate = closurePeriod.end();
		// ドメインモデル「雇用に紐づく就業締め」より、雇用コードと締めIDを取得する
		listEmployee = closureEmpRepo.findByClosureId(companyId, closureId);
		List<String> listEmpCode = new ArrayList<>();
		for (ClosureEmployment emp : listEmployee) {
			listEmpCode.add(emp.getEmploymentCD());
		}
		return new ApprovalStatusPeriorDto(startDate, endDate, listEmpCode, processingYmNew);
	}

	/**
	 * アルゴリズム「承認状況職場別起動」を実行する
	 * 
	 * @param appStatus
	 */
	public List<ApprovalSttAppOutput> getAppSttByWorkpace(ApprovalStatusActivityData appStatus) {
		List<ApprovalSttAppOutput> listAppSttApp = new ArrayList<>();
		GeneralDate startDate = appStatus.getStartDate();
		GeneralDate endDate = appStatus.getEndDate();
		List<WorkplaceInfor> listWorkPlaceInfor = appStatus.getListWorkplace();
		for (WorkplaceInfor wkp : listWorkPlaceInfor) {
			// アルゴリズム「承認状況取得職場社員」を実行する
			List<ApprovalStatusEmployeeOutput> listAppStatusEmp = appSttService.getApprovalStatusEmployee(wkp.getCode(),
					startDate, endDate, appStatus.getListEmpCd());
			// アルゴリズム「承認状況取得申請承認」を実行する
			ApprovalSttAppOutput approvalSttApp = appSttService.getApprovalSttApp(wkp, listAppStatusEmp);
			listAppSttApp.add(approvalSttApp);
		}

		return listAppSttApp;
	}

	/**
	 * アルゴリズム「承認状況未承認メール送信」を実行する
	 */
	public List<String> getAppSttSendingUnapprovedMail(List<UnApprovalSendMail> listAppSttApp) {
		return appSttService.getAppSttSendingUnapprovedMail(listAppSttApp);

	}

	/**
	 * アルゴリズム「承認状況未承認メール送信実行」を実行する
	 */
	public SendMailResultOutput exeSendUnconfirmedMail(UnAppMailTransmisDto unAppMail) {
		return appSttService.exeSendUnconfirmedMail(unAppMail.getListWkpId(), unAppMail.getClosureStart(),
				unAppMail.getClosureEnd(), unAppMail.getListEmpCd());
	}

	/**
	 * アルゴリズム「承認状況社員別起動」を実行する
	 */
	public ApprovalSttByEmpListOutput initApprovalSttByEmployee(ApprovalStatusByIdDto appSttById) {
		return appSttService.getApprovalSttById(appSttById.getSelectedWkpId(), appSttById.getListWkpId(),
				appSttById.getStartDate(), appSttById.getEndDate(), appSttById.getListEmpCode());
	}

	/**
	 * アルゴリズム「承認状況申請内容表示」を実行する
	 */
	public ApplicationListDto initApprovalSttRequestContentDis(ApprovalSttRequestContentDis appSttContent) {
		String companyID = AppContexts.user().companyId();
		ApplicationsListOutput appList = appSttService
				.initApprovalSttRequestContentDis(appSttContent.getListStatusEmp());
		HdAppSetDto hdAppSetDto = HdAppSetDto.convertToDto(appList.getLstHdAppSet().get());
		List<ApplicationDetailDto> listApplicationDetail = new ArrayList<>();
		List<ApprovalSttAppDetail> listAppSttDetail = appList.getApprovalSttAppDetail();
		List<AppCompltLeaveSync> lstCompltLeaveSync = appList.getListSync();
		//List<ApplicationDto_New> listApp = new ArrayList<>();
		for (ApprovalSttAppDetail app : listAppSttDetail) {
			ApplicationDetailDto detail = new ApplicationDetailDto();

			ApplicationType appType = app.getAppDispName().getAppType();
			ApplicationDto_New applicaton_N = ApplicationDto_New.fromDomain(app.getAppContent().getApplication());
			//listApp.add(applicaton_N);
			String appId = applicaton_N.getApplicationID();
			detail.setAppType(appType.value);
			detail.setAppStartDate(applicaton_N.getStartDate());
			detail.setAppEndDate(applicaton_N.getEndDate());
			detail.setAppName(app.getAppDispName().getDispName().v());
			detail.setPrePostAtr(applicaton_N.getPrePostAtr());
			detail.setApplicationDate(applicaton_N.getApplicationDate());
			detail.setApplicationID(applicaton_N.getApplicationID());
			List<ApprovalPhaseStateImport_New> listApprovalPhase = app.getAppContent().getApprRootContentExport()
					.getApprovalRootState().getListApprovalPhaseState();
			listApprovalPhase.sort((ApprovalPhaseStateImport_New x1,
					ApprovalPhaseStateImport_New x2) -> x1.getPhaseOrder() - x2.getPhaseOrder());
			List<Integer> appStatus = new ArrayList<>();
			for (ApprovalPhaseStateImport_New appPhase : listApprovalPhase) {
				ApprovalBehaviorAtrImport_New appBehavior = appPhase.getApprovalAtr();
				switch (appBehavior) {
				case UNAPPROVED:
					appStatus.add(0);
					break;
				case APPROVED:
					appStatus.add(1);
					break;
				case REMAND:
					appStatus.add(0);
					break;
				case DENIAL:
					appStatus.add(2);
					break;
				default:
					break;
				}
			}
			detail.setApprovalStatus(appStatus);
			List<ApproverOutput> listApprover = app.getListApprover();
			for (ApproverOutput approver : listApprover) {
				int phase = approver.getPhase();
				String numOfPerson = approver.getNumOfPeople() > 0
						? I18NText.getText("KAF018_47", approver.getNumOfPeople().toString()) : "";
				String others = approver.getEmpName() + numOfPerson;
				switch (phase) {
				case 1:
					detail.setPhase1(others);
					break;
				case 2:
					detail.setPhase2(others);
					break;
				case 3:
					detail.setPhase3(others);
					break;
				case 4:
					detail.setPhase4(others);
					break;
				case 5:
					detail.setPhase5(others);
					break;
				default:
					break;
				}
			}
			String appContent = "";
			switch (appType) {
			// 残業申請
			case OVER_TIME_APPLICATION:
				appContent = getAppContentOverTime(companyID, appId);
				break;
			// 休暇申請
			case ABSENCE_APPLICATION:
				appContent = getAbsenceApplication(hdAppSetDto, applicaton_N, companyID, appId);
				break;
			// 勤務変更申請
			case WORK_CHANGE_APPLICATION:
				appContent = getAppWorkChangeInfo(companyID, appId);
				// có endDate
				break;
			// 出張申請
			case BUSINESS_TRIP_APPLICATION:
				// TODO
				// có endDate
				break;
			// 直行直帰申請
			case GO_RETURN_DIRECTLY_APPLICATION:
				appContent = getAppGoBackInfo(companyID, appId);
				break;
			// 休出時間申請
			case BREAK_TIME_APPLICATION:
				appContent = getBreakTimeApp(applicaton_N, companyID, appId);
				break;
			// 打刻申請
			case STAMP_APPLICATION:
				// TODO
				break;
			// 時間年休申請
			case ANNUAL_HOLIDAY_APPLICATION:

				break;
			// 遅刻早退取消申請
			case EARLY_LEAVE_CANCEL_APPLICATION:
				// TODO
				break;
			// 振休振出申請
			case COMPLEMENT_LEAVE_APPLICATION:
				appContent = "";
				break;
			// 打刻申請（NR形式）
			case STAMP_NR_APPLICATION:
				// TODO
				break;
			// 連続出張申請
			case LONG_BUSINESS_TRIP_APPLICATION:
				// TODO
				break;
			// 出張申請オフィスヘルパー
			case BUSINESS_TRIP_APPLICATION_OFFICE_HELPER:
				// TODO
				break;
			// ３６協定時間申請
			case APPLICATION_36:
				// TODO
				break;

			default:
				break;
			}
			detail.setAppContent(appContent);
			int reflectState = applicaton_N.getReflectPerState();
			detail.setReflectState(reflectState);
			listApplicationDetail.add(detail);
		}
		return new ApplicationListDto(listApplicationDetail, lstCompltLeaveSync);
	}

	private String getBreakTimeApp(ApplicationDto_New applicaton_N, String companyID, String appId) {
		String appContent = "";
		AppHolidayWorkFull appHoliday = appDetailInfoRepo.getAppHolidayWorkInfo(companyID, appId);
		appContent += I18NText.getText("KAF018_275") + appHoliday.getWorkTypeName() + appHoliday.getWorkTimeName();
		appContent += appHoliday.getStartTime1();
		if (!appHoliday.getStartTime1().equals("") && !appHoliday.getEndTime1().equals("")) {
			appContent += I18NText.getText("KAF018_220");
		}
		appContent += appHoliday.getEndTime1();
		appContent += appHoliday.getStartTime2();
		if (!appHoliday.getStartTime2().equals("") && !appHoliday.getEndTime2().equals("")) {
			appContent += I18NText.getText("KAF018_220");
		}
		appContent += appHoliday.getEndTime2();
		List<OverTimeFrame> lstFrame = appHoliday.getLstFrame();
		int totalTime = 0;
		for (OverTimeFrame overTime : lstFrame) {
			totalTime += overTime.getApplicationTime();
		}
		int countItem = 0;
		String contentOther = "";
		for (OverTimeFrame overFrame : lstFrame) {
			if (overFrame.getApplicationTime() != 0) {
				contentOther += overFrame.getName() + " " + clockShorHm(overFrame.getApplicationTime());
				countItem++;
				if (countItem > 1) {
					break;
				}
			}
		}
		appContent += I18NText.getText("KAF018_276") + " " + clockShorHm(totalTime) + "（" + contentOther + "）";
		return appContent;
	}

	private String getAppGoBackInfo(String companyID, String appId) {
		String appContent = "";
		AppGoBackInfoFull appGoBackInfo = appDetailInfoRepo.getAppGoBackInfo(companyID, appId);
		appContent += I18NText.getText("KAF018_258");
		appContent += Objects.isNull(appGoBackInfo.getGoWorkAtr1()) ? "" : I18NText.getText("KAF018_259");
		appContent += appGoBackInfo.getWorkTimeStart1();
		appContent += Objects.isNull(appGoBackInfo.getBackHomeAtr1()) ? "" : I18NText.getText("KAF018_260");
		appContent += appGoBackInfo.getWorkTimeEnd1();
		appContent += Objects.isNull(appGoBackInfo.getGoWorkAtr2()) ? "" : I18NText.getText("KAF018_259");
		appContent += appGoBackInfo.getWorkTimeStart2();
		appContent += Objects.isNull(appGoBackInfo.getBackHomeAtr2()) ? "" : I18NText.getText("KAF018_260");
		appContent += appGoBackInfo.getWorkTimeEnd2();
		return appContent;
	}

	private String getAppWorkChangeInfo(String companyID, String appId) {
		String appContent = "";
		AppWorkChangeFull appWorkChange = appDetailInfoRepo.getAppWorkChangeInfo(companyID, appId);
		appContent += I18NText.getText("KAF018_250") + " ";
		appContent += appWorkChange.getWorkTypeName() + appWorkChange.getWorkTimeName();
		if (!appWorkChange.getWorkTimeStart1().equals("") && !appWorkChange.getWorkTimeEnd1().equals("")) {
			appContent += Objects.isNull(appWorkChange.getGoWorkAtr1()) ? "" : I18NText.getText("KAF018_252");
			appContent += appWorkChange.getWorkTimeStart1();
			appContent += I18NText.getText("KAF018_220");
			appContent += Objects.isNull(appWorkChange.getBackHomeAtr1()) ? "" : I18NText.getText("KAF018_252");
			appContent += appWorkChange.getWorkTimeEnd1();
		}
		if (!appWorkChange.getWorkTimeStart2().equals("") && !appWorkChange.getWorkTimeEnd2().equals("")) {
			appContent += Objects.isNull(appWorkChange.getGoWorkAtr2()) ? "" : I18NText.getText("KAF018_252");
			appContent += appWorkChange.getWorkTimeStart2();
			appContent += I18NText.getText("KAF018_220");
			appContent += Objects.isNull(appWorkChange.getBackHomeAtr2()) ? "" : I18NText.getText("KAF018_252");
			appContent += appWorkChange.getWorkTimeEnd2();
		}
		if (!appWorkChange.getBreakTimeStart1().equals("") && !appWorkChange.getBreakTimeEnd1().equals("")) {
			appContent += I18NText.getText("KAF018_251");
			appContent += appWorkChange.getBreakTimeStart1() + I18NText.getText("KAF018_220")
					+ appWorkChange.getBreakTimeEnd1();
		}
		return appContent;
	}

	private String getAppContentOverTime(String companyID, String appId) {
		String appContent = "";

		AppOverTimeInfoFull appOverTime = appDetailInfoRepo.getAppOverTimeInfo(companyID, appId);
		appContent += I18NText.getText("KAF018_268");
		appContent += appOverTime.getWorkClockFrom1();
		appContent += I18NText.getText("KAF018_220");
		appContent += appOverTime.getWorkClockTo1();
		appContent += appOverTime.getWorkClockFrom2() != "" ? appOverTime.getWorkClockFrom2() : "";
		appContent += appOverTime.getWorkClockTo2() != "" ? I18NText.getText("KAF018_220") : "";
		appContent += appOverTime.getWorkClockTo2() != "" ? appOverTime.getWorkClockTo2() : "";
		appContent += "残業合計  ";

		List<OverTimeFrame> lstFrame = appOverTime.getLstFrame();
		Comparator<OverTimeFrame> sortList = Comparator.comparing(OverTimeFrame::getAttendanceType)
				.thenComparing(OverTimeFrame::getFrameNo);
		lstFrame.sort(sortList);
		int countItem = 0;
		int countRest = 0;
		int time = 0;
		String frameName = "";
		for (OverTimeFrame overFrame : lstFrame) {
			if (overFrame.getApplicationTime() != 0) {
				frameName += overFrame.getName() + clockShorHm(overFrame.getApplicationTime());
				time += overFrame.getApplicationTime();
				countItem++;
				if (countItem > 2) {
					countRest = lstFrame.size() - 3;
					break;
				}
			}
		}
		String other = countRest > 0 ? I18NText.getText("KAF018_231", String.valueOf(countRest)) : "";
		String otherFull = (frameName != "" || other != "") ? "（" + frameName + other + "）" : "";
		appContent += clockShorHm(time) + "　" + otherFull;
		return appContent;
	}

	private String getAbsenceApplication(HdAppSetDto hdAppSetDto, ApplicationDto_New applicaton_N, String companyID,
			String appId) {
		String appContent = "";
		AppAbsenceFull appabsence = appDetailInfoRepo.getAppAbsenceInfo(companyID, appId, 0);
		HolidayAppType holidayAppType = EnumAdaptor.valueOf(appabsence.getHolidayAppType(), HolidayAppType.class);
		String value = "";
		switch (holidayAppType) {
		case ABSENCE:
			value = hdAppSetDto.getAbsenteeism();
			break;
		case ANNUAL_PAID_LEAVE:
			value = hdAppSetDto.getYearHdName();
			break;
		case DIGESTION_TIME:
			value = hdAppSetDto.getTimeDigest();
			break;
		case HOLIDAY:
			value = hdAppSetDto.getHdName();
			break;
		case REST_TIME:
			value = hdAppSetDto.getFurikyuName();
			break;
		case SUBSTITUTE_HOLIDAY:
			value = hdAppSetDto.getObstacleName();
			break;
		case SPECIAL_HOLIDAY:
			value = hdAppSetDto.getSpecialVaca();
			break;
		case YEARLY_RESERVE:
			value = hdAppSetDto.getYearResig();
			break;
		default:
			value = "";
			break;
		}
		AllDayHalfDayLeaveAtr allDayHaflDay = EnumAdaptor.valueOf(appabsence.getAllDayHalfDayLeaveAtr(),
				AllDayHalfDayLeaveAtr.class);
		if (allDayHaflDay.equals(AllDayHalfDayLeaveAtr.ALL_DAY_LEAVE)
				&& !holidayAppType.equals(HolidayAppType.SPECIAL_HOLIDAY)) {
			appContent += I18NText.getText("KAF018_279") + I18NText.getText("KAF018_248")
					+ I18NText.getText("CMM045_230", value);
		} else if (holidayAppType.equals(HolidayAppType.SPECIAL_HOLIDAY)) {
			// TODO
			// Pending
			appContent += I18NText.getText("KAF018_279") + I18NText.getText("KAF018_248");
			appContent += value;
		} else if (allDayHaflDay.equals(AllDayHalfDayLeaveAtr.HALF_DAY_LEAVE)) {
			appContent += I18NText.getText("KAF018_279") + I18NText.getText("KAF018_249");
			// 休暇申請.就業時間帯コード
			appContent += I18NText.getText("KAF018_230", appabsence.getWorkTimeName());
			appContent += appabsence.getStartTime1();
			appContent += (appabsence.getStartTime1().equals("") || appabsence.getEndTime1().equals("")) ? ""
					: I18NText.getText("KAF018_220");
			appContent += appabsence.getEndTime1();
			appContent += appabsence.getStartTime2();
			appContent += (appabsence.getStartTime2().equals("") || appabsence.getEndTime2().equals("")) ? ""
					: I18NText.getText("KAF018_220");
			appContent += appabsence.getEndTime2();
		}
		return appContent;
	}
	
	private String clockShorHm(Integer minute) {
		return (minute / 60 + ":" + (minute % 60 < 10 ? "0" + minute % 60 : minute % 60));
	}
}
