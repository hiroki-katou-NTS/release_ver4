/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.unemployeerate;

import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.CareerGroup;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItemGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItemSetting;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate.QismtEmpInsuRate;

/**
 * The Class JpaUnemployeeInsuranceRateItemGetMemento.
 */
public class JpaUnemployeeInsuranceRateItemGetMemento
	implements UnemployeeInsuranceRateItemGetMemento {

	/** The type value. */
	private QismtEmpInsuRate typeValue;

	/** The career group. */
	private CareerGroup careerGroup;

	/**
	 * Instantiates a new jpa unemployee insurance rate item get memento.
	 *
	 * @param typeValue
	 *            the type value
	 * @param careerGroup
	 *            the career group
	 */
	public JpaUnemployeeInsuranceRateItemGetMemento(QismtEmpInsuRate typeValue,
		CareerGroup careerGroup) {
		this.typeValue = typeValue;
		this.careerGroup = careerGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemGetMemento#getCareerGroup()
	 */
	@Override
	public CareerGroup getCareerGroup() {
		return this.careerGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemGetMemento#getCompanySetting()
	 */
	@Override
	public UnemployeeInsuranceRateItemSetting getCompanySetting() {
		UnemployeeInsuranceRateItemSetting unemployeeInsuranceRateItemSetting;

		unemployeeInsuranceRateItemSetting = new UnemployeeInsuranceRateItemSetting();
		switch (this.careerGroup) {

		case Agroforestry:
			unemployeeInsuranceRateItemSetting
				.setRate(this.typeValue.getCEmpRateGeneral().doubleValue());
			unemployeeInsuranceRateItemSetting
				.setRoundAtr(RoundingMethod.valueOf(this.typeValue.getCEmpRoundGeneral()));
			break;

		case Other:
			unemployeeInsuranceRateItemSetting
				.setRate(this.typeValue.getCEmpRateOther().doubleValue());
			unemployeeInsuranceRateItemSetting
				.setRoundAtr(RoundingMethod.valueOf(this.typeValue.getCEmpRoundOther()));
			break;

		case Contruction:
			unemployeeInsuranceRateItemSetting
				.setRate(this.typeValue.getCEmpRateConst().doubleValue());
			unemployeeInsuranceRateItemSetting
				.setRoundAtr(RoundingMethod.valueOf(this.typeValue.getCEmpRoundConst()));
			break;
		default:
			break;
		}
		return unemployeeInsuranceRateItemSetting;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemGetMemento#getPersonalSetting()
	 */
	@Override
	public UnemployeeInsuranceRateItemSetting getPersonalSetting() {
		UnemployeeInsuranceRateItemSetting unemployeeInsuranceRateItemSetting;

		unemployeeInsuranceRateItemSetting = new UnemployeeInsuranceRateItemSetting();
		switch (this.careerGroup) {
		case Agroforestry:
			unemployeeInsuranceRateItemSetting
				.setRate(this.typeValue.getPEmpRateGeneral().doubleValue());
			unemployeeInsuranceRateItemSetting
				.setRoundAtr(RoundingMethod.valueOf(this.typeValue.getPEmpRoundGeneral()));
			break;

		case Other:
			unemployeeInsuranceRateItemSetting
				.setRate(this.typeValue.getPEmpRateOther().doubleValue());
			unemployeeInsuranceRateItemSetting
				.setRoundAtr(RoundingMethod.valueOf(this.typeValue.getPEmpRoundOther()));
			break;

		case Contruction:
			unemployeeInsuranceRateItemSetting
				.setRate(this.typeValue.getPEmpRateConst().doubleValue());
			unemployeeInsuranceRateItemSetting
				.setRoundAtr(RoundingMethod.valueOf(this.typeValue.getPEmpRoundConst()));
			break;
		default:
			break;
		}
		return unemployeeInsuranceRateItemSetting;
	}

}
