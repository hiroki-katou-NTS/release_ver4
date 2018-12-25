package nts.uk.file.at.infra.worktime;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceDivision;
import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodExceededPredAddVacation;
import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodNoBreak;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedRestCalculateMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.FontRearSection;
import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.LateEarlyAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.MultiStampTimePiorityAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.RestTimeOfficeWorkCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.RoundingGoOutTimeSheet;
import nts.uk.ctx.at.shared.dom.worktime.common.StampPiorityAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.Superiority;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkSystemAtr;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FixedChangeAtr;
import nts.uk.ctx.at.shared.dom.worktime.flowset.PrePlanWorkTimeCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.worktimedisplay.DisplayMode;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.infra.repository.worktime.common.RoundingTimeType;
import nts.uk.ctx.at.shared.infra.repository.worktime.flowset.ResttimeAtr;
import nts.uk.file.at.app.export.worktime.WorkTimeReportRepository;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class JpaWorkTimeReportRepository extends JpaRepository implements WorkTimeReportRepository {
	private static final String SELECT_WORK_TIME_NORMAL;
	private static final String SELECT_WORK_TIME_FLOW;
	private static final String SELECT_WORK_TIME_FLEX;
	static {
		StringBuilder sqlNormal = new StringBuilder();
		sqlNormal.append("SELECT");
		// R1_59 コード
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.WORKTIME_CD, NULL),");
		// R1_60 名称
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.NAME, NULL),");
		// R1_61 略名
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.ABNAME, NULL),");
		// R1_62 記号
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.SYMBOL, NULL),");
		// R1_63 廃止
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.ABOLITION_ATR = ?isAbolish THEN ?isAbolishText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.ABOLITION_ATR = ?notAbolish THEN ?notAbolishText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END, ");
		// R1_64 勤務形態
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.DAILY_WORK_ATR = ?regularWork THEN ?regularWorkText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_65 設定方法
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork THEN ?fixedWorkText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork THEN ?difftimeWorkText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_66 半日勤務
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork");
		sqlNormal.append("			THEN IIF(FIXED_WORK_SET.USE_HALF_DAY = ?isTrue, ?isUseHalfDayText, ?isNotUseHalfDayText)");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork");
		sqlNormal.append("			THEN IIF(DIFF_TIME_WORK_SET.USE_HALF_DAY = ?isFalse, ?isUseHalfDayText, ?isNotUseHalfDayText)");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_67 モード
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_DISP_MODE.DISP_MODE = ?detailMode THEN ?detailModeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_DISP_MODE.DISP_MODE = ?simpleMode THEN ?simpleModeText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_68 備考
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.NOTE, NULL),");
		// R1_69 コメント
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.MEMO, NULL),");
		// R1_70 所定設定.1日の始まりの時刻
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.START_DATE_CLOCK IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(PRED_TIME_SET.START_DATE_CLOCK AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(PRED_TIME_SET.START_DATE_CLOCK AS INTEGER)%60,'0#')), NULL),");
		// R1_71 所定設定.1日の範囲時間.時間
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, PRED_TIME_SET.RANGE_TIME_DAY, NULL),");
		// R1_72 所定設定.1日の範囲時間.夜勤シフト
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.NIGHT_SHIFT_ATR = ?isTrue THEN ?isNightShiftText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.NIGHT_SHIFT_ATR = ?isFalse THEN ?isNotNightShiftText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_235 所定設定.変動可能範囲.前に変動
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DIFF_TIME_WORK_SET.CHANGE_AHEAD IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(DIFF_TIME_WORK_SET.CHANGE_AHEAD AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DIFF_TIME_WORK_SET.CHANGE_AHEAD AS INTEGER)%60,'0#')), NULL),");
		// R1_236 所定設定.変動可能範囲.後に変動
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DIFF_TIME_WORK_SET.CHANGE_BEHIND IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(DIFF_TIME_WORK_SET.CHANGE_BEHIND AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DIFF_TIME_WORK_SET.CHANGE_BEHIND AS INTEGER)%60,'0#')), NULL),");
		// R1_73 所定設定.就業時刻1回目.始業時刻
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET1.START_TIME IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(WORK_TIME_SHEET_SET1.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(WORK_TIME_SHEET_SET1.START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_74 所定設定.就業時刻1回目.終業時刻
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET1.END_TIME IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(WORK_TIME_SHEET_SET1.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(WORK_TIME_SHEET_SET1.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_75 所定設定.就業時刻1回目.残業を含めた所定時間帯を設定する
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.IS_INCLUDE_OT = ?isTrue THEN ?isIncludeOtText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.IS_INCLUDE_OT = ?isFalse THEN ?isNotIncludeOtText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_4 所定設定.就業時刻2回目
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET2.USE_ATR = ?isTrue THEN ?isUseSecondWorkingDayText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET2.USE_ATR = ?isFalse THEN ?isNotUseSecondWorkingDayText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_76 所定設定.就業時刻2回目.始業時刻
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET2.START_TIME IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(WORK_TIME_SHEET_SET2.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(WORK_TIME_SHEET_SET2.START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_77 所定設定.就業時刻2回目.終業時刻
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET2.END_TIME IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(WORK_TIME_SHEET_SET2.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(WORK_TIME_SHEET_SET2.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_78 所定設定.半日勤務.前半終了時刻
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.MORNING_END_TIME IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(PRED_TIME_SET.MORNING_END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(PRED_TIME_SET.MORNING_END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_79 所定設定.半日勤務.後半開始時刻
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.AFTERNOON_START_TIME IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(PRED_TIME_SET.AFTERNOON_START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(PRED_TIME_SET.AFTERNOON_START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_80 所定設定.所定時間.1日
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, CONCAT(CAST(PRED_TIME_SET.PRED_ONE_DAY AS INTEGER)/60, ':',");
		sqlNormal.append("		FORMAT(CAST(PRED_TIME_SET.PRED_ONE_DAY AS INTEGER)%60,'0#')), NULL),");
		// R1_81 所定設定.所定時間.午前
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.PRED_MORNING IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(PRED_TIME_SET.PRED_MORNING AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(PRED_TIME_SET.PRED_MORNING AS INTEGER)%60,'0#')), NULL),");
		// R1_82 所定設定.所定時間.午後
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.PRED_AFTERNOON IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(PRED_TIME_SET.PRED_AFTERNOON AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(PRED_TIME_SET.PRED_AFTERNOON AS INTEGER)%60,'0#')), NULL),");
		// R1_83 所定設定.休暇取得時加算時間.1日
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.WORK_ADD_ONE_DAY IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(PRED_TIME_SET.WORK_ADD_ONE_DAY AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(PRED_TIME_SET.WORK_ADD_ONE_DAY AS INTEGER)%60,'0#')), NULL),");
		// R1_84 所定設定.休暇取得時加算時間.午前
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.WORK_ADD_MORNING IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(PRED_TIME_SET.WORK_ADD_MORNING AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(PRED_TIME_SET.WORK_ADD_MORNING AS INTEGER)%60,'0#')), NULL),");
		// R1_85 所定設定.休暇取得時加算時間.午後
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.WORK_ADD_AFTERNOON IS NOT NULL,");
		sqlNormal.append("		CONCAT(CAST(PRED_TIME_SET.WORK_ADD_AFTERNOON AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(PRED_TIME_SET.WORK_ADD_AFTERNOON AS INTEGER)%60,'0#')), NULL),");
		// R1_86 勤務時間帯.1日勤務用.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_WORK_TIME_SET1.TIME_STR IS NOT NULL, CONCAT(CAST(FIXED_WORK_TIME_SET1.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_WORK_TIME_SET1.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_WORK_TIME_SET1.TIME_STR IS NOT NULL, CONCAT(CAST(DT_WORK_TIME_SET1.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_WORK_TIME_SET1.TIME_STR AS INTEGER)%60,'0#')), NULL)),");
		// R1_87 勤務時間帯.1日勤務用.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_WORK_TIME_SET1.TIME_END IS NOT NULL, CONCAT(CAST(FIXED_WORK_TIME_SET1.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_WORK_TIME_SET1.TIME_END AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_WORK_TIME_SET1.TIME_END IS NOT NULL, CONCAT(CAST(DT_WORK_TIME_SET1.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_WORK_TIME_SET1.TIME_END AS INTEGER)%60,'0#')), NULL)),");
		// R1_88 勤務時間帯.1日勤務用.丸め
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_WORK_TIME_SET1.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET1.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET1.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET1.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET1.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET1.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET1.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET1.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_WORK_TIME_SET1.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET1.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET1.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET1.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET1.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET1.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET1.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET1.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_89 勤務時間帯.1日勤務用.端数
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_WORK_TIME_SET1.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET1.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_WORK_TIME_SET1.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET1.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_90 勤務時間帯.午前勤務用.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_WORK_TIME_SET2.TIME_STR IS NOT NULL, CONCAT(CAST(FIXED_WORK_TIME_SET2.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_WORK_TIME_SET2.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_WORK_TIME_SET2.TIME_STR IS NOT NULL, CONCAT(CAST(DT_WORK_TIME_SET2.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_WORK_TIME_SET2.TIME_STR AS INTEGER)%60,'0#')), NULL)),");
		// R1_91 勤務時間帯.午前勤務用.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_WORK_TIME_SET2.TIME_END IS NOT NULL, CONCAT(CAST(FIXED_WORK_TIME_SET2.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_WORK_TIME_SET2.TIME_END AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_WORK_TIME_SET2.TIME_END IS NOT NULL, CONCAT(CAST(DT_WORK_TIME_SET2.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_WORK_TIME_SET2.TIME_END AS INTEGER)%60,'0#')), NULL)),");
		// R1_92 勤務時間帯.午前勤務用.丸め
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_WORK_TIME_SET2.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET2.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET2.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET2.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET2.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET2.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET2.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET2.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_WORK_TIME_SET2.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET2.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET2.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET2.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET2.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET2.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET2.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET2.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_93 勤務時間帯.午前勤務用.端数
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_WORK_TIME_SET2.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_WORK_TIME_SET2.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_94 勤務時間帯.午後勤務用.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_WORK_TIME_SET3.TIME_STR IS NOT NULL, CONCAT(CAST(FIXED_WORK_TIME_SET3.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_WORK_TIME_SET3.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_WORK_TIME_SET3.TIME_STR IS NOT NULL, CONCAT(CAST(DT_WORK_TIME_SET3.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_WORK_TIME_SET3.TIME_STR AS INTEGER)%60,'0#')), NULL)),");
		// R1_95 勤務時間帯.午後勤務用.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_WORK_TIME_SET3.TIME_END IS NOT NULL, CONCAT(CAST(FIXED_WORK_TIME_SET3.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_WORK_TIME_SET3.TIME_END AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_WORK_TIME_SET3.TIME_END IS NOT NULL, CONCAT(CAST(DT_WORK_TIME_SET3.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_WORK_TIME_SET3.TIME_END AS INTEGER)%60,'0#')), NULL)),");
		// R1_96 勤務時間帯.午後勤務用.丸め
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_WORK_TIME_SET3.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET3.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET3.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET3.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET3.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET3.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET3.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET3.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_WORK_TIME_SET3.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET3.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET3.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET3.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET3.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET3.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET3.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET3.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_97 勤務時間帯.午後勤務用.端数
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_WORK_TIME_SET3.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN FIXED_WORK_TIME_SET3.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_WORK_TIME_SET3.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN DT_WORK_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_98 残業時間帯.法定内残業自動計算
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_WORK_SET.LEGAL_OT_SET = ?isLegalOtSet");
		sqlNormal.append("			THEN ?isUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_WORK_SET.LEGAL_OT_SET = ?isNotLegalOtSet");
		sqlNormal.append("			THEN ?isNotUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DIFF_TIME_WORK_SET.OT_SET = ?isLegalOtSet");
		sqlNormal.append("			THEN ?isUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DIFF_TIME_WORK_SET.OT_SET = ?isNotLegalOtSet");
		sqlNormal.append("			THEN ?isNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_99 残業時間帯.1日勤務用.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_OT_TIME_SET1.TIME_STR IS NOT NULL, CONCAT(CAST(FIXED_OT_TIME_SET1.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_OT_TIME_SET1.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_OT_TIME_SET1.TIME_STR IS NOT NULL, CONCAT(CAST(DT_OT_TIME_SET1.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_OT_TIME_SET1.TIME_STR AS INTEGER)%60,'0#')), NULL)),");
		// R1_100 残業時間帯.1日勤務用.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_OT_TIME_SET1.TIME_END IS NOT NULL, CONCAT(CAST(FIXED_OT_TIME_SET1.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_OT_TIME_SET1.TIME_END AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_OT_TIME_SET1.TIME_END IS NOT NULL, CONCAT(CAST(DT_OT_TIME_SET1.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_OT_TIME_SET1.TIME_END AS INTEGER)%60,'0#')), NULL)),");
		// R1_101 残業時間帯.1日勤務用.丸め
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_OT_TIME_SET1.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET1.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET1.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET1.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET1.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET1.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET1.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET1.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_OT_TIME_SET1.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET1.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET1.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET1.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET1.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET1.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET1.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET1.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_102 残業時間帯.1日勤務用.端数
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_OT_TIME_SET1.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET1.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_OT_TIME_SET1.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET1.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_103 残業時間帯.1日勤務用.残業枠
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_OT_TIME_SET1.OT_FRAME_NO, DT_OT_TIME_SET1.OT_FRAME_NO),");
		// R1_104 残業時間帯.1日勤務用.早出
		sqlNormal.append("	CASE WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_OT_TIME_SET1.TREAT_EARLY_OT_WORK = ?isTrue");
		sqlNormal.append("			THEN ?isUseText");
		sqlNormal.append("		 WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_OT_TIME_SET1.TREAT_EARLY_OT_WORK = ?isFalse");
		sqlNormal.append("			THEN ?isNotUseText");
		sqlNormal.append("		 WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_OT_TIME_SET1.TREAT_EARLY_OT_WORK = ?isTrue");
		sqlNormal.append("			THEN ?isUseText");
		sqlNormal.append("		 WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_OT_TIME_SET1.TREAT_EARLY_OT_WORK = ?isFalse");
		sqlNormal.append("			THEN ?isNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_237 残業時間帯.1日勤務用.固定
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, NULL, DT_OT_TIME_SET1.UPD_START_TIME),");
		// R1_105 残業時間帯.1日勤務用.法定内残業枠
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_OT_TIME_SET1.LEGAL_OT_FRAME_NO, DT_OT_TIME_SET1.LEGAL_OT_FRAME_NO),");
		// R1_106 残業時間帯.1日勤務用.積残順序
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_OT_TIME_SET1.PAYOFF_ORDER, DT_OT_TIME_SET1.PAYOFF_ORDER),");
		// R1_107 残業時間帯.午前勤務用.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_OT_TIME_SET2.TIME_STR IS NOT NULL, CONCAT(CAST(FIXED_OT_TIME_SET2.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_OT_TIME_SET2.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_OT_TIME_SET2.TIME_STR IS NOT NULL, CONCAT(CAST(DT_OT_TIME_SET2.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_OT_TIME_SET2.TIME_STR AS INTEGER)%60,'0#')), NULL)),");
		// R1_108 残業時間帯.午前勤務用.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_OT_TIME_SET2.TIME_END IS NOT NULL, CONCAT(CAST(FIXED_OT_TIME_SET2.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_OT_TIME_SET2.TIME_END AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_OT_TIME_SET2.TIME_END IS NOT NULL, CONCAT(CAST(DT_OT_TIME_SET2.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_OT_TIME_SET2.TIME_END AS INTEGER)%60,'0#')), NULL)),");
		// R1_109 残業時間帯.午前勤務用.丸め
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_OT_TIME_SET2.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET2.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET2.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET2.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET2.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET2.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET2.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET2.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_OT_TIME_SET2.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET2.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET2.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET2.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET2.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET2.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET2.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET2.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_110 残業時間帯.午前勤務用.端数
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_OT_TIME_SET2.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_OT_TIME_SET2.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_111 残業時間帯.午前勤務用.残業枠
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_OT_TIME_SET2.OT_FRAME_NO, DT_OT_TIME_SET2.OT_FRAME_NO),");
		// R1_112 残業時間帯.午前勤務用.早出
		sqlNormal.append("	CASE WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_OT_TIME_SET2.TREAT_EARLY_OT_WORK = ?isTrue");
		sqlNormal.append("			THEN ?isUseText");
		sqlNormal.append("		 WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_OT_TIME_SET2.TREAT_EARLY_OT_WORK = ?isFalse");
		sqlNormal.append("			THEN ?isNotUseText");
		sqlNormal.append("		 WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_OT_TIME_SET2.TREAT_EARLY_OT_WORK = ?isTrue");
		sqlNormal.append("			THEN ?isUseText");
		sqlNormal.append("		 WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_OT_TIME_SET2.TREAT_EARLY_OT_WORK = ?isFalse");
		sqlNormal.append("			THEN ?isNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_238 残業時間帯.午前勤務用.固定
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, NULL, DT_OT_TIME_SET2.UPD_START_TIME),");
		// R1_113 残業時間帯.午前勤務用.法定内残業枠
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_OT_TIME_SET2.LEGAL_OT_FRAME_NO, DT_OT_TIME_SET2.LEGAL_OT_FRAME_NO),");
		// R1_114 残業時間帯.午前勤務用.積残順序
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_OT_TIME_SET2.PAYOFF_ORDER, DT_OT_TIME_SET2.PAYOFF_ORDER),");
		// R1_115 残業時間帯.午後勤務用.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_OT_TIME_SET3.TIME_STR IS NOT NULL, CONCAT(CAST(FIXED_OT_TIME_SET3.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_OT_TIME_SET3.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_OT_TIME_SET3.TIME_STR IS NOT NULL, CONCAT(CAST(DT_OT_TIME_SET3.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_OT_TIME_SET3.TIME_STR AS INTEGER)%60,'0#')), NULL)),");
		// R1_116 残業時間帯.午後勤務用.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_OT_TIME_SET3.TIME_END IS NOT NULL, CONCAT(CAST(FIXED_OT_TIME_SET3.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_OT_TIME_SET3.TIME_END AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_OT_TIME_SET3.TIME_END IS NOT NULL, CONCAT(CAST(DT_OT_TIME_SET3.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_OT_TIME_SET3.TIME_END AS INTEGER)%60,'0#')), NULL)),");
		// R1_117 残業時間帯.午後勤務用.丸め
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_OT_TIME_SET3.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET3.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET3.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET3.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET3.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET3.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET3.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET3.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_OT_TIME_SET3.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET3.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET3.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET3.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET3.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET3.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET3.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET3.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_118 残業時間帯.午後勤務用.端数
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_OT_TIME_SET3.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN FIXED_OT_TIME_SET3.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DT_OT_TIME_SET3.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN DT_OT_TIME_SET3.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_119 残業時間帯.午後勤務用.残業枠
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_OT_TIME_SET3.OT_FRAME_NO, DT_OT_TIME_SET3.OT_FRAME_NO),");
		// R1_120 残業時間帯.午後勤務用.早出
		sqlNormal.append("	CASE WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_OT_TIME_SET3.TREAT_EARLY_OT_WORK = ?isTrue");
		sqlNormal.append("			THEN ?isUseText");
		sqlNormal.append("		 WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_OT_TIME_SET3.TREAT_EARLY_OT_WORK = ?isFalse");
		sqlNormal.append("			THEN ?isNotUseText");
		sqlNormal.append("		 WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_OT_TIME_SET3.TREAT_EARLY_OT_WORK = ?isTrue");
		sqlNormal.append("			THEN ?isUseText");
		sqlNormal.append("		 WHEN WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_OT_TIME_SET3.TREAT_EARLY_OT_WORK = ?isFalse");
		sqlNormal.append("			THEN ?isNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_239 残業時間帯.午後勤務用.固定
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, NULL, DT_OT_TIME_SET3.UPD_START_TIME),");
		// R1_121 残業時間帯.午後勤務用.法定内残業枠
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_OT_TIME_SET3.LEGAL_OT_FRAME_NO, DT_OT_TIME_SET3.LEGAL_OT_FRAME_NO),");
		// R1_122 残業時間帯.午後勤務用.積残順序
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_OT_TIME_SET3.PAYOFF_ORDER, DT_OT_TIME_SET3.PAYOFF_ORDER),");
		// R1_123 打刻時間帯.優先設定.出勤
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET1.PIORITY_ATR = ?beforePiority");
		sqlNormal.append("			THEN ?beforePiorityText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET1.PIORITY_ATR = ?afterPiority");
		sqlNormal.append("			THEN ?afterPiorityText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_124 打刻時間帯.優先設定.退勤
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET2.PIORITY_ATR = ?beforePiority");
		sqlNormal.append("			THEN ?beforePiorityText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET2.PIORITY_ATR = ?afterPiority");
		sqlNormal.append("			THEN ?afterPiorityText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_125 打刻時間帯.打刻丸め.出勤
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("		CASE WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		NULL),");
		// R1_126 打刻時間帯.打刻丸め.出勤前後設定
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET1.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlNormal.append("			THEN ?fontRearSectionBeforeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET1.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlNormal.append("			THEN ?fontRearSectionAfterText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_127 打刻時間帯.打刻丸め.退勤
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("		CASE WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		NULL),");
		// R1_128 打刻時間帯.打刻丸め.退勤前後設定
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET2.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlNormal.append("			THEN ?fontRearSectionBeforeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET2.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlNormal.append("			THEN ?fontRearSectionAfterText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_129 打刻詳細設定.出勤反映時間帯1回目.開始時刻
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_STAMP_REFLECT1.START_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(FIXED_STAMP_REFLECT1.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(FIXED_STAMP_REFLECT1.START_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_STAMP_REFLECT1.START_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(DT_STAMP_REFLECT1.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(DT_STAMP_REFLECT1.START_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_130 打刻詳細設定.出勤反映時間帯1回目.終了時刻
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_STAMP_REFLECT1.END_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(FIXED_STAMP_REFLECT1.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(FIXED_STAMP_REFLECT1.END_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_STAMP_REFLECT1.END_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(DT_STAMP_REFLECT1.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(DT_STAMP_REFLECT1.END_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_131 打刻詳細設定.出勤反映時間帯2回目.開始時刻
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_STAMP_REFLECT3.START_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(FIXED_STAMP_REFLECT3.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(FIXED_STAMP_REFLECT3.START_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_STAMP_REFLECT3.START_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(DT_STAMP_REFLECT3.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(DT_STAMP_REFLECT3.START_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_132 打刻詳細設定.出勤反映時間帯2回目.終了時刻
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_STAMP_REFLECT3.END_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(FIXED_STAMP_REFLECT3.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(FIXED_STAMP_REFLECT3.END_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_STAMP_REFLECT3.END_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(DT_STAMP_REFLECT3.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(DT_STAMP_REFLECT3.END_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_133 打刻詳細設定.退勤反映時間帯1回目.開始時刻
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_STAMP_REFLECT2.START_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(FIXED_STAMP_REFLECT2.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(FIXED_STAMP_REFLECT2.START_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_STAMP_REFLECT2.START_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(DT_STAMP_REFLECT2.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(DT_STAMP_REFLECT2.START_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_134 打刻詳細設定.退勤反映時間帯1回目.終了時刻
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_STAMP_REFLECT2.END_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(FIXED_STAMP_REFLECT2.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(FIXED_STAMP_REFLECT2.END_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_STAMP_REFLECT2.END_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(DT_STAMP_REFLECT2.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(DT_STAMP_REFLECT2.END_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_135 打刻詳細設定.退勤反映時間帯2回目.開始時刻
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_STAMP_REFLECT4.START_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(FIXED_STAMP_REFLECT4.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(FIXED_STAMP_REFLECT4.START_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_STAMP_REFLECT4.START_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(DT_STAMP_REFLECT4.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(DT_STAMP_REFLECT4.START_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_136 打刻詳細設定.退勤反映時間帯2回目.終了時刻
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_STAMP_REFLECT4.END_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(FIXED_STAMP_REFLECT4.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(FIXED_STAMP_REFLECT4.END_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DT_STAMP_REFLECT4.END_TIME IS NOT NULL");
		sqlNormal.append("			THEN CONCAT(CAST(DT_STAMP_REFLECT4.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("				FORMAT(CAST(DT_STAMP_REFLECT4.END_TIME AS INTEGER)%60,'0#'))");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_137 打刻詳細設定.優先設定.入門
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET3.PIORITY_ATR = ?beforePiority");
		sqlNormal.append("			THEN ?beforePiorityText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET3.PIORITY_ATR = ?afterPiority");
		sqlNormal.append("			THEN ?afterPiorityText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_138 打刻詳細設定.優先設定.退門
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET4.PIORITY_ATR = ?beforePiority");
		sqlNormal.append("			THEN ?beforePiorityText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET4.PIORITY_ATR = ?afterPiority");
		sqlNormal.append("			THEN ?afterPiorityText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_139 打刻詳細設定.優先設定.PCログオン
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET5.PIORITY_ATR = ?beforePiority");
		sqlNormal.append("			THEN ?beforePiorityText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET5.PIORITY_ATR = ?afterPiority");
		sqlNormal.append("			THEN ?afterPiorityText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_140 打刻詳細設定.優先設定.PCログオフ
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET6.PIORITY_ATR = ?beforePiority");
		sqlNormal.append("			THEN ?beforePiorityText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET6.PIORITY_ATR = ?afterPiority");
		sqlNormal.append("			THEN ?afterPiorityText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_141 打刻詳細設定.打刻丸め.外出
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("		CASE WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		NULL),");
		// R1_142 打刻詳細設定.打刻丸め.外出前後設定
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET3.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlNormal.append("			THEN ?fontRearSectionBeforeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET3.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlNormal.append("			THEN ?fontRearSectionAfterText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_143 打刻詳細設定.打刻丸め.戻り
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("		CASE WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		NULL),");
		// R1_144 打刻詳細設定.打刻丸め.戻り前後設定
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET4.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlNormal.append("			THEN ?fontRearSectionBeforeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET4.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlNormal.append("			THEN ?fontRearSectionAfterText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_145 休憩時間帯.1日勤務用.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HALF_REST_SET1.START_TIME IS NOT NULL, CONCAT(CAST(FIXED_HALF_REST_SET1.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HALF_REST_SET1.START_TIME AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_HALF_REST_TIME1.START_TIME IS NOT NULL, CONCAT(CAST(DT_HALF_REST_TIME1.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_HALF_REST_TIME1.START_TIME AS INTEGER)%60,'0#')), NULL)),");
		// R1_146 休憩時間帯.1日勤務用.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HALF_REST_SET1.END_TIME IS NOT NULL, CONCAT(CAST(FIXED_HALF_REST_SET1.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HALF_REST_SET1.END_TIME AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_HALF_REST_TIME1.END_TIME IS NOT NULL, CONCAT(CAST(DT_HALF_REST_TIME1.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_HALF_REST_TIME1.END_TIME AS INTEGER)%60,'0#')), NULL)),");
		// R1_240 休憩時間帯.1日勤務用.固定
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, NULL, DT_HALF_REST_TIME1.UPD_START_TIME),");
		// R1_147 休憩時間帯.午前勤務用.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HALF_REST_SET2.START_TIME IS NOT NULL, CONCAT(CAST(FIXED_HALF_REST_SET2.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HALF_REST_SET2.START_TIME AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_HALF_REST_TIME2.START_TIME IS NOT NULL, CONCAT(CAST(DT_HALF_REST_TIME2.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_HALF_REST_TIME2.START_TIME AS INTEGER)%60,'0#')), NULL)),");
		// R1_148 休憩時間帯.午前勤務用.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HALF_REST_SET2.END_TIME IS NOT NULL, CONCAT(CAST(FIXED_HALF_REST_SET2.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HALF_REST_SET2.END_TIME AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_HALF_REST_TIME2.END_TIME IS NOT NULL, CONCAT(CAST(DT_HALF_REST_TIME2.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_HALF_REST_TIME2.END_TIME AS INTEGER)%60,'0#')), NULL)),");
		// R1_241 休憩時間帯.午前勤務用.固定
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, NULL, DT_HALF_REST_TIME2.UPD_START_TIME),");
		// R1_149 休憩時間帯.開始時間.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HALF_REST_SET3.START_TIME IS NOT NULL, CONCAT(CAST(FIXED_HALF_REST_SET3.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HALF_REST_SET3.START_TIME AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_HALF_REST_TIME3.START_TIME IS NOT NULL, CONCAT(CAST(DT_HALF_REST_TIME3.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_HALF_REST_TIME3.START_TIME AS INTEGER)%60,'0#')), NULL)),");
		// R1_150 休憩時間帯.開始時間.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HALF_REST_SET3.END_TIME IS NOT NULL, CONCAT(CAST(FIXED_HALF_REST_SET3.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HALF_REST_SET3.END_TIME AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_HALF_REST_TIME3.END_TIME IS NOT NULL, CONCAT(CAST(DT_HALF_REST_TIME3.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_HALF_REST_TIME3.END_TIME AS INTEGER)%60,'0#')), NULL)),");
		// R1_242 休憩時間帯.開始時間.固定
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, NULL, DT_HALF_REST_TIME3.UPD_START_TIME),");
		// R1_151 休憩時間帯.休憩計算設定.実績での休憩計算方法
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_WORK_SET.CALC_METHOD = ?calculateMethodMasterRef");
		sqlNormal.append("			THEN ?calculateMethodMasterRefText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_WORK_SET.CALC_METHOD = ?calculateMethodPlanRef");
		sqlNormal.append("			THEN ?calculateMethodPlanRefText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DIFF_TIME_WORK_SET.DT_CALC_METHOD = ?calculateMethodMasterRef");
		sqlNormal.append("			THEN ?calculateMethodMasterRefText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DIFF_TIME_WORK_SET.DT_CALC_METHOD = ?calculateMethodPlanRef");
		sqlNormal.append("			THEN ?calculateMethodPlanRefText");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_152 休憩時間帯.休憩計算設定.休憩中に退勤した場合の休憩時間の計算方法
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_WORK_SET.LEV_REST_CALC_TYPE = ?calcMethodAppropAll");
		sqlNormal.append("			THEN ?calcMethodAppropAllText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_WORK_SET.LEV_REST_CALC_TYPE = ?calcMethodNotAppropAll");
		sqlNormal.append("			THEN ?calcMethodNotAppropAllText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork AND FIXED_WORK_SET.LEV_REST_CALC_TYPE = ?calcMethodOfficeWorkAppropAll");
		sqlNormal.append("			THEN ?calcMethodOfficeWorkAppropAllText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DIFF_TIME_WORK_SET.DT_COMMON_REST_SET = ?calcMethodAppropAll");
		sqlNormal.append("			THEN ?calcMethodAppropAllText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DIFF_TIME_WORK_SET.DT_COMMON_REST_SET = ?calcMethodNotAppropAll");
		sqlNormal.append("			THEN ?calcMethodNotAppropAllText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork AND DIFF_TIME_WORK_SET.DT_COMMON_REST_SET = ?calcMethodOfficeWorkAppropAll");
		sqlNormal.append("			THEN ?calcMethodOfficeWorkAppropAllText");
		sqlNormal.append("		ELSE NULL");
		sqlNormal.append("	END,");
		// R1_153 休出時間帯.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HOL_TIME_SET.TIME_STR IS NOT NULL, CONCAT(CAST(FIXED_HOL_TIME_SET.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HOL_TIME_SET.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DIFF_TIME_HOL_SET.TIME_STR IS NOT NULL, CONCAT(CAST(DIFF_TIME_HOL_SET.TIME_STR AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DIFF_TIME_HOL_SET.TIME_STR AS INTEGER)%60,'0#')), NULL)),");
		// R1_154 休出時間帯.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HOL_TIME_SET.TIME_END IS NOT NULL, CONCAT(CAST(FIXED_HOL_TIME_SET.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HOL_TIME_SET.TIME_END AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DIFF_TIME_HOL_SET.TIME_END IS NOT NULL, CONCAT(CAST(DIFF_TIME_HOL_SET.TIME_END AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DIFF_TIME_HOL_SET.TIME_END AS INTEGER)%60,'0#')), NULL)),");
		// R1_155 休出時間帯.法定内休出枠
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_HOL_TIME_SET.HOL_FRAME_NO, DIFF_TIME_HOL_SET.HOL_FRAME_NO),");
		// R1_156 休出時間帯.法定外休出枠
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_HOL_TIME_SET.OUT_HOL_FRAME_NO, DIFF_TIME_HOL_SET.OUT_HOL_FRAME_NO),");
		// R1_157 休出時間帯.法定外休出枠（祝日）
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_HOL_TIME_SET.PUB_HOL_FRAME_NO, DIFF_TIME_HOL_SET.PUB_HOL_FRAME_NO),");
		// R1_158 休出時間帯.丸め
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_HOL_TIME_SET.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN FIXED_HOL_TIME_SET.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN FIXED_HOL_TIME_SET.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN FIXED_HOL_TIME_SET.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN FIXED_HOL_TIME_SET.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN FIXED_HOL_TIME_SET.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN FIXED_HOL_TIME_SET.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN FIXED_HOL_TIME_SET.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DIFF_TIME_HOL_SET.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("			 WHEN DIFF_TIME_HOL_SET.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("			 WHEN DIFF_TIME_HOL_SET.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("			 WHEN DIFF_TIME_HOL_SET.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("			 WHEN DIFF_TIME_HOL_SET.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("			 WHEN DIFF_TIME_HOL_SET.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("			 WHEN DIFF_TIME_HOL_SET.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("			 WHEN DIFF_TIME_HOL_SET.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_159 休出時間帯.端数
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		CASE WHEN FIXED_HOL_TIME_SET.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN FIXED_HOL_TIME_SET.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END,");
		sqlNormal.append("		CASE WHEN DIFF_TIME_HOL_SET.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("			 WHEN DIFF_TIME_HOL_SET.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("			 ELSE NULL");
		sqlNormal.append("		END),");
		// R1_160 休出休憩.開始時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HOL_REST_SET.START_TIME IS NOT NULL, CONCAT(CAST(FIXED_HOL_REST_SET.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HOL_REST_SET.START_TIME AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_HOL_REST_TIME.START_TIME IS NOT NULL, CONCAT(CAST(DT_HOL_REST_TIME.START_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_HOL_REST_TIME.START_TIME AS INTEGER)%60,'0#')), NULL)),");
		// R1_161 休出休憩.終了時間
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork,");
		sqlNormal.append("		IIF(FIXED_HOL_REST_SET.END_TIME IS NOT NULL, CONCAT(CAST(FIXED_HOL_REST_SET.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(FIXED_HOL_REST_SET.END_TIME AS INTEGER)%60,'0#')), NULL),");
		sqlNormal.append("		IIF(DT_HOL_REST_TIME.END_TIME IS NOT NULL, CONCAT(CAST(DT_HOL_REST_TIME.END_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(DT_HOL_REST_TIME.END_TIME AS INTEGER)%60,'0#')), NULL)),");
		// R1_243 休出休憩.固定
		sqlNormal.append("	IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, NULL, DT_HOL_REST_TIME.UPD_START_TIME),");
		// R1_162 外出.外出丸め設定.同じ枠内での丸め設定
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_SAME_FRAME = ?isTrue THEN ?roudingAfterTotalText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_SAME_FRAME = ?isFalse THEN ?roudingAfterEachTimePeriodText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_163 外出.外出丸め設定.枠を跨る場合の丸め設定
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_CROSS_FRAME = ?isTrue THEN ?roudingAfterTotalText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_CROSS_FRAME = ?isFalse THEN ?roudingAfterEachTimePeriodText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_164 外出.私用・組合外出時間.就業時間帯
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_165 外出.私用・組合外出時間.丸め設定
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_166 外出.私用・組合外出時間.丸め設定端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_167 外出.私用・組合外出時間.残業時間帯
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_168 外出.私用・組合外出時間.丸め設定
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_169 外出.私用・組合外出時間.丸め設定端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_170 外出.私用・組合外出時間.休出時間帯
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_171 外出.私用・組合外出時間.丸め設定
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_172 外出.私用・組合外出時間.丸め設定端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_173 外出.私用・組合外出控除時間.就業時間帯
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_174 外出.私用・組合外出控除時間.丸め設定
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_175 外出.私用・組合外出控除時間.丸め設定端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_176 外出.私用・組合外出控除時間.残業時間帯
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_177 外出.私用・組合外出控除時間.丸め設定
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_178 外出.私用・組合外出控除時間.丸め設定端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_179 外出.私用・組合外出控除時間.休出時間帯
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_180 外出.私用・組合外出控除時間.丸め設定
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_181 外出.私用・組合外出控除時間.丸め設定端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_182 外出.公用・有償外出時間.就業時間帯
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_183 外出.公用・有償外出時間.丸め設定
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_184 外出.公用・有償外出時間.丸め設定端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_185 外出.公用・有償外出時間.残業時間帯
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_186 外出.公用・有償外出時間.丸め設定
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_187 外出.公用・有償外出時間.丸め設定端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_188 外出.公用・有償外出時間.休出時間帯
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_189 外出.公用・有償外出時間.丸め設定
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_190 外出.公用・有償外出時間.丸め設定端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_191 遅刻早退.遅刻早退時間丸め.遅刻丸め
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_192 遅刻早退.遅刻早退時間丸め.遅刻端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.RECORD_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.RECORD_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_193 遅刻早退.遅刻早退時間丸め.早退丸め
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_194 遅刻早退.遅刻早退時間丸め.早退端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.RECORD_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.RECORD_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_195 遅刻早退.遅刻早退控除時間丸め.遅刻丸め
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_196 遅刻早退.遅刻早退控除時間丸め.遅刻端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.DEDUCTION_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.DEDUCTION_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_197 遅刻早退.遅刻早退控除時間丸め.早退丸め
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_198 遅刻早退.遅刻早退控除時間丸め.早退端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.DEDUCTION_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.DEDUCTION_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_199 遅刻早退詳細設定.控除時間.遅刻早退時間を就業時間から控除する
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND LATE_EARLY_SET.IS_DEDUCTE_FROM_TIME = ?isTrue THEN ?isDeducteFromTimeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND LATE_EARLY_SET.IS_DEDUCTE_FROM_TIME = ?isFalse THEN ?isNotDeducteFromTimeText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_200 遅刻早退詳細設定.遅刻早退の判定設定.時間丁度の打刻は遅刻とする
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.EXTRACT_LATE_EARLY_TIME = ?isTrue THEN ?isExtractLateEarlyTimeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.EXTRACT_LATE_EARLY_TIME = ?isFalse THEN ?isNotExtractLateEarlyTimeText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_201 遅刻早退詳細設定.遅刻早退の判定設定.時間丁度の打刻は早退とする
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.EXTRACT_LATE_EARLY_TIME = ?isTrue THEN ?isExtractLateEarlyTimeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.EXTRACT_LATE_EARLY_TIME = ?isFalse THEN ?isNotExtractLateEarlyTimeText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_202 遅刻早退詳細設定.猶予時間.遅刻猶予時間
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.GRACE_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(OTHER_LATE_EARLY1.GRACE_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(OTHER_LATE_EARLY1.GRACE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_203 遅刻早退詳細設定.猶予時間.遅刻猶予時間を就業時間に含める
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.INCLUDE_WORKTIME = ?isTrue THEN ?isIncludeWorktimeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.INCLUDE_WORKTIME = ?isFalse THEN ?isNotIncludeWorktimeText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_204 遅刻早退詳細設定.猶予時間.早退猶予時間
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.GRACE_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(OTHER_LATE_EARLY2.GRACE_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(OTHER_LATE_EARLY2.GRACE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_205 遅刻早退詳細設定.猶予時間.早退猶予時間を就業時間に含める
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.INCLUDE_WORKTIME = ?isTrue THEN ?isIncludeWorktimeText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.INCLUDE_WORKTIME = ?isFalse THEN ?isNotIncludeWorktimeText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_206 加給.コード
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, WORKTIME_COMMON_SET.RAISING_SALARY_SET, NULL),");
		// R1_207 加給.名称
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, BONUS_PAY_SET.BONUS_PAY_SET_NAME, NULL),");
		// R1_47 代休.休日出勤
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.USE_ATR = ?isTrue THEN ?isUseSubstitutionText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.USE_ATR = ?isFalse THEN ?isNotUseSubstitutionText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_208 代休.休日出勤.時間区分
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.TRANFER_ATR = ?specifiedTimeSubHol THEN ?specifiedTimeSubHolText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.TRANFER_ATR = ?certainTimeExcSubHol THEN ?certainTimeExcSubHolText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_209 代休.休日出勤.１日
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.ONE_DAY_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(SUBSTITUTION_SET2.ONE_DAY_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(SUBSTITUTION_SET2.ONE_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_210 代休.休日出勤.半日
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.HALF_DAY_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(SUBSTITUTION_SET2.HALF_DAY_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(SUBSTITUTION_SET2.HALF_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_211 代休.休日出勤.一定時間
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.CERTAIN_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(SUBSTITUTION_SET2.CERTAIN_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(SUBSTITUTION_SET2.CERTAIN_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_48 代休.残業
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.USE_ATR = ?isTrue THEN ?isUseSubstitutionText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.USE_ATR = ?isFalse THEN ?isNotUseSubstitutionText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_212 代休.残業.一定時間
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.TRANFER_ATR = ?specifiedTimeSubHol THEN ?specifiedTimeSubHolText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.TRANFER_ATR = ?certainTimeExcSubHol THEN ?certainTimeExcSubHolText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_213 代休.残業.１日
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.ONE_DAY_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(SUBSTITUTION_SET1.ONE_DAY_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(SUBSTITUTION_SET1.ONE_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_214 代休.残業.半日
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.HALF_DAY_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(SUBSTITUTION_SET1.HALF_DAY_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(SUBSTITUTION_SET1.HALF_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_215 代休.残業.一定時間
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.CERTAIN_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(SUBSTITUTION_SET1.CERTAIN_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(SUBSTITUTION_SET1.CERTAIN_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_216 深夜残業.深夜時間丸め
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_217 深夜残業.深夜時間端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.LATE_NIGHT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.LATE_NIGHT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_218 臨時.臨時丸め
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_219 臨時.臨時端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND TEMP_WORKTIME_SET.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND TEMP_WORKTIME_SET.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_220 育児.育児時間帯に勤務した場合の扱い
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.CHILD_CARE_WORK_USE = ?isTrue THEN ?childCareWorkUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.CHILD_CARE_WORK_USE = ?isFalse THEN ?childCareWorkNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_221 育児.介護時間帯に勤務した場合の扱い
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.NUR_TIMEZONE_WORK_USE = ?isTrue THEN ?nurTimezoneWorkUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.NUR_TIMEZONE_WORK_USE = ?isFalse THEN ?nurTimezoneWorkNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_222 医療.日勤申し送り時間
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET1.APP_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(MEDICAL_TIME_SET1.APP_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(MEDICAL_TIME_SET1.APP_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_223 医療.夜勤申し送り時間
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET2.APP_TIME IS NOT NULL, ");
		sqlNormal.append("		CONCAT(CAST(MEDICAL_TIME_SET2.APP_TIME AS INTEGER)/60, ':',");
		sqlNormal.append("			FORMAT(CAST(MEDICAL_TIME_SET2.APP_TIME AS INTEGER)%60,'0#')), NULL),");
		// R1_224 医療.日勤勤務時間.丸め
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_225 医療.日勤勤務時間.端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET1.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET1.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_226 医療.夜勤勤務時間.丸め
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlNormal.append("	CASE WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlNormal.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		sqlNormal.append("	NULL),");
		// R1_227 医療.夜勤勤務時間.端数
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET2.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_228 ０時跨ぎ.0時跨ぎ計算
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.OVER_DAY_CALC_SET = ?isTrue THEN ?isUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.OVER_DAY_CALC_SET = ?isFalse THEN ?isNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_229 その他.勤務種類が休暇の場合に就業時間を計算するか
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.HD_CAL_IS_CALCULATE = ?isTrue THEN ?isUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.HD_CAL_IS_CALCULATE = ?isFalse THEN ?isNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_57 その他.休暇加算時間を加算する場合に就業時間として加算するか
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork ");
		sqlNormal.append("			AND FIXED_WORK_SET.EXCEEDED_PRED_CALC_METHOD = ?calcMethodExceededPredAddVacationWorking THEN ?isUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork ");
		sqlNormal.append("			AND FIXED_WORK_SET.EXCEEDED_PRED_CALC_METHOD = ?calcMethodExceededPredAddVacationOvertime THEN ?isNotUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork ");
		sqlNormal.append("			AND DIFF_TIME_WORK_SET.EXCEEDED_PRED_CALC_METHOD = ?calcMethodExceededPredAddVacationWorking THEN ?isUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork ");
		sqlNormal.append("			AND DIFF_TIME_WORK_SET.EXCEEDED_PRED_CALC_METHOD = ?calcMethodExceededPredAddVacationOvertime THEN ?isNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_231 その他.休暇加算時間を加算する場合に就業時間として加算するか.残業枠
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_WORK_SET.EXCEEDED_PRED_OT_FRAME_NO, DIFF_TIME_WORK_SET.EXCEEDED_PRED_OT_FRAME_NO), NULL),");
		// R1_58 その他.休憩未取得時に就業時間として計算するか
		sqlNormal.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork ");
		sqlNormal.append("			AND FIXED_WORK_SET.OT_CALC_METHOD = ?calcMethodNoBreakWorking THEN ?isUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork ");
		sqlNormal.append("			AND FIXED_WORK_SET.OT_CALC_METHOD = ?calcMethodNoBreakOvertime THEN ?isNotUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork ");
		sqlNormal.append("			AND DIFF_TIME_WORK_SET.OT_CALC_METHOD = ?calcMethodNoBreakWorking THEN ?isUseText");
		sqlNormal.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?difftimeWork ");
		sqlNormal.append("			AND DIFF_TIME_WORK_SET.OT_CALC_METHOD = ?calcMethodNoBreakOvertime THEN ?isNotUseText");
		sqlNormal.append("		 ELSE NULL");
		sqlNormal.append("	END,");
		// R1_232 その他.休憩未取得時に就業時間として計算するか.法定内残業枠
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_WORK_SET.OT_IN_LAW, DIFF_TIME_WORK_SET.OT_IN_LAW), NULL),");
		// R1_233 その他.休憩未取得時に就業時間として計算するか.法定外残業枠
		sqlNormal.append("	IIF(TEMP.ROW_ID = 1, IIF(WORK_TIME_SET.WORKTIME_SET_METHOD = ?fixedWork, FIXED_WORK_SET.OT_NOT_IN_LAW, DIFF_TIME_WORK_SET.OT_NOT_IN_LAW), NULL) ");
		sqlNormal.append("FROM");
		sqlNormal.append("	(SELECT CID, WORKTIME_CD, NAME, ABNAME, SYMBOL, ABOLITION_ATR, DAILY_WORK_ATR, WORKTIME_SET_METHOD, NOTE, MEMO");
		sqlNormal.append("		FROM KSHMT_WORK_TIME_SET WORK_TIME_SET");
		sqlNormal.append("		WHERE CID = ?companyId AND DAILY_WORK_ATR = ?regularWork AND WORKTIME_SET_METHOD IN (?fixedWork, ?difftimeWork)) WORK_TIME_SET");
		sqlNormal.append("	CROSS JOIN (VALUES(1),(2),(3),(4),(5),(6),(7),(8),(9),(10)) TEMP(ROW_ID)");
		sqlNormal.append("	LEFT JOIN KSHMT_WORKTIME_DISP_MODE WORKTIME_DISP_MODE");
		sqlNormal.append("		ON WORK_TIME_SET.CID = WORKTIME_DISP_MODE.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = WORKTIME_DISP_MODE.WORKTIME_CD");
		sqlNormal.append("	JOIN KSHMT_WORKTIME_COMMON_SET WORKTIME_COMMON_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = WORKTIME_COMMON_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = WORKTIME_COMMON_SET.WORKTIME_CD");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_WORK_SET FIXED_WORK_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_WORK_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_WORK_SET.WORKTIME_CD");
		sqlNormal.append("	LEFT JOIN KSHMT_DIFF_TIME_WORK_SET DIFF_TIME_WORK_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DIFF_TIME_WORK_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DIFF_TIME_WORK_SET.WORKTIME_CD");
		sqlNormal.append("	LEFT JOIN KSHMT_PRED_TIME_SET PRED_TIME_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = PRED_TIME_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = PRED_TIME_SET.WORKTIME_CD");
		sqlNormal.append("	JOIN KSHMT_WORK_TIME_SHEET_SET WORK_TIME_SHEET_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = WORK_TIME_SHEET_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = WORK_TIME_SHEET_SET1.WORKTIME_CD");
		sqlNormal.append("		AND WORK_TIME_SHEET_SET1.WORK_NO = ?timezoneUseOne");
		sqlNormal.append("	JOIN KSHMT_WORK_TIME_SHEET_SET WORK_TIME_SHEET_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = WORK_TIME_SHEET_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = WORK_TIME_SHEET_SET2.WORKTIME_CD");
		sqlNormal.append("		AND WORK_TIME_SHEET_SET2.WORK_NO = ?timezoneUseTwo");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_WORK_TIME_SET FIXED_WORK_TIME_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_WORK_TIME_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_WORK_TIME_SET1.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_WORK_TIME_SET1.TIME_FRAME_NO");
		sqlNormal.append("		AND FIXED_WORK_TIME_SET1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_WORK_TIME_SET FIXED_WORK_TIME_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_WORK_TIME_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_WORK_TIME_SET2.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_WORK_TIME_SET2.TIME_FRAME_NO");
		sqlNormal.append("		AND FIXED_WORK_TIME_SET2.AM_PM_ATR = ?amPmAtrAm");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_WORK_TIME_SET FIXED_WORK_TIME_SET3");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_WORK_TIME_SET3.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_WORK_TIME_SET3.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_WORK_TIME_SET3.TIME_FRAME_NO");
		sqlNormal.append("		AND FIXED_WORK_TIME_SET3.AM_PM_ATR = ?amPmAtrPm");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_WORK_TIME_SET DT_WORK_TIME_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_WORK_TIME_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_WORK_TIME_SET1.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_WORK_TIME_SET1.TIME_FRAME_NO");
		sqlNormal.append("		AND DT_WORK_TIME_SET1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_WORK_TIME_SET DT_WORK_TIME_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_WORK_TIME_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_WORK_TIME_SET2.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_WORK_TIME_SET2.TIME_FRAME_NO");
		sqlNormal.append("		AND DT_WORK_TIME_SET2.AM_PM_ATR = ?amPmAtrAm");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_WORK_TIME_SET DT_WORK_TIME_SET3");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_WORK_TIME_SET3.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_WORK_TIME_SET3.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_WORK_TIME_SET3.TIME_FRAME_NO");
		sqlNormal.append("		AND DT_WORK_TIME_SET3.AM_PM_ATR = ?amPmAtrPm");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_OT_TIME_SET FIXED_OT_TIME_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_OT_TIME_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_OT_TIME_SET1.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_OT_TIME_SET1.WORKTIME_NO");
		sqlNormal.append("		AND FIXED_OT_TIME_SET1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_OT_TIME_SET FIXED_OT_TIME_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_OT_TIME_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_OT_TIME_SET2.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_OT_TIME_SET2.WORKTIME_NO");
		sqlNormal.append("		AND FIXED_OT_TIME_SET2.AM_PM_ATR = ?amPmAtrAm");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_OT_TIME_SET FIXED_OT_TIME_SET3");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_OT_TIME_SET3.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_OT_TIME_SET3.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_OT_TIME_SET3.WORKTIME_NO");
		sqlNormal.append("		AND FIXED_OT_TIME_SET3.AM_PM_ATR = ?amPmAtrPm");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_OT_TIME_SET DT_OT_TIME_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_OT_TIME_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_OT_TIME_SET1.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_OT_TIME_SET1.WORK_TIME_NO");
		sqlNormal.append("		AND DT_OT_TIME_SET1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_OT_TIME_SET DT_OT_TIME_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_OT_TIME_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_OT_TIME_SET2.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_OT_TIME_SET2.WORK_TIME_NO");
		sqlNormal.append("		AND DT_OT_TIME_SET2.AM_PM_ATR = ?amPmAtrAm");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_OT_TIME_SET DT_OT_TIME_SET3");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_OT_TIME_SET3.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_OT_TIME_SET3.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_OT_TIME_SET3.WORK_TIME_NO");
		sqlNormal.append("		AND DT_OT_TIME_SET3.AM_PM_ATR = ?amPmAtrPm");
		sqlNormal.append("	JOIN KSHMT_PIORITY_SET PIORITY_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = PIORITY_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET1.WORKTIME_CD");
		sqlNormal.append("		AND PIORITY_SET1.STAMP_ATR = ?stampPiorityAtrGoingWork");
		sqlNormal.append("	JOIN KSHMT_PIORITY_SET PIORITY_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = PIORITY_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET2.WORKTIME_CD");
		sqlNormal.append("		AND PIORITY_SET2.STAMP_ATR = ?stampPiorityAtrLeaveWork");
		sqlNormal.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET3");
		sqlNormal.append("		ON WORK_TIME_SET.CID = PIORITY_SET3.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET3.WORKTIME_CD");
		sqlNormal.append("		AND PIORITY_SET3.STAMP_ATR = ?stampPiorityAtrEntering");
		sqlNormal.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET4");
		sqlNormal.append("		ON WORK_TIME_SET.CID = PIORITY_SET4.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET4.WORKTIME_CD");
		sqlNormal.append("		AND PIORITY_SET4.STAMP_ATR = ?stampPiorityAtrExit");
		sqlNormal.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET5");
		sqlNormal.append("		ON WORK_TIME_SET.CID = PIORITY_SET5.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET5.WORKTIME_CD");
		sqlNormal.append("		AND PIORITY_SET5.STAMP_ATR = ?stampPiorityAtrPcLogin");
		sqlNormal.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET6");
		sqlNormal.append("		ON WORK_TIME_SET.CID = PIORITY_SET6.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET6.WORKTIME_CD");
		sqlNormal.append("		AND PIORITY_SET6.STAMP_ATR = ?stampPiorityAtrPcLogout");
		sqlNormal.append("	JOIN KSHMT_ROUNDING_SET ROUNDING_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = ROUNDING_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET1.WORKTIME_CD");
		sqlNormal.append("		AND ROUNDING_SET1.ATR = ?superiorityAttendance");
		sqlNormal.append("	JOIN KSHMT_ROUNDING_SET ROUNDING_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = ROUNDING_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET2.WORKTIME_CD");
		sqlNormal.append("		AND ROUNDING_SET2.ATR = ?superiorityOfficeWork");
		sqlNormal.append("	LEFT JOIN KSHMT_ROUNDING_SET ROUNDING_SET3");
		sqlNormal.append("		ON WORK_TIME_SET.CID = ROUNDING_SET3.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET3.WORKTIME_CD");
		sqlNormal.append("		AND ROUNDING_SET3.ATR = ?superiorityGoOut");
		sqlNormal.append("	LEFT JOIN KSHMT_ROUNDING_SET ROUNDING_SET4");
		sqlNormal.append("		ON WORK_TIME_SET.CID = ROUNDING_SET4.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET4.WORKTIME_CD");
		sqlNormal.append("		AND ROUNDING_SET4.ATR = ?superiorityTurnBack");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_STAMP_REFLECT FIXED_STAMP_REFLECT1");
		sqlNormal.append("		 ON WORK_TIME_SET.CID = FIXED_STAMP_REFLECT1.CID");
		sqlNormal.append("		 AND WORK_TIME_SET.WORKTIME_CD = FIXED_STAMP_REFLECT1.WORKTIME_CD");
		sqlNormal.append("		 AND FIXED_STAMP_REFLECT1.WORK_NO = ?workNoOne");
		sqlNormal.append("		 AND FIXED_STAMP_REFLECT1.ATR = ?goLeavingWorkAtrGoWork");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_STAMP_REFLECT FIXED_STAMP_REFLECT2");
		sqlNormal.append("		 ON WORK_TIME_SET.CID = FIXED_STAMP_REFLECT2.CID");
		sqlNormal.append("		 AND WORK_TIME_SET.WORKTIME_CD = FIXED_STAMP_REFLECT2.WORKTIME_CD");
		sqlNormal.append("		 AND FIXED_STAMP_REFLECT2.WORK_NO = ?workNoOne");
		sqlNormal.append("		 AND FIXED_STAMP_REFLECT2.ATR = ?goLeavingWorkAtrLeaveWork");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_STAMP_REFLECT FIXED_STAMP_REFLECT3");
		sqlNormal.append("		 ON WORK_TIME_SET.CID = FIXED_STAMP_REFLECT3.CID");
		sqlNormal.append("		 AND WORK_TIME_SET.WORKTIME_CD = FIXED_STAMP_REFLECT3.WORKTIME_CD");
		sqlNormal.append("		 AND FIXED_STAMP_REFLECT3.WORK_NO = ?workNoTwo");
		sqlNormal.append("		 AND FIXED_STAMP_REFLECT3.ATR = ?goLeavingWorkAtrGoWork");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_STAMP_REFLECT FIXED_STAMP_REFLECT4");
		sqlNormal.append("		 ON WORK_TIME_SET.CID = FIXED_STAMP_REFLECT4.CID");
		sqlNormal.append("		 AND WORK_TIME_SET.WORKTIME_CD = FIXED_STAMP_REFLECT4.WORKTIME_CD");
		sqlNormal.append("		 AND FIXED_STAMP_REFLECT4.WORK_NO = ?workNoTwo");
		sqlNormal.append("		 AND FIXED_STAMP_REFLECT4.ATR = ?goLeavingWorkAtrLeaveWork");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_STAMP_REFLECT DT_STAMP_REFLECT1");
		sqlNormal.append("		 ON WORK_TIME_SET.CID = DT_STAMP_REFLECT1.CID");
		sqlNormal.append("		 AND WORK_TIME_SET.WORKTIME_CD = DT_STAMP_REFLECT1.WORKTIME_CD");
		sqlNormal.append("		 AND DT_STAMP_REFLECT1.WORK_NO = ?workNoOne");
		sqlNormal.append("		 AND DT_STAMP_REFLECT1.ATR = ?goLeavingWorkAtrGoWork");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_STAMP_REFLECT DT_STAMP_REFLECT2");
		sqlNormal.append("		 ON WORK_TIME_SET.CID = DT_STAMP_REFLECT2.CID");
		sqlNormal.append("		 AND WORK_TIME_SET.WORKTIME_CD = DT_STAMP_REFLECT2.WORKTIME_CD");
		sqlNormal.append("		 AND DT_STAMP_REFLECT2.WORK_NO = ?workNoOne");
		sqlNormal.append("		 AND DT_STAMP_REFLECT2.ATR = ?goLeavingWorkAtrLeaveWork");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_STAMP_REFLECT DT_STAMP_REFLECT3");
		sqlNormal.append("		 ON WORK_TIME_SET.CID = DT_STAMP_REFLECT3.CID");
		sqlNormal.append("		 AND WORK_TIME_SET.WORKTIME_CD = DT_STAMP_REFLECT3.WORKTIME_CD");
		sqlNormal.append("		 AND DT_STAMP_REFLECT3.WORK_NO = ?workNoTwo");
		sqlNormal.append("		 AND DT_STAMP_REFLECT3.ATR = ?goLeavingWorkAtrGoWork");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_STAMP_REFLECT DT_STAMP_REFLECT4");
		sqlNormal.append("		 ON WORK_TIME_SET.CID = DT_STAMP_REFLECT4.CID");
		sqlNormal.append("		 AND WORK_TIME_SET.WORKTIME_CD = DT_STAMP_REFLECT4.WORKTIME_CD");
		sqlNormal.append("		 AND DT_STAMP_REFLECT4.WORK_NO = ?workNoTwo");
		sqlNormal.append("		 AND DT_STAMP_REFLECT4.ATR = ?goLeavingWorkAtrLeaveWork");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_HALF_REST_SET FIXED_HALF_REST_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_HALF_REST_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_HALF_REST_SET1.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_HALF_REST_SET1.PERIOD_NO");
		sqlNormal.append("		AND FIXED_HALF_REST_SET1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_HALF_REST_SET FIXED_HALF_REST_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_HALF_REST_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_HALF_REST_SET2.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_HALF_REST_SET2.PERIOD_NO");
		sqlNormal.append("		AND FIXED_HALF_REST_SET2.AM_PM_ATR = ?amPmAtrAm");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_HALF_REST_SET FIXED_HALF_REST_SET3");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_HALF_REST_SET3.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_HALF_REST_SET3.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_HALF_REST_SET3.PERIOD_NO");
		sqlNormal.append("		AND FIXED_HALF_REST_SET3.AM_PM_ATR = ?amPmAtrPm");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_HALF_REST_TIME DT_HALF_REST_TIME1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_HALF_REST_TIME1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_HALF_REST_TIME1.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_HALF_REST_TIME1.PERIOD_NO");
		sqlNormal.append("		AND DT_HALF_REST_TIME1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_HALF_REST_TIME DT_HALF_REST_TIME2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_HALF_REST_TIME2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_HALF_REST_TIME2.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_HALF_REST_TIME2.PERIOD_NO");
		sqlNormal.append("		AND DT_HALF_REST_TIME2.AM_PM_ATR = ?amPmAtrAm");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_HALF_REST_TIME DT_HALF_REST_TIME3");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_HALF_REST_TIME3.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_HALF_REST_TIME3.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_HALF_REST_TIME3.PERIOD_NO");
		sqlNormal.append("		AND DT_HALF_REST_TIME3.AM_PM_ATR = ?amPmAtrPm");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_HOL_TIME_SET FIXED_HOL_TIME_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_HOL_TIME_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_HOL_TIME_SET.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_HOL_TIME_SET.WORKTIME_NO");
		sqlNormal.append("	LEFT JOIN KSHMT_DIFF_TIME_HOL_SET DIFF_TIME_HOL_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DIFF_TIME_HOL_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DIFF_TIME_HOL_SET.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DIFF_TIME_HOL_SET.WORK_TIME_NO");
		sqlNormal.append("	LEFT JOIN KSHMT_FIXED_HOL_REST_SET FIXED_HOL_REST_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = FIXED_HOL_REST_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = FIXED_HOL_REST_SET.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = FIXED_HOL_REST_SET.PERIOD_NO");
		sqlNormal.append("	LEFT JOIN KSHMT_DT_HOL_REST_TIME DT_HOL_REST_TIME");
		sqlNormal.append("		ON WORK_TIME_SET.CID = DT_HOL_REST_TIME.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = DT_HOL_REST_TIME.WORKTIME_CD");
		sqlNormal.append("		AND TEMP.ROW_ID = DT_HOL_REST_TIME.PERIOD_NO");
		sqlNormal.append("	JOIN KSHMT_WORKTIME_GO_OUT_SET WORKTIME_GO_OUT_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = WORKTIME_GO_OUT_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = WORKTIME_GO_OUT_SET.WORKTIME_CD");
		sqlNormal.append("	JOIN KSHMT_SPECIAL_ROUND_OUT SPECIAL_ROUND_OUT1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = SPECIAL_ROUND_OUT1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = SPECIAL_ROUND_OUT1.WORKTIME_CD");
		sqlNormal.append("		AND SPECIAL_ROUND_OUT1.ROUNDING_TIME_TYPE = ?roundingTimeTypeWorkTimezone");
		sqlNormal.append("	JOIN KSHMT_SPECIAL_ROUND_OUT SPECIAL_ROUND_OUT2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = SPECIAL_ROUND_OUT2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = SPECIAL_ROUND_OUT2.WORKTIME_CD");
		sqlNormal.append("		AND SPECIAL_ROUND_OUT2.ROUNDING_TIME_TYPE = ?roundingTimeTypePubHolWorkTimezone");
		sqlNormal.append("	JOIN KSHMT_SPECIAL_ROUND_OUT SPECIAL_ROUND_OUT3");
		sqlNormal.append("		ON WORK_TIME_SET.CID = SPECIAL_ROUND_OUT3.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = SPECIAL_ROUND_OUT3.WORKTIME_CD");
		sqlNormal.append("		AND SPECIAL_ROUND_OUT3.ROUNDING_TIME_TYPE = ?roundingTimeTypeOtTimezone");
		sqlNormal.append("	JOIN KSHMT_OTHER_LATE_EARLY OTHER_LATE_EARLY1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = OTHER_LATE_EARLY1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = OTHER_LATE_EARLY1.WORKTIME_CD");
		sqlNormal.append("		AND OTHER_LATE_EARLY1.LATE_EARLY_ATR = ?lateEarlyAtrLate");
		sqlNormal.append("	JOIN KSHMT_OTHER_LATE_EARLY OTHER_LATE_EARLY2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = OTHER_LATE_EARLY2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = OTHER_LATE_EARLY2.WORKTIME_CD");
		sqlNormal.append("		AND OTHER_LATE_EARLY2.LATE_EARLY_ATR = ?lateEarlyAtrEarly");
		sqlNormal.append("	JOIN KSHMT_LATE_EARLY_SET LATE_EARLY_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = LATE_EARLY_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = LATE_EARLY_SET.WORKTIME_CD");
		sqlNormal.append("		LEFT JOIN KBPMT_BONUS_PAY_SET BONUS_PAY_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = BONUS_PAY_SET.CID");
		sqlNormal.append("		AND WORKTIME_COMMON_SET.RAISING_SALARY_SET = BONUS_PAY_SET.BONUS_PAY_SET_CD");
		sqlNormal.append("	JOIN KSHMT_SUBSTITUTION_SET SUBSTITUTION_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = SUBSTITUTION_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = SUBSTITUTION_SET1.WORKTIME_CD");
		sqlNormal.append("		AND SUBSTITUTION_SET1.ORIGIN_ATR = ?fromOverTime");
		sqlNormal.append("	JOIN KSHMT_SUBSTITUTION_SET SUBSTITUTION_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = SUBSTITUTION_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = SUBSTITUTION_SET2.WORKTIME_CD");
		sqlNormal.append("		AND SUBSTITUTION_SET2.ORIGIN_ATR = ?workDayOffTime");
		sqlNormal.append("	JOIN KSHMT_TEMP_WORKTIME_SET TEMP_WORKTIME_SET");
		sqlNormal.append("		ON WORK_TIME_SET.CID = TEMP_WORKTIME_SET.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = TEMP_WORKTIME_SET.WORKTIME_CD");
		sqlNormal.append("	JOIN KSHMT_MEDICAL_TIME_SET MEDICAL_TIME_SET1");
		sqlNormal.append("		ON WORK_TIME_SET.CID = MEDICAL_TIME_SET1.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = MEDICAL_TIME_SET1.WORKTIME_CD");
		sqlNormal.append("		AND MEDICAL_TIME_SET1.WORK_SYS_ATR = ?workSystemAtrDayShift");
		sqlNormal.append("	JOIN KSHMT_MEDICAL_TIME_SET MEDICAL_TIME_SET2");
		sqlNormal.append("		ON WORK_TIME_SET.CID = MEDICAL_TIME_SET2.CID");
		sqlNormal.append("		AND WORK_TIME_SET.WORKTIME_CD = MEDICAL_TIME_SET2.WORKTIME_CD");
		sqlNormal.append("		AND MEDICAL_TIME_SET2.WORK_SYS_ATR = ?workSystemAtrNightShift");
		sqlNormal.append("	ORDER BY WORK_TIME_SET.WORKTIME_CD, TEMP.ROW_ID;");

		SELECT_WORK_TIME_NORMAL = sqlNormal.toString();

		StringBuilder sqlFlow = new StringBuilder();
		sqlFlow.append("SELECT");
		// R2_51 コード
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.WORKTIME_CD, NULL),");
		// R2_52 名称
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.NAME, NULL),");
		// R2_53 略名
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.ABNAME, NULL),");
		// R2_54 記号
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.SYMBOL, NULL),");
		// R2_55 廃止
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.ABOLITION_ATR = ?isAbolish THEN ?isAbolishText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.ABOLITION_ATR = ?notAbolish THEN ?notAbolishText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END, ");
		// R2_56 勤務形態
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.DAILY_WORK_ATR = ?regularWork THEN ?regularWorkText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_57 設定方法
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.WORKTIME_SET_METHOD = ?flowWork THEN ?flowWorkText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_58 モード
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_DISP_MODE.DISP_MODE = ?detailMode THEN ?detailModeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_DISP_MODE.DISP_MODE = ?simpleMode THEN ?simpleModeText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_60 備考
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.NOTE, NULL),");
		// R2_61 コメント
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.MEMO, NULL),");
		// R2_62 所定設定.1日の始まりの時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.START_DATE_CLOCK IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(PRED_TIME_SET.START_DATE_CLOCK AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(PRED_TIME_SET.START_DATE_CLOCK AS INTEGER)%60,'0#')), NULL),");
		// R2_63 所定設定.1日の範囲時間.時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, PRED_TIME_SET.RANGE_TIME_DAY, NULL),");
		// R2_64 所定設定.1日の範囲時間.夜勤シフト
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.NIGHT_SHIFT_ATR = ?isTrue THEN ?isNightShiftText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.NIGHT_SHIFT_ATR = ?isFalse THEN ?isNotNightShiftText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_65 所定設定.就業時刻1回目.始業時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET1.START_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(WORK_TIME_SHEET_SET1.START_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(WORK_TIME_SHEET_SET1.START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_66 所定設定.就業時刻1回目.終業時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET1.END_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(WORK_TIME_SHEET_SET1.END_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(WORK_TIME_SHEET_SET1.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_67 所定設定.就業時刻1回目.残業を含めた所定時間帯を設定する
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.IS_INCLUDE_OT = ?isTrue THEN ?isIncludeOtText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.IS_INCLUDE_OT = ?isFalse THEN ?isNotIncludeOtText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_4 所定設定.就業時刻2回目
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET2.USE_ATR = ?isTrue THEN ?isUseSecondWorkingDayText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET2.USE_ATR = ?isFalse THEN ?isNotUseSecondWorkingDayText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_69 所定設定.就業時刻2回目.始業時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET2.START_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(WORK_TIME_SHEET_SET2.START_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(WORK_TIME_SHEET_SET2.START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_70 所定設定.就業時刻2回目.終業時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET2.END_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(WORK_TIME_SHEET_SET2.END_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(WORK_TIME_SHEET_SET2.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_71 所定設定.半日勤務.前半終了時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.MORNING_END_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(PRED_TIME_SET.MORNING_END_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(PRED_TIME_SET.MORNING_END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_72 所定設定.半日勤務.後半開始時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.AFTERNOON_START_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(PRED_TIME_SET.AFTERNOON_START_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(PRED_TIME_SET.AFTERNOON_START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_73 所定設定.所定時間.1日
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.PRED_ONE_DAY IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(PRED_TIME_SET.PRED_ONE_DAY AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(PRED_TIME_SET.PRED_ONE_DAY AS INTEGER)%60,'0#')), NULL),");
		// R2_74 所定設定.所定時間.午前
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.PRED_MORNING IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(PRED_TIME_SET.PRED_MORNING AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(PRED_TIME_SET.PRED_MORNING AS INTEGER)%60,'0#')), NULL),");
		// R2_75 所定設定.所定時間.午後
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.PRED_AFTERNOON IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(PRED_TIME_SET.PRED_AFTERNOON AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(PRED_TIME_SET.PRED_AFTERNOON AS INTEGER)%60,'0#')), NULL),");
		// R2_76 所定設定.休暇取得時加算時間.1日
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.WORK_ADD_ONE_DAY IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(PRED_TIME_SET.WORK_ADD_ONE_DAY AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(PRED_TIME_SET.WORK_ADD_ONE_DAY AS INTEGER)%60,'0#')), NULL),");
		// R2_77 所定設定.休暇取得時加算時間.午前
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.WORK_ADD_MORNING IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(PRED_TIME_SET.WORK_ADD_MORNING AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(PRED_TIME_SET.WORK_ADD_MORNING AS INTEGER)%60,'0#')), NULL),");
		// R2_78 所定設定.休暇取得時加算時間.午後
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.WORK_ADD_AFTERNOON IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(PRED_TIME_SET.WORK_ADD_AFTERNOON AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(PRED_TIME_SET.WORK_ADD_AFTERNOON AS INTEGER)%60,'0#')), NULL),");
		// R2_79 勤務時間帯.就業時間帯丸め.丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("		CASE WHEN FLOW_TIME_ZONE.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("			 WHEN FLOW_TIME_ZONE.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("			 WHEN FLOW_TIME_ZONE.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("			 WHEN FLOW_TIME_ZONE.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("			 WHEN FLOW_TIME_ZONE.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("			 WHEN FLOW_TIME_ZONE.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("			 WHEN FLOW_TIME_ZONE.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("			 WHEN FLOW_TIME_ZONE.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("			 ELSE NULL");
		sqlFlow.append("		END,");
		sqlFlow.append("		NULL),");
		// R2_80 勤務時間帯.就業時間帯丸め.丸め
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_TIME_ZONE.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_TIME_ZONE.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_81 勤務時間帯.予定開始時刻より前に出勤した場合の計算方法
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_WORK_SET.CALC_STR_TIME_SET = ?calcFromPlanStartTime");
		sqlFlow.append("			THEN ?calcFromPlanStartTimeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_WORK_SET.CALC_STR_TIME_SET = ?calcFromWorkTime");
		sqlFlow.append("			THEN ?calcFromWorkTimeText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_82 勤務時間帯.所定時間に変更（予定開始・終了時刻の変更）があった場合、所定時間が終わり次第、残業を求める
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_WORK_SET.FIXED_CHANGE_ATR = ?fixedChangeAtrNotChange");
		sqlFlow.append("			THEN ?fixedChangeAtrNotChangeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_WORK_SET.FIXED_CHANGE_ATR = ?fixedChangeAtrAfterShift");
		sqlFlow.append("			THEN ?fixedChangeAtrAfterShiftText");
		sqlFlow.append("		WHEN TEMP.ROW_ID = 1 AND FLOW_WORK_SET.FIXED_CHANGE_ATR = ?fixedChangeAtrBeforeAfterShift");
		sqlFlow.append("			THEN ?fixedChangeAtrBeforeAfterShiftText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_83 残業時間帯.経過時間
		sqlFlow.append("	IIF(OT_TIME_ZONE.PASSAGE_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(OT_TIME_ZONE.PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(OT_TIME_ZONE.PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_84 残業時間帯.丸め
		sqlFlow.append("	CASE WHEN OT_TIME_ZONE.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN OT_TIME_ZONE.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN OT_TIME_ZONE.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN OT_TIME_ZONE.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN OT_TIME_ZONE.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN OT_TIME_ZONE.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN OT_TIME_ZONE.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN OT_TIME_ZONE.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_85 残業時間帯.端数
		sqlFlow.append("	CASE WHEN OT_TIME_ZONE.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN OT_TIME_ZONE.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_86 残業時間帯.残業枠
		sqlFlow.append("	OT_TIME_ZONE.OT_FRAME_NO,");
		// R2_87 残業時間帯.法定内残業枠
		sqlFlow.append("	OT_TIME_ZONE.IN_LEGAL_OT_FRAME_NO,");
		// R2_88 残業時間帯.積残順序
		sqlFlow.append("	OT_TIME_ZONE.SETTLEMENT_ORDER,");
		// R2_89 打刻時間帯.優先設定.出勤
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET1.PIORITY_ATR = ?beforePiority");
		sqlFlow.append("			THEN ?beforePiorityText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET1.PIORITY_ATR = ?afterPiority");
		sqlFlow.append("			THEN ?afterPiorityText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_90 打刻時間帯.優先設定.退勤
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET2.PIORITY_ATR = ?beforePiority");
		sqlFlow.append("			THEN ?beforePiorityText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET2.PIORITY_ATR = ?afterPiority");
		sqlFlow.append("			THEN ?afterPiorityText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_91 打刻時間帯.打刻丸め.出勤
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("		CASE WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("			 ELSE NULL");
		sqlFlow.append("		END,");
		sqlFlow.append("		NULL),");
		// R2_92 打刻時間帯.打刻丸め.出勤前後設定
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET1.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlFlow.append("			THEN ?fontRearSectionBeforeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET1.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlFlow.append("			THEN ?fontRearSectionAfterText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_93 打刻時間帯.打刻丸め.退勤
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("		CASE WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("			 ELSE NULL");
		sqlFlow.append("		END,");
		sqlFlow.append("		NULL),");
		// R2_94 打刻時間帯.打刻丸め.退勤前後設定
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET2.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlFlow.append("			THEN ?fontRearSectionBeforeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET2.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlFlow.append("			THEN ?fontRearSectionAfterText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_95 打刻詳細設定.出勤反映時間帯.開始時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND STAMP_REFLECT1.STR_CLOCK IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(STAMP_REFLECT1.STR_CLOCK AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(STAMP_REFLECT1.STR_CLOCK AS INTEGER)%60,'0#')), NULL),");
		// R2_96 打刻詳細設定.出勤反映時間帯.終了時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND STAMP_REFLECT1.END_CLOCK IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(STAMP_REFLECT1.END_CLOCK AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(STAMP_REFLECT1.END_CLOCK AS INTEGER)%60,'0#')), NULL),");
		// R2_97 打刻詳細設定.出勤反映時間帯.2勤務目の開始（1勤務目の退勤から分けられる時間）
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND FSTAMP_REFLECT_TIME.TWO_REFLECT_BASIC_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FSTAMP_REFLECT_TIME.TWO_REFLECT_BASIC_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FSTAMP_REFLECT_TIME.TWO_REFLECT_BASIC_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_98 打刻詳細設定.退勤反映時間帯.開始時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND STAMP_REFLECT2.STR_CLOCK IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(STAMP_REFLECT2.STR_CLOCK AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(STAMP_REFLECT2.STR_CLOCK AS INTEGER)%60,'0#')), NULL),");
		// R2_99 打刻詳細設定.退勤反映時間帯.終了時刻
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND STAMP_REFLECT2.END_CLOCK IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(STAMP_REFLECT2.END_CLOCK AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(STAMP_REFLECT2.END_CLOCK AS INTEGER)%60,'0#')), NULL),");
		// R2_100 打刻詳細設定.優先設定.入門
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET3.PIORITY_ATR = ?beforePiority");
		sqlFlow.append("			THEN ?beforePiorityText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET3.PIORITY_ATR = ?afterPiority");
		sqlFlow.append("			THEN ?afterPiorityText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_101 打刻詳細設定.優先設定.退門
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET4.PIORITY_ATR = ?beforePiority");
		sqlFlow.append("			THEN ?beforePiorityText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET4.PIORITY_ATR = ?afterPiority");
		sqlFlow.append("			THEN ?afterPiorityText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_102 打刻詳細設定.優先設定.PCログオン
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET5.PIORITY_ATR = ?beforePiority");
		sqlFlow.append("			THEN ?beforePiorityText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET5.PIORITY_ATR = ?afterPiority");
		sqlFlow.append("			THEN ?afterPiorityText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_103 打刻詳細設定.優先設定.PCログオフ
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET6.PIORITY_ATR = ?beforePiority");
		sqlFlow.append("			THEN ?beforePiorityText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET6.PIORITY_ATR = ?afterPiority");
		sqlFlow.append("			THEN ?afterPiorityText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_104 打刻詳細設定.打刻丸め.外出
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("		CASE WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("			 ELSE NULL");
		sqlFlow.append("		END,");
		sqlFlow.append("		NULL),");
		// R2_105 打刻詳細設定.打刻丸め.外出前後設定
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET3.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlFlow.append("			THEN ?fontRearSectionBeforeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET3.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlFlow.append("			THEN ?fontRearSectionAfterText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_106 打刻詳細設定.打刻丸め.戻り
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("		CASE WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("			 ELSE NULL");
		sqlFlow.append("		END,");
		sqlFlow.append("		NULL),");
		// R2_107 打刻詳細設定.打刻丸め.戻り前後設定
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET4.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlFlow.append("			THEN ?fontRearSectionBeforeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET4.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlFlow.append("			THEN ?fontRearSectionAfterText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_108 休憩時間帯.休憩時間の固定
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_RT_SET2.FIX_REST_TIME = ?isTrue THEN ?isUseText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_RT_SET2.FIX_REST_TIME = ?isFalse THEN ?isNotUseText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_109 休憩時間帯.休憩時間の固定する.開始時間
		sqlFlow.append("	IIF(FLOW_FIXED_RT_SET2.STR_DAY IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_FIXED_RT_SET2.STR_DAY AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_FIXED_RT_SET2.STR_DAY AS INTEGER)%60,'0#')), NULL),");
		// R2_110 休憩時間帯.休憩時間の固定する.終了時間
		sqlFlow.append("	IIF(FLOW_FIXED_RT_SET2.END_DAY IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_FIXED_RT_SET2.END_DAY AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_FIXED_RT_SET2.END_DAY AS INTEGER)%60,'0#')), NULL),");
		// R2_111 休憩時間帯.休憩時間の固定しない.経過時間
		sqlFlow.append("	IIF(FLOW_FLOW_RT_SET2.PASSAGE_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_FLOW_RT_SET2.PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_FLOW_RT_SET2.PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_112 休憩時間帯.休憩時間の固定しない.休憩時間
		sqlFlow.append("	IIF(FLOW_FLOW_RT_SET2.REST_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_FLOW_RT_SET2.REST_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_FLOW_RT_SET2.REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_113 休憩時間帯.休憩時間の固定しない.以降は下記の時間で繰り返す
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_RT_SET2.USE_REST_AFTER_SET = ?isTrue THEN ?isUseRestAfterSetText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_RT_SET2.USE_REST_AFTER_SET = ?isFalse THEN ?isNotUseRestAfterSetText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_114 休憩時間帯.休憩時間の固定しない.経過時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND FLOW_RT_SET2.AFTER_PASSAGE_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_RT_SET2.AFTER_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_RT_SET2.AFTER_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_115 休憩時間帯.休憩時間の固定しない.休憩時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND FLOW_RT_SET2.AFTER_REST_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_RT_SET2.AFTER_REST_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_RT_SET2.AFTER_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_116 休憩時間帯.休憩計算設定（詳細設定）.休憩中に退勤した場合の休憩時間の計算方法
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_REST_SET.COMMON_CALCULATE_METHOD = ?calcMethodAppropAll");
		sqlFlow.append("			THEN ?calcMethodAppropAllText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_REST_SET.COMMON_CALCULATE_METHOD = ?calcMethodNotAppropAll");
		sqlFlow.append("			THEN ?calcMethodNotAppropAllText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_REST_SET.COMMON_CALCULATE_METHOD = ?calcMethodOfficeWorkAppropAll");
		sqlFlow.append("			THEN ?calcMethodOfficeWorkAppropAllText");
		sqlFlow.append("		ELSE NULL");
		sqlFlow.append("	END,");
		// R2_117 休憩時間帯.休憩計算設定（詳細設定）.2勤務目設定
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_REST_SET.USE_PLURAL_WORK_REST_TIME = ?isTrue THEN ?isUsePluralWorkRestTimeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_REST_SET.USE_PLURAL_WORK_REST_TIME = ?isFalse THEN ?isNotUsePluralWorkRestTimeText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_118 休憩時間帯.休憩計算設定（詳細設定）.丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("		CASE WHEN FLOW_REST_SET.REST_SET_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("			 WHEN FLOW_REST_SET.REST_SET_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("			 WHEN FLOW_REST_SET.REST_SET_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("			 WHEN FLOW_REST_SET.REST_SET_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("			 WHEN FLOW_REST_SET.REST_SET_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("			 WHEN FLOW_REST_SET.REST_SET_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("			 WHEN FLOW_REST_SET.REST_SET_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("			 WHEN FLOW_REST_SET.REST_SET_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("			 ELSE NULL");
		sqlFlow.append("		END,");
		sqlFlow.append("		NULL),");
		// R2_119 休憩時間帯.休憩計算設定（詳細設定）.端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_REST_SET.REST_SET_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_REST_SET.REST_SET_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_120 休憩時間帯.固定休憩設定（休憩時間の固定する）.実績での休憩計算方法
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, FLOW_REST_SET.FIXED_REST_CALC_METHOD, NULL),");
		// R2_121 休憩時間帯.固定休憩設定（休憩時間の固定する）.予定と実績の勤務が一致しな場合はマスタを参照する
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, FLOW_REST_SET.IS_CALC_FROM_SCHEDULE, NULL),");
		// R2_122 休憩時間帯.固定休憩設定（休憩時間の固定する）.休憩時刻が無い場合はマスタから休憩時刻を参照する
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, FLOW_REST_SET.IS_REFER_REST_TIME, NULL),");
		// R2_123 休憩時間帯.固定休憩設定（休憩時間の固定する）.私用外出を休憩として扱う
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, FLOW_REST_SET.USER_PRIVATE_GO_OUT_REST, NULL),");
		// R2_124 休憩時間帯.固定休憩設定（休憩時間の固定する）.組合外出を休憩として扱う
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, FLOW_REST_SET.USER_ASSO_GO_OUT_REST, NULL),");
		// R2_124 休憩時間帯.流動休憩設定（休憩時間の固定しない）.実績での休憩計算方法
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, FLOW_REST_SET.FIXED_CALCULATE_METHOD, NULL),");
		// R2_126 休憩時間帯.流動休憩設定（休憩時間の固定しない）.外出を休憩として扱う
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, FLOW_REST_SET.USE_STAMP, NULL),");
		// R2_127 休憩時間帯.流動休憩設定（休憩時間の固定しない）.外出の計上方法
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, FLOW_REST_SET.USE_STAMP_CALC_METHOD, NULL),");
		// R2_128 休憩時間帯.流動休憩設定（休憩時間の固定しない）.優先設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, FLOW_REST_SET.TIME_MANAGER_SET_ATR, NULL),");
		// R2_129 休出時間帯.経過時間
		sqlFlow.append("	IIF(FWORK_HOLIDAY_TIME.PASSAGE_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FWORK_HOLIDAY_TIME.PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FWORK_HOLIDAY_TIME.PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_130 休出時間帯.法定内休出枠
		sqlFlow.append("	FWORK_HOLIDAY_TIME.INLEGAL_BREAK_REST_TIME,");
		// R2_131 休出時間帯.法定外休出枠
		sqlFlow.append("	FWORK_HOLIDAY_TIME.OUT_LEGALB_REAK_REST_TIME,");
		// R2_132 休出時間帯.法定外休出枠（祝日）
		sqlFlow.append("	FWORK_HOLIDAY_TIME.OUT_LEGAL_PUBHOL_REST_TIME,");
		// R2_133 休出時間帯.丸め
		sqlFlow.append("	CASE WHEN FWORK_HOLIDAY_TIME.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN FWORK_HOLIDAY_TIME.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN FWORK_HOLIDAY_TIME.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN FWORK_HOLIDAY_TIME.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN FWORK_HOLIDAY_TIME.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN FWORK_HOLIDAY_TIME.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN FWORK_HOLIDAY_TIME.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN FWORK_HOLIDAY_TIME.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_134 休出時間帯.端数
		sqlFlow.append("	CASE WHEN FWORK_HOLIDAY_TIME.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN FWORK_HOLIDAY_TIME.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_135 休出休憩.休憩時間の固定
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_RT_SET1.FIX_REST_TIME = ?isTrue THEN ?isUseText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_RT_SET1.FIX_REST_TIME = ?isFalse THEN ?isNotUseText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_136 休出休憩.固定する.開始時間
		sqlFlow.append("	IIF(FLOW_FIXED_RT_SET1.STR_DAY IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_FIXED_RT_SET1.STR_DAY AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_FIXED_RT_SET1.STR_DAY AS INTEGER)%60,'0#')), NULL),");
		// R2_137 休出休憩.固定する.終了時間
		sqlFlow.append("	IIF(FLOW_FIXED_RT_SET1.END_DAY IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_FIXED_RT_SET1.END_DAY AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_FIXED_RT_SET1.END_DAY AS INTEGER)%60,'0#')), NULL),");
		// R2_138 休出休憩.固定しない.経過時間
		sqlFlow.append("	IIF(FLOW_FLOW_RT_SET1.PASSAGE_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_FLOW_RT_SET1.PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_FLOW_RT_SET1.PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_139 休出休憩.固定しない.休憩時間
		sqlFlow.append("	IIF(FLOW_FLOW_RT_SET1.REST_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_FLOW_RT_SET1.REST_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_FLOW_RT_SET1.REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_140 休出休憩.固定しない.以降は下記の時間で繰り返す
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLOW_RT_SET1.USE_REST_AFTER_SET = ?isTrue THEN ?isUseRestAfterSetText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND FLOW_RT_SET1.USE_REST_AFTER_SET = ?isFalse THEN ?isNotUseRestAfterSetText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_141 休出休憩.固定しない.経過時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND FLOW_RT_SET1.AFTER_PASSAGE_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_RT_SET1.AFTER_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_RT_SET1.AFTER_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_142 休出休憩.固定しない.休憩時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND FLOW_RT_SET1.AFTER_REST_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(FLOW_RT_SET1.AFTER_REST_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(FLOW_RT_SET1.AFTER_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_143 外出.外出丸め設定.同じ枠内での丸め設定
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_SAME_FRAME = ?isTrue THEN ?roudingAfterTotalText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_SAME_FRAME = ?isFalse THEN ?roudingAfterEachTimePeriodText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_144 外出.外出丸め設定.枠を跨る場合の丸め設定
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_CROSS_FRAME = ?isTrue THEN ?roudingAfterTotalText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_CROSS_FRAME = ?isFalse THEN ?roudingAfterEachTimePeriodText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_145 外出.私用・組合外出時間.就業時間帯
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_146 外出.私用・組合外出時間.丸め設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_147 外出.私用・組合外出時間.丸め設定端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_148 外出.私用・組合外出時間.残業時間帯
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_149 外出.私用・組合外出時間.丸め設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_150 外出.私用・組合外出時間.丸め設定端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_151 外出.私用・組合外出時間.休出時間帯
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_152 外出.私用・組合外出時間.丸め設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_153 外出.私用・組合外出時間.丸め設定端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_154 外出.私用・組合外出控除時間.就業時間帯
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_155 外出.私用・組合外出控除時間.丸め設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_156 外出.私用・組合外出控除時間.丸め設定端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_157 外出.私用・組合外出控除時間.残業時間帯
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_158 外出.私用・組合外出控除時間.丸め設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_159 外出.私用・組合外出控除時間.丸め設定端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_160 外出.私用・組合外出控除時間.休出時間帯
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_161 外出.私用・組合外出控除時間.丸め設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_162 外出.私用・組合外出控除時間.丸め設定端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_163 外出.公用・有償外出時間.就業時間帯
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_164 外出.公用・有償外出時間.丸め設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_165 外出.公用・有償外出時間.丸め設定端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_166 外出.公用・有償外出時間.残業時間帯
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_167 外出.公用・有償外出時間.丸め設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_168 外出.公用・有償外出時間.丸め設定端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_169 外出.公用・有償外出時間.休出時間帯
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_170 外出.公用・有償外出時間.丸め設定
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_171 外出.公用・有償外出時間.丸め設定端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_172 遅刻早退.遅刻早退時間丸め.遅刻丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_173 遅刻早退.遅刻早退時間丸め.遅刻端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.RECORD_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.RECORD_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_174 遅刻早退.遅刻早退時間丸め.早退丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_175 遅刻早退.遅刻早退時間丸め.早退端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.RECORD_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.RECORD_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_176 遅刻早退.遅刻早退控除時間丸め.遅刻丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_177 遅刻早退.遅刻早退控除時間丸め.遅刻端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.DEDUCTION_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.DEDUCTION_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_178 遅刻早退.遅刻早退控除時間丸め.早退丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_179 遅刻早退.遅刻早退控除時間丸め.早退端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.DEDUCTION_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.DEDUCTION_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_180 遅刻早退詳細設定.遅刻早退の判定設定.時間丁度の打刻は遅刻とする
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.EXTRACT_LATE_EARLY_TIME = ?isTrue THEN ?isExtractLateEarlyTimeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.EXTRACT_LATE_EARLY_TIME = ?isFalse THEN ?isNotExtractLateEarlyTimeText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_181 遅刻早退詳細設定.遅刻早退の判定設定.時間丁度の打刻は早退とする
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.EXTRACT_LATE_EARLY_TIME = ?isTrue THEN ?isExtractLateEarlyTimeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.EXTRACT_LATE_EARLY_TIME = ?isFalse THEN ?isNotExtractLateEarlyTimeText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_182 遅刻早退詳細設定.猶予時間.遅刻猶予時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.GRACE_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(OTHER_LATE_EARLY1.GRACE_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(OTHER_LATE_EARLY1.GRACE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_183 遅刻早退詳細設定.猶予時間.遅刻猶予時間を就業時間に含める
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.INCLUDE_WORKTIME = ?isTrue THEN ?isIncludeWorktimeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.INCLUDE_WORKTIME = ?isFalse THEN ?isNotIncludeWorktimeText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_184 遅刻早退詳細設定.猶予時間.早退猶予時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.GRACE_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(OTHER_LATE_EARLY2.GRACE_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(OTHER_LATE_EARLY2.GRACE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_185 遅刻早退詳細設定.猶予時間.早退猶予時間を就業時間に含める
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.INCLUDE_WORKTIME = ?isTrue THEN ?isIncludeWorktimeText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.INCLUDE_WORKTIME = ?isFalse THEN ?isNotIncludeWorktimeText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_186 加給.コード
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, WORKTIME_COMMON_SET.RAISING_SALARY_SET, NULL),");
		// R2_187 加給.名称
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, BONUS_PAY_SET.BONUS_PAY_SET_NAME, NULL),");
		// R2_41 代休.休日出勤
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.USE_ATR = ?isTrue THEN ?isUseSubstitutionText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.USE_ATR = ?isFalse THEN ?isNotUseSubstitutionText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_189 代休.休日出勤.時間区分
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.TRANFER_ATR = ?specifiedTimeSubHol THEN ?specifiedTimeSubHolText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.TRANFER_ATR = ?certainTimeExcSubHol THEN ?certainTimeExcSubHolText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_190 代休.休日出勤.１日
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.ONE_DAY_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(SUBSTITUTION_SET2.ONE_DAY_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(SUBSTITUTION_SET2.ONE_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_191 代休.休日出勤.半日
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.HALF_DAY_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(SUBSTITUTION_SET2.HALF_DAY_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(SUBSTITUTION_SET2.HALF_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_192 代休.休日出勤.一定時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.CERTAIN_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(SUBSTITUTION_SET2.CERTAIN_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(SUBSTITUTION_SET2.CERTAIN_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_42 代休.残業
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.USE_ATR = ?isTrue THEN ?isUseSubstitutionText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.USE_ATR = ?isFalse THEN ?isNotUseSubstitutionText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_194 代休.残業.一定時間
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.TRANFER_ATR = ?specifiedTimeSubHol THEN ?specifiedTimeSubHolText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.TRANFER_ATR = ?certainTimeExcSubHol THEN ?certainTimeExcSubHolText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_195 代休.残業.１日
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.ONE_DAY_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(SUBSTITUTION_SET1.ONE_DAY_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(SUBSTITUTION_SET1.ONE_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_196 代休.残業.半日
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.HALF_DAY_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(SUBSTITUTION_SET1.HALF_DAY_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(SUBSTITUTION_SET1.HALF_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_197 代休.残業.一定時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.CERTAIN_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(SUBSTITUTION_SET1.CERTAIN_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(SUBSTITUTION_SET1.CERTAIN_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_198 深夜残業.深夜時間丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_199 深夜残業.深夜時間端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.LATE_NIGHT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.LATE_NIGHT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_200 臨時.臨時丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_201 臨時.臨時端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND TEMP_WORKTIME_SET.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND TEMP_WORKTIME_SET.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_202 育児.育児時間帯に勤務した場合の扱い
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.CHILD_CARE_WORK_USE = ?isTrue THEN ?childCareWorkUseText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.CHILD_CARE_WORK_USE = ?isFalse THEN ?childCareWorkNotUseText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_203 育児.介護時間帯に勤務した場合の扱い
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.NUR_TIMEZONE_WORK_USE = ?isTrue THEN ?nurTimezoneWorkUseText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.NUR_TIMEZONE_WORK_USE = ?isFalse THEN ?nurTimezoneWorkNotUseText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_204 医療.日勤申し送り時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET1.APP_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(MEDICAL_TIME_SET1.APP_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(MEDICAL_TIME_SET1.APP_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_205 医療.夜勤申し送り時間
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET2.APP_TIME IS NOT NULL, ");
		sqlFlow.append("		CONCAT(CAST(MEDICAL_TIME_SET2.APP_TIME AS INTEGER)/60, ':',");
		sqlFlow.append("			FORMAT(CAST(MEDICAL_TIME_SET2.APP_TIME AS INTEGER)%60,'0#')), NULL),");
		// R2_206 医療.日勤勤務時間.丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_207 医療.日勤勤務時間.端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET1.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET1.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_208 医療.夜勤勤務時間.丸め
		sqlFlow.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlow.append("	CASE WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlow.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		sqlFlow.append("	NULL),");
		// R2_209 医療.夜勤勤務時間.端数
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET2.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_210 ０時跨ぎ.0時跨ぎ計算
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.OVER_DAY_CALC_SET = ?isTrue THEN ?isUseText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.OVER_DAY_CALC_SET = ?isFalse THEN ?isNotUseText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END,");
		// R2_211 その他.勤務種類が休暇の場合に就業時間を計算するか
		sqlFlow.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.HD_CAL_IS_CALCULATE = ?isTrue THEN ?isUseText");
		sqlFlow.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.HD_CAL_IS_CALCULATE = ?isFalse THEN ?isNotUseText");
		sqlFlow.append("		 ELSE NULL");
		sqlFlow.append("	END ");
		sqlFlow.append("FROM");
		sqlFlow.append("	(SELECT CID, WORKTIME_CD, NAME, ABNAME, SYMBOL, ABOLITION_ATR, DAILY_WORK_ATR, WORKTIME_SET_METHOD, NOTE, MEMO");
		sqlFlow.append("		FROM KSHMT_WORK_TIME_SET WORK_TIME_SET");
		sqlFlow.append("		WHERE CID = ?companyId AND DAILY_WORK_ATR = ?regularWork AND WORKTIME_SET_METHOD = ?flowWork) WORK_TIME_SET");
		sqlFlow.append("	CROSS JOIN (VALUES(1),(2),(3),(4),(5),(6),(7),(8),(9),(10)) TEMP(ROW_ID)");
		sqlFlow.append("	LEFT JOIN KSHMT_WORKTIME_DISP_MODE WORKTIME_DISP_MODE");
		sqlFlow.append("		ON WORK_TIME_SET.CID = WORKTIME_DISP_MODE.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = WORKTIME_DISP_MODE.WORKTIME_CD");
		sqlFlow.append("	JOIN KSHMT_WORKTIME_COMMON_SET WORKTIME_COMMON_SET");
		sqlFlow.append("		ON WORK_TIME_SET.CID = WORKTIME_COMMON_SET.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = WORKTIME_COMMON_SET.WORKTIME_CD");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_WORK_SET FLOW_WORK_SET");
		sqlFlow.append("		ON WORK_TIME_SET.CID = FLOW_WORK_SET.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = FLOW_WORK_SET.WORKTIME_CD");
		sqlFlow.append("	LEFT JOIN KSHMT_PRED_TIME_SET PRED_TIME_SET");
		sqlFlow.append("		ON WORK_TIME_SET.CID = PRED_TIME_SET.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = PRED_TIME_SET.WORKTIME_CD");
		sqlFlow.append("	JOIN KSHMT_WORK_TIME_SHEET_SET WORK_TIME_SHEET_SET1");
		sqlFlow.append("		ON WORK_TIME_SET.CID = WORK_TIME_SHEET_SET1.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = WORK_TIME_SHEET_SET1.WORKTIME_CD");
		sqlFlow.append("		AND WORK_TIME_SHEET_SET1.WORK_NO = ?timezoneUseOne");
		sqlFlow.append("	JOIN KSHMT_WORK_TIME_SHEET_SET WORK_TIME_SHEET_SET2");
		sqlFlow.append("		ON WORK_TIME_SET.CID = WORK_TIME_SHEET_SET2.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = WORK_TIME_SHEET_SET2.WORKTIME_CD");
		sqlFlow.append("		AND WORK_TIME_SHEET_SET2.WORK_NO = ?timezoneUseTwo");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_TIME_ZONE FLOW_TIME_ZONE");
		sqlFlow.append("		ON WORK_TIME_SET.CID = FLOW_TIME_ZONE.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = FLOW_TIME_ZONE.WORKTIME_CD");
		sqlFlow.append("	LEFT JOIN KSHMT_OT_TIME_ZONE OT_TIME_ZONE");
		sqlFlow.append("		ON WORK_TIME_SET.CID = OT_TIME_ZONE.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = OT_TIME_ZONE.WORKTIME_CD");
		sqlFlow.append("		AND TEMP.ROW_ID = OT_TIME_ZONE.WORKTIME_NO");
		sqlFlow.append("	JOIN KSHMT_PIORITY_SET PIORITY_SET1");
		sqlFlow.append("		ON WORK_TIME_SET.CID = PIORITY_SET1.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET1.WORKTIME_CD");
		sqlFlow.append("		AND PIORITY_SET1.STAMP_ATR = ?stampPiorityAtrGoingWork");
		sqlFlow.append("	JOIN KSHMT_PIORITY_SET PIORITY_SET2");
		sqlFlow.append("		ON WORK_TIME_SET.CID = PIORITY_SET2.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET2.WORKTIME_CD");
		sqlFlow.append("		AND PIORITY_SET2.STAMP_ATR = ?stampPiorityAtrLeaveWork");
		sqlFlow.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET3");
		sqlFlow.append("		ON WORK_TIME_SET.CID = PIORITY_SET3.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET3.WORKTIME_CD");
		sqlFlow.append("		AND PIORITY_SET3.STAMP_ATR = ?stampPiorityAtrEntering");
		sqlFlow.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET4");
		sqlFlow.append("		ON WORK_TIME_SET.CID = PIORITY_SET4.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET4.WORKTIME_CD");
		sqlFlow.append("		AND PIORITY_SET4.STAMP_ATR = ?stampPiorityAtrExit");
		sqlFlow.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET5");
		sqlFlow.append("		ON WORK_TIME_SET.CID = PIORITY_SET5.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET5.WORKTIME_CD");
		sqlFlow.append("		AND PIORITY_SET5.STAMP_ATR = ?stampPiorityAtrPcLogin");
		sqlFlow.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET6");
		sqlFlow.append("		ON WORK_TIME_SET.CID = PIORITY_SET6.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET6.WORKTIME_CD");
		sqlFlow.append("		AND PIORITY_SET6.STAMP_ATR = ?stampPiorityAtrPcLogout");
		sqlFlow.append("	JOIN KSHMT_ROUNDING_SET ROUNDING_SET1");
		sqlFlow.append("		ON WORK_TIME_SET.CID = ROUNDING_SET1.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET1.WORKTIME_CD");
		sqlFlow.append("		AND ROUNDING_SET1.ATR = ?superiorityAttendance");
		sqlFlow.append("	JOIN KSHMT_ROUNDING_SET ROUNDING_SET2");
		sqlFlow.append("		ON WORK_TIME_SET.CID = ROUNDING_SET2.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET2.WORKTIME_CD");
		sqlFlow.append("		AND ROUNDING_SET2.ATR = ?superiorityOfficeWork");
		sqlFlow.append("	LEFT JOIN KSHMT_ROUNDING_SET ROUNDING_SET3");
		sqlFlow.append("		ON WORK_TIME_SET.CID = ROUNDING_SET3.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET3.WORKTIME_CD");
		sqlFlow.append("		AND ROUNDING_SET3.ATR = ?superiorityGoOut");
		sqlFlow.append("	LEFT JOIN KSHMT_ROUNDING_SET ROUNDING_SET4");
		sqlFlow.append("		ON WORK_TIME_SET.CID = ROUNDING_SET4.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET4.WORKTIME_CD");
		sqlFlow.append("		AND ROUNDING_SET4.ATR = ?superiorityTurnBack");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_STAMP_REFLECT STAMP_REFLECT1");
		sqlFlow.append("		 ON WORK_TIME_SET.CID = STAMP_REFLECT1.CID");
		sqlFlow.append("		 AND WORK_TIME_SET.WORKTIME_CD = STAMP_REFLECT1.WORKTIME_CD");
		sqlFlow.append("		 AND STAMP_REFLECT1.WORK_NO = ?workNoOne");
		sqlFlow.append("		 AND STAMP_REFLECT1.ATTEND_ATR = ?goLeavingWorkAtrGoWork");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_STAMP_REFLECT STAMP_REFLECT2");
		sqlFlow.append("		 ON WORK_TIME_SET.CID = STAMP_REFLECT2.CID");
		sqlFlow.append("		 AND WORK_TIME_SET.WORKTIME_CD = STAMP_REFLECT2.WORKTIME_CD");
		sqlFlow.append("		 AND STAMP_REFLECT2.WORK_NO = ?workNoOne");
		sqlFlow.append("		 AND STAMP_REFLECT2.ATTEND_ATR = ?goLeavingWorkAtrLeaveWork");
		sqlFlow.append("	LEFT JOIN KSHMT_FSTAMP_REFLECT_TIME FSTAMP_REFLECT_TIME");
		sqlFlow.append("		 ON WORK_TIME_SET.CID = FSTAMP_REFLECT_TIME.CID");
		sqlFlow.append("		 AND WORK_TIME_SET.WORKTIME_CD = FSTAMP_REFLECT_TIME.WORKTIME_CD");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_RT_SET FLOW_RT_SET1");
		sqlFlow.append("		 ON WORK_TIME_SET.CID = FLOW_RT_SET1.CID");
		sqlFlow.append("		 AND WORK_TIME_SET.WORKTIME_CD = FLOW_RT_SET1.WORKTIME_CD");
		sqlFlow.append("		 AND FLOW_RT_SET1.RESTTIME_ATR = ?resttimeAtrOffDay");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_RT_SET FLOW_RT_SET2");
		sqlFlow.append("		 ON WORK_TIME_SET.CID = FLOW_RT_SET2.CID");
		sqlFlow.append("		 AND WORK_TIME_SET.WORKTIME_CD = FLOW_RT_SET2.WORKTIME_CD");
		sqlFlow.append("		 AND FLOW_RT_SET2.RESTTIME_ATR = ?resttimeAtrHalfDay");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_FIXED_RT_SET FLOW_FIXED_RT_SET1");
		sqlFlow.append("		ON WORK_TIME_SET.CID = FLOW_FIXED_RT_SET1.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = FLOW_FIXED_RT_SET1.WORKTIME_CD");
		sqlFlow.append("		AND TEMP.ROW_ID = FLOW_FIXED_RT_SET1.PERIOD_NO");
		sqlFlow.append("		AND FLOW_FIXED_RT_SET1.RESTTIME_ATR = ?resttimeAtrOffDay");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_FIXED_RT_SET FLOW_FIXED_RT_SET2");
		sqlFlow.append("		ON WORK_TIME_SET.CID = FLOW_FIXED_RT_SET2.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = FLOW_FIXED_RT_SET2.WORKTIME_CD");
		sqlFlow.append("		AND TEMP.ROW_ID = FLOW_FIXED_RT_SET2.PERIOD_NO");
		sqlFlow.append("		AND FLOW_FIXED_RT_SET2.RESTTIME_ATR = ?resttimeAtrHalfDay");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_FLOW_RT_SET FLOW_FLOW_RT_SET1");
		sqlFlow.append("		ON WORK_TIME_SET.CID = FLOW_FLOW_RT_SET1.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = FLOW_FLOW_RT_SET1.WORKTIME_CD");
		sqlFlow.append("		AND TEMP.ROW_ID = FLOW_FLOW_RT_SET1.PERIOD_NO");
		sqlFlow.append("		AND FLOW_FLOW_RT_SET1.RESTTIME_ATR = ?resttimeAtrOffDay");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_FLOW_RT_SET FLOW_FLOW_RT_SET2");
		sqlFlow.append("		ON WORK_TIME_SET.CID = FLOW_FLOW_RT_SET2.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = FLOW_FLOW_RT_SET2.WORKTIME_CD");
		sqlFlow.append("		AND TEMP.ROW_ID = FLOW_FLOW_RT_SET2.PERIOD_NO");
		sqlFlow.append("		AND FLOW_FLOW_RT_SET2.RESTTIME_ATR = ?resttimeAtrHalfDay");
		sqlFlow.append("	LEFT JOIN KSHMT_FLOW_REST_SET FLOW_REST_SET");
		sqlFlow.append("		 ON WORK_TIME_SET.CID = FLOW_REST_SET.CID");
		sqlFlow.append("		 AND WORK_TIME_SET.WORKTIME_CD = FLOW_REST_SET.WORKTIME_CD");
		sqlFlow.append("	LEFT JOIN KSHMT_FWORK_HOLIDAY_TIME FWORK_HOLIDAY_TIME");
		sqlFlow.append("		ON WORK_TIME_SET.CID = FWORK_HOLIDAY_TIME.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = FWORK_HOLIDAY_TIME.WORKTIME_CD");
		sqlFlow.append("		AND TEMP.ROW_ID = FWORK_HOLIDAY_TIME.WORKTIME_NO");
		sqlFlow.append("	JOIN KSHMT_WORKTIME_GO_OUT_SET WORKTIME_GO_OUT_SET");
		sqlFlow.append("		ON WORK_TIME_SET.CID = WORKTIME_GO_OUT_SET.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = WORKTIME_GO_OUT_SET.WORKTIME_CD");
		sqlFlow.append("	JOIN KSHMT_SPECIAL_ROUND_OUT SPECIAL_ROUND_OUT1");
		sqlFlow.append("		ON WORK_TIME_SET.CID = SPECIAL_ROUND_OUT1.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = SPECIAL_ROUND_OUT1.WORKTIME_CD");
		sqlFlow.append("		AND SPECIAL_ROUND_OUT1.ROUNDING_TIME_TYPE = ?roundingTimeTypeWorkTimezone");
		sqlFlow.append("	JOIN KSHMT_SPECIAL_ROUND_OUT SPECIAL_ROUND_OUT2");
		sqlFlow.append("		ON WORK_TIME_SET.CID = SPECIAL_ROUND_OUT2.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = SPECIAL_ROUND_OUT2.WORKTIME_CD");
		sqlFlow.append("		AND SPECIAL_ROUND_OUT2.ROUNDING_TIME_TYPE = ?roundingTimeTypePubHolWorkTimezone");
		sqlFlow.append("	JOIN KSHMT_SPECIAL_ROUND_OUT SPECIAL_ROUND_OUT3");
		sqlFlow.append("		ON WORK_TIME_SET.CID = SPECIAL_ROUND_OUT3.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = SPECIAL_ROUND_OUT3.WORKTIME_CD");
		sqlFlow.append("		AND SPECIAL_ROUND_OUT3.ROUNDING_TIME_TYPE = ?roundingTimeTypeOtTimezone");
		sqlFlow.append("	JOIN KSHMT_OTHER_LATE_EARLY OTHER_LATE_EARLY1");
		sqlFlow.append("		ON WORK_TIME_SET.CID = OTHER_LATE_EARLY1.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = OTHER_LATE_EARLY1.WORKTIME_CD");
		sqlFlow.append("		AND OTHER_LATE_EARLY1.LATE_EARLY_ATR = ?lateEarlyAtrLate");
		sqlFlow.append("	JOIN KSHMT_OTHER_LATE_EARLY OTHER_LATE_EARLY2");
		sqlFlow.append("		ON WORK_TIME_SET.CID = OTHER_LATE_EARLY2.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = OTHER_LATE_EARLY2.WORKTIME_CD");
		sqlFlow.append("		AND OTHER_LATE_EARLY2.LATE_EARLY_ATR = ?lateEarlyAtrEarly");
		sqlFlow.append("	LEFT JOIN KBPMT_BONUS_PAY_SET BONUS_PAY_SET");
		sqlFlow.append("		ON WORK_TIME_SET.CID = BONUS_PAY_SET.CID");
		sqlFlow.append("		AND WORKTIME_COMMON_SET.RAISING_SALARY_SET = BONUS_PAY_SET.BONUS_PAY_SET_CD");
		sqlFlow.append("	JOIN KSHMT_SUBSTITUTION_SET SUBSTITUTION_SET1");
		sqlFlow.append("		ON WORK_TIME_SET.CID = SUBSTITUTION_SET1.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = SUBSTITUTION_SET1.WORKTIME_CD");
		sqlFlow.append("		AND SUBSTITUTION_SET1.ORIGIN_ATR = ?fromOverTime");
		sqlFlow.append("	JOIN KSHMT_SUBSTITUTION_SET SUBSTITUTION_SET2");
		sqlFlow.append("		ON WORK_TIME_SET.CID = SUBSTITUTION_SET2.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = SUBSTITUTION_SET2.WORKTIME_CD");
		sqlFlow.append("		AND SUBSTITUTION_SET2.ORIGIN_ATR = ?workDayOffTime");
		sqlFlow.append("	JOIN KSHMT_TEMP_WORKTIME_SET TEMP_WORKTIME_SET");
		sqlFlow.append("		ON WORK_TIME_SET.CID = TEMP_WORKTIME_SET.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = TEMP_WORKTIME_SET.WORKTIME_CD");
		sqlFlow.append("	JOIN KSHMT_MEDICAL_TIME_SET MEDICAL_TIME_SET1");
		sqlFlow.append("		ON WORK_TIME_SET.CID = MEDICAL_TIME_SET1.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = MEDICAL_TIME_SET1.WORKTIME_CD");
		sqlFlow.append("		AND MEDICAL_TIME_SET1.WORK_SYS_ATR = ?workSystemAtrDayShift");
		sqlFlow.append("	JOIN KSHMT_MEDICAL_TIME_SET MEDICAL_TIME_SET2");
		sqlFlow.append("		ON WORK_TIME_SET.CID = MEDICAL_TIME_SET2.CID");
		sqlFlow.append("		AND WORK_TIME_SET.WORKTIME_CD = MEDICAL_TIME_SET2.WORKTIME_CD");
		sqlFlow.append("		AND MEDICAL_TIME_SET2.WORK_SYS_ATR = ?workSystemAtrNightShift");
		sqlFlow.append("	ORDER BY WORK_TIME_SET.WORKTIME_CD, TEMP.ROW_ID;");
		
		SELECT_WORK_TIME_FLOW = sqlFlow.toString();

		StringBuilder sqlFlex = new StringBuilder();
		sqlFlex.append("SELECT");
		// R3_61 コード
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.WORKTIME_CD, NULL),");
		// R3_62 名称
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.NAME, NULL),");
		// R3_63 略名
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.ABNAME, NULL),");
		// R3_64 記号
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.SYMBOL, NULL),");
		// R3_65 廃止
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.ABOLITION_ATR = ?isAbolish THEN ?isAbolishText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.ABOLITION_ATR = ?notAbolish THEN ?notAbolishText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END, ");
		// R3_66 勤務形態
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORK_TIME_SET.DAILY_WORK_ATR = ?flexWork THEN ?flexWorkText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_67 設定方法
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.WORKTIME_SET_METHOD, NULL),");
		// R3_68 半日勤務
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_WORK_SET.USE_HALFDAY_SHIFT = ?isTrue");
		sqlFlex.append("			THEN ?isUseHalfDayText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_WORK_SET.USE_HALFDAY_SHIFT = ?isFalse");
		sqlFlex.append("			THEN ?isNotUseHalfDayText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_69 備考
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.NOTE, NULL),");
		// R3_70 コメント
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, WORK_TIME_SET.MEMO, NULL),");
		// R3_71 所定設定.1日の始まりの時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.START_DATE_CLOCK IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(PRED_TIME_SET.START_DATE_CLOCK AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(PRED_TIME_SET.START_DATE_CLOCK AS INTEGER)%60,'0#')), NULL),");
		// R3_72 所定設定.1日の範囲時間.時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, PRED_TIME_SET.RANGE_TIME_DAY, NULL),");
		// R3_73 所定設定.1日の範囲時間.夜勤シフト
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.NIGHT_SHIFT_ATR = ?isTrue THEN ?isNightShiftText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.NIGHT_SHIFT_ATR = ?isFalse THEN ?isNotNightShiftText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_74 所定設定.就業時刻1回目.始業時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET.START_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(WORK_TIME_SHEET_SET.START_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(WORK_TIME_SHEET_SET.START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_75 所定設定.就業時刻1回目.終業時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND WORK_TIME_SHEET_SET.END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(WORK_TIME_SHEET_SET.END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(WORK_TIME_SHEET_SET.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_76 所定設定.就業時刻1回目.残業を含めた所定時間帯を設定する
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.IS_INCLUDE_OT = ?isTrue THEN ?isIncludeOtText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND PRED_TIME_SET.IS_INCLUDE_OT = ?isFalse THEN ?isNotIncludeOtText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_4 所定設定.コアタイム時間帯
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_WORK_SET.CORETIME_USE_ATR, NULL),");
		// R3_77 所定設定.コアタイム時間帯.始業時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_WORK_SET.CORE_TIME_STR IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_WORK_SET.CORE_TIME_STR AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_WORK_SET.CORE_TIME_STR AS INTEGER)%60,'0#')), NULL),");
		// R3_78 所定設定.コアタイム時間帯.終業時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_WORK_SET.CORE_TIME_END IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_WORK_SET.CORE_TIME_END AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_WORK_SET.CORE_TIME_END AS INTEGER)%60,'0#')), NULL),");
		// R3_79 所定設定.コアタイム時間帯.最低勤務時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_WORK_SET.LEAST_WORK_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_WORK_SET.LEAST_WORK_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_WORK_SET.LEAST_WORK_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_80 所定設定.半日勤務.前半終了時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.MORNING_END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(PRED_TIME_SET.MORNING_END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(PRED_TIME_SET.MORNING_END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_81 所定設定.半日勤務.後半開始時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.AFTERNOON_START_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(PRED_TIME_SET.AFTERNOON_START_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(PRED_TIME_SET.AFTERNOON_START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_82 所定設定.所定時間.1日
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.PRED_ONE_DAY IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(PRED_TIME_SET.PRED_ONE_DAY AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(PRED_TIME_SET.PRED_ONE_DAY AS INTEGER)%60,'0#')), NULL),");
		// R3_83 所定設定.所定時間.午前
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.PRED_MORNING IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(PRED_TIME_SET.PRED_MORNING AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(PRED_TIME_SET.PRED_MORNING AS INTEGER)%60,'0#')), NULL),");
		// R3_84 所定設定.所定時間.午後
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.PRED_AFTERNOON IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(PRED_TIME_SET.PRED_AFTERNOON AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(PRED_TIME_SET.PRED_AFTERNOON AS INTEGER)%60,'0#')), NULL),");
		// R3_85 所定設定.休暇取得時加算時間.午後
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.WORK_ADD_ONE_DAY IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(PRED_TIME_SET.WORK_ADD_ONE_DAY AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(PRED_TIME_SET.WORK_ADD_ONE_DAY AS INTEGER)%60,'0#')), NULL),");
		// R3_86 所定設定.休暇取得時加算時間.午後
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.WORK_ADD_MORNING IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(PRED_TIME_SET.WORK_ADD_MORNING AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(PRED_TIME_SET.WORK_ADD_MORNING AS INTEGER)%60,'0#')), NULL),");
		// R3_87 所定設定.休暇取得時加算時間.午後
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND PRED_TIME_SET.WORK_ADD_AFTERNOON IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(PRED_TIME_SET.WORK_ADD_AFTERNOON AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(PRED_TIME_SET.WORK_ADD_AFTERNOON AS INTEGER)%60,'0#')), NULL),");
		// R3_88 勤務時間帯.1日勤務用.開始時間
		sqlFlex.append("	IIF(FLEX_WORK_TIME_SET1.TIME_STR IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_WORK_TIME_SET1.TIME_STR AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_WORK_TIME_SET1.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		// R3_89 勤務時間帯.1日勤務用.終了時間
		sqlFlex.append("	IIF(FLEX_WORK_TIME_SET1.TIME_END IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_WORK_TIME_SET1.TIME_END AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_WORK_TIME_SET1.TIME_END AS INTEGER)%60,'0#')), NULL),");
		// R3_90 勤務時間帯.1日勤務用.丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN FLEX_WORK_TIME_SET1.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET1.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET1.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET1.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET1.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET1.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET1.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET1.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_91 勤務時間帯.1日勤務用.端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_WORK_TIME_SET1.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_WORK_TIME_SET1.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_92 勤務時間帯.午前勤務用.開始時間
		sqlFlex.append("	IIF(FLEX_WORK_TIME_SET2.TIME_STR IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_WORK_TIME_SET2.TIME_STR AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_WORK_TIME_SET2.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		// R3_93 勤務時間帯.午前勤務用.終了時間
		sqlFlex.append("	IIF(FLEX_WORK_TIME_SET2.TIME_END IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_WORK_TIME_SET2.TIME_END AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_WORK_TIME_SET2.TIME_END AS INTEGER)%60,'0#')), NULL),");
		// R3_94 勤務時間帯.午前勤務用.丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN FLEX_WORK_TIME_SET2.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET2.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET2.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET2.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET2.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET2.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET2.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET2.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_95 勤務時間帯.午前勤務用.端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_WORK_TIME_SET2.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_WORK_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_96 勤務時間帯.午後勤務用.開始時間
		sqlFlex.append("	IIF(FLEX_WORK_TIME_SET3.TIME_STR IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_WORK_TIME_SET3.TIME_STR AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_WORK_TIME_SET3.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		// R3_97 勤務時間帯.午後勤務用.終了時間
		sqlFlex.append("	IIF(FLEX_WORK_TIME_SET3.TIME_END IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_WORK_TIME_SET3.TIME_END AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_WORK_TIME_SET3.TIME_END AS INTEGER)%60,'0#')), NULL),");
		// R3_98 勤務時間帯.午後勤務用.丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN FLEX_WORK_TIME_SET3.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET3.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET3.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET3.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET3.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET3.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET3.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN FLEX_WORK_TIME_SET3.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_99 勤務時間帯.午後勤務用.端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_WORK_TIME_SET3.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_WORK_TIME_SET3.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_100 残業時間帯.1日勤務用.開始時間
		sqlFlex.append("	IIF(FLEX_OT_TIME_SET1.TIME_STR IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OT_TIME_SET1.TIME_STR AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OT_TIME_SET1.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		// R3_101 残業時間帯.1日勤務用.終了時間
		sqlFlex.append("	IIF(FLEX_OT_TIME_SET1.TIME_END IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OT_TIME_SET1.TIME_END AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OT_TIME_SET1.TIME_END AS INTEGER)%60,'0#')), NULL),");
		// R3_102 残業時間帯.1日勤務用.丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN FLEX_OT_TIME_SET1.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET1.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET1.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET1.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET1.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET1.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET1.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET1.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_103 残業時間帯.1日勤務用.端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_OT_TIME_SET1.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_OT_TIME_SET1.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_104 残業時間帯.1日勤務用.残業枠
		sqlFlex.append("	FLEX_OT_TIME_SET1.OT_FRAME_NO,");
		// R3_105 残業時間帯.1日勤務用.早出
		sqlFlex.append("	FLEX_OT_TIME_SET1.TREAT_EARLY_OT_WORK,");
		// R3_106 残業時間帯.午前勤務用.開始時間
		sqlFlex.append("	IIF(FLEX_OT_TIME_SET2.TIME_STR IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OT_TIME_SET2.TIME_STR AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OT_TIME_SET2.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		// R3_107 残業時間帯.午前勤務用.終了時間
		sqlFlex.append("	IIF(FLEX_OT_TIME_SET2.TIME_END IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OT_TIME_SET2.TIME_END AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OT_TIME_SET2.TIME_END AS INTEGER)%60,'0#')), NULL),");
		// R3_108 残業時間帯.午前勤務用.丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN FLEX_OT_TIME_SET2.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET2.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET2.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET2.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET2.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET2.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET2.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET2.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_109 残業時間帯.午前勤務用.端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_OT_TIME_SET2.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_OT_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_110 残業時間帯.午前勤務用.残業枠
		sqlFlex.append("	FLEX_OT_TIME_SET2.OT_FRAME_NO,");
		// R3_111 残業時間帯.午前勤務用.早出
		sqlFlex.append("	FLEX_OT_TIME_SET2.TREAT_EARLY_OT_WORK,");
		// R3_112 残業時間帯.午後勤務用.開始時間
		sqlFlex.append("	IIF(FLEX_OT_TIME_SET3.TIME_STR IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OT_TIME_SET3.TIME_STR AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OT_TIME_SET3.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		// R3_113 残業時間帯.午後勤務用.終了時間
		sqlFlex.append("	IIF(FLEX_OT_TIME_SET3.TIME_END IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OT_TIME_SET3.TIME_END AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OT_TIME_SET3.TIME_END AS INTEGER)%60,'0#')), NULL),");
		// R3_114 残業時間帯.午後勤務用.丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN FLEX_OT_TIME_SET3.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET3.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET3.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET3.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET3.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET3.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET3.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN FLEX_OT_TIME_SET3.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_115 残業時間帯.午後勤務用.端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_OT_TIME_SET3.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_OT_TIME_SET3.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_116 残業時間帯.午後勤務用.残業枠
		sqlFlex.append("	FLEX_OT_TIME_SET3.OT_FRAME_NO,");
		// R3_117 残業時間帯.午後勤務用.早出
		sqlFlex.append("	FLEX_OT_TIME_SET3.TREAT_EARLY_OT_WORK,");
		// R3_118 打刻時間帯.優先設定.出勤
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET1.PIORITY_ATR = ?beforePiority");
		sqlFlex.append("			THEN ?beforePiorityText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET1.PIORITY_ATR = ?afterPiority");
		sqlFlex.append("			THEN ?afterPiorityText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_119 打刻時間帯.優先設定.退勤
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET2.PIORITY_ATR = ?beforePiority");
		sqlFlex.append("			THEN ?beforePiorityText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET2.PIORITY_ATR = ?afterPiority");
		sqlFlex.append("			THEN ?afterPiorityText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_120 打刻時間帯.打刻丸め.出勤
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET1.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_121 打刻時間帯.打刻丸め.出勤前後設定
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET1.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlFlex.append("			THEN ?fontRearSectionBeforeText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET1.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlFlex.append("			THEN ?fontRearSectionAfterText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_122 打刻時間帯.打刻丸め.退勤
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET2.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_123 打刻時間帯.打刻丸め.退勤前後設定
		// R2_94 打刻時間帯.打刻丸め.退勤前後設定
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET2.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlFlex.append("			THEN ?fontRearSectionBeforeText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET2.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlFlex.append("			THEN ?fontRearSectionAfterText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_124 打刻詳細設定.出勤反映時間帯.開始時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_STAMP_REFLECT1.START_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_STAMP_REFLECT1.START_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_STAMP_REFLECT1.START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_125 打刻詳細設定.出勤反映時間帯.終了時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_STAMP_REFLECT1.END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_STAMP_REFLECT1.END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_STAMP_REFLECT1.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_126 打刻詳細設定.出勤反映時間帯.2勤務目の開始（1勤務目の退勤から分けられる時間）
		sqlFlex.append("		IIF(TEMP.ROW_ID = 1 AND FLEX_STAMP_REFLECT3.START_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_STAMP_REFLECT3.START_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_STAMP_REFLECT3.START_TIME AS INTEGER)%60,'0#')), NULL), ");
		//
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_STAMP_REFLECT3.END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_STAMP_REFLECT3.END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_STAMP_REFLECT3.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_127 打刻詳細設定.退勤反映時間帯.開始時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_STAMP_REFLECT2.START_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_STAMP_REFLECT2.START_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_STAMP_REFLECT2.START_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_128 打刻詳細設定.退勤反映時間帯.終了時刻
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_STAMP_REFLECT2.END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_STAMP_REFLECT2.END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_STAMP_REFLECT2.END_TIME AS INTEGER)%60,'0#')), NULL),");
		//
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_STAMP_REFLECT4.START_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_STAMP_REFLECT4.START_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_STAMP_REFLECT4.START_TIME AS INTEGER)%60,'0#')), NULL),");
		//
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_STAMP_REFLECT4.END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_STAMP_REFLECT4.END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_STAMP_REFLECT4.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_129 打刻詳細設定.優先設定.入門
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET3.PIORITY_ATR = ?beforePiority");
		sqlFlex.append("			THEN ?beforePiorityText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET3.PIORITY_ATR = ?afterPiority");
		sqlFlex.append("			THEN ?afterPiorityText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_130 打刻詳細設定.優先設定.退門
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET4.PIORITY_ATR = ?beforePiority");
		sqlFlex.append("			THEN ?beforePiorityText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET4.PIORITY_ATR = ?afterPiority");
		sqlFlex.append("			THEN ?afterPiorityText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_131 打刻詳細設定.優先設定.PCログオン
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET5.PIORITY_ATR = ?beforePiority");
		sqlFlex.append("			THEN ?beforePiorityText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET5.PIORITY_ATR = ?afterPiority");
		sqlFlex.append("			THEN ?afterPiorityText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_132 打刻詳細設定.優先設定.PCログオフ
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND PIORITY_SET6.PIORITY_ATR = ?beforePiority");
		sqlFlex.append("			THEN ?beforePiorityText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND PIORITY_SET6.PIORITY_ATR = ?afterPiority");
		sqlFlex.append("			THEN ?afterPiorityText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_133 打刻詳細設定.打刻丸め.外出
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET3.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_134 打刻詳細設定.打刻丸め.外出前後設定
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET3.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlFlex.append("			THEN ?fontRearSectionBeforeText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET3.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlFlex.append("			THEN ?fontRearSectionAfterText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_135 打刻詳細設定.打刻丸め.戻り
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("		CASE WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("			 WHEN ROUNDING_SET4.ROUNDING_TIME_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("			 ELSE NULL");
		sqlFlex.append("		END,");
		sqlFlex.append("		NULL),");
		// R3_136 打刻詳細設定.打刻丸め.戻り前後設定
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET4.FRONT_REAR_ATR = ?fontRearSectionBefore");
		sqlFlex.append("			THEN ?fontRearSectionBeforeText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND ROUNDING_SET4.FRONT_REAR_ATR = ?fontRearSectionAfter");
		sqlFlex.append("			THEN ?fontRearSectionAfterText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_137 休憩時間帯.休憩時間の固定
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET1.FIX_REST_TIME = ?isTrue THEN ?isUseText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET1.FIX_REST_TIME = ?isFalse THEN ?isNotUseText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_138 休憩時間帯.1日勤務用（固定する）.開始時間
		sqlFlex.append("	IIF(FLEX_HA_FIX_REST1.STR_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_FIX_REST1.STR_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_FIX_REST1.STR_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_139 休憩時間帯.1日勤務用（固定する）.終了時間
		sqlFlex.append("	IIF(FLEX_HA_FIX_REST1.END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_FIX_REST1.END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_FIX_REST1.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_140 休憩時間帯.午前勤務用（固定する）.開始時間
		sqlFlex.append("	IIF(FLEX_HA_FIX_REST2.STR_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_FIX_REST2.STR_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_FIX_REST2.STR_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_141 休憩時間帯.午前勤務用（固定する）.終了時間
		sqlFlex.append("	IIF(FLEX_HA_FIX_REST2.END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_FIX_REST2.END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_FIX_REST2.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_142 休憩時間帯.午後勤務用（固定する）.開始時間
		sqlFlex.append("	IIF(FLEX_HA_FIX_REST3.STR_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_FIX_REST3.STR_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_FIX_REST3.STR_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_143 休憩時間帯.午後勤務用（固定する）.終了時間
		sqlFlex.append("	IIF(FLEX_HA_FIX_REST3.END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_FIX_REST3.END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_FIX_REST3.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_144 休憩時間帯.1日勤務用（固定しない）.経過時間
		sqlFlex.append("	IIF(FLEX_HA_REST_SET1.FLOW_PASSAGE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_REST_SET1.FLOW_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_REST_SET1.FLOW_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_145 休憩時間帯.1日勤務用（固定しない）.休憩時間
		sqlFlex.append("	IIF(FLEX_HA_REST_SET1.FLOW_REST_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_REST_SET1.FLOW_REST_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_REST_SET1.FLOW_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_146 休憩時間帯.1日勤務用（固定しない）.以降は下記の時間で繰り返す
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET1.USE_REST_AFTER_SET = ?isTrue THEN ?isUseRestAfterSetText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET1.USE_REST_AFTER_SET = ?isFalse THEN ?isNotUseRestAfterSetText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_147 休憩時間帯.1日勤務用（固定しない）.経過時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET1.AFTER_PASSAGE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_RT_SET1.AFTER_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_RT_SET1.AFTER_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_148 休憩時間帯.1日勤務用（固定しない）.休憩時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET1.AFTER_REST_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_RT_SET1.AFTER_REST_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_RT_SET1.AFTER_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_149 休憩時間帯.1日勤務用（固定しない）.経過時間
		sqlFlex.append("	IIF(FLEX_HA_REST_SET2.FLOW_PASSAGE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_REST_SET2.FLOW_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_REST_SET2.FLOW_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_150 休憩時間帯.1日勤務用（固定しない）.休憩時間
		sqlFlex.append("	IIF(FLEX_HA_REST_SET2.FLOW_REST_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_REST_SET2.FLOW_REST_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_REST_SET2.FLOW_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_151 休憩時間帯.1日勤務用（固定しない）.以降は下記の時間で繰り返す
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET2.USE_REST_AFTER_SET = ?isTrue THEN ?isUseRestAfterSetText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET2.USE_REST_AFTER_SET = ?isFalse THEN ?isNotUseRestAfterSetText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_152 休憩時間帯.1日勤務用（固定しない）.経過時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET2.AFTER_PASSAGE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_RT_SET2.AFTER_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_RT_SET2.AFTER_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_153 休憩時間帯.1日勤務用（固定しない）.休憩時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET2.AFTER_REST_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_RT_SET2.AFTER_REST_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_RT_SET2.AFTER_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_154 休憩時間帯.1日勤務用（固定しない）.経過時間
		sqlFlex.append("	IIF(FLEX_HA_REST_SET3.FLOW_PASSAGE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_REST_SET3.FLOW_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_REST_SET3.FLOW_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_155 休憩時間帯.1日勤務用（固定しない）.休憩時間
		sqlFlex.append("	IIF(FLEX_HA_REST_SET3.FLOW_REST_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_REST_SET3.FLOW_REST_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_REST_SET3.FLOW_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_156 休憩時間帯.1日勤務用（固定しない）.以降は下記の時間で繰り返す
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET3.USE_REST_AFTER_SET = ?isTrue THEN ?isUseRestAfterSetText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET3.USE_REST_AFTER_SET = ?isFalse THEN ?isNotUseRestAfterSetText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_157 休憩時間帯.1日勤務用（固定しない）.経過時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET3.AFTER_PASSAGE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_RT_SET3.AFTER_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_RT_SET3.AFTER_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_158 休憩時間帯.1日勤務用（固定しない）.休憩時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_HA_RT_SET3.AFTER_REST_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HA_RT_SET3.AFTER_REST_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("						FORMAT(CAST(FLEX_HA_RT_SET3.AFTER_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_159 休憩時間帯.休憩計算設定.休憩中に退勤した場合の休憩時間の計算方法
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_REST_SET.COMMON_CALCULATE_METHOD = ?calcMethodAppropAll");
		sqlFlex.append("			THEN ?calcMethodAppropAllText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_REST_SET.COMMON_CALCULATE_METHOD = ?calcMethodNotAppropAll");
		sqlFlex.append("			THEN ?calcMethodNotAppropAllText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_REST_SET.COMMON_CALCULATE_METHOD = ?calcMethodOfficeWorkAppropAll");
		sqlFlex.append("			THEN ?calcMethodOfficeWorkAppropAllText");
		sqlFlex.append("		ELSE NULL");
		sqlFlex.append("	END,");
		// R3_160 休憩時間帯.固定休憩設定（休憩時間の固定する）
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_REST_SET.FIXED_REST_CALC_METHOD, NULL),");
		// R3_161 休憩時間帯.固定休憩設定（休憩時間の固定する）.予定と実績の勤務が一致しな場合はマスタを参照する
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_REST_SET.IS_CALC_FROM_SCHEDULE, NULL),");
		// R3_162 休憩時間帯.固定休憩設定（休憩時間の固定する）.休憩時刻が無い場合はマスタから休憩時刻を参照する
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_REST_SET.IS_REFER_REST_TIME, NULL),");
		// R3_163 休憩時間帯.固定休憩設定（休憩時間の固定する）.私用外出を休憩として扱う
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_REST_SET.USER_PRIVATE_GO_OUT_REST, NULL),");
		// R3_164 休憩時間帯.固定休憩設定（休憩時間の固定する）.組合外出を休憩として扱う
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_REST_SET.USER_ASSO_GO_OUT_REST, NULL),");
		// R3_165 休憩時間帯.流動休憩設定（休憩時間の固定しない）.実績での休憩計算方法
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_REST_SET.CALCULATE_METHOD, NULL),");
		// R3_166 休憩時間帯.流動休憩設定（休憩時間の固定しない）.外出を休憩として扱う
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_REST_SET.USE_STAMP, NULL),");
		// R3_167 休憩時間帯.流動休憩設定（休憩時間の固定しない）.外出の計上方法
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_REST_SET.USE_STAMP_CALC_METHOD, NULL),");
		// R3_168 休憩時間帯.流動休憩設定（休憩時間の固定しない）.優先設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, FLEX_REST_SET.TIME_MANAGER_SET_ATR, NULL),");
		// R3_169 休出時間帯.開始時間
		sqlFlex.append("	IIF(FLEX_HOL_SET.TIME_STR IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HOL_SET.TIME_STR AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_HOL_SET.TIME_STR AS INTEGER)%60,'0#')), NULL),");
		// R3_170 休出時間帯.終了時間
		sqlFlex.append("	IIF(FLEX_HOL_SET.TIME_END IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_HOL_SET.TIME_END AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_HOL_SET.TIME_END AS INTEGER)%60,'0#')), NULL),");
		// R3_171 休出時間帯.法定内休出枠
		sqlFlex.append("	FLEX_HOL_SET.HOL_FRAME_NO,");
		// R3_172 休出時間帯.法定外休出枠
		sqlFlex.append("	FLEX_HOL_SET.OUT_HOL_FRAME_NO,");
		// R3_173 休出時間帯.法定外休出枠（祝日）
		sqlFlex.append("	FLEX_HOL_SET.PUB_HOL_FRAME_NO,");
		// R3_174 休出時間帯.丸め
		sqlFlex.append("	CASE WHEN FLEX_HOL_SET.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN FLEX_HOL_SET.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN FLEX_HOL_SET.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN FLEX_HOL_SET.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN FLEX_HOL_SET.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN FLEX_HOL_SET.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN FLEX_HOL_SET.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN FLEX_HOL_SET.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_175 休出時間帯.端数
		sqlFlex.append("	CASE WHEN FLEX_HOL_SET.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN FLEX_HOL_SET.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_176 休出休憩.休憩時間の固定
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_OD_RT_SET.FIX_REST_TIME = ?isTrue THEN ?isUseText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_OD_RT_SET.FIX_REST_TIME = ?isFalse THEN ?isNotUseText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_177 休出休憩.固定する.開始時間
		sqlFlex.append("	IIF(FLEX_OD_FIX_REST.STR_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OD_FIX_REST.STR_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OD_FIX_REST.STR_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_178 休出休憩.固定する.終了時間
		sqlFlex.append("	IIF(FLEX_OD_FIX_REST.END_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OD_FIX_REST.END_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OD_FIX_REST.END_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_179 休出休憩.固定しない.経過時間
		sqlFlex.append("	IIF(FLEX_OD_REST_SET.FLOW_PASSAGE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OD_REST_SET.FLOW_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OD_REST_SET.FLOW_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_180 休出休憩.固定しない.休憩時間
		sqlFlex.append("	IIF(FLEX_OD_REST_SET.FLOW_REST_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OD_REST_SET.FLOW_REST_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OD_REST_SET.FLOW_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_181 休出休憩.固定しない.以降は下記の時間で繰り返す
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND FLEX_OD_RT_SET.USE_REST_AFTER_SET = ?isTrue THEN ?isUseRestAfterSetText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND FLEX_OD_RT_SET.USE_REST_AFTER_SET = ?isFalse THEN ?isNotUseRestAfterSetText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_182 休出休憩.固定しない.経過時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_OD_RT_SET.AFTER_PASSAGE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OD_RT_SET.AFTER_PASSAGE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OD_RT_SET.AFTER_PASSAGE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_183 休出休憩.固定しない.休憩時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND FLEX_OD_RT_SET.AFTER_REST_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(FLEX_OD_RT_SET.AFTER_REST_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(FLEX_OD_RT_SET.AFTER_REST_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_184 外出.外出丸め設定.同じ枠内での丸め設定
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_SAME_FRAME = ?isTrue THEN ?roudingAfterTotalText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_SAME_FRAME = ?isFalse THEN ?roudingAfterEachTimePeriodText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_185 外出.外出丸め設定.枠を跨る場合の丸め設定
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_CROSS_FRAME = ?isTrue THEN ?roudingAfterTotalText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_GO_OUT_SET.ROUNDING_CROSS_FRAME = ?isFalse THEN ?roudingAfterEachTimePeriodText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_186 外出.私用・組合外出時間.就業時間帯
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_187 外出.私用・組合外出時間.丸め設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_188 外出.私用・組合外出時間.丸め設定端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_189 外出.私用・組合外出時間.残業時間帯
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_190 外出.私用・組合外出時間.丸め設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_191 外出.私用・組合外出時間.丸め設定端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_192 外出.私用・組合外出時間.休出時間帯
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_193 外出.私用・組合外出時間.丸め設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_194 外出.私用・組合外出時間.丸め設定端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_195 外出.私用・組合外出控除時間.就業時間帯
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_196 外出.私用・組合外出控除時間.丸め設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_197 外出.私用・組合外出控除時間.丸め設定端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PERSONAL_DEDUCT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_198 外出.私用・組合外出控除時間.残業時間帯
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_199 外出.私用・組合外出控除時間.丸め設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_200 外出.私用・組合外出控除時間.丸め設定端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PERSONAL_DEDUCT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_201 外出.私用・組合外出控除時間.休出時間帯
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_202 外出.私用・組合外出控除時間.丸め設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_203 外出.私用・組合外出控除時間.丸め設定端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PERSONAL_DEDUCT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_204 外出.公用・有償外出時間.就業時間帯
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_205 外出.公用・有償外出時間.丸め設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT1.PUB_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_206 外出.公用・有償外出時間.丸め設定端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT1.PUB_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_207 外出.公用・有償外出時間.残業時間帯
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_208 外出.公用・有償外出時間.丸め設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT3.PUB_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_209 外出.公用・有償外出時間.丸め設定端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT3.PUB_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_210 外出.公用・有償外出時間.休出時間帯
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING_METHOD = ?reverseTimezoneRounding THEN ?reverseTimezoneRoundingText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING_METHOD = ?isSetRounding THEN ?isSetRoundingText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_211 外出.公用・有償外出時間.丸め設定
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN SPECIAL_ROUND_OUT2.PUB_ROUNDING_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_212 外出.公用・有償外出時間.丸め設定端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SPECIAL_ROUND_OUT2.PUB_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_213 遅刻早退.遅刻早退時間丸め.遅刻丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.RECORD_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_214 遅刻早退.遅刻早退時間丸め.遅刻端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.RECORD_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.RECORD_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_215 遅刻早退.遅刻早退時間丸め.早退丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.RECORD_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_216 遅刻早退.遅刻早退時間丸め.早退端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.RECORD_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.RECORD_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_217 遅刻早退.遅刻早退控除時間丸め.遅刻丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY1.DEDUCTION_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_218 遅刻早退.遅刻早退控除時間丸め.遅刻端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.DEDUCTION_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.DEDUCTION_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_219 遅刻早退.遅刻早退控除時間丸め.早退丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN OTHER_LATE_EARLY2.DEDUCTION_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_220 遅刻早退.遅刻早退控除時間丸め.早退端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.DEDUCTION_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.DEDUCTION_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_221 遅刻早退詳細設定.控除時間.遅刻早退時間を就業時間から控除する
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND LATE_EARLY_SET.IS_DEDUCTE_FROM_TIME = ?isTrue THEN ?isDeducteFromTimeText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND LATE_EARLY_SET.IS_DEDUCTE_FROM_TIME = ?isFalse THEN ?isNotDeducteFromTimeText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_222 遅刻早退詳細設定.遅刻早退の判定設定.時間丁度の打刻は遅刻とする
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.EXTRACT_LATE_EARLY_TIME = ?isTrue THEN ?isExtractLateEarlyTimeText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.EXTRACT_LATE_EARLY_TIME = ?isFalse THEN ?isNotExtractLateEarlyTimeText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_223 遅刻早退詳細設定.遅刻早退の判定設定.時間丁度の打刻は早退とする
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.EXTRACT_LATE_EARLY_TIME = ?isTrue THEN ?isExtractLateEarlyTimeText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.EXTRACT_LATE_EARLY_TIME = ?isFalse THEN ?isNotExtractLateEarlyTimeText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_224 遅刻早退詳細設定.猶予時間.遅刻猶予時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.GRACE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(OTHER_LATE_EARLY1.GRACE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(OTHER_LATE_EARLY1.GRACE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_225 遅刻早退詳細設定.猶予時間.遅刻猶予時間を就業時間に含める
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.INCLUDE_WORKTIME = ?isTrue THEN ?isIncludeWorktimeText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY1.INCLUDE_WORKTIME = ?isFalse THEN ?isNotIncludeWorktimeText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_226 遅刻早退詳細設定.猶予時間.早退猶予時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.GRACE_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(OTHER_LATE_EARLY2.GRACE_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(OTHER_LATE_EARLY2.GRACE_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_227 遅刻早退詳細設定.猶予時間.早退猶予時間を就業時間に含める
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.INCLUDE_WORKTIME = ?isTrue THEN ?isIncludeWorktimeText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND OTHER_LATE_EARLY2.INCLUDE_WORKTIME = ?isFalse THEN ?isNotIncludeWorktimeText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_228 加給.コード
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, WORKTIME_COMMON_SET.RAISING_SALARY_SET, NULL),");
		// R3_229 加給.名称
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, BONUS_PAY_SET.BONUS_PAY_SET_NAME, NULL),");
		// R3_230 代休.休日出勤
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.USE_ATR = ?isTrue THEN ?isUseSubstitutionText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.USE_ATR = ?isFalse THEN ?isNotUseSubstitutionText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_231 代休.休日出勤.時間区分
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.TRANFER_ATR = ?specifiedTimeSubHol THEN ?specifiedTimeSubHolText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.TRANFER_ATR = ?certainTimeExcSubHol THEN ?certainTimeExcSubHolText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_232 代休.休日出勤.１日
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.ONE_DAY_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(SUBSTITUTION_SET2.ONE_DAY_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(SUBSTITUTION_SET2.ONE_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_233 代休.休日出勤.半日
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.HALF_DAY_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(SUBSTITUTION_SET2.HALF_DAY_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(SUBSTITUTION_SET2.HALF_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_234 代休.休日出勤.一定時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET2.CERTAIN_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(SUBSTITUTION_SET2.CERTAIN_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(SUBSTITUTION_SET2.CERTAIN_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_235 代休.残業
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.USE_ATR = ?isTrue THEN ?isUseSubstitutionText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.USE_ATR = ?isFalse THEN ?isNotUseSubstitutionText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_236 代休.残業.一定時間
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.TRANFER_ATR = ?specifiedTimeSubHol THEN ?specifiedTimeSubHolText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.TRANFER_ATR = ?certainTimeExcSubHol THEN ?certainTimeExcSubHolText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_237 代休.残業.１日
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.ONE_DAY_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(SUBSTITUTION_SET1.ONE_DAY_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(SUBSTITUTION_SET1.ONE_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_238 代休.残業.半日
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.HALF_DAY_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(SUBSTITUTION_SET1.HALF_DAY_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(SUBSTITUTION_SET1.HALF_DAY_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_239 代休.残業.一定時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND SUBSTITUTION_SET1.CERTAIN_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(SUBSTITUTION_SET1.CERTAIN_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(SUBSTITUTION_SET1.CERTAIN_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_240 深夜残業.深夜時間丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN WORKTIME_COMMON_SET.LATE_NIGHT_UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_241 深夜残業.深夜時間端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.LATE_NIGHT_ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.LATE_NIGHT_ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_242 臨時.臨時丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN TEMP_WORKTIME_SET.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_243 臨時.臨時端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND TEMP_WORKTIME_SET.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND TEMP_WORKTIME_SET.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_244 育児.育児時間帯に勤務した場合の扱い
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.CHILD_CARE_WORK_USE = ?isTrue THEN ?childCareWorkUseText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.CHILD_CARE_WORK_USE = ?isFalse THEN ?childCareWorkNotUseText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_245 育児.介護時間帯に勤務した場合の扱い
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.NUR_TIMEZONE_WORK_USE = ?isTrue THEN ?nurTimezoneWorkUseText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.NUR_TIMEZONE_WORK_USE = ?isFalse THEN ?nurTimezoneWorkNotUseText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_246 医療.日勤申し送り時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET1.APP_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(MEDICAL_TIME_SET1.APP_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(MEDICAL_TIME_SET1.APP_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_247 医療.夜勤申し送り時間
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET2.APP_TIME IS NOT NULL, ");
		sqlFlex.append("		CONCAT(CAST(MEDICAL_TIME_SET2.APP_TIME AS INTEGER)/60, ':',");
		sqlFlex.append("			FORMAT(CAST(MEDICAL_TIME_SET2.APP_TIME AS INTEGER)%60,'0#')), NULL),");
		// R3_248 医療.日勤勤務時間.丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET1.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_249 医療.日勤勤務時間.端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET1.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET1.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_250 医療.夜勤勤務時間.丸め
		sqlFlex.append("	IIF(TEMP.ROW_ID = 1, ");
		sqlFlex.append("	CASE WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime1Min THEN ?roundingTime1MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime5Min THEN ?roundingTime5MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime6Min THEN ?roundingTime6MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime10Min THEN ?roundingTime10MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime15Min THEN ?roundingTime15MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime20Min THEN ?roundingTime20MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime30Min THEN ?roundingTime30MinText");
		sqlFlex.append("		 WHEN MEDICAL_TIME_SET2.UNIT = ?roundingTime60Min THEN ?roundingTime60MinText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		sqlFlex.append("	NULL),");
		// R3_251 医療.夜勤勤務時間.端数
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET2.ROUNDING = ?roundingDown THEN ?roundingDownText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND MEDICAL_TIME_SET2.ROUNDING = ?roundingUp THEN ?roundingUpText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_252 ０時跨ぎ.0時跨ぎ計算
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.OVER_DAY_CALC_SET = ?isTrue THEN ?isUseText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.OVER_DAY_CALC_SET = ?isFalse THEN ?isNotUseText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END,");
		// R3_253 その他.勤務種類が休暇の場合に就業時間を計算するか
		sqlFlex.append("	CASE WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.HD_CAL_IS_CALCULATE = ?isTrue THEN ?isUseText");
		sqlFlex.append("		 WHEN TEMP.ROW_ID = 1 AND WORKTIME_COMMON_SET.HD_CAL_IS_CALCULATE = ?isFalse THEN ?isNotUseText");
		sqlFlex.append("		 ELSE NULL");
		sqlFlex.append("	END ");
		sqlFlex.append(" FROM");
		sqlFlex.append("	(SELECT CID, WORKTIME_CD, NAME, ABNAME, SYMBOL, ABOLITION_ATR, DAILY_WORK_ATR, WORKTIME_SET_METHOD, NOTE, MEMO");
		sqlFlex.append("		FROM KSHMT_WORK_TIME_SET WORK_TIME_SET");
		sqlFlex.append("		WHERE CID = ?companyId AND DAILY_WORK_ATR = ?flexWork) WORK_TIME_SET");
		sqlFlex.append("	CROSS JOIN (VALUES(1),(2),(3),(4),(5),(6),(7),(8),(9),(10)) AS TEMP(ROW_ID)");
		sqlFlex.append("	JOIN KSHMT_WORKTIME_COMMON_SET WORKTIME_COMMON_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = WORKTIME_COMMON_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = WORKTIME_COMMON_SET.WORKTIME_CD");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_WORK_SET FLEX_WORK_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_WORK_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_WORK_SET.WORKTIME_CD");
		sqlFlex.append("	LEFT JOIN KSHMT_PRED_TIME_SET PRED_TIME_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = PRED_TIME_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = PRED_TIME_SET.WORKTIME_CD");
		sqlFlex.append("	JOIN KSHMT_WORK_TIME_SHEET_SET WORK_TIME_SHEET_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = WORK_TIME_SHEET_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = WORK_TIME_SHEET_SET.WORKTIME_CD");
		sqlFlex.append("		AND WORK_TIME_SHEET_SET.WORK_NO = ?timezoneUseOne");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HA_RT_SET FLEX_HA_RT_SET1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HA_RT_SET1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HA_RT_SET1.WORKTIME_CD");
		sqlFlex.append("		AND FLEX_HA_RT_SET1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HA_RT_SET FLEX_HA_RT_SET2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HA_RT_SET2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HA_RT_SET2.WORKTIME_CD");
		sqlFlex.append("		AND FLEX_HA_RT_SET2.AM_PM_ATR = ?amPmAtrAm");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HA_RT_SET FLEX_HA_RT_SET3");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HA_RT_SET3.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HA_RT_SET3.WORKTIME_CD");
		sqlFlex.append("		AND FLEX_HA_RT_SET3.AM_PM_ATR = ?amPmAtrPm");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_WORK_TIME_SET FLEX_WORK_TIME_SET1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_WORK_TIME_SET1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_WORK_TIME_SET1.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_WORK_TIME_SET1.TIME_FRAME_NO");
		sqlFlex.append("		AND FLEX_WORK_TIME_SET1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_WORK_TIME_SET FLEX_WORK_TIME_SET2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_WORK_TIME_SET2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_WORK_TIME_SET2.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_WORK_TIME_SET2.TIME_FRAME_NO");
		sqlFlex.append("		AND FLEX_WORK_TIME_SET2.AM_PM_ATR = ?amPmAtrAm");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_WORK_TIME_SET FLEX_WORK_TIME_SET3");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_WORK_TIME_SET3.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_WORK_TIME_SET3.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_WORK_TIME_SET3.TIME_FRAME_NO");
		sqlFlex.append("		AND FLEX_WORK_TIME_SET3.AM_PM_ATR = ?amPmAtrPm");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_OT_TIME_SET FLEX_OT_TIME_SET1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_OT_TIME_SET1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_OT_TIME_SET1.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_OT_TIME_SET1.WORKTIME_NO");
		sqlFlex.append("		AND FLEX_OT_TIME_SET1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_OT_TIME_SET FLEX_OT_TIME_SET2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_OT_TIME_SET2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_OT_TIME_SET2.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_OT_TIME_SET2.WORKTIME_NO");
		sqlFlex.append("		AND FLEX_OT_TIME_SET2.AM_PM_ATR = ?amPmAtrAm");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_OT_TIME_SET FLEX_OT_TIME_SET3");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_OT_TIME_SET3.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_OT_TIME_SET3.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_OT_TIME_SET3.WORKTIME_NO");
		sqlFlex.append("		AND FLEX_OT_TIME_SET3.AM_PM_ATR = ?amPmAtrPm");
		sqlFlex.append("	JOIN KSHMT_PIORITY_SET PIORITY_SET1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = PIORITY_SET1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET1.WORKTIME_CD");
		sqlFlex.append("		AND PIORITY_SET1.STAMP_ATR = ?stampPiorityAtrGoingWork");
		sqlFlex.append("	JOIN KSHMT_PIORITY_SET PIORITY_SET2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = PIORITY_SET2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET2.WORKTIME_CD");
		sqlFlex.append("		AND PIORITY_SET2.STAMP_ATR = ?stampPiorityAtrLeaveWork");
		sqlFlex.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET3");
		sqlFlex.append("		ON WORK_TIME_SET.CID = PIORITY_SET3.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET3.WORKTIME_CD");
		sqlFlex.append("		AND PIORITY_SET3.STAMP_ATR = ?stampPiorityAtrEntering");
		sqlFlex.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET4");
		sqlFlex.append("		ON WORK_TIME_SET.CID = PIORITY_SET4.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET4.WORKTIME_CD");
		sqlFlex.append("		AND PIORITY_SET4.STAMP_ATR = ?stampPiorityAtrExit");
		sqlFlex.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET5");
		sqlFlex.append("		ON WORK_TIME_SET.CID = PIORITY_SET5.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET5.WORKTIME_CD");
		sqlFlex.append("		AND PIORITY_SET5.STAMP_ATR = ?stampPiorityAtrPcLogin");
		sqlFlex.append("	LEFT JOIN KSHMT_PIORITY_SET PIORITY_SET6");
		sqlFlex.append("		ON WORK_TIME_SET.CID = PIORITY_SET6.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = PIORITY_SET6.WORKTIME_CD");
		sqlFlex.append("		AND PIORITY_SET6.STAMP_ATR = ?stampPiorityAtrPcLogout");
		sqlFlex.append("	JOIN KSHMT_ROUNDING_SET ROUNDING_SET1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = ROUNDING_SET1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET1.WORKTIME_CD");
		sqlFlex.append("		AND ROUNDING_SET1.ATR = ?superiorityAttendance");
		sqlFlex.append("	JOIN KSHMT_ROUNDING_SET ROUNDING_SET2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = ROUNDING_SET2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET2.WORKTIME_CD");
		sqlFlex.append("		AND ROUNDING_SET2.ATR = ?superiorityOfficeWork");
		sqlFlex.append("	LEFT JOIN KSHMT_ROUNDING_SET ROUNDING_SET3");
		sqlFlex.append("		ON WORK_TIME_SET.CID = ROUNDING_SET3.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET3.WORKTIME_CD");
		sqlFlex.append("		AND ROUNDING_SET3.ATR = ?superiorityGoOut");
		sqlFlex.append("	LEFT JOIN KSHMT_ROUNDING_SET ROUNDING_SET4");
		sqlFlex.append("		ON WORK_TIME_SET.CID = ROUNDING_SET4.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = ROUNDING_SET4.WORKTIME_CD");
		sqlFlex.append("		AND ROUNDING_SET4.ATR = ?superiorityTurnBack");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_STAMP_REFLECT FLEX_STAMP_REFLECT1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_STAMP_REFLECT1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_STAMP_REFLECT1.WORKTIME_CD");
		sqlFlex.append("		AND FLEX_STAMP_REFLECT1.WORK_NO = ?workNoOne");
		sqlFlex.append("		AND FLEX_STAMP_REFLECT1.ATR = ?goLeavingWorkAtrGoWork");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_STAMP_REFLECT FLEX_STAMP_REFLECT2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_STAMP_REFLECT2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_STAMP_REFLECT2.WORKTIME_CD");
		sqlFlex.append("		AND FLEX_STAMP_REFLECT2.WORK_NO = ?workNoOne");
		sqlFlex.append("		AND FLEX_STAMP_REFLECT2.ATR = ?goLeavingWorkAtrLeaveWork");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_STAMP_REFLECT FLEX_STAMP_REFLECT3");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_STAMP_REFLECT3.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_STAMP_REFLECT3.WORKTIME_CD");
		sqlFlex.append("		AND FLEX_STAMP_REFLECT3.WORK_NO = ?workNoTwo");
		sqlFlex.append("		AND FLEX_STAMP_REFLECT3.ATR = ?goLeavingWorkAtrGoWork");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_STAMP_REFLECT FLEX_STAMP_REFLECT4");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_STAMP_REFLECT4.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_STAMP_REFLECT4.WORKTIME_CD");
		sqlFlex.append("		AND FLEX_STAMP_REFLECT4.WORK_NO = ?workNoTwo");
		sqlFlex.append("		AND FLEX_STAMP_REFLECT4.ATR = ?goLeavingWorkAtrLeaveWork");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HA_FIX_REST FLEX_HA_FIX_REST1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HA_FIX_REST1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HA_FIX_REST1.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_HA_FIX_REST1.PERIOD_NO");
		sqlFlex.append("		AND FLEX_HA_FIX_REST1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HA_FIX_REST FLEX_HA_FIX_REST2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HA_FIX_REST2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HA_FIX_REST2.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_HA_FIX_REST2.PERIOD_NO");
		sqlFlex.append("		AND FLEX_HA_FIX_REST2.AM_PM_ATR = ?amPmAtrAm");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HA_FIX_REST FLEX_HA_FIX_REST3");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HA_FIX_REST3.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HA_FIX_REST3.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_HA_FIX_REST3.PERIOD_NO");
		sqlFlex.append("		AND FLEX_HA_FIX_REST3.AM_PM_ATR = ?amPmAtrPm");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HA_REST_SET FLEX_HA_REST_SET1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HA_REST_SET1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HA_REST_SET1.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_HA_REST_SET1.PERIOD_NO");
		sqlFlex.append("		AND FLEX_HA_REST_SET1.AM_PM_ATR = ?amPmAtrOneDay");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HA_REST_SET FLEX_HA_REST_SET2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HA_REST_SET2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HA_REST_SET2.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_HA_REST_SET2.PERIOD_NO");
		sqlFlex.append("		AND FLEX_HA_REST_SET2.AM_PM_ATR = ?amPmAtrAm");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HA_REST_SET FLEX_HA_REST_SET3");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HA_REST_SET3.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HA_REST_SET3.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_HA_REST_SET3.PERIOD_NO");
		sqlFlex.append("		AND FLEX_HA_REST_SET3.AM_PM_ATR = ?amPmAtrPm");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_REST_SET FLEX_REST_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_REST_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_REST_SET.WORKTIME_CD");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_OD_RT_SET FLEX_OD_RT_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_OD_RT_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_OD_RT_SET.WORKTIME_CD");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_HOL_SET FLEX_HOL_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_HOL_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_HOL_SET.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_HOL_SET.WORKTIME_NO");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_OD_FIX_REST FLEX_OD_FIX_REST");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_OD_FIX_REST.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_OD_FIX_REST.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_OD_FIX_REST.PERIOD_NO");
		sqlFlex.append("	LEFT JOIN KSHMT_FLEX_OD_REST_SET FLEX_OD_REST_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = FLEX_OD_REST_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = FLEX_OD_REST_SET.WORKTIME_CD");
		sqlFlex.append("		AND TEMP.ROW_ID = FLEX_OD_REST_SET.PERIOD_NO");
		sqlFlex.append("	JOIN KSHMT_WORKTIME_GO_OUT_SET WORKTIME_GO_OUT_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = WORKTIME_GO_OUT_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = WORKTIME_GO_OUT_SET.WORKTIME_CD");
		sqlFlex.append("	JOIN KSHMT_SPECIAL_ROUND_OUT SPECIAL_ROUND_OUT1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = SPECIAL_ROUND_OUT1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = SPECIAL_ROUND_OUT1.WORKTIME_CD");
		sqlFlex.append("		AND SPECIAL_ROUND_OUT1.ROUNDING_TIME_TYPE = ?roundingTimeTypeWorkTimezone");
		sqlFlex.append("	JOIN KSHMT_SPECIAL_ROUND_OUT SPECIAL_ROUND_OUT2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = SPECIAL_ROUND_OUT2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = SPECIAL_ROUND_OUT2.WORKTIME_CD");
		sqlFlex.append("		AND SPECIAL_ROUND_OUT2.ROUNDING_TIME_TYPE = ?roundingTimeTypePubHolWorkTimezone");
		sqlFlex.append("	JOIN KSHMT_SPECIAL_ROUND_OUT SPECIAL_ROUND_OUT3");
		sqlFlex.append("		ON WORK_TIME_SET.CID = SPECIAL_ROUND_OUT3.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = SPECIAL_ROUND_OUT3.WORKTIME_CD");
		sqlFlex.append("		AND SPECIAL_ROUND_OUT3.ROUNDING_TIME_TYPE = ?roundingTimeTypeOtTimezone");
		sqlFlex.append("	JOIN KSHMT_OTHER_LATE_EARLY OTHER_LATE_EARLY1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = OTHER_LATE_EARLY1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = OTHER_LATE_EARLY1.WORKTIME_CD");
		sqlFlex.append("		AND OTHER_LATE_EARLY1.LATE_EARLY_ATR = ?lateEarlyAtrLate");
		sqlFlex.append("	JOIN KSHMT_OTHER_LATE_EARLY OTHER_LATE_EARLY2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = OTHER_LATE_EARLY2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = OTHER_LATE_EARLY2.WORKTIME_CD");
		sqlFlex.append("		AND OTHER_LATE_EARLY2.LATE_EARLY_ATR = ?lateEarlyAtrEarly");
		sqlFlex.append("	JOIN KSHMT_LATE_EARLY_SET LATE_EARLY_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = LATE_EARLY_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = LATE_EARLY_SET.WORKTIME_CD");
		sqlFlex.append("	LEFT JOIN KBPMT_BONUS_PAY_SET BONUS_PAY_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = BONUS_PAY_SET.CID");
		sqlFlex.append("		AND WORKTIME_COMMON_SET.RAISING_SALARY_SET = BONUS_PAY_SET.BONUS_PAY_SET_CD");
		sqlFlex.append("	JOIN KSHMT_SUBSTITUTION_SET SUBSTITUTION_SET1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = SUBSTITUTION_SET1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = SUBSTITUTION_SET1.WORKTIME_CD");
		sqlFlex.append("		AND SUBSTITUTION_SET1.ORIGIN_ATR = ?fromOverTime");
		sqlFlex.append("	JOIN KSHMT_SUBSTITUTION_SET SUBSTITUTION_SET2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = SUBSTITUTION_SET2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = SUBSTITUTION_SET2.WORKTIME_CD");
		sqlFlex.append("		AND SUBSTITUTION_SET2.ORIGIN_ATR = ?workDayOffTime");
		sqlFlex.append("	JOIN KSHMT_TEMP_WORKTIME_SET TEMP_WORKTIME_SET");
		sqlFlex.append("		ON WORK_TIME_SET.CID = TEMP_WORKTIME_SET.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = TEMP_WORKTIME_SET.WORKTIME_CD");
		sqlFlex.append("	JOIN KSHMT_MEDICAL_TIME_SET MEDICAL_TIME_SET1");
		sqlFlex.append("		ON WORK_TIME_SET.CID = MEDICAL_TIME_SET1.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = MEDICAL_TIME_SET1.WORKTIME_CD");
		sqlFlex.append("		AND MEDICAL_TIME_SET1.WORK_SYS_ATR = ?workSystemAtrDayShift");
		sqlFlex.append("	JOIN KSHMT_MEDICAL_TIME_SET MEDICAL_TIME_SET2");
		sqlFlex.append("		ON WORK_TIME_SET.CID = MEDICAL_TIME_SET2.CID");
		sqlFlex.append("		AND WORK_TIME_SET.WORKTIME_CD = MEDICAL_TIME_SET2.WORKTIME_CD");
		sqlFlex.append("		AND MEDICAL_TIME_SET2.WORK_SYS_ATR = ?workSystemAtrNightShift");
		sqlFlex.append("	ORDER BY WORK_TIME_SET.WORKTIME_CD, TEMP.ROW_ID;");

		SELECT_WORK_TIME_FLEX = sqlFlex.toString();
	}
	@Override
	public List<Object[]> findWorkTimeNormal(String companyId) {
		Query queryString = getEntityManager().createNativeQuery(SELECT_WORK_TIME_NORMAL)
				.setParameter("companyId", companyId)
				.setParameter("isAbolish", AbolishAtr.ABOLISH.value)
				.setParameter("notAbolish", AbolishAtr.NOT_ABOLISH.value)
				.setParameter("isAbolishText", "✓")
				.setParameter("notAbolishText", "　")
				.setParameter("regularWork", WorkTimeDailyAtr.REGULAR_WORK.value)
				.setParameter("fixedWork", WorkTimeMethodSet.FIXED_WORK.value)
				.setParameter("difftimeWork", WorkTimeMethodSet.DIFFTIME_WORK.value)
				.setParameter("regularWorkText", TextResource.localize("Enum_WorkTimeDailyAtr_Regular_Work"))
				.setParameter("fixedWorkText", TextResource.localize("Enum_WorkTimeMethodSet_Fixed_Work"))
				.setParameter("difftimeWorkText", TextResource.localize("Enum_WorkTimeMethodSet_DiffTime_Work"))
				.setParameter("isTrue", BooleanGetAtr.USE_ATR)
				.setParameter("isFalse", BooleanGetAtr.USE_NOT)
				.setParameter("isUseHalfDayText", TextResource.localize("KMK003_49"))
				.setParameter("isNotUseHalfDayText", TextResource.localize("KMK003_50"))
				.setParameter("detailMode", DisplayMode.DETAIL.value)
				.setParameter("simpleMode", DisplayMode.SIMPLE.value)
				.setParameter("detailModeText", TextResource.localize("KMK003_190"))
				.setParameter("simpleModeText", TextResource.localize("KMK003_191"))
				.setParameter("isNightShiftText", "○")
				.setParameter("isNotNightShiftText", "-")
				.setParameter("isIncludeOtText", "○")
				.setParameter("isNotIncludeOtText", "-")
				.setParameter("isUseSecondWorkingDayText", "○")
				.setParameter("isNotUseSecondWorkingDayText", "-")
				.setParameter("roundingTime1Min", Unit.ROUNDING_TIME_1MIN.value)
				.setParameter("roundingTime5Min", Unit.ROUNDING_TIME_5MIN.value)
				.setParameter("roundingTime6Min", Unit.ROUNDING_TIME_6MIN.value)
				.setParameter("roundingTime10Min", Unit.ROUNDING_TIME_10MIN.value)
				.setParameter("roundingTime15Min", Unit.ROUNDING_TIME_15MIN.value)
				.setParameter("roundingTime20Min", Unit.ROUNDING_TIME_20MIN.value)
				.setParameter("roundingTime30Min", Unit.ROUNDING_TIME_30MIN.value)
				.setParameter("roundingTime60Min", Unit.ROUNDING_TIME_60MIN.value)
				.setParameter("roundingTime1MinText", TextResource.localize("Enum_RoundingTime_1Min"))
				.setParameter("roundingTime5MinText", TextResource.localize("Enum_RoundingTime_5Min"))
				.setParameter("roundingTime6MinText", TextResource.localize("Enum_RoundingTime_6Min"))
				.setParameter("roundingTime10MinText", TextResource.localize("Enum_RoundingTime_10Min"))
				.setParameter("roundingTime15MinText", TextResource.localize("Enum_RoundingTime_15Min"))
				.setParameter("roundingTime20MinText", TextResource.localize("Enum_RoundingTime_20Min"))
				.setParameter("roundingTime30MinText", TextResource.localize("Enum_RoundingTime_30Min"))
				.setParameter("roundingTime60MinText", TextResource.localize("Enum_RoundingTime_60Min"))
				.setParameter("roundingDown", Rounding.ROUNDING_DOWN.value)
				.setParameter("roundingUp", Rounding.ROUNDING_UP.value)
				.setParameter("roundingDownText", TextResource.localize("Enum_Rounding_Down"))
				.setParameter("roundingUpText", TextResource.localize("Enum_Rounding_Up"))
				.setParameter("isLegalOtSet", LegalOTSetting.LEGAL_INTERNAL_TIME.value)
				.setParameter("isNotLegalOtSet", LegalOTSetting.OUTSIDE_LEGAL_TIME.value)
				.setParameter("isUseText", TextResource.localize("KMK003_142"))
				.setParameter("isNotUseText", TextResource.localize("KMK003_143"))
				.setParameter("beforePiority", MultiStampTimePiorityAtr.BEFORE_PIORITY.value)
				.setParameter("afterPiority", MultiStampTimePiorityAtr.AFTER_PIORITY.value)
				.setParameter("beforePiorityText", TextResource.localize("Enum_MultiStampTimePiorityAtr_beforePiority"))
				.setParameter("afterPiorityText", TextResource.localize("Enum_MultiStampTimePiorityAtr_afterPiority"))
				.setParameter("fontRearSectionBefore", FontRearSection.BEFORE.value)
				.setParameter("fontRearSectionAfter", FontRearSection.AFTER.value)
				.setParameter("fontRearSectionBeforeText", TextResource.localize("Enum_FontRearSection_before"))
				.setParameter("fontRearSectionAfterText", TextResource.localize("Enum_FontRearSection_after"))
				.setParameter("calculateMethodMasterRef", FixedRestCalculateMethod.MASTER_REF.value)
				.setParameter("calculateMethodPlanRef", FixedRestCalculateMethod.PLAN_REF.value)
				.setParameter("calculateMethodMasterRefText", TextResource.localize("KMK003_239"))
				.setParameter("calculateMethodPlanRefText", TextResource.localize("KMK003_240"))
				.setParameter("calcMethodAppropAll", RestTimeOfficeWorkCalcMethod.APPROP_ALL.value)
				.setParameter("calcMethodNotAppropAll", RestTimeOfficeWorkCalcMethod.NOT_APPROP_ALL.value)
				.setParameter("calcMethodOfficeWorkAppropAll", RestTimeOfficeWorkCalcMethod.OFFICE_WORK_APPROP_ALL.value)
				.setParameter("calcMethodAppropAllText", TextResource.localize("KMK003_235"))
				.setParameter("calcMethodNotAppropAllText", TextResource.localize("KMK003_236"))
				.setParameter("calcMethodOfficeWorkAppropAllText", TextResource.localize("KMK003_237"))
				.setParameter("roudingAfterTotalText", TextResource.localize("KMK003_198"))
				.setParameter("roudingAfterEachTimePeriodText", TextResource.localize("KMK003_199"))
				.setParameter("reverseTimezoneRounding", RoundingGoOutTimeSheet.REVERSE_ROUNDING_EACH_TIMEZONE.value)
				.setParameter("isSetRounding", RoundingGoOutTimeSheet.INDIVIDUAL_ROUNDING.value)
				.setParameter("reverseTimezoneRoundingText", TextResource.localize("KMK003_87"))
				.setParameter("isSetRoundingText", TextResource.localize("KMK003_88"))
				.setParameter("isDeducteFromTimeText", "○")
				.setParameter("isNotDeducteFromTimeText", "-")
				.setParameter("isExtractLateEarlyTimeText", "○")
				.setParameter("isNotExtractLateEarlyTimeText", "-")
				.setParameter("isIncludeWorktimeText", "○")
				.setParameter("isNotIncludeWorktimeText", "-")
				.setParameter("isUseSubstitutionText", "○")
				.setParameter("isNotUseSubstitutionText", "-")
				.setParameter("specifiedTimeSubHol", SubHolTransferSetAtr.SPECIFIED_TIME_SUB_HOL.value)
				.setParameter("certainTimeExcSubHol", SubHolTransferSetAtr.CERTAIN_TIME_EXC_SUB_HOL.value)
				.setParameter("specifiedTimeSubHolText", TextResource.localize("KMK003_106"))
				.setParameter("certainTimeExcSubHolText", TextResource.localize("KMK003_108"))
				.setParameter("childCareWorkUseText", TextResource.localize("KMK003_116"))
				.setParameter("childCareWorkNotUseText", TextResource.localize("KMK003_117"))
				.setParameter("nurTimezoneWorkUseText", TextResource.localize("KMK003_196"))
				.setParameter("nurTimezoneWorkNotUseText", TextResource.localize("KMK003_197"))
				.setParameter("calcMethodExceededPredAddVacationWorking", CalcMethodExceededPredAddVacation.CALC_AS_WORKING.value)
				.setParameter("calcMethodExceededPredAddVacationOvertime", CalcMethodExceededPredAddVacation.CALC_AS_OVERTIME.value)
				.setParameter("calcMethodNoBreakWorking", CalcMethodNoBreak.CALC_AS_WORKING.value)
				.setParameter("calcMethodNoBreakOvertime", CalcMethodNoBreak.CALC_AS_OVERTIME.value)
				.setParameter("timezoneUseOne", TimezoneUse.SHIFT_ONE)
				.setParameter("timezoneUseTwo", TimezoneUse.SHIFT_TWO)
				.setParameter("amPmAtrOneDay", AmPmAtr.ONE_DAY.value)
				.setParameter("amPmAtrAm", AmPmAtr.AM.value)
				.setParameter("amPmAtrPm", AmPmAtr.PM.value)
				.setParameter("stampPiorityAtrGoingWork", StampPiorityAtr.GOING_WORK.value)
				.setParameter("stampPiorityAtrLeaveWork", StampPiorityAtr.LEAVE_WORK.value)
				.setParameter("stampPiorityAtrEntering", StampPiorityAtr.ENTERING.value)
				.setParameter("stampPiorityAtrExit", StampPiorityAtr.EXIT.value)
				.setParameter("stampPiorityAtrPcLogin", StampPiorityAtr.PCLOGIN.value)
				.setParameter("stampPiorityAtrPcLogout", StampPiorityAtr.PC_LOGOUT.value)
				.setParameter("superiorityAttendance", Superiority.ATTENDANCE.value)
				.setParameter("superiorityOfficeWork", Superiority.OFFICE_WORK.value)
				.setParameter("superiorityGoOut", Superiority.GO_OUT.value)
				.setParameter("superiorityTurnBack", Superiority.TURN_BACK.value)
				.setParameter("goLeavingWorkAtrGoWork", GoLeavingWorkAtr.GO_WORK.value)
				.setParameter("goLeavingWorkAtrLeaveWork", GoLeavingWorkAtr.LEAVING_WORK.value)
				.setParameter("roundingTimeTypeWorkTimezone", RoundingTimeType.WORK_TIMEZONE.value)
				.setParameter("roundingTimeTypePubHolWorkTimezone", RoundingTimeType.PUB_HOL_WORK_TIMEZONE.value)
				.setParameter("roundingTimeTypeOtTimezone", RoundingTimeType.OT_TIMEZONE.value)
				.setParameter("lateEarlyAtrLate", LateEarlyAtr.LATE.value)
				.setParameter("lateEarlyAtrEarly", LateEarlyAtr.EARLY.value)
				.setParameter("fromOverTime", CompensatoryOccurrenceDivision.FromOverTime.value)
				.setParameter("workDayOffTime", CompensatoryOccurrenceDivision.WorkDayOffTime.value)
				.setParameter("workSystemAtrDayShift", WorkSystemAtr.DAY_SHIFT.value)
				.setParameter("workSystemAtrNightShift", WorkSystemAtr.NIGHT_SHIFT.value)
				.setParameter("workNoOne", 1)
				.setParameter("workNoTwo", 2);
		return queryString.getResultList();
	}

	@Override
	public List<Object[]> findWorkTimeFlow(String companyId) {
		Query queryString = getEntityManager().createNativeQuery(SELECT_WORK_TIME_FLOW)
				.setParameter("companyId", companyId)
				.setParameter("isAbolish", AbolishAtr.ABOLISH.value)
				.setParameter("notAbolish", AbolishAtr.NOT_ABOLISH.value)
				.setParameter("isAbolishText", "✓")
				.setParameter("notAbolishText", "　")
				.setParameter("regularWork", WorkTimeDailyAtr.REGULAR_WORK.value)
				.setParameter("flowWork", WorkTimeMethodSet.FLOW_WORK.value)
				.setParameter("regularWorkText", TextResource.localize("Enum_WorkTimeDailyAtr_Regular_Work"))
				.setParameter("flowWorkText", TextResource.localize("Enum_WorkTimeMethodSet_Flow_Work"))
				.setParameter("detailMode", DisplayMode.DETAIL.value)
				.setParameter("simpleMode", DisplayMode.SIMPLE.value)
				.setParameter("detailModeText", TextResource.localize("KMK003_190"))
				.setParameter("simpleModeText", TextResource.localize("KMK003_191"))
				.setParameter("isTrue", BooleanGetAtr.USE_ATR)
				.setParameter("isFalse", BooleanGetAtr.USE_NOT)
				.setParameter("isNightShiftText", "○")
				.setParameter("isNotNightShiftText", "-")			
				.setParameter("isIncludeOtText", "○")
				.setParameter("isNotIncludeOtText", "-")		
				.setParameter("isUseSecondWorkingDayText", "○")
				.setParameter("isNotUseSecondWorkingDayText", "-")			
				.setParameter("roundingTime1Min", Unit.ROUNDING_TIME_1MIN.value)
				.setParameter("roundingTime5Min", Unit.ROUNDING_TIME_5MIN.value)
				.setParameter("roundingTime6Min", Unit.ROUNDING_TIME_6MIN.value)
				.setParameter("roundingTime10Min", Unit.ROUNDING_TIME_10MIN.value)
				.setParameter("roundingTime15Min", Unit.ROUNDING_TIME_15MIN.value)
				.setParameter("roundingTime20Min", Unit.ROUNDING_TIME_20MIN.value)
				.setParameter("roundingTime30Min", Unit.ROUNDING_TIME_30MIN.value)
				.setParameter("roundingTime60Min", Unit.ROUNDING_TIME_60MIN.value)
				.setParameter("roundingTime1MinText", TextResource.localize("Enum_RoundingTime_1Min"))
				.setParameter("roundingTime5MinText", TextResource.localize("Enum_RoundingTime_5Min"))
				.setParameter("roundingTime6MinText", TextResource.localize("Enum_RoundingTime_6Min"))
				.setParameter("roundingTime10MinText", TextResource.localize("Enum_RoundingTime_10Min"))
				.setParameter("roundingTime15MinText", TextResource.localize("Enum_RoundingTime_15Min"))
				.setParameter("roundingTime20MinText", TextResource.localize("Enum_RoundingTime_20Min"))
				.setParameter("roundingTime30MinText", TextResource.localize("Enum_RoundingTime_30Min"))
				.setParameter("roundingTime60MinText", TextResource.localize("Enum_RoundingTime_60Min"))	
				.setParameter("roundingDown", Rounding.ROUNDING_DOWN.value)
				.setParameter("roundingUp", Rounding.ROUNDING_UP.value)	
				.setParameter("roundingDownText", TextResource.localize("Enum_Rounding_Down"))
				.setParameter("roundingUpText", TextResource.localize("Enum_Rounding_Up"))
				.setParameter("calcFromPlanStartTime", PrePlanWorkTimeCalcMethod.CALC_FROM_PLAN_START_TIME.value)
				.setParameter("calcFromWorkTime", PrePlanWorkTimeCalcMethod.CALC_FROM_WORK_TIME.value)
				.setParameter("calcFromPlanStartTimeText", TextResource.localize("KMK003_136"))
				.setParameter("calcFromWorkTimeText", TextResource.localize("KMK003_137"))
				.setParameter("fixedChangeAtrNotChange", FixedChangeAtr.NOT_CHANGE.value)
				.setParameter("fixedChangeAtrAfterShift", FixedChangeAtr.AFTER_SHIFT.value)
				.setParameter("fixedChangeAtrBeforeAfterShift", FixedChangeAtr.BEFORE_AFTER_SHIFT.value)
				.setParameter("fixedChangeAtrNotChangeText", TextResource.localize("Enum_FixedChangeAtr_notChange"))
				.setParameter("fixedChangeAtrAfterShiftText", TextResource.localize("Enum_FixedChangeAtr_afterShift"))
				.setParameter("fixedChangeAtrBeforeAfterShiftText", TextResource.localize("Enum_FixedChangeAtr_beforeAfterShift"))
				.setParameter("isUseText", TextResource.localize("KMK003_142"))
				.setParameter("isNotUseText", TextResource.localize("KMK003_143"))
				.setParameter("isUseRestAfterSetText", "○")
				.setParameter("isNotUseRestAfterSetText", "-")
				.setParameter("beforePiority", MultiStampTimePiorityAtr.BEFORE_PIORITY.value)
				.setParameter("afterPiority", MultiStampTimePiorityAtr.AFTER_PIORITY.value)
				.setParameter("beforePiorityText", TextResource.localize("Enum_MultiStampTimePiorityAtr_beforePiority"))
				.setParameter("afterPiorityText", TextResource.localize("Enum_MultiStampTimePiorityAtr_afterPiority"))
				.setParameter("fontRearSectionBefore", FontRearSection.BEFORE.value)
				.setParameter("fontRearSectionAfter", FontRearSection.AFTER.value)
				.setParameter("fontRearSectionBeforeText", TextResource.localize("Enum_FontRearSection_before"))
				.setParameter("fontRearSectionAfterText", TextResource.localize("Enum_FontRearSection_after"))
				.setParameter("calcMethodAppropAll", RestTimeOfficeWorkCalcMethod.APPROP_ALL.value)
				.setParameter("calcMethodNotAppropAll", RestTimeOfficeWorkCalcMethod.NOT_APPROP_ALL.value)
				.setParameter("calcMethodOfficeWorkAppropAll",RestTimeOfficeWorkCalcMethod.OFFICE_WORK_APPROP_ALL.value)
				.setParameter("calcMethodAppropAllText", TextResource.localize("KMK003_235"))
				.setParameter("calcMethodNotAppropAllText", TextResource.localize("KMK003_236"))
				.setParameter("calcMethodOfficeWorkAppropAllText", TextResource.localize("KMK003_237"))
				.setParameter("isUsePluralWorkRestTimeText", TextResource.localize("KMK003_113"))
				.setParameter("isNotUsePluralWorkRestTimeText", TextResource.localize("KMK003_114"))
				.setParameter("reverseTimezoneRounding", RoundingGoOutTimeSheet.REVERSE_ROUNDING_EACH_TIMEZONE.value)
				.setParameter("isSetRounding", RoundingGoOutTimeSheet.INDIVIDUAL_ROUNDING.value)
				.setParameter("reverseTimezoneRoundingText", TextResource.localize("KMK003_87"))
				.setParameter("isSetRoundingText", TextResource.localize("KMK003_88"))
				.setParameter("roudingAfterTotalText", TextResource.localize("KMK003_198"))
				.setParameter("roudingAfterEachTimePeriodText", TextResource.localize("KMK003_199"))
				.setParameter("isExtractLateEarlyTimeText", "○")
				.setParameter("isNotExtractLateEarlyTimeText", "-")
				.setParameter("isIncludeWorktimeText", "○")
				.setParameter("isNotIncludeWorktimeText", "-")
				.setParameter("isUseSubstitutionText", "○")
				.setParameter("isNotUseSubstitutionText", "-")
				.setParameter("specifiedTimeSubHol", SubHolTransferSetAtr.SPECIFIED_TIME_SUB_HOL.value)
				.setParameter("certainTimeExcSubHol", SubHolTransferSetAtr.CERTAIN_TIME_EXC_SUB_HOL.value)
				.setParameter("specifiedTimeSubHolText", TextResource.localize("KMK003_106"))
				.setParameter("certainTimeExcSubHolText", TextResource.localize("KMK003_108"))
				.setParameter("childCareWorkUseText", TextResource.localize("KMK003_116"))
				.setParameter("childCareWorkNotUseText", TextResource.localize("KMK003_117"))
				.setParameter("nurTimezoneWorkUseText", TextResource.localize("KMK003_196"))
				.setParameter("nurTimezoneWorkNotUseText", TextResource.localize("KMK003_197"))
				.setParameter("timezoneUseOne", TimezoneUse.SHIFT_ONE)
				.setParameter("timezoneUseTwo", TimezoneUse.SHIFT_TWO)
				.setParameter("stampPiorityAtrGoingWork", StampPiorityAtr.GOING_WORK.value)
				.setParameter("stampPiorityAtrLeaveWork", StampPiorityAtr.LEAVE_WORK.value)
				.setParameter("stampPiorityAtrEntering", StampPiorityAtr.ENTERING.value)
				.setParameter("stampPiorityAtrExit", StampPiorityAtr.EXIT.value)
				.setParameter("stampPiorityAtrPcLogin", StampPiorityAtr.PCLOGIN.value)
				.setParameter("stampPiorityAtrPcLogout", StampPiorityAtr.PC_LOGOUT.value)
				.setParameter("superiorityAttendance", Superiority.ATTENDANCE.value)
				.setParameter("superiorityOfficeWork", Superiority.OFFICE_WORK.value)
				.setParameter("superiorityGoOut", Superiority.GO_OUT.value)
				.setParameter("superiorityTurnBack", Superiority.TURN_BACK.value)
				.setParameter("goLeavingWorkAtrGoWork", GoLeavingWorkAtr.GO_WORK.value)
				.setParameter("goLeavingWorkAtrLeaveWork", GoLeavingWorkAtr.LEAVING_WORK.value)
				.setParameter("roundingTimeTypeWorkTimezone", RoundingTimeType.WORK_TIMEZONE.value)
				.setParameter("roundingTimeTypePubHolWorkTimezone", RoundingTimeType.PUB_HOL_WORK_TIMEZONE.value)
				.setParameter("roundingTimeTypeOtTimezone", RoundingTimeType.OT_TIMEZONE.value)
				.setParameter("lateEarlyAtrLate", LateEarlyAtr.LATE.value)
				.setParameter("lateEarlyAtrEarly", LateEarlyAtr.EARLY.value)
				.setParameter("fromOverTime", CompensatoryOccurrenceDivision.FromOverTime.value)
				.setParameter("workDayOffTime", CompensatoryOccurrenceDivision.WorkDayOffTime.value)
				.setParameter("workSystemAtrDayShift", WorkSystemAtr.DAY_SHIFT.value)
				.setParameter("workSystemAtrNightShift", WorkSystemAtr.NIGHT_SHIFT.value)
				.setParameter("resttimeAtrOffDay", ResttimeAtr.OFF_DAY.value)
				.setParameter("resttimeAtrHalfDay", ResttimeAtr.HALF_DAY.value)
				.setParameter("workNoOne", 1);
		return queryString.getResultList();
	}

	@Override
	public List<Object[]> findWorkTimeFlex(String companyId) {
		Query queryString = getEntityManager().createNativeQuery(SELECT_WORK_TIME_FLEX)
				.setParameter("companyId", companyId)
				.setParameter("isAbolish", AbolishAtr.ABOLISH.value)
				.setParameter("notAbolish", AbolishAtr.NOT_ABOLISH.value)
				.setParameter("isAbolishText", "✓")
				.setParameter("notAbolishText", "　")
				.setParameter("flexWork", WorkTimeDailyAtr.FLEX_WORK.value)
				.setParameter("flexWorkText", TextResource.localize("Enum_WorkTimeDailyAtr_Flex_Work"))
				.setParameter("isTrue", BooleanGetAtr.USE_ATR)
				.setParameter("isFalse", BooleanGetAtr.USE_NOT)
				.setParameter("isUseHalfDayText", TextResource.localize("KMK003_49"))
				.setParameter("isNotUseHalfDayText", TextResource.localize("KMK003_50"))
				.setParameter("isNightShiftText", "○")
				.setParameter("isNotNightShiftText", "-")			
				.setParameter("isIncludeOtText", "○")
				.setParameter("isNotIncludeOtText", "-")			
				.setParameter("isDeducteFromTimeText", "○")
				.setParameter("isNotDeducteFromTimeText", "-")
				.setParameter("roundingTime1Min", Unit.ROUNDING_TIME_1MIN.value)
				.setParameter("roundingTime5Min", Unit.ROUNDING_TIME_5MIN.value)
				.setParameter("roundingTime6Min", Unit.ROUNDING_TIME_6MIN.value)
				.setParameter("roundingTime10Min", Unit.ROUNDING_TIME_10MIN.value)
				.setParameter("roundingTime15Min", Unit.ROUNDING_TIME_15MIN.value)
				.setParameter("roundingTime20Min", Unit.ROUNDING_TIME_20MIN.value)
				.setParameter("roundingTime30Min", Unit.ROUNDING_TIME_30MIN.value)
				.setParameter("roundingTime60Min", Unit.ROUNDING_TIME_60MIN.value)
				.setParameter("roundingTime1MinText", TextResource.localize("Enum_RoundingTime_1Min"))
				.setParameter("roundingTime5MinText", TextResource.localize("Enum_RoundingTime_5Min"))
				.setParameter("roundingTime6MinText", TextResource.localize("Enum_RoundingTime_6Min"))
				.setParameter("roundingTime10MinText", TextResource.localize("Enum_RoundingTime_10Min"))
				.setParameter("roundingTime15MinText", TextResource.localize("Enum_RoundingTime_15Min"))
				.setParameter("roundingTime20MinText", TextResource.localize("Enum_RoundingTime_20Min"))
				.setParameter("roundingTime30MinText", TextResource.localize("Enum_RoundingTime_30Min"))
				.setParameter("roundingTime60MinText", TextResource.localize("Enum_RoundingTime_60Min"))	
				.setParameter("roundingDown", Rounding.ROUNDING_DOWN.value)
				.setParameter("roundingUp", Rounding.ROUNDING_UP.value)	
				.setParameter("roundingDownText", TextResource.localize("Enum_Rounding_Down"))
				.setParameter("roundingUpText", TextResource.localize("Enum_Rounding_Up"))
				.setParameter("isUseText", TextResource.localize("KMK003_142"))
				.setParameter("isNotUseText", TextResource.localize("KMK003_143"))
				.setParameter("isUseRestAfterSetText", "○")
				.setParameter("isNotUseRestAfterSetText", "-")
				.setParameter("beforePiority", MultiStampTimePiorityAtr.BEFORE_PIORITY.value)
				.setParameter("afterPiority", MultiStampTimePiorityAtr.AFTER_PIORITY.value)
				.setParameter("beforePiorityText", TextResource.localize("Enum_MultiStampTimePiorityAtr_beforePiority"))
				.setParameter("afterPiorityText", TextResource.localize("Enum_MultiStampTimePiorityAtr_afterPiority"))
				.setParameter("fontRearSectionBefore", FontRearSection.BEFORE.value)
				.setParameter("fontRearSectionAfter", FontRearSection.AFTER.value)
				.setParameter("fontRearSectionBeforeText", TextResource.localize("Enum_FontRearSection_before"))
				.setParameter("fontRearSectionAfterText", TextResource.localize("Enum_FontRearSection_after"))
				.setParameter("calcMethodAppropAll", RestTimeOfficeWorkCalcMethod.APPROP_ALL.value)
				.setParameter("calcMethodNotAppropAll", RestTimeOfficeWorkCalcMethod.NOT_APPROP_ALL.value)
				.setParameter("calcMethodOfficeWorkAppropAll",RestTimeOfficeWorkCalcMethod.OFFICE_WORK_APPROP_ALL.value)
				.setParameter("calcMethodAppropAllText", TextResource.localize("KMK003_235"))
				.setParameter("calcMethodNotAppropAllText", TextResource.localize("KMK003_236"))
				.setParameter("calcMethodOfficeWorkAppropAllText", TextResource.localize("KMK003_237"))
				.setParameter("reverseTimezoneRounding", RoundingGoOutTimeSheet.REVERSE_ROUNDING_EACH_TIMEZONE.value)
				.setParameter("isSetRounding", RoundingGoOutTimeSheet.INDIVIDUAL_ROUNDING.value)
				.setParameter("reverseTimezoneRoundingText", TextResource.localize("KMK003_87"))
				.setParameter("isSetRoundingText", TextResource.localize("KMK003_88"))
				.setParameter("roudingAfterTotalText", TextResource.localize("KMK003_198"))
				.setParameter("roudingAfterEachTimePeriodText", TextResource.localize("KMK003_199"))
				.setParameter("isExtractLateEarlyTimeText", "○")
				.setParameter("isNotExtractLateEarlyTimeText", "-")
				.setParameter("isIncludeWorktimeText", "○")
				.setParameter("isNotIncludeWorktimeText", "-")
				.setParameter("isUseSubstitutionText", "○")
				.setParameter("isNotUseSubstitutionText", "-")
				.setParameter("specifiedTimeSubHol", SubHolTransferSetAtr.SPECIFIED_TIME_SUB_HOL.value)
				.setParameter("certainTimeExcSubHol", SubHolTransferSetAtr.CERTAIN_TIME_EXC_SUB_HOL.value)
				.setParameter("specifiedTimeSubHolText", TextResource.localize("KMK003_106"))
				.setParameter("certainTimeExcSubHolText", TextResource.localize("KMK003_108"))
				.setParameter("childCareWorkUseText", TextResource.localize("KMK003_116"))
				.setParameter("childCareWorkNotUseText", TextResource.localize("KMK003_117"))
				.setParameter("nurTimezoneWorkUseText", TextResource.localize("KMK003_196"))
				.setParameter("nurTimezoneWorkNotUseText", TextResource.localize("KMK003_197"))
				.setParameter("timezoneUseOne", TimezoneUse.SHIFT_ONE)
				.setParameter("amPmAtrOneDay", AmPmAtr.ONE_DAY.value)
				.setParameter("amPmAtrAm", AmPmAtr.AM.value)
				.setParameter("amPmAtrPm", AmPmAtr.PM.value)
				.setParameter("stampPiorityAtrGoingWork", StampPiorityAtr.GOING_WORK.value)
				.setParameter("stampPiorityAtrLeaveWork", StampPiorityAtr.LEAVE_WORK.value)
				.setParameter("stampPiorityAtrEntering", StampPiorityAtr.ENTERING.value)
				.setParameter("stampPiorityAtrExit", StampPiorityAtr.EXIT.value)
				.setParameter("stampPiorityAtrPcLogin", StampPiorityAtr.PCLOGIN.value)
				.setParameter("stampPiorityAtrPcLogout", StampPiorityAtr.PC_LOGOUT.value)
				.setParameter("superiorityAttendance", Superiority.ATTENDANCE.value)
				.setParameter("superiorityOfficeWork", Superiority.OFFICE_WORK.value)
				.setParameter("superiorityGoOut", Superiority.GO_OUT.value)
				.setParameter("superiorityTurnBack", Superiority.TURN_BACK.value)
				.setParameter("goLeavingWorkAtrGoWork", GoLeavingWorkAtr.GO_WORK.value)
				.setParameter("goLeavingWorkAtrLeaveWork", GoLeavingWorkAtr.LEAVING_WORK.value)
				.setParameter("roundingTimeTypeWorkTimezone", RoundingTimeType.WORK_TIMEZONE.value)
				.setParameter("roundingTimeTypePubHolWorkTimezone", RoundingTimeType.PUB_HOL_WORK_TIMEZONE.value)
				.setParameter("roundingTimeTypeOtTimezone", RoundingTimeType.OT_TIMEZONE.value)
				.setParameter("lateEarlyAtrLate", LateEarlyAtr.LATE.value)
				.setParameter("lateEarlyAtrEarly", LateEarlyAtr.EARLY.value)
				.setParameter("fromOverTime", CompensatoryOccurrenceDivision.FromOverTime.value)
				.setParameter("workDayOffTime", CompensatoryOccurrenceDivision.WorkDayOffTime.value)
				.setParameter("workSystemAtrDayShift", WorkSystemAtr.DAY_SHIFT.value)
				.setParameter("workSystemAtrNightShift", WorkSystemAtr.NIGHT_SHIFT.value)
				.setParameter("resttimeAtrHalfDay", ResttimeAtr.HALF_DAY.value)
				.setParameter("workNoOne", 1)
				.setParameter("workNoTwo", 2);
		return queryString.getResultList();
	}

}
