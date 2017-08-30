package nts.uk.screen.at.infra.schedule.basicschedule;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdtBasicSchedule;
import nts.uk.screen.at.app.schedule.basicschedule.BasicScheduleScreenDto;
import nts.uk.screen.at.app.schedule.basicschedule.BasicScheduleScreenRepository;
import nts.uk.screen.at.app.schedule.basicschedule.WorkTimeScreenDto;

/**
 * 
 * @author sonnh1
 *
 */
@Stateless
public class JpaBasicScheduleScreenRepository extends JpaRepository implements BasicScheduleScreenRepository {

	private static final String SEL = "SELECT c FROM KscdtBasicSchedule c ";
	private static final String SEL_BY_LIST_SID_AND_DATE = SEL
			+ "WHERE c.kscdpBSchedulePK.sId IN :sId AND c.kscdpBSchedulePK.date >= :startDate AND c.kscdpBSchedulePK.date <= :endDate";

	private static final String GET_WORK_TIME_AND_WT_DAY = "SELECT NEW " + WorkTimeScreenDto.class.getName()
			+ " (a.kwtmpWorkTimePK.siftCD, a.workTimeName, a.workTimeAbName, a.workTimeDailyAtr, a.workTimeMethodSet, a.displayAtr, a.note,"
			+ " b.a_m_StartClock, b.p_m_EndClock, b.kwtdpWorkTimeDayPK.timeNumberCnt)"
			+ " FROM KwtmtWorkTime a JOIN KwtdtWorkTimeDay b ON a.kwtmpWorkTimePK.siftCD = b.kwtdpWorkTimeDayPK.siftCD"
			+ " JOIN KshmtWorkTimeOrder c ON a.kwtmpWorkTimePK.siftCD = c.kshmpWorkTimeOrderPK.workTimeCode"
			+ " WHERE a.kwtmpWorkTimePK.companyID = :companyId" + " AND a.displayAtr = :displayAtr"
			+ " ORDER BY c.dispOrder ASC";

	private static BasicScheduleScreenDto toDto(KscdtBasicSchedule entity) {
		return new BasicScheduleScreenDto(entity.kscdpBSchedulePK.sId, entity.kscdpBSchedulePK.date,
				entity.workTypeCode, entity.workTimeCode);
	}

	/**
	 * get list BasicSchedule by list String and startDate and endDate
	 */
	@Override
	public List<BasicScheduleScreenDto> getByListSidAndDate(List<String> sId, GeneralDate startDate,
			GeneralDate endDate) {
		return this.queryProxy().query(SEL_BY_LIST_SID_AND_DATE, KscdtBasicSchedule.class).setParameter("sId", sId)
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getList(x -> toDto(x));
	}

	/**
	 * get list WorkTime by CompanyId and DisplayAtr = DISPLAY Join with table
	 * WorkTimeDay Join with table ORDER to sort workTimeCode
	 */
	@Override
	public List<WorkTimeScreenDto> getListWorkTime(String companyId, int displayAtr) {
		return this.queryProxy().query(GET_WORK_TIME_AND_WT_DAY, WorkTimeScreenDto.class)
				.setParameter("companyId", companyId).setParameter("displayAtr", displayAtr).getList();
	}
}
