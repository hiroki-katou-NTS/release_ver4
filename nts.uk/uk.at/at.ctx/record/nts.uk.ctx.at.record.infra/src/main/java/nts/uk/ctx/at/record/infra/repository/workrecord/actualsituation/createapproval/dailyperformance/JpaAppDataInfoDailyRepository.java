package nts.uk.ctx.at.record.infra.repository.workrecord.actualsituation.createapproval.dailyperformance;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDaily;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDailyRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.actualsituation.createapproval.dailyperformance.KrcmtAppDataInfoDaily;
import nts.uk.ctx.at.record.infra.entity.workrecord.actualsituation.createapproval.dailyperformance.KrcmtAppDataInfoDailyPK;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaAppDataInfoDailyRepository extends JpaRepository implements AppDataInfoDailyRepository {

	private static final String SELECT_ALL_APP_DATA_INFO = " SELECT c FROM KrcmtAppDataInfoDaily c ";
	
	private static final String SELECT_APP_DATA_INFO_BY_ID = SELECT_ALL_APP_DATA_INFO 
			+ " WHERE c.krcmtAppDataInfoDailyPK.employeeId = :employeeId"
			+ " AND c.krcmtAppDataInfoDailyPK.executionId = :executionId";
	
	private static final String SELECT_APP_DATA_INFO_BY_EXE_ID = SELECT_ALL_APP_DATA_INFO 
			+ " WHERE c.krcmtAppDataInfoDailyPK.executionId = :executionId";
	
	@Override
	public List<AppDataInfoDaily> getAppDataInfoDailyByExeID(String executionId) {
		List<AppDataInfoDaily> data = this.queryProxy().query(SELECT_APP_DATA_INFO_BY_EXE_ID,KrcmtAppDataInfoDaily.class)
				.setParameter("executionId", executionId)
				.getList(c->c.toDomain());
		return data;
	}
	@Override
	public Optional<AppDataInfoDaily> getAppDataInfoDailyByID(String employeeId, String executionId) {
		Optional<AppDataInfoDaily> data = this.queryProxy().query(SELECT_APP_DATA_INFO_BY_ID,KrcmtAppDataInfoDaily.class)
				.setParameter("employeeId", employeeId)
				.setParameter("executionId", executionId)
				.getSingle(c->c.toDomain());
		return data;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void addAppDataInfoDaily(AppDataInfoDaily appDataInfoDaily) {
		Optional<AppDataInfoDaily> data = getAppDataInfoDailyByID(appDataInfoDaily.getEmployeeId(),
				appDataInfoDaily.getExecutionId());
		if (!data.isPresent()) {
			this.commandProxy().insert(KrcmtAppDataInfoDaily.toEntity(appDataInfoDaily));
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void updateAppDataInfoDaily(AppDataInfoDaily appDataInfoDaily) {
		KrcmtAppDataInfoDaily newEntity = KrcmtAppDataInfoDaily.toEntity(appDataInfoDaily);
		KrcmtAppDataInfoDaily updateEntity = this.queryProxy().find(new KrcmtAppDataInfoDailyPK(
				newEntity.krcmtAppDataInfoDailyPK.employeeId,
				newEntity.krcmtAppDataInfoDailyPK.executionId
				), KrcmtAppDataInfoDaily.class).get();
		updateEntity.errorMessage = newEntity.errorMessage;
		this.commandProxy().update(updateEntity);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void deleteAppDataInfoDaily(String employeeId, String executionId) {
		Optional<AppDataInfoDaily> newEntity = this.queryProxy().query(SELECT_APP_DATA_INFO_BY_ID,KrcmtAppDataInfoDaily.class)
				.setParameter("employeeId", employeeId)
				.setParameter("executionId", executionId)
				.getSingle(c->c.toDomain());
		this.commandProxy().remove(newEntity);
		
	}
}
