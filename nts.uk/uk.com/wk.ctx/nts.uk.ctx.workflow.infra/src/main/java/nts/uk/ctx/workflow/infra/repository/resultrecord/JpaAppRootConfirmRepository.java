package nts.uk.ctx.workflow.infra.repository.resultrecord;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import org.apache.logging.log4j.util.Strings;

import lombok.SneakyThrows;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootConfirm;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdpAppFrameConfirmPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdpAppPhaseConfirmPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppFrameConfirm;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppPhaseConfirm;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppRootConfirm;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaAppRootConfirmRepository extends JpaRepository implements AppRootConfirmRepository {
	
	private final String BASIC_SELECT = 
			/*"SELECT appRoot.ROOT_ID, appRoot.CID, appRoot.EMPLOYEE_ID, appRoot.RECORD_DATE, appRoot.ROOT_TYPE, " +
			"phaseJoin.PHASE_ORDER, phaseJoin.APP_PHASE_ATR, phaseJoin.FRAME_ORDER, phaseJoin.APPROVER_ID, phaseJoin.REPRESENTER_ID, phaseJoin.APPROVAL_DATE " + 
			"FROM WWFDT_APP_ROOT_CONFIRM appRoot LEFT JOIN " +
			"(SELECT phase.ROOT_ID, phase.PHASE_ORDER, phase.APP_PHASE_ATR, " +
			"frame.FRAME_ORDER, frame.APPROVER_ID, frame.REPRESENTER_ID, frame.APPROVAL_DATE " +
			"FROM WWFDT_APP_PHASE_CONFIRM phase " +
			"LEFT JOIN WWFDT_APP_FRAME_CONFIRM frame " +
			"ON phase.ROOT_ID = frame.ROOT_ID and phase.PHASE_ORDER = frame.PHASE_ORDER) phaseJoin " +
			"ON appRoot.ROOT_ID = phaseJoin.ROOT_ID";*/
			"SELECT appRoot.ROOT_ID, appRoot.CID, appRoot.EMPLOYEE_ID, appRoot.RECORD_DATE, appRoot.ROOT_TYPE, "+
			"appRoot.YEARMONTH, appRoot.CLOSURE_ID, appRoot.CLOSURE_DAY, appRoot.LAST_DAY_FLG, "+
			"phase.PHASE_ORDER, phase.APP_PHASE_ATR, frame.FRAME_ORDER, frame.APPROVER_ID, frame.REPRESENTER_ID, frame.APPROVAL_DATE "+
			"FROM WWFDT_APP_ROOT_CONFIRM appRoot "+
			"LEFT JOIN WWFDT_APP_PHASE_CONFIRM phase "+
			"ON appRoot.ROOT_ID = phase.ROOT_ID "+
			"LEFT JOIN WWFDT_APP_FRAME_CONFIRM frame "+
			"ON phase.ROOT_ID = frame.ROOT_ID "+
			"AND phase.PHASE_ORDER = frame.PHASE_ORDER";
	
	private final String FIND_BY_ID = BASIC_SELECT + " WHERE appRoot.ROOT_ID = 'rootID'";
	
	private final String DELETE_PHASE_APPROVER = 
			"DELETE FROM WWFDT_APP_PHASE_CONFIRM WHERE ROOT_ID IN ( " +
			"SELECT appRoot.ROOT_ID FROM WWFDT_APP_ROOT_CONFIRM appRoot " +
			"WHERE appRoot.CID = 'companyID' " +
			"AND appRoot.EMPLOYEE_ID = 'employeeID' " +
			"AND appRoot.ROOT_TYPE = rootType " +
			"AND appRoot.RECORD_DATE = 'recordDate' )";
	
	private final String DELETE_FRAME_APPROVER = 
			"DELETE FROM WWFDT_APP_FRAME_CONFIRM WHERE ROOT_ID IN ( " +
			"SELECT appRoot.ROOT_ID FROM WWFDT_APP_ROOT_CONFIRM appRoot " +
			"WHERE appRoot.CID = 'companyID' " +
			"AND appRoot.EMPLOYEE_ID = 'employeeID' " +
			"AND appRoot.ROOT_TYPE = rootType " +
			"AND appRoot.RECORD_DATE = 'recordDate' )";
	
	private final String DELETE_PHASE_APPROVER_FROM_DATE = 
			"DELETE FROM WWFDT_APP_PHASE_CONFIRM WHERE ROOT_ID IN ( " +
			"SELECT appRoot.ROOT_ID FROM WWFDT_APP_ROOT_CONFIRM appRoot " +
			"WHERE appRoot.CID = 'companyID' " +
			"AND appRoot.EMPLOYEE_ID = 'employeeID' " +
			"AND appRoot.ROOT_TYPE = rootType " +
			"AND appRoot.RECORD_DATE >= 'recordDate' )";
	
	private final String DELETE_FRAME_APPROVER_FROM_DATE = 
			"DELETE FROM WWFDT_APP_FRAME_CONFIRM WHERE ROOT_ID IN ( " +
			"SELECT appRoot.ROOT_ID FROM WWFDT_APP_ROOT_CONFIRM appRoot " +
			"WHERE appRoot.CID = 'companyID' " +
			"AND appRoot.EMPLOYEE_ID = 'employeeID' " +
			"AND appRoot.ROOT_TYPE = rootType " +
			"AND appRoot.RECORD_DATE >= 'recordDate' )";
	
	private final String FIND_BY_EMP_DATE = BASIC_SELECT +
			" WHERE appRoot.CID = 'companyID'" +
			" AND appRoot.ROOT_TYPE = rootType" +
			" AND appRoot.EMPLOYEE_ID = 'employeeID'" +
			" AND appRoot.RECORD_DATE = 'recordDate'";
	
//	private final String DELETE_APP_ROOT_CONFIRM = 
//			"DELETE FROM WWFDT_APP_ROOT_CONFIRM  "+
//			"WHERE CID = 'companyID' " +
//			"AND EMPLOYEE_ID = 'employeeID' " +
//			"AND ROOT_TYPE = rootType " +
//			"AND RECORD_DATE = 'recordDate' ";
//	
//	private final String DELETE_PHASE_APPROVER_FOR_424 = 
//			"DELETE FROM WWFDT_APP_PHASE_CONFIRM WHERE ROOT_ID IN ( " +
//			"SELECT appRoot.ROOT_ID FROM WWFDT_APP_ROOT_CONFIRM appRoot " +
//			"WHERE appRoot.CID = 'companyID' " +
//			"AND appRoot.EMPLOYEE_ID = 'employeeID' " +
//			"AND appRoot.ROOT_TYPE = rootType " +
//			"AND appRoot.RECORD_DATE = 'recordDate' )";
//	
//	private final String DELETE_FRAME_APPROVER_FOR_424 = 
//			"DELETE FROM WWFDT_APP_FRAME_CONFIRM WHERE ROOT_ID IN ( " +
//			"SELECT appRoot.ROOT_ID FROM WWFDT_APP_ROOT_CONFIRM appRoot " +
//			"WHERE appRoot.CID = 'companyID' " +
//			"AND appRoot.EMPLOYEE_ID = 'employeeID' " +
//			"AND appRoot.ROOT_TYPE = rootType " +
//			"AND appRoot.RECORD_DATE = 'recordDate' )";
	
	private final String FIND_BY_EMP_MONTH = BASIC_SELECT +
			" WHERE appRoot.CID = 'companyID'" +
			" AND appRoot.ROOT_TYPE = rootType" +
			" AND appRoot.EMPLOYEE_ID = 'employeeID'" +
			" AND appRoot.YEARMONTH = yearMonth" +
			" AND appRoot.CLOSURE_ID = closureID" +
			" AND appRoot.CLOSURE_DAY = closureDay" +
			" AND appRoot.LAST_DAY_FLG = lastDayFlg";
	
	private final String FIND_BY_EMP_PERIOD_MONTH = BASIC_SELECT +
			" WHERE appRoot.CID = 'companyID'" +
			" AND appRoot.ROOT_TYPE = rootType" +
			" AND appRoot.EMPLOYEE_ID = 'employeeID'" +
			" AND appRoot.RECORD_DATE >= 'startDate'"+
			" AND appRoot.RECORD_DATE <= 'endDate'";
	
	private final String FIND_BY_EMP_YEARMONTH = BASIC_SELECT +
			" WHERE appRoot.CID = 'companyID'" +
			" AND appRoot.ROOT_TYPE = rootType" +
			" AND appRoot.EMPLOYEE_ID = 'employeeID'" +
			" AND appRoot.YEARMONTH = yearMonth";

	@Override
	public Optional<AppRootConfirm> findByID(String rootID) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_ID;
		query = query.replaceAll("rootID", rootID);
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootConfirm> listResult = toDomain(createFullJoinAppRootConfirm(rs));
			if(CollectionUtil.isEmpty(listResult)){
				return Optional.empty();
			} else {
				return Optional.of(listResult.get(0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public void insert(AppRootConfirm appRootConfirm) {
		this.commandProxy().insert(fromDomain(appRootConfirm));
	}

	@Override
	public void update(AppRootConfirm appRootConfirm) {
		this.commandProxy().update(fromDomain(appRootConfirm));
	}

	@Override
	public void delete(AppRootConfirm appRootConfirm) {
		this.commandProxy().remove(WwfdtAppRootConfirm.class, appRootConfirm.getRootID());
	}
	
	@Override
	@SneakyThrows
	public void deleteByRequestList424(String companyID,String employeeID, GeneralDate date, Integer rootType) {
		Connection conn = this.connection();
		String sQue = "SELECT appRoot.ROOT_ID FROM WWFDT_APP_ROOT_CONFIRM appRoot " +
						"WHERE appRoot.CID = '" + companyID + "' AND appRoot.ROOT_TYPE = "
						+ rootType +"  AND appRoot.EMPLOYEE_ID = '"+ employeeID +"' AND appRoot.RECORD_DATE = '"+date.toString("yyyy/MM/dd")+"'";
		String rootID = "";
		try(PreparedStatement statement = conn.prepareStatement(sQue)){
//			statement.setString(1, companyID);
//			statement.setString(2, employeeID);
//			statement.setInt(3, rootType);
//			statement.setDate(4, Date.valueOf(date.localDate()));
			rootID = new NtsResultSet(statement.executeQuery()).getSingle(rec -> {
				
				return rec.getString("ROOT_ID");
			}).orElse("");
		}
		
		if(!rootID.isEmpty()){
			try(PreparedStatement dStatement1 = conn.prepareStatement("DELETE  FROM WWFDT_APP_FRAME_CONFIRM WHERE ROOT_ID = ?")){
				dStatement1.setString(1, rootID);
				dStatement1.executeUpdate();
			}
			try(PreparedStatement dStatement2 = conn.prepareStatement("DELETE FROM WWFDT_APP_PHASE_CONFIRM WHERE ROOT_ID = ?")){
				dStatement2.setString(1, rootID);
				dStatement2.executeUpdate();
			}
			try(PreparedStatement dStatement3 = conn.prepareStatement("DELETE  FROM WWFDT_APP_ROOT_CONFIRM WHERE ROOT_ID = ?")){
				dStatement3.setString(1, rootID);
				dStatement3.executeUpdate();
			}
		}
	}

	private WwfdtAppRootConfirm fromDomain(AppRootConfirm appRootConfirm){
		return new WwfdtAppRootConfirm(
				appRootConfirm.getRootID(), 
				appRootConfirm.getCompanyID(), 
				appRootConfirm.getEmployeeID(), 
				appRootConfirm.getRecordDate(), 
				appRootConfirm.getRootType().value, 
				appRootConfirm.getYearMonth().map(x -> x.v()).orElse(null),
				appRootConfirm.getClosureID().orElse(null),
				appRootConfirm.getClosureDate().map(x -> x.getClosureDay().v()).orElse(null),
				appRootConfirm.getClosureDate().map(x -> x.getLastDayOfMonth()?1:0).orElse(null),
				appRootConfirm.getListAppPhase().stream()
					.map(x -> new WwfdtAppPhaseConfirm(
							new WwfdpAppPhaseConfirmPK(
									appRootConfirm.getRootID(), 
									x.getPhaseOrder()), 
							x.getAppPhaseAtr().value, 
							null,
							x.getListAppFrame().stream()
								.map(y -> new WwfdtAppFrameConfirm(
										new WwfdpAppFrameConfirmPK(
												appRootConfirm.getRootID(), 
												x.getPhaseOrder(), 
												y.getFrameOrder()), 
										y.getApproverID().orElse(null), 
										y.getRepresenterID().orElse(null), 
										y.getApprovalDate(),
										null))
								.collect(Collectors.toList())))
					.collect(Collectors.toList()));
	} 
	
	private List<AppRootConfirm> toDomain(List<FullJoinAppRootConfirm> listFullJoin){
		return listFullJoin.stream().collect(Collectors.groupingBy(FullJoinAppRootConfirm::getRootID)).entrySet()
				.stream().map(x -> {
					String companyID =  x.getValue().get(0).getCompanyID();
					String rootID = x.getValue().get(0).getRootID();
					GeneralDate recordDate = x.getValue().get(0).getRecordDate();
					RecordRootType rootType = EnumAdaptor.valueOf(x.getValue().get(0).getRootType(), RecordRootType.class);
					String employeeID = x.getValue().get(0).getEmployeeID();
					Integer yearMonth = x.getValue().get(0).getYearMonth();
					Integer closureID = x.getValue().get(0).getClosureID();
					Integer closureDay = x.getValue().get(0).getClosureDay();
					Integer lastDayFlg = x.getValue().get(0).getLastDayFlg();
					List<AppPhaseConfirm> listAppPhase = new ArrayList<>();
					Optional<FullJoinAppRootConfirm> isEmptyConfirm = x.getValue().stream().filter(y1 -> y1.getPhaseOrder()==null).findAny();
					if(!isEmptyConfirm.isPresent()){
						listAppPhase = x.getValue().stream().collect(Collectors.groupingBy(FullJoinAppRootConfirm::getPhaseOrder)).entrySet()
						.stream().map(y -> {
							Integer phaseOrder  = y.getValue().get(0).getPhaseOrder();
							ApprovalBehaviorAtr appPhaseAtr =  EnumAdaptor.valueOf(y.getValue().get(0).getAppPhaseAtr(), ApprovalBehaviorAtr.class);
							List<AppFrameConfirm> listAppFrame =
							y.getValue().stream().collect(Collectors.groupingBy(FullJoinAppRootConfirm::getFrameOrder)).entrySet()
							.stream().map(z -> { 
								Integer frameOrder = z.getValue().get(0).getFrameOrder();
								Optional<String> frameApproverID =  Optional.ofNullable(z.getValue().get(0).getApproverID());
								Optional<String> representerID = Optional.ofNullable(z.getValue().get(0).getRepresenterID());
								GeneralDate approvalDate = z.getValue().get(0).getApprovalDate();
								return new AppFrameConfirm(frameOrder, frameApproverID, representerID, approvalDate);
							}).collect(Collectors.toList());
							return new AppPhaseConfirm(phaseOrder, appPhaseAtr, listAppFrame);
						}).collect(Collectors.toList());
					}
					return new AppRootConfirm(rootID, companyID, employeeID, recordDate, rootType, listAppPhase,
							yearMonth == null ? Optional.empty() : Optional.of(new YearMonth(yearMonth)),
							closureID == null ? Optional.empty() : Optional.of(closureID),
							closureDay == null ?  Optional.empty() : Optional.of(new ClosureDate(closureDay, lastDayFlg == 1)));
				}).collect(Collectors.toList());
	}
	
	private List<FullJoinAppRootConfirm> createFullJoinAppRootConfirm(ResultSet rs){
		List<FullJoinAppRootConfirm> listFullData = new ArrayList<>();
		try {
			while (rs.next()) {
				listFullData.add(new FullJoinAppRootConfirm(
						rs.getString("ROOT_ID"), 
						rs.getString("CID"), 
						rs.getString("EMPLOYEE_ID"), 
						GeneralDate.fromString(rs.getString("RECORD_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						Strings.isNotBlank(rs.getString("ROOT_TYPE")) ? Integer.valueOf(rs.getString("ROOT_TYPE")) : null, 
						Strings.isNotBlank(rs.getString("YEARMONTH")) ? Integer.valueOf(rs.getString("YEARMONTH")) : null, 
						Strings.isNotBlank(rs.getString("CLOSURE_ID")) ? Integer.valueOf(rs.getString("CLOSURE_ID")) : null, 
						Strings.isNotBlank(rs.getString("CLOSURE_DAY")) ? Integer.valueOf(rs.getString("CLOSURE_DAY")) : null, 
						Strings.isNotBlank(rs.getString("LAST_DAY_FLG")) ? Integer.valueOf(rs.getString("LAST_DAY_FLG")) : null, 
						Strings.isNotBlank(rs.getString("PHASE_ORDER")) ? Integer.valueOf(rs.getString("PHASE_ORDER")) : null, 
						Strings.isNotBlank(rs.getString("APP_PHASE_ATR")) ? Integer.valueOf(rs.getString("APP_PHASE_ATR")) : null, 
						Strings.isNotBlank(rs.getString("FRAME_ORDER")) ? Integer.valueOf(rs.getString("FRAME_ORDER")) : null, 
						rs.getString("APPROVER_ID"), 
						rs.getString("REPRESENTER_ID"), 
						Strings.isNotBlank(rs.getString("APPROVAL_DATE")) ? GeneralDate.fromString(rs.getString("APPROVAL_DATE"), "yyyy-MM-dd HH:mm:ss") : null ));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listFullData;
	}

	@Override
	public void clearStatus(String companyID, String employeeID, GeneralDate date, RecordRootType rootType) {
		String query1 = DELETE_PHASE_APPROVER.replaceAll("employeeID", employeeID);
		query1 = query1.replaceAll("companyID", companyID);
		query1 = query1.replaceAll("rootType", String.valueOf(rootType.value));
		query1 = query1.replaceAll("recordDate", date.toString("yyyy-MM-dd"));
		String query2 = DELETE_FRAME_APPROVER.replaceAll("employeeID", employeeID);
		query2 = query2.replaceAll("companyID", companyID);
		query2 = query2.replaceAll("rootType", String.valueOf(rootType.value));
		query2 = query2.replaceAll("recordDate", date.toString("yyyy-MM-dd"));
		Connection con = this.getEntityManager().unwrap(Connection.class);
		try (PreparedStatement pstatement1 = con.prepareStatement(query1)) {
			pstatement1.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		try (PreparedStatement pstatement2 = con.prepareStatement(query2)) {
			pstatement2.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void createNewStatus(String companyID, String employeeID, GeneralDate date, RecordRootType rootType) {
		String rootID = UUID.randomUUID().toString();
		AppRootConfirm appRootInstanceNew = new AppRootConfirm(rootID, companyID, employeeID, date, rootType, 
				Collections.emptyList(), Optional.empty(), Optional.empty(), Optional.empty());
		this.insert(appRootInstanceNew);
	}

	@Override
	@SneakyThrows
	public Optional<AppRootConfirm> findByEmpDate(String companyID, String employeeID, GeneralDate date,
			RecordRootType rootType) {

		String query = FIND_BY_EMP_DATE;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		query = query.replaceAll("employeeID", employeeID);
		query = query.replaceAll("recordDate", date.toString("yyyy-MM-dd"));
		try (PreparedStatement pstatement = this.connection().prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootConfirm> listResult = toDomain(createFullJoinAppRootConfirm(rs));
			if(CollectionUtil.isEmpty(listResult)){
				return Optional.empty();
			} else {
				return Optional.of(listResult.get(0));
			}
		}
		
	}

	@Override
	public List<AppRootConfirm> findByEmpDate(String companyID, List<String> employeeIDLst, DatePeriod date,
			RecordRootType rootType) {
		
		List<AppRootConfirm> results = new ArrayList<AppRootConfirm>();
		
		CollectionUtil.split(employeeIDLst, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, employeeIDs -> {
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT appRoot.ROOT_ID, appRoot.CID, appRoot.EMPLOYEE_ID, appRoot.RECORD_DATE, appRoot.ROOT_TYPE, ");
			sql.append(" appRoot.YEARMONTH, appRoot.CLOSURE_ID, appRoot.CLOSURE_DAY, appRoot.LAST_DAY_FLG, ");
			sql.append(" phase.PHASE_ORDER, phase.APP_PHASE_ATR, frame.FRAME_ORDER, frame.APPROVER_ID, frame.REPRESENTER_ID, frame.APPROVAL_DATE ");
			sql.append(" FROM WWFDT_APP_ROOT_CONFIRM appRoot LEFT JOIN WWFDT_APP_PHASE_CONFIRM phase ");
			sql.append(" ON appRoot.ROOT_ID = phase.ROOT_ID ");
			sql.append(" LEFT JOIN WWFDT_APP_FRAME_CONFIRM frame ");
			sql.append(" ON phase.ROOT_ID = frame.ROOT_ID and phase.PHASE_ORDER = frame.PHASE_ORDER");
			sql.append(" WHERE appRoot.CID = ? AND appRoot.ROOT_TYPE = ? AND appRoot.RECORD_DATE <= ? AND appRoot.RECORD_DATE >= ?");
			sql.append(" AND appRoot.EMPLOYEE_ID IN (");
			sql.append(employeeIDs.stream().map(s -> "?").collect(Collectors.joining(",")));
			sql.append(" )");
			
			try (PreparedStatement statement = this.connection().prepareStatement(sql.toString())) {
				statement.setString(1, companyID);
				statement.setInt(2, rootType.value);
				statement.setDate(3, Date.valueOf(date.end().localDate()));
				statement.setDate(4, Date.valueOf(date.start().localDate()));
				for (int i = 0; i < employeeIDs.size(); i++) {
					statement.setString(i + 5, employeeIDs.get(i));
				}
				results.addAll(toDomain(new NtsResultSet(statement.executeQuery()).getList(rs -> createFullJoinAppRootConfirm(rs))));
				
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		
		return results;
	}

	@Override
	public Optional<AppRootConfirm> findByEmpMonth(String companyID, String employeeID, YearMonth yearMonth,
			Integer closureID, ClosureDate closureDate, RecordRootType rootType) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_EMP_MONTH;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		query = query.replaceAll("employeeID", employeeID);
		query = query.replaceAll("yearMonth", yearMonth.v().toString());
		query = query.replaceAll("closureID", closureID.toString());
		query = query.replaceAll("closureDay", closureDate.getClosureDay().v().toString());
		query = query.replaceAll("lastDayFlg", closureDate.getLastDayOfMonth() ? "1" : "0");
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootConfirm> listResult = toDomain(createFullJoinAppRootConfirm(rs));
			if(CollectionUtil.isEmpty(listResult)){
				return Optional.empty();
			} else {
				return Optional.of(listResult.get(0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<AppRootConfirm> findByEmpPeriodMonth(String companyID, String employeeID, DatePeriod period) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_EMP_PERIOD_MONTH;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("rootType", String.valueOf(RecordRootType.CONFIRM_WORK_BY_MONTH.value));
		query = query.replaceAll("employeeID", employeeID);
		query = query.replaceAll("startDate", period.start().toString("yyyy-MM-dd"));
		query = query.replaceAll("endDate", period.end().toString("yyyy-MM-dd"));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootConfirm> listResult = toDomain(createFullJoinAppRootConfirm(rs));
			if(CollectionUtil.isEmpty(listResult)){
				return Optional.empty();
			} else {
				return Optional.of(listResult.get(0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public List<AppRootConfirm> findByEmpYearMonth(String companyID, String employeeID, YearMonth yearMonth) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_EMP_YEARMONTH;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("rootType", String.valueOf(RecordRootType.CONFIRM_WORK_BY_MONTH.value));
		query = query.replaceAll("employeeID", employeeID);
		query = query.replaceAll("yearMonth", yearMonth.v().toString());
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootConfirm> listResult = toDomain(createFullJoinAppRootConfirm(rs));
			if(CollectionUtil.isEmpty(listResult)){
				return Collections.emptyList();
			} else {
				return listResult;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	private FullJoinAppRootConfirm createFullJoinAppRootConfirm(NtsResultRecord rs){
		return new FullJoinAppRootConfirm(
				rs.getString("ROOT_ID"), 
				rs.getString("CID"), 
				rs.getString("EMPLOYEE_ID"), 
				rs.getGeneralDate("RECORD_DATE"), 
				rs.getInt("ROOT_TYPE"), 
				rs.getInt("YEARMONTH"),//yearMonth
				rs.getInt("CLOSURE_ID"),//closureID
				rs.getInt("CLOSURE_DAY"),//closureDay
				rs.getInt("LAST_DAY_FLG"),//lastDayFlg
				rs.getInt("PHASE_ORDER"), 
				rs.getInt("APP_PHASE_ATR"), 
				rs.getInt("FRAME_ORDER"), 
				rs.getString("APPROVER_ID"), 
				rs.getString("REPRESENTER_ID"), 
				rs.getGeneralDate("APPROVAL_DATE"));
			
	}

	@Override
	public void clearStatusFromDate(String companyID, String employeeID, GeneralDate date, RecordRootType rootType) {
		String query1 = DELETE_PHASE_APPROVER_FROM_DATE.replaceAll("employeeID", employeeID);
		query1 = query1.replaceAll("companyID", companyID);
		query1 = query1.replaceAll("rootType", String.valueOf(rootType.value));
		query1 = query1.replaceAll("recordDate", date.toString("yyyy-MM-dd"));
		String query2 = DELETE_FRAME_APPROVER_FROM_DATE.replaceAll("employeeID", employeeID);
		query2 = query2.replaceAll("companyID", companyID);
		query2 = query2.replaceAll("rootType", String.valueOf(rootType.value));
		query2 = query2.replaceAll("recordDate", date.toString("yyyy-MM-dd"));
		Connection con = this.getEntityManager().unwrap(Connection.class);
		try (PreparedStatement pstatement1 = con.prepareStatement(query1)) {
			pstatement1.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		try (PreparedStatement pstatement2 = con.prepareStatement(query2)) {
			pstatement2.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
