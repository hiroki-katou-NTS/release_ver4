package nts.uk.ctx.basic.dom.organization.classification;

import java.util.List;
import java.util.Optional;

public interface ClassificationRepository {
	/** Add Classification
	 * @param classification
	 */
	void add(Classification classification);

	/** Update Classification
	 * @param classification
	 */
	void update(Classification classification);

	/** Remove Classification by companyCode, classificationCode
	 * @param companyCode
	 * @param classificationCode
	 */
	void remove(String companyCode, ClassificationCode classificationCode);

	/** find Single Classification by companyCode,classificationCode
	 * @param companyCode
	 * @param classificationCode
	 * @return
	 */
	Optional<Classification> findSingleClassification(String companyCode, ClassificationCode classificationCode);

	/**
	 * get All  Classification by companyCode
	 * @param companyCode
	 * @return companyCode
	 */
	List<Classification> findAll(String companyCode);

	/** Check isExits  Classification by companyCode,classificationCode
	 * @param companyCode
	 * @param classificationCode
	 * @return
	 */
	boolean isExisted(String companyCode, ClassificationCode classificationCode);

}
