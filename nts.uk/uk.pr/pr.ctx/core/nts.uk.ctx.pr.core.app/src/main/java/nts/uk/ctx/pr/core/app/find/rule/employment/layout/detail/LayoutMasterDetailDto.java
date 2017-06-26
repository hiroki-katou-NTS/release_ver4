package nts.uk.ctx.pr.core.app.find.rule.employment.layout.detail;

import java.math.BigDecimal;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;

/** Finder DTO of item */
@Value
public class LayoutMasterDetailDto {
	private String stmtCode;
	/**カテゴリ区分 */
	private int categoryAtr;
	/**項目CD*/
	private String itemCode;
	/**自動採番された行番号	 */
	private String autoLineId;
	/**項目位置（列）	 */
	private int itemPosColumn;
	/** 合計対象区分 */
	private int sumScopeAtr;
	/** 支給相殺コード */	
	private String setOffItemCode;	
	/**通勤区分*/
	private int commuteAtr;
	/**計算方法 */
	private int calculationMethod;
	/**按分設定 */
	private int distributeSet;
	/**按分方法	 */
	private int distributeWay;
	
	private String formulaCode;
	/**個人金額コード	 */
	private String personalWageCode;
	
	private String wageTableCode;
	
	private BigDecimal commonAmount;
	
	/**エラー範囲上限利用区分	 */
	private int isUseHighError;
	/**エラー範囲上限	 */
	private BigDecimal errRangeHigh;
	/**エラー範囲下限利用区分	 */
	private int isUseLowError;
	/**エラー範囲下限	 */
	private BigDecimal errRangeLow;
	/**アラーム範囲下限利用区分 */
	private int isUseHighAlam;
	/**アラーム範囲上限	 */
	private BigDecimal alamRangeHigh;
	/**アラーム範囲下限利用区分	 */
	private int isUseLowAlam;
	/**アラーム範囲下限	 */
	private BigDecimal alamRangeLow;
	
	public static LayoutMasterDetailDto fromDomain(LayoutMasterDetail domain)
	{
		return new LayoutMasterDetailDto(
				domain.getStmtCode().v(),
				domain.getCategoryAtr().value,
				domain.getItemCode().v(), 
				domain.getAutoLineId().v(),
				domain.getItemPosColumn().v(),
				domain.getSumScopeAtr().value,
				domain.getSetOffItemCode().v(),
				domain.getCommuteAtr().value,
				domain.getCalculationMethod().value,
				domain.getDistribute().getSetting().value, 
				domain.getDistribute().getMethod().value,
				domain.getFormulaCode().v(),
				domain.getPersonalWageCode().v(),
				domain.getWageTableCode().v(),
				domain.getCommonAmount().v(),
				domain.getError().getIsUseHigh().value,
				domain.getError().getRange().max(),
				domain.getError().getIsUseLow().value,				
				domain.getError().getRange().min(),				
				domain.getAlarm().getIsUseHigh().value,
				domain.getAlarm().getRange().max(),
				domain.getAlarm().getIsUseLow().value,
				domain.getAlarm().getRange().min());		
	}
	
}
