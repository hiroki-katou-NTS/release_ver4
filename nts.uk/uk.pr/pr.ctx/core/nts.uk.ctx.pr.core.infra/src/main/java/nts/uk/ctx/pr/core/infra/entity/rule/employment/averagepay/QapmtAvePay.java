package nts.uk.ctx.pr.core.infra.entity.rule.employment.averagepay;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="QAPMT_AVE_PAY")
public class QapmtAvePay extends UkJpaEntity {
	@Id
	@Column(name="CCD")
	public String companyCode;
	
	@Column(name="ROUND_TIMING_SET")
	public int roundTimingSet;
	
	@Column(name="ATTEND_DAY_GETTING_SET")
	public int attendDayGettingSet;
	
	@Column(name="ROUND_DIGIT_SET")
	public int roundDigitSet;
	
	@Column(name="EXCEPTION_PAY_RATE")
	public BigDecimal exceptionPayRate;
	
	@Override
	protected Object getKey() {
		return companyCode;
	}
}
