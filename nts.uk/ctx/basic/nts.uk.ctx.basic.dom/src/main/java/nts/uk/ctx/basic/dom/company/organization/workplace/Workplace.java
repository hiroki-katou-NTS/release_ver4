/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.dom.company.organization.workplace;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.basic.dom.common.history.Period;
import nts.uk.ctx.basic.dom.company.organization.CompanyId;

/**
 * The Class Workplace.
 */
@Getter
public class Workplace extends AggregateRoot{
	
	/** The company id. */
	//会社ID
	private CompanyId companyId;
	
	/** The period. */
	//期間
	private Period period;
	
	/** The work place id. */
	//職場ID
	private WorkplaceId workplaceId;
	
	/** The work place code. */
	//職場コード
	private WorkplaceCode workplaceCode;
	
	/** The workplace name. */
	//職場名称
	private WorkplaceName workplaceName;
	
}
