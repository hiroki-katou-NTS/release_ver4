package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkingData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
//import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
//import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.FixedConditionDataRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class CheckingDataDefault implements CheckingDataService {

	@Inject
	private WorkInformationRepository workInfoRepo;

	@Inject
	private FixedConditionDataRepository fixedConditionDataRepository;

	@Override
	public List<ValueExtractAlarmWR> checkingData(String workplaceID, String employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		List<ValueExtractAlarmWR> listValueExtractAlarmWR = new ArrayList<>();
		// 勤務実績のアラームデータを生成する
		String comment = fixedConditionDataRepository.getFixedByNO(5).get().getMessage().v();
		// 社員に一致する日別実績（ドメインモデル「日別実績の勤怠時間」）を取得する
		GeneralDate date = GeneralDate.localDate(startDate.localDate());
		while (date.beforeOrEquals(endDate)) {
			Optional<WorkInfoOfDailyPerformance> data = workInfoRepo.find(employeeID, date);
			// 取得出来なかった
			if (!data.isPresent()) {
				listValueExtractAlarmWR
						.add(new ValueExtractAlarmWR(workplaceID, employeeID, date, TextResource.localize("KAL010_1"),
								TextResource.localize("KAL010_65"), TextResource.localize("KAL010_66"), comment,null));
			}
			date = date.addDays(1);
		}
		return listValueExtractAlarmWR;
	}
}
