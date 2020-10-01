package nts.uk.screen.at.app.kmk.kmk008.unitsetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.enums.UseClassificationAtr;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.setting.AgreementUnitSetting;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementUnitSettingDto {

    /** 分類使用区分 */
    private UseClassificationAtr classificationUseAtr;

    /** 雇用使用区分 */
    private UseClassificationAtr employmentUseAtr;

    /** 職場使用区分 */
    private UseClassificationAtr workPlaceUseAtr;

    public static AgreementUnitSettingDto setData(Optional<AgreementUnitSetting> data){

        return data.map(setting -> new AgreementUnitSettingDto(
                setting.getClassificationUseAtr(),
                setting.getEmploymentUseAtr(),
                setting.getWorkPlaceUseAtr()
        )).orElseGet(AgreementUnitSettingDto::new);
    }
}
