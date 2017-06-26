package nts.uk.ctx.pr.core.app.command.rule.employment.layout.register;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMaster;
import nts.uk.shr.com.context.AppContexts;

@Getter
public class Layout {

	 private String stmtCode;
	 private int startYm;
	 private String stmtName;
	 private int endYm;
	 private String historyId;
	 /**
	  * Convert to domain object from command values
	  * @return
	  */
	 public LayoutHistory toDomain(int layoutAtr){
		  return LayoutHistory.createFromJavaType(AppContexts.user().companyCode(), 
		     this.stmtCode,  this.historyId,  this.startYm,this.endYm, layoutAtr);
		 }
	 public LayoutMaster toDomain(){
		  return LayoutMaster.createFromJavaType(AppContexts.user().companyCode(), 
		     this.stmtCode, this.stmtName);
		 }
//	 public LayoutMaster toDomain(int layoutAtr){
//	  return LayoutMaster.createFromJavaType(AppContexts.user().companyCode(), 
//	     this.startYm, this.stmtCode, this.endYm, layoutAtr, this.stmtName,
//	     this.historyId);
//	 }
}
