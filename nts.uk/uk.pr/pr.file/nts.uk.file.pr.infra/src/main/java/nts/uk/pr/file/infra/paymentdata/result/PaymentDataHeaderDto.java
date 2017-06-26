package nts.uk.pr.file.infra.paymentdata.result;

import java.util.List;

import lombok.Value;

@Value
public class PaymentDataHeaderDto {
	String personId;
	
	String companyCode;
	
	String companyName;
	
	String departmentCode;
	
	String departmentName;
	
	String personName;

	Integer processingYM;

	/** 扶養人数（その月時点） */
	Integer dependentNumber;

	/** 明細書コード */
	String specificationCode;

	/** 明細書名 */
	String specificationName;

	/** 作成方法フラグ */
	// Integer makeMethodFlag;

	/** 社員コード */
	String employeeCode;

	/** memo */
	String comment;

	List<PrintPositionCategoryDto> printPositionCategories;

	boolean isCreated;

	public PaymentDataHeaderDto(String personId, String companyCode, String companyName, String departmentCode, String departmentName, String personName, Integer processingYM, Integer dependentNumber,
			String specificationCode, String specificationName, String employeeCode, String comment, boolean isCreated,
			List<PrintPositionCategoryDto> printPositionCategories) {
		super();
		
		this.personId = personId;
		this.companyCode = companyCode;
		this.companyName = companyName;
		this.departmentCode = departmentCode;
		this.departmentName = departmentName;
		this.personName = personName;
		this.processingYM = processingYM;
		this.dependentNumber = dependentNumber;
		this.specificationCode = specificationCode;
		this.specificationName = specificationName;
		// this.makeMethodFlag = makeMethodFlag;
		this.employeeCode = employeeCode;
		this.comment = comment;
		this.isCreated = isCreated;
		this.printPositionCategories = printPositionCategories;
	}
}
