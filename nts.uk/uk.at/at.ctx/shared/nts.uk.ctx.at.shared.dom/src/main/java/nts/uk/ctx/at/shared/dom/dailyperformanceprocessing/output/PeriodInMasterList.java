package nts.uk.ctx.at.shared.dom.dailyperformanceprocessing.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 期間内マスタ一覧
 * @author nampt
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodInMasterList {

	private String employeeId;
	
	/** マスタ一覧 **/
	private List<MasterList> masterLists; 
}
