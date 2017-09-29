package nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FormulaTime {
	/* 会社ID */
    private String companyId;
    
    /*コード*/
    private String verticalCalCd;
    
    /* 汎用縦計項目ID */
    private String verticalCalItemId;
    
    /* カテゴリ区分 */
    private CategoryAtr categoryAtr;
    
    /* 実績表示区分 */
    private ActualDisplayAtr actualDisplayAtr;
}
