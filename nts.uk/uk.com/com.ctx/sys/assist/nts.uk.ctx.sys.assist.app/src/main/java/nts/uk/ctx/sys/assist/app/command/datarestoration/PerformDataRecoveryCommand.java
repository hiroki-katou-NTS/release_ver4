package nts.uk.ctx.sys.assist.app.command.datarestoration;

import java.util.List;

public class PerformDataRecoveryCommand {
	public String recoveryProcessingId;
	public List<EmployeeListCommand> employeeList;
	public List<RecoveryCategoryCommand> recoveryCategoryList;
	public String recoveryFile;
	public Integer recoveryMethodOptions;
}
