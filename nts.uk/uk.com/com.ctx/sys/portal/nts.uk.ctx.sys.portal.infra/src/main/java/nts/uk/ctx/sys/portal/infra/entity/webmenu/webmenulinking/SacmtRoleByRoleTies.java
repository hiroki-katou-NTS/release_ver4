package nts.uk.ctx.sys.portal.infra.entity.webmenu.webmenulinking;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenuCode;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleByRoleTies;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@Entity
@Table(name = "SACMT_ROLE_BY_ROLE_TIES")
@Setter
public class SacmtRoleByRoleTies extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ROLE_ID")
	public String roleId;
	
	@Column(name = "WEB_MENU_CD")
	public String webMenuCd;
	
	@Override
	protected Object getKey() {
		return this.roleId;
	}

	
	public static SacmtRoleByRoleTies toEntity(RoleByRoleTies domain) {
		return new SacmtRoleByRoleTies(
				domain.getRoleId(),
				domain.getWebMenuCd().v()
				);
	}
	
	public RoleByRoleTies toDomain() {
		return new RoleByRoleTies(
				this.roleId,
				new WebMenuCode(this.webMenuCd)
				);
	}


	public SacmtRoleByRoleTies(String roleId,String webMenuCd) {
		super();
		this.roleId = roleId;
		this.webMenuCd = webMenuCd;
	}
}
