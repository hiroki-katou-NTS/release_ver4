package nts.uk.ctx.pr.core.app.find.rule.employment.layout.line;

import java.util.ArrayList;
import java.util.List;

import lombok.Value;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.detail.LayoutMasterDetailDto;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLine;

@Value
public class LayoutMasterLineDto {
	/**会社コード	 */
	private String companyCode;
	/** 明細書コード*/
	private String stmtCode;
	/**自動採番の行番号	 */
	private String autoLineId;
	/**表示区分	 */
	private int lineDisplayAtr;
	/**画面の表示位置	 */
	private int linePosition;
	/**カテゴリ区分	 */
	private int categoryAtr;
	private List<LayoutMasterDetailDto> details;
	
	public static LayoutMasterLineDto fromDomain(LayoutMasterLine domain){
		return new LayoutMasterLineDto(domain.getCompanyCode().v(), 
				domain.getStmtCode().v(),
				domain.getAutoLineId().v(), 
				domain.getLineDisplayAttribute().value,
				domain.getLinePosition().v(),
				domain.getCategoryAtr().value,
				new ArrayList<>());
		
	}
}
