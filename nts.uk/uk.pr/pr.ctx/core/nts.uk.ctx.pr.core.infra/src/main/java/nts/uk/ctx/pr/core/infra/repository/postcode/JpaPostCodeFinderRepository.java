/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.postcode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.app.postcode.find.PostCode;
import nts.uk.ctx.pr.core.app.postcode.find.PostCodeFinder;
import nts.uk.ctx.pr.core.infra.entity.postcode.CpcstPostcode;
import nts.uk.ctx.pr.core.infra.entity.postcode.CpcstPostcode_;

/**
 * The Class JpaAddressFinderRepository.
 */

@Stateless
public class JpaPostCodeFinderRepository extends JpaRepository implements PostCodeFinder {

	/** The Constant MAX_RESULT. */
	public static final int MAX_RESULT = 100;

	/** The Constant FIRST_RESULT. */
	public static final int FIRST_RESULT = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.app.address.find.AddressFinder#
	 * findAddressSelectionList(java.lang.String)
	 */
	@Override
	public List<PostCode> findPostCodeList(String zipCode) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call XXXXX_ADDRESS (XxxxxAddress SQL)
		CriteriaQuery<CpcstPostcode> cq = criteriaBuilder.createQuery(CpcstPostcode.class);

		// root data
		Root<CpcstPostcode> root = cq.from(CpcstPostcode.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// like zipcode
		lstpredicateWhere
				.add(criteriaBuilder.like(root.get(CpcstPostcode_.postcode), zipCode + "%"));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<CpcstPostcode> query = em.createQuery(cq).setFirstResult(FIRST_RESULT)
				.setMaxResults(MAX_RESULT);

		// exclude select
		List<PostCode> lstPostCode = query.getResultList().stream().map(item -> toPostCode(item))
				.collect(Collectors.toList());

		return lstPostCode;
	}

	/**
	 * To post code.
	 *
	 * @param entity
	 *            the entity
	 * @return the post code
	 */
	private PostCode toPostCode(CpcstPostcode entity) {
		PostCode postCode = new PostCode();
		postCode.setId(entity.getId());
		postCode.setLocalGovCode(entity.getLocalGovCode());
		postCode.setMunicipalityName(entity.getMunicipalityName());
		postCode.setMunicipalityNameKn(entity.getMunicipalityNameKn());
		postCode.setPostcode(entity.getPostcode());
		postCode.setPrefectureName(entity.getPrefectureName());
		postCode.setPrefectureNameKn(entity.getPrefectureNameKn());
		postCode.setTownName(entity.getTownName());
		postCode.setTownNameKn(entity.getTownNameKn());
		return postCode;
	}

}
