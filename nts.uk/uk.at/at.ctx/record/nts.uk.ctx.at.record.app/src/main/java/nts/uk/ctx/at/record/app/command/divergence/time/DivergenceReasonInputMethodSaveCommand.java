package nts.uk.ctx.at.record.app.command.divergence.time;

import java.util.List;

import lombok.AllArgsConstructor;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelect;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceReasonInputMethodSaveCommand.
 */
@AllArgsConstructor
public class DivergenceReasonInputMethodSaveCommand implements DivergenceReasonInputMethodGetMemento {

	/** The divergence time no. */
	private int divergenceTimeNo;

	/** The divergence reason inputed. */
	private boolean divergenceReasonInputed;

	/** The divergence reason selected. */
	private boolean divergenceReasonSelected;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodGetMemento#getDivergenceTimeNo()
	 */
	@Override
	public int getDivergenceTimeNo() {
		return this.divergenceTimeNo;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return AppContexts.user().companyId();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodGetMemento#getDivergenceReasonInputed()
	 */
	@Override
	public boolean getDivergenceReasonInputed() {
		return this.divergenceReasonInputed;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodGetMemento#getDivergenceReasonSelected()
	 */
	@Override
	public boolean getDivergenceReasonSelected() {
		return this.divergenceReasonSelected;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodGetMemento#getReasons()
	 */
	@Override
	public List<DivergenceReasonSelect> getReasons() {
		return null;
	}

}
