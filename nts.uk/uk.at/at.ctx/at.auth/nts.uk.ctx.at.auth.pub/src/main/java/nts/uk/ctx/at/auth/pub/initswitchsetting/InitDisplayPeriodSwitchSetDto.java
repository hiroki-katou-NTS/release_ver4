/**
 * 
 */
package nts.uk.ctx.at.auth.pub.initswitchsetting;

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
