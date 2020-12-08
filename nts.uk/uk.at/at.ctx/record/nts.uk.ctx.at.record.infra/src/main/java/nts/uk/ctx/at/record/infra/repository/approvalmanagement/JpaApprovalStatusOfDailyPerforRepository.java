package nts.uk.ctx.at.record.infra.repository.approvalmanagement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalStatusOfDailyPerfor;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalStatusOfDailyPerforRepository;
import nts.uk.ctx.at.record.infra.entity.approvalmanagement.KrcdtDaiApprovalStatus;
import nts.uk.ctx.at.record.infra.entity.approvalmanagement.KrcdtDaiApprovalStatusPK;

@Stateless
public class JpaApprovalStatusOfDailyPerforRepository extends JpaRepository
		implements ApprovalStatusOfDailyPerforRepository {

//	private static final String REMOVE_BY_EMPLOYEE;

	private static final String DEL_BY_LIST_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDaiApprovalStatus a ");
//		builderString.append("WHERE a.krcdtDaiApprovalPK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDaiApprovalPK.ymd = :ymd ");
//		REMOVE_BY_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiApprovalStatus a ");
		builderString.append("WHERE a.krcdtDaiApprovalPK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDaiApprovalPK.ymd IN :ymds ");
		DEL_BY_LIST_KEY = builderString.toString();
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAY_BOSS_CHECK Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + ymd + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE).setParameter("employeeId", employeeId)
//				.setParameter("ymd", ymd).executeUpdate();
//		this.getEntityManager().flush();
	}

	@Override
	public void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> ymds) {
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistEmployeeIds -> {
			CollectionUtil.split(ymds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistYmds -> {
				this.getEntityManager().createQuery(DEL_BY_LIST_KEY)
					.setParameter("employeeIds", sublistEmployeeIds)
					.setParameter("ymds", sublistYmds)
					.executeUpdate();
			});
		});
		this.getEntityManager().flush();
	}

	@Override
	public void insert(ApprovalStatusOfDailyPerfor approvalStatusOfDailyPerfor) {
		this.commandProxy().insert(KrcdtDaiApprovalStatus.toEntity(approvalStatusOfDailyPerfor));
		this.getEntityManager().flush();
	}

	@Override
	public Optional<ApprovalStatusOfDailyPerfor> find(String employeeId, GeneralDate ymd) {
		return this.queryProxy().find(new KrcdtDaiApprovalStatusPK(employeeId, ymd), KrcdtDaiApprovalStatus.class).map(c -> c.toDomain());
	}

}
