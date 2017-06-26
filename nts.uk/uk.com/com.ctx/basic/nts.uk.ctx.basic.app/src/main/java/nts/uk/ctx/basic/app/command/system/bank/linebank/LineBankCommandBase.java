package nts.uk.ctx.basic.app.command.system.bank.linebank;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.basic.dom.system.bank.linebank.LineBank;

@Data
@NoArgsConstructor
public class LineBankCommandBase {
	private int accountAtr;
	private String accountNo;
	private String branchId;
	private List<ConsignorCommand> consignor;
	private String lineBankCode;
	private String lineBankName;
	private String memo;
	private String requesterName;

	public LineBank toDomain(String companyCode) {
		LineBank domain = LineBank.createFromJavaType(companyCode, this.accountAtr, this.accountNo,
				this.branchId, this.lineBankCode, this.lineBankName, this.memo, this.requesterName);
		domain.createConsignorFromJavaType(consignor.get(0).getCode(), consignor.get(0).getMemo(),
				consignor.get(1).getCode(), consignor.get(1).getMemo(), consignor.get(2).getCode(),
				consignor.get(2).getMemo(), consignor.get(3).getCode(), consignor.get(3).getMemo(),
				consignor.get(4).getCode(), consignor.get(4).getMemo());
		return domain;
	}
}
