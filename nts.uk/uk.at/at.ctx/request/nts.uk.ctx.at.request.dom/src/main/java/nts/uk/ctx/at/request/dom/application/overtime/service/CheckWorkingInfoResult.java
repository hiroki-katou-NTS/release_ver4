package nts.uk.ctx.at.request.dom.application.overtime.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckWorkingInfoResult {
	// 勤務種類エラーFlg
	private boolean wkTypeError;
	// 就業時間帯エラーFlg
	private boolean wkTimeError;
}
