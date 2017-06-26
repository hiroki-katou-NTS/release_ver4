package nts.uk.file.pr.app.export.banktransfer.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDto {
	private String branchName;
	private String branchCode;
	private String bankCode;
}
