package nts.uk.ctx.at.shared.dom.adapter.workplace.config.info;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
//職場構成情報
public class WorkPlaceConfigInfoImport {
	/** The company id. */
	// 会社ID
	private String companyId;

	/** The history id. */
	// 履歴ID
	private String historyId;

	/** The lst wkp hierarchy. */
	// 階層
	private List<WorkplaceHierarchyImport> lstWkpHierarchy;
}
