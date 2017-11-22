package nts.uk.ctx.at.record.infra.entity.bonuspay;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KBPST_PS_BP_SET")
public class KbpstPersonalBPSetting extends UkJpaEntity implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public KbpstPersonalBPSettingPK kbpstPersonalBPSettingPK;
	
	@Column(name = "BONUS_PAY_SET_CD")
	public String bonusPaySettingCode;

	@Override
	protected Object getKey() {
		return kbpstPersonalBPSettingPK;
	}
}
