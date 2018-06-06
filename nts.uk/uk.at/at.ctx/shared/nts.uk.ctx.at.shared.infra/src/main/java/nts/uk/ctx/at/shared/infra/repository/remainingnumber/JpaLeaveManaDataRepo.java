package nts.uk.ctx.at.shared.infra.repository.remainingnumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.error.BusinessException;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.DigestionAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManagementData;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.subhdmana.KrcmtLeaveManaData;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaLeaveManaDataRepo extends JpaRepository implements LeaveManaDataRepository {

	private String QUERY_BYSID = "SELECT l FROM KrcmtLeaveManaData l WHERE l.cID = :cid AND l.sID =:employeeId ";

	private String QUERY_BYSIDWITHSUBHDATR = String.join(" ", QUERY_BYSID, "AND l.subHDAtr =:subHDAtr");
	
	private String QUERY_LEAVEDAYOFF = String.join(" ", QUERY_BYSID, "AND l.leaveID IN (SELECT b.krcmtLeaveDayOffManaPK.leaveID FROM KrcmtLeaveDayOffMana b WHERE b.krcmtLeaveDayOffManaPK.comDayOffID = :comDayOffID )");
	
	private String QUERY_BYSIDANDHOLIDAYDATECONDITION = "SELECT l FROM KrcmtLeaveManaData l WHERE l.cID = :cid AND l.sID =:employeeId AND l.dayOff = :dateHoliday";

	private String QUERY_BYSID_AND_NOT_UNUSED = String.join(" ", QUERY_BYSID, "AND l.subHDAtr !=:subHDAtr");

	private String QUERY_BYID = "SELECT l FROM KrcmtLeaveManaData l WHERE l.leaveID IN :leaveIDs";
	
	
	private String QUERY_BY_DAYOFF_PERIOD = "SELECT c FROM KrcmtLeaveManaData c"
			+ " WHERE c.sID = :sid"
			+ " AND c.dayOff >= :startDate"
			+ " AND c.dayOff <= :endDate";
	private String QUERY_BY_EX = QUERY_BY_DAYOFF_PERIOD
			+ " AND (c.unUsedDays > :unUsedDays AND c.expiredDate >= :sDate AND c.expiredDate <= :eDate)"
			+ " OR (c.subHDAtr = :subHDAtr AND c.disapearDate >= :sDate AND c.disapearDate <= :eDate)";
	@Override
	public List<LeaveManagementData> getBySidWithsubHDAtr(String cid, String sid, int state) {
		List<KrcmtLeaveManaData> listListMana = this.queryProxy()
				.query(QUERY_BYSIDWITHSUBHDATR, KrcmtLeaveManaData.class).setParameter("cid", cid)
				.setParameter("employeeId", sid).setParameter("subHDAtr", state).getList();
		return listListMana.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public List<LeaveManagementData> getBySid(String cid, String sid) {
		List<KrcmtLeaveManaData> listListMana = this.queryProxy().query(QUERY_BYSID, KrcmtLeaveManaData.class)
				.setParameter("cid", cid).setParameter("employeeId", sid).getList();
		return listListMana.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	/**
	 * Convert to domain
	 * 
	 * @param entity
	 * @return
	 */
	private LeaveManagementData toDomain(KrcmtLeaveManaData entity) {
		return new LeaveManagementData(entity.leaveID, entity.cID, entity.sID, entity.unknownDate, entity.dayOff,
				entity.expiredDate, entity.occurredDays, entity.occurredTimes, entity.unUsedDays, entity.unUsedTimes,
				entity.subHDAtr, entity.fullDayTime, entity.halfDayTime);
	}

	private KrcmtLeaveManaData toEntity(LeaveManagementData domain) {
		KrcmtLeaveManaData entity = new KrcmtLeaveManaData();
		entity.leaveID = domain.getID();
		entity.cID = domain.getCID();
		entity.sID = domain.getSID();
		entity.unknownDate = domain.getComDayOffDate().isUnknownDate();
		entity.dayOff = domain.getComDayOffDate().getDayoffDate().isPresent()
				? domain.getComDayOffDate().getDayoffDate().get() : null;
		entity.expiredDate = domain.getExpiredDate();
		entity.occurredDays = domain.getOccurredDays().v();
		entity.occurredTimes = domain.getOccurredTimes().v();
		entity.unUsedDays = domain.getUnUsedDays().v();
		entity.unUsedTimes = domain.getUnUsedTimes().v();
		entity.subHDAtr = domain.getSubHDAtr().value;
		entity.fullDayTime = domain.getFullDayTime().v();
		entity.halfDayTime = domain.getHalfDayTime().v();
		return entity;
	}

	@Override
	public void create(LeaveManagementData domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public List<LeaveManagementData> getByDateCondition(String cid, String sid, GeneralDate startDate,
			GeneralDate endDate) {
		List<KrcmtLeaveManaData> listLeaveData = new ArrayList<>();
		String query = "";
		if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
			query = "SELECT a FROM KrcmtLeaveManaData a WHERE a.cID = :cid AND a.sID =:employeeId AND a.dayOff >= :startDate AND a.dayOff <= :endDate OR "
					+ "a.leaveID IN (SELECT c.krcmtLeaveDayOffManaPK.leaveID FROM KrcmtLeaveDayOffMana c INNER JOIN KrcmtComDayoffMaData b ON c.krcmtLeaveDayOffManaPK.comDayOffID = b.comDayOffID WHERE b.cID = :cid AND b.sID =:employeeId AND b.dayOff >= :startDate AND b.dayOff <= :endDate )";
			listLeaveData = this.queryProxy().query(query, KrcmtLeaveManaData.class).setParameter("cid", cid)
					.setParameter("employeeId", sid).setParameter("startDate", startDate)
					.setParameter("endDate", endDate).getList();
		} else if (!Objects.isNull(startDate)) {
			query = "SELECT a FROM KrcmtLeaveManaData a WHERE a.cID = :cid AND a.sID =:employeeId AND a.dayOff >= :startDate OR "
					+ "a.leaveID IN (SELECT c.krcmtLeaveDayOffManaPK.leaveID FROM KrcmtLeaveDayOffMana c INNER JOIN KrcmtComDayoffMaData b ON c.krcmtLeaveDayOffManaPK.comDayOffID = b.comDayOffID WHERE b.cID = :cid AND b.sID =:employeeId AND b.dayOff >= :startDate )";
			listLeaveData = this.queryProxy().query(query, KrcmtLeaveManaData.class).setParameter("cid", cid)
					.setParameter("employeeId", sid).setParameter("startDate", startDate)
					.getList();
		} else if (!Objects.isNull(endDate)) {
			query = "SELECT a FROM KrcmtLeaveManaData a WHERE a.cID = :cid AND a.sID =:employeeId AND a.dayOff <= :endDate OR "
					+ "a.leaveID IN (SELECT c.krcmtLeaveDayOffManaPK.leaveID KrcmtLeaveDayOffMana c INNER JOIN KrcmtComDayoffMaData b ON c.krcmtLeaveDayOffManaPK.comDayOffID = b.comDayOffID WHERE b.cID = :cid AND b.sID =:employeeId AND b.dayOff <= :endDate )";
			listLeaveData = this.queryProxy().query(query, KrcmtLeaveManaData.class).setParameter("cid", cid)
					.setParameter("employeeId", sid).setParameter("endDate", endDate)
					.getList();
		} else {
			query = "SELECT a FROM KrcmtLeaveManaData a WHERE a.cID = :cid AND a.sID =:employeeId OR "
					+ "a.leaveID IN (SELECT c.krcmtLeaveDayOffManaPK.leaveID FROM KrcmtLeaveDayOffMana c INNER JOIN KrcmtComDayoffMaData b ON c.krcmtLeaveDayOffManaPK.comDayOffID = b.comDayOffID WHERE b.cID = :cid AND b.sID =:employeeId )";
			listLeaveData = this.queryProxy().query(query, KrcmtLeaveManaData.class).setParameter("cid", cid)
					.setParameter("employeeId", sid).getList();
		}
		return listLeaveData.stream().map(x -> toDomain(x)).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.
	 * LeaveManaDataRepository#getBySidWithHolidayDate(java.lang.String,
	 * java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<LeaveManagementData> getBySidWithHolidayDate(String cid, String sid, GeneralDate dateHoliday) {
		List<KrcmtLeaveManaData> listLeaveData = this.queryProxy()
				.query(QUERY_BYSIDANDHOLIDAYDATECONDITION, KrcmtLeaveManaData.class).setParameter("cid", cid)
				.setParameter("employeeId", sid).setParameter("dateHoliday", dateHoliday).getList();
		return listLeaveData.stream().map(x -> toDomain(x)).collect(Collectors.toList());
	}

	@Override
	public List<LeaveManagementData> getBySidNotUnUsed(String cid, String sid) {
		List<KrcmtLeaveManaData> listListMana = this.queryProxy()
				.query(QUERY_BYSID_AND_NOT_UNUSED, KrcmtLeaveManaData.class).setParameter("cid", cid)
				.setParameter("employeeId", sid).setParameter("subHDAtr", 0).getList();
		return listListMana.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public List<LeaveManagementData> getByComDayOffId(String cid, String sid, String comDayOffID) {
		List<KrcmtLeaveManaData> listListMana = this.queryProxy()
				.query(QUERY_LEAVEDAYOFF, KrcmtLeaveManaData.class).setParameter("cid", cid)
				.setParameter("employeeId", sid).setParameter("comDayOffID", comDayOffID).getList();
		return listListMana.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public void updateByLeaveIds(List<String> leaveIds) {
		List<KrcmtLeaveManaData> listListMana = this.queryProxy()
				.query(QUERY_BYID, KrcmtLeaveManaData.class)
				.setParameter("leaveIDs", leaveIds).getList();
		for(KrcmtLeaveManaData busItem: listListMana){
			busItem.subHDAtr =  DigestionAtr.USED.value;
		}
		this.commandProxy().updateAll(listListMana);
	}
	
	@Override
	public void updateSubByLeaveId(List<String> leaveIds) {
		List<KrcmtLeaveManaData> listListMana = this.queryProxy()
				.query(QUERY_BYID, KrcmtLeaveManaData.class)
				.setParameter("leaveIDs", leaveIds).getList();
		for(KrcmtLeaveManaData busItem: listListMana){
			busItem.subHDAtr =  DigestionAtr.UNUSED.value;
			busItem.unUsedDays = 1.0;
		}
		this.commandProxy().updateAll(listListMana);
	}
	
	
	@Override
	public void updateUnUseDayLeaveId(String leaveId,Double unUsedDay) {
		KrcmtLeaveManaData leaveMana =  this.getEntityManager().find(KrcmtLeaveManaData.class, leaveId);
		leaveMana.unUsedDays = unUsedDay;
		this.commandProxy().update(leaveMana);
	}
	
		
	public Optional<LeaveManagementData> getByLeaveId(String leaveManaId) {
		KrcmtLeaveManaData entity = this.getEntityManager().find(KrcmtLeaveManaData.class, leaveManaId);
		return Optional.ofNullable(toDomain(entity));
	}

	@Override
	public void udpate(LeaveManagementData domain) {
		this.commandProxy().update(this.toEntity(domain));
	}

	@Override
	public void deleteByLeaveId(String leaveId) {
		KrcmtLeaveManaData entity = this.getEntityManager().find(KrcmtLeaveManaData.class, leaveId);
		if(Objects.isNull(entity)){
			throw new BusinessException("Msg_198");
		}
		this.commandProxy().remove(entity);
	}

	@Override
	public List<LeaveManagementData> getByDayOffDatePeriod(String sid, DatePeriod dateData) {
		List<KrcmtLeaveManaData> listListMana = this.queryProxy().query(QUERY_BY_DAYOFF_PERIOD, KrcmtLeaveManaData.class)
				.setParameter("sid", sid)
				.setParameter("startDate", dateData.start())
				.setParameter("endDate", dateData.end())
				.getList();
		return listListMana.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public List<LeaveManagementData> getByExtinctionPeriod(String sid, DatePeriod tmpDateData,DatePeriod dateData, Double unUseDays,
			DigestionAtr subHDAtr) {
		List<KrcmtLeaveManaData> listListMana = this.queryProxy()
				.query(QUERY_BY_EX, KrcmtLeaveManaData.class)
				.setParameter("sid", sid)
				.setParameter("startDate", tmpDateData.start())
				.setParameter("endDate", tmpDateData.end())
				.setParameter("unUsedDays", unUseDays)
				.setParameter("sDate", dateData.start())
				.setParameter("eDate", dateData.end())
				.setParameter("subHDAtr", subHDAtr.value)
				.getList();
		return listListMana.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}
}
