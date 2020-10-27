package nts.uk.ctx.at.record.infra.repository.workrecord.actualsituation.createapproval.monthlyperformance;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.AppDataInfoMonthly;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.AppDataInfoMonthlyRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.actualsituation.createapproval.monthlyperformance.KrcdtAppInstErrMonthly;
import nts.uk.ctx.at.record.infra.entity.workrecord.actualsituation.createapproval.monthlyperformance.KrcdtAppInstErrMonthlyPK;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaAppDataInfoMonthlyRepository extends JpaRepository implements AppDataInfoMonthlyRepository {

	private static final String SELECT_ALL_APP_DATA_INFO = " SELECT c FROM KrcdtAppInstErrMonthly c ";
	
	private static final String SELECT_APP_DATA_INFO_BY_ID = SELECT_ALL_APP_DATA_INFO 
			+ " WHERE c.krcdtAppInstErrMonthlyPK.employeeId = :employeeId"
			+ " AND c.krcdtAppInstErrMonthlyPK.executionId = :executionId";
	private static final String SELECT_APP_DATA_INFO_BY_EXE_ID = SELECT_ALL_APP_DATA_INFO 
			+ " WHERE c.krcdtAppInstErrMonthlyPK.executionId = :executionId";
	
	@Override
	public List<AppDataInfoMonthly> getAppDataInfoMonthlyByExeID(String executionId) {
		List<AppDataInfoMonthly> data = this.queryProxy().query(SELECT_APP_DATA_INFO_BY_EXE_ID,KrcdtAppInstErrMonthly.class)
				.setParameter("executionId", executionId)
				.getList(c->c.toDomain());
		return data;
	}
	
	@Override
	public Optional<AppDataInfoMonthly> getAppDataInfoMonthlyByID(String employeeId, String executionId) {
		Optional<AppDataInfoMonthly> data = this.queryProxy().query(SELECT_APP_DATA_INFO_BY_ID,KrcdtAppInstErrMonthly.class)
				.setParameter("employeeId", employeeId)
				.setParameter("executionId", executionId)
				.getSingle(c->c.toDomain());
		return data;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void addAppDataInfoMonthly(AppDataInfoMonthly appDataInfoMonthly) {
		Optional<AppDataInfoMonthly> data = this.queryProxy().query(SELECT_APP_DATA_INFO_BY_ID,KrcdtAppInstErrMonthly.class)
				.setParameter("employeeId", appDataInfoMonthly.getEmployeeId())
				.setParameter("executionId", appDataInfoMonthly.getExecutionId())
				.getSingle(c->c.toDomain());
		if(!data.isPresent()) {
			this.commandProxy().insert(KrcdtAppInstErrMonthly.toEntity(appDataInfoMonthly));
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void updateAppDataInfoMonthly(AppDataInfoMonthly appDataInfoMonthly) {
		KrcdtAppInstErrMonthly newEntity = KrcdtAppInstErrMonthly.toEntity(appDataInfoMonthly);
		KrcdtAppInstErrMonthly updateEntity = this.queryProxy().find(new KrcdtAppInstErrMonthlyPK(
				newEntity.krcdtAppInstErrMonthlyPK.employeeId,
				newEntity.krcdtAppInstErrMonthlyPK.executionId
				), KrcdtAppInstErrMonthly.class).get();
		updateEntity.errorMessage = newEntity.errorMessage;
		this.commandProxy().update(updateEntity);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void deleteAppDataInfoMonthly(String employeeId,String executionId) {
		Optional<AppDataInfoMonthly> newEntity = this.queryProxy().query(SELECT_APP_DATA_INFO_BY_ID,KrcdtAppInstErrMonthly.class)
				.setParameter("employeeId", employeeId)
				.setParameter("executionId", executionId)
				.getSingle(c->c.toDomain());
		this.commandProxy().remove(newEntity);
		
	}

	

}
