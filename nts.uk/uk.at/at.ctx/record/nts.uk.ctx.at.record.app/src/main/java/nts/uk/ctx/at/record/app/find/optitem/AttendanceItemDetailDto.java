/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.find.optitem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class AttendanceItemDetailDto.
 */
@Getter
@Setter
@Builder
public class AttendanceItemDetailDto {

	/** The display number. */
	private int displayNumber;

	/** The name. */
	private String name;

}
