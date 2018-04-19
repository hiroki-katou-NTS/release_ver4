package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ApplicationReflectOutput;
/**
 * 申請反映処理: 直行直帰申請_事前申請の処理, 事後申請の処理
 * @author do_dt
 *
 */
public interface PreGoBackReflectService {
	/**
	 * 事前申請の処理
	 * @param para
	 * @return
	 */
	public boolean gobackReflect(GobackReflectParameter para);
	/**
	 * 事後申請の処理
	 * @param para
	 * @return
	 */
	public boolean afterGobackReflect(GobackReflectParameter para);

}
