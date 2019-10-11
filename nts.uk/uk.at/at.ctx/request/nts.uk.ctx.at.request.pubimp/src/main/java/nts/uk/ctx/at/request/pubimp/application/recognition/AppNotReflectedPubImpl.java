package nts.uk.ctx.at.request.pubimp.application.recognition;
//import java.util.Collections;
//import java.util.Optional;
//import nts.uk.ctx.at.request.pub.application.recognition.ApplicationHdTimeExport;
//import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
//import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;
//import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
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
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.pub.application.recognition.AppNotReflectedPub;
import nts.uk.ctx.at.request.pub.application.recognition.ApplicationOvertimeExport;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AppNotReflectedPubImpl implements AppNotReflectedPub {
	@Inject
	private ApplicationRepository_New repoApplication;
	
	@Inject
	private OvertimeRepository repoOvertime;
	
	@Inject
	private AppHolidayWorkRepository appHdWorkRepository;
	
	/**
	 * Request list No.300
	 * @return List (年月日, total就業時間外深夜時間)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ApplicationOvertimeExport> acquireAppNotReflected(String sId, GeneralDate startDate, GeneralDate endDate) {
		String companyId = AppContexts.user().companyId();
		Map<String, AppHolidayWork> mapHd = new HashMap<>();
		Map<String, AppOverTime> mapOt = new HashMap<>();
		List<ApplicationOvertimeExport> results = new ArrayList<>();
		//条件を元に、ドメインモデル「残業申請」を取得する
		List<Application_New> appOt = repoApplication.getListAppByType(companyId, sId, startDate, endDate, PrePostAtr.POSTERIOR.value,
				ApplicationType.OVER_TIME_APPLICATION.value, Arrays.asList(ReflectedState_New.NOTREFLECTED.value,ReflectedState_New.WAITREFLECTION.value));
		//条件を元に、ドメインモデル「休日出勤申請」を取得する
		List<Application_New> appHdw = repoApplication.getListAppByType(companyId, sId, startDate, endDate, PrePostAtr.POSTERIOR.value,
				ApplicationType.BREAK_TIME_APPLICATION.value, Arrays.asList(ReflectedState_New.NOTREFLECTED.value,ReflectedState_New.WAITREFLECTION.value));
		List<String> lstIdOt = new ArrayList<>();
		List<String> lstIdHdw = new ArrayList<>();
		Map<GeneralDate, String> mapAppOt = new HashMap<>();
		Map<GeneralDate, String> mapAppHdw = new HashMap<>();
		////※同じ申請日の残業申請が２件あった場合、入力日が後のもの（latest）だけSetする
		for (Application_New app : appOt) {
			if(mapAppOt.containsKey(app.getAppDate())){
				continue;
			}
			mapAppOt.put(app.getAppDate(), app.getAppID());
			lstIdOt.add(app.getAppID());
		}
		//※同じ申請日の休日出勤申請が２件あった場合、入力日が後のもの（latest）だけ集計する
		for (Application_New app : appHdw) {
			if(mapAppHdw.containsKey(app.getAppDate())){
				continue;
			}
			mapAppHdw.put(app.getAppDate(), app.getAppID());
			lstIdHdw.add(app.getAppID());
		}
		//get detail overtime
		mapOt = repoOvertime.getListAppOvertimeFrame(companyId, lstIdOt);
		//get detail holiday work
		mapHd = appHdWorkRepository.getListAppHdWorkFrame(companyId, lstIdHdw);
		//tinh thoi gian over night the Don lam them.
		for(Map.Entry<GeneralDate, String> entry : mapAppOt.entrySet()) {
			AppOverTime otDetail = mapOt.get(entry.getValue());
			//残業申請．就業時間外深夜時間
			int cal = otDetail.getOverTimeShiftNight() == null ? 0 : otDetail.getOverTimeShiftNight();
			results.add(new ApplicationOvertimeExport(entry.getKey(), cal));
		}
		List<ApplicationOvertimeExport> bonus = new ArrayList<>();
		//tinh thoi gian over night bo sung theo Don lam ngay nghi
		for(Map.Entry<GeneralDate, String> entry : mapAppHdw.entrySet()) {
			AppHolidayWork hdDetail = mapHd.get(entry.getValue());
			//休日出勤申請．就業時間外深夜時間
			int cal = hdDetail.getHolidayShiftNight();
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
