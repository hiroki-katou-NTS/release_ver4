package nts.uk.ctx.bs.employee.dom.workplace.export;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author sonnh1
 *
 */
@Data
@AllArgsConstructor
public class WkpInfoDto {
	/** The workplace id. */
	// 職場ID
	private String workplaceId;

	// 階層コード
	private String hierarchyCd;
}
