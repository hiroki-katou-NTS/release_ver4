package nts.uk.ctx.bs.employee.app.find.workplace.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItem;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository_v1;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository_v1;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory_ver1;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.pereg.app.ComboBoxObject;
import nts.uk.shr.pereg.app.find.PeregFinder;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.dto.DataClassification;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Stateless
public class AffWorlplaceHistItemFinder implements PeregFinder<AffWorlplaceHistItemDto>{

	@Inject
	private AffWorkplaceHistoryItemRepository_v1 affWrkplcHistItemRepo;
	
	@Inject
	private AffWorkplaceHistoryRepository_v1 affWrkplcHistRepo;
	
	@Override
	public String targetCategoryCode() {
		return "CS00017";
	}

	@Override
	public Class<AffWorlplaceHistItemDto> dtoClass() {
		return AffWorlplaceHistItemDto.class;
	}

	@Override
	public DataClassification dataType() {
		return DataClassification.EMPLOYEE;
	}

	@Override
	public PeregDomainDto getSingleData(PeregQuery query) {
		return query.getInfoId() == null ? getByEmpIdAndStandDate(query.getEmployeeId(), query.getStandardDate()) 
				: getByHistId(query.getInfoId());
	}

	@Override
	public List<PeregDomainDto> getListData(PeregQuery query) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private PeregDomainDto getByEmpIdAndStandDate(String employeeId, GeneralDate standDate){
		Optional<AffWorkplaceHistory_ver1> affWrkplcHist = affWrkplcHistRepo.getByEmpIdAndStandDate(employeeId, standDate);
		if(affWrkplcHist.isPresent()){
			Optional<AffWorkplaceHistoryItem> affWrkplcHistItem = affWrkplcHistItemRepo.getByHistId(affWrkplcHist.get().getHistoryItems().get(0).identifier());
			return AffWorlplaceHistItemDto.getFirstFromDomain(affWrkplcHist.get(), affWrkplcHistItem.get());
		}
		return null;
	}
	
	private PeregDomainDto getByHistId(String historyId){
		Optional<AffWorkplaceHistory_ver1> affWrkplcHist = affWrkplcHistRepo.getByHistId(historyId);
		if(affWrkplcHist.isPresent()){
			Optional<AffWorkplaceHistoryItem> affWrkplcHistItem = affWrkplcHistItemRepo.getByHistId(affWrkplcHist.get().getHistoryItems().get(0).identifier());
			if(!affWrkplcHistItem.isPresent()) return null;
			return AffWorlplaceHistItemDto.getFirstFromDomain(affWrkplcHist.get(), affWrkplcHistItem.get());
		}
		return null;
	}

	@Override
	public List<ComboBoxObject> getListFirstItems(PeregQuery query) {
		String companyId = AppContexts.user().companyId();
		Optional<AffWorkplaceHistory_ver1> affWrkplcHist = affWrkplcHistRepo.getAffWorkplaceHistByEmployeeId(companyId, query.getEmployeeId());
		if (!affWrkplcHist.isPresent())
			return new ArrayList<>();
		List<DateHistoryItem> historyItems = affWrkplcHist.get().getHistoryItems();
		if(historyItems.size() == 0) 
			return new ArrayList<>();
		return historyItems.stream()
				.map(x -> ComboBoxObject.toComboBoxObject(x.identifier(), x.start().toString(), x.end().toString()))
				.collect(Collectors.toList());
	
	}
}
