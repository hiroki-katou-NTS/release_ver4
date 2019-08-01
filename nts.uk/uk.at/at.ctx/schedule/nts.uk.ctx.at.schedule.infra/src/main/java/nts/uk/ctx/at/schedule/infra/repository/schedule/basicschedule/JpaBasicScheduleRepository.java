/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.tuple.Pair;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
//import nts.gul.text.StringUtil;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.ConfirmedAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.childcareschedule.ChildCareSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.personalfee.WorkSchedulePersonFee;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workschedulebreak.WorkScheduleBreak;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletime.PersonFeeTime;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletime.WorkScheduleTime;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone.WorkScheduleTimeZone;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.ScheMasterInfo;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdtBasicSchedule;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdtBasicSchedulePK;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscdtScheChildCare;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscdtScheChildCarePK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscdtScheChildCare_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFee;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFeePK_;
//import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFeeTime;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFee_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workschedulebreak.KscdtWorkScheduleBreak;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workschedulebreak.KscdtWorkScheduleBreakPK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workschedulebreak.KscdtWorkScheduleBreak_;
//import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletime.KscdtScheTime;
//import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletime.KscdtScheTimePK;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletimezone.KscdtWorkScheduleTimeZone;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletimezone.KscdtWorkScheduleTimeZonePK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletimezone.KscdtWorkScheduleTimeZone_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.schedulemaster.KscdtScheMasterInfo;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.childcareschedule.JpaChildCareScheduleGetMemento;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.childcareschedule.JpaChildCareScheduleSetMememto;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.personalfee.JpaWorkSchedulePersonFeeGetMemento;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

/**
 * The Class JpaBasicScheduleRepository.
 */
@Stateless
public class JpaBasicScheduleRepository extends JpaRepository implements BasicScheduleRepository {
		
	public static final String GET_LIST_DATE_BY_LIST_SID = "SELECT a.kscdpBSchedulePK.date "
			+ "FROM KscdtBasicSchedule a " + "WHERE a.kscdpBSchedulePK.sId IN :sIds "
			+ "ORDER BY a.kscdpBSchedulePK.date DESC";

	private static final String QUERY_BY_SID_PERIOD = "SELECT c FROM KscdtBasicSchedule c"
			+ " WHERE c.kscdpBSchedulePK.sId = :employeeId" + " AND c.kscdpBSchedulePK.date >= :startDate"
			+ " AND c.kscdpBSchedulePK.date <= :endDate";

	public static final String GET_LIST_BY_LIST_SID_DATE = "SELECT a " + "FROM KscdtBasicSchedule a "
			+ "WHERE a.kscdpBSchedulePK.sId IN :sIds "
			+ "AND (a.kscdpBSchedulePK.date BETWEEN :startDate AND :endDate)";
	
	public static final String GET_BY_LIST_DATE = "SELECT c FROM KscdtBasicSchedule c"
			+ " WHERE c.kscdpBSchedulePK.sId = :employeeId"
			+ " AND c.kscdpBSchedulePK.date IN :dates";

	@Override
	public void insert(BasicSchedule bSchedule) {
		this.insertScheBasic(bSchedule);
		this.insertRelateToWorkTimeCd(bSchedule);
		this.insertScheduleMaster(bSchedule.getWorkScheduleMaster());
		this.insertAllScheduleState(bSchedule.getWorkScheduleStates());
	}

	private void insertRelateToWorkTimeCd(BasicSchedule bSchedule) {
		String employeeId = bSchedule.getEmployeeId();
		GeneralDate date = bSchedule.getDate();
		List<WorkScheduleTimeZone> list = new ArrayList<>();
		bSchedule.getWorkScheduleTimeZones().stream()
				.filter(map -> (map.getScheduleStartClock() != null && map.getScheduleEndClock() != null))
				.map(map -> list.add(map)).collect(Collectors.toList());
		if (list.size() > 0) {
			this.insertAllWorkScheduleTimeZone(employeeId, bSchedule.getDate(), list);
		}
		this.insertScheduleBreakTime(employeeId, date, bSchedule.getWorkScheduleBreaks());
		this.insertScheduleTime(employeeId, date, bSchedule.getWorkScheduleTime());
		this.insertAllChildCare(employeeId, date, bSchedule.getChildCareSchedules());
	}

	private void insertScheBasic(BasicSchedule bSchedule) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String workTimeCode = bSchedule.getWorkTimeCode() != null ? "'" + bSchedule.getWorkTimeCode() + "'" : null;
		
		String sqlQuery = "INSERT INTO KSCDT_SCHE_BASIC (SID, YMD, WORKTYPE_CD, WORKTIME_CD, CONFIRMED_ATR) VALUES (" + "'"
				+ bSchedule.getEmployeeId() + "', " + "'"
				+ bSchedule.getDate() + "', " + "'"
				+ bSchedule.getWorkTypeCode() + "', " + workTimeCode + ", "
				+ bSchedule.getConfirmedAtr().value + ")";
		try {
			con.createStatement().executeUpdate(JDBCUtil.toInsertWithCommonField(sqlQuery));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * follow document phrase 3 KSU001, kscdtScheChildCare and
	 * kscdtScheMasterInfo no need update
	 */
	@Override
	public void update(BasicSchedule bSchedule) {
		String employeeId = bSchedule.getEmployeeId();
		GeneralDate date = bSchedule.getDate();
		this.updateScheBasic(bSchedule);
		
		this.removeAllChildCare(employeeId, date);
		this.insertAllChildCare(employeeId, date, bSchedule.getChildCareSchedules());
		
		this.removeAllTimeZone(employeeId, date);
		this.insertAllWorkScheduleTimeZone(employeeId, date, bSchedule.getWorkScheduleTimeZones());
		
		this.removeAllScheduleBreakTime(employeeId, date);
		this.insertScheduleBreakTime(employeeId, date, bSchedule.getWorkScheduleBreaks());
		
		this.updateScheduleTime(employeeId, date, bSchedule.getWorkScheduleTime());
		this.updateScheduleMaster(bSchedule.getWorkScheduleMaster());
		
		this.removeScheState(employeeId, date, bSchedule.getWorkScheduleStates());
		this.insertAllScheduleState(bSchedule.getWorkScheduleStates());
	}
	
	private void updateScheBasic(BasicSchedule bSchedule) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String workTimeCode = bSchedule.getWorkTimeCode() != null ? "'" + bSchedule.getWorkTimeCode() + "'" : null;
		String sqlQuery = "Update KSCDT_SCHE_BASIC Set WORKTYPE_CD = " + "'" + bSchedule.getWorkTypeCode() + "', "
				+ "WORKTIME_CD = " + workTimeCode + ", " + "CONFIRMED_ATR = " + bSchedule.getConfirmedAtr().value
				+ " Where SID = " + "'" + bSchedule.getEmployeeId() + "'" + " and YMD = " + "'"
				+ bSchedule.getDate() + "'";
		try {
			con.createStatement().executeUpdate(JDBCUtil.toUpdateWithCommonField(sqlQuery));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void changeWorkTypeTime(String sId, GeneralDate date, String workTypeCode, String workTimeCode) {
		Optional<KscdtBasicSchedule> optionalEntity = this.findById(sId, date);
		if (!optionalEntity.isPresent()) {
			return;
		}
		KscdtBasicSchedule schedule = optionalEntity.get();
		schedule.workTypeCode = workTypeCode;
		schedule.workTimeCode = workTimeCode;
		this.commandProxy().update(schedule);
		this.getEntityManager().flush();
	}

	/**

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #delete(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public void delete(String employeeId, GeneralDate baseDate, BasicSchedule basicSchedule) {
		this.removeScheduleBasic(employeeId, baseDate);
		this.removeAllTimeZone(employeeId, baseDate);
		this.removeAllChildCare(employeeId, baseDate);
		this.removeAllScheduleBreakTime(employeeId, baseDate);
		this.removeScheduleTime(employeeId, baseDate);
		this.removeScheduleMaster(employeeId, baseDate);
		this.removeScheStateWithoutListScheState(employeeId, baseDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #find(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<BasicSchedule> find(String sId, GeneralDate date) {
		Optional<KscdtBasicSchedule> optionalEntity = this.findById(sId, date);
		if (optionalEntity.isPresent()) {
			BasicSchedule basicSchedule = this.toDomain(optionalEntity.get(),
					this.findAllWorkScheduleTimeZone(sId, date));
			basicSchedule.setWorkScheduleMaster(toDomainScheMaster(optionalEntity.get().getKscdtScheMasterInfo()));
			return Optional.of(basicSchedule);
		}
		return Optional.empty();
	}
	
	@Override
	public List<BasicSchedule> findSomePropertyWithJDBC(List<String> listSid, DatePeriod datePeriod) {
		List<BasicSchedule> listNewBasicSchedule = new ArrayList<>();
		Connection con = this.getEntityManager().unwrap(Connection.class);
		CollectionUtil.split(listSid, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String listEmp = "(";
			for(int i = 0; i < subList.size(); i++){
				listEmp += "'"+ subList.get(i) +"',";
			}
			// remove last , in string and add )
			listEmp = listEmp.substring(0, listEmp.length() - 1) + ")";
			
			String sqlQueryWhere = " WHERE KSCDT_SCHE_BASIC.SID IN " + listEmp + " AND KSCDT_SCHE_BASIC.YMD BETWEEN " + "'" + datePeriod.start() + "' AND '" + datePeriod.end() + "'";
	
			String sqlQuery = "SELECT KSCDT_SCHE_BASIC.SID, KSCDT_SCHE_BASIC.YMD, KSCDT_SCHE_BASIC.WORKTYPE_CD, KSCDT_SCHE_BASIC.WORKTIME_CD, KSCDT_SCHE_BASIC.CONFIRMED_ATR,"
					+ " KSCDT_SCHE_MASTER.BUSINESS_TYPE_CD, KSCDT_SCHE_MASTER.WKP_ID FROM KSCDT_SCHE_BASIC"
					+ " LEFT JOIN KSCDT_SCHE_MASTER ON KSCDT_SCHE_BASIC.SID = KSCDT_SCHE_MASTER.SID AND KSCDT_SCHE_BASIC.YMD = KSCDT_SCHE_MASTER.YMD"
					+ sqlQueryWhere;
			try {
				ResultSet rs = con.createStatement().executeQuery(sqlQuery);
				while (rs.next()) {
					String sId = rs.getString("SID");
					GeneralDate date = GeneralDate.fromString(rs.getString("YMD"), "yyyy-MM-dd");
					String workTypeCode = rs.getString("WORKTYPE_CD");
					String workTimeCode = rs.getString("WORKTIME_CD");
					int confirmAtr = rs.getInt("CONFIRMED_ATR");
					String bussinessTypeCd = rs.getString("BUSINESS_TYPE_CD");
					String wkpId = rs.getString("WKP_ID");
					
					ScheMasterInfo scheMasterInfo =  new ScheMasterInfo(bussinessTypeCd, wkpId);
					listNewBasicSchedule.add(new BasicSchedule(sId, date, workTypeCode, workTimeCode, EnumAdaptor.valueOf(confirmAtr, ConfirmedAtr.class), scheMasterInfo));
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		return listNewBasicSchedule;
	}

	@Override
	public List<BasicSchedule> findSomeChildWithJDBC(List<BasicSchedule> listBasicSchedule) {
		List<BasicScheduleFromSql> listBasicScheduleFromSql = new ArrayList<>();
		List<BasicSchedule> listNewBasicSchedule = new ArrayList<>();
		Connection con = this.getEntityManager().unwrap(Connection.class);
		int size = listBasicSchedule.size();
		String sqlQueryWhere = " WHERE (KSCDT_SCHE_BASIC.SID = " + "'" + listBasicSchedule.get(0).getEmployeeId() + "'"
				+ " AND KSCDT_SCHE_BASIC.YMD = " + "'" + listBasicSchedule.get(0).getDate() + "')";

		if (size >= 2) {
			for (int i = 1; i < size; i++) {
				sqlQueryWhere += " OR (KSCDT_SCHE_BASIC.SID = " + "'" + listBasicSchedule.get(i).getEmployeeId() + "'"
						+ " AND KSCDT_SCHE_BASIC.YMD = " + "'" + listBasicSchedule.get(i).getDate() + "')";
			}
		}

		String sqlQuery = "SELECT KSCDT_SCHE_BASIC.SID, KSCDT_SCHE_BASIC.YMD, KSCDT_SCHE_BASIC.WORKTYPE_CD, KSCDT_SCHE_BASIC.WORKTIME_CD, KSCDT_SCHE_BASIC.CONFIRMED_ATR,"
				+ " KSCDT_SCHE_TIMEZONE.CNT, KSCDT_SCHE_TIMEZONE.BOUNCE_ATR, KSCDT_SCHE_TIMEZONE.START_CLOCK as TZ_START_CLOCK, KSCDT_SCHE_TIMEZONE.END_CLOCK as TZ_END_CLOCK,"
				+ " KSCDT_SCHE_TIME.BREAK_TIME, KSCDT_SCHE_TIME.WORKING_TIME, KSCDT_SCHE_TIME.WEEKDAY_TIME, KSCDT_SCHE_TIME.PRESCRIBED_TIME, KSCDT_SCHE_TIME.TOTAL_LABOR_TIME, KSCDT_SCHE_TIME.CHILD_TIME, KSCDT_SCHE_TIME.CARE_TIME, KSCDT_SCHE_TIME.FLEX_TIME,"
				+ " KSCDT_SCHE_FEE_TIME.NO as FT_NO, KSCDT_SCHE_FEE_TIME.PERSON_FEE_TIME,"
				+ " KSCDT_SCHE_BREAK.BREAK_CNT, KSCDT_SCHE_BREAK.START_CLOCK as BT_START_CLOCK, KSCDT_SCHE_BREAK.END_CLOCK as BT_END_CLOCK,"
				+ " KSCDT_SCHE_MASTER.EMP_CD, KSCDT_SCHE_MASTER.CLS_CD, KSCDT_SCHE_MASTER.BUSINESS_TYPE_CD, KSCDT_SCHE_MASTER.JOB_ID, KSCDT_SCHE_MASTER.WKP_ID FROM KSCDT_SCHE_BASIC"
				+ " LEFT JOIN KSCDT_SCHE_TIMEZONE ON KSCDT_SCHE_BASIC.SID = KSCDT_SCHE_TIMEZONE.SID AND KSCDT_SCHE_BASIC.YMD = KSCDT_SCHE_TIMEZONE.YMD"
				+ " LEFT JOIN KSCDT_SCHE_TIME ON KSCDT_SCHE_BASIC.SID = KSCDT_SCHE_TIME.SID AND KSCDT_SCHE_BASIC.YMD = KSCDT_SCHE_TIME.YMD"
				+ " LEFT JOIN KSCDT_SCHE_FEE_TIME ON KSCDT_SCHE_BASIC.SID = KSCDT_SCHE_FEE_TIME.SID AND KSCDT_SCHE_BASIC.YMD = KSCDT_SCHE_FEE_TIME.YMD"
				+ " LEFT JOIN KSCDT_SCHE_BREAK ON KSCDT_SCHE_BASIC.SID = KSCDT_SCHE_BREAK.SID AND KSCDT_SCHE_BASIC.YMD = KSCDT_SCHE_BREAK.YMD"
				+ " LEFT JOIN KSCDT_SCHE_MASTER ON KSCDT_SCHE_BASIC.SID = KSCDT_SCHE_MASTER.SID AND KSCDT_SCHE_BASIC.YMD = KSCDT_SCHE_MASTER.YMD"
				+ sqlQueryWhere;
		try {
			ResultSet rs = con.createStatement().executeQuery(sqlQuery);
			while (rs.next()) {
				String sId = rs.getString("SID");
				GeneralDate date = GeneralDate.fromString(rs.getString("YMD"), "yyyy-MM-dd");
				String workTypeCode = rs.getString("WORKTYPE_CD");
				String workTimeCode = rs.getString("WORKTIME_CD");
				int confirmAtr = rs.getInt("CONFIRMED_ATR");

				Integer timezoneCnt = rs.getObject("CNT") == null ? null : Integer.valueOf(rs.getInt("CNT"));
				Integer bounceAtr = rs.getObject("BOUNCE_ATR") == null ? null : Integer.valueOf(rs.getInt("BOUNCE_ATR"));
				Integer timezoneStart = rs.getObject("TZ_START_CLOCK") == null ? null : Integer.valueOf(rs.getInt("TZ_START_CLOCK"));
				Integer timezoneEnd = rs.getObject("TZ_END_CLOCK") == null ? null : Integer.valueOf(rs.getInt("TZ_END_CLOCK"));

				Integer breakTime = rs.getObject("BREAK_TIME") == null ? null : Integer.valueOf(rs.getInt("BREAK_TIME"));
				Integer workingTime = rs.getObject("WORKING_TIME") == null ? null : Integer.valueOf(rs.getInt("WORKING_TIME"));
				Integer weekdayTime = rs.getObject("WEEKDAY_TIME") == null ? null : Integer.valueOf(rs.getInt("WEEKDAY_TIME"));
				Integer prescribedTime = rs.getObject("PRESCRIBED_TIME") == null ? null : Integer.valueOf(rs.getInt("PRESCRIBED_TIME"));
				Integer totalLaborTime = rs.getObject("TOTAL_LABOR_TIME") == null ? null : Integer.valueOf(rs.getInt("TOTAL_LABOR_TIME"));
				Integer childTime = rs.getObject("CHILD_TIME") == null ? null : Integer.valueOf(rs.getInt("CHILD_TIME"));
				Integer careTime = rs.getObject("CARE_TIME") == null ? null : Integer.valueOf(rs.getInt("CARE_TIME"));
				Integer flexTime = rs.getObject("FLEX_TIME") == null ? null : Integer.valueOf(rs.getInt("FLEX_TIME"));

				Integer feeTimeNo = rs.getObject("FT_NO") == null ? null : Integer.valueOf(rs.getInt("FT_NO"));
				Integer personFeeTime = rs.getObject("PERSON_FEE_TIME") == null ? null : Integer.valueOf(rs.getInt("PERSON_FEE_TIME"));

				Integer breakCnt = rs.getObject("BREAK_CNT") == null ? null : Integer.valueOf(rs.getInt("BREAK_CNT"));
				Integer breakTimeStart = rs.getObject("BT_START_CLOCK") == null ? null : Integer.valueOf(rs.getInt("BT_START_CLOCK"));
				Integer breakTimeEnd = rs.getObject("BT_END_CLOCK") == null ? null : Integer.valueOf(rs.getInt("BT_END_CLOCK"));

				String empCd = rs.getString("EMP_CD");
				String clsCd = rs.getString("CLS_CD");
				String bussinessTypeCd = rs.getString("BUSINESS_TYPE_CD");
				String jobId = rs.getString("JOB_ID");
				String wkpId = rs.getString("WKP_ID");

				listBasicScheduleFromSql.add(new BasicScheduleFromSql(sId, date, workTypeCode, workTimeCode, confirmAtr,
						timezoneCnt, bounceAtr, timezoneStart, timezoneEnd, breakTime, workingTime, weekdayTime,
						prescribedTime, totalLaborTime, childTime, careTime, flexTime, feeTimeNo, personFeeTime,  breakCnt, breakTimeStart, breakTimeEnd, empCd,
						clsCd, bussinessTypeCd, jobId, wkpId));
			}

			Map<Pair<String, GeneralDate>, List<BasicScheduleFromSql>> mapPairBasicSche = listBasicScheduleFromSql
					.stream().collect(Collectors.groupingBy(x -> Pair.of(x.getSId(), x.getDate())));
			listNewBasicSchedule.addAll(this.mapData(mapPairBasicSche));

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return listNewBasicSchedule;
	}
	
	private List<BasicSchedule> mapData(Map<Pair<String, GeneralDate>, List<BasicScheduleFromSql>> dataMap) {
		List<BasicSchedule> basics = new ArrayList<>();
		dataMap.forEach((key, value) -> {
			BasicSchedule basic = BasicSchedule.createFromJavaType(key.getLeft(), key.getRight(),
					value.get(0).getWorkTypeCode(), value.get(0).getWorkTimeCode(), value.get(0).getConfirmAtr());

			basic.setWorkScheduleTimeZones(value.stream().filter(x -> x.getTimezoneCnt() != null).map(x -> WorkScheduleTimeZone.createFromJavaType(x.getTimezoneCnt(),
					x.getTimezoneStart(), x.getTimezoneEnd(), x.getBounceAtr())).filter(distinctByKey(x -> x.getScheduleCnt())).collect(Collectors.toList()));
			
			basic.setWorkScheduleBreaks(value.stream().filter(x -> x.getBreakCnt() != null ).map(x -> WorkScheduleBreak.createFromJavaType(x.getBreakCnt(),
					x.getBreakTimeStart(), x.getBreakTimeEnd())).filter(distinctByKey(x -> x.getScheduleBreakCnt())).collect(Collectors.toList()));
			// theo nghiep vu thi thang scheTime luon tra ra gia tri (do no duoc tinh toan ra) nen k ton tai truong hop null
			// chi can 1 column null la thang scheTime k ton tai
			// hien tai dang loc scheTime theo breakTime # null
			// co ve khong dung lam nhung ke me
			List<PersonFeeTime> listPersonFeeTime = new ArrayList<>();
			listPersonFeeTime.addAll(value.stream().filter(x -> x.getFeeTimeNo() != null).map(x -> PersonFeeTime.createFromJavaType(x.getFeeTimeNo(),
					x.getPersonFeeTime())).filter(distinctByKey(x -> x.getNo())).collect(Collectors.toList()));
			basic.setWorkScheduleTime(value.stream().filter(x -> x.getBreakTime() != null).map(x -> WorkScheduleTime.createFromJavaType(listPersonFeeTime,
					x.getBreakTime(), x.getWorkingTime(), x.getWeekdayTime(), x.getPrescribedTime(),
					x.getTotalLaborTime(), x.getChildTime(), x.getCareTime(), x.getFlexTime())).findFirst().orElse(null));
			
			basic.setWorkScheduleMaster(value.stream()
					.map(x -> ScheMasterInfo.createFromJavaType(key.getLeft(), key.getRight(), x.getEmpCd(),
							x.getClsCd(), x.getBussinessTypeCd(), x.getJobId(), x.getWkpId()))
					.findFirst().orElse(null));

			basics.add(basic);
		});
		return basics;
	}
	
	private static <T> java.util.function.Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	/**
	 * 
	 * @param kscdtScheMasterEntity
	 * @return
	 */
	private ScheMasterInfo toDomainScheMaster(KscdtScheMasterInfo kscdtScheMasterEntity) {
		ScheMasterInfo scheMasterInfo = ScheMasterInfo.createFromJavaType(
				kscdtScheMasterEntity.kscdtScheMasterInfoPk.sId,
				kscdtScheMasterEntity.kscdtScheMasterInfoPk.generalDate, kscdtScheMasterEntity.employmentCd,
				kscdtScheMasterEntity.classificationCd, kscdtScheMasterEntity.businessTypeCd,
				kscdtScheMasterEntity.jobId, kscdtScheMasterEntity.workplaceId);
		return scheMasterInfo;
	}

//	private WorkScheduleTime toDomainScheduleTime(KscdtScheTime entity) {
//		if (entity == null) {
//			return null;
//		}
//
//		WorkScheduleTime workScheduleTime = WorkScheduleTime.createFromJavaType(
//				entity.getKscdtScheFeeTime().stream().map(x -> toDomainPersonFeeTime(x)).collect(Collectors.toList()),
//				entity.getBreakTime(), entity.getWorkingTime(), entity.getWeekdayTime(), entity.getPrescribedTime(),
//				entity.getTotalLaborTime(), entity.getChildTime(), entity.getCareTime(), entity.getFlexTime());
//		return workScheduleTime;
//	}

//	private PersonFeeTime toDomainPersonFeeTime(KscdtScheFeeTime entityScheFeeTime) {
//		PersonFeeTime personFeeTime = PersonFeeTime.createFromJavaType(
//				entityScheFeeTime.getKscdtScheFeeTimePK().getNo(), entityScheFeeTime.getPersonFeeTime());
//		return personFeeTime;
//	}

	@Override
	public boolean isExists(String employeeId, GeneralDate date) {
		Optional<KscdtBasicSchedule> result = findById(employeeId, date);
		return result.isPresent();
	}

	/**
	 * Find by id.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param date
	 *            the date
	 * @return the optional
	 */
	private Optional<KscdtBasicSchedule> findById(String employeeId, GeneralDate date) {
		return this.queryProxy().find(new KscdtBasicSchedulePK(employeeId, date), KscdtBasicSchedule.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #findChildCareById(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<ChildCareSchedule> findChildCareById(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSCMT_CHILD_CARE_SCH (KscmtChildCareSch SQL)
		CriteriaQuery<KscdtScheChildCare> cq = criteriaBuilder.createQuery(KscdtScheChildCare.class);

		// root data
		Root<KscdtScheChildCare> root = cq.from(KscdtScheChildCare.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.sid), employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.ymd), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by child care number asc
		cq.orderBy(criteriaBuilder
				.asc(root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.childCareNumber)));

		// create query
		TypedQuery<KscdtScheChildCare> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomainChildCare(entity))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #findPersonFeeById(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<WorkSchedulePersonFee> findPersonFeeById(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSCMT_WS_PERSON_FEE (KscmtWsPersonFee SQL)
		CriteriaQuery<KscdtScheFee> cq = criteriaBuilder.createQuery(KscdtScheFee.class);

		// root data
		Root<KscdtScheFee> root = cq.from(KscdtScheFee.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(
				criteriaBuilder.equal(root.get(KscdtScheFee_.kscdtScheFeePK).get(KscdtScheFeePK_.sid), employeeId));

		// equal year month date base date
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KscdtScheFee_.kscdtScheFeePK).get(KscdtScheFeePK_.ymd), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by no id asc
		cq.orderBy(criteriaBuilder.asc(root.get(KscdtScheFee_.kscdtScheFeePK).get(KscdtScheFeePK_.no)));

		// create query
		TypedQuery<KscdtScheFee> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomainPersonFee(entity))
				.collect(Collectors.toList());
	}

	@Override
	public List<WorkScheduleBreak> findWorkBreakTime(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KscdtWorkScheduleBreak> cq = criteriaBuilder.createQuery(KscdtWorkScheduleBreak.class);

		// root data
		Root<KscdtWorkScheduleBreak> root = cq.from(KscdtWorkScheduleBreak.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleBreak_.kscdtWorkScheduleBreakPk).get(KscdtWorkScheduleBreakPK_.sId),
				employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleBreak_.kscdtWorkScheduleBreakPk).get(KscdtWorkScheduleBreakPK_.date),
				baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KscdtWorkScheduleBreak> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomainWorkScheduleBreak(entity))
				.collect(Collectors.toList());
	}

	/**
	 * Convert entity KscdtWorkScheduleBreak to domain object WorkScheduleBreak
	 * 
	 * @param entity
	 * @return
	 */
	private WorkScheduleBreak toDomainWorkScheduleBreak(KscdtWorkScheduleBreak entity) {
		return WorkScheduleBreak.createFromJavaType(entity.kscdtWorkScheduleBreakPk.scheduleBreakCnt,
				entity.scheduleStartClock, entity.scheduleEndClock);
	}

	/**
	 * Insert all child care.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 * @param childCareSchedules
	 *            the child care schedules
	 */
	private void insertAllChildCare(String employeeId, GeneralDate baseDate,
			List<ChildCareSchedule> childCareSchedules) {
		if (CollectionUtil.isEmpty(childCareSchedules)) {
			return;
		}
		List<KscdtScheChildCare> entityChildCares = childCareSchedules.stream().map(domain -> {
			KscdtScheChildCare entity = new KscdtScheChildCare();
			domain.saveToMemento(new JpaChildCareScheduleSetMememto(entity, employeeId, baseDate));
			return entity;
		}).collect(Collectors.toList());
		this.commandProxy().insertAll(entityChildCares);
	}

	/**
	 * To domain child care.
	 *
	 * @param entity
	 *            the entity
	 * @return the child care schedule
	 */
	private ChildCareSchedule toDomainChildCare(KscdtScheChildCare entity) {
		return new ChildCareSchedule(new JpaChildCareScheduleGetMemento(entity));
	}

	/**
	 * To domain person fee.
	 *
	 * @param entity
	 *            the entity
	 * @return the work schedule person fee
	 */
	private WorkSchedulePersonFee toDomainPersonFee(KscdtScheFee entity) {
		return new WorkSchedulePersonFee(new JpaWorkSchedulePersonFeeGetMemento(entity));
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the kscdt basic schedule
	 */
//	private KscdtBasicSchedule toEntity(BasicSchedule domain) {
//		KscdtBasicSchedule entity = new KscdtBasicSchedule();
//		domain.saveToMemento(new JpaBasicScheduleSetMemento(entity));
//		return entity;
//	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the kscdt basic schedule
	 */
//	private KscdtBasicSchedule toEntityUpdate(BasicSchedule domain) {
//		KscdtBasicSchedule entity = new KscdtBasicSchedule();
//		Optional<KscdtBasicSchedule> optionalEntity = this.findById(domain.getEmployeeId(), domain.getDate());
//		entity = optionalEntity.get();
//		domain.saveToMemento(new JpaBasicScheduleSetMemento(entity));
//		entity.workTimeCode = StringUtil.isNullOrEmpty(domain.getWorkTimeCode(), true) ? null
//				: domain.getWorkTimeCode();
//
//		return entity;
//	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @param entityTimeZones
	 *            the entity time zones
	 * @return the basic schedule
	 */
	private BasicSchedule toDomain(KscdtBasicSchedule entity, List<KscdtWorkScheduleTimeZone> entityTimeZones) {
		return new BasicSchedule(new JpaBasicScheduleGetMemento(entity, entityTimeZones));
	}

	private BasicSchedule toDomain(KscdtBasicSchedule entity) {
		return new BasicSchedule(entity.kscdpBSchedulePK.sId, entity.kscdpBSchedulePK.date, entity.workTypeCode,
				entity.workTimeCode, ConfirmedAtr.valueOf(entity.confirmedAtr));
	}

	/**
	 * Insert all work schedule time zone.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 * @param list
	 *            the list
	 */
	private void insertAllWorkScheduleTimeZone(String employeeId, GeneralDate baseDate,
			List<WorkScheduleTimeZone> list) {
		if (CollectionUtil.isEmpty(list)) {
			return;
		}

		Connection con = this.getEntityManager().unwrap(Connection.class);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			String sqlQuery = "INSERT INTO KSCDT_SCHE_TIMEZONE (SID, YMD, CNT, BOUNCE_ATR, START_CLOCK, END_CLOCK) VALUES ( '"
					+ employeeId + "', " + "'" + baseDate + "', " + list.get(i).getScheduleCnt() + ", "
					+ list.get(i).getBounceAtr().value + ", " + list.get(i).getScheduleStartClock().valueAsMinutes()
					+ ", " + list.get(i).getScheduleEndClock().valueAsMinutes() + ")";

			try {
				con.createStatement().executeUpdate(JDBCUtil.toInsertWithCommonField(sqlQuery));
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	/**
	 * Removes schedule basic.
	 * @param employeeId
	 * @param baseDate
	 */
	private void removeScheduleBasic(String employeeId, GeneralDate baseDate){
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KSCDT_SCHE_BASIC Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate.toString("yyyy-MM-dd") + "'";
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Removes the all child care.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 */
	private void removeAllChildCare(String employeeId, GeneralDate baseDate) {

		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KSCDT_SCHE_CHILD_CARE Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate.toString("yyyy-MM-dd")
				+ "'";
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Removes the all time zone.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 */
	private void removeAllTimeZone(String employeeId, GeneralDate baseDate) {

		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KSCDT_SCHE_TIMEZONE Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate.toString("yyyy-MM-dd")
				+ "'";
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Find all work schedule time zone.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 * @return the list
	 */
	private List<KscdtWorkScheduleTimeZone> findAllWorkScheduleTimeZone(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSCDT_WORK_SCH_TIMEZONE (KscdtWorkScheduleTimeZone SQL)
		CriteriaQuery<KscdtWorkScheduleTimeZone> cq = criteriaBuilder.createQuery(KscdtWorkScheduleTimeZone.class);

		// root data
		Root<KscdtWorkScheduleTimeZone> root = cq.from(KscdtWorkScheduleTimeZone.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleTimeZone_.kscdtWorkScheduleTimeZonePk).get(KscdtWorkScheduleTimeZonePK_.sId),
				employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleTimeZone_.kscdtWorkScheduleTimeZonePk).get(KscdtWorkScheduleTimeZonePK_.date),
				baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KscdtWorkScheduleTimeZone> query = em.createQuery(cq);

		// exclude select
		return query.getResultList();
	}

	/**
	 * insert 勤務予定マスタ情報
	 * 
	 * @param workScheduleMaster
	 */
	private void insertScheduleMaster(ScheMasterInfo scheMasterInfo) {

		Connection con = this.getEntityManager().unwrap(Connection.class);
		
		String classificationCd = scheMasterInfo.getClassificationCd() != null
				? "'" + scheMasterInfo.getClassificationCd() + "'" : null;
		String businessTypeCd = scheMasterInfo.getBusinessTypeCd() != null
				? "'" + scheMasterInfo.getBusinessTypeCd() + "'" : null;
		// jobId, workplaceId, employmentCd trong DB khac null nhung van can check do nhieu truong hop no van = null
		String jobId = scheMasterInfo.getJobId() != null
				? "'" + scheMasterInfo.getJobId() + "'" : null;
		String workplaceId = scheMasterInfo.getWorkplaceId() != null
				? "'" + scheMasterInfo.getWorkplaceId() + "'" : null;
		String employmentCd = scheMasterInfo.getEmploymentCd() != null
				? "'" + scheMasterInfo.getEmploymentCd() + "'" : null;

		String sqlQuery = "INSERT INTO KSCDT_SCHE_MASTER (SID, YMD, EMP_CD, CLS_CD, BUSINESS_TYPE_CD, JOB_ID, WKP_ID) VALUES ("
				+ "'" + scheMasterInfo.getSId() + "', " + "'" + scheMasterInfo.getGeneralDate() + "', "
				+ employmentCd + ", " + classificationCd + ", " + businessTypeCd + ", "
				+ jobId + ", " + workplaceId + ")";
		try {
			con.createStatement().executeUpdate(JDBCUtil.toInsertWithCommonField(sqlQuery));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * update 勤務予定マスタ情報
	 * 
	 * @param workScheduleMaster
	 */
	private void updateScheduleMaster(ScheMasterInfo scheMasterInfo) {
		if (scheMasterInfo == null) {
			return;
		}

		Connection con = this.getEntityManager().unwrap(Connection.class);
		String classificationCd = scheMasterInfo.getClassificationCd() != null ? "'" + scheMasterInfo.getClassificationCd() + "'" : null;
		String businessTypeCd = scheMasterInfo.getBusinessTypeCd() != null ? "'" + scheMasterInfo.getBusinessTypeCd() + "'" : null;
		String sqlQuery = "Update KSCDT_SCHE_MASTER Set EMP_CD = " + "'" + scheMasterInfo.getEmploymentCd() + "', "
				+ "CLS_CD = " + classificationCd + ", " + "BUSINESS_TYPE_CD = "
				+ businessTypeCd + ", " + "JOB_ID = " + "'" + scheMasterInfo.getJobId() + "', "
				+ "WKP_ID = " + "'" + scheMasterInfo.getWorkplaceId() + "'" + " Where SID = " + "'"
				+ scheMasterInfo.getSId() + "'" + " and YMD = " + "'"
				+ scheMasterInfo.getGeneralDate() + "'";
		try {
			con.createStatement().executeUpdate(JDBCUtil.toUpdateWithCommonField(sqlQuery));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * remove 勤務予定マスタ情報
	 * 
	 * @param employeeId
	 * @param baseDate
	 */
	private void removeScheduleMaster(String employeeId, GeneralDate baseDate) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = null;
		sqlQuery = "Delete From KSCDT_SCHE_MASTER Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate
				+ "'";
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * insert 勤務予定休憩
	 * 
	 * @param workScheduleBreaks
	 */
	private void insertScheduleBreakTime(String employeeId, GeneralDate baseDate,
			List<WorkScheduleBreak> workScheduleBreaks) {
		if (CollectionUtil.isEmpty(workScheduleBreaks)) {
			return;
		}
		
		Connection con = this.getEntityManager().unwrap(Connection.class);

		int sizeWorkBreakList = workScheduleBreaks.size();

		for (int i = 0; i < sizeWorkBreakList; i++) {
			String sqlQuery = "INSERT INTO KSCDT_SCHE_BREAK (SID, YMD, BREAK_CNT, START_CLOCK, END_CLOCK) VALUES ( '"
					+ employeeId + "', " + "'" + baseDate + "', "
					+ workScheduleBreaks.get(i).getScheduleBreakCnt().v().intValue() + ", "
					+ workScheduleBreaks.get(i).getScheduledStartClock().valueAsMinutes() + ", "
					+ workScheduleBreaks.get(i).getScheduledEndClock().valueAsMinutes() + ")";

			try {
				con.createStatement().executeUpdate(JDBCUtil.toInsertWithCommonField(sqlQuery));
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void insertAllScheduleState(List<WorkScheduleState> listWorkScheduleState) {
		if (listWorkScheduleState == null || listWorkScheduleState.size() == 0) {
			return;
		}

		Connection con = this.getEntityManager().unwrap(Connection.class);
		int sizeListWorkScheduleState = listWorkScheduleState.size();
		
		for (int i = 0; i < sizeListWorkScheduleState; i++) {
			String sqlQuery = "INSERT INTO KSCDT_SCHE_STATE (SID, SCHE_ITEM_ID, YMD, SCHE_EDIT_STATE) VALUES ( '"
					+ listWorkScheduleState.get(i).getSId() + "', " + listWorkScheduleState.get(i).getScheduleItemId()
					+ ", " + "'" + listWorkScheduleState.get(i).getYmd() + "', "
					+ listWorkScheduleState.get(i).getScheduleEditState().value + ")";
			try {
				con.createStatement().executeUpdate(JDBCUtil.toInsertWithCommonField(sqlQuery));
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
	}

	/**
	 * update 勤務予定休憩
	 * 
	 * @param workScheduleBreaks
	 */
//	private void updateScheduleBreakTime(String employeeId, GeneralDate baseDate,
//			List<WorkScheduleBreak> listWorkScheduleBreaks) {
//		if (CollectionUtil.isEmpty(listWorkScheduleBreaks)) {
//			return;
//		}
//
//		Connection con = this.getEntityManager().unwrap(Connection.class);
//		String sqlQuery = null;
//		for (WorkScheduleBreak workScheduleBreak : listWorkScheduleBreaks) {
//			sqlQuery = "Update KSCDT_SCHE_BREAK Set START_CLOCK = "
//					+ workScheduleBreak.getScheduledStartClock().valueAsMinutes() + ", END_CLOCK = "
//					+ workScheduleBreak.getScheduledEndClock().valueAsMinutes() + " Where SID = " + "'" + employeeId
//					+ "'" + " and BREAK_CNT = " + workScheduleBreak.getScheduleBreakCnt() + " and YMD = " + "'"
//					+ baseDate + "'";
//			try {
//				con.createStatement().executeUpdate(sqlQuery);
//			} catch (SQLException e) {
//				throw new RuntimeException(e);
//			}
//		}
//	}
	
//	private void updateScheState(String employeeId, GeneralDate baseDate,
//			List<WorkScheduleState> listWorkScheduleState) {
//		if (CollectionUtil.isEmpty(listWorkScheduleState)) {
//			return;
//		}
//
//		Connection con = this.getEntityManager().unwrap(Connection.class);
//		String sqlQuery = null;
//		for (WorkScheduleState workScheduleState : listWorkScheduleState) {
//			sqlQuery = "Update KSCDT_SCHE_STATE Set SCHE_EDIT_STATE = " + workScheduleState.getScheduleEditState().value
//					+ " Where SID = " + "'" + workScheduleState.getSId() + "'" + " and SCHE_ITEM_ID = " + "'"
//					+ workScheduleState.getScheduleItemId() + "'" + " and YMD = " + "'" + workScheduleState.getYmd()
//					+ "'";
//			try {
//				con.createStatement().executeUpdate(JDBCUtil.toUpdateWithCommonField(sqlQuery));
//			} catch (SQLException e) {
//				throw new RuntimeException(e);
//			}
//		}
//	}
	
	@Override
	public void removeScheState(String employeeId, GeneralDate baseDate,
			List<WorkScheduleState> listWorkScheduleState) {
		
		if(CollectionUtil.isEmpty(listWorkScheduleState)){
			return;
		}
		
		List<Integer> listItemId = listWorkScheduleState.stream().map(x -> x.getScheduleItemId()).collect(Collectors.toList());
		CollectionUtil.split(listItemId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String listItemIdString = "(";
			for(int i = 0; i < subList.size(); i++){
				listItemIdString += "'"+ subList.get(i) +"',";
			}
			// remove last , in string and add )
			listItemIdString = listItemIdString.substring(0, listItemIdString.length() - 1) + ")";
			Connection con = this.getEntityManager().unwrap(Connection.class);
			String sqlQuery = "Delete From KSCDT_SCHE_STATE Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate.toString("yyyy-MM-dd")
					+ "'"+ " and SCHE_ITEM_ID IN " + listItemIdString ;
			try {
				con.createStatement().executeUpdate(sqlQuery);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	private void removeScheStateWithoutListScheState(String employeeId, GeneralDate baseDate) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KSCDT_SCHE_STATE Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate.toString("yyyy-MM-dd") + "'";
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Removes the all time zone.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 */
	private void removeAllScheduleBreakTime(String employeeId, GeneralDate baseDate) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KSCDT_SCHE_BREAK Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate.toString("yyyy-MM-dd")
				+ "'";
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * insert 勤務予定時間
	 * 
	 * @param workScheduleTime
	 */
	private void insertScheduleTime(String employeeId, GeneralDate baseDate,
			Optional<WorkScheduleTime> workScheduleTime) {
		if (workScheduleTime == null || !workScheduleTime.isPresent()) {
			return;
		}

		WorkScheduleTime scheduleTime = workScheduleTime.get();
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "INSERT INTO KSCDT_SCHE_TIME (SID, YMD, BREAK_TIME, WORKING_TIME, WEEKDAY_TIME, PRESCRIBED_TIME, TOTAL_LABOR_TIME, CHILD_TIME, CARE_TIME, FLEX_TIME) VALUES ("
				+ "'" + employeeId + "', " + "'" + baseDate + "', " + scheduleTime.getBreakTime().valueAsMinutes()
				+ ", " + scheduleTime.getWorkingTime().valueAsMinutes() + ", "
				+ scheduleTime.getWeekdayTime().valueAsMinutes() + ", "
				+ scheduleTime.getPredetermineTime().valueAsMinutes() + ", "
				+ scheduleTime.getTotalLaborTime().valueAsMinutes() + ", "
				+ scheduleTime.getChildTime().valueAsMinutes() + ", " + scheduleTime.getCareTime().valueAsMinutes()
				+ ", " + scheduleTime.getFlexTime().valueAsMinutes() + ")";
		try {
			con.createStatement().executeUpdate(JDBCUtil.toInsertWithCommonField(sqlQuery));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * update 勤務予定時間
	 * 
	 * @param workScheduleTime
	 */
	private void updateScheduleTime(String employeeId, GeneralDate baseDate,
			Optional<WorkScheduleTime> workScheduleTime) {
		if (workScheduleTime == null || !workScheduleTime.isPresent()) {
			return;
		}

		WorkScheduleTime scheduleTime = workScheduleTime.get();
//		KscdtScheTimePK key = new KscdtScheTimePK(employeeId, baseDate);
		// can check isPresent vi ngay truoc ben man KSC001 k insert vao bang scheTime
		// neu update lai vao du lieu ngay xua se loi do chua ton tai
//		Optional<KscdtScheTime> entityOpt = this.queryProxy().find(key, KscdtScheTime.class);
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String countQuery = "select count (SID) from KSCDT_SCHE_TIME"
				+ " Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'"
				+ baseDate + "'";
		try {
			ResultSet rs = con.createStatement().executeQuery(countQuery);
			while (rs.next()) {
				if (rs.getInt(1) > 0) {
					String sqlQuery = "Update KSCDT_SCHE_TIME Set BREAK_TIME = "
							+ scheduleTime.getBreakTime().valueAsMinutes() + ", " + "WORKING_TIME = "
							+ scheduleTime.getWorkingTime().valueAsMinutes() + ", " + "WEEKDAY_TIME = "
							+ scheduleTime.getWeekdayTime().valueAsMinutes() + ", " + "PRESCRIBED_TIME = "
							+ scheduleTime.getPredetermineTime().valueAsMinutes() + ", " + "TOTAL_LABOR_TIME = "
							+ scheduleTime.getTotalLaborTime().valueAsMinutes() + ", " + "CHILD_TIME = "
							+ scheduleTime.getChildTime().valueAsMinutes() + ", " + "CARE_TIME = "
							+ scheduleTime.getCareTime().valueAsMinutes() + ", " + "FLEX_TIME = "
							+ scheduleTime.getFlexTime().valueAsMinutes() + " Where SID = " + "'" + employeeId + "'"
							+ " and YMD = " + "'" + baseDate + "'";
					con.createStatement().executeUpdate(JDBCUtil.toUpdateWithCommonField(sqlQuery));
				} else {
					// case for KSC001 update
					this.insertScheduleTime(employeeId, baseDate, workScheduleTime);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


	private void removeScheduleTime(String employeeId, GeneralDate baseDate) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = null;
		sqlQuery = "Delete From KSCDT_SCHE_TIME Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate
				+ "'";
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param sIds
	 * @return
	 */
	@Override
	public GeneralDate findMaxDateByListSid(List<String> sIds) {
		List<GeneralDate> listDate = new ArrayList<>(); 
		CollectionUtil.split(sIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			listDate.addAll(this.queryProxy().query(GET_LIST_DATE_BY_LIST_SID, GeneralDate.class)
				.setParameter("sIds", subList)
				.getList());
		});
		if (listDate.isEmpty()) return null;
		listDate.sort(Comparator.comparing(GeneralDate::date).reversed()); // DESC order
		return listDate.get(0);
	}

	@Override
	public List<BasicSchedule> getBasicScheduleBySidPeriodDate(String employeeId, DatePeriod dateData) {
		List<BasicSchedule> lstData = this.queryProxy().query(QUERY_BY_SID_PERIOD, KscdtBasicSchedule.class)
				.setParameter("employeeId", employeeId).setParameter("startDate", dateData.start())
				.setParameter("endDate", dateData.end())
				.getList(x -> toDomain(x, this.findAllWorkScheduleTimeZone(employeeId, x.kscdpBSchedulePK.date)));
		return lstData;
	}

	@Override
	public void updateConfirmAtr(List<BasicSchedule> listBasicSchedule) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = null;
		for (BasicSchedule basicSchedule : listBasicSchedule) {
			sqlQuery = "Update KSCDT_SCHE_BASIC Set CONFIRMED_ATR = " + basicSchedule.getConfirmedAtr().value
					+ " Where SID = " + "'" + basicSchedule.getEmployeeId() + "'" + " and YMD = " + "'"
					+ basicSchedule.getDate() + "'";
			try {
				con.createStatement().executeUpdate(sqlQuery);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	@SneakyThrows
	public List<BasicSchedule> getBasicScheduleBySidPeriodDate(String employeeId, List<GeneralDate> dates) {
		if(dates.isEmpty()) {
			return Collections.emptyList();
		}
		List<BasicSchedule> lstOutput = new ArrayList<>();
		CollectionUtil.split(dates, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String subIn = NtsStatement.In.createParamsString(subList);
			String sql = "SELECT * FROM KSCDT_SCHE_BASIC "
					+ " WHERE YMD IN (" + subIn + ") "
							+ "AND SID = ?";
			try(val stmt = this.connection().prepareStatement(sql)){
				for (int i = 0; i < subList.size(); i++) {
					stmt.setDate(i + 1, Date.valueOf(subList.get(i).localDate()));
				}
				stmt.setString(1  + subList.size(), employeeId);
				List<BasicSchedule> lstOutputTmp =  new NtsResultSet(stmt.executeQuery()).getList(x -> {
					return BasicSchedule.createFromJavaType(x.getString("SID"),
							x.getGeneralDate("YMD"),
							x.getString("WORKTYPE_CD"),
							x.getString("WORKTIME_CD"),
							x.getInt("CONFIRMED_ATR"));
				});
				lstOutput.addAll(lstOutputTmp);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		return lstOutput;
	}

	@Override
	public void changeWorkType(String sid, GeneralDate date, String workTypeCode) {
		Optional<KscdtBasicSchedule> optionalEntity = this.findById(sid, date);
		if (!optionalEntity.isPresent()) {
			return;
		}
		KscdtBasicSchedule schedule = optionalEntity.get();
		schedule.workTypeCode = workTypeCode;
		this.commandProxy().update(schedule);
		this.getEntityManager().flush();
	}

	@Override
	public void changeWorkTime(String sid, GeneralDate date, String worktimeCode) {
		Optional<KscdtBasicSchedule> optionalEntity = this.findById(sid, date);
		if (!optionalEntity.isPresent()) {
			return;
		}
		KscdtBasicSchedule schedule = optionalEntity.get();
		schedule.workTimeCode = worktimeCode;
		this.commandProxy().update(schedule);
		this.getEntityManager().flush();
	}
}
