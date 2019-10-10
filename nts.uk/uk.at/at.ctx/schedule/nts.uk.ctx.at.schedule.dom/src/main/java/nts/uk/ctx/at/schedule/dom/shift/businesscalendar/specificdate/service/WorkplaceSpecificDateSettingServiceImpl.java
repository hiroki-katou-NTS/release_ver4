package nts.uk.ctx.at.schedule.dom.shift.businesscalendar.specificdate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.ScWorkplaceAdapter;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.specificdate.item.SpecificDateItem;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.specificdate.primitives.SpecificDateItemNo;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.specificdate.repository.SpecificDateItemRepository;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.company.CompanySpecificDateItem;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.company.CompanySpecificDateRepository;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.workplace.WorkplaceSpecificDateItem;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.workplace.WorkplaceSpecificDateRepository;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WorkplaceSpecificDateSettingServiceImpl implements IWorkplaceSpecificDateSettingService {
	
	@Inject
	private CompanySpecificDateRepository companySpecificDateRepository;
	
	@Inject
	private WorkplaceSpecificDateRepository workplaceSpecificDateRepository;
	
	@Inject
	private SpecificDateItemRepository specificDateItemRepository;
	
	@Inject
	private ScWorkplaceAdapter scWorkplaceAdapter;

	@Override
	public SpecificDateItemOutput workplaceSpecificDateSettingService(String companyID, String workPlaceID, GeneralDate date) {
		List<SpecificDateItemNo> specificDateItemList = new ArrayList<SpecificDateItemNo>();
		List<CompanySpecificDateItem> companySpecificDateItemList = companySpecificDateRepository.getComSpecByDateWithName(companyID, date, date);
		if(!CollectionUtil.isEmpty(companySpecificDateItemList)){
			List<Integer> numberList = companySpecificDateItemList.stream().map(x -> x.getSpecificDateItemNo().v()).collect(Collectors.toList());
			numberList.stream().distinct();
			List<SpecificDateItem> currentList = specificDateItemRepository.getSpecifiDateByListCode(companyID, numberList);
			specificDateItemList.addAll(currentList.stream().map(x -> x.getSpecificDateItemNo()).collect(Collectors.toList()));
		}
			
		// アルゴリズム「職場IDから上位職場を取得する」を実行する ( Acquire upper workplace from workplace ID )
		List<String> workplaceIDList = scWorkplaceAdapter.findParentWpkIdsByWkpId(companyID, workPlaceID, date);
		workplaceIDList.stream().forEach(workplace -> {
			List<WorkplaceSpecificDateItem> workplaceSpecificDateItemList = workplaceSpecificDateRepository.getWpSpecByDateWithName(workplace, date, date);
			workplaceSpecificDateItemList.stream().distinct();
			workplaceSpecificDateItemList.forEach(item -> {
				if(!specificDateItemList.contains(item.getSpecificDateItemNo())){
					specificDateItemList.add(item.getSpecificDateItemNo());
				}
			});
		});
		return new SpecificDateItemOutput(date, specificDateItemList.stream().distinct().map(x -> x.v().intValue()).collect(Collectors.toList()));
	}

	@Override
	public SpecificDateItemOutput findSpecDateSetByWkpLst(String companyID, List<String> workPlaceIDLst,
			GeneralDate date) {
		List<SpecificDateItemNo> specificDateItemList = new ArrayList<SpecificDateItemNo>();
		List<CompanySpecificDateItem> companySpecificDateItemList = companySpecificDateRepository.getComSpecByDateWithName(companyID, date, date);
		if(!CollectionUtil.isEmpty(companySpecificDateItemList)){
			List<Integer> numberList = companySpecificDateItemList.stream().map(x -> x.getSpecificDateItemNo().v()).collect(Collectors.toList());
			numberList.stream().distinct();
			List<SpecificDateItem> currentList = specificDateItemRepository.getSpecifiDateByListCode(companyID, numberList);
			specificDateItemList.addAll(currentList.stream().map(x -> x.getSpecificDateItemNo()).collect(Collectors.toList()));
		}
		workPlaceIDLst.stream().forEach(workplace -> {
			List<WorkplaceSpecificDateItem> workplaceSpecificDateItemList = workplaceSpecificDateRepository.getWpSpecByDateWithName(workplace, date, date);
			workplaceSpecificDateItemList.stream().distinct();
			workplaceSpecificDateItemList.forEach(item -> {
				if(!specificDateItemList.contains(item.getSpecificDateItemNo())){
					specificDateItemList.add(item.getSpecificDateItemNo());
				}
			});
		});
		return new SpecificDateItemOutput(date, specificDateItemList.stream().distinct().map(x -> x.v().intValue()).collect(Collectors.toList()));
	}
	
}
