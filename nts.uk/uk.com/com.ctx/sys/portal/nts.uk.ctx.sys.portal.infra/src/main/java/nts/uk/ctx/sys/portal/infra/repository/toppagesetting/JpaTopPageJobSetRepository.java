package nts.uk.ctx.sys.portal.infra.repository.toppagesetting;

import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.portal.dom.enums.MenuClassification;
import nts.uk.ctx.sys.portal.dom.enums.System;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPageJobSet;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPageJobSetRepository;
import nts.uk.ctx.sys.portal.infra.entity.toppagesetting.CcgptTopPageJobSet;
import nts.uk.ctx.sys.portal.infra.entity.toppagesetting.CcgptTopPageJobSetPK;

/**
 * 
 * @author sonnh1
 *
 */
@Stateless
public class JpaTopPageJobSetRepository extends JpaRepository implements TopPageJobSetRepository {

	private final String SEL = "SELECT a FROM CcgptTopPageJobSet a ";
	private final String SEL_BY_LIST_JOB_ID = SEL
			+ "WHERE a.ccgptTopPageJobSetPK.companyId = :companyId AND a.ccgptTopPageJobSetPK.jobId IN :jobId";

	public static TopPageJobSet toDomain(CcgptTopPageJobSet entity) {
		TopPageJobSet domain = TopPageJobSet.createFromJavaType(entity.ccgptTopPageJobSetPK.companyId, entity.topMenuCd,
				entity.loginMenuCd, entity.ccgptTopPageJobSetPK.jobId, entity.personPermissionSet, entity.system,
				entity.loginMenuCls);
		return domain;
	}

	/**
	 * Convert to type of database
	 * 
	 * @param domain
	 * @return CcgptTopPageJobSet
	 */
	private CcgptTopPageJobSet toEntity(TopPageJobSet domain) {
		val entity = new CcgptTopPageJobSet();

		entity.ccgptTopPageJobSetPK = new CcgptTopPageJobSetPK();
		entity.ccgptTopPageJobSetPK.companyId = domain.getCompanyId();
		entity.ccgptTopPageJobSetPK.jobId = domain.getJobId();
		entity.loginMenuCd = domain.getLoginMenuCode().v();
		entity.topMenuCd = domain.getTopMenuCode().v();
		entity.personPermissionSet = domain.getPersonPermissionSet().value;
		entity.system = domain.getLoginSystem().value;
		entity.loginMenuCls = domain.getMenuClassification().value;
		return entity;
	}

	/**
	 * find TopPageJobSet by companyId and list JobId
	 */
	@Override
	public List<TopPageJobSet> findByListJobId(String companyId, List<String> jobId) {
		return this.queryProxy().query(SEL_BY_LIST_JOB_ID, CcgptTopPageJobSet.class)
				.setParameter("companyId", companyId).setParameter("jobId", jobId).getList(x -> toDomain(x));
	}

	/**
	 * Insert TopPageJobSet
	 */
	@Override
	public void add(TopPageJobSet topPageJobSet) {
		this.commandProxy().insert(toEntity(topPageJobSet));
	}
	
	/**
	 * Update TopPageJobSet
	 */
	@Override
	public void update(TopPageJobSet topPageJobSet) {
		CcgptTopPageJobSetPK pk = new CcgptTopPageJobSetPK(topPageJobSet.getCompanyId(), topPageJobSet.getJobId());
		CcgptTopPageJobSet entity = this.queryProxy().find(pk, CcgptTopPageJobSet.class).get();
		entity.ccgptTopPageJobSetPK = pk;
		entity.loginMenuCd = topPageJobSet.getLoginMenuCode().v();
		entity.loginMenuCls = topPageJobSet.getMenuClassification().value;
		entity.personPermissionSet = topPageJobSet.getPersonPermissionSet().value;
		entity.system = topPageJobSet.getLoginSystem().value;
		entity.topMenuCd = topPageJobSet.getTopMenuCode().v();
		this.commandProxy().update(entity);
	}

	/**
	 * Update property of TopPageJobSet: loginMenuCd = topMenuCd, 
	 * loginMenuCls = TopPage, system = Common
	 */
	@Override
	public void updateProperty(TopPageJobSet topPageJobSet) {
		CcgptTopPageJobSetPK pk = new CcgptTopPageJobSetPK(topPageJobSet.getCompanyId(), topPageJobSet.getJobId());
		CcgptTopPageJobSet entity = this.queryProxy().find(pk, CcgptTopPageJobSet.class).get();
		entity.ccgptTopPageJobSetPK = pk;
		entity.personPermissionSet = topPageJobSet.getPersonPermissionSet().value;
		entity.topMenuCd = topPageJobSet.getTopMenuCode().v();
		entity.loginMenuCd = topPageJobSet.getTopMenuCode().v();
		entity.loginMenuCls = MenuClassification.TopPage.value;
		entity.system = System.COMMON.value;
		this.commandProxy().update(entity);
	}
}
