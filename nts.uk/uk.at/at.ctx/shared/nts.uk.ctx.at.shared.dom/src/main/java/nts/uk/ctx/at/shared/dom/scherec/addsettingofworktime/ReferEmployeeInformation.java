/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class ReferEmployeeInformation.
 */
// 社員情報を参照する
@Getter
public class ReferEmployeeInformation extends DomainObject{
	// 所定時間参照先
	private VacationSpecifiedTimeRefer timeReferenceDestination;

	/**
	 * @param timeReferenceDestination
	 */
	public ReferEmployeeInformation(VacationSpecifiedTimeRefer timeReferenceDestination) {
		super();
		this.timeReferenceDestination = timeReferenceDestination;
	}
}

