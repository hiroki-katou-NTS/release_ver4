/**
 * 
 */
package find.layout.classification.definition;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.person.dom.person.layoutitemclassification.definition.ILayoutPersonInfoClsDefRepository;

/**
 * @author laitv
 *
 */
@Stateless
public class LayoutPersonInfoClsDefFinder {

	@Inject
	private ILayoutPersonInfoClsDefRepository classItemDefRepo;

	public List<String> getItemDefineIds(String layoutId, int classDispOrder) {
		return classItemDefRepo.getAllItemDefineIds(layoutId, classDispOrder);
	}

}
