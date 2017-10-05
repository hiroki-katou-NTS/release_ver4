package nts.uk.ctx.bs.employee.infra.repository.jobtitle;

import nts.uk.ctx.bs.employee.dom.jobtitle.history.HistoryId;
import nts.uk.ctx.bs.employee.dom.jobtitle.history.JobTitleHistorySetMemento;
import nts.uk.ctx.bs.employee.dom.jobtitle.history.Period;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHist;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHistPK;

/**
 * The Class JpaJobTitleHistorySetMemento.
 */
public class JpaJobTitleHistorySetMemento implements JobTitleHistorySetMemento {
	
	/** The entity. */
	private BsymtJobHist entity;
	
	/**
	 * Instantiates a new jpa job title history set memento.
	 *
	 * @param entity the entity
	 */
	public JpaJobTitleHistorySetMemento(BsymtJobHist entity) {
		this.entity = entity;
	}
	
	/**
	 * Sets the history id.
	 *
	 * @param historyId the new history id
	 */
	@Override
	public void setHistoryId(HistoryId historyId) {
		BsymtJobHistPK pk = new BsymtJobHistPK();
		pk.setHistId(historyId.v());
		this.entity.setBsymtJobHistPK(pk);
	}

	/**
	 * Sets the period.
	 *
	 * @param period the new period
	 */
	@Override
	public void setPeriod(Period period) {
		this.entity.setStartDate(period.getStartDate());
		this.entity.setEndDate(period.getEndDate());
	}
}
