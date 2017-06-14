package nts.uk.ctx.at.record.infra.entity.standardtime;

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
@Table(name="KMKMT_AGREEMENT_OPE_SET")
public class KmkmtAgeementOperationSetting extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    public KmkmtAgeementOperationSettingPK kmkmtAgeementOperationSettingPK;
	
	@Column(name ="STARTING_MONTH_ATR")
	public BigDecimal startingMonthAtr;
	
	@Column(name ="NUMBER_OF_TIMES_OVER_LIMIT_ATR")
	public BigDecimal numberOfTimesOverLimitAtr;
	
	@Column(name ="CLOSING_DATE_ATR")
	public BigDecimal closingDateAtr;
	
	@Column(name ="CLOSING_DATE_TYPE")
	public BigDecimal closingDateType;
	
	@Column(name ="TARGET_SETTING_TYPE")
	public BigDecimal targetSettingType;
	
	@Override
	protected Object getKey() {
		return this.kmkmtAgeementOperationSettingPK;
	}
}
