package nts.uk.ctx.pereg.ac.info.setting.event.no19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pereg.dom.person.info.category.IsAbolition;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.shr.com.context.AppContexts;

/**
 * Event:複数回勤務項目の廃止区分の切り替え
 * 
 * @author lanlt
 *
 */
@Stateless
public class WorkTimesMultipleEventSubcriber {
	@Inject
	private PerInfoCategoryRepositoty ctgRepo;

	@Inject
	private PerInfoItemDefRepositoty itemRepo;

	private static final String ctgCode = "CS00020";

	private static final List<String> itemCdLst = Arrays.asList(new String[] { "IS00135", "IS00136", "IS00137",
			"IS00144", "IS00145", "IS00146", "IS00162", "IS00163", "IS00164", "IS00171", "IS00172", "IS00173",
			"IS00180", "IS00181", "IS00182", "IS00153", "IS00154", "IS00155", "IS00198", "IS00199", "IS00200",
			"IS00207", "IS00208", "IS00209", "IS00216", "IS00217", "IS00218", "IS00225", "IS00226", "IS00227",
			"IS00234", "IS00235", "IS00236", "IS00243", "IS00244", "IS00245", "IS00189", "IS00190", "IS00191" });

	private void updateItem(boolean params) {
		List<PersonInfoItemDefinition> itemUpdateLst = new ArrayList<>();
		itemUpdateLst.addAll(setAbolition( getItemLst() , params));
		if (itemUpdateLst.size() > 0)
			this.itemRepo.updateAbolitionItem(itemUpdateLst);
	}
	
	private List<PersonInfoItemDefinition> getItemLst() {
		String companyLogin = AppContexts.user().companyId();
		Optional<PersonInfoCategory> CS00020 = ctgRepo.getPerInfoCategoryByCtgCD(ctgCode, companyLogin);
		if (CS00020.isPresent()) {
			return this.itemRepo.getAllItemId(Arrays.asList(new String[] {(CS00020.get().getPersonInfoCategoryId())}), itemCdLst);
		}else {
			return new ArrayList<>();
		}
	}
	
	private  List<PersonInfoItemDefinition> setAbolition(List<PersonInfoItemDefinition> itemLst, boolean params ){
		List<PersonInfoItemDefinition> itemUpdateLst = new ArrayList<>();
		if (itemLst.size() > 0) {
				itemUpdateLst.addAll(itemLst.stream().map(c -> {
					c.setIsAbolition(params == true? IsAbolition.NOT_ABOLITION: IsAbolition.ABOLITION);
					return c;
				}).collect(Collectors.toList()));
		}
		return itemUpdateLst;
	}
}
