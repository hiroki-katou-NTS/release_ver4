package nts.uk.ctx.at.function.infra.repository.dailyperformanceformat;

import java.math.BigDecimal;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.AuthorityFormatSheet;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.primitivevalue.DailyPerformanceFormatCode;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.repository.AuthorityFormatSheetRepository;
import nts.uk.ctx.at.function.infra.entity.dailyperformanceformat.KfnmtAuthorityFormSheet;
import nts.uk.ctx.at.function.infra.entity.dailyperformanceformat.KfnmtAuthorityFormSheetPK;

@Stateless
public class JpaAuthorityFormatSheetRepository extends JpaRepository implements AuthorityFormatSheetRepository {

	private static final String FIND;

	private static final String UPDATE_BY_KEY;

	private static final String DEL_BY_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KfnmtAuthorityFormSheet a ");
		builderString.append("WHERE a.kfnmtAuthorityFormSheetPK.companyId = :companyId ");
		builderString
				.append("AND a.kfnmtAuthorityFormSheetPK.dailyPerformanceFormatCode = :dailyPerformanceFormatCode ");
		builderString.append("AND a.kfnmtAuthorityFormSheetPK.sheetNo = :sheetNo ");
		FIND = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("UPDATE KfnmtAuthorityFormSheet a ");
		builderString.append("SET a.sheetName = :sheetName ");
		builderString.append("WHERE a.kfnmtAuthorityFormSheetPK.companyId = :companyId ");
		builderString
				.append("AND a.kfnmtAuthorityFormSheetPK.dailyPerformanceFormatCode = :dailyPerformanceFormatCode ");
		builderString.append("AND a.kfnmtAuthorityFormSheetPK.sheetNo = :sheetNo ");
		UPDATE_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KfnmtAuthorityFormSheet a ");
		builderString.append("WHERE a.kfnmtAuthorityFormSheetPK.companyId = :companyId ");
		builderString
				.append("AND a.kfnmtAuthorityFormSheetPK.dailyPerformanceFormatCode = :dailyPerformanceFormatCode ");
		builderString.append("AND a.kfnmtAuthorityFormSheetPK.sheetNo = :sheetNo ");
		DEL_BY_KEY = builderString.toString();
	}

	@Override
	public Optional<AuthorityFormatSheet> find(String companyId, DailyPerformanceFormatCode dailyPerformanceFormatCode,
			BigDecimal sheetNo) {
		return this.queryProxy().query(FIND, KfnmtAuthorityFormSheet.class).setParameter("companyId", companyId)
				.setParameter("dailyPerformanceFormatCode", dailyPerformanceFormatCode.v())
				.setParameter("sheetNo", sheetNo).getSingle(f -> toDomain(f));
	}

	@Override
	public void add(AuthorityFormatSheet authorityFormatSheet) {
		this.commandProxy().insert(toEntity(authorityFormatSheet));
	}

	@Override
	public void update(AuthorityFormatSheet authorityFormatSheet) {
		this.getEntityManager().createNamedQuery(UPDATE_BY_KEY)
				.setParameter("companyId", authorityFormatSheet.getCompanyId())
				.setParameter("dailyPerformanceFormatCode", authorityFormatSheet.getDailyPerformanceFormatCode().v())
				.setParameter("sheetNo", authorityFormatSheet.getSheetNo())
				.setParameter("sheetName", authorityFormatSheet.getSheetName()).executeUpdate();
	}

	@Override
	public void remove(String companyId, DailyPerformanceFormatCode dailyPerformanceFormatCode, BigDecimal sheetNo) {
		this.getEntityManager().createQuery(DEL_BY_KEY).setParameter("companyId", companyId)
				.setParameter("dailyPerformanceFormatCode", dailyPerformanceFormatCode.v())
				.setParameter("sheetNo", sheetNo).executeUpdate();
	}

	private static AuthorityFormatSheet toDomain(KfnmtAuthorityFormSheet kfnmtAuthorityFormSheet) {
		AuthorityFormatSheet authorityFormatSheet = AuthorityFormatSheet.createJavaTye(
				kfnmtAuthorityFormSheet.kfnmtAuthorityFormSheetPK.companyId,
				kfnmtAuthorityFormSheet.kfnmtAuthorityFormSheetPK.dailyPerformanceFormatCode,
				kfnmtAuthorityFormSheet.kfnmtAuthorityFormSheetPK.sheetNo, kfnmtAuthorityFormSheet.sheetName);
		return authorityFormatSheet;
	}

	private KfnmtAuthorityFormSheet toEntity(AuthorityFormatSheet authorityFormatSheet) {
		val entity = new KfnmtAuthorityFormSheet();

		entity.kfnmtAuthorityFormSheetPK = new KfnmtAuthorityFormSheetPK();
		entity.kfnmtAuthorityFormSheetPK.companyId = authorityFormatSheet.getCompanyId();
		entity.kfnmtAuthorityFormSheetPK.dailyPerformanceFormatCode = authorityFormatSheet
				.getDailyPerformanceFormatCode().v();
		entity.kfnmtAuthorityFormSheetPK.sheetNo = authorityFormatSheet.getSheetNo();
		entity.sheetName = authorityFormatSheet.getSheetName();

		return entity;
	}
}
