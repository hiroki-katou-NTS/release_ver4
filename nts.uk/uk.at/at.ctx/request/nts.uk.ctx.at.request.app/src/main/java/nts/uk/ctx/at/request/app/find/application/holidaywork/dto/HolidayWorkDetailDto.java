package nts.uk.ctx.at.request.app.find.application.holidaywork.dto;

import java.util.List;

import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.dom.application.common.ovetimeholiday.PreActualColorResult;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.HolidayWorkDetailOutput;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;

@NoArgsConstructor
public class HolidayWorkDetailDto {
	
	public AppHdWorkDispInfoDto appHdWorkDispInfoOutput;
	
	public AppHolidayWorkDto appHolidayWork;
	
	public List<CaculationTime> caculationTimes;
	
	public PreActualColorResult preActualColorResult;
	
	public static HolidayWorkDetailDto fromDomain(HolidayWorkDetailOutput holidayWorkDetailOutput) {
		HolidayWorkDetailDto result = new HolidayWorkDetailDto();
		result.appHdWorkDispInfoOutput = AppHdWorkDispInfoDto.fromDomain(holidayWorkDetailOutput.getAppHdWorkDispInfoOutput());
		result.appHolidayWork = AppHolidayWorkDto.fromDomain(holidayWorkDetailOutput.getAppHolidayWork());
		result.caculationTimes = holidayWorkDetailOutput.getCaculationTimes();
		result.preActualColorResult = holidayWorkDetailOutput.getPreActualColorResult();
		return result;
	}
	
}
