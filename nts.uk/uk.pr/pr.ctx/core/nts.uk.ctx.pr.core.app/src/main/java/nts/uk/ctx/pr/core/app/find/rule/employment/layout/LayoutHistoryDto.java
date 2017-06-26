package nts.uk.ctx.pr.core.app.find.rule.employment.layout;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistory;

@Value
public class LayoutHistoryDto {
	/**
	 * Company Code
	 */
	private String companyCode;

	/**
	 * Layout Code
	 */
	private String stmtCode;
	
	private String historyId;
	private int layoutAtr;
	private int startYm;
	private int endYm;
	
	public static LayoutHistoryDto fromDomain(LayoutHistory domain) {
		return new LayoutHistoryDto(domain.getCompanyCode().v(), 
				domain.getStmtCode().v(), 
				domain.getHistoryId(), 
				domain.getLayoutAtr().value, 
				domain.getStartYm().v(), 
				domain.getEndYm().v());
	}
}
