package nts.uk.ctx.pr.core.dom.rule.employment.layout;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;

public class LayoutHistory {

	/** code */
	@Getter
	private CompanyCode companyCode;
	
	/** 明細書コード */
	@Getter
	private LayoutCode stmtCode;
	
	@Getter
	private String historyId;
	
	/** 開始年月 */
	@Getter
	@Setter
	private YearMonth startYm;
	
	/** 終了年月 */
	@Getter
	@Setter
	private YearMonth endYm;
	
	/** レイアウト区分 */
	@Getter
	private LayoutAtr layoutAtr;

	/**
	 * contructors
	 * @param companyCode
	 * @param stmtCode
	 * @param historyId
	 * @param startYm
	 * @param endYm
	 * @param layoutAtr
	 * @return LayoutHist
	 */
	public LayoutHistory(CompanyCode companyCode, LayoutCode stmtCode, String historyId, YearMonth startYm,
			YearMonth endYm, LayoutAtr layoutAtr) {
		super();
		this.companyCode = companyCode;
		this.stmtCode = stmtCode;
		this.historyId = historyId;
		this.startYm = startYm;
		this.endYm = endYm;
		this.layoutAtr = layoutAtr;
	}
	public static LayoutHistory createFromJavaType(String companyCode, String stmtCode, String historyId, int startYm,
			int endYm, int layoutAtr){
		
		return new LayoutHistory(new CompanyCode(companyCode), new LayoutCode(stmtCode), historyId, new YearMonth(startYm), new YearMonth(endYm), EnumAdaptor.valueOf(layoutAtr, LayoutAtr.class));
	}
	
}
