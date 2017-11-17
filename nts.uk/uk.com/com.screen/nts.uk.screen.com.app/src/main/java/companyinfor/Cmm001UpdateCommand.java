package companyinfor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.bs.employee.app.command.workplace.workplacedifferinfor.UpdateDivWorkPlaceDifferInforCommand;
import nts.uk.ctx.command.UpdateCompanyInforCommand;
import nts.uk.ctx.sys.env.app.command.sysusagesetfinder.UpdateSysUsageSetCommand;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cmm001UpdateCommand {
	private UpdateCompanyInforCommand updateComCm;
	private UpdateSysUsageSetCommand updateSysCm;
	private UpdateDivWorkPlaceDifferInforCommand updateDivCm;
}
