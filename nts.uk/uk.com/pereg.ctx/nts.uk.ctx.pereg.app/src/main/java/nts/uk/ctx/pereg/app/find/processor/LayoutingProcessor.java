package nts.uk.ctx.pereg.app.find.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import lombok.val;
import nts.uk.shr.pereg.app.ComboBoxObject;
import nts.uk.shr.pereg.app.find.PeregEmpOptRepository;
import nts.uk.shr.pereg.app.find.PeregFinder;
import nts.uk.shr.pereg.app.find.PeregPerOptRepository;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.PeregQueryByListEmp;
import nts.uk.shr.pereg.app.find.dto.DataClassification;
import nts.uk.shr.pereg.app.find.dto.GridPeregDomainDto;
import nts.uk.shr.pereg.app.find.dto.GridPeregDto;
import nts.uk.shr.pereg.app.find.dto.OptionalItemDataDto;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;
import nts.uk.shr.pereg.app.find.dto.PeregDto;

@ApplicationScoped
public class LayoutingProcessor {

	@Inject
	private PeregFinderProcessorCollector peregFinderCollector;

	private Map<String, PeregFinder<?>> finders;

	@Inject
	private PeregEmpOptRepository empOptRepo;

	@Inject
	private PeregPerOptRepository perOptRepo;

	/**
	 * Initializes.
	 */
	public void init(@Observes @Initialized(ApplicationScoped.class) Object event) {
		this.finders = this.peregFinderCollector.peregFinderCollect().stream()
				.collect(Collectors.toMap(h -> h.targetCategoryCode(), h -> h));
	}

	/**
	 * Ctg single handler
	 * 
	 * @param query
	 * @return
	 */
	public PeregDto findSingle(PeregQuery query) {
		// get domain data
		val finderClass = this.finders.get(query.getCategoryCode());
		if (finderClass == null)
			return null;
		PeregDomainDto domainDto = finderClass.findSingle(query);

		if (domainDto == null) {
			return null;
		}

		// get optional data
		List<OptionalItemDataDto> optionalItems = getUserDefData(finderClass.dataType(), domainDto.getRecordId());

		return new PeregDto(domainDto, finderClass.dtoClass(), optionalItems);
	}

	/**
	 * Ctg list handler
	 * 
	 * @param query
	 * @return
	 */
	public List<PeregDto> findList(PeregQuery query) {

		// get domain data
		val finderClass = this.finders.get(query.getCategoryCode());
		List<PeregDomainDto> lstDtos = finderClass.findList(query);

		return lstDtos.stream().map(domainDto -> {

			// get optional items data
			List<OptionalItemDataDto> optionalItems = getUserDefData(finderClass.dataType(), domainDto.getRecordId());

			return new PeregDto(domainDto, finderClass.dtoClass(), optionalItems);

		}).collect(Collectors.toList());
	}

	public List<ComboBoxObject> getListFirstItems(PeregQuery query) {
		val finderClass = this.finders.get(query.getCategoryCode());
		
		return finderClass.getListFirstItems(query);
	}
	
	/**
	 * find all by sids
	 * @param query
	 * @return
	 */
	public List<GridPeregDto> findAllData(PeregQueryByListEmp query) {
		
		if (query.getEmpInfos().size() == 0) {
			return new ArrayList<>();
		}
		
		// get domain data
		val finderClass = this.finders.get(query.getCategoryCode());

		if (finderClass == null)
			return null;

		List<GridPeregDomainDto> objectDto = finderClass.findAllData(query);

		if (objectDto.size() == 0) {
			return new ArrayList<>();
		}

		List<String> recordIds = objectDto.stream().map(c -> c.getPeregDomainDto().getRecordId())
				.collect(Collectors.toList());

		// get optional data
		Map<String, List<OptionalItemDataDto>> optionalItems = getUserDefData(finderClass.dataType(), recordIds);

		List<GridPeregDto> peregDtoLst = objectDto.stream().map(c -> {
			List<OptionalItemDataDto> itemData = optionalItems.get(c.getPeregDomainDto().getRecordId());
			PeregDto pereg = new PeregDto(c.getPeregDomainDto(), finderClass.dtoClass(), itemData);
			
			return new GridPeregDto(c.getEmployeeId(), c.getPersonId(), pereg);
		}).collect(Collectors.toList());

		return peregDtoLst;
	}


	private List<OptionalItemDataDto> getUserDefData(DataClassification personEmpType, String recordId) {
		if (personEmpType == DataClassification.PERSON) {
			return perOptRepo.getData(recordId);
		} else {
			return empOptRepo.getData(recordId);
		}
	}
	
	/**
	 * getUserDefData by recordIds
	 * 
	 * @param personEmpType
	 * @param recordId
	 * @return
	 */
	private Map<String, List<OptionalItemDataDto>> getUserDefData(DataClassification personEmpType,
			List<String> recordIds) {
		if (personEmpType == DataClassification.PERSON) {
			return perOptRepo.getDatas(recordIds);
		} else {
			return empOptRepo.getDatas(recordIds);
		}
	}
}
