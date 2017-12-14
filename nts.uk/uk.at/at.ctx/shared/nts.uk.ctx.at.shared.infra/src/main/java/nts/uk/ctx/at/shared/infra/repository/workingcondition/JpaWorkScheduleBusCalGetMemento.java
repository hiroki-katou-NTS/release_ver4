package nts.uk.ctx.at.shared.infra.repository.workingcondition;

import nts.uk.ctx.at.shared.dom.workingcondition.TimeZoneScheduledMasterAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleBusCalGetMemento;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleMasterReferenceAtr;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtScheduleMethod;


/**
 * The Class JpaWorkScheduleBusCalGetMemento.
 */
public class JpaWorkScheduleBusCalGetMemento implements WorkScheduleBusCalGetMemento {
	
	/** The kshmt schedule method. */
	private KshmtScheduleMethod kshmtScheduleMethod;
	
	/**
	 * Instantiates a new jpa work schedule bus cal get memento.
	 *
	 * @param entity the entity
	 */
	public JpaWorkScheduleBusCalGetMemento(KshmtScheduleMethod entity){
		this.kshmtScheduleMethod = entity;
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleBusCalGetMemento#getReferenceBusinessDayCalendar()
	 */
	@Override
	public WorkScheduleMasterReferenceAtr getReferenceBusinessDayCalendar() {
		return WorkScheduleMasterReferenceAtr.valueOf(this.kshmtScheduleMethod.getRefBusinessDayCalendar());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleBusCalGetMemento#getReferenceBasicWork()
	 */
	@Override
	public WorkScheduleMasterReferenceAtr getReferenceBasicWork() {
		return WorkScheduleMasterReferenceAtr.valueOf(this.kshmtScheduleMethod.getRefBasicWork());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleBusCalGetMemento#getReferenceWorkingHours()
	 */
	@Override
	public TimeZoneScheduledMasterAtr getReferenceWorkingHours() {
		return TimeZoneScheduledMasterAtr.valueOf(this.kshmtScheduleMethod.getRefWorkingHours());
	}
	
}
