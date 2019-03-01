package nts.uk.ctx.at.shared.app.find.remainingnumber.empinfo.basicinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.SpecialLeaveCode;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.find.PeregEmpInfoQuery;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.PeregQueryByListEmp;
import nts.uk.shr.pereg.app.find.dto.GridPeregDomainDto;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Stateless
public class SpecialleaveInformationFinder {
	@Inject 
	private SpecialLeaveBasicInfoRepository specialLeaveBasicInfoRepository;
	
	@Inject
	private SpecialLeaveGrantRemainService specialLeaveGrantRemainService;
	
	public PeregDomainDto getSingleData(PeregQuery query, int specialLeaveCD) {
		Optional<SpecialLeaveBasicInfo> spLeaBasicInfo = specialLeaveBasicInfoRepository.getBySidLeaveCd(query.getEmployeeId(), specialLeaveCD);
		
		String dayTime = specialLeaveGrantRemainService.calDayTime(query.getEmployeeId(),specialLeaveCD);
		
		switch (EnumAdaptor.valueOf(specialLeaveCD, SpecialLeaveCode.class)) {
		case CS00025:
			Specialleave1InformationDto dto1 = new Specialleave1InformationDto();
			if (spLeaBasicInfo.isPresent()){
				dto1 = Specialleave1InformationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto1.setSpHDRemain(dayTime);
			return dto1;
		case CS00026:
			Specialleave2informationDto dto2 = new Specialleave2informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto2 = Specialleave2informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto2.setSpHDRemain(dayTime);
			return dto2;
		case CS00027:
			Specialleave3informationDto dto3 = new Specialleave3informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto3 = Specialleave3informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto3.setSpHDRemain(dayTime);
			return dto3;
		case CS00028:
			Specialleave4informationDto dto4 = new Specialleave4informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto4 = Specialleave4informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto4.setSpHDRemain(dayTime);
			return dto4;
		case CS00029:
			Specialleave5informationDto dto5 = new Specialleave5informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto5 = Specialleave5informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto5.setSpHDRemain(dayTime);
				
			return dto5;
		case CS00030:
			Specialleave6informationDto dto6 = new Specialleave6informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto6 = Specialleave6informationDto.createFromDomain(spLeaBasicInfo.get());
			}	
			dto6.setSpHDRemain(dayTime);
			return dto6;
		case CS00031:
			Specialleave7informationDto dto7 = new Specialleave7informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto7 = Specialleave7informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto7.setSpHDRemain(dayTime);
			return dto7;
		case CS00032:
			Specialleave8informationDto dto8 = new Specialleave8informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto8 = Specialleave8informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto8.setSpHDRemain(dayTime);
			return dto8;
		case CS00033:
			Specialleave9informationDto dto9 = new Specialleave9informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto9 = Specialleave9informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto9.setSpHDRemain(dayTime);
			return dto9;
		case CS00034:
			Specialleave10informationDto dto10 = new Specialleave10informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto10 = Specialleave10informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto10.setSpHDRemain(dayTime);
			return dto10;
		case CS00049:
			Specialleave11informationDto dto11 = new Specialleave11informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto11 = Specialleave11informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto11.setSpHDRemain(dayTime);
			return dto11;
		case CS00050:
			Specialleave12informationDto dto12 = new Specialleave12informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto12 = Specialleave12informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto12.setSpHDRemain(dayTime);
			return dto12;
		case CS00051:
			Specialleave13informationDto dto13 = new Specialleave13informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto13 = Specialleave13informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto13.setSpHDRemain(dayTime);
			return dto13;
		case CS00052:
			Specialleave14informationDto dto14 = new Specialleave14informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto14 = Specialleave14informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto14.setSpHDRemain(dayTime);
			return dto14;
		case CS00053:
			Specialleave15informationDto dto15 = new Specialleave15informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto15 = Specialleave15informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto15.setSpHDRemain(dayTime);
			return dto15;
		case CS00054:
			Specialleave16informationDto dto16 = new Specialleave16informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto16 = Specialleave16informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto16.setSpHDRemain(dayTime);
			return dto16;
		case CS00055:
			Specialleave17informationDto dto17 = new Specialleave17informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto17 = Specialleave17informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto17.setSpHDRemain(dayTime);
			return dto17;
		case CS00056:
			Specialleave18informationDto dto18 = new Specialleave18informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto18 = Specialleave18informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto18.setSpHDRemain(dayTime);
			return dto18;
		case CS00057:
			Specialleave19informationDto dto19 = new Specialleave19informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto19 = Specialleave19informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto19.setSpHDRemain(dayTime);
			return dto19;
		case CS00058:
			Specialleave20informationDto dto20 = new Specialleave20informationDto();
			if (spLeaBasicInfo.isPresent()){
				dto20 = Specialleave20informationDto.createFromDomain(spLeaBasicInfo.get());
			}
			dto20.setSpHDRemain(dayTime);
			return dto20;
		default:
			return null;
		}
		
	}
	
	public List<GridPeregDomainDto> getAllData(PeregQueryByListEmp query, int specialLeaveCD) {
		String cid = AppContexts.user().companyId();
		List<GridPeregDomainDto> result = new ArrayList<>();
		// key - sid , value -pid   getEmployeeId getPersonId
		Map<String, String> mapSids = query.getEmpInfos().stream()
				.collect(Collectors.toMap(PeregEmpInfoQuery::getEmployeeId, PeregEmpInfoQuery::getPersonId));
		
		Map<String, List<SpecialLeaveBasicInfo>> spLeaBasicInfo = specialLeaveBasicInfoRepository
				.getAllBySidsLeaveCd(cid, new ArrayList<String>(mapSids.keySet()), specialLeaveCD).stream()
				.collect(Collectors.groupingBy(c -> c.getSID()));
		
		Map<String, String> dayTimeMap = specialLeaveGrantRemainService.calDayTime(cid,
				new ArrayList<String>(spLeaBasicInfo.keySet()), specialLeaveCD);
		
		for (Map.Entry<String, List<SpecialLeaveBasicInfo>> entry : spLeaBasicInfo.entrySet()) {
			String key = entry.getKey();
			List<SpecialLeaveBasicInfo> value = entry.getValue();
			switch (EnumAdaptor.valueOf(specialLeaveCD, SpecialLeaveCode.class)) {
			case CS00025:
				Specialleave1InformationDto dto1 = new Specialleave1InformationDto();
				if (value.size() > 0){
					dto1 = Specialleave1InformationDto.createFromDomain(value.get(0));
				}
				dto1.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto1));
				break;
			case CS00026:
				Specialleave2informationDto dto2 = new Specialleave2informationDto();
				if (value.size() > 0){
					dto2 = Specialleave2informationDto.createFromDomain(value.get(0));
				}
				dto2.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto2));
				break;
			case CS00027:
				Specialleave3informationDto dto3 = new Specialleave3informationDto();
				if (value.size() > 0){
					dto3 = Specialleave3informationDto.createFromDomain(value.get(0));
				}
				dto3.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto3));
				break;
			case CS00028:
				Specialleave4informationDto dto4 = new Specialleave4informationDto();
				if (value.size() > 0){
					dto4 = Specialleave4informationDto.createFromDomain(value.get(0));
				}
				dto4.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto4));
				break;
			case CS00029:
				Specialleave5informationDto dto5 = new Specialleave5informationDto();
				if (value.size() > 0){
					dto5 = Specialleave5informationDto.createFromDomain(value.get(0));
				}
				dto5.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto5));
				break;	
			case CS00030:
				Specialleave6informationDto dto6 = new Specialleave6informationDto();
				if (value.size() > 0){
					dto6 = Specialleave6informationDto.createFromDomain(value.get(0));
				}	
				dto6.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto6));
				break;
			case CS00031:
				Specialleave7informationDto dto7 = new Specialleave7informationDto();
				if (value.size() > 0){
					dto7 = Specialleave7informationDto.createFromDomain(value.get(0));
				}
				dto7.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto7));
				break;
			case CS00032:
				Specialleave8informationDto dto8 = new Specialleave8informationDto();
				if (value.size() > 0){
					dto8 = Specialleave8informationDto.createFromDomain(value.get(0));
				}
				dto8.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto8));
				break;
			case CS00033:
				Specialleave9informationDto dto9 = new Specialleave9informationDto();
				if (value.size() > 0){
					dto9 = Specialleave9informationDto.createFromDomain(value.get(0));
				}
				dto9.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto9));
				break;
			case CS00034:
				Specialleave10informationDto dto10 = new Specialleave10informationDto();
				if (value.size() > 0){
					dto10 = Specialleave10informationDto.createFromDomain(value.get(0));
				}
				dto10.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto10));
				break;
			case CS00049:
				Specialleave11informationDto dto11 = new Specialleave11informationDto();
				if (value.size() > 0){
					dto11 = Specialleave11informationDto.createFromDomain(value.get(0));
				}
				dto11.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto11));
				break;
			case CS00050:
				Specialleave12informationDto dto12 = new Specialleave12informationDto();
				if (value.size() > 0){
					dto12 = Specialleave12informationDto.createFromDomain(value.get(0));
				}
				dto12.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto12));
				break;
			case CS00051:
				Specialleave13informationDto dto13 = new Specialleave13informationDto();
				if (value.size() > 0){
					dto13 = Specialleave13informationDto.createFromDomain(value.get(0));
				}
				dto13.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto13));
				break;
			case CS00052:
				Specialleave14informationDto dto14 = new Specialleave14informationDto();
				if (value.size() > 0){
					dto14 = Specialleave14informationDto.createFromDomain(value.get(0));
				}
				dto14.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto14));
				break;
			case CS00053:
				Specialleave15informationDto dto15 = new Specialleave15informationDto();
				if (value.size() > 0){
					dto15 = Specialleave15informationDto.createFromDomain(value.get(0));
				}
				dto15.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto15));
				break;
			case CS00054:
				Specialleave16informationDto dto16 = new Specialleave16informationDto();
				if (value.size() > 0){
					dto16 = Specialleave16informationDto.createFromDomain(value.get(0));
				}
				dto16.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto16));
				break;
			case CS00055:
				Specialleave17informationDto dto17 = new Specialleave17informationDto();
				if (value.size() > 0){
					dto17 = Specialleave17informationDto.createFromDomain(value.get(0));
				}
				dto17.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto17));
				break;
			case CS00056:
				Specialleave18informationDto dto18 = new Specialleave18informationDto();
				if (value.size() > 0){
					dto18 = Specialleave18informationDto.createFromDomain(value.get(0));
				}
				dto18.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto18));
				break;
			case CS00057:
				Specialleave19informationDto dto19 = new Specialleave19informationDto();
				if (value.size() > 0){
					dto19 = Specialleave19informationDto.createFromDomain(value.get(0));
				}
				dto19.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto19));
				break;
			case CS00058:
				Specialleave20informationDto dto20 = new Specialleave20informationDto();
				if (value.size() > 0){
					dto20 = Specialleave20informationDto.createFromDomain(value.get(0));
				}
				dto20.setSpHDRemain(dayTimeMap.get(key));
				result.add(new GridPeregDomainDto(key, mapSids.get(key), dto20));
				break;
			default:
				result.add(new GridPeregDomainDto(key, mapSids.get(key), null));
			}
		}

		return result;
		
	}
	
}
