package nts.uk.ctx.sys.portal.infra.entity.webmenu;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CCGST_WEB_MENU")
public class CcgstWebMenu extends UkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public CcgstWebMenuPK ccgstWebMenuPK;
	
	@Column(name = "WEB_MENU_NAME")
	public String webMenuName;
	
	@Column(name = "DEFAULT_MENU")
	public int defaultMenu;

	@Override
	protected Object getKey() {
		
		return ccgstWebMenuPK;
	}

}
