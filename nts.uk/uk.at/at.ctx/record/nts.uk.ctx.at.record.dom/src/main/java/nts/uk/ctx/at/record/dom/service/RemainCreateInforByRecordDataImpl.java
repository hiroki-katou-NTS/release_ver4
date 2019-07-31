package nts.uk.ctx.at.record.dom.service;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.RecordRemainCreateInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByRecordData;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class RemainCreateInforByRecordDataImpl implements RemainCreateInforByRecordData{
	@Inject
	private AttendanceTimeRepository attendanceRespo;
	@Inject
	private WorkInformationRepository workRespo;
	@Inject
	private RemainNumberCreateInformation remainInfor;
	@Override
	public List<RecordRemainCreateInfor> lstRecordRemainData(String cid, String sid, DatePeriod dateData) {
		//ドメインモデル「日別実績の勤怠時間」を取得する
		List<AttendanceTimeOfDailyPerformance> lstAttendanceTimeData =  attendanceRespo.findByPeriodOrderByYmd(sid, dateData);
		//ドメインモデル「日別実績の勤務情報」を取得する
		List<WorkInfoOfDailyPerformance> lstWorkInfor = workRespo.findByPeriodOrderByYmd(sid, dateData);
		//残数作成元情報を作成する
		return remainInfor.createRemainInfor(lstAttendanceTimeData, lstWorkInfor);
	}
	
	
	@Override
	public List<RecordRemainCreateInfor> lstRecordRemainData(String cid, String sid, List<GeneralDate> dateData) {
		//ドメインモデル「日別実績の勤怠時間」を取得する
		List<AttendanceTimeOfDailyPerformance> lstAttendanceTimeData = attendanceRespo.find(sid, dateData);
		//ドメインモデル「日別実績の勤務情報」を取得する
		List<WorkInfoOfDailyPerformance> lstWorkInfor = workRespo.findByListDate(sid, dateData);		
		return remainInfor.createRemainInfor(lstAttendanceTimeData, lstWorkInfor);
	}
	
}
