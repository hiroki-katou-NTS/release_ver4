package nts.uk.ctx.at.request.app.find.application.workchange;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.at.request.app.find.application.common.dto.AppCommonSettingDto;
import nts.uk.ctx.at.request.app.find.setting.applicationreason.ApplicationReasonDto;
import nts.uk.ctx.at.request.dom.setting.request.application.workchange.service.WorkChangeBasicData;

@AllArgsConstructor
@Value
public class AppWorkChangeCommonSetDto {
	/**
	 * 勤務変更申請設定
	 */
	AppWorkChangeSetDto workChangeSetDto;
	/**
	 * 社員.社員名
	 */
	String employeeName;
	/**
	 * 社員.社員ID
	 */
	String sID;
	/**
	 * 申請定型理由
	 */
	List<ApplicationReasonDto> listReasonDto;
	/**
	 * 申請共通設定
	 */
	AppCommonSettingDto appCommonSettingDto;

	public static AppWorkChangeCommonSetDto fromDomain(WorkChangeBasicData domain) {
		return new AppWorkChangeCommonSetDto(AppWorkChangeSetDto.fromDomain(domain.getWorkChangeCommonSetting().get()),
				domain.getEmployeeName(),
				domain.getSID(), domain.getListAppReason().stream().map(x -> ApplicationReasonDto.convertToDto(x))
						.collect(Collectors.toList()),
				AppCommonSettingDto.convertToDto(domain.getAppCommonSettingOutput()));
	}
}
