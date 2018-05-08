package nts.uk.ctx.at.record.dom.workrecord.manageactualsituation.approval.monthly;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.workrecord.manageactualsituation.ApprovalStatus;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApprovalProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApprovalProcessRepository;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.DailyPerformanceAdapter;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.SharedAffJobTitleHisImport;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.SharedAffJobtitleHisAdapter;

@Stateless
public class MonthlyApprovalProcess {
	
	@Inject 
	ApprovalProcessRepository approvalRepo;
	@Inject
	private SharedAffJobtitleHisAdapter affJobTitleAdapter;
	@Inject
	DailyPerformanceAdapter dailyPerformanceAdapter;
	
	/**
	 * 対象月の月の承認が済んでいるかチェックする
	 * @param employeeId: 社員ID
	 * @param processDateYM: 年月
	 * @param closureId: 締め
	 * @param closureDate: 締め日
	 * @return 承認が済んでいる
	 */
	public ApprovalStatus monthlyApprovalCheck(String cId, String employeeId, Integer processDateYM, Integer closureId, GeneralDate closureDate){
		//社員が対象月の承認処理を利用できるかチェックする
		if(!canUseMonthlyApprovalCheck(cId, employeeId, closureDate)){
			//利用できない場合
			return ApprovalStatus.UNAPPROVAL;
		}
		//対応するImported「（就業．勤務実績）承認対象者の月別実績の承認状況」をすべて取得する
		//rootType = 1(Monthly)
		List<ApproveRootStatusForEmpImport> lstApprovalState = dailyPerformanceAdapter.getApprovalByListEmplAndListApprovalRecordDate(Arrays.asList(closureDate), Arrays.asList(employeeId), 1);
		if(!CollectionUtil.isEmpty(lstApprovalState) && lstApprovalState.get(0).getApprovalStatus() == 2){
			return ApprovalStatus.APPROVAL;
		}
		return ApprovalStatus.UNAPPROVAL;
	}
	/**
	 * 社員が対象月の承認処理を利用できるかチェックする
	 * @param cId: ログイン会社に一致する
	 * @param employeeId: 社員ID
	 * @param baseDate: 基準日：年月日
	 * @return
	 */
	public boolean canUseMonthlyApprovalCheck(String cId, String employeeId, GeneralDate baseDate){
		//対応するドメインモデル「承認処理の利用設定」を取得する
		 Optional<ApprovalProcess> approvalProcOp = approvalRepo.getApprovalProcessById(cId);
		 if(!approvalProcOp.isPresent())
			 return false;
		 //「月の承認者確認を利用する」をチェックする
		 if(approvalProcOp.get().getUseMonthBossChk() == 0)
			 return false;
		 //Imported「（就業）所属職位履歴」を取得する
		 Optional<SharedAffJobTitleHisImport> jobTitleHasData = this.affJobTitleAdapter.findAffJobTitleHis(employeeId, baseDate);
		 //承認処理が必要な職位かチェックする
		 if(!jobTitleHasData.isPresent())
			 return false;
		 //パラメータ「社員の職位ID」がドメインモデル「承認処理の利用設定．承認処理が必要な職位」に該当するかチェックする
		 if(approvalProcOp.get().getJobTitleId().equals(jobTitleHasData.get().getJobTitleId())){
			 return true;
		 }
		return false;
	}
	
	
}
