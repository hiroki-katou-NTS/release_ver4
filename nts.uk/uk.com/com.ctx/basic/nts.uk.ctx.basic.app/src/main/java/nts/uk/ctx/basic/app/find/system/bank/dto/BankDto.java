package nts.uk.ctx.basic.app.find.system.bank.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BankDto {
	private String bankCode;
	private String bankName;
	private String bankNameKana;
	private String memo;
	private List<BranchDto> bankBranch;
}
