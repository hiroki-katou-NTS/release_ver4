package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.totaltimes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimes;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesResult;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.algorithm.GetTotalTimesFromDailyRecord;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：日別実績から回数集計結果を取得する
 * @author shuichu_ishida
 */
@Stateless
public class GetTotalTimesFromDailyRecordImpl implements GetTotalTimesFromDailyRecord {

	/** 日別実績の勤怠時間 */
	@Inject
	private AttendanceTimeRepository attendanceTimeRepo;
	/** 日別実績の出退勤の取得 */
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyRepo;
	/** 日別実績の勤務情報 */
	@Inject
	public WorkInformationRepository workInfoOfDailyRepo;
	/** 勤務種類の取得 */
	@Inject
	private WorkTypeRepository workTypeRepo;
	/** 日別実績と勤怠項目の相互変換 */
	@Inject
	private DailyRecordToAttendanceItemConverter dailyToAttendanceItem;

	/** 回数集計結果情報を取得 */
	@Override
	public Optional<TotalTimesResult> getResult(String companyId, String employeeId, DatePeriod period,
			TotalTimes totalTimes) {
		
		TotalTimesFromDailyRecord algorithm = new TotalTimesFromDailyRecord(
				companyId, employeeId, period,
				this.attendanceTimeRepo,
				this.timeLeavingOfDailyRepo,
				this.workInfoOfDailyRepo,
				this.workTypeRepo);
		return algorithm.getResult(totalTimes, period, this.dailyToAttendanceItem);
	}
	
	/** 回数集計結果情報を取得 */
	@Override
	public Map<Integer, TotalTimesResult> getResults(String companyId, String employeeId, DatePeriod period,
			List<TotalTimes> totalTimesList) {
		
		TotalTimesFromDailyRecord algorithm = new TotalTimesFromDailyRecord(
				companyId, employeeId, period,
				this.attendanceTimeRepo,
				this.timeLeavingOfDailyRepo,
				this.workInfoOfDailyRepo,
				this.workTypeRepo);
		return algorithm.getResults(totalTimesList, period, this.dailyToAttendanceItem);
	}
}
