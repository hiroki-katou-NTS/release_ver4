package nts.uk.ctx.at.schedule.app.command.budget.schedulevertical.verticalsetting;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting.FormPeople;
import nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting.VerticalCalItem;

@Data
@AllArgsConstructor
public class VerticalCalItemCommand {
	/* 会社ID */
    private String companyId;
    
    /*コード*/
    private String verticalCalCd;
    
    /* 汎用縦計項目ID */
    private String itemId;
    
    /* 項目名 */
    private String itemName;
    
    /* 計算区分 */
    private int calculateAtr;
    
    /* 表示区分 */
    private int displayAtr;
    
    /* 累計区分 */
    private int cumulativeAtr;
    
    /* 属性 */
    private int attributes;
    
    // 端数処理
 	private int rounding;
 	
 	private int dispOrder;
 	private FormPeopleCommand formPeople;
 	
 	public VerticalCalItem toDomainCalItem(String companyId, String verticalCalCd, String itemId){
 		FormPeople formPeople1 = this.formPeople != null
 				? this.formPeople.toDomainFormPeople(companyId, verticalCalCd, itemId)
				: null;
		return VerticalCalItem.createFromJavatype(companyId, verticalCalCd, itemId, itemId, calculateAtr, displayAtr, cumulativeAtr, attributes, rounding, dispOrder, formPeople1);
 	}
}
