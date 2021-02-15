package nts.uk.ctx.pereg.infra.entity.person.info.setting.initvalue;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PPEMT_PER_INIT_SET_CTG")
public class PpemtPersonInitValueSettingCtg extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public PpemtPersonInitValueSettingCtgPk settingCtgPk;

	@Override
	protected Object getKey() {
		return settingCtgPk;
	}

}
