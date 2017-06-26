package nts.uk.ctx.basic.dom.organization.payclassification;

import java.util.List;


public interface PayClassificationRepository {

	/**
	 * add pay classification
	 * @param payClassification
	 */
	
	void add(PayClassification payClassification);

	/**
	 * update pay classification
	 * @param payClassification
	 */
	
	void update(PayClassification payClassification);

	/**
	 * check existed
	 * @param companyCode
	 * @param payClassificationCode
	 * @return
	 */
	
	boolean isExisted(String companyCode, String payClassificationCode);

	/**
	 * find all pay classification
	 * @param companyCode
	 * @return
	 */
	
	List<PayClassification> findAll(String companyCode);

	/**
	 * delete pay classification
	 * @param companyCode
	 * @param payClassificationCode
	 */
	
	void remove(String companyCode, String payClassificationCode);
}
