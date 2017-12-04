package nts.uk.ctx.pereg.app.find.copysetting.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.uk.ctx.pereg.app.find.person.category.PerInfoCtgMapDto;
import nts.uk.ctx.pereg.app.find.person.setting.init.category.SettingCtgDto;
import nts.uk.ctx.pereg.dom.copysetting.setting.EmpCopySetting;
import nts.uk.ctx.pereg.dom.copysetting.setting.EmpCopySettingRepository;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.roles.auth.category.PersonInfoCategoryAuthRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class EmpCopySettingFinder {

	@Inject
	private EmpCopySettingRepository empCopyRepo;

	@Inject
	private PersonInfoCategoryAuthRepository PerInfoCtgRepo;

	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRepositoty;

	@Inject
	private PerInfoItemDefRepositoty pernfoItemDefRep;

	public List<SettingCtgDto> getEmpCopySetting() {

		String companyId = AppContexts.user().companyId();
		List<EmpCopySetting> copyList = this.empCopyRepo.find(companyId);

		if (copyList.isEmpty()) {
			// check permision
			boolean isPerRep = true;
			if (isPerRep) {
				throw new BusinessException(new RawErrorMessage("Msg_347"));
			} else {
				throw new BusinessException(new RawErrorMessage("Msg_348"));
			}
		}

		List<String> categoryList = new ArrayList<String>();

		copyList.stream().forEach(i -> categoryList.add(i.getCategoryId()));

		return this.PerInfoCtgRepo.getAllCategoryByCtgIdList(companyId, categoryList).stream().map(p -> {
			return new SettingCtgDto(p.getCategoryCode(), p.getCategoryName());
		}).collect(Collectors.toList());

	}

	public List<PerInfoCtgMapDto> getAllPerInfoCategoryWithCondition(String ctgName) {
		// get all perinforcategory by company id
		String companyId = AppContexts.user().companyId();
		String contractCode = AppContexts.user().contractCode();
		List<PersonInfoCategory> lstPerInfoCtg = null;
		if (ctgName.equals(""))
			lstPerInfoCtg = perInfoCtgRepositoty.getAllPerInfoCategory(companyId, contractCode);
		else {
			lstPerInfoCtg = perInfoCtgRepositoty.getPerInfoCategoryByName(companyId, contractCode, ctgName);
		}
		List<PersonInfoCategory> lstFilter = new ArrayList<PersonInfoCategory>();

		// get all PersonInfoItemDefinition
		for (PersonInfoCategory obj : lstPerInfoCtg) {
			// check whether category has already copied or not
			// filter: category has items
			if (pernfoItemDefRep.countPerInfoItemDefInCategory(obj.getPersonInfoCategoryId(), companyId) > 0) {
				lstFilter.add(obj);
			}
		}
		List<PerInfoCtgMapDto> lstReturn = null;
		if (lstFilter.size() != 0) {
			lstReturn = PersonInfoCategory.getAllPerInfoCategoryWithCondition(lstFilter).stream().map(p -> {
				boolean alreadyCopy = empCopyRepo.checkPerInfoCtgAlreadyCopy(p.getPersonInfoCategoryId(), companyId);
				// boolean alreadyCopy = true;
				return new PerInfoCtgMapDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
						p.getCategoryName().v(), alreadyCopy);
			}).collect(Collectors.toList());
		}
		if (lstFilter.size() == 0 || lstReturn.size() == 0)
			throw new BusinessException("Msg_352");
		return lstReturn;
	}
}
