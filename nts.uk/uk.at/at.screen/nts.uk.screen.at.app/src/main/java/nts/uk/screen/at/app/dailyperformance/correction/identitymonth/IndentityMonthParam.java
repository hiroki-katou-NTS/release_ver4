package nts.uk.screen.at.app.dailyperformance.correction.identitymonth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndentityMonthParam {

	public String companyId;
	
	public String employeeId;
	
	public GeneralDate dateRefer;
	
	public Integer closureId;
}
