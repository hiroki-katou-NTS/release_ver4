package nts.uk.ctx.at.request.dom.application.common.service.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.gul.mail.send.MailContents;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.EnvAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.dto.MailDestinationImport;
import nts.uk.ctx.at.request.dom.application.common.service.application.IApplicationContentService;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AppCompltLeaveSyncOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.MailResult;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.PeriodCurrentMonth;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.compltleavesimmng.CompltLeaveSimMng;
import nts.uk.ctx.at.request.dom.application.holidayshipment.compltleavesimmng.CompltLeaveSimMngRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.compltleavesimmng.SyncState;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispName;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailapplicationapproval.ApprovalTemp;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailapplicationapproval.ApprovalTempRepository;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailcontenturlsetting.UrlEmbedded;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailcontenturlsetting.UrlEmbeddedRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.ReceptionRestrictionSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.CheckMethod;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.workplace.WorkTimeWorkplaceRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.url.RegisterEmbededURL;

@Stateless
public class OtherCommonAlgorithmImpl implements OtherCommonAlgorithm {
	
	@Inject
	private EmployeeRequestAdapter employeeAdaptor;
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepo;
	
	@Inject
	private ApplicationSettingRepository appSettingRepo;
	
	@Inject
	private WorkTimeWorkplaceRepository workTimeWorkplaceRepo;
	
	@Inject
	private ClosureRepository closureRepository;
	
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;
	
	@Inject
	private ClosureService closureService;
	@Inject
	private AbsenceLeaveAppRepository absRepo;
	@Inject
	private CompltLeaveSimMngRepository compLeaveRepo;
	@Inject
	private RequestSettingRepository requestSettingRepository;
	
	@Inject
	private MailSender mailsender;
	
	@Inject
	private ApprovalTempRepository approvalTempRepository;
	
	@Inject
	private AppDispNameRepository appDispNameRepository;
	
	@Inject
	private EnvAdapter envAdapter;
	
	@Inject
	private UrlEmbeddedRepository urlEmbeddedRepository;
	
	@Inject
	private RegisterEmbededURL registerEmbededURL;
	
	@Inject
	private IApplicationContentService applicationContentService;
	@Inject
	private CollectAchievement collectAch;
	@Inject
	private WorkTypeIsClosedService workTypeRepo;
	
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;
	public PeriodCurrentMonth employeePeriodCurrentMonthCalculate(String companyID, String employeeID, GeneralDate date){
		/*
		アルゴリズム「社員所属雇用履歴を取得」を実行する(thực hiện xử lý 「社員所属雇用履歴を取得」)
		*/
		SEmpHistImport empHistImport = employeeAdaptor.getEmpHist(companyID, employeeID, date);
		if(empHistImport==null || empHistImport.getEmploymentCode()==null){
			throw new BusinessException("Msg_426");
		}
		String employmentCD = empHistImport.getEmploymentCode();
		// ドメインモデル「雇用に紐づく就業締め」を取得する
		Optional<ClosureEmployment> closureEmployment = closureEmploymentRepository.findByEmploymentCD(companyID, employmentCD);
		if(!closureEmployment.isPresent()){
			throw new BusinessException("Msg_1134");
		}
		/*
		ドメインモデル「締め」を取得する(lấy thông tin domain「締め」)
		Object<String: tightenID, String: currentMonth> obj1 = Tighten.find(companyID, employeeCD); // obj1 <=> (締めID,当月)
		*/
		Optional<Closure> closure = closureRepository.findById(companyID, closureEmployment.get().getClosureId());
		if(!closure.isPresent()){
			throw new RuntimeException("khong co closure");
		}
		/*
		当月の期間を算出する(tính period của tháng hiện tại)
		Object<String: startDate, String: endDate> obj2 = Period.find(obj1.tightenID, obj1.currentMonth); // obj2 <=> 締め期間(開始年月日,終了年月日) 
		*/
		DatePeriod datePeriod = closureService.getClosurePeriod(closure.get().getClosureId().value,
				closure.get().getClosureMonth().getProcessingYm());
		return new PeriodCurrentMonth(datePeriod.start(), datePeriod.end());
	}
	/**
	 * 1.職場別就業時間帯を取得
	 */
	@Override
	public List<String> getWorkingHoursByWorkplace(String companyID, String employeeID, GeneralDate referenceDate) {
		List<String> listEmployeeAdaptor = employeeAdaptor.findWpkIdsBySid(companyID, employeeID, referenceDate);
		//取得した所属職場ID＋その上位職場IDを先頭から最後までループする
		List<String> listWorkTimeCodes = new ArrayList<>();
		for(String employeeAdaptor : listEmployeeAdaptor) {
			listWorkTimeCodes = workTimeWorkplaceRepo
					.getWorkTimeWorkplaceById(companyID, employeeAdaptor);
			if(listWorkTimeCodes.size()>0) {
				Collections.sort(listWorkTimeCodes);
				break;
			}
			
		}
		listWorkTimeCodes = workTimeSettingRepository.findByCompanyId(companyID).stream()
			.filter(x -> x.getAbolishAtr()==AbolishAtr.NOT_ABOLISH).map(x -> x.getWorktimeCode().v()).collect(Collectors.toList());
		return listWorkTimeCodes;
	}

	@Override
	public PrePostAtr preliminaryJudgmentProcessing(ApplicationType appType, GeneralDate appDate,int overTimeAtr) {
		GeneralDate systemDate = GeneralDate.today();
		Integer systemTime = GeneralDateTime.now().localDateTime().getHour()*60 
				+ GeneralDateTime.now().localDateTime().getMinute();
		String companyID = AppContexts.user().companyId();
		PrePostAtr prePostAtr = null;
		Optional<AppTypeDiscreteSetting> appTypeDisc = appTypeDiscreteSettingRepo.getAppTypeDiscreteSettingByAppType(companyID, appType.value);
		Optional<RequestSetting> requestSetting = this.requestSettingRepository.findByCompany(companyID);
		List<ReceptionRestrictionSetting> receptionRestrictionSetting = new ArrayList<>();
		if(requestSetting.isPresent()){
			receptionRestrictionSetting = requestSetting.get().getApplicationSetting().getListReceptionRestrictionSetting().stream().filter(x -> x.getAppType().equals(ApplicationType.OVER_TIME_APPLICATION)).collect(Collectors.toList());
		}
		//if appdate > systemDate 
		if(appDate.after(systemDate) ) {
			//xin truoc 事前事後区分= 事前
			prePostAtr = PrePostAtr.PREDICT;
			
		}else if(appDate.before(systemDate)) { // if appDate < systemDate
			//xin sau 事前事後区分= 事後
			prePostAtr = PrePostAtr.POSTERIOR;
		}else{ // if appDate = systemDate
//			// if RetrictPreUseFlg = notuse ->prePostAtr = POSTERIOR
//			if(appTypeDisc.get().getRetrictPreUseFlg() == UseAtr.NOTUSE) {
//				prePostAtr = PrePostAtr.POSTERIOR;
//			}else {
//				//「事前の受付制限」．チェック方法が日数でチェック
//				if(appTypeDisc.get().getRetrictPreMethodFlg() == CheckMethod.DAYCHECK) {
//					prePostAtr = PrePostAtr.POSTERIOR;
//				}else {//システム日時と受付制限日時と比較する
//					if(systemTime.compareTo(appTypeDisc.get().getRetrictPreTimeDay().v())==1) {
//						
//						prePostAtr = PrePostAtr.POSTERIOR;
//					}else { // if systemDateTime <=  RetrictPreTimeDay - > xin truoc
//						prePostAtr = PrePostAtr.PREDICT;
//					}
//				}
//			}
			if(appType.equals(ApplicationType.OVER_TIME_APPLICATION)){
				if(appTypeDisc.get().getRetrictPreMethodFlg() == CheckMethod.DAYCHECK) {
					prePostAtr = PrePostAtr.POSTERIOR;
				}else{
					int resultCompare = 0;
					if(overTimeAtr == 0 && receptionRestrictionSetting.get(0).getBeforehandRestriction().getPreOtTime() != null){
						resultCompare = systemTime.compareTo(receptionRestrictionSetting.get(0).getBeforehandRestriction().getPreOtTime().v());
					}else if(overTimeAtr == 1 && receptionRestrictionSetting.get(0).getBeforehandRestriction().getNormalOtTime() !=  null){
						resultCompare = systemTime.compareTo(receptionRestrictionSetting.get(0).getBeforehandRestriction().getNormalOtTime().v());
					}else if(overTimeAtr == 2 && receptionRestrictionSetting.get(0).getBeforehandRestriction().getTimeBeforehandRestriction() !=  null){
						resultCompare = systemTime.compareTo(receptionRestrictionSetting.get(0).getBeforehandRestriction().getTimeBeforehandRestriction().v());
					}
					if(resultCompare == 1) {
						prePostAtr = PrePostAtr.POSTERIOR;
					}else { // if systemDateTime <=  RetrictPreTimeDay - > xin truoc
						prePostAtr = PrePostAtr.PREDICT;
					}
				}
			}else{
				prePostAtr = PrePostAtr.POSTERIOR;
			}
			
		}
			
		return prePostAtr;
	}
	/**
	 * 5.事前事後区分の判断
	 */
	@Override
	public InitValueAtr judgmentPrePostAtr(ApplicationType appType, GeneralDate appDate,boolean checkCaller) {
		InitValueAtr outputInitValueAtr = null;
		String companyID = AppContexts.user().companyId();
		Optional<AppTypeDiscreteSetting> appTypeDisc = appTypeDiscreteSettingRepo.getAppTypeDiscreteSettingByAppType(companyID, appType.value);
		Optional<ApplicationSetting> appSetting = appSettingRepo.getApplicationSettingByComID(companyID);
		if(appSetting.get().getDisplayPrePostFlg() == AppDisplayAtr.DISPLAY) { // AppDisplayAtr displayPrePostFlg
			//メニューから起動(Boot from menu) : checkCaller == true
			if(checkCaller) {
				outputInitValueAtr = appTypeDisc.get().getPrePostInitFlg();
			}else {// その他のPG（日別修正、トップページアラーム、残業指示）から起動(Start from other PG (daily correction, top page alarm, overtime work instruction)): checkCaller == false
				outputInitValueAtr = InitValueAtr.POST;
			}
		}else { //if not display
			outputInitValueAtr = InitValueAtr.NOCHOOSE;
		}
		return outputInitValueAtr;
	}
	/**
	 * 9.同時申請された振休振出申請を取得する
	 * @author hoatt
	 * @param companyId
	 * @param appId
	 * @return
	 */
	@Override
	public AppCompltLeaveSyncOutput getAppComplementLeaveSync(String companyId, String appId) {
		// TODO Auto-generated method stub
		Optional<AbsenceLeaveApp> abs = absRepo.findByAppId(appId);
		Optional<CompltLeaveSimMng> sync = null;
		String absId = "";
		String recId = "";
		boolean synced = false;
		int type = 0;
		if(abs.isPresent()){//don xin nghi
			absId = appId;
			//tim lien ket theo abs
			sync = compLeaveRepo.findByAbsID(appId);
			if(sync.isPresent() && sync.get().getSyncing().equals(SyncState.SYNCHRONIZING)){
				recId = sync.get().getRecAppID();
				synced = true;
			}
		}else{//don lam bu
			type = 1;
			recId = appId;
			sync = compLeaveRepo.findByRecID(appId);
			if(sync.isPresent() && sync.get().getSyncing().equals(SyncState.SYNCHRONIZING)){
				absId = sync.get().getAbsenceLeaveAppID();
				synced = true;
			}
		}
		return new AppCompltLeaveSyncOutput(absId, recId, synced, type);
	}
	
	@Override
	public MailResult sendMailApproverApprove(List<String> employeeIDList, Application_New application) {
		Optional<ApprovalTemp> opApprovalTemp = approvalTempRepository.getAppTem();
		String content = "";
		if(opApprovalTemp.isPresent() && (opApprovalTemp.get().getContent() != null)){
			content = opApprovalTemp.get().getContent().v();
		}
		MailResult mailResult = sendMailApprover(employeeIDList, application, content);
		return new MailResult(mailResult.getSuccessList(), mailResult.getFailList());
	}
	@Override
	public MailResult sendMailApproverDelete(List<String> employeeIDList, Application_New application) {
		String inputText = I18NText.getText("Msg_1262",Collections.emptyList());
		MailResult mailResult = sendMailApprover(employeeIDList, application, inputText);
		return new MailResult(mailResult.getSuccessList(), mailResult.getFailList());
	}
	@Override
	public MailResult sendMailApplicantApprove(Application_New application) {
		String inputText = I18NText.getText("Msg_1263",Collections.emptyList());
		MailResult mailResult = sendMailApplicant(application, inputText);
		return new MailResult(mailResult.getSuccessList(), mailResult.getFailList());
	}
	@Override
	public MailResult sendMailApplicantDeny(Application_New application) {
		String inputText = I18NText.getText("Msg_1264",Collections.emptyList());
		MailResult mailResult = sendMailApplicant(application, inputText);
		return new MailResult(mailResult.getSuccessList(), mailResult.getFailList());
	}
	@Override
	public MailResult sendMailApprover(List<String> listDestination, Application_New application, String text) {
		List<String> successList = new ArrayList<>();
		List<String> failList = new ArrayList<>();
		String sIDlogin = AppContexts.user().employeeId();
		String companyID = AppContexts.user().companyId();
		List<String> paramIDList = new ArrayList<>();
		paramIDList.addAll(listDestination);
		paramIDList.add(sIDlogin);
		List<MailDestinationImport> mailResultList = envAdapter.getEmpEmailAddress(companyID, paramIDList, 6);
		String loginMail = mailResultList.stream().filter(x -> x.getEmployeeID().equals(sIDlogin)).findAny()
				.map(x -> { 
					if(CollectionUtil.isEmpty(x.getOutGoingMails()) || x.getOutGoingMails().get(0)==null){
						return ""; 
					} else { 
						return x.getOutGoingMails().get(0).getEmailAddress(); 
					} 
				}).orElse("");
		String loginName = employeeAdaptor.getEmployeeName(sIDlogin);
		String applicantName = employeeAdaptor.getEmployeeName(application.getEmployeeID());
		for(String employeeID : listDestination){
			String employeeName = employeeAdaptor.getEmployeeName(employeeID);
			String approverMail = mailResultList.stream().filter(x -> x.getEmployeeID().equals(employeeID)).findAny()
					.map(x -> { 
						if(CollectionUtil.isEmpty(x.getOutGoingMails()) || x.getOutGoingMails().get(0)==null){
							return ""; 
						} else { 
							return x.getOutGoingMails().get(0).getEmailAddress(); 
						} 
					}).orElse("");
			if(Strings.isBlank(approverMail)){
				failList.add(employeeName);
				continue;
			}
			String URL = "";
			// ドメインモデル「メール内容のURL埋込設定」を取得する
			Optional<UrlEmbedded> opUrlEmbedded = urlEmbeddedRepository.getUrlEmbeddedById(companyID);
			if(opUrlEmbedded.isPresent()){
				URL = registerEmbededURL.registerEmbeddedForApp(
						application.getAppID(), 
						application.getAppType().value, 
						application.getPrePostAtr().value, 
						"", 
						employeeID);
			};
			Optional<AppDispName> opAppDispName = appDispNameRepository.getDisplay(application.getAppType().value);
			if(!opAppDispName.isPresent() || opAppDispName.get().getDispName()==null){
				throw new RuntimeException("no setting AppDispName 申請表示名");
			}
			AppDispName appDispName = opAppDispName.get();
			String appContent = applicationContentService.getApplicationContent(application);
			String newText = Strings.isNotBlank(URL) ? text + "\n" + URL : text;
			String mailContentToSend = I18NText.getText("Msg_703",
					loginName, 
					newText,
					application.getAppDate().toLocalDate().toString(), 
					appDispName.getDispName().toString(),
					applicantName, 
					application.getAppDate().toLocalDate().toString(),
					appContent, 
					loginName, 
					loginMail);
			String mailTitle = application.getAppDate().toLocalDate().toString()+" "+appDispName.getDispName().toString();
			String mailBody = mailContentToSend;
			try {
				mailsender.sendFromAdmin(approverMail, new MailContents(mailTitle, mailBody));
				successList.add(employeeName);
			} catch (Exception e) {
				failList.add(employeeName);
			}
		}
		return new MailResult(successList, failList);
	}
	@Override
	public MailResult sendMailApplicant(Application_New application, String text) {
		List<String> successList = new ArrayList<>();
		List<String> failList = new ArrayList<>();
		String sIDlogin = AppContexts.user().employeeId();
		String companyID = AppContexts.user().companyId();
		String employeeID = application.getEmployeeID();
		String employeeName = employeeAdaptor.getEmployeeName(employeeID);
		List<String> listDestination = new ArrayList<>(Arrays.asList(sIDlogin, employeeID));
		List<MailDestinationImport> mailResultList = envAdapter.getEmpEmailAddress(companyID, listDestination, 6);
		String loginMail = mailResultList.stream().filter(x -> x.getEmployeeID().equals(sIDlogin)).findAny()
				.map(x -> { 
					if(CollectionUtil.isEmpty(x.getOutGoingMails()) || x.getOutGoingMails().get(0)==null){ 
						return ""; 
					} else {
						return x.getOutGoingMails().get(0).getEmailAddress(); 
					} 
				}).orElse("");
		String loginName = employeeAdaptor.getEmployeeName(sIDlogin);
		String applicantName = employeeAdaptor.getEmployeeName(application.getEmployeeID());
		String applicantMail = mailResultList.stream().filter(x -> x.getEmployeeID().equals(employeeID)).findAny()
				.map(x -> { 
					if(CollectionUtil.isEmpty(x.getOutGoingMails()) || x.getOutGoingMails().get(0)==null){ 
						return ""; 
					} else { 
						return x.getOutGoingMails().get(0).getEmailAddress(); 
					} 
				}).orElse("");
		if(Strings.isBlank(applicantMail)){
			failList.add(employeeName);
			return new MailResult(successList, failList);
		}
		String URL = "";
		// ドメインモデル「メール内容のURL埋込設定」を取得する
		Optional<UrlEmbedded> opUrlEmbedded = urlEmbeddedRepository.getUrlEmbeddedById(companyID);
		if(opUrlEmbedded.isPresent()){
			URL = registerEmbededURL.registerEmbeddedForApp(
					application.getAppID(), 
					application.getAppType().value, 
					application.getPrePostAtr().value, 
					"", 
					employeeID);
		};
		Optional<AppDispName> opAppDispName = appDispNameRepository.getDisplay(application.getAppType().value);
		if(!opAppDispName.isPresent() || opAppDispName.get().getDispName()==null){
			throw new RuntimeException("no setting AppDispName 申請表示名");
		}
		AppDispName appDispName = opAppDispName.get();
		String appContent = applicationContentService.getApplicationContent(application);
		String newText = Strings.isNotBlank(URL) ? text + "\n" + URL : text;
		String mailContentToSend = I18NText.getText("Msg_703",
				loginName, 
				newText,
				application.getAppDate().toLocalDate().toString(), 
				appDispName.getDispName().toString(),
				applicantName, 
				application.getAppDate().toLocalDate().toString(),
				appContent, 
				loginName, 
				loginMail);
		String mailTitle = application.getAppDate().toLocalDate().toString()+" "+appDispName.getDispName().toString();
		String mailBody = mailContentToSend;
		try {
			mailsender.sendFromAdmin(applicantMail, new MailContents(mailTitle, mailBody));
			successList.add(employeeName);
		} catch (Exception e) {
			failList.add(employeeName);
		}
		return new MailResult(successList, failList);
	}
	@Override
	public List<GeneralDate> lstDateNotHoliday(String cid, String sid, DatePeriod dates) {
		List<GeneralDate> lstOutput = new ArrayList<>();
		for(int i = 0; dates.start().daysTo(dates.end()) - i >= 0; i++){
			GeneralDate loopDate = dates.start().addDays(i);
			//実績の取得
			AchievementOutput achInfor = collectAch.getAchievement(cid, sid, loopDate);
			if(achInfor != null 
					&& achInfor.getWorkType() != null
					&& workTypeRepo.checkHoliday(achInfor.getWorkType().getWorkTypeCode()) //1日休日の判定
					) {
				lstOutput.add(loopDate);
			}
		}
		return lstOutput;
	}
}
