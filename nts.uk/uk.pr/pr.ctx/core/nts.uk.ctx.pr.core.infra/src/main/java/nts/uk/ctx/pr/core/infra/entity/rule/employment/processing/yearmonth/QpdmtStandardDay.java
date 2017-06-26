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
@Table(name = "QPDMT_STANDARD_DAY")
public class QpdmtStandardDay extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public QpdmtStandardDayPK qpdmtStandardDayPk;

	@Basic(optional = false)
	@Column(name = "SOCIAL_INS_STD_YEAR_ATR")
	public int socialInsStdYearAtr;
	
	@Basic(optional = false)
	@Column(name = "SOCIAL_INS_STD_MON")
	public int socialInsStdMon;
	
	@Basic(optional = false)
	@Column(name = "SOCIAL_INS_STD_DAY")
	public int socialInsStdDay;
	
	@Basic(optional = false)
	@Column(name = "EMP_INS_STD_MON")
	public int empInsStdMon;
	
	@Basic(optional = false)
	@Column(name = "EMP_INS_STD_DAY")
	public int empInsStdDay;
	
	@Basic(optional = false)
	@Column(name = "INCOMETAX_STD_YEAR_ATR")
	public int incometaxStdYearAtr;
	
	@Basic(optional = false)
	@Column(name = "INCOMETAX_STD_MON")
	public int incometaxStdMon;
	
	@Basic(optional = false)
	@Column(name = "INCOMETAX_STD_DAY")
	public int incometaxStdDay;

	@Override
	protected Object getKey() {
		return qpdmtStandardDayPk;
	}
	
}
