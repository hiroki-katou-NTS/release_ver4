package nts.uk.screen.at.app.query.kdl.kdl014.a;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.stamp.application.CommonSettingsStampInput;
import nts.uk.ctx.at.record.dom.stamp.application.CommonSettingsStampInputRepository;
import nts.uk.ctx.at.record.dom.stamp.application.SettingsUsingEmbossing;
import nts.uk.ctx.at.record.dom.stamp.application.SettingsUsingEmbossingRepository;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCard;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCardRepository;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampNumber;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.WorkLocation;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.WorkLocationRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampDakokuRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.EmployeeStampInfo;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.GetListStampEmployeeService;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.StampInfoDisp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkLocationCD;
import nts.uk.query.model.employee.EmployeeInformation;
import nts.uk.query.model.employee.EmployeeInformationQuery;
import nts.uk.query.model.employee.EmployeeInformationRepository;
import nts.uk.query.pub.employee.EmployeeInformationExport;
import nts.uk.query.pub.employee.EmployeeInformationPub;
import nts.uk.query.pub.employee.EmployeeInformationQueryDto;
import nts.uk.screen.at.app.query.kdl.kdl014.a.dto.EmpInfomationDto;
import nts.uk.screen.at.app.query.kdl.kdl014.a.dto.Kdl014EmpParamDto;
import nts.uk.screen.at.app.query.kdl.kdl014.a.dto.ReferToTheStampingResultsDto;
import nts.uk.shr.com.context.AppContexts;

/**UKDesign.UniversalK.??????.KDL_???????????????.KDL014_???????????????????????????.???????????????OCD.???????????????????????????.???????????????????????????*/
@Stateless
public class ReferToTheStampingResultsFinder {

	@Inject
	private StampCardRepository stampCardRepository;

	@Inject
	private StampDakokuRepository stampDakokuRepository;

	@Inject
	private EmployeeInformationRepository empInfoRepo;

	@Inject
	private CommonSettingsStampInputRepository commonSettingsStampInputRepo;
	
	@Inject
	private WorkLocationRepository workLocationRepo;
	
	@Inject
	private EmployeeInformationPub employeeInformationPub;
	
	@Inject
	private SettingsUsingEmbossingRepository embossingRepository;
	
	public ReferToTheStampingResultsDto get(Kdl014EmpParamDto param) {
		
		Optional<SettingsUsingEmbossing> embo = embossingRepository.get(AppContexts.user().companyId());
		
		// 1.????????????(@Require, ??????ID, ?????????) -> ????????????????????? / EmployeeStampInfo
		List<EmployeeStampInfo> listEmployeeStampInfo = new ArrayList<>(); // list ?????????????????????

		List<String> employeeIds = step1(param, listEmployeeStampInfo);
		
		// 2.<call> ?????????????????????????????? -> List<????????????>
		List<EmployeeInformation> listEmpInfo = step2(param, employeeIds);
		List<EmployeeInformation> listEmpInfo1 = new ArrayList<>();
		listEmpInfo1 = listEmpInfo.stream().sorted((o1, o2) -> o1.getEmployeeCode().compareTo(o2.getEmployeeCode())).collect(Collectors.toList());
		
		// 3.get*(List<????????????????????????????????????????????????) -> List<????????????>
		List<WorkLocation> workLocationList = step3(listEmployeeStampInfo);
		
		// 4.get(??????ID) -> GoogleMap?????????????????????????????????????????????
		ReferToTheStampingResultsDto result = step4(listEmployeeStampInfo, listEmpInfo1, workLocationList);
		
		if(param.getMode() == 0 && param.getListEmp().size() == 1 && result.getListEmps().isEmpty()) throw new BusinessException("Msg_1617");
		
		if(param.getMode() == 1 && result.getListEmps().isEmpty()) throw new BusinessException("Msg_1617");
		
		result.getListEmps().stream().sorted((o1, o2) -> o1.getCode().compareTo(o2.getCode())).collect(Collectors.toList());
		
		if (embo.isPresent()) {
			result.setCheckMobile(embo.get().isSmart_phone());
		} else {
			result.setCheckMobile(false);
		}
		
		return result;
	}

	private List<String> step1(Kdl014EmpParamDto param, List<EmployeeStampInfo> listEmployeeStampInfo) {
		List<String> employeeIds = param.getListEmp();
		
		GetListStampEmployeeService.Require require = new RequireImpl(stampCardRepository, stampDakokuRepository);
		
		DatePeriod period = new DatePeriod(param.getStart(), param.getEnd());
		
		employeeIds.stream().forEach(empId -> {
			period.datesBetween().stream().forEach(date -> {
				Optional<EmployeeStampInfo> optEmStampInfo = GetListStampEmployeeService.get(require, empId, date);
		
				if (optEmStampInfo.isPresent()) listEmployeeStampInfo.add(optEmStampInfo.get());
			});
		});
		
		return employeeIds;
	}

	private List<EmployeeInformation> step2(Kdl014EmpParamDto param, List<String> employeeIds) {
		EmployeeInformationQuery infoQuery = EmployeeInformationQuery.builder()
				.employeeIds(employeeIds)
				.referenceDate(param.getEnd())
				.toGetClassification(false)
				.toGetDepartment(false)
				.toGetEmployment(false)
				.toGetEmploymentCls(false)
				.toGetPosition(false)
				.toGetWorkplace(false).build();
		
		return empInfoRepo.find(infoQuery);
	}
	
	private List<WorkLocation> step3(List<EmployeeStampInfo> listEmployeeStampInfo) {
		
		List<StampInfoDisp> listStampInfoDisp = listEmployeeStampInfo.stream().flatMap(m->m.getListStampInfoDisp().stream()).collect(Collectors.toList());

		List<Stamp> listStamp = new ArrayList<>();
		
		listStampInfoDisp.stream().forEach(f -> {
			if (f.getStamp().isPresent()){
				listStamp.add(f.getStamp().get());
			}
		});
		
		List<Stamp> listStampFilter = listStamp.stream().filter(m -> m.getRefActualResults().getWorkInforStamp().isPresent()).collect(Collectors.toList());
		
		List<WorkLocationCD> listWorkLocation = listStampFilter.stream().filter(m->m.getRefActualResults().getWorkInforStamp().get().getWorkLocationCD().isPresent()).map(m->m.getRefActualResults().getWorkInforStamp().get().getWorkLocationCD().get()).collect(Collectors.toList());

		List<String> listWorkLocationCode = listWorkLocation.stream().map(m->m.v()).distinct().collect(Collectors.toList());
		
		List<WorkLocation> workLocationList = workLocationRepo.findByCodes(AppContexts.user().contractCode(), listWorkLocationCode);
		
		return workLocationList;
	}

	private ReferToTheStampingResultsDto step4(List<EmployeeStampInfo> listEmployeeStampInfo,
			List<EmployeeInformation> listEmpInfo, List<WorkLocation> workLocationList) {
		
		Optional<CommonSettingsStampInput> optSettingStampInput = commonSettingsStampInputRepo.get(AppContexts.user().companyId());
		
		ReferToTheStampingResultsDto result = new ReferToTheStampingResultsDto();
		
		if(optSettingStampInput.isPresent()) {
			CommonSettingsStampInput input = optSettingStampInput.get();
			
			result.setDisplay(input.isGooglemap());

			if (input.getMapAddres().isPresent()) result.setAddress(input.getMapAddres().get().v());
		}
		
		List<EmpInfomationDto> listEmps = new ArrayList<>();
		
		listEmpInfo.stream().forEach(empInfo -> {
			
			List<EmployeeStampInfo> employeeStampListInfo = listEmployeeStampInfo.stream()
					.filter(c -> c.getEmployeeId().equals(empInfo.getEmployeeId())).collect(Collectors.toList());
			
			employeeStampListInfo.stream().forEach(stampInfo -> {
				stampInfo.getListStampInfoDisp().stream().forEach(stamp -> {
					
					if (stamp.getStamp().isPresent()) {
						Optional<WorkLocation> wl = Optional.empty();
						
						if (stamp.getStamp().get().getRefActualResults().getWorkInforStamp().isPresent() 
								&& stamp.getStamp().get().getRefActualResults().getWorkInforStamp().get().getWorkLocationCD().isPresent()) {
							wl = workLocationList.stream()
									.filter(c -> c.getWorkLocationCD().v()
											.equals(stamp.getStamp().get().getRefActualResults().getWorkInforStamp().get().getWorkLocationCD().get().v()))
									.findFirst();
						}
						
						EmpInfomationDto em = createEmpInfoDto(empInfo, stamp, stamp.getStamp().get(), wl);
						
						listEmps.add(em);
					}
				});
			});
		});
		
		result.setListEmps(listEmps);
		
		return result;
	}

	private EmpInfomationDto createEmpInfoDto(EmployeeInformation empInfo, StampInfoDisp stamp, Stamp st,
			Optional<WorkLocation> wl) {
		
		return new EmpInfomationDto(
				empInfo.getEmployeeId(),
				empInfo.getEmployeeCode(),
				empInfo.getBusinessName(),
				st.getStampDateTime(),
				st.getRelieve().getStampMeans().value,
				stamp.getStampAtr(),
				wl.isPresent() ? wl.get().getWorkLocationName().v() : null,
				st.getLocationInfor().isPresent() ? st.getLocationInfor().get() : null);
	}
	
	public List<EmployeeInformationExport> getEmployeeData(Kdl014EmpParamDto param) {
	    EmployeeInformationQueryDto dataParam = new EmployeeInformationQueryDto(
	            param.getListEmp(), 
	            param.getEnd(), 
	            true, 
	            false, 
	            false, 
	            false, 
	            false, 
	            false);
	    return employeeInformationPub.find(dataParam);
	}

	@AllArgsConstructor
	private static class RequireImpl implements GetListStampEmployeeService.Require {
		
		private StampCardRepository stampCardRepository;
		
		private StampDakokuRepository stampDakokuRepository;

		@Override
		public List<StampCard> getListStampCard(String sid) {
			return stampCardRepository.getListStampCard(sid);
		}

		@Override
		public List<Stamp> getStamp(List<StampNumber> stampNumbers, GeneralDate date) {
			return stampDakokuRepository.get(AppContexts.user().contractCode(), stampNumbers, date);
		}
	}
}
