package nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPDMT_SYSTEM_DAY")
public class QpdmtSystemDay extends UkJpaEntity implements Serializable {

	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public QpdmtSystemDayPK qpdmtSystemDayPk;

	@Basic(optional = false)
	@Column(name = "PAY_STD_DAY")
	public int payStdDay;

	@Basic(optional = false)
	@Column(name = "PICKUP_STD_MON_ATR")
	public int pickupStdMonAtr;

	@Basic(optional = false)
	@Column(name = "PICKUP_STD_DAY")
	public int pickupStdDay;

	@Basic(optional = false)
	@Column(name = "SOCIAL_INSU_LEVY_MON_ATR")
	public int socialInsuLevyMonAtr;

	@Basic(optional = false)
	@Column(name = "ACCOUNT_DUE_MON_ATR")
	public int accountDueMonAtr;

	@Basic(optional = false)
	@Column(name = "ACCOUNT_DUE_DAY")
	public int accountDueDay;

	@Basic(optional = false)
	@Column(name = "PAYSLIP_PRINT_MONTH_ATR")
	public int payslipPrintMonthAtr;

	@Override
	protected Object getKey() {
		return this.qpdmtSystemDayPk;
	}
}
