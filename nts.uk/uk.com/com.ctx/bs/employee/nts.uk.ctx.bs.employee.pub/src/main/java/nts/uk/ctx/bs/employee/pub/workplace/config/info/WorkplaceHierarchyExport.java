package nts.uk.ctx.bs.employee.pub.workplace.config.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkplaceHierarchyExport {
	/** The workplace id. */
	// 職場ID
	private String workplaceId;

	/** The hierarchy code. */
	// 階層コード
	private String hierarchyCode;	
}
