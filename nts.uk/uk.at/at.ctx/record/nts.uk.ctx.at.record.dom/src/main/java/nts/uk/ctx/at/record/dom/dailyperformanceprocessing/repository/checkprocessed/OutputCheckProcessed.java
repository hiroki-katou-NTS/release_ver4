package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.checkprocessed;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.organization.EmploymentHistoryImported;

@Getter
@Setter
@NoArgsConstructor
public class OutputCheckProcessed {
    private StatusOutput statusOutput;

    private Optional<EmploymentHistoryImported> employmentHistoryImported;

    public OutputCheckProcessed(StatusOutput statusOutput, EmploymentHistoryImported employmentHistoryImported) {
        super();
        this.statusOutput = statusOutput;
        this.employmentHistoryImported = Optional.ofNullable(employmentHistoryImported);
    }
    
    
}