package nts.uk.ctx.at.record.app.find.divergence.time;

import java.util.List;

import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodSetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelect;

/**
 * The Class DivergenceReasonInputMethodDto.
 */
public class DivergenceReasonInputMethodDto
		implements DivergenceReasonInputMethodSetMemento, DivergenceReasonInputMethodGetMemento {

	/** The divergence time no. */

	private int divergenceTimeNo;

	/** The companyId. */

	private String companyId;

	/** The divergence reason inputed. */

	private boolean divergenceReasonInputed;

	/** The divergence reason selected. */

	private boolean divergenceReasonSelected;

	/** The reason list. */

	private List<DivergenceReasonSelect> reasons;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodSetMemento#setDivergenceTimeNo(int)
	 */
	@Override
	public void setDivergenceTimeNo(int DivergenceTimeNo) {
		this.divergenceTimeNo = DivergenceTimeNo;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		this.companyId = companyId;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodSetMemento#setDivergenceReasonInputed(boolean)
	 */
	@Override
	public void setDivergenceReasonInputed(boolean divergenceReasonInputed) {
		this.divergenceReasonInputed = divergenceReasonInputed;

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
		this.divergenceReasonSelected = divergenceReasonSelected;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodSetMemento#setReasons(java.util.List)
	 */
	@Override
	public void setReasons(List<DivergenceReasonSelect> reasons) {
		this.reasons = reasons;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodGetMemento#getDivergenceReasonInputed()
	 */
	@Override
	public boolean getDivergenceReasonInputed() {
		return divergenceReasonInputed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodGetMemento#getDivergenceReasonSelected()
	 */
	@Override
	public boolean getDivergenceReasonSelected() {
		return divergenceReasonSelected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodGetMemento#getDivergenceTimeNo()
	 */
	@Override
	public int getDivergenceTimeNo() {
		return divergenceTimeNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return companyId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodGetMemento#getReasons()
	 */
	@Override
	public List<DivergenceReasonSelect> getReasons() {
		return reasons;
	}

}
