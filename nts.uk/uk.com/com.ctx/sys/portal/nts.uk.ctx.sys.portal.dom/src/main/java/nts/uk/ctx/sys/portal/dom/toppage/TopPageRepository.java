package nts.uk.ctx.sys.portal.dom.toppage;

import java.util.List;
import java.util.Optional;

public interface TopPageRepository {

	/**
	 * Find all.
	 *
	 * @param companyId the company id
	 * @param constractCode the constract code
	 * @return the list
	 */
	 List<TopPage> findAll(String companyId);

	/**
	 * Find by code.
	 *
	 * @param companyId the company id
	 * @param constractCode the constract code
	 * @param topPageCode the top page code
	 * @return the optional
	 */
 	Optional<TopPage> findByCode(String companyId,String topPageCode);
 	
 	/**
	  * Adds the.
	  *
	  * @param CompanyId the company id
	  * @param topPage the top page
	  */
	 void add(String CompanyId, TopPage topPage);
	 
 	/**
	  * Update.
	  *
	  * @param CompanyId the company id
	  * @param topPage the top page
	  */
 	void update(String CompanyId, TopPage topPage);
	 
 	/**
	  * Removes the.
	  *
	  * @param CompanyId the company id
	  * @param topPageCode the top page code
	  */
 	void remove(String CompanyId, String topPageCode);
}
