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
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.util.value.MutableValue;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstanceRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdpAppApproveInstancePK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdpAppFrameInstancePK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdpAppPhaseInstancePK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppApproveInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppFrameInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppPhaseInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppRootInstance;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaAppRootInstanceRepository extends JpaRepository implements AppRootInstanceRepository {
	
	private final String BASIC_SELECT = 
			/*"SELECT appRoot.ROOT_ID, appRoot.CID, appRoot.EMPLOYEE_ID, appRoot.START_DATE, appRoot.END_DATE, appRoot.ROOT_TYPE, " +
			"phaseJoin.PHASE_ORDER, phaseJoin.APPROVAL_FORM, " +
			"phaseJoin.FRAME_ORDER, phaseJoin.CONFIRM_ATR, phaseJoin.APPROVER_CHILD_ID " +
			"FROM WWFDT_APP_ROOT_INSTANCE appRoot LEFT JOIN " +
			"(SELECT phase.ROOT_ID, phase.PHASE_ORDER, phase.APPROVAL_FORM, " +
			"frameJoin.FRAME_ORDER, frameJoin.CONFIRM_ATR, frameJoin.APPROVER_CHILD_ID " +
			"FROM WWFDT_APP_PHASE_INSTANCE phase LEFT JOIN " +
			"(SELECT frame.ROOT_ID, frame.PHASE_ORDER, frame.FRAME_ORDER, frame.CONFIRM_ATR, a.APPROVER_CHILD_ID " +
			"FROM WWFDT_APP_FRAME_INSTANCE frame LEFT JOIN WWFDT_APP_APPROVE_INSTANCE a " +
			"ON frame.ROOT_ID = a.ROOT_ID AND frame.PHASE_ORDER = a.PHASE_ORDER AND frame.FRAME_ORDER = a.FRAME_ORDER) frameJoin " +
			"ON phase.ROOT_ID = frameJoin.ROOT_ID AND phase.PHASE_ORDER = frameJoin.PHASE_ORDER) phaseJoin " +
			"ON appRoot.ROOT_ID = phaseJoin.ROOT_ID";*/
			"SELECT appRoot.ROOT_ID, appRoot.CID, appRoot.EMPLOYEE_ID, appRoot.START_DATE, appRoot.END_DATE, appRoot.ROOT_TYPE, "+
			"phase.PHASE_ORDER, phase.APPROVAL_FORM, frame.FRAME_ORDER, frame.CONFIRM_ATR, appApprover.APPROVER_CHILD_ID "+
			"FROM WWFDT_APP_ROOT_INSTANCE appRoot "+
			"LEFT JOIN WWFDT_APP_PHASE_INSTANCE phase "+
			"ON appRoot.ROOT_ID = phase.ROOT_ID "+
			"LEFT JOIN WWFDT_APP_FRAME_INSTANCE frame "+
			"ON phase.ROOT_ID = frame.ROOT_ID "+
			"AND phase.PHASE_ORDER = frame.PHASE_ORDER "+
			"LEFT JOIN WWFDT_APP_APPROVE_INSTANCE appApprover "+
			"ON frame.ROOT_ID = appApprover.ROOT_ID "+
			"AND frame.PHASE_ORDER = appApprover.PHASE_ORDER "+
			"AND frame.FRAME_ORDER = appApprover.FRAME_ORDER";
	
	private final String FIND_BY_ID = BASIC_SELECT + " WHERE appRoot.ROOT_ID = 'rootID'";
	
	private final String FIND_BY_EMP_DATE = BASIC_SELECT +
			" WHERE appRoot.EMPLOYEE_ID = 'employeeID'" +
			" AND appRoot.CID = 'companyID'" +
			" AND appRoot.ROOT_TYPE = rootType" +
			" AND appRoot.START_DATE <= 'recordDate'";
	
	private final String FIND_BY_EMP_FROM_DATE = BASIC_SELECT +
			" WHERE appRoot.EMPLOYEE_ID = 'employeeID'" +
			" AND appRoot.CID = 'companyID'" +
			" AND appRoot.ROOT_TYPE = rootType" +
			" AND appRoot.START_DATE >= 'recordDate'";
	
	private final String FIND_BY_EMP_DATE_NEWEST = BASIC_SELECT +
			" WHERE appRoot.ROOT_ID IN (" +
			" SELECT TOP 1 ROOT_ID FROM WWFDT_APP_ROOT_INSTANCE" +
			" WHERE EMPLOYEE_ID = 'employeeID'" +
			" AND CID = 'companyID'" +
			" AND ROOT_TYPE = rootType " +
			"order by START_DATE desc)";
	
	private final String FIND_BY_EMP_DATE_NEWEST_BELOW = BASIC_SELECT +
			" WHERE appRoot.ROOT_ID IN (" +
			" SELECT TOP 1 ROOT_ID FROM WWFDT_APP_ROOT_INSTANCE" +
			" WHERE EMPLOYEE_ID = 'employeeID'" +
			" AND CID = 'companyID'" +
			" AND ROOT_TYPE = rootType " +
			" AND START_DATE < 'recordDate' " +
			"order by START_DATE desc)";
	
	private final String FIND_BY_EMPS_PERIOD = BASIC_SELECT + 
			" WHERE appRoot.EMPLOYEE_ID IN (employeeIDLst)"+
			" AND appRoot.CID = 'companyID'"+
			" AND appRoot.ROOT_TYPE = rootType"+
			" AND appRoot.END_DATE >= 'startDate'"+
			" AND appRoot.START_DATE <= 'endDate'";
	
	private final String FIND_BY_APPROVER_PERIOD = BASIC_SELECT + 
			" WHERE appRoot.ROOT_ID IN (SELECT ROOT_ID FROM (" +
			BASIC_SELECT +
			" WHERE appApprover.APPROVER_CHILD_ID = 'approverID'"+
			" AND appRoot.CID = 'companyID'"+
			" AND appRoot.ROOT_TYPE = rootType"+
			" AND appRoot.END_DATE >= 'startDate'"+
			" AND appRoot.START_DATE <= 'endDate') result)";
	
	private final String FIND_BY_CONTAIN_DATE = BASIC_SELECT +
			" WHERE appRoot.EMPLOYEE_ID = 'employeeID'" +
			" AND appRoot.CID = 'companyID'" +
			" AND appRoot.ROOT_TYPE = rootType" +
			" AND appRoot.START_DATE <= 'recordDate'" +
			" AND appRoot.END_DATE >= 'recordDate'";

	@Override
	public Optional<AppRootInstance> findByID(String rootID) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_ID;
		query = query.replaceAll("rootID", rootID);
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootInstance> listResult = toDomain(createFullJoinAppRootInstance(rs));
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
	public void insert(AppRootInstance appRootInstance) {
		this.commandProxy().insert(fromDomain(appRootInstance));
		this.getEntityManager().flush();
	}

	@Override
	public void update(AppRootInstance appRootInstance) {
		this.commandProxy().update(fromDomain(appRootInstance));
		this.getEntityManager().flush();
	}

	@Override
	public void delete(AppRootInstance appRootInstance) {
		this.commandProxy().remove(WwfdtAppRootInstance.class, appRootInstance.getRootID());
		this.getEntityManager().flush();
	}
	
	private WwfdtAppRootInstance fromDomain(AppRootInstance appRootInstance){
		return new WwfdtAppRootInstance(
				appRootInstance.getRootID(), 
				appRootInstance.getCompanyID(), 
				appRootInstance.getEmployeeID(), 
				appRootInstance.getDatePeriod().start(), 
				appRootInstance.getDatePeriod().end(), 
				appRootInstance.getRootType().value, 
				appRootInstance.getListAppPhase().stream()
					.map(x -> new WwfdtAppPhaseInstance(
							new WwfdpAppPhaseInstancePK(
									appRootInstance.getRootID(), 
									x.getPhaseOrder()), 
							x.getApprovalForm().value, 
							null,
							x.getListAppFrame().stream()
								.map(y -> new WwfdtAppFrameInstance(
										new WwfdpAppFrameInstancePK(
												appRootInstance.getRootID(), 
												x.getPhaseOrder(), 
												y.getFrameOrder()), 
										y.isConfirmAtr() ? 1 : 0,
										null,
										y.getListApprover().stream()
											.map(z -> new WwfdtAppApproveInstance(
													new WwfdpAppApproveInstancePK(
															appRootInstance.getRootID(), 
															x.getPhaseOrder(), 
															y.getFrameOrder(), 
															z),
													null)
											).collect(Collectors.toList())))
							.collect(Collectors.toList())))
					.collect(Collectors.toList()));
	}
	
	private List<AppRootInstance> toDomain(List<FullJoinAppRootInstance> listFullJoin){
		return listFullJoin.stream().collect(Collectors.groupingBy(FullJoinAppRootInstance::getRootID))
						.entrySet().stream().map(x -> {
					FullJoinAppRootInstance first = x.getValue().get(0);
					String companyID =  first.getCompanyID();
					String rootID = first.getRootID();
					GeneralDate startDate = first.getStartDate();
					GeneralDate endDate = first.getEndDate();
					RecordRootType rootType = EnumAdaptor.valueOf(first.getRootType(), RecordRootType.class);
					String employeeID = first.getEmployeeID();
					List<AppPhaseInstance> listAppPhase = x.getValue().stream()
							.collect(Collectors.groupingBy(FullJoinAppRootInstance::getPhaseOrder))
							.entrySet().stream().map(y -> {
						Integer phaseOrder  = y.getValue().get(0).getPhaseOrder();
						ApprovalForm approvalForm =  EnumAdaptor.valueOf(y.getValue().get(0).getApprovalForm(), ApprovalForm.class);
						List<AppFrameInstance> listAppFrame = y.getValue().stream()
								.collect(Collectors.groupingBy(FullJoinAppRootInstance::getFrameOrder))
								.entrySet().stream().map(z -> { 
							Integer frameOrder = z.getValue().get(0).getFrameOrder();
							Boolean confirmAtr = z.getValue().get(0).getConfirmAtr()==1?true:false;
							List<String> approvalIDLst = z.getValue().stream().map(t -> t.getApproverChildID()).collect(Collectors.toList());
							return new AppFrameInstance(frameOrder, confirmAtr, approvalIDLst);
						}).collect(Collectors.toList());
						return new AppPhaseInstance(phaseOrder, approvalForm, listAppFrame);
					}).collect(Collectors.toList());
					return new AppRootInstance(rootID, companyID, employeeID, new DatePeriod(startDate, endDate), rootType, listAppPhase);
				}).collect(Collectors.toList());
	}
	
	private List<FullJoinAppRootInstance> createFullJoinAppRootInstance(ResultSet rs){
		List<FullJoinAppRootInstance> listFullData = new ArrayList<>();
		try {
			while (rs.next()) {
				listFullData.add(new FullJoinAppRootInstance(
						rs.getString("ROOT_ID"), 
						rs.getString("CID"), 
						rs.getString("EMPLOYEE_ID"), 
						GeneralDate.fromString(rs.getString("START_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						GeneralDate.fromString(rs.getString("END_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						Integer.valueOf(rs.getString("ROOT_TYPE")), 
						Integer.valueOf(rs.getString("PHASE_ORDER")), 
						Integer.valueOf(rs.getString("APPROVAL_FORM")), 
						Integer.valueOf(rs.getString("FRAME_ORDER")), 
						Integer.valueOf(rs.getString("CONFIRM_ATR")), 
						rs.getString("APPROVER_CHILD_ID")));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listFullData;
	}

	@Override
	public Optional<AppRootInstance> findByEmpDate(String companyID, String employeeID, GeneralDate recordDate, RecordRootType rootType) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_EMP_DATE;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("employeeID", employeeID);
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		query = query.replaceAll("recordDate", recordDate.toString("yyyy-MM-dd"));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootInstance> listResult = toDomain(createFullJoinAppRootInstance(rs));
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
	public Optional<AppRootInstance> findByEmpDateNewest(String companyID, String employeeID, RecordRootType rootType) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_EMP_DATE_NEWEST;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("employeeID", employeeID);
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootInstance> listResult = toDomain(createFullJoinAppRootInstance(rs));
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
	public List<AppRootInstance> findByEmpLstPeriod(List<String> employeeIDLst, DatePeriod period,
			RecordRootType rootType) {
		// Đối ứng SPR
		String companyID = "000000000000-0001";
		String loginCompanyID = AppContexts.user().companyId();
		if(Strings.isNotBlank(loginCompanyID)){
			companyID = loginCompanyID;
		}
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_EMPS_PERIOD;
		
		String employeeIDLstParam = "";
		if(CollectionUtil.isEmpty(employeeIDLst)){
			employeeIDLstParam = "''";
		} else {
			for(int i = 0; i<employeeIDLst.size(); i++){
				employeeIDLstParam+="'"+employeeIDLst.get(i)+"'";
				if(i<employeeIDLst.size()-1){
					employeeIDLstParam+=",";	
				}
			}
		}
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("employeeIDLst", employeeIDLstParam);
		query = query.replaceAll("startDate", period.start().toString("yyyy-MM-dd"));
		query = query.replaceAll("endDate", period.end().toString("yyyy-MM-dd"));
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootInstance> listResult = toDomain(createFullJoinAppRootInstance(rs));
			if(!CollectionUtil.isEmpty(listResult)){
				return listResult;
			} else {
				return Collections.emptyList();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	@Override
	public List<AppRootInstance> findByEmpLstPeriod(String compID, List<String> employeeIDLst, DatePeriod period,
			RecordRootType rootType) {
		if(employeeIDLst.isEmpty()){
			return new ArrayList<>();
		}
		List<AppRootInstance> result = new ArrayList<>();
		MutableValue<Exception> exception = new MutableValue<>(null);
		
		CollectionUtil.split(employeeIDLst, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, c -> {
			if(exception.optional().isPresent()){
				return;
			}
			try {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT appRoot.ROOT_ID, appRoot.CID, appRoot.EMPLOYEE_ID, appRoot.START_DATE, appRoot.END_DATE, appRoot.ROOT_TYPE, ");
				sql.append(" frame.PHASE_ORDER, phase.APPROVAL_FORM, frame.FRAME_ORDER, frame.CONFIRM_ATR, a.APPROVER_CHILD_ID  ");
				sql.append(" FROM WWFDT_APP_ROOT_INSTANCE appRoot LEFT JOIN WWFDT_APP_PHASE_INSTANCE phase ON appRoot.ROOT_ID = phase.ROOT_ID ");
				sql.append(" LEFT JOIN WWFDT_APP_FRAME_INSTANCE frame ON phase.ROOT_ID = frame.ROOT_ID AND phase.PHASE_ORDER = frame.PHASE_ORDER ");
				sql.append(" LEFT JOIN WWFDT_APP_APPROVE_INSTANCE a ON frame.ROOT_ID = a.ROOT_ID AND frame.PHASE_ORDER = a.PHASE_ORDER AND frame.FRAME_ORDER = a.FRAME_ORDER ");
				sql.append(" WHERE appRoot.CID = ? AND appRoot.ROOT_TYPE = ? AND appRoot.END_DATE >= ? AND appRoot.START_DATE <= ? ");
				sql.append(" AND appRoot.EMPLOYEE_ID IN ( ");
				sql.append(joinParam(c));
				sql.append(" ) ");
				
				try (PreparedStatement statement = this.connection().prepareStatement(sql.toString())) {
					statement.setString(1, compID);
					statement.setInt(2, rootType.value);
					statement.setDate(3, Date.valueOf(period.start().localDate()));
					statement.setDate(4, Date.valueOf(period.end().localDate()));
					for (int i = 0; i < employeeIDLst.size(); i++) {
						statement.setString(i + 5, employeeIDLst.get(i));
					}
					result.addAll(toDomain(new NtsResultSet(statement.executeQuery()).getList(rs -> createFullJoinAppRootInstance(rs,compID, rootType.value))));
				}
			} catch (SQLException e) {
				exception.set(e);
			}
		});
		
		if(exception.optional().isPresent()){
			throw new RuntimeException(exception.get());
		}
		
		return result;
	}

	private String joinParam(List<String> employeeIDLst) {
		return employeeIDLst.stream().map(x -> "?").collect(Collectors.joining(","));
	}

	private FullJoinAppRootInstance createFullJoinAppRootInstance(NtsResultRecord rs, String comID, int rootType){
		return new FullJoinAppRootInstance(
				rs.getString("ROOT_ID"), 
				comID, 
				rs.getString("EMPLOYEE_ID"), 
				rs.getGeneralDate("START_DATE"), 
				rs.getGeneralDate("END_DATE"), 
				rootType, 
				rs.getInt("PHASE_ORDER"), 
				rs.getInt("APPROVAL_FORM"), 
				rs.getInt("FRAME_ORDER"), 
				rs.getInt("CONFIRM_ATR"), 
				rs.getString("APPROVER_CHILD_ID"));
	}
	
	@Override
	public List<AppRootInstance> findByApproverPeriod(String approverID, DatePeriod period,
			RecordRootType rootType) {
		String companyID =  AppContexts.user().companyId();
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_APPROVER_PERIOD;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("approverID", approverID);
		query = query.replaceAll("startDate", period.start().toString("yyyy-MM-dd"));
		query = query.replaceAll("endDate", period.end().toString("yyyy-MM-dd"));
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootInstance> listResult = toDomain(createFullJoinAppRootInstance(rs));
			if(!CollectionUtil.isEmpty(listResult)){
				return listResult;
			} else {
				return Collections.emptyList();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public List<AppRootInstance> findByEmpFromDate(String companyID, String employeeID, GeneralDate recordDate,
			RecordRootType rootType) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_EMP_FROM_DATE;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("employeeID", employeeID);
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		query = query.replaceAll("recordDate", recordDate.toString("yyyy-MM-dd"));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootInstance> listResult = toDomain(createFullJoinAppRootInstance(rs));
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

	@Override
	public Optional<AppRootInstance> findByEmpDateNewestBelow(String companyID, String employeeID,
			GeneralDate recordDate, RecordRootType rootType) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_EMP_DATE_NEWEST_BELOW;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("employeeID", employeeID);
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		query = query.replaceAll("recordDate", recordDate.toString("yyyy-MM-dd"));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootInstance> listResult = toDomain(createFullJoinAppRootInstance(rs));
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
	public Optional<AppRootInstance> findByContainDate(String companyID, String employeeID, GeneralDate recordDate,
			RecordRootType rootType) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_CONTAIN_DATE;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("employeeID", employeeID);
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		query = query.replaceAll("recordDate", recordDate.toString("yyyy-MM-dd"));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootInstance> listResult = toDomain(createFullJoinAppRootInstance(rs));
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
	public List<AppRootInstance> findByApproverDateCID(String companyID, String approverID, GeneralDate date, RecordRootType rootType) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = FIND_BY_APPROVER_PERIOD;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("approverID", approverID);
		query = query.replaceAll("startDate", date.toString("yyyy-MM-dd"));
		query = query.replaceAll("endDate", date.toString("yyyy-MM-dd"));
		query = query.replaceAll("rootType", String.valueOf(rootType.value));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<AppRootInstance> listResult = toDomain(createFullJoinAppRootInstance(rs));
			if(!CollectionUtil.isEmpty(listResult)){
				return listResult;
			} else {
				return Collections.emptyList();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
