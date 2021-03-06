package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.ExecutionTaskSetting;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.OneDayRepeatInterval;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.RepeatDetailSetting;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.TaskEndDate;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.TaskEndTime;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatDetailSettingMonthly;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatDetailSettingWeekly;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatMonthDaysSelect;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatMonthSelect;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatWeekDaysSelect;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.EndDateClassification;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.EndTimeClassification;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.OneDayRepeatClassification;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.RepeatContentItem;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.primitivevalue.EndTime;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.primitivevalue.OneDayRepeatIntervalDetail;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.primitivevalue.StartTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveExecutionTaskSettingCommand {
	private boolean newMode;

	/* ??????ID */
	private String companyId;

	/* ????????? */
	private String execItemCd;

	/* ????????? */
	private GeneralDate startDate;

	/* ???????????? */
	private Integer startTime;

	/* ????????????????????????????????? */
	private Integer endTimeCls;

	/* ???????????? */
	private Integer endTime;

	/* ????????????????????????????????? */
	private Integer oneDayRepCls;

	/* ?????????????????? */
	private Integer oneDayRepInterval;

	/* ?????????????????? */
	private boolean repeatCls;

	/* ?????????????????? */
	private Integer repeatContent;

	/* ?????????????????????????????? */
	private Integer endDateCls;

	/* ????????????????????? */
	private GeneralDate endDate;

	/* ???????????????????????? */
	private boolean enabledSetting;

	/* ?????????????????? */
//	private GeneralDateTime nextExecDateTime;

	/* ??? */
	private boolean monday;

	/* ??? */
	private boolean tuesday;

	/* ??? */
	private boolean wednesday;

	/* ??? */
	private boolean thursday;

	/* ??? */
	private boolean friday;

	/* ??? */
	private boolean saturday;

	/* ??? */
	private boolean sunday;

	/* 1??? */
	private boolean january;

	/* 2??? */
	private boolean february;

	/* 3??? */
	private boolean march;

	/* 4??? */
	private boolean april;

	/* 5??? */
	private boolean may;

	/* 6??? */
	private boolean june;

	/* 7??? */
	private boolean july;

	/* 8??? */
	private boolean august;

	/* 9??? */
	private boolean september;

	/* 10??? */
	private boolean october;

	/* 11??? */
	private boolean november;

	/* 12??? */
	private boolean december;

	private List<Integer> repeatMonthDateList;

	/* ??????????????????ID */
	private String scheduleId;
	
	/* ??????????????????????????????ID*/
	private String endScheduleId;
	
	/* 1????????????????????????????????????ID */
	private String repeatScheduleId;

	public ExecutionTaskSetting toDomain() {
		List<RepeatMonthDaysSelect> days = repeatMonthDateList.stream()
				.map(x -> EnumAdaptor.valueOf(x, RepeatMonthDaysSelect.class)).collect(Collectors.toList());
		RepeatMonthSelect months = new RepeatMonthSelect(january, february, march, april, may, june, july, august,
				september, october, november, december);
		return ExecutionTaskSetting.builder().companyId(companyId)
				.content(EnumAdaptor.valueOf(repeatContent, RepeatContentItem.class))
				.detailSetting(
						new RepeatDetailSetting(
								Optional.ofNullable(new RepeatDetailSettingWeekly(new RepeatWeekDaysSelect(monday, tuesday, wednesday,
										thursday, friday, saturday, sunday))),
								Optional.ofNullable(new RepeatDetailSettingMonthly(days, months))))
				.enabledSetting(enabledSetting)
				.endDate(new TaskEndDate(EnumAdaptor.valueOf(endDateCls, EndDateClassification.class), Optional.ofNullable(endDate)))
				.endScheduleId(Optional.ofNullable(endScheduleId))
				.endTime(new TaskEndTime(EnumAdaptor.valueOf(endTimeCls, EndTimeClassification.class),
						Optional.ofNullable(endTime).map(EndTime::new)))
				.execItemCd(new ExecutionCode(execItemCd)).nextExecDateTime(
						Optional.empty())
				.oneDayRepInr(new OneDayRepeatInterval(
						Optional.ofNullable(oneDayRepInterval).map(data -> EnumAdaptor.valueOf(data, OneDayRepeatIntervalDetail.class)),
						EnumAdaptor.valueOf(oneDayRepCls, OneDayRepeatClassification.class)))
				.repeatScheduleId(Optional.ofNullable(repeatScheduleId))
				.scheduleId(scheduleId)
				.startDate(startDate)
				.startTime(new StartTime(startTime))
				.build();
	}
}
