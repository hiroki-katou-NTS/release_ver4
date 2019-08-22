package nts.uk.screen.at.app.dailyperformance.correction.identitymonth;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.screen.at.app.dailyperformance.correction.dto.IdentityProcessUseSetDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndentityMonthParam {

	public String companyId;
	
	public String employeeId;
	
	public GeneralDate dateRefer;
	
	public Integer closureId;
	
	private Integer displayFormat;
	
	private Optional<IdentityProcessUseSetDto> indentityUseSetOpt;
	
}
