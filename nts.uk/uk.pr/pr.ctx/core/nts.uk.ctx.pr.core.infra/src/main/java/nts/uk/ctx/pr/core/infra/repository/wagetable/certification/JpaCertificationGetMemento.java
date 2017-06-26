/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.certification;

import nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationGetMemento;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QcemtCertification;

/**
 * The Class JpaCertificationGetMemento.
 */
public class JpaCertificationGetMemento implements CertificationGetMemento {

	/** The type value. */
	private QcemtCertification typeValue;

	/**
	 * Instantiates a new jpa certification get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaCertificationGetMemento(QcemtCertification typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertificationGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQcemtCertificationPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.CertificationGetMemento#getCode()
	 */
	@Override
	public String getCode() {
		return this.typeValue.getQcemtCertificationPK().getCertCd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.CertificationGetMemento#getName()
	 */
	@Override
	public String getName() {
		return this.typeValue.getName();
	}

}
