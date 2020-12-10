package nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.workrule.businesstype.BusinessTypeCode;
/**
 * 勤務種別の月別実績の修正のフォーマット
 * @author tutk
 *
 */
@Getter
public class MonthlyRecordWorkType extends AggregateRoot{
	/**会社ID*/
	private String companyID;
	/**コード*/
	private BusinessTypeCode businessTypeCode;
	/**表示項目*/
	private MonthlyActualResults displayItem;

	public MonthlyRecordWorkType(String companyID, BusinessTypeCode businessTypeCode, MonthlyActualResults displayItem) {
		super();
		this.companyID = companyID;
		this.businessTypeCode = businessTypeCode;
		this.displayItem = displayItem;
	}

	public MonthlyRecordWorkType clone() {
		return new MonthlyRecordWorkType(companyID, new BusinessTypeCode(businessTypeCode.v()), displayItem.clone());
	}

	public MonthlyRecordWorkType() {
		super();
	}
	

}
