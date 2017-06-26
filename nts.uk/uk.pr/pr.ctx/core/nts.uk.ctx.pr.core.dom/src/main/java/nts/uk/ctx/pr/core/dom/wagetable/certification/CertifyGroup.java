/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.certification;

import java.util.Set;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class CertifyGroup.
 */
@Getter
public class CertifyGroup extends DomainObject {

	/** The company code. */
	private String companyCode;

	/** The code. */
	private CertifyGroupCode code;

	/** The name. */
	private CertifyGroupName name;

	/** The multi apply set. */
	private MultipleTargetSetting multiApplySet;

	/** The certifies. */
	private Set<Certification> certifies;

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new certify group.
	 *
	 * @param memento
	 *            the memento
	 */
	public CertifyGroup(CertifyGroupGetMemento memento) {
		this.companyCode = memento.getCompanyCode();
		this.code = memento.getCode();
		this.name = memento.getName();
		this.multiApplySet = memento.getMultiApplySet();
		this.certifies = memento.getCertifies();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(CertifyGroupSetMemento memento) {
		memento.setCompanyCode(this.companyCode);
		memento.setCode(this.code);
		memento.setName(this.name);
		memento.setMultiApplySet(this.multiApplySet);
		memento.setCertifies(this.certifies);
	}

}
