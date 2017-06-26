/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.reference;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.wagetable.WtElementRefNo;

/**
 * The Class WageTableCodeRef.
 */
@Getter
public class WtCodeRef {

	/** The company code. */
	private String companyCode;

	/** The ref no. */
	private WtElementRefNo refNo;

	/** The ref name. */
	private String refName;

	/** The wage ref value. */
	private String wageRefValue;

	/** The wage person table. */
	private String wagePersonTable;

	/** The wage person field. */
	private String wagePersonField;

	/** The wage person query. */
	private String wagePersonQuery;

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new wage table code ref.
	 *
	 * @param memento the memento
	 */
	public WtCodeRef(WtCodeRefGetMemento memento) {
		this.companyCode = memento.getCompanyCode();
		this.refNo = memento.getRefNo();
		this.refName = memento.getRefName();
		this.wageRefValue = memento.getWageRefValue();
		this.wagePersonTable = memento.getWagePersonTable();
		this.wagePersonField = memento.getWagePersonField();
		this.wagePersonQuery = memento.getWagePersonQuery();
	}

}
