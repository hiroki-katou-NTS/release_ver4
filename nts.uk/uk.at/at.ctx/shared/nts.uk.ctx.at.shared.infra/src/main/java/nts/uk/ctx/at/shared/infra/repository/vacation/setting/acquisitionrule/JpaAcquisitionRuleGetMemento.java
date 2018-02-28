/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.acquisitionrule;

import java.util.ArrayList;
import java.util.List;

import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.acquisitionrule.AcquisitionOrder;
import nts.uk.ctx.at.shared.dom.vacation.setting.acquisitionrule.AcquisitionRuleGetMemento;
import nts.uk.ctx.at.shared.dom.vacation.setting.acquisitionrule.AcquisitionType;
import nts.uk.ctx.at.shared.dom.vacation.setting.acquisitionrule.AnnualHoliday;
import nts.uk.ctx.at.shared.dom.vacation.setting.acquisitionrule.HoursHoliday;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.acquisitionrule.KarstAcquisitionRule;

/**
 * The Class JpaAcquisitionRuleGetMemento.
 */
public class JpaAcquisitionRuleGetMemento implements AcquisitionRuleGetMemento {

	/** The type value. */
	private KarstAcquisitionRule typeValue;

	/**
	 * Instantiates a new jpa acquisition rule get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaAcquisitionRuleGetMemento(KarstAcquisitionRule typeValue) {
		super();
		this.typeValue = typeValue;

	}

	/**
	 * Gets the company id.
	 *
	 * @return the company id
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.vacation.setting.acquisitionrule.
	 * VaAcRuleGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return this.typeValue.getCid();
	}

	/**
	 * Gets the settingclassification.
	 *
	 * @return the settingclassification
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.vacation.setting.acquisitionrule.
	 * VaAcRuleGetMemento#getSettingclassification()
	 */
	@Override
	public ManageDistinct getCategory() {
		return ManageDistinct.valueOf(this.typeValue.getCategory());
	}

	/**
	 * Gets the acquisition order.
	 *
	 * @return the acquisition order
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.vacation.setting.acquisitionrule.
	 * VaAcRuleGetMemento#getAcquisitionOrder()
	 */
	@Override
	public List<AcquisitionOrder> getAcquisitionOrder() {
		List<AcquisitionOrder> acOrder = new ArrayList<>();

		// Add AcquisitionOrder AnnualPaidLeave.
		acOrder.add(new AcquisitionOrder(new JpaAcquisitionOrderGetMemento(this.typeValue,
				AcquisitionType.AnnualPaidLeave)));

		// Add AcquisitionOrder CompensatoryDayOff.
		acOrder.add(new AcquisitionOrder(new JpaAcquisitionOrderGetMemento(this.typeValue,
				AcquisitionType.CompensatoryDayOff)));

		// Add AcquisitionOrder SubstituteHoliday.
		acOrder.add(new AcquisitionOrder(new JpaAcquisitionOrderGetMemento(this.typeValue,
				AcquisitionType.SubstituteHoliday)));

		// Add AcquisitionOrder FundedPaidHoliday.
		acOrder.add(new AcquisitionOrder(new JpaAcquisitionOrderGetMemento(this.typeValue,
				AcquisitionType.FundedPaidHoliday)));

		// Add AcquisitionOrder ExsessHoliday.
		acOrder.add(new AcquisitionOrder(
				new JpaAcquisitionOrderGetMemento(this.typeValue, AcquisitionType.ExsessHoliday)));

		// Add AcquisitionOrder SpecialHoliday.
		acOrder.add(new AcquisitionOrder(
				new JpaAcquisitionOrderGetMemento(this.typeValue, AcquisitionType.SpecialHoliday)));

		return acOrder;
	}

	@Override
	public AnnualHoliday getAnnualHoliday() {
		
		AnnualHoliday annualHoliday = new AnnualHoliday();
		
		if(this.typeValue.getCompensatoryDayOff() == 0) {
			annualHoliday.setPriorityPause(false);
		} else {
			annualHoliday.setPriorityPause(true);
		}
		
		if(this.typeValue.getSabstituteHoliday() == 0) {
			annualHoliday.setPrioritySubstitute(false);
		} else {
			annualHoliday.setPrioritySubstitute(true);
		}
		
		if(this.typeValue.getFundedPaidHoliday() == 0) {
			annualHoliday.setSixtyHoursOverrideHoliday(false);
		} else {
			annualHoliday.setSixtyHoursOverrideHoliday(true);
		}
		
		return annualHoliday;
	}

	@Override
	public HoursHoliday getHoursHoliday() {
		
		HoursHoliday hoursHoliday = new HoursHoliday();
		
		if(this.typeValue.getExcessHoliday() == 0) {
			hoursHoliday.setPriorityOverpaid(false);
		} else {
			hoursHoliday.setPriorityOverpaid(true);
		}
		
		if(this.typeValue.getOverrideHoliday() == 0) {
			hoursHoliday.setSixtyHoursOverrideHoliday(false);
		} else {
			hoursHoliday.setSixtyHoursOverrideHoliday(true);
		}
		
		return hoursHoliday;
	}
}
