package nts.uk.ctx.at.shared.infra.entity.worktype.specialholidayframe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

@Entity
@Table(name="KSHMT_HDSP_FRAME")
@AllArgsConstructor
@NoArgsConstructor
public class KshmtSpecialHolidayFrame extends ContractUkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KshmtSpecialHolidayFramePK kshmtSpecialHolidayFramePK;
	/*枠名称*/
	@Column(name = "NAME")
	public String name;
	/*特別休暇枠の廃止区分*/
	@Column(name = "USE_ATR")
	public int useAtr;
	/** **/
	@Column(name = "TIME_MNG_ATR")
	public int timeMngAtr;
	
	@Override
	protected Object getKey() {		
		return kshmtSpecialHolidayFramePK;
	}
}
