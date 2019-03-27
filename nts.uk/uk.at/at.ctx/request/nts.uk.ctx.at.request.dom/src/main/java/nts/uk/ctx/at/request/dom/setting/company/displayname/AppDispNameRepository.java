package nts.uk.ctx.at.request.dom.setting.company.displayname;

import java.util.List;
import java.util.Optional;
/**
 * the display name setting interface
 * @author yennth
 *
 */
public interface AppDispNameRepository {
	/**
	 * get all display name by company Id
	 * @return
	 * @author yennth
	 */
	List<AppDispName> getAll();
	
	List<AppDispName> getAll(List<Integer> appType);
	
	/**
	 * get display name by company id and apptype
	 * @return
	 * @author yennth
	 */
	Optional<AppDispName> getDisplay(int appType);
	/**
	 * update display name
	 * @param display
	 * @author yennth
	 */
	void update(AppDispName display);
	/**
	 * insert display name
	 * @param display
	 * @author yennth
	 */
	void insert(AppDispName display);
}
