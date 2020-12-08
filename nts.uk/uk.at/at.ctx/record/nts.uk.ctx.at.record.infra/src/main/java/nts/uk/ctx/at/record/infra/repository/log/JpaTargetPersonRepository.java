package nts.uk.ctx.at.record.infra.repository.log;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPerson;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPersonRepository;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtExecTarget;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtExecTargetSts;

@Stateless
public class JpaTargetPersonRepository extends JpaRepository implements TargetPersonRepository {

	private static final String SELECT_FROM_TARGET = "SELECT c FROM KrcdtExecTarget c ";
	private static final String SELECT_ALL_TARGET = SELECT_FROM_TARGET
			+ " WHERE c.krcdtEmpExeTargetPK.employeeId = :employeeId ";
	private static final String SELECT_TARGET_BY_ID = SELECT_ALL_TARGET
			+ " AND c.krcdtEmpExeTargetPK.empCalAndSumExecLogID = :empCalAndSumExecLogID ";

	private static final String SELECT_TARGET_PERSON = SELECT_FROM_TARGET
			+ " WHERE c.krcdtEmpExeTargetPK.empCalAndSumExecLogID = :empCalAndSumExecLogID ";

	private static final String SELECT_BY_LOG_ID = SELECT_FROM_TARGET
			+ "WHERE c.krcdtEmpExeTargetPK.empCalAndSumExecLogID = :empCalAndSumExecLogID";

	@Override
	public List<TargetPerson> getAllTargetPerson(String employeeID) {
		List<TargetPerson> data = this.queryProxy().query(SELECT_ALL_TARGET, KrcdtExecTarget.class)
				.setParameter("employeeId", employeeID).getList(c -> c.toDomain());
		return data;
	}

	@Override
	public Optional<TargetPerson> getTargetPersonByID(String employeeID, String empCalAndSumExecLogId) {
		Optional<TargetPerson> data = this.queryProxy().query(SELECT_TARGET_BY_ID, KrcdtExecTarget.class)
				.setParameter("employeeId", employeeID).setParameter("empCalAndSumExecLogID", empCalAndSumExecLogId)
				.getSingle(c -> c.toDomain());
		return data;
	}

	@Override
	public List<TargetPerson> getTargetPersonById(String empCalAndSumExecLogId) {
		return this.queryProxy().query(SELECT_TARGET_PERSON, KrcdtExecTarget.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogId).getList(f -> f.toDomain());
	}

	@Override
	public List<TargetPerson> getByempCalAndSumExecLogID(String empCalAndSumExecLogID) {
		List<TargetPerson> data = this.queryProxy().query(SELECT_BY_LOG_ID, KrcdtExecTarget.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID).getList(c -> c.toDomain());
		return data;
	}

	@Override
	public void add(TargetPerson targetPerson) {
		this.commandProxy().insert(KrcdtExecTarget.toEntity(targetPerson));
	}

	@Override
	public void addAll(List<TargetPerson> lstTargetPerson) {
		this.commandProxy().insertAll(
				lstTargetPerson.stream().map(c -> KrcdtExecTarget.toEntity(c)).collect(Collectors.toList()));
	}

	@Override
	public void update(String employeeID, String empCalAndSumExecLogId, int state) {
		KrcdtExecTarget krcdtEmpExeTarget = this.queryProxy().query(SELECT_TARGET_BY_ID, KrcdtExecTarget.class)
				.setParameter("employeeId", employeeID).setParameter("empCalAndSumExecLogID", empCalAndSumExecLogId).getSingle().get();
		
		KrcdtExecTargetSts target = krcdtEmpExeTarget.lstEmpExeTargetStt.stream().filter(item -> item.KrcdtEmpExeTargetSttPK.executionContent == 0).findFirst().get();
		target.executionState = state;
		
		this.commandProxy().update(krcdtEmpExeTarget);
	}

	@Override
	public void updateWithContent(String employeeID, String empCalAndSumExecLogId, int executionContent, int state) {
		Optional<KrcdtExecTarget> krcdtEmpExeTarget = this.queryProxy().query(SELECT_TARGET_BY_ID, KrcdtExecTarget.class)
				.setParameter("employeeId", employeeID).setParameter("empCalAndSumExecLogID", empCalAndSumExecLogId).getSingle();
		if(!krcdtEmpExeTarget.isPresent()) {
			return;
		}
		KrcdtExecTargetSts target = krcdtEmpExeTarget.get().lstEmpExeTargetStt.stream()
				.filter(item -> item.KrcdtEmpExeTargetSttPK.executionContent == executionContent)
				.findFirst().get();
		target.executionState = state;
		this.commandProxy().update(krcdtEmpExeTarget.get());
	}

}
