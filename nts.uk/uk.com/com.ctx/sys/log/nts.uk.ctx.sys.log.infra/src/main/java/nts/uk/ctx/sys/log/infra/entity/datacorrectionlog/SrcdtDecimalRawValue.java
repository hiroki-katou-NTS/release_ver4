package nts.uk.ctx.sys.log.infra.entity.datacorrectionlog;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;


/**
 * 
 * @author HungTT
 *
 */

@Entity
@Table(name = "SRCDT_DECIMAL_RAW_VALUE")
@NoArgsConstructor
@AllArgsConstructor
public class SrcdtDecimalRawValue extends UkJpaEntity {

	@Id
	@Column(name = "ID")
	String id;
	
	@Column(name = "VALUE")
	@Basic(optional = false)
	BigDecimal value;

	@OneToOne(mappedBy = "rawDecimalValueBefore")
	SrcdtDataCorrectionLog beforeLog;

	@OneToOne(mappedBy = "rawDecimalValueAfter")
	SrcdtDataCorrectionLog afterLog;

	@Override
	protected Object getKey() {
		return this.id;
	}

}
