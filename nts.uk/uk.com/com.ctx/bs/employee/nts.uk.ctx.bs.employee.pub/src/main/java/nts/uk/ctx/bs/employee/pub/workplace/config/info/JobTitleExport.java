package nts.uk.ctx.bs.employee.pub.workplace.config.info;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobTitleExport {

	/** The company id. */
	//会社ID
	private String companyId;

	/** The job title id. */
	//職位ID
	private String jobTitleId;
	
	/** The job title history. */
	//履歴
	private List<JobTitleHistoryExport> jobTitleHistories;
}
