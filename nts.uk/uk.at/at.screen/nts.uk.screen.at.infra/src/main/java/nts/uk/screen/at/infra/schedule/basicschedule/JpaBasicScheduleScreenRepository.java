package nts.uk.screen.at.infra.schedule.basicschedule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdtBasicSchedule;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscmtScheDispControl;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscmtScheDispControlPK;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscmtWorkEmpCombine;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletimezone.KscdtWorkScheduleTimeZone;
import nts.uk.ctx.at.schedule.infra.entity.shift.workpairpattern.KscmtComPattern;
import nts.uk.ctx.at.schedule.infra.entity.shift.workpairpattern.KscmtComPatternItem;
import nts.uk.ctx.at.schedule.infra.entity.shift.workpairpattern.KscmtComWorkPairSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.workpairpattern.KscmtWkpPattern;
import nts.uk.ctx.at.schedule.infra.entity.shift.workpairpattern.KscmtWkpPatternItem;
import nts.uk.ctx.at.schedule.infra.entity.shift.workpairpattern.KscmtWkpWorkPairSet;
import nts.uk.screen.at.app.schedule.basicschedule.BasicScheduleScreenDto;
import nts.uk.screen.at.app.schedule.basicschedule.BasicScheduleScreenRepository;
import nts.uk.screen.at.app.schedule.basicschedule.ScheduleDisplayControlScreenDto;
import nts.uk.screen.at.app.schedule.basicschedule.WorkEmpCombineScreenDto;
import nts.uk.screen.at.app.schedule.basicschedule.WorkTimeScreenDto;
import nts.uk.screen.at.app.schedule.basicschedule.WorkTypeScreenDto;
import nts.uk.screen.at.app.shift.workpairpattern.ComPatternScreenDto;
import nts.uk.screen.at.app.shift.workpairpattern.PatternItemScreenDto;
import nts.uk.screen.at.app.shift.workpairpattern.WkpPatternScreenDto;
import nts.uk.screen.at.app.shift.workpairpattern.WorkPairSetScreenDto;

/**
 * 
 * @author sonnh1
 *
 */
@Stateless
public class JpaBasicScheduleScreenRepository extends JpaRepository implements BasicScheduleScreenRepository {

	private static final String SEL_BY_LIST_SID_AND_DATE = "SELECT c FROM KscdtBasicSchedule c"
			+ " WHERE c.kscdpBSchedulePK.sId IN :sId AND c.kscdpBSchedulePK.date >= :startDate AND c.kscdpBSchedulePK.date <= :endDate";
	private static final String GET_WTIME_SET_AND_WTIME_SHEET_SET = "SELECT NEW " + WorkTimeScreenDto.class.getName()
			+ " (a.kshmtWorkTimeSetPK.worktimeCd, a.name, a.abname, a.symbol, a.dailyWorkAtr, a.worktimeSetMethod, a.abolitionAtr, a.color, a.memo, a.note,"
			+ " b.kshmtWorkTimeSheetSetPK.workNo, b.useAtr, b.startTime, b.endTime)"
			+ " FROM KshmtWorkTimeSet a JOIN KshmtWorkTimeSheetSet b ON a.kshmtWorkTimeSetPK.worktimeCd = b.kshmtWorkTimeSheetSetPK.worktimeCd"
			+ " AND a.kshmtWorkTimeSetPK.cid = b.kshmtWorkTimeSheetSetPK.cid"
			+ " WHERE a.kshmtWorkTimeSetPK.cid = :companyId" + " AND a.abolitionAtr = :abolitionAtr"
			+ " ORDER BY a.kshmtWorkTimeSetPK.worktimeCd ASC";
	private static final String GET_WORK_EMP_COMBINE = "SELECT c FROM KscmtWorkEmpCombine c"
			+ " WHERE c.kscmtWorkEmpCombinePK.companyId = :companyId " + " AND c.workTypeCode IN :workTypeCode"
			+ " OR c.workTimeCode IN :workTimeCode";
	private static final String GET_WORK_SCH_TIMEZONE = "SELECT a FROM KscdtWorkScheduleTimeZone a"
			+ " WHERE a.kscdtWorkScheduleTimeZonePk.sId IN :sId"
			+ " AND a.kscdtWorkScheduleTimeZonePk.date >= :startDate"
			+ " AND a.kscdtWorkScheduleTimeZonePk.date <= :endDate"
			+ " AND a.kscdtWorkScheduleTimeZonePk.scheduleCnt = 1";
	private static final String GET_COM_PATTERN = "SELECT a FROM KscmtComPattern a"
			+ " WHERE a.kscmtComPatternPk.companyId =:companyId";

	private static final String GET_WPK_PATTERN = "SELECT a FROM KscmtWkpPattern a"
			+ " WHERE a.kscmtWkpPatternPk.workplaceId =:workplaceId";

	private static BasicScheduleScreenDto toDto(KscdtBasicSchedule entity) {
		return new BasicScheduleScreenDto(entity.kscdpBSchedulePK.sId, entity.kscdpBSchedulePK.date,
				entity.workTypeCode, entity.workTimeCode, entity.confirmedAtr);
	}

	private static BasicScheduleScreenDto toBasicScheduleScreenDto(KscdtWorkScheduleTimeZone entity) {
		return new BasicScheduleScreenDto(entity.kscdtWorkScheduleTimeZonePk.sId,
				entity.kscdtWorkScheduleTimeZonePk.date, entity.kscdtWorkScheduleTimeZonePk.scheduleCnt,
				entity.scheduleStartClock, entity.scheduleEndClock, entity.bounceAtr);
	}

	private static ScheduleDisplayControlScreenDto toScheduleDisplayControlScreenDto(KscmtScheDispControl entity) {
		return new ScheduleDisplayControlScreenDto(entity.symbolAtr, entity.symbolHalfDayAtr, entity.symbolHalfDayName);
	}

	private static WorkEmpCombineScreenDto toWorkEmpCombineScreenDto(KscmtWorkEmpCombine entity) {
		return new WorkEmpCombineScreenDto(entity.workTypeCode, entity.workTimeCode, entity.symbolName);
	}

	private static WorkPairSetScreenDto toComWorkPairSetScreenDto(KscmtComWorkPairSet entity) {
		return new WorkPairSetScreenDto(entity.kscmtComWorkPairSetPk.pairNo, entity.workTypeCode, entity.workTimeCode);
	}

	private static WorkPairSetScreenDto toWkpWorkPairSetScreenDto(KscmtWkpWorkPairSet entity) {
		return new WorkPairSetScreenDto(entity.kscmtWkpWorkPairSetPk.pairNo, entity.workTypeCode, entity.workTimeCode);
	}

	private static PatternItemScreenDto toComPatternItemScreenDto(KscmtComPatternItem entity) {
		List<WorkPairSetScreenDto> comWorkPairSet = entity.kscmtComWorkPairSet.stream()
				.map(x -> toComWorkPairSetScreenDto(x)).collect(Collectors.toList());
		return new PatternItemScreenDto(entity.kscmtComPatternItemPk.patternNo, entity.patternName, comWorkPairSet);
	}

	private static PatternItemScreenDto toWkpPatternItemScreenDto(KscmtWkpPatternItem entity) {
		List<WorkPairSetScreenDto> wkpWorkPairSet = entity.kscmtWkpWorkPairSet.stream()
				.map(x -> toWkpWorkPairSetScreenDto(x)).collect(Collectors.toList());
		return new PatternItemScreenDto(entity.kscmtWkpPatternItemPk.patternNo, entity.patternName, wkpWorkPairSet);
	}

	private static ComPatternScreenDto toComPatternScreenDto(KscmtComPattern entity) {
		List<PatternItemScreenDto> comPatternItems = entity.kscmtComPatternItem.stream()
				.map(x -> toComPatternItemScreenDto(x)).collect(Collectors.toList());
		return new ComPatternScreenDto(entity.kscmtComPatternPk.groupNo, entity.groupName, entity.groupUsageAtr,
				entity.note, comPatternItems);
	}

	private static WkpPatternScreenDto toWkpPatternScreenDto(KscmtWkpPattern entity) {
		List<PatternItemScreenDto> wkpPatternItems = entity.kscmtWkpPatternItem.stream()
				.map(x -> toWkpPatternItemScreenDto(x)).collect(Collectors.toList());
		return new WkpPatternScreenDto(entity.kscmtWkpPatternPk.workplaceId, entity.kscmtWkpPatternPk.groupNo,
				entity.groupName, entity.groupUsageAtr, entity.note, wkpPatternItems);
	}

	/**
	 * get list BasicSchedule by list String and startDate and endDate
	 */
	@Override
	public List<BasicScheduleScreenDto> getByListSidAndDate(List<String> employeeId, GeneralDate startDate,
			GeneralDate endDate) {
		List<BasicScheduleScreenDto> datas = new ArrayList<BasicScheduleScreenDto>();
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
			datas.addAll(this.queryProxy().query(SEL_BY_LIST_SID_AND_DATE, KscdtBasicSchedule.class)
					.setParameter("sId", subIdList).setParameter("startDate", startDate)
					.setParameter("endDate", endDate).getList(x -> toDto(x)));
		});
		return datas;

	}
	
	/**
	 * Get data source for screen KSU001
	 * get data of timezone with cnt = 1 (chi hien thi gio ca 1 nen k can lay gio ca 2) 
	 * @param employeeIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public List<BasicScheduleScreenDto> getBasicScheduleWithJDBC(List<String> employeeIds, GeneralDate startDate,
			GeneralDate endDate) {
		List<BasicScheduleScreenDto> listBasicScheduleScreenDto = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			Connection con = this.getEntityManager().unwrap(Connection.class);
	
			String listEmp = "(";
			for(int i = 0; i < subList.size(); i++){
				listEmp += "'"+ subList.get(i) +"',";
			}
			// remove last , in string and add )
			listEmp = listEmp.substring(0, listEmp.length() - 1) + ")";
	
			String sqlQueryWhere = " WHERE KSCDT_SCHE_BASIC.SID IN " + listEmp
					+ " AND (KSCDT_SCHE_BASIC.YMD between " + "'" + startDate + "' and '" + endDate + "')"
					+ " AND (KSCDT_SCHE_TIMEZONE.CNT IS NULL OR KSCDT_SCHE_TIMEZONE.CNT = " + 1 + ")";
	
			String sqlQuery = "SELECT KSCDT_SCHE_BASIC.SID, KSCDT_SCHE_BASIC.YMD, KSCDT_SCHE_BASIC.WORKTYPE_CD, KSCDT_SCHE_BASIC.WORKTIME_CD, KSCDT_SCHE_BASIC.CONFIRMED_ATR,"
					+ " KSCDT_SCHE_TIMEZONE.CNT, KSCDT_SCHE_TIMEZONE.BOUNCE_ATR, KSCDT_SCHE_TIMEZONE.START_CLOCK as TZ_START_CLOCK, KSCDT_SCHE_TIMEZONE.END_CLOCK as TZ_END_CLOCK"
					+ " FROM KSCDT_SCHE_BASIC LEFT JOIN KSCDT_SCHE_TIMEZONE ON KSCDT_SCHE_BASIC.SID = KSCDT_SCHE_TIMEZONE.SID AND KSCDT_SCHE_BASIC.YMD = KSCDT_SCHE_TIMEZONE.YMD"
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
	
					listBasicScheduleScreenDto.add(new BasicScheduleScreenDto(sId, date, workTypeCode, workTimeCode,
							confirmAtr, timezoneCnt, timezoneStart, timezoneEnd, bounceAtr));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
		return listBasicScheduleScreenDto;
	}
	
	/**
	 * get list WorkTimeSet by companyId and abolitionAtr = ABOLISH join with
	 * table WorkTimeSheetSet, sort by workTimeCode
	 */
	@Override
	public List<WorkTimeScreenDto> getListWorkTime(String companyId, int abolitionAtr) {
		return this.queryProxy().query(GET_WTIME_SET_AND_WTIME_SHEET_SET, WorkTimeScreenDto.class)
				.setParameter("companyId", companyId).setParameter("abolitionAtr", abolitionAtr).getList();
	}

	/**
	 * Find by companyId and deprecateClassification
	 */
	@Override
	public List<WorkTypeScreenDto> findByCIdAndDeprecateCls1(String companyId, int deprecateClassification) {
		
		List<WorkTypeScreenDto> listWorkTypeScreenDto = new ArrayList<>();
		Connection con = this.getEntityManager().unwrap(Connection.class);
		
		String sqlQueryWhere = "where KSHMT_WORKTYPE.CID = '" + companyId + "' AND KSHMT_WORKTYPE.ABOLISH_ATR = " + deprecateClassification
				+ " order by CASE WHEN KSHMT_WORKTYPE_ORDER.DISPORDER IS NULL THEN 1 ELSE 0 END,"
				+ " CASE WHEN KSHMT_WORKTYPE_ORDER.DISPORDER IS NULL THEN KSHMT_WORKTYPE.CD END ASC,"
				+ " CASE WHEN KSHMT_WORKTYPE_ORDER.DISPORDER IS NOT NULL THEN KSHMT_WORKTYPE_ORDER.DISPORDER END ASC";

		String sqlQuery = 
				"select KSHMT_WORKTYPE.CD, KSHMT_WORKTYPE.NAME, KSHMT_WORKTYPE.ABNAME, KSHMT_WORKTYPE.SYNAME, KSHMT_WORKTYPE.MEMO,"
				+ " KSHMT_WORKTYPE.WORK_ATR, KSHMT_WORKTYPE.ONE_DAY_CLS, KSHMT_WORKTYPE.MORNING_CLS, KSHMT_WORKTYPE.AFTERNOON_CLS"
				+ " from KSHMT_WORKTYPE left join KSHMT_WORKTYPE_ORDER"
				+ " on KSHMT_WORKTYPE.CID = KSHMT_WORKTYPE_ORDER.CID and KSHMT_WORKTYPE.CD = KSHMT_WORKTYPE_ORDER.WORKTYPE_CD "
				+ sqlQueryWhere;
		try {
			ResultSet rs = con.createStatement().executeQuery(sqlQuery);
			while (rs.next()) {
				String workTypeCode = rs.getString("CD");
				String name = rs.getString("NAME");
				String abbreviationName = rs.getString("ABNAME");
				String symbolicName = rs.getString("SYNAME");
				String memo = rs.getString("MEMO");
				int workTypeUnit = rs.getInt("WORK_ATR");
				int oneDay = rs.getInt("ONE_DAY_CLS");
				int morning = rs.getInt("MORNING_CLS");
				int afternoon = rs.getInt("AFTERNOON_CLS");
				
				listWorkTypeScreenDto.add(new WorkTypeScreenDto(workTypeCode, name, abbreviationName, symbolicName, memo, workTypeUnit, oneDay, morning, afternoon));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return listWorkTypeScreenDto;
	}

	/**
	 * Get data of table WorkEmpCombine
	 */
	@Override
	public List<WorkEmpCombineScreenDto> getListWorkEmpCobine(String companyId, List<String> lstWorkTypeCode,
			List<String> lstWorkTimeCode) {
		List<WorkEmpCombineScreenDto> resultList = new ArrayList<>();
		CollectionUtil.split(lstWorkTypeCode, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, workTypes -> {
			CollectionUtil.split(lstWorkTimeCode, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, workTimes -> {
				resultList.addAll(this.queryProxy().query(GET_WORK_EMP_COMBINE, KscmtWorkEmpCombine.class)
						.setParameter("companyId", companyId)
						.setParameter("workTypeCode", workTypes)
						.setParameter("workTimeCode", workTimes)
						.getList(x -> toWorkEmpCombineScreenDto(x)));
			});
		});
		return resultList;
	}

	/**
	 * Get data of table ScheduleDisplayControl
	 */
	@Override
	public Optional<ScheduleDisplayControlScreenDto> getScheduleDisControl(String companyId) {
		KscmtScheDispControlPK pk = new KscmtScheDispControlPK(companyId);
		return this.queryProxy().find(pk, KscmtScheDispControl.class).map(x -> toScheduleDisplayControlScreenDto(x));
	}

	/**
	 * Get data of table WorkScheTimezone with scheduleCnt = 1
	 */
	@Override
	public List<BasicScheduleScreenDto> getDataWorkScheTimezone(List<String> sId, GeneralDate startDate,
			GeneralDate endDate) {
		List<BasicScheduleScreenDto> datas = new ArrayList<BasicScheduleScreenDto>();
		CollectionUtil.split(sId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
			datas.addAll(this.queryProxy().query(GET_WORK_SCH_TIMEZONE, KscdtWorkScheduleTimeZone.class)
					.setParameter("sId", subIdList).setParameter("startDate", startDate)
					.setParameter("endDate", endDate).getList(x -> toBasicScheduleScreenDto(x)));
		});
		return datas;
	}

	@Override
	public List<ComPatternScreenDto> getDataComPattern(String companyId) {
		return this.queryProxy().query(GET_COM_PATTERN, KscmtComPattern.class).setParameter("companyId", companyId)
				.getList(x -> toComPatternScreenDto(x));
	}

	@Override
	public List<WkpPatternScreenDto> getDataWkpPattern(String workplaceId) {
		return this.queryProxy().query(GET_WPK_PATTERN, KscmtWkpPattern.class).setParameter("workplaceId", workplaceId)
				.getList(x -> toWkpPatternScreenDto(x));
	}
}
