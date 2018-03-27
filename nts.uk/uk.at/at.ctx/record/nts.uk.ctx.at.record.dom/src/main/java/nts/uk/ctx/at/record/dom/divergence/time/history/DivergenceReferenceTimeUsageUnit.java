package nts.uk.ctx.at.record.dom.divergence.time.history;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;


/**
 * The Class DivergenceReferenceTimeUsageUnit.
 */
@Getter
@Setter
public class DivergenceReferenceTimeUsageUnit extends AggregateRoot {

	/** The c id. */
	/* Company Id */
	private String cId;

	/** The work type use set. */
	/* Worktype usage */
	private BigDecimal workTypeUseSet;

	/**
	 * Instantiates a new divergence reference time usage unit.
	 *
	 * @param memento the memento
	 */
	public DivergenceReferenceTimeUsageUnit(DivergenceReferenceTimeUsageUnitGetMemento memento) {
		this.cId = memento.getCompanyId();
		this.workTypeUseSet = memento.getWorkTypeUseSet();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(DivergenceReferenceTimeUsageUnitSetMemento memento) {
		memento.setCompanyId(this.cId);
		memento.setWorkTypeUseSet(this.workTypeUseSet);
	}

}
