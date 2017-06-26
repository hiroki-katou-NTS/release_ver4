/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.aggregate.find;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.report.app.salarydetail.aggregate.find.dto.SalaryAggregateItemFindDto;
import nts.uk.ctx.pr.report.app.salarydetail.aggregate.find.dto.SalaryAggregateItemInDto;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItem;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class SalaryAggregateItemFinder.
 */
@Stateless
public class SalaryAggregateItemFinder {

	/** The salary aggregate item repository. */
	@Inject
	private SalaryAggregateItemRepository salaryAggregateItemRepository;

	public SalaryAggregateItemFindDto findSalaryAggregateItem(
		SalaryAggregateItemInDto salaryAggregateItemInDto) {
		// get login user info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companycode by user login
		String companyCode = loginUserContext.companyCode();

		// to Dto
		SalaryAggregateItemFindDto salaryAggregateItemFindDto = new SalaryAggregateItemFindDto();

		// call service find Id
		Optional<SalaryAggregateItem> optionalSalaryAggregateItem;
		optionalSalaryAggregateItem = this.salaryAggregateItemRepository.findByCode(companyCode,
			salaryAggregateItemInDto.getAggregateItemCode(), salaryAggregateItemInDto.getTaxDivision());

		// value exsit
		if (optionalSalaryAggregateItem.isPresent()) {
			optionalSalaryAggregateItem.get().saveToMemento(salaryAggregateItemFindDto);
			return salaryAggregateItemFindDto;
		}
		salaryAggregateItemFindDto.defaultData(salaryAggregateItemInDto);
		return salaryAggregateItemFindDto;
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<SalaryAggregateItemFindDto> findAll() {
		List<SalaryAggregateItem> listAggregateItem = this.salaryAggregateItemRepository
				.findAll(AppContexts.user().companyCode());
		List<SalaryAggregateItemFindDto> mappedList = listAggregateItem.stream().map(domain -> {
			SalaryAggregateItemFindDto dto = new SalaryAggregateItemFindDto();
			domain.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
		return mappedList;
	}
}
