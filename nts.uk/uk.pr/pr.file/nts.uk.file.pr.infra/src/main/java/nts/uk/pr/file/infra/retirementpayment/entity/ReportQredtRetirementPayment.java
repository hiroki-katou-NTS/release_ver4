package nts.uk.pr.file.infra.retirementpayment.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * @author hungnm
 *
 */
@Entity
@Table(name = "QREDT_RETIREMENT_PAYMENT")
public class ReportQredtRetirementPayment extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public ReportQredtRetirementPaymentPK reportQredtRetirementPaymentPK;

	@Column(name = "TRIAL_PERIOD_SET")
	public BigDecimal trialPeriodSet;

	@Column(name = "EXCLUSION_YEARS")
	public BigDecimal exclusionYears;

	@Column(name = "ADDITIONAL_BOARD_YEARS")
	public BigDecimal additionalBoardYears;

	@Column(name = "BOARD_YEARS")
	public BigDecimal boardYears;

	@Column(name = "TOTAL_PAYMENT_MNY")
	public BigDecimal totalPaymentMoney;

	@Column(name = "DEDUCTION1_MNY")
	public BigDecimal deductionMoney1;

	@Column(name = "DEDUCTION2_MNY")
	public BigDecimal deductionMoney2;

	@Column(name = "DEDUCTION3_MNY")
	public BigDecimal deductionMoney3;

	@Column(name = "OTHER_RETIREMENT_PAY_OP")
	public BigDecimal otherRetirementPayOptional;

	@Column(name = "TAX_CAL_METHOD_SET")
	public BigDecimal taxCalMethodSet;

	@Column(name = "INCOME_TAX_MNY")
	public BigDecimal incomeTaxMoney;

	@Column(name = "CITY_TAX_MNY")
	public BigDecimal cityTaxMoney;

	@Column(name = "PREFECTURE_TAX_MNY")
	public BigDecimal prefectureTaxMoney;

	@Column(name = "TOTAL_DEDUCTION_MNY")
	public BigDecimal totalDeductionMoney;

	@Column(name = "ACTUAL_RECIEVE_MNY")
	public BigDecimal actualRecieveMoney;

	@Column(name = "BANK_TRANSFER_OP1")
	public BigDecimal bankTransferOptional1;

	@Column(name = "OP1_MNY")
	public BigDecimal optionalMoney1;

	@Column(name = "BANK_TRANSFER_OP2")
	public BigDecimal bankTransferOptional2;

	@Column(name = "OP2_MNY")
	public BigDecimal optionalMoney2;

	@Column(name = "BANK_TRANSFER_OP3")
	public BigDecimal bankTransferOptional3;

	@Column(name = "OP3_MNY")
	public BigDecimal optionalMoney3;

	@Column(name = "BANK_TRANSFER_OP4")
	public BigDecimal bankTransferOptional4;

	@Column(name = "OP4_MNY")
	public BigDecimal optionalMoney4;

	@Column(name = "BANK_TRANSFER_OP5")
	public BigDecimal bankTransferOptional5;

	@Column(name = "OP5_MNY")
	public BigDecimal optionalMoney5;

	@Column(name = "WITHHOLDING_MENO")
	public String withHoldingMemo;

	@Column(name = "STATEMENT_MEMO")
	public String statementMemo;

	public ReportQredtRetirementPayment() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	public Object getKey() {
		// TODO Auto-generated method stub
		return this.reportQredtRetirementPaymentPK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((reportQredtRetirementPaymentPK == null) ? 0 : reportQredtRetirementPaymentPK.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportQredtRetirementPayment other = (ReportQredtRetirementPayment) obj;
		if (reportQredtRetirementPaymentPK == null) {
			if (other.reportQredtRetirementPaymentPK != null)
				return false;
		} else if (!reportQredtRetirementPaymentPK.equals(other.reportQredtRetirementPaymentPK))
			return false;
		return true;
	}

}
