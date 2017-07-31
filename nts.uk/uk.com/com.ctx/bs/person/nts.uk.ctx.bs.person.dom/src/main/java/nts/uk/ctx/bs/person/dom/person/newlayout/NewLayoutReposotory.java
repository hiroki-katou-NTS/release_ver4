/**
 * 
 */
package nts.uk.ctx.bs.person.dom.person.newlayout;

import java.util.List;
import java.util.Optional;


/**
 * @author laitv
 *
 */
public interface NewLayoutReposotory {
	
	void add(NewLayout newLayout);
	
	void update(NewLayout newLayout);
	
	void remove(String layoutID);
	
	Optional<NewLayout> getDetailNewLayout(String layoutID);
	
	List<NewLayout> getAllNewLayout();

}
