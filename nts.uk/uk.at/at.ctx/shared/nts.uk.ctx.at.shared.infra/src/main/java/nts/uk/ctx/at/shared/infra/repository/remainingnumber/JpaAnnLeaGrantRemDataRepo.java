package nts.uk.ctx.at.shared.infra.repository.remainingnumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveConditionInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveNumberInfo;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.annlea.KRcmtAnnLeaRemain;

@Stateless
public class JpaAnnLeaGrantRemDataRepo extends JpaRepository implements AnnLeaGrantRemDataRepository {

	private static final String QUERY_WITH_EMP_ID = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId ORDER BY a.grantDate DESC";

	private static final String CHECK_UNIQUE_SID_GRANTDATE_FOR_ADD = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId and a.grantDate = :grantDate";
	
	private static final String CHECK_UNIQUE_SID_GRANTDATE_FOR_UPDATE = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId and a.annLeavID !=:annLeavID and a.grantDate = :grantDate";
	
	private static final String QUERY_WITH_EMPID_CHECKSTATE = "SELECT a FROM KRcmtAnnLeaRemain a"
			+ " WHERE a.sid = :employeeId AND a.expStatus = :checkState ORDER BY a.grantDate DESC";

	private static final String DELETE_QUERY = "DELETE FROM KRcmtAnnLeaRemain a"
			+ " WHERE a.sid = :employeeId and a.grantDate = :grantDate";
	
	private static final String DELETE_AFTER_QUERY = "DELETE FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId and a.grantDate > :startDate";
	
	private static final String QUERY_WITH_EMP_ID_NOT_EXP = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId AND a.expStatus = 1 ORDER BY a.grantDate DESC";
	
	private static final String FIND_BY_EMP_AND_DATE = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId AND a.grantDate >= :startDate AND a.grantDate <= :endDate ORDER BY a.grantDate DESC";

	@Override
	public List<AnnualLeaveGrantRemainingData> find(String employeeId) {
		List<KRcmtAnnLeaRemain> entities = this.queryProxy().query(QUERY_WITH_EMP_ID, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId).getList();
		return entities.stream().map(ent -> toDomain(ent)).collect(Collectors.toList());
	}
	

	@Override
	public Optional<AnnualLeaveGrantRemainingData> getLast(String employeeId) {
		List<KRcmtAnnLeaRemain> entities = this.queryProxy().query(QUERY_WITH_EMP_ID, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId).getList();
		
		if (entities.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(toDomain(entities.get(0)));
	}

	@Override
	public List<AnnualLeaveGrantRemainingData> findNotExp(String employeeId) {
		List<KRcmtAnnLeaRemain> entities = this.queryProxy().query(QUERY_WITH_EMP_ID_NOT_EXP, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId).getList();
		return entities.stream().map(ent -> toDomain(ent)).collect(Collectors.toList());
	}

	private AnnualLeaveGrantRemainingData toDomain(KRcmtAnnLeaRemain ent) {
		return AnnualLeaveGrantRemainingData.createFromJavaType(ent.annLeavID, ent.cid, ent.sid, ent.grantDate,
				ent.deadline, ent.expStatus, ent.registerType, ent.grantDays, ent.grantMinutes, ent.usedDays,
				ent.usedMinutes, ent.stowageDays, ent.remainingDays, ent.remaningMinutes, ent.usedPercent,
				ent.perscribedDays, ent.deductedDays, ent.workingDays);
	}

	@Override
	public List<AnnualLeaveGrantRemainingData> findByCheckState(String employeeId, int checkState) {
		List<KRcmtAnnLeaRemain> entities = this.queryProxy().query(QUERY_WITH_EMPID_CHECKSTATE, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId).setParameter("checkState", checkState).getList();
		return entities.stream().map(ent -> toDomain(ent)).collect(Collectors.toList());
	}

	@Override
	public void add(AnnualLeaveGrantRemainingData data) {
		if(data != null) {
		KRcmtAnnLeaRemain entity = new KRcmtAnnLeaRemain();
		entity.annLeavID = data.getAnnLeavID();
		entity.sid = data.getEmployeeId();
		entity.cid = data.getCid();
		updateValue(entity, data);

		this.commandProxy().insert(entity);
		}
	}

	@Override
	public void update(AnnualLeaveGrantRemainingData data) {
		if (data != null) {
			Optional<KRcmtAnnLeaRemain> entityOpt = this.queryProxy().find(data.getAnnLeavID(),KRcmtAnnLeaRemain.class);
			if (entityOpt.isPresent()) {
				KRcmtAnnLeaRemain entity = entityOpt.get();
				updateValue(entity, data);
				this.commandProxy().update(entity);
			}
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
			entity.perscribedDays = conditionInfo.getPrescribedDays().v();
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
		this.getEntityManager().createQuery(DELETE_QUERY).setParameter("employeeId", employeeId)
				.setParameter("grantDate", grantDate);
	}

	@Override
	public void delete(String annaLeavID) {
		Optional<KRcmtAnnLeaRemain> entity = this.queryProxy().find(annaLeavID, KRcmtAnnLeaRemain.class);
		if (entity.isPresent()) {
			this.commandProxy().remove(KRcmtAnnLeaRemain.class, annaLeavID);
		}
	}

	@Override
	public Optional<AnnualLeaveGrantRemainingData> findByID(String id) {
		Optional<KRcmtAnnLeaRemain> entity = this.queryProxy().find(id, KRcmtAnnLeaRemain.class);
		if (entity.isPresent()) {
			return Optional.ofNullable(toDomain(entity.get()));
		}
		return Optional.empty();
	}

	@Override
	public List<AnnualLeaveGrantRemainingData> find(String employeeId, GeneralDate grantDate) {
		String sql = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId AND a.grantDate = :grantDate";
		return this.queryProxy().query(sql, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId).setParameter("grantDate", grantDate)
				.getList(e -> toDomain(e));
	}


	@Override
	public void deleteAfterDate(String employeeId, GeneralDate date) {
		this.getEntityManager().createQuery(DELETE_AFTER_QUERY).setParameter("employeeId", employeeId)
				.setParameter("startDate", date);
	}


	@Override
	public List<AnnualLeaveGrantRemainingData> checkConditionUniqueForAdd(String employeeId, GeneralDate grantDate) {
		return this.queryProxy().query(CHECK_UNIQUE_SID_GRANTDATE_FOR_ADD, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("grantDate", grantDate).getList(e -> toDomain(e));
	}


	@Override
	public List<AnnualLeaveGrantRemainingData> checkConditionUniqueForUpdate(String employeeId, String annLeavID,
			GeneralDate grantDate) {
		return this.queryProxy().query(CHECK_UNIQUE_SID_GRANTDATE_FOR_UPDATE, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("annLeavID", annLeavID)
				.setParameter("grantDate", grantDate).getList(e -> toDomain(e));
	}

	@Override
	public List<AnnualLeaveGrantRemainingData> findByPeriod(String employeeId, GeneralDate startDate, GeneralDate endDate) {
		String sql = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId AND a.grantDate >= :startDate AND a.grantDate <= :endDate";
		return this.queryProxy().query(sql, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(e -> toDomain(e));
	}

	@Override
	public List<AnnualLeaveGrantRemainingData> findByGrantDateAndDeadline(String employeeId, GeneralDate grantDate, GeneralDate deadline) {
		String sql = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid = :employeeId AND a.grantDate < :grantDate AND a.deadline >= :deadline";
		return this.queryProxy().query(sql, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("grantDate", grantDate)
				.setParameter("deadline", deadline)
				.getList(e -> toDomain(e));
	}
	/**
	 * @author yennth
	 */
	@Override
	public List<AnnualLeaveGrantRemainingData> findInDate(String employeeId, GeneralDate startDate, GeneralDate endDate) {
		List<KRcmtAnnLeaRemain> entities = this.queryProxy().query(FIND_BY_EMP_AND_DATE, KRcmtAnnLeaRemain.class)
				.setParameter("employeeId", employeeId) 
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList();
		return entities.stream().map(ent -> toDomain(ent)).collect(Collectors.toList());
	}


	@Override
	public Map<String, List<AnnualLeaveGrantRemainingData>> findInDate(List<String> employeeId, GeneralDate startDate,
			GeneralDate endDate) {
		if (employeeId.isEmpty())
			return Collections.emptyMap();
		String query = "SELECT a FROM KRcmtAnnLeaRemain a WHERE a.sid IN :employeeId AND a.grantDate >= :startDate AND a.grantDate <= :endDate ORDER BY a.grantDate DESC";
		List<AnnualLeaveGrantRemainingData> result = new ArrayList<>();
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
			result.addAll(this.queryProxy().query(query, KRcmtAnnLeaRemain.class).setParameter("employeeId", subIdList)
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate)
					.getList(ent -> toDomain(ent)));
		});
		return result.stream().collect(Collectors.groupingBy(i -> i.getEmployeeId()));
	}

}
