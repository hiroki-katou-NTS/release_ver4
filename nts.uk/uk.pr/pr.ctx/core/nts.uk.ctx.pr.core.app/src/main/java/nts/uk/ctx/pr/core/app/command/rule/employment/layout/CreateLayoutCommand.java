package nts.uk.ctx.pr.core.app.command.rule.employment.layout;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMaster;
import nts.uk.shr.com.context.AppContexts;

@Getter
public class CreateLayoutCommand {

	/** Nếu mode trên màn hình là copy từ layout khác thì isCopy=true */
	private boolean checkCopy;
	/** StmtCode của Layout được copy */
	private String stmtCodeCopied;
	/** startYm của Layout được copy */
	private int startYmCopied;
	private String stmtCode;
	private int startYm;
	private int layoutAtr;
	private String stmtName;
	private int endYm;

	/**
	 * Convert to domain object from command values
	 * 
	 * @return
	 */
	public LayoutMaster toDomain() {
		return LayoutMaster.createFromJavaType(AppContexts.user().companyCode(), this.stmtCode, this.stmtName);
	}

	public LayoutHistory toDomain(String newHistoryId) {
		return LayoutHistory.createFromJavaType(AppContexts.user().companyCode(), this.stmtCode, newHistoryId,
				this.startYm, this.endYm, this.layoutAtr);
	}
	// code cua anh Lam truoc khi thay doi DB
	// public LayoutMaster toDomain(String newHistoryId) {
	// return LayoutMaster.createFromJavaType(AppContexts.user().companyCode(),
	// this.stmtCode, this.stmtName);
	// }
}
