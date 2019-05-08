package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AbsRecMngInPeriodParamInput {
	/**
	 * 会社ID
	 */
	private String cid;
	/**社員ID 
	 */	
	private String sid;
	/**集計開始日, 集計終了日	 */
	private DatePeriod dateData;
	/**基準日 	 */
	private GeneralDate baseDate;
	/**モード	 True: 月次か, false: その他か*/
	private boolean mode;
	/**上書きフラグ	 True: 上書き, False: 未上書き*/
	private boolean overwriteFlg;
	/**上書き用の暫定管理データ	 */
	private List<InterimAbsMng> useAbsMng;
	/**	上書き用の暫定残数管理データ */
	private List<InterimRemain> interimMng;
	/**
	 * 上書き用の暫定振出管理データ
	 */
	private List<InterimRecMng> useRecMng;
	/**
	 * 前回振休の集計結果
	 */
	private Optional<AbsRecRemainMngOfInPeriod> optBeforeResult;
	
}
