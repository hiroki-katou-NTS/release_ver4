package find.person.info.category;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import find.person.info.item.PersonInfoItemDefDto;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCtgByCompanyRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.bs.person.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class PerInfoCtgFinder {
	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRepositoty;

	@Inject
	private PerInfoCtgByCompanyRepositoty perInfoByCompanyRepo;

	@Inject
	private PerInfoItemDefRepositoty pernfoItemDefRep;

	public List<PerInfoCtgFullDto> getAllPerInfoCtg(String companyId) {
		String contractCd = companyId.substring(0,12);
		return perInfoCtgRepositoty.getAllPerInfoCategory(companyId, contractCd)
				.stream().map(p -> {
					return new PerInfoCtgFullDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
							p.getCategoryName().v(), p.getPersonEmployeeType().value, p.getIsAbolition().value,
							p.getCategoryType().value, p.getIsFixed().value);
				}).collect(Collectors.toList());
	};

	public PerCtgInfoDto getDetailCtgInfo(String categoryId) {
		String companyIdRoot = "000000000000-0000";
		String companyId = AppContexts.user().companyId();
		String contractCd = companyId.substring(0, 12);
		PerCtgInfoDto detailCtgInfo = new PerCtgInfoDto();
		PersonInfoCategory perInfoCtg = this.perInfoByCompanyRepo
				.getDetailCategoryInfo(companyId, categoryId, contractCd).orElse(null);
		if (perInfoCtg != null) {
			String categoryNameDefault = this.perInfoByCompanyRepo.getNameCategoryInfo(companyIdRoot,
					perInfoCtg.getCategoryCode().toString());
			detailCtgInfo.setCategoryName(perInfoCtg.getCategoryName().toString());
			detailCtgInfo.setCategoryType(perInfoCtg.getCategoryType().value);
			detailCtgInfo.setAbolition(perInfoCtg.getIsAbolition().value == 1? true: false);
			if (!categoryNameDefault.equals("null")) {
				detailCtgInfo.setCategoryNameDefault(categoryNameDefault);
			}
			List<PersonInfoItemDefDto> itemLst = this.pernfoItemDefRep
					.getAllPerInfoItemDefByCategoryId(categoryId, contractCd).stream()
					.map(item -> PersonInfoItemDefDto.fromDomain(item)).collect(Collectors.toList());
			detailCtgInfo.setItemLst(itemLst);
			List<String> requiredLst = this.perInfoByCompanyRepo.getItemInfoId(categoryId, contractCd);
			if (requiredLst.size() > 0) {
				detailCtgInfo.setSystemRequired(1);
			} else {
				detailCtgInfo.setSystemRequired(0);
			}
		}
		return detailCtgInfo;
	}
}
