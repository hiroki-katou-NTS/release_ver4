/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.env.infra.repository.mailnoticeset.company;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.env.dom.mailnoticeset.company.MailDestinationFunction;
import nts.uk.ctx.sys.env.dom.mailnoticeset.company.MailDestinationFunctionRepository;
import nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UserInfoItem;
import nts.uk.ctx.sys.env.infra.entity.mailnoticeset.company.SevstMailDestinFunc;
import nts.uk.ctx.sys.env.infra.entity.mailnoticeset.company.SevstMailDestinFuncPK;
import nts.uk.ctx.sys.env.infra.entity.mailnoticeset.company.SevstMailDestinFuncPK_;
import nts.uk.ctx.sys.env.infra.entity.mailnoticeset.company.SevstMailDestinFunc_;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class JpaMailDestinationFunctionRepository.
 */
@Stateless
public class JpaMailDestinationFunctionRepository extends JpaRepository implements MailDestinationFunctionRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.env.dom.mailnoticeset.company.
	 * MailDestinationFunctionRepository#findByCidAndSettingItem(java.lang.
	 * String, nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UserInfoItem)
	 */
	@Override
	public MailDestinationFunction findByCidAndSettingItem(String companyId, UserInfoItem userInfoItem) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<SevstMailDestinFunc> cq = criteriaBuilder.createQuery(SevstMailDestinFunc.class);
		Root<SevstMailDestinFunc> root = cq.from(SevstMailDestinFunc.class);

		// Build query
		cq.select(root);

		// Add where conditions
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(SevstMailDestinFunc_.sevstMailDestinFuncPK).get(SevstMailDestinFuncPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(SevstMailDestinFunc_.sevstMailDestinFuncPK).get(SevstMailDestinFuncPK_.settingItem),
				userInfoItem.value));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<SevstMailDestinFunc> listSevstMailDestinFunc = em.createQuery(cq).getResultList();

		// Check exist
		if (CollectionUtil.isEmpty(listSevstMailDestinFunc)) {
			return null;
		}

		// Return
		return new MailDestinationFunction(new JpaMailDestinationFunctionGetMemento(listSevstMailDestinFunc));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.env.dom.mailnoticeset.company.
	 * MailDestinationFunctionRepository#add(nts.uk.ctx.sys.env.dom.
	 * mailnoticeset.company.MailDestinationFunction)
	 */
	@Override
	public void add(MailDestinationFunction domain) {
		List<SevstMailDestinFunc> entities = new ArrayList<>();
		domain.saveToMemento(new JpaMailDestinationFunctionSetMemento(entities, domain.getCompanyId()));
		this.commandProxy().insertAll(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.env.dom.mailnoticeset.company.
	 * MailDestinationFunctionRepository#remove(java.lang.String,
	 * nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UserInfoItem)
	 */
	@Override
	public void remove(String companyId, UserInfoItem userInfoItem) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaDelete<SevstMailDestinFunc> cq = criteriaBuilder.createCriteriaDelete(SevstMailDestinFunc.class);
		Root<SevstMailDestinFunc> root = cq.from(SevstMailDestinFunc.class);

		// Add where conditions
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(SevstMailDestinFunc_.sevstMailDestinFuncPK).get(SevstMailDestinFuncPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(SevstMailDestinFunc_.sevstMailDestinFuncPK).get(SevstMailDestinFuncPK_.settingItem),
				userInfoItem.value));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		em.createQuery(cq).executeUpdate();
	}

	@Override
	@SneakyThrows
	public List<MailDestinationFunction> findByCidSettingItemAndUse(String cID, Integer functionID, NotUseAtr use) {

		List<SevstMailDestinFunc> listSevstMailDestinFunc;

		String sql = "select * from SEVST_MAIL_DESTIN_FUNC"
				+ " where CID = ?"
				+ " and FUNCTION_ID = ?"
				+ " and SEND_SET = ?";
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
			stmt.setString(1, cID);
			stmt.setInt(2, functionID);
			stmt.setInt(3, use.value);
			
			listSevstMailDestinFunc = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				SevstMailDestinFunc ent = new SevstMailDestinFunc();
				SevstMailDestinFuncPK pk = new SevstMailDestinFuncPK();
				pk.setCid(rec.getString("CID"));
				pk.setSettingItem(rec.getInt("SETTING_ITEM"));
				pk.setFunctionId(rec.getInt("FUNCTION_ID"));
				ent.setSevstMailDestinFuncPK(pk);
				ent.setSendSet(rec.getInt("SEND_SET"));
				return ent;
			});
		}
		
		// Check exist
		if (CollectionUtil.isEmpty(listSevstMailDestinFunc)) {
			return null;
		}
		List<Integer> keys = new ArrayList<Integer>();
		listSevstMailDestinFunc.forEach(x -> {
			if (!keys.contains(x.getSevstMailDestinFuncPK().getSettingItem())) {
				keys.add(x.getSevstMailDestinFuncPK().getSettingItem());
			}
		});
		List<MailDestinationFunction> result = new ArrayList<MailDestinationFunction>();
		keys.forEach(x -> {
			List<SevstMailDestinFunc> entity = listSevstMailDestinFunc.stream()
					.filter(item -> item.getSevstMailDestinFuncPK().getSettingItem().equals(x))
					.collect(Collectors.toList());
			result.add(new MailDestinationFunction(new JpaMailDestinationFunctionGetMemento(entity), use));
		});
		return result;

	}

}
