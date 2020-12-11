package nts.uk.ctx.sys.assist.infra.repository.datarestoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryOperatingCondition;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryResult;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryResultRepository;
import nts.uk.ctx.sys.assist.dom.storage.SaveStatus;
import nts.uk.ctx.sys.assist.infra.entity.datarestoration.SspdtRecoverResult;

@Stateless
public class JpaDataRecoverResultRepository extends JpaRepository implements DataRecoveryResultRepository {

	private static final String FIND_RESULTS_BY_STARTDATETIME = "SELECT r FROM SspdtRecoverResult r "
			+ "WHERE r.startDateTime >= :start AND r.startDateTime <= :end ";
//			+ "AND r.saveForm = " + StorageForm.AUTOMATIC.value;
	private static final String UPDATE_BY_DATARECOVERYPROCESSID = "UPDATE SspdtRecoverResult t SET t.executionResult =:executionResult, t.endDateTime =:endDateTime WHERE t.dataRecoveryProcessId =:dataRecoveryProcessId";
	private static final String SELECT_WITH_NULL_LIST_EMPLOYEE = " SELECT f FROM SspdtRecoverResult f "
			+ " WHERE f.cid =:cid " + " AND f.startDateTime >=:startDateOperator "
			+ " AND f.endDateTime <=:endDateOperator ";

	private static final String SELECT_WITH_NOT_NULL_LIST_EMPLOYEE = " SELECT f FROM SspdtRecoverResult f "
			+ " WHERE f.cid =:cid " + " AND f.startDateTime =:startDateOperator "
			+ " AND f.endDateTime =:endDateOperator " + " AND f.practitioner =:practitioner ";
	private static final String SELECT_BY_MULTIPLE_RECOVERY_IDS = "SELECT t FROM SspdtRecoverResult t "
			+ "WHERE t.dataRecoveryProcessId IN :dataRecoveryProcessIds";
	private static final String SELECT_BY_MULTIPLE_SAVE_NAMES = "SELECT t from SspdtRecoverResult t "
			+ "WHERE t.saveName IN :saveNames";

	@Override
	public Optional<DataRecoveryResult> getDataRecoverResultById(String dataRecoveryProcessId) {
		return Optional.ofNullable(
				this.getEntityManager().find(SspdtRecoverResult.class, dataRecoveryProcessId).toDomain());
	}

	@Override
	public List<DataRecoveryResult> getDataRecoveryResultByStartDatetime(GeneralDateTime from, GeneralDateTime to) {
		return this.queryProxy()
				.query(FIND_RESULTS_BY_STARTDATETIME, SspdtRecoverResult.class).setParameter("start", from)
				.setParameter("end", to).getList(SspdtRecoverResult::toDomain);
	}

	@Override
	public void add(DataRecoveryResult domain) {
		this.commandProxy().insert(SspdtRecoverResult.toEntity(domain));
	}

	@Override
	public void update(DataRecoveryResult domain) {
		this.commandProxy().update(SspdtRecoverResult.toEntity(domain));
	}

	@Override
	public void remove(String dataRecoveryProcessId) {
		this.commandProxy().remove(SspdtRecoverResult.class, dataRecoveryProcessId);
	}

	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public void updateEndDateTimeExecutionResult(String dataRecoveryProcessId,
			DataRecoveryOperatingCondition dataRecoveryOperatingCondition) {
		int executionResult;
		switch (dataRecoveryOperatingCondition) {
		case INTERRUPTION_END:
			executionResult = SaveStatus.INTERRUPTION.value;
			break;
		case DONE:
			executionResult = SaveStatus.SUCCESS.value;
			break;
		default:
			executionResult = SaveStatus.FAILURE.value;
		}
		this.getEntityManager().createQuery(UPDATE_BY_DATARECOVERYPROCESSID, SspdtRecoverResult.class)
				.setParameter("executionResult", executionResult).setParameter("endDateTime", GeneralDateTime.now())
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId).executeUpdate();

	}

	@Override
	public List<DataRecoveryResult> getResultOfRestoration(String cid, GeneralDateTime startDateOperator,
			GeneralDateTime endDateOperator, List<String> listOperatorEmployeeId) {
		List<DataRecoveryResult> list = new ArrayList<DataRecoveryResult>();

		if (!CollectionUtil.isEmpty(listOperatorEmployeeId)) {
			for (String employeeId : listOperatorEmployeeId) {
				list.addAll(this.queryProxy().query(SELECT_WITH_NOT_NULL_LIST_EMPLOYEE, SspdtRecoverResult.class)
						.setParameter("cid", cid).setParameter("startDateOperator", startDateOperator)
						.setParameter("endDateOperator", endDateOperator).setParameter("practitioner", employeeId)
						.getList(item -> item.toDomain()));
			}
		} else {
			list.addAll(this.queryProxy().query(SELECT_WITH_NULL_LIST_EMPLOYEE, SspdtRecoverResult.class)
					.setParameter("cid", cid).setParameter("startDateOperator", startDateOperator)
					.setParameter("endDateOperator", endDateOperator).getList(item -> item.toDomain()));
		}
		return list;
	}

	@Override
	public List<DataRecoveryResult> getDataRecoveryResultsByIds(List<String> dataRecoveryProcessIds) {
		return this.queryProxy().query(SELECT_BY_MULTIPLE_RECOVERY_IDS, SspdtRecoverResult.class)
				.setParameter("dataRecoveryProcessIds", dataRecoveryProcessIds)
				.getList(SspdtRecoverResult::toDomain);
	}

	@Override
	public List<DataRecoveryResult> getDataRecoveryResultsBySaveNames(List<String> saveNames) {
		return this.queryProxy().query(SELECT_BY_MULTIPLE_SAVE_NAMES, SspdtRecoverResult.class)
				.setParameter("saveNames", saveNames).getList(SspdtRecoverResult::toDomain);
	}
}
