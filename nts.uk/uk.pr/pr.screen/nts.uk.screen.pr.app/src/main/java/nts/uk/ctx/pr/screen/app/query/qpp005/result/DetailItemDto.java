package nts.uk.ctx.pr.screen.app.query.qpp005.result;

import lombok.Data;
import lombok.Getter;

@Data
public class DetailItemDto {

	/**
	 * category atr
	 */
	int categoryAtr;

	Integer itemAtr;

	/**
	 * 項目コード
	 */
	String itemCode;

	/**
	 * 項目名
	 */
	String itemName;

	/**
	 * 値
	 */
	Double value;

	int calculationMethod;

	/**
	 * 修正フラグ
	 */
	int correctFlag;

	/**
	 * 行
	 */
	int linePosition;

	/**
	 * 列
	 */
	int columnPosition;

	Integer deductAtr;

	Integer displayAtr;

	/**
	 * 課税区分
	 */
	Integer taxAtr;

	private Integer limitAmount;

	@Getter
	private Double commuteAllowTaxImpose;

	@Getter
	private Double commuteAllowMonth;

	@Getter
	private Double commuteAllowFraction;

	@Getter
	boolean isCreated;

	@Getter
	public int itemType;
	
	public String itemColor;
	
	public Integer sumScopeAtr;
	
	public DetailItemDto(int categoryAtr, Integer itemAtr, String itemCode, String itemName, Double value,
			int calculationMethod, int correctFlag, int linePosition, int columnPosition, Integer deductAtr,
			Integer displayAtr, Integer taxAtr, Integer limitAmount, Double commuteAllowTaxImpose,
			Double commuteAllowMonth, Double commuteAllowFraction, Integer sumScopeAtr, boolean isCreated) {
		super();
		this.categoryAtr = categoryAtr;
		this.itemAtr = itemAtr;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.value = value;
		this.calculationMethod = calculationMethod;
		this.correctFlag = correctFlag;
		this.linePosition = linePosition;
		this.columnPosition = columnPosition;
		this.deductAtr = deductAtr;
		this.displayAtr = displayAtr;
		this.taxAtr = taxAtr;
		this.limitAmount = limitAmount;
		this.commuteAllowTaxImpose = commuteAllowTaxImpose;
		this.commuteAllowMonth = commuteAllowMonth;
		this.commuteAllowFraction = commuteAllowFraction;
		this.sumScopeAtr = sumScopeAtr;
		this.isCreated = isCreated;
		if(this.correctFlag == 1) {
			this.itemColor = "#bdd7ee";
		}
		
		
	}
	
	public DetailItemDto(int categoryAtr, Integer itemAtr, String itemCode,Double value,
			int correctFlag, int linePosition, int columnPosition, Integer deductAtr,
			Integer displayAtr, Integer taxAtr, Integer limitAmount, Double commuteAllowTaxImpose,
			Double commuteAllowMonth, Double commuteAllowFraction) {
		super();
		this.categoryAtr = categoryAtr;
		this.itemAtr = itemAtr;
		this.itemCode = itemCode;
		this.value = value;
		this.correctFlag = correctFlag;
		this.linePosition = linePosition;
		this.columnPosition = columnPosition;
		this.deductAtr = deductAtr;
		this.displayAtr = displayAtr;
		this.taxAtr = taxAtr;
		this.limitAmount = limitAmount;
		this.commuteAllowTaxImpose = commuteAllowTaxImpose;
		this.commuteAllowMonth = commuteAllowMonth;
		this.commuteAllowFraction = commuteAllowFraction;
	}
	

	/**
	 * App から
	 * 
	 * @param categoryAtr
	 * @param itemAtr
	 * @param itemCode
	 * @param itemName
	 * @param value
	 * @param calculationMethod
	 * @param correctFlag
	 * @param linePosition
	 * @param colPosition
	 * @param deductAtr
	 * @param displayAtr
	 * @param taxAtr
	 * @param limitAmount
	 * @param commuteAllowTaxImpose
	 * @param commuteAllowMonth
	 * @param commuteAllowFraction
	 * @param isCreated
	 * @return
	 */
	public static DetailItemDto fromData(
			int categoryAtr, 
			Integer itemAtr, 
			String itemCode, 
			String itemName,
			Double value, 
			int calculationMethod, 
			int correctFlag, 
			int linePosition, 
			int colPosition, 
			Integer deductAtr,
			Integer displayAtr, 
			Integer taxAtr, 
			Integer limitAmount, 
			Double commuteAllowTaxImpose,
			Double commuteAllowMonth, 
			Double commuteAllowFraction, 
			Integer sumScopeAtr,
			boolean isCreated) {
		return new DetailItemDto(
				categoryAtr, 
				itemAtr, 
				itemCode, 
				itemName, 
				value, 
				calculationMethod, 
				correctFlag,
				linePosition, 
				colPosition, 
				deductAtr, 
				displayAtr, 
				taxAtr, 
				limitAmount, 
				commuteAllowTaxImpose,
				commuteAllowMonth, 
				commuteAllowFraction, 
				sumScopeAtr,
				isCreated);

	}

	/**
	 * DBから
	 * 
	 * @param categoryAtr
	 * @param itemAtr
	 * @param itemCode
	 * @param itemName
	 * @param value
	 * @param correctFlag
	 * @param linePosition
	 * @param colPosition
	 * @param deductAtr
	 * @param displayAtr
	 * @param taxAtr
	 * @param limitAmount
	 * @param commuteAllowTaxImpose
	 * @param commuteAllowMonth
	 * @param commuteAllowFraction
	 * @param isCreated
	 * @return
	 */
	public static DetailItemDto toData(
			int categoryAtr, 
			Integer itemAtr, 
			String itemCode, 
			Double value, 
			int correctFlag, 
			int linePosition, 
			int colPosition, 
			Integer deductAtr,
			Integer displayAtr, 
			Integer taxAtr, 
			Integer limitAmount, 
			Double commuteAllowTaxImpose,
			Double commuteAllowMonth, 
			Double commuteAllowFraction) {
		return new DetailItemDto(
				categoryAtr, 
				itemAtr, 
				itemCode, 
				value, 
				correctFlag,
				linePosition, 
				colPosition, 
				deductAtr, 
				displayAtr, 
				taxAtr, 
				limitAmount, 
				commuteAllowTaxImpose,
				commuteAllowMonth, 
				commuteAllowFraction);
	}
	
	/**
	 * settup item type
	 */
	public void setItemType() {
		switch (this.itemAtr) {
		case 0:
			// システム計算
			if (this.calculationMethod != 9) {
				this.itemType = 0;
			} else {
				this.itemType = -1;
			}
			break;
		case 1:
			// システム計算
			if (this.calculationMethod != 9) {
				// 通勤費リンク
				if (this.taxAtr == 3 || this.taxAtr == 4) {
					this.itemType = 1;
				} else {
					this.itemType = 0;
				}
			} else {
				this.itemType = -1;
			}
			break;
		case 2:
		case 3:
			// システム計算
			if (this.calculationMethod != 9) {
				// 通勤費リンク
				if (this.taxAtr == 3 || this.taxAtr == 4) {
					this.itemType = 1;
				} else {
					this.itemType = 0;
				}
			} else {
				this.itemType = -1;
			}
			break;
		default:
			break;
		}
	}

}
