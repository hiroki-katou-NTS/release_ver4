package nts.uk.ctx.sys.portal.dom.toppagesetting;

import java.util.List;

/**
 * 
 * @author sonnh1
 *
 */
public interface TopPageJobSetRepository {
	/**
	 *  find by list jobId
	 * @param companyId
	 * @param jobId
	 * @return
	 */
	List<TopPageJobSet> findByListJobId(String companyId, List<String> jobId);

	/**
	 * Insert into table TOPPAGE_JOB_SET
	 * 
	 * @param topPageJobSet
	 */
	void add(TopPageJobSet topPageJobSet);

	/**
	 * update data in table TOPPAGE_JOB_SET
	 * 
	 * @param topPageJobSet
	 */
	void update(TopPageJobSet topPageJobSet);
}
