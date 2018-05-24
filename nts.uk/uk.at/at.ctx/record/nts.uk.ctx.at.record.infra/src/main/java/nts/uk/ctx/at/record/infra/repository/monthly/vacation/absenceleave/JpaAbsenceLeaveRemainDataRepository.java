package nts.uk.ctx.at.record.infra.repository.monthly.vacation.absenceleave;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainDataRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.vacation.absenceleave.KrcdtMonSubOfHdRemain;

@Stateless
public class JpaAbsenceLeaveRemainDataRepository extends JpaRepository implements AbsenceLeaveRemainDataRepository{
	private String QUERY_BY_SID_YM_STATUS = "SELECT c FROM KrcdtMonSubOfHdRemain c"
			+ " WHERE c.pk.sId = :employeeId"
			+ " AND c.pk.ym = :ym"
			+ " AND c.closureStatus = :status"
			+ " ORDER BY c.endDate ASC";
	
	@Override
	public List<AbsenceLeaveRemainData> getDataBySidYmClosureStatus(String employeeId, YearMonth ym,
			ClosureStatus status) {
		return  this.queryProxy().query(QUERY_BY_SID_YM_STATUS, KrcdtMonSubOfHdRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("ym", ym.v())
				.setParameter("closureStatus", status.value)
				.getList(c -> toDomain(c));
	}

	private AbsenceLeaveRemainData toDomain(KrcdtMonSubOfHdRemain c) {
		return new AbsenceLeaveRemainData(c.pk.sId,
				YearMonth.of(c.pk.ym),
				c.pk.closureId,
				c.pk.closureDay, 
				c.pk.isLastDay == 1 ? true : false,
				EnumAdaptor.valueOf(c.closureStatus, ClosureStatus.class),
				c.startDate,
				c.endDate,
				new AttendanceDaysMonth(c.occurredDays),
				new AttendanceDaysMonth(c.usedDays),
				new AttendanceDaysMonth(c.remainingDays),
				new AttendanceDaysMonth(c.carryForWardDays),
				new AttendanceDaysMonth(c.unUsedDays));
	}

}
