package nts.uk.ctx.at.request.app.find.application.applicationlist;
/*import java.util.HashMap;
import javax.print.attribute.HashAttributeSet;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispName;*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.text.StringUtil;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.setting.company.request.approvallistsetting.ApprovalListDisplaySetDto;
import nts.uk.ctx.at.request.app.find.setting.company.vacationapplicationsetting.HdAppSetDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.AppListExtractCondition;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.ApplicationListAtr;
import nts.uk.ctx.at.request.dom.application.applist.service.AppListInitialRepository;
import nts.uk.ctx.at.request.dom.application.applist.service.AppListOutPut;
import nts.uk.ctx.at.request.dom.application.applist.service.AppMasterInfo;
import nts.uk.ctx.at.request.dom.application.applist.service.ApplicationFullOutput;
import nts.uk.ctx.at.request.dom.application.applist.service.CheckColorTime;
import nts.uk.ctx.at.request.dom.application.applist.service.PhaseStatus;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.ApprovalListDisplaySetting;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 11 - 申請一覧初期処理
 * @author hoatt
 *
 */
@Stateless
public class ApplicationListFinder {

	@Inject
	private AppListInitialRepository repoAppListInit;
	@Inject
	private RequestSettingRepository repoRequestSet;
	@Inject
	private HdAppSetRepository repoHdAppSet;
	@Inject
	private AppDispNameRepository repoAppDispName;
	
	public ApplicationListDto getAppList(AppListParamFilter param){
		AppListExtractConditionDto condition = param.getCondition();
		String companyId = AppContexts.user().companyId();
		//ドメインモデル「承認一覧表示設定」を取得する-(Lấy domain Approval List display Setting)
		Optional<RequestSetting> requestSet = repoRequestSet.findByCompany(companyId);
		ApprovalListDisplaySetting appDisplaySet = null;
		ApprovalListDisplaySetDto displaySet = null;
		Integer isDisPreP = null;
		if(requestSet.isPresent()){
			appDisplaySet = requestSet.get().getApprovalListDisplaySetting();
			displaySet = ApprovalListDisplaySetDto.fromDomain(appDisplaySet);
			isDisPreP = requestSet.get().getApplicationSetting().getAppDisplaySetting().getPrePostAtr().value;
		}
		//URパラメータが存在する-(Check param)
		if(StringUtil.isNullOrEmpty(condition.getStartDate(), false) || StringUtil.isNullOrEmpty(condition.getEndDate(), false)){
			//アルゴリズム「申請一覧初期日付期間」を実行する-(Thực hiện thuật toán lấy ngày　－12)
			DatePeriod date = null;
			if(condition.getAppListAtr().equals(ApplicationListAtr.APPROVER.value)){
				date = repoAppListInit.getInitialPeriod(companyId);
			}else{
				date = repoAppListInit.getInitPeriodApp(companyId);
			}
			condition.setStartDate(date.start().toString());
			condition.setEndDate(date.end().toString());
			
		}
		//ドメインモデル「申請一覧共通設定フォーマット.表の列幅」を取得-(Lấy 表の列幅)//xu ly o ui
		//アルゴリズム「申請一覧リスト取得」を実行する-(Thực hiện thuật toán Application List get): 1-申請一覧リスト取得
		AppListExtractCondition appListExCon = condition.convertDtotoDomain(condition);
		AppListOutPut lstApp = repoAppListInit.getApplicationList(appListExCon, appDisplaySet);
		List<ApplicationDto_New> lstAppDto = new ArrayList<>();
		for (Application_New app : lstApp.getLstApp()) {
			lstAppDto.add(ApplicationDto_New.fromDomain(app));
		}
		List<AppStatusApproval> lstStatusApproval = new ArrayList<>();
		List<ApproveAgent> lstAgent = new ArrayList<>();
		if(condition.getAppListAtr() == 1){//mode approval
			List<ApplicationFullOutput> lstFil = lstApp.getLstAppFull().stream().filter(c -> c.getStatus() != null).collect(Collectors.toList());
			for (ApplicationFullOutput app : lstFil) {
				lstAgent.add(new ApproveAgent(app.getApplication().getAppID(), app.getAgentId()));
				lstStatusApproval.add(new AppStatusApproval(app.getApplication().getAppID(), app.getStatus()));
			}
			for (ApplicationDto_New appDto : lstAppDto) {
				appDto.setReflectPerState(this.findStatusAppv(lstStatusApproval, appDto.getApplicationID()));
			}
			for (AppMasterInfo master : lstApp.getDataMaster().getLstAppMasterInfo()) {
				master.setStatusFrameAtr(this.findStatusFrame(lstApp.getLstFramStatus(), master.getAppID()));
				master.setPhaseStatus(this.findStatusPhase(lstApp.getLstPhaseStatus(), master.getAppID()));
				master.setCheckTimecolor(this.findColorAtr(lstApp.getLstTimeColor(), master.getAppID()));
			}
		}
		//ドメインモデル「休暇申請設定」を取得する (Vacation application setting)- YenNTH
		Optional<HdAppSet> lstHdAppSet = repoHdAppSet.getAll();
		HdAppSetDto hdAppSetDto = HdAppSetDto.convertToDto(lstHdAppSet.get());
		List<AppInfor> lstAppType = this.findListApp(lstApp.getDataMaster().getLstAppMasterInfo(), param.isSpr(), param.getExtractCondition());
		List<ApplicationDto_New> lstAppSort = param.getCondition().getAppListAtr() == 1 ? this.sortByIdModeApproval(lstAppDto, lstApp.getDataMaster().getLstAppMasterInfo()) : 
									this.sortByIdModeApp(lstAppDto, lstApp.getDataMaster().getMapAppBySCD(), lstApp.getDataMaster().getLstSCD());
		return new ApplicationListDto(isDisPreP, condition.getStartDate(), condition.getEndDate(), displaySet, lstApp.getDataMaster().getLstAppMasterInfo(),lstAppSort,
				lstApp.getLstAppOt(),lstApp.getLstAppGoBack(), lstApp.getAppStatusCount(), lstApp.getLstAppGroup(), lstAgent,
				lstApp.getLstAppHdWork(), lstApp.getLstAppWorkChange(), lstApp.getLstAppAbsence(), lstAppType, hdAppSetDto, lstApp.getLstAppCompltLeaveSync());
	}
	/**
	 * find status approval
	 * @param lstStatusApproval
	 * @param appID
	 * @return
	 */
	private Integer findStatusAppv(List<AppStatusApproval> lstStatusApproval, String appID){
		for (AppStatusApproval appStatus : lstStatusApproval) {
			if(appStatus.getAppId().equals(appID)){
				return appStatus.getStatusApproval();
			}
		}
		return null;
	}
	/**
	 * find status frame
	 * @param lstFramStatus
	 * @param appID
	 * @return
	 */
	private boolean findStatusFrame(List<String> lstFramStatus, String appID){
		for (String id : lstFramStatus) {
			if(id.equals(appID)){
				return true;
			}
		}
		return false;
	}
	/**
	 * find status phase by appId
	 * @param phaseState
	 * @param appId
	 * @return
	 */
	private String findStatusPhase(List<PhaseStatus> phaseState, String appId){
		for (PhaseStatus phaseStatus : phaseState) {
			if(phaseStatus.getAppID().equals(appId)){
				return phaseStatus.getPhaseStatus();
			}
		}
		return null;
	}
	/**
	 * find color by appId
	 * @param lstTimeColor
	 * @param appId
	 * @return
	 */
	private int findColorAtr(List<CheckColorTime> lstTimeColor, String appId){
		for (CheckColorTime checkColorTime : lstTimeColor) {
			if(checkColorTime.getAppID().equals(appId)){
				return checkColorTime.getColorAtr();
			}
		}
		return 0;
	}
	/**
	 * find list appType
	 * @param lstApp
	 * @return
	 */
	private List<AppInfor> findListApp(List<AppMasterInfo> lstApp, boolean isSpr, int extractCondition){
		List<AppInfor> lstAppType = new ArrayList<>();
		for (AppMasterInfo app : lstApp) {
			if(!lstAppType.contains(new AppInfor(app.getAppType(), app.getDispName()))){
				lstAppType.add(new AppInfor(app.getAppType(), app.getDispName()));
			}
		}
		if(isSpr && extractCondition == 1){
			if(!this.findAppTypeOt(lstAppType)){
				String name = repoAppDispName.getDisplay(ApplicationType.OVER_TIME_APPLICATION.value).get().getDispName().v();
				lstAppType.add(new AppInfor(ApplicationType.OVER_TIME_APPLICATION.value, name));
			}
		}
		return lstAppType.stream().sorted((x, y) -> x.getAppType()-y.getAppType()).collect(Collectors.toList());
	}
	private boolean findAppTypeOt(List<AppInfor> lstAppType){
		for (AppInfor appInfor : lstAppType) {
			if(appInfor.getAppType() == 0){
				return true;
			}
		}
		return false;
	}
	private List<ApplicationDto_New> sortById(List<ApplicationDto_New> lstApp){
		return lstApp.stream().sorted((a,b) ->{
			Integer rs = a.getApplicationDate().compareTo(b.getApplicationDate());
			if (rs == 0) {
			 return  a.getApplicationType().compareTo(b.getApplicationType());
			} else {
			 return rs;
			}
		}).collect(Collectors.toList());
	}
	/**
	 * 2018/07/09　　201807CMM045改修　EA2236
	 * 並び順を申請日付順⇒社員コード＋申請日付＋申請種類順でソートするに変更
	 * 2018/07/20　EA2338
	 * 申請種類を追加
	 * @param lstApp
	 * @return
	 */
	private List<ApplicationDto_New> sortByIdModeApp(List<ApplicationDto_New> lstApp, Map<String, List<String>> mapAppBySCD,
			List<String> lstSCD){
		List<ApplicationDto_New> lstResult = new ArrayList<>();
		java.util.Collections.sort(lstSCD);
		for (String sCD : lstSCD) {
			lstResult.addAll(this.sortById(this.findBylstID(lstApp, mapAppBySCD.get(sCD))));
			
		}
		return lstResult;
	}
	private List<ApplicationDto_New> findBylstID(List<ApplicationDto_New> lstApp, List<String> lstAppID){
		List<ApplicationDto_New> lstAppFind = new ArrayList<>();
		for (ApplicationDto_New app : lstApp) {
			if(lstAppID.contains(app.getApplicationID())){
				lstAppFind.add(app);
			}
		}
		return lstAppFind;
	}
	private List<ApplicationDto_New> sortByIdModeApproval(List<ApplicationDto_New> lstApp, List<AppMasterInfo> lstAppMasterInfo){
		List<String> lstSCD = new ArrayList<>();
		for(AppMasterInfo master : lstAppMasterInfo){
			if(!lstSCD.contains(master.getEmpSD())){
				lstSCD.add(master.getEmpSD());
			}
		}
		Collections.sort(lstSCD);
		List<ApplicationDto_New> lstResult = new ArrayList<>();
		for (String sCD : lstSCD) {
			List<String> lstAppID = lstAppMasterInfo.stream().filter(c -> c.getEmpSD().equals(sCD))
					.map(d -> d.getAppID()).collect(Collectors.toList());
			lstResult.addAll(this.sortById(this.findBylstID(lstApp, lstAppID)));
		}
		return lstResult;
	}
}
