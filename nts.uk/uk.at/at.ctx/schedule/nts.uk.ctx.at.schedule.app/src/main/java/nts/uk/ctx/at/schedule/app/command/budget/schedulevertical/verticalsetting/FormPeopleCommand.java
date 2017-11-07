package nts.uk.ctx.at.schedule.app.command.budget.schedulevertical.verticalsetting;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting.ActualDisplayAtr;
import nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting.FormPeople;
import nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting.FormPeopleFunc;
/**
 * 
 * @author yennth
 *
 */
@Data
@AllArgsConstructor
public class FormPeopleCommand {
	/* 会社ID */
    private String companyId;
    
    /*コード*/
    private String verticalCalCd;
    
    /* 汎用縦計項目ID */
    private String verticalCalItemId;
    
    /* 実績表示区分 */
    private int actualDisplayAtr;
    private List<FormPeopleFuncCommand> lstPeopleFunc;
    
    public FormPeople toDomainFormPeople(String companyId, String verticalCalCd, String verticalCalItemId){
    	List<FormPeopleFunc> formPeopleLst = this.lstPeopleFunc != null
    			? this.lstPeopleFunc.stream().map(c -> c.toDomainFunc(companyId, 
																	c.getVerticalCalCd(), 
																	c.getVerticalCalItemId(), 
																	c.getExternalBudgetCd(), 
																	c.getCategoryAtr(), 
																	c.getOperatorAtr(), 
																	c.getDispOrder())).collect(Collectors.toList())
				: null;
		return FormPeople.createFromJavaType(companyId, verticalCalCd, verticalCalItemId, actualDisplayAtr, formPeopleLst);
    }	
}
