package nts.uk.ctx.bs.employee.pub.employee.workplace.export;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author sonnh1
 *
 */
@Data
@AllArgsConstructor
public class WorkplaceExportPubDto {
	/** The workplace id. */
	//職場ID
	private String workplaceId;

	// 階層コード
	private String hierarchyCd;
}
