package nts.uk.ctx.at.aggregation.app.find.schedulecounter.timesnumbercounter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectNoListDto {

    private List<Integer> selectedNoList =  new ArrayList<>();
}
