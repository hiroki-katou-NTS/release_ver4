package nts.uk.ctx.at.record.dom.approvalmanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAffiliationInforDto {
	
	/** 社員ID */
	public String employeeID;
	
	/** 社員コード */
	public String employeeCode;
	
	/** 職場ID */
	public String workPlaceID;
	
	/** 職位ID */
	public String positionID;
	
	/** 雇用情報コード */
	public String employmentInforCode;
	
	/** 分類コード */
	public String classificationCode;
	
	/** 職位 */
	public String sequenceCode;
	
	/** 階層コード */
	public String hierarchyCd;
	
	public String positionCd;
	
	public int order;

	public EmployeeAffiliationInforDto(String employeeID, String employeeCode, String workPlaceID, String positionID,
			String employmentInforCode, String classificationCode) {
		super();
		this.employeeID = employeeID;
		this.employeeCode = employeeCode;
		this.workPlaceID = workPlaceID;
		this.positionID = positionID;
		this.employmentInforCode = employmentInforCode;
		this.classificationCode = classificationCode;
	}
	
}
