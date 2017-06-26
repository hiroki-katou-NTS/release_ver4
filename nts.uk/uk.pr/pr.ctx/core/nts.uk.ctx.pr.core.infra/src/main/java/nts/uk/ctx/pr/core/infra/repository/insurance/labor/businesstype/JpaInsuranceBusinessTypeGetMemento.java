/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.businesstype;

import nts.uk.ctx.pr.core.dom.insurance.BusinessName;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.InsuranceBusinessTypeGetMemento;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.businesstype.QismtBusinessType;

/**
 * The Class JpaInsuranceBusinessTypeGetMemento.
 */
public class JpaInsuranceBusinessTypeGetMemento implements InsuranceBusinessTypeGetMemento {

	/** The type value. */
	private QismtBusinessType typeValue;

	/** The type. */
	private BusinessTypeEnum type;

	/**
	 * Instantiates a new jpa insurance business type get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaInsuranceBusinessTypeGetMemento(QismtBusinessType typeValue, BusinessTypeEnum type) {
		this.typeValue = typeValue;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.
	 * InsuranceBusinessTypeGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.
	 * InsuranceBusinessTypeGetMemento#getBizOrder()
	 */
	@Override
	public BusinessTypeEnum getBizOrder() {
		return this.type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.
	 * InsuranceBusinessTypeGetMemento#getBizName()
	 */
	@Override
	public BusinessName getBizName() {
		String bizName = null;
		// Check type
		switch (this.type) {
		case Biz1St:
			bizName = this.typeValue.getBizName01();
			break;

		case Biz2Nd:
			bizName = this.typeValue.getBizName02();
			break;

		case Biz3Rd:
			bizName = this.typeValue.getBizName03();
			break;

		case Biz4Th:
			bizName = this.typeValue.getBizName04();
			break;

		case Biz5Th:
			bizName = this.typeValue.getBizName05();
			break;

		case Biz6Th:
			bizName = this.typeValue.getBizName06();
			break;

		case Biz7Th:
			bizName = this.typeValue.getBizName07();
			break;

		case Biz8Th:
			bizName = this.typeValue.getBizName08();
			break;

		case Biz9Th:
			bizName = this.typeValue.getBizName09();
			break;

		case Biz10Th:
			bizName = this.typeValue.getBizName10();
			break;

		default:
			break;
		}

		// Return
		return new BusinessName(bizName);
	}

}
