package nts.uk.ctx.basic.app.find.organization.workplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.app.find.organization.department.DepartmentMemoDto;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlace;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceMemo;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class WorkPlaceFinder {

	@Inject
	private WorkPlaceRepository workPlaceRepository;

	private static final String ROOT = "ROOT";

	public WorkPlaceQueryResult handle() {
		WorkPlaceQueryResult queryResult = new WorkPlaceQueryResult();
		String companyCode = AppContexts.user().companyCode();
		if (!workPlaceRepository.checkExist(companyCode)) {
			System.out.println("" + !workPlaceRepository.checkExist(companyCode));
			return queryResult;
		}
		List<WorkPlaceHistoryDto> histories = workPlaceRepository.findHistories(companyCode).stream().map(e -> {
			return new WorkPlaceHistoryDto(e.getHistoryId(), e.getStartDate(), e.getEndDate());
		}).collect(Collectors.toList());
		if (!histories.isEmpty()) {
			List<WorkPlace> workPlaces = workPlaceRepository.findAllByHistory(companyCode,
					histories.get(0).getHistoryId());
			queryResult.setWorkPlaces(workPlaces.isEmpty() ? null : convertToTree(workPlaces));
			Optional<WorkPlaceMemo> workPlaceMemo = workPlaceRepository.findMemo(companyCode,
					histories.get(0).getHistoryId());
			queryResult.setMemo(workPlaceMemo.isPresent()
					? new WorkPlaceMemoDto(workPlaceMemo.get().getHistoryId(), workPlaceMemo.get().getMemo().toString())
					: null);
		}
		queryResult.setHistories(histories);
		return queryResult;
	}

	private List<WorkPlaceDto> convertToTree(List<WorkPlace> workPlaces) {
		Function<WorkPlace, WorkPlaceDto> converter = e -> {
			return new WorkPlaceDto(e.getWorkPlaceCode().toString(), e.getEndDate(),e.getHistoryId(), e.getExternalCode().toString(),
					e.getGenericName().toString(), e.getHierarchyCode().toString(), e.getName().toString(),
					e.getParentChildAttribute1().toString(), e.getParentChildAttribute2().toString(), e.getParentWorkCode1().toString(),
					e.getParentWorkCode2().toString(), e.getStartDate());
		};
		Map<String, List<WorkPlaceDto>> childrens = new HashMap<>();
		childrens.put(ROOT, new ArrayList<>());
		createTree(workPlaces, childrens, converter);
		return childrens.get(ROOT);
	}

	private void createTree(List<WorkPlace> workPlaces, Map<String, List<WorkPlaceDto>> childrens,
			Function<WorkPlace, WorkPlaceDto> converter) {
		if (workPlaces.size() > 0) {
			WorkPlace dto = workPlaces.remove(0);
			WorkPlaceDto organizationDto = converter.apply(dto);
			childrens.put(dto.getHierarchyCode().toString(), organizationDto.getChildren());

			String inLevelCd = dto.getHierarchyCode().toString();
			if (inLevelCd.length() > 3) {
				int toIndex = inLevelCd.length() - 3;
				String parentInLevelCd = inLevelCd.substring(0, toIndex);

				if (childrens.containsKey(parentInLevelCd)) {
					List<WorkPlaceDto> childrenDepts = childrens.get(parentInLevelCd);
					childrenDepts.add(organizationDto);
				} else {
					childrens.get(ROOT).add(organizationDto);
				}
			} else {
				childrens.get(ROOT).add(organizationDto);
			}
			this.createTree(workPlaces, childrens, converter);
		}
	}

	public boolean checkExist(String companyCode) {
		return workPlaceRepository.checkExist(companyCode);
	}

	public List<WorkPlace> findHistories(String companyCode) {
		return workPlaceRepository.findHistories(companyCode);
	}

	public List<WorkPlaceDto> findAllByHistory(String companyCode, String historyId) {
		List<WorkPlace> list = workPlaceRepository.findAllByHistory(companyCode, historyId);
		return list.isEmpty() ? null : convertToTree(list);
	}

	public WorkPlaceMemoDto findMemo(String companyCode, String historyId) {
		Optional<WorkPlaceMemoDto> wkpmemo = workPlaceRepository.findMemo(companyCode, historyId)
				.map(d -> WorkPlaceMemoDto.fromDomain(d));
		return wkpmemo.isPresent()
				? new WorkPlaceMemoDto(wkpmemo.get().getHistoryId(), wkpmemo.get().getMemo().toString()) : null;
	}
	
	
	public List<WorkPlaceHistoryDto> getAllHistory(String companyCode) {
		List<WorkPlaceHistoryDto> histories = workPlaceRepository.findHistories(companyCode).stream().map(e -> {
			return new WorkPlaceHistoryDto(e.getHistoryId(), e.getStartDate(), e.getEndDate());
		}).collect(Collectors.toList());
		return histories;
	}

}
