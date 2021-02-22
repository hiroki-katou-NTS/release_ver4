package nts.uk.ctx.sys.portal.infra.entity.webmenu.smartphonemenu;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 
 * スマホメニュー並び順
 * 
 * @author sonnh1
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SPTMT_SP_MENU_SORT")
public class SptmtSPMenuSort extends ContractUkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public SptmtSPMenuSortPK pk;

	@Column(name = "SORT")
	public int sortOrder;

	@Override
	protected Object getKey() {
		return this.pk;
	}
}
