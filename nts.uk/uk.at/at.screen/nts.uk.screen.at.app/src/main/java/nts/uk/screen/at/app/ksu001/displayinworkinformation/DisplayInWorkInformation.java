/**
 * 
 */
package nts.uk.screen.at.app.ksu001.displayinworkinformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.algorithm.JudgeHdSystemOneDayService;
import nts.uk.screen.at.app.ksu001.scheduleactualworkinfo.GetScheduleActualOfWorkInfo;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author laitv ScreenQuery 勤務情報で表示する
 */
@Stateless
public class DisplayInWorkInformation {

	@Inject
	private BasicScheduleService basicScheduleService;
	@Inject
	private JudgeHdSystemOneDayService judgeHdSystemOneDayService;
	@Inject
	private GetScheduleActualOfWorkInfo getScheduleActualOfWorkInfo;
	
	public DisplayInWorkInfoResult getDataWorkInfo(DisplayInWorkInfoParam param) {

		DisplayInWorkInfoResult result = new DisplayInWorkInfoResult();

		String cid = AppContexts.user().companyId();
		List<WorkTypeInfomation> listWorkTypeInfo = new ArrayList<>();

		// <<Public>> 廃止されていない勤務種類をすべて取得する
		List<WorkType> listWorkType = basicScheduleService.getAllWorkTypeNotAbolished(cid);
		List<WorkTypeDto> listWorkTypeDto = listWorkType.stream().map(mapper -> {
			return new WorkTypeDto(mapper);
		}).collect(Collectors.toList());

		for (int i = 0; i < listWorkTypeDto.size(); i++) {

			// <<Public>> 廃止されていない勤務種類をすべて取得する
			WorkTypeDto workTypeDto = listWorkTypeDto.get(i);
			// 就業時間帯の必須チェック
			SetupType workTimeSetting = basicScheduleService.checkNeededOfWorkTimeSetting(listWorkTypeDto.get(i).getWorkTypeCode());

			// 1日半日出勤・1日休日系の判定 - (Thực hiện thuật toán [Kiểm tra hệ thống đi làm
			// nửa ngày・ nghỉ cả ngày ])
			AttendanceHolidayAttr attHdAtr = judgeHdSystemOneDayService
					.judgeHdOnDayWorkPer(listWorkTypeDto.get(i).getWorkTypeCode());

			WorkTypeInfomation workTypeInfomation = new WorkTypeInfomation(workTypeDto, workTimeSetting.value,
					attHdAtr.value);
			listWorkTypeInfo.add(workTypeInfomation);
		}
		result.setListWorkTypeInfo(listWorkTypeInfo);
		
		// Lấy data Schedule
		// Get schedule/actual results with work information
		List<WorkScheduleWorkInforDto> listWorkScheduleWorkInfor = getScheduleActualOfWorkInfo.getDataScheduleAndAactualOfWorkInfo(param);

		result.setListWorkScheduleWorkInfor(listWorkScheduleWorkInfor);
		return result;
	}
}
