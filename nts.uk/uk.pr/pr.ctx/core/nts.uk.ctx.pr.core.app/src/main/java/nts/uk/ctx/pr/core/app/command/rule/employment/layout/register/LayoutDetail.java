package nts.uk.ctx.pr.core.app.command.rule.employment.layout.register;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;
import nts.uk.shr.com.context.AppContexts;

@Getter
@Setter
public class LayoutDetail {
	/**カテゴリ区分 */
	private int categoryAtr;	
	/**項目CD */
	private String itemCode;
	/** new itemCode for update*/
	private String updateItemCode;
	/** khi detail nay là thêm mới thì added = true*/
	private boolean added;
	/**自動採番された行番号	 */
	private String autoLineId;
	/**項目位置（列）	 */
	private int itemPosColumn;
	/**計算方法 */	
	private int calculationMethod;
//	/**項目位置 */
//	private int columnPosition;
//	/** 按分設定  */
//	private int distribute;	
	/** 表示区分 */	
	private int displayAtr;
	/** 合計対象区分 */	
	private int sumScopeAtr;	
	//今回、対応対象外	
	/** 計算式コード */
	private String formulaCode;	
	/**個人金額コード	 */
	private String personalWageCode;
	/** 賃金テーブルコード */
	private String wageTableCode;
	/** 共通金額 */
	private BigDecimal commonAmount;	
	/** 支給相殺コード */	
	private String setOffItemCode;	
	/**通勤区分*/
	private int commuteAtr;
	
	/**按分方法	 */
	private int distributeWay;
	/**按分設定	 */
	private int distributeSet;
	/**	エラー範囲上限利用区分 */
	private int isErrorUseHigh;
	/**	エラー範囲上限 */
	private BigDecimal errorRangeHigh;
	/**エラー範囲下限利用区分	 */
	private int isErrorUserLow;
	/**エラー範囲下限 */
	private BigDecimal errorRangeLow;
	/**アラーム範囲上限利用区分	 */
	private int isAlamUseHigh;
	/**アラーム範囲上限	 */
	private BigDecimal alamRangeHigh;
	/**	 アラーム範囲下限利用区分*/
	private int isAlamUseLow;
	/**アラーム範囲下限	 */
	private BigDecimal alamRangeLow;
	
	public LayoutMasterDetail toDomain(String layoutCode,String historyId){
		
		return LayoutMasterDetail.createFromJavaType(
				AppContexts.user().companyCode(), 
				layoutCode, 
				this.categoryAtr, 
				this.itemCode, 
				this.autoLineId, 
				this.displayAtr, 
				this.sumScopeAtr, 
				this.calculationMethod, 
				this.distributeWay, 
				this.distributeSet,
				this.formulaCode,
				this.personalWageCode,
				this.wageTableCode,
				this.commonAmount,
				this.setOffItemCode,
				this.commuteAtr,
				this.isErrorUseHigh, 
				this.errorRangeHigh, 
				this.isErrorUserLow, 
				this.errorRangeLow,
				this.isAlamUseHigh, 
				this.alamRangeHigh, 
				this.isAlamUseLow,
				this.alamRangeLow,
				this.itemPosColumn,
				historyId);
	}
}
