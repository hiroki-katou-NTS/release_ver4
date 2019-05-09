package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@AllArgsConstructor
@Setter
@Getter
public class ComplileInPeriodOfSpecialLeaveParam {
	/**会社ID	 */
	private String cid;
	/**	社員ID */
	private String sid;
	/**
	 * 集計開始日, 集計終了日
	 */
	private DatePeriod complileDate;
	/**
	 * ・モード（月次か、その他か） TRUE: 月次, FALSE: その他  
	 * 月次モード：当月以降は日次のみ見るが、申請とスケは見ない
	 * その他モード：当月以降は申請日次スケを見る
	 */
	private boolean mode;
	/**	基準日 */
	private GeneralDate baseDate;
	/** 特別休暇コード	 */
	private int specialLeaveCode;
	/**
	 * true: 翌月管理データ取得区分がする, false: 翌月管理データ取得区分がしない。
	 */
	private boolean mngAtr;
	/**上書きフラグ	 */
	private boolean overwriteFlg;
	/**	上書き用の暫定管理データ */
	private List<InterimRemain> remainData;
	/**
	 * 特別休暇暫定データ
	 */
	List<InterimSpecialHolidayMng> interimSpecialData;
	/**
	 * 前回の特別休暇の集計結果情報
	 */
	Optional<InPeriodOfSpecialLeaveResultInfor> optBeforeResult;
}
