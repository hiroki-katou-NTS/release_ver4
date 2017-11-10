/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.company.infra.repository.company;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.company.dom.company.AddInfor;
import nts.uk.ctx.bs.company.dom.company.Company;
import nts.uk.ctx.bs.company.dom.company.CompanyInforNew;
import nts.uk.ctx.bs.company.dom.company.CompanyRepository;
import nts.uk.ctx.bs.company.infra.entity.company.BcmmtAddInfor;
import nts.uk.ctx.bs.company.infra.entity.company.BcmmtCompany;
import nts.uk.ctx.bs.company.infra.entity.company.BcmmtCompanyInfor;
import nts.uk.ctx.bs.company.infra.entity.company.BcmmtCompanyInforPK;

/**
 * The Class JpaCompanyRepository.
 */
@Stateless
public class JpaCompanyRepository extends JpaRepository implements CompanyRepository {

	private static final String GETALLCOMPANY;

	public final String SELECT_BY_CID = "SELECT c FROM BcmmtCompany c WHERE c.cid = :cid" + " AND c.abolitionAtr = 0 "
			+ " AND c.employmentSystem = 1 ";
	static {
		StringBuilder builderString = new StringBuilder();
		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM BcmmtCompany e");
		builderString.append(" WHERE e.abolitionAtr = 0 ");
		GETALLCOMPANY = builderString.toString();
	}
	/**
	 * Bcmmt Company Infor author: Hoang Yen
	 */
	private final String SELECT_NO_WHERE = "SELECT c FROM BcmmtCompanyInfor c ";
	private final String SELECT_COM = SELECT_NO_WHERE + "WHERE c.bcmmtCompanyInforPK.companyId = :companyId";
	private final String SELECT_COM_CD = SELECT_COM + " AND c.bcmmtCompanyInforPK.companyCode = :companyCode AND c.bcmmtCompanyInforPK.contractCd = :contractCd";
	// bcmmt add infor
	private final String SELECT_ADD_NO_WHERE = "SELECT  c FROM BcmmtAddInfor c ";
	private final String SELECT_ADD = SELECT_ADD_NO_WHERE + "WHERE c.bcmmtAddInforPK.companyId = :companyId";

	/**
	 * @param entity
	 * @return new Company(companyCode,companyName,companyId,isAboltiton)
	 */
	private static Company toSimpleDomain(BcmmtCompany entity) {
		Company domain = Company.createFromJavaType(entity.getCcd(), entity.getCompanyName(), entity.getCid(),
				entity.getAbolitionAtr(), entity.getPersonSystem(), entity.getEmploymentSystem(),
				entity.getPayrollSystem());

		return domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.basic.dom.company.CompanyRepository#getComanyId(java.lang.
	 * String)
	 */
	@Override
	public Optional<Company> getComanyById(String companyId) {
		return this.queryProxy().find(companyId, BcmmtCompany.class).map(company -> this.toDomain(company));
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the company
	 */
	private Company toDomain(BcmmtCompany entity) {
		return new Company(new JpaCompanyGetMemento(entity));
	}

	@Override
	public List<Company> getAllCompany() {
		return this.queryProxy().query(GETALLCOMPANY, BcmmtCompany.class).getList(c -> toSimpleDomain(c));
	}

	/**
	 * for RequestList 108
	 */
	@Override
	public Optional<Company> getComanyInfoByCid(String cid) {
		BcmmtCompany entity = this.queryProxy().query(SELECT_BY_CID, BcmmtCompany.class).setParameter("cid", cid)
				.getSingleOrNull();
		Company company = new Company();
		if (entity != null) {
			company = toSimpleDomain(entity);
		}
		return Optional.of(company);
	}

	/**
	 * convert from entity to domain
	 * 
	 * @param entity
	 * @return author Hoang Yen
	 */
	private static CompanyInforNew toDomainCom(BcmmtCompanyInfor entity) {
		AddInfor add = toDomainAdd(entity.bcmmtAddInfor);
		CompanyInforNew domain = CompanyInforNew.createFromJavaType(entity.bcmmtCompanyInforPK.companyCode,
				entity.companyName, entity.bcmmtCompanyInforPK.companyId, entity.startMonth, entity.isAbolition,
				entity.repname, entity.repost, entity.comNameKana, entity.shortComName,
				entity.bcmmtCompanyInforPK.contractCd, entity.taxNum, add);
		return domain;
	}

	/**
	 * convert to domain address
	 * 
	 * @param entity
	 * @return author Hoang Yen
	 */
	private static AddInfor toDomainAdd(BcmmtAddInfor entity) {
		AddInfor domain = AddInfor.createFromJavaType(entity.bcmmtAddInforPK.companyId,
				entity.bcmmtAddInforPK.companyCode, entity.bcmmtAddInforPK.contractCd, entity.faxNum, entity.add_1,
				entity.add_2, entity.addKana_1, entity.addKana_2, entity.postCd, entity.phoneNum);
		return domain;
	}

	/**
	 * change from company domain to company entity
	 * 
	 * @param domain
	 * @return author: Hoang Yen
	 */
	private static BcmmtCompanyInfor toEntityCom(CompanyInforNew domain) {
		val entity = new BcmmtCompanyInfor();
		entity.bcmmtCompanyInforPK = new BcmmtCompanyInforPK(domain.getCompanyId().v(), domain.getCompanyCode().v(),
				domain.getContractCd().v());
		entity.repname = domain.getRepname().v();
		entity.repost = domain.getRepost().v();
		entity.companyName = domain.getCompanyName().v();
		entity.comNameKana = domain.getComNameKana().v();
		entity.shortComName = domain.getShortComName().v();
		entity.isAbolition = domain.getIsAbolition().value;
		entity.startMonth = domain.getStartMonth().value;
		entity.taxNum = domain.getTaxNum();
		if (domain.getAddInfor() != null) {
			entity.bcmmtAddInfor = toEntityAdd(domain.getAddInfor());
		}
		return entity;
	}

	private static BcmmtAddInfor toEntityAdd(AddInfor domain) {
		val entity = new BcmmtAddInfor();
		// entity.bcmmtAddInforPK = new
		// BcmmtAddInforPK(domain.getCompanyId().toString());
		entity.faxNum = domain.getFaxNum().v();
		entity.add_1 = domain.getAdd_1().v();
		entity.add_2 = domain.getAdd_2().v();
		entity.addKana_1 = domain.getAddKana_1().v();
		entity.addKana_2 = domain.getAddKana_2().v();
		entity.postCd = domain.getPostCd().v();
		entity.phoneNum = domain.getPhoneNum().v();
		return entity;
	}

	/**
	 * get all company in database 
	 * author: Hoang Yen
	 */
	@Override
	public List<CompanyInforNew> findAll(String contractCd) {
		return this.queryProxy().query(SELECT_COM, BcmmtCompanyInfor.class).setParameter("contractCd", contractCd)
				.getList(c -> toDomainCom(c));
	}

	/**
	 * find company information by code 
	 * author: Hoang Yen
	 */
	@Override
	public Optional<CompanyInforNew> findComByCode(String contractCd, String companyId, String companyCd) {
		return this.queryProxy().query(SELECT_COM_CD, BcmmtCompanyInfor.class)
								.setParameter("contractCd", contractCd)
								.setParameter("companyId", companyId)
								.setParameter("companyCd", companyCd)
								.getSingle(c->toDomainCom(c));
	}
	
	/**
	 * update a company author: Hoang Yen
	 */
	@Override
	public void updateCom(CompanyInforNew company) {
		BcmmtCompanyInfor entity = toEntityCom(company);
		BcmmtCompanyInfor oldEntity = this.queryProxy().find(entity.bcmmtCompanyInforPK, BcmmtCompanyInfor.class).get();
		oldEntity.repname = entity.repname;
		oldEntity.repost = entity.repost;
		oldEntity.companyName = entity.companyName;
		oldEntity.comNameKana = entity.comNameKana;
		oldEntity.shortComName = entity.shortComName;
		oldEntity.isAbolition = entity.isAbolition;
		oldEntity.startMonth = entity.startMonth;
		oldEntity.taxNum = entity.taxNum;
	}

	/**
	 * insert a company author: Hoang Yen
	 */
	@Override
	public void insertCom(CompanyInforNew company) {
		BcmmtCompanyInfor entity = toEntityCom(company);
		this.commandProxy().insert(entity);
	}

	/**
	 * delete a company item author: Hoang Yen
	 */
	@Override
	public void deleteCom(String companyId, String contractCd, String companyCode) {
		BcmmtCompanyInforPK bcmmtCompanyInforPK = new BcmmtCompanyInforPK(companyId, companyCode, contractCd);
		this.commandProxy().remove(BcmmtCompanyInfor.class, bcmmtCompanyInforPK);
	}

	/**
	 * find Address author: Hoang Yen
	 */
	@Override
	public List<AddInfor> findAdd() {
		return this.queryProxy().query(SELECT_ADD_NO_WHERE, BcmmtAddInfor.class).getList(c -> toDomainAdd(c));
	}

	/**
	 * update address item author: Hoang Yen
	 */
	@Override
	public void updateAdd(AddInfor addInfor) {
		BcmmtAddInfor entity = toEntityAdd(addInfor);
		BcmmtAddInfor oldEntity = this.queryProxy().find(entity.bcmmtAddInforPK, BcmmtAddInfor.class).get();
		oldEntity.faxNum = entity.faxNum;
		oldEntity.add_1 = entity.add_1;
		oldEntity.add_2 = entity.add_2;
		oldEntity.addKana_1 = entity.addKana_1;
		oldEntity.addKana_2 = entity.addKana_2;
		oldEntity.postCd = entity.postCd;
		oldEntity.phoneNum = entity.phoneNum;
	}

	/**
	 * insert address item author: Hoang Yen
	 */
	@Override
	public void insertAdd(AddInfor addInfor) {
		BcmmtAddInfor entity = toEntityAdd(addInfor);
		this.commandProxy().insert(entity);
	}
}
