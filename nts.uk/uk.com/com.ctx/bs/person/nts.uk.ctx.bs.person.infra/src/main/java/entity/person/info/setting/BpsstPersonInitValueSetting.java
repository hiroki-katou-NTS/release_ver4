package entity.person.info.setting;

import java.io.Serializable;

import javax.persistence.Basic;
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
@Table(name = "BPSST_PER_INIT_SET")
public class BpsstPersonInitValueSetting extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public BpsstPersonInitValueSettingPk bpsstPersonInitValueSettingPk;

	@Basic(optional = false)
	@Column(name = "CID")
	public String companyId;

	@Basic(optional = false)
	@Column(name = "PER_INIT_SET_CODE")
	public String settingCode;

	@Basic(optional = false)
	@Column(name = "PER_INIT_SET_NAME")
	public String settingName;

	@Override
	protected Object getKey() {
		return bpsstPersonInitValueSettingPk;
	}

}
