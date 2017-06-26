package nts.uk.ctx.pr.core.dom.rule.employment.layout.line;

import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutCode;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;

import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;

public class LayoutMasterLine extends AggregateRoot {

	@Getter
	private CompanyCode companyCode;

	@Getter
	private LayoutCode stmtCode;
	@Getter
	private String historyId;

	@Getter
	private AutoLineId autoLineId;

	@Getter
	private LineDispAtr lineDisplayAttribute;

	@Getter
	private LinePosition linePosition;

	@Getter
	private CategoryAtr categoryAtr;

	@Getter
	private List<LayoutMasterDetail> layoutMasterDetails;

	public LayoutMasterLine(CompanyCode companyCode, LayoutCode stmtCode, AutoLineId autoLineId,
			CategoryAtr categoryAtr, LineDispAtr lineDispayAttribute, LinePosition linePosition, String historyId) {
		super();
		this.companyCode = companyCode;
		this.stmtCode = stmtCode;
		this.autoLineId = autoLineId;
		this.categoryAtr = categoryAtr;
		this.lineDisplayAttribute = lineDispayAttribute;
		this.linePosition = linePosition;
		this.historyId = historyId;
	}

	public static LayoutMasterLine createFromJavaType(String companyCode, String stmtCode, String autoLineId,
			int lineDispayAttribute, int linePosition, int categoryAtr, String historyId) {

		return new LayoutMasterLine(new CompanyCode(companyCode), new LayoutCode(stmtCode), new AutoLineId(autoLineId),
				EnumAdaptor.valueOf(categoryAtr, CategoryAtr.class),
				EnumAdaptor.valueOf(lineDispayAttribute, LineDispAtr.class), new LinePosition(linePosition), historyId);

	}

	public static LayoutMasterLine createFromDomain(CompanyCode companyCode, LayoutCode stmtCode, AutoLineId autoLineId,
			CategoryAtr categoryAtr, LineDispAtr lineDispayAttribute, LinePosition linePosition, String historyId) {

		return new LayoutMasterLine(companyCode, stmtCode, autoLineId, categoryAtr, lineDispayAttribute, linePosition,
				historyId);
	}

}
