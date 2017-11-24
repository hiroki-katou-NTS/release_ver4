package nts.uk.ctx.pereg.app.find.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import find.layout.classification.LayoutPersonInfoClsDto;
import find.layout.classification.LayoutPersonInfoClsFinder;
import find.person.info.category.PerInfoCategoryFinder;
import find.person.info.category.PerInfoCtgFullDto;
import find.person.info.item.PerInfoItemDefForLayoutDto;
import find.person.info.item.PerInfoItemDefForLayoutFinder;
import nts.uk.ctx.bs.employee.app.find.layout.dto.EmpMaintLayoutDto;
import nts.uk.ctx.bs.employee.dom.person.ParamForGetPerItem;
import nts.uk.ctx.bs.employee.dom.person.PerInfoCtgDomainService;
import nts.uk.ctx.bs.employee.dom.regpersoninfo.personinfoadditemdata.item.EmpInfoItemData;
import nts.uk.ctx.bs.employee.dom.regpersoninfo.personinfoadditemdata.item.EmpInfoItemDataRepository;
import nts.uk.ctx.bs.person.dom.person.info.category.IsFixed;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonEmployeeType;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.bs.person.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.bs.person.dom.person.layout.classification.LayoutItemType;
import nts.uk.ctx.bs.person.dom.person.personinfoctgdata.item.PerInfoItemDataRepository;
import nts.uk.ctx.bs.person.dom.person.personinfoctgdata.item.PersonInfoItemData;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.find.LayoutingProcessor;
import nts.uk.shr.pereg.app.find.PeregResult;
import nts.uk.shr.pereg.app.find.PeregMaintLayoutQuery;
import nts.uk.shr.pereg.app.find.PeregQuery;

@Stateless
public class PeregProcessor {
	@Inject
	private PerInfoCategoryFinder perInfoCategoryFinder;
	
	@Inject
	private PerInfoCtgDomainService perInfoCtgDomainService;
	
	@Inject
	private PerInfoItemDefForLayoutFinder perInfoItemDefForLayoutFinder;
	
	@Inject
	private EmpInfoItemDataRepository empInfoItemDataRepository;
	
	@Inject
	private PerInfoItemDataRepository perInfoItemDataRepository;
	
	@Inject
	private LayoutingProcessor layoutingProcessor;
	
	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRepositoty;
	
	@Inject
	 private LayoutPersonInfoClsFinder clsFinder;
	
	/**
	 * get person information category and it's children (Hiển thị category và
	 * danh sách tab category con của nó)
	 * 
	 * @param ctgId
	 * @return list PerCtgInfo: cha va danh sach con
	 */
	public List<PerInfoCtgFullDto> getCtgTab(String ctgId) {
		String contractCode = AppContexts.user().contractCode();
		String companyId = AppContexts.user().companyId();
		PersonInfoCategory perInfoCtg = perInfoCtgRepositoty.getPerInfoCategory(ctgId, contractCode).get();
		List<PersonInfoCategory> lstPerInfoCtg = new ArrayList<>();
		lstPerInfoCtg = perInfoCtgRepositoty.getPerInfoCtgByParentCdWithOrder(perInfoCtg.getCategoryCode().v(),
				contractCode, companyId, true);
		lstPerInfoCtg.add(0, perInfoCtg);
		return lstPerInfoCtg.stream().map(x -> new PerInfoCtgFullDto(x.getPersonInfoCategoryId(), x.getCategoryCode().v(), x.getCategoryParentCode().v(),
				x.getCategoryName().v(), x.getPersonEmployeeType().value, x.getIsAbolition().value, x.getCategoryType().value, x.getIsFixed().value))
				.collect(Collectors.toList());
	}
	
	/**
	 * get data in layout
	 * 
	 * @param query
	 * @return
	 */
	public EmpMaintLayoutDto getLayout(PeregMaintLayoutQuery layoutQuery) {
		List<LayoutPersonInfoClsDto> itemClassList = this.clsFinder.getListClsDto(layoutQuery.getLayoutId());
		EmpMaintLayoutDto empMaintLayoutDto = new EmpMaintLayoutDto();
		itemClassList.forEach(item -> {
			// get ctgCd
			PersonInfoCategory perInfoCtg = perInfoCtgRepositoty
					.getPerInfoCategory(item.getPersonInfoCategoryID(), AppContexts.user().contractCode()).get();
			PeregQuery query = new PeregQuery(item.getPersonInfoCategoryID(), perInfoCtg.getCategoryCode().v(),
					layoutQuery.getEmpId(), layoutQuery.getStandardDate(), null);
			if (item.getLayoutItemType() == LayoutItemType.LIST) {
				// //get data
				PeregResult returnValue = layoutingProcessor.findList(query);
				List<PeregQueryResult> lstQueryResult = ((List<Object>) returnValue.getDto()).stream()
						.map(x -> PeregQueryResult.toObject(x)).collect(Collectors.toList());
				List<Object> dataTable = new ArrayList<>();
				lstQueryResult.forEach(queryResult -> {

					Object finderDto = queryResult.getDto();
					List<PersonInfoItemData> perOptionalData = queryResult.getPerOptionalData();
					List<EmpInfoItemData> empOptionalData = queryResult.getEmpOptionalData();
					EmpMaintLayoutDto empLayoutDto = new EmpMaintLayoutDto();
					matching(empLayoutDto, perInfoCtg, finderDto, returnValue.getDtoClass(),
							item.getListItemDf().stream()
									.map(x -> perInfoItemDefForLayoutFinder.createFromItemDefDto(layoutQuery.getEmpId(),
											x, perInfoCtg.getCategoryCode().v(), item.getDispOrder()))
									.collect(Collectors.toList()),
							empOptionalData, perOptionalData);
					// dto -> list LayoutPersonInfoValueDto
					// row.addAll(list LayoutPersonInfoValueDto);
					// perOptionalData -> list LayoutPersonInfoValueDto
					// row.addAll(list LayoutPersonInfoValueDto)
					dataTable.add(empLayoutDto.getClassificationItems());
				});
				item.setItems(dataTable);

			} else {
				setEmpMaintLayoutDto(empMaintLayoutDto, query, perInfoCtg,
						item.getListItemDf().stream()
								.map(x -> perInfoItemDefForLayoutFinder.createFromItemDefDto(layoutQuery.getEmpId(), x,
										perInfoCtg.getCategoryCode().v(), item.getDispOrder()))
								.collect(Collectors.toList()));
			}
		});
		return empMaintLayoutDto;
	}
	
	/**
	 * get data in tab
	 * 
	 * @param query
	 * @return 
	 */
	public EmpMaintLayoutDto getCategoryChild(PeregQuery query)
	{
		// app context
		String contractCode = AppContexts.user().contractCode();
		String companyId = AppContexts.user().companyId();
		String loginEmpId = AppContexts.user().employeeId();
		//String roleId = AppContexts.user().roles().forPersonalInfo();
		String roleId = "99900000-0000-0000-0000-000000000001";
		
		// get ctgCd
		PersonInfoCategory perInfoCtg = perInfoCtgRepositoty.getPerInfoCategory(query.getCtgId(), contractCode).get();
		query.setCtgCd(perInfoCtg.getCategoryCode().v());
		
		// get PerInfoItemDefForLayoutDto
		//check per info auth
		if(!perInfoCategoryFinder.checkPerInfoCtgAuth(query.getEmpId(), perInfoCtg.getPersonInfoCategoryId()))
			return new EmpMaintLayoutDto();
		
		//get item def
		List<PersonInfoItemDefinition> lstItemDef = perInfoCtgDomainService
				.getPerItemDef(new ParamForGetPerItem(perInfoCtg, query.getInfoId(), roleId == null ? "" : roleId, companyId,
						contractCode, loginEmpId.equals(query.getEmpId())));
		if(lstItemDef.size() == 0) return new EmpMaintLayoutDto();
		List<PerInfoItemDefForLayoutDto> lstPerInfoItemDef = new ArrayList<>();
		for (int i = 0; i < lstItemDef.size(); i++)
			lstPerInfoItemDef.add(perInfoItemDefForLayoutFinder.createFromDomain(query.getEmpId(), lstItemDef.get(i), query.getCtgCd(), i));
		
		EmpMaintLayoutDto empMaintLayoutDto = new EmpMaintLayoutDto();
		setEmpMaintLayoutDto(empMaintLayoutDto, query, perInfoCtg, lstPerInfoItemDef);
		
		//set optional data
		return empMaintLayoutDto;
	}
	
	/**
	 * set data in tab
	 * 
	 * @param empMaintLayoutDto
	 * @param query
	 * @param perInfoCtg
	 * @param lstPerInfoItemDef
	 */
	
	private void setEmpMaintLayoutDto(EmpMaintLayoutDto empMaintLayoutDto, PeregQuery query, PersonInfoCategory perInfoCtg, List<PerInfoItemDefForLayoutDto> lstPerInfoItemDef){
		if(perInfoCtg.getIsFixed() == IsFixed.FIXED){
			//get domain data
			PeregResult returnValue = layoutingProcessor.findSingle(query);
			PeregQueryResult queryResult = PeregQueryResult.toObject(returnValue.getDto());

			//set fixed data			
			matching(empMaintLayoutDto, perInfoCtg,  queryResult.getDto(), returnValue.getDtoClass(), 
					lstPerInfoItemDef, queryResult.getEmpOptionalData(), queryResult.getPerOptionalData());
		}else{
			setOptionalData(empMaintLayoutDto, query.getInfoId() == null ? perInfoCtg.getPersonInfoCategoryId() : query.getInfoId(), perInfoCtg, lstPerInfoItemDef);
		}
	}
	
	private void matching(EmpMaintLayoutDto empMaintLayoutDto, PersonInfoCategory perInfoCtg, Object dto, Class<?> finderClass, 
			List<PerInfoItemDefForLayoutDto> lstPerInfoItemDef, List<EmpInfoItemData> empOptionalData, List<PersonInfoItemData> perOptionalData ){
		LayoutMapping.mapFixDto(empMaintLayoutDto, dto, finderClass, lstPerInfoItemDef);
		
		int startOptionDtoPos = lstPerInfoItemDef.size();
		if(perInfoCtg.getPersonEmployeeType() == PersonEmployeeType.EMPLOYEE)
				LayoutMapping.mapEmpOptionalDto(empMaintLayoutDto, empOptionalData, lstPerInfoItemDef, startOptionDtoPos);
		else LayoutMapping.mapPerOptionalDto(empMaintLayoutDto, perOptionalData, lstPerInfoItemDef, startOptionDtoPos);
	}
	
	/**
	 * set optional data in tab
	 * 
	 * @param empMaintLayoutDto
	 * @param recordId
	 * @param perInfoCtg
	 * @param lstPerInfoItemDef
	 */
	private void setOptionalData(EmpMaintLayoutDto empMaintLayoutDto, String recordId, PersonInfoCategory perInfoCtg, List<PerInfoItemDefForLayoutDto> lstPerInfoItemDef){
		if(perInfoCtg.getPersonEmployeeType() == PersonEmployeeType.EMPLOYEE)
			setEmpInfoItemData(empMaintLayoutDto, recordId, lstPerInfoItemDef);
		else 
			setPerInfoItemData(empMaintLayoutDto, recordId, lstPerInfoItemDef);;
	}
	
	/**
	 * set employee optional data
	 * 
	 * @param empMaintLayoutDto
	 * @param recordId
	 * @param lstPerInfoItemDef
	 */
	private void setEmpInfoItemData(EmpMaintLayoutDto empMaintLayoutDto, String recordId, List<PerInfoItemDefForLayoutDto> lstPerInfoItemDef){
		List<EmpInfoItemData> lstCtgItemOptionalDto = empInfoItemDataRepository.getAllInfoItemByRecordId(recordId);
		if(lstCtgItemOptionalDto.size() > 0)
			LayoutMapping.mapEmpOptionalDto(empMaintLayoutDto, lstCtgItemOptionalDto, lstPerInfoItemDef, 0);
	}
	
	/**
	 * set person optional data
	 * 
	 * @param empMaintLayoutDto
	 * @param recordId
	 * @param lstPerInfoItemDef
	 */
	private void setPerInfoItemData(EmpMaintLayoutDto empMaintLayoutDto, String recordId, List<PerInfoItemDefForLayoutDto> lstPerInfoItemDef){
		List<PersonInfoItemData> lstCtgItemOptionalDto = perInfoItemDataRepository.getAllInfoItemByRecordId(recordId);
		if(lstCtgItemOptionalDto.size() > 0)
			LayoutMapping.mapPerOptionalDto(empMaintLayoutDto, lstCtgItemOptionalDto, lstPerInfoItemDef, 0);
	}

}
