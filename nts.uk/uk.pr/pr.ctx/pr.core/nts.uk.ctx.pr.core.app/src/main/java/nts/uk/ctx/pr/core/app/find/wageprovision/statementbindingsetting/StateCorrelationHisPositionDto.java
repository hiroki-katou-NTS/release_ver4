package nts.uk.ctx.pr.core.app.find.wageprovision.statementbindingsetting;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.pr.core.dom.wageprovision.statementbindingsetting.StateCorrelationHisPosition;
/**
* 明細書紐付け履歴（職位）: DTO
*/
@AllArgsConstructor
@Value
public class StateCorrelationHisPositionDto {

    
    /**
    * 履歴ID
    */
    private String hisId;
    
    /**
    * 開始年月
    */
    private Integer startYearMonth;
    
    /**
    * 終了年月
    */
    private Integer endYearMonth;
    
    
    public static List<StateCorrelationHisPositionDto> fromDomain(StateCorrelationHisPosition domain) {
        return domain.getHistory().stream().map(item -> {
            return new StateCorrelationHisPositionDto(item.identifier(), item.start().v(), item.end().v());
        }).collect(Collectors.toList());
    }
    
}
