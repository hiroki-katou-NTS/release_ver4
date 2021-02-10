package nts.uk.ctx.at.record.infra.repository.divergence.time;

import java.math.BigDecimal;
import java.util.List;

import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodSetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelect;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcmtDvgcTime;

/**
 * The Class JpaDivergenceReasonInputMethodRepositorySetMemento.
 */
public class JpaDivergenceReasonInputMethodSetMemento implements DivergenceReasonInputMethodSetMemento {

	/** The entities. */
	private KrcmtDvgcTime entity;

	/**
	 * Instantiates a new jpa divergence time repository set memento.
	 */
	public JpaDivergenceReasonInputMethodSetMemento() {

	}

	/**
	 * Instantiates a new jpa divergence time repository set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaDivergenceReasonInputMethodSetMemento(KrcmtDvgcTime entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodSetMemento#setDivergenceTimeNo(int)
	 */
	@Override
	public void setDivergenceTimeNo(int DivergenceTimeNo) {

		this.entity.getId().setNo(DivergenceTimeNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		// No code

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodSetMemento#setDivergenceReasonInputed(boolean)
	 */
	@Override
	public void setDivergenceReasonInputed(boolean divergenceReasonInputed) {

		this.entity.setDvgcReasonInputed(divergenceReasonInputed ? BigDecimal.ONE : BigDecimal.ZERO);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodSetMemento#setDivergenceReasonSelected(
	 * boolean)
	 */
	@Override
	public void setDivergenceReasonSelected(boolean divergenceReasonSelected) {

		this.entity.setDvgcReasonSelected(divergenceReasonSelected ? BigDecimal.ONE : BigDecimal.ZERO);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodSetMemento#setReasons(java.util.List)
	 */
	@Override
	public void setReasons(List<DivergenceReasonSelect> reason) {
		// no code

	}

}
