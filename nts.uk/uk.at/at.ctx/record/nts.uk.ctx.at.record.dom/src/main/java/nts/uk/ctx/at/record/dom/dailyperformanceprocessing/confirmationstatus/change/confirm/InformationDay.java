package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.record.dom.workrecord.approval.daily.ApprovalProgressDaily;
import nts.uk.ctx.at.record.dom.workrecord.approval.daily.ApprovalSubjectiveDailyOnWorkflow;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;

/**
 * @author thanhnx
 * 日の情報
 */
@AllArgsConstructor
@Data
public class InformationDay {

	//日の本人確認
	private List<Identification> indentities;

	//承認対象者の承認状況
	private List<ApprovalProgressDaily> lstApprovalDayStatus;

	//基準社員の承認対象者
	List<ApprovalSubjectiveDailyOnWorkflow> lstApprovalRootDaily;

	public InformationDay(List<Identification> indentities, List<ApprovalProgressDaily> lstApprovalDayStatus) {
		this.indentities = indentities;
		this.lstApprovalDayStatus = lstApprovalDayStatus;
		this.lstApprovalRootDaily = new ArrayList<>();
	}


}
