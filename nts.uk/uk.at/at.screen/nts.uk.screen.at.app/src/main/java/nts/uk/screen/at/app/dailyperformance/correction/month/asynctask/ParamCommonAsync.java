package nts.uk.screen.at.app.dailyperformance.correction.month.asynctask;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.IdentityProcessUseSetDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParamCommonAsync implements Serializable{
	
	private static final long serialVersionUID = 1L;

	 String employeeTarget;
	
	 DateRange dateRange;
	
	 String employmentCode;
	
	 Set<String> autBussCode;
	
	 int displayFormat;
	 
	 IdentityProcessUseSetDto identityUseSetDto;
}
