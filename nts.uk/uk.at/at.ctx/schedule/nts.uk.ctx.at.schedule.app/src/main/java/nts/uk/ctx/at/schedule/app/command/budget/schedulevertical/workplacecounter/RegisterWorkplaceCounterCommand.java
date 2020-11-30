package nts.uk.ctx.at.schedule.app.command.budget.schedulevertical.workplacecounter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterWorkplaceCounterCommand {

    private List<Integer> workplaceCategory;
    
}