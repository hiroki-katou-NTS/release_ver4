package nts.uk.ctx.pr.transfer.infra.entity.emppaymentinfo;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.EmployeeSalaryPaymentMethod;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.PaymentMethodDetail;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.TransferInfor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author HungTT - 社員給与支払方法
 *
 */

@NoArgsConstructor
@Entity
@Table(name = "QBTMT_EMP_SAL_PAY_METHOD")
public class QbtmtEmpSalPayMethod extends UkJpaEntity {

	@EmbeddedId
	public QbtmtEmpPayMethodPk pk;

	@Column(name = "USE_ATR")
	public int useAtr;

	@Column(name = "PAY_PROPORTION_ATR")
	@Basic(optional = true)
	public Integer paymentProportionAtr;

	@Column(name = "PAY_METHOD")
	@Basic(optional = true)
	public Integer paymentMethod;

	@Column(name = "PAY_RATE")
	@Basic(optional = true)
	public Integer paymentRate;

	@Column(name = "PAY_AMOUNT")
	@Basic(optional = true)
	public Long paymentAmount;

	@Column(name = "PAY_PRIORITY")
	@Basic(optional = true)
	public Integer paymentPriority;

	@Column(name = "SRC_BANK_CD")
	@Basic(optional = true)
	public String sourceBankCode;

	@Column(name = "DES_ACCOUNT_NUMBER")
	@Basic(optional = true)
	public String desAccountNumber;

	@Column(name = "DES_ACCOUNT_NAME")
	@Basic(optional = true)
	public String desAccountName;

	@Column(name = "DES_ACCOUNT_KANA_NAME")
	@Basic(optional = true)
	public String desAccountKanaName;

	@Column(name = "DES_BANK_BRANCH_ID")
	@Basic(optional = true)
	public String desBranchId;

	@Column(name = "DES_DEPOSIT_TYPE")
	@Basic(optional = true)
	public Integer desDepositType;

	@Override
	protected Object getKey() {
		return this.pk;
	}

	public QbtmtEmpSalPayMethod(String historyId, int paymentMethodNo, int useAtr, Integer paymentProportionAtr,
			Integer paymentMethod, Integer paymentRate, Long paymentAmount, Integer paymentPriority,
			String sourceBankCode, String desAccountNumber, String desAccountName, String desAccountKanaName,
			String desBranchId, Integer desDepositType) {
		super();
		this.pk = new QbtmtEmpPayMethodPk(historyId, paymentMethodNo);
		this.useAtr = useAtr;
		this.paymentProportionAtr = paymentProportionAtr;
		this.paymentMethod = paymentMethod;
		this.paymentRate = paymentRate;
		this.paymentAmount = paymentAmount;
		this.paymentPriority = paymentPriority;
		this.sourceBankCode = sourceBankCode;
		this.desAccountNumber = desAccountNumber;
		this.desAccountName = desAccountName;
		this.desAccountKanaName = desAccountKanaName;
		this.desBranchId = desBranchId;
		this.desDepositType = desDepositType;
	}

	public QbtmtEmpSalPayMethod(String historyId, PaymentMethodDetail paymentMethodDetail) {
		super();
		this.pk = new QbtmtEmpPayMethodPk(historyId, paymentMethodDetail.getPaymentMethodNo());
		this.useAtr = paymentMethodDetail.getUseAtr().value ? 1 : 0;
		if (paymentMethodDetail.getUseAtr().value) {
			this.paymentMethod = paymentMethodDetail.getPaymentMethod().get().value ? 1 : 0;
			if (paymentMethodDetail.getPaymentMethod().get().value
					&& paymentMethodDetail.getTransferInfor().isPresent()) {
				this.sourceBankCode = paymentMethodDetail.getTransferInfor().get().getSourceBankBranchInfor().v();
				this.desAccountNumber = paymentMethodDetail.getTransferInfor().get().getDesAccountNumber().v();
				this.desAccountName = paymentMethodDetail.getTransferInfor().get().getDesAccountName().v();
				this.desAccountKanaName = paymentMethodDetail.getTransferInfor().get().getDesAccountKanaName().v();
				this.desBranchId = paymentMethodDetail.getTransferInfor().get().getDesBankBranchInfor();
				this.desDepositType = paymentMethodDetail.getTransferInfor().get().getDesDepositType().value;
			}
			this.paymentProportionAtr = paymentMethodDetail.getPaymentProportionAtr().isPresent()
					? paymentMethodDetail.getPaymentProportionAtr().get().value : null;
			this.paymentPriority = paymentMethodDetail.getPaymentPriority().isPresent()
					? paymentMethodDetail.getPaymentPriority().get().value : null;
			if (paymentProportionAtr != null) {
				switch (paymentProportionAtr) {
				case 0: // 定率 - FIXED_RATE
					this.paymentRate = paymentMethodDetail.getPaymentRate().isPresent()
							? paymentMethodDetail.getPaymentRate().get().v() : null;
					break;
				case 1: // 定額 - FIXED_AMOUNT
					this.paymentAmount = paymentMethodDetail.getPaymentAmount().isPresent()
							? paymentMethodDetail.getPaymentAmount().get().v() : null;
					break;
				default: // 全額 - FULL_AMOUNT
					break;
				}
			}
		}
	}

	public static EmployeeSalaryPaymentMethod toDomain(List<QbtmtEmpSalPayMethod> listEntity) {
		if (listEntity.isEmpty())
			return null;
		List<PaymentMethodDetail> listPaymentMethod = listEntity.stream()
				.map(m -> m.useAtr == 1
						? new PaymentMethodDetail(true,
								m.pk.paymentMethodNo,
								m.paymentMethod == 1 ? new TransferInfor(m.sourceBankCode, m.desAccountKanaName,
										m.desAccountName, m.desAccountNumber, m.desBranchId, m.desDepositType) : null,
								m.paymentProportionAtr, m.paymentMethod == 1 ? true : false, m.paymentRate,
								m.paymentAmount, m.paymentPriority)
						: new PaymentMethodDetail(false, m.pk.paymentMethodNo, null, null, null, null, null, null))
				.collect(Collectors.toList());
		return new EmployeeSalaryPaymentMethod(listEntity.get(0).pk.historyId, listPaymentMethod);
	}

	public static List<QbtmtEmpSalPayMethod> fromDomain(EmployeeSalaryPaymentMethod domain) {
		return domain.getListPaymentMethod().stream().map(m -> new QbtmtEmpSalPayMethod(domain.getHistoryId(), m))
				.collect(Collectors.toList());
	}
	
}
