/**
 * 4:21:18 PM Aug 22, 2017
 */
package nts.uk.screen.at.app.dailyperformance.correction;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hungnm
 *
 */
@Data
@AllArgsConstructor
public class DailyPerformanceEmployeeDto {
	private String id;
	private String code;
	private String businessName;
	private String workplaceName;
	private String depName;
}
