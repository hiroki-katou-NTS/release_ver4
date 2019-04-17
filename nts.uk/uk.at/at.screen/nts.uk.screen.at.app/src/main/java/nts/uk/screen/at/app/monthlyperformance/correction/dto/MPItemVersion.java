package nts.uk.screen.at.app.monthlyperformance.correction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MPItemVersion {

	private String rowId;

	private long version;

	private String employeeId;
}
