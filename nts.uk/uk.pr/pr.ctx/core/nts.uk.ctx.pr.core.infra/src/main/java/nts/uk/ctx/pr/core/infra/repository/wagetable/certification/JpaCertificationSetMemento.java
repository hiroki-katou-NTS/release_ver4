/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.certification;

import nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationSetMemento;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertify;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyPK;

/**
 * The Class JpaCertificationSetMemento.
 */
public class JpaCertificationSetMemento implements CertificationSetMemento {

	/** The type value. */
	private QwtmtWagetableCertify typeValue;

	/**
	 * Instantiates a new jpa certification set memento.
	 *
	 * @param typeValue
	 *            the type value
	 * @param groupCode
	 *            the group code
	 */
	public JpaCertificationSetMemento(QwtmtWagetableCertify typeValue, String groupCode) {
		this.typeValue = typeValue;
		QwtmtWagetableCertifyPK qcemtCertificationPK = new QwtmtWagetableCertifyPK();
		qcemtCertificationPK.setCertifyGroupCd(groupCode);
		this.typeValue.setQwtmtWagetableCertifyPK(qcemtCertificationPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertificationSetMemento#setCompanyCode(
	 * nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		QwtmtWagetableCertifyPK qcemtCertificationPK = this.typeValue.getQwtmtWagetableCertifyPK();
		qcemtCertificationPK.setCcd(companyCode);
		this.typeValue.setQwtmtWagetableCertifyPK(qcemtCertificationPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertificationSetMemento#setCode(java.
	 * lang.String)
	 */
	@Override
	public void setCode(String code) {
		// Certification Code
		QwtmtWagetableCertifyPK qcemtCertificationPK = this.typeValue.getQwtmtWagetableCertifyPK();
		qcemtCertificationPK.setCertifyCd(code);
		this.typeValue.setQwtmtWagetableCertifyPK(qcemtCertificationPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationSetMemento#
	 * setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// Do nothing.
	}

}
