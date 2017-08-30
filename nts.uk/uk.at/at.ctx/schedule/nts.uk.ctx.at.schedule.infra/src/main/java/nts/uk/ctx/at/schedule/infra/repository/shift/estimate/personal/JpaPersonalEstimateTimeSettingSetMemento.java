/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.estimate.personal;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.shift.estimate.EstimateTargetClassification;
import nts.uk.ctx.at.schedule.dom.shift.estimate.time.EstimateTimeSettingSetMemento;
import nts.uk.ctx.at.schedule.dom.shift.estimate.time.MonthlyEstimateTimeSetting;
import nts.uk.ctx.at.schedule.dom.shift.estimate.time.YearlyEstimateTimeSetting;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.personal.KscmtEstTimePerSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.personal.KscmtEstTimePerSetPK;

/**
 * The Class JpaEstimateTimeSettingPersonalGetMemento.
 */
public class JpaPersonalEstimateTimeSettingSetMemento implements EstimateTimeSettingSetMemento{
	
	/** The est time Personal. */
	private KscmtEstTimePerSet estTimePersonal;
	
	/**
	 * Instantiates a new jpa estimate time setting Personal get memento.
	 *
	 * @param estTimePersonal the est time Personal
	 */
	public JpaPersonalEstimateTimeSettingSetMemento(KscmtEstTimePerSet estTimePersonal){
		if(estTimePersonal.getKscmtEstTimePerSetPK() == null){
			estTimePersonal.setKscmtEstTimePerSetPK(new KscmtEstTimePerSetPK());
		}
		this.estTimePersonal = estTimePersonal;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.time.
	 * EstimateTimeSettingSetMemento#setTargetClassification(nts.uk.ctx.at.
	 * schedule.dom.shift.estimate.EstimateTargetClassification)
	 */
	@Override
	public void setTargetClassification(EstimateTargetClassification targetClassification) {
		this.estTimePersonal.getKscmtEstTimePerSetPK().setTargetCls(targetClassification.value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.time.
	 * EstimateTimeSettingSetMemento#setYearlyEstimateTimeSetting(java.util.
	 * List)
	 */
	@Override
	public void setYearlyEstimateTimeSetting(
			List<YearlyEstimateTimeSetting> yearlyEstimateTimeSetting) {
		if (this.estTimePersonal.getKscmtEstTimePerSetPK()
				.getTargetCls() == EstimateTargetClassification.YEARLY.value) {

			
			yearlyEstimateTimeSetting.forEach(yearly->{
				switch (yearly.getEstimatedCondition()) {
				case CONDITION_1ST:
					this.estTimePersonal.setEstCondition1stTime(new Double(yearly.getTime().v()).intValue());
					break;
				case CONDITION_2ND:
					this.estTimePersonal.setEstCondition2ndTime(new Double(yearly.getTime().v()).intValue());
					break;
				case CONDITION_3RD:
					this.estTimePersonal.setEstCondition3rdTime(new Double(yearly.getTime().v()).intValue());
					break;
				case CONDITION_4TH:
					this.estTimePersonal.setEstCondition4thTime(new Double(yearly.getTime().v()).intValue());
					break;
				case CONDITION_5TH:
					this.estTimePersonal.setEstCondition5thTime(new Double(yearly.getTime().v()).intValue());
					break;

				default:
					break;
				}
			});
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.time.
	 * EstimateTimeSettingSetMemento#setMonthlyEstimateTimeSetting(java.util.
	 * List)
	 */
	@Override
	public void setMonthlyEstimateTimeSetting(
			List<MonthlyEstimateTimeSetting> monthlyEstimateTimeSetting) {
		
		// check target class not yearly
		if (this.estTimePersonal.getKscmtEstTimePerSetPK()
				.getTargetCls() != EstimateTargetClassification.YEARLY.value) {

			
			monthlyEstimateTimeSetting.forEach(monthly->{
				switch (monthly.getEstimatedCondition()) {
				case CONDITION_1ST:
					this.estTimePersonal.setEstCondition1stTime(new Double(monthly.getTime().v()).intValue());
					break;
				case CONDITION_2ND:
					this.estTimePersonal.setEstCondition2ndTime(new Double(monthly.getTime().v()).intValue());
					break;
				case CONDITION_3RD:
					this.estTimePersonal.setEstCondition3rdTime(new Double(monthly.getTime().v()).intValue());
					break;
				case CONDITION_4TH:
					this.estTimePersonal.setEstCondition4thTime(new Double(monthly.getTime().v()).intValue());
					break;
				case CONDITION_5TH:
					this.estTimePersonal.setEstCondition5thTime(new Double(monthly.getTime().v()).intValue());
					break;

				default:
					break;
				}
			});
		}
		
	}
	

}
