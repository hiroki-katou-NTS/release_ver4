package nts.uk.ctx.sys.portal.infra.entity.webmenu;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CCGST_TREE_MENU")
public class CcgstTreeMenu extends UkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public CcgstTreeMenuPK ccgstTreeMenuPK;
	
	@Column(name = "CODE")
	public String code;
	
	@Column(name = "CLASSIFICATION")
	public int classification;
	
	@Column(name = "SYSTEM")
	public int system;

	@ManyToOne
	@JoinColumns( {
        @JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
        @JoinColumn(name = "WEB_MENU_CD", referencedColumnName = "WEB_MENU_CD", insertable = false, updatable = false),
        @JoinColumn(name = "WEB_MENU_CD", referencedColumnName = "WEB_MENU_CD", insertable = false, updatable = false)
    })
	public CcgstTitleMenu titleMenu;
	
	@Override
	protected Object getKey() {
		
		return ccgstTreeMenuPK;
	}

	public CcgstTreeMenu(CcgstTreeMenuPK ccgstTreeMenuPK, String code, int classification, int system) {
		super();
		this.ccgstTreeMenuPK = ccgstTreeMenuPK;
		this.code = code;
		this.classification = classification;
		this.system = system;
	}
  
}
