/**
 * 
 */
package nts.uk.ctx.bs.employee.app.find.jobtitle.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistoryItem;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistoryItemRepository_v1;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistoryRepository_ver1;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistory_ver1;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.pereg.app.ComboBoxObject;
import nts.uk.shr.pereg.app.find.PeregFinder;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.dto.DataClassification;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

/**
 * @author danpv
 *
 */
@Stateless
public class AffJobTitleFinder implements PeregFinder<AffJobTitleDto> {

	@Inject
	private AffJobTitleHistoryItemRepository_v1 affJobTitleItemRepo;

	@Inject
	private AffJobTitleHistoryRepository_ver1 affJobTitleRepo;

	@Override
	public String targetCategoryCode() {
		return "CS00016";
	}

	@Override
	public Class<AffJobTitleDto> dtoClass() {
		return AffJobTitleDto.class;
	}

	@Override
	public DataClassification dataType() {
		return DataClassification.EMPLOYEE;
	}

	@Override
	public PeregDomainDto getSingleData(PeregQuery query) {
		
		Optional<AffJobTitleHistory_ver1> historyOpt;
		if ( query.getInfoId() != null ) {
			historyOpt = affJobTitleRepo.getByHistoryId(query.getInfoId());
		} else {
			historyOpt = affJobTitleRepo.getByEmpIdAndStandardDate(query.getEmployeeId(), query.getStandardDate());
		}
		
		if ( historyOpt.isPresent()) {
			Optional<AffJobTitleHistoryItem> histItemOpt = affJobTitleItemRepo
					.findByHitoryId(historyOpt.get().getHistoryItems().get(0).identifier());
			if ( histItemOpt.isPresent()) {
				return AffJobTitleDto.createFromDomain(histItemOpt.get(), historyOpt.get());
			}
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.shr.pereg.app.find.PeregFinder#getListData(nts.uk.shr.pereg.app.
	 * find.PeregQuery)
	 */
	@Override
	public List<PeregDomainDto> getListData(PeregQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ComboBoxObject> getListFirstItems(PeregQuery query) {
		Optional<AffJobTitleHistory_ver1> historyOpt = affJobTitleRepo.getListBySid(AppContexts.user().companyId(), query.getEmployeeId());
		if(!historyOpt.isPresent()) return new ArrayList<>();
		List<DateHistoryItem> historyItems = historyOpt.get().getHistoryItems();
		if(historyItems.size() == 0) return new ArrayList<>(); 
		List<DateHistoryItem> containItemHists = historyItems.stream().filter(x -> {
			return affJobTitleItemRepo.findByHitoryId(x.identifier()).isPresent();
		}).collect(Collectors.toList());
		return containItemHists.stream()
				.sorted((a, b) -> b.start().compareTo(a.start()))
				.map(x -> ComboBoxObject.toComboBoxObject(x.identifier(), x.start().toString(), x.end().toString()))
				.collect(Collectors.toList());
	}

}
