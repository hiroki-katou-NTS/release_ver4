package nts.uk.ctx.pr.core.infra.entity.paymentdata;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "QSTDT_PAYMENT_DETAIL")
public class QstdtPaymentDetail {

	@EmbeddedId
	public QstdtPaymentDetailPK qstdtPaymentDetailPK;

	@Column(name = "INV_SCD")
	public String employeeCode;

	@Basic(optional = false)
	@Column(name = "VAL")
	public BigDecimal value;

	@Basic(optional = false)
	@Column(name = "CORRECT_FLG")
	public int correctFlag;

	@Basic(optional = false)
	@Column(name = "TAX_ATR")
	public int taxATR;

	@Basic(optional = false)
	@Column(name = "LIMIT_MNY")
	public int limitAmount;

	@Basic(optional = false)
	@Column(name = "SOCIAL_INS_ATR")
	public int socialInsurranceAttribute;

	@Basic(optional = false)
	@Column(name = "LABOR_INS_ATR")
	public int laborSubjectAttribute;

	@Basic(optional = false)
	@Column(name = "FIX_PAY_ATR")
	public int fixPayATR;

	@Basic(optional = false)
	@Column(name = "AVE_PAY_ATR")
	public int averagePayATR;

	@Basic(optional = false)
	@Column(name = "DEDUCT_ATR")
	public int deductAttribute;

	@Basic(optional = false)
	@Column(name = "ITEM_ATR")
	public int itemAtr;

	@Basic(optional = false)
	@Column(name = "COMMU_ALLOW_TAX_IMPOSE")
	public BigDecimal commuteAllowTaxImpose;

	@Basic(optional = false)
	@Column(name = "COMMU_ALLOW_MONTH")
	public BigDecimal commuteAllowMonth;

	@Basic(optional = false)
	@Column(name = "COMMU_ALLOW_FRACTION")
	public BigDecimal commuteAllowFraction;

	@Basic(optional = false)
	@Column(name = "PRINT_LINE_POS")
	public int printLinePosition;

	@Basic(optional = false)
	@Column(name = "ITEM_POS_COLUMN")
	public int columnPosition;

	@ManyToOne(optional = false)
	@JoinColumns({ @JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "PID", referencedColumnName = "PID", insertable = false, updatable = false),
			@JoinColumn(name = "PROCESSING_NO", referencedColumnName = "PROCESSING_NO", insertable = false, updatable = false),
			@JoinColumn(name = "PAY_BONUS_ATR", referencedColumnName = "PAY_BONUS_ATR", insertable = false, updatable = false),
			@JoinColumn(name = "PROCESSING_YM", referencedColumnName = "PROCESSING_YM", insertable = false, updatable = false),
			@JoinColumn(name = "SPARE_PAY_ATR", referencedColumnName = "SPARE_PAY_ATR", insertable = false, updatable = false) })
	public QstdtPaymentHeader qstdtPaymentHeader;
}
