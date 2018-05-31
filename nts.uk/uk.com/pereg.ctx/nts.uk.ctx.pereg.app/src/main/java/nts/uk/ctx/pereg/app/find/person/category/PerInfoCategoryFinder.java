package nts.uk.ctx.pereg.app.find.person.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pereg.app.find.person.info.item.PerInfoItemDefDto;
import nts.uk.ctx.pereg.app.find.person.info.item.PerInfoItemDefFinder;
import nts.uk.ctx.pereg.dom.person.additemdata.category.EmInfoCtgDataRepository;
import nts.uk.ctx.pereg.dom.person.additemdata.category.EmpInfoCtgData;
import nts.uk.ctx.pereg.dom.person.info.category.HistoryTypes;
import nts.uk.ctx.pereg.dom.person.info.category.IsFixed;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.roles.auth.PersonInfoPermissionType;
import nts.uk.ctx.pereg.dom.roles.auth.category.PersonInfoCategoryAuth;
import nts.uk.ctx.pereg.dom.roles.auth.category.PersonInfoCategoryAuthRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;
import nts.uk.shr.pereg.app.find.PeregQuery;

@Stateless
public class PerInfoCategoryFinder {

	@Inject
	I18NResourcesForUK internationalization;

	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRepositoty;

	@Inject
	private PerInfoItemDefRepositoty pernfoItemDefRep;

	@Inject
	private PersonInfoCategoryAuthRepository personInfoCategoryAuthRepository;

	@Inject
	private EmInfoCtgDataRepository empDataRepo;

	@Inject
	private PerInfoItemDefFinder perInfoItemDfFinder;

	public List<PerInfoCtgFullDto> getAllPerInfoCtg() {
		String zeroCompanyId = AppContexts.user().zeroCompanyIdInContract();
		String contractCode = AppContexts.user().contractCode();
		List<PersonInfoCategory> categoryList = perInfoCtgRepositoty.getAllPerInfoCategory(zeroCompanyId, contractCode);
		return categoryList.stream()
				.map(p -> new PerInfoCtgFullDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
						p.getCategoryName().v(), p.getPersonEmployeeType().value, p.getIsAbolition().value,
						p.getCategoryType().value, p.getIsFixed().value))
				.collect(Collectors.toList());
	}

	// vinhpx: start

	// get per info ctg list: contains ctg and children
	// isParent, 1 - parent; 0 - is not
	public List<PerInfoCtgWithParentMapDto> getPerInfoCtgWithParent(String parentCd) {
		String contractCode = AppContexts.user().contractCode();
		List<PerInfoCtgWithParentMapDto> lstResult = new ArrayList<>();
		lstResult = perInfoCtgRepositoty.getPerInfoCtgByParentCode(parentCd, contractCode).stream().map(p -> {
			return new PerInfoCtgWithParentMapDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
					p.getCategoryName().v(), p.getPersonEmployeeType().value, p.getIsAbolition().value,
					p.getCategoryType().value, p.getIsFixed().value, 0);
		}).collect(Collectors.toList());
		lstResult.add(perInfoCtgRepositoty.getPerInfoCategory(parentCd, contractCode).map(p -> {
			return new PerInfoCtgWithParentMapDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(),
					p.getCategoryName().v(), p.getPersonEmployeeType().value, p.getIsAbolition().value,
					p.getCategoryType().value, p.getIsFixed().value, 1);
		}).orElse(null));
		return lstResult;
	}

	/**
	 * check exist auth of ctg by empId and ctgId
	 * 
	 * @param empId
	 * @param ctgId
	 * @return
	 */
	public boolean checkCategoryAuth(PeregQuery query, PersonInfoCategory perInfoCtg, String roleId) {

		boolean isSelfAuth = AppContexts.user().employeeId().equals(query.getEmployeeId());
		
		// get perInfoCtgAuth
		Optional<PersonInfoCategoryAuth> perInfoCtgAuth = personInfoCategoryAuthRepository
				.getDetailPersonCategoryAuthByPId(roleId, perInfoCtg.getPersonInfoCategoryId());
		
		if (!perInfoCtgAuth.isPresent()) {
			return false;
		}
		
		PersonInfoCategoryAuth personInfoCategoryAuth = perInfoCtgAuth.get();

		switch (perInfoCtg.getCategoryType()) {
		case SINGLEINFO:
			if (isSelfAuth) {
				return personInfoCategoryAuth.getAllowPersonRef() == PersonInfoPermissionType.YES;
			} else {
				return personInfoCategoryAuth.getAllowOtherRef() == PersonInfoPermissionType.YES;
			}
		case MULTIINFO:
			if (query.getInfoId() == null ) {
				// create new data case
				if (isSelfAuth) {
					return personInfoCategoryAuth.getAllowPersonRef() == PersonInfoPermissionType.YES
							&& personInfoCategoryAuth.getSelfAllowAddMulti() == PersonInfoPermissionType.YES;
				} else {
					return personInfoCategoryAuth.getAllowOtherRef() == PersonInfoPermissionType.YES
							&& personInfoCategoryAuth.getOtherAllowAddMulti() == PersonInfoPermissionType.YES;
				}
			} else {
				// view data case
				if (isSelfAuth) {
					return personInfoCategoryAuth.getAllowPersonRef() == PersonInfoPermissionType.YES;
				} else {
					return personInfoCategoryAuth.getAllowOtherRef() == PersonInfoPermissionType.YES;
				}
			}
		default:
			// HISTORY
			if (query.getInfoId() == null ) {
				// create new data case
				if (isSelfAuth) {
					return personInfoCategoryAuth.getAllowPersonRef() == PersonInfoPermissionType.YES
							&& personInfoCategoryAuth.getSelfAllowAddHis() == PersonInfoPermissionType.YES;
				} else {
					return personInfoCategoryAuth.getAllowOtherRef() == PersonInfoPermissionType.YES
							&& personInfoCategoryAuth.getOtherAllowAddHis() == PersonInfoPermissionType.YES;
				}
			} else {
				// view data case
				if (isSelfAuth) {
					return personInfoCategoryAuth.getAllowPersonRef() == PersonInfoPermissionType.YES;
				} else {
					return personInfoCategoryAuth.getAllowOtherRef() == PersonInfoPermissionType.YES;
				}
			}
		}
	}

	// vinhpx: end

	public PerInfoCtgFullDto getPerInfoCtg(String perInfoCtgId) {
		return perInfoCtgRepositoty.getPerInfoCategory(perInfoCtgId, AppContexts.user().contractCode()).map(p -> {
			return new PerInfoCtgFullDto(p.getPersonInfoCategoryId(), p.getCategoryCode().v(), p.getCategoryName().v(),
					p.getPersonEmployeeType().value, p.getIsAbolition().value, p.getCategoryType().value,
					p.getIsFixed().value);
		}).orElse(null);
	};

	public PerInfoCtgDataEnumDto getAllPerInfoCtgByCompany() {
		String companyId = AppContexts.user().companyId();
		String contractCode = AppContexts.user().contractCode();

		List<PerInfoCtgShowDto> categoryList = perInfoCtgRepositoty.getAllPerInfoCategory(companyId, contractCode)
				.stream().map(p -> {

					if ((pernfoItemDefRep.countPerInfoItemDefInCategory(p.getPersonInfoCategoryId(), companyId) > 0)) {
						return new PerInfoCtgShowDto(p.getPersonInfoCategoryId(), p.getCategoryName().v(),
								p.getCategoryType().value, p.getCategoryCode().v(), p.getIsAbolition().value, p.getCategoryParentCode().v(),
								p.getInitValMasterCls().value, p.getAddItemCls().value);
					}
					return null;
				}).filter(m -> m != null).collect(Collectors.toList());

		List<EnumConstant> historyTypes = EnumAdaptor.convertToValueNameList(HistoryTypes.class, internationalization);
		return new PerInfoCtgDataEnumDto(historyTypes, categoryList);
	};
	
	//Function get List Category Combobox CPS007
	public PerInfoCtgDataEnumDto getAllPerInfoCtgByCompanyv3() {
		String companyId = AppContexts.user().companyId();
		String contractCode = AppContexts.user().contractCode();
		
		List<PersonInfoCategory> lstCtg = perInfoCtgRepositoty.getAllCategoryForCPS007(companyId, contractCode);
		
		List<String> lstCtgId = lstCtg.stream().map(c -> c.getPersonInfoCategoryId()).collect(Collectors.toList());
		
		Map<String, PersonInfoCategory> mapCtgAndId = lstCtg.stream()
				.collect(Collectors.toMap(e -> e.getPersonInfoCategoryId(), e -> e));
		
		
		Map<String, List<Object[]>> mapCategoryIdAndLstItemDf = this.perInfoItemDfFinder.mapCategoryIdAndLstItemDf(lstCtgId);
		
		List<PerInfoCtgShowDto> categoryList = lstCtg.stream().map(p ->{
			List<Object[]> lstItemDfGroupByCtgId = mapCategoryIdAndLstItemDf.get(p.getPersonInfoCategoryId());
			if (lstItemDfGroupByCtgId == null || CollectionUtil.isEmpty(lstItemDfGroupByCtgId)) {
				return null;
			}
			return new PerInfoCtgShowDto(p.getPersonInfoCategoryId(), p.getCategoryName().v(),
					p.getCategoryType().value, p.getCategoryCode().v(), p.getIsAbolition().value,
					p.getCategoryParentCode().v(),
					p.getInitValMasterCls() == null ? 1 : p.getInitValMasterCls().value,
					p.getAddItemCls() == null ? 1 : p.getAddItemCls().value);
			
		}).filter(m -> m != null).collect(Collectors.toList());
		
		List<EnumConstant> historyTypes = EnumAdaptor.convertToValueNameList(HistoryTypes.class, internationalization);
		return new PerInfoCtgDataEnumDto(historyTypes, categoryList);
	};
	
	

	public PerInfoCtgDataEnumDto getAllPerInfoCtgByCompanyRoot() {

		// ログイン者がグループ会社管理者かどうか判定する - Kiểm tra quyền
		// String roleId = AppContexts.user().roles().forGroupCompaniesAdmin();
		// if (roleId == null) {
		// // false Msg_1103
		// throw new BusinessException("Msg_1103");
		// } else {
		// true
		List<PersonInfoCategory> categoryList = perInfoCtgRepositoty
				.getAllPerInfoCategory(AppContexts.user().zeroCompanyIdInContract(), AppContexts.user().contractCode());

		List<PerInfoCtgShowDto> x = categoryList.stream().map(p -> {
			return new PerInfoCtgShowDto(p.getPersonInfoCategoryId(), p.getCategoryName().v(),
					p.getCategoryType().value, p.getCategoryCode().v(), p.getIsAbolition().value, p.getCategoryParentCode().v(),
					p.getInitValMasterCls() == null ? 1 : p.getInitValMasterCls().value,
					p.getAddItemCls() == null ? 1 : p.getAddItemCls().value);
		}).collect(Collectors.toList());

		List<EnumConstant> historyTypes = EnumAdaptor.convertToValueNameList(HistoryTypes.class, internationalization);

		return new PerInfoCtgDataEnumDto(historyTypes, x);

		// }
	};

	public PerInfoCtgWithItemsNameDto getPerInfoCtgWithItemsName(String perInfoCtgId) {
		String categoryCode = AppContexts.user().contractCode();

		List<String> itemNameList = pernfoItemDefRep.getPerInfoItemsName(perInfoCtgId, categoryCode);
		Optional<PersonInfoCategory> categoryOpt = perInfoCtgRepositoty.getPerInfoCategory(perInfoCtgId, categoryCode);

		return categoryOpt.map(p -> new PerInfoCtgWithItemsNameDto(p.getPersonInfoCategoryId(), p.getCategoryName().v(),
				p.getCategoryType().value, p.getIsFixed().value, p.getPersonEmployeeType().value,
				p.getInitValMasterCls() == null ? true : (p.getInitValMasterCls().value == 1 ? true : false),
				p.getAddItemCls() == null ? true : (p.getAddItemCls().value == 1 ? true : false), itemNameList,
				p.getIsFixed().equals(IsFixed.NOT_FIXED) ? isChangeAbleCtgType(p.getPersonInfoCategoryId()) : false))
				.orElse(null);

	};

	public int getDispOrder(String perInfoCtgId) {
		return perInfoCtgRepositoty.getDispOrder(perInfoCtgId);
	}

	private boolean isChangeAbleCtgType(String perInfoCtgId) {

		String contractCd = AppContexts.user().contractCode();
		// check not change Ctg Type
		PersonInfoCategory ctg = this.perInfoCtgRepositoty.getPerInfoCategory(perInfoCtgId, contractCd).get();

		List<String> ctgIds = this.perInfoCtgRepositoty.getAllCategoryByCtgCD(ctg.getCategoryCode().toString());

		if (ctgIds.size() > 0) {

			List<EmpInfoCtgData> empDataLst = this.empDataRepo.getByEmpIdAndCtgId(ctgIds);

			if (empDataLst.size() > 0) {
				return false;
			}
		}

		return true;
	}
}
