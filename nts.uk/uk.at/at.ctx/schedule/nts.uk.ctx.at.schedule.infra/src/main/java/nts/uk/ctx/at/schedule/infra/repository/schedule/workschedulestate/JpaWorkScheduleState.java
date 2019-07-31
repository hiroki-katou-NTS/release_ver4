package nts.uk.ctx.at.schedule.infra.repository.schedule.workschedulestate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;
import nts.uk.ctx.at.schedule.infra.entity.schedule.workschedulestate.KscdtScheState;
import nts.uk.ctx.at.schedule.infra.entity.schedule.workschedulestate.KscdtScheStatePK;

/**
 * 
 * @author sonnh1
 *
 */
@Stateless
public class JpaWorkScheduleState extends JpaRepository implements WorkScheduleStateRepository {

	private static final String SELECT_ALL = "SELECT a FROM KscdtScheState a";
	private static final String SELCET_BY_DATE_EMPID = SELECT_ALL + " WHERE a.kscdtScheStatePK.employeeId = :sId " + "AND a.kscdtScheStatePK.date = :date";

	private WorkScheduleState toDomain(KscdtScheState entity) {
		WorkScheduleState domain = WorkScheduleState.createFromJavaType(entity.scheduleEditState,
				entity.kscdtScheStatePK.scheduleItemId, entity.kscdtScheStatePK.date,
				entity.kscdtScheStatePK.employeeId);
		return domain;
	}

	@Override
	public List<WorkScheduleState> findAll() {
		return this.queryProxy().query(SELECT_ALL, KscdtScheState.class).getList(x -> toDomain(x));
	}

	@Override
	public void updateScheduleEditState(WorkScheduleState domain) {
		Optional<KscdtScheState> kscstWorkScheduleState = this.queryProxy().find(
				new KscdtScheStatePK(domain.getSId(), domain.getScheduleItemId(), domain.getYmd()),
				KscdtScheState.class);
		if (kscstWorkScheduleState.isPresent()) {
			kscstWorkScheduleState.get().scheduleEditState = domain.getScheduleEditState().value;
			this.commandProxy().update(kscstWorkScheduleState.get());
			this.getEntityManager().flush();
		}
	}

	@Override
	public List<WorkScheduleState> findByDateAndEmpId(String sId, GeneralDate date) {
		return this.queryProxy().query(SELCET_BY_DATE_EMPID, KscdtScheState.class).setParameter("sId", sId)
				.setParameter("date", date).getList(x -> toDomain(x));
	}

	@Override
	public void updateOrInsert(WorkScheduleState domain) {
		KscdtScheStatePK pk = new KscdtScheStatePK(domain.getSId(), domain.getScheduleItemId(), domain.getYmd());
		Optional<KscdtScheState> kscstWorkScheduleState = this.queryProxy().find(pk,KscdtScheState.class);
		if (kscstWorkScheduleState.isPresent()) {
			kscstWorkScheduleState.get().scheduleEditState = domain.getScheduleEditState().value;
			this.commandProxy().update(kscstWorkScheduleState.get());
		} else {
			KscdtScheState entity = new KscdtScheState(pk, domain.getScheduleEditState().value);
			this.commandProxy().insert(entity);
		}
		this.getEntityManager().flush();
	}

	@Override
	public void updateOrInsert(List<WorkScheduleState> lstState) {
		this.commandProxy().updateAll(
				lstState.stream().map(domain -> 
					new KscdtScheState(new KscdtScheStatePK(domain.getSId(), domain.getScheduleItemId(), domain.getYmd()),
							domain.getScheduleEditState().value)
				).collect(Collectors.toList()));
	}
}
