/**
 * 
 */
package nts.uk.ctx.bs.person.dom.person.layout;

import java.util.List;
import java.util.Optional;

/**
 * @author laitv
 * 
 */
public interface IMaintenanceLayoutRepository {

	void add(MaintenanceLayout maintenanceLayout);

	void update(MaintenanceLayout maintenanceLayout);

	void remove(String maintenanceLayoutID);

	boolean checkExit(String cpmpanyId, String layoutCode);

	List<MaintenanceLayout> getAllMaintenanceLayout();

	Optional<MaintenanceLayout> getById(String cid ,String id);

}
