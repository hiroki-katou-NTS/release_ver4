package nts.uk.ctx.at.record.dom.workinformation.service.updateworkinfo;

import java.util.Arrays;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.createdailyapprover.CreateDailyApproverAdapter;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalsituationmanagement.export.clearapprovalconfirm.ClearConfirmApprovalService;

@Stateless
public class DeleteWorkInfoOfDailyPerforServiceImpl implements DeleteWorkInfoOfDailyPerforService {

	@Inject
	private WorkInformationRepository workInformationRepository;

	@Inject
	private ClearConfirmApprovalService clearConfirmApprovalService;

	@Inject
	private CreateDailyApproverAdapter createDailyApproverAdapter;

	@Override
	public void deleteWorkInfoOfDailyPerforService(String employeeID, GeneralDate processingDate) {

		// ドメインモデル「日別実績の勤務情報」を削除
		this.workInformationRepository.delete(employeeID, processingDate);

		// 日別実績の勤務情報を削除する
		// 日別実績の就業実績確認状態を削除する
		// 確認、承認のクリア
		clearConfirmApprovalService.clearConfirmApproval(employeeID, Arrays.asList(processingDate));

		// RequestList 424
		this.createDailyApproverAdapter.deleteApprovalStatus(employeeID, processingDate, 1);
	}

}
