/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.command.dto;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationGetMemento;

/**
 * The Class CertificationDto.
 */

@Data
public class CertificationDto {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the certification
	 */
	public Certification toDomain(String companyCode) {
		return new Certification(new CertificationGetMementoImpl(this, companyCode));
	}

	/**
	 * The Class CertificationGetMementoImpl.
	 */
	public class CertificationGetMementoImpl implements CertificationGetMemento {

		/** The dto. */
		private CertificationDto dto;

		/** The company code. */
		private String companyCode;

		/**
		 * Instantiates a new certification get memento impl.
		 *
		 * @param dto
		 *            the dto
		 * @param companyCode
		 *            the company code
		 */
		public CertificationGetMementoImpl(CertificationDto dto, String companyCode) {
			super();
			this.dto = dto;
			this.companyCode = companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.certification.
		 * CertificationGetMemento#getName()
		 */
		@Override
		public String getName() {
			return dto.name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.certification.
		 * CertificationGetMemento#getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.certification.
		 * CertificationGetMemento#getCode()
		 */
		@Override
		public String getCode() {
			return dto.code;
		}
	}

}
