package nts.uk.ctx.at.record.infra.repository.affiliationinformation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.primitivevalue.ClassificationCode;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.infra.entity.affiliationinformation.KrcdtDaiAffiliationInf;
import nts.uk.ctx.at.record.infra.entity.affiliationinformation.KrcdtDaiAffiliationInfPK;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.BonusPaySettingCode;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@Stateless
public class JpaAffiliationInforOfDailyPerforRepository extends JpaRepository
		implements AffiliationInforOfDailyPerforRepository {

//	private static final String REMOVE_BY_EMPLOYEE;
	
	private static final String FIND_BY_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDaiAffiliationInf a ");
//		builderString.append("WHERE a.krcdtDaiAffiliationInfPK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDaiAffiliationInfPK.ymd = :ymd ");
//		REMOVE_BY_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiAffiliationInf a ");
		builderString.append("WHERE a.krcdtDaiAffiliationInfPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiAffiliationInfPK.ymd = :ymd ");
		FIND_BY_KEY = builderString.toString();
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAI_AFFILIATION_INF Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + ymd + "'" ;
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
	public void add(AffiliationInforOfDailyPerfor affiliationInforOfDailyPerfor) {
		// this.commandProxy().insert(toEntity(affiliationInforOfDailyPerfor));
		try {
			Connection con = this.getEntityManager().unwrap(Connection.class);
			String bonusPaycode = affiliationInforOfDailyPerfor.getBonusPaySettingCode() != null ? "'" + affiliationInforOfDailyPerfor.getBonusPaySettingCode().v() + "'" : null;
			String insertTableSQL = "INSERT INTO KRCDT_DAI_AFFILIATION_INF ( SID , YMD , EMP_CODE, JOB_ID , CLS_CODE , WKP_ID , BONUS_PAY_CODE ) "
					+ "VALUES( '" + affiliationInforOfDailyPerfor.getEmployeeId() + "' , '"
					+ affiliationInforOfDailyPerfor.getYmd() + "' , '"
					+ affiliationInforOfDailyPerfor.getEmploymentCode().v() + "' , '"
					+ affiliationInforOfDailyPerfor.getJobTitleID() + "' , '"
					+ affiliationInforOfDailyPerfor.getClsCode().v() + "' , '"
					+ affiliationInforOfDailyPerfor.getWplID() + "' , "
					+ bonusPaycode + " )";
			Statement statementI = con.createStatement();
			statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTableSQL));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
	private KrcdtDaiAffiliationInf toEntity(AffiliationInforOfDailyPerfor affiliationInforOfDailyPerfor) {
		val entity = new KrcdtDaiAffiliationInf();

		entity.krcdtDaiAffiliationInfPK = new KrcdtDaiAffiliationInfPK();
		entity.krcdtDaiAffiliationInfPK.employeeId = affiliationInforOfDailyPerfor.getEmployeeId();
		entity.krcdtDaiAffiliationInfPK.ymd = affiliationInforOfDailyPerfor.getYmd();
		entity.bonusPayCode = affiliationInforOfDailyPerfor.getBonusPaySettingCode() != null
				? affiliationInforOfDailyPerfor.getBonusPaySettingCode().v() : null;
		entity.classificationCode = affiliationInforOfDailyPerfor.getClsCode() == null ? null
				: affiliationInforOfDailyPerfor.getClsCode().v();
		entity.employmentCode = affiliationInforOfDailyPerfor.getEmploymentCode() == null ? null
				: affiliationInforOfDailyPerfor.getEmploymentCode().v();
		entity.jobtitleID = affiliationInforOfDailyPerfor.getJobTitleID();
		entity.workplaceID = affiliationInforOfDailyPerfor.getWplID();

		return entity;
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<AffiliationInforOfDailyPerfor> findByKey(String employeeId, GeneralDate ymd) {
    	Optional<AffiliationInforOfDailyPerfor> result =  this.queryProxy().query(FIND_BY_KEY, KrcdtDaiAffiliationInf.class)
    			.setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).getSingle(f -> f.toDomain());
		Optional<AffiliationInforOfDailyPerfor> data = Optional.empty();
		String sql = "select * from KRCDT_DAI_AFFILIATION_INF"
				+ " where SID = ?"
				+ " and YMD = ?";
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
			stmt.setString(1, employeeId);
			stmt.setDate(2, Date.valueOf(ymd.localDate()));
			data = new NtsResultSet(stmt.executeQuery()).getSingle(rec -> {
				AffiliationInforOfDailyPerfor ent = new AffiliationInforOfDailyPerfor(
						new EmploymentCode(rec.getString("EMP_CODE")), 
						employeeId, 
						rec.getString("JOB_ID"), 
						rec.getString("WKP_ID"), 
						ymd, 
						new ClassificationCode(rec.getString("CLS_CODE")), 
						new BonusPaySettingCode(rec.getString("BONUS_PAY_CODE")));
				return ent;
			});
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public void updateByKey(AffiliationInforOfDailyPerfor domain) {
		// Optional<KrcdtDaiAffiliationInf> dataOpt =
		// this.queryProxy().query(FIND_BY_KEY, KrcdtDaiAffiliationInf.class)
		// .setParameter("employeeId", domain.getEmployeeId())
		// .setParameter("ymd", domain.getYmd()).getSingle();
		// KrcdtDaiAffiliationInf data = dataOpt.isPresent() ? dataOpt.get() :
		// new KrcdtDaiAffiliationInf();
		// if(!dataOpt.isPresent()){
		// data.krcdtDaiAffiliationInfPK = new
		// KrcdtDaiAffiliationInfPK(domain.getEmployeeId(), domain.getYmd());
		// }
		// data.bonusPayCode = domain.getBonusPaySettingCode() == null ? null :
		// domain.getBonusPaySettingCode().v();
		// data.classificationCode = domain.getClsCode() == null ? null :
		// domain.getClsCode().v();
		// data.employmentCode = domain.getEmploymentCode() == null ? null :
		// domain.getEmploymentCode().v();
		// data.workplaceID = domain.getWplID();
		// data.jobtitleID = domain.getJobTitleID();
		// this.commandProxy().update(data);

		Connection con = this.getEntityManager().unwrap(Connection.class);
		String bonusPaycode = domain.getBonusPaySettingCode() != null ? "'" + domain.getBonusPaySettingCode().v() + "'" : null;
		String updateTableSQL = " UPDATE KRCDT_DAI_AFFILIATION_INF SET EMP_CODE = '"
				+ domain.getEmploymentCode().v() + "' , JOB_ID = '" + domain.getJobTitleID()
				+ "' , CLS_CODE = '" + domain.getClsCode().v() + "' , WKP_ID = '" + domain.getWplID()
				+ "' , BONUS_PAY_CODE = " + bonusPaycode + " WHERE SID = '"
				+ domain.getEmployeeId() + "' AND YMD = '" + domain.getYmd() + "'";
		try {
				con.createStatement().executeUpdate(JDBCUtil.toUpdateWithCommonField(updateTableSQL));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<AffiliationInforOfDailyPerfor> finds(List<String> employeeId, DatePeriod ymd) {
		List<AffiliationInforOfDailyPerfor> result = new ArrayList<>();
		
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(internalQuery(ymd, empIds));
		});
		return result;
	}
	
	@SneakyThrows
	private List<AffiliationInforOfDailyPerfor> internalQuery(DatePeriod baseDate, List<String> empIds) {
		String subEmp = NtsStatement.In.createParamsString(empIds);
		List<AffiliationInforOfDailyPerfor> result = new ArrayList<>();
		String sql = "select EMP_CODE, SID, JOB_ID, WKP_ID, YMD, CLS_CODE, BONUS_PAY_CODE from BSYMT_AFF_JOB_HIST h"
				+ " where SID in (" + NtsStatement.In.createParamsString(empIds) + ")"
				+ " and YMD <= ?"
				+ " and YMD >= ?";
		
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
			
			int i = 0;
			for (; i < empIds.size(); i++) {
				stmt.setString(1 + i, empIds.get(i));
			}

			stmt.setDate(1 + i, Date.valueOf(baseDate.end().localDate()));
			stmt.setDate(2 + i, Date.valueOf(baseDate.start().localDate()));
			
			result = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				AffiliationInforOfDailyPerfor ent = new AffiliationInforOfDailyPerfor(new EmploymentCode(rec.getString("EMP_CODE")), 
						rec.getString("SID"), rec.getString("JOB_ID"), rec.getString("WKP_ID"), rec.getGeneralDate("YMD"), 
						new ClassificationCode(rec.getString("CLS_CODE")), new BonusPaySettingCode(rec.getString("BONUS_PAY_CODE")));
				return ent;
			});
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@SneakyThrows
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<AffiliationInforOfDailyPerfor> finds(Map<String, List<GeneralDate>> param) {
		List<String> subList = param.keySet().stream().collect(Collectors.toList());
		List<GeneralDate> subListDate = param.values().stream().flatMap(x -> x.stream()).collect(Collectors.toList());
		List<AffiliationInforOfDailyPerfor> result = new ArrayList<>();

		CollectionUtil.split(subList, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(internalQueryMap(subListDate, empIds));
		});
		return result;
	}
	
	@SneakyThrows
	private List<AffiliationInforOfDailyPerfor> internalQueryMap(List<GeneralDate> subListDate, List<String> subList) {
		String subEmp = NtsStatement.In.createParamsString(subList);
    	String subInDate = NtsStatement.In.createParamsString(subListDate);
    	
		StringBuilder query = new StringBuilder("SELECT EMP_CODE, SID, JOB_ID, WKP_ID, YMD, CLS_CODE, BONUS_PAY_CODE FROM KRCDT_DAI_AFFILIATION_INF");
		query.append(" WHERE SID IN (" + subEmp + ")");
		query.append(" AND YMD IN (" + subInDate + ")");
		
		try (val stmt = this.connection().prepareStatement(query.toString())){
			for (int i = 0; i < subList.size(); i++) {
				stmt.setString(i + 1, subList.get(i));
			}
			
			for (int i = 0; i < subListDate.size(); i++) {
				stmt.setDate(1 + i + subList.size(),  Date.valueOf(subListDate.get(i).localDate()));
			}
			
			return new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				return new AffiliationInforOfDailyPerfor(new EmploymentCode(rec.getString("EMP_CODE")), 
						rec.getString("SID"), rec.getString("JOB_ID"), rec.getString("WKP_ID"), rec.getGeneralDate("YMD"), 
						new ClassificationCode(rec.getString("CLS_CODE")), new BonusPaySettingCode(rec.getString("BONUS_PAY_CODE")));
			});
		}
	}
}
