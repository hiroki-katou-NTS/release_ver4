package nts.uk.ctx.at.record.infra.repository.log;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtErrMessageInfo;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtErrMessageInfoPK;
@Stateless
public class JpaErrMessageInfoRepository extends JpaRepository implements ErrMessageInfoRepository {

	private static final String SELECT_FROM_ERR_MESSAGE = "SELECT c FROM KrcdtErrMessageInfo c ";
	
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
	
	private ErrMessageInfo toDomain(KrcdtErrMessageInfo entity) {
		return new ErrMessageInfo(
				entity.krcdtErrMessageInfoPK.employeeID,
				entity.krcdtErrMessageInfoPK.empCalAndSumExecLogID,
				new ErrMessageResource(entity.krcdtErrMessageInfoPK.resourceID),
				EnumAdaptor.valueOf( entity.krcdtErrMessageInfoPK.executionContent,ExecutionContent.class),
				entity.krcdtErrMessageInfoPK.disposalDay,
				new ErrMessageContent(entity.messageError)
				);
	}
	
	private KrcdtErrMessageInfo toEntity(ErrMessageInfo errMessageInfo){
		val entity = new KrcdtErrMessageInfo();
		
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
		List<ErrMessageInfo> data = this.queryProxy().query(SELECT_ERR_MESSAGE_BYID , KrcdtErrMessageInfo.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID)
				.setParameter("executionContent", executionContent)
				.getList(c -> toDomain(c));
		return data;
	}

	@Override
	public List<ErrMessageInfo> getAllErrMessageInfoByEmpID(String empCalAndSumExecLogID) {
		List<ErrMessageInfo> data = this.queryProxy().query(SELECT_ERR_MESSAGE_BY_EMPID , KrcdtErrMessageInfo.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID)
				.getList(c -> toDomain(c));
		return data;
	}

	@Override
	public List<ErrMessageInfo> getErrMessageInfoByExecutionContent(String empCalAndSumExecLogID, int executionContent) {
		List<ErrMessageInfo> data = this.queryProxy().query(SELECT_ERR_MESSAGE_BY_EXECONTEXT , KrcdtErrMessageInfo.class)
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
		Optional<ErrMessageInfo> data = this.queryProxy().query(SELECT_ERR_MESSAGE_BY_ALL_ID , KrcdtErrMessageInfo.class)
		.setParameter("employeeID", employeeID)
		.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID)
		.setParameter("resourceID", resourceID)
		.setParameter("executionContent", executionContent)
		.setParameter("disposalDay", disposalDay)
		.getSingle(c->toDomain(c));
		return data;
		
	}

}
