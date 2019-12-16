package nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BreakDayOffRemainMngParam {
	/**
	 * 会社ID
	 */
	private String cid;
	/**社員ID	 */
	private String sid;
	/**集計開始日, 集計終了日	 */
	private DatePeriod dateData;
	/**	モード : True: 月次か, False: その他か*/
	private boolean mode;
	/**基準日	 */
	private GeneralDate baseDate;
	/**	上書きフラグ: True:  上書き, False: 未上書き*/
	private boolean replaceChk;
	/**上書き用の暫定休出代休紐付け管理	 */
	private List<InterimRemain> interimMng;
	/**	上書き用の暫定休出管理データ */
	private List<InterimBreakMng> breakMng;
	/**	上書き用の  暫定代休管理データ*/
	private List<InterimDayOffMng> dayOffMng;
	/**
	 * 前回代休の集計結果
	 */
	private Optional<BreakDayOffRemainMngOfInPeriod> optBeforeResult;
	/**
	 * 作成元区分
	 */
	private Optional<CreateAtr> creatorAtr;
	/**
	 * 対象期間
	 */
	private Optional<DatePeriod> processDate;
}
