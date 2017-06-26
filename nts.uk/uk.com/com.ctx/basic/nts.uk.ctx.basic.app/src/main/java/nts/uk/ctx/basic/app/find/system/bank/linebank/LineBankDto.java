package nts.uk.ctx.basic.app.find.system.bank.linebank;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;
import nts.uk.ctx.basic.app.command.system.bank.linebank.ConsignorCommand;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBank;

@Value
public class LineBankDto {
	int accountAtr;
	String accountNo;
	String branchId;
	List<ConsignorCommand> consignors;
	String lineBankCode;
	String lineBankName;
	String memo;
	String requesterName;

	public static LineBankDto fromDomain(LineBank domain) {
		return new LineBankDto(domain.getAccountAtr().value, domain.getAccountNo().v(),
				domain.getBranchId().toString(),
				domain.getConsignor().stream()
						.map(x -> new ConsignorCommand(x.getConsignorCode().v(), x.getConsignorMemo().v()))
						.collect(Collectors.toList()),
				domain.getLineBankCode().v(), domain.getLineBankName().v(), domain.getMemo().v(),
				domain.getRequesterName().v());
	}
}
