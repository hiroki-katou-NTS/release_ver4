package nts.uk.ctx.at.request.pubimp.application.recognition;
/*import nts.uk.ctx.at.request.pub.application.recognition.ApplicationHdTimeExport;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReflectedState;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.pub.application.recognition.ApplicationOvertimeExport;
import nts.uk.ctx.at.request.pub.application.recognition.ApplicationOvertimePub;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ApplicationOvertimePubImpl implements ApplicationOvertimePub {
	
	@Inject
	private ApplicationRepository repoApplication;
	
	
	
	/**
	 * Request list No.236
	 * @return List (申請日, total残業時間)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ApplicationOvertimeExport> acquireTotalApplicationOverTimeHours(String sId, GeneralDate startDate, GeneralDate endDate) {
		String companyId = AppContexts.user().companyId();
		Map<String, AppHolidayWork_Old> mapHd = new HashMap<>();
		Map<String, AppOverTime_Old> mapOt = new HashMap<>();
		List<ApplicationOvertimeExport> results = new ArrayList<>();
		//条件を元に、ドメインモデル「残業申請」を取得する
		List<Application> appOt = repoApplication.getListAppByType(companyId, sId, startDate, endDate, PrePostAtr.POSTERIOR.value,
				ApplicationType.OVER_TIME_APPLICATION.value, Arrays.asList(ReflectedState.NOTREFLECTED.value,ReflectedState.WAITREFLECTION.value));
		//条件を元に、ドメインモデル「休日出勤申請」を取得する
		List<Application> appHdw = repoApplication.getListAppByType(companyId, sId, startDate, endDate, PrePostAtr.POSTERIOR.value,
				ApplicationType.HOLIDAY_WORK_APPLICATION.value, Arrays.asList(ReflectedState.NOTREFLECTED.value,ReflectedState.WAITREFLECTION.value));
		List<String> lstIdOt = new ArrayList<>();
		List<String> lstIdHdw = new ArrayList<>();
		Map<GeneralDate, String> mapAppOt = new HashMap<>();
		Map<GeneralDate, String> mapAppHdw = new HashMap<>();
		////※同じ申請日の残業申請が２件あった場合、入力日が後のもの（latest）だけSetする
		for (Application app : appOt) {
			if(mapAppOt.containsKey(app.getAppDate().getApplicationDate())){
				continue;
			}
			mapAppOt.put(app.getAppDate().getApplicationDate(), app.getAppID());
			lstIdOt.add(app.getAppID());
		}
		//※同じ申請日の休日出勤申請が２件あった場合、入力日が後のもの（latest）だけ集計する
		for (Application app : appHdw) {
			if(mapAppHdw.containsKey(app.getAppDate().getApplicationDate())){
				continue;
			}
			mapAppHdw.put(app.getAppDate().getApplicationDate(), app.getAppID());
			lstIdHdw.add(app.getAppID());
		}
		//get detail overtime
		mapOt = repoOvertime.getListAppOvertimeFrame(companyId, lstIdOt);
		//get detail holiday work
		mapHd = appHdWorkRepository.getListAppHdWorkFrame(companyId, lstIdHdw);
		//tinh thoi gian over night the Don lam them.
		for(Map.Entry<GeneralDate, String> entry : mapAppOt.entrySet()) {
			AppOverTime_Old otDetail = mapOt.get(entry.getValue());
			List<OverTimeInput> oTWorkInput = otDetail.getOverTimeInput();
			int cal = 0;
			//残業申請．残業時間1～10
			for (OverTimeInput item : oTWorkInput) {
				if(item.getAttendanceType().equals(AttendanceType.NORMALOVERTIME) && item.getFrameNo() != 11 && item.getFrameNo() != 12){
					cal = item.getApplicationTime() == null ? cal : cal + item.getApplicationTime().v();
				}
			}
			results.add(new ApplicationOvertimeExport(entry.getKey(), cal));
		}
		List<ApplicationOvertimeExport> bonus = new ArrayList<>();
		//tinh thoi gian over night bo sung theo Don lam ngay nghi
		for(Map.Entry<GeneralDate, String> entry : mapAppHdw.entrySet()) {
			AppHolidayWork_Old hdDetail = mapHd.get(entry.getValue());
			List<HolidayWorkInput> hdWorkInput = hdDetail.getHolidayWorkInputs();
			int cal = 0;
			//休日出勤申請．残業時間1～10
			for (HolidayWorkInput item : hdWorkInput) {
				if(item.getAttendanceType().equals(AttendanceType.NORMALOVERTIME) && item.getFrameNo() != 11 && item.getFrameNo() != 12){
					cal = item.getApplicationTime() == null ? cal : cal + item.getApplicationTime().v();
				}
			}
			//find index exist
			Integer index = this.findIndexExist(results, entry.getKey());
			if(index != null && cal != 0){//Ngay do da co va time them != 0
				ApplicationOvertimeExport objOld = results.get(index);
				ApplicationOvertimeExport objNew  = new ApplicationOvertimeExport(objOld.getDate(), objOld.getTotalOtHours() + cal);
				results.set(index, objNew);
			}
			if(index == null){//Ngay moi
				bonus.add(new ApplicationOvertimeExport(entry.getKey(), cal));
			}
		}
		results.addAll(bonus);
		return results;
	}
	
	private Integer findIndexExist(List<ApplicationOvertimeExport> lstResuls, GeneralDate date){
		for (int index = 0; index < lstResuls.size(); index ++) {
			if(lstResuls.get(index).getDate().equals(date)){
				return index;
			}
		}
		return null;
	}
}
