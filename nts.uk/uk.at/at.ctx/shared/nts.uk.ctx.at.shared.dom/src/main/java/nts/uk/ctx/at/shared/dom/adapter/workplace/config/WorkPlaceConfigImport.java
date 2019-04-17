package nts.uk.ctx.at.shared.dom.adapter.workplace.config;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//職場構成
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkPlaceConfigImport {
	/** The company id. */
	// 会社ID
	private String companyId;

	/** The wkp config history. */
	// 履歴
	private List<WorkplaceConfigHistoryImport> wkpConfigHistory;
}
