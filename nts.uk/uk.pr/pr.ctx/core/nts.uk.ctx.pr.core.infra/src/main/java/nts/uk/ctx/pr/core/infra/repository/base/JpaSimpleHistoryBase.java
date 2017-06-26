/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.base;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import nts.arc.layer.infra.data.JpaRepository;

/**
 * The Class JpaSimpleHistoryBase.
 * Provide simple method to act with simple history. 
 */
public abstract class JpaSimpleHistoryBase extends JpaRepository {
	
	/**
	 * Delete by uuid.
	 * <br/>
	 * <b>THIS WILL EXECUTE CRITERIAL QUERY. SO IT WILL NOT EFFECTED BY RELATION IN JPA ENTITY</b>
	 * @param <E> the element type
	 * @param entityClass the entity class
	 * @param attributePath the attribute path
	 * @param uuid the uuid
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <E> void deleteByUuid(Class<E> entityClass, String uuid, SingularAttribute<?, ?>... attributePath) {
		// Get entity manager.
		EntityManager em = this.getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// create delete
		CriteriaDelete<E> delete = cb
				.createCriteriaDelete(entityClass);

		// set the root class
		Root<E> root = delete.from(entityClass);

		// set where clause
		Path path = null;
		for (SingularAttribute singularAttribute : attributePath) {
			if (path == null) {
				path = root.get(singularAttribute);
			} else {
				path = path.get(singularAttribute);
			}
		}
		delete.where(cb.equal(path, uuid));

		// perform update
		em.createQuery(delete).executeUpdate();
	}
}
