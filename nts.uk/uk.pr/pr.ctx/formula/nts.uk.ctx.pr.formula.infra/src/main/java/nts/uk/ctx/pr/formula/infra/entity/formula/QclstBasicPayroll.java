package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "QCLST_BASIC_CALC")
public class QclstBasicPayroll extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public QclstBasicPayrollPK qclstBasicPayrollPK;
	
	@Column(name ="TIME_NOTATION_SET")
	public BigDecimal timeNotationSetting;
	
	@Column(name ="BASE_DAYS")
	public BigDecimal baseDays;
	
	@Column(name ="BASE_HOURS")
	public BigDecimal baseHours;

	@Override
	protected Object getKey() {
		return this.qclstBasicPayrollPK;
	}

}
