package nts.uk.ctx.bs.employee.pub.jobtitle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class JobTitleInfoExport {
	/** The company id. */
	// 会社ID
	private String companyId;

	/** The job title history id. */
	// 職位履歴ID
	private String jobTitleHistoryId;

	/** The is manager. */
	// 管理職とする
	private boolean isManager;
	
	/** The job title id. */
	// 職位ID
	private String jobTitleId;

	/** The job title code. */
	// 職位コード
	private String jobTitleCode;

	/** The job title name. */
	// 職位名称
	private String jobTitleName;

	/** The sequence code. */
	// 序列コード
	private String sequenceCode;	
}
