package nts.uk.ctx.pr.core.infra.entity.personalinfo.wage;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.AggregateTableEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="PPRMT_PERSON_WAGE")
public class PprmtPersonWage {
	@EmbeddedId
    public PprmtPersonWagePK pprmtPersonWagePK;
	
	@Column(name = "END_YM")
	public int endYm;
	
	@Column(name = "VAL")
	public BigDecimal val;
}
