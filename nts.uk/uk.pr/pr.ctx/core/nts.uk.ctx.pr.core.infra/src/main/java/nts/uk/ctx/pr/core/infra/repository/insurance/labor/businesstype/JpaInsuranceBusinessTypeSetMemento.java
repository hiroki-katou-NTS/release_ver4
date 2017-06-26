/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.businesstype;

import nts.uk.ctx.pr.core.dom.insurance.BusinessName;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.InsuranceBusinessTypeSetMemento;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.businesstype.QismtBusinessType;

/**
 * The Class JpaInsuranceBusinessTypeSetMemento.
 */
public class JpaInsuranceBusinessTypeSetMemento implements InsuranceBusinessTypeSetMemento {

	/** The type value. */
	private QismtBusinessType typeValue;

	/** The type. */
	private BusinessTypeEnum type;

	/**
	 * Instantiates a new jpa insurance business type set memento.
	 *
	 * @param typeValue
	 *            the type value
	 * @param type
	 *            the type
	 */
	public JpaInsuranceBusinessTypeSetMemento(QismtBusinessType typeValue, BusinessTypeEnum type) {
		this.typeValue = typeValue;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.
	 * InsuranceBusinessTypeSetMemento#setCompanyCode(nts.uk.ctx.core.dom.
	 * company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.typeValue.setCcd(companyCode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.
	 * InsuranceBusinessTypeSetMemento#setBizOrder(nts.uk.ctx.pr.core.dom.
	 * insurance.labor.businesstype.BusinessTypeEnum)
	 */
	@Override
	public void setBizOrder(BusinessTypeEnum businessTypeEnum) {
		// Not function

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.
	 * InsuranceBusinessTypeSetMemento#setBizName(nts.uk.ctx.pr.core.dom.
	 * insurance.BusinessName)
	 */
	@Override
	public void setBizName(BusinessName name) {
		switch (this.type) {
		case Biz1St:
			this.typeValue.setBizName01(name.v());
			break;

		case Biz2Nd:
			this.typeValue.setBizName02(name.v());
			break;

		case Biz3Rd:
			this.typeValue.setBizName03(name.v());
			break;

		case Biz4Th:
			this.typeValue.setBizName04(name.v());
			break;

		case Biz5Th:
			this.typeValue.setBizName05(name.v());
			break;

		case Biz6Th:
			this.typeValue.setBizName06(name.v());
			break;

		case Biz7Th:
			this.typeValue.setBizName07(name.v());
			break;

		case Biz8Th:
			this.typeValue.setBizName08(name.v());
			break;

		case Biz9Th:
			this.typeValue.setBizName09(name.v());
			break;

		case Biz10Th:
			this.typeValue.setBizName10(name.v());
			break;

		default:
			break;
		}
	}

}
