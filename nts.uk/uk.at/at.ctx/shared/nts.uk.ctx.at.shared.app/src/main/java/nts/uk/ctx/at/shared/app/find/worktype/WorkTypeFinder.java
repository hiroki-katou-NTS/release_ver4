/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktype;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeInfor;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.language.WorkTypeLanguage;
import nts.uk.ctx.at.shared.dom.worktype.language.WorkTypeLanguageRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkTypeFinder.
 */
@Stateless
public class WorkTypeFinder {

	/** The work type repo. */
	@Inject
	private WorkTypeRepository workTypeRepo;

	/** The work type language repo. */
	@Inject
	private WorkTypeLanguageRepository workTypeLanguageRepo;

	
	/**
	 * Gets the possible work type.
	 *
	 * @param lstPossible the lst possible
	 * @return the possible work type
	 */
	public List<WorkTypeInfor> getPossibleWorkType(List<String> lstPossible) {
		// company id
		String companyId = AppContexts.user().companyId();
		List<WorkTypeInfor> lst = this.workTypeRepo.getPossibleWorkTypeAndOrder(companyId, lstPossible);
		return lst;
	}
	
	/**
	 * Gets the possible work type. with No Master
	 *
	 * @param lstPossible the lst possible
	 * @return the possible work type
	 */
	public List<WorkTypeInfor> getPossibleWorkTypeWithNoMaster(List<String> lstPossible) {
		// company id
		String companyId = AppContexts.user().companyId();
		List<WorkTypeInfor> lst = this.workTypeRepo.getPossibleWorkTypeWithNoMasterAndOrder(companyId, lstPossible);
		return lst;
	}

	/**
	 * Find not deprecated by list code.
	 *
	 * @param codes the codes
	 * @return the list
	 */
	public List<WorkTypeDto> findNotDeprecatedByListCode(List<String> codes) {
		// company id
		String companyId = AppContexts.user().companyId();
		return this.workTypeRepo.findNotDeprecatedByListCode(companyId, codes).stream()
				.map(dom -> WorkTypeDto.fromDomain(dom)).collect(Collectors.toList());
	}

	/**
	 * Find by company id.
	 *
	 * @return the list
	 */
	public List<WorkTypeDto> findByCompanyId() {
		// company id
		String companyId = AppContexts.user().companyId();
		
		List<WorkTypeDto> listWorktypeDto = this.workTypeRepo.findByCompanyId(companyId).stream().map(c -> {
			List<WorkTypeSetDto> workTypeSetList = c.getWorkTypeSetList().stream()
					.map(x -> WorkTypeSetDto.fromDomain(x)).collect(Collectors.toList());

			WorkTypeDto workType = WorkTypeDto.fromDomain(c);
			workType.setWorkTypeSets(workTypeSetList);
			return workType;
		}).collect(Collectors.toList());
						
		// Sorting by workType Code
		Collections.sort(listWorktypeDto, new Comparator<WorkTypeDto>() {
		        @Override
		        public int compare(WorkTypeDto workTypeDto2, WorkTypeDto workTypeDto1)
		        {
		            return workTypeDto2.getWorkTypeCode().compareTo(workTypeDto1.getWorkTypeCode());
		        }
		    });
		
		return listWorktypeDto;
	}

	/**
	 * Find not deprecated.
	 *
	 * @return the list
	 */
	public List<WorkTypeDto> findNotDeprecated() {
		// company id
		String companyId = AppContexts.user().companyId();
		return this.workTypeRepo.findNotDeprecated(companyId).stream().map(dom -> WorkTypeDto.fromDomain(dom))
				.collect(Collectors.toList());
	}
	
	/**
	 * Find all by order.
	 *
	 * @return the list
	 */
	public List<WorkTypeInfor> findAllByOrder() {
		// company id
		String companyId = AppContexts.user().companyId();
		List<WorkTypeInfor> lst = this.workTypeRepo.findAllByOrder(companyId);
		return lst;
	}

	/**
	 * Find by code.
	 *
	 * @param workTypeCode the work type code
	 * @return the work type dto
	 */
	public WorkTypeDto findByCode(String workTypeCode) {
		// company id
		String companyId = AppContexts.user().companyId();
		Optional<WorkType> workTypeOpt = this.workTypeRepo.findByPK(companyId, workTypeCode);
		if (!workTypeOpt.isPresent()) {
			return null;
		}
		
		WorkType workType = workTypeOpt.get();
		WorkTypeDto workTypeDto = WorkTypeDto.fromDomain(workType);
		// set work type setting
		if (workType.getWorkTypeSetList() != null) {
			List<WorkTypeSetDto> workTypeSetList = workType.getWorkTypeSetList().stream()
					.map(x -> WorkTypeSetDto.fromDomain(x)).collect(Collectors.toList());
			workTypeDto.setWorkTypeSets(workTypeSetList);
		}

		return workTypeDto;
	}


	/**
	 * Find work type language.
	 *
	 * @param langId the lang id
	 * @return the list
	 */
	public List<WorkTypeDto> findWorkTypeLanguage(String langId) {
		// company id
		String companyId = AppContexts.user().companyId();
		List<WorkTypeLanguage> workTypeLanguage = workTypeLanguageRepo.findByCIdAndLangId(companyId, langId);
		return workTypeLanguage.stream().map(x -> {
			WorkType wT = new WorkType(companyId, x.getWorkTypeCode(), x.getName(), x.getAbbreviationName());
			return WorkTypeDto.fromDomainWorkTypeLanguage(wT);
		}).collect(Collectors.toList());
	}
	
	/**
	 * Find work type by condition.
	 *
	 * @return the list
	 */
	public List<WorkTypeDto> findWorkTypeByCondition() {
		// company id
		String companyId = AppContexts.user().companyId();
		return this.workTypeRepo.findWorkTypeByCondition(companyId).stream().map(dom -> WorkTypeDto.fromDomain(dom))
				.collect(Collectors.toList());
	}
	/**
	 * UKDesign.UniversalK.就業.KDL_ダイアログ.KDL002_勤務種類選択.A：勤務種類選択.アルゴリズム.勤務種類の表示
	 * @param lstPossible
	 * @return
	 */
	public List<WorkTypeInfor> getPossibleWorkTypeKDL002(List<String> lstPossible) {
		// company id
		String companyId = AppContexts.user().companyId();
		//<<Public>> 指定した勤務種類をすべて取得する
		List<WorkTypeInfor> lst = this.workTypeRepo.getPossibleWorkTypeAndOrder(companyId, lstPossible);
		//取得されている勤務種類一覧から廃止されている勤務種類を取り除く
//		"廃止区分
//		0: 廃止しない
//		1: 廃止する"
		return lst.stream().filter(c -> c.getAbolishAtr() == 0).collect(Collectors.toList());
	}
}
