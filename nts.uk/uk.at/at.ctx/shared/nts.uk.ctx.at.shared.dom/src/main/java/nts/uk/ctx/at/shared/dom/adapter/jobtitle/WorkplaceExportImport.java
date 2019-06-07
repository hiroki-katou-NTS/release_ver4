package nts.uk.ctx.at.shared.dom.adapter.jobtitle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class WorkplaceExportImport {
	/** The workplace id. */
	//職場ID
	private String workplaceId;

	// 階層コード
	private String hierarchyCd;
	
}
