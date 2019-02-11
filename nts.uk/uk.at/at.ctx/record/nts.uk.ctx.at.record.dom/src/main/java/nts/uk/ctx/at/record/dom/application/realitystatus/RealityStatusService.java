package nts.uk.ctx.at.record.dom.application.realitystatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.record.dom.adapter.request.application.ApprovalStatusRequestAdapter;
import nts.uk.ctx.at.record.dom.adapter.request.application.dto.ApprovalStatusMailTempImport;
import nts.uk.ctx.at.record.dom.adapter.request.application.dto.EmployeeUnconfirmImport;
import nts.uk.ctx.at.record.dom.adapter.request.application.dto.RealityStatusEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.request.application.dto.SendMailResultImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSttMonthEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproverApproveImport;
//import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproverEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.EmpPerformMonthParamImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.application.realitystatus.enums.ApprovalStatusMailType;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.DailyConfirmOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.EmpDailyConfirmOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.EmpPerformanceOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.EmpUnconfirmResultOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.EmployeeErrorOuput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.ErrorStatusOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.MailTranmissionContentResultOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.MailTransmissionContentOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.StatusWkpActivityOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.SttWkpActivityOutputFull;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.SumCountOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.UseSetingOutput;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.WkpIdMailCheckOutput;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@RequestScoped
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

	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepo;
	
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;

	/**
	 * 承認状況職場実績起動
	 */
	public SttWkpActivityOutputFull getStatusWkpActivity(List<String> listWorkplaceId, GeneralDate startDate,
			GeneralDate endDate, List<String> listEmpCd, boolean isConfirmData, Integer closureID) {
		String cId = AppContexts.user().companyId();
		List<StatusWkpActivityOutput> listStatusActivity = new ArrayList<StatusWkpActivityOutput>();
		// アルゴリズム「承認状況取得実績使用設定」を実行する
		UseSetingOutput useSeting = this.getUseSetting(cId);
		boolean error = false;
		// 職場ID(リスト)
		for (String wkpId : listWorkplaceId) {
			// アルゴリズム「承認状況取得社員」を実行する
			List<RealityStatusEmployeeImport> listStatusEmp = approvalStatusRequestAdapter
					.getApprovalStatusEmployee(wkpId, startDate, endDate, listEmpCd);
			// アルゴリズム「承認状況取得職場実績確認」を実行する
			SumCountOutput count = new SumCountOutput();
			try{
				count = this.getApprovalSttConfirmWkpResults(listStatusEmp, wkpId, useSeting, endDate, closureID);
			}
			catch(BusinessException ex){
				error = true;
			}
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
		return new SttWkpActivityOutputFull(listStatusActivity, error);
	}

	/**
	 * 承認状況未確認メール送信
	 */
	public void checkSendUnconfirmedMail(List<WkpIdMailCheckOutput> listWkp) {
		// EA修正履歴 2127
		/*// アルゴリズム「承認状況送信者メール確認」を実行する
		String email = approvalStatusRequestAdapter.confirmApprovalStatusMailSender();*/
		// アルゴリズム「承認状況未確認メール送信実行チェック」を実行する
		if (listWkp.stream().filter(x -> x.isCheckOn()).count() == 0) {
			throw new BusinessException("Msg_794");
		}
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
			UseSetingOutput useSeting, GeneralDate endDate, Integer closureID) throws BusinessException{
		SumCountOutput sumCount = new SumCountOutput();
		// 社員ID(リスト)
		List<String> litEmp = listEmp.stream().map(c -> c.getSId()).collect(Collectors.toList());
		//imported(申請承認）「上司確認の状況」を取得する - RequestList533
		Map<String, AppRootSttMonthEmpImport> mapApprByMonth = new HashMap<>();
		try{
			mapApprByMonth = this.mapApprByMonth(litEmp, endDate, closureID);
		}
		catch(BusinessException ex){
			throw new BusinessException("Msg_1430", "承認者");
		}
		for (RealityStatusEmployeeImport emp : listEmp) {
			// 月別確認を利用する
			if (useSeting.isMonthlyConfirm()) {
				//imported(申請承認）「上司確認の状況」を取得する - RequestList533
				AppRootSttMonthEmpImport approval = mapApprByMonth.get(emp.getSId());
				// 承認状況＝「承認済」の場合(trạng thái approval = 「承認済」)
				if (approval != null && ApprovalStatusForEmployee.APPROVED.equals(approval.getApprovalStatus())) {
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
	private Map<String, AppRootSttMonthEmpImport> mapApprByMonth(List<String> lstEmp, GeneralDate baseDate, 
			Integer closureID) throws BusinessException{
		Map<String, AppRootSttMonthEmpImport> result = new HashMap<>();
		//imported(申請承認）「上司確認の状況」を取得する - RequestList533
//		・社員ID（LIST)＝社員ID(申請者）
//		・締めID＝A画面で指定した締めID
//		・締め日＝A画面で指定した締めIDの締め日
//		・基準日＝期間終了日（起動時パラメータ）
		List<EmpPerformMonthParamImport> lstParam = new ArrayList<>();
		GeneralDate tmpDate = baseDate.addDays(1);
		int monthTmp = tmpDate.month();
		int month = baseDate.month();
		int day = baseDate.day();
		Boolean lastDayOfMonth = monthTmp - 1 == month ? true : false;
		ClosureDate closureDate = new ClosureDate(lastDayOfMonth ? 1 : day, lastDayOfMonth);
		for (String emp : lstEmp) {
			lstParam.add(new EmpPerformMonthParamImport(baseDate.yearMonth(), closureID, closureDate, baseDate, emp));
		}
		List<AppRootSttMonthEmpImport> listApproval = new ArrayList<>();
		try {
			listApproval = approvalStatusAdapter.getAppRootStatusByEmpsMonth(lstParam);
		}
		catch(Exception ex){
			throw new BusinessException("Msg_1430", "承認者");
		}
		for (AppRootSttMonthEmpImport appRootStt : listApproval) {
			result.put(appRootStt.getEmployeeID(), appRootStt);
		}
		return result;
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
		// // Request list 113
		// imported（ワークフロー）「承認ルート状況」を取得する
		List<ApproveRootStatusForEmpImport> listApproval = approvalStatusAdapter.getApprovalByEmplAndDate(startDate,
				endDate, sId, cid, 1);
		// 承認ルートの状況
		for (ApproveRootStatusForEmpImport approval : listApproval) {
			DailyConfirmOutput dailyConfirm;
			// 日別確認（リスト）に同じ職場ID、社員ID、対象日が登録済
			Optional<DailyConfirmOutput> confirm = listDailyConfirm.stream().filter(x -> x.getWkpId().equals(wkpId)
					&& x.getSId().equals(sId) && x.getTargetDate().equals(approval.getAppDate())).findFirst();
			if (confirm.isPresent()) {
				// 対象の日別確認（リスト）の行を対象とする
				dailyConfirm = confirm.get();
			} else {
				// 日別確認（リスト）に追加する
				dailyConfirm = new DailyConfirmOutput(wkpId, sId, approval.getAppDate(), false, false);
				listDailyConfirm.add(dailyConfirm);
			}
			// 承認ルート状況.承認状況
			if (ApprovalStatusForEmployee.APPROVED.equals(approval.getApprovalStatus())) {
				// 上司確認 ＝確認
				dailyConfirm.setBossConfirm(true);
				// 上司確認件数 ＝＋１
				sumCount.bossConfirm++;
			} else {
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
			} else {
				// 日別確認（リスト）に追加
				DailyConfirmOutput newConfirm = new DailyConfirmOutput(wkpId, sId, identification.getProcessingYmd(),
						true, false);
				listDailyConfirm.add(newConfirm);
			}
			// 本人確認件数 ＝＋１
			sumCount.personConfirm++;
		}
		GeneralDate tempDate = startDate;
		while (tempDate.beforeOrEquals(endDate)) {
			GeneralDate date = tempDate;
			Optional<DailyConfirmOutput> confirmOtp = listDailyConfirm.stream()
					.filter(x -> x.getTargetDate().equals(date)).findFirst();
			if (!confirmOtp.isPresent()) {
				DailyConfirmOutput newConfirm = new DailyConfirmOutput(wkpId, sId, date, false, false);
				listDailyConfirm.add(newConfirm);
			}
			tempDate = tempDate.addDays(1);
		}
		// 本人未確認件数 ＝日別確認(リスト)で上司確認＝未確認、本人確認＝未確認の件数
		sumCount.personUnconfirm = (int) listDailyConfirm.stream()
				.filter(x -> !x.isBossConfirm() && !x.isPersonConfirm()).count();
	}

	/**
	 * 承認状況未確認メール送信実行
	 */
	public SendMailResultImport exeSendUnconfirmMail(ApprovalStatusMailType type, List<WkpIdMailCheckOutput> listWkp,
			GeneralDate startDate, GeneralDate endDate, List<String> listEmpCd, Integer closureID) {
		// アルゴリズム「承認状況未確認メール送信社員取得」を実行する
		EmpUnconfirmResultOutput result = this.getListEmpUnconfirm(type, listWkp, startDate, endDate, listEmpCd, closureID);
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
			List<String> listEmpCd, Integer closureID) {
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
				listSId.addAll(this.getEmpUnconfirmByPerson(listEmp, wkp.getWkpId()));
				break;
			case DAILY:
				// アルゴリズム「承認状況未確認メール送信上司取得」を実行する
				listSId.addAll(this.getEmpUnconfirmByBoss(listEmp, wkp.getWkpId()));
				break;
			case MONTHLY:
				// アルゴリズム「承認状況未確認メール送信月次確認者取得」を実行する
				listSId.addAll(this.getEmpUnconfirmByMonthly(listEmp, wkp.getWkpId(), closureID, closureStart, closureEnd));
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
			List<String> listUnconfirmBoss = this.checkUnconfirmBoss(emp.getSId(), wkpId, emp.getStartDate(),
					emp.getEndDate());
			if (listUnconfirmBoss.size() > 0) {
				// 上司社員ID（リスト）を未確認者（リスト）にセット
				listEmpUnconfirm.addAll(listUnconfirmBoss);
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
	private List<String> checkUnconfirmBoss(String sId, String wkpId, GeneralDate startDate, GeneralDate endDate) {
		List<String> listEmpId = new ArrayList<>();
		List<GeneralDate> dateLst = new ArrayList<>();
		for(GeneralDate loopDate = startDate; loopDate.beforeOrEquals(endDate); loopDate = loopDate.addDays(1)){
			dateLst.add(loopDate);
		}
		// imported（ワークフロー）「社員」を取得する
		List<ApproverApproveImport> listResult = approvalStatusAdapter.getApproverByDateLst(Arrays.asList(sId), dateLst, 1);
		listResult.stream().forEach(x -> {
			x.getAuthorList().stream().forEach(y -> {
				listEmpId.add(y.getEmployeeID());
			});
		});
		return listEmpId;
	}

	/**
	 * 承認状況未確認メール送信月次確認者取得
	 * @param listEmp - 社員ID＜社員ID、期間＞(リスト)
	 * @param wkpId - 職場ID
	 * @return 未確認者＜社員ID、期間＞（リスト）
	 */
	private List<String> getEmpUnconfirmByMonthly(List<RealityStatusEmployeeImport> listEmp, 
			String wkpId, Integer closureID, GeneralDate startDate, GeneralDate endDate) {
		List<String> listEmpUnconfirm = new ArrayList<>();
		// 社員ID（リスト）
        GeneralDate tmpDate = endDate.addDays(1);
        int monthTmp = tmpDate.month();
        int month = endDate.month();
        int day = endDate.day();
        Boolean lastDayOfMonth = monthTmp - 1 == month ? true : false;
        ClosureDate closureDate = new ClosureDate(lastDayOfMonth ? 1 : day, lastDayOfMonth);
		for (RealityStatusEmployeeImport emp : listEmp) {
			//アルゴリズム「承認状況未確認チェック月別」を実行する
			List<String> lstNew = this.checkUnConfByMonth(listEmpUnconfirm, emp.getSId(), endDate.yearMonth(), 
					new DatePeriod(startDate, endDate), closureID, closureDate);
			if(!lstNew.isEmpty()){
				listEmpUnconfirm.addAll(lstNew);
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
	 * 承認状況取得職場社員実績一覧
	 */
	public List<EmpPerformanceOutput> getAcquisitionWkpEmpPerformance(String wkpId, GeneralDate startDate,
			GeneralDate endDate, List<String> listEmpCd, Integer closureID) {
		String cId = AppContexts.user().companyId();
		List<EmpPerformanceOutput> listEmpPerformance = new ArrayList<>();

		// アルゴリズム「承認状況取得実績使用設定」を実行する
		UseSetingOutput useSetting = this.getUseSetting(cId);
		// アルゴリズム「承認状況取得社員」を実行する
		List<RealityStatusEmployeeImport> listStatusEmp = approvalStatusRequestAdapter.getApprovalStatusEmployee(wkpId,
				startDate, endDate, listEmpCd);
		// 社員ID(リスト)
		List<String> litEmp = listStatusEmp.stream().map(c -> c.getSId()).collect(Collectors.toList());
		//imported(申請承認）「上司確認の状況」を取得する - RequestList533
		Map<String, AppRootSttMonthEmpImport> mapApprByMonth = new HashMap<>();
		try{
			mapApprByMonth = this.mapApprByMonth(litEmp, endDate, closureID);
		}
		catch(Exception ex){
			return listEmpPerformance;
		}
		for (RealityStatusEmployeeImport emp : listStatusEmp) {
			ApproveRootStatusForEmpImport routeStatus = null;

			// imported（就業）「社員基本情報」を取得する
			// RequestList1
			EmployeeRecordImport empInfo = employeeRecordAdapter.getPersonInfor(emp.getSId());
			String empName = Objects.isNull(empInfo) ? "" : empInfo.getEmployeeCode() + "　 " +empInfo.getPname();

			if (useSetting.isMonthlyConfirm()) {
				//imported（ワークフロー）「承認ルート状況」を取得する - RequestList533
				AppRootSttMonthEmpImport approval = mapApprByMonth.get(emp.getSId());
				if (approval != null) {
					routeStatus =  new ApproveRootStatusForEmpImport(approval.getEmployeeID(), endDate, approval.getApprovalStatus());
				}
			}
			// アルゴリズム「承認状況取得日別確認状況」を実行する
			EmpDailyConfirmOutput result = this.getApprovalSttByDate(emp.getSId(), wkpId, emp.getStartDate(),
					emp.getEndDate(), useSetting.isUseBossConfirm(), useSetting.isUsePersonConfirm());
			// アルゴリズム「承認状況取得日別エラー状況」を実行する
			List<ErrorStatusOutput> listErrorStatus = this.getErrorStatus(emp.getSId(), wkpId, emp.getStartDate(),
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
	private List<ErrorStatusOutput> getErrorStatus(String sId, String wkpId, GeneralDate startDate,
			GeneralDate endDate) {
		List<ErrorStatusOutput> listError = new ArrayList<>();
		// imported（KAF018承認状況の照会）「実績エラー状況＜年月日、boolean＞」を取得する
		List<EmployeeErrorOuput> listEmpError = this.checkEmployeeErrorOnProcessingDate(sId, startDate, endDate);
		for (EmployeeErrorOuput employeeErrorOuput : listEmpError) {
			if (employeeErrorOuput.getHasError()) {
				// エラー状況(リスト)に追加する
				listError.add(new ErrorStatusOutput(wkpId, sId, employeeErrorOuput.getDate()));
			}
		}
		return listError;
	}

	/**
	 * Request list 303
	 */
	private List<EmployeeErrorOuput> checkEmployeeErrorOnProcessingDate(String employeeId, GeneralDate startDate,
			GeneralDate endDate) {
		List<EmployeeErrorOuput> listEmpErrorOutput = new ArrayList<>();
		
		// 対応するドメインモデル「社員の日別実績エラー一覧」を取得する
		List<EmployeeDailyPerError> listEmpDailyError = this.employeeDailyPerErrorRepo
				.findByPeriodOrderByYmd(employeeId, new DatePeriod(startDate, endDate));
		
		String companyID = AppContexts.user().companyId();
		// 対応するドメインモデル「勤務実績のエラーアラーム」を取得する
		for(EmployeeDailyPerError emp : listEmpDailyError){
			List<ErrorAlarmWorkRecord> errorAlarmWorkRecordLst =  errorAlarmWorkRecordRepository.getListErAlByListCodeError(
					companyID, Arrays.asList(emp.getErrorAlarmWorkRecordCode().v()));
			if(!CollectionUtil.isEmpty(errorAlarmWorkRecordLst)){
				listEmpErrorOutput.add(new EmployeeErrorOuput(emp.getDate(), true));
			}
		}
		
		return listEmpErrorOutput;
	}

	/*
	private List<GeneralDate> getDaysBetween(GeneralDate startDate, GeneralDate endDate) {
		List<GeneralDate> daysBetween = new ArrayList<>();

		while (startDate.beforeOrEquals(endDate)) {
			daysBetween.add(startDate);
			GeneralDate temp = startDate.addDays(1);
			startDate = temp;
		}

		return daysBetween;
	}
	*/
	/**
	 * 承認状況未確認チェック月別
	 * @param 社員ID - sID
	 * @param 年月 - yearMonth
	 * @param 期間 - period
	 * @return 結果（あり/なし) - 月別未承認社員ID（リスト）
	 */
	public List<String> checkUnConfByMonth(List<String> lstAppr, String sID, YearMonth yearMonth, DatePeriod period, 
			Integer closureID, ClosureDate baseDate){
		//imported（ワークフロー）「社員」を取得する - RequestList538
//		社員ID（申請者）＝社員ID
//		年月＝年月
//		締めID＝A画面で指定した締めID
//		締め日＝A画面で指定した締めIDの締め日
//		基準日＝期間終了日
		ApproverApproveImport appr = approvalStatusAdapter.getApproverByPeriodMonth(sID, closureID, yearMonth, baseDate, period.end());
		//月別未承認社員ID（リスト）に「承認すべき承認者.社員（リスト）」を追加する
		List<String> lstResult = appr.getAuthorList().stream()
				.filter(c -> !lstAppr.contains(c)).map(c -> c.getEmployeeID()).collect(Collectors.toList());
		return lstResult;
	}
}
