package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

/**
 * 申請反映処理: 直行直帰申請_事前申請の処理, 事後申請の処理
 * @author do_dt
 *
 */
public interface PreGoBackReflectService {
	/**
	 * 申請の処理
	 * @param isPre: True 事前, False 事後 
	 * @return
	 */
	public void gobackReflect(GobackReflectParameter para, boolean isPre);

}
