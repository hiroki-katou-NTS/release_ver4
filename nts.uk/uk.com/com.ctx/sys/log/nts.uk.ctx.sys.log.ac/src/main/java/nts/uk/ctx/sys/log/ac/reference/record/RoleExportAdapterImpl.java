package nts.uk.ctx.sys.log.ac.reference.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

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
		List<RoleExport> rsLst = roleExportRepo.findByListRoleId(cid, roleIds);
		if(rsLst != null && !rsLst.isEmpty()) {
			Map<String, String> result = rsLst.stream()
			.collect(Collectors.toMap(c -> c.getRoleId(), c -> c.getRoleName()));
			return result;
		}
		return new HashMap<>();
	}

}
