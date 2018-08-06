package nts.uk.ctx.workflow.infra.repository.resultrecord;

import java.sql.Connection;
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

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstanceRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdpAppFrameInstancePK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdpAppPhaseInstancePK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppFrameInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppPhaseInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.WwfdtAppRootInstance;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaAppRootInstanceRepository extends JpaRepository implements AppRootInstanceRepository {
	
	private final String BASIC_SELECT = 
			"SELECT appRoot.ROOT_ID, appRoot.CID, appRoot.EMPLOYEE_ID, appRoot.RECORD_DATE, appRoot.ROOT_TYPE, " +
			"phaseJoin.PHASE_ORDER, phaseJoin.APP_PHASE_ATR, phaseJoin.FRAME_ORDER, phaseJoin.APPROVER_ID, phaseJoin.REPRESENTER_ID, phaseJoin.APPROVAL_DATE " + 
			"FROM WWFDT_APP_ROOT_INSTANCE appRoot LEFT JOIN " +
			"(SELECT phase.ROOT_ID, phase.PHASE_ORDER, phase.APP_PHASE_ATR, " +
			"frame.FRAME_ORDER, frame.APPROVER_ID, frame.REPRESENTER_ID, frame.APPROVAL_DATE " +
			"FROM WWFDT_APP_PHASE_INSTANCE phase " +
			"LEFT JOIN WWFDT_APP_FRAME_INSTANCE frame " +
			"ON phase.ROOT_ID = frame.ROOT_ID and phase.PHASE_ORDER = frame.PHASE_ORDER) phaseJoin " +
			"ON appRoot.ROOT_ID = phaseJoin.ROOT_ID";
	
	private final String FIND_BY_ID = BASIC_SELECT + " WHERE appRoot.ROOT_ID = 'rootID'";
	
	private final String DELETE_PHASE_APPROVER = 
			"DELETE FROM WWFDT_APP_PHASE_INSTANCE WHERE ROOT_ID IN ( " +
			"SELECT appRoot.ROOT_ID FROM WWFDT_APP_ROOT_INSTANCE appRoot " +
			"WHERE appRoot.CID = 'companyID' " +
			"AND appRoot.EMPLOYEE_ID = 'employeeID' " +
			"AND appRoot.ROOT_TYPE = rootType " +
			"AND appRoot.RECORD_DATE >= 'recordDate' )";
	
	private final String DELETE_FRAME_APPROVER = 
			"DELETE FROM WWFDT_APP_FRAME_INSTANCE WHERE ROOT_ID IN ( " +
			"SELECT appRoot.ROOT_ID FROM WWFDT_APP_ROOT_INSTANCE appRoot " +
			"WHERE appRoot.CID = 'companyID' " +
			"AND appRoot.EMPLOYEE_ID = 'employeeID' " +
			"AND appRoot.ROOT_TYPE = rootType " +
			"AND appRoot.RECORD_DATE >= 'recordDate' )";
	
	private final String FIND_BY_EMP_DATE = BASIC_SELECT +
			" WHERE appRoot.CID = 'companyID'" +
			" AND appRoot.ROOT_TYPE = 'rootType'" +
			" AND appRoot.EMPLOYEE_ID = 'employeeID'" +
			" AND appRoot.RECORD_DATE = 'recordDate'";

	@Override
	public Optional<AppRootInstance> findByID(String rootID) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		try {
			String query = FIND_BY_ID;
			query = query.replaceAll("rootID", rootID);
			PreparedStatement pstatement = con.prepareStatement(query);
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
	}

	@Override
	public void update(AppRootInstance appRootInstance) {
		this.commandProxy().update(fromDomain(appRootInstance));
	}

	@Override
	public void delete(AppRootInstance appRootInstance) {
		this.commandProxy().remove(WwfdtAppRootInstance.class, appRootInstance.getRootID());
	}

	private WwfdtAppRootInstance fromDomain(AppRootInstance appRootInstance){
		return new WwfdtAppRootInstance(
				appRootInstance.getRootID(), 
				appRootInstance.getCompanyID(), 
				appRootInstance.getEmployeeID(), 
				appRootInstance.getRecordDate(), 
				appRootInstance.getRootType().value, 
				appRootInstance.getListAppPhase().stream()
					.map(x -> new WwfdtAppPhaseInstance(
							new WwfdpAppPhaseInstancePK(
									appRootInstance.getRootID(), 
									x.getPhaseOrder()), 
							x.getAppPhaseAtr().value, 
							null,
							x.getListAppFrame().stream()
								.map(y -> new WwfdtAppFrameInstance(
										new WwfdpAppFrameInstancePK(
												appRootInstance.getRootID(), 
												x.getPhaseOrder(), 
												y.getFrameOrder()), 
										y.getApproverID().orElse(null), 
										y.getRepresenterID().orElse(null), 
										y.getApprovalDate(),
										null))
								.collect(Collectors.toList())))
					.collect(Collectors.toList()));
	} 
	
	private List<AppRootInstance> toDomain(List<FullJoinAppRootInstance> listFullJoin){
		return listFullJoin.stream().collect(Collectors.groupingBy(FullJoinAppRootInstance::getRootID)).entrySet()
				.stream().map(x -> {
					String companyID =  x.getValue().get(0).getCompanyID();
					String rootID = x.getValue().get(0).getRootID();
					GeneralDate recordDate = x.getValue().get(0).getRecordDate();
					RecordRootType rootType = EnumAdaptor.valueOf(x.getValue().get(0).getRootType(), RecordRootType.class);
					String employeeID = x.getValue().get(0).getEmployeeID();
					List<AppPhaseInstance> listAppPhase =
					x.getValue().stream().collect(Collectors.groupingBy(FullJoinAppRootInstance::getPhaseOrder)).entrySet()
					.stream().map(y -> {
						Integer phaseOrder  = y.getValue().get(0).getPhaseOrder();
						ApprovalBehaviorAtr appPhaseAtr =  EnumAdaptor.valueOf(y.getValue().get(0).getAppPhaseAtr(), ApprovalBehaviorAtr.class);
						List<AppFrameInstance> listAppFrame =
						y.getValue().stream().collect(Collectors.groupingBy(FullJoinAppRootInstance::getFrameOrder)).entrySet()
						.stream().map(z -> { 
							Integer frameOrder = z.getValue().get(0).getFrameOrder();
							Optional<String> frameApproverID =  Optional.ofNullable(z.getValue().get(0).getApproverID());
							Optional<String> representerID = Optional.ofNullable(z.getValue().get(0).getRepresenterID());
							GeneralDate approvalDate = z.getValue().get(0).getApprovalDate();
							return new AppFrameInstance(frameOrder, frameApproverID, representerID, approvalDate);
						}).collect(Collectors.toList());
						return new AppPhaseInstance(phaseOrder, appPhaseAtr, listAppFrame);
					}).collect(Collectors.toList());
					return new AppRootInstance(rootID, companyID, employeeID, recordDate, rootType, listAppPhase);
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
						GeneralDate.fromString(rs.getString("RECORD_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						Integer.valueOf(rs.getString("ROOT_TYPE")), 
						Integer.valueOf(rs.getString("PHASE_ORDER")), 
						Integer.valueOf(rs.getString("APP_PHASE_ATR")), 
						Integer.valueOf(rs.getString("FRAME_ORDER")), 
						rs.getString("APPROVER_ID"), 
						rs.getString("REPRESENTER_ID"), 
						GeneralDate.fromString(rs.getString("APPROVAL_DATE"), "yyyy-MM-dd HH:mm:ss")));
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
		try {
			PreparedStatement pstatement1 = con.prepareStatement(query1);
			pstatement1.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		try {
			PreparedStatement pstatement2 = con.prepareStatement(query2);
			pstatement2.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void createNewStatus(String companyID, String employeeID, GeneralDate date, RecordRootType rootType) {
		String rootID = UUID.randomUUID().toString();
		AppRootInstance appRootInstanceNew = new AppRootInstance(rootID, companyID, employeeID, date, rootType, Collections.emptyList());
		this.insert(appRootInstanceNew);
	}

	@Override
	public Optional<AppRootInstance> findByEmpDate(String companyID, String employeeID, GeneralDate date,
			RecordRootType rootType) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		try {
			String query = FIND_BY_EMP_DATE;
			query = query.replaceAll("companyID", companyID);
			query = query.replaceAll("rootType", String.valueOf(rootType.value));
			query = query.replaceAll("employeeID", employeeID);
			query = query.replaceAll("recordDate", date.toString("yyyy-MM-dd"));
			PreparedStatement pstatement = con.prepareStatement(query);
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
	
}
