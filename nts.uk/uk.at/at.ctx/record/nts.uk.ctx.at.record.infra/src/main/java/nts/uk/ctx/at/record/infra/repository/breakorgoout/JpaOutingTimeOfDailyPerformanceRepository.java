package nts.uk.ctx.at.record.infra.repository.breakorgoout;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.OutingFrameNo;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.infra.entity.breakorgoout.KrcdtDaiOutingTime;
import nts.uk.ctx.at.record.infra.entity.breakorgoout.KrcdtDaiOutingTimePK;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaOutingTimeOfDailyPerformanceRepository extends JpaRepository
		implements OutingTimeOfDailyPerformanceRepository {


	private static final String SELECT_BY_EMPLOYEE_AND_DATE;

	private static final String SELECT_BY_KEY;

	private static final String CHECK_EXIST_DATA;
	
	static {
		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDaiOutingTime a ");
//		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDaiOutingTimePK.ymd = :ymd ");
//		REMOVE_BY_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd = :ymd ");
		SELECT_BY_EMPLOYEE_AND_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd = :ymd ");
		builderString.append("AND a.krcdtDaiOutingTimePK.outingFrameNo IN :outingFrameNos ");
		SELECT_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd = :ymd ");
		builderString.append("AND a.krcdtDaiOutingTimePK.outingFrameNo = :outingFrameNo ");
		CHECK_EXIST_DATA = builderString.toString();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAI_OUTING_TIME_TS Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + ymd + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE).setParameter("employeeId", employeeId)
//				.setParameter("ymd", ymd).executeUpdate();

	}

	@Override
	public Optional<OutingTimeOfDailyPerformance> findByEmployeeIdAndDate(String employeeId, GeneralDate ymd) {
		List<KrcdtDaiOutingTime> lstKrcdtDaiOutingTime = this.queryProxy()
				.query(SELECT_BY_EMPLOYEE_AND_DATE, KrcdtDaiOutingTime.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).getList();
		if (lstKrcdtDaiOutingTime == null || lstKrcdtDaiOutingTime.isEmpty()) {
			return Optional.empty();
		}
		List<OutingTimeSheet> lstOutingTimeSheet = new ArrayList<OutingTimeSheet>();

		lstKrcdtDaiOutingTime.forEach(x -> {
			lstOutingTimeSheet.add(toDtomain(x));
		});
		return Optional.ofNullable(new OutingTimeOfDailyPerformance(employeeId, ymd, lstOutingTimeSheet));
	}

	@Override
	public void add(OutingTimeOfDailyPerformance outing) {
		// commandProxy().insertAll(outing.getOutingTimeSheets().stream()
		// .map(c -> KrcdtDaiOutingTime.toEntity(outing.getEmployeeId(),
		// outing.getYmd(), c))
		// .collect(Collectors.toList()));
		// this.getEntityManager().flush();

		try {
			Connection con = this.getEntityManager().unwrap(Connection.class);
			Statement statementI = con.createStatement();
			for (OutingTimeSheet outingTimeSheet : outing.getOutingTimeSheets()) {

				// OutingTimeSheet - goOut - actualStamp
				Integer outActualRoundingTime = (outingTimeSheet.getGoOut().isPresent()
						&& outingTimeSheet.getGoOut().get().getActualStamp().isPresent())
								? outingTimeSheet.getGoOut().get().getActualStamp().get().getAfterRoundingTime()
										.valueAsMinutes()
								: null;
				Integer outActualTime = (outingTimeSheet.getGoOut().isPresent()
						&& outingTimeSheet.getGoOut().get().getActualStamp().isPresent())
								? outingTimeSheet.getGoOut().get().getActualStamp().get().getTimeWithDay()
										.valueAsMinutes()
								: null;
				String outActualStampLocationCode = (outingTimeSheet.getGoOut().isPresent()
						&& outingTimeSheet.getGoOut().get().getActualStamp().isPresent()
						&& outingTimeSheet.getGoOut().get().getActualStamp().get().getLocationCode().isPresent()) ? "'"
								+ outingTimeSheet.getGoOut().get().getActualStamp().get().getLocationCode().get().v()
								+ "'" : null;
				Integer outActualStampSource = (outingTimeSheet.getGoOut().isPresent()
						&& outingTimeSheet.getGoOut().get().getActualStamp().isPresent())
								? outingTimeSheet.getGoOut().get().getActualStamp().get().getStampSourceInfo().value
								: null;

				// OutingTimeSheet - goOut - stamp
				Integer outStampRoundingTime = (outingTimeSheet.getGoOut().isPresent()
						&& outingTimeSheet.getGoOut().get().getStamp().isPresent())
								? outingTimeSheet.getGoOut().get().getStamp().get().getAfterRoundingTime()
										.valueAsMinutes()
								: null;
				Integer outStampTime = (outingTimeSheet.getGoOut().isPresent()
						&& outingTimeSheet.getGoOut().get().getStamp().isPresent())
								? outingTimeSheet.getGoOut().get().getStamp().get().getTimeWithDay().valueAsMinutes()
								: null;
				String outStampLocationCode = (outingTimeSheet.getGoOut().isPresent()
						&& outingTimeSheet.getGoOut().get().getStamp().isPresent()
						&& outingTimeSheet.getGoOut().get().getStamp().get().getLocationCode().isPresent())
								? "'" + outingTimeSheet.getGoOut().get().getStamp().get().getLocationCode().get().v()
										+ "'"
								: null;
				Integer outStampSource = (outingTimeSheet.getGoOut().isPresent()
						&& outingTimeSheet.getGoOut().get().getStamp().isPresent())
								? outingTimeSheet.getGoOut().get().getStamp().get().getStampSourceInfo().value : null;

				// OutingTimeSheet - goOut - numberOfReflectionStamp
				Integer outNumberReflec = outingTimeSheet.getGoOut().isPresent()
						? outingTimeSheet.getGoOut().get().getNumberOfReflectionStamp() : null;

				// OutingTimeSheet - comeBack - actualStamp
				Integer backActualRoundingTime = (outingTimeSheet.getComeBack().isPresent()
						&& outingTimeSheet.getComeBack().get().getActualStamp().isPresent())
								? outingTimeSheet.getComeBack().get().getActualStamp().get().getAfterRoundingTime()
										.valueAsMinutes()
								: null;
				Integer backActualTime = (outingTimeSheet.getComeBack().isPresent()
						&& outingTimeSheet.getComeBack().get().getActualStamp().isPresent())
								? outingTimeSheet.getComeBack().get().getActualStamp().get().getTimeWithDay()
										.valueAsMinutes()
								: null;
				String backActualStampLocationCode = (outingTimeSheet.getComeBack().isPresent()
						&& outingTimeSheet.getComeBack().get().getActualStamp().isPresent()
						&& outingTimeSheet.getComeBack().get().getActualStamp().get().getLocationCode().isPresent())
								? "'" + outingTimeSheet.getComeBack().get().getActualStamp().get().getLocationCode()
										.get().v() + "'"
								: null;
				Integer backActualStampSource = (outingTimeSheet.getComeBack().isPresent()
						&& outingTimeSheet.getComeBack().get().getActualStamp().isPresent())
								? outingTimeSheet.getComeBack().get().getActualStamp().get().getStampSourceInfo().value
								: null;

				// OutingTimeSheet - comeBack - stamp
				Integer backStampRoundingTime = (outingTimeSheet.getComeBack().isPresent()
						&& outingTimeSheet.getComeBack().get().getStamp().isPresent())
								? outingTimeSheet.getComeBack().get().getStamp().get().getAfterRoundingTime()
										.valueAsMinutes()
								: null;
				Integer backStampTime = (outingTimeSheet.getComeBack().isPresent()
						&& outingTimeSheet.getComeBack().get().getStamp().isPresent())
								? outingTimeSheet.getComeBack().get().getStamp().get().getTimeWithDay().valueAsMinutes()
								: null;
				Integer backStampSource = (outingTimeSheet.getComeBack().isPresent()
						&& outingTimeSheet.getComeBack().get().getStamp().isPresent())
								? outingTimeSheet.getComeBack().get().getStamp().get().getStampSourceInfo().value
								: null;
				String backStampLocationCode = (outingTimeSheet.getComeBack().isPresent()
						&& outingTimeSheet.getComeBack().get().getStamp().isPresent()
						&& outingTimeSheet.getComeBack().get().getStamp().get().getLocationCode().isPresent())
								? "'" + outingTimeSheet.getComeBack().get().getStamp().get().getLocationCode().get().v()
										+ "'"
								: null;

				// TimeLeavingWork - leaveStamp - numberOfReflectionStamp
				Integer backNumberReflec = outingTimeSheet.getComeBack().isPresent()
						? outingTimeSheet.getComeBack().get().getNumberOfReflectionStamp() : null;

				String insertTableSQL = "INSERT INTO KRCDT_DAI_OUTING_TIME_TS ( SID , YMD , OUTING_FRAME_NO , OUT_STAMP_TIME , OUT_STAMP_ROUDING_TIME_DAY , OUT_STAMP_PLACE_CODE , OUT_STAMP_SOURCE_INFO , "
						+ " OUT_ACTUAL_TIME, OUT_ACTUAL_ROUDING_TIME_DAY , OUT_ACTUAL_PLACE_CODE , OUT_ACTUAL_SOURCE_INFO , OUT_NUMBER_STAMP , BACK_STAMP_TIME , BACK_STAMP_ROUDING_TIME_DAY , BACK_STAMP_PLACE_CODE , "
						+ " BACK_STAMP_SOURCE_INFO , BACK_ACTUAL_TIME , BACK_ACTUAL_ROUDING_TIME_DAY , BACK_ACTUAL_PLACE_CODE , BACK_ACTUAL_SOURCE_INFO , BACK_NUMBER_STAMP , OUTING_TIME_CALCULATION , "
						+ " OUTING_TIME , OUTING_REASON ) " + "VALUES( '" + outing.getEmployeeId() + "' , '"
						+ outing.getYmd() + "' , " + outingTimeSheet.getOutingFrameNo().v() + " , " + outStampTime
						+ " , " + outStampRoundingTime + " , " + outStampLocationCode + " , " + outStampSource + ", "
						+ outActualTime + " , " + outActualRoundingTime + " , " + outActualStampLocationCode + " , "
						+ outActualStampSource + ", " + outNumberReflec + ", " + backStampTime + " , "
						+ backStampRoundingTime + " , " + backStampLocationCode + " , " + backStampSource + ", "
						+ backActualTime + " , " + backActualRoundingTime + " , " + backActualStampLocationCode + " , "
						+ backActualStampSource + ", " + backNumberReflec + " , "
						+ outingTimeSheet.getOutingTimeCalculation().valueAsMinutes() + " , "
						+ outingTimeSheet.getOutingTime().valueAsMinutes() + " , "
						+ outingTimeSheet.getReasonForGoOut().value + " )";
				statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTableSQL));
			}
		} catch (Exception e) {

		}
	}

	@Override
	public boolean checkExistData(String employeeId, GeneralDate ymd, OutingFrameNo outingFrameNo) {
		return this.queryProxy().query(CHECK_EXIST_DATA, long.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).setParameter("outingFrameNo", outingFrameNo.v()).getSingle().get() > 0;
	}

	@Override
	public void update(OutingTimeOfDailyPerformance domain) {
		List<Integer> outingFrameNos = domain.getOutingTimeSheets().stream().map(item -> {
			return item.getOutingFrameNo().v();
		}).collect(Collectors.toList());
		
		List<KrcdtDaiOutingTime> krcdtDaiOutingTimeLists = new ArrayList<>();
		CollectionUtil.split(outingFrameNos, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			krcdtDaiOutingTimeLists.addAll(this.queryProxy()
				.query(SELECT_BY_KEY, KrcdtDaiOutingTime.class)
					.setParameter("employeeId", domain.getEmployeeId())
					.setParameter("ymd", domain.getYmd())
					.setParameter("outingFrameNos", subList)
				.getList());
		});

		List<OutingTimeSheet> outingTimeSheets = domain.getOutingTimeSheets();

		outingTimeSheets.stream().forEach(outingTimeSheet -> {
			KrcdtDaiOutingTime krcdtDaiOutingTime = krcdtDaiOutingTimeLists.stream()
					.filter(c -> c.krcdtDaiOutingTimePK.employeeId == domain.getEmployeeId()
							&& c.krcdtDaiOutingTimePK.ymd == domain.getYmd()
							&& c.krcdtDaiOutingTimePK.outingFrameNo == outingTimeSheet.getOutingFrameNo().v())
					.findFirst().orElse(null);
			if (krcdtDaiOutingTime != null) {
				setEntityValue(outingTimeSheet, krcdtDaiOutingTime);
			} else {
				krcdtDaiOutingTime = new KrcdtDaiOutingTime();
				krcdtDaiOutingTime.krcdtDaiOutingTimePK = new KrcdtDaiOutingTimePK(domain.getEmployeeId(),
						domain.getYmd(), outingTimeSheet.getOutingFrameNo().v());
				setEntityValue(outingTimeSheet, krcdtDaiOutingTime);
				krcdtDaiOutingTimeLists.add(krcdtDaiOutingTime);
			}
		});
		this.commandProxy().updateAll(krcdtDaiOutingTimeLists);
		this.getEntityManager().flush();
	}

	private void setEntityValue(OutingTimeSheet domain, KrcdtDaiOutingTime krcdtDaiOutingTime) {
		setBackMainStamp(domain.getComeBack().orElse(null), krcdtDaiOutingTime);
		krcdtDaiOutingTime.outingReason = domain.getReasonForGoOut().value;
		krcdtDaiOutingTime.outingTime = domain.getOutingTime().v();
		krcdtDaiOutingTime.outingTimeCalculation = domain.getOutingTimeCalculation().v();
		setGoOutMainStamp(domain.getGoOut().orElse(null), krcdtDaiOutingTime);
	}

	private void setBackMainStamp(TimeActualStamp stamp, KrcdtDaiOutingTime krcdtDaiOutingTime) {
		if (stamp != null) {
			setBackActualStamp(stamp.getActualStamp().orElse(null), krcdtDaiOutingTime);
			krcdtDaiOutingTime.backNumberStamp = stamp.getNumberOfReflectionStamp();
			setBackStamp(stamp.getStamp().orElse(null), krcdtDaiOutingTime);
		} else {
			setBackActualStampNull(krcdtDaiOutingTime);
			setGoOutStampNull(krcdtDaiOutingTime);
		}
	}

	private void setBackActualStamp(WorkStamp stamp, KrcdtDaiOutingTime krcdtDaiOutingTime) {
		if (stamp != null) {
			krcdtDaiOutingTime.backActualPlaceCode = !stamp.getLocationCode().isPresent() ? null
					: stamp.getLocationCode().get().v();
			krcdtDaiOutingTime.backActualRoundingTimeDay = stamp.getAfterRoundingTime() == null ? null
					: stamp.getAfterRoundingTime().valueAsMinutes();
			krcdtDaiOutingTime.backActualSourceInfo = stamp.getStampSourceInfo() == null ? null
					: stamp.getStampSourceInfo().value;
			krcdtDaiOutingTime.backActualTime = stamp.getTimeWithDay() == null ? null
					: stamp.getTimeWithDay().valueAsMinutes();
		} else {
			setBackActualStampNull(krcdtDaiOutingTime);
		}
	}

	private void setBackStamp(WorkStamp stamp, KrcdtDaiOutingTime krcdtDaiOutingTime) {
		if (stamp != null) {
			krcdtDaiOutingTime.backStampPlaceCode = !stamp.getLocationCode().isPresent() ? null
					: stamp.getLocationCode().get().v();
			krcdtDaiOutingTime.backStampRoundingTimeDay = stamp.getAfterRoundingTime() == null ? null
					: stamp.getAfterRoundingTime().valueAsMinutes();
			krcdtDaiOutingTime.backStampSourceInfo = stamp.getStampSourceInfo() == null ? null
					: stamp.getStampSourceInfo().value;
			krcdtDaiOutingTime.backStampTime = stamp.getTimeWithDay() == null ? null
					: stamp.getTimeWithDay().valueAsMinutes();
		} else {
			setBackStampNull(krcdtDaiOutingTime);
		}
	}

	private void setBackActualStampNull(KrcdtDaiOutingTime krcdtDaiOutingTime) {
		krcdtDaiOutingTime.backActualPlaceCode = null;
		krcdtDaiOutingTime.backActualRoundingTimeDay = null;
		krcdtDaiOutingTime.backActualSourceInfo = null;
		krcdtDaiOutingTime.backActualTime = null;
	}

	private void setBackStampNull(KrcdtDaiOutingTime krcdtDaiOutingTime) {
		krcdtDaiOutingTime.backStampPlaceCode = null;
		krcdtDaiOutingTime.backStampRoundingTimeDay = null;
		krcdtDaiOutingTime.backStampSourceInfo = null;
		krcdtDaiOutingTime.backStampTime = null;
	}

	private void setGoOutMainStamp(TimeActualStamp stamp, KrcdtDaiOutingTime krcdtDaiOutingTime) {
		if (stamp != null) {
			setGoOutActualStamp(stamp.getActualStamp().orElse(null), krcdtDaiOutingTime);
			krcdtDaiOutingTime.outNumberStamp = stamp.getNumberOfReflectionStamp();
			setGoOutStamp(stamp.getStamp().orElse(null), krcdtDaiOutingTime);
		} else {
			krcdtDaiOutingTime.outActualPlaceCode = null;
			krcdtDaiOutingTime.outActualRoundingTimeDay = null;
			krcdtDaiOutingTime.outActualSourceInfo = null;
			krcdtDaiOutingTime.outActualTime = null;
			setGoOutStampNull(krcdtDaiOutingTime);
		}
	}

	private void setGoOutActualStamp(WorkStamp stamp, KrcdtDaiOutingTime krcdtDaiOutingTime) {
		if (stamp != null) {
			krcdtDaiOutingTime.outActualPlaceCode = !stamp.getLocationCode().isPresent() ? null
					: stamp.getLocationCode().get().v();
			krcdtDaiOutingTime.outActualRoundingTimeDay = stamp.getAfterRoundingTime() == null ? null
					: stamp.getAfterRoundingTime().valueAsMinutes();
			krcdtDaiOutingTime.outActualSourceInfo = stamp.getStampSourceInfo() == null ? null
					: stamp.getStampSourceInfo().value;
			krcdtDaiOutingTime.outActualTime = stamp.getTimeWithDay() == null ? null
					: stamp.getTimeWithDay().valueAsMinutes();
		} else {
			setGoOutActualStampNull(krcdtDaiOutingTime);
		}
	}

	private void setGoOutActualStampNull(KrcdtDaiOutingTime krcdtDaiOutingTime) {
		krcdtDaiOutingTime.outActualPlaceCode = null;
		krcdtDaiOutingTime.outActualRoundingTimeDay = null;
		krcdtDaiOutingTime.outActualSourceInfo = null;
		krcdtDaiOutingTime.outActualTime = null;
	}

	private void setGoOutStamp(WorkStamp stamp, KrcdtDaiOutingTime krcdtDaiOutingTime) {
		if (stamp != null) {
			krcdtDaiOutingTime.outStampPlaceCode = !stamp.getLocationCode().isPresent() ? null
					: stamp.getLocationCode().get().v();
			krcdtDaiOutingTime.outStampRoundingTimeDay = stamp.getAfterRoundingTime() == null ? null
					: stamp.getAfterRoundingTime().valueAsMinutes();
			krcdtDaiOutingTime.outStampSourceInfo = stamp.getStampSourceInfo() == null ? null
					: stamp.getStampSourceInfo().value;
			krcdtDaiOutingTime.outStampTime = stamp.getTimeWithDay() == null ? null
					: stamp.getTimeWithDay().valueAsMinutes();
		} else {
			setGoOutStampNull(krcdtDaiOutingTime);
		}
	}

	private void setGoOutStampNull(KrcdtDaiOutingTime krcdtDaiOutingTime) {
		krcdtDaiOutingTime.outStampPlaceCode = null;
		krcdtDaiOutingTime.outStampRoundingTimeDay = null;
		krcdtDaiOutingTime.outStampSourceInfo = null;
		krcdtDaiOutingTime.outStampTime = null;
	}

	@Override
	public List<OutingTimeOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd) {
		List<OutingTimeOfDailyPerformance> result = new ArrayList<>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM KrcdtDaiOutingTime a ");
		query.append("WHERE a.krcdtDaiOutingTimePK.employeeId IN :employeeId ");
		query.append("AND a.krcdtDaiOutingTimePK.ymd <= :end AND a.krcdtDaiOutingTimePK.ymd >= :start");
		TypedQueryWrapper<KrcdtDaiOutingTime> tQuery = queryProxy().query(query.toString(), KrcdtDaiOutingTime.class);
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(tQuery.setParameter("employeeId", empIds).setParameter("start", ymd.start())
					.setParameter("end", ymd.end()).getList().stream()
					.collect(Collectors
							.groupingBy(c -> c.krcdtDaiOutingTimePK.employeeId + c.krcdtDaiOutingTimePK.ymd.toString()))
					.entrySet().stream()
					.map(c -> new OutingTimeOfDailyPerformance(c.getValue().get(0).krcdtDaiOutingTimePK.employeeId,
							c.getValue().get(0).krcdtDaiOutingTimePK.ymd,
							c.getValue().stream().map(x -> toDtomain(x)).collect(Collectors.toList())))
					.collect(Collectors.toList()));
		});
		return result;
	}

	private OutingTimeSheet toDtomain(KrcdtDaiOutingTime x) {
		WorkStamp outStamp = new WorkStamp(
				x.outStampRoundingTimeDay != null ? new TimeWithDayAttr(x.outStampRoundingTimeDay) : null,
				x.outStampTime != null ? new TimeWithDayAttr(x.outStampTime) : null,
				x.outStampPlaceCode != null ? new WorkLocationCD(x.outStampPlaceCode) : null,
				x.outStampSourceInfo != null ? EnumAdaptor.valueOf(x.outStampSourceInfo, StampSourceInfo.class) : null);
		WorkStamp outActualStamp = new WorkStamp(
				x.outActualRoundingTimeDay != null ? new TimeWithDayAttr(x.outActualRoundingTimeDay) : null,
				x.outActualTime != null ? new TimeWithDayAttr(x.outActualTime) : null,
				x.outActualPlaceCode != null ? new WorkLocationCD(x.outActualPlaceCode) : null,
				x.outActualSourceInfo != null ? EnumAdaptor.valueOf(x.outActualSourceInfo, StampSourceInfo.class)
						: null);
		TimeActualStamp goOut = new TimeActualStamp(outActualStamp, outStamp, x.outNumberStamp);
		WorkStamp backStamp = new WorkStamp(
				x.backStampRoundingTimeDay != null ? new TimeWithDayAttr(x.backStampRoundingTimeDay) : null,
				x.backStampTime != null ? new TimeWithDayAttr(x.backStampTime) : null,
				x.backStampPlaceCode != null ? new WorkLocationCD(x.backStampPlaceCode) : null,
				x.backStampSourceInfo != null ? EnumAdaptor.valueOf(x.backStampSourceInfo, StampSourceInfo.class)
						: null);
		WorkStamp backActualStamp = new WorkStamp(
				x.backActualRoundingTimeDay != null ? new TimeWithDayAttr(x.backActualRoundingTimeDay) : null,
				x.backActualTime != null ? new TimeWithDayAttr(x.backActualTime) : null,
				x.backActualPlaceCode != null ? new WorkLocationCD(x.backActualPlaceCode) : null,
				x.backActualSourceInfo != null ? EnumAdaptor.valueOf(x.backActualSourceInfo, StampSourceInfo.class)
						: null);
		TimeActualStamp comeBack = new TimeActualStamp(backActualStamp, backStamp, x.backNumberStamp);
		GoingOutReason reasonForGoOut = EnumAdaptor.valueOf(x.outingReason, GoingOutReason.class);
		AttendanceTime outingTimeCalculation = new AttendanceTime(x.outingTimeCalculation);
		AttendanceTime outingTime = new AttendanceTime(x.outingTime);
		OutingTimeSheet outingTimeSheet = new OutingTimeSheet(new OutingFrameNo(x.krcdtDaiOutingTimePK.outingFrameNo),
				Optional.of(goOut), outingTimeCalculation, outingTime, reasonForGoOut, Optional.of(comeBack));
		return outingTimeSheet;
	}

	@Override
	public List<OutingTimeOfDailyPerformance> finds(Map<String, List<GeneralDate>> param) {
		List<OutingTimeOfDailyPerformance> result = new ArrayList<>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM KrcdtDaiOutingTime a ");
		query.append("WHERE a.krcdtDaiOutingTimePK.employeeId IN :employeeId ");
		query.append("AND a.krcdtDaiOutingTimePK.ymd IN :date");
		TypedQueryWrapper<KrcdtDaiOutingTime> tQuery = queryProxy().query(query.toString(), KrcdtDaiOutingTime.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream()
					.filter(c -> p.get(c.krcdtDaiOutingTimePK.employeeId).contains(c.krcdtDaiOutingTimePK.ymd))
					.collect(Collectors
							.groupingBy(c -> c.krcdtDaiOutingTimePK.employeeId + c.krcdtDaiOutingTimePK.ymd.toString()))
					.entrySet().stream()
					.map(c -> new OutingTimeOfDailyPerformance(c.getValue().get(0).krcdtDaiOutingTimePK.employeeId,
							c.getValue().get(0).krcdtDaiOutingTimePK.ymd,
							c.getValue().stream().map(x -> toDtomain(x)).collect(Collectors.toList())))
					.collect(Collectors.toList()));
		});
		return result;
	}
}
