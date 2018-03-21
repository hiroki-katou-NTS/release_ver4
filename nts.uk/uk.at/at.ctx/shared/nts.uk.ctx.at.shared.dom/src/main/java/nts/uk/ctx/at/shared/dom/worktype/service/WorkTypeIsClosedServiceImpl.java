package nts.uk.ctx.at.shared.dom.worktype.service;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class WorkTypeIsClosedServiceImpl implements WorkTypeIsClosedService{
	@Inject
	private WorkTypeRepository workTypeRepo;

	@Override
	public boolean checkWorkTypeIsClosed(String workTypeCode) {
		boolean isFlag = false;
		String companyId = AppContexts.user().companyId();
		//ドメインモデル「勤務種類」を取得する
		Optional<WorkType> optWorkTypeData = workTypeRepo.findByPK(companyId, workTypeCode);
		if(!optWorkTypeData.isPresent()) {
			return false;
		}
		WorkType workTypeData = optWorkTypeData.get();
		//「1日の勤務」．1日 in (休日出勤, 振出) ||
		//「1日の勤務」．午前 = 振出 ||
		//「1日の勤務」．午後 = 振出
		if(workTypeData.getDailyWork().getOneDay() == WorkTypeClassification.HolidayWork
				||workTypeData.getDailyWork().getOneDay() ==WorkTypeClassification.Shooting
				||workTypeData.getDailyWork().getMorning() == WorkTypeClassification.Shooting
				||workTypeData.getDailyWork().getAfternoon() == WorkTypeClassification.Shooting) {
			isFlag = true;
		} else {
			isFlag = false;
		}
		return isFlag;
	}
	@Override
	public boolean checkStampAutoSet(String workTypeCode, AttendanceOfficeAtr workTypeAtr) {
		String companyId = AppContexts.user().companyId();
		//ドメインモデル「勤務種類」を取得する
		Optional<WorkType> optWorkTypeData = workTypeRepo.findByPK(companyId, workTypeCode);
		if(!optWorkTypeData.isPresent()) {
			return false;
		}
		WorkType workTypeData = optWorkTypeData.get();
		//INPUT．出勤退勤区分をチェックする
		if(workTypeAtr == AttendanceOfficeAtr.ATTENDANCE) {
			//自動打刻セット区分=「所定勤務の設定」．出勤時刻を直行とする
			List<WorkTypeSet> lstAttendance = workTypeData.getWorkTypeSetList();
			if(lstAttendance.isEmpty()) {
				//TODO : lam tiep khi xac nhan lai voi ben vn
			}
					
			//return 
		} else {
			//自動打刻セット区分=「所定勤務の設定」．退勤時刻を直帰とする
		}
		return false;
	}

}
