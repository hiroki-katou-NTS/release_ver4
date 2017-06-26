package nts.uk.ctx.pr.core.infra.entity.payment.banktransfer;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QBKDT_BANK_TRANSFER")
public class QbkdtBankTransfer extends UkJpaEntity {

	@EmbeddedId
	public QbkdtBankTransferPK qbkdtBankTransferPK;

	@Basic(optional = false)
	@Column(name = "CNAME_KANA")
	public String cnameKana;
	@Basic(optional = false)
	@Column(name = "FROM_BANK_KN_NAME")
	public String fromBankKnName;
	@Basic(optional = false)
	@Column(name = "FROM_BRANCH_KN_NAME")
	public String fromBranchKnName;
	@Basic(optional = false)
	@Column(name = "TO_BANK_KN_NAME")
	public String toBankKnName;
	@Basic(optional = false)
	@Column(name = "TO_BRANCH_KN_NAME")
	public String toBranchKnName;
	@Basic(optional = false)
	@Column(name = "TO_ACCOUNT_KN_NAME")
	public String toAccountKnName;
	@Basic(optional = false)
	@Column(name = "DEPCD")
	public String depcd;
	@Basic(optional = false)
	@Column(name = "PAYMENT_MNY")
	public BigDecimal paymentMny;
	@Basic(optional = false)
	@Column(name = "PROCESSING_YM")
	public int processingYm;

	public QbkdtBankTransfer(QbkdtBankTransferPK qbkdtBankTransferPK) {
		this.qbkdtBankTransferPK = qbkdtBankTransferPK;
	}

	public QbkdtBankTransfer(String ccd, String pid, String fromBranchId, int fromAccountAtr, String fromAccountNo,
			String toBranchId, int toAccountAtr, String toAccountNo, int payBonusAtr, int processingNo,
			GeneralDate payDate, int sparePayAtr) {
		this.qbkdtBankTransferPK = new QbkdtBankTransferPK(ccd, pid, fromBranchId, fromAccountAtr, fromAccountNo,
				toBranchId, toAccountAtr, toAccountNo, payBonusAtr, processingNo, payDate, sparePayAtr);
	}

	@Override
	protected QbkdtBankTransferPK getKey() {
		return this.qbkdtBankTransferPK;
	}
}
