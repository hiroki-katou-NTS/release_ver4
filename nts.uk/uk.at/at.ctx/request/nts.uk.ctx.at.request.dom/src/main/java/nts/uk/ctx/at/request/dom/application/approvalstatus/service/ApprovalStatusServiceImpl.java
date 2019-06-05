package nts.uk.ctx.at.request.dom.application.approvalstatus.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.mail.send.MailContents;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.appabsence.appforspecleave.AppForSpecLeave;
import nts.uk.ctx.at.request.dom.application.applist.service.AppCompltLeaveSync;
import nts.uk.ctx.at.request.dom.application.applist.service.CheckExitSync;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppCompltLeaveFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppDetailInfoRepository;
import nts.uk.ctx.at.request.dom.application.approvalstatus.ApprovalStatusMailTemp;
import nts.uk.ctx.at.request.dom.application.approvalstatus.ApprovalStatusMailTempRepository;
import nts.uk.ctx.at.request.dom.application.approvalstatus.ApprovalStatusMailType;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApplicationApprContent;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApplicationsListOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalStatusEmployeeOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttAppDetail;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttAppOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttByEmpListOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttDetailRecord;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApproverOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApproverSpecial;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.DailyStatus;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.EmployeeEmailOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.MailTransmissionContentOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.MailTransmissionContentResultOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.PeriodOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.SendMailResultOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.UnApprovalPerson;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.UnApprovalPersonAndResult;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.UnApprovalSendMail;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.WorkplaceInfor;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.AtEmploymentAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.AffWorkplaceImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.EmployeeEmailImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.EmploymentHisImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendanceitem.AttendanceResultImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendanceitem.DailyAttendanceItemAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.EnvAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.dto.MailDestinationImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.AgentAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentDataRequestPubImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalBehaviorAtrImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalFrameImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.EmployeeBasicInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AppCompltLeaveSyncOutput;
import nts.uk.ctx.at.request.dom.application.stamp.AppStamp;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampRepository;
import nts.uk.ctx.at.request.dom.application.stamp.StampRequestMode;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispName;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.RequestOfEachCompanyRepository;
import nts.uk.ctx.at.request.dom.setting.workplace.RequestOfEachWorkplaceRepository;
import nts.uk.ctx.at.shared.dom.relationship.repository.RelationshipRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.url.RegisterEmbededURL;
import nts.uk.shr.com.url.UrlParamAtr;
import nts.uk.shr.com.url.UrlTaskIncre;

@Stateless
public class ApprovalStatusServiceImpl implements ApprovalStatusService {
	@Inject
	private EmployeeRequestAdapter employeeRequestAdapter;

	@Inject
	private ApplicationRepository_New appRepoNew;

	@Inject
	private ApprovalStatusMailTempRepository approvalStatusMailTempRepo;

	@Inject
	private MailSender mailsender;

	@Inject
	private AgentAdapter agentApdater;

	@Inject
	private ApprovalRootStateAdapter approvalStateAdapter;

	@Inject
	private WorkplaceAdapter workplaceAdapter;

	@Inject
	private RegisterEmbededURL registerEmbededURL;

	@Inject
	private AppDispNameRepository repoAppDispName;

	@Inject
	private AppStampRepository repoAppStamp;

	@Inject
	private CollectAchievement collectAchievement;

	@Inject
	private DailyAttendanceItemAdapter dailyAttendanceItemAdapter;

	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;

	@Inject
	private RelationshipRepository repoRelationship;

	@Inject
	private AppAbsenceRepository repoAbsence;

	@Inject
	private HdAppSetRepository repoHdAppSet;

	@Inject
	private AtEmploymentAdapter atEmploymentAdapter;

	@Inject
	private AppDetailInfoRepository repoAppDetail;

	@Inject
	private ApplicationRepository_New repoApp;

	@Inject
	private RequestOfEachWorkplaceRepository repoRequestWkp;

	@Inject
	private RequestOfEachCompanyRepository repoRequestCompany;

	@Inject
	private WorkplaceAdapter wkpAdapter;

	@Inject
	private EnvAdapter envAdapter;

	@Inject
	private RequestSettingRepository requestSetRepo;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<ApprovalStatusEmployeeOutput> getApprovalStatusEmployee(String wkpId, GeneralDate closureStart,
			GeneralDate closureEnd, List<String> listEmpCd) {
		List<ApprovalStatusEmployeeOutput> listEmp = new ArrayList<>();
		// imported(申請承認)「社員ID（リスト）」を取得する
		// RequestList120-1
		List<AffWorkplaceImport> listEmpInOut = employeeRequestAdapter.getListSIdByWkpIdAndPeriod(wkpId, closureStart,
				closureEnd);
		// 社員ID(リスト)
		for (AffWorkplaceImport empInOut : listEmpInOut) {
			// imported(就業)「所属雇用履歴」より雇用コードを取得する
			DatePeriod datePeriod = new DatePeriod(closureStart, closureEnd);
			// RequestList264
			List<EmploymentHisImport> listEmpHist = atEmploymentAdapter.findByListSidAndPeriod(empInOut.getSId(),
					datePeriod);
			if (listEmpHist.isEmpty())
				continue;
			// 雇用（リスト）
			for (EmploymentHisImport empHist : listEmpHist) {

				// 取得した雇用コードが雇用コード(リスト)に存在する
				if (!listEmpCd.contains(empHist.getEmploymentCode())) {
					// 存在しない場合
					continue;
				}
				// 存在する場合
				// アルゴリズム「承認状況対象期間取得」を実行する
				PeriodOutput sttPeriod = this.getApprovalSttPeriod(empInOut.getSId(), empHist.getDatePeriod().start(),
						empHist.getDatePeriod().end(), closureStart, closureEnd, empInOut.getJobEntryDate(),
						empInOut.getRetirementDate());
				// 社員ID＜社員ID、期間＞(リスト)を追加する
				listEmp.add(new ApprovalStatusEmployeeOutput(empInOut.getSId(), sttPeriod.getStartDate(),
						sttPeriod.getEndDate()));
			}
		}
		return listEmp;
	}

	/**
	 * アルゴリズム「承認状況対象期間取得」を実行する
	 * 
	 * @param sId
	 *            社員ID
	 * @param empStartDate
	 *            雇用期間（開始日）
	 * @param empEndDate
	 *            雇用期間（終了日）
	 * @param closureStartDate
	 *            締め期間（開始日）
	 * @param closureEndDate
	 *            期間（終了日）
	 * @param entryDate
	 *            入退社期間（入社年月日）
	 * @param leaveDate
	 *            入退社期間（退社年月日）
	 * @return 期間（開始日～終了日）
	 */
	private PeriodOutput getApprovalSttPeriod(String sId, GeneralDate empStartDate, GeneralDate empEndDate,
			GeneralDate closureStartDate, GeneralDate closureEndDate, GeneralDate entryDate, GeneralDate leaveDate) {
		GeneralDate startDate;
		GeneralDate endDate;
		// 雇用期間（開始日）≦締め期間（開始日）
		if (empStartDate.beforeOrEquals(closureStartDate)) {
			// 対象期間.開始日＝締め期間（開始日）
			startDate = closureStartDate;
		} else {
			// 対象期間.開始日＝雇用期間（開始日）
			startDate = empStartDate;
		}
		// 対象期間.開始日≦入退社期間（入社年月日）
		if (startDate.beforeOrEquals(entryDate)) {
			// 対象期間.開始日＝入退社期間（入社年月日）
			startDate = entryDate;
		}
		// 雇用期間（終了日）≧締め期間（終了日）
		if (empEndDate.afterOrEquals(closureEndDate)) {
			// 対象期間終了日＝締め期間（終了日）
			endDate = closureEndDate;
		} else {
			// 対象期間.終了日＝雇用期間（終了日）
			endDate = empEndDate;
		}
		// 対象期間.開始日≧入退社期間（退社年月日）
		if (endDate.afterOrEquals(leaveDate)) {
			// 対象期間.開始日＝入退社期間（退社年月日）
			endDate = leaveDate;
		}
		return new PeriodOutput(startDate, endDate);
	}

	/**
	 * アルゴリズム「承認状況取得申請承認」を実行する
	 * 
	 * @param wkpInfoDto
	 * @return ApprovalSttAppDto
	 */
	@Override
	public ApprovalSttAppOutput getApprovalSttApp(WorkplaceInfor wkpInfor,
			List<ApprovalStatusEmployeeOutput> listAppStatusEmp) {
		List<ApprovalSttAppOutput> appSttAppliStateList = new ArrayList<>();
		
		val mailDestCache = this.approvalStateAdapter.createMailDestinationCache(AppContexts.user().companyId());
		
		for (ApprovalStatusEmployeeOutput approvalStt : listAppStatusEmp) {
			// アルゴリズム「承認状況取得申請」を実行する
			List<ApplicationApprContent> getAppSttAcquisitionAppl = this.getAppSttAcquisitionAppl(approvalStt, mailDestCache);
			// アルゴリズム「承認状況取得申請状態カウント」を実行する
			ApprovalSttAppOutput appStt = this.getCountAppSttAppliState(wkpInfor, getAppSttAcquisitionAppl);
			appSttAppliStateList.add(appStt);
		}

		if (appSttAppliStateList.isEmpty()) {
			return new ApprovalSttAppOutput(wkpInfor.getCode(), wkpInfor.getName(), false, false, null, null, null,
					null, null);
		} else {
			int numOfApp = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getNumOfApp).sum();
			int appNumOfCase = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getApprovedNumOfCase).sum();
			int numOfUnreflected = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getNumOfUnreflected)
					.sum();
			int numOfUnapproval = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getNumOfUnapproval)
					.sum();
			int numOfDenials = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getNumOfDenials).sum();
			Integer numOfAppDisp = numOfApp == 0 ? null : numOfApp;
			Integer appNumOfCaseDisp = appNumOfCase == 0 ? null : appNumOfCase;
			Integer numOfUnreflectedDisp = numOfUnreflected == 0 ? null : numOfUnreflected;
			Integer numOfUnapprovalDisp = numOfUnapproval == 0 ? null : numOfUnapproval;
			Integer numOfDenialsDisp = numOfDenials == 0 ? null : numOfDenials;
			boolean isEnable = true;
			if (Objects.isNull(numOfUnapprovalDisp) || numOfUnapprovalDisp <= 0) {
				isEnable = false;
			}
			return new ApprovalSttAppOutput(wkpInfor.getCode(), wkpInfor.getName(), isEnable, false, numOfAppDisp,
					appNumOfCaseDisp, numOfUnreflectedDisp, numOfUnapprovalDisp, numOfDenialsDisp);
		}
	}

	/**
	 * アルゴリズム「承認状況取得申請」を実行する
	 */
	private List<ApplicationApprContent> getAppSttAcquisitionAppl(ApprovalStatusEmployeeOutput approvalStt,
			ApprovalRootStateAdapter.MailDestinationCache mailDestCache) {
		List<ApplicationApprContent> listAppSttAcquisitionAppl = new ArrayList<>();
		String companyId = AppContexts.user().companyId();
		String sId = approvalStt.getSid();
		GeneralDate startDate = approvalStt.getStartDate();
		GeneralDate endDate = approvalStt.getEndDate();
		List<Application_New> listApp = appRepoNew.getListAppBySID(companyId, sId, startDate, endDate);
		if (!listApp.isEmpty()) {
			for (Application_New app : listApp) {
				// 申請承認内容(リスト）
				ApprovalRootContentImport_New approvalRoot = this.approvalStateAdapter.getApprovalRootContent(companyId,
						app.getEmployeeID(), app.getAppType().value, app.getAppDate(), app.getAppID(), false, mailDestCache);
				listAppSttAcquisitionAppl.add(new ApplicationApprContent(app, approvalRoot));
			}
		}
		return listAppSttAcquisitionAppl;
	}

	/**
	 * アルゴリズム「承認状況取得申請状態カウント」を実行する
	 */
	private ApprovalSttAppOutput getCountAppSttAppliState(WorkplaceInfor wkpInfor,
			List<ApplicationApprContent> listAppContent) {
		int numOfApp = 0;
		int numOfUnapproval = 0;
		int numOfUnreflected = 0;
		int approvedNumOfCase = 0;
		int numOfDenials = 0;
		for (ApplicationApprContent appContent : listAppContent) {
			Application_New app = appContent.getApplication();
			// 申請.反映情報.実績反映状態
			ReflectedState_New reflectState = app.getReflectionInformation().getStateReflectionReal();
			if (!ReflectedState_New.WAITCANCEL.equals(reflectState)
					|| !ReflectedState_New.CANCELED.equals(reflectState)) {
				numOfApp++;
				if (ReflectedState_New.NOTREFLECTED.equals(reflectState)
						|| ReflectedState_New.REMAND.equals(reflectState)) {
					numOfUnapproval++;
					numOfUnreflected++;
				} else if (ReflectedState_New.WAITREFLECTION.equals(reflectState)) {
					approvedNumOfCase++;
					numOfUnreflected++;
				} else if (ReflectedState_New.DENIAL.equals(reflectState)) {
					numOfDenials++;
				} else if (ReflectedState_New.REFLECTED.equals(reflectState)) {
					approvedNumOfCase++;
				}
			}
		}
		return new ApprovalSttAppOutput(wkpInfor.getCode(), wkpInfor.getName(), false, false, numOfApp,
				approvedNumOfCase, numOfUnreflected, numOfUnapproval, numOfDenials);
	}

	/**
	 * 承認状況社員メールアドレス取得
	 * 
	 * @return ・取得社員ID（リスト）＜社員ID、社員名、メールアドレス、期間＞
	 */
	@Override
	public List<EmployeeEmailImport> findEmpMailAddr(List<String> listsId) {
		String cid = AppContexts.user().companyId();
		// imported（就業）「個人社員基本情報」を取得する
		// RequestList126
		List<EmployeeEmailImport> listEmployee = employeeRequestAdapter.getApprovalStatusEmpMailAddr(listsId);
		// Imported（申請承認）「社員メールアドレス」を取得する
		// RequestList419
		List<MailDestinationImport> listMailEmp = envAdapter.getEmpEmailAddress(cid, listsId, 6);
		for (EmployeeEmailImport emp : listEmployee) {
			Optional<MailDestinationImport> empMailOtp = listMailEmp.stream()
					.filter(x -> x.getEmployeeID().equals(emp.getSId())).findFirst();
			empMailOtp.ifPresent(empMail -> {
				if (empMail.getOutGoingMails().isEmpty())
					return;
				emp.setMailAddr(empMail.getOutGoingMails().get(0).getEmailAddress());
			});
		}
		return listEmployee;
	}

	@Override
	public ApprovalStatusMailTemp getApprovalStatusMailTemp(int type) {
		String cId = AppContexts.user().companyId();
		Optional<ApprovalStatusMailTemp> data = approvalStatusMailTempRepo.getApprovalStatusMailTempById(cId, type);
		return data.isPresent() ? data.get() : null;
	}

	@Override
	public SendMailResultOutput sendTestMail(int mailType) {
		// 会社ID
		String cid = AppContexts.user().companyId();
		// アルゴリズム「承認状況メール本文取得」を実行する
		ApprovalStatusMailTemp domain = approvalStatusMailTempRepo.getApprovalStatusMailTempById(cid, mailType).get();
		// 社員ID
		String sid = AppContexts.user().employeeId();
		// 社員名
		String sName = employeeRequestAdapter.getEmployeeName(sid);
		// メールアドレス
		String mailAddr = this.confirmApprovalStatusMailSender();
		// 件名
		String subject = domain.getMailSubject().v();
		// 送信本文
		String text = domain.getMailContent().v();

		// ログイン者よりメール送信内容を作成する(create nội dung send mail theo người login)
		List<MailTransmissionContentOutput> listMailContent = new ArrayList<MailTransmissionContentOutput>();
		listMailContent.add(new MailTransmissionContentOutput(sid, sName, mailAddr, subject, text));
		// アルゴリズム「承認状況メール送信実行」を実行する
		return this.exeApprovalStatusMailTransmission(listMailContent, domain,
				EnumAdaptor.valueOf(mailType, ApprovalStatusMailType.class));
	}

	@Override
	public SendMailResultOutput exeApprovalStatusMailTransmission(List<MailTransmissionContentOutput> listMailContent,
			ApprovalStatusMailTemp domain, ApprovalStatusMailType mailType) {
		List<String> listError = new ArrayList<>();
		for (MailTransmissionContentOutput mailTransmission : listMailContent) {
			if(mailTransmission.getMailAddr() == null){
				// 送信エラー社員(リスト)と社員名、エラー内容を追加する
				listError.add(mailTransmission.getSName());
				continue;
			}
			// アルゴリズム「承認状況メール埋込URL取得」を実行する
			String embeddedURL = this.getEmbeddedURL(mailTransmission.getSId(), domain, mailType);
			try {
				// アルゴリズム「メールを送信する」を実行する
				mailsender.sendFromAdmin(mailTransmission.getMailAddr(),
						new MailContents(mailTransmission.getSubject(), mailTransmission.getText() + embeddedURL));
			} catch (Exception e) {
				// 送信エラー社員(リスト)と社員名、エラー内容を追加する
				listError.add(mailTransmission.getSName());
			}
		}
		SendMailResultOutput result = new SendMailResultOutput();
		if (listError.size() > 0) {
			result.setOK(false);
			result.setListError(listError);
		} else {
			result.setOK(true);
		}
		return result;
	}

	/**
	 * 承認状況メール埋込URL取得
	 */
	private String getEmbeddedURL(String eid, ApprovalStatusMailTemp domain, ApprovalStatusMailType mailType) {
		List<String> listUrl = new ArrayList<>();
		String contractCD = AppContexts.user().contractCode();
		String employeeCD = AppContexts.user().employeeCode();
		// 承認状況メールテンプレート.URL承認埋込
		if (NotUseAtr.USE.equals(domain.getUrlApprovalEmbed())) {
			List<UrlTaskIncre> listTask = new ArrayList<>();
			listTask.add(new UrlTaskIncre("", "", "", "activeMode", "approval", UrlParamAtr.URL_PARAM));
			// アルゴリズム「埋込URL情報登録」を実行する
			String url1 = registerEmbededURL.embeddedUrlInfoRegis("CMM045", "A", 1, 1, eid, contractCD, "", employeeCD, 0, listTask, Optional.empty());
			listUrl.add(url1);
		}
		// 承認状況メールテンプレート.URL日別埋込
		if (NotUseAtr.USE.equals(domain.getUrlDayEmbed())) {
			List<UrlTaskIncre> listTask = new ArrayList<>();
			if (ApprovalStatusMailType.DAILY_UNCONFIRM_BY_CONFIRMER.equals(mailType)) {
				// アルゴリズム「埋込URL情報登録」を実行する
				String url2 = registerEmbededURL.embeddedUrlInfoRegis("KDW004", "A", 1, 1, eid, contractCD, "", employeeCD, 0, listTask, Optional.empty());
				listUrl.add(url2);
			} else if (ApprovalStatusMailType.DAILY_UNCONFIRM_BY_PRINCIPAL.equals(mailType)){
				listTask.add(UrlTaskIncre.createFromJavaType("", "", "", "screenMode", "normal"));
				listTask.add(UrlTaskIncre.createFromJavaType("", "", "", "errorRef", "true"));
				listTask.add(UrlTaskIncre.createFromJavaType("", "", "", "changePeriod", "true"));
				// アルゴリズム「埋込URL情報登録」を実行する
				String url2 = registerEmbededURL.embeddedUrlInfoRegis("KDW003", "A", 1, 1, eid, contractCD, "", employeeCD, 0, listTask, Optional.empty());
				listUrl.add(url2);
			}
		}
		// 承認状況メールテンプレート.URL月別埋込
		if (NotUseAtr.USE.equals(domain.getUrlMonthEmbed())) {
			List<UrlTaskIncre> listTask = new ArrayList<>();
			listTask.add(new UrlTaskIncre("", "", "", "activeMode", "approval", UrlParamAtr.URL_PARAM));
			// アルゴリズム「埋込URL情報登録」を実行する
			String url3 = registerEmbededURL.embeddedUrlInfoRegis("KMW003", "A", 1, 1, eid, contractCD, "", employeeCD, 0, listTask, Optional.empty());
			listUrl.add(url3);
		}
		if (listUrl.size() == 0) {
			return "";
		}
		String url = StringUtils.join(listUrl, System.lineSeparator());
		return System.lineSeparator() + TextResource.localize("KAF018_190") + System.lineSeparator() + url;
	}

	@Override
	public String confirmApprovalStatusMailSender() {
		String sId = AppContexts.user().employeeId();
		List<String> listSId = new ArrayList<>();
		listSId.add(sId);
		// アルゴリズム「承認状況社員メールアドレス取得」を実行する
		Optional<EmployeeEmailImport> emp = this.findEmpMailAddr(listSId).stream().findFirst();
		if (!emp.isPresent()) {
			throw new BusinessException("Msg_791");
		}
		EmployeeEmailImport empEmail = emp.get();
		if (Objects.isNull(empEmail.getMailAddr()) || empEmail.getMailAddr().isEmpty()) {
			throw new BusinessException("Msg_791");
		}
		return empEmail.getMailAddr();
	}

	/**
	 * アルゴリズム「承認状況社員メールアドレス取得」を実行する RequestList #126
	 * 
	 * @return 取得社員ID＜社員ID、社員名、メールアドレス＞
	 */
	@Override
	public EmployeeEmailOutput findEmpMailAddr() {
		String cId = AppContexts.user().employeeId();
		List<String> listCId = new ArrayList<String>();
		listCId.add(cId);
		Optional<EmployeeEmailImport> employee = employeeRequestAdapter.getApprovalStatusEmpMailAddr(listCId).stream()
				.findFirst();
		return employee.isPresent() ? EmployeeEmailOutput.fromImport(employee.get()) : null;
	}

	/**
	 * アルゴリズム「承認状況未承認メール送信実行」を実行する
	 */
	@Override
	public SendMailResultOutput exeSendUnconfirmedMail(List<String> listWkpId, GeneralDate closureStart,
			GeneralDate closureEnd, List<String> listEmpCd) {
		List<ApprovalStatusEmployeeOutput> listTotalEmp = new ArrayList<>();
		for (String wkpId : listWkpId) {
			List<ApprovalStatusEmployeeOutput> listAppSttEmpOut = this.getApprovalStatusEmployee(wkpId, closureStart,
					closureEnd, listEmpCd);
			listTotalEmp.addAll(listAppSttEmpOut);
		}
		List<ApprovalStatusEmployeeOutput> listEmpOutput = listTotalEmp.stream().distinct()
				.collect(Collectors.toList());
		// アルゴリズム「承認状況未承認申請取得」を実行する
		List<UnApprovalPerson> listApprovalPerson = this.getUnapprovalForAppStt(listEmpOutput);
		// アルゴリズム「承認状況未承認メール本文取得」を実行する
		MailTransmissionContentResultOutput getMailTransmissContent = this.getMailTransmissContent(listApprovalPerson);
		// アルゴリズム「承認状況メール送信実行」を実行する
		return this.exeApprovalStatusMailTransmission(getMailTransmissContent.getListMailTransmisContent(),
				getMailTransmissContent.getMailDomain(), EnumAdaptor.valueOf(0, ApprovalStatusMailType.class));
	}

	/**
	 * 承認状況未承認申請取得
	 */
	private List<UnApprovalPerson> getUnapprovalForAppStt(List<ApprovalStatusEmployeeOutput> listEmpOutput) {
		List<UnApprovalPerson> listUnAppPerson = new ArrayList<>();

		val mailDestCache = this.approvalStateAdapter.createMailDestinationCache(AppContexts.user().companyId());
		
		for (ApprovalStatusEmployeeOutput appEmp : listEmpOutput) {
			// アルゴリズム「承認状況取得申請」を実行する
			List<ApplicationApprContent> listAppContent = this.getAppSttAcquisitionAppl(appEmp, mailDestCache);
			GeneralDate startDate = appEmp.getStartDate();
			GeneralDate endDate = appEmp.getEndDate();
			for (ApplicationApprContent app : listAppContent) {
				if (Objects.isNull(app)) {
					continue;
				}
				if (app.getApplication().getReflectionInformation().getStateReflectionReal().value != 0) {
					continue;
				}
				GeneralDate appDate = app.getApplication().getEndDate().get();
				// アルゴリズム「承認状況未承認メール対象者取得」を実行する
				List<String> listUnAppEmpIds = this.getUnApprovalMailTarget(app.getApprRootContentExport(), appDate);
				listUnAppEmpIds.stream().forEach(item -> {
					listUnAppPerson.add(new UnApprovalPerson(item, startDate, endDate));
				});
			}
		}
		return listUnAppPerson;
	}

	/**
	 * アルゴリズム「承認状況未承認メール対象者取得」を実行する
	 * 
	 * @param appRoot
	 * @param appDate
	 * @return List<UnApprovalPerson>
	 */
	private List<String> getUnApprovalMailTarget(ApprovalRootContentImport_New appRoot, GeneralDate appDate) {
		List<ApprovalPhaseStateImport_New> listPhaseState = appRoot.getApprovalRootState().getListApprovalPhaseState();
		List<String> listUnAppPerson = new ArrayList<>();
		boolean result = false;
		UnApprovalPersonAndResult getUnAppPersonAndResult = null;
		// クラス：承認フェーズClass: Approval Phase
		for (ApprovalPhaseStateImport_New appPhase : listPhaseState) {
			// 承認フェーズ.承認区分
			if (appPhase.getApprovalAtr().equals(ApprovalBehaviorAtrImport_New.APPROVED)
					|| appPhase.getApprovalAtr().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {
				continue;
			}
			List<ApprovalFrameImport_New> listAppFrame = appPhase.getListApprovalFrame();
			// クラス：承認枠
			for (ApprovalFrameImport_New appFrame : listAppFrame) {
				// 承認済、否認の場合
				if (appFrame.getApprovalAtr().equals(ApprovalBehaviorAtrImport_New.APPROVED)
						|| appFrame.getApprovalAtr().equals(ApprovalBehaviorAtrImport_New.DENIAL)) {
					continue;
				}
				// 未承認、差し戻しの場合
				// アルゴリズム「承認状況未承認メール未承認者取得」を実行する
				getUnAppPersonAndResult = this.getUnApprovalMailPerson(listAppFrame, appDate);
				if (getUnAppPersonAndResult.isResult()) {
					result = true;
				}
			}
			// 次の承認枠が存在しない場合
			listUnAppPerson = getUnAppPersonAndResult.getListUnAppPerson();
			if (result)
				return listUnAppPerson;
		}
		return Collections.emptyList();
	}

	/**
	 * 承認状況未承認メール未承認者取得
	 */
	private UnApprovalPersonAndResult getUnApprovalMailPerson(List<ApprovalFrameImport_New> listAppFrame,
			GeneralDate appDate) {
		String companyID = AppContexts.user().companyId();
		UnApprovalPersonAndResult unAppPersonAndResult = new UnApprovalPersonAndResult(null, false);
		List<String> listApprovalEmpId = new ArrayList<>();

		for (ApprovalFrameImport_New appFrame : listAppFrame) {
			if (Objects.isNull(appFrame)) {
				continue;
			}
			List<ApproverStateImport_New> listAppState = appFrame.getListApprover();
			for (ApproverStateImport_New appState : listAppState) {
				if (Objects.isNull(appFrame)) {
					continue;
				}
				listApprovalEmpId.add(appState.getApproverID());
			}
		}
		// imported（申請承認）「代行者」を取得する
		// RequestList310
		List<String> listUnAppPersonEmp = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			List<AgentInfoImport> listAgentInfor = agentApdater.findAgentByPeriod(companyID, listApprovalEmpId, appDate,
					appDate, i);
			// 対象が存在する場合
			if (listAgentInfor.size() > 0) {
				for (AgentInfoImport agent : listAgentInfor) {
					listUnAppPersonEmp.add(agent.getAgentID());
				}
			}
			// 対象が存在しない場合
			listUnAppPersonEmp.addAll(listApprovalEmpId);
		}

		if (!listUnAppPersonEmp.isEmpty()) {
			unAppPersonAndResult.setResult(true);
			unAppPersonAndResult.setListUnAppPerson(listUnAppPersonEmp);
		}
		return unAppPersonAndResult;
	}

	/**
	 * 承認状況未承認メール本文取得
	 */
	private MailTransmissionContentResultOutput getMailTransmissContent(List<UnApprovalPerson> listUnAppPerson) {
		List<MailTransmissionContentOutput> listMailTransmissContent = new ArrayList<>();
		// アルゴリズム「承認状況メール本文取得」を実行する
		ApprovalStatusMailTemp mailDomain = this
				.getApprovalStatusMailTemp(ApprovalStatusMailType.APP_APPROVAL_UNAPPROVED.value);
		MailTransmissionContentResultOutput mailTransContentResult = new MailTransmissionContentResultOutput(
				Collections.emptyList(), mailDomain);
		// 未承認者を社員ID順に並び替える
		// 未承認者（リスト）
		List<String> listEmpId = new ArrayList<>();
		for (UnApprovalPerson unAppPerson : listUnAppPerson) {
			listEmpId.add(unAppPerson.getSId());
		}
		// 次の未承認者の社員IDが異なる(EmployeeID chưa approval tiếp theo có khác không)
		// アルゴリズム「承認状況社員メールアドレス取得」を実行する
		// imported（就業）「個人社員基本情報」を取得する
		if (listEmpId.isEmpty())
			return mailTransContentResult;
		List<EmployeeEmailImport> listEmailEmployee = this.findEmpMailAddr(listEmpId);
		for (EmployeeEmailImport emp : listEmailEmployee) {
			// 件名
			String subject = mailDomain.getMailSubject().v();
			// 送信本文
			String text = mailDomain.getMailContent().v();
			listMailTransmissContent.add(
					new MailTransmissionContentOutput(emp.getSId(), emp.getSName(), emp.getMailAddr(), subject, text));
		}
		mailTransContentResult.setListMailTransmisContent(listMailTransmissContent);
		return mailTransContentResult;
	}

	/**
	 * アルゴリズム「承認状況未承認メール送信」を実行する
	 */
	@Override
	public List<String> getAppSttSendingUnapprovedMail(List<UnApprovalSendMail> listAppSttApp) {
		List<String> listWorksp = new ArrayList<>();
		// EA修正履歴 2125
		// this.confirmApprovalStatusMailSender();
		//hoatt - 2018.10.24
//		2018/10/24　EA2865
//		#102263
		// アルゴリズム「承認状況メール本文取得」を実行する
		//input： ・メール種類　＝　日別未確認(本人)
		ApprovalStatusMailTemp domain = this.getApprovalStatusMailTemp(ApprovalStatusMailType.DAILY_UNCONFIRM_BY_PRINCIPAL.value);
		//対象が存在しない場合
		if(domain == null){
			//メッセージ（#Msg_1458）を表示する
			throw new BusinessException("Msg_1458");
		}
		// 職場一覧のメール送信欄のチェックがONの件数
		if (listAppSttApp.stream().filter(x -> x.isChecked()).count() == 0) {
			throw new BusinessException("Msg_794");
		}
		return listWorksp;
	}

	@Override
	public List<ApprovalSttByEmpListOutput> getApprovalSttById(String selectedWkpId, List<String> listWkpId,
			GeneralDate startDate, GeneralDate endDate, List<String> listEmpCode) {
		List<ApprovalSttByEmpListOutput> lstApprovalSttByEmpList = new ArrayList<>();
		// アルゴリズム「承認状況取得社員」を実行する
		List<ApprovalStatusEmployeeOutput> listAppSttEmp = this.getApprovalStatusEmployee(selectedWkpId, startDate,
				endDate, listEmpCode);
		
		val mailDestCache = this.approvalStateAdapter.createMailDestinationCache(AppContexts.user().companyId());
		
		// 社員ID(リスト)
		for (ApprovalStatusEmployeeOutput appStt : listAppSttEmp) {
			List<String> listEmpId = new ArrayList<>();
			listEmpId.add(appStt.getSid());
			if (listEmpId.isEmpty())
				continue;
			// Imported（就業）「個人社員基本情報」を取得する
			// RequestList126
			String empName = "";
			List<EmployeeBasicInfoImport> listEmpInfor = this.workplaceAdapter.findBySIds(listEmpId);
			if (!listEmpInfor.isEmpty()) {
				EmployeeBasicInfoImport empInfo = listEmpInfor.stream().findFirst().get();
				empName = empInfo.getEmployeeCode() + "　" + empInfo.getPName();
			}
			// アルゴリズム「承認状況取得申請」を実行する
			List<ApplicationApprContent> listAppSttAcquisitionAppl = this.getAppSttAcquisitionAppl(appStt, mailDestCache);
			List<Application_New> listApprovalContent = new ArrayList<>();
			for (ApplicationApprContent applicationContent : listAppSttAcquisitionAppl) {
				Application_New app = applicationContent.getApplication();
				listApprovalContent.add(app);
			}
			// アルゴリズム「承認状況日別状態作成」を実行する
			List<DailyStatus> dailyStatus = this.getApprovalSttByDate(appStt.getStartDate(), appStt.getEndDate(),
					listApprovalContent);
			lstApprovalSttByEmpList.add(new ApprovalSttByEmpListOutput(appStt.getSid(), empName, dailyStatus,
					appStt.getStartDate(), appStt.getEndDate()));
		}
		return lstApprovalSttByEmpList;
	}

	/**
	 * 承認状況日別状態作成
	 */
	private List<DailyStatus> getApprovalSttByDate(GeneralDate startDate, GeneralDate endDate,
			List<Application_New> listApprovalContent) {
		List<DailyStatus> listDailyStatus = new ArrayList<>();
		for (Application_New app : listApprovalContent) {
			DailyStatus dailyStatus = new DailyStatus();
			ReflectedState_New reflectedState = app.getReflectionInformation().getStateReflectionReal();
			Integer symbol = null;
			switch (reflectedState) {
			case REFLECTED:
				symbol = 0;
				break;
			case WAITREFLECTION:
				symbol = 1;
				break;
			case DENIAL:
				symbol = 2;
				break;
			case NOTREFLECTED:
				symbol = 3;
				break;
			case REMAND:
				symbol = 3;
				break;
			case WAITCANCEL:
				continue;
			case CANCELED:
				continue;
			}
			GeneralDate dateTemp;
			// 申請開始日が期間内に存在する
			if (app.getStartDate().get().afterOrEquals(startDate) && app.getStartDate().get().beforeOrEquals(endDate)) {
				// 対象日付を申請開始日とする
				dateTemp = app.getStartDate().get();
			} else {
				// 対象日付を期間の開始日とする
				dateTemp = startDate;
			}

			Optional<DailyStatus> dailyStt = listDailyStatus.stream().filter(x -> x.getDate().equals(dateTemp))
					.findFirst();

			if (!dailyStt.isPresent()) {
				List<Integer> listSymbol = new ArrayList<>();
				listSymbol.add(symbol);
				dailyStatus = new DailyStatus(dateTemp, listSymbol);
				listDailyStatus.add(dailyStatus);
			} else {
				// 日別状態(リスト)に社員ID＝社員ID、日付＝対象日付が存在する
				Integer sbTemp = symbol;
				DailyStatus dailyTemp = dailyStt.get();
				if (dailyTemp.getStateSymbol().stream().filter(x -> x.equals(sbTemp)).count() == 0) {
					dailyTemp.getStateSymbol().add(sbTemp);
				}
			}
		}
		return listDailyStatus;
	}

	@Override
	public ApplicationsListOutput initApprovalSttRequestContentDis(List<ApprovalStatusEmployeeOutput> listStatusEmp) {
		List<ApplicationApprContent> listAppContents = new ArrayList<>();
		String companyId = AppContexts.user().companyId();

		val mailDestCache = this.approvalStateAdapter.createMailDestinationCache(companyId);
		
		// 期間（リスト）
		for (ApprovalStatusEmployeeOutput appEmp : listStatusEmp) {
			// アルゴリズム「承認状況取得申請」を実行する
			List<ApplicationApprContent> listAppContent = this.getAppSttAcquisitionAppl(appEmp, mailDestCache);
			listAppContents.addAll(listAppContent);
		}
		List<Application_New> listCompltLeaveSync = new ArrayList<>();
		List<ApplicationApprContent> listAppContentsSorted = this.sortById(listAppContents);
		for (ApplicationApprContent appContent : listAppContentsSorted) {
			if (appContent.getApplication().isAppCompltLeave())
				listCompltLeaveSync.add(appContent.getApplication());
		}
		// アルゴリズム「承認状況申請内容取得振休振出」を実行する
		List<AppCompltLeaveSync> listSync = this.getCompltLeaveSyncOutput(companyId, listCompltLeaveSync);
		GeneralDate endDateMax = this.findEndDateMax(listStatusEmp);
		// アルゴリズム「承認状況申請内容追加」を実行する
		List<ApprovalSttAppDetail> listApprovalAppDetail = this.getApprovalSttAppDetail(listAppContentsSorted,endDateMax);
		// ドメインモデル「休暇申請設定」を取得する
		Optional<HdAppSet> lstHdAppSet = repoHdAppSet.getAll();

		boolean displayPrePostFlg = this.isDisplayPrePostFlg(companyId);
		return new ApplicationsListOutput(listApprovalAppDetail, lstHdAppSet, listSync, displayPrePostFlg);
	}

	private GeneralDate findEndDateMax(List<ApprovalStatusEmployeeOutput> lstDate){
		GeneralDate endDateMax = lstDate.get(0).getEndDate();
		for (ApprovalStatusEmployeeOutput priod : lstDate) {
			if(endDateMax.before(priod.getEndDate())){
				endDateMax = priod.getEndDate();
			}
		}
		return endDateMax;
	}
	/**
	 * 承認状況申請内容追加
	 */
	private List<ApprovalSttAppDetail> getApprovalSttAppDetail(List<ApplicationApprContent> listAppContent, GeneralDate endDateMax) {
		List<ApprovalSttAppDetail> listApprovalSttAppDetail = new ArrayList<>();
		String companyId = AppContexts.user().companyId();
		for (ApplicationApprContent appContent : listAppContent) {
			Application_New app = appContent.getApplication();
			/// ドメインモデル「申請表示名」より申請表示名称を取得する
			Optional<AppDispName> appDispName = repoAppDispName.getDisplay(app.getAppType().value);
			// アルゴリズム「承認状況申請承認者取得」を実行する
			List<ApproverOutput> listApprover = this.getApprovalSttApprover(appContent);
			// アルゴリズム「承認状況申請内容取得実績」を実行する
			ApprovalSttDetailRecord approvalSttDetail = this.getApplicationDetailRecord(appContent);
			// アルゴリズム「承認状況申請内容取得休暇」を実行する
			String relationshipName = this.getApprovalSttDetailVacation(app);
			WkpHistImport wkp = wkpAdapter.findWkpBySid(app.getEmployeeID(), app.getAppDate());
			int detailSet = this.detailSet(companyId, wkp.getWorkplaceId(), app.getAppType().value, endDateMax);
			listApprovalSttAppDetail.add(new ApprovalSttAppDetail(appContent, appDispName.get(), listApprover,
					approvalSttDetail, relationshipName, detailSet));
		}
		return listApprovalSttAppDetail;
	}

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
	 * 「承認状況申請内容取得振休振出
	 */
	private List<AppCompltLeaveSync> getCompltLeaveSyncOutput(String companyId, List<Application_New> lstCompltLeave) {
		List<AppCompltLeaveSync> lstAppCompltLeaveSync = new ArrayList<>();
		List<String> lstSyncId = new ArrayList<>();
		for (Application_New app : lstCompltLeave) {
			if (lstSyncId.contains(app.getAppID())) {
				continue;
			}
			AppCompltLeaveFull appMain = null;
			AppCompltLeaveFull appSub = null;
			String appDateSub = null;
			String appInputSub = null;
			// アルゴリズム「申請一覧リスト取得振休振出」を実行する-(get List App Complement Leave): 6 -
			// 申請一覧リスト取得振休振出
			AppCompltLeaveSyncOutput sync = otherCommonAlgorithm.getAppComplementLeaveSync(companyId, app.getAppID());
			if (!sync.isSync()) {// TH k co don lien ket
				// lay thong tin chi tiet
				appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType());
			} else {// TH co don lien ket
					// lay thong tin chi tiet A
				appMain = repoAppDetail.getAppCompltLeaveInfo(companyId, app.getAppID(), sync.getType());
				// check B co trong list don xin k?
				String appIdSync = sync.getType() == 0 ? sync.getRecId() : sync.getAbsId();
				CheckExitSync checkExit = this.checkExitSync(lstCompltLeave, appIdSync);
				if (checkExit.isCheckExit()) {// exist
					lstSyncId.add(appIdSync);
					appDateSub = checkExit.getAppDateSub().toString("yyyy/MM/dd");
					appInputSub = checkExit.getInputDateSub().toString("yyyy/MM/dd HH:mm");
				} else {// not exist
						// lay thong tin chung
					Application_New sub = repoApp.findByID(companyId, appIdSync).get();
					appDateSub = sub.getAppDate().toString("yyyy/MM/dd");
					appInputSub = sub.getInputDate().toString("yyyy/MM/dd HH:mm");
				}
				appSub = repoAppDetail.getAppCompltLeaveInfo(companyId, appIdSync, sync.getType() == 0 ? 1 : 0);
			}

			lstAppCompltLeaveSync.add(
					new AppCompltLeaveSync(sync.getType(), sync.isSync(), appMain, appSub, appDateSub, appInputSub));
		}
		return lstAppCompltLeaveSync;
	}

	private CheckExitSync checkExitSync(List<Application_New> lstCompltLeave, String appId) {
		for (Application_New app : lstCompltLeave) {
			if (app.getAppID().equals(appId)) {
				return new CheckExitSync(true, app.getAppDate(), app.getInputDate());
			}
		}
		return new CheckExitSync(false, null, null);
	}

	/**
	 * 承認状況申請承認者取得
	 */
	private List<ApproverOutput> getApprovalSttApprover(ApplicationApprContent appContent) {
		List<ApproverOutput> listApprover = new ArrayList<>();
		List<ApprovalPhaseStateImport_New> listAppPhaseState = appContent.getApprRootContentExport()
				.getApprovalRootState().getListApprovalPhaseState();
		GeneralDate appDate = appContent.getApplication().getStartDate().get();
		// クラス：承認フェーズ
		for (ApprovalPhaseStateImport_New appPhase : listAppPhaseState) {
			List<ApprovalFrameImport_New> listApprovalFrame = appPhase.getListApprovalFrame();
			List<ApproverSpecial> listEmployeeSpecials = new ArrayList<>();
			String empName = "";
			int numOfPeople = 0;
			// クラス：承認枠
			for (ApprovalFrameImport_New appFrame : listApprovalFrame) {
				// アルゴリズム「承認状況未承認者取得代行優先」を実行する
				List<ApproverStateImport_New> listApproverState = appFrame.getListApprover();
				List<ApproverSpecial> listEmpSpecial = this.getUnAppSubstitutePriority(listApproverState, appDate,
						appFrame.getConfirmAtr());
				listEmployeeSpecials.addAll(listEmpSpecial);
			}

			listEmployeeSpecials = listEmployeeSpecials.stream()
					.sorted(Comparator.comparing(ApproverSpecial::getConfirmAtr).reversed())
					.collect(Collectors.toList());
			List<String> listEmployee = new ArrayList<>();
			for (ApproverSpecial appSpecial : listEmployeeSpecials) {
				listEmployee.add(appSpecial.getApproverId());
			}
			String epmIdSpecial = listEmployee.stream().findFirst().get();
			if (!listEmployee.isEmpty()) {
				// Imported（就業）「個人社員基本情報」を取得する
				// RequestList126
				List<EmployeeBasicInfoImport> listEmpInfor = this.workplaceAdapter.findBySIds(listEmployee);
				for (EmployeeBasicInfoImport empBase : listEmpInfor) {
					if (empBase.getEmployeeId().equals(epmIdSpecial)) {
						empName = empBase.getPName();
						break;
					}
				}
				numOfPeople = listEmployee.size() - 1;
				ApproverOutput approver = new ApproverOutput(appPhase.getPhaseOrder(), empName, numOfPeople);
				listApprover.add(approver);
			} else {
				ApproverOutput approver = new ApproverOutput(appPhase.getPhaseOrder(), null, null);
				listApprover.add(approver);
			}
		}
		return listApprover;
	}

	/**
	 * 承認状況未承認者取得代行優先
	 * 
	 * @param appDate
	 * @param confirmAtr
	 * @param listApprovalFrame
	 * 
	 */
	private List<ApproverSpecial> getUnAppSubstitutePriority(List<ApproverStateImport_New> listApproverState,
			GeneralDate appDate, int confirmAtr) {
		List<ApproverSpecial> listEmpId = new ArrayList<>();
		String cId = AppContexts.user().companyId();
		for (ApproverStateImport_New approver : listApproverState) {
			String sID = approver.getApproverID();
			// ドメインモデル「代行者管理」を取得する
			List<AgentDataRequestPubImport> lstAgentData = agentApdater.lstAgentBySidData(cId, sID, appDate, appDate);
			Optional<AgentDataRequestPubImport> agent = Optional.empty();
			if (lstAgentData != null && !lstAgentData.isEmpty()) {
				agent = lstAgentData.stream().findFirst();
			}
			// 対象が存在する場合
			if (agent.isPresent()) {
				switch (agent.get().getAgentAppType1()) {
				// 0:代行者指定
				case SUBSTITUTE_DESIGNATION:
					// 代行者管理.承認代行者を社員IDにセットする
					listEmpId.add(new ApproverSpecial(agent.get().getAgentSid1(), confirmAtr));
					break;
				// 1:パス
				case PATH:
					break;
				// 2:設定なし
				case NO_SETTINGS:
					// 承認者IDを社員IDにセットする
					listEmpId.add(new ApproverSpecial(approver.getApproverID(), confirmAtr));
					break;
				default:
					break;
				}
			} else {
				// 承認者IDを社員IDにセットする
				listEmpId.add(new ApproverSpecial(approver.getApproverID(), confirmAtr));
			}
		}
		return listEmpId;
	}

	/**
	 * 承認状況申請内容取得実績
	 */
	private ApprovalSttDetailRecord getApplicationDetailRecord(ApplicationApprContent appContent) {
		String cId = AppContexts.user().companyId();
		Application_New application = appContent.getApplication();
		// 打刻申請の場合
		if (!application.isAppStemApp()) {
			return null;
		}
		// ドメインモデル「打刻申請」を取得する
		AppStamp stamp = repoAppStamp.findByAppID(cId, application.getAppID());
		// 打刻取消の場合
		if (!StampRequestMode.STAMP_CANCEL.equals(stamp.getStampRequestMode())) {
			return null;
		}
		// アルゴリズム「実績の取得」を実行する
		AchievementOutput achievement = collectAchievement.getAchievement(cId, application.getAppID(),
				application.getAppDate());
		// アルゴリズム「勤務実績の取得」を実行する
		List<AttendanceResultImport> listAttendanceResult = this.getAttendanceResult(application);
		return new ApprovalSttDetailRecord(listAttendanceResult, achievement);
	}

	/**
	 * 勤務実績の取得
	 */
	List<AttendanceResultImport> getAttendanceResult(Application_New application) {
		int[] listKey = { 30, 40, 31, 41, 33, 43, 3451, 59, 67, 52, 60, 68, 53, 61, 69, 86, 91, 96, 101, 106, 111, 116,
				121, 126, 131, 87, 92, 65, 102, 107, 112, 117, 122, 127, 132, 88, 93, 67, 103, 108, 113, 118, 123, 128,
				133, 89, 94, 68, 104, 109, 114, 119, 124, 129, 134, 90, 95, 70, 105, 110, 115, 120, 125, 130, 135 };
		List<Integer> itemIds = new ArrayList<>();
		for (int x : listKey) {
			itemIds.add(x);
		}
		DatePeriod workingDate = new DatePeriod(application.getAppDate(), application.getAppDate());
		List<String> listEmps = new ArrayList<>();
		listEmps.add(application.getEmployeeID());
		// Imported（申請承認）勤怠項目実績を取得
		// RequestList6
		List<AttendanceResultImport> listAttendanceResult = dailyAttendanceItemAdapter.getValueOf(listEmps, workingDate,
				itemIds);
		return listAttendanceResult;
	}

	/**
	 * 承認状況申請内容取得休暇
	 */
	private String getApprovalSttDetailVacation(Application_New app) {
		String relaName = "";
		Optional<AppAbsence> absence = repoAbsence.getAbsenceById(app.getCompanyID(), app.getAppID());
		if (!absence.isPresent())
			return "";
		AppForSpecLeave appForSpec = absence.get().getAppForSpecLeave();
		String relaCode = appForSpec == null ? ""
				: appForSpec.getRelationshipCD() == null ? "" : appForSpec.getRelationshipCD().v();
		// 休暇申請以外の場合
		if (!app.isAppAbsence()) {
			return "";
		}
		// imported(就業.Shared)「続柄」を取得する
		relaName = relaCode.equals("") ? ""
				: repoRelationship.findByCode(app.getCompanyID(), relaCode).get().getRelationshipName().v();
		return relaName;
	}

	private List<ApplicationApprContent> sortById(List<ApplicationApprContent> lstApp) {

		return lstApp.stream().sorted((a, b) -> {
			Integer rs = a.getApplication().getAppDate().compareTo(b.getApplication().getAppDate());
			if (rs == 0) {
				return a.getApplication().getAppType().compareTo(b.getApplication().getAppType());
			} else {
				return rs;
			}
		}).collect(Collectors.toList());

	}

	private boolean isDisplayPrePostFlg(String companyID) {
		Optional<RequestSetting> requestSetting = this.requestSetRepo.findByCompany(companyID);
		if (requestSetting.isPresent()
				&& requestSetting.get().getApplicationSetting().getAppDisplaySetting().getPrePostAtr().value == 1)
			return true;
		return false;
	}
}
