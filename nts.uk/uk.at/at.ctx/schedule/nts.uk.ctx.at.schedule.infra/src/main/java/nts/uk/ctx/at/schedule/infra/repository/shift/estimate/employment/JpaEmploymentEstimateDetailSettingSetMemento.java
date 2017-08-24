/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.estimate.employment;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.shift.estimate.EstimateDetailSettingSetMemento;
import nts.uk.ctx.at.schedule.dom.shift.estimate.numberofday.EstimateNumberOfDay;
import nts.uk.ctx.at.schedule.dom.shift.estimate.price.EstimatedPriceSetting;
import nts.uk.ctx.at.schedule.dom.shift.estimate.time.EstimateTimeSetting;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.employment.KscmtEstDaysEmpSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.employment.KscmtEstPriceEmpSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.employment.KscmtEstTimeEmpSet;

/**
 * The Class JpaEstimateDetailSettingEmploymentSetMemento.
 */
public class JpaEmploymentEstimateDetailSettingSetMemento implements EstimateDetailSettingSetMemento{
	
	/** The estimate time Employments. */
	private List<KscmtEstTimeEmpSet> estimateTimeEmployments;
	
	/** The estimate price Employments. */
	private List<KscmtEstPriceEmpSet> estimatePriceEmployments;
	
	/** The estimate days Employments. */
	private List<KscmtEstDaysEmpSet> estimateDaysEmployments;

	
	/**
	 * Instantiates a new jpa estimate detail setting Employment set memento.
	 *
	 * @param estimateTimeEmployments the estimate time Employments
	 * @param estimatePriceEmployments the estimate price Employments
	 */
	public JpaEmploymentEstimateDetailSettingSetMemento(List<KscmtEstTimeEmpSet> estimateTimeEmployments,
			List<KscmtEstPriceEmpSet> estimatePriceEmployments,List<KscmtEstDaysEmpSet> estimateDaysEmployments) {
		this.estimateTimeEmployments = estimateTimeEmployments;
		this.estimatePriceEmployments = estimatePriceEmployments;
		this.estimateDaysEmployments = estimateDaysEmployments;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.shift.estimate.EstimateDetailSettingSetMemento
	 * #setEstimateTime(java.util.List)
	 */
	@Override
	public void setEstimateTime(List<EstimateTimeSetting> estimateTime) {

		estimateTime.forEach(estimateSetting -> {
			this.estimateTimeEmployments.forEach(entity -> {
				if (entity.getKscmtEstTimeEmpSetPK()
						.getTargetCls() == estimateSetting.getTargetClassification().value) {
					estimateSetting
							.saveToMemento(new JpaEmploymentEstimateTimeSettingSetMemento(entity));
				}
			});
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.shift.estimate.EstimateDetailSettingSetMemento
	 * #setEstimatePrice(java.util.List)
	 */
	@Override
	public void setEstimatePrice(List<EstimatedPriceSetting> estimatePrice) {
		estimatePrice.forEach(estimateSetting -> {
			this.estimatePriceEmployments.forEach(entity -> {
				if (entity.getKscmtEstPriceEmpSetPK()
						.getTargetCls() == estimateSetting.getTargetClassification().value) {
					estimateSetting.saveToMemento(new JpaEmploymentEstimatedPriceSetMemento(entity));
				}
			});
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.shift.estimate.EstimateDetailSettingSetMemento
	 * #setEstimateNumberOfDay(java.util.List)
	 */
	@Override
	public void setEstimateNumberOfDay(List<EstimateNumberOfDay> estimateNumberOfDay) {
		estimateNumberOfDay.forEach(estimateSetting -> {
			this.estimateDaysEmployments.forEach(entity -> {
				if (entity.getKscmtEstDaysEmpSetPK()
						.getTargetCls() == estimateSetting.getTargetClassification().value) {
					estimateSetting
							.saveToMemento(new JpaEmploymentEstimateNumberOfDaySetMemento(entity));
				}
			});
		});

	}
}
