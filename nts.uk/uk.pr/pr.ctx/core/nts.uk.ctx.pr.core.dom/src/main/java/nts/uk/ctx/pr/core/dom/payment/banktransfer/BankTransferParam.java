package nts.uk.ctx.pr.core.dom.payment.banktransfer;

import lombok.Value;
import nts.arc.time.GeneralDate;

/**
 * Include all information for query bank transfer
 * @author chinhbv
 *
 */
@Value
public class BankTransferParam {
	String companyCode;
	String personId;
	
	String fromBranchId;
	int fromAccountAtr;
	String fromAccountNo;
	
	String toBranchId;
	int toAccountAtr;
	String toAccountNo;
	
	int processNo;
	int processYearMonth;
	int payBonusAtr;
	int sparePayAtr;
	GeneralDate payDate;
}
