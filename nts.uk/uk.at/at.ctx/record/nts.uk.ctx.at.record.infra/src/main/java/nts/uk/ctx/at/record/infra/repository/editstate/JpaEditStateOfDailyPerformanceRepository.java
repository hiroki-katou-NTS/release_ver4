package nts.uk.ctx.at.record.infra.repository.editstate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.infra.entity.editstate.KrcdtDailyRecEditSet;
import nts.uk.ctx.at.record.infra.entity.editstate.KrcdtDailyRecEditSetPK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaEditStateOfDailyPerformanceRepository extends JpaRepository
		implements EditStateOfDailyPerformanceRepository {

	private static final String REMOVE_BY_EMPLOYEE;

	private static final String DEL_BY_LIST_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDailyRecEditSet a ");
		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd = :ymd ");
		REMOVE_BY_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDailyRecEditSet a ");
		builderString.append("WHERE WHERE a.krcdtDailyRecEditSetPK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd IN :processingYmds ");
		DEL_BY_LIST_KEY = builderString.toString();
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).executeUpdate();
	}

	@Override
	public void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> processingYmds) {
		this.getEntityManager().createQuery(DEL_BY_LIST_KEY).setParameter("employeeIds", employeeIds)
				.setParameter("processingYmds", processingYmds).executeUpdate();
	}

	@Override
	public void add(List<EditStateOfDailyPerformance> editStates) {
		this.commandProxy().insertAll(
						editStates.stream()
								.map(c -> new KrcdtDailyRecEditSet(new KrcdtDailyRecEditSetPK(c.getEmployeeId(),
										c.getYmd(), c.getAttendanceItemId()), c.getEditStateSetting().value))
								.collect(Collectors.toList()));
	}

	@Override
	public List<EditStateOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDailyRecEditSet a ");
		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd = :ymd ");
		return this.queryProxy().query(builderString.toString(), KrcdtDailyRecEditSet.class)
				.setParameter("employeeId", employeeId).setParameter("ymd", ymd)
				.getList(c -> toDomain(c));
	}

	private EditStateOfDailyPerformance toDomain(KrcdtDailyRecEditSet c) {
		return new EditStateOfDailyPerformance(c.krcdtDailyRecEditSetPK.employeeId,
				c.krcdtDailyRecEditSetPK.attendanceItemId, c.krcdtDailyRecEditSetPK.processingYmd,
				EnumAdaptor.valueOf(c.editState, EditStateSetting.class));
	}

	@Override
	public void updateByKey(List<EditStateOfDailyPerformance> editStates) {
		this.commandProxy().updateAll(
				editStates.stream()
						.map(c -> new KrcdtDailyRecEditSet(new KrcdtDailyRecEditSetPK(c.getEmployeeId(),
								c.getYmd(), c.getAttendanceItemId()), c.getEditStateSetting().value))
						.collect(Collectors.toList()));
	}

	@Override
	public List<EditStateOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM KrcdtDailyRecEditSet a ");
		query.append("WHERE a.krcdtDailyRecEditSetPK.employeeId IN :employeeId ");
		query.append("AND a.krcdtDailyRecEditSetPK.processingYmd <= :end AND a.krcdtDailyRecEditSetPK.processingYmd >= :start");
		return queryProxy().query(query.toString(), KrcdtDailyRecEditSet.class).setParameter("employeeId", employeeId)
				.setParameter("start", ymd.start()).setParameter("end", ymd.end()).getList().stream()
				.collect(Collectors.groupingBy(c -> c.krcdtDailyRecEditSetPK.employeeId + c.krcdtDailyRecEditSetPK.processingYmd.toString()))
				.entrySet().stream().map(c -> c.getValue().stream().map(x -> toDomain(x)).collect(Collectors.toList()))
				.flatMap(List::stream).collect(Collectors.toList());
	}

	@Override
	public void addAndUpdate(List<EditStateOfDailyPerformance> editStates) {
		editStates.forEach(x -> {
			if(this.findByKeyId(x.getEmployeeId(), x.getYmd(), x.getAttendanceItemId()).isPresent()){
				this.commandProxy().update(new KrcdtDailyRecEditSet(new KrcdtDailyRecEditSetPK(x.getEmployeeId(),
						x.getYmd(), x.getAttendanceItemId()), x.getEditStateSetting().value));
			}else{
				KrcdtDailyRecEditSet entity = new KrcdtDailyRecEditSet(new KrcdtDailyRecEditSetPK(x.getEmployeeId(),
						x.getYmd(), x.getAttendanceItemId()), x.getEditStateSetting().value);
				this.commandProxy().insert(entity);
			}
		});
	}

	@Override
	public Optional<EditStateOfDailyPerformance> findByKeyId(String employeeId, GeneralDate ymd, Integer id) {
		return this.queryProxy().find(new KrcdtDailyRecEditSetPK(employeeId, ymd, id), KrcdtDailyRecEditSet.class)
				.map(x -> new EditStateOfDailyPerformance(employeeId, id, ymd, EnumAdaptor.valueOf(x.editState, EditStateSetting.class)));
	}

	@Override
	public void updateByKeyFlush(List<EditStateOfDailyPerformance> editStates) {
		this.addAndUpdate(editStates);
		this.getEntityManager().flush();
	}

}
