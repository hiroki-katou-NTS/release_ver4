package nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime;

import java.util.Optional;
/**
 * the App Overtime Setting interface
 * @author yennth
 *
 */
public interface AppOvertimeSettingRepository {
	/**
	 * get app over time setting by company id 
	 * @return
	 * @author yennth
	 */
	Optional<AppOvertimeSetting> getAppOver();
	/**
	 * update app over time setting
	 * @param appOverTime
	 * @author yennth
	 */
	void update(AppOvertimeSetting appOverTime);
	/**
	 * insert app over time setting
	 * @param appOverTime
	 * @author yennth
	 */
	void insert(AppOvertimeSetting appOverTime);
	Optional<AppOvertimeSetting> getByCid(String cid);}
