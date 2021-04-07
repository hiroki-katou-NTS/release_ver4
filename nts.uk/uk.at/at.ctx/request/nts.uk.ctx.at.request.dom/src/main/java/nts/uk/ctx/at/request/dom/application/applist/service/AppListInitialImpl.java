package nts.uk.ctx.at.request.dom.application.applist.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.ApprovalDevice;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.AppListExtractCondition;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.ApplicationListAtr;
import nts.uk.ctx.at.request.dom.application.applist.service.datacreate.AppDataCreation;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppDetailInfoRepository;
import nts.uk.ctx.at.request.dom.application.applist.service.param.AppListInfo;
import nts.uk.ctx.at.request.dom.application.applist.service.param.AppListInitOutput;
import nts.uk.ctx.at.request.dom.application.applist.service.param.ListOfApplication;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.AtEmploymentAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.SyEmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.EmploymentHisImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SyEmployeeImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.closure.PresentClosingPeriodImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.closure.RqClosureAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.AgentAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentDataRequestPubImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalBehaviorAtrImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalFrameImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpInfo;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkPlaceHistBySIDImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.ActualStatusCheckResult;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.OvertimeColorCheck;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.PreActualColorCheck;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AppCompltLeaveSyncOutput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeAppAtr;
import nts.uk.ctx.at.request.dom.application.stamp.StampRequestMode;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.OvertimeAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.OvertimeAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.CalcStampMiss;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.OverrideSet;
import nts.uk.ctx.at.request.dom.setting.workplace.appuseset.ApplicationUseSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.requestbycompany.RequestByCompany;
import nts.uk.ctx.at.request.dom.setting.workplace.requestbycompany.RequestByCompanyRepository;
import nts.uk.ctx.at.request.dom.setting.workplace.requestbyworkplace.RequestByWorkplace;
import nts.uk.ctx.at.request.dom.setting.workplace.requestbyworkplace.RequestByWorkplaceRepository;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrame;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameRepository;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrameRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
//import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
//import nts.uk.ctx.at.shared.dom.workrule.closure.CurrentMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class AppListInitialImpl implements AppListInitialRepository{

	@Inject
	private ClosureRepository repoClosure;
	
	@Inject
	private RequestByWorkplaceRepository repoRequestWkp;
	
	@Inject
	private RequestByCompanyRepository repoRequestCompany;
	
	@Inject
	private ApplicationRepository repoApp;
	
	@Inject
	private WorkplaceAdapter wkpAdapter;
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Inject
	private AppDetailInfoRepository repoAppDetail;
	
	@Inject
	private AgentAdapter agentAdapter;
	
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	
	@Inject
	private ClosureEmploymentRepository closureEmpRepo;
	
	@Inject
	private RqClosureAdapter closureAdapter;
	
	@Inject
	private AtEmploymentAdapter employmentAdapter;
	
	@Inject
	private SyEmployeeAdapter syEmpAdapter;
	
	@Inject
	private PreActualColorCheck preActualCheck;
	
	private HolidayWorkAppSetRepository withdrawalAppSetRepo;
	@Inject
	private OvertimeAppSetRepository appOtSetRepo;
	
	@Inject
	private OvertimeWorkFrameRepository repoOverTimeFr;
	
	@Inject
	private WorkdayoffFrameRepository repoWork;
	
	
	@Inject
	private AppDataCreation appDataCreation;

	/**
	 * 0 - 申請一覧事前必須チェック
	 */
	@Override
	public Boolean checkAppPredictRequire(int appType, String wkpID, String companyId) {
//		//申請種類-(Check AppType)
//		if (appType != 0 && appType != 6) {//「休出時間申請」又は「残業申請」以外の場合
//			return false;
//		}
//		//「休出時間申請」又は「残業申請」の場合
//		//ドメイン「職場別申請承認設定」を取得する-(lấy dữ liệu domain Application approval setting by workplace)
//		Optional<ApprovalFunctionSetting> appFuncSet = null;
//		appFuncSet = repoRequestWkp.getFunctionSetting(companyId, wkpID, appType);
//		// 対象が存在しない場合 - TH doi tuong k ton tai
//		if (!appFuncSet.isPresent()
//				|| appFuncSet.get().getInstructionUseSetting().getInstructionUseDivision().equals(UseAtr.NOTUSE)) {
//			//ドメイン「会社別申請承認設定」を取得する-(lấy dữ liệu domain Application approval setting by company)
//			appFuncSet = repoRequestCompany.getFunctionSetting(companyId, appType);
//		}
//		if (!appFuncSet.isPresent()
//				|| appFuncSet.get().getInstructionUseSetting().getInstructionUseDivision().equals(UseAtr.NOTUSE)) {
//			return false;
//		}
//		//申請承認機能設定.残業申請の事前必須設定
//		if (appFuncSet.get().getOvertimeAppSetting().equals(SettingFlg.SETTING)) {
//			return true;
//		} else {
//			return false;
//		}
		return null;
	}

	/**
	 * 1 - 申請一覧リスト取得
	 */
	@Override
	public AppListInitOutput getApplicationList(AppListExtractCondition param, int device, AppListInfo appListInfo) {
		//申請一覧区分が申請 OR 承認-(Check xem là application hay aprove)
		if (param.getAppListAtr().equals(ApplicationListAtr.APPLICATION)) {//申請
			//アルゴリズム「申請一覧リスト取得申請」を実行する - 2
			appListInfo = this.getApplicationListByApp(param, device, appListInfo);
		} else {//承認
				//アルゴリズム「申請一覧リスト取得承認」を実行する - 3
			appListInfo = this.getAppListByApproval(param, device, appListInfo);
		}
		// 各申請の申請名称を「申請一覧抽出条件.申請種類リスト」より取得する
		param.setOpAppTypeLst(param.getOpListOfAppTypes());
		// 取得した一覧の申請種類(単一化）で申請一覧抽出条件.申請種類を作成する
		// thêm select all ở dropdownlist, xử lý ở UI
		return new AppListInitOutput(param, appListInfo);
	}

	/**
	 * 2 - 申請一覧リスト取得申請
	 */
	@Override
	public AppListInfo getApplicationListByApp(AppListExtractCondition param, int device, AppListInfo appListInfo) {
		String companyID = AppContexts.user().companyId();
		// アルゴリズム「申請一覧対象申請者取得」を実行する
		ListApplicantOutput checkMySelf = this.getListApplicantForListApp(param);
		List<Application> appLst = new ArrayList<>();
		boolean condition = param.getOpListOfAppTypes().map(x -> {
			return !x.stream().filter(y -> !y.isChoice()).findAny().isPresent();
		}).orElse(true);
		List<PrePostAtr> prePostAtrLst = new ArrayList<>();
		if(param.isPreOutput()) {
			prePostAtrLst.add(PrePostAtr.PREDICT);
		}
		if(param.isPostOutput()) {
			prePostAtrLst.add(PrePostAtr.POSTERIOR);
		}
		if(condition) {
			// ドメインモデル「申請」を取得する
			List<ApplicationType> allAppTypeLst = new ArrayList<>();
			for(ApplicationType appType : ApplicationType.values()) {
				if(appType==ApplicationType.STAMP_APPLICATION) {
					continue;
				}
				if(appType==ApplicationType.OVER_TIME_APPLICATION) {
					continue;
				}
				allAppTypeLst.add(appType);
			} 
			List<StampRequestMode> stampRequestModeLst = new ArrayList<>();
			for(StampRequestMode stampRequestMode : StampRequestMode.values()) {
				stampRequestModeLst.add(stampRequestMode);
			}
			List<OvertimeAppAtr> overtimeAppAtrLst = new ArrayList<>();
			for(OvertimeAppAtr overtimeAppAtr : OvertimeAppAtr.values()) {
				overtimeAppAtrLst.add(overtimeAppAtr);
			}
			appLst = repoApp.getByAppTypeList(checkMySelf.getLstSID(), param.getPeriodStartDate(), param.getPeriodEndDate(), 
					allAppTypeLst, prePostAtrLst, stampRequestModeLst, overtimeAppAtrLst);
		} else {
			// ドメインモデル「申請」を取得する
			List<ApplicationType> appTypeLst = param.getOpListOfAppTypes().map(x -> {
				return x.stream().filter(y -> y.isChoice() && y.getAppType()!=ApplicationType.STAMP_APPLICATION && y.getAppType()!=ApplicationType.OVER_TIME_APPLICATION)
						.map(y -> y.getAppType()).collect(Collectors.toList());
			}).orElse(Collections.emptyList());
			List<StampRequestMode> stampRequestModeLst = param.getOpListOfAppTypes().map(x -> {
				return x.stream().filter(y -> y.isChoice() && y.getAppType()==ApplicationType.STAMP_APPLICATION)
						.map(y -> {
							if(y.getOpApplicationTypeDisplay().get()==ApplicationTypeDisplay.STAMP_ADDITIONAL) {
								return StampRequestMode.STAMP_ADDITIONAL;
							} else {
								return StampRequestMode.STAMP_ONLINE_RECORD;
							}
						}).collect(Collectors.toList());
			}).orElse(Collections.emptyList());
			List<OvertimeAppAtr> overtimeAppAtrLst = param.getOpListOfAppTypes().map(x -> {
				return x.stream().filter(y -> y.isChoice() && y.getAppType()==ApplicationType.OVER_TIME_APPLICATION)
						.map(y -> {
							if(y.getOpApplicationTypeDisplay().get()==ApplicationTypeDisplay.EARLY_OVERTIME) {
								return OvertimeAppAtr.EARLY_OVERTIME;
							} else if(y.getOpApplicationTypeDisplay().get()==ApplicationTypeDisplay.NORMAL_OVERTIME) {
								return OvertimeAppAtr.NORMAL_OVERTIME;
							} else {
								return OvertimeAppAtr.EARLY_NORMAL_OVERTIME;
							}
						}).collect(Collectors.toList());
			}).orElse(Collections.emptyList());
			appLst = repoApp.getByAppTypeList(checkMySelf.getLstSID(), param.getPeriodStartDate(), param.getPeriodEndDate(), 
					appTypeLst, prePostAtrLst, stampRequestModeLst, overtimeAppAtrLst);
		}
		// 承認ルートの内容取得
		Map<String,List<ApprovalPhaseStateImport_New>> mapResult = approvalRootStateAdapter
				.getApprovalPhaseByID(appLst.stream().map(x -> x.getAppID()).collect(Collectors.toList()));
		// 申請一覧リストのデータを作成
		appListInfo = appDataCreation.createAppLstData(
				companyID, 
				appLst, 
				new DatePeriod(param.getPeriodStartDate(), param.getPeriodEndDate()), 
				ApplicationListAtr.APPLICATION, 
				mapResult, 
				device, 
				param, 
				appListInfo);
		return appListInfo;
		
		/*
		
		// ドメインモデル「申請」を取得する-(Lấy dữ liệu domain Application) - get List
		// Application By SID
		List<Application_New> lstApp = new ArrayList<>();
		// 2018/11/02 EA2892(#102518)
		// ・社員指定ができるため代行申請分の表示は不要
		// 「自分の申請」に関する条件も不要
		// if(checkMySelf.isMySelf()){//【自分の申請＝Trueの場合】
		// ・申請者ID＝社員ID（リスト） または 入力者ID＝社員ID（リスト）
		// lstApp = repoApp.getByListSID(companyId, checkMySelf.getLstSID(),
		// param.getStartDate(), param.getEndDate());
		// }else{
		// ・申請者ID＝社員ID（リスト）
		if (device == PC || lstAppType.isEmpty()) {
			Integer[] data = { 0, 1, 2, 4, 6, 10 };
			lstAppType = Arrays.asList(data);
		}
		lstApp = repoApp.getByListApplicant(companyId, checkMySelf.getLstSID(), param.getStartDate(),
				param.getEndDate(), lstAppType);
		// }
		List<Application_New> lstOverTime = lstApp.stream().filter(c -> c.isAppOverTime()).collect(Collectors.toList());
		List<Application_New> lstGoBack = lstApp.stream().filter(d -> d.isAppGoBack()).collect(Collectors.toList());
		List<Application_New> lstHdWork = lstApp.stream().filter(d -> d.isAppHdWork()).collect(Collectors.toList());
		List<Application_New> lstWkChange = lstApp.stream().filter(d -> d.isAppWkChange()).collect(Collectors.toList());
		List<Application_New> lstAbsence = lstApp.stream().filter(d -> d.isAppAbsence()).collect(Collectors.toList());
		List<Application_New> lstCompltLeave = lstApp.stream().filter(d -> d.isAppCompltLeave())
				.collect(Collectors.toList());

		List<AppOverTimeInfoFull> lstAppOt = new ArrayList<>();
		List<AppGoBackInfoFull> lstAppGoBack = new ArrayList<>();
		List<AppHolidayWorkFull> lstAppHdWork = new ArrayList<>();
		List<AppWorkChangeFull> lstAppWkChange = new ArrayList<>();
		List<AppAbsenceFull> lstAppAbsence = new ArrayList<>();
		List<AppCompltLeaveSync> lstAppCompltLeaveSync = new ArrayList<>();

		List<Application_New> lstAppFilter = new ArrayList<>();
		List<WorkType> lstWkType = new ArrayList<>();
		List<WorkTimeSetting> lstWkTime = new ArrayList<>();
		if (device == PC) {
			// Lay List workType/workTime
			// Chi 6,7,10,11 moi su dung den workType va workTime
			if (!lstAbsence.isEmpty() || !lstGoBack.isEmpty() || !lstWkChange.isEmpty() || !lstCompltLeave.isEmpty()) {
				lstWkType = repoWorkType.findListByCid(companyId);
			}
			if (!lstAbsence.isEmpty() || !lstGoBack.isEmpty() || !lstWkChange.isEmpty()) {
				lstWkTime = repoworkTime.findByCId(companyId);
			}
			// 残業申請: get full info (0)
			List<String> lstAppOtID = lstOverTime.stream().map(c -> c.getAppID()).collect(Collectors.toList());
			if (!lstAppOtID.isEmpty()) {
				lstAppOt = repoAppDetail.getListAppOverTimeInfo(companyId, lstAppOtID);
			}
			// 直行直帰申請: get full info(4)
			List<String> lstAppGoBackID = lstGoBack.stream().map(c -> c.getAppID()).collect(Collectors.toList());
			if (!lstAppGoBackID.isEmpty()) {
				lstAppGoBack = repoAppDetail.getListAppGoBackInfo(companyId, lstAppGoBackID);
			}
			// 休日出勤時間申請: get full info(6);
			List<String> lstAppHdID = lstHdWork.stream().map(c -> c.getAppID()).collect(Collectors.toList());
			if (!lstAppHdID.isEmpty()) {
				lstAppHdWork = repoAppDetail.getListAppHdWorkInfo(companyId, lstAppHdID, lstWkType, lstWkTime);
			}
			// 勤務変更申請: get full info(2);
			List<String> lstAppWkChangeID = lstWkChange.stream().map(c -> c.getAppID()).collect(Collectors.toList());
			if (!lstAppWkChangeID.isEmpty()) {
				lstAppWkChange = repoAppDetail.getListAppWorkChangeInfo(companyId, lstAppWkChangeID, lstWkType,
						lstWkTime);
			}
			// 休暇申請: get full info(1);
			for (Application_New app : lstAbsence) {
				Integer day = 0;
				if (app.getStartDate().isPresent() && app.getEndDate().isPresent()) {
					day = app.getStartDate().get().daysTo(app.getEndDate().get()) + 1;
				}
				AppAbsenceFull appAbsence = repoAppDetail.getAppAbsenceInfo(companyId, app.getAppID(), day, lstWkType,
						lstWkTime);
				lstAppAbsence.add(appAbsence);
			}
			// 振休振出申請: get full info(10);
			List<String> lstSyncId = new ArrayList<>();
			for (Application_New app : lstCompltLeave) {
				if (lstSyncId.contains(app.getAppID())) {
					continue;
				}
				AppCompltLeaveFull appMain = null;
				AppCompltLeaveFull appSub = null;
				String appDateSub = null;
				String appInputSub = null;
				// アルゴリズム「申請一覧リスト取得振休振出」を実行する-(get List App Complement Leave): 6 - 申請一覧リスト取得振休振出
				AppCompltLeaveSyncOutput sync = this.getListAppComplementLeave(app, companyId);
				if (!sync.isSync()) {// TH k co don lien ket lay thong tin chi tiet
					appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType(), lstWkType);
				} else {// TH co don lien ket lay thong tin chi tiet A
					appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType(), lstWkType);
					// check B co trong list don xin k?
					String appIdSync = sync.getType() == 0 ? sync.getRecId() : sync.getAbsId();
					CheckExitSync checkExit = this.checkExitSync(lstCompltLeave, appIdSync);
					if (checkExit.isCheckExit()) {// exist
						lstSyncId.add(appIdSync);
						appDateSub = checkExit.getAppDateSub().toString("yyyy/MM/dd");
						appInputSub = checkExit.getInputDateSub().toString("yyyy/MM/dd HH:mm");
					} else {// not exist lay thong tin chung
						Application sub = repoApp.findByID(companyId, appIdSync).get();
						appDateSub = sub.getAppDate().getApplicationDate().toString("yyyy/MM/dd");
						appInputSub = sub.getInputDate().toString("yyyy/MM/dd HH:mm");
					}
					appSub = repoAppDetail.getAppCompltLeaveInfo(companyId, appIdSync, sync.getType() == 0 ? 1 : 0,
							lstWkType);
				}

				lstAppCompltLeaveSync.add(new AppCompltLeaveSync(sync.getType(), sync.isSync(), appMain, appSub,
						appDateSub, appInputSub));
			}
			List<Application_New> lstCompltSync = lstCompltLeave.stream().filter(c -> !lstSyncId.contains(c.getAppID()))
					.collect(Collectors.toList());
			lstAppFilter.addAll(lstOverTime);
			lstAppFilter.addAll(lstGoBack);
			lstAppFilter.addAll(lstHdWork);
			lstAppFilter.addAll(lstWkChange);
			lstAppFilter.addAll(lstAbsence);
			lstAppFilter.addAll(lstCompltSync);
		} else {
			lstAppFilter.addAll(lstApp);
		}
		// for (Application_New application : lstApp) {
		// アルゴリズム「申請一覧リスト取得打刻取消」を実行する-(Cancel get list app stamp): 7 -
		// 申請一覧リスト取得打刻取消
		// this.getListAppStampIsCancel(application);
		// アルゴリズム「申請一覧リスト取得振休振出」を実行する-(get List App Complement Leave): 6 -
		// 申請一覧リスト取得振休振出
		// this.getListAppComplementLeave(application);
		// アルゴリズム「申請一覧リスト取得休暇」を実行する-(get List App Absence): 8 - 申請一覧リスト取得休暇
		// this.getListAppAbsence(application);
		// }
		// imported(申請承認）「稟議書」を取得する - wait
		// アルゴリズム「申請一覧リスト取得マスタ情報」を実行する(get List App Master Info): 9 -
		// 申請一覧リスト取得マスタ情報
		DataMasterOutput lstAppMasterInfo = this.getListAppMasterInfo(lstAppFilter, companyId,
				new DatePeriod(param.getStartDate(), param.getEndDate()), device);
		List<ContentApp> lstContentApp = new ArrayList<>();
		if (device == PC) {
			lstContentApp = this.crateContentApp(lstAppFilter, param.getAppListAtr(), appReasonDisAtr, lstAppOt,
					lstAppAbsence, lstAppWkChange, lstAppGoBack, lstAppHdWork, lstAppCompltLeaveSync,
					Collections.emptyList(), lstAppMasterInfo.getLstAppMasterInfo(), lstWkType, lstWkTime);
		}

		return new AppListOutPut(lstAppMasterInfo, lstAppFilter, null, null, null, null, null, lstContentApp,
				lstAppCompltLeaveSync);// NOTE
		*/
	}

	/**
	 * 2.1 - 申請一覧対象申請者取得
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public ListApplicantOutput getListApplicantForListApp(AppListExtractCondition param) {
		String sIdLogin = AppContexts.user().employeeId();
		// 自分の申請＝False（初期状態）
		boolean mySelf = false;
		List<String> lstSID = new ArrayList<>();
		// 申請一覧抽出条件. 社員IDリストを確認
		if (!param.getOpListEmployeeID().isPresent() || CollectionUtil.isEmpty(param.getOpListEmployeeID().get())) {// リストが存在しない場合
			// 社員IDリストにログイン者の社員IDをセットする
			lstSID.add(sIdLogin);
			// 自分の申請＝True
			mySelf = true;
		} else {// リストが存在する場合
				// 社員IDリストが１件でログイン者IDの場合
			if (param.getOpListEmployeeID().get().size() == 1 && param.getOpListEmployeeID().get().get(0).equals(sIdLogin)) {// 1件でログイン者IDだった場合
				// 自分の申請と判断する
				// 自分の申請＝True
				mySelf = true;
			}
			lstSID = param.getOpListEmployeeID().get();
		}
		return new ListApplicantOutput(mySelf, lstSID);
	}

	/**
	 * 3 - 申請一覧リスト取得承認
	 */
	@Override
	public AppListInfo getAppListByApproval(AppListExtractCondition param, int device, AppListInfo appListInfo) {
		// 承認区分から承認ルートを取得
		Map<String, List<ApprovalPhaseStateImport_New>> mapApprInfo = this.mergeAppAndPhase_New(
			new DatePeriod(param.getPeriodStartDate(), param.getPeriodEndDate()), 
			param.getOpUnapprovalStatus().orElse(false),
			param.getOpApprovalStatus().orElse(false), 
			param.getOpDenialStatus().orElse(false), 
			param.getOpAgentApprovalStatus().orElse(false), 
			param.getOpRemandStatus().orElse(false),
			param.getOpCancelStatus().orElse(false),
			AppContexts.user().employeeId(),
			Collections.emptyList());
		// 取得したドメインモデル「承認ルートインスタンス」をチェック
		if(mapApprInfo.isEmpty()) {
			return appListInfo;
		}
		// 承認一覧の申請を取得
		String companyID = AppContexts.user().companyId();
		List<PrePostAtr> prePostAtrLst = new ArrayList<>();
		if(param.isPreOutput()) {
			prePostAtrLst.add(PrePostAtr.PREDICT);
		}
		if(param.isPostOutput()) {
			prePostAtrLst.add(PrePostAtr.POSTERIOR);
		}
		List<Integer> appTypeLst = param.getOpListOfAppTypes().map(x -> {
			return x.stream().filter(y -> y.isChoice() && y.getAppType()!=ApplicationType.STAMP_APPLICATION && y.getAppType()!=ApplicationType.OVER_TIME_APPLICATION)
					.map(y -> y.getAppType().value).collect(Collectors.toList());
		}).orElse(Collections.emptyList());
		List<StampRequestMode> stampRequestModeLst = param.getOpListOfAppTypes().map(x -> {
			return x.stream().filter(y -> y.isChoice() && y.getAppType()==ApplicationType.STAMP_APPLICATION)
					.map(y -> {
						if(y.getOpApplicationTypeDisplay().get()==ApplicationTypeDisplay.STAMP_ADDITIONAL) {
							return StampRequestMode.STAMP_ADDITIONAL;
						} else {
							return StampRequestMode.STAMP_ONLINE_RECORD;
						}
					}).collect(Collectors.toList());
		}).orElse(Collections.emptyList());
		List<OvertimeAppAtr> overtimeAppAtrLst = param.getOpListOfAppTypes().map(x -> {
			return x.stream().filter(y -> y.isChoice() && y.getAppType()==ApplicationType.OVER_TIME_APPLICATION)
					.map(y -> {
						if(y.getOpApplicationTypeDisplay().get()==ApplicationTypeDisplay.EARLY_OVERTIME) {
							return OvertimeAppAtr.EARLY_OVERTIME;
						} else if(y.getOpApplicationTypeDisplay().get()==ApplicationTypeDisplay.NORMAL_OVERTIME) {
							return OvertimeAppAtr.NORMAL_OVERTIME;
						} else {
							return OvertimeAppAtr.EARLY_NORMAL_OVERTIME;
						}
					}).collect(Collectors.toList());
		}).orElse(Collections.emptyList());
		List<Application> lstApp = repoApp.getListAppModeApprCMM045(
				companyID, 
				new DatePeriod(param.getPeriodStartDate(), param.getPeriodEndDate()),
				new ArrayList<String>(mapApprInfo.keySet()), 
				param.getOpUnapprovalStatus().orElse(false),
				param.getOpApprovalStatus().orElse(false), 
				param.getOpDenialStatus().orElse(false), 
				param.getOpAgentApprovalStatus().orElse(false), 
				param.getOpRemandStatus().orElse(false),
				param.getOpCancelStatus().orElse(false), 
				appTypeLst,
				prePostAtrLst,
				param.getOpListEmployeeID().isPresent() ? param.getOpListEmployeeID().get() : Collections.emptyList(),
				stampRequestModeLst,
				overtimeAppAtrLst);
		// 申請一覧リストのデータを作成
		appListInfo = appDataCreation.createAppLstData(
				companyID, 
				lstApp, 
				new DatePeriod(param.getPeriodStartDate(), param.getPeriodEndDate()), 
				ApplicationListAtr.APPROVER, 
				mapApprInfo, 
				device, 
				param, 
				appListInfo);
		return appListInfo;
//		String companyId = AppContexts.user().companyId();
//		String sID = AppContexts.user().employeeId();
//		GeneralDate sysDate = GeneralDate.today();
//		// 申請一覧抽出条件.申請表示対象が「事前通知」または「検討指示」が指定
//		// ドメインモデル「代行者管理」を取得する-(Lấy dữ liệu domain 代行者管理) - wait request 244
//		List<AgentDataRequestPubImport> lstAgent = agentAdapter.lstAgentData(companyId, sID, sysDate, sysDate);
//		List<String> lstEmp = new ArrayList<>();
//		for (AgentDataRequestPubImport agent : lstAgent) {
//			lstEmp.add(agent.getEmployeeId());
//		}
//		List<ApplicationFullOutput> lstAppFull = new ArrayList<>();
//		// hoatt 2019.07.24
//		// #108408 レスポンス対応
//		// imported（申請承認）「承認ルートの内容」を取得する - RequestList309

//		List<String> lstAppId = new ArrayList<>();
//		for (Map.Entry<String, List<ApprovalPhaseStateImport_New>> entry : mapApprInfo.entrySet()) {
//			lstAppId.add(entry.getKey());
//		}
//		// ドメインモデル「申請」を取得する-(Lấy dữ liệu domain 申請) - get List App By Reflect
//		if (device == PC || lstAppType.isEmpty()) {
//			Integer[] data = { 0, 1, 2, 4, 6, 10 };
//			lstAppType = Arrays.asList(data);
//		}
//		List<Application_New> lstApp = new ArrayList<>();
//		lstApp = repoApp.getListAppModeApprCMM045(companyId, new DatePeriod(param.getStartDate(), param.getEndDate()),
//				lstAppId, param.isUnapprovalStatus(), param.isApprovalStatus(), param.isDenialStatus(),
//				param.isAgentApprovalStatus(), param.isRemandStatus(), param.isCancelStatus(), lstAppType);
//		for (Application_New app : lstApp) {
//			lstAppFull.add(new ApplicationFullOutput(app, mapApprInfo.get(app.getAppID()), -1, "", null, null));
//		}
//		// loc du lieu
//		// 条件１： ログイン者の表示対象の基本条件
//		List<ApplicationFullOutput> lstAppFullFil1 = new ArrayList<>();
//		for (ApplicationFullOutput appFull : lstAppFull) {
//			ApproverStt appstt = this.filterConditions1(appFull, lstAgent, sID);
//			if (appstt.isCheck()) {
//				appFull.setApprId(appstt.getApprId());
//				lstAppFullFil1.add(appFull);
//			}
//		}
//
//		// 条件３：承認区分の指定条件
//		List<Application_New> lstAppFilter3 = new ArrayList<>();
//		List<ApplicationFullOutput> lstAppFullFilter3 = new ArrayList<>();
//		List<String> lstFrameUn = new ArrayList<>();
//		List<PhaseStatus> lstPhaseStatus = new ArrayList<>();
//
//		// Replace lstAppFilter2 -> lstAppFullFil1: EA2237
//		for (ApplicationFullOutput appFull : lstAppFullFil1) {
//			PhaseFrameStatus status = this.check3(param, appFull);
//			if(status != null) {
//				appFull.setStatusA(status);
//				PhaseStatus statusPhase = this.convertStatusPhase(appFull.getApplication().getAppID(),
//						appFull.getLstPhaseState());
//					lstAppFilter3.add(appFull.getApplication());
//					lstAppFullFilter3.add(appFull);
//					if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {
//						lstFrameUn.add(appFull.getApplication().getAppID());
//					}
//
//					lstPhaseStatus.add(statusPhase);
//				}
//		}
//
//		// 条件５：重複承認の対応条件
//		List<Application_New> lstOverTime = lstAppFilter3.stream().filter(c -> c.isAppOverTime())
//				.collect(Collectors.toList());
//		List<Application_New> lstGoBack = lstAppFilter3.stream().filter(d -> d.isAppGoBack())
//				.collect(Collectors.toList());
//		List<Application_New> lstHdWork = lstAppFilter3.stream().filter(d -> d.isAppHdWork())
//				.collect(Collectors.toList());
//		List<Application_New> lstWkChange = lstAppFilter3.stream().filter(d -> d.isAppWkChange())
//				.collect(Collectors.toList());
//		List<Application_New> lstAbsence = lstAppFilter3.stream().filter(d -> d.isAppAbsence())
//				.collect(Collectors.toList());
//		List<Application_New> lstCompltLeave = lstAppFilter3.stream().filter(d -> d.isAppCompltLeave())
//				.collect(Collectors.toList());
//
//		List<AppOverTimeInfoFull> lstAppOt = new ArrayList<>();
//		List<AppGoBackInfoFull> lstAppGoBack = new ArrayList<>();
//		List<AppHolidayWorkFull> lstAppHdWork = new ArrayList<>();
//		List<AppWorkChangeFull> lstAppWorkChange = new ArrayList<>();
//		List<AppAbsenceFull> lstAppAbsence = new ArrayList<>();
//		List<AppCompltLeaveSync> lstAppCompltLeaveSync = new ArrayList<>();
//
//		// Lay List workType/workTime
//		List<WorkType> lstWkType = new ArrayList<>();
//		List<WorkTimeSetting> lstWkTime = new ArrayList<>();
//		List<Application_New> lstAppFilter = new ArrayList<>();
//		AppListAtrOutput timeOutput = new AppListAtrOutput();
//		if (device == PC) {
//			// Chi 6,7,10,11 moi su dung den workType va workTime
//			if (!lstAbsence.isEmpty() || !lstGoBack.isEmpty() || !lstWkChange.isEmpty() || !lstCompltLeave.isEmpty()) {
//				lstWkType = repoWorkType.findListByCid(companyId);
//			}
//			if (!lstAbsence.isEmpty() || !lstGoBack.isEmpty() || !lstWkChange.isEmpty()) {
//				lstWkTime = repoworkTime.findByCId(companyId);
//			}
//			// get info full
//			// 残業申請: get full info (0)
//			List<String> lstAppOtID = lstOverTime.stream().map(c -> c.getAppID()).collect(Collectors.toList());
//			if (!lstAppOtID.isEmpty()) {
//				lstAppOt = repoAppDetail.getListAppOverTimeInfo(companyId, lstAppOtID);
//			}
//			// 直行直帰申請: get full info (4)
//			List<String> lstAppGoBackID = lstGoBack.stream().map(c -> c.getAppID()).collect(Collectors.toList());
//			if (!lstAppGoBackID.isEmpty()) {
//				lstAppGoBack = repoAppDetail.getListAppGoBackInfo(companyId, lstAppGoBackID);
//			}
//			// 休日出勤時間申請: get full info(6);
//			List<String> lstAppHdID = lstHdWork.stream().map(c -> c.getAppID()).collect(Collectors.toList());
//			if (!lstAppHdID.isEmpty()) {
//				lstAppHdWork = repoAppDetail.getListAppHdWorkInfo(companyId, lstAppHdID, lstWkType, lstWkTime);
//			}
//			// 勤務変更申請: get full info (2)
//			List<String> lstAppWkChangeID = lstWkChange.stream().map(c -> c.getAppID()).collect(Collectors.toList());
//			if (!lstAppWkChangeID.isEmpty()) {
//				lstAppWorkChange = repoAppDetail.getListAppWorkChangeInfo(companyId, lstAppWkChangeID, lstWkType,
//						lstWkTime);
//			}
//			// 休暇申請: get full info (1)
//			for (Application_New app : lstAbsence) {
//				Integer day = 0;
//				if (app.getStartDate().isPresent() && app.getEndDate().isPresent()) {
//					day = app.getStartDate().get().daysTo(app.getEndDate().get()) + 1;
//				}
//				AppAbsenceFull appAbsence = repoAppDetail.getAppAbsenceInfo(companyId, app.getAppID(), day, lstWkType,
//						lstWkTime);
//				lstAppAbsence.add(appAbsence);
//			}
//			// 振休振出申請: get full info(10);
//			List<String> lstSyncId = new ArrayList<>();
//			for (Application_New app : lstCompltLeave) {
//				if (lstSyncId.contains(app.getAppID())) {
//					continue;
//				}
//				AppCompltLeaveFull appMain = null;
//				AppCompltLeaveFull appSub = null;
//				String appDateSub = null;
//				String appInputSub = null;
//				// アルゴリズム「申請一覧リスト取得振休振出」を実行する-(get List App Complement Leave): 6 - 申請一覧リスト取得振休振出
//				AppCompltLeaveSyncOutput sync = this.getListAppComplementLeave(app, companyId);
//				if (!sync.isSync()) {// TH k co don lien ket
//					// lay thong tin chi tiet
//					appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType(), lstWkType);
//				} else {// TH co don lien ket
//						// lay thong tin chi tiet A
//					appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType(), lstWkType);
//					// check B co trong list don xin k?
//					String appIdSync = sync.getType() == 0 ? sync.getRecId() : sync.getAbsId();
//					CheckExitSync checkExit = this.checkExitSync(lstCompltLeave, appIdSync);
//					if (checkExit.isCheckExit()) {// exist
//						lstSyncId.add(appIdSync);
//						appDateSub = checkExit.getAppDateSub().toString("yyyy/MM/dd");
//						appInputSub = checkExit.getInputDateSub().toString("yyyy/MM/dd HH:mm");
//					} else {// not exist
//							// lay thong tin chung
//						Application sub = repoApp.findByID(companyId, appIdSync).get();
//						appDateSub = sub.getAppDate().getApplicationDate().toString("yyyy/MM/dd");
//						appInputSub = sub.getInputDate().toString("yyyy/MM/dd HH:mm");
//					}
//					appSub = repoAppDetail.getAppCompltLeaveInfo(companyId, appIdSync, sync.getType() == 0 ? 1 : 0,
//							lstWkType);
//				}
//				lstAppCompltLeaveSync.add(new AppCompltLeaveSync(sync.getType(), sync.isSync(), appMain, appSub,
//						appDateSub, appInputSub));
//			}
//			List<Application_New> lstCompltSync = lstCompltLeave.stream().filter(c -> !lstSyncId.contains(c.getAppID()))
//					.collect(Collectors.toList());
//			lstAppFilter = lstOverTime;
//			lstAppFilter.addAll(lstGoBack);
//			lstAppFilter.addAll(lstHdWork);
//			lstAppFilter.addAll(lstWkChange);
//			lstAppFilter.addAll(lstAbsence);
//			lstAppFilter.addAll(lstCompltSync);
//			// アルゴリズム「申請一覧リスト取得出張」を実行する
//			// TODO Auto-generated method stub
//			// アルゴリズム「申請一覧リスト取得実績」を実行する-(get App List Achievement): 5 - 申請一覧リスト取得実績
//			// loai bo nhung don dong bo
//			List<ApplicationFullOutput> lstCount = lstAppFullFilter3.stream()
//					.filter(c -> !lstSyncId.contains(c.getApplication().getAppID())).collect(Collectors.toList());
//			List<AppCompltLeaveSync> lstSync = lstAppCompltLeaveSync.stream().filter(c -> c.isSync())
//					.collect(Collectors.toList());
//			timeOutput = this.getAppListAchievement(lstCount, lstAppOt, lstAppHdWork, displaySet, companyId, sID,
//					lstSync, lstWkType, lstWkTime, device);
//		} else {
//			lstAppFilter.addAll(lstAppFilter3);
//			timeOutput = this.getAppListAchievement(lstAppFullFilter3, lstAppOt, lstAppHdWork, displaySet, companyId,
//					sID, new ArrayList<>(), lstWkType, lstWkTime, device);
//		}
//
//		// imported(申請承認）「稟議書」を取得する - wait request : return list app - tam thoibo qua
//		// TODO Auto-generated method stub
//		// アルゴリズム「申請一覧リスト取得マスタ情報」を実行する(get List App Master Info): 9 - 申請一覧リスト取得マスタ情報
//		DataMasterOutput lstMaster = this.getListAppMasterInfo(lstAppFilter, companyId,
//				new DatePeriod(param.getStartDate(), param.getEndDate()), device);
//
//		// 承認一覧に稟議書リスト追加し、申請日付順に整列する - phu thuoc vao request
//		List<ContentApp> lstContentApp = new ArrayList<>();
//		if (device == PC) {
//			lstContentApp = this.crateContentApp(lstAppFilter3, param.getAppListAtr(),
//					displaySet.getAppReasonDisAtr().value, lstAppOt, lstAppAbsence, lstAppWorkChange, lstAppGoBack,
//					lstAppHdWork, lstAppCompltLeaveSync, timeOutput.getLstAppGroup(), lstMaster.getLstAppMasterInfo(),
//					lstWkType, lstWkTime);
//		}
//
//		return new AppListOutPut(lstMaster, lstAppFilter, timeOutput.getAppStatus(), timeOutput.getLstAppFull(),
//				timeOutput.getLstAppColor(), lstFrameUn, lstPhaseStatus, lstContentApp, lstAppCompltLeaveSync);
	}

	/**
	 * lam o ui 4 - 申請一覧リスト取得承認件数
	 */
	@Override
	public ApplicationStatus countAppListApproval(List<ListOfApplication> listApp, ApplicationStatus appStatus) {
		String loginID = AppContexts.user().employeeId();
		for (ListOfApplication appFull : listApp) {
			int add = 1;
			if(appFull.getAppType() == ApplicationType.COMPLEMENT_LEAVE_APPLICATION && appFull.getOpComplementLeaveApp().isPresent()) {
				add = 2;
			}
			//承認状況＝否
			if(appFull.getReflectionStatus().equals("CMM045_65")) {
				//否認件数に＋１する
				appStatus.setDenialNumber(appStatus.getDenialNumber() + add);
			}
			//承認状況＝差戻
			if(appFull.getReflectionStatus().equals("CMM045_66")) {
				//差戻件数に＋１する
				appStatus.setRemandNumner(appStatus.getRemandNumner() + add);
			}
			//承認状況＝取消
			if(appFull.getReflectionStatus().equals("CMM045_67")) {
				//取消件数に＋１する
				appStatus.setCancelNumber(appStatus.getCancelNumber() + add);
			}
			//承認状況＝承認済み/反映済み
			if(appFull.getReflectionStatus().equals("CMM045_63") || appFull.getReflectionStatus().equals("CMM045_64")) {
				List<ApprovalPhaseStateImport_New> listPhase = appFull.getOpApprovalPhaseLst()
						.map(x -> x.stream().sorted(Comparator.comparing(ApprovalPhaseStateImport_New::getPhaseOrder).reversed()).collect(Collectors.toList()))
						.orElse(Collections.emptyList());
				for(ApprovalPhaseStateImport_New phase : listPhase) {
					for(ApprovalFrameImport_New frame : phase.getListApprovalFrame()) {
						for(ApproverStateImport_New approver : frame.getListApprover()) {
							if((approver.getApprovalAtr()==ApprovalBehaviorAtrImport_New.APPROVED) &&
									approver.getApproverID().equals(loginID) &&
									Strings.isBlank(approver.getAgentID())) {
								appStatus.setApprovalNumber(appStatus.getApprovalNumber() + add);
							}
							if((approver.getApprovalAtr()==ApprovalBehaviorAtrImport_New.APPROVED) &&
									Strings.isNotBlank(approver.getAgentID()) &&
									approver.getAgentID().equals(loginID)) {
								appStatus.setApprovalNumber(appStatus.getApprovalNumber() + add);
							}
							if((approver.getApprovalAtr()==ApprovalBehaviorAtrImport_New.APPROVED) &&
									Strings.isNotBlank(approver.getAgentID()) &&
									!approver.getAgentID().equals(loginID)) {
								appStatus.setApprovalAgentNumber(appStatus.getApprovalAgentNumber() + add);
							}
						}
					}
				}
//				if (StringUtil.isNullOrEmpty(appFull.getAgentId(), true) || appFull.getAgentId().equals(sID)) {
//					//代行者＝未登録  または  代行者＝ログインID
//					appStatus.setApprovalNumber(appStatus.getApprovalNumber() + add);
//				} else {// 代行者≠ログインID
//					appStatus.setApprovalAgentNumber(appStatus.getApprovalAgentNumber() + add);
//				}
			}
			//承認状況＝未
			if(appFull.getReflectionStatus().equals("CMM045_62")) {
				//未承認件数に＋１する
				appStatus.setUnApprovalNumber(appStatus.getUnApprovalNumber() + add);
			}
		}
		return appStatus;
	}

	/**
	 * 申請一覧リスト取得実績
	 */
	@Override
	public TimeResultOutput getDataActual(String sID, GeneralDate date, List<OverTimeFrame> time,
			ApplicationType appType, String wkTypeCd, String wkTimeCd, List<WorkType> lstWkType,
			List<WorkTimeSetting> lstWkTime) {
		OverrideSet overrideSet = OverrideSet.SYSTEM_TIME_PRIORITY;
		Optional<CalcStampMiss> calStampMiss = Optional.empty();
		if (appType.equals(ApplicationType.OVER_TIME_APPLICATION)) {
			Optional<OvertimeAppSet> otSet = appOtSetRepo.findSettingByCompanyId(AppContexts.user().companyId());
			overrideSet = otSet.isPresent() ? otSet.get().getOvertimeLeaveAppCommonSet().getOverrideSet() : overrideSet;
		} else {
			Optional<HolidayWorkAppSet> hdSet = withdrawalAppSetRepo.findSettingByCompany(AppContexts.user().companyId());
			overrideSet = hdSet.isPresent() ? hdSet.get().getOvertimeLeaveAppCommonSet().getOverrideSet() : overrideSet;
			calStampMiss = hdSet.isPresent() ? Optional.of(hdSet.get().getCalcStampMiss()) : calStampMiss;
		}

		//07-02_実績取得・状態チェック
		ActualStatusCheckResult cal = preActualCheck.actualStatusCheck(AppContexts.user().companyId(), sID, date,
				appType, wkTypeCd, wkTimeCd, overrideSet, calStampMiss, Collections.emptyList());

		boolean checkColor = false;
		List<OverTimeFrame> lstFrameResult = new ArrayList<>();

		List<OvertimeColorCheck> actualLst = Collections.emptyList();
		for (OverTimeFrame timeFrame : time) {
			Integer actTime = this.findTimeRes(actualLst, timeFrame);
			if (actTime == null || actTime < timeFrame.getApplicationTime()) {
				checkColor = true;
			}
			OverTimeFrame frameRes = timeFrame;
			frameRes.setApplicationTime(actTime);
			lstFrameResult.add(frameRes);
		}
		if (lstFrameResult.size() < actualLst.size()) {// TH xin sau k co nhung thuc te co
			for (OvertimeColorCheck act : actualLst) {
				if (lstFrameResult.stream()
						.filter(c -> c.getAttendanceType() == act.attendanceID && c.getFrameNo() == act.frameNo)
						.collect(Collectors.toList()).size() == 0) {
					String name = "";
					if (act.frameNo == 11) {
						name = I18NText.getText("CMM045_270");
					} else if (act.frameNo == 12) {
						name = I18NText.getText("CMM045_271");
					} else {
						if (act.attendanceID == 1) {//
							List<OvertimeWorkFrame> lstFramOt = repoOverTimeFr.getOvertimeWorkFrameByFrameNos(
									AppContexts.user().companyId(), Arrays.asList(act.frameNo));
							name = !lstFramOt.isEmpty() ? lstFramOt.get(0).getOvertimeWorkFrName().v()
									: act.frameNo + "マスタ未登録";
						}
						if (act.attendanceID == 2) {
							List<WorkdayoffFrame> lstFramWork = repoWork
									.getWorkdayoffFrameBy(AppContexts.user().companyId(), Arrays.asList(act.frameNo));
							name = !lstFramWork.isEmpty() ? lstFramWork.get(0).getWorkdayoffFrName().v()
									: act.frameNo + "マスタ未登録";
						}
					}
					lstFrameResult.add(
							new OverTimeFrame(act.attendanceID, act.frameNo, name, null, act.actualTime, null, null));
				}
			}
		}
		/** 就業時間外深夜 - 計算就業外深夜 */
		Integer shiftNightTime = this.findTimeRes(actualLst, new OverTimeFrame(1, 11, "", null, null, null, null));
		/** フレックス超過時間 - 計算フレックス */
		Integer flexTime = this.findTimeRes(actualLst, new OverTimeFrame(1, 12, "", null, null, null, null));
		;
		String workTypeName = "";
		String workTimeName = "";
		if (appType.equals(ApplicationType.HOLIDAY_WORK_APPLICATION)) {
			if (Strings.isNotBlank(cal.workType)) {
				workTypeName = repoAppDetail.findWorkTypeName(lstWkType, cal.workType);
			}
			if (Strings.isNotBlank(cal.workTime)) {
				workTimeName = repoAppDetail.findWorkTimeName(lstWkTime, cal.workTime);
			}
		}

		return new TimeResultOutput(checkColor, lstFrameResult, repoAppDetail.convertTime(cal.startTime),
				repoAppDetail.convertTime(cal.endTime), repoAppDetail.convertTime(null),
				repoAppDetail.convertTime(null), shiftNightTime, flexTime, workTypeName, workTimeName);
	}

	private Integer findTimeRes(List<OvertimeColorCheck> actualLst, OverTimeFrame time) {
		for (OvertimeColorCheck act : actualLst) {
			if (act.attendanceID == time.getAttendanceType() && act.frameNo == time.getFrameNo()) {
				return act.actualTime;
			}
		}
		return null;
	}

	/**
	 * 6 - 申請一覧リスト取得振休振出 wait SonLB - kaf011
	 */
	@Override
	public AppCompltLeaveSyncOutput getListAppComplementLeave(Application_New application, String companyId) {
		// TODO Auto-generated method stub
		// Check 申請種類 - appType
		if (!application.isAppCompltLeave()) {
			return null;
		}
		String appId = application.getAppID();
		AppCompltLeaveSyncOutput sync = otherCommonAlgorithm.getAppComplementLeaveSync(companyId, appId);
		return sync;
	}


	/**
	 * 8 - 申請一覧リスト取得休暇 wait - kaf006
	 */
	@Override
	public List<Application_New> getListAppAbsence(Application_New application, String companyID) {
		// 申請種類 - check app type
		if (application.isAppAbsence()) {// 休暇申請以外の場合

		}
		// 休暇申請の場合
		// String relationshipCd = "";
		// TODO Auto-generated method stub
		// imported(就業.Shared)「続柄」を取得する
		// Optional<Relationship> relShip =
		// repoRelationship.findByCode(companyID, relationshipCd);
		return null;
	}

	/**
	 * 9 - 申請一覧リスト取得マスタ情報
	 */
	@Override
	public AppInfoMasterOutput getListAppMasterInfo(Application application, DatePeriod period, NotUseAtr displayWorkPlaceName, 
			Map<String, SyEmployeeImport> mapEmpInfo, Map<Pair<String, DatePeriod>, WkpInfo> mapWkpInfo, int device) {
		SyEmployeeImport applicant = null;
		Optional<SyEmployeeImport> opEnteredPerson = Optional.empty();
		// 社員のキャッシュがあるかチェックする ( Check xem  cash của employee có hay không?
		if(mapEmpInfo.containsKey(application.getEmployeeID())) {
			// 申請一覧リスト.申請一覧リスト.社員名をキャッシュからセットする(applicationList. Set employee name from cache)
			applicant = mapEmpInfo.get(application.getEmployeeID());
		} else {
			// アルゴリズム「社員IDから個人社員基本情報を取得」を実行する ( Thực hiện thuật toán 「社員IDから個人社員基本情報を取得」)
			applicant = syEmpAdapter.getPersonInfor(application.getEmployeeID());
			if(!application.getEmployeeID().equals(application.getEnteredPersonID())) {
				// アルゴリズム「社員IDから個人社員基本情報を取得」を実行する ( Thực hiện thuật toán 「社員IDから個人社員基本情報を取得」
				SyEmployeeImport enteredPerson = syEmpAdapter.getPersonInfor(application.getEnteredPersonID());
				opEnteredPerson = Optional.of(enteredPerson);
			}
			// 取得したデータをキャッシュに追加 ( Thêm data đã lấy vào cache)
			mapEmpInfo.put(application.getEmployeeID(), applicant);
			if(opEnteredPerson.isPresent()) {
				mapEmpInfo.put(application.getEnteredPersonID(), opEnteredPerson.get());
			}
		}
		String workplaceName = null;
		Optional<Integer> opTimeCalcUseAtr = Optional.empty();
		if((displayWorkPlaceName==NotUseAtr.USE || 
				application.isOverTimeApp() || application.isHolidayWorkApp()) &&
				device == ApprovalDevice.PC.value) {
			// 所属職場履歴Listのキャッシュがあるかチェックする(Check xem có cache List lịch sử nơi làm việc)
			Optional<Pair<String, DatePeriod>> containKey = mapWkpInfo.keySet().stream().filter(x -> {
				boolean employeeCondition = x.getLeft().equals(application.getEmployeeID());
				boolean startDateCondition = x.getRight().start().beforeOrEquals(
						application.getOpAppStartDate().map(y -> y.getApplicationDate()).orElse(application.getAppDate().getApplicationDate()));
				boolean endDateCondition = x.getRight().end().afterOrEquals(
						application.getOpAppEndDate().map(y -> y.getApplicationDate()).orElse(application.getAppDate().getApplicationDate()));
				return employeeCondition && startDateCondition && endDateCondition;
			}).findAny();
			if(containKey.isPresent()) {
				// 申請一覧リスト.職場名をキャッシュからセットする(applicationList.Set workPlace từ cache)
				workplaceName = mapWkpInfo.get(containKey.get()).getWpkName();
			} else {
				// 社員指定期間所属職場履歴を取得  (Lấy lịch sử nơi làm việc period chỉ định nhân viên)
				WorkPlaceHistBySIDImport wkp = wkpAdapter.findWpkBySIDandPeriod(application.getEmployeeID(), period);
				Optional<WkpInfo> opWkpInfo = wkp.getLstWkpInfo().stream().filter(x -> {
					boolean startDateCondition = x.getDatePeriod().start().beforeOrEquals(
							application.getOpAppStartDate().map(y -> y.getApplicationDate()).orElse(application.getAppDate().getApplicationDate()));
					boolean endDateCondition = x.getDatePeriod().end().afterOrEquals(
							application.getOpAppEndDate().map(y -> y.getApplicationDate()).orElse(application.getAppDate().getApplicationDate()));
					return startDateCondition && endDateCondition;
				}).findFirst();
				if(opWkpInfo.isPresent()) {
					workplaceName = opWkpInfo.get().getWpkName();
				}
				// 取得したデータをキャッシュに追加 ( Thêm data đã lấy vào cache)
				for(WkpInfo wkpInfo : wkp.getLstWkpInfo()) {
					mapWkpInfo.put(Pair.of(application.getEmployeeID(), wkpInfo.getDatePeriod()), wkpInfo);
				}
			}
		}
		// 申請データを作成して申請一覧に追加 ( Tạo data application và thêm vào applicationList)
		ListOfApplication result = new ListOfApplication();
		result.setAppID(application.getAppID());
		result.setApplicantID(application.getEmployeeID());
		result.setPrePostAtr(application.getPrePostAtr().value);
		result.setApplicantName(applicant.getBusinessName());
		result.setWorkplaceName(workplaceName);
		result.setAppType(application.getAppType());
		result.setApplicantCD(applicant.getEmployeeCode());
		// result.setAppReason
		result.setInputDate(application.getInputDate());
		result.setOpEntererName(opEnteredPerson.map(x -> x.getBusinessName()));
		result.setOpAppStartDate(application.getOpAppStartDate().map(x -> x.getApplicationDate()));
		result.setOpAppEndDate(application.getOpAppEndDate().map(x -> x.getApplicationDate()));
		result.setAppDate(application.getAppDate().getApplicationDate());
		// result.setOpTimeCalcUseAtr();
		result.setVersion(application.getVersion());
		result.setApplication(application);
		return new AppInfoMasterOutput(result, mapEmpInfo, mapWkpInfo);
		
//		if (lstApp.isEmpty()) {
//			return new DataMasterOutput(Collections.emptyList(), Collections.emptyList(), new HashMap<>());
//		}
//		//ドメインモデル「申請一覧共通設定」を取得する-(Lấy domain Application list common settings)
//		Optional<AppCommonSet> appCommonSet = repoAppCommonSet.find();
//		ShowName displaySet = appCommonSet.get().getShowWkpNameBelong();
//		//ドメインモデル「申請表示名」より申請表示名称を取得する (Lấy Application display name)
//		List<AppDispName> appDispName = repoAppDispName.getAll();
//		Map<String, SyEmployeeImport> mapEmpInfo = new HashMap<>();
//		List<AppMasterInfo> lstAppMasterInfo = new ArrayList<>();
//		Map<String, Integer> mapWpkSet = new HashMap<>();
//		Map<String, List<WkpInfo>> mapWpkInfo = new HashMap<>();
//		List<String> lstSCD = new ArrayList<>();
//		//key - SCD, value - lstAppID
//		Map<String, List<String>> mapAppBySCD = new HashMap<>();
//		//申請一覧リスト 繰返し実行
//		for (Application_New app : lstApp) {
//			String applicantID = app.getEmployeeID();
//			String enteredPersonID = app.getEnteredPersonID();
//			//アルゴリズム「社員IDから個人社員基本情報を取得」を実行する - req #1 lay ten ng duoc lam don
//			SyEmployeeImport empInfo = this.findNamebySID(mapEmpInfo, applicantID);
//			String empName = "";
//			String empCD = "";
//			if (empInfo == null) {
//				SyEmployeeImport emp = syEmpAdapter.getPersonInfor(applicantID);
//				empName = emp.getBusinessName();
//				empCD = emp.getEmployeeCode();
//				mapEmpInfo.put(app.getEmployeeID(), emp);
//			} else {
//				empName = empInfo.getBusinessName();
//				empCD = empInfo.getEmployeeCode();
//			}
//			if (!lstSCD.contains(empCD)) {
//				lstSCD.add(empCD);
//			}
//
//			String inpEmpName = null;//khoi tao = null -> truong hop trung nhau
//			String wkpName = "";
//			Integer detailSet = 0;
//			//get Application Name
//			String appDispNameStr = this.findAppName(appDispName, app.getAppType());
//			if (device == PC) {
//				//lay ten ng tao don
//				SyEmployeeImport inpEmpInfo = applicantID.equals(enteredPersonID) ? null
//						: this.findNamebySID(mapEmpInfo, enteredPersonID);
//
//				if (!app.getEmployeeID().equals(enteredPersonID) && inpEmpInfo == null) {
//					inpEmpInfo = syEmpAdapter.getPersonInfor(enteredPersonID);
//					inpEmpName = inpEmpInfo.getBusinessName();
//					mapEmpInfo.put(enteredPersonID, inpEmpInfo);
//				}
//				if (!app.getEmployeeID().equals(enteredPersonID) && inpEmpInfo != null) {
//					inpEmpName = inpEmpInfo.getBusinessName();
//				}
//				//get work place info
//				List<WkpInfo> findExitWkp = this.findExitWkp(mapWpkInfo, applicantID);
//				if (findExitWkp == null) {//chua co trong cache
//					//社員指定期間所属職場履歴を取得 - req #168
//					WorkPlaceHistBySIDImport wkp = wkpAdapter.findWpkBySIDandPeriod(app.getEmployeeID(), period);
//					findExitWkp = wkp.getLstWkpInfo();
//					mapWpkInfo.put(applicantID, findExitWkp);
//				}
//				WkpInfo wkp = null;
//				if (!findExitWkp.isEmpty()) {
//					wkp = this.findWpk(findExitWkp, app.getAppDate());
//				}
//				String wkpID = wkp == null ? "" : wkp.getWpkID();
//
//				if (displaySet.equals(ShowName.SHOW)) {
//					wkpName = wkp == null ? "" : wkp.getWpkName();
//				}
//				if (app.isAppOverTime()) {// TH: app.isAppAbsence() tam thoi bo qua do tren spec chua su dung
//					String wpk = wkpID + app.getAppType().value;
//					detailSet = this.finSetByWpkIDAppType(mapWpkSet, wpk);
//					if (detailSet != null && detailSet == -1) {
//						detailSet = this.detailSet(companyId, wkpID, app.getAppType().value, period.end());
//						mapWpkSet.put(wpk, detailSet);
//					}
//				}
//			}
//			lstAppMasterInfo.add(new AppMasterInfo(app.getAppID(), app.getAppType().value, appDispNameStr, empName,
//					inpEmpName, wkpName, false, null, false, 0, detailSet, empCD));
//		}
//		for (String sCD : lstSCD) {
//			List<String> lstAppID = new ArrayList<>();
//			for (AppMasterInfo master : lstAppMasterInfo) {
//				if (master.getEmpSD().equals(sCD)) {
//					lstAppID.add(master.getAppID());
//				}
//			}
//			mapAppBySCD.put(sCD, lstAppID);
//		}
//		return new DataMasterOutput(lstAppMasterInfo, lstSCD, mapAppBySCD);
	}

	/**
	 * 12 - 申請一覧初期日付期間
	 */
	@Override
	public DatePeriod getInitialPeriod(String companyId) {
		// imported(就業)「所属雇用履歴」より雇用コードを取得する
		DatePeriod date = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		List<EmploymentHisImport> lst = employmentAdapter.findByListSidAndPeriod(AppContexts.user().employeeId(), date);
		if(CollectionUtil.isEmpty(lst)) {
			// #Msg_426を表示する
			throw new BusinessException("Msg_426");
		}
		//ドメイン「締め」を取得する
		List<Closure> lstClosure = repoClosure.findAllActive(companyId, UseClassification.UseClass_Use);
		// list clourse hist 締め開始日
		GeneralDate startDate = null;
		//締め終了日  GeneralDate endDate = null;
		for (Closure closure : lstClosure) {
			//アルゴリズム「処理年月と締め期間を取得する」を実行する
			Optional<PresentClosingPeriodImport> closureA = closureAdapter.getClosureById(companyId,
					closure.getClosureId().value);
			PresentClosingPeriodImport priod = closureA.get();
			if (startDate == null || startDate.after(priod.getClosureStartDate())) {
				startDate = priod.getClosureStartDate();
			}
		}
		//EA2238
		//上記、開始日付の２年後（＋２年－１日）」を終了日付として取得
		return new DatePeriod(startDate, startDate.addYears(2).addDays(-1));
	}

	/**
	 * 12.1 - 申請一覧初期日付期間_申請
	 * 
	 * @param companyId
	 * @return
	 */
	@Override
	public DatePeriod getInitPeriodApp(String companyId) {
		//imported(就業)「所属雇用履歴」より雇用コードを取得する - request list 264
		DatePeriod date = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		List<EmploymentHisImport> lst = employmentAdapter.findByListSidAndPeriod(AppContexts.user().employeeId(), date);
		if(CollectionUtil.isEmpty(lst)) {
			// #Msg_426を表示する
			throw new BusinessException("Msg_426");
		}
		//imported（就業.shared）「雇用に紐づく就業締め」を取得する
		Optional<ClosureEmployment> closureEmp = closureEmpRepo.findByEmploymentCD(companyId, lst.get(0).getEmploymentCode());
		//アルゴリズム「処理年月と締め期間を取得する」を実行する
		Optional<PresentClosingPeriodImport> closure = closureAdapter.getClosureById(companyId, closureEmp.get().getClosureId());
		//締め開始日を開始日付とする
		GeneralDate startDate = closure.get().getClosureStartDate();
		//締め終了日付の３か月後を終了日付として取得
		GeneralDate endDate = closure.get().getClosureEndDate().addMonths(3);
		return new DatePeriod(startDate, endDate);
	}

	/**
	 * refactor 4
	 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.ワークフロー.Export.承認ルートの内容取得.承認区分から承認ルートを取得.承認区分から承認ルートを取得
	 * @param period 対象期間（開始日、終了日）
	 * @param unapprovalStatus, approvalStatus, denialStatus, agentApprovalStatus, remandStatus, cancelStatus List<承認区分＞
	 * @param approverID 承認者ID
	 * @param appIDLst List<申請ID＞
	 * @return
	 */
	private Map<String, List<ApprovalPhaseStateImport_New>> mergeAppAndPhase_New(DatePeriod period,
			boolean unapprovalStatus, boolean approvalStatus, boolean denialStatus, boolean agentApprovalStatus, boolean remandStatus, boolean cancelStatus,
			String approverID, List<String> appIDLst) {
		// List<承認者の承認区分＞＝Empty、List<承認フェーズの承認区分＞＝Empty
		Map<String, List<ApprovalPhaseStateImport_New>> mapApprInfo = new HashMap<>();
		// ドメインモデル「代行承認」を取得する ( Lấy domain model 「代行承認」)
		// request 244
		String companyID = AppContexts.user().companyId();
		GeneralDate sysDate = GeneralDate.today();
		List<String> lstAgent = agentAdapter.lstAgentData(companyID, approverID, sysDate, sysDate).stream().map(x -> x.getEmployeeId()).collect(Collectors.toList());
		// ドメインモデル「承認ルートインスタンス」を取得
		mapApprInfo = approvalRootStateAdapter
				.getApprovalRootContentCMM045(companyID, approverID, lstAgent, period, unapprovalStatus, approvalStatus,
						denialStatus, agentApprovalStatus, remandStatus, cancelStatus);
		if(!CollectionUtil.isEmpty(appIDLst)) {
			Map<String, List<ApprovalPhaseStateImport_New>> mapApprInfoByAppLst = new HashMap<>();
			for(String appID : appIDLst) {
				List<ApprovalPhaseStateImport_New> approvalPhaseLst = mapApprInfoByAppLst.get(appID);
				if(!CollectionUtil.isEmpty(approvalPhaseLst)) {
					mapApprInfoByAppLst.put(appID, approvalPhaseLst);
				}
			}
			mapApprInfo = mapApprInfoByAppLst;
		}
		return mapApprInfo;
	}

	private PhaseFrameStatus check3(AppListExtractCondition param, ApplicationFullOutput appFull) {
//		String sID = AppContexts.user().employeeId();
//		ReflectedState_New state = appFull.getApplication().getReflectionInformation().getStateReflectionReal();
//		List<PhaseFrameStatus> statusApr = this.findPhaseFrameStatus(appFull.getLstPhaseState(), sID);
//		List<PhaseFrameStatus> statusOK = new ArrayList<>();
//		for(PhaseFrameStatus status: statusApr) {
//			boolean check = false;
//			if (status.getFrameStatus() == null && status.getPhaseStatus() == null) {
//				continue;
//			}
//			int phaseMin = this.phaseNotApprMax(appFull.getLstPhaseState());
//			if (status.getPhaseOrder().intValue() < phaseMin) continue;
//			
//			// 申請一覧共通設定.承認状況＿未承認がチェックあり(True)の場合 - A4_1_1: check
//			if (param.isUnapprovalStatus() && state.equals(ReflectedState_New.NOTREFLECTED)
//					|| param.getAppDisplayAtr().equals(ApplicationDisplayAtr.APP_APPROVED)) {
//				if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
//						&& status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {
//					check = true;
//				}
//			}
//			// 申請一覧共通設定.承認状況＿承認がチェックあり(True)の場合 - A4_1_2: check
//			if (param.isApprovalStatus()) {
//				if ((state.equals(ReflectedState_New.NOTREFLECTED) || state.equals(ReflectedState_New.WAITREFLECTION)
//						|| state.equals(ReflectedState_New.REFLECTED))
//						&& ((status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
//								&& status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED))
//								|| (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)))) {
//					check = true;
//				}
//			}
//			// 申請一覧共通設定.承認状況＿否認がチェックあり(True)の場合 - A4_1_3: check
//			if (param.isDenialStatus() && state.equals(ReflectedState_New.DENIAL)) {
//				check = true;
//			}
//			// 申請一覧共通設定.承認状況＿代行承認済がチェックあり(True)の場合 - A4_1_4: check
//			if (param.isAgentApprovalStatus() && (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
//					|| status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED))) {
//				// 承認フェーズ.承認区分 ＝ 未承認または承認済
//				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
//						&& Strings.isNotBlank(status.getAgentId()) && !status.getAgentId().equals(sID)) {
//					check = true;
//				}
//	
//			}
//			// 申請一覧共通設定.承認状況＿差戻がチェックあり(True)の場合 - A4_1_5: check
//			if (param.isRemandStatus() && state.equals(ReflectedState_New.NOTREFLECTED)) {
//				if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.REMAND)) {
//					check = true;
//				}
//			}
//			// 申請一覧共通設定.承認状況＿取消がチェックあり(True)の場合 - A4_1_6: check
//			if (param.isCancelStatus()
//					&& (state.equals(ReflectedState_New.CANCELED) || state.equals(ReflectedState_New.WAITCANCEL))) {// 反映状態.実績反映状態 ＝ 取消または取消待ち
//				check = true;
//			}
//			if (check) {//条件 bo sung: phase truoc do phai duoc approval thi moi hien thi don
//				statusOK.add(status);
//			}
//		}
//		if(statusOK.isEmpty()) return null;
//		if(statusOK.size() == 1) return statusOK.get(0);
//		//TH 1ng nhieu phase
//		return statusOK.get(statusOK.size() - 1);
		return null;
	}
	/**
	 * find status phase and frame
	 * 
	 * @param lstPhase
	 * @param sID
	 * @return
	 */
	private List<PhaseFrameStatus> findPhaseFrameStatus(List<ApprovalPhaseStateImport_New> lstPhase, String sID) {
		List<PhaseFrameStatus> lstStt = new ArrayList<>();
		Collections.sort(lstPhase, Comparator.comparing(ApprovalPhaseStateImport_New::getPhaseOrder).reversed());
		for (ApprovalPhaseStateImport_New appPhase : lstPhase) {
			FrameOutput frame = this.checkPhaseCurrent(appPhase, sID);
			if (frame.getFrameStatus() != null) {
				lstStt.add(new PhaseFrameStatus(appPhase.getPhaseOrder(), appPhase.getApprovalAtr(),
						EnumAdaptor.valueOf(frame.getFrameStatus(), ApprovalBehaviorAtrImport_New.class),
						frame.getAgentId()));
			}
		}
		return lstStt;
	}

	/**
	 * check phase current (login)
	 * 
	 * @param phase
	 * @param sID
	 * @return null: not phase
	 * @return not null: phase current and status frame
	 */
	private FrameOutput checkPhaseCurrent(ApprovalPhaseStateImport_New phase, String sID) {
		List<ApprovalFrameImport_New> lstFrame = phase.getListApprovalFrame();
		FrameOutput statusFrame = new FrameOutput();
		for (ApprovalFrameImport_New frame : lstFrame) {
			List<ApproverStateImport_New> listApprover = frame.getListApprover();
			for(ApproverStateImport_New appr : listApprover) {
				if (appr.getApproverID().equals(sID)) {
					statusFrame.setFrameStatus(appr.getApprovalAtr().value);
					statusFrame.setAgentId(appr.getAgentID());
					break;
				}
				// TH login la agent va da approval
				if (appr.getAgentID() != null && appr.getAgentID().equals(sID)) {
					statusFrame.setFrameStatus(appr.getApprovalAtr().value);
					statusFrame.setAgentId(appr.getAgentID());
					break;
				}
				// TH login la agent va chua approval
				if (Strings.isNotBlank(appr.getRepresenterID()) 
						&& appr.getApprovalAtr().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {
					statusFrame.setFrameStatus(appr.getApprovalAtr().value);
					statusFrame.setAgentId(appr.getRepresenterID());
					break;
				}
			}
		}
		return statusFrame;
	}

	/**
	 * calculate status of application
	 * 
	 * @param appStatus
	 * @param status
	 * @param agentId
	 * @return
	 */
	private int calApplication(ReflectedState_New appStatus, PhaseFrameStatus status, String agentId, String sIdLogin) {
		if (agentId != null) {// ※ログイン者 ＝代行者の場合
			if (agentId.equals(sIdLogin)) {
				return this.calAppAgent(appStatus, status);
			}
		}
		// ※ログイン者 ＝承認者の場合
		return this.calAppAproval(appStatus, status);
	}

	/**
	 * calculate status TH ログイン者 ＝承認者の場合
	 * 
	 * @param appStatus
	 * @param status
	 * @return
	 */
	private int calAppAproval(ReflectedState_New appStatus, PhaseFrameStatus status) {
		switch (appStatus.value) {
		case 0:// APP: 未反映 - NOTREFLECTED
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {//Phase: 未承認
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {//Frame: 未承認
					return 5;
				}
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Frame: 承認済
					return 4;
				}
				return 0;
			}
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.REMAND)) {// 差し戻し
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {
					return 2;
				}
				return 0;
			}
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Phase: 承認済
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Frame: 未承認/承認済
					return 4;
				}
				return 0;
			}
			return 0;
		case 1:// APP: 反映待ち - WAITREFLECTION
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Phase: 承認済
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Frame: 未承認/承認済
					return 4;
				}
				return 0;
			}
			return 0;
		case 2:// APP: 反映済 - REFLECTED
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Phase: 承認済
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Frame: 未承認/承認済
					return 4;
				}
				return 0;
			}
			return 0;
		case 5:// APP: 差し戻し - REMAND
			return 0;
		case 6:// APP: 否認 - DENIAL
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {//Phase: 未承認
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 未承認/承認済/否認
					return 1;
				}
				return 0;
			}
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Phase: 承認済
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 未承認/承認済/否認
					return 1;
				}
				return 0;
			}
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Phase: 否認
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 未承認/承認済/否認
					return 1;
				}
				return 0;
			}
			return 0;
		default:// APP: 取消待ち - WAITCANCEL/取消済 - CANCELED
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {//Phase: 未承認
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 未承認/承認済/否認
					return 3;
				}
				return 0;
			}
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Phase: 承認済
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 未承認/承認済/否認
					return 3;
				}
				return 0;
			}
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Phase: 否認
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 未承認/承認済/否認
					return 3;
				}
				return 0;
			}
			return 0;
		}
	}

	/**
	 * calculate status TH ログイン者 ＝代行者の場合
	 * 
	 * @param appStatus
	 * @param status
	 * @return
	 */
	private int calAppAgent(ReflectedState_New appStatus, PhaseFrameStatus status) {
		switch (appStatus.value) {
		case 0://APP: 未反映 - NOTREFLECTED
				//Phase: 未承認
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {//Frame: 未承認
					return 5;
				}
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Frame: 承認済
					return 4;
				}
				return 0;
			}
			//差し戻し
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.REMAND)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {
					return 2;
				}
				return 0;
			}
			//Phase: 承認済
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Frame: 承認済
					return 4;
				}
				return 0;
			}
			return 0;
		case 1://APP: 反映待ち - WAITREFLECTION
				//Phase: 承認済
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {//Frame: 承認済
					return 4;
				}
				return 0;
			}
			return 0;
		case 2://APP: 反映済 - REFLECTED
				//Phase: 承認済
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {// Frame: 承認済
					return 4;
				}
				return 0;
			}
			return 0;
		case 5://APP: 差し戻し - REMAND
			return 0;
		case 6://APP: 否認 - DENIAL
				//Phase: 未承認
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 承認済/否認
					return 1;
				}
				return 0;
			}
			//Phase: 承認済
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 承認済/否認
					return 1;
				}
				return 0;
			}
			//Phase: 否認
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 承認済/否認
					return 1;
				}
				return 0;
			}
			return 0;
		default://APP: 取消待ち - WAITCANCEL/取消済 - CANCELED
			//Phase: 未承認
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 承認済/否認
					return 3;
				}
				return 0;
			}
			//Phase: 承認済
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 承認済/否認
					return 3;
				}
				return 0;
			}
			//Phase: 否認
			if (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {
				if (status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {//Frame: 承認済/否認
					return 3;
				}
				return 0;
			}
			return 0;
		}
	}

	/**
	 * 条件１： ログイン者の表示対象の基本条件 (filter application by conditions 1)
	 * 
	 * @param app
	 * @return
	 */
	private ApproverStt filterConditions1(ApplicationFullOutput app, List<AgentDataRequestPubImport> lstAgent,
			String sID) {
		//dk1
		List<ApprovalPhaseStateImport_New> lstPhase = app.getLstPhaseState();
		ApproverStt check = new ApproverStt(false, null);
		for (ApprovalPhaseStateImport_New appPhase : lstPhase) {
			for (ApprovalFrameImport_New frame : appPhase.getListApprovalFrame()) {
				List<ApproverStateImport_New> listApprover = frame.getListApprover();
				int approverCount = frame.getListApprover().size();
				for(ApproverStateImport_New appr : listApprover) {
					//承認枠.承認区分!=未承認
					if (!appr.getApprovalAtr().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) {
						if (this.checkDifNotAppv(appr, sID)) {
							check = new ApproverStt(true, null);
							break;
						}
					} else {//承認枠.承認区分 = 未承認
						ApproverStt checkNotAppv = this.checkNotAppv(appr, lstAgent, appPhase.getApprovalAtr(),
								app.getApplication(), sID, approverCount);
						if (checkNotAppv.isCheck()) {
							check = new ApproverStt(true, checkNotAppv.getApprId());
							;
							break;
						}
					}
				}
			}

		}
		return check;
	}

	/**
	 * 承認枠.承認区分!=未承認
	 *
	 * @return
	 */
	private boolean checkDifNotAppv(ApproverStateImport_New approver, String sID) {
		if (approver.getApproverID().equals(sID)) {
			return true;
		}
		if (Strings.isNotBlank(approver.getAgentID()) && approver.getAgentID().equals(sID)) {
			return true;
		}
		return false;
	}

	/**
	 * 承認枠.承認区分 = 未承認
	 * @return
	 */
	private ApproverStt checkNotAppv(ApproverStateImport_New appr, List<AgentDataRequestPubImport> lstAgent,
			ApprovalBehaviorAtrImport_New phaseAtr, Application_New app, String sID, int frameCount) {
		//※前提条件：「申請.反映情報 ＝ 未反映」且 「自身の承認フェーズ ＝ 未承認/差し戻し」の場合
//		if (frameCount <= 1
//				&& (!app.getReflectionInformation().getStateReflectionReal().equals(ReflectedState_New.NOTREFLECTED)
//						|| (!phaseAtr.equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
//								&& !phaseAtr.equals(ApprovalBehaviorAtrImport_New.REMAND)))) {
//			return new ApproverStt(false, null);
//		}
		//１．承認予定者より取得（自身が承認する申請）
		if (appr.getApproverID().equals(sID)) {//承認枠.承認者リスト(複数ID) ＝ ログイン者社員ID
			return new ApproverStt(true, null);
		}
		//２．取得したドメイン「代行者管理」より代理者指定されている場合は取得
		//申請.申請日付 ＝ 代行者管理：代行承認.代行依頼期間 &&承認枠.承認者リスト（複数ID）＝ 代行者管理：代行承認.代行依頼者
		List<String> lstId = new ArrayList<>();
		String idAppr = null;
		for (AgentDataRequestPubImport agent : lstAgent) {
			//2019/05/14 EA修正履歴 No.3436 #107724
			if (appr.getApproverID().equals(agent.getEmployeeId())) {
				lstId.add(agent.getAgentSid1());
				idAppr = agent.getEmployeeId();
			}
		}
		if (lstId.contains(sID)) {//代行承認.承認代行者 ＝ ログイン者社員ID
			return new ApproverStt(true, idAppr);
		}
		return new ApproverStt(false, null);
	}

	/**
	 * convert status phase －：承認フェーズ.承認区分 ＝ 未承認 〇：承認フェーズ.承認区分 ＝ 承認済
	 * ×：承認フェーズ.承認区分 ＝ 否認
	 * 
	 * @param appId
	 * @param lstPhaseState
	 * @return
	 */
	private PhaseStatus convertStatusPhase(String appId, List<ApprovalPhaseStateImport_New> lstPhaseState) {
		String phaseStatus = "";
		List<Integer> lstPhaseAtr = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			String phaseI = "";
			Integer status = this.findPhaseStatus(lstPhaseState, i);
			lstPhaseAtr.add(status);
			if (status != null) {//phase exist
				phaseI = status == 1 ? "〇" : status == 2 ? "×" : "－";
			}
			phaseStatus += phaseI;
		}
		return new PhaseStatus(appId, phaseStatus, lstPhaseAtr);
	}

	/**
	 * find phase status
	 * 
	 * @param lstPhaseState
	 * @param order
	 * @return
	 */
	private Integer findPhaseStatus(List<ApprovalPhaseStateImport_New> lstPhaseState, int order) {
		for (ApprovalPhaseStateImport_New phase : lstPhaseState) {
			if (phase.getPhaseOrder().equals(order)) {
				return phase.getApprovalAtr().value;
			}
		}
		return null;
	}

	/**
	 * check color application exist list check???
	 * 
	 * @param lstColor
	 * @param appId
	 * @return
	 */
	private boolean checkExistColor(List<CheckColorTime> lstColor, String appId) {
		for (CheckColorTime checkColorTime : lstColor) {
			if (checkColorTime.getAppID().equals(appId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check time pr < time post
	 * 
	 * @param timePre
	 * @param timePost
	 * @return true : error -> fill color
	 */
	private boolean checkPrePostColor(List<OverTimeFrame> timePre, List<OverTimeFrame> timePost) {
		//loop by frame post
		for (OverTimeFrame overTimeFrame : timePost) {
			//frame pre
			Integer checkColor = this.findTimePre(timePre, overTimeFrame.getFrameNo());
			if (checkColor == null) {
				return overTimeFrame == null ? false : true;
			}
			if (overTimeFrame.getApplicationTime() == null) {
				return false;
			}
			if (overTimeFrame.getApplicationTime() < checkColor) {
				return true;
			}
		}
		return false;
	}

	/**
	 * find time application pre
	 * 
	 * @param lstFrame
	 * @param frameNo
	 * @return
	 */
	private Integer findTimePre(List<OverTimeFrame> lstFrame, int frameNo) {
		for (OverTimeFrame overTimeFrame : lstFrame) {
			if (overTimeFrame.getFrameNo() == frameNo) {
				return overTimeFrame.getApplicationTime();
			}
		}
		return null;
	}
	
	@Override
	public List<ApplicationUseSetting> detailSetKAF022(String companyId, String wkpId, GeneralDate date) {
		// ドメイン「職場別申請承認設定」を取得する-(lấy dữ liệu domain Application approval setting by workplace)
		if(Strings.isNotBlank(wkpId)) {
			Optional<RequestByWorkplace> wpkSet = Optional.empty();
			wpkSet = repoRequestWkp.findByCompanyAndWorkplace(companyId, wkpId);
			if (wpkSet.isPresent()) return wpkSet.get().getApprovalFunctionSet().getAppUseSetLst();
			
			//取得できなかった場合
			//職場の上位職場を取得する - ※RequestList569
			List<String> lstWpkIDPr = wkpAdapter.getUpperWorkplaceRQ569(companyId, wkpId, date);
			if (lstWpkIDPr.size() >= 1) {
				for (int i = 0; i < lstWpkIDPr.size(); i++) {
					//ドメイン「職場別申請承認設定」を取得する
					wpkSet = repoRequestWkp.findByCompanyAndWorkplace(companyId, lstWpkIDPr.get(i));
					if (wpkSet.isPresent()) return wpkSet.get().getApprovalFunctionSet().getAppUseSetLst();
				}
			}
		}
		
		//ドメイン「会社別申請承認設定」を取得する-(lấy dữ liệu domain Application approval setting by company)
		Optional<RequestByCompany> comSet = Optional.empty();
		comSet = repoRequestCompany.findByCompanyId(companyId);
		return comSet.isPresent() ? comSet.get().getApprovalFunctionSet().getAppUseSetLst() : new ArrayList<>();
	}
}
