package nts.uk.screen.at.app.ksu001.sortsetting;

import lombok.Value;

@Value
public class EmpLicenseClassificationDto {
	/** 社員ID**/
	public String empID;
	/**	免許区分 **/
	public Integer licenseClassification;
	public EmpLicenseClassificationDto(String empID, Integer licenseClassification) {
		super();
		this.empID = empID;
		this.licenseClassification = licenseClassification;
	}
}
