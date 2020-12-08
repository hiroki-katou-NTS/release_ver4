package nts.uk.ctx.at.record.infra.repository.log;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtExecErrMsg;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtErrMessageInfoPK;
import nts.uk.ctx.at.shared.dom.dailyperformanceprocessing.ErrMessageResource;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
@Stateless
public class JpaErrMessageInfoRepository extends JpaRepository implements ErrMessageInfoRepository {

	private static final String SELECT_FROM_ERR_MESSAGE = "SELECT c FROM KrcdtExecErrMsg c ";
	
	private static final String SELECT_ERR_MESSAGE_BY_EMPID = SELECT_FROM_ERR_MESSAGE
			+ " WHERE c.krcdtErrMessageInfoPK.empCalAndSumExecLogID = :empCalAndSumExecLogID ";
	
	private static final String SELECT_ERR_MESSAGE_BY_EXECONTEXT = SELECT_FROM_ERR_MESSAGE
			+ " WHERE c.krcdtErrMessageInfoPK.empCalAndSumExecLogID = :empCalAndSumExecLogID"
			+ " AND c.krcdtErrMessageInfoPK.executionContent = :executionContent";
	
	private static final String SELECT_ERR_MESSAGE_BYID = SELECT_ERR_MESSAGE_BY_EMPID
			+ " AND c.krcdtErrMessageInfoPK.executionContent = :executionContent ";
	
	private static final String SELECT_ERR_MESSAGE_BY_ALL_ID = SELECT_FROM_ERR_MESSAGE
			+ " WHERE c.krcdtErrMessageInfoPK.employeeID = :employeeID "
			+ " AND c.krcdtErrMessageInfoPK.empCalAndSumExecLogID = :empCalAndSumExecLogID "
			+ " AND c.krcdtErrMessageInfoPK.resourceID = :resourceID "
			+ " AND c.krcdtErrMessageInfoPK.executionContent = :executionContent "
			+ " AND c.krcdtErrMessageInfoPK.disposalDay = :disposalDay ";
	
	private ErrMessageInfo toDomain(KrcdtExecErrMsg entity) {
		return new ErrMessageInfo(
				entity.krcdtErrMessageInfoPK.employeeID,
				entity.krcdtErrMessageInfoPK.empCalAndSumExecLogID,
				new ErrMessageResource(entity.krcdtErrMessageInfoPK.resourceID),
				EnumAdaptor.valueOf( entity.krcdtErrMessageInfoPK.executionContent,ExecutionContent.class),
				entity.krcdtErrMessageInfoPK.disposalDay,
				new ErrMessageContent(entity.messageError)
				);
	}
	
	private KrcdtExecErrMsg toEntity(ErrMessageInfo errMessageInfo){
		val entity = new KrcdtExecErrMsg();
		
		entity.krcdtErrMessageInfoPK = new KrcdtErrMessageInfoPK();
		entity.krcdtErrMessageInfoPK.employeeID = errMessageInfo.getEmployeeID();
		entity.krcdtErrMessageInfoPK.empCalAndSumExecLogID = errMessageInfo.getEmpCalAndSumExecLogID();
		entity.krcdtErrMessageInfoPK.resourceID = errMessageInfo.getResourceID().v();
		entity.krcdtErrMessageInfoPK.disposalDay = errMessageInfo.getDisposalDay();
		entity.krcdtErrMessageInfoPK.executionContent = errMessageInfo.getExecutionContent().value;
		entity.messageError = errMessageInfo.getMessageError().v();
		
		return entity;
	}
	
	@Override
	public List<ErrMessageInfo> getAllErrMessageInfoByID(String empCalAndSumExecLogID,int executionContent) {
//		List<ErrMessageInfo> data = this.queryProxy().query(SELECT_ERR_MESSAGE_BYID , KrcdtExecErrMsg.class)
//				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID)
//				.setParameter("executionContent", executionContent)
//				.getList(c -> toDomain(c));
		List<ErrMessageInfo> data = new ArrayList<>();
		String sql = "select * from KRCDT_EXEC_ERR_MSG "
				+ " where EMP_EXECUTION_LOG_ID  = ?"
				+ " and EXECUTION_CONTENT = ?";
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
						
			stmt.setString(1, empCalAndSumExecLogID);
			stmt.setInt(2, executionContent);
			
			data = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				ErrMessageInfo ent = ErrMessageInfo.createFromJavaType(
						rec.getString("SID"), 
						rec.getString("EMP_EXECUTION_LOG_ID"), 
						rec.getString("RESOURCE_ID"), 
						rec.getInt("EXECUTION_CONTENT"), 
						rec.getGeneralDate("DISPOSAL_DAY"),
						rec.getString("MESSAGE_ERROR"));
				return ent;
			});
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return data;
	}

	@Override
	public List<ErrMessageInfo> getAllErrMessageInfoByEmpID(String empCalAndSumExecLogID) {
		List<ErrMessageInfo> data = this.queryProxy().query(SELECT_ERR_MESSAGE_BY_EMPID , KrcdtExecErrMsg.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID)
				.getList(c -> toDomain(c));
		return data;
	}

	@Override
	public List<ErrMessageInfo> getErrMessageInfoByExecutionContent(String empCalAndSumExecLogID, int executionContent) {
		List<ErrMessageInfo> data = this.queryProxy().query(SELECT_ERR_MESSAGE_BY_EXECONTEXT , KrcdtExecErrMsg.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID)
				.setParameter("executionContent", executionContent)
				.getList(c -> toDomain(c));
		return data;
	}
	
	@Override
	public void add(ErrMessageInfo errMessageInfo) {
		Optional<ErrMessageInfo> data = getErrMessageByID(errMessageInfo.getEmployeeID(),
				errMessageInfo.getEmpCalAndSumExecLogID(), errMessageInfo.getResourceID().v(),
				errMessageInfo.getExecutionContent().value, errMessageInfo.getDisposalDay());
		if (!data.isPresent() && errMessageInfo.getEmployeeID() != null ) {
			this.commandProxy().insert(toEntity(errMessageInfo));
			this.getEntityManager().flush();
		}
	}

	@Override
	public void addList(List<ErrMessageInfo> errMessageInfos) {
		errMessageInfos.forEach(f -> this.commandProxy().insert(toEntity(f)));		
	}

	@Override
	public Optional<ErrMessageInfo> getErrMessageByID(String employeeID, String empCalAndSumExecLogID,
			String resourceID, int executionContent, GeneralDate disposalDay) {
		Optional<ErrMessageInfo> data = this.queryProxy().query(SELECT_ERR_MESSAGE_BY_ALL_ID , KrcdtExecErrMsg.class)
		.setParameter("employeeID", employeeID)
		.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID)
		.setParameter("resourceID", resourceID)
		.setParameter("executionContent", executionContent)
		.setParameter("disposalDay", disposalDay)
		.getSingle(c->toDomain(c));
		return data;
		
	}

}
