package nts.uk.ctx.at.shared.dom.adapter.workplace.config.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkplaceHierarchyImport {
	/** The workplace id. */
	// 職場ID
	private String workplaceId;

	/** The hierarchy code. */
	// 階層コード
	private String hierarchyCode;
}
