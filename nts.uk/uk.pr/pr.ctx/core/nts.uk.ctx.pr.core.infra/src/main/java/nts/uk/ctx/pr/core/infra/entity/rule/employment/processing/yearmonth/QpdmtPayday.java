package nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPDMT_PAYDAY")
public class QpdmtPayday extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public QpdmtPaydayPK qpdmtPaydayPK;

	@Basic(optional = false)
	@Column(name = "PAY_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate payDate;

	@Basic(optional = false)
	@Column(name = "STD_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate stdDate;

	@Basic(optional = false)
	@Column(name = "SOCIAL_INS_LEVY_MON")
	public int socialInsLevyMon;

	@Basic(optional = false)
	@Column(name = "SOCIAL_INS_STD_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate socialInsStdDate;

	@Basic(optional = false)
	@Column(name = "INCOME_TAX_STD_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate incomeTaxStdDate;
	
	@Basic(optional = false)
	@Column(name = "EMP_INS_STD_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate empInsStdDate;

	@Basic(optional = false)
	@Column(name = "STMT_OUTPUT_MON")
	public int stmtOutputMon;

	@Basic(optional = false)
	@Column(name = "NEEDED_WORK_DAY")
	public BigDecimal neededWorkDay;

	@Basic(optional = false)
	@Column(name = "ACCOUNTING_CLOSING")
	public GeneralDate accountingClosing;

	@Override
	protected Object getKey() {
		return qpdmtPaydayPK;
	}
}
