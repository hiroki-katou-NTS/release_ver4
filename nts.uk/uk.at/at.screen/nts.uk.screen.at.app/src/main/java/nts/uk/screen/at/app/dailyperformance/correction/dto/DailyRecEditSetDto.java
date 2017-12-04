package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecEditSetDto {

	private String employeeId;

	private GeneralDate processingYmd;

	private BigDecimal attendanceItemId;

	private BigDecimal editState;
}
