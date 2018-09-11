package nts.uk.ctx.pr.core.app.command.wageprovision.statementitem;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class ItemRangeSetCommand {

	/**
	 * 会社ID
	 */
	private String cid;

	/**
	 * 給与項目ID
	 */
	private String salaryItemId;

	/**
	 * 範囲値の属性
	 */
	private int rangeValueAtr;

	/**
	 * エラー上限値設定区分
	 */
	private int errorUpperLimitSettingAtr;

	/**
	 * エラー上限値金額
	 */
	private BigDecimal errorUpperRangeValueAmount;

	/**
	 * エラー上限値時間
	 */
	private Integer errorUpperRangeValueTime;

	/**
	 * エラー上限値回数
	 */
	private BigDecimal errorUpperRangeValueNum;

	/**
	 * エラー下限値設定区分
	 */
	private int errorLowerLimitSettingAtr;

	/**
	 * エラー上限値金額
	 */
	private BigDecimal errorLowerRangeValueAmount;

	/**
	 * エラー上限値時間
	 */
	private Integer errorLowerRangeValueTime;

	/**
	 * エラー上限値回数
	 */
	private BigDecimal errorLowerRangeValueNum;

	/**
	 * アラーム上限値設定区分
	 */
	private int alarmUpperLimitSettingAtr;

	/**
	 * アラーム上限値金額
	 */
	private BigDecimal alarmUpperRangeValueAmount;

	/**
	 * アラーム上限値時間
	 */
	private Integer alarmUpperRangeValueTime;

	/**
	 * アラーム上限値回数
	 */
	private BigDecimal alarmUpperRangeValueNum;

	/**
	 * アラーム下限値設定区分
	 */
	private int alarmLowerLimitSettingAtr;

	/**
	 * アラーム上限値金額
	 */
	private BigDecimal alarmLowerRangeValueAmount;

	/**
	 * アラーム上限値時間
	 */
	private Integer alarmLowerRangeValueTime;

	/**
	 * アラーム上限値回数
	 */
	private BigDecimal alarmLowerRangeValueNum;	
	
}
