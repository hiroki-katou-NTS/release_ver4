package nts.uk.ctx.bs.employee.infra.repository.jobtitle;

import nts.uk.ctx.bs.employee.dom.jobtitle.history.HistoryId;
import nts.uk.ctx.bs.employee.dom.jobtitle.history.JobTitleHistoryGetMemento;
import nts.uk.ctx.bs.employee.dom.jobtitle.history.Period;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHist;

/**
 * The Class JpaJobTitleHistoryGetMemento.
 */
public class JpaJobTitleHistoryGetMemento implements JobTitleHistoryGetMemento {

	/** The entity. */
	private BsymtJobHist entity;
	
	/**
	 * Instantiates a new jpa job title history get memento.
	 *
	 * @param item the item
	 */
	public JpaJobTitleHistoryGetMemento(BsymtJobHist entity) {
		this.entity = entity;
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleHistoryGetMemento#getHistoryId()
	 */
	@Override
	public HistoryId getHistoryId() {
		return new HistoryId(this.entity.getBsymtJobHistPK().getHistId());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleHistoryGetMemento#getPeriod()
	 */
	@Override
	public Period getPeriod() {
		return new Period(this.entity.getStartDate(), this.entity.getEndDate());
	}

}
