package nts.uk.ctx.at.shared.app.find.remainingnumber.annualleave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoDomService;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnLeaMaxDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxData;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.RervLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.ReserveLeaveGrantRemainingData;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.ComboBoxObject;
import nts.uk.shr.pereg.app.find.PeregEmpInfoQuery;
import nts.uk.shr.pereg.app.find.PeregFinder;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.PeregQueryByListEmp;
import nts.uk.shr.pereg.app.find.dto.DataClassification;
import nts.uk.shr.pereg.app.find.dto.GridPeregDomainDto;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Stateless
public class AnnualLeaveFinder implements PeregFinder<AnnualLeaveDto> {

	@Inject
	private AnnLeaEmpBasicInfoRepository annLeaBasicInfoRepo;

	@Inject
	private AnnLeaMaxDataRepository maxDataRepo;
	
	@Inject
	private AnnLeaEmpBasicInfoDomService annLeaDomainService;
	
	@Inject
	private AnnLeaGrantRemDataRepository annLeaDataRepo;
	
	@Inject
	private RervLeaGrantRemDataRepository rervLeaDataRepo;

	@Override
	public String targetCategoryCode() {
		return "CS00024";
	}

	@Override
	public Class<AnnualLeaveDto> dtoClass() {
		return AnnualLeaveDto.class;
	}

	@Override
	public DataClassification dataType() {
		return DataClassification.EMPLOYEE;
	}

	@Override
	public AnnualLeaveDto getSingleData(PeregQuery query) {
		String companyId = AppContexts.user().companyId();
		String employeeId = query.getEmployeeId();
		AnnualLeaveDto dto = new AnnualLeaveDto(employeeId);
		
		
		
		// 年休残数
		List<AnnualLeaveGrantRemainingData> annualLeaveDataList = annLeaDataRepo.findNotExp(employeeId);
		dto.setAnnualLeaveNumber(annLeaDomainService.calculateAnnLeaNumWithFormat(companyId, annualLeaveDataList));
		dto.setLastGrantDate(annLeaDomainService.calculateLastGrantDate(employeeId));

		// 年休社員基本情報
		Optional<AnnualLeaveEmpBasicInfo> basicInfoOpt = annLeaBasicInfoRepo.get(employeeId);
		
		if (basicInfoOpt.isPresent()) {			
			dto.pullDataFromBasicInfo(basicInfoOpt.get());
		}

		// 年休上限データ
		Optional<AnnualLeaveMaxData> maxDataOpt = maxDataRepo.get(employeeId);
		if (maxDataOpt.isPresent()) {
			dto.pullDataFromMaxData(maxDataOpt.get());
		}

		// 積立年休残数
		List<ReserveLeaveGrantRemainingData> rervLeaveDataList = rervLeaDataRepo.findNotExp(employeeId, companyId);
		dto.setResvLeaRemainNumber(annLeaDomainService.calculateRervLeaveNumber(rervLeaveDataList));

		return dto;
	}

	@Override
	public List<PeregDomainDto> getListData(PeregQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ComboBoxObject> getListFirstItems(PeregQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GridPeregDomainDto> getAllData(PeregQueryByListEmp query) {
		List<GridPeregDomainDto> result = new ArrayList<>();
		String cid = AppContexts.user().companyId();
		// key - sid , value - pid   getEmployeeId getPersonId
		Map<String, String> mapSids = query.getEmpInfos().stream()
				.collect(Collectors.toMap(PeregEmpInfoQuery::getEmployeeId, PeregEmpInfoQuery::getPersonId));
		List<String> sids = new ArrayList<String> (mapSids.keySet());
		List<AnnualLeaveDto> resultDto = new ArrayList<>();
		
		setEmployeeId(sids, resultDto);
		
		// 年休残数
		Map<String, List<AnnualLeaveGrantRemainingData>> annualLeaveDataList = annLeaDataRepo.findByCidAndSids(cid, sids).stream().collect(Collectors.groupingBy(c -> c.getEmployeeId()));
		
		resultDto.stream().forEach(c -> {
			List<AnnualLeaveGrantRemainingData> data = annualLeaveDataList.get(c.getRecordId());
			if(data == null) {
				annualLeaveDataList.put(c.getRecordId(), new ArrayList<>());
			}
		});
		
		Map<String, String> numFormatLst = annLeaDomainService.calculateAnnLeaNumWithFormat(cid, annualLeaveDataList);
		
		Map<String, String> lastGrantDateLst = annLeaDomainService.calculateLastGrantDate( cid, sids);
		
		// 年休社員基本情報
		Map<String, List<AnnualLeaveEmpBasicInfo>> basicInfoOptLst = annLeaBasicInfoRepo.getAll( cid, sids).stream().collect(Collectors.groupingBy(c -> c.getEmployeeId()));
		
		// 年休上限データ
		Map<String, List<AnnualLeaveMaxData>> maxDataOptLst =  maxDataRepo.getAll(cid, sids).stream().collect(Collectors.groupingBy(c -> c.getEmployeeId()));
		
		// 積立年休残数
		Map<String, List<ReserveLeaveGrantRemainingData>> rervLeaveDataList = rervLeaDataRepo.getAll(cid, sids).stream().collect(Collectors.groupingBy(c -> c.getEmployeeId()));
		
		resultDto.stream().forEach(c -> {			
			// 積立年休残数
			List<ReserveLeaveGrantRemainingData> rervLeaveData = rervLeaveDataList.get(c.getRecordId());
			if(rervLeaveData == null) {
				rervLeaveDataList.put(c.getRecordId(), new ArrayList<>());
			}
		});
		
		resultDto.stream().forEach(c -> {

			c.setAnnualLeaveNumber(numFormatLst.get(c.getRecordId()));

			c.setLastGrantDate(lastGrantDateLst.get(c.getRecordId()));

			// 年休社員基本情報
			List<AnnualLeaveEmpBasicInfo> basicInfo = basicInfoOptLst.get(c.getRecordId());
			if (basicInfo != null) {
				c.pullDataFromBasicInfo(basicInfo.get(0));
			}

			// 年休上限データ
			List<AnnualLeaveMaxData> maxData = maxDataOptLst.get(c.getRecordId());
			if (maxData != null) {
				c.pullDataFromMaxData(maxData.get(0));
			}

			// 積立年休残数
			List<ReserveLeaveGrantRemainingData> rervLeaveData = rervLeaveDataList.get(c.getRecordId());
			c.setResvLeaRemainNumber(annLeaDomainService.calculateRervLeaveNumber(rervLeaveData));
			
			result.add(new GridPeregDomainDto(c.getRecordId(), mapSids.get(c.getRecordId()), c));
		});
		
		
		
		return result;
	}
	
	public void setEmployeeId(List<String> sids, List<AnnualLeaveDto> resultDto) {
		sids.stream().forEach(c -> {
			resultDto.add(new AnnualLeaveDto(c));			
		});
	}
}
