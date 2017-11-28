package nts.uk.screen.at.app.dailyperformance.correction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualLockDto {
	/** The company id. */
	// 会社ID
	private String companyId;
	
	/** The closure id. */
	// 締めID
	private int closureId;
	
	/** The daily lock state. */
	// 日別のロック状態
	private int dailyLockState;
	
	/** The monthly lock state. */
	// 月別のロック状態
	private int monthlyLockState;
}
