package nts.uk.ctx.at.request.app.find.application.holidayshipment;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationSettingDto;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.HolidayShipmentDto;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.absenceleaveapp.AbsenceLeaveAppDto;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.recruitmentapp.RecruitmentAppDto;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.BeforePreBootMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.init.ApplicationMetaOutput;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.init.DetailAppCommonSetService;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.DetailedScreenPreBootModeOutput;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.BeforePrelaunchAppCommonSet;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.compltleavesimmng.CompltLeaveSimMng;
import nts.uk.ctx.at.request.dom.application.holidayshipment.compltleavesimmng.CompltLeaveSimMngRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.compltleavesimmng.SyncState;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class HolidayShipmentScreenBFinder {
	@Inject
	private BeforePrelaunchAppCommonSet beforePrelaunchAppCommonSet;
	@Inject
	private DetailAppCommonSetService detailService;
	@Inject
	private BeforePreBootMode bootMode;
	@Inject
	private ApplicationSettingRepository appSetRepo;
	@Inject
	private AbsenceLeaveAppRepository absRepo;
	@Inject
	private RecruitmentAppRepository recRepo;
	@Inject
	private CompltLeaveSimMngRepository CompLeaveRepo;
	@Inject
	private ApplicationRepository_New appRepo;
	@Inject
	private HolidayShipmentScreenAFinder aFinder;
	@Inject
	private EmployeeRequestAdapter empAdaptor;

	/**
	 * find by Id
	 * 
	 * @param applicationID
	 */
	RecruitmentApp recApp;
	AbsenceLeaveApp absApp;
	ApplicationMetaOutput recAppOutput;
	ApplicationMetaOutput absAppOutput;
	String companyID;
	AppCommonSettingOutput appCommonSettingOutput;
	ApplicationType appType = ApplicationType.COMPLEMENT_LEAVE_APPLICATION;
	HolidayShipmentDto output;

	public HolidayShipmentDto findByID(String applicationID) {
		output = new HolidayShipmentDto();
		companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		boolean isRecApp = isRecApp(applicationID);
		// 1-1.新規画面起動前申請共通設定を取得する
		int rootAtr = 1;
		appCommonSettingOutput = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID, employeeID,
				rootAtr, appType, GeneralDate.today());
		output.setApplicationSetting(ApplicationSettingDto.convertToDto(appCommonSettingOutput.applicationSetting));
		// 入力者
		// 14-1.詳細画面起動前申請共通設定を取得する
		Optional<Application_New> appOutputOpt = appRepo.findByID(companyID, applicationID);
		// 14-2.詳細画面起動前モードの判断
		if (appOutputOpt.isPresent()) {
			Application_New appOutput = appOutputOpt.get();
			String employeeName = "";
			if (appOutput.getEmployeeID().equals(employeeID)) {
				employeeName = empAdaptor.getEmployeeName(employeeID);
			} else {

				employeeName = empAdaptor.getEmployeeName(appOutput.getEmployeeID()) + " (入力者 : "
						+ empAdaptor.getEmployeeName(employeeID) + ")";

			}

			output.setEmployeeName(employeeName);
			output.setApplication(ApplicationDto_New.fromDomain(appOutput));
			DetailedScreenPreBootModeOutput bootOutput = bootMode.judgmentDetailScreenMode(companyID, employeeID,
					applicationID, appOutput.getAppDate());

			if (bootOutput != null) {
				// 14-3.詳細画面の初期モード
				Optional<ApplicationSetting> appSetOpt = appSetRepo.getApplicationSettingByComID(companyID);
				if (appSetOpt.isPresent()) {
					ApplicationSetting appSet = appSetOpt.get();
					output.setApplicationSetting(ApplicationSettingDto.convertToDto(appSet));
				}
				if (isRecApp) {
					// 申請＝振出申請
					// アルゴリズム「振出申請に対応する振休情報の取得」を実行する
					getRecApp(applicationID);

				} else {
					// 申請＝振休申請
					// アルゴリズム「振休申請に対応する振出情報の取得」を実行する
					getAbsApp(applicationID);
				}
				// アルゴリズム「基準申請日の決定」を実行する

				GeneralDate recAppDate = recAppOutput != null ? recAppOutput.getAppDate() : null;
				GeneralDate absAppDate = absAppOutput != null ? absAppOutput.getAppDate() : null;
				String recWorkTypeCD = recApp != null ? recApp.getWorkTypeCD() : null;
				String absWorkTypeCD = absApp != null ? absApp.getWorkTypeCD() : null;
				String recWorkTimeCD = recApp != null ? recApp.getWorkTimeCD().v() : null;
				String absWorkTimeCD = absApp != null ? absApp.getWorkTimeCD().v() : null;
				GeneralDate refDate = HolidayShipmentScreenAFinder.DetRefDate(recAppDate, absAppDate);
				// アルゴリズム「振休振出申請起動時の共通処理」を実行する
				aFinder.commonProcessAtStartup(companyID, employeeID, refDate, recAppDate, recWorkTypeCD,
						recWorkTimeCD, absAppDate, absWorkTypeCD, absWorkTimeCD, output);

			}
		}

		return output;

	}

	private void getAbsApp(String applicationID) {
		// アルゴリズム「振休申請に同期された振出申請の取得」を実行する
		SyncState syncState = getCompltLeaveSimMngFromAbsID(applicationID);
		if (syncState.equals(SyncState.SYNCHRONIZING)) {
			// アルゴリズム「振休申請と関連付けた振出情報の取得」を実行する
			absApp.getSubDigestions().forEach(x -> {
				if (x.getPayoutMngDataID() != null) {
					// TODO Imported(就業.申請承認.休暇残数.振出振休)「振出情報」を取得する chưa có ai
					// làm domain 振出データID指定
				} else {
					// TODO Imported(就業.申請承認.休暇残数.振出振休)「振出情報」を取得する chưa có ai
					// làm domain 暫定振出管理データ

				}
			});

		}
	}

	private SyncState getCompltLeaveSimMngFromAbsID(String applicationID) {
		// ドメインモデル「振休振出同時申請管理」を1件取得する
		SyncState result = SyncState.ASYNCHRONOUS;
		Optional<CompltLeaveSimMng> CompltLeaveSimMngOpt = CompLeaveRepo.findByAbsID(applicationID);
		if (CompltLeaveSimMngOpt.isPresent()) {
			CompltLeaveSimMng compltLeaveSimMng = CompltLeaveSimMngOpt.get();
			result = compltLeaveSimMng.getSyncing();
			Optional<RecruitmentApp> recAppOpt = recRepo.findByID(compltLeaveSimMng.getRecAppID());
			if (recAppOpt.isPresent()) {
				setRecApp(recAppOpt.get());
			} else {

				throw new BusinessException("");
			}

		}
		return result;
	}

	private void getRecApp(String applicationID) {
		// アルゴリズム「振出申請に同期された振休申請の取得」を実行する
		SyncState syncState = getCompltLeaveSimMngFromRecID(applicationID);
		if (syncState.equals(SyncState.SYNCHRONIZING)) {
			// アルゴリズム「振出日に関連付いた振休情報の取得」を実行する
			// TODO chưa có ai làm domain 暫定振出管理データ
		}

	}

	private SyncState getCompltLeaveSimMngFromRecID(String applicationID) {
		// ドメインモデル「振休振出同時申請管理」を1件取得する
		SyncState result = SyncState.ASYNCHRONOUS;
		Optional<CompltLeaveSimMng> CompltLeaveSimMngOpt = CompLeaveRepo.findByRecID(applicationID);
		if (CompltLeaveSimMngOpt.isPresent()) {
			CompltLeaveSimMng compltLeaveSimMng = CompltLeaveSimMngOpt.get();
			result = compltLeaveSimMng.getSyncing();
			Optional<AbsenceLeaveApp> absAppOpt = absRepo.findByID(compltLeaveSimMng.getAbsenceLeaveAppID());
			if (absAppOpt.isPresent()) {
				setAbsApp(absAppOpt.get());

			} else {

				throw new BusinessException("");
			}

		}
		return result;

	}

	private boolean isRecApp(String applicationID) {
		boolean result = false;
		Optional<RecruitmentApp> recAppOpt = recRepo.findByID(applicationID);
		if (recAppOpt.isPresent()) {
			setRecApp(recAppOpt.get());

			result = true;

		} else {
			Optional<AbsenceLeaveApp> absAppOpt = absRepo.findByID(applicationID);
			if (absAppOpt.isPresent()) {
				setAbsApp(absAppOpt.get());
			}

		}
		return result;

	}

	private void setRecApp(RecruitmentApp recruitmentApp) {

		recApp = recruitmentApp;
		recAppOutput = detailService.getDetailAppCommonSet(companyID, recruitmentApp.getAppID());
		output.setRecApp(RecruitmentAppDto.fromDomain(recruitmentApp, recAppOutput.getAppDate()));

	}

	private void setAbsApp(AbsenceLeaveApp absenceLeaveApp) {
		absApp = absenceLeaveApp;
		absAppOutput = detailService.getDetailAppCommonSet(companyID, absenceLeaveApp.getAppID());
		output.setAbsApp(AbsenceLeaveAppDto.fromDomain(absenceLeaveApp, absAppOutput.getAppDate()));

	}
}
