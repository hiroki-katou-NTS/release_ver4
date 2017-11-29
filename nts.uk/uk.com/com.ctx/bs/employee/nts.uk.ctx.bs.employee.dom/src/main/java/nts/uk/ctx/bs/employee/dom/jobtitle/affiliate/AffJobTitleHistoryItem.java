package nts.uk.ctx.bs.employee.dom.jobtitle.affiliate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleCode;

/**
 * The Class AffJobHistoryItem.
 */
//  所属職位履歴項目
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AffJobTitleHistoryItem extends AggregateRoot{

	
	/** The history Id. */
	// 履歴ID
	private String historyId;
	
	/** The Employee Id. */
	// 社員ID
	private String employeeId;
	
	/** The job title code. */
	// 職位コード
	private String jobTitleId;
	
	/** The AffJobHistoryItemNote. */
	// 備考
	private AffJobTitleHistoryItemNote note;
	
	public static AffJobTitleHistoryItem createFromJavaType(String histId, String employeeId, String jobTitleId, String note){
		return new AffJobTitleHistoryItem(histId,employeeId, jobTitleId, new AffJobTitleHistoryItemNote(note));
	}
}
