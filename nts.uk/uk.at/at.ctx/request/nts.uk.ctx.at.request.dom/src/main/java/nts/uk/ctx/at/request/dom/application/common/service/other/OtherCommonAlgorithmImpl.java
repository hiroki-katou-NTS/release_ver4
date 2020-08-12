package nts.uk.ctx.at.request.dom.application.common.service.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.collection.CollectionUtil;
import nts.gul.mail.send.MailContents;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleImport;
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
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeAppAtr;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeInputRepository;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.application.overtime.service.CheckWorkingInfoResult;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispName;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
import nts.uk.ctx.at.request.dom.setting.company.emailset.AppEmailSet;
import nts.uk.ctx.at.request.dom.setting.company.emailset.AppEmailSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.emailset.Division;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.DisplayReason;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.DisplayReasonRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.displaysetting.DisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.workplace.WorkTimeWorkplaceRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.url.RegisterEmbededURL;

@Stateless
public class OtherCommonAlgorithmImpl implements OtherCommonAlgorithm {
	
	@Inject
	private EmployeeRequestAdapter employeeAdaptor;
//	@Inject
//	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepo;
	
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
	private AppDispNameRepository appDispNameRepository;
	
	@Inject
	private EnvAdapter envAdapter;
	
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
	
	@Inject
	private RecordWorkInfoAdapter recordWorkInfoAdapter;
	
	@Inject
	private ScBasicScheduleAdapter scBasicScheduleAdapter;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
//	@Inject
//	private AppTypeDiscreteSettingRepository appTypeSetRepo;
	@Inject
	private AppAbsenceRepository repoAbsence;
	@Inject
	private DisplayReasonRepository displayRep;
	
//	@Inject
//	private ApplicationReasonRepository applicationReasonRepository;
	
	@Inject
	private ApplicationRepository applicationRepository;
	
	@Inject
	private OvertimeRepository overtimeRepository;
	
	@Inject
	private OvertimeInputRepository overtimeInputRepository;
	
	@Inject
	private WorkTimeSettingRepository workTimeRepository;
	
	@Inject
	private AppEmailSetRepository appEmailSetRepository;
	
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
		return new PeriodCurrentMonth(closure.get().getClosureId(), datePeriod.start(), datePeriod.end());
	}
	/**
	 * 1.職場別就業時間帯を取得
	 */
	@Override
	public List<WorkTimeSetting> getWorkingHoursByWorkplace(String companyID, String employeeID, GeneralDate referenceDate) {
		// 申請本人の所属職場を含める上位職場を取得する
		List<String> wkpIDLst = employeeAdaptor.findWpkIdsBySid(companyID, employeeID, referenceDate);
		// 取得した所属職場ID＋その上位職場IDを先頭から最後までループする
		// Loop theo AffWorkplace ID + upperWorkplaceID  từ đầu đến cuối
		for(String wkpID : wkpIDLst) {
			// アルゴリズム「職場IDから職場別就業時間帯を取得」を実行する
			List<WorkTimeSetting> listWorkTime = workTimeWorkplaceRepo.getWorkTimeWorkplaceById(companyID, wkpID);
			if(listWorkTime.size()>0) {
				Collections.sort(listWorkTime, Comparator.comparing(x -> x.getWorktimeCode().v()));
				return listWorkTime;
			}
		}
		// アルゴリズム「廃止区分によって就業時間帯を取得する」を実行する
		return workTimeSettingRepository.findByCompanyId(companyID).stream()
			.filter(x -> x.getAbolishAtr()==AbolishAtr.NOT_ABOLISH)
			.sorted(Comparator.comparing(x -> x.getWorktimeCode().v())).collect(Collectors.toList());
	}

	@Override
	public PrePostAtr preliminaryJudgmentProcessing(ApplicationType appType, GeneralDate appDate, OvertimeAppAtr overtimeAppAtr) {
//		GeneralDate systemDate = GeneralDate.today();
//		Integer systemTime = GeneralDateTime.now().localDateTime().getHour()*60 
//				+ GeneralDateTime.now().localDateTime().getMinute();
//		String companyID = AppContexts.user().companyId();
//		PrePostAtr prePostAtr = null;
//		Optional<AppTypeDiscreteSetting> appTypeDisc = appTypeDiscreteSettingRepo.getAppTypeDiscreteSettingByAppType(companyID, appType.value);
//		Optional<RequestSetting> requestSetting = this.requestSettingRepository.findByCompany(companyID);
//		List<ReceptionRestrictionSetting> receptionRestrictionSetting = new ArrayList<>();
//		if(requestSetting.isPresent()){
//			receptionRestrictionSetting = requestSetting.get().getApplicationSetting().getListReceptionRestrictionSetting().stream().filter(x -> x.getAppType().equals(ApplicationType.OVER_TIME_APPLICATION)).collect(Collectors.toList());
//		}
//		//if appdate > systemDate 
//		if (appDate == null || appDate.equals(systemDate)) { // if appDate = systemDate
////			// if RetrictPreUseFlg = notuse ->prePostAtr = POSTERIOR
////			if(appTypeDisc.get().getRetrictPreUseFlg() == UseAtr.NOTUSE) {
////				prePostAtr = PrePostAtr.POSTERIOR;
////			}else {
////				//「事前の受付制限」．チェック方法が日数でチェック
////				if(appTypeDisc.get().getRetrictPreMethodFlg() == CheckMethod.DAYCHECK) {
////					prePostAtr = PrePostAtr.POSTERIOR;
////				}else {//システム日時と受付制限日時と比較する
////					if(systemTime.compareTo(appTypeDisc.get().getRetrictPreTimeDay().v())==1) {
////						
////						prePostAtr = PrePostAtr.POSTERIOR;
////					}else { // if systemDateTime <=  RetrictPreTimeDay - > xin truoc
////						prePostAtr = PrePostAtr.PREDICT;
////					}
////				}
////			}
//			if(appType == ApplicationType.OVER_TIME_APPLICATION){
//				if(appTypeDisc.get().getRetrictPreMethodFlg() == CheckMethod.DAYCHECK) {
//					prePostAtr = PrePostAtr.POSTERIOR;
//				}else{
//					int resultCompare = 0;
//					if(overtimeAppAtr == OvertimeAppAtr.EARLY_OVERTIME && receptionRestrictionSetting.get(0).getBeforehandRestriction().getPreOtTime() != null){
//						resultCompare = systemTime.compareTo(receptionRestrictionSetting.get(0).getBeforehandRestriction().getPreOtTime().v());
//					}else if(overtimeAppAtr == OvertimeAppAtr.NORMAL_OVERTIME && receptionRestrictionSetting.get(0).getBeforehandRestriction().getNormalOtTime() !=  null){
//						resultCompare = systemTime.compareTo(receptionRestrictionSetting.get(0).getBeforehandRestriction().getNormalOtTime().v());
//					}else if(overtimeAppAtr == OvertimeAppAtr.EARLY_NORMAL_OVERTIME && receptionRestrictionSetting.get(0).getBeforehandRestriction().getTimeBeforehandRestriction() !=  null){
//						resultCompare = systemTime.compareTo(receptionRestrictionSetting.get(0).getBeforehandRestriction().getTimeBeforehandRestriction().v());
//					}
//					if(resultCompare == 1) {
//						prePostAtr = PrePostAtr.POSTERIOR;
//					}else { // if systemDateTime <=  RetrictPreTimeDay - > xin truoc
//						prePostAtr = PrePostAtr.PREDICT;
//					}
//				}
//			}else{
//				prePostAtr = PrePostAtr.POSTERIOR;
//			}
//			
//		} else if(appDate.after(systemDate) ) {
//			//xin truoc 事前事後区分= 事前
//			prePostAtr = PrePostAtr.PREDICT;
//			
//		} else if(appDate.before(systemDate)) { // if appDate < systemDate
//			//xin sau 事前事後区分= 事後
//			prePostAtr = PrePostAtr.POSTERIOR;
//		}
//		
//			
//		return prePostAtr;
		return null;
	}
	/**
	 * 5.事前事後区分の判断
	 */
	@Override
	public InitValueAtr judgmentPrePostAtr(ApplicationType appType, GeneralDate appDate,boolean checkCaller) {
		InitValueAtr outputInitValueAtr = null;
//		String companyID = AppContexts.user().companyId();
//		Optional<AppTypeDiscreteSetting> appTypeDisc = appTypeDiscreteSettingRepo.getAppTypeDiscreteSettingByAppType(companyID, appType.value);
//		Optional<ApplicationSetting> appSetting = appSettingRepo.getApplicationSettingByComID(companyID);
//		if(appSetting.get().getDisplayPrePostFlg() == AppDisplayAtr.DISPLAY) { // AppDisplayAtr displayPrePostFlg
//			//メニューから起動(Boot from menu) : checkCaller == true
//			if(checkCaller) {
//				outputInitValueAtr = appTypeDisc.get().getPrePostInitFlg();
//			}else {// その他のPG（日別修正、トップページアラーム、残業指示）から起動(Start from other PG (daily correction, top page alarm, overtime work instruction)): checkCaller == false
//				outputInitValueAtr = InitValueAtr.POST;
//			}
//		}else { //if not display
//			outputInitValueAtr = InitValueAtr.NOCHOOSE;
//		}
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
	public MailResult sendMailApproverApprove(List<String> employeeIDList, Application application) {
		// ドメインモデル「申請メール設定」を取得する(get domain model 「」)
		AppEmailSet appEmailSet = appEmailSetRepository.findByDivision(Division.APPLICATION_APPROVAL);
		// アルゴリズム「承認者へ送る」を実行する(thực hiện thuật toán 「Gửi tới người phê duyệt」)
		MailResult mailResult = sendMailApprover(employeeIDList, application, appEmailSet.getEmailContentLst().get(0).getOpEmailText().map(x -> x.v()).orElse(""));
		return new MailResult(mailResult.getSuccessList(), mailResult.getFailList(), mailResult.getFailServerList());
	}
	@Override
	public MailResult sendMailApproverDelete(List<String> employeeIDList, Application application) {
		String inputText = I18NText.getText("Msg_1262",Collections.emptyList());
		// アルゴリズム「承認者へ送る」を実行する (Thực hiện thuật toán "Gửi tới người phê duyệt")
		MailResult mailResult = sendMailApprover(employeeIDList, application, inputText);
		return new MailResult(mailResult.getSuccessList(), mailResult.getFailList(), mailResult.getFailServerList());
	}
	@Override
	public MailResult sendMailApplicantApprove(Application application) {
		String inputText = I18NText.getText("Msg_1263",Collections.emptyList());
		MailResult mailResult = sendMailApplicant(application, inputText);
		return new MailResult(mailResult.getSuccessList(), mailResult.getFailList(), mailResult.getFailServerList());
	}
	@Override
	public MailResult sendMailApplicantDeny(Application application) {
		String inputText = I18NText.getText("Msg_1264",Collections.emptyList());
		MailResult mailResult = sendMailApplicant(application, inputText);
		return new MailResult(mailResult.getSuccessList(), mailResult.getFailList(), mailResult.getFailServerList());
	}
	@Override
	public MailResult sendMailApprover(List<String> listDestination, Application application, String text) {
		List<String> successList = new ArrayList<>();
		List<String> failList = new ArrayList<>();
		List<String> failServerList = new ArrayList<>(); 
		String sIDlogin = AppContexts.user().employeeId();
		String companyID = AppContexts.user().companyId();
		List<String> paramIDList = new ArrayList<>();
		paramIDList.addAll(listDestination);
		paramIDList.add(sIDlogin);
		// ログイン者のメールアドレスを取得する
		List<MailDestinationImport> mailResultList = envAdapter.getEmpEmailAddress(companyID, paramIDList, 6);
		String loginMail = mailResultList.stream().filter(x -> x.getEmployeeID().equals(sIDlogin)).findAny()
				.map(x -> { 
					if(CollectionUtil.isEmpty(x.getOutGoingMails()) || x.getOutGoingMails().get(0)==null){
						return ""; 
					} else { 
						return x.getOutGoingMails().get(0).getEmailAddress(); 
					} 
				}).orElse("");
		// ログイン者の社員名を取得する
		String loginName = employeeAdaptor.getEmployeeName(sIDlogin);
		// 申請者の社員名を取得する
		String applicantName = employeeAdaptor.getEmployeeName(application.getEmployeeID());
		for(String employeeID : listDestination){
			String employeeName = employeeAdaptor.getEmployeeName(employeeID);
			// 対象者のメールアドレスを取得する
			String approverMail = mailResultList.stream().filter(x -> x.getEmployeeID().equals(employeeID)).findAny()
					.map(x -> { 
						if(CollectionUtil.isEmpty(x.getOutGoingMails()) || x.getOutGoingMails().get(0)==null){
							return ""; 
						} else { 
							return x.getOutGoingMails().get(0).getEmailAddress(); 
						} 
					}).orElse("");
			if(Strings.isBlank(approverMail)){
				// エラーメッセージ Msg_768　対象者氏名　エラーリストにセットする
				failList.add(employeeName);
				continue;
			}
			String URL = "";
			// ドメインモデル「申請メール設定」を取得する
			AppEmailSet appEmailSet = appEmailSetRepository.findByCID(companyID);
			if(appEmailSet.getUrlReason() == NotUseAtr.USE){
				// 埋込URL情報登録申請(application đăng ký info URL nén)
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
			//アルゴリズム「申請理由出力_共通」を実行する -> xu ly trong ham get content
			String appContent = applicationContentService.getApplicationContent(application);
			String newText = Strings.isNotBlank(URL) ? text + "\n" + URL : text;
			String mailContentToSend = I18NText.getText("Msg_703",
					loginName, 
					newText,
					application.getAppDate().getApplicationDate().toLocalDate().toString(), 
					appDispName.getDispName().toString(),
					applicantName, 
					application.getAppDate().getApplicationDate().toLocalDate().toString(),
					appContent, 
					loginName, 
					loginMail);
			String mailTitle = application.getAppDate().getApplicationDate().toLocalDate().toString()+" "+appDispName.getDispName().toString();
			String mailBody = mailContentToSend;
			try {
				mailsender.sendFromAdmin(approverMail, new MailContents(mailTitle, mailBody));
				successList.add(employeeName);
			} catch (Exception e) {
				failServerList.add(employeeName);
			}
		}
		return new MailResult(successList, failList, failServerList);
	}
	@Override
	public MailResult sendMailApplicant(Application application, String text) {
		List<String> successList = new ArrayList<>();
		List<String> failList = new ArrayList<>();
		List<String> failServerList = new ArrayList<>();
		String sIDlogin = AppContexts.user().employeeId();
		String companyID = AppContexts.user().companyId();
		String employeeID = application.getEmployeeID();
		String employeeName = employeeAdaptor.getEmployeeName(employeeID);
		List<String> listDestination = new ArrayList<>(Arrays.asList(sIDlogin, employeeID));
		// ログイン者のメールアドレスを取得する
		List<MailDestinationImport> mailResultList = envAdapter.getEmpEmailAddress(companyID, listDestination, 6);
		String loginMail = mailResultList.stream().filter(x -> x.getEmployeeID().equals(sIDlogin)).findAny()
				.map(x -> { 
					if(CollectionUtil.isEmpty(x.getOutGoingMails()) || x.getOutGoingMails().get(0)==null){ 
						return ""; 
					} else {
						return x.getOutGoingMails().get(0).getEmailAddress(); 
					} 
				}).orElse("");
		// ログイン者の社員名を取得する
		String loginName = employeeAdaptor.getEmployeeName(sIDlogin);
		// 社員名を取得する
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
			// エラーメッセージ Msg_768　対象者氏名　エラーリストにセットする
			failList.add(employeeName);
			return new MailResult(successList, failList, failServerList);
		}
		String URL = "";
		// ドメインモデル「申請メール設定」を取得する
		AppEmailSet appEmailSet = appEmailSetRepository.findByCID(companyID);
		if(appEmailSet.getUrlReason() == NotUseAtr.USE){
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
		// 申請を差し戻すメール本文の編集
		String appContent = applicationContentService.getApplicationContent(application);
		String newText = Strings.isNotBlank(URL) ? text + "\n" + URL : text;
		String mailContentToSend = I18NText.getText("Msg_703",
				loginName, 
				newText,
				application.getAppDate().getApplicationDate().toLocalDate().toString(), 
				appDispName.getDispName().toString(),
				applicantName, 
				application.getAppDate().getApplicationDate().toLocalDate().toString(),
				appContent, 
				loginName, 
				loginMail);
		String mailTitle = application.getAppDate().getApplicationDate().toLocalDate().toString()+" "+appDispName.getDispName().toString();
		String mailBody = mailContentToSend;
		try {
			mailsender.sendFromAdmin(applicantMail, new MailContents(mailTitle, mailBody));
			successList.add(employeeName);
		} catch (Exception e) {
			failServerList.add(employeeName);
		}
		return new MailResult(successList, failList, failServerList);
	}
	@Override
	public List<GeneralDate> lstDateIsHoliday(String cid, String sid, DatePeriod dates) {
		List<GeneralDate> lstOutput = new ArrayList<>();
		for(int i = 0; dates.start().daysTo(dates.end()) - i >= 0; i++){
			GeneralDate loopDate = dates.start().addDays(i);
			//実績の取得
			/*AchievementOutput achInfor = collectAch.getAchievement(cid, sid, loopDate);*/
			AchievementOutput achInfor = null;
			if(achInfor != null 
					&& achInfor.getWorkType() != null
					&& workTypeRepo.checkHoliday(achInfor.getWorkType().getWorkTypeCode()) //1日休日の判定
					) {
				lstOutput.add(loopDate);
			}
		}
		return lstOutput;
	}
	@Override
	public WorkType getWorkTypeScheduleSpec(String companyID, String employeeID, GeneralDate appDate) {
		// Imported(申請承認)「勤務実績」を取得する
		RecordWorkInfoImport recordWorkInfoImport = recordWorkInfoAdapter.getRecordWorkInfo(employeeID, appDate);
		if(Strings.isNotBlank(recordWorkInfoImport.getWorkTypeCode())){
			String workTypeCd = recordWorkInfoImport.getWorkTypeCode();
			// ドメインモデル「勤務種類」を1件取得する
			Optional<WorkType> opWorkType = workTypeRepository.findByPK(companyID, workTypeCd);
			if(!opWorkType.isPresent()){
				return null;
			}
			WorkType workType = opWorkType.get();
			return workType;
		}
		// Imported(申請承認)「勤務予定」を取得する
		Optional<ScBasicScheduleImport> opScBasicScheduleImport = scBasicScheduleAdapter.findByID(employeeID, appDate);
		if(opScBasicScheduleImport.isPresent()){
			String workTypeCd = opScBasicScheduleImport.get().getWorkTypeCode();
			// ドメインモデル「勤務種類」を1件取得する
			Optional<WorkType> opWorkType = workTypeRepository.findByPK(companyID, workTypeCd);
			if(!opWorkType.isPresent()){
				return null;
			}
			WorkType workType = opWorkType.get();
			return workType;
		}
		return null;
	}
	/**
	 * 申請理由出力_共通
	 * @author hoatt
	 * @param 申請 application
	 * @param 休暇種類(Optional) holidayType
	 * @return 結果(使用/未使用)
	 */
	@Override
	public boolean appReasonOutFlg(Application application, Optional<Integer> holidayType) {
		String companyId = AppContexts.user().companyId();
		if(application.isAbsenceApp()){
			if(!holidayType.isPresent()){
				//ドメインモデル「休暇申請」を取得する
				Optional<AppAbsence> absence = repoAbsence.getAbsenceById(companyId, application.getAppID());
				if(absence.isPresent()){
					holidayType = Optional.of(absence.get().getHolidayAppType().value);
				}
			}
			if(holidayType.isPresent()){
				//ドメインモデル「申請理由表示」を取得する
				Optional<DisplayReason> disReason = displayRep.findByHdType(companyId, application.getAppType().value);
				if(disReason.isPresent() && disReason.get().getDisplayFixedReason().equals(DisplayAtr.NOT_DISPLAY)
						 && disReason.get().getDisplayAppReason().equals(DisplayAtr.NOT_DISPLAY)){
					//定型理由の表示＝しない　AND 申請理由の表示＝しない
					return false;//output：・結果＝未使用
				}
				return true;//output：・結果＝使用
			}
			return true;
		}else{
			//ドメインモデル「申請種類別設定」を取得する
//			Optional<AppTypeDiscreteSetting> appTypeSet = appTypeSetRepo.getAppTypeDiscreteSettingByAppType(companyId, application.getAppType().value);
//			if(appTypeSet.isPresent() && appTypeSet.get().getTypicalReasonDisplayFlg().equals(AppDisplayAtr.NOTDISPLAY) &&
//					appTypeSet.get().getDisplayReasonFlg().equals(AppDisplayAtr.NOTDISPLAY)){
//				//定型理由の表示＝しない　AND 申請理由の表示＝しない
//				return false;//output：・結果＝未使用
//			}
			return true;//output：・結果＝使用
		}
	}
	
//	@Override
//	public List<ApplicationReason> getApplicationReasonType(String companyID, DisplayAtr typicalReasonDisplayFlg, ApplicationType appType) {
//		// Input．申請種類をチェックする
//		if(appType != ApplicationType.ABSENCE_APPLICATION) {
//			// Input．定型理由の表示区分をチェック
//			if (typicalReasonDisplayFlg == DisplayAtr.NOT_DISPLAY) {
//				return Collections.emptyList();
//			}
//		}
//		// ドメインモデル「申請定型理由」を取得
//		List<ApplicationReason> applicationReasons = applicationReasonRepository.getReasonByAppType(companyID, appType.value);
//		return applicationReasons;
//		
//	}
	
	@Override
	public boolean displayAppReasonContentFlg(AppDisplayAtr displayReasonFlg) {
		// Input．申請理由の表示区分をチェック
		if (displayReasonFlg == AppDisplayAtr.DISPLAY) {
			return true;
		}
		return false;
	}
	
	@Override
	public AppOverTime getPreApplication(String employeeID, PrePostAtr prePostAtr, UseAtr preDisplayAtr, GeneralDate appDate, ApplicationType appType) {
//		String companyID =  AppContexts.user().companyId();
//		AppOverTime result = new AppOverTime();
//		if (prePostAtr == PrePostAtr.POSTERIOR) {
//			if(preDisplayAtr == UseAtr.USE){
//				List<Application_New> applicationLst = applicationRepository.getApp(employeeID, appDate, PrePostAtr.PREDICT.value, appType.value);
//				if(!CollectionUtil.isEmpty(applicationLst)){
//					Application_New applicationOvertime = Application_New.firstCreate(companyID, prePostAtr, appDate, appType, employeeID, new AppReason(Strings.EMPTY));
//					applicationOvertime.setAppDate(applicationLst.get(0).getAppDate());
//					Optional<AppOverTime> appOvertime = this.overtimeRepository
//							.getAppOvertime(applicationLst.get(0).getCompanyID(), applicationLst.get(0).getAppID());
//					if (appOvertime.isPresent()) {
//						result.setWorkTypeCode(appOvertime.get().getWorkTypeCode());
//						result.setSiftCode(appOvertime.get().getSiftCode());
//						result.setWorkClockFrom1(appOvertime.get().getWorkClockFrom1());
//						result.setWorkClockTo1(appOvertime.get().getWorkClockTo1());
//						result.setWorkClockFrom2(appOvertime.get().getWorkClockFrom2());
//						result.setWorkClockTo2(appOvertime.get().getWorkClockTo2());
//
//						List<OverTimeInput> overtimeInputs = overtimeInputRepository.getOvertimeInputByAttendanceId(
//								appOvertime.get().getCompanyID(), appOvertime.get().getAppID(),
//								AttendanceType.NORMALOVERTIME.value);
//						result.setOverTimeInput(overtimeInputs);
//						result.setOverTimeShiftNight(appOvertime.get().getOverTimeShiftNight());
//						result.setFlexExessTime(appOvertime.get().getFlexExessTime());
//						result.setApplication(applicationOvertime);
//						result.setAppID(appOvertime.get().getAppID());
//						return result;
//					}
//				}
//			}
//		}
		return null;
	}
	
	/**
	 * 12.マスタ勤務種類、就業時間帯データをチェック
	 * @param companyID
	 * @param wkTypeCode
	 * @param wkTimeCode
	 * @return
	 */
	@Override
	public CheckWorkingInfoResult checkWorkingInfo(String companyID, String wkTypeCode, String wkTimeCode) {
		CheckWorkingInfoResult result = new CheckWorkingInfoResult();
		
		
		// 「勤務種類CD ＝＝ Null」 をチェック
		boolean isWkTypeCDNotEmpty = !StringUtil.isNullOrEmpty(wkTypeCode, true);
		if (isWkTypeCDNotEmpty) {
			String WkTypeName = null;
			Optional<WorkType> wkTypeOpt = this.workTypeRepository.findByPK(companyID, wkTypeCode);
			if (wkTypeOpt.isPresent()) {
				WkTypeName = wkTypeOpt.get().getName().v();
			}
			// 「勤務種類名称を取得する」 ＝＝NULL をチェック
			boolean isWkTypeNameEmpty = StringUtil.isNullOrEmpty(WkTypeName, true);
			if (isWkTypeNameEmpty ) {
				// 勤務種類エラーFlg ＝ True
				result.setWkTypeError(true);
			}
		}
		// 「就業時間帯CD ＝＝ NULL」をチェック
		boolean isWkTimeCDNotEmpty = !StringUtil.isNullOrEmpty(wkTimeCode, true);
		if (isWkTimeCDNotEmpty) {
			// 「就業時間帯名称を取得する」＝＝ NULL をチェック
			String WkTimeName = null;
			Optional<WorkTimeSetting> wwktimeOpt = this.workTimeRepository.findByCode(companyID, wkTimeCode);
			if (wwktimeOpt.isPresent()) {
				WkTimeName = wwktimeOpt.get().getWorkTimeDisplayName().getWorkTimeName().v();
			}
			boolean isWkTimeNameEmpty = StringUtil.isNullOrEmpty(WkTimeName, true);
			if (isWkTimeNameEmpty) {
				// 就業時間帯エラーFlg ＝ True
				result.setWkTimeError(true);
			}
		}
			
		
		return result;
	}
}
