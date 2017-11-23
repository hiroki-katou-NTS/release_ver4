package nts.uk.ctx.bs.person.infra.entity.person.info.setting.initvalue;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PpemtPersonInitValueSettingCtgPk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "PER_INFO_CTG_ID")
	public String perInfoCtgId;
	
	@Basic(optional = false)
	@Column(name = "PER_INIT_SET_ID")
	public String settingId;


}
