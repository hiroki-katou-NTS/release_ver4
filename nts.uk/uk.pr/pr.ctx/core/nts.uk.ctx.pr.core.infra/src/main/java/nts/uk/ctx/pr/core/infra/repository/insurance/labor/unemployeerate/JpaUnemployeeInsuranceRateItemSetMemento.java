/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.unemployeerate;

import java.math.BigDecimal;

import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.CareerGroup;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItemSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItemSetting;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate.QismtEmpInsuRate;

/**
 * The Class JpaUnemployeeInsuranceRateItemSetMemento.
 */
public class JpaUnemployeeInsuranceRateItemSetMemento
	implements UnemployeeInsuranceRateItemSetMemento {

	/** The type value. */
	private QismtEmpInsuRate typeValue;

	/** The career group. */
	private CareerGroup careerGroup;

	/**
	 * Instantiates a new jpa unemployee insurance rate item set memento.
	 *
	 * @param typeValue
	 *            the type value
	 * @param careerGroup
	 *            the career group
	 */
	public JpaUnemployeeInsuranceRateItemSetMemento(QismtEmpInsuRate typeValue,
		CareerGroup careerGroup) {
		this.typeValue = typeValue;
		this.careerGroup = careerGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetMemento#setCareerGroup(nts.uk.ctx.pr.core.
	 * dom.insurance.labor.unemployeerate.CareerGroup)
	 */
	@Override
	public void setCareerGroup(CareerGroup careerGroup) {
		// None
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetMemento#setCompanySetting(nts.uk.ctx.pr.
	 * core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetting)
	 */
	@Override
	public void setCompanySetting(UnemployeeInsuranceRateItemSetting companySetting) {
		switch (this.careerGroup) {

		case Agroforestry:
			this.typeValue
				.setCEmpRateGeneral(new BigDecimal(String.valueOf(companySetting.getRate())));
			this.typeValue.setCEmpRoundGeneral(companySetting.getRoundAtr().value);
			break;

		case Other:
			this.typeValue
				.setCEmpRateOther(new BigDecimal(String.valueOf(companySetting.getRate())));
			this.typeValue.setCEmpRoundOther(companySetting.getRoundAtr().value);
			break;

		case Contruction:
			this.typeValue
				.setCEmpRateConst(new BigDecimal(String.valueOf(companySetting.getRate())));
			this.typeValue.setCEmpRoundConst(companySetting.getRoundAtr().value);
			break;

		default:
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetMemento#setPersonalSetting(nts.uk.ctx.pr.
	 * core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetting)
	 */
	@Override
	public void setPersonalSetting(UnemployeeInsuranceRateItemSetting personalSetting) {
		switch (this.careerGroup) {

		case Agroforestry:
			this.typeValue
				.setPEmpRateGeneral(new BigDecimal(String.valueOf(personalSetting.getRate())));
			this.typeValue.setPEmpRoundGeneral(personalSetting.getRoundAtr().value);
			break;

		case Other:
			this.typeValue
				.setPEmpRateOther(new BigDecimal(String.valueOf(personalSetting.getRate())));
			this.typeValue.setPEmpRoundOther(personalSetting.getRoundAtr().value);
			break;

		case Contruction:
			this.typeValue
				.setPEmpRateConst(new BigDecimal(String.valueOf(personalSetting.getRate())));
			this.typeValue.setPEmpRoundConst(personalSetting.getRoundAtr().value);
			break;

		default:
			break;

		}
	}

}
