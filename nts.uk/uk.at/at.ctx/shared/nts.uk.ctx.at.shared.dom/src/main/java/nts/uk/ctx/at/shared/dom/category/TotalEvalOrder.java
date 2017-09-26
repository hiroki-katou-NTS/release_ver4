package nts.uk.ctx.at.shared.dom.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.category.primitives.CategoryCode;
import nts.uk.ctx.at.shared.dom.category.primitives.TotalItemNo;
/**
 * 
 * @author yennth
 *
 */
@Getter
@AllArgsConstructor
public class TotalEvalOrder extends DomainObject{
	/**会社ID**/
	private String companyId;
	/** カテゴリコード */
	private CategoryCode categoryCode;
	/** 集計項目NO */
	private TotalItemNo totalItemNo;
	/** 並び順 */
	private Integer dispOrder;
	
	public static TotalEvalOrder createFromJavaType(String companyId, String categoryCode, Integer totalItemNo, Integer dispOrder){
		return new TotalEvalOrder(companyId, new CategoryCode(categoryCode), new TotalItemNo(totalItemNo), dispOrder);
	}
}
