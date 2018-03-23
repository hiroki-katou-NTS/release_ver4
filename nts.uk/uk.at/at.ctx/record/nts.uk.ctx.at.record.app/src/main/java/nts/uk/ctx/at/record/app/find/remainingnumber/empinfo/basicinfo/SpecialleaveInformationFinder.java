package nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfoRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainService;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRepository;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Stateless
public class SpecialleaveInformationFinder {
	@Inject 
	private SpecialLeaveBasicInfoRepository specialLeaveBasicInfoRepository;
	
	@Inject 
	private SpecialLeaveGrantRepository specialLeaveGrantRepo;
	
	@Inject
	private SpecialLeaveGrantRemainService specialLeaveGrantRemainService;
	
	public PeregDomainDto getSingleData(PeregQuery query, int specialLeaveCD) {
		// 社員ID　＝　社員ID　 and 特別休暇コード＝9
		Optional<SpecialLeaveBasicInfo> spLeaBasicInfo = specialLeaveBasicInfoRepository.getBySidLeaveCd(query.getEmployeeId(), specialLeaveCD);
		if (spLeaBasicInfo.isPresent()){
			SpecialleaveInformationDto dto = SpecialleaveInformationDto.createFromDomain(spLeaBasicInfo.get());
			List<SpecialLeaveGrantRemainingData> grantRemain = specialLeaveGrantRepo.getAllByExpStatus(query.getEmployeeId(), specialLeaveCD, true);
			dto.setSpHDRemain(specialLeaveGrantRemainService.calDayTime(grantRemain));
			return dto;
		}
		
		return null;
	}
}
