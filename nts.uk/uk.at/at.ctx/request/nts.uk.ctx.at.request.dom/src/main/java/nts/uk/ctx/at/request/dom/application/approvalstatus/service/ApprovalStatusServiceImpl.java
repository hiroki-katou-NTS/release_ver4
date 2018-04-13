package nts.uk.ctx.at.request.dom.application.approvalstatus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.mail.send.MailContents;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.approvalstatus.ApprovalStatusMailTemp;
import nts.uk.ctx.at.request.dom.application.approvalstatus.ApprovalStatusMailTempRepository;
import nts.uk.ctx.at.request.dom.application.approvalstatus.ApprovalStatusMailType;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApplicationApprContent;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalStatusEmployeeOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttAppOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.DailyStatus;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.EmbeddedUrlOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.EmployeeEmailOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.EmploymentOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.MailTransmissionContentOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.MailTransmissionContentResultOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.PeriodOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.SendMailResultOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.UnApprovalPerson;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.UnApprovalPersonAndResult;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.WorkplaceInfor;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.EmployeeEmailImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.AgentAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalFrameImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.EmployeeBasicInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.mail.SendMailFailedException;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

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
	@Override
	public List<ApprovalStatusEmployeeOutput> getApprovalStatusEmployee(String wkpId, GeneralDate closureStart,
			GeneralDate closureEnd, List<String> listEmpCd) {

		List<ApprovalStatusEmployeeOutput> listSttEmp = new ArrayList<>();
		// imported(申請承認)「社員ID（リスト）」を取得する
		// TODO Requestlist 120-1
		// Waiting for Q&A
		List<ApprovalStatusEmployeeOutput> listEmpInOut = new ArrayList<>();
		// TODO List<String> listSId =
		// employeeRequestAdapter.getListSIdByWkpIdAndPeriod(wkpId,
		// closureStart, closureEnd);
		// 社員ID(リスト)
		for (ApprovalStatusEmployeeOutput empInOut : listEmpInOut) {
			// imported(就業)「所属雇用履歴」より雇用コードを取得する
			DatePeriod datePeriod = new DatePeriod(empInOut.getStartDate(), empInOut.getEndDate());
			// TODO 264
			// List<EmploymentHisImport> listEmpHist =
			// atEmpAdapter.findByListSidAndPeriod(empInOut.getSId(),
			// datePeriod);
			List<EmploymentOutput> listEmpHist = new ArrayList<>();
			// 雇用（リスト）
			for (EmploymentOutput empHist : listEmpHist) {
				// 存在しない場合
				if (listEmpCd.contains(empHist.getEmpCd())) {
					continue;
				}
				// 存在する場合
				// アルゴリズム「承認状況対象期間取得」を実行する
				PeriodOutput sttPeriod = this.getApprovalSttPeriod(empInOut.getSId(), empHist.getStartDate(),
						empHist.getEndDate(), closureStart, closureEnd, empInOut.getStartDate(), empInOut.getEndDate());
				listSttEmp.add(new ApprovalStatusEmployeeOutput(empInOut.getSId(), sttPeriod.getStartDate(),
						sttPeriod.getEndDate()));
			}
		}
		return listSttEmp;
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
		ApprovalSttAppOutput approvalSttApp = null;
		for (ApprovalStatusEmployeeOutput approvalStt : listAppStatusEmp) {
			List<ApplicationApprContent> getAppSttAcquisitionAppl = this.getAppSttAcquisitionAppl(approvalStt);
			appSttAppliStateList.add(this.getCountAppSttAppliState(wkpInfor, getAppSttAcquisitionAppl));
		}

		if (appSttAppliStateList.isEmpty())
			return null;
		int numOfApp = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getNumOfApp).sum();
		int appNumOfCase = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getApprovedNumOfCase).sum();
		int numOfUnreflected = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getNumOfUnreflected).sum();
		int numOfUnapproval = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getNumOfUnapproval).sum();
		int numOfDenials = appSttAppliStateList.stream().mapToInt(ApprovalSttAppOutput::getNumOfDenials).sum();
		Integer numOfAppDisp = numOfApp == 0 ? null : numOfApp;
		Integer appNumOfCaseDisp = appNumOfCase == 0 ? null : appNumOfCase;
		Integer numOfUnreflectedDisp = numOfUnreflected == 0 ? null : numOfUnreflected;
		Integer numOfUnapprovalDisp = numOfUnapproval == 0 ? null : numOfUnapproval;
		Integer numOfDenialsDisp = numOfDenials == 0 ? null : numOfDenials;
		boolean isEnable = true;
		if (Objects.isNull(numOfUnapprovalDisp)) {
			isEnable = false;
		}
		approvalSttApp = new ApprovalSttAppOutput(wkpInfor.getCode(), wkpInfor.getName(), isEnable, false, numOfAppDisp,
				appNumOfCaseDisp, numOfUnreflectedDisp, numOfUnapprovalDisp, numOfDenialsDisp);
		return approvalSttApp;
	}

	/**
	 * アルゴリズム「承認状況取得申請」を実行する
	 */
	private List<ApplicationApprContent> getAppSttAcquisitionAppl(ApprovalStatusEmployeeOutput approvalStt) {
		List<ApplicationApprContent> listAppSttAcquisitionAppl = new ArrayList<>();
		String sId = AppContexts.user().companyId();
		String empId = approvalStt.getSId();
		GeneralDate startDate = approvalStt.getStartDate();
		GeneralDate endDate = approvalStt.getEndDate();
		List<Application_New> listApp = appRepoNew.getListAppById(sId, empId, startDate, endDate);
		for (Application_New app : listApp) {
			// 申請承認内容(リスト）
			ApprovalRootContentImport_New approvalRoot = this.approvalStateAdapter.getApprovalRootContent(sId,
					app.getEmployeeID(), app.getAppType().value, app.getAppDate(), app.getAppID(), false);
			listAppSttAcquisitionAppl.add(new ApplicationApprContent(app, approvalRoot));
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
		List<Application_New> listApp_New = new ArrayList<>();
		for (ApplicationApprContent appContent : listAppContent) {
			Application_New app = appContent.getApplication();
			listApp_New.add(app);
		}
		for (Application_New app : listApp_New) {
			// アルゴリズム「承認状況申請内容取得出張」を実行する
			int valueState = app.getReflectionInformation().getStateReflectionReal().value;
			if (valueState != 3 || valueState != 4) {
				numOfApp++;
				if (valueState == 0 || valueState == 5) {
					numOfUnapproval++;
					numOfUnreflected++;
				} else if (valueState == 1) {
					approvedNumOfCase++;
					numOfUnreflected++;
				} else if (valueState == 6) {
					numOfDenials++;
				} else if (valueState == 2) {
					numOfApp++;
				}
			}
		}
		return new ApprovalSttAppOutput(wkpInfor.getCode(), wkpInfor.getName(), false, false, numOfApp,
				approvedNumOfCase, numOfUnreflected, numOfUnapproval, numOfDenials);
	}

	/**
	 * 
	 * @param listAppSttDadta
	 * @return
	 */
	/*
	 * public List<ApprovalSttAppOutput>
	 * getConfirmStatus(List<ApprovalSttAppData> listAppSttDadta) {
	 * List<ApprovalSttAppOutput> listAppDto = new ArrayList<>();
	 * for(ApprovalSttAppData appData: listAppSttDadta) { ApprovalSttAppOutput
	 * appSttDto = new ApprovalSttAppOutput(appData.getWorkplaceId(),
	 * appData.isEnabled(), appData.isChecked() ,appData.getNumOfApp(),
	 * appData.getApprovedNumOfCase(), appData.getNumOfUnreflected(),
	 * appData.getNumOfUnapproval(), appData.getNumOfDenials());
	 * listAppDto.add(appSttDto); } return listAppDto; }
	 */

	/**
	 * 承認状況社員メールアドレス取得
	 * 
	 * @return ・取得社員ID（リスト）＜社員ID、社員名、メールアドレス、期間＞
	 */
	@Override
	public List<EmployeeEmailImport> findEmpMailAddr(List<String> listsId) {
		List<EmployeeEmailImport> listEmployee = employeeRequestAdapter.getApprovalStatusEmpMailAddr(listsId);
		// TODO 225
		// Imported（申請承認）「社員メールアドレス」を取得する

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
		// ドメインモデル「承認状況メールテンプレート」を取得する
		ApprovalStatusMailTemp domain = approvalStatusMailTempRepo.getApprovalStatusMailTempById(cid, mailType).get();
		// 社員ID
		String sid = AppContexts.user().employeeId();
		// 社員名
		String sName = employeeRequestAdapter.getEmployeeName(sid);
		// メールアドレス
		String mailAddr = employeeRequestAdapter.empEmail(sid);
		// 件名
		String subject = domain.getMailSubject().v();
		// 送信本文
		String text = domain.getMailContent().v();

		// ログイン者よりメール送信内容を作成する(create nội dung send mail theo người login)
		List<MailTransmissionContentOutput> listMailContent = new ArrayList<MailTransmissionContentOutput>();
		listMailContent.add(new MailTransmissionContentOutput(sid, sName, mailAddr, subject, text));
		// UseSetingDto transmissionAttr = this.getUseSeting();
		// アルゴリズム「承認状況メール送信実行」を実行する
		return this.exeApprovalStatusMailTransmission(listMailContent, domain);
	}

	@Override
	public SendMailResultOutput exeApprovalStatusMailTransmission(List<MailTransmissionContentOutput> listMailContent,
			ApprovalStatusMailTemp domain) {
		List<String> listError = new ArrayList<>();
		for (MailTransmissionContentOutput mailTransmission : listMailContent) {
			// アルゴリズム「承認状況メール埋込URL取得」を実行する
			EmbeddedUrlOutput embeddedURL = this.getEmbeddedURL(mailTransmission.getSId(), domain);
			try {
				// アルゴリズム「メールを送信する」を実行する
				mailsender.send("nts", mailTransmission.getMailAddr(),
						new MailContents(mailTransmission.getSubject(), mailTransmission.getText()));
			} catch (SendMailFailedException e) {
				// 送信エラー社員(リスト)と社員名、エラー内容を追加する
				listError.add(e.getMessage());
			}
		}
		SendMailResultOutput result = new SendMailResultOutput();
		if (listError.size() == 0) {
			result.setOK(false);
			result.setListError(listError);
		}
		result.setOK(true);
		return result;
	}

	/**
	 * アルゴリズム「承認状況メール埋込URL取得」を実行する
	 * 
	 * @param eid
	 * @param domain
	 * @param transmissionAttr
	 */
	private EmbeddedUrlOutput getEmbeddedURL(String eid, ApprovalStatusMailTemp domain) {
		// TODO waiting for Hiệp
		String url1 = "123123123";
		String url2 = "1231ád23123";
		return new EmbeddedUrlOutput(url1, url2);
	}

	@Override
	public String confirmApprovalStatusMailSender() {
		String sId = AppContexts.user().userId();
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
	public void exeSendUnconfirmedMail(List<String> listWkpId, GeneralDate closureStart, GeneralDate closureEnd,
			List<String> listEmpCd) {
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
		this.exeApprovalStatusMailTransmission(getMailTransmissContent.getListMailTransmisContent(),
				getMailTransmissContent.getMailDomain());
	}

	/**
	 * 承認状況未承認申請取得
	 */
	private List<UnApprovalPerson> getUnapprovalForAppStt(List<ApprovalStatusEmployeeOutput> listEmpOutput) {
		List<UnApprovalPerson> listUnAppPerson = new ArrayList<>();
		for (ApprovalStatusEmployeeOutput appEmp : listEmpOutput) {
			// アルゴリズム「承認状況取得申請」を実行する
			List<ApplicationApprContent> listAppContent = this.getAppSttAcquisitionAppl(appEmp);
			GeneralDate startDate = appEmp.getStartDate();
			GeneralDate endDate = appEmp.getEndDate();
			for (ApplicationApprContent app : listAppContent) {
				if (app.getApplication().getReflectionInformation().getStateReflectionReal().value != 0) {
					continue;
				} else {
					GeneralDate appDate = app.getApplication().getEndDate().get();
					// アルゴリズム「承認状況未承認メール対象者取得」を実行する
					List<String> listUnAppEmpIds = this.getUnApprovalMailTarget(app.getApprRootContentExport(),
							appDate);
					listUnAppEmpIds.stream().forEach(item -> {
						listUnAppPerson.add(new UnApprovalPerson(item, startDate, endDate));
					});
				}
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
			if (appPhase.getApprovalAtr().value == 1 || appPhase.getApprovalAtr().value == 2) {
				continue;
			} else {
				List<ApprovalFrameImport_New> listAppFrame = appPhase.getListApprovalFrame();
				// クラス：承認枠
				for (ApprovalFrameImport_New appFrame : listAppFrame) {
					// 承認済、否認の場合
					if (appFrame.getApprovalAtr().value == 1 || appFrame.getApprovalAtr().value == 2) {
						continue;
					} else {
						// 未承認、差し戻しの場合
						// アルゴリズム「承認状況未承認メール未承認者取得」を実行する
						getUnAppPersonAndResult = this.getUnApprovalMailPerson(listAppFrame, appDate);
						if (!getUnAppPersonAndResult.isResult()) {
							result = true;
						} else {
							continue;
						}
					}
				}
				// 次の承認枠が存在しない場合
				listUnAppPerson = getUnAppPersonAndResult.getListUnAppPerson();
				if (result)
					return listUnAppPerson;
			}
		}
		return null;
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
			List<ApproverStateImport_New> listAppState = appFrame.getListApprover();
			for (ApproverStateImport_New appState : listAppState) {
				listApprovalEmpId.add(appState.getApproverID());
			}
		}
		// imported（申請承認）「代行者」を取得する
		// RequestList310
		List<String> listUnAppPersonEmp = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			List<AgentInfoImport> listAgentInfor = agentApdater.findAgentByPeriod(companyID, listApprovalEmpId, appDate,
					appDate, i);
			for (AgentInfoImport agent : listAgentInfor) {
				listUnAppPersonEmp.add(agent.getAgentID());
			}
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
		MailTransmissionContentResultOutput mailTransContentResult = null;
		List<MailTransmissionContentOutput> listMailTransmissContent = new ArrayList<>();
		// アルゴリズム「承認状況メール本文取得」を実行する
		ApprovalStatusMailTemp mailDomain = this
				.getApprovalStatusMailTemp(ApprovalStatusMailType.APP_APPROVAL_UNAPPROVED.value);
		// 未承認者を社員ID順に並び替える
		// 未承認者（リスト）
		List<String> listEmpId = new ArrayList<>();
		for (UnApprovalPerson unAppPerson : listUnAppPerson) {
			listEmpId.add(unAppPerson.getSId());
		}
		// 次の未承認者の社員IDが異なる(EmployeeID chưa approval tiếp theo có khác không)
		// アルゴリズム「承認状況社員メールアドレス取得」を実行する
		// imported（就業）「個人社員基本情報」を取得する
		// Cho confirm request225
		// TODO
		List<EmployeeEmailImport> listEmailEmployee = this.findEmpMailAddr(listEmpId);
		for (EmployeeEmailImport emp : listEmailEmployee) {
			// 件名
			String subject = mailDomain.getMailSubject().v();
			// 送信本文
			String text = mailDomain.getMailContent().v();
			listMailTransmissContent.add(
					new MailTransmissionContentOutput(emp.getSId(), emp.getSName(), emp.getMailAddr(), subject, text));
		}
		mailTransContentResult = new MailTransmissionContentResultOutput(listMailTransmissContent, mailDomain);
		return mailTransContentResult;
	}

	/**
	 * アルゴリズム「承認状況未承認メール送信」を実行する
	 */
	@Override
	public List<String> getAppSttSendingUnapprovedMail(List<ApprovalSttAppOutput> listAppSttApp) {
		List<String> listWorksp = new ArrayList<>();
		this.confirmApprovalStatusMailSender();
		// 職場一覧のメール送信欄のチェックがONの件数
		if (listAppSttApp.stream().filter(x -> x.isChecked()).count() == 0) {
			throw new BusinessException("Msg_794");
		}
		return listWorksp;
	}

	@Override
	public List<DailyStatus> getApprovalSttById(String selectedWkpId, List<String> listWkpId, GeneralDate startDate,
			GeneralDate endDate, List<String> listEmpCode) {
		List<DailyStatus> listDailyStatus = new ArrayList<>();
		// アルゴリズム「承認状況取得社員」を実行する
		List<ApprovalStatusEmployeeOutput> listAppSttEmp = this.getApprovalStatusEmployee(selectedWkpId, startDate,
				endDate, listEmpCode);
		// 社員ID(リスト)
		for (ApprovalStatusEmployeeOutput appStt : listAppSttEmp) {
			List<String> listEmpId = new ArrayList<>();
			listEmpId.add(appStt.getSId());
			// Imported（就業）「個人社員基本情報」を取得する
			// RequestList126
			List<EmployeeBasicInfoImport> listEmpInfor = this.workplaceAdapter.findBySIds(listEmpId);
			String empName = listEmpInfor.stream().findFirst().get().getPName();
			String empId = listEmpInfor.stream().findFirst().get().getEmployeeId();
			// アルゴリズム「承認状況取得申請」を実行する
			List<ApplicationApprContent> listAppSttAcquisitionAppl = this.getAppSttAcquisitionAppl(appStt);
			List<Application_New> listApp = new ArrayList<>();
			listAppSttAcquisitionAppl.stream().forEach(item -> listApp.add(item.getApplication()));
			// アルゴリズム「承認状況日別状態作成」を実行する
			DailyStatus dailyStatus = this.getApprovalSttByDate(appStt, listApp);
			if (dailyStatus.getEmpId().equals(empId)) {
				dailyStatus.setEmpName(empName);
			}
			listDailyStatus.add(dailyStatus);
		}
		return listDailyStatus;
	}

	/**
	 * 承認状況日別状態作成
	 */
	private DailyStatus getApprovalSttByDate(ApprovalStatusEmployeeOutput appStt, List<Application_New> listApp) {
		DailyStatus dailyStatus = null;
		for (Application_New app : listApp) {
			int value = app.getReflectionInformation().getStateReflectionReal().value;
			if (value == 2) {
				dailyStatus.setStateSymbol(0);
			} else if (value == 1) {
				dailyStatus.setStateSymbol(1);
			} else if (value == 6) {
				dailyStatus.setStateSymbol(2);
			} else if (value == 0 || value == 5) {
				dailyStatus.setStateSymbol(3);
			} else {
				continue;
			}

			GeneralDate dateTemp;
			// 申請開始日が期間内に存在する
			if (app.getStartDate().get().after(appStt.getStartDate())
					&& app.getStartDate().get().before(appStt.getEndDate())) {
				// 対象日付を申請開始日とする
				dateTemp = app.getStartDate().get();
			} else {
				// 対象日付を期間の開始日とする
				dateTemp = appStt.getStartDate();
			}
			// 日別状態(リスト)に社員ID＝社員ID、日付＝対象日付が存在する
			// TODO
		}
		return dailyStatus;
	}
}
