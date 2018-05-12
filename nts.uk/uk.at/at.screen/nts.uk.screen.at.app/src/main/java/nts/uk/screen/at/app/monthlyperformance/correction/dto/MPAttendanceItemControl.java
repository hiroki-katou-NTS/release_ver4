/**
 * 5:45:13 PM Aug 23, 2017
 */
package nts.uk.screen.at.app.monthlyperformance.correction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hungnm
 *
 */
@Data
@AllArgsConstructor
public class MPAttendanceItemControl {

	private Integer attendanceItemId;

	// 時間項目の入力単位
	// 0:1分
	// 1:5分
	// 2:10分
	// 3:15分
	// 4:30分
	// 5:60分
	private Integer timeInputUnit;

	private String headerBackgroundColor;

	private Integer lineBreakPosition;
}