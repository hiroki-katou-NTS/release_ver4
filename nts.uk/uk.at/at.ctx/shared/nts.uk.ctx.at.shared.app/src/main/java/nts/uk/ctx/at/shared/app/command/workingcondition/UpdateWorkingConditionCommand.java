package nts.uk.ctx.at.shared.app.command.workingcondition;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.PeregRecordId;

@Getter
public class UpdateWorkingConditionCommand{
	
	@PeregRecordId
	private String histId;
	
	@PeregEmployeeId
	private String employeeId;
	
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
	private BigDecimal scheduleManagementAtr;

	/**
	 * 勤務予定作成方法
	 */
	// @PeregItem("IS00122")

	/**
	 * 基本作成方法 予定作成方法.基本作成方法
	 */
	@PeregItem("IS00123")
	private BigDecimal basicCreateMethod;

	/**
	 * 営業日カレンダーのマスタ参照先 予定作成方法.営業日カレンダーによる勤務予定作成.営業日カレンダーの参照先
	 */
	@PeregItem("IS00124")
	private BigDecimal referenceBusinessDayCalendar;

	/**
	 * 基本勤務のマスタ参照先 予定作成方法.営業日カレンダーによる勤務予定作成.基本勤務の参照先
	 */
	@PeregItem("IS00125")
	private BigDecimal referenceBasicWork;

	/**
	 * 就業時間帯の参照先 予定作成方法.月間パターンによる勤務予定作成.勤務種類と就業時間帯の参照先
	 */
	@PeregItem("IS00126")
	private BigDecimal referenceType;

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
	private BigDecimal weekDayStartTime1;

	/**
	 * 平日終了1 区分別勤務.平日時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00134")
	private BigDecimal weekDayEndTime1;

	/**
	 * 平日時勤務時間2
	 */
	// @PeregItem("IS00135")

	/**
	 * 平日開始2 区分別勤務.平日時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00136")
	private BigDecimal weekDayStartTime2;

	/**
	 * 平日終了2 区分別勤務.平日時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00137")
	private BigDecimal weekDayEndTime2;

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
	private BigDecimal workInHolidayStartTime1;

	/**
	 * 休出終了1 区分別勤務.休日出勤時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00143")
	private BigDecimal workInHolidayEndTime1;

	/**
	 * 休出時勤務時間2
	 */
	// @PeregItem("IS00144")

	/**
	 * 休出開始2 区分別勤務.休日出勤時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00145")
	private BigDecimal workInHolidayStartTime2;

	/**
	 * 休出終了2 区分別勤務.休日出勤時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00146")
	private BigDecimal workInHolidayEndTime2;

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
	private BigDecimal workInPublicHolidayStartTime1;

	/**
	 * 公休出終了1 区分別勤務.公休出勤時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00152")
	private BigDecimal workInPublicHolidayEndTime1;

	/**
	 * 公休時勤務時間2
	 */
	// @PeregItem("IS00153")

	/**
	 * 公休出開始2 区分別勤務.公休出勤時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00154")
	private BigDecimal workInPublicHolidayStartTime2;

	/**
	 * 公休出終了2 区分別勤務.公休出勤時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00155")
	private BigDecimal workInPublicHolidayEndTime2;

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
	private BigDecimal inLawBreakTimeStartTime1;

	/**
	 * 法内休出終了1 区分別勤務.法内休出時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00161")
	private BigDecimal inLawBreakTimeEndTime1;

	/**
	 * 法内休出時勤務時間2
	 */
	// @PeregItem("IS00162")

	/**
	 * 法内休出開始2 区分別勤務.法内休出時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00163")
	private BigDecimal inLawBreakTimeStartTime2;

	/**
	 * 法内休出終了2 区分別勤務.法内休出時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00164")
	private BigDecimal inLawBreakTimeEndTime2;

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
	private BigDecimal outsideLawBreakTimeStartTime1;

	/**
	 * 法外休出終了1 区分別勤務.法外休出時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00170")
	private BigDecimal outsideLawBreakTimeEndTime1;

	/**
	 * 法外休出時勤務時間2
	 */
	// @PeregItem("IS00171")

	/**
	 * 法外休出開始2 区分別勤務.法外休出時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00172")
	private BigDecimal outsideLawBreakTimeStartTime2;

	/**
	 * 法外休出終了2 区分別勤務.法外休出時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00173")
	private BigDecimal outsideLawBreakTimeEndTime2;

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
	private BigDecimal holidayAttendanceTimeStartTime1;

	/**
	 * 法外祝日終了1 区分別勤務.祝日出勤時.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00179")
	private BigDecimal holidayAttendanceTimeEndTime1;

	/**
	 * 法外祝日時勤務時間2
	 */
	// @PeregItem("IS00180")

	/**
	 * 法外祝日開始2 区分別勤務.祝日出勤時.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00181")
	private BigDecimal holidayAttendanceTimeStartTime2;

	/**
	 * 法外祝日終了2 区分別勤務.祝日出勤時.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00182")
	private BigDecimal holidayAttendanceTimeEndTime2;

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
	private BigDecimal sundayStartTime1;

	/**
	 * 日終了1 曜日別勤務.日曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00188")
	private BigDecimal sundayEndTime1;

	/**
	 * 日曜出勤時勤務時間2
	 */
	// @PeregItem("IS00189")

	/**
	 * 日開始2 曜日別勤務.日曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00190")
	private BigDecimal sundayStartTime2;

	/**
	 * 日終了2 曜日別勤務.日曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00191")
	private BigDecimal sundayEndTime2;

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
	private BigDecimal mondayStartTime1;

	/**
	 * 月終了1 曜日別勤務.月曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00197")
	private BigDecimal mondayEndTime1;

	/**
	 * 月曜出勤時勤務時間2
	 */
	// @PeregItem("IS00198")

	/**
	 * 月開始2 曜日別勤務.月曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00199")
	private BigDecimal mondayStartTime2;

	/**
	 * 月終了2 曜日別勤務.月曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00200")
	private BigDecimal mondayEndTime2;

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
	private BigDecimal tuesdayStartTime1;

	/**
	 * 火終了1 曜日別勤務.火曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00206")
	private BigDecimal tuesdayEndTime1;

	/**
	 * 火曜出勤時勤務時間2
	 */
	// @PeregItem("IS00207")

	/**
	 * 火開始2 曜日別勤務.火曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00208")
	private BigDecimal tuesdayStartTime2;

	/**
	 * 火終了2 曜日別勤務.火曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00209")
	private BigDecimal tuesdayEndTime2;

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
	private BigDecimal wednesdayStartTime1;

	/**
	 * 水終了1 曜日別勤務.水曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00215")
	private BigDecimal wednesdayEndTime1;

	/**
	 * 水曜出勤時勤務時間2
	 */
	// @PeregItem("IS00216")

	/**
	 * 水開始2 曜日別勤務.水曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00217")
	private BigDecimal wednesdayStartTime2;

	/**
	 * 水終了2 曜日別勤務.水曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00218")
	private BigDecimal wednesdayEndTime2;

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
	private BigDecimal thursdayStartTime1;

	/**
	 * 木終了1 曜日別勤務.木曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00224")
	private BigDecimal thursdayEndTime1;

	/**
	 * 木曜出勤時勤務時間2
	 */
	// @PeregItem("IS00225")

	/**
	 * 木開始2 曜日別勤務.木曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00226")
	private BigDecimal thursdayStartTime2;

	/**
	 * 木終了2 曜日別勤務.木曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00227")
	private BigDecimal thursdayEndTime2;

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
	private BigDecimal fridayStartTime1;

	/**
	 * 金終了1 曜日別勤務.金曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00233")
	private BigDecimal fridayEndTime1;

	/**
	 * 金曜出勤時勤務時間2
	 */
	// @PeregItem("IS00234")

	/**
	 * 金開始2 曜日別勤務.金曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00235")
	private BigDecimal fridayStartTime2;

	/**
	 * 金終了2 曜日別勤務.金曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00236")
	private BigDecimal fridayEndTime2;

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
	private BigDecimal saturdayStartTime1;

	/**
	 * 土終了1 曜日別勤務.土曜日.勤務時間帯.終了 ※回数=1
	 */
	@PeregItem("IS00242")
	private BigDecimal saturdayEndTime1;

	/**
	 * 土曜出勤時勤務時間2
	 */
	// @PeregItem("IS00243")

	/**
	 * 土開始2 曜日別勤務.土曜日.勤務時間帯.開始 ※回数=2
	 */
	@PeregItem("IS00244")
	private BigDecimal saturdayStartTime2;

	/**
	 * 土終了2 曜日別勤務.土曜日.勤務時間帯.終了 ※回数=2
	 */
	@PeregItem("IS00245")
	private BigDecimal saturdayEndTime2;

	/**
	 * 加給CD
	 */
	// @PeregItem("IS00246")

	/**
	 * 年休就時設定 就業時間帯の自動設定区分
	 */
	@PeregItem("IS00247")
	private BigDecimal autoIntervalSetAtr;

	/**
	 * 加算時間利用区分 休暇加算時間利用区分
	 */
	@PeregItem("IS00248")
	private BigDecimal vacationAddedTimeAtr;

	/**
	 * 加算時間１日 休暇加算時間設定.１日
	 */
	@PeregItem("IS00249")
	private BigDecimal oneDay;

	/**
	 * 加算時間ＡＭ 休暇加算時間設定.午前
	 */
	@PeregItem("IS00250")
	private BigDecimal morning;

	/**
	 * 加算時間ＰＭ 休暇加算時間設定.午後
	 */
	@PeregItem("IS00251")
	private BigDecimal afternoon;

	/**
	 * 就業区分 労働制
	 */
	@PeregItem("IS00252")
	private BigDecimal laborSystem;

	/**
	 * 契約時間 契約時間
	 */
	@PeregItem("IS00253")
	private BigDecimal contractTime;

}
