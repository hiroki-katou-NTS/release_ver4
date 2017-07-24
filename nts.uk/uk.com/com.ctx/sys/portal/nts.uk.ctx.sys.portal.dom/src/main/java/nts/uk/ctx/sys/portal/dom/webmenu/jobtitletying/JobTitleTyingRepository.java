package nts.uk.ctx.sys.portal.dom.webmenu.jobtitletying;

import java.util.List;

/**
 * @author yennth
 * The interface JobTitleTyingRepository
 */
public interface JobTitleTyingRepository {

	/**
	 * update menu code, if record doesn't exit in list, insert record to the list
	 * @author yennth
	 * @param JobTitleTying
	 */
	void updateMenuCode(List<JobTitleTying> JobTitleTying);
	/**
	 *insert menu code, if record doesn't exit in list, insert record to the list
	 * @author yennth
	 * @param JobTitleTying
	 */
	void insertMenuCode(List<JobTitleTying> JobTitleTying);
	/**
	 * find a web menu code list in the list
	 * @param companyId
	 * @param jobId
	 * @return
	 */
	List<JobTitleTying> findWebMenuCode(String companyId, List<String> jobId);
    
	/**
	 * Remove all job title by web menu code
	 * @param companyId
	 * @param webMenuCode
	 */
	void removeByMenuCode(String companyId, String webMenuCode);
}
