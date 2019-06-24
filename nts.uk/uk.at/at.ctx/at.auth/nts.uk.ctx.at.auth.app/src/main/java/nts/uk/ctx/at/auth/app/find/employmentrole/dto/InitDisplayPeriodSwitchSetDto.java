/**
 * 
 */
package nts.uk.ctx.at.auth.app.find.employmentrole.dto;

import java.util.List;

import lombok.Value;

/**
 * @author hieult
 *
 */
@Value
public class InitDisplayPeriodSwitchSetDto {

	private int currentOrNextMonth;
	private List<DateProcessed> listDateProcessed;

}
