package nts.uk.ctx.at.record.app.find.divergence.time;

import java.util.List;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeErrorCancelMethod;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeName;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeUseSet;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceType;

/**
 * Checks if is reason select.
 *
 * @return true, if is reason select
 */
@Getter
public class DivergenceTimeDto implements DivergenceTimeSetMemento {

	/** The divergence time no. */
	private int divergenceTimeNo;

	/** The Use classification. */
	private int divergenceTimeUseSet;

	/** The divergence time name. */
	private String divergenceTimeName;

	/** The divergence type. */
	private int divType;

	/** The divergence time error cancel method. */
	private boolean reasonInput;

	/** The reason select. */
	private boolean reasonSelect;

	/**
	 * Instantiates a new divergence time dto.
	 */
	public DivergenceTimeDto() {
		super();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setDivergenceTimeNo(int)
	 */
	@Override
	public void setDivergenceTimeNo(int divergenceTimeNo) {
		this.divergenceTimeNo = divergenceTimeNo;

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		// no coding

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setDivTimeUseSet(nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeUseSet)
	 */
	@Override
	public void setDivTimeUseSet(DivergenceTimeUseSet divTimeUset) {
		this.divergenceTimeUseSet = divTimeUset.value;

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setDivTimeName(nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeName)
	 */
	@Override
	public void setDivTimeName(DivergenceTimeName divTimeName) {
		this.divergenceTimeName = divTimeName.toString();

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setDivType(nts.uk.ctx.at.record.dom.divergence.time.DivergenceType)
	 */
	@Override
	public void setDivType(DivergenceType divType) {
		this.divType = divType.value;

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setErrorCancelMedthod(nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeErrorCancelMethod)
	 */
	@Override
	public void setErrorCancelMedthod(DivergenceTimeErrorCancelMethod errorCancelMedthod) {
		this.reasonInput = errorCancelMedthod.isReasonInputed();
		this.reasonSelect = errorCancelMedthod.isReasonSelected();

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeSetMemento#setTarsetItems(java.util.List)
	 */
	@Override
	public void setTarsetItems(List<Double> targetItems) {
		// no code

	}

}
