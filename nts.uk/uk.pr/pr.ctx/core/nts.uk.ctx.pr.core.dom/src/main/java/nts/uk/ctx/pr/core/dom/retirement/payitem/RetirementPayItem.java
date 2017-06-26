package nts.uk.ctx.pr.core.dom.retirement.payitem;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.shr.com.primitive.Memo;

/**
 * 退職金項目マスタ
 * 
 * @author Doan Duy Hung
 *
 */
public class RetirementPayItem extends AggregateRoot {
	@Getter
	private String companyCode;

	@Getter
	private IndicatorCategory category;

	@Getter
	private String itemCode;

	@Getter
	private String itemName;

	@Getter
	private RetirementPayItemPrintName printName;

	@Getter
	private String englishName;

	@Getter
	private String fullName;

	@Getter
	private Memo memo;

	@Override
	public void validate() {
		super.validate();
		if (printName == null || StringUtil.isNullOrEmpty(printName.v(), true)) {
			throw new BusinessException("ER001");
		}
	}

	public RetirementPayItem(String companyCode, IndicatorCategory category, String itemCode, String itemName,
			RetirementPayItemPrintName printName, String englishName, String fullName, Memo memo) {
		super();
		this.companyCode = companyCode;
		this.category = category;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.printName = printName;
		this.englishName = englishName;
		this.fullName = fullName;
		this.memo = memo;
	}
}
