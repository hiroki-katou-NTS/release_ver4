package nts.uk.ctx.pereg.app.find.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employeeinfo.Employee;
import nts.uk.ctx.bs.employee.dom.employeeinfo.EmployeeRepository;
import nts.uk.ctx.pereg.app.find.common.MappingFactory;
import nts.uk.ctx.pereg.app.find.layout.LayoutQuery;
import nts.uk.ctx.pereg.app.find.layout.dto.EmpMaintLayoutDto;
import nts.uk.ctx.pereg.app.find.layoutdef.classification.LayoutPersonInfoClsDto;
import nts.uk.ctx.pereg.app.find.layoutdef.classification.LayoutPersonInfoClsFinder;
import nts.uk.ctx.pereg.app.find.person.category.PerInfoCategoryFinder;
import nts.uk.ctx.pereg.app.find.person.category.PerInfoCtgFullDto;
import nts.uk.ctx.pereg.app.find.person.info.item.PerInfoItemDefForLayoutDto;
import nts.uk.ctx.pereg.app.find.person.info.item.PerInfoItemDefForLayoutFinder;
import nts.uk.ctx.pereg.dom.person.ParamForGetPerItem;
import nts.uk.ctx.pereg.dom.person.PerInfoCtgDomainService;
import nts.uk.ctx.pereg.dom.person.additemdata.item.EmpInfoItemDataRepository;
import nts.uk.ctx.pereg.dom.person.info.category.IsFixed;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonEmployeeType;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.pereg.dom.person.layout.classification.LayoutItemType;
import nts.uk.ctx.pereg.dom.person.personinfoctgdata.item.PerInfoItemDataRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.dto.EmpOptionalDto;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;
import nts.uk.shr.pereg.app.find.dto.PeregDto;
import nts.uk.shr.pereg.app.find.dto.PersonOptionalDto;

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
	
	@Inject
	private EmployeeRepository employeeRepository;
	
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
	public EmpMaintLayoutDto getLayout(LayoutQuery layoutQuery) {
		List<LayoutPersonInfoClsDto> itemClassList = this.clsFinder.getListClsDto(layoutQuery.getLayoutId());
		EmpMaintLayoutDto empMaintLayoutDto = new EmpMaintLayoutDto();
		// get Employee
		Employee employee = employeeRepository.findBySid(AppContexts.user().companyId(), layoutQuery.getBrowsingEmpId()).get();
		itemClassList.forEach(item -> {
			// get ctgCd
			PersonInfoCategory perInfoCtg = perInfoCtgRepositoty
					.getPerInfoCategory(item.getPersonInfoCategoryID(), AppContexts.user().contractCode()).get();
			PeregQuery query = new PeregQuery(item.getPersonInfoCategoryID(), perInfoCtg.getCategoryCode().v(),
					layoutQuery.getBrowsingEmpId(), employee.getPId(), GeneralDate.legacyDate(layoutQuery.getStandardDate()), null);
			if (item.getLayoutItemType() == LayoutItemType.LIST) {
				// //get data
				List<PeregDto> lstPeregDtos = layoutingProcessor.findList(query);
				List<Object> dataTable = new ArrayList<>();
				lstPeregDtos.forEach(peregDto -> {
					PeregDomainDto finderDto = peregDto.getDomainDto();
					List<PersonOptionalDto> perOptionalData = peregDto.getPerOptionalData();
					List<EmpOptionalDto> empOptionalData = peregDto.getEmpOptionalData();
					EmpMaintLayoutDto empLayoutDto = new EmpMaintLayoutDto();
					matching(empLayoutDto, perInfoCtg, finderDto, peregDto.getDtoClass(),
							item.getListItemDf().stream()
									.map(x -> perInfoItemDefForLayoutFinder.createFromItemDefDto(layoutQuery.getBrowsingEmpId(),
											x, perInfoCtg.getCategoryCode().v(), item.getDispOrder()))
									.collect(Collectors.toList()),
							empOptionalData, perOptionalData);
					dataTable.add(empLayoutDto.getClassificationItems());
				});
				item.setItems(dataTable);

			} else {
				setEmpMaintLayoutDto(empMaintLayoutDto, query, perInfoCtg,
						item.getListItemDf().stream()
								.map(x -> perInfoItemDefForLayoutFinder.createFromItemDefDto(layoutQuery.getBrowsingEmpId(), x,
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
	 * @return EmpMaintLayoutDto
	 */
	public EmpMaintLayoutDto getCategoryChild(PeregQuery query) {
		return getCategoryDetail(query, true);
	}
	
	/**
	 * get sub detail data in tab
	 * 
	 * @param query
	 * @return EmpMaintLayoutDto
	 */
	public EmpMaintLayoutDto getSubDetailInCtgChild(PeregQuery query) {
		return getCategoryDetail(query, false);
	}
	
	/**
	 * get detail data in tab
	 * 
	 * @param query - params received from client
	 * @param isMainDetail - to detect detail tab or sub detail in tab
	 * @return EmpMaintLayoutDto
	 */
	private EmpMaintLayoutDto getCategoryDetail(PeregQuery query, boolean isMainDetail){
		// app context
		String contractCode = AppContexts.user().contractCode();
		String companyId = AppContexts.user().companyId();
		String loginEmpId = AppContexts.user().employeeId();
		// String roleId = AppContexts.user().roles().forPersonalInfo();
		String roleId = "99900000-0000-0000-0000-000000000001";

		// get ctgCd
		PersonInfoCategory perInfoCtg = perInfoCtgRepositoty.getPerInfoCategory(query.getCategoryId(), contractCode)
				.get();
		String ctgCode = perInfoCtg.getCategoryCode().v();
		if(!isMainDetail) ctgCode = perInfoCtg.getCategoryCode().v() + "SD";
		query.setCategoryCode(ctgCode);

		// get PerInfoItemDefForLayoutDto
		// check per info auth
		if (!perInfoCategoryFinder.checkPerInfoCtgAuth(query.getEmployeeId(), perInfoCtg.getPersonInfoCategoryId()))
			return new EmpMaintLayoutDto();

		// get item def
		List<PersonInfoItemDefinition> lstItemDef = perInfoCtgDomainService
				.getPerItemDef(new ParamForGetPerItem(perInfoCtg, query.getInfoId(), roleId == null ? "" : roleId,
													companyId, contractCode, loginEmpId.equals(query.getEmployeeId())));
		if (lstItemDef.size() == 0)
			return new EmpMaintLayoutDto();
		List<PerInfoItemDefForLayoutDto> lstPerInfoItemDef = new ArrayList<>();
		for (int i = 0; i < lstItemDef.size(); i++)
			lstPerInfoItemDef.add(perInfoItemDefForLayoutFinder.createFromDomain(query.getEmployeeId(),
					lstItemDef.get(i), query.getCategoryCode(), i));

		EmpMaintLayoutDto empMaintLayoutDto = new EmpMaintLayoutDto();
		
		//set fix data
		setEmpMaintLayoutDto(empMaintLayoutDto, query, perInfoCtg, lstPerInfoItemDef);
		
		// set optional data
		setOptionalData(empMaintLayoutDto, query.getInfoId() == null ? perInfoCtg.getPersonInfoCategoryId() : query.getInfoId(), perInfoCtg, lstPerInfoItemDef);
		
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
			PeregDto queryResult = layoutingProcessor.findSingle(query);

			//set fixed data	
			MappingFactory.mapListClsDto(empMaintLayoutDto, queryResult, lstPerInfoItemDef);
		}else{
			setOptionalData(empMaintLayoutDto, query.getInfoId() == null ? perInfoCtg.getPersonInfoCategoryId() : query.getInfoId(), perInfoCtg, lstPerInfoItemDef);
		}
	}
	
	private void matching(EmpMaintLayoutDto empMaintLayoutDto, PersonInfoCategory perInfoCtg, Object dto, Class<?> finderClass, 
			List<PerInfoItemDefForLayoutDto> lstPerInfoItemDef, List<EmpOptionalDto> empOptionalData, List<PersonOptionalDto> perOptionalData ){
		LayoutMapping.mapFixDto(empMaintLayoutDto, dto, finderClass, lstPerInfoItemDef);
		if(perInfoCtg.getPersonEmployeeType() == PersonEmployeeType.EMPLOYEE)
				LayoutMapping.mapEmpOptionalDto(empMaintLayoutDto, empOptionalData, lstPerInfoItemDef);
		else LayoutMapping.mapPerOptionalDto(empMaintLayoutDto, perOptionalData, lstPerInfoItemDef);
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
		List<EmpOptionalDto> lstCtgItemOptionalDto = empInfoItemDataRepository.getAllInfoItemByRecordId(recordId)
				.stream().map(x -> x.genToPeregDto()).collect(Collectors.toList());
		if(lstCtgItemOptionalDto.size() > 0)
			LayoutMapping.mapEmpOptionalDto(empMaintLayoutDto, lstCtgItemOptionalDto, lstPerInfoItemDef);
	}
	
	/**
	 * set person optional data
	 * 
	 * @param empMaintLayoutDto
	 * @param recordId
	 * @param lstPerInfoItemDef
	 */
	private void setPerInfoItemData(EmpMaintLayoutDto empMaintLayoutDto, String recordId, List<PerInfoItemDefForLayoutDto> lstPerInfoItemDef){
		List<PersonOptionalDto> lstCtgItemOptionalDto = perInfoItemDataRepository.getAllInfoItemByRecordId(recordId)
				.stream().map(x -> x.genToPeregDto()).collect(Collectors.toList());
		
		if(lstCtgItemOptionalDto.size() > 0)
			LayoutMapping.mapPerOptionalDto(empMaintLayoutDto, lstCtgItemOptionalDto, lstPerInfoItemDef);
	}

}
