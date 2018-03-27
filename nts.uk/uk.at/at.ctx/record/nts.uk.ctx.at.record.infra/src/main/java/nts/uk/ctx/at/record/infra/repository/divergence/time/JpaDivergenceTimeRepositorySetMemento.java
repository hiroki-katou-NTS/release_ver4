package nts.uk.ctx.at.record.infra.repository.divergence.time;

import java.math.BigDecimal;
import java.util.List;

import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeErrorCancelMethod;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeName;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeUseSet;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceType;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTime;

/**
 * The Class JpaDivergenceTimeRepositorySetMemento.
 */
public class JpaDivergenceTimeRepositorySetMemento implements DivergenceTimeSetMemento {

	/** The entity. */
	private KrcstDvgcTime entity;

	/**
	 * Instantiates a new jpa divergence time repository set memento.
	 */
	public JpaDivergenceTimeRepositorySetMemento() {

	}

	/**
	 * Instantiates a new jpa divergence time repository set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaDivergenceTimeRepositorySetMemento(KrcstDvgcTime entity) {
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setDivergenceTimeNo(int)
	 */
	@Override
	public void setDivergenceTimeNo(int DivergenceTimeNo) {
		this.entity.getId().setNo(DivergenceTimeNo);
		;

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String CompanyId) {
		// No coding

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setDivTimeUseSet(nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeUseSet)
	 */
	@Override
	public void setDivTimeUseSet(DivergenceTimeUseSet divTimeUset) {
		this.entity.setDvgcTimeUseSet(new BigDecimal(divTimeUset.value));

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setDivTimeName(nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeName)
	 */
	@Override
	public void setDivTimeName(DivergenceTimeName divTimeName) {
		this.entity.setDvgcTimeName(divTimeName.toString());

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setDivType(nts.uk.ctx.at.record.dom.divergence.time.DivergenceType)
	 */
	@Override
	public void setDivType(DivergenceType divType) {
		this.entity.setDvgcType(new BigDecimal(divType.value));

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setErrorCancelMedthod(nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeErrorCancelMethod)
	 */
	@Override
	public void setErrorCancelMedthod(DivergenceTimeErrorCancelMethod errorCancelMedthod) {

		this.entity
				.setReasonInputCanceled(errorCancelMedthod.isReasonInputed() ? new BigDecimal(1) : new BigDecimal(0));

		this.entity
				.setReasonSelectCanceled(errorCancelMedthod.isReasonSelected() ? new BigDecimal(1) : new BigDecimal(0));

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setTarsetItems(java.util.List)
	 */
	@Override
	public void setTarsetItems(List<Double> targetItems) {
		// no coding

	}

}
