package nts.uk.ctx.at.aggregation.app.find.schedulecounter.wkpcounter;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumConstant;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.WorkplaceCounter;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.WorkplaceCounterCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkplaceCounterCategoryDto {

    private int value;
    private boolean use;
    private Boolean setting;

    public static List<WorkplaceCounterCategoryDto> setData(List<EnumConstant> listEnum, Optional<WorkplaceCounter> workplaceCounter,
                                                            Boolean settingCostAndTime,Boolean settingTimesNumber,Boolean settingTimeZone) {
        return listEnum.stream().map(x -> new WorkplaceCounterCategoryDto(
            x.getValue(),
            workplaceCounter.isPresent() && workplaceCounter.get().isUsed(WorkplaceCounterCategory.of(x.getValue())),
            x.getValue() == 0 ? settingCostAndTime : x.getValue() == 2 ? settingTimesNumber : x.getValue() == 4 ? settingTimeZone : null
        )).sorted(Comparator.comparing(WorkplaceCounterCategoryDto::getValue)).collect(Collectors.toList());
    }
}
