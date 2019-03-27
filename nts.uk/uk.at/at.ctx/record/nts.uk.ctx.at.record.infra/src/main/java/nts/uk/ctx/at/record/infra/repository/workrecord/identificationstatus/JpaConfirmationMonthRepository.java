package nts.uk.ctx.at.record.infra.repository.workrecord.identificationstatus;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.identificationstatus.month.KrcdtConfirmationMonth;
import nts.uk.ctx.at.record.infra.entity.workrecord.identificationstatus.month.KrcdtConfirmationMonthPK;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class JpaConfirmationMonthRepository  extends JpaRepository implements ConfirmationMonthRepository{
    
	private static final String DELETE_BY_PARENT_PK = "DELETE FROM KrcdtConfirmationMonth a "
			+ "WHERE a.krcdtConfirmationMonthPK.companyID = :companyID "
			+ "AND a.krcdtConfirmationMonthPK.employeeId = :employeeId "
			+ "AND a.krcdtConfirmationMonthPK.closureId = :closureId "
			+ "AND a.krcdtConfirmationMonthPK.closureDay = :closureDay "
			+ "AND a.krcdtConfirmationMonthPK.isLastDay = :isLastDayOfMonth "
			+ "AND a.krcdtConfirmationMonthPK.processYM = :processYM ";
	
	private static final String FIND_BY_SID_YM = "SELECT a FROM KrcdtConfirmationMonth a "
			+ "WHERE a.krcdtConfirmationMonthPK.companyID = :companyId "
			+ "AND a.krcdtConfirmationMonthPK.employeeId = :employeeId "
			+ "AND a.krcdtConfirmationMonthPK.processYM = :processYM ";
	
//	private static final String FIND_BY_SOME_PROPERTY = "SELECT a FROM KrcdtConfirmationMonth a "
//			+ "WHERE a.krcdtConfirmationMonthPK.employeeId IN :employeeIds "
//			+ "AND a.krcdtConfirmationMonthPK.processYM = :processYM "
//			+ "AND a.krcdtConfirmationMonthPK.closureDay = :closureDay "
//			+ "AND a.krcdtConfirmationMonthPK.isLastDay = :isLastDayOfMonth "
//			+ "AND a.krcdtConfirmationMonthPK.closureId = :closureId ";
	@Override
	public Optional<ConfirmationMonth> findByKey(String companyID, String employeeID, ClosureId closureId, ClosureDate closureDate, YearMonth processYM) {
		return this.queryProxy().find(new KrcdtConfirmationMonthPK(companyID, employeeID,
						closureId.value, closureDate.getClosureDay().v(),(closureDate.getLastDayOfMonth() ? 1 : 0), processYM.v()) , KrcdtConfirmationMonth.class).map(x -> x.toDomain());
	}

	@Override
	public void insert(ConfirmationMonth confirmationMonth) { 
		this.commandProxy().insert(new KrcdtConfirmationMonth(
				new KrcdtConfirmationMonthPK(confirmationMonth.getCompanyID().v(), confirmationMonth.getEmployeeId(),
						confirmationMonth.getClosureId().value, confirmationMonth.getClosureDate().getClosureDay().v(),(confirmationMonth.getClosureDate().getLastDayOfMonth() ? 1 : 0), confirmationMonth.getProcessYM().v()),
				confirmationMonth.getIndentifyYmd()));
	}

	@Override
	public void delete(String companyId, String employeeId, int closureId, int closureDate, boolean isLastDayOfMonth, int processYM) {
		this.getEntityManager().createQuery(DELETE_BY_PARENT_PK, KrcdtConfirmationMonth.class)
				.setParameter("companyID", companyId).setParameter("employeeId", employeeId)
				.setParameter("closureId", closureId)
				.setParameter("closureDay", closureDate)
				.setParameter("isLastDayOfMonth", isLastDayOfMonth ? 1 : 0)
				.setParameter("processYM", processYM).executeUpdate();
	}

	@Override
	public List<ConfirmationMonth> findBySidAndYM(String employeeId, int processYM) {
		return this.queryProxy().query(FIND_BY_SID_YM, KrcdtConfirmationMonth.class)
				.setParameter("companyId", AppContexts.user().companyId())
				.setParameter("employeeId", employeeId)
				.setParameter("processYM", processYM).getList(x -> x.toDomain());
	}

	@Override
	public List<ConfirmationMonth> findBySomeProperty(List<String> employeeIds, int processYM, int closureDate, boolean isLastDayOfMonth,
			int closureId) {
		List<ConfirmationMonth> data = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			try (PreparedStatement statement = this.connection().prepareStatement(
						"SELECT * from KRCDT_CONFIRMATION_MONTH h WHERE h.CLOSURE_DAY = ? AND h.IS_LAST_DAY = ? AND h.PROCESS_YM = ?"
						+ " AND h.CLOSURE_ID = ? AND h.SID IN (" + subList.stream().map(s -> "?").collect(Collectors.joining(",")) + ")")) {
				statement.setInt(1, closureDate);
				statement.setInt(2, isLastDayOfMonth ? 1 : 0);
				statement.setInt(3, processYM);
				statement.setInt(4, closureId);
				for (int i = 0; i < subList.size(); i++) {
					statement.setString(i + 5, subList.get(i));
				}
				ClosureDate cloDate = new ClosureDate(closureDate, isLastDayOfMonth);
				YearMonth ym = new YearMonth(processYM);
				ClosureId cloId = ClosureId.valueOf(closureId);
				data.addAll(new NtsResultSet(statement.executeQuery()).getList(rec -> {
					return new ConfirmationMonth(new CompanyId(rec.getString("CID")),
							rec.getString("SID"), cloId, cloDate, ym, 
							rec.getGeneralDate("IDENTIFY_DATE"));
				}));
			}catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		
		return data;
	}

	@Override
	public List<ConfirmationMonth> findBySomeProperty(List<String> employeeIds, List<YearMonth> yearMonth) {
		List<ConfirmationMonth> data = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			CollectionUtil.split(yearMonth, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, ym -> {
				data.addAll(internalQuery(subList, ym));
			});
		});
		
		return data;
	}

	@SneakyThrows
	private List<ConfirmationMonth> internalQuery(List<String> subList, List<YearMonth> ym) {
		String subEmp = NtsStatement.In.createParamsString(subList);
		String subYm = NtsStatement.In.createParamsString(ym);
		try (val stmt = this.connection().prepareStatement("SELECT * FROM KRCDT_CONFIRMATION_MONTH WHERE PROCESS_YM IN (" + subYm +") AND SID IN (" + subEmp + ")")){
			for (int i = 0; i < ym.size(); i++) {
				stmt.setInt(i + 1, ym.get(i).v());
			}
			for (int i = 0; i < subList.size(); i++) {
				stmt.setString(i + 1 + ym.size(), subList.get(i));
			}
			return new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				return new ConfirmationMonth(new CompanyId(rec.getString("CID")), rec.getString("SID"), 
						EnumAdaptor.valueOf(rec.getInt("CLOSURE_ID"), ClosureId.class), 
						new ClosureDate(rec.getInt("CLOSURE_DAY"), rec.getBoolean("IS_LAST_DAY")), 
						new YearMonth(rec.getInt("PROCESS_YM")), 
						rec.getGeneralDate("IDENTIFY_DATE"));
			});
		}
	}
}
