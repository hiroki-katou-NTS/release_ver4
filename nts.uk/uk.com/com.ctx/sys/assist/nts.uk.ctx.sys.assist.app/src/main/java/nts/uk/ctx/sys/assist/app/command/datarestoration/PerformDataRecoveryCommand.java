package nts.uk.ctx.sys.assist.app.command.datarestoration;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PerformDataRecoveryCommand {
	private String saveSetCode;
	private Integer saveForm;
	private String saveName;
	private String recoveryProcessingId;
	private List<EmployeeListCommand> employeeList;
	private List<RecoveryCategoryCommand> recoveryCategoryList;
	private String recoveryFile;
	private int recoveryMethodOptions;
	private String store_del_ProcessingId;
}
