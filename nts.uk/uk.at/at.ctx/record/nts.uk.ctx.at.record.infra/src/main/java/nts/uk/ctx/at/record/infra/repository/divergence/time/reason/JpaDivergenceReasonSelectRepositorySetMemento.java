package nts.uk.ctx.at.record.infra.repository.divergence.time.reason;

import java.math.BigDecimal;

import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceInputRequired;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReason;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonCode;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelectSetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.reason.KrcstDvgcReason;
import nts.uk.ctx.at.record.infra.entity.divergence.reason.KrcstDvgcReasonPK;

/**
 * The Class JpaDivergenceReasonSelectRepositorySetMemento.
 */
public class JpaDivergenceReasonSelectRepositorySetMemento implements DivergenceReasonSelectSetMemento {

	/** The entity. */
	private KrcstDvgcReason entity;

	/**
	 * Instantiates a new jpa divergence reason select repository set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaDivergenceReasonSelectRepositorySetMemento(KrcstDvgcReason entity) {
		if (entity.getId() == null) {
			KrcstDvgcReasonPK PK = new KrcstDvgcReasonPK();
			entity.setId(PK);
		}

		this.entity = entity;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.reason.
	 * DivergenceReasonSelectSetMemento#setDivergenceReasonCode(nts.uk.ctx.at.
	 * record.dom.divergence.time.reason.DivergenceReasonCode)
	 */
	@Override
	public void setDivergenceReasonCode(DivergenceReasonCode divergenceReasonCode) {
		this.entity.getId().setReasonCode(divergenceReasonCode.toString());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.reason.
	 * DivergenceReasonSelectSetMemento#setReason(nts.uk.ctx.at.record.dom.
	 * divergence.time.reason.DivergenceReason)
	 */
	@Override
	public void setReason(DivergenceReason reason) {
		this.entity.setReason(reason.toString());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.reason.
	 * DivergenceReasonSelectSetMemento#setReasonRequired(nts.uk.ctx.at.record.
	 * dom.divergence.time.reason.DivergenceInputRequired)
	 */
	@Override
	public void setReasonRequired(DivergenceInputRequired reasonRequired) {
		this.entity.setReasonRequired(new BigDecimal(reasonRequired.value));
	}
}
