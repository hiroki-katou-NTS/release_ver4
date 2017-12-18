package nts.uk.ctx.at.shared.app.find.workingcondition;

import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workingcondition.NotUseAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.PersonalDayOfWeek;
import nts.uk.ctx.at.shared.dom.workingcondition.PersonalWorkCategory;
import nts.uk.ctx.at.shared.dom.workingcondition.ScheduleMethod;
import nts.uk.ctx.at.shared.dom.workingcondition.SingleDaySchedule;
import nts.uk.ctx.at.shared.dom.workingcondition.TimeZone;
import nts.uk.ctx.at.shared.dom.workingcondition.TimeZoneScheduledMasterAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleBasicCreMethod;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleMasterReferenceAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Setter
public class WorkingConditionDto extends PeregDomainDto {
	/**
	 * 期間
	 */
	@PeregItem("IS00118")
	private String period;

	/**
	 * 開始日
	 */
	@PeregItem("IS00119")
	private GeneralDate startDate;

	/**
	 * 終了日
	 */
	@PeregItem("IS00120")
	private GeneralDate endDate;

	/**
	 * スケ管理区分 予定管理区分
	 */
	@PeregItem("IS00121")
	private int scheduleManagementAtr;

	/**
	 * 勤務予定作成方法
	 */
	// @PeregItem("IS00122")

	/**
	 * 基本作成方法 予定作成方法.基本作成方法
	 */
	@PeregItem("IS00123")
	private WorkScheduleBasicCreMethod basicCreateMethod;

	/**
	 * 営業日カレンダーのマスタ参照先 予定作成方法.営業日カレンダーによる勤務予定作成.営業日カレンダーの参照先
	 */
	@PeregItem("IS00124")
	private WorkScheduleMasterReferenceAtr referenceBusinessDayCalendar;

	/**
	 * 基本勤務のマスタ参照先 予定作成方法.営業日カレンダーによる勤務予定作成.基本勤務の参照先
	 */
	@PeregItem("IS00125")
	private WorkScheduleMasterReferenceAtr referenceBasicWork;

	/**
	 * 就業時間帯の参照先 予定作成方法.月間パターンによる勤務予定作成.勤務種類と就業時間帯の参照先
	 */
	@PeregItem("IS00126")
	private TimeZoneScheduledMasterAtr referenceType;

	/**
	 * 月間パターンCD
	 */
	// @PeregItem("IS00127")

	/**
	 * 休日勤種CD 区分別勤務.休日時.勤務種類コード
	 */
	@PeregItem("IS00128")
	private String holidayWorkTypeCode;

	/**
	 * 平日時勤務設定
	 */
	// @PeregItem("IS00129")

	/**
	 * 平日勤種CD 区分別勤務.平日時.勤務種類コード
	 *
	 */
	@PeregItem("IS00130")
	private String weekdayWorkTypeCode;

	/**
	 * 平日就時CD 区分別勤務.平日時.就業時間帯コード
	 */
	@PeregItem("IS00131")
	private String weekdayWorkTimeCode;

	/**
	 * 平日時勤務時間1
	 * 
	 */
	// @PeregItem("IS00132")

	/**
	 * 平日開始1 区分別勤務.平日時.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00133")
	private int weekDayStartTime1;

	/**
	 * 平日終了1 区分別勤務.平日時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00134")
	private int weekDayEndTime1;

	/**
	 * 平日時勤務時間2
	 */
	// @PeregItem("IS00135")

	/**
	 * 平日開始2 区分別勤務.平日時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00136")
	private int weekDayStartTime2;

	/**
	 * 平日終了2 区分別勤務.平日時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00137")
	private int weekDayEndTime2;

	/**
	 * 休出時勤務設定
	 */
	// @PeregItem("IS00138")

	/**
	 * 休出勤種CD 区分別勤務.休日出勤時.勤務種類コード
	 */
	@PeregItem("IS00139")
	private String workInHolidayWorkTypeCode;

	/**
	 * 休出就時CD 区分別勤務.休日出勤時.就業時間帯コード
	 */
	@PeregItem("IS00140")
	private String workInHolidayWorkTimeCode;

	/**
	 * 休出時勤務時間1
	 */
	// @PeregItem("IS00141")

	/**
	 * 休出開始1 区分別勤務.休日出勤時.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00142")
	private int workInHolidayStartTime1;

	/**
	 * 休出終了1 区分別勤務.休日出勤時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00143")
	private int workInHolidayEndTime1;

	/**
	 * 休出時勤務時間2
	 */
	// @PeregItem("IS00144")

	/**
	 * 休出開始2 区分別勤務.休日出勤時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00145")
	private int workInHolidayStartTime2;

	/**
	 * 休出終了2 区分別勤務.休日出勤時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00146")
	private int workInHolidayEndTime2;

	/**
	 * 公休出勤時勤務設定
	 */
	// @PeregItem("IS00147")

	/**
	 * 公休出勤種CD 区分別勤務.公休出勤時.勤務種類コード
	 */
	@PeregItem("IS00148")
	private String workInPublicHolidayWorkTypeCode;

	/**
	 * 公休出就時CD 区分別勤務.公休出勤時.就業時間帯コード
	 */
	@PeregItem("IS00149")
	private String workInPublicHolidayWorkTimeCode;

	/**
	 * 公休時勤務時間1
	 */
	// @PeregItem("IS00150")

	/**
	 * 公休出開始1 区分別勤務.公休出勤時.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00151")
	private int workInPublicHolidayStartTime1;

	/**
	 * 公休出終了1 区分別勤務.公休出勤時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00152")
	private int workInPublicHolidayEndTime1;

	/**
	 * 公休時勤務時間2
	 */
	// @PeregItem("IS00153")

	/**
	 * 公休出開始2 区分別勤務.公休出勤時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00154")
	private int workInPublicHolidayStartTime2;

	/**
	 * 公休出終了2 区分別勤務.公休出勤時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00155")
	private int workInPublicHolidayEndTime2;

	/**
	 * 法定休出時勤務設定
	 */
	// @PeregItem("IS00156")

	/**
	 * 法内休出勤種CD 区分別勤務.法内休出時.勤務種類コード
	 */
	@PeregItem("IS00157")
	private String inLawBreakTimeWorkTypeCode;

	/**
	 * 法内休出就時CD 区分別勤務.法内休出時.就業時間帯コード
	 */
	@PeregItem("IS00158")
	private String inLawBreakTimeWorkTimeCode;

	/**
	 * 法内休出時勤務時間1
	 */
	// @PeregItem("IS00159")

	/**
	 * 法内休出開始1 区分別勤務.法内休出時.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00160")
	private int inLawBreakTimeStartTime1;

	/**
	 * 法内休出終了1 区分別勤務.法内休出時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00161")
	private int inLawBreakTimeEndTime1;

	/**
	 * 法内休出時勤務時間2
	 */
	// @PeregItem("IS00162")

	/**
	 * 法内休出開始2 区分別勤務.法内休出時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00163")
	private int inLawBreakTimeStartTime2;

	/**
	 * 法内休出終了2 区分別勤務.法内休出時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00164")
	private int inLawBreakTimeEndTime2;

	/**
	 * 法定外休出時勤務設定
	 */
	// @PeregItem("IS00165")

	/**
	 * 法外休出勤種CD 区分別勤務.法外休出時.勤務種類コード
	 */
	@PeregItem("IS00166")
	private String outsideLawBreakTimeWorkTypeCode;

	/**
	 * 法外休出就時CD 区分別勤務.法外休出時.就業時間帯コード
	 */
	@PeregItem("IS00167")
	private String outsideLawBreakTimeWorkTimeCode;

	/**
	 * 法外休出時勤務時間1
	 */
	// @PeregItem("IS00168")

	/**
	 * 法外休出開始1 区分別勤務.法外休出時.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00169")
	private int outsideLawBreakTimeStartTime1;

	/**
	 * 法外休出終了1 区分別勤務.法外休出時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00170")
	private int outsideLawBreakTimeEndTime1;

	/**
	 * 法外休出時勤務時間2
	 */
	// @PeregItem("IS00171")

	/**
	 * 法外休出開始2 区分別勤務.法外休出時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00172")
	private int outsideLawBreakTimeStartTime2;

	/**
	 * 法外休出終了2 区分別勤務.法外休出時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00173")
	private int outsideLawBreakTimeEndTime2;

	/**
	 * 祝日時勤務設定
	 */
	// @PeregItem("IS00174")

	/**
	 * 法外祝日勤種CD 区分別勤務.祝日出勤時.勤務種類コード
	 */
	@PeregItem("IS00175")
	private String holidayAttendanceTimeWorkTypeCode;

	/**
	 * 法外祝日就時CD 区分別勤務.祝日出勤時.就業時間帯コード
	 */
	@PeregItem("IS00176")
	private String holidayAttendanceTimeWorkTimeCode;

	/**
	 * 法外祝日時勤務時間1
	 */
	// @PeregItem("IS00177")

	/**
	 * 法外祝日開始1 区分別勤務.祝日出勤時.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00178")
	private int holidayAttendanceTimeStartTime1;

	/**
	 * 法外祝日終了1 区分別勤務.祝日出勤時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00179")
	private int holidayAttendanceTimeEndTime1;

	/**
	 * 法外祝日時勤務時間2
	 */
	// @PeregItem("IS00180")

	/**
	 * 法外祝日開始2 区分別勤務.祝日出勤時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00181")
	private int holidayAttendanceTimeStartTime2;

	/**
	 * 法外祝日終了2 区分別勤務.祝日出勤時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00182")
	private int holidayAttendanceTimeEndTime2;

	/**
	 * 日曜勤務設定
	 */
	// @PeregItem("IS00183")

	/**
	 * 日勤種CD 曜日別勤務.日曜日.勤務種類コード
	 */
	@PeregItem("IS00184")
	private String sundayWorkTypeCode;

	/**
	 * 日就時CD 曜日別勤務.日曜日.就業時間帯コード
	 */
	@PeregItem("IS00185")
	private String sundayWorkTimeCode;

	/**
	 * 日曜出勤時勤務時間1
	 */
	// @PeregItem("IS00186")

	/**
	 * 日開始1 曜日別勤務.日曜日.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00187")
	private int sundayStartTime1;

	/**
	 * 日終了1 曜日別勤務.日曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00188")
	private int sundayEndTime1;

	/**
	 * 日曜出勤時勤務時間2
	 */
	// @PeregItem("IS00189")

	/**
	 * 日開始2 曜日別勤務.日曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00190")
	private int sundayStartTime2;

	/**
	 * 日終了2 曜日別勤務.日曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00191")
	private int sundayEndTime2;

	/**
	 * 月曜勤務設定
	 */
	// @PeregItem("IS00192")

	/**
	 * 月勤種CD 曜日別勤務.月曜日.勤務種類コード
	 */
	@PeregItem("IS00193")
	private String mondayWorkTypeCode;

	/**
	 * 月就時CD 曜日別勤務.月曜日.就業時間帯コード
	 */
	@PeregItem("IS00194")
	private String mondayWorkTimeCode;

	/**
	 * 月曜出勤時勤務時間1
	 */
	// @PeregItem("IS00195")

	/**
	 * 月開始1 曜日別勤務.月曜日.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00196")
	private int mondayStartTime1;

	/**
	 * 月終了1 曜日別勤務.月曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00197")
	private int mondayEndTime1;

	/**
	 * 月曜出勤時勤務時間2
	 */
	// @PeregItem("IS00198")

	/**
	 * 月開始2 曜日別勤務.月曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00199")
	private int mondayStartTime2;

	/**
	 * 月終了2 曜日別勤務.月曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00200")
	private int mondayEndTime2;

	/**
	 * 火曜勤務設定
	 */
	// @PeregItem("IS00201")

	/**
	 * 火勤種CD 曜日別勤務.火曜日.勤務種類コード
	 */
	@PeregItem("IS00202")
	private String tuesdayWorkTypeCode;

	/**
	 * 火就時CD 曜日別勤務.火曜日.就業時間帯コード
	 */
	@PeregItem("IS00203")
	private String tuesdayWorkTimeCode;

	/**
	 * 火曜出勤時勤務時間1
	 */
	// @PeregItem("IS00204")

	/**
	 * 火開始1 曜日別勤務.火曜日.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00205")
	private int tuesdayStartTime1;

	/**
	 * 火終了1 曜日別勤務.火曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00206")
	private int tuesdayEndTime1;

	/**
	 * 火曜出勤時勤務時間2
	 */
	// @PeregItem("IS00207")

	/**
	 * 火開始2 曜日別勤務.火曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00208")
	private int tuesdayStartTime2;

	/**
	 * 火終了2 曜日別勤務.火曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00209")
	private int tuesdayEndTime2;

	/**
	 * 水曜勤務設定
	 */
	// @PeregItem("IS00210")

	/**
	 * 水勤種CD 曜日別勤務.水曜日.勤務種類コード
	 */
	@PeregItem("IS00211")
	private String wednesdayWorkTypeCode;

	/**
	 * 水就時CD 曜日別勤務.水曜日.就業時間帯コード
	 */
	@PeregItem("IS00212")
	private String wednesdayWorkTimeCode;

	/**
	 * 水曜出勤時勤務時間1
	 */
	// @PeregItem("IS00213")

	/**
	 * 水開始1 曜日別勤務.水曜日.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00214")
	private int wednesdayStartTime1;

	/**
	 * 水終了1 曜日別勤務.水曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00215")
	private int wednesdayEndTime1;

	/**
	 * 水曜出勤時勤務時間2
	 */
	// @PeregItem("IS00216")

	/**
	 * 水開始2 曜日別勤務.水曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00217")
	private int wednesdayStartTime2;

	/**
	 * 水終了2 曜日別勤務.水曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00218")
	private int wednesdayEndTime2;

	/**
	 * 木曜勤務設定
	 */
	// @PeregItem("IS00219")

	/**
	 * 木勤種CD 曜日別勤務.木曜日.勤務種類コード
	 */
	@PeregItem("IS00220")
	private String thursdayWorkTypeCode;

	/**
	 * 木就時CD 曜日別勤務.木曜日.就業時間帯コード
	 */
	@PeregItem("IS00221")
	private String thursdayWorkTimeCode;

	/**
	 * 木曜出勤時勤務時間1
	 */
	// @PeregItem("IS00222")

	/**
	 * 木開始1 曜日別勤務.木曜日.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00223")
	private int thursdayStartTime1;

	/**
	 * 木終了1 曜日別勤務.木曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00224")
	private int thursdayEndTime1;

	/**
	 * 木曜出勤時勤務時間2
	 */
	// @PeregItem("IS00225")

	/**
	 * 木開始2 曜日別勤務.木曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00226")
	private int thursdayStartTime2;

	/**
	 * 木終了2 曜日別勤務.木曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00227")
	private int thursdayEndTime2;

	/**
	 * 金曜勤務設定
	 */
	// @PeregItem("IS00228")

	/**
	 * 金勤種CD 曜日別勤務.金曜日.勤務種類コード
	 */
	@PeregItem("IS00229")
	private String fridayWorkTypeCode;

	/**
	 * 金就時CD 曜日別勤務.金曜日.就業時間帯コード
	 */
	@PeregItem("IS00230")
	private String fridayWorkTimeCode;

	/**
	 * 金曜出勤時勤務時間1
	 *
	 */
	// @PeregItem("IS00231")

	/**
	 * 金開始1 曜日別勤務.金曜日.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00232")
	private int fridayStartTime1;

	/**
	 * 金終了1 曜日別勤務.金曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00233")
	private int fridayEndTime1;

	/**
	 * 金曜出勤時勤務時間2
	 */
	// @PeregItem("IS00234")

	/**
	 * 金開始2 曜日別勤務.金曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00235")
	private int fridayStartTime2;

	/**
	 * 金終了2 曜日別勤務.金曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00236")
	private int fridayEndTime2;

	/**
	 * 土曜勤務設定
	 */
	// @PeregItem("IS00237")

	/**
	 * 土勤種CD 曜日別勤務.土曜日.勤務種類コード
	 */
	@PeregItem("IS00238")
	private String saturdayWorkTypeCode;

	/**
	 * 土就時CD 曜日別勤務.土曜日.就業時間帯コード
	 */
	@PeregItem("IS00239")
	private String saturdayWorkTimeCode;

	/**
	 * 土曜出勤時勤務時間1
	 */
	// @PeregItem("IS00240")

	/**
	 * 土開始1 曜日別勤務.土曜日.勤務時間帯.開始 ※回数=1
	 */
	@PeregItem("IS00241")
	private int saturdayStartTime1;

	/**
	 * 土終了1 曜日別勤務.土曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00242")
	private int saturdayEndTime1;

	/**
	 * 土曜出勤時勤務時間2
	 */
	// @PeregItem("IS00243")

	/**
	 * 土開始2 曜日別勤務.土曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00244")
	private int saturdayStartTime2;

	/**
	 * 土終了2 曜日別勤務.土曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00245")
	private int saturdayEndTime2;

	/**
	 * 加給CD
	 */
	// @PeregItem("IS00246")

	/**
	 * 年休就時設定 就業時間帯の自動設定区分
	 */
	@PeregItem("IS00247")
	private NotUseAtr autoIntervalSetAtr;

	/**
	 * 加算時間利用区分 休暇加算時間利用区分
	 */
	@PeregItem("IS00248")
	private NotUseAtr vacationAddedTimeAtr;

	/**
	 * 加算時間１日 休暇加算時間設定.１日
	 */
	@PeregItem("IS00249")
	private int oneDay;

	/**
	 * 加算時間ＡＭ 休暇加算時間設定.午前
	 */
	@PeregItem("IS00250")
	private int morning;

	/**
	 * 加算時間ＰＭ 休暇加算時間設定.午後
	 */
	@PeregItem("IS00251")
	private int afternoon;

	/**
	 * 就業区分 労働制
	 */
	@PeregItem("IS00252")
	private WorkingSystem laborSystem;

	/**
	 * 契約時間 契約時間
	 */
	@PeregItem("IS00253")
	private int contractTime;

	public WorkingConditionDto(String recordId) {
		super(recordId);
	}

	public static WorkingConditionDto createWorkingConditionDto(DateHistoryItem dateHistoryItem,
			WorkingConditionItem workingConditionItem) {
		WorkingConditionDto dto = new WorkingConditionDto(dateHistoryItem.identifier());
		
		dto.setStartDate(dateHistoryItem.start());
		dto.setEndDate(dateHistoryItem.end());

		dto.setScheduleManagementAtr(workingConditionItem.getScheduleManagementAtr().value);

		// 予定作成方法
		setScheduleMethod(dto, workingConditionItem.getScheduleMethod());

		PersonalWorkCategory workCategory = workingConditionItem.getWorkCategory();

		// 休日出勤時
		setHolidayTime(dto, workCategory.getHolidayTime());

		// 平日時
		setWeekDay(dto, workCategory.getWeekdayTime());

		// 休日出勤時
		setWorkInHoliday(dto, workCategory.getHolidayWork());

		// 公休出勤時
		if (workCategory.getPublicHolidayWork().isPresent()) {
			setWorkInPublicHoliday(dto, workCategory.getPublicHolidayWork().get());
		}

		// 法内休出時
		if (workCategory.getInLawBreakTime().isPresent()) {
			setInLawBreakTime(dto, workCategory.getInLawBreakTime().get());
		}

		// 法外休出時
		if (workCategory.getOutsideLawBreakTime().isPresent()) {
			setOutsideLawBreakTime(dto, workCategory.getOutsideLawBreakTime().get());
		}

		// 祝日出勤時
		if (workCategory.getHolidayAttendanceTime().isPresent()) {
			setHolidayAttendanceTime(dto, workCategory.getHolidayAttendanceTime().get());
		}

		PersonalDayOfWeek workDayOfWeek = workingConditionItem.getWorkDayOfWeek();

		// 日曜日
		if (workDayOfWeek.getSunday().isPresent()) {
			setSunday(dto, workDayOfWeek.getSunday().get());
		}

		// 月曜日
		if (workDayOfWeek.getMonday().isPresent()) {
			setMonday(dto, workDayOfWeek.getMonday().get());
		}

		// 火曜日
		if (workDayOfWeek.getTuesday().isPresent()) {
			setTuesday(dto, workDayOfWeek.getTuesday().get());
		}

		// 水曜日
		if (workDayOfWeek.getWednesday().isPresent()) {
			setWednesday(dto, workDayOfWeek.getWednesday().get());
		}

		// 木曜日
		if (workDayOfWeek.getThursday().isPresent()) {
			setThursday(dto, workDayOfWeek.getThursday().get());
		}

		// 金曜日
		if (workDayOfWeek.getFriday().isPresent()) {
			setFriday(dto, workDayOfWeek.getFriday().get());
		}

		// 土曜日
		if (workDayOfWeek.getSaturday().isPresent()) {
			setSaturday(dto, workDayOfWeek.getSaturday().get());
		}

		dto.setAutoIntervalSetAtr(workingConditionItem.getAutoIntervalSetAtr());
		dto.setVacationAddedTimeAtr(workingConditionItem.getVacationAddedTimeAtr());
		dto.setOneDay(workingConditionItem.getHolidayAddTimeSet().getOneDay().v());
		dto.setMorning(workingConditionItem.getHolidayAddTimeSet().getMorning().v());
		dto.setAfternoon(workingConditionItem.getHolidayAddTimeSet().getAfternoon().v());
		dto.setLaborSystem(workingConditionItem.getLaborSystem());
		dto.setContractTime(workingConditionItem.getContractTime().v());

		return dto;
	}

	private static void setScheduleMethod(WorkingConditionDto dto, ScheduleMethod scheduleMethod) {
		dto.setBasicCreateMethod(scheduleMethod.getBasicCreateMethod());
		dto.setReferenceBusinessDayCalendar(scheduleMethod.getWorkScheduleBusCal().getReferenceBusinessDayCalendar());
		dto.setReferenceBasicWork(scheduleMethod.getWorkScheduleBusCal().getReferenceBasicWork());
		dto.setReferenceType(scheduleMethod.getMonthlyPatternWorkScheduleCre().getReferenceType());
	}

	private static void setHolidayTime(WorkingConditionDto dto, SingleDaySchedule holidayTime) {
		dto.setHolidayWorkTypeCode(holidayTime.getWorkTypeCode().v());
	}

	private static void setWeekDay(WorkingConditionDto dto, SingleDaySchedule weekDay) {
		dto.setWeekdayWorkTypeCode(weekDay.getWorkTypeCode().v());
		dto.setWeekdayWorkTimeCode(weekDay.getWorkTimeCode().get().v());
		TimeZone timeZone1 = weekDay.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1).findFirst()
				.get();
		TimeZone timeZone2 = weekDay.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2).findFirst()
				.get();
		dto.setWeekDayStartTime1(timeZone1.getStart().v());
		dto.setWeekDayEndTime1(timeZone1.getEnd().v());
		dto.setWeekDayStartTime2(timeZone2.getStart().v());
		dto.setWeekDayEndTime2(timeZone2.getEnd().v());
	}

	private static void setWorkInHoliday(WorkingConditionDto dto, SingleDaySchedule workInHoliday) {
		dto.setWorkInHolidayWorkTypeCode(workInHoliday.getWorkTypeCode().v());
		dto.setWorkInHolidayWorkTimeCode(workInHoliday.getWorkTimeCode().get().v());
		TimeZone timeZone1 = workInHoliday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1)
				.findFirst().get();
		TimeZone timeZone2 = workInHoliday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2)
				.findFirst().get();
		dto.setWorkInHolidayStartTime1(timeZone1.getStart().v());
		dto.setWorkInHolidayEndTime1(timeZone1.getEnd().v());
		dto.setWorkInHolidayStartTime2(timeZone2.getStart().v());
		dto.setWorkInHolidayEndTime2(timeZone2.getEnd().v());
	}

	private static void setWorkInPublicHoliday(WorkingConditionDto dto, SingleDaySchedule workInPublicHoliday) {
		dto.setWorkInPublicHolidayWorkTypeCode(workInPublicHoliday.getWorkTypeCode().v());
		dto.setWorkInPublicHolidayWorkTimeCode(workInPublicHoliday.getWorkTimeCode().get().v());
		TimeZone timeZone1 = workInPublicHoliday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1)
				.findFirst().get();
		TimeZone timeZone2 = workInPublicHoliday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2)
				.findFirst().get();
		dto.setWorkInPublicHolidayStartTime1(timeZone1.getStart().v());
		dto.setWorkInPublicHolidayEndTime1(timeZone1.getEnd().v());
		dto.setWorkInPublicHolidayStartTime2(timeZone2.getStart().v());
		dto.setWorkInPublicHolidayEndTime2(timeZone2.getEnd().v());
	}

	private static void setInLawBreakTime(WorkingConditionDto dto, SingleDaySchedule inLawBreakTime) {
		dto.setInLawBreakTimeWorkTypeCode(inLawBreakTime.getWorkTypeCode().v());
		dto.setInLawBreakTimeWorkTimeCode(inLawBreakTime.getWorkTimeCode().get().v());
		TimeZone timeZone1 = inLawBreakTime.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1)
				.findFirst().get();
		TimeZone timeZone2 = inLawBreakTime.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2)
				.findFirst().get();
		dto.setInLawBreakTimeStartTime1(timeZone1.getStart().v());
		dto.setInLawBreakTimeEndTime1(timeZone1.getStart().v());
		dto.setInLawBreakTimeStartTime2(timeZone2.getStart().v());
		dto.setInLawBreakTimeEndTime2(timeZone2.getEnd().v());
	}

	private static void setOutsideLawBreakTime(WorkingConditionDto dto, SingleDaySchedule outsideLawBreakTime) {
		dto.setOutsideLawBreakTimeWorkTypeCode(outsideLawBreakTime.getWorkTypeCode().v());
		dto.setOutsideLawBreakTimeWorkTimeCode(outsideLawBreakTime.getWorkTimeCode().get().v());
		TimeZone timeZone1 = outsideLawBreakTime.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1)
				.findFirst().get();
		TimeZone timeZone2 = outsideLawBreakTime.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2)
				.findFirst().get();
		dto.setOutsideLawBreakTimeStartTime1(timeZone1.getStart().v());
		dto.setOutsideLawBreakTimeEndTime1(timeZone1.getEnd().v());
		dto.setOutsideLawBreakTimeStartTime2(timeZone2.getStart().v());
		dto.setOutsideLawBreakTimeEndTime2(timeZone2.getEnd().v());
	}

	private static void setHolidayAttendanceTime(WorkingConditionDto dto, SingleDaySchedule holidayAttendanceTime) {
		dto.setHolidayAttendanceTimeWorkTypeCode(holidayAttendanceTime.getWorkTypeCode().v());
		dto.setHolidayAttendanceTimeWorkTimeCode(holidayAttendanceTime.getWorkTimeCode().get().v());
		TimeZone timeZone1 = holidayAttendanceTime.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1)
				.findFirst().get();
		TimeZone timeZone2 = holidayAttendanceTime.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2)
				.findFirst().get();
		dto.setHolidayAttendanceTimeStartTime1(timeZone1.getStart().v());
		dto.setHolidayAttendanceTimeEndTime1(timeZone1.getEnd().v());
		dto.setHolidayAttendanceTimeStartTime2(timeZone2.getStart().v());
		dto.setHolidayAttendanceTimeEndTime2(timeZone2.getEnd().v());
	}

	private static void setSunday(WorkingConditionDto dto, SingleDaySchedule sunday) {
		dto.setSundayWorkTypeCode(sunday.getWorkTypeCode().v());
		dto.setSundayWorkTimeCode(sunday.getWorkTimeCode().get().v());
		TimeZone timeZone1 = sunday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1).findFirst()
				.get();
		TimeZone timeZone2 = sunday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2).findFirst()
				.get();
		dto.setSundayStartTime1(timeZone1.getStart().v());
		dto.setSundayEndTime1(timeZone1.getEnd().v());
		dto.setSundayStartTime2(timeZone2.getStart().v());
		dto.setSundayEndTime2(timeZone2.getEnd().v());
	}

	private static void setMonday(WorkingConditionDto dto, SingleDaySchedule monday) {
		dto.setMondayWorkTypeCode(monday.getWorkTypeCode().v());
		dto.setMondayWorkTimeCode(monday.getWorkTimeCode().get().v());
		TimeZone timeZone1 = monday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1).findFirst()
				.get();
		TimeZone timeZone2 = monday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2).findFirst()
				.get();
		dto.setMondayStartTime1(timeZone1.getStart().v());
		dto.setMondayEndTime1(timeZone1.getEnd().v());
		dto.setMondayStartTime2(timeZone2.getStart().v());
		dto.setMondayEndTime2(timeZone2.getEnd().v());
	}

	private static void setTuesday(WorkingConditionDto dto, SingleDaySchedule tuesday) {
		dto.setTuesdayWorkTypeCode(tuesday.getWorkTypeCode().v());
		dto.setTuesdayWorkTimeCode(tuesday.getWorkTimeCode().get().v());
		TimeZone timeZone1 = tuesday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1).findFirst()
				.get();
		TimeZone timeZone2 = tuesday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2).findFirst()
				.get();
		dto.setTuesdayStartTime1(timeZone1.getStart().v());
		dto.setTuesdayEndTime1(timeZone1.getEnd().v());
		dto.setTuesdayStartTime2(timeZone2.getStart().v());
		dto.setTuesdayEndTime2(timeZone2.getEnd().v());
	}

	private static void setWednesday(WorkingConditionDto dto, SingleDaySchedule wednesday) {
		dto.setWednesdayWorkTypeCode(wednesday.getWorkTypeCode().v());
		dto.setWednesdayWorkTimeCode(wednesday.getWorkTimeCode().get().v());
		TimeZone timeZone1 = wednesday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1).findFirst()
				.get();
		TimeZone timeZone2 = wednesday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2).findFirst()
				.get();
		dto.setWednesdayStartTime1(timeZone1.getStart().v());
		dto.setWednesdayEndTime1(timeZone1.getEnd().v());
		dto.setWednesdayStartTime2(timeZone2.getStart().v());
		dto.setWednesdayEndTime2(timeZone2.getEnd().v());
	}

	private static void setThursday(WorkingConditionDto dto, SingleDaySchedule thursday) {
		dto.setThursdayWorkTypeCode(thursday.getWorkTypeCode().v());
		dto.setThursdayWorkTimeCode(thursday.getWorkTimeCode().get().v());
		TimeZone timeZone1 = thursday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1).findFirst()
				.get();
		TimeZone timeZone2 = thursday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2).findFirst()
				.get();
		dto.setThursdayStartTime1(timeZone1.getStart().v());
		dto.setThursdayEndTime1(timeZone1.getEnd().v());
		dto.setThursdayStartTime2(timeZone2.getStart().v());
		dto.setThursdayEndTime2(timeZone2.getEnd().v());
	}

	private static void setFriday(WorkingConditionDto dto, SingleDaySchedule friday) {
		dto.setFridayWorkTypeCode(friday.getWorkTypeCode().v());
		dto.setFridayWorkTimeCode(friday.getWorkTimeCode().get().v());
		TimeZone timeZone1 = friday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1).findFirst()
				.get();
		TimeZone timeZone2 = friday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2).findFirst()
				.get();
		dto.setFridayStartTime1(timeZone1.getStart().v());
		dto.setFridayEndTime1(timeZone1.getEnd().v());
		dto.setFridayStartTime2(timeZone2.getStart().v());
		dto.setFridayEndTime2(timeZone2.getEnd().v());
	}

	private static void setSaturday(WorkingConditionDto dto, SingleDaySchedule saturday) {
		dto.setSaturdayWorkTypeCode(saturday.getWorkTypeCode().v());
		dto.setSaturdayWorkTimeCode(saturday.getWorkTimeCode().get().v());
		TimeZone timeZone1 = saturday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 1).findFirst()
				.get();
		TimeZone timeZone2 = saturday.getWorkingHours().stream().filter(timeZone -> timeZone.getCnt() == 2).findFirst()
				.get();
		dto.setSaturdayStartTime1(timeZone1.getStart().v());
		dto.setSaturdayEndTime1(timeZone1.getEnd().v());
		dto.setSaturdayStartTime2(timeZone2.getStart().v());
		dto.setSaturdayEndTime2(timeZone2.getEnd().v());
	}

}
