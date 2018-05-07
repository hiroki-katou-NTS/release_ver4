package nts.uk.ctx.at.record.dom.application.realitystatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.record.dom.adapter.request.application.ApprovalStatusRequestAdapter;
import nts.uk.ctx.at.record.dom.adapter.request.application.dto.ApprovalStatusMailTempImport;
import nts.uk.ctx.at.record.dom.adapter.request.application.dto.EmployeeUnconfirmImport;
import nts.uk.ctx.at.record.dom.adapter.request.application.dto.RealityStatusEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.request.application.dto.SendMailResultImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.application.realitystatus.enums.ApprovalStatusMailType;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.DailyConfirmOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.EmpDailyConfirmOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.EmpPerformanceOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.EmpUnconfirmResultOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.ErrorStatusOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.MailTranmissionContentResultOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.MailTransmissionContentOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.StatusWkpActivityOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.SumCountOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.UseSetingOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.WkpIdMailCheckOutput;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class RealityStatusService {
	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	@Inject
	private ApprovalStatusRequestAdapter approvalStatusRequestAdapter;

	@Inject
	IdentityProcessUseSetRepository identityProcessUseSetRepo;

	@Inject
	ApprovalProcessingUseSettingRepository approvalProcessingUseSettingRepo;

	@Inject
	private EmployeeRecordAdapter employeeRecordAdapter;

	@Inject
	IdentificationRepository identificationRepo;
	
	/**
	 * 承認状況職場実績起動
	 */
	public List<StatusWkpActivityOutput> getStatusWkpActivity(List<String> listWorkplaceId, GeneralDate startDate,
			GeneralDate endDate, List<String> listEmpCd, boolean isConfirmData) {
		String cId = AppContexts.user().companyId();
		List<StatusWkpActivityOutput> listStatusActivity = new ArrayList<StatusWkpActivityOutput>();
		// アルゴリズム「承認状況取得実績使用設定」を実行する
		UseSetingOutput useSeting = this.getUseSetting(cId);
		// 職場ID(リスト)
		for (String wkpId : listWorkplaceId) {
			// アルゴリズム「承認状況取得社員」を実行する
			List<RealityStatusEmployeeImport> listStatusEmp = approvalStatusRequestAdapter
					.getApprovalStatusEmployee(wkpId, startDate, endDate, listEmpCd);
			// アルゴリズム「承認状況取得職場実績確認」を実行する
			SumCountOutput count = this.getApprovalSttConfirmWkpResults(listStatusEmp, wkpId, useSeting);
			// 保持内容．未確認データ確認
			if (isConfirmData) {
				// 職場本人未確認件数及び職場上司未確認件数、月別未確認件数がすべて「０件」の場合
				if (count.personUnconfirm == 0 && count.bossUnconfirm == 0 && count.monthUnconfirm == 0) {
					continue;
				}
			}
			listStatusActivity.add(new StatusWkpActivityOutput(wkpId, count.monthConfirm, count.monthUnconfirm,
					count.personConfirm, count.personUnconfirm, count.bossConfirm, count.bossUnconfirm));
		}
		return listStatusActivity;
	}

	/**
	 * 承認状況未確認メール送信
	 */
	public String checkSendUnconfirmedMail(List<WkpIdMailCheckOutput> listWkp) {
		// アルゴリズム「承認状況送信者メール確認」を実行する
		String email = approvalStatusRequestAdapter.confirmApprovalStatusMailSender();
		// アルゴリズム「承認状況未確認メール送信実行チェック」を実行する
		if (listWkp.stream().filter(x -> x.isCheckOn()).count() == 0) {
			throw new BusinessException("Msg_794");
		}
		return email;
	}

	/**
	 * 承認状況取得実績使用設定
	 */
	public UseSetingOutput getUseSetting(String cid) {
		// 承認処理の利用設定
		Optional<ApprovalProcessingUseSetting> approval = approvalProcessingUseSettingRepo.findByCompanyId(cid);
		// 本人確認処理の利用設定
		Optional<IdentityProcessUseSet> identity = identityProcessUseSetRepo.findByKey(cid);

		// 月別確認を利用する ← 承認処理の利用設定.月の承認者確認を利用する
		boolean monthlyConfirm = approval.isPresent() ? approval.get().getUseMonthApproverConfirm() : false;
		// 上司確認を利用する ← 承認処理の利用設定.日の承認者確認を利用する
		boolean useBossConfirm = approval.isPresent() ? approval.get().getUseDayApproverConfirm() : false;
		// 本人確認を利用する ← 本人確認処理の利用設定.日の本人確認を利用する
		boolean usePersonConfirm = identity.isPresent() ? identity.get().isUseConfirmByYourself() : false;
		return new UseSetingOutput(monthlyConfirm, useBossConfirm, usePersonConfirm);
	}

	/**
	 * 承認状況取得職場実績確認
	 * 
	 * @param listEmp
	 *            社員ID＜社員ID、期間＞(リスト)
	 * @param wkpId
	 *            職場ID
	 * @param useSeting
	 *            月別確認を利用する/上司確認を利用する/本人確認を利用する
	 * @return
	 */
	private SumCountOutput getApprovalSttConfirmWkpResults(List<RealityStatusEmployeeImport> listEmp, String wkpId,
			UseSetingOutput useSeting) {
		SumCountOutput sumCount = new SumCountOutput();
		// 会社ID
		String cid = AppContexts.user().companyId();

		// 社員ID(リスト)
		for (RealityStatusEmployeeImport emp : listEmp) {
			// 月別確認を利用する
			if (useSeting.isMonthlyConfirm()) {
				// Request list 113
				// imported(申請承認）「上司確認の状況」を取得する
				List<ApproveRootStatusForEmpImport> listApproval = approvalStatusAdapter
						.getApprovalByEmplAndDate(emp.getStartDate(), emp.getEndDate(), emp.getSId(), cid, 2);
				Optional<ApproveRootStatusForEmpImport> approval = listApproval.stream().findFirst();
				// 承認状況＝「承認済」の場合(trạng thái approval = 「承認済」)
				if (approval.isPresent()
						&& ApprovalStatusForEmployee.APPROVED.equals(approval.get().getApprovalStatus())) {
					// 月別確認件数 ＝ ＋１
					sumCount.monthConfirm++;
				}
				// 承認状況＝「未承認 」又は「承認中」の場合
				else {
					// 月別未確認件数 ＝ ＋１
					sumCount.monthUnconfirm++;
				}
			}

			// アルゴリズム「承認状況取得日別確認状況」を実行する
			EmpDailyConfirmOutput result = this.getApprovalSttByDate(emp.getSId(), wkpId, emp.getStartDate(),
					emp.getEndDate(), useSeting.isUseBossConfirm(), useSeting.isUsePersonConfirm());
			// 各件数を合算する
			sumCount.personConfirm += result.getSumCount().personConfirm;
			sumCount.personUnconfirm += result.getSumCount().personUnconfirm;
			sumCount.bossConfirm += result.getSumCount().bossConfirm;
			sumCount.bossUnconfirm += result.getSumCount().bossUnconfirm;
		}
		return sumCount;
	}

	/**
	 * 承認状況取得日別確認状況
	 * 
	 * @param sId
	 *            社員ID
	 * @param wkpId
	 *            職場ID
	 * @param period
	 *            期間(開始日～終了日)
	 * @param useBossConfirm
	 *            上司確認を利用する
	 * @param usePersonConfirm
	 *            本人確認を利用する
	 * @param sumCount
	 *            output
	 */
	private EmpDailyConfirmOutput getApprovalSttByDate(String sId, String wkpId, GeneralDate startDate,
			GeneralDate endDate, boolean useBossConfirm, boolean usePersonConfirm) {
		// 期間範囲分の日別確認（リスト）を作成する(Tạo list confirm hàng ngày)
		List<DailyConfirmOutput> listDailyConfirm = new ArrayList<DailyConfirmOutput>();
		GeneralDate currentDate = startDate;
		while (currentDate.beforeOrEquals(endDate)) {
			listDailyConfirm.add(new DailyConfirmOutput(wkpId, sId, currentDate, false, false));
			currentDate = currentDate.addDays(1);
		}
		SumCountOutput sumCount = new SumCountOutput();

		// 利用するの場合(use)
		if (useBossConfirm) {
			// アルゴリズム「承認状況取得日別上司承認状況」を実行する
			this.getApprovalSttByDateOfBoss(sId, wkpId, startDate, endDate, listDailyConfirm, sumCount);
		}
		// 利用しないの場合

		// 利用するの場合(use)
		if (usePersonConfirm) {
			// アルゴリズム「承認状況取得日別本人確認状況」を実行する
			this.getApprovalSttByDateOfPerson(sId, wkpId, startDate, endDate, listDailyConfirm, sumCount);
		}
		// 利用しないの場合

		return new EmpDailyConfirmOutput(listDailyConfirm, sumCount);
	}

	/**
	 * 承認状況取得日別上司承認状況
	 * 
	 * @param sId
	 *            社員ID
	 * @param wkpId
	 *            職場ID
	 * @param period
	 *            期間(開始日～終了日)
	 * @param listDailyConfirm
	 *            output 日別確認（リスト）＜職場ID、社員ID、対象日、本人確認、上司確認＞
	 * @param sumCount
	 *            output 上司確認件数,上司未確認件数
	 */
	private void getApprovalSttByDateOfBoss(String sId, String wkpId, GeneralDate startDate, GeneralDate endDate,
			List<DailyConfirmOutput> listDailyConfirm, SumCountOutput sumCount) {
		// 会社ID
		String cid = AppContexts.user().companyId();
		// imported（ワークフロー）「承認ルート状況」を取得する
		List<ApproveRootStatusForEmpImport> listApproval = approvalStatusAdapter.getApprovalByEmplAndDate(startDate,
				endDate, sId, cid, 1);
		// 承認ルートの状況
		for (ApproveRootStatusForEmpImport approval : listApproval) {
			// 承認ルート状況.承認状況
			if (ApprovalStatusForEmployee.APPROVED.equals(approval.getApprovalStatus())) {
				// 「承認済」の場合
				// 上司確認件数 ＝＋１
				sumCount.bossConfirm++;
			} else {
				// 「未承認」又は「承認中」の場合
				// 上司未確認件数 ＝＋１
				sumCount.bossUnconfirm++;
			}
		}
	}

	/**
	 * 承認状況取得日別本人確認状況
	 * 
	 * @param sId
	 *            社員ID
	 * @param wkpId
	 *            職場ID
	 * @param period
	 *            期間(開始日～終了日)
	 * @param listDailyConfirm
	 *            output 日別確認（リスト）＜職場ID、社員ID、対象日、本人確認、上司確認＞
	 * @param sumCount
	 *            output
	 */
	private void getApprovalSttByDateOfPerson(String sId, String wkpId, GeneralDate startDate, GeneralDate endDate,
			List<DailyConfirmOutput> listDailyConfirm, SumCountOutput sumCount) {
		// RequestList165
		// imported（KAF018承認状況の照会）本人確認済みの日付を取得
		List<Identification> listIdentification = identificationRepo.findByEmployeeIDSortDate(sId, startDate, endDate);
		// 確認日付
		for (Identification identification : listIdentification) {
			// 日別確認（リスト）に同じ職場ID、社員ID、対象日が登録済
			Optional<DailyConfirmOutput> confirm = listDailyConfirm.stream().filter(x -> x.getWkpId().equals(wkpId)
					&& x.getSId().equals(sId) && x.getTargetDate().equals(identification.getProcessingYmd()))
					.findFirst();
			if (confirm.isPresent()) {
				// 対象の日別確認（リスト）の行を対象として内容を更新するする
				confirm.get().setPersonConfirm(true);
			}
			// 本人確認件数 ＝＋１
			sumCount.personConfirm++;
		}
		// 本人未確認件数 ＝日別確認(リスト)で上司確認＝未確認、本人確認＝未確認の件数
		sumCount.personUnconfirm = (int) listDailyConfirm.stream()
				.filter(x -> !x.isBossConfirm() && !x.isPersonConfirm()).count();
	}

	/**
	 * 承認状況未確認メール送信実行
	 */
	public SendMailResultImport exeSendUnconfirmMail(ApprovalStatusMailType type, List<WkpIdMailCheckOutput> listWkp,
			GeneralDate startDate, GeneralDate endDate, List<String> listEmpCd) {
		// アルゴリズム「承認状況未確認メール送信社員取得」を実行する
		EmpUnconfirmResultOutput result = this.getListEmpUnconfirm(type, listWkp, startDate, endDate, listEmpCd);
		if (!result.isOK()) {
			// メッセージ（Msg_787）を表示する
			throw new BusinessException("Msg_787");
		}
		// アルゴリズム「承認状況未確認メール本文取得」を実行する
		MailTranmissionContentResultOutput mailResult = this.getUnconfirmEmailContent(result.getListEmpUmconfirm(),
				type);
		// アルゴリズム「承認状況メール送信実行」を実行する
		return approvalStatusRequestAdapter.exeApprovalStatusMailTransmission(mailResult.getListMail(),
				mailResult.getApprovalStatusMailTemp(), type.value);
	}

	/**
	 * 承認状況未確認メール送信社員取得
	 * 
	 * @param type
	 *            送信区分（本人/日次/月次）
	 * @param listWkp
	 * @return
	 */
	private EmpUnconfirmResultOutput getListEmpUnconfirm(ApprovalStatusMailType type,
			List<WkpIdMailCheckOutput> listWkpId, GeneralDate closureStart, GeneralDate closureEnd,
			List<String> listEmpCd) {
		List<String> listSId = new ArrayList<>();
		// 職場一覧
		for (WkpIdMailCheckOutput wkp : listWkpId) {
			// メール送信のチェック
			if (!wkp.isCheckOn()) {
				continue;
			}
			// アルゴリズム「承認状況取得職場社員」を実行する
			List<RealityStatusEmployeeImport> listEmp = approvalStatusRequestAdapter
					.getApprovalStatusEmployee(wkp.getWkpId(), closureStart, closureEnd, listEmpCd);
			switch (type) {
			case PERSON:
				// アルゴリズム「承認状況未確認メール送信本人取得」を実行する
				listSId = this.getEmpUnconfirmByPerson(listEmp, wkp.getWkpId());
				break;
			case DAILY:
				// アルゴリズム「承認状況未確認メール送信上司取得」を実行する
				listSId = this.getEmpUnconfirmByBoss(listEmp, wkp.getWkpId());
				break;
			case MONTHLY:
				// アルゴリズム「承認状況未確認メール送信月次確認者取得」を実行する
				listSId = this.getEmpUnconfirmByMonthly(listEmp, wkp.getWkpId());
				break;
			}
		}
		// 未確認者のリストが存在する
		if (listSId.size() == 0) {
			// 存在しない場合
			return new EmpUnconfirmResultOutput(false, null);
		}
		// 存在する場合(Có tồn tại)
		// アルゴリズム「承認状況社員メールアドレス取得」を実行する
		List<EmployeeUnconfirmImport> listEmpUnconfirm = approvalStatusRequestAdapter.getListEmployeeEmail(listSId);

		return new EmpUnconfirmResultOutput(true, listEmpUnconfirm);
	}

	/**
	 * 承認状況未確認メール送信本人取得
	 * 
	 * @param listEmp
	 *            社員ID＜社員ID、期間＞(リスト)
	 * @param wkpId
	 *            職場ID
	 * @return 未確認者＜社員ID、期間＞（リスト）
	 */
	private List<String> getEmpUnconfirmByPerson(List<RealityStatusEmployeeImport> listEmp, String wkpId) {
		List<String> listEmpUnconfirm = new ArrayList<>();
		// 社員ID（リスト）
		for (RealityStatusEmployeeImport emp : listEmp) {
			// アルゴリズム「承認状況未確認チェック」を実行する
			if (this.checkUnconfirm(emp.getSId(), wkpId, emp.getStartDate(), emp.getEndDate())) {
				// 未確認者（リスト）に社員IDをセット
				listEmpUnconfirm.add(emp.getSId());
			}
		}
		return listEmpUnconfirm;
	}

	/**
	 * 承認状況未確認チェック
	 * 
	 * @param sId
	 *            社員ID
	 * @param wkpId
	 *            職場ID
	 * @param startDate
	 *            社員ID.期間(開始日)
	 * @param endDate
	 *            社員ID.期間(終了日)
	 * @return 結果（あり/なし)
	 */
	private boolean checkUnconfirm(String sId, String wkpId, GeneralDate startDate, GeneralDate endDate) {
		String cId = AppContexts.user().companyId();
		// アルゴリズム「承認状況取得実績使用設定」を実行する
		UseSetingOutput useSetting = this.getUseSetting(cId);
		// アルゴリズム「承認状況取得日別確認状況」を実行する
		EmpDailyConfirmOutput result = this.getApprovalSttByDate(sId, wkpId, startDate, endDate,
				useSetting.isUseBossConfirm(), useSetting.isUsePersonConfirm());

		return result.getSumCount().personUnconfirm != 0;
	}

	/**
	 * 承認状況未確認メール送信上司取得
	 * 
	 * @param listEmp
	 *            社員ID＜社員ID、期間＞(リスト)
	 * @param wkpId
	 *            職場ID
	 * @return 未確認者＜社員ID、期間＞（リスト）
	 */
	private List<String> getEmpUnconfirmByBoss(List<RealityStatusEmployeeImport> listEmp, String wkpId) {
		List<String> listEmpUnconfirm = new ArrayList<>();
		// 社員ID（リスト）
		for (RealityStatusEmployeeImport emp : listEmp) {
			// アルゴリズム「承認状況未確認チェック上司」を実行する
			if (this.checkUnconfirmBoss(emp.getSId(), wkpId, emp.getStartDate(), emp.getEndDate())) {
				// 上司社員ID（リスト）を未確認者（リスト）にセット
				listEmpUnconfirm.add(emp.getSId());
			}
		}
		return listEmpUnconfirm;
	}

	/**
	 * 承認状況未確認チェック上司
	 * 
	 * @param sId
	 *            社員ID
	 * @param wkpId
	 *            職場ID
	 * @param startDate
	 *            社員ID.期間(開始日)
	 * @param endDate
	 *            社員ID.期間(終了日)
	 * @return 結果（あり/なし)
	 */
	private boolean checkUnconfirmBoss(String sId, String wkpId, GeneralDate startDate, GeneralDate endDate) {
		String cId = AppContexts.user().companyId();
		List<String> listEmpId = new ArrayList<>();
		// imported（ワークフロー）「承認ルート状況」を取得する
		// RequestList113
		List<ApproveRootStatusForEmpImport> listAppRootStatus = approvalStatusAdapter
				.getApprovalByEmplAndDate(startDate, endDate, sId, cId, 1);
		// 承認ルートの状況
		for (ApproveRootStatusForEmpImport appRoot : listAppRootStatus) {
			if (ApprovalStatusForEmployee.APPROVED.equals(appRoot.getApprovalStatus())) {
				// 上司社員ID（リスト）に承認ルートの承認者を追加する
				listEmpId.add(appRoot.getEmployeeID());
			}
		}
		// 上司社員ID（リスト）の存在状態を確認
		return listEmpId.size() != 0;
	}

	/**
	 * 承認状況未確認メール送信月次確認者取得
	 * 
	 * @param listEmp
	 *            社員ID＜社員ID、期間＞(リスト)
	 * @param wkpId
	 *            職場ID
	 * @return 未確認者＜社員ID、期間＞（リスト）
	 */
	private List<String> getEmpUnconfirmByMonthly(List<RealityStatusEmployeeImport> listEmp, String wkpId) {
		String cId = AppContexts.user().companyId();
		List<String> listEmpUnconfirm = new ArrayList<>();
		// 社員ID（リスト）
		for (RealityStatusEmployeeImport emp : listEmp) {
			// imported（申請承認）「List<日付、状態＞」を取得する
			// RequestList113
			List<ApproveRootStatusForEmpImport> listAppRootStatus = approvalStatusAdapter
					.getApprovalByEmplAndDate(emp.getStartDate(), emp.getEndDate(), emp.getSId(), cId, 2);
			Optional<ApproveRootStatusForEmpImport> appRootStatus = listAppRootStatus.stream().findFirst();
			// 承認ルートの状況
			if (appRootStatus.isPresent()
					&& ApprovalStatusForEmployee.UNAPPROVED.equals(appRootStatus.get().getApprovalStatus())) {
				// 未確認者に承認ルートの状況.承認者をセット ※複数の場合複数セット
				listEmpUnconfirm.add(emp.getSId());
			}
		}
		return listEmpUnconfirm;
	}

	/**
	 * 承認状況未確認メール本文取得
	 * 
	 * @param listEmpUmconfirm
	 *            未確認者
	 * @param type
	 *            送信区分
	 */
	private MailTranmissionContentResultOutput getUnconfirmEmailContent(List<EmployeeUnconfirmImport> listEmpUmconfirm,
			ApprovalStatusMailType type) {
		List<MailTransmissionContentOutput> listMail = new ArrayList<>();
		// アルゴリズム「承認状況メール本文取得」を実行する
		ApprovalStatusMailTempImport mailDomain = approvalStatusRequestAdapter.getApprovalStatusMailTemp(type.value);

		// 未確認者を社員ID順に並び替える
		listEmpUmconfirm.stream().sorted(Comparator.comparing(EmployeeUnconfirmImport::getSId));
		String sIdTemp = "";
		// 未確認者（リスト）
		for (EmployeeUnconfirmImport emp : listEmpUmconfirm) {
			if (sIdTemp.equals(emp.getSId())) {
				// 同じ場合
				continue;
			}
			// メール送信内容（リスト）に値を追加する
			MailTransmissionContentOutput mail = new MailTransmissionContentOutput(emp.getSId(), emp.getSName(),
					emp.getMailAddr(), mailDomain.getMailSubject(), mailDomain.getMailContent());
			listMail.add(mail);
			sIdTemp = emp.getSId();
		}
		return new MailTranmissionContentResultOutput(mailDomain, listMail);
	}

	/**
	 * 承認状況取得職場社員実績
	 */
	public List<EmpPerformanceOutput> getAcquisitionWkpEmpPerformance(String wkpId, GeneralDate startDate,
			GeneralDate endDate, List<String> listEmpCd) {
		String cId = AppContexts.user().companyId();
		List<EmpPerformanceOutput> listEmpPerformance = new ArrayList<>();

		// アルゴリズム「承認状況取得実績使用設定」を実行する
		UseSetingOutput useSetting = this.getUseSetting(cId);
		// アルゴリズム「承認状況取得社員」を実行する
		List<RealityStatusEmployeeImport> listStatusEmp = approvalStatusRequestAdapter.getApprovalStatusEmployee(wkpId,
				startDate, endDate, listEmpCd);
		// 社員ID(リスト)
		for (RealityStatusEmployeeImport emp : listStatusEmp) {
			ApproveRootStatusForEmpImport routeStatus = null;

			// imported（就業）「社員基本情報」を取得する
			// RequestList1
			EmployeeRecordImport empInfo = employeeRecordAdapter.getPersonInfor(emp.getSId());
			String empName = Objects.isNull(empInfo) ? "" : empInfo.getPname();

			if (useSetting.isMonthlyConfirm()) {
				// imported（ワークフロー）「承認ルート状況」を取得する
				Optional<ApproveRootStatusForEmpImport> appRootStatus = approvalStatusAdapter
						.getApprovalByEmplAndDate(startDate, endDate, emp.getSId(), cId, 2).stream().findFirst();
				if (appRootStatus.isPresent()) {
					routeStatus = appRootStatus.get();
				}
			}
			// アルゴリズム「承認状況取得日別確認状況」を実行する
			EmpDailyConfirmOutput result = this.getApprovalSttByDate(emp.getSId(), wkpId, emp.getStartDate(),
					emp.getEndDate(), useSetting.isUseBossConfirm(), useSetting.isUsePersonConfirm());
			// アルゴリズム「承認状況取得日別エラー状況」を実行する
			List<ErrorStatusOutput> listErrorStatus = this.getErrorStatus(cId, emp.getSId(), wkpId, emp.getStartDate(),
					emp.getEndDate());
			// 画面に内容をセットする
			listEmpPerformance.add(new EmpPerformanceOutput(emp.getSId(), empName, emp.getStartDate(), emp.getEndDate(),
					routeStatus, result.getListDailyConfirm(), listErrorStatus));
		}
		return listEmpPerformance;
	}

	/**
	 * 承認状況取得日別エラー状況
	 */
	private List<ErrorStatusOutput> getErrorStatus(String cId, String sId, String wkpId, GeneralDate startDate,
			GeneralDate endDate) {
		List<ErrorStatusOutput> listError = new ArrayList<>();
		// imported（KAF018承認状況の照会）「実績エラー状況＜年月日、boolean＞」を取得する
		// TODO RequestList303
		// listError.add(new ErrorStatusOutput(wkpId, sId, startDate.addDays(1)));
		return listError;
	}
}
