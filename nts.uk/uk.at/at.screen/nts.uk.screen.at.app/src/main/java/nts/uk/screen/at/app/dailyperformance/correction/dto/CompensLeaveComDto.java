/**
 * 11:29:57 AM Aug 22, 2017
 */
package nts.uk.screen.at.app.dailyperformance.correction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hungnm - 代休残数
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompensLeaveComDto {

	private boolean manageCompenLeave;

	private boolean manageTimeOff;

	private Integer compenLeaveRemain;
	
	private Integer timeRemain;
	
}
