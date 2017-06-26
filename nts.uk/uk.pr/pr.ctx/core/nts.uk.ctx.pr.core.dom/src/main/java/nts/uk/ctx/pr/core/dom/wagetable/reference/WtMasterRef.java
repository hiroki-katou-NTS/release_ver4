/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.reference;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.wagetable.WtElementRefNo;

/**
 * The Class WageTableMasterRef.
 */
@Getter
public class WtMasterRef {

	/** The company code. */
	private String companyCode;

	/** The ref no. */
	private WtElementRefNo refNo;

	/** The ref name. */
	private String refName;

	/** The wage ref table. */
	private String wageRefTable;

	/** The wage ref field. */
	private String wageRefField;

	/** The wage ref disp field. */
	private String wageRefDispField;

	/** The wage person table. */
	private String wagePersonTable;

	/** The wage person field. */
	private String wagePersonField;

	/** The wage ref query. */
	private String wageRefQuery;

	/** The wage person query. */
	private String wagePersonQuery;

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new wage table master ref.
	 *
	 * @param memento
	 *            the memento
	 */
	public WtMasterRef(WtMasterRefGetMemento memento) {
		this.companyCode = memento.getCompanyCode();
		this.refNo = memento.getRefNo();
		this.refName = memento.getRefName();
		this.wageRefTable = memento.getWageRefTable();
		this.wageRefField = memento.getWageRefField();
		this.wageRefDispField = memento.getWageRefDispField();
		this.wagePersonTable = memento.getWagePersonTable();
		this.wagePersonField = memento.getWagePersonField();
		this.wageRefQuery = memento.getWageRefQuery();
		this.wagePersonQuery = memento.getWagePersonQuery();
	}

}
