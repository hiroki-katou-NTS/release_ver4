package nts.uk.ctx.at.record.infra.repository.remainingnumber;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveConditionInfo;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveNumberInfo;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.annlea.KRcmtAnnLeaRemain;

@Stateless
public class JpaAnnLeaGrantRemDataRepo extends JpaRepository implements AnnLeaGrantRemDataRepository {

	private String QUERY_WITH_EMP_ID = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId ORDER BY a.grantDate DESC";
	
	private String QUERY_WITH_EMPID_CHECKSTATE = "SELECT a FROM KRcmtAnnLeaRemain a"
			+ " WHERE a.sid = :employeeId AND a.expStatus = :checkState ORDER BY a.grantDate DESC";
	
	private String DELETE_QUERY = "DELETE FROM KRcmtAnnLeaRemain a"
			+ " WHERE a.sid = :employeeId and a.grantDate = :grantDate";
	
	private String QUERY_WITH_EMP_ID_NOT_EXP = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId AND a.expStatus = 1 ORDER BY a.grantDate";

	@Override
	public List<AnnualLeaveGrantRemainingData> find(String employeeId) {
		List<KRcmtAnnLeaRemain> entities = this.queryProxy().query(QUERY_WITH_EMP_ID, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId).getList();
		return entities.stream().map(ent -> toDomain(ent)).collect(Collectors.toList());
	}

	@Override
	public List<AnnualLeaveGrantRemainingData> findNotExp(String employeeId) {
		List<KRcmtAnnLeaRemain> entities = this.queryProxy().query(QUERY_WITH_EMP_ID_NOT_EXP, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId).getList();
		return entities.stream().map(ent -> toDomain(ent)).collect(Collectors.toList());
	}

	private AnnualLeaveGrantRemainingData toDomain(KRcmtAnnLeaRemain ent) {
		return AnnualLeaveGrantRemainingData.createFromJavaType(ent.annLeavID, ent.sid ,ent.cid, ent.grantDate, ent.deadline,
				ent.expStatus, ent.registerType, ent.grantDays, ent.grantMinutes, ent.usedDays, ent.usedMinutes,
				ent.stowageDays, ent.remainingDays, ent.remaningMinutes, ent.usedPercent, ent.perscribedDays,
				ent.deductedDays, ent.workingDays);
	}

	@Override
	public List<AnnualLeaveGrantRemainingData> findByCheckState(String employeeId, Boolean checkState) {
		List<KRcmtAnnLeaRemain> entities = this.queryProxy().query(QUERY_WITH_EMPID_CHECKSTATE, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("checkState", checkState ? 1 : 0)
				.getList();
		return entities.stream()
				.map(ent -> AnnualLeaveGrantRemainingData.createFromJavaType(ent.annLeavID, ent.cid, ent.sid, ent.grantDate,
						ent.deadline, ent.expStatus, ent.registerType, ent.grantDays, ent.grantMinutes, ent.usedDays,
						ent.usedMinutes, ent.stowageDays, ent.remainingDays, ent.remaningMinutes, ent.usedPercent,
						ent.perscribedDays, ent.deductedDays, ent.workingDays))
				.collect(Collectors.toList());
	}
	
	@Override
	public void add(AnnualLeaveGrantRemainingData data) {
		KRcmtAnnLeaRemain entity = new KRcmtAnnLeaRemain();
		entity.annLeavID = data.getAnnLeavID();
		entity.sid = data.getEmployeeId();
		entity.cid = data.getCid();
		updateValue(entity, data);

		this.commandProxy().insert(entity);
	}

	@Override
	public void update(AnnualLeaveGrantRemainingData data) {
		Optional<KRcmtAnnLeaRemain> entityOpt = this.queryProxy().find(data.getAnnLeavID(), KRcmtAnnLeaRemain.class);
		if (entityOpt.isPresent()) {
			KRcmtAnnLeaRemain entity = entityOpt.get();
			updateValue(entity, data);

			this.commandProxy().update(entity);
		}

	}

	private void updateValue(KRcmtAnnLeaRemain entity, AnnualLeaveGrantRemainingData data) {
		entity.deadline = data.getDeadline();
		entity.expStatus = data.getExpirationStatus().value;
		entity.registerType = data.getRegisterType().value;
		entity.grantDate = data.getGrantDate();
		AnnualLeaveNumberInfo details = data.getDetails();

		// grant data
		entity.grantDays = details.getGrantNumber().getDays().v();
		entity.grantMinutes = details.getGrantNumber().getMinutes().isPresent()
				? details.getGrantNumber().getMinutes().get().v() : null;
		// used data
		entity.usedDays = details.getUsedNumber().getDays().v();
		entity.usedMinutes = details.getUsedNumber().getMinutes().isPresent()
				? details.getUsedNumber().getMinutes().get().v() : null;
		entity.stowageDays = details.getUsedNumber().getStowageDays().isPresent()
				? details.getUsedNumber().getStowageDays().get().v() : null;
		// remain data
		entity.remainingDays = details.getRemainingNumber().getDays().v();
		entity.remaningMinutes = details.getRemainingNumber().getMinutes().isPresent()
				? details.getRemainingNumber().getMinutes().get().v() : null;

		entity.usedPercent = details.getUsedPercent().v().doubleValue();

		if (data.getAnnualLeaveConditionInfo().isPresent()) {
			AnnualLeaveConditionInfo conditionInfo = data.getAnnualLeaveConditionInfo().get();
			entity.perscribedDays = conditionInfo.getDeductedDays().v();
			entity.deductedDays = conditionInfo.getDeductedDays().v();
			entity.workingDays = conditionInfo.getWorkingDays().v();
		} else {
			entity.perscribedDays = null;
			entity.deductedDays = null;
			entity.workingDays = null;
		}
	}

	@Override
	public void delete(String employeeId, GeneralDate grantDate) {
		this.getEntityManager().createQuery(DELETE_QUERY)
		.setParameter("employeeId", employeeId)
		.setParameter("grantDate", grantDate);
	}
	
	@Override
	public void delete(String annaLeavID) {
		Optional<KRcmtAnnLeaRemain> entity = this.queryProxy().find(annaLeavID, KRcmtAnnLeaRemain.class);
		if (entity.isPresent()){
			this.commandProxy().remove(KRcmtAnnLeaRemain.class, annaLeavID);
		}
	}

	@Override
	public Optional<AnnualLeaveGrantRemainingData> findByID(String id) {
		Optional<KRcmtAnnLeaRemain> entity = this.queryProxy().find(id, KRcmtAnnLeaRemain.class);
		if (entity.isPresent()){
			return Optional.ofNullable(toDomain(entity.get()));
		}
		return Optional.empty();
	}


}
