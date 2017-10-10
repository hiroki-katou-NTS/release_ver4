/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.schedulemanagementcontrol;

import java.util.Optional;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.schedule.dom.schedulemanagementcontrol.ScheduleManagementControl;
import nts.uk.ctx.at.schedule.dom.schedulemanagementcontrol.ScheduleManagementControlRepository;
import nts.uk.ctx.at.schedule.dom.schedulemanagementcontrol.UseAtr;
import nts.uk.ctx.at.schedule.infra.entity.schedulemanagementcontrol.KscmtScheduleManCon;

/**
 * The Class JpaScheduleManagementControlRepository.
 */
public class JpaScheduleManagementControlRepository extends JpaRepository
		implements ScheduleManagementControlRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.schedulemanagementcontrol.
	 * ScheduleManagementControlRepository#findById(java.lang.String)
	 */
	@Override
	public Optional<ScheduleManagementControl> findById(String employeeId) {
		return this.queryProxy().find(employeeId, KscmtScheduleManCon.class)
				.map(entity -> this.toDomain(entity));
	}
	
	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the schedule management control
	 */
	private ScheduleManagementControl toDomain(KscmtScheduleManCon entity) {
		return new ScheduleManagementControl(entity.getSid(),
				UseAtr.valueOf(entity.getScheduleManAtr()));
	}

}
