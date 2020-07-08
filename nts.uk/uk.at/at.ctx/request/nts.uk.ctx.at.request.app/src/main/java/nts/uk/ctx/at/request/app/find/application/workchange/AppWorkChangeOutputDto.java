package nts.uk.ctx.at.request.app.find.application.workchange;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.app.find.application.workchange.dto.AppWorkChangeDispInfoDto;
import nts.uk.ctx.at.request.dom.application.workchange.output.AppWorkChangeOutput;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppWorkChangeOutputDto {
	// 勤務変更申請の表示情報
	private AppWorkChangeDispInfoDto appWorkChangeDispInfo;
	// 勤務変更申請＜Optional＞
	private AppWorkChangeDto appWorkChange;

	public static AppWorkChangeOutputDto fromDomain(AppWorkChangeOutput appWorkChangeOutput) {
		AppWorkChangeOutputDto appWorkChangeOutputDto = new AppWorkChangeOutputDto();
		appWorkChangeOutputDto.setAppWorkChangeDispInfo(
				AppWorkChangeDispInfoDto.fromDomain(appWorkChangeOutput.getAppWorkChangeDispInfo()));
		if (appWorkChangeOutput.getAppWorkChange().isPresent()) {
			appWorkChangeOutputDto
					.setAppWorkChange(AppWorkChangeDto.fromDomain(appWorkChangeOutput.getAppWorkChange().get()));
		}
		return appWorkChangeOutputDto;
	}
	public AppWorkChangeOutput toDomain() {
		AppWorkChangeOutput appWorkChangeOutput = new AppWorkChangeOutput();
		appWorkChangeOutput.setAppWorkChangeDispInfo(appWorkChangeDispInfo.toDomain());
		appWorkChangeOutput.setAppWorkChange(Optional.ofNullable(appWorkChange.toDomain()));
		return appWorkChangeOutput;
	}
}
