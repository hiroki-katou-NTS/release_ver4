package find.person.info.category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.error.BusinessException;
import nts.arc.i18n.custom.IInternationalization;
import nts.uk.ctx.bs.person.dom.person.info.category.HistoryTypes;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.bs.person.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class PerInfoCategoryFinder {

	@Inject
	IInternationalization internationalization;

	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRepositoty;

	@Inject
	private PerInfoItemDefRepositoty pernfoItemDefRep;

	public List<PerInfoCtgFullDto> getAllPerInfoCtg() {
		return perInfoCtgRepositoty
				.getAllPerInfoCategory(PersonInfoCategory.ROOT_COMPANY_ID, PersonInfoItemDefinition.ROOT_CONTRACT_CODE)
				.stream().map(p -> {
					return new PerInfoCtgFullDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
							p.getCategoryName().v(), p.getPersonEmployeeType().value, p.getIsAbolition().value,
							p.getCategoryType().value, p.getIsFixed().value);
				}).collect(Collectors.toList());
	};
	
	//vinhpx: start
	public List<PerInfoCtgMapDto> getAllPerInfoCategoryWithCondition(String ctgName){
		//get all perinforcategory by company id
		String companyId = AppContexts.user().companyId();
		String contractCode = AppContexts.user().contractCode();
		List<PersonInfoCategory> lstPerInfoCtg = null;
		if(ctgName.equals(""))
			lstPerInfoCtg = perInfoCtgRepositoty.getAllPerInfoCategory(
					companyId, contractCode);
		else {
			lstPerInfoCtg = perInfoCtgRepositoty.getPerInfoCategoryByName(companyId, contractCode, ctgName); 
		}
		List<PersonInfoCategory> lstFilter  = new ArrayList<PersonInfoCategory>();
		
		
		//get all PersonInfoItemDefinition 
		for(PersonInfoCategory obj : lstPerInfoCtg){
			//check whether category has already copied or not
			//filter: category has items 
			if(pernfoItemDefRep.countPerInfoItemDefInCategory(obj.getPersonInfoCategoryId(), companyId) > 0){
				lstFilter.add(obj);
			}
		}
		List<PerInfoCtgMapDto> lstReturn = null;
		if(lstFilter.size() != 0){
			lstReturn = PersonInfoCategory.getAllPerInfoCategoryWithCondition(lstFilter).stream().map(p -> {
				//boolean alreadyCopy = perInfoCtgRepositoty.checkPerInfoCtgAlreadyCopy(p.getPersonInfoCategoryId(), companyId);
				boolean alreadyCopy = true;
				return new PerInfoCtgMapDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
						p.getCategoryName().v(), alreadyCopy);
			}).collect(Collectors.toList());
		}
		if(lstFilter.size() == 0 || lstReturn.size() == 0)
			throw new BusinessException("Msg_352");
		return lstReturn;
	}
	
	// get per info ctg list: contains ctg and children
	// isParent, 1 - parent; 0 - is not
	public List<PerInfoCtgWithParentMapDto> getPerInfoCtgWithParent(String parentCd){
		List<PerInfoCtgWithParentMapDto> lstResult = new ArrayList<>();
		lstResult = perInfoCtgRepositoty.getPerInfoCtgByParentId(parentCd, PersonInfoItemDefinition.ROOT_CONTRACT_CODE)
				.stream().map(
						p -> {
					return new PerInfoCtgWithParentMapDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
							p.getCategoryName().v(), p.getPersonEmployeeType().value, p.getIsAbolition().value,
							p.getCategoryType().value, p.getIsFixed().value, 0);
				}).collect(Collectors.toList());
		lstResult.add(perInfoCtgRepositoty.getPerInfoCategory(parentCd, PersonInfoItemDefinition.ROOT_CONTRACT_CODE)
				.map(p -> {
					return new PerInfoCtgWithParentMapDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
							p.getCategoryName().v(), p.getPersonEmployeeType().value, p.getIsAbolition().value,
							p.getCategoryType().value, p.getIsFixed().value, 1);
				}).orElse(null));
		return lstResult;
	}
	 
	 
	//vinhpx: end

	public PerInfoCtgFullDto getPerInfoCtg(String perInfoCtgId) {
		return perInfoCtgRepositoty.getPerInfoCategory(perInfoCtgId, PersonInfoItemDefinition.ROOT_CONTRACT_CODE)
				.map(p -> {
					return new PerInfoCtgFullDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
							p.getCategoryName().v(), p.getPersonEmployeeType().value, p.getIsAbolition().value,
							p.getCategoryType().value, p.getIsFixed().value);
				}).orElse(null);
	};

	public PerInfoCtgDataEnumDto getAllPerInfoCtgByCompany() {
		List<PerInfoCtgShowDto> categoryList = perInfoCtgRepositoty
				.getAllPerInfoCategory(AppContexts.user().companyId(),AppContexts.user().contractCode())
				.stream().map(p -> {
					return new PerInfoCtgShowDto(p.getPersonInfoCategoryId(), p.getCategoryName().v(),
							p.getCategoryType().value, p.getIsAbolition().value, p.getCategoryParentCode().v());
				}).collect(Collectors.toList());

		List<EnumConstant> historyTypes = EnumAdaptor.convertToValueNameList(HistoryTypes.class, internationalization);
		return new PerInfoCtgDataEnumDto(historyTypes, categoryList);
	};
	
	public PerInfoCtgDataEnumDto getAllPerInfoCtgByCompanyRoot() {
		List<PerInfoCtgShowDto> categoryList = perInfoCtgRepositoty
				.getAllPerInfoCategory(PersonInfoCategory.ROOT_COMPANY_ID, PersonInfoItemDefinition.ROOT_CONTRACT_CODE)
				.stream().map(p -> {
					return new PerInfoCtgShowDto(p.getPersonInfoCategoryId(), p.getCategoryName().v(),
							p.getCategoryType().value, p.getIsAbolition().value, p.getCategoryParentCode().v());
				}).collect(Collectors.toList());

		List<EnumConstant> historyTypes = EnumAdaptor.convertToValueNameList(HistoryTypes.class, internationalization);
		return new PerInfoCtgDataEnumDto(historyTypes, categoryList);
	};
	
	
	public PerInfoCtgWithItemsNameDto getPerInfoCtgWithItemsName(String perInfoCtgId) {
		List<String> itemNameList = pernfoItemDefRep.getPerInfoItemsName(perInfoCtgId,
				PersonInfoItemDefinition.ROOT_CONTRACT_CODE);
		return perInfoCtgRepositoty.getPerInfoCategory(perInfoCtgId, PersonInfoItemDefinition.ROOT_CONTRACT_CODE)
				.map(p -> {
					return new PerInfoCtgWithItemsNameDto(p.getPersonInfoCategoryId(), p.getCategoryName().v(),
							p.getCategoryType().value, p.getIsFixed().value, itemNameList);
				}).orElse(null);
	};
}
