package nts.uk.ctx.at.record.dom.remainingnumber.param;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;

/**
 * 休暇残数シフトリストWORK
 * @author masaaki_jinno
 */
public class RemNumShiftListWork {

	/**
	 * 休暇残数シフトのリスト
	 */
	private Optional<List<RemNumShiftWork>> remNumShiftWorkListOpt;
	
	/**
	 * 合計残数を取得する
	 * @return
	 */
	public void GetTotalRemNum(){
		
		List<RemNumShiftWork> list = remNumShiftWorkListOpt.get();
		

	}
	
	
}
