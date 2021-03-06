package nts.uk.ctx.at.request.app.command.application.appabsence;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.request.dom.application.appabsence.HolidayAppType;
import nts.uk.ctx.at.request.dom.application.appabsence.VacationRequestInfo;

/**
 * @author anhnm
 * 休暇申請画面描画情報
 *
 */
@Data
@AllArgsConstructor
public class VacationRequestInfoDto {

    // 休暇申請の種類
    private int holidayApplicationType;
    
    // 休暇申請の種類
    private SupplementInfoVacationDto info;
    
    public VacationRequestInfo toDomain() {
        return new VacationRequestInfo(
                EnumAdaptor.valueOf(holidayApplicationType, HolidayAppType.class), 
                Optional.ofNullable(info).map(SupplementInfoVacationDto::toDomain).orElse(null));
    }
    
    public static VacationRequestInfoDto fromDomain(VacationRequestInfo domain) {
        return new VacationRequestInfoDto(
                domain.getHolidayApplicationType().value, 
                Optional.ofNullable(domain.getInfo())
                	.map(x -> SupplementInfoVacationDto.fromDomain(x))
                	.orElse(null));
    }
}
