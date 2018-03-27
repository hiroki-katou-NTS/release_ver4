/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums;

import lombok.AllArgsConstructor;

/**
 * @author danpv
 *
 */
@AllArgsConstructor
public enum SysErClass {
	
	NO_ERROR(0),
	
	DB_ACCESS_FAILURE(1),
	
	MAN_MAS_SHORTAGE(2);
	
	public final int value;

}
