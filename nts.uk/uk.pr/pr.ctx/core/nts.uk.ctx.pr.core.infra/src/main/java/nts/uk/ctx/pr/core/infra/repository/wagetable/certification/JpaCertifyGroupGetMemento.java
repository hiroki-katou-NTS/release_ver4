/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.certification;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupCode;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupGetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupName;
import nts.uk.ctx.pr.core.dom.wagetable.certification.MultipleTargetSetting;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertify;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyG;

/**
 * The Class JpaCertifyGroupGetMemento.
 */
public class JpaCertifyGroupGetMemento implements CertifyGroupGetMemento {

	/** The type value. */
	private QwtmtWagetableCertifyG typeValue;

	/**
	 * Instantiates a new jpa certify group get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaCertifyGroupGetMemento(QwtmtWagetableCertifyG typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQwtmtWagetableCertifyGPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupGetMemento#getCode()
	 */
	@Override
	public CertifyGroupCode getCode() {
		return new CertifyGroupCode(
				this.typeValue.getQwtmtWagetableCertifyGPK().getCertifyGroupCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupGetMemento#getName()
	 */
	@Override
	public CertifyGroupName getName() {
		return new CertifyGroupName(this.typeValue.getCertifyGroupName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupGetMemento#getMultiApplySet(
	 * )
	 */
	@Override
	public MultipleTargetSetting getMultiApplySet() {
		return MultipleTargetSetting.valueOf(this.typeValue.getMultiApplySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.CertifyGroupGetMemento#getCertifies()
	 */
	@Override
	public Set<Certification> getCertifies() {
		// Get list of QwtmtWagetableCertify.
		List<QwtmtWagetableCertify> qwtmtWagetableCertifyList = this.typeValue
				.getQwtmtWagetableCertifyList();

		// Return.
		return qwtmtWagetableCertifyList.stream()
				.map(item -> new Certification(
						new JpaCertificationGetMemento(item.getQcemtCertification())))
				.collect(Collectors.toSet());
	}

}
