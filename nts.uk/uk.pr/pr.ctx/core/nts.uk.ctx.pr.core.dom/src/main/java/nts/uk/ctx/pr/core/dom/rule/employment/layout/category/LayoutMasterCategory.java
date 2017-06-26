package nts.uk.ctx.pr.core.dom.rule.employment.layout.category;

import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutCode;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLine;

/**
 * LayoutMasterCategory AggregateRoot
 * 
 * 
 *
 */
public class LayoutMasterCategory extends AggregateRoot {
	/** code */
	@Getter
	private CompanyCode companyCode;

	/** 明細書コード */
	@Getter
	private LayoutCode stmtCode;

	@Getter
	private CategoryAtr ctAtr;

	@Getter
	private String historyId;

	/** 明細書名 */
	@Getter
	private CategoryPosition ctgPos;

	@Getter
	private List<LayoutMasterLine> layoutMasterLines;

	public LayoutMasterCategory(CompanyCode companyCode, LayoutCode stmtCode, CategoryAtr categoryAtr,
			CategoryPosition ctgPos, String historyId) {
		super();
		this.companyCode = companyCode;
		this.stmtCode = stmtCode;
		this.historyId = historyId;
		this.ctAtr = categoryAtr;
		this.ctgPos = ctgPos;

	}

	/**
	 * create From Java Type
	 * 
	 * @return LayoutMasterCategory
	 */
	public static LayoutMasterCategory createFromJavaType(String companyCode,  String stmtCode, int ctgAtr,
			 int ctgPos, String historyId) {

		return new LayoutMasterCategory(new CompanyCode(companyCode), new LayoutCode(stmtCode),
				EnumAdaptor.valueOf(ctgAtr, CategoryAtr.class), new CategoryPosition(ctgPos), historyId);
	}

	/**
	 * 
	 * @param companyCode
	 * @param startYm
	 * @param stmtCode
	 * @param categoryAtr
	 * @param endYm
	 * @param ctgPos
	 * @return
	 */
	public static LayoutMasterCategory createFromDomain(CompanyCode companyCode,LayoutCode stmtCode,
			CategoryAtr categoryAtr, CategoryPosition ctgPos, String historyId) {

		return new LayoutMasterCategory(companyCode, stmtCode, categoryAtr,ctgPos, historyId);
	}
}
