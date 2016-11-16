package nts.uk.ctx.core.infra.data.repository.layout;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.proto.dom.layout.LayoutMaster;
import nts.uk.ctx.pr.proto.dom.layout.category.LayoutMasterCategory;
import nts.uk.ctx.pr.proto.dom.layout.category.LayoutMasterCategoryRepository;
import nts.uk.ctx.pr.proto.dom.layout.detail.LayoutMasterDetail;
import nts.uk.ctx.pr.proto.infra.entity.layout.QstmtStmtLayoutCtg;
import nts.uk.ctx.pr.proto.infra.entity.layout.QstmtStmtLayoutDetail;
import nts.uk.ctx.pr.proto.infra.entity.layout.QstmtStmtLayoutHead;
@RequestScoped


public class JpaLayoutCategoryRepository extends JpaRepository implements LayoutMasterCategoryRepository{

	private static String SELECT_NO_WHERE = "SELECT c FROM QstmtStmtLayoutCtgPK c";
	private static String SELECT_ALL_DETAILS = SELECT_NO_WHERE 
			+ " WHERE c.qstmtStmtLayoutCtgPK.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutCtgPK.stmtCd = :stmtCd"
			+ " AND c.qstmtStmtLayoutCtgPK.strYm = :strYm"
			+ "AND c.qstmtStmtLayoutCtgPK.ctgAtr = :ctgAtr";
	
	private static LayoutMasterCategory toDomain(QstmtStmtLayoutCtg entity) {
		LayoutMasterCategory domain = LayoutMasterCategory.createFromJavaType(
				entity.qstmtStmtLayoutCtgPk.companyCd, 
				entity.qstmtStmtLayoutCtgPk.strYm, 
				entity.qstmtStmtLayoutCtgPk.stmtCd,
				entity.qstmtStmtLayoutCtgPk.ctgAtr,
				entity.endYm, 
				entity.ctgAtr, 
				entity.ctgPos);
		
		entity.toDomain(domain);
		return domain;
	}
	

	

	
	@Override
	public Optional<LayoutMasterCategory> find(String companyCode, String layoutCode, int startYm) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<LayoutMasterCategory> findAll(String companyCode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void add(String companyCode, String layoutCode, int startYm) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(String companyCode, String stmtCode, int startYm, int categoryAtr) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void remove(String companyCode, String stmtCode, int startYm, int categoryAtr) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<LayoutMasterCategory> getCategories(String companyCd, String stmtCd, int startYm) {
		// TODO Auto-generated method stub
		
	return this.queryProxy().query(SELECT_ALL_DETAILS, QstmtStmtLayoutCtg.class)
			.setParameter("companyCd", companyCd)
			.setParameter("stmtCd", stmtCd)
			.setParameter("startYM", startYm)
			.getList(c -> toDomain(c));
	}

		


}