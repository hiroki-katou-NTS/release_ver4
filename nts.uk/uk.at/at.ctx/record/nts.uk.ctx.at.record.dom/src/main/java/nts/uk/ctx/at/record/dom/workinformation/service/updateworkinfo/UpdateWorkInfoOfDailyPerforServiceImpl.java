package nts.uk.ctx.at.record.dom.workinformation.service.updateworkinfo;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.DailyRecordAdUpService;
//import nts.uk.ctx.at.record.dom.workinformation.WorkInfoChangeEvent;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

@Stateless
public class UpdateWorkInfoOfDailyPerforServiceImpl implements UpdateWorkInfoOfDailyPerforService {

	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private WorkInformationRepository workInformationRepository;

	@Inject
	private DailyRecordAdUpService dailyRecordAdUpService;

	@Override
	public void updateWorkInfoOfDailyPerforService(String companyId, String employeeID, GeneralDate processingDate,
			WorkInfoOfDailyPerformance workInfoOfDailyPerformance) {
		Optional<WorkType> workType = this.workTypeRepository.findByPK(companyId,
				workInfoOfDailyPerformance.getRecordInfo().getWorkTypeCode().v());

		if (workType.isPresent()) {
			WorkTypeClassification oneDay = workType.get().getDailyWork().getOneDay();
			if (oneDay == WorkTypeClassification.Holiday || oneDay == WorkTypeClassification.Pause
					|| oneDay == WorkTypeClassification.ContinuousWork
					|| oneDay == WorkTypeClassification.LeaveOfAbsence || oneDay == WorkTypeClassification.Closure) {
				WorkInformation recordWorkInformation = new WorkInformation(null,
						workInfoOfDailyPerformance.getRecordInfo().getWorkTypeCode().v());
				workInfoOfDailyPerformance.setRecordInfo(recordWorkInformation);
			}
			
			// ドメインモデル「日別実績の勤務情報」を更新する(Update domain 「日別実績の勤務情報」)
			//this.workInformationRepository.updateByKeyFlush(workInfoOfDailyPerformance);
			dailyRecordAdUpService.adUpWorkInfo(workInfoOfDailyPerformance);
			
			// domain event 
			workInfoOfDailyPerformance.workInfoChanged();
		}
	}

}
