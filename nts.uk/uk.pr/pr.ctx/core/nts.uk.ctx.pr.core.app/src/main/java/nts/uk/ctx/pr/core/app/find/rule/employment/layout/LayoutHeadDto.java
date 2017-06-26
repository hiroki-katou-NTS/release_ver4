package nts.uk.ctx.pr.core.app.find.rule.employment.layout;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMaster;

/** Finder DTO of layout */
@Value
public class LayoutHeadDto {

	/**
	 * Company Code
	 */
	String companyCode;

	/**
	 * Layout Code
	 */
	String stmtCode;
	/**
	 * Layout Name
	 */
	String stmtName;

	public static LayoutHeadDto fromDomain(LayoutMaster domain) {
		return new LayoutHeadDto(
				domain.getCompanyCode().v(), 
				domain.getStmtCode().v(), 
				domain.getStmtName().v());
	}

}
