package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
@AllArgsConstructor
@Getter
@Setter
public class OvertimeParameter {
	/**社員ID	 */
	private String employeeId;
	/** 年月日	 */
	private GeneralDate dateInfo;
	/**
	 * 勤種反映フラグ(実績)
	 * True: する
	 * False: しない
	 */
	private boolean actualReflectFlg;
	/**
	 * 勤種反映フラグ(予定)
	 * True: する
	 * False: しない
	 */
	private boolean scheReflectFlg;
	/**
	 * 残業時間反映フラグ
	 * True: する
	 * False: しない
	 */
	private boolean timeReflectFlg;
	/**
	 * 自動セット打刻をクリアフラグ
	 * True: する
	 * False: しない
	 */
	private boolean autoClearStampFlg;
	/**
	 * 予定と実績を同じに変更する区分
	 */
	private ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg;
	/**
	 * 予定出退勤反映フラグ
	 * true: する
	 * false: しない
	 */
	private boolean scheTimeOutFlg;
	/**
	 * 残業申請
	 */
	private OvertimeAppParameter overtimePara;
}
