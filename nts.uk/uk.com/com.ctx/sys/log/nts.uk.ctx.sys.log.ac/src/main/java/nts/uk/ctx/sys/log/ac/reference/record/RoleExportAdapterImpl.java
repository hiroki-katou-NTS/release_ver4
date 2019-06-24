package nts.uk.ctx.sys.log.ac.reference.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.auth.pub.role.RoleExport;
import nts.uk.ctx.sys.auth.pub.role.RoleExportRepo;
import nts.uk.ctx.sys.log.dom.reference.RoleExportAdapter;
/**
 *  author: huannv
 */

@Stateless
public class RoleExportAdapterImpl implements RoleExportAdapter {
	
	@Inject
	RoleExportRepo roleExportRepo;
	
	@Override
	public String getNameByRoleId(String roleId) {
		String nameRole=""; 
		Optional<RoleExport> rs=roleExportRepo.findByRoleId(roleId);
		if(rs.isPresent()){
			RoleExport roleExport=rs.get();
			nameRole=roleExport.getRoleName();
		}
		return nameRole;
	}

	@Override
	public Map<String, String> getNameLstByRoleIds(String cid, List<String> roleIds) {
		if(CollectionUtil.isEmpty(roleIds)) return new HashMap<>();
		Map<String, String> result = roleExportRepo.getNameLstByRoleIds(cid, roleIds);
		return result;
	}

}
