/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.log.objectvalue;

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
