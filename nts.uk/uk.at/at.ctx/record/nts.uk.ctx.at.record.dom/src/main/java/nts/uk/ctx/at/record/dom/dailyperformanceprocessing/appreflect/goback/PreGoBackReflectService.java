package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

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
	public void gobackReflect(GobackReflectParameter para);
	/**
	 * 事後申請の処理
	 * @param para
	 * @return
	 */
	public void afterGobackReflect(GobackReflectParameter para);

}
