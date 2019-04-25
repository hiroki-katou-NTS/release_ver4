package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.Optional;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDateTime;

@Data
@NoArgsConstructor
public class ExecuteProcessExecutionCommand {
	/* 会社ID */
	private String companyId;
	
	/* コード */
	private String execItemCd;
    private String execId;
    private int execType;
    private Optional<GeneralDateTime> nextFireTime;
}