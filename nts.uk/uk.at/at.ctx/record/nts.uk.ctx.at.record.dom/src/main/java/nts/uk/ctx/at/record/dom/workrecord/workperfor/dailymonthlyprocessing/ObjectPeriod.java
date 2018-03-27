/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;

/**
 * @author danpv
 *
 */
@Getter
@AllArgsConstructor
public class ObjectPeriod {
	
	private GeneralDate startDate;
	
	private GeneralDate endDate;

}
