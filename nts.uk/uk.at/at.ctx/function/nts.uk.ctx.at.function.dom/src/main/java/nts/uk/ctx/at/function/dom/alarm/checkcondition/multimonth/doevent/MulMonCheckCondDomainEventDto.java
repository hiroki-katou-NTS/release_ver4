package nts.uk.ctx.at.function.dom.alarm.checkcondition.multimonth.doevent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlAtdItemConAdapterDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MulMonCheckCondDomainEventDto {
	
	private String cid;

	/** 勤務実績のエラーアラームチェックID */
	private String errorAlarmCheckID;
	
	private int condNo;

	/** 名称 */
	private String nameAlarmMulMon;

	/** 使用区分 */
	private boolean useAtr;

	/** チェック項目の種類 */
	private int typeCheckItem;

	/** 表示メッセージ */
	private String displayMessage;

	/** 複数月のﾁｪｯｸ条件 */
	private ErAlAtdItemConAdapterDto erAlAtdItem;

	/** 連続月数 */
	private int continuousMonths;
	
	/** 回数 */
	private int times;

	/** 比較演算子 */
	private int compareOperator;

}
