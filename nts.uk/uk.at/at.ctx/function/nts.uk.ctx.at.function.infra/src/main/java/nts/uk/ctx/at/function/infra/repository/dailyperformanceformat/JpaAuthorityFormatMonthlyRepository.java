package nts.uk.ctx.at.function.infra.repository.dailyperformanceformat;

import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.AuthorityFomatMonthly;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.primitivevalue.DailyPerformanceFormatCode;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.repository.AuthorityFormatMonthlyRepository;
import nts.uk.ctx.at.function.infra.entity.dailyperformanceformat.KfnmtAuthorityMonthlyItem;
import nts.uk.ctx.at.function.infra.entity.dailyperformanceformat.KfnmtAuthorityMonthlyItemPK;

@Stateless
public class JpaAuthorityFormatMonthlyRepository extends JpaRepository implements AuthorityFormatMonthlyRepository {

	private static final String FIND;

	private static final String UPDATE_BY_KEY;

	private static final String DEL_BY_KEY;
	
	private static final String REMOVE_EXIST_DATA;
	
	private static final String IS_EXIST_CODE;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KfnmtAuthorityMonthlyItem a ");
		builderString.append("WHERE a.kfnmtAuthorityMonthlyItemPK.companyId = :companyId ");
		builderString
				.append("AND a.kfnmtAuthorityMonthlyItemPK.dailyPerformanceFormatCode = :dailyPerformanceFormatCode ");
		FIND = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("UPDATE KfnmtAuthorityMonthlyItem a ");
		builderString.append(
				"SET a.displayOrder = :displayOrder , a.columnWidth = :columnWidth ");
		builderString.append("WHERE a.kfnmtAuthorityMonthlyItemPK.companyId = :companyId ");
		builderString
				.append("AND a.kfnmtAuthorityMonthlyItemPK.dailyPerformanceFormatCode = :dailyPerformanceFormatCode ");
		builderString.append("AND a.kfnmtAuthorityMonthlyItemPK.attendanceItemId = :attendanceItemId ");
		UPDATE_BY_KEY = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KfnmtAuthorityMonthlyItem a ");
		builderString.append("WHERE a.kfnmtAuthorityMonthlyItemPK.attendanceItemId IN :attendanceItemIds ");
		REMOVE_EXIST_DATA = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KfnmtAuthorityMonthlyItem a ");
		builderString.append("WHERE a.kfnmtAuthorityMonthlyItemPK.companyId = :companyId ");
		builderString
				.append("AND a.kfnmtAuthorityMonthlyItemPK.dailyPerformanceFormatCode = :dailyPerformanceFormatCode ");
		DEL_BY_KEY = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KfnmtAuthorityMonthlyItem a ");
		builderString
				.append("WHERE a.kfnmtAuthorityMonthlyItemPK.dailyPerformanceFormatCode = :dailyPerformanceFormatCode ");
		IS_EXIST_CODE = builderString.toString();
	}

	@Override
	public List<AuthorityFomatMonthly> getMonthlyDetail(String companyId, DailyPerformanceFormatCode dailyPerformanceFormatCode) {
		return this.queryProxy().query(FIND, KfnmtAuthorityMonthlyItem.class).setParameter("companyId", companyId)
				.setParameter("dailyPerformanceFormatCode", dailyPerformanceFormatCode.v()).getList(f -> toDomain(f));
	}

	@Override
	public void add(List<AuthorityFomatMonthly> authorityFomatMonthlies) {
		authorityFomatMonthlies.forEach(f -> this.commandProxy().insert(toEntity(f)));
	}

	@Override
	public void update(AuthorityFomatMonthly authorityFomatMonthly) {
		this.getEntityManager().createQuery(UPDATE_BY_KEY)
				.setParameter("companyId", authorityFomatMonthly.getCompanyId())
				.setParameter("dailyPerformanceFormatCode", authorityFomatMonthly.getDailyPerformanceFormatCode().v())
				.setParameter("attendanceItemId", authorityFomatMonthly.getAttendanceItemId())
				.setParameter("displayOrder", authorityFomatMonthly.getDisplayOrder())
				.setParameter("columnWidth", authorityFomatMonthly.getColumnWidth()).executeUpdate();
	}

	@Override
	public void deleteExistData(List<Integer> attendanceItemIds) {
		this.getEntityManager().createQuery(REMOVE_EXIST_DATA).setParameter("attendanceItemIds", attendanceItemIds).executeUpdate();
	}

	@Override
	public void remove(String companyId, DailyPerformanceFormatCode dailyPerformanceFormatCode) {
		this.getEntityManager().createQuery(DEL_BY_KEY).setParameter("companyId", companyId)
				.setParameter("dailyPerformanceFormatCode", dailyPerformanceFormatCode.v()).executeUpdate();
	}

	@Override
	public boolean checkExistCode(DailyPerformanceFormatCode dailyPerformanceFormatCode) {
		return this.queryProxy().query(IS_EXIST_CODE, long.class)
				.setParameter("dailyPerformanceFormatCode", dailyPerformanceFormatCode.v()).getSingle().get() > 0;
	}

	private static AuthorityFomatMonthly toDomain(KfnmtAuthorityMonthlyItem kfnmtAuthorityMonthlyItem) {
		AuthorityFomatMonthly authorityFomatMonthly = AuthorityFomatMonthly.createFromJavaType(
				kfnmtAuthorityMonthlyItem.kfnmtAuthorityMonthlyItemPK.companyId,
				kfnmtAuthorityMonthlyItem.kfnmtAuthorityMonthlyItemPK.dailyPerformanceFormatCode,
				kfnmtAuthorityMonthlyItem.kfnmtAuthorityMonthlyItemPK.attendanceItemId,
				kfnmtAuthorityMonthlyItem.displayOrder,
				kfnmtAuthorityMonthlyItem.columnWidth);
		return authorityFomatMonthly;
	}

	private KfnmtAuthorityMonthlyItem toEntity(AuthorityFomatMonthly authorityFomatMonthly) {
		val entity = new KfnmtAuthorityMonthlyItem();

		entity.kfnmtAuthorityMonthlyItemPK = new KfnmtAuthorityMonthlyItemPK();
		entity.kfnmtAuthorityMonthlyItemPK.companyId = authorityFomatMonthly.getCompanyId();
		entity.kfnmtAuthorityMonthlyItemPK.dailyPerformanceFormatCode = authorityFomatMonthly
				.getDailyPerformanceFormatCode().v();
		entity.kfnmtAuthorityMonthlyItemPK.attendanceItemId = authorityFomatMonthly.getAttendanceItemId();
		entity.columnWidth = authorityFomatMonthly.getColumnWidth();
		entity.displayOrder = authorityFomatMonthly.getDisplayOrder();

		return entity;
	}

}
