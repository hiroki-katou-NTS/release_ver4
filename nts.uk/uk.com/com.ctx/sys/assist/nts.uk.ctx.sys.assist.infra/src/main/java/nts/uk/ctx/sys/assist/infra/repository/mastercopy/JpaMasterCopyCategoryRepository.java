package nts.uk.ctx.sys.assist.infra.repository.mastercopy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.assist.dom.mastercopy.MasterCopyCategory;
import nts.uk.ctx.sys.assist.dom.mastercopy.MasterCopyCategoryGetMemento;
import nts.uk.ctx.sys.assist.dom.mastercopy.MasterCopyCategoryRepository;
import nts.uk.ctx.sys.assist.infra.entity.mastercopy.SspctMastercopyCategory;

/**
 * The Class JpaMasterCopyCategoryRepository.
 */
@Stateless
public class JpaMasterCopyCategoryRepository extends JpaRepository implements MasterCopyCategoryRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.assist.dom.mastercopy.MasterCopyCategoryRepository#
	 * findAllMasterCopyCategory(java.lang.String)
	 */
	@Override
	public List<MasterCopyCategory> findAllMasterCopyCategory() {

		EntityManager em = this.getEntityManager();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<SspctMastercopyCategory> cq = criteriaBuilder.createQuery(SspctMastercopyCategory.class);
		Root<SspctMastercopyCategory> root = cq.from(SspctMastercopyCategory.class);

		// Build query
		cq.select(root);

		// query data
		List<SspctMastercopyCategory> sspmtMastercopyCategories = em.createQuery(cq).getResultList();

		// return
		if (sspmtMastercopyCategories != null)
			return sspmtMastercopyCategories.stream().map(e -> this.toDomain(e)).collect(Collectors.toList());
		return new ArrayList<MasterCopyCategory>();

	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.assist.dom.mastercopy.MasterCopyCategoryRepository#findByMasterCopyId(java.lang.String)
	 */
	@Override
	public Optional<MasterCopyCategory> findByMasterCopyId(String masterCopyId){
		return this.queryProxy().find(masterCopyId, SspctMastercopyCategory.class).map(item -> this.toDomain(item));
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the master copy category
	 */
	private MasterCopyCategory toDomain(SspctMastercopyCategory entity) {
		MasterCopyCategoryGetMemento memento = new JpaMasterCopyCategoryGetMemento(entity);
		return new MasterCopyCategory(memento);
	}

}
