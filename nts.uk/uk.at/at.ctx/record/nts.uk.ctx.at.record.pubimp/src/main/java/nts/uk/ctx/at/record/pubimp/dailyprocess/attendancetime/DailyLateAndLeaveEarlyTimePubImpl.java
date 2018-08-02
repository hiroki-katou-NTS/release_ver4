package nts.uk.ctx.at.record.pubimp.dailyprocess.attendancetime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.DailyLateAndLeaveEarlyTimePub;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.DailyLateAndLeaveEarlyTimePubExport;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.DailyLateAndLeaveEarlyTimePubImport;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.exportparam.LateLeaveEarlyAtr;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.exportparam.LateLeaveEarlyManage;

/**
 * RequestListNo197
 * @author keisuke_hoshina
 *
 */
@Stateless
public class DailyLateAndLeaveEarlyTimePubImpl implements DailyLateAndLeaveEarlyTimePub{

	@Inject
	private AttendanceTimeRepository attendanceTimeRepository; 
	
	@Override
	public DailyLateAndLeaveEarlyTimePubExport getLateLeaveEarly(DailyLateAndLeaveEarlyTimePubImport imp) {
		val domains = attendanceTimeRepository.findByPeriodOrderByYmd(imp.getEmployeeId(), imp.getDaterange());
		List<LateLeaveEarlyManage> lateLeaveEarlyManages = new ArrayList<>();
		for(AttendanceTimeOfDailyPerformance nowDomain : domains) {
			if(nowDomain != null && nowDomain.getActualWorkingTimeOfDaily() != null && nowDomain.getActualWorkingTimeOfDaily().getTotalWorkingTime() != null) {
				boolean kt = false;
				LateLeaveEarlyManage lateLeaveEarlyManage = new LateLeaveEarlyManage(nowDomain.getYmd(), false, false, false, false);
				TotalWorkingTime totalWorkingTime = nowDomain.getActualWorkingTimeOfDaily().getTotalWorkingTime();
				if(!totalWorkingTime.getLateTimeOfDaily().isEmpty() && totalWorkingTime.getLateTimeOfDaily().get(0).getLateTime().getTime().greaterThan(0)) {
					if(totalWorkingTime.getLateTimeOfDaily().get(0).getWorkNo().v() == 1) {
						lateLeaveEarlyManage.setLate1(true);
					}else if(totalWorkingTime.getLateTimeOfDaily().get(0).getWorkNo().v() == 2) {
						lateLeaveEarlyManage.setLate2(true);
					}
					kt = true;
				}
				if(!totalWorkingTime.getLeaveEarlyTimeOfDaily().isEmpty() && totalWorkingTime.getLeaveEarlyTimeOfDaily().get(0).getLeaveEarlyTime().getTime().greaterThan(0)) {
					if(totalWorkingTime.getLeaveEarlyTimeOfDaily().get(0).getWorkNo().v() == 1) {
						lateLeaveEarlyManage.setLeaveEarly1(true);
					}else if(totalWorkingTime.getLeaveEarlyTimeOfDaily().get(0).getWorkNo().v() == 2) {
						lateLeaveEarlyManage.setLeaveEarly2(true);
					}
					kt = true;
				}
				if(kt) { 
					lateLeaveEarlyManages.add(lateLeaveEarlyManage);
				}
			}
		}
		return new DailyLateAndLeaveEarlyTimePubExport(lateLeaveEarlyManages);
	}

	/**
	 * MapからListへの変換
	 * @param returnMap 変換したいmap
	 * @return 変換後List
	 */
	private DailyLateAndLeaveEarlyTimePubExport plainMap(Map<GeneralDate, List<LateLeaveEarlyAtr>> returnMap) {
		List<LateLeaveEarlyManage> list = new ArrayList<>();
		for(Map.Entry<GeneralDate, List<LateLeaveEarlyAtr>> e : returnMap.entrySet()) {
			//X枠目の遅刻、早退が入っているかチェック
			Optional<LateLeaveEarlyAtr> element1 = e.getValue().stream().filter(tc -> tc.getWorkNo().v().intValue() == 1).findFirst();
			boolean late1 = element1.isPresent() && e.getValue().get(element1.get().getWorkNo().v() - 1).isLate()
							?true
							:false;
			boolean leaveEarly1 = element1.isPresent() && e.getValue().get(element1.get().getWorkNo().v() - 1).isLeaveEarly()
								  ?true
								  :false;
			
			
			Optional<LateLeaveEarlyAtr> element2 = e.getValue().stream().filter(tc -> tc.getWorkNo().v().intValue() == 2).findFirst();
			boolean late2 = element2.isPresent() && e.getValue().get(element2.get().getWorkNo().v() - 1).isLate()
							?true
							:false;
			boolean leaveEarly2 = element2.isPresent() && e.getValue().get(element1.get().getWorkNo().v() - 1).isLeaveEarly()
								  ?true
								  :false;
			list.add(new LateLeaveEarlyManage(e.getKey(), late1,late2,leaveEarly1,leaveEarly2));
			
		}
		return new DailyLateAndLeaveEarlyTimePubExport(list);
	}

	/**
	 * 早退の埋め込み(遅刻取得時に作成したクラスに対して埋め込む) 
	 * @return
	 */
	private List<LateLeaveEarlyAtr> getLeaveEarlyList(List<LeaveEarlyTimeOfDaily> leaveEarlyTimeOfDaily,
			List<LateLeaveEarlyAtr> returnList) {
		
		leaveEarlyTimeOfDaily.forEach(tc ->{
			val test = returnList.stream()
								 .filter(ts -> ts.getWorkNo().equals(tc.getWorkNo()))
								 .findFirst();
			if(test.isPresent()) {
				int index = returnList.indexOf(test.get());
				returnList.get(index).setLeaveEarly(true);
			}
			else {
				returnList.add(new LateLeaveEarlyAtr(test.get().getWorkNo(),false,true));
			}
		});
		return returnList;
	}

	/**
	 * 遅刻の取得
	 */
	private List<LateLeaveEarlyAtr> getLateList(List<LateTimeOfDaily> lateTimeOfDaily) {
		List<LateLeaveEarlyAtr> returnList = new ArrayList<>();
		for(LateTimeOfDaily a : lateTimeOfDaily) {
			returnList.add(new LateLeaveEarlyAtr(a.getWorkNo(),true,false));
		}
		return returnList;
	}

}
