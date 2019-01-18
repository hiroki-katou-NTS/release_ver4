package nts.uk.ctx.at.request.dom.application.applist.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.AppListExtractCondition;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.ApplicationDisplayAtr;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.ApplicationListAtr;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppAbsenceFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppCompltLeaveFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppDetailInfoRepository;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppGoBackInfoFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppHolidayWorkFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppOverTimeInfoFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppWorkChangeFull;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.AtEmploymentAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.SyEmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.EmploymentHisImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SyEmployeeImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.closure.PresentClosingPeriodImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.closure.RqClosureAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.frame.OvertimeInputCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculationImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.TimeWithCalculationImport;
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
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
//import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AppCompltLeaveSyncOutput;
import nts.uk.ctx.at.request.dom.application.stamp.AppStamp;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampRepository;
import nts.uk.ctx.at.request.dom.application.stamp.StampRequestMode;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationcommonsetting.AppCommonSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationcommonsetting.AppCommonSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationcommonsetting.primitive.ShowName;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispName;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.displaysetting.DisplayAtr;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.ApprovalListDisplaySetting;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.RequestOfEachCompanyRepository;
import nts.uk.ctx.at.request.dom.setting.workplace.RequestOfEachWorkplaceRepository;
import nts.uk.ctx.at.request.dom.setting.workplace.SettingFlg;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
//import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
//import nts.uk.ctx.at.shared.dom.workrule.closure.CurrentMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
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
	private CollectAchievement collectAchievement;
	@Inject
	private AppStampRepository repoAppStamp;
	@Inject
	private RequestOfEachWorkplaceRepository repoRequestWkp;
	@Inject
	private RequestOfEachCompanyRepository repoRequestCompany;
	@Inject
	private ApplicationRepository_New repoApp;
	@Inject
	private RecordWorkInfoAdapter recordWkpInfoAdapter;
	@Inject
	private WorkplaceAdapter wkpAdapter;
	@Inject
	private AppCommonSetRepository repoAppCommonSet;
	@Inject
	private AppDispNameRepository repoAppDispName;
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	@Inject
	private AppDetailInfoRepository repoAppDetail;
	@Inject
	private DailyAttendanceTimeCaculation calTime;
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
	
	/**
	 * 0 - 申請一覧事前必須チェック
	 */
	@Override
	public Boolean checkAppPredictRequire(int appType, String wkpID, String companyId) {
		//申請種類-(Check AppType)
		if(appType != 0 && appType != 6){//「休出時間申請」又は「残業申請」以外の場合
			return false;
		}
		//「休出時間申請」又は「残業申請」の場合
		//ドメイン「職場別申請承認設定」を取得する-(lấy dữ liệu domain Application approval setting by workplace)
		Optional<ApprovalFunctionSetting> appFuncSet = null;
		appFuncSet = repoRequestWkp.getFunctionSetting(companyId, wkpID, appType);
		//対象が存在しない場合 - TH doi tuong k ton tai
		if(!appFuncSet.isPresent() || appFuncSet.get().getInstructionUseSetting().getInstructionUseDivision().equals(UseAtr.NOTUSE)){
			//ドメイン「会社別申請承認設定」を取得する-(lấy dữ liệu domain Application approval setting by company)
			appFuncSet = repoRequestCompany.getFunctionSetting(companyId, appType);
		}
		if(!appFuncSet.isPresent()|| appFuncSet.get().getInstructionUseSetting().getInstructionUseDivision().equals(UseAtr.NOTUSE)){
			return false;
		}
		//申請承認機能設定.残業申請の事前必須設定
		if(appFuncSet.get().getOvertimeAppSetting().equals(SettingFlg.SETTING)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 1 - 申請一覧リスト取得
	 */
	@Override
	public AppListOutPut getApplicationList(AppListExtractCondition param, ApprovalListDisplaySetting displaySet) {
		//申請一覧区分が申請 OR 承認-(Check xem là application hay aprove)
		AppListOutPut appFull = null;
		if(param.getAppListAtr().equals(ApplicationListAtr.APPLICATION)){//申請
			//アルゴリズム「申請一覧リスト取得申請」を実行する - 2
			appFull = this.getApplicationListByApp(param);
		}else{//承認
			//アルゴリズム「申請一覧リスト取得承認」を実行する - 3
			appFull = this.getAppListByApproval(param,displaySet);
		}
		return appFull;
	}
	/**
	 * 2 - 申請一覧リスト取得申請
	 */
	@Override
	public AppListOutPut getApplicationListByApp(AppListExtractCondition param) {
		String companyId = AppContexts.user().companyId();
		//アルゴリズム「申請一覧対象申請者取得」を実行する
		ListApplicantOutput checkMySelf = this.getListApplicantForListApp(param);
		//ドメインモデル「申請」を取得する-(Lấy dữ liệu domain Application) - get List Application By SID
		List<Application_New> lstApp = new ArrayList<>();
//		2018/11/02　EA2892(#102518)
//		・社員指定ができるため代行申請分の表示は不要
//		「自分の申請」に関する条件も不要
//		if(checkMySelf.isMySelf()){//【自分の申請＝Trueの場合】
			//・申請者ID＝社員ID（リスト）　　または　入力者ID＝社員ID（リスト）
//			lstApp = repoApp.getByListSID(companyId, checkMySelf.getLstSID(), param.getStartDate(), param.getEndDate());
//		}else{
			//・申請者ID＝社員ID（リスト）
			lstApp = repoApp.getByListApplicant(companyId, checkMySelf.getLstSID(), param.getStartDate(), param.getEndDate());
//		}
		List<Application_New> lstOverTime = lstApp.stream().filter(c -> c.isAppOverTime()).collect(Collectors.toList());
		List<Application_New> lstGoBack = lstApp.stream().filter(d -> d.isAppGoBack()).collect(Collectors.toList());
		List<Application_New> lstHdWork = lstApp.stream().filter(d -> d.isAppHdWork()).collect(Collectors.toList());
		List<Application_New> lstWkChange = lstApp.stream().filter(d -> d.isAppWkChange()).collect(Collectors.toList());
		List<Application_New> lstAbsence = lstApp.stream().filter(d -> d.isAppAbsence()).collect(Collectors.toList());
		List<Application_New> lstCompltLeave = lstApp.stream().filter(d -> d.isAppCompltLeave()).collect(Collectors.toList());
		
		List<AppOverTimeInfoFull> lstAppOt = new ArrayList<>();
		List<AppGoBackInfoFull> lstAppGoBack = new ArrayList<>();
		List<AppHolidayWorkFull> lstAppHdWork = new ArrayList<>();
		List<AppWorkChangeFull> lstAppWkChange = new ArrayList<>();
		List<AppAbsenceFull> lstAppAbsence = new ArrayList<>();
		List<AppCompltLeaveSync> lstAppCompltLeaveSync = new ArrayList<>();
		//残業申請: get full info (0)
		List<String> lstAppOtID = lstOverTime.stream().map(c -> c.getAppID()).collect(Collectors.toList());
		if(!lstAppOtID.isEmpty()){
			lstAppOt = repoAppDetail.getListAppOverTimeInfo(companyId, lstAppOtID);
		}
		//直行直帰申請: get full info(4)
		List<String> lstAppGoBackID = lstGoBack.stream().map(c -> c.getAppID()).collect(Collectors.toList());
		if(!lstAppGoBackID.isEmpty()){
			lstAppGoBack = repoAppDetail.getListAppGoBackInfo(companyId, lstAppGoBackID);
		}
		//休日出勤時間申請: get full info(6);
		List<String> lstAppHdID = lstHdWork.stream().map(c -> c.getAppID()).collect(Collectors.toList());
		if(!lstAppHdID.isEmpty()){
			lstAppHdWork = repoAppDetail.getListAppHdWorkInfo(companyId, lstAppHdID);
		}
		//勤務変更申請: get full info(2);
		List<String> lstAppWkChangeID = lstWkChange.stream().map(c -> c.getAppID()).collect(Collectors.toList());
		if(!lstAppWkChangeID.isEmpty()){
			lstAppWkChange = repoAppDetail.getListAppWorkChangeInfo(companyId, lstAppWkChangeID);
		}
		//休暇申請: get full info(1);
		for (Application_New app : lstAbsence) {
			Integer day = 0;
			if(app.getStartDate().isPresent()&& app.getEndDate().isPresent()){
				day = app.getStartDate().get().daysTo(app.getEndDate().get()) + 1;
			}
			AppAbsenceFull appAbsence = repoAppDetail.getAppAbsenceInfo(companyId, app.getAppID(), day);
			lstAppAbsence.add(appAbsence);
		}
		//振休振出申請: get full info(10);
		List<String> lstSyncId = new ArrayList<>();
		for (Application_New app : lstCompltLeave) {
			if(lstSyncId.contains(app.getAppID())){
				continue;
			}
			AppCompltLeaveFull appMain = null;
			AppCompltLeaveFull appSub = null;
			String appDateSub = null;
			String appInputSub = null;
			//アルゴリズム「申請一覧リスト取得振休振出」を実行する-(get List App Complement Leave): 6 - 申請一覧リスト取得振休振出
			AppCompltLeaveSyncOutput sync = this.getListAppComplementLeave(app, companyId);
			if(!sync.isSync()){//TH k co don lien ket
				//lay thong tin chi tiet
				appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType());
			}else{//TH co don lien ket
				//lay thong tin chi tiet A
				appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType());
				//check B co trong list don xin k?
				String appIdSync = sync.getType() == 0 ? sync.getRecId() : sync.getAbsId();
				CheckExitSync checkExit = this.checkExitSync(lstCompltLeave, appIdSync);
				if(checkExit.isCheckExit()){//exist
					lstSyncId.add(appIdSync);
					appDateSub = checkExit.getAppDateSub().toString("yyyy/MM/dd");
					appInputSub = checkExit.getInputDateSub().toString("yyyy/MM/dd HH:mm");
				}else{//not exist
					//lay thong tin chung
					Application_New sub = repoApp.findByID(companyId, appIdSync).get();
					appDateSub = sub.getAppDate().toString("yyyy/MM/dd");
					appInputSub = sub.getInputDate().toString("yyyy/MM/dd HH:mm");
				}
				appSub = repoAppDetail.getAppCompltLeaveInfo(companyId, appIdSync, sync.getType() == 0 ? 1 : 0);
			}
			
			lstAppCompltLeaveSync.add(new AppCompltLeaveSync(sync.getType(), sync.isSync(), appMain, appSub, appDateSub, appInputSub));
		}
		List<Application_New> lstCompltSync = lstCompltLeave.stream()
				.filter(c -> !lstSyncId.contains(c.getAppID())).collect(Collectors.toList());
		List<Application_New> lstAppFilter = lstOverTime;
		lstAppFilter.addAll(lstGoBack);
		lstAppFilter.addAll(lstHdWork);
		lstAppFilter.addAll(lstWkChange);
		lstAppFilter.addAll(lstAbsence);
		lstAppFilter.addAll(lstCompltSync);
//		for (Application_New application : lstApp) {
			//アルゴリズム「申請一覧リスト取得打刻取消」を実行する-(Cancel get list app stamp): 7 - 申請一覧リスト取得打刻取消
//			this.getListAppStampIsCancel(application);
			//アルゴリズム「申請一覧リスト取得振休振出」を実行する-(get List App Complement Leave): 6 - 申請一覧リスト取得振休振出
//			this.getListAppComplementLeave(application);
			//アルゴリズム「申請一覧リスト取得休暇」を実行する-(get List App Absence): 8 - 申請一覧リスト取得休暇
//			this.getListAppAbsence(application);
//		}
		//imported(申請承認）「稟議書」を取得する - wait
		//アルゴリズム「申請一覧リスト取得マスタ情報」を実行する(get List App Master Info): 9 - 申請一覧リスト取得マスタ情報
		DataMasterOutput lstAppMasterInfo = this.getListAppMasterInfo(lstAppFilter, companyId, new DatePeriod(param.getStartDate(), param.getEndDate()));
		//申請日付順でソートする
		
		// TODO Auto-generated method stub
		
		//get status app
//		List<ApplicationFullOutput> lstAppFull = this.findStatusAPp(lstAppFilter);
		return new AppListOutPut(lstAppMasterInfo, lstAppFilter, lstAppOt, lstAppGoBack,lstAppHdWork, 
				lstAppWkChange,lstAppAbsence,lstAppCompltLeaveSync, null, null, null, null, null, null);// NOTE
	}
	/**
	 * 2.1 - 申請一覧対象申請者取得
	 * @param param
	 * @return
	 */
	@Override
	public ListApplicantOutput getListApplicantForListApp(AppListExtractCondition param) {
		String sIdLogin = AppContexts.user().employeeId();
		//自分の申請＝False（初期状態）
		boolean mySelf = false;
		List<String> lstSID = new ArrayList<>();
		//申請一覧抽出条件. 社員IDリストを確認
		if(param.getListEmployeeId().isEmpty()){//リストが存在しない場合
			//社員IDリストにログイン者の社員IDをセットする
			lstSID.add(sIdLogin);
			//自分の申請＝True
			mySelf = true;
		}else{//リストが存在する場合
			//社員IDリストが１件でログイン者IDの場合
			if(param.getListEmployeeId().size() == 1 && param.getListEmployeeId().get(0).equals(sIdLogin)){//1件でログイン者IDだった場合
				//自分の申請と判断する
				//自分の申請＝True
				mySelf = true;
			}
			lstSID = param.getListEmployeeId();	
		}
		return new ListApplicantOutput(mySelf, lstSID);
	}
	/**
	 * 3 - 申請一覧リスト取得承認
	 */
	@Override
	public AppListOutPut getAppListByApproval(AppListExtractCondition param, ApprovalListDisplaySetting displaySet) {
		String companyId = AppContexts.user().companyId();
		String sID = AppContexts.user().employeeId();
//		GeneralDate baseDate = GeneralDate.today();
		//ドメインモデル「承認機能設定」を取得する-(Lấy dữ liệu domain 承認機能設定) - wait hoi lai ben nhat
		//comment code - EA2237
//		//申請一覧抽出条件.申請表示対象が「部下の申請」が指定-(Check đk lọc)
//		List<String> lstEmployeeIdSub = new ArrayList<>();
//		if(param.getAppDisplayAtr().equals(ApplicationDisplayAtr.APP_SUB)){//部下の申請の場合
//			//アルゴリズム「自部門職場と配下の社員をすべて取得する」を実行する - wait request 243
//			lstEmployeeIdSub = employeeAdapter.getListSid(sID, baseDate);
//		}
		//申請一覧抽出条件.申請表示対象が「事前通知」または「検討指示」が指定
		List<Application_New> lstApp = new ArrayList<>();
			//ドメインモデル「代行者管理」を取得する-(Lấy dữ liệu domain 代行者管理) - wait request 244
			List<AgentDataRequestPubImport> lstAgent = agentAdapter.lstAgentData(companyId, sID, param.getStartDate(), param.getEndDate());
			List<String> lstEmp = new ArrayList<>();
			for (AgentDataRequestPubImport agent : lstAgent) {
				lstEmp.add(agent.getEmployeeId());
			}
			//ドメインモデル「申請」を取得する-(Lấy dữ liệu domain 申請) - get List App By Reflect
			lstApp = repoApp.getListAppByReflect(companyId, param.getStartDate(), param.getEndDate());
			//loc du lieu
			//imported（申請承認）「承認ルートの内容」を取得する - RequestList309
			System.out.println("before merger: "+ LocalDateTime.now());
			List<ApplicationFullOutput> lstAppFull = this.mergeAppAndPhase(lstApp, companyId);
			System.out.println("after merger: "+ LocalDateTime.now());
			//条件１： ログイン者の表示対象の基本条件
			List<ApplicationFullOutput> lstAppFullFil1 = new ArrayList<>();
			for (ApplicationFullOutput appFull : lstAppFull) {
				ApproverStt appstt = this.filterConditions1(appFull, lstAgent, sID);
				if(appstt.isCheck()){
					appFull.setApprId(appstt.getApprId());
					lstAppFullFil1.add(appFull);
				}
			}
			//Detele 条件2 - EA2237
			//条件2: 申請者の指定条件
//			List<ApplicationFullOutput> lstAppFilter2 = new ArrayList<>();
//			for (ApplicationFullOutput app : lstAppFullFil1) {
//				//「全て」の場合
//				if(param.getAppDisplayAtr().equals(ApplicationDisplayAtr.ALL_APP)){
//					lstAppFilter2.add(app);
//				}
//				//「自分の申請」の場合
//				if(param.getAppDisplayAtr().equals(ApplicationDisplayAtr.APP_MYSELF)){
//					if(app.getApplication().getEmployeeID().equals(sID)){
//						lstAppFilter2.add(app);
//					}
//				}
//				//「部下の申請」の場合
//				if(param.getAppDisplayAtr().equals(ApplicationDisplayAtr.APP_SUB)){
//					if(lstEmployeeIdSub.contains(app.getApplication().getEmployeeID())){
//						lstAppFilter2.add(app);
//					}
//				}
//				//「承認する申請」の場合
//				if(param.getAppDisplayAtr().equals(ApplicationDisplayAtr.APP_APPROVED)){
//					lstAppFilter2.add(app);
//				}
//			}
			//条件３：承認区分の指定条件
			List<Application_New> lstAppFilter3 = new ArrayList<>();
			List<ApplicationFullOutput> lstAppFullFilter3 = new ArrayList<>();
			List<String> lstFrameUn = new ArrayList<>();
			List<PhaseStatus> lstPhaseStatus = new ArrayList<>();
			
			//Replace lstAppFilter2 -> lstAppFullFil1: EA2237
			for (ApplicationFullOutput appFull : lstAppFullFil1) {
				ReflectedState_New state = appFull.getApplication().getReflectionInformation().getStateReflectionReal();
				PhaseFrameStatus statusApr = this.findPhaseFrameStatus(appFull.getLstPhaseState(), sID);
				//TH agent
				PhaseFrameStatus statusAg = appFull.getApprId() == null ? null : this.findPhaseFrameStatus(appFull.getLstPhaseState(), appFull.getApprId());
				boolean check = false;
				PhaseFrameStatus status = statusAg == null ? statusApr : statusApr.getFrameStatus() == null ? statusAg : statusApr;
				if(status.getFrameStatus() == null && statusApr.getPhaseStatus() == null){
						continue;
				}
				//申請一覧共通設定.承認状況＿未承認がチェックあり(True)の場合 - A4_1_1: check
				//「承認する申請」の場合
				if(param.isUnapprovalStatus() && state.equals(ReflectedState_New.NOTREFLECTED) || param.getAppDisplayAtr().equals(ApplicationDisplayAtr.APP_APPROVED)){
					if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
							&& status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){
						check = true;
					}
				}
				//申請一覧共通設定.承認状況＿承認がチェックあり(True)の場合 - A4_1_2: check
				if(param.isApprovalStatus()){
					if(( state.equals(ReflectedState_New.NOTREFLECTED)|| state.equals(ReflectedState_New.WAITREFLECTION) || state.equals(ReflectedState_New.REFLECTED))
							&& (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) || status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)) 
							&& status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){
						check = true;
					}
				}
				//申請一覧共通設定.承認状況＿否認がチェックあり(True)の場合 - A4_1_3: check
				if(param.isDenialStatus() && state.equals(ReflectedState_New.DENIAL)){
					check = true;
				}
				//申請一覧共通設定.承認状況＿代行承認済がチェックあり(True)の場合 - A4_1_4: check
				if(param.isAgentApprovalStatus()&& (status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) 
						|| status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED))){//承認フェーズ.承認区分　＝　未承認または承認済
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)
							&& Strings.isNotBlank(status.getAgentId()) && !status.getAgentId().equals(sID)){
						check = true;
					}
				}
				//申請一覧共通設定.承認状況＿差戻がチェックあり(True)の場合 - A4_1_5: check
				if(param.isRemandStatus() && state.equals(ReflectedState_New.NOTREFLECTED)){
					if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.REMAND)){
						check = true;
					}
				}
				//申請一覧共通設定.承認状況＿取消がチェックあり(True)の場合 - A4_1_6: check
				if(param.isCancelStatus() && (state.equals(ReflectedState_New.CANCELED) ||
								state.equals(ReflectedState_New.WAITCANCEL))){//反映状態.実績反映状態　＝　取消または取消待ち
					check = true;
				}
				if(check){
					//条件 bo sung: phase truoc do phai duoc approval thi moi hien thi don
					int phaseOrderCur = status.getPhaseOrder().intValue();
					PhaseStatus statusPhase = this.convertStatusPhase(appFull.getApplication().getAppID(), appFull.getLstPhaseState());
					if(phaseOrderCur == 1 || this.checkApprove(statusPhase, phaseOrderCur)){//phase truoc do da approve
						lstAppFilter3.add(appFull.getApplication());
						lstAppFullFilter3.add(appFull);
						if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){
							lstFrameUn.add(appFull.getApplication().getAppID());
						}
						
						lstPhaseStatus.add(statusPhase);
					}
				}
			}
			//条件５：重複承認の対応条件
			
			
			List<Application_New> lstOverTime = lstAppFilter3.stream().filter(c -> c.isAppOverTime()).collect(Collectors.toList());
			List<Application_New> lstGoBack = lstAppFilter3.stream().filter(d -> d.isAppGoBack()).collect(Collectors.toList());
			List<Application_New> lstHdWork = lstAppFilter3.stream().filter(d -> d.isAppHdWork()).collect(Collectors.toList());
			List<Application_New> lstWkChange = lstAppFilter3.stream().filter(d -> d.isAppWkChange()).collect(Collectors.toList());
			List<Application_New> lstAbsence = lstAppFilter3.stream().filter(d -> d.isAppAbsence()).collect(Collectors.toList());
			List<Application_New> lstCompltLeave = lstAppFilter3.stream().filter(d -> d.isAppCompltLeave()).collect(Collectors.toList());
			
			List<AppOverTimeInfoFull> lstAppOt = new ArrayList<>();
			List<AppGoBackInfoFull> lstAppGoBack = new ArrayList<>();
			List<AppHolidayWorkFull> lstAppHdWork = new ArrayList<>();
			List<AppWorkChangeFull> lstAppWorkChange = new ArrayList<>();
			List<AppAbsenceFull> lstAppAbsence = new ArrayList<>();
			List<AppCompltLeaveSync> lstAppCompltLeaveSync = new ArrayList<>();
			//get info full
			//残業申請: get full info (0)
			List<String> lstAppOtID = lstOverTime.stream().map(c -> c.getAppID()).collect(Collectors.toList());
			if(!lstAppOtID.isEmpty()){
				lstAppOt = repoAppDetail.getListAppOverTimeInfo(companyId, lstAppOtID);
			}
			//直行直帰申請: get full info (4)
			List<String> lstAppGoBackID = lstGoBack.stream().map(c -> c.getAppID()).collect(Collectors.toList());
			if(!lstAppGoBackID.isEmpty()){
				lstAppGoBack = repoAppDetail.getListAppGoBackInfo(companyId, lstAppGoBackID);
			}
			//休日出勤時間申請: get full info(6);
			List<String> lstAppHdID = lstHdWork.stream().map(c -> c.getAppID()).collect(Collectors.toList());
			if(!lstAppHdID.isEmpty()){
				lstAppHdWork = repoAppDetail.getListAppHdWorkInfo(companyId, lstAppHdID);
			}
			//勤務変更申請: get full info (2)
			List<String> lstAppWkChangeID = lstWkChange.stream().map(c -> c.getAppID()).collect(Collectors.toList());
			if(!lstAppWkChangeID.isEmpty()){
				lstAppWorkChange = repoAppDetail.getListAppWorkChangeInfo(companyId, lstAppWkChangeID);
			}
			//休暇申請: get full info (1)
			for (Application_New app : lstAbsence) {
				Integer day = 0;
				if(app.getStartDate().isPresent()&& app.getEndDate().isPresent()){
					day = app.getStartDate().get().daysTo(app.getEndDate().get()) + 1;
				}
				AppAbsenceFull appAbsence = repoAppDetail.getAppAbsenceInfo(companyId, app.getAppID(), day);
				lstAppAbsence.add(appAbsence);
			}
			//振休振出申請: get full info(10);
			List<String> lstSyncId = new ArrayList<>();
			for (Application_New app : lstCompltLeave) {
				if(lstSyncId.contains(app.getAppID())){
					continue;
				}
				AppCompltLeaveFull appMain = null;
				AppCompltLeaveFull appSub = null;
				String appDateSub = null;
				String appInputSub = null;
				//アルゴリズム「申請一覧リスト取得振休振出」を実行する-(get List App Complement Leave): 6 - 申請一覧リスト取得振休振出
				AppCompltLeaveSyncOutput sync = this.getListAppComplementLeave(app, companyId);
				if(!sync.isSync()){//TH k co don lien ket
					//lay thong tin chi tiet
					appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType());
				}else{//TH co don lien ket
					//lay thong tin chi tiet A
					appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType());
					//check B co trong list don xin k?
					String appIdSync = sync.getType() == 0 ? sync.getRecId() : sync.getAbsId();
					CheckExitSync checkExit = this.checkExitSync(lstCompltLeave, appIdSync);
					if(checkExit.isCheckExit()){//exist
						lstSyncId.add(appIdSync);
						appDateSub = checkExit.getAppDateSub().toString("yyyy/MM/dd");
						appInputSub = checkExit.getInputDateSub().toString("yyyy/MM/dd HH:mm");
					}else{//not exist
						//lay thong tin chung
						Application_New sub = repoApp.findByID(companyId, appIdSync).get();
						appDateSub = sub.getAppDate().toString("yyyy/MM/dd");
						appInputSub = sub.getInputDate().toString("yyyy/MM/dd HH:mm");
					}
					appSub = repoAppDetail.getAppCompltLeaveInfo(companyId, appIdSync, sync.getType() == 0 ? 1 : 0);
				}
				
				lstAppCompltLeaveSync.add(new AppCompltLeaveSync(sync.getType(), sync.isSync(), appMain, appSub, appDateSub, appInputSub));
			}
			List<Application_New> lstCompltSync = lstCompltLeave.stream()
					.filter(c -> !lstSyncId.contains(c.getAppID())).collect(Collectors.toList());
			List<Application_New> lstAppFilter = lstOverTime;
			lstAppFilter.addAll(lstGoBack);
			lstAppFilter.addAll(lstHdWork);
			lstAppFilter.addAll(lstWkChange);
			lstAppFilter.addAll(lstAbsence);
			lstAppFilter.addAll(lstCompltSync);
			//アルゴリズム「申請一覧リスト取得出張」を実行する
			// TODO Auto-generated method stub
//		}
		//imported(申請承認）「稟議書」を取得する - wait request : return list app - tam thoi bo qua
		// TODO Auto-generated method stub
		//アルゴリズム「申請一覧リスト取得マスタ情報」を実行する(get List App Master Info): 9 - 申請一覧リスト取得マスタ情報
		DataMasterOutput lstMaster = this.getListAppMasterInfo(lstAppFilter, companyId, new DatePeriod(param.getStartDate(), param.getEndDate()));
		//アルゴリズム「申請一覧リスト取得実績」を実行する-(get App List Achievement): 5 - 申請一覧リスト取得実績
		//loai bo nhung don dong bo
		List<ApplicationFullOutput> lstCount = lstAppFullFilter3.stream()
				.filter(c -> !lstSyncId.contains(c.getApplication().getAppID())).collect(Collectors.toList());
		List<AppCompltLeaveSync> lstSync = lstAppCompltLeaveSync.stream()
				.filter(c -> c.isSync()).collect(Collectors.toList());
		AppListAtrOutput timeOutput = this.getAppListAchievement(lstCount, displaySet, companyId, sID, lstSync);
		//承認一覧に稟議書リスト追加し、申請日付順に整列する - phu thuoc vao request
		// TODO Auto-generated method stub
		return new AppListOutPut(lstMaster, lstAppFilter, lstAppOt, lstAppGoBack,lstAppHdWork, lstAppWorkChange,
				lstAppAbsence, lstAppCompltLeaveSync, timeOutput.getAppStatus(),timeOutput.getLstAppFull(), timeOutput.getLstAppColor(), 
				lstFrameUn, lstPhaseStatus, timeOutput.getLstAppGroup());
	}
	/**
	 * check phase cur is display??
	 * @param statusPhase
	 * @param phaseOrderCur
	 * @return
	 */
	private boolean checkApprove(PhaseStatus statusPhase, int phaseOrderCur){
		List<Integer> phaseAtr = statusPhase.getPhaseAtr();
		Integer tmp = phaseOrderCur-2;//index phase(n-1) before phase current(n)
		Integer stt = null;
		if(tmp < 0){
			return true;
		}
		boolean check = false;
		for(int i = tmp; i >= 0; i--){//loop phase (n-1) -> phase 1
			if(phaseAtr.get(i) != null){//phase setting
				stt = phaseAtr.get(i);
				check = true;
				return check ? stt == 1 ? true : false : true;
			}
		}
		return true;
	}
	/**
	 * lam o ui
	 * 4 - 申請一覧リスト取得承認件数
	 */
	@Override
	public AppInfoStatus countAppListApproval(List<ApplicationFullOutput> lstAppFull, String sID, List<AppCompltLeaveSync> lstSync) {
		ApplicationStatus appStatus = new ApplicationStatus(0,0,0,0,0,0);
//		List<ApplicationFullOutput> lstAppFull = mergeAppAndPhase(lstApp);
		for (ApplicationFullOutput appFull : lstAppFull) {
			PhaseFrameStatus status = appFull.getApprId() == null ? this.findPhaseFrameStatus(appFull.getLstPhaseState(), sID) : this.findPhaseFrameStatus(appFull.getLstPhaseState(), appFull.getApprId());
			if(status.getFrameStatus() == null && status.getPhaseStatus() == null){
				appFull.setStatus(null);
			}else{
				int statusApp = this.calApplication(appFull.getApplication().getReflectionInformation().getStateReflectionReal(), status, status.getAgentId(), sID);
				appFull.setStatus(statusApp);
				appFull.setAgentId(status.getAgentId());
			}
		}
//		List<ApplicationFullOutput> lstAppFull = this.findStatusAPp(lstApp);
		
		List<ApplicationFullOutput> listApp = lstAppFull.stream().filter(c -> c.getStatus() != null).collect(Collectors.toList());
		for (ApplicationFullOutput appFull : listApp) {
			//check co sync k?
			boolean check = this.checkSync(lstSync, appFull.getApplication().getAppID());
			int add = check ? 2 : 1;
			switch(appFull.getStatus()){
				case 1://承認状況＝否
					//否認件数に＋１する
					appStatus.setDenialNumber(appStatus.getDenialNumber() + add);
					break;
				case 2://承認状況＝差戻
					//差戻件数に＋１する
					appStatus.setRemandNumner(appStatus.getRemandNumner() + add);
					break;
				case 3://承認状況＝取消
					//取消件数に＋１する
					appStatus.setCancelNumber(appStatus.getCancelNumber() + add);
					break;
				case 4://承認状況＝承認済み/反映済み
					if(StringUtil.isNullOrEmpty(appFull.getAgentId(), true) || appFull.getAgentId().equals(sID)){//代行者＝未登録　または　代行者＝ログインID
						appStatus.setApprovalNumber(appStatus.getApprovalNumber() + add);
					}else{//代行者≠ログインID
						appStatus.setApprovalAgentNumber(appStatus.getApprovalAgentNumber() + add);
					}
					break;
				case 5://承認状況＝未
					//未承認件数に＋１する
					appStatus.setUnApprovalNumber(appStatus.getUnApprovalNumber() + add);
					break;
				default:
					break;
			}
		}
		return new AppInfoStatus(lstAppFull, appStatus);
	}
	private boolean checkSync(List<AppCompltLeaveSync> lstSync, String appId){
		for (AppCompltLeaveSync appSync : lstSync) {
			if(appSync.getAppMain().getAppID().equals(appId)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 5 - 申請一覧リスト取得実績
	 */
	@Override
	public AppListAtrOutput getAppListAchievement(List<ApplicationFullOutput> lstAppFull, ApprovalListDisplaySetting displaySet, String companyId, String sIDLogin, List<AppCompltLeaveSync> lstSync) {
		List<ApplicationFullOutput> lstOtPost = lstAppFull.stream().filter(c -> c.getApplication().isAppOverTime())
				.filter(c->c.getApplication().getPrePostAtr().equals(PrePostAtr.POSTERIOR))
				.collect(Collectors.toList());
		List<ApplicationFullOutput> lstHdPost = lstAppFull.stream().filter(c -> c.getApplication().isAppHdWork())
				.filter(c->c.getApplication().getPrePostAtr().equals(PrePostAtr.POSTERIOR)).collect(Collectors.toList());
		List<CheckColorTime> lstColorTime = new ArrayList<>();
		//事後申請で且申請種類が「残業申請」または「休出時間申請」の場合 (Xin sau của xin làm thêm hoặc làm ngày nghỉ)
		List<AppPrePostGroup> lstAppGroup = new ArrayList<>();
		//残業申請の事後の場合
		for (ApplicationFullOutput appPost : lstOtPost) {
				//承認一覧表示設定.残業の事前申請
				String appID = appPost.getApplication().getAppID();
				String sID = appPost.getApplication().getEmployeeID();
				GeneralDate appDate = appPost.getApplication().getAppDate();
				AppPrePostGroup group = null;
				AppOverTimeInfoFull appOtPost = repoAppDetail.getAppOverTimeInfo(companyId, appID);
				CheckColorTime checkColor = null;
				AppOverTimeInfoFull appPre = null;
				String reasonAppPre = "";
				if(displaySet.getOtAdvanceDisAtr().equals(DisplayAtr.DISPLAY)){//表示する
					//ドメインモデル「申請」を取得する
					//※2018/04/17
					//複数存在する場合は、最後に新規登録された内容を対象とする
					List<Application_New> lstAppPre = repoApp.getApp(sID, appDate, PrePostAtr.PREDICT.value, ApplicationType.OVER_TIME_APPLICATION.value);
					if(lstAppPre.isEmpty()){
//						lstColorTime.add(new CheckColorTime(appID, 1));
						checkColor = new CheckColorTime(appID, 1);
					}else{
						appPre = repoAppDetail.getAppOverTimeInfo(companyId, lstAppPre.get(0).getAppID());
						reasonAppPre = lstAppPre.get(0).getAppReason().v();
						if(lstAppPre.get(0).getReflectionInformation().getStateReflectionReal().equals(ReflectedState_New.DENIAL)){
							checkColor = new CheckColorTime(appID, 1);
						}else{
							boolean checkPrePostColor = this.checkPrePostColor(appPre.getLstFrame(), appOtPost.getLstFrame());
							if(checkPrePostColor){
								checkColor = new CheckColorTime(appID, 1);
							}
						}
					}
					if(!lstAppPre.isEmpty()){
						group = new AppPrePostGroup(lstAppPre.get(0).getAppID(), appID, null,"","","","", appPre, reasonAppPre, null);
					}
				}
				//承認一覧表示設定.残業の実績
				if(displaySet.getOtActualDisAtr().equals(DisplayAtr.DISPLAY)){//表示する
					//アルゴリズム「申請一覧リスト取得実績残業申請」を実行する-(5.2)
					List<OverTimeFrame> time = appOtPost.getLstFrame();
					List<OverTimeFrame> lstFrameRestTime = this.findRestTime(time);
					List<Integer> lstRestStart = new ArrayList<>();
					List<Integer> lstRestEnd = new ArrayList<>();
					for (OverTimeFrame restTime : lstFrameRestTime) {
						lstRestStart.add(restTime.getStartTime());
						lstRestEnd.add(restTime.getEndTime());
					}
					TimeResultOutput result = this.getAppListAchievementOverTime(sID, appDate, time, lstRestStart, lstRestEnd);
					if(result.isCheckColor()){
						if(this.checkExistColor(lstColorTime, appID)){
							checkColor.setColorAtr(2);
						}else{
							checkColor = new CheckColorTime(appID, 2);
						}
					}
					if(group != null){
						group.setTime(result.getLstFrameResult());
						group.setStrTime1(result.getStrTime1());
						group.setEndTime1(result.getEndTime1());
						group.setStrTime2(result.getStrTime2());
						group.setEndTime2(result.getEndTime2());
						//NOTE
					}else{
						group = new AppPrePostGroup("", appID, result.getLstFrameResult(),"","","","", appPre, reasonAppPre, null);
					}
				}
				if(group != null){
					lstAppGroup.add(group);
				}
				if(checkColor != null){
					lstColorTime.add(checkColor);
				}
		}
		//休出時間申請の事後の場合
		for (ApplicationFullOutput appPost : lstHdPost) {
			String appID = appPost.getApplication().getAppID();
			String sID = appPost.getApplication().getEmployeeID();
			GeneralDate appDate = appPost.getApplication().getAppDate();
			AppPrePostGroup group = null;
			AppHolidayWorkFull appHdPost = repoAppDetail.getAppHolidayWorkInfo(companyId, appID);
			CheckColorTime checkColor = null;
			AppHolidayWorkFull appPre = null;
			String reasonAppPre = "";
			//承認一覧表示設定.休出の事前申請
			if(displaySet.getHwAdvanceDisAtr().equals(DisplayAtr.DISPLAY)){//表示する
				//ドメインモデル「申請」を取得する
				List<Application_New> lstAppPre = repoApp.getApp(sID, appDate, PrePostAtr.PREDICT.value, ApplicationType.BREAK_TIME_APPLICATION.value);
				if(lstAppPre.isEmpty()){
					checkColor = new CheckColorTime(appID, 1);
				}else{
					appPre = repoAppDetail.getAppHolidayWorkInfo(companyId, lstAppPre.get(0).getAppID());
					reasonAppPre = lstAppPre.get(0).getAppReason().v();
					if(lstAppPre.get(0).getReflectionInformation().getStateReflectionReal().equals(ReflectedState_New.DENIAL)){
						checkColor = new CheckColorTime(appID, 1);
					}else{
						boolean checkPrePostColor = this.checkPrePostColor(appPre.getLstFrame(), appHdPost.getLstFrame());
						if(checkPrePostColor){
							checkColor = new CheckColorTime(appID, 1);
						}
					}
				}
				if(!lstAppPre.isEmpty()){
					group = new AppPrePostGroup(lstAppPre.get(0).getAppID(), appID, null,"","","","", null, reasonAppPre, appPre);
				}
			}
			//承認一覧表示設定.休出の実績
			if(displaySet.getHwActualDisAtr().equals(DisplayAtr.DISPLAY)){//表示する
				//アルゴリズム「申請一覧リスト取得実績残業申請」を実行する-(5.2)
				List<OverTimeFrame> time = appHdPost.getLstFrame();
				TimeResultOutput result = this.getAppListAchievementBreak(sID, appDate, time);
				if(result.isCheckColor()){
					if(this.checkExistColor(lstColorTime, appID)){
						checkColor.setColorAtr(2);
					}else{
						checkColor = new CheckColorTime(appID, 2);
					}
				}
				if(group != null){
					group.setTime(result.getLstFrameResult());
				}else{
					group = new AppPrePostGroup("", appID, result.getLstFrameResult(),"","","","", null, reasonAppPre, appPre);
				}
			}
			if(group != null){
				lstAppGroup.add(group);
			}
			if(checkColor != null){
				lstColorTime.add(checkColor);
			}
		}
		
		
		
		
		
		//「残業申請」または「休出時間申請」の事後以外の場合 (Không phải xin sau)
//		for (Application_New appDif : lstDif) {
//			//アルゴリズム「申請一覧リスト取得打刻取消」を実行する-(getListAppStampIsCancel 7)
//			this.getListAppStampIsCancel(appDif);
//			//アルゴリズム「申請一覧リスト取得振休振出」を実行する (get List App Complement Leave - 6)
//			this.getListAppComplementLeave(appDif);
//			//アルゴリズム「申請一覧リスト取得休暇」を実行する (get List App Absence - 8)
//			this.getListAppAbsence(appDif);
//		}
		//アルゴリズム「申請一覧リスト取得承認件数」を実行する(countAppListApproval): 4 -   申請一覧リスト取得承認件数
		AppInfoStatus appStatus = this.countAppListApproval(lstAppFull, sIDLogin, lstSync);
		// TODO Auto-generated method stub
		return new AppListAtrOutput(appStatus.getLstAppFull(), appStatus.getCount(), lstColorTime, lstAppGroup);
	}
	private List<OverTimeFrame> findRestTime(List<OverTimeFrame> lstFrame){
		List<OverTimeFrame> lstRestTime = new ArrayList<>();
		for (OverTimeFrame frame : lstFrame) {
			if(frame.getAttendanceType() == 0){//休出時間 - RESTTIME
				lstRestTime.add(frame);
			}
		}
		return lstRestTime;
	}
	/**
	 * 5.1 - 申請一覧リスト取得実績休出申請
	 * sID: 申請者ID
	 * date: 申請日付
	 */
	@Override
	public TimeResultOutput getAppListAchievementBreak(String sID, GeneralDate date, List<OverTimeFrame> time) {
		// TODO Auto-generated method stub
		//Imported(申請承認)「勤務実績」を取得する - req #5
		RecordWorkInfoImport record = recordWkpInfoAdapter.getRecordWorkInfo(sID, date);
		//Imported(申請承認)「勤務予定」を取得する - req #4
//		Optional<ScBasicScheduleImport> scBsSchedule = scBasicScheduleAdapter.findByID(sID, date);
		//計算休日出勤
		List<OvertimeInputCaculation> lstHdCal = record.getOvertimeHolidayCaculation();
		boolean checkColor = false;
		List<OverTimeFrame> lstFrameResult = new ArrayList<>();
		for (OvertimeInputCaculation timeCal : lstHdCal) {
			OverTimeFrame a = this.findFrameTime(time, timeCal.getFrameNo(), timeCal.getAttendanceID());
			if(a == null){
				continue;
			}
			if(timeCal.getResultCaculation() < a.getApplicationTime()){
				checkColor = true;
			}
			OverTimeFrame frameRes = a;
			frameRes.setApplicationTime(timeCal.getResultCaculation());
			lstFrameResult.add(frameRes);
		}
		return new TimeResultOutput(checkColor, lstFrameResult, repoAppDetail.convertTime(record.getAttendanceStampTimeFirst()),
				repoAppDetail.convertTime(record.getLeaveStampTimeFirst()),	repoAppDetail.convertTime(record.getAttendanceStampTimeSecond()),
				repoAppDetail.convertTime(record.getLeaveStampTimeSecond()));
	}
	private OverTimeFrame findFrameTime(List<OverTimeFrame> time, int frameNo, int attendanceType){
		for (OverTimeFrame overTimeFrame : time) {
			if(overTimeFrame.getFrameNo() == frameNo && overTimeFrame.getAttendanceType() == attendanceType){
				return overTimeFrame;
			}
		}
		return null;
	}
	/**
	 * 5.2 - 申請一覧リスト取得実績残業申請
	 */
	@Override
	public TimeResultOutput getAppListAchievementOverTime(String sID, GeneralDate date, List<OverTimeFrame> time, 
			List<Integer> lstRestStart, List<Integer> lstRestEnd) {
		//Imported(申請承認)「勤務実績」を取得する - req #5
		RecordWorkInfoImport record = recordWkpInfoAdapter.getRecordWorkInfo(sID, date);
		DailyAttendanceTimeCaculationImport cal = calTime.getCalculation(sID, date, record.getWorkTypeCode(), record.getWorkTimeCode(),
				record.getAttendanceStampTimeFirst(), record.getLeaveStampTimeFirst(), lstRestStart, lstRestEnd);
		//Imported(申請承認)「計算残業時間」を取得する - req #23
		boolean checkColor = false;
		List<OverTimeFrame> lstFrameResult = new ArrayList<>();
		for(Map.Entry<Integer,TimeWithCalculationImport> entry : cal.getOverTime().entrySet()){
			for(OverTimeFrame i : time){
				if(i.getFrameNo() == entry.getKey() && i.getAttendanceType() == 1){
					int a =	entry.getValue().getCalTime();
					if(a < i.getApplicationTime()){
						checkColor = true;
					}
					OverTimeFrame frameRes = i;
					frameRes.setApplicationTime(a);
					lstFrameResult.add(frameRes);
				}
			}
		}
		// TODO Auto-generated method stub
		//Imported(申請承認)「計算休出時間」を取得する - req #23
		for(Map.Entry<Integer,TimeWithCalculationImport> entry : cal.getHolidayWorkTime().entrySet()){
			for(OverTimeFrame i : time){
				if(i.getFrameNo() == entry.getKey() && i.getAttendanceType() == 0){
					int a =	entry.getValue().getCalTime();
					if(a < i.getApplicationTime()){
						checkColor = true;
					}
					OverTimeFrame frameRes = i;
					frameRes.setApplicationTime(a);
					lstFrameResult.add(frameRes);
				}
			}
		}
		// TODO Auto-generated method stub
		//Imported(申請承認)「計算加給時間」を取得する - req #23
		for(Map.Entry<Integer,Integer> entry : cal.getBonusPayTime().entrySet()){
			for(OverTimeFrame i : time){
				if(i.getFrameNo() == entry.getKey() && i.getAttendanceType() == 3){
					int a =	entry.getValue().intValue();
					if(a < i.getApplicationTime()){
						checkColor = true;
					}
					OverTimeFrame frameRes = i;
					frameRes.setApplicationTime(a);
					lstFrameResult.add(frameRes);
				}
			}
		}
		for(Map.Entry<Integer,Integer> entry : cal.getSpecBonusPayTime().entrySet()){
			for(OverTimeFrame i : time){
				if(i.getFrameNo() == entry.getKey() && i.getAttendanceType() == 4){
					int a =	entry.getValue().intValue();
					if(a < i.getApplicationTime()){
						checkColor = true;
					}
					OverTimeFrame frameRes = i;
					frameRes.setApplicationTime(a);
					lstFrameResult.add(frameRes);
				}
			}
		}
		return new TimeResultOutput(checkColor, lstFrameResult, repoAppDetail.convertTime(record.getAttendanceStampTimeFirst()),
				repoAppDetail.convertTime(record.getLeaveStampTimeFirst()),	repoAppDetail.convertTime(record.getAttendanceStampTimeSecond()),
				repoAppDetail.convertTime(record.getLeaveStampTimeSecond()));
	}
	/**
	 * 6 - 申請一覧リスト取得振休振出
	 * wait SonLB - kaf011
	 */
	@Override
	public AppCompltLeaveSyncOutput getListAppComplementLeave(Application_New application, String companyId) {
		// TODO Auto-generated method stub
		//Check 申請種類 - appType
		if(!application.isAppCompltLeave()){
			return null;
		}
		String appId = application.getAppID();
		AppCompltLeaveSyncOutput sync = otherCommonAlgorithm.getAppComplementLeaveSync(companyId, appId);
//		if(!sync.isSync()){
//			return null;
//		}
//		return null;
		return sync;
	}

	/**
	 * 7 - 申請一覧リスト取得打刻取消
	 */
	@Override
	public Boolean getListAppStampIsCancel(Application_New application, String companyID) {
		String applicantID = "";
		//申請種類-(Check AppType)
		if(!application.getAppType().equals(ApplicationType.STAMP_APPLICATION)){
			return null;
		}
		//打刻申請.打刻申請モード-(Check 打刻申請モード)
		//get domain 打刻申請
		AppStamp stamp = repoAppStamp.findByAppID(companyID, application.getAppID());
		if(!stamp.getStampRequestMode().equals(StampRequestMode.STAMP_CANCEL)){
			return null;
		}
		//アルゴリズム「実績の取得」を実行する - 13/KAF
		//AchievementOutput achievement = 
				collectAchievement.getAchievement(companyID, applicantID, application.getAppDate());
		//アルゴリズム「勤務実績の取得」を実行する
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 8 - 申請一覧リスト取得休暇
	 * wait - kaf006
	 */
	@Override
	public List<Application_New> getListAppAbsence(Application_New application, String companyID) {
		//申請種類 - check app type
		if(application.isAppAbsence()){//休暇申請以外の場合
			
		}
		//休暇申請の場合
		//String relationshipCd = "";
		// TODO Auto-generated method stub
		//imported(就業.Shared)「続柄」を取得する
//		Optional<Relationship> relShip = repoRelationship.findByCode(companyID, relationshipCd);
		return null;
	}

	/**
	 * 9 - 申請一覧リスト取得マスタ情報
	 */
	@Override
	public DataMasterOutput getListAppMasterInfo(List<Application_New> lstApp, String companyId, DatePeriod period) {
		//ドメインモデル「申請一覧共通設定」を取得する-(Lấy domain Application list common settings)
		Optional<AppCommonSet> appCommonSet = repoAppCommonSet.find();
		ShowName displaySet = appCommonSet.get().getShowWkpNameBelong();
		//ドメインモデル「申請表示名」より申請表示名称を取得する (Lấy Application display name)
		List<AppDispName> appDispName = repoAppDispName.getAll();
		Map<String, SyEmployeeImport> mapEmpInfo = new HashMap<>();
		List<AppMasterInfo> lstAppMasterInfo = new ArrayList<>();
		Map<String, Integer> mapWpkSet = new HashMap<>();
		Map<String, List<WkpInfo>> mapWpkInfo = new HashMap<>();
		List<String> lstSCD = new ArrayList<>();
		Map<String, List<String>> mapAppBySCD = new HashMap<>();//key - SCD, value - lstAppID
		
		//申請一覧リスト　繰返し実行
		for (Application_New app : lstApp) {
			String applicantID = app.getEmployeeID();
			String enteredPersonID = app.getEnteredPersonID();
			//アルゴリズム「社員IDから個人社員基本情報を取得」を実行する - req #1
			//lay ten ng duoc lam don
			SyEmployeeImport empInfo = this.findNamebySID(mapEmpInfo, applicantID);
			String empName = "";
			String empCD = "";
			if(empInfo == null){
				SyEmployeeImport emp = syEmpAdapter.getPersonInfor(applicantID);
				empName = emp.getBusinessName();
				empCD = emp.getEmployeeCode();
				mapEmpInfo.put(app.getEmployeeID(), emp);
			}else{
				empName = empInfo.getBusinessName();
				empCD = empInfo.getEmployeeCode();
			}
			if(!lstSCD.contains(empCD)){
				lstSCD.add(empCD);
			}
			
			
			//lay ten ng tao don
			SyEmployeeImport inpEmpInfo = applicantID.equals(enteredPersonID) ? null : this.findNamebySID(mapEmpInfo, enteredPersonID);
			String inpEmpName = null;//khoi tao = null -> truong hop trung nhau
			if(!app.getEmployeeID().equals(enteredPersonID) && inpEmpInfo == null){
				inpEmpInfo = syEmpAdapter.getPersonInfor(enteredPersonID);
				inpEmpName = inpEmpInfo.getBusinessName();
				mapEmpInfo.put(enteredPersonID, inpEmpInfo);
			}
			if(!app.getEmployeeID().equals(enteredPersonID) && inpEmpInfo != null){
				inpEmpName = inpEmpInfo.getBusinessName();
			}
			//get work place info
			List<WkpInfo> findExitWkp = this.findExitWkp(mapWpkInfo, applicantID);
			if(findExitWkp == null){//chua co trong cache
				//社員指定期間所属職場履歴を取得  - req #168
				WorkPlaceHistBySIDImport wkp = wkpAdapter.findWpkBySIDandPeriod(app.getEmployeeID(), period);
				findExitWkp = wkp.getLstWkpInfo();
				mapWpkInfo.put(applicantID, findExitWkp);
			}
			WkpInfo wkp = null;
			if(!findExitWkp.isEmpty()){
				wkp = this.findWpk(findExitWkp, app.getAppDate());
			}
			String wkpID = wkp == null ? "" : wkp.getWpkID();
			String wkpName = "";
			if(displaySet.equals(ShowName.SHOW)){
				wkpName =  wkp == null ? "" : wkp.getWpkName();
			}
			//get Application Name
			String appDispNameStr = this.findAppName(appDispName, app.getAppType());
			Integer detailSet = 0;
			if(app.isAppOverTime()){//TH: app.isAppAbsence() tam thoi bo qua do tren spec chua su dung
				String wpk = wkpID + app.getAppType().value;
				detailSet = this.finSetByWpkIDAppType(mapWpkSet, wpk);
				if(detailSet != null && detailSet == -1){
					detailSet = this.detailSet(companyId, wkpID, app.getAppType().value, period.end());
					mapWpkSet.put(wpk, detailSet);
				}
			}
			lstAppMasterInfo.add(new AppMasterInfo(app.getAppID(), app.getAppType().value, appDispNameStr,
					empName, inpEmpName, wkpName, false, null, false, 0, detailSet, empCD));
		}
		for (String sCD : lstSCD) {
			List<String> lstAppID = new ArrayList<>();
			for (AppMasterInfo master : lstAppMasterInfo) {
				if(master.getEmpSD().equals(sCD)){
					lstAppID.add(master.getAppID());
				}
			}
			mapAppBySCD.put(sCD, lstAppID);
		}
		return new DataMasterOutput(lstAppMasterInfo, lstSCD, mapAppBySCD);
	}
	//tim xem da tung di lay thong tin wkp cua nhan vien nay chua
	private List<WkpInfo> findExitWkp(Map<String, List<WkpInfo>> mapWpkInfo, String sID){
		 return mapWpkInfo.containsKey(sID)? mapWpkInfo.get(sID) : null;
	}
	//tim tai thoi diem lam don nv do thuoc wkp nao
	private WkpInfo findWpk(List<WkpInfo> lstWpkInfo, GeneralDate appDate){
		for (WkpInfo wpkHist : lstWpkInfo) {
			if(wpkHist.getDatePeriod().start().beforeOrEquals(appDate) 
					&& wpkHist.getDatePeriod().end().afterOrEquals(appDate)){
				return wpkHist;
			}
		}
		return null;
	}
	//tim ten hien thi loai don xin
	private String findAppName(List<AppDispName> appDispName, ApplicationType appType){
		for (AppDispName appName : appDispName) {
			if(appName.getAppType().value == appType.value){
				return appName.getDispName().v();
			}
		}
		return "";
	}
	//tim ten nhan vien
	private SyEmployeeImport findNamebySID(Map<String, SyEmployeeImport> mapEmpInfo, String sID){
		return mapEmpInfo.containsKey(sID)? mapEmpInfo.get(sID) : null;
	}
	//fin setting hien thi thoi gian overtime va absense
	private Integer finSetByWpkIDAppType(Map<String, Integer> mapWpkSet, String wpk){
		return mapWpkSet.containsKey(wpk)? mapWpkSet.get(wpk) : -1;
	}
//	public List<AppMasterInfo> getListAppMasterInfo(List<Application_New> lstApp, String companyId, DatePeriod period) {
//		//ドメインモデル「申請一覧共通設定」を取得する-(Lấy domain Application list common settings) - wait YenNTH
//		Optional<AppCommonSet> appCommonSet = repoAppCommonSet.find();
//		ShowName displaySet = appCommonSet.get().getShowWkpNameBelong();
//		List<AppMasterInfo> lstAppMasterInfo = new ArrayList<>();
//		//申請一覧リスト　繰返し実行
//		for (Application_New app : lstApp) {
//			//ドメインモデル「申請表示名」より申請表示名称を取得する-(Lấy Application display name) - wait YenNTH
//			Optional<AppDispName> appDispName = repoAppDispName.getDisplay(app.getAppType().value);
//			//アルゴリズム「社員IDから個人社員基本情報を取得」を実行する - req #1
//			String empName = "";
//			String inpEmpName = null;
////			if(displaySet.equals(ShowName.SHOW)){
//				 empName = empRequestAdapter.getEmployeeName(app.getEmployeeID());
//				 inpEmpName = app.getEmployeeID().equals(app.getEnteredPersonID()) ? null : empRequestAdapter.getEmployeeName(app.getEnteredPersonID());
////			}
//			
//			// TODO Auto-generated method stub
//			//アルゴリズム「社員から職場を取得する」を実行する - req #30
//			WkpHistImport wkp = wkpAdapter.findWkpBySid(app.getEmployeeID(), app.getAppDate());
//			String wkpID = "";
//			String wkpName = "";
//			if(wkp != null){
//				wkpID = wkp.getWorkplaceId();
//			}
//			
//			if(displaySet.equals(ShowName.SHOW) && wkp != null){
//				wkpName = wkp.getWkpDisplayName();
//			}
////			String wkpID = wkp == null ? "" : wkp.getWorkplaceId();
//			//アルゴリズム「申請一覧事前必須チェック」を実行する- (check App Predict Require): 0 - 申請一覧事前必須チェック
////			Boolean check = this.checkAppPredictRequire(app.getAppType().value, wkpID, companyId);
//			boolean checkAddNote = false;
////			if(check == true){//必須(True)
////				//事前、事後の後ろに#CMM045_101(※)を追加
////				// TODO Auto-generated method stub
////				checkAddNote = true;
////			}
//			String appDispNameStr = "";
//			if(appDispName.isPresent()){
//				appDispNameStr = appDispName.get().getDispName().v();
//			}
//			
//			Integer detailSet = app.isAppOverTime() ? this.detailSet(companyId, wkpID, app.getAppType().value) : null;
//			lstAppMasterInfo.add(new AppMasterInfo(app.getAppID(), app.getAppType().value, appDispNameStr,
//					empName, inpEmpName, wkpName, false, null, checkAddNote, 0, detailSet));
//		}
//		return lstAppMasterInfo;
//	}
	//ver14 + EA1360
	////Bug #97415 - EA2161、2162
	private Integer detailSet(String companyId, String wkpId, Integer appType, GeneralDate date){
		//ドメイン「職場別申請承認設定」を取得する-(lấy dữ liệu domain Application approval setting by workplace)
		Optional<ApprovalFunctionSetting> appFuncSet = null;
		appFuncSet = repoRequestWkp.getFunctionSetting(companyId, wkpId, appType);
		if(appFuncSet.isPresent() && appFuncSet.get().getAppUseSetting().getUserAtr().equals(UseAtr.USE)){
			return appFuncSet.get().getApplicationDetailSetting().get().getTimeCalUse().value;
		}
		//取得できなかった場合
		//<Imported>(就業）職場ID(リスト）を取得する - ※RequestList83-1
		List<String> lstWpkIDPr = wkpAdapter.findListWpkIDParentDesc(companyId, wkpId, date);
		if(lstWpkIDPr.size() > 1){
			for (int i=1;i < lstWpkIDPr.size(); i++) {
				//ドメイン「職場別申請承認設定」を取得する
				appFuncSet = repoRequestWkp.getFunctionSetting(companyId, lstWpkIDPr.get(i), appType);
				if(appFuncSet.isPresent() && appFuncSet.get().getAppUseSetting().getUserAtr().equals(UseAtr.USE)){
					return appFuncSet.get().getApplicationDetailSetting().get().getTimeCalUse().value;
				}
			}
		}
		//ドメイン「会社別申請承認設定」を取得する-(lấy dữ liệu domain Application approval setting by company)
		appFuncSet = repoRequestCompany.getFunctionSetting(companyId, appType);
		return appFuncSet.isPresent() &&  appFuncSet.get().getAppUseSetting().getUserAtr().equals(UseAtr.USE) 
				? appFuncSet.get().getApplicationDetailSetting().get().getTimeCalUse().value : 0;
	} 
	/**
	 * 12 - 申請一覧初期日付期間
	 */
	@Override
	public DatePeriod getInitialPeriod(String companyId) {
		//ドメイン「締め」を取得する
		List<Closure> lstClosure = repoClosure.findAllActive(companyId, UseClassification.UseClass_Use);
		//list clourse hist
		// 締め開始日
		GeneralDate startDate = null;
		// 締め終了日
//		GeneralDate endDate = null;
		for (Closure closure : lstClosure) {
			//アルゴリズム「処理年月と締め期間を取得する」を実行する
			Optional<PresentClosingPeriodImport> closureA = closureAdapter.getClosureById(companyId, closure.getClosureId().value);
			PresentClosingPeriodImport priod = closureA.get();
			if(startDate == null || startDate.after(priod.getClosureStartDate())){
				startDate = priod.getClosureStartDate();
//				endDate	= priod.getClosureEndDate();
			}
		}
		//EA2238
		//上記、開始日付の２年後（＋２年－１日）」を終了日付として取得
		return new DatePeriod(startDate,startDate.addYears(2).addDays(-1));
	}
	/**
	 * 12.1 - 申請一覧初期日付期間_申請
	 * @param companyId
	 * @return
	 */
	@Override
	public DatePeriod getInitPeriodApp(String companyId) {
		//imported(就業)「所属雇用履歴」より雇用コードを取得する - request list 264
		DatePeriod date = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		List<EmploymentHisImport> lst = employmentAdapter.findByListSidAndPeriod(AppContexts.user().employeeId(), date);
		// TODO Auto-generated method stub
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
	 * find closure history min
	 * @param lstClosureHist
	 * @return
	 */
//	private Closure findHistMin(List<Closure> lstClosure){
//		lstClosure.sort((hist1, hist2)
//				-> hist1.getClosureHistories().get(0).getClosureDate().getClosureDay().compareTo(hist2.getClosureHistories().get(0).getClosureDate().getClosureDay()));
//		Closure histMin = null;
//		for (Closure closure : lstClosure) {
//			if(closure.getClosureHistories().get(0).getClosureDate().getLastDayOfMonth() != true){
//				histMin = closure;
//				break;
//			}
//		}
//		return histMin == null? lstClosure.get(0) : histMin;
//	}
	/**
	 * find closure history by period
	 * @param closureHistories
	 * @param closureMonth
	 * @return
	 */
//	private ClosureHistory findHistClosure(List<ClosureHistory> closureHistories, CurrentMonth closureMonth){
//		for (ClosureHistory closureHist : closureHistories) {
//			if(closureHist.getStartYearMonth().lessThanOrEqualTo(closureMonth.getProcessingYm()) &&
//					closureHist.getEndYearMonth().greaterThanOrEqualTo(closureMonth.getProcessingYm())){
//				return closureHist;
//			}
//		}
//		return null;
//	}
	/**
	 * merge App And Phase
	 * @param lstApp
	 * @return
	 */
	private List<ApplicationFullOutput> mergeAppAndPhase(List<Application_New> lstApp, String companyID){
		List<ApplicationFullOutput> lstAppFull = new ArrayList<>();
		List<String> appIDs = lstApp.stream().map(x -> x.getAppID()).collect(Collectors.toList());
//		for (Application_New app : lstApp) {
//			List<ApprovalPhaseStateImport_New> lstPhase = approvalRootStateAdapter.getApprovalRootContent(companyID, 
//					app.getEmployeeID(), app.getAppType().value, app.getAppDate(), app.getAppID(), false).getApprovalRootState().getListApprovalPhaseState();
//			lstAppFull.add(new ApplicationFullOutput(app, lstPhase,-1,""));
//		}
		if(!CollectionUtil.isEmpty(appIDs)){
			Map<String,List<ApprovalPhaseStateImport_New>> approvalRootContentImport_News = approvalRootStateAdapter.getApprovalRootContents(appIDs, companyID);
			for(Map.Entry<String,List<ApprovalPhaseStateImport_New>> entry : approvalRootContentImport_News.entrySet()){
				for (Application_New app : lstApp) {
					if(entry.getKey().equals(app.getAppID())){
						lstAppFull.add(new ApplicationFullOutput(app, entry.getValue(),-1,"", null));
					}
				}
				
			}
		}
		return lstAppFull;
	}
	/**
	 * find status phase and frame
	 * @param lstPhase
	 * @param sID
	 * @return
	 */
	private PhaseFrameStatus findPhaseFrameStatus(List<ApprovalPhaseStateImport_New> lstPhase, String sID){
		PhaseFrameStatus status = new PhaseFrameStatus();
		for (ApprovalPhaseStateImport_New appPhase : lstPhase) {
			FrameOutput frame = this.checkPhaseCurrent(appPhase, sID);
			if(frame.getFrameStatus() != null){
				status.setPhaseOrder(appPhase.getPhaseOrder());
				status.setFrameStatus(EnumAdaptor.valueOf(frame.getFrameStatus(), ApprovalBehaviorAtrImport_New.class));
				status.setPhaseStatus(appPhase.getApprovalAtr());
				status.setAgentId(frame.getAgentId());
				break;
			}
		}
		return status;
		
	}
	/**
	 * check phase current (login)
	 * @param phase
	 * @param sID
	 * @return null: not phase
	 * @return not null: phase current and status frame
	 */
	private FrameOutput checkPhaseCurrent(ApprovalPhaseStateImport_New phase, String sID){
		List<ApprovalFrameImport_New> lstFrame = phase.getListApprovalFrame();
		FrameOutput statusFrame = new FrameOutput();
		for (ApprovalFrameImport_New frame : lstFrame) {
			if(this.checkExistEmp(frame.getListApprover(), sID)){
				statusFrame.setFrameStatus(frame.getApprovalAtr().value);
				statusFrame.setAgentId(frame.getRepresenterID());
				break;
			}
			//TH login la agent va da approval
			if(frame.getRepresenterID() != null && frame.getRepresenterID().equals(sID)){
				statusFrame.setFrameStatus(frame.getApprovalAtr().value);
				statusFrame.setAgentId(frame.getRepresenterID());
				break;
			}
		}
		return statusFrame;
	}
	/**
	 * check frame exist employee?
	 * @param listApprover
	 * @param sID
	 * @return
	 */
	private boolean checkExistEmp(List<ApproverStateImport_New> listApprover, String sID){
		boolean check = false;
		for (ApproverStateImport_New approver : listApprover) {
			if(approver.getApproverID().equals(sID)){
				check = true;
				break;
			}
		}
		return check;
	}
	/**
	 * calculate status of application
	 * @param appStatus
	 * @param status
	 * @param agentId
	 * @return
	 */
	private int calApplication(ReflectedState_New appStatus, PhaseFrameStatus status, String agentId, String sIdLogin){
		if(agentId != null){//※ログイン者　　＝代行者の場合
			if(agentId.equals(sIdLogin)){
				return this.calAppAgent(appStatus, status);
			}
		}	
		//※ログイン者　　＝承認者の場合
		return this.calAppAproval(appStatus, status);
	}
	
	/**
	 * calculate status TH ログイン者　　＝承認者の場合
	 * @param appStatus
	 * @param status
	 * @return
	 */
	private int calAppAproval(ReflectedState_New appStatus, PhaseFrameStatus status){
		switch(appStatus.value){
			case 0://APP: 未反映 - NOTREFLECTED
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){//Phase: 未承認
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){//Frame: 未承認
						return 5;
					}
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Frame: 承認済  
						return 4;
					}
					return 0;
				}
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.REMAND)){//差し戻し 
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){
						return 2;
					}
					return 0;
				}
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Phase: 承認済   
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Frame: 未承認/承認済 
						return 4;
					}
					return 0;
				}
				return 0;
			case 1://APP: 反映待ち - WAITREFLECTION
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Phase: 承認済 
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Frame: 未承認/承認済 
						return 4;
					}
					return 0;
				}
				return 0;
			case 2://APP: 反映済 - REFLECTED
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Phase: 承認済 
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Frame: 未承認/承認済 
						return 4;
					}
					return 0;
				}
				return 0;
			case 5://APP: 差し戻し - REMAND
				return 0;
			case 6://APP: 否認 - DENIAL
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){//Phase: 未承認
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 未承認/承認済/否認
						return 1;
					}
					return 0;
				}
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Phase: 承認済
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 未承認/承認済/否認
						return 1;
					}
					return 0;
				}
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Phase: 否認
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 未承認/承認済/否認
						return 1;
					}
					return 0;
				}
				return 0;
			default://APP: 取消待ち - WAITCANCEL/取消済  - CANCELED
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){//Phase: 未承認
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 未承認/承認済/否認
						return 3;
					}
					return 0;
				}
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Phase: 承認済
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 未承認/承認済/否認
						return 3;
					}
					return 0;
				}
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Phase: 否認
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 未承認/承認済/否認
						return 3;
					}
					return 0;
				}
				return 0;
		}
	}
	
	/**
	 * calculate status TH ログイン者　　＝代行者の場合
	 * @param appStatus
	 * @param status
	 * @return
	 */
	private int calAppAgent(ReflectedState_New appStatus, PhaseFrameStatus status){
		switch(appStatus.value){
			case 0://APP: 未反映 - NOTREFLECTED
				//Phase: 未承認
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){//Frame: 未承認
						return 5;
					}
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Frame: 承認済  
						return 4;
					}
					return 0;
				}
				//差し戻し 
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.REMAND)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){
						return 2;
					}
					return 0;
				}
				//Phase: 承認済   
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Frame: 承認済 
						return 4;
					}
					return 0;
				}
				return 0;
			case 1://APP: 反映待ち - WAITREFLECTION
				//Phase: 承認済 
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Frame: 承認済 
						return 4;
					}
					return 0;
				}
				return 0;
			case 2://APP: 反映済 - REFLECTED
				//Phase: 承認済 
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){//Frame: 承認済 
						return 4;
					}
					return 0;
				}
				return 0;
			case 5://APP: 差し戻し - REMAND
				return 0;
			case 6://APP: 否認 - DENIAL
				//Phase: 未承認
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 承認済/否認
						return 1;
					}
					return 0;
				}
				//Phase: 承認済
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 承認済/否認
						return 1;
					}
					return 0;
				}
				//Phase: 否認
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 承認済/否認
						return 1;
					}
					return 0;
				}
				return 0;
			default://APP: 取消待ち - WAITCANCEL/取消済  - CANCELED
				////Phase: 未承認
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 承認済/否認
						return 3;
					}
					return 0;
				}
				//Phase: 承認済
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 承認済/否認
						return 3;
					}
					return 0;
				}
				//Phase: 否認
				if(status.getPhaseStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){
					if(status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.APPROVED) ||
							status.getFrameStatus().equals(ApprovalBehaviorAtrImport_New.DENIAL)){//Frame: 承認済/否認
						return 3;
					}
					return 0;
				}
				return 0;
		}
	}
	/**
	 * 条件１： ログイン者の表示対象の基本条件 (filter application by conditions 1)
	 * @param app
	 * @return
	 */
	private ApproverStt filterConditions1(ApplicationFullOutput app, List<AgentDataRequestPubImport> lstAgent, String sID){
		//dk1
//		List<ApprovalPhaseStateImport_New> lstPhase = approvalRootStateAdapter.getApprovalRootContent(companyID, 
//				app.getEmployeeID(), app.getAppType().value, app.getAppDate(), app.getAppID(), false).getApprovalRootState().getListApprovalPhaseState();
		List<ApprovalPhaseStateImport_New> lstPhase = app.getLstPhaseState();
		ApproverStt check = new ApproverStt(false, null);
		for (ApprovalPhaseStateImport_New appPhase : lstPhase) {
			for (ApprovalFrameImport_New frame : appPhase.getListApprovalFrame()) {
				//承認枠.承認区分!=未承認 
				if(!frame.getApprovalAtr().equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)){
					if(this.checkDifNotAppv(frame, sID)){
						check = new ApproverStt(true, null);;
						break;
					}
				}else{
					ApproverStt checkNotAppv = this.checkNotAppv(frame, lstAgent, appPhase.getApprovalAtr(), app.getApplication(), sID);
					if(checkNotAppv.isCheck()){
						check = new ApproverStt(true, checkNotAppv.getApprId());;
						break;
					}
				}
			}
			
		}
		return check;
	}
	/**
	 * 承認枠.承認区分!=未承認 
	 * @param frame
	 * @return
	 */
	private boolean checkDifNotAppv(ApprovalFrameImport_New frame, String sID){
		
		if(Strings.isNotBlank(frame.getApproverID())&& frame.getApproverID().equals(sID)){
			return true;
		}
		if(Strings.isNotBlank(frame.getRepresenterID())&& frame.getRepresenterID().equals(sID)){
			return true;
		}
		if(this.checkExistEmp(frame.getListApprover(), sID)){
			return true;
		}
		return false;
	}
	/**
	 * 承認枠.承認区分 = 未承認 
	 * @param frame
	 * @return
	 */
	private ApproverStt checkNotAppv(ApprovalFrameImport_New frame, List<AgentDataRequestPubImport> lstAgent,
			ApprovalBehaviorAtrImport_New phaseAtr, Application_New app, String sID){
		//※前提条件：「申請.反映情報　＝　未反映」且　「自身の承認フェーズ　＝　未承認/差し戻し」の場合
		if(!app.getReflectionInformation().getStateReflectionReal().equals(ReflectedState_New.NOTREFLECTED) || (!phaseAtr.equals(ApprovalBehaviorAtrImport_New.UNAPPROVED)
				&& !phaseAtr.equals(ApprovalBehaviorAtrImport_New.REMAND))){
			return new ApproverStt(false, null);
		}
		//１．承認予定者より取得（自身が承認する申請）
		if(this.checkExistEmp(frame.getListApprover(), sID)){//承認枠.承認者リスト(複数ID)　＝ログイン者社員ID
			return new ApproverStt(true, null);
		}
		//２．取得したドメイン「代行者管理」より代理者指定されている場合は取得
		//申請.申請日付　＝　代行者管理：代行承認.代行依頼期間 &&承認枠.承認者リスト（複数ID）＝　代行者管理：代行承認.代行依頼者\
		List<String> lstId = new ArrayList<>();
		String idAppr = null;
		for(AgentDataRequestPubImport agent : lstAgent){
			if(agent.getStartDate().beforeOrEquals(app.getAppDate()) && agent.getEndDate().afterOrEquals(app.getAppDate())
					&& this.checkExistEmp(frame.getListApprover(), agent.getEmployeeId())){
				lstId.add(agent.getAgentSid1());
				idAppr = agent.getEmployeeId();
			}
		}
//		List<AgentDataRequestPubImport> lstAgentFilter = lstAgent.stream()
//				.filter(c -> c.getStartDate().beforeOrEquals(app.getAppDate()) && c.getEndDate().equals(app.getAppDate()) && this.checkExistEmp(frame.getListApprover(), c.getAgentSid1()))
//				.collect(Collectors.toList());
//		 lstId = lstAgentFilter.stream().map(c -> c.getAgentSid1()).collect(Collectors.toList());
		if(lstId.contains(sID)){//代行承認.承認代行者　＝　ログイン者社員ID
			return new ApproverStt(true, idAppr);
		}
		return new ApproverStt(false, null);
	}
	/**
	 * convert status phase
	 * －：承認フェーズ.承認区分　＝　未承認
	 * 〇：承認フェーズ.承認区分　＝　承認済
	 * ×：承認フェーズ.承認区分　＝　否認
	 * @param appId
	 * @param lstPhaseState
	 * @return
	 */
	private PhaseStatus convertStatusPhase(String appId, List<ApprovalPhaseStateImport_New> lstPhaseState){
		String phaseStatus = "";
		List<Integer> lstPhaseAtr = new ArrayList<>();
		for (int i = 1; i<= 5; i++) {
			String phaseI = "";
			Integer status = this.findPhaseStatus(lstPhaseState, i);
			lstPhaseAtr.add(status);
			if(status != null){//phase exist
				phaseI = status == 1 ? "〇" : status == 2 ? "×" : "－";
			}
			phaseStatus += phaseI;
			//Doi ung theo QA #90893
//			String phaseI = "";
//			Integer status = this.findPhaseStatus(lstPhaseState, i);
//			if(status == null){
//				continue;
//			}
			//phase exist
//			lstPhaseAtr.add(status);
//			phaseI = status == 1 ? "〇" : status == 2 ? "×" : "－";
//			phaseStatus += phaseI;
		}
		return new PhaseStatus(appId, phaseStatus, lstPhaseAtr);
	}
	/**
	 * find phase status
	 * @param lstPhaseState
	 * @param order
	 * @return
	 */
	private Integer findPhaseStatus(List<ApprovalPhaseStateImport_New> lstPhaseState, int order){
		for (ApprovalPhaseStateImport_New phase : lstPhaseState) {
			if(phase.getPhaseOrder().equals(order)){
				return phase.getApprovalAtr().value;
			}
		}
		return null;
	}
	/**
	 * check color application exist list check???
	 * @param lstColor
	 * @param appId
	 * @return
	 */
	private boolean checkExistColor(List<CheckColorTime> lstColor, String appId){
		for (CheckColorTime checkColorTime : lstColor) {
			if(checkColorTime.getAppID().equals(appId)){
				return true;
			}
		}
		return false;
	}
	/**
	 * check time pr < time post
	 * @param timePre
	 * @param timePost
	 * @return true : error -> fill color
	 */
	private boolean checkPrePostColor(List<OverTimeFrame> timePre, List<OverTimeFrame> timePost){
		//loop by frame post
		for (OverTimeFrame overTimeFrame : timePost) {
			//frame pre
			Integer checkColor = this.findTimePre(timePre, overTimeFrame.getFrameNo());
			if(checkColor == null){
				return overTimeFrame == null ? false : true;
			}
			if(overTimeFrame.getApplicationTime() == null){
				return false;
			}
			if(overTimeFrame.getApplicationTime() < checkColor){
				return true;
			}
		}
		return false;
	}
	/**
	 * find time application pre
	 * @param lstFrame
	 * @param frameNo
	 * @return
	 */
	private Integer findTimePre(List<OverTimeFrame> lstFrame, int frameNo){
		for (OverTimeFrame overTimeFrame : lstFrame) {
			if(overTimeFrame.getFrameNo() == frameNo){
				return overTimeFrame.getApplicationTime();
			}
		}
		return null;
	}
//	private List<AppCompltLeaveFull> getListCompltDetail(List<Application_New> lstComplt, String companyId){
//		List<AppCompltLeaveFull> lstCompltFull = new ArrayList<>();
//		for (Application_New app : lstComplt) {
//			AppCompltLeaveFull complt = null;
//			//アルゴリズム「申請一覧リスト取得振休振出」を実行する-(get List App Complement Leave): 6 - 申請一覧リスト取得振休振出
//			AppCompltLeaveSyncOutput sync = this.getListAppComplementLeave(app, companyId);
//			if(!sync.isSync()){//TH k co don lien ket
//				//lay thong tin chi tiet
//				complt = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType());
//			}else{//TH co don lien ket
//				//lay thong tin chi tiet
//				complt = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType());
//			}
//			lstCompltFull.add(complt);
//		}
//		return null;
//	}
	private CheckExitSync checkExitSync(List<Application_New> lstCompltLeave, String appId){
		for (Application_New app : lstCompltLeave) {
			if(app.getAppID().equals(appId)){
				return new CheckExitSync(true, app.getAppDate(), app.getInputDate());
			}
		}
		return new CheckExitSync(false, null, null);
	}
}
