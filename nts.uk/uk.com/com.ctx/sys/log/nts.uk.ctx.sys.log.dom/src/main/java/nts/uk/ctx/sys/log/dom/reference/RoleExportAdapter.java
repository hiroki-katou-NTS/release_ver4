package nts.uk.ctx.sys.log.dom.reference;

import java.util.List;
import java.util.Map;

public interface RoleExportAdapter {
	String getNameByRoleId(String roleId);
	
	Map<String, String> getNameLstByRoleIds(String cid, List<String> roleIds);

}
