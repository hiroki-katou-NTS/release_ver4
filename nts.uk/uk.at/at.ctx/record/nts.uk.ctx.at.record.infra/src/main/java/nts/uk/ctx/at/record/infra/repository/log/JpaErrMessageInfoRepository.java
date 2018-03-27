package nts.uk.ctx.at.record.infra.repository.log;

import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtErrMessageInfo;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtErrMessageInfoPK;
@Stateless
public class JpaErrMessageInfoRepository extends JpaRepository implements ErrMessageInfoRepository {

	private final String SELECT_FROM_ERR_MESSAGE = "SELECT c FROM KrcdtErrMessageInfo c ";
	
	private final String SELECT_ERR_MESSAGE_BY_EMPID = SELECT_FROM_ERR_MESSAGE
			+ " WHERE c.krcdtErrMessageInfoPK.empCalAndSumExecLogID = :empCalAndSumExecLogID ";
	
	private final String SELECT_ERR_MESSAGE_BY_EXECONTEXT = SELECT_FROM_ERR_MESSAGE
			+ " WHERE c.krcdtErrMessageInfoPK.empCalAndSumExecLogID = :empCalAndSumExecLogID"
			+ " AND c.krcdtErrMessageInfoPK.executionContent = :executionContent";
	
	private final String SELECT_ERR_MESSAGE_BYID = SELECT_ERR_MESSAGE_BY_EMPID
			+ " AND c.krcdtErrMessageInfoPK.executionContent = :executionContent ";
	
	
	
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
		this.commandProxy().insert(toEntity(errMessageInfo));
		this.getEntityManager().flush();
	}

	@Override
	public void addList(List<ErrMessageInfo> errMessageInfos) {
		errMessageInfos.forEach(f -> this.commandProxy().insert(toEntity(f)));		
	}

}
