package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm.condition;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkRecordExtraConRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkRecordExtractingCondition;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcmtWorkRecordExtraCon;

@Stateless
public class JpaWorkRecorExtraConRepository extends JpaRepository implements WorkRecordExtraConRepository  {

	
	private final String SELECT_FROM_WORK_RECORD = " SELECT c FROM KrcmtWorkRecordExtraCon c ";
	private final String SELECT_FROM_WORK_RECORD_BY_ID = SELECT_FROM_WORK_RECORD 
			+ " WHERE c.errorAlarmCheckID = :errorAlarmCheckID ";
	
	@Override
	public List<WorkRecordExtractingCondition> getAllWorkRecordExtraCon() {
		List<WorkRecordExtractingCondition> data = this.queryProxy().query(SELECT_FROM_WORK_RECORD,KrcmtWorkRecordExtraCon.class)
				.getList(c->c.toDomain()); 
		return data;
	}

	@Override
	public Optional<WorkRecordExtractingCondition> getWorkRecordExtraConById(String errorAlarmCheckID) {
		Optional<WorkRecordExtractingCondition> data = this.queryProxy().query(SELECT_FROM_WORK_RECORD_BY_ID,KrcmtWorkRecordExtraCon.class)
				.setParameter("errorAlarmCheckID", errorAlarmCheckID)
				.getSingle(c->c.toDomain());
				
		return data;
	}

	@Override
	public void addWorkRecordExtraCon(WorkRecordExtractingCondition workRecordExtractingCondition) {
		this.commandProxy().insert(KrcmtWorkRecordExtraCon.toEntity(workRecordExtractingCondition));
		this.getEntityManager().flush();
	}

	@Override
	public void updateWorkRecordExtraCon(WorkRecordExtractingCondition workRecordExtractingCondition) {
		KrcmtWorkRecordExtraCon newEntity = KrcmtWorkRecordExtraCon.toEntity(workRecordExtractingCondition);
		KrcmtWorkRecordExtraCon updateEntity = this.queryProxy().find(newEntity.errorAlarmCheckID, KrcmtWorkRecordExtraCon.class).get();
		updateEntity.messageBold = newEntity.messageBold;
		updateEntity.messageColor = newEntity.messageColor;
		updateEntity.sortOrderBy = newEntity.sortOrderBy;
		updateEntity.useAtr = newEntity.useAtr;
		updateEntity.nameWKRecord = newEntity.nameWKRecord;
		this.commandProxy().update(updateEntity);
	}

	@Override
	public void deleteWorkRecordExtraCon(String errorAlarmCheckID) {
		this.commandProxy().remove(KrcmtWorkRecordExtraCon.class,errorAlarmCheckID);
		
	}

}
