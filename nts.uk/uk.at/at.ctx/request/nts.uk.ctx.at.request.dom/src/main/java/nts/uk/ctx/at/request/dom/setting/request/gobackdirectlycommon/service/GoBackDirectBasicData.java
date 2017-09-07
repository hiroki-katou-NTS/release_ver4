package nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.service;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.setting.applicationformreason.ApplicationFormReason;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSetting;
/**
 * 直行直帰基本データ
 * @author ducpm
 *
 */
@Getter
@Setter
public class GoBackDirectBasicData {
	Optional<GoBackDirectlyCommonSetting> goBackDirectSet;
	String employeeName;
	List<ApplicationFormReason> listAppReason;
}
