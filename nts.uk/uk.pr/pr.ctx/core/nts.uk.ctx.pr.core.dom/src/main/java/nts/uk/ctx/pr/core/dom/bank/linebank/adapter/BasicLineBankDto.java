package nts.uk.ctx.pr.core.dom.bank.linebank.adapter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BasicLineBankDto {
	@Getter
	private String companyCode;
	@Getter
	private int accountAtr;
	@Getter
	private String accountNo;
	@Getter
	private String branchId;
	@Getter
	private List<BasicConsignorDto> consignor;
	@Getter
	private String lineBankCode;
	@Getter
	private String lineBankName;
	@Getter
	private String memo;
	@Getter
	private String requesterName;
}
