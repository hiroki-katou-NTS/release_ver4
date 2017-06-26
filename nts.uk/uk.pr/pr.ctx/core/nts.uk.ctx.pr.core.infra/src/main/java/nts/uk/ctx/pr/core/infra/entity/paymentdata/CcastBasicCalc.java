package nts.uk.ctx.pr.core.infra.entity.paymentdata;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.AggregateTableEntity;

@Entity
@Table(name="CCAST_BASIC_CALC")
@AllArgsConstructor
@NoArgsConstructor
public class CcastBasicCalc {

	@Id
	@Column(name = "CCD")
	public String ccd;
	
	@Column(name = "BASE_DAYS")
	public BigDecimal baseDays;
	
	@Column(name = "BASE_HOURS")
	public BigDecimal baseHours;
}
