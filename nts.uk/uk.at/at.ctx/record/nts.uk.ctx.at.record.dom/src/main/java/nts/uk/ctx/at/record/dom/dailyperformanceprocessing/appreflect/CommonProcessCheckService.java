package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect;

import java.util.List;

/**
 * 反映状況によるチェック
 * @author do_dt
 *
 */
public interface CommonProcessCheckService {
	/**
	 * 反映状況によるチェック
	 * @param para
	 * @return false: 反映しない, true: 反映する
	 */
	public boolean commonProcessCheck(CommonCheckParameter para);	
	/**
	 * 予定勤務種類の項目ID
	 * @return
	 */
	public List<Integer> lstScheWorkItem();
	

}
