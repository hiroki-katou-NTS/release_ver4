package nts.uk.ctx.at.auth.infra.repository.wplmanagementauthority;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.auth.dom.wplmanagementauthority.WorkPlaceAuthority;
import nts.uk.ctx.at.auth.dom.wplmanagementauthority.WorkPlaceAuthorityRepository;
import nts.uk.ctx.at.auth.infra.entity.wplmanagementauthority.KacmtWorkPlaceAuthority;

@Stateless
public class JpaWorkPlaceAuthotityRepository  extends JpaRepository implements WorkPlaceAuthorityRepository {

	private static final String  GET_ALL_WRK_AUTHORITY = "SELECT c FROM KacmtWorkPlaceAuthority "
			+ " WHERE c.kacmtWorkPlaceAuthorityPK.companyId  = :companyId ";
	private static final String  GET_ALL_WRK_AUTHORITY_BY_ROLE_ID = GET_ALL_WRK_AUTHORITY
			+ " AND c.kacmtWorkPlaceAuthorityPK.roleId  = :roleId ";
	private static final String  GET_WRK_AUTHORITY_BY_ID = GET_ALL_WRK_AUTHORITY_BY_ROLE_ID
			+ " AND c.kacmtWorkPlaceAuthorityPK.functionNo = :functionNo ";
	private static final String DELETE_FROM_AUTHORITY = "DELETE FROM KacmtWorkPlaceAuthority c "
			+ " WHERE c.kacmtWorkPlaceAuthorityPK.roleId  = :roleId"
			+ " AND c.kacmtWorkPlaceAuthorityPK.companyId  = :companyId  ";
	

	@Override
	public List<WorkPlaceAuthority> getAllWorkPlaceAuthority(String companyId) {
		List<WorkPlaceAuthority> data = this.queryProxy().query(GET_ALL_WRK_AUTHORITY,KacmtWorkPlaceAuthority.class)
				.setParameter("companyId", companyId)
				.getList(c->c.toDomain());
		return data;
	}

	@Override
	public List<WorkPlaceAuthority> getAllWorkPlaceAuthorityByRoleId(String companyId, String roleId) {
		List<WorkPlaceAuthority> data = this.queryProxy().query(GET_ALL_WRK_AUTHORITY_BY_ROLE_ID,KacmtWorkPlaceAuthority.class)
				.setParameter("companyId", companyId)
				.setParameter("roleId", roleId)
				.getList(c->c.toDomain());
		return data;
	}

	@Override
	public Optional<WorkPlaceAuthority> getWorkPlaceAuthorityById(String companyId, String roleId, int functionNo) {
		Optional<WorkPlaceAuthority> data = this.queryProxy().query(GET_WRK_AUTHORITY_BY_ID,KacmtWorkPlaceAuthority.class)
				.setParameter("companyId", companyId)
				.setParameter("roleId", roleId)
				.setParameter("functionNo", functionNo)
				.getSingle(c->c.toDomain());
		return data;
	}

	@Override
	public void addWorkPlaceAuthority(WorkPlaceAuthority workPlaceAuthotity) {
		this.commandProxy().insert(KacmtWorkPlaceAuthority.toEntity(workPlaceAuthotity));
	}

	@Override
	public void updateWorkPlaceAuthority(WorkPlaceAuthority workPlaceAuthotity) {
		KacmtWorkPlaceAuthority dataUpdate = KacmtWorkPlaceAuthority.toEntity(workPlaceAuthotity);
		KacmtWorkPlaceAuthority newData = this.queryProxy().find(dataUpdate.kacmtWorkPlaceAuthorityPK, KacmtWorkPlaceAuthority.class).get();
		newData.setAvailability(dataUpdate.availability);
		this.commandProxy().update(newData);
	}

	@Override
	public void deleteWorkPlaceAuthority(String companyId, String roleId) {
		this.queryProxy().query(DELETE_FROM_AUTHORITY,KacmtWorkPlaceAuthority.class)
		.setParameter("roleId", roleId)
		.setParameter("companyId", companyId);
		
	}
	
	

}
