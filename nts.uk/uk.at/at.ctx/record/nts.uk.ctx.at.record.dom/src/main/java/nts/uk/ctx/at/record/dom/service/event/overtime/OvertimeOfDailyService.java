package nts.uk.ctx.at.record.dom.service.event.overtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
@Stateless
public class OvertimeOfDailyService {
	@Inject
	private WorkUpdateService recordUpdate;
	@Inject
	private EditStateOfDailyPerformanceRepository editStateDaily;
	@Inject
	private AttendanceTimeRepository attendanceTimeRepo;
	/**
	 * 事前残業、休日出勤時間を補正する
	 * @param working
	 * @param cachedWorkType
	 * @return
	 */
	public IntegrationOfDaily correct(IntegrationOfDaily working,Optional<WorkType> cachedWorkType){
		if(!cachedWorkType.isPresent() 
				&& !working.getAttendanceTimeOfDailyPerformance().isPresent()) {
			return working;
		}
		List<Integer> itemIdList = new ArrayList<>();
		ExcessOfStatutoryTimeOfDaily timeOfDaily = working.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily();
		if(cachedWorkType.get().getDailyWork().getOneDay() == WorkTypeClassification.HolidayWork) {
			UpdateDataWhenHolidayWork(itemIdList, timeOfDaily);
		} else if(cachedWorkType.get().getDailyWork().getAttendanceHolidayAttr() != AttendanceHolidayAttr.HOLIDAY){
			UpdateDataWhenNotHolidaywork(itemIdList, timeOfDaily);
		} else {
			UpdateDataWhenHolidayWork(itemIdList, timeOfDaily);
			UpdateDataWhenNotHolidaywork(itemIdList, timeOfDaily);
			//事前所定外深夜時間をクリアする
			ExcessOfStatutoryMidNightTime exMidNightTime = timeOfDaily.getExcessOfStatutoryMidNightTime();
			ExcessOfStatutoryMidNightTime tmp = new ExcessOfStatutoryMidNightTime(exMidNightTime.getTime(), new AttendanceTime(0));
			timeOfDaily.setExcessOfStatutoryMidNightTime(tmp);
			itemIdList.add(565);
		}
		//休出時間、替時間(休出)の反映、をクリアする
		itemIdList.addAll(recordUpdate.lstAfterWorktimeFrameItem());
		itemIdList.addAll(recordUpdate.lstTranfertimeFrameItem());
		
		attendanceTimeRepo.updateFlush(working.getAttendanceTimeOfDailyPerformance().get());
		//削除
		editStateDaily.deleteByListItemId(working.getWorkInformation().getEmployeeId(), working.getWorkInformation().getYmd(), itemIdList);
		return working;
	}
	private void UpdateDataWhenNotHolidaywork(List<Integer> itemIdList, ExcessOfStatutoryTimeOfDaily timeOfDaily) {
		//休日出勤時間をクリアする
		timeOfDaily.getWorkHolidayTime().ifPresent(x -> {
			List<HolidayWorkFrameTime> lstHoliday = x.getHolidayWorkFrameTime();
			lstHoliday.stream().forEach(y -> {
				y.setBeforeApplicationTime(Finally.of(new AttendanceTime(0)));
			});
		});
		itemIdList.addAll(recordUpdate.lstPreWorktimeFrameItem());
	}
	private void UpdateDataWhenHolidayWork(List<Integer> itemIdList, ExcessOfStatutoryTimeOfDaily timeOfDaily) {
		//事前残業時間をクリアする
		timeOfDaily.getOverTimeWork().ifPresent(x -> {
			List<OverTimeFrameTime> lstOvertime = x.getOverTimeWorkFrameTime();
			lstOvertime.stream().forEach(y -> {
				y.setBeforeApplicationTime(new AttendanceTime(0));
			});
		});
		itemIdList.addAll(recordUpdate.lstPreOvertimeItem());
		//フレックス時間をクリアする
		Optional<OverTimeOfDaily> optOverTimeOfDaily = timeOfDaily.getOverTimeWork();
		optOverTimeOfDaily.ifPresent(z -> {
			z.setFlexTime(new FlexTime(z.getFlexTime().getFlexTime(), new AttendanceTime(0)));
		});
		itemIdList.add(555);
	}
}
