package nts.uk.ctx.bs.employee.pub.workplace.config;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
// 職場構成
public class WorkPlaceConfigExport {
	/** The company id. */
	// 会社ID
	private String companyId;

	/** The wkp config history. */
	// 履歴
	private List<WorkplaceConfigHistoryExport> wkpConfigHistory;
}
