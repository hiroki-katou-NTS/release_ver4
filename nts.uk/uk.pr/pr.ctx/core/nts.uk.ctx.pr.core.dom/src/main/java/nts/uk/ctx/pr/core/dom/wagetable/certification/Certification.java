/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.certification;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * The Class Certification.
 */
@Getter
@EqualsAndHashCode(of = { "companyCode", "code" })
public class Certification {

	/** The company code. */
	private String companyCode;

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new labor insurance office.
	 *
	 * @param memento
	 *            the memento
	 */
	public Certification(CertificationGetMemento memento) {
		this.companyCode = memento.getCompanyCode();
		this.code = memento.getCode();
		this.name = memento.getName();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(CertificationSetMemento memento) {
		memento.setCompanyCode(this.companyCode);
		memento.setCode(this.code);
		memento.setName(this.name);
	}

}
