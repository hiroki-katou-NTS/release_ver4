package nts.uk.ctx.at.record.infra.repository.breakorgoout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
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

@Stateless
public class JpaOutingTimeOfDailyPerformanceRepository extends JpaRepository
		implements OutingTimeOfDailyPerformanceRepository {

	private static final String REMOVE_BY_EMPLOYEE;

	private static final String DEL_BY_LIST_KEY;

	private static final String SELECT_BY_EMPLOYEE_AND_DATE;

	private static final String SELECT_BY_KEY;

	private static final String CHECK_EXIST_DATA;

	private static final String FIND_BY_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd = :ymd ");
		REMOVE_BY_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd = :ymd ");
		SELECT_BY_EMPLOYEE_AND_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd IN :ymds ");
		DEL_BY_LIST_KEY = builderString.toString();

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

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd = :ymd ");
		builderString.append("AND a.krcdtDaiOutingTimePK.outingFrameNo = :outingFrameNo ");
		FIND_BY_KEY = builderString.toString();
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).executeUpdate();
	}

	@Override
	public void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> ymds) {
		this.getEntityManager().createQuery(DEL_BY_LIST_KEY).setParameter("employeeIds", employeeIds)
				.setParameter("ymds", ymds).executeUpdate();
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
		commandProxy().insertAll(outing.getOutingTimeSheets().stream()
				.map(c -> KrcdtDaiOutingTime.toEntity(outing.getEmployeeId(), outing.getYmd(), c))
				.collect(Collectors.toList()));
	}

	// @Override
	// public void update(OutingTimeOfDailyPerformance outing) {
	// commandProxy().insertAll(outing.getOutingTimeSheets().stream()
	// .map(c -> KrcdtDaiOutingTime.toEntity(outing.getEmployeeId(),
	// outing.getYmd(), c))
	// .collect(Collectors.toList()));
	// }

	@Override
	public void insert(OutingTimeOfDailyPerformance outingTimeOfDailyPerformance) {
		// KrcdtDaiOutingTime.toEntity(outingTimeOfDailyPerformance).stream().forEach(item
		// -> {
		// this.commandProxy().insert(item);
		// });
		// this.getEntityManager().flush();
	}

	@Override
	public boolean checkExistData(String employeeId, GeneralDate ymd, OutingFrameNo outingFrameNo) {
		return this.queryProxy().query(CHECK_EXIST_DATA, long.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).setParameter("outingFrameNo", outingFrameNo.v()).getSingle().get() > 0;
	}

	@Override
	public void updateOneDataInlist(String employeeId, GeneralDate ymd, OutingTimeSheet outingTimeSheet) {
		Optional<KrcdtDaiOutingTime> krcdtDaiOutingTime = this.queryProxy().query(FIND_BY_KEY, KrcdtDaiOutingTime.class)
				.setParameter("employeeId", employeeId).setParameter("ymd", ymd)
				.setParameter("outingFrameNo", outingTimeSheet.getOutingFrameNo().v()).getSingle();

		if (krcdtDaiOutingTime.isPresent()) {
			KrcdtDaiOutingTime outingTime = krcdtDaiOutingTime.get();
			setEntityValue(outingTimeSheet, outingTime);
			this.commandProxy().update(outingTime);
			this.getEntityManager().flush();
		}
	}

	@Override
	public void update(OutingTimeOfDailyPerformance domain) {
		List<Integer> outingFrameNos = domain.getOutingTimeSheets().stream().map(item -> {
			return item.getOutingFrameNo().v();
		}).collect(Collectors.toList());
		List<KrcdtDaiOutingTime> krcdtDaiOutingTimeLists = this.queryProxy()
				.query(SELECT_BY_KEY, KrcdtDaiOutingTime.class).setParameter("employeeId", domain.getEmployeeId())
				.setParameter("ymd", domain.getYmd()).setParameter("outingFrameNos", outingFrameNos).getList();

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
			setBackActualStamp(stamp.getActualStamp(), krcdtDaiOutingTime);
			krcdtDaiOutingTime.backNumberStamp = stamp.getNumberOfReflectionStamp();
			setBackStamp(stamp.getStamp().orElse(null), krcdtDaiOutingTime);
		} else {
			setBackActualStampNull(krcdtDaiOutingTime);
			setGoOutStampNull(krcdtDaiOutingTime);
		}
	}

	private void setBackActualStamp(WorkStamp stamp, KrcdtDaiOutingTime krcdtDaiOutingTime) {
		if (stamp != null) {
			krcdtDaiOutingTime.backActualPlaceCode = stamp.getLocationCode() == null ? null
					: stamp.getLocationCode().v();
			krcdtDaiOutingTime.backActualRoundingTimeDay = stamp.getAfterRoundingTime() == null ? null
					: stamp.getAfterRoundingTime().valueAsMinutes();
			krcdtDaiOutingTime.backActualSourceInfo = stamp.getStampSourceInfo().value;
			krcdtDaiOutingTime.backActualTime = stamp.getTimeWithDay() == null ? null
					: stamp.getTimeWithDay().valueAsMinutes();
		} else {
			setBackActualStampNull(krcdtDaiOutingTime);
		}
	}

	private void setBackStamp(WorkStamp stamp, KrcdtDaiOutingTime krcdtDaiOutingTime) {
		if (stamp != null) {
			krcdtDaiOutingTime.backStampPlaceCode = stamp.getLocationCode() == null ? null
					: stamp.getLocationCode().v();
			krcdtDaiOutingTime.backStampRoundingTimeDay = stamp.getAfterRoundingTime() == null ? null
					: stamp.getAfterRoundingTime().valueAsMinutes();
			krcdtDaiOutingTime.backStampSourceInfo = stamp.getStampSourceInfo().value;
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
			setGoOutActualStamp(stamp.getActualStamp(), krcdtDaiOutingTime);
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
			krcdtDaiOutingTime.outActualPlaceCode = stamp.getLocationCode() == null ? null
					: stamp.getLocationCode().v();
			krcdtDaiOutingTime.outActualRoundingTimeDay = stamp.getAfterRoundingTime() == null ? null
					: stamp.getAfterRoundingTime().valueAsMinutes();
			krcdtDaiOutingTime.outActualSourceInfo = stamp.getStampSourceInfo().value;
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
			krcdtDaiOutingTime.outStampPlaceCode = stamp.getLocationCode() == null ? null : stamp.getLocationCode().v();
			krcdtDaiOutingTime.outStampRoundingTimeDay = stamp.getAfterRoundingTime() == null ? null
					: stamp.getAfterRoundingTime().valueAsMinutes();
			krcdtDaiOutingTime.outStampSourceInfo = stamp.getStampSourceInfo().value;
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
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM KrcdtDaiOutingTime a ");
		query.append("WHERE a.krcdtDaiOutingTimePK.employeeId IN :employeeId ");
		query.append("AND a.krcdtDaiOutingTimePK.ymd <= :end AND a.krcdtDaiOutingTimePK.ymd >= :start");
		return queryProxy().query(query.toString(), KrcdtDaiOutingTime.class).setParameter("employeeId", employeeId)
				.setParameter("start", ymd.start()).setParameter("end", ymd.end()).getList().stream()
				.collect(Collectors.groupingBy(c -> c.krcdtDaiOutingTimePK.employeeId + c.krcdtDaiOutingTimePK.ymd.toString()))
				.entrySet().stream().map(c -> new OutingTimeOfDailyPerformance(
												c.getValue().get(0).krcdtDaiOutingTimePK.employeeId,
												c.getValue().get(0).krcdtDaiOutingTimePK.ymd,
												c.getValue().stream().map(x -> toDtomain(x)).collect(Collectors.toList())))
				.collect(Collectors.toList());
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
}
