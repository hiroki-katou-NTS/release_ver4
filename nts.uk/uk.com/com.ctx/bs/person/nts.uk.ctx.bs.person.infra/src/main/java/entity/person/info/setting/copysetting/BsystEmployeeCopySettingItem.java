package entity.person.info.setting.copysetting;

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
@Table(name = "PPEST_EMP_COPY_SET_ITEM")
public class BsystEmployeeCopySettingItem extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public BsystEmployeeCopySettingItemPk BsystEmployeeCopySettingItemPk;

	@Basic(optional = false)
	@Column(name = "EMP_COPY_CTG_ID")
	public String categoryId;

	@Override
	protected Object getKey() {
		return BsystEmployeeCopySettingItemPk;
	}
}
