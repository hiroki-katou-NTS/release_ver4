package nts.uk.ctx.pr.core.infra.repository.rule.employment.layout;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategoryRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.layout.QstmtStmtLayoutCtg;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.layout.QstmtStmtLayoutCtgPK;

@Stateless

public class JpaLayoutCategoryRepository extends JpaRepository implements LayoutMasterCategoryRepository {

	private final String SELECT_NO_WHERE = "SELECT c FROM QstmtStmtLayoutCtg c";
	private final String SELECT_ALL_DETAILS = SELECT_NO_WHERE 
			+ " WHERE c.qstmtStmtLayoutCtgPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutCtgPk.stmtCd = :stmtCd" 
			+ " AND c.strYm = :startYm";
	private final String SELECT_ALL_BY_HISTORY_ID = SELECT_NO_WHERE 
			+ " WHERE c.qstmtStmtLayoutCtgPk.historyId = :historyId";
	private final String SELECT_ALL_DETAILS_BEFORE = SELECT_NO_WHERE 
			+ " WHERE c.qstmtStmtLayoutCtgPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutCtgPk.stmtCd = :stmtCd" 
			+ " AND c.endYm = :endYm";
	private final String SELECT_ALL_DETAILS_BEFORE1 = SELECT_NO_WHERE 
			+ " WHERE c.qstmtStmtLayoutCtgPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutCtgPk.stmtCd = :stmtCd" 
			+ " AND c.qstmtStmtLayoutCtgPk.historyId = :historyId";
	private final String SELECT_ALL_DETAILS1= SELECT_NO_WHERE 
			+ " WHERE c.qstmtStmtLayoutCtgPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutCtgPk.stmtCd = :stmtCd" ;

	private static LayoutMasterCategory toDomain(QstmtStmtLayoutCtg entity) {
		LayoutMasterCategory domain = LayoutMasterCategory.createFromJavaType(entity.qstmtStmtLayoutCtgPk.companyCd,
				 entity.qstmtStmtLayoutCtgPk.stmtCd,
				entity.qstmtStmtLayoutCtgPk.ctgAtr,  entity.ctgPos, entity.qstmtStmtLayoutCtgPk.historyId);

		return domain;
	}

	private QstmtStmtLayoutCtg toEntity(LayoutMasterCategory domain) {
		val entity = new QstmtStmtLayoutCtg();

		entity.qstmtStmtLayoutCtgPk = new QstmtStmtLayoutCtgPK();
		entity.qstmtStmtLayoutCtgPk.companyCd = domain.getCompanyCode().v();
		entity.qstmtStmtLayoutCtgPk.stmtCd = domain.getStmtCode().v();
		entity.qstmtStmtLayoutCtgPk.ctgAtr = domain.getCtAtr().value;
		entity.ctgPos = domain.getCtgPos().v();
		entity.qstmtStmtLayoutCtgPk.historyId = domain.getHistoryId();

		return entity;
	}

	@Override
	public void add(LayoutMasterCategory layoutMasterCategory) {
		this.commandProxy().insert(toEntity(layoutMasterCategory));

	}

	@Override
	public void update(LayoutMasterCategory layoutMasterCategory) {
		this.commandProxy().update(toEntity(layoutMasterCategory));

	}

	@Override
	public void remove(String companyCode, String stmtCode, String historyId, int categoryAtr) {
		val objectKey = new QstmtStmtLayoutCtgPK();
		objectKey.companyCd = companyCode;
		objectKey.stmtCd = stmtCode;
		objectKey.historyId = historyId;
		objectKey.ctgAtr = categoryAtr;
		this.commandProxy().remove(QstmtStmtLayoutCtg.class, objectKey);
	}

	@Override
	public List<LayoutMasterCategory> getCategories(String companyCd, String stmtCd, int startYm) {
		try {
			return this.queryProxy().query(SELECT_ALL_DETAILS, QstmtStmtLayoutCtg.class)
					.setParameter("companyCd", companyCd)
					.setParameter("stmtCd", stmtCd)
					.setParameter("startYm", startYm)
					.getList(c -> toDomain(c));
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		
	}

	@Override
	public void add(List<LayoutMasterCategory> categories) {
		for (LayoutMasterCategory category : categories) {
			this.commandProxy().insert(toEntity(category));	
			System.out.println(category);
		}		
	}

	@Override
	public void update(List<LayoutMasterCategory> categories) {
		for (LayoutMasterCategory category : categories) {
			this.commandProxy().update(toEntity(category));
		}
		
	}

	@Override
	public List<LayoutMasterCategory> getCategoriesBefore(String companyCode, String stmtCode, int endYm) {
		try {
			List<LayoutMasterCategory> lstCategory = this.queryProxy().query(SELECT_ALL_DETAILS_BEFORE, QstmtStmtLayoutCtg.class)
				.setParameter("companyCd", companyCode)
				.setParameter("stmtCd", stmtCode)
				.setParameter("endYm", endYm)
				.getList(c -> toDomain(c));
		return lstCategory;
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public void removeAllCategory(List<LayoutMasterCategory> categories) {
		for (LayoutMasterCategory category : categories) {
			val objectKey = new QstmtStmtLayoutCtgPK();
			objectKey.companyCd = category.getCompanyCode().v();
			objectKey.stmtCd = category.getStmtCode().v();
			objectKey.historyId = category.getHistoryId();
			objectKey.ctgAtr = category.getCtAtr().value;
			this.commandProxy().remove(QstmtStmtLayoutCtg.class, objectKey);
		}
	}

	@Override
	public Optional<LayoutMasterCategory> find(String companyCode, String stmtCode, String historyId, int categoryAtr) {
		QstmtStmtLayoutCtgPK primaryKey = new QstmtStmtLayoutCtgPK(companyCode, stmtCode, historyId, categoryAtr);
		
		return this.queryProxy().find(primaryKey, QstmtStmtLayoutCtg.class).map(x -> toDomain(x));
	}

	@Override
	public List<LayoutMasterCategory> getCategories(String historyId) {
		return this.queryProxy().query(SELECT_ALL_BY_HISTORY_ID, QstmtStmtLayoutCtg.class)
				.setParameter("historyId", historyId)
				.getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterCategory> getCategories(String companyCode, String stmtCode, String historyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LayoutMasterCategory> getCategoriesBefore(String companyCode, String stmtCode, String historyId) {
		try {
			List<LayoutMasterCategory> lstCategory = this.queryProxy().query(SELECT_ALL_DETAILS_BEFORE1, QstmtStmtLayoutCtg.class)
				.setParameter("companyCd", companyCode)
				.setParameter("stmtCd", stmtCode)
				.setParameter("historyId", historyId)
				.getList(c -> toDomain(c));
		return lstCategory;
		} catch (Exception e) {
			 e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<LayoutMasterCategory> getCategoriesBefore(String companyCode, String stmtCode) {
		return this.queryProxy().query(SELECT_ALL_DETAILS1, QstmtStmtLayoutCtg.class)
		.setParameter("companyCd", companyCode)
		.setParameter("stmtCd", stmtCode)
		.getList(c -> toDomain(c));
	}
}