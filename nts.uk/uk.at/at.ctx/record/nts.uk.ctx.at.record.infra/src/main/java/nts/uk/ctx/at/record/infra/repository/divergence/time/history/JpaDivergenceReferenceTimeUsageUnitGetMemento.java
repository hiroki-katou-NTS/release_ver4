package nts.uk.ctx.at.record.infra.repository.divergence.time.history;

import java.math.BigDecimal;

import lombok.Setter;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeUsageUnitGetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDrtUseUnit;

/**
 * The Class JpaDivergenceReferenceTimeUsageUnitGetMemento.
 */
public class JpaDivergenceReferenceTimeUsageUnitGetMemento implements DivergenceReferenceTimeUsageUnitGetMemento {

	/** The entity. */
	@Setter
	private KrcstDrtUseUnit krcstDrtUseUnit;

	/**
	 * Instantiates a new jpa divergence reference time usage unit get memento.
	 */
	public JpaDivergenceReferenceTimeUsageUnitGetMemento() {

	}

	/**
	 * Instantiates a new jpa divergence reference time usage unit get memento.
	 *
	 * @param krcstDrtUseUnit the krcst drt use unit
	 */
	public JpaDivergenceReferenceTimeUsageUnitGetMemento(KrcstDrtUseUnit krcstDrtUseUnit) {
		this.krcstDrtUseUnit = krcstDrtUseUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * DivergenceReferenceTimeUsageUnitGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		// TODO Auto-generated method stub
		return this.krcstDrtUseUnit.getCid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * DivergenceReferenceTimeUsageUnitGetMemento#getWorkTypeUseSet()
	 */
	@Override
	public BigDecimal getWorkTypeUseSet() {
		// TODO Auto-generated method stub
		return this.krcstDrtUseUnit.getWorktypeUseSet();
	}

}
