package nts.uk.ctx.at.record.app.find.remainingnumber.subhdmana.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.SWkpHistImport;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.service.ExtraHolidayManagementOutput;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.service.SubstituteManagementOutput;

@AllArgsConstructor
@Data
public class SubstituteDataManagementDto {
	public SWkpHistDto wkHistory;
	public ExtraHolidayManagementDataDto extraHolidayManagementDataDto;
	public String leaveSettingExpiredDate;
	public String compenSettingEmpExpiredDate;
	
	public static SubstituteDataManagementDto convertToDto(SubstituteManagementOutput subDataOutput){
		SWkpHistImport sWkpHistImport = subDataOutput.getSWkpHistImport();
		SWkpHistDto sWkpHist = null;
		String leaveSettingExpiredDate = "";
		String compenSettingEmpExpiredDate = "";
		if (!Objects.isNull(sWkpHistImport)){
			sWkpHist = SWkpHistDto.convertToDto(sWkpHistImport);
		}
		ExtraHolidayManagementOutput extraHolidayManagementOutput = subDataOutput.getExtraHolidayManagementOutput();
		if (!Objects.isNull(subDataOutput.getCompensatoryLeaveEmSetting())){
			leaveSettingExpiredDate = subDataOutput.getCompensatoryLeaveEmSetting().getCompensatoryAcquisitionUse().getExpirationTime().name();
		}
		if (!Objects.isNull(subDataOutput.getCompensatoryLeaveComSetting())){
			compenSettingEmpExpiredDate = subDataOutput.getCompensatoryLeaveComSetting().getCompensatoryAcquisitionUse().getExpirationTime().name();
		}
		return new SubstituteDataManagementDto(sWkpHist, ExtraHolidayManagementDataDto.convertToDto(extraHolidayManagementOutput), leaveSettingExpiredDate, compenSettingEmpExpiredDate);
	}
}
