/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.estimate.employment;

import java.util.ArrayList;
import java.util.List;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.schedule.dom.shift.estimate.EstimateTargetClassification;
import nts.uk.ctx.at.schedule.dom.shift.estimate.EstimatedCondition;
import nts.uk.ctx.at.schedule.dom.shift.estimate.price.EstimatePrice;
import nts.uk.ctx.at.schedule.dom.shift.estimate.price.EstimatedPrice;
import nts.uk.ctx.at.schedule.dom.shift.estimate.price.EstimatedPriceSettingGetMemento;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.employment.KscmtEstPriceEmpSet;

/**
 * The Class JpaEstimatedEmploymentPriceGetMemento.
 */
public class JpaEmploymentEstimatedPriceGetMemento implements  EstimatedPriceSettingGetMemento{
	
	/** The estimate price Employment. */
	private KscmtEstPriceEmpSet estimatePriceEmployment;
	
	/**
	 * Instantiates a new jpa estimated Employment price get memento.
	 *
	 * @param estimatePriceEmployment the estimate price Employment
	 */
	public JpaEmploymentEstimatedPriceGetMemento(KscmtEstPriceEmpSet estimatePriceEmployment) {
		this.estimatePriceEmployment = estimatePriceEmployment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.price.
	 * EstimatedPriceSettingGetMemento#getTargetClassification()
	 */
	@Override
	public EstimateTargetClassification getTargetClassification() {
		return EnumAdaptor.valueOf(
				this.estimatePriceEmployment.getKscmtEstPriceEmpSetPK().getTargetCls(),
				EstimateTargetClassification.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.price.
	 * EstimatedPriceSettingGetMemento#getPriceSetting()
	 */
	@Override
	public List<EstimatedPrice> getPriceSetting() {
		List<EstimatedPrice> estimatedPrice = new ArrayList<>();
		estimatedPrice.add(new EstimatedPrice(EstimatedCondition.CONDITION_1ST,
				new EstimatePrice(this.estimatePriceEmployment.getEstCondition1stMny())));
		estimatedPrice.add(new EstimatedPrice(EstimatedCondition.CONDITION_2ND,
				new EstimatePrice(this.estimatePriceEmployment.getEstCondition2ndMny())));
		estimatedPrice.add(new EstimatedPrice(EstimatedCondition.CONDITION_3RD,
				new EstimatePrice(this.estimatePriceEmployment.getEstCondition3rdMny())));
		estimatedPrice.add(new EstimatedPrice(EstimatedCondition.CONDITION_4TH,
				new EstimatePrice(this.estimatePriceEmployment.getEstCondition4thMny())));
		estimatedPrice.add(new EstimatedPrice(EstimatedCondition.CONDITION_5TH,
				new EstimatePrice(this.estimatePriceEmployment.getEstCondition5thMny())));
		return estimatedPrice;
	}
	
}
