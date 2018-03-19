package nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.primitive.WorkLocationCD;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;

/**
 * 振出申請
 * 
 * @author sonnlb
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecruitmentApp extends AggregateRoot {
	/**
	 * 申請ID
	 */
	private String appID;
	/**
	 * 勤務種類
	 */
	private String workTypeCD;
	/**
	 * 勤務場所コード
	 */
	private WorkLocationCD workLocationCD;
	/**
	 * 就業時間帯
	 */
	private WorkTimeCode workTimeCD;

	/**
	 * 勤務時間1
	 */
	private RecruitmentWorkingHour workTime1;
	/**
	 * 勤務時間2
	 */
	private RecruitmentWorkingHour workTime2;

}
