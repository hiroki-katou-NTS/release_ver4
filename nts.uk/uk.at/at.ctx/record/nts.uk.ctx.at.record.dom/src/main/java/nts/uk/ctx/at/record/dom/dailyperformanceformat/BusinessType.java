package nts.uk.ctx.at.record.dom.dailyperformanceformat;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeName;

/**
 * 
 * @author nampt
 * 勤務種別
 */
@Getter
public class BusinessType extends AggregateRoot {

	private String companyId;
	
	/**
	 * padding left "0"
	 */
	private BusinessTypeCode businessTypeCode;
	
	private BusinessTypeName businessTypeName;

	public BusinessType(String companyId, BusinessTypeCode businessTypeCode, BusinessTypeName businessTypeName) {
		super();
		this.companyId = companyId;
		this.businessTypeCode = businessTypeCode;
		this.businessTypeName = businessTypeName;
	}
	
	public static BusinessType createFromJavaType(String companyId, String businessTypeCode, String businessTypeName){
		return new BusinessType(companyId, new BusinessTypeCode(businessTypeCode), new BusinessTypeName(businessTypeName));
	}	
}
