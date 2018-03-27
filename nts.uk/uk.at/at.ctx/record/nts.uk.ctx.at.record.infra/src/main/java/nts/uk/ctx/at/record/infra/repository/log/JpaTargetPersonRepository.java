package nts.uk.ctx.at.record.infra.repository.log;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPerson;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPersonRepository;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtEmpExeTarget;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtEmpExeTargetStt;

@Stateless
public class JpaTargetPersonRepository extends JpaRepository implements TargetPersonRepository {

	private final String SELECT_FROM_TARGET = "SELECT c FROM KrcdtEmpExeTarget c ";
	private final String SELECT_ALL_TARGET = SELECT_FROM_TARGET
			+ " WHERE c.krcdtEmpExeTargetPK.employeeId = :employeeId ";
	private final String SELECT_TARGET_BY_ID = SELECT_ALL_TARGET
			+ " AND c.krcdtEmpExeTargetPK.empCalAndSumExecLogID = :empCalAndSumExecLogID ";

	private final String SELECT_TARGET_PERSON = SELECT_FROM_TARGET
			+ " WHERE c.krcdtEmpExeTargetPK.empCalAndSumExecLogID = :empCalAndSumExecLogID ";

	private final String SELECT_BY_LOG_ID = SELECT_FROM_TARGET
			+ "WHERE c.krcdtEmpExeTargetPK.empCalAndSumExecLogID = :empCalAndSumExecLogID";

	@Override
	public List<TargetPerson> getAllTargetPerson(String employeeID) {
		List<TargetPerson> data = this.queryProxy().query(SELECT_ALL_TARGET, KrcdtEmpExeTarget.class)
				.setParameter("employeeId", employeeID).getList(c -> c.toDomain());
		return data;
	}

	@Override
	public Optional<TargetPerson> getTargetPersonByID(String employeeID, String empCalAndSumExecLogId) {
		Optional<TargetPerson> data = this.queryProxy().query(SELECT_TARGET_BY_ID, KrcdtEmpExeTarget.class)
				.setParameter("employeeId", employeeID).setParameter("empCalAndSumExecLogID", empCalAndSumExecLogId)
				.getSingle(c -> c.toDomain());
		return data;
	}

	@Override
	public List<TargetPerson> getTargetPersonById(String empCalAndSumExecLogId) {
		return this.queryProxy().query(SELECT_TARGET_PERSON, KrcdtEmpExeTarget.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogId).getList(f -> f.toDomain());
	}

	@Override
	public List<TargetPerson> getByempCalAndSumExecLogID(String empCalAndSumExecLogID) {
		List<TargetPerson> data = this.queryProxy().query(SELECT_BY_LOG_ID, KrcdtEmpExeTarget.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID).getList(c -> c.toDomain());
		return data;
	}

	@Override
	public void add(TargetPerson targetPerson) {
		this.commandProxy().insert(KrcdtEmpExeTarget.toEntity(targetPerson));
	}

	@Override
	public void addAll(List<TargetPerson> lstTargetPerson) {
		this.commandProxy().insertAll(
				lstTargetPerson.stream().map(c -> KrcdtEmpExeTarget.toEntity(c)).collect(Collectors.toList()));
	}

	@Override
	public void update(String employeeID, String empCalAndSumExecLogId, int state) {
		KrcdtEmpExeTarget krcdtEmpExeTarget = this.queryProxy().query(SELECT_TARGET_BY_ID, KrcdtEmpExeTarget.class)
				.setParameter("employeeId", employeeID).setParameter("empCalAndSumExecLogID", empCalAndSumExecLogId).getSingle().get();
		
		KrcdtEmpExeTargetStt target = krcdtEmpExeTarget.lstEmpExeTargetStt.stream().filter(item -> item.KrcdtEmpExeTargetSttPK.executionContent == 0).findFirst().get();
		target.executionState = state;
		
		this.commandProxy().update(krcdtEmpExeTarget);
	}

}
