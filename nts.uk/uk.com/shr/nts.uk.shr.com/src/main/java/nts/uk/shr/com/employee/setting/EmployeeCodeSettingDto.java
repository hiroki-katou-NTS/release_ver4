package nts.uk.shr.com.employee.setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCodeSettingDto {

	private String companyId;
	
	private int ceMethodAttr;
	
	private int numberOfDigits;
	
}
