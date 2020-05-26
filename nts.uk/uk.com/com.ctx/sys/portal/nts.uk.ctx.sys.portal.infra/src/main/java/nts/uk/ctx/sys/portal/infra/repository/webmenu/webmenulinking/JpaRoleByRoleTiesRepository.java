package nts.uk.ctx.sys.portal.infra.repository.webmenu.webmenulinking;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenuCode;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleByRoleTies;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleByRoleTiesRepository;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.webmenulinking.SacmtRoleByRoleTies;

@Stateless
public class JpaRoleByRoleTiesRepository extends JpaRepository implements  RoleByRoleTiesRepository {

	private static final String  GET_ROLE_BY_ROLE_TIES_BY_CODE = "SELECT c FROM SacmtRoleByRoleTies c "
			+ " WHERE c.roleId  = :roleId ";
	
	@Override
	public void insertRoleByRoleTies(RoleByRoleTies roleByRoleTies) {
		this.commandProxy().insert(SacmtRoleByRoleTies.toEntity(roleByRoleTies));
		
	}

	@Override
	public void updateRoleByRoleTies(RoleByRoleTies roleByRoleTies) {
		SacmtRoleByRoleTies dataUpdate = SacmtRoleByRoleTies.toEntity(roleByRoleTies);
		SacmtRoleByRoleTies newData = this.queryProxy().find(dataUpdate.roleId, SacmtRoleByRoleTies.class).get();
		newData.setWebMenuCd(dataUpdate.webMenuCd);
		
	}

	@Override
	public void deleteRoleByRoleTies(String roleId) {
		this.commandProxy().remove(SacmtRoleByRoleTies.class,roleId);
	}

	@Override
	public Optional<RoleByRoleTies> getRoleByRoleTiesById(String roleId) {
		Optional<RoleByRoleTies> data = this.queryProxy().query(GET_ROLE_BY_ROLE_TIES_BY_CODE,SacmtRoleByRoleTies.class)
				.setParameter("roleId", roleId)
				.getSingle(c->c.toDomain());
		return data;
	}
	
	@Override
	public Optional<RoleByRoleTies> getByRoleIdAndCompanyId(String roleId, String companyId) {
		String sql = 
				"select * from SACMT_ROLE_BY_ROLE_TIES with(index(SACMP_ROLE_BY_ROLE_TIES)) " +
				"where ROLE_ID = @roleId " +
				"and (CID = @companyId OR CID = '000000000000-0000')";
		
		Optional<RoleByRoleTies> opRoleByRoleTies = new NtsStatement(sql, this.jdbcProxy())
				.paramString("roleId", roleId)
				.paramString("companyId", companyId)
				.getSingle(rec -> {
					return new RoleByRoleTies(
							roleId, 
							new WebMenuCode(rec.getString("WEB_MENU_CD")), 
							companyId);
				});
		
		return opRoleByRoleTies;
	}




}
