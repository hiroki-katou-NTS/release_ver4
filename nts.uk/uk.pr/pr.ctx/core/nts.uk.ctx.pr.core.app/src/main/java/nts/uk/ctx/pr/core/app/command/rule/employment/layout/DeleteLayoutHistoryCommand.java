package nts.uk.ctx.pr.core.app.command.rule.employment.layout;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.register.LayoutCategory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMaster;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategory;
import nts.uk.shr.com.context.AppContexts;

@Getter
@Setter
public class DeleteLayoutHistoryCommand {
	
	private String companyCode;
	
	private String stmtCode;
	
	private int startYm;
		
	private int layoutAtr;
	
	private String stmtName;
	
	private int endYm;
	
	private String historyId;
	private int categoryAtr;
	private int categoryPosition;
	public LayoutMaster toDomain(String stmtName){
		return LayoutMaster.createFromJavaType(
				AppContexts.user().companyCode(), 
				this.stmtCode,
				stmtName);
	}
	public LayoutHistory toDomain(int endYm, int layoutAtr){
		return LayoutHistory.createFromJavaType(
				AppContexts.user().companyCode(), 
				this.stmtCode, 
				this.historyId,
				this.startYm, 
				endYm, 
				layoutAtr);
	}
	public LayoutMasterCategory toDomain(){
		return LayoutMasterCategory.createFromJavaType(
				AppContexts.user().companyCode(), 
				this.stmtCode, 
				this.categoryAtr, 
				this.categoryPosition,
				this.historyId);
	}
}
