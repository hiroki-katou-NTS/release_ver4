package nts.uk.ctx.pr.core.infra.entity.payment.banktransfer;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QbkdtBankTransferPK {
	@Basic(optional = false)
	@Column(name = "CCD")
	public String ccd;
	@Basic(optional = false)
	@Column(name = "PID")
	public String pid;
	@Basic(optional = false)
	@Column(name = "FROM_BRANCH_ID")
	public String fromBranchId;
	@Basic(optional = false)
	@Column(name = "FROM_ACCOUNT_ATR")
	public int fromAccountAtr;
	@Basic(optional = false)
	@Column(name = "FROM_ACCOUNT_NO")
	public String fromAccountNo;
	@Basic(optional = false)
	@Column(name = "TO_BRANCH_ID")
	public String toBranchId;
	@Basic(optional = false)
	@Column(name = "TO_ACCOUNT_ATR")
	public int toAccountAtr;
	@Basic(optional = false)
	@Column(name = "TO_ACCOUNT_NO")
	public String toAccountNo;
	@Basic(optional = false)
	@Column(name = "PAY_BONUS_ATR")
	public int payBonusAtr;
	@Basic(optional = false)
	@Column(name = "PROCESSING_NO")
	public int processingNo;
	@Basic(optional = false)
	@Column(name = "PAY_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate payDate;
	@Basic(optional = false)
	@Column(name = "SPARE_PAY_ATR")
	public int sparePayAtr;
}
