package nts.uk.ctx.at.shared.infra.repository.remainingnumber;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRepository;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.KrcmtSpecialLeaveReam;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaSpecialLeaveGrantRepo extends JpaRepository implements SpecialLeaveGrantRepository {

	private static final String GET_ALL_BY_SID_SPECIALCODE = "SELECT a FROM KrcmtSpecialLeaveReam a WHERE a.employeeId = :employeeId AND a.specialLeaCode = :specialLeaCode order by a.grantDate DESC";

	private static final String GET_ALL_BY_SID_SPECIALCODE_STATUS = "SELECT a FROM KrcmtSpecialLeaveReam a WHERE a.employeeId = :employeeId AND a.specialLeaCode = :specialLeaCode AND a.expStatus = :expStatus order by a.grantDate";
	private static final String GET_ALL_BY_SID_AND_GRANT_DATE = "SELECT a FROM KrcmtSpecialLeaveReam a WHERE a.employeeId = :sid AND a.grantDate =:grantDate AND a.specialLeaID !=:specialLeaID AND a.specialLeaCode =:specialLeaCode";

	@Override
	public List<SpecialLeaveGrantRemainingData> getAll(String employeeId, int specialCode) {
		List<KrcmtSpecialLeaveReam> entities = this.queryProxy()
				.query(GET_ALL_BY_SID_SPECIALCODE, KrcmtSpecialLeaveReam.class).setParameter("employeeId", employeeId)
				.setParameter("specialLeaCode", specialCode).getList();

		return entities.stream()
				.map(x -> SpecialLeaveGrantRemainingData.createFromJavaType(x.specialLeaID, x.cId, x.employeeId,
						x.specialLeaCode, x.grantDate, x.deadlineDate, x.expStatus, x.registerType, x.numberDayGrant,
						x.timeGrant, x.numberDayUse, x.timeUse, x.useSavingDays, x.numberOverDays, x.timeOver,
						x.numberDayRemain, x.timeRemain))
				.collect(Collectors.toList());
	}

	@Override
	public void add(SpecialLeaveGrantRemainingData data) {
		if (data != null)
			this.commandProxy().insert(toEntity(data));
	}

	@Override
	public void update(SpecialLeaveGrantRemainingData data) {
		if (data != null) {
			Optional<KrcmtSpecialLeaveReam> entityOpt = this.queryProxy().find(data.getSpecialId(),
					KrcmtSpecialLeaveReam.class);
			if (entityOpt.isPresent()) {
				KrcmtSpecialLeaveReam entity = entityOpt.get();
				updateDetail(entity, data);
				this.commandProxy().update(entity);
			}
		}
	}

	@Override
	public void delete(String specialid) {

		Optional<KrcmtSpecialLeaveReam> entityOpt = this.queryProxy().find(specialid, KrcmtSpecialLeaveReam.class);
		if (entityOpt.isPresent()) {
			KrcmtSpecialLeaveReam entity = entityOpt.get();
			this.commandProxy().remove(entity);
		}
	}
	@SneakyThrows
	@Override
	public Optional<SpecialLeaveGrantRemainingData> getBySpecialId(String specialId) {
		try (PreparedStatement sql = this.connection().prepareStatement(
				"SELECT * FROM KRCMT_SPEC_LEAVE_REMAIN"
				+ " WHERE SPECIAL_LEAVE_ID = ?")) {

			sql.setString(1, specialId);
			Optional<SpecialLeaveGrantRemainingData> entities = new NtsResultSet(sql.executeQuery())
					.getSingle(x -> toDomain(x));
			if(!entities.isPresent()) {
				return Optional.empty();
			}
			return entities;	
		}
			
	}
	
	private SpecialLeaveGrantRemainingData toDomain(NtsResultRecord  record) {
		return SpecialLeaveGrantRemainingData.createFromJavaType(record.getString("SPECIAL_LEAVE_ID"),
				record.getString("CID"),
				record.getString("SID"),
				record.getInt("SPECIAL_LEAVE_CD"),
				record.getGeneralDate("GRANT_DATE"),
				record.getGeneralDate("DEADLINE_DATE"),
				record.getInt("EXPIRED_STATE"),
				record.getInt("REGISTRATION_TYPE"),
				record.getBigDecimal("NUMBER_DAYS_GRANT") == null ? 0.0 : record.getBigDecimal("NUMBER_DAYS_GRANT").doubleValue(),
				record.getInt("TIME_GRANT"),
				record.getBigDecimal("NUMBER_DAYS_USE") == null ? 0.0 : record.getBigDecimal("NUMBER_DAYS_USE").doubleValue(),
				record.getInt("TIME_USE"),
				record.getBigDecimal("USED_SAVING_DAYS") == null ? 0.0 : record.getBigDecimal("USED_SAVING_DAYS").doubleValue(),
				record.getBigDecimal("NUMBER_OVER_DAYS") == null ? 0.0 : record.getBigDecimal("NUMBER_OVER_DAYS").doubleValue(),
				record.getInt("TIME_OVER"),
				record.getBigDecimal("NUMBER_DAYS_REMAIN") == null ? 0.0 : record.getBigDecimal("NUMBER_DAYS_REMAIN").doubleValue(),
				record.getInt("TIME_REMAIN"));
	}

	private void updateDetail(KrcmtSpecialLeaveReam entity, SpecialLeaveGrantRemainingData data) {
		entity.employeeId = data.getEmployeeId();
		entity.cId = data.getCId();
		entity.specialLeaCode = data.getSpecialLeaveCode().v();

		entity.grantDate = data.getGrantDate();
		entity.deadlineDate = data.getDeadlineDate();
		entity.expStatus = data.getExpirationStatus().value;
		entity.registerType = data.getRegisterType().value;
		// grant data
		entity.numberDayGrant = data.getDetails().getGrantNumber().getDayNumberOfGrant().v();
		entity.timeGrant = data.getDetails().getGrantNumber().getTimeOfGrant().isPresent()
				? data.getDetails().getGrantNumber().getTimeOfGrant().get().v()
				: 0;
		// remain data
		entity.numberDayRemain = data.getDetails().getRemainingNumber().getDayNumberOfRemain().v();
		entity.timeRemain = data.getDetails().getRemainingNumber().getTimeOfRemain().isPresent()
				? data.getDetails().getRemainingNumber().getTimeOfRemain().get().v()
				: 0;
		// use data
		entity.numberDayUse = data.getDetails().getUsedNumber().getDayNumberOfUse().v();
		entity.timeUse = data.getDetails().getUsedNumber().getTimeOfUse().isPresent()
				? data.getDetails().getUsedNumber().getTimeOfUse().get().v()
				: 0;
		// use Saving data(tai lieu đang bảo truyền null vào)
		// entity.useSavingDays =
		// data.getDetails().getUsedNumber().getUseSavingDays().isPresent()
		// ? data.getDetails().getUsedNumber().getUseSavingDays().get().v()
		// : 0;
		
		// Over
		if (data.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().isPresent()) {
			entity.numberOverDays = data.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get()
					.getNumberOverDays().v();
			entity.timeOver = data.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getTimeOver()
					.isPresent()
							? data.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getTimeOver()
									.get().v()
							: 0;
		}

	}

	/**
	 * Convert to entity
	 * 
	 * @param domain
	 * @return
	 */
	private KrcmtSpecialLeaveReam toEntity(SpecialLeaveGrantRemainingData data) {
		KrcmtSpecialLeaveReam entity = new KrcmtSpecialLeaveReam();
		entity.cId = data.getCId();
		entity.specialLeaID = data.getSpecialId();
		entity.employeeId = data.getEmployeeId();
		entity.specialLeaCode = data.getSpecialLeaveCode().v();

		entity.expStatus = data.getExpirationStatus().value;
		entity.registerType = data.getRegisterType().value;

		entity.grantDate = data.getGrantDate();
		entity.deadlineDate = data.getDeadlineDate();

		// grant data
		entity.numberDayGrant = data.getDetails().getGrantNumber().getDayNumberOfGrant().v();
		entity.timeGrant = data.getDetails().getGrantNumber().getTimeOfGrant().isPresent()
				? data.getDetails().getGrantNumber().getTimeOfGrant().get().v()
				: 0;
		// remain data
		entity.numberDayRemain = data.getDetails().getRemainingNumber().getDayNumberOfRemain().v();
		entity.timeRemain = data.getDetails().getRemainingNumber().getTimeOfRemain().isPresent()
				? data.getDetails().getRemainingNumber().getTimeOfRemain().get().v()
				: 0;
		// use data
		entity.numberDayUse = data.getDetails().getUsedNumber().getDayNumberOfUse().v();
		entity.timeUse = data.getDetails().getUsedNumber().getTimeOfUse().isPresent()
				? data.getDetails().getUsedNumber().getTimeOfUse().get().v()
				: 0;
		// use Saving data(tai lieu đang bảo truyền null vào)
		// entity.useSavingDays =
		// data.getDetails().getUsedNumber().getUseSavingDays().isPresent()
		// ? data.getDetails().getUsedNumber().getUseSavingDays().get().v()
		// : 0;
		entity.useSavingDays = 0d;
		// Over
		if (data.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().isPresent()) {
			entity.numberOverDays = data.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get()
					.getNumberOverDays().v();
			entity.timeOver = data.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getTimeOver()
					.isPresent()
							? data.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getTimeOver()
									.get().v()
							: 0;
		}

		return entity;
	}

	@Override
	public List<SpecialLeaveGrantRemainingData> getAllByExpStatus(String employeeId, int specialCode,
			int expirationStatus) {
		List<KrcmtSpecialLeaveReam> entities = this.queryProxy()
				.query(GET_ALL_BY_SID_SPECIALCODE_STATUS, KrcmtSpecialLeaveReam.class)
				.setParameter("employeeId", employeeId).setParameter("specialLeaCode", specialCode)
				.setParameter("expStatus", expirationStatus).getList();

		return entities.stream()
				.map(x -> SpecialLeaveGrantRemainingData.createFromJavaType(x.specialLeaID, x.cId, x.employeeId,
						x.specialLeaCode, x.grantDate, x.deadlineDate, x.expStatus, x.registerType, x.numberDayGrant,
						x.timeGrant, x.numberDayUse, x.timeUse, x.useSavingDays, x.numberOverDays, x.timeOver,
						x.numberDayRemain, x.timeRemain))
				.collect(Collectors.toList());
	}
	@SneakyThrows
	@Override
	public List<SpecialLeaveGrantRemainingData> getByPeriodStatus(String sid, int specialLeaveCode,
			LeaveExpirationStatus expirationStatus, GeneralDate grantDate, GeneralDate deadlineDate) {
			
		try (PreparedStatement sql = this.connection().prepareStatement("SELECT * FROM KRCMT_SPEC_LEAVE_REMAIN"
						+ " WHERE SID = ?"
						+ " AND SPECIAL_LEAVE_CD = ?"
						+ " AND GRANT_DATE <= ?"
						+ " AND DEADLINE_DATE >= ?"
						+ " AND EXPIRED_STATE = ?"
						+ " ORDER BY GRANT_DATE ASC")){

			sql.setString(1, sid);
			sql.setInt(2, specialLeaveCode);
			sql.setDate(3, Date.valueOf(grantDate.toLocalDate()));
			sql.setDate(4, Date.valueOf(deadlineDate.toLocalDate()));
			sql.setInt(5, expirationStatus.value);
			List<SpecialLeaveGrantRemainingData> entities = new NtsResultSet(sql.executeQuery())
					.getList(x -> toDomain(x));
			if(entities.isEmpty()) {
				return Collections.emptyList();
			}
			return entities;
		}
		
	}

	@Override
	public boolean isHasData(String sid, String specialId, GeneralDate grantDate, int specialLeaCode) {
		//GET_ALL_BY_SID_AND_GRANT_DATE
		List<KrcmtSpecialLeaveReam> specialLeave = this.queryProxy().query(GET_ALL_BY_SID_AND_GRANT_DATE, KrcmtSpecialLeaveReam.class)
				.setParameter("sid", sid)
				.setParameter("grantDate", grantDate)
				.setParameter("specialLeaID", specialId)
				.setParameter("specialLeaCode", specialLeaCode)
				.getList();
		if(specialLeave.size()> 0) {
			return true;
		}		
		return false;
	}
	@SneakyThrows
	@Override
	public List<SpecialLeaveGrantRemainingData> getByNextDate(String sid, int speCode, DatePeriod datePriod,
			GeneralDate startDate, LeaveExpirationStatus expirationStatus) {
		try (PreparedStatement sql = this.connection().prepareStatement("SELECT * FROM KRCMT_SPEC_LEAVE_REMAIN"
				+ " WHERE SID = ?"
				+ " AND SPECIAL_LEAVE_CD = ?"
				+ " AND GRANT_DATE > ?"
				+ " AND GRANT_DATE <= ?"
				+ " AND DEADLINE_DATE >= ?"
				+ " AND EXPIRED_STATE = ?"
				+ " ORDER BY GRANT_DATE ASC")){

			sql.setString(1, sid);
			sql.setInt(2, speCode);
			sql.setDate(3, Date.valueOf(datePriod.start().toLocalDate()));
			sql.setDate(4, Date.valueOf(datePriod.end().toLocalDate()));
			sql.setDate(5, Date.valueOf(startDate.toLocalDate()));
			sql.setInt(6, expirationStatus.value);
			List<SpecialLeaveGrantRemainingData> entities = new NtsResultSet(sql.executeQuery())
					.getList(x -> toDomain(x));
			if(entities.isEmpty()) {
				return Collections.emptyList();
			}
			return entities;
		}
	}

}
