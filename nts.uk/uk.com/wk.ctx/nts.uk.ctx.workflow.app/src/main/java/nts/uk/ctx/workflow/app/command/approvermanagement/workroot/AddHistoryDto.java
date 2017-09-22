package nts.uk.ctx.workflow.app.command.approvermanagement.workroot;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AddHistoryDto {

	/**申請種類*/
	private Integer applicationType;
	/**開始日*/
	private String startDate;
	/**開始日 Old*/
	private String startDateOld;
	/**check 申請承認の種類区分*/
	private int mode;
	/**履歴から引き継ぐか、初めから作成するかを選択する*/
	private boolean copyDataFlag;
}
