package nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.recruitmentapp;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.request.app.find.application.ApplicationDto;
import nts.uk.ctx.at.request.app.find.application.gobackdirectly.WorkInformationDto;
import nts.uk.ctx.at.request.dom.application.holidayshipment.TypeApplicationHolidays;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.shared.app.find.common.TimeZoneWithWorkNoDto;

/**
 * @author thanhpv
 *
 */
@Getter
@AllArgsConstructor
public class RecruitmentAppDto {
	
	public ApplicationDto application;
	
	public List<TimeZoneWithWorkNoDto> workingHours;
	
	public WorkInformationDto workInformation;
	
	public RecruitmentAppDto(RecruitmentApp domain) {
		this.application = ApplicationDto.fromDomain(domain);
		this.workingHours = domain.getWorkingHours().stream().map(c->TimeZoneWithWorkNoDto.fromDomain(c)).collect(Collectors.toList());
		this.workInformation = WorkInformationDto.fromDomain(domain.getWorkInformation());
	}

	public RecruitmentApp toDomain() {
		return new RecruitmentApp(
				workInformation.toDomain(),
				this.workingHours.stream().map(c-> c.toDomain()).collect(Collectors.toList()), 
				TypeApplicationHolidays.Abs,				 
				application.toDomain());
	}

}
