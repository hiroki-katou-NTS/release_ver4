package nts.uk.ctx.pr.proto.dom.layout;

import java.util.List;

import nts.arc.enums.EnumAdaptor;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.proto.dom.layout.category.LayoutMasterCategory;

public class LayoutMaster extends AggregateRoot {

	/** code */
	@Getter
	private CompanyCode companyCode;
	
	/** 開始年月 */
	@Getter
	private YearMonth startYM;
	
	/** 明細書コード */
	@Getter
	private LayoutCode stmtCode;
	
	/** 終了年月 */
	@Getter
	@Setter
	private YearMonth endYM;
	
	/** レイアウト区分 */
	@Getter
	private LayoutAtr layoutAtr;
	
	/** 明細書名 */
	@Getter
	private LayoutName stmtName;
	
	/** 明細書のレイアウトのカテゴリ情報 */
	@Getter
	private List<LayoutMasterCategory> layoutMasterCategories;

	public LayoutMaster(CompanyCode code, YearMonth startYM, LayoutCode stmtCode, YearMonth endYM, LayoutAtr layoutAtr,
			LayoutName stmtName) {
		super();
		this.companyCode = code;
		this.startYM = startYM;
		this.stmtCode = stmtCode;
		this.endYM = endYM;
		this.layoutAtr = layoutAtr;
		this.stmtName = stmtName;
	}

	/**
	 * create From Java Type
	 * @return LayoutMaster
	 */
	public static LayoutMaster createFromJavaType(String companyCode, int startYM, String stmtCode, int endYM, int layoutAtr,
			String stmtName){
		return new LayoutMaster(
				new CompanyCode(companyCode), 
				new YearMonth(startYM), 
				new LayoutCode(stmtCode), 
				new YearMonth(endYM),
				EnumAdaptor.valueOf(layoutAtr, LayoutAtr.class), 
				new LayoutName(stmtName));
	}
	
}
