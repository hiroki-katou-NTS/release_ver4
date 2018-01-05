package nts.uk.ctx.workflow.app.command.approvermanagement.workroot;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWorkAppApprovalRByHistCommand {
	/**開始日*/
	private String startDate;
	/**終了日*/
	private String endDate;
	/**履歴ID*/
	private String workplaceId;
	/**社員ID*/
	private String employeeId;
	/**check 申請承認の種類区分: 会社(0)　－　職場(1)　－　社員(2)*/
	private int check;
	/**「履歴を削除する」を選択する か、「履歴を修正する」を選択する か。*/
	private int editOrDelete;
	/**開始日 previous*/
	private String startDatePrevious;
	/**	list history and approvalId */
	private List<UpdateHistoryDto> lstUpdate;
	/**	checkMode */
	private int checkMode;
}
