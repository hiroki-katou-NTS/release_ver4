package nts.uk.ctx.pr.core.dom.rule.employment.layout;

import java.util.List;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategory;

public class LayoutMaster extends AggregateRoot {

	/** code */
	@Getter
	private CompanyCode companyCode;

	/** 明細書コード */
	@Getter
	private LayoutCode stmtCode;

	/** 明細書名 */
	@Getter
	private LayoutName stmtName;

	/** 明細書のレイアウトのカテゴリ情報 */
	@Getter
	private List<LayoutMasterCategory> layoutMasterCategories;

	public LayoutMaster(CompanyCode companyCode, LayoutCode stmtCode, LayoutName stmtName) {
		super();
		this.companyCode = companyCode;
		this.stmtCode = stmtCode;
		this.stmtName = stmtName;
	}

	/**
	 * create From Java Type
	 * 
	 * @return LayoutMaster
	 */
	public static LayoutMaster createFromJavaType(String companyCode,  String stmtCode,String stmtName) {
		if (stmtName.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		}

		return new LayoutMaster(new CompanyCode(companyCode),  new LayoutCode(stmtCode),
				 new LayoutName(stmtName));
	}

	public void adjustForNextHistory(LayoutMaster nextHistory) {
		//this.endYm = nextHistory.getStartYM().previousMonth();
	}
}
