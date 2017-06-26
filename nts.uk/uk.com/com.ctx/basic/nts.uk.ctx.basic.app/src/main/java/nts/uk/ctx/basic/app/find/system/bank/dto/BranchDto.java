package nts.uk.ctx.basic.app.find.system.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BranchDto {
	private String bankBranchCode;
	private String bankBranchID;
	private String bankBranchName;
	private String bankBranchNameKana;
	private String memo;
}
