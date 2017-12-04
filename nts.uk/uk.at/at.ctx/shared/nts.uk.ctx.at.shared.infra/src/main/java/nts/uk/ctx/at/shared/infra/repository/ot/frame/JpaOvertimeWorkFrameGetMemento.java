/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.ot.frame;

import java.math.BigDecimal;

import lombok.Setter;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameGetMemento;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameName;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameNo;
import nts.uk.ctx.at.shared.infra.entity.ot.frame.KshstOvertimeFrame;

/**
 * The Class JpaOvertimeWorkFrameGetMemento.
 */
public class JpaOvertimeWorkFrameGetMemento implements OvertimeWorkFrameGetMemento{
	
	/** The kshst overtime frame. */
	
	/**
	 * Sets the kscst plan year hd frame.
	 *
	 * @param kscstPlanYearHdFrame the new kscst plan year hd frame
	 */
	
	/**
	 * Sets the kshst overtime frame.
	 *
	 * @param kshstOvertimeFrame the new kshst overtime frame
	 */
	@Setter
	private KshstOvertimeFrame kshstOvertimeFrame;
	
	/**
	 * Instantiates a new jpa overtime work frame get memento.
	 *
	 * @param kshstOvertimeFrame the kshst overtime frame
	 */
	public JpaOvertimeWorkFrameGetMemento(KshstOvertimeFrame kshstOvertimeFrame) {
		this.kshstOvertimeFrame = kshstOvertimeFrame;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.classification.ClassificationGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return this.kshstOvertimeFrame.getKshstOvertimeFramePK().getCid();
	}


	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.plannedyearholiday.frame.PlanYearHolidayFrameGetMemento#getUseClassification()
	 */
	@Override
	public NotUseAtr getUseClassification() {
		return NotUseAtr.valueOf((int) this.kshstOvertimeFrame.getUseAtr());
	}


	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameGetMemento#getOvertimeWorkFrameNo()
	 */
	@Override
	public OvertimeWorkFrameNo getOvertimeWorkFrameNo() {
		return new OvertimeWorkFrameNo(BigDecimal.valueOf(this.kshstOvertimeFrame.getKshstOvertimeFramePK().getOtFrNo()));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameGetMemento#getTransferFrameName()
	 */
	@Override
	public OvertimeWorkFrameName getTransferFrameName() {
		return new OvertimeWorkFrameName(this.kshstOvertimeFrame.getTransFrName());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameGetMemento#getOvertimeWorkFrameName()
	 */
	@Override
	public OvertimeWorkFrameName getOvertimeWorkFrameName() {
		return new OvertimeWorkFrameName(this.kshstOvertimeFrame.getOtFrName());
	}
	
}
