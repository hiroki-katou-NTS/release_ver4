package nts.uk.ctx.at.record.pubimp.workrecord.erroralarm.condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.management.RuntimeErrorException;

import nts.arc.enums.EnumAdaptor;
import nts.gul.text.IdentifierUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmConditionRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.DisplayMessages;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.ErrorAlarmCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkRecordExtraConRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkRecordExtractingCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.TypeCheckWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ColorCode;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.NameWKRecord;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.MessageWRExtraConPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.WorkRecordExtraConPub;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.WorkRecordExtraConPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.ErAlAtdItemConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.ErrorAlarmConditionPubExport;

@Stateless
public class WorkRecordExtraConPubImpl implements WorkRecordExtraConPub {

	@Inject
	private WorkRecordExtraConRepository repo;
	
	@Inject
    private ErrorAlarmConditionRepository errorAlarmConditionRepository;
	
	@Override
	public List<WorkRecordExtraConPubExport> getAllWorkRecordExtraConByListID(List<String> listErrorAlarmID) {
		List<WorkRecordExtraConPubExport> data = repo.getAllWorkRecordExtraConByListID(listErrorAlarmID)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
		if(data.isEmpty())
			return Collections.emptyList();
		// get List ErrorAlarmCondition
		List<ErrorAlarmCondition> listErrorAlarmCondition = errorAlarmConditionRepository.findConditionByListErrorAlamCheckId(listErrorAlarmID);
		data = data.stream().map(item -> setErrorAlarmConditionPubExport(item, listErrorAlarmCondition)).collect(Collectors.toList());
		return data;
	}

	@Override
	public WorkRecordExtraConPubExport getWorkRecordExtraConById(String errorAlarmCheckID) {
		Optional<WorkRecordExtraConPubExport> data = repo.getWorkRecordExtraConById(errorAlarmCheckID).map(c->convertToExport(c));
		if(data.isPresent())
			return data.get();
		return null;
	}
	
	public WorkRecordExtraConPubExport convertToExport(WorkRecordExtractingCondition domain) {
		return new WorkRecordExtraConPubExport(
					domain.getErrorAlarmCheckID(),
					domain.getCheckItem().value,
					domain.getDisplayMessages().isMessageBold(),
					domain.getDisplayMessages().getMessageColor().v(),
					domain.getSortOrderBy(),
					domain.isUseAtr(),
					domain.getNameWKRecord().v(),
					null
				);
	}
	
	private WorkRecordExtractingCondition convertToDomainWR(WorkRecordExtraConPubExport dto) {
		return new  WorkRecordExtractingCondition(
				dto.getErrorAlarmCheckID(),
				EnumAdaptor.valueOf(dto.getCheckItem(), TypeCheckWorkRecord.class),
				new DisplayMessages(
						dto.isMessageBold(),
						new ColorCode(dto.getMessageColor())),
				dto.getSortOrderBy(),
				dto.isUseAtr(),
				new NameWKRecord(dto.getNameWKRecord())
				);
	}

	private WorkRecordExtraConPubExport setErrorAlarmConditionPubExport(WorkRecordExtraConPubExport workRecordExtraConPubExport,
			List<ErrorAlarmCondition> listErrorAlarmCondition) {
		workRecordExtraConPubExport.setErrorAlarmCondition(getErrorAlarmCondition(
				workRecordExtraConPubExport.getErrorAlarmCheckID(), listErrorAlarmCondition));
		return workRecordExtraConPubExport;
	}
	private ErrorAlarmConditionPubExport getErrorAlarmCondition(String eralCheckId, List<ErrorAlarmCondition> listErrorAlarmCondition) {
		Optional<ErrorAlarmCondition> optErAlCondition = 
				listErrorAlarmCondition.stream().filter(item -> item.getErrorAlarmCheckID().equals(eralCheckId)).findFirst();
        if (!optErAlCondition.isPresent()) {
//        	throw new RuntimeErrorException(new Error(), "ErrorAlarmCondition is invalid!");
        	return null;
        }
      //ドメインモデル「勤怠項目に対する条件」を取得する - Acquire domain model "Condition for attendance item"
        ErrorAlarmCondition errorAlarmCondition = optErAlCondition.get();
       
    	ErrorAlarmConditionPubExport errorAlarmConditionPubExport = new ErrorAlarmConditionPubExport();
    	errorAlarmConditionPubExport.convertFromDomain(errorAlarmCondition);
        return errorAlarmConditionPubExport;
	}

	@Override
	public void addWorkRecordExtraConPub(WorkRecordExtraConPubExport workRecordExtraConPubExport) {
		
		String errorAlarmCheckId = IdentifierUtil.randomUniqueId();
		
		ErrorAlarmConditionPubExport errorAlarmConditionPubExport = workRecordExtraConPubExport.getErrorAlarmCondition();
		
		// for ErrorAlarmCondition
		errorAlarmConditionPubExport.setErrorAlarmCheckID(errorAlarmCheckId);
		ErrorAlarmCondition errorAlarmCondition = errorAlarmConditionPubExport.toConditionDomain();
		
		workRecordExtraConPubExport.setErrorAlarmCheckID(errorAlarmCheckId);
		
		this.setIndex(errorAlarmCondition);
		this.errorAlarmConditionRepository.addErrorAlarmCondition(errorAlarmCondition);
		
		
		this.repo.addWorkRecordExtraCon(
				convertToDomainWR(workRecordExtraConPubExport)
				);
	}

	@Override
	public void updateWorkRecordExtraConPub(WorkRecordExtraConPubExport workRecordExtraConPubExport) {
		ErrorAlarmConditionPubExport errorAlarmConditionPubExport = workRecordExtraConPubExport.getErrorAlarmCondition();
		
		int checkItem = workRecordExtraConPubExport.getCheckItem();

		Optional<ErrorAlarmCondition> optErrorAlarmCondition = errorAlarmConditionRepository.findConditionByErrorAlamCheckId(workRecordExtraConPubExport.getErrorAlarmCheckID());
		if (!optErrorAlarmCondition.isPresent()) {
			throw new RuntimeErrorException(new Error(), "Update but the ErrorAlarmCondition doesn't exist!!!. ErrorAlarmCheckID: " + workRecordExtraConPubExport.getErrorAlarmCheckID());
		}
		
		ErrorAlarmCondition errorAlarmCondition = errorAlarmConditionPubExport.toConditionDomain();
		ErrorAlarmCondition updateErrorAlarmCondition = optErrorAlarmCondition.get();
		//update data
		updateErrorAlarmCondition.setByCheckItem(checkItem, errorAlarmCondition);
		
		//set index an isUse group1
		this.setIndex(updateErrorAlarmCondition);
		
		this.errorAlarmConditionRepository.updateErrorAlarmCondition(updateErrorAlarmCondition);
		this.repo.updateWorkRecordExtraCon(convertToDomainWR(workRecordExtraConPubExport));
	}
	
	private  void setIndex(ErrorAlarmCondition updateErrorAlarmCondition){
		List<ErAlAttendanceItemCondition<?>> lstErAlAtdItemCon = updateErrorAlarmCondition.getAtdItemCondition().getGroup1().getLstErAlAtdItemCon();
		int size = lstErAlAtdItemCon.size();
		for (int i = 0; i < size; i++) {
			ErAlAttendanceItemCondition<?> erAlAttendanceItemCondition = lstErAlAtdItemCon.get(i);
			erAlAttendanceItemCondition.setTargetNO(i);
			erAlAttendanceItemCondition.setUseAtr(true);
		}
		List<ErAlAttendanceItemCondition<?>> lstErAlAtdItemCon2 = updateErrorAlarmCondition.getAtdItemCondition().getGroup2().getLstErAlAtdItemCon();
		int size2 = lstErAlAtdItemCon2.size();
		for (int i = 0; i < size2; i++) {
			ErAlAttendanceItemCondition<?> erAlAttendanceItemCondition = lstErAlAtdItemCon2.get(i);
			erAlAttendanceItemCondition.setTargetNO(i);
			erAlAttendanceItemCondition.setUseAtr(true);
		}
	}

	@Override
	public void deleteWorkRecordExtraConPub(List<String> lstErrorAlarmCheckID) {
		// for ErrorAlarmCondition
		if (lstErrorAlarmCheckID != null && !lstErrorAlarmCheckID.isEmpty()) {
			this.errorAlarmConditionRepository.removeErrorAlarmCondition(lstErrorAlarmCheckID);
			for(String eralId : lstErrorAlarmCheckID) {
				this.repo.deleteWorkRecordExtraCon(eralId);
			}
		}
	}
	
	@Override
	public List<String> checkUpdateListErAl(List<String> oldlstErrorAlarmCheckID, List<WorkRecordExtraConPubExport> listErroAlarm) {
		//get list gốc theo list Error AlarmCheckID
		List<WorkRecordExtraConPubExport> oldData = getAllWorkRecordExtraConByListID(oldlstErrorAlarmCheckID);
		List<String> newListCheckIds = listErroAlarm.stream().filter(em -> !StringUtil.isNullOrEmpty(em.getErrorAlarmCheckID(), true))
				.map(item -> item.getErrorAlarmCheckID()).collect(Collectors.toList());
		//ktra xem có xóa phần tử nào k?
		//lặp list gốc
		List<String> listErrorAlarmCheckID = new ArrayList<>();
		
		List<String> listDeleteErrorAlarmID = oldData.stream().filter(item -> !isExistCheckIdInList(item.getErrorAlarmCheckID(),
				newListCheckIds)).map(id -> id.getErrorAlarmCheckID()).collect(Collectors.toList());
		//nếu list id có dữ liệu, tức có phần tử bị xóa đy, gọi hàm xóa
		if(!listDeleteErrorAlarmID.isEmpty()) {
			deleteWorkRecordExtraConPub(listDeleteErrorAlarmID);
		}
		
		//ktra xem có add và update phần tử nào k?
		//lặp list truyền vào
		for(WorkRecordExtraConPubExport newWorkRecordExtraCon :listErroAlarm) {
			//check tồn tại
			boolean checkExist1 = false;
			//lặp list gốc
			for(WorkRecordExtraConPubExport oldWorkRecordExtraCon : oldData) {
				if(newWorkRecordExtraCon.getErrorAlarmCheckID().equals(oldWorkRecordExtraCon.getErrorAlarmCheckID())) {
					//if(!newWorkRecordExtraCon.equals(oldWorkRecordExtraCon)) {
						updateWorkRecordExtraConPub(newWorkRecordExtraCon);
					//}
					checkExist1 = true;
					listErrorAlarmCheckID.add(newWorkRecordExtraCon.getErrorAlarmCheckID());
					break;
				}
			}//end lặp list gốc
			//nếu checkExist1 = false, tức là phần tử thêm mới, gọi hàm thêm mới
			if(!checkExist1) {
				addWorkRecordExtraConPub(newWorkRecordExtraCon);
				listErrorAlarmCheckID.add(newWorkRecordExtraCon.getErrorAlarmCheckID());
			}
		}//end lặp list truyền vào
		return listErrorAlarmCheckID;
	}

	@Override
	public List<String> addNewListErAl(List<WorkRecordExtraConPubExport> listErroAlarm) {
		List<String> listErrorAlarmCheckID = new ArrayList<>();
		for(WorkRecordExtraConPubExport workRecordExtraCon :listErroAlarm) {
			addWorkRecordExtraConPub(workRecordExtraCon);
			listErrorAlarmCheckID.add(workRecordExtraCon.getErrorAlarmCheckID());
		}
		return listErrorAlarmCheckID;
	}
	
	private boolean isExistCheckIdInList(String checkId, List<String> listCheckIds) {
		Optional<String> optCheckId = listCheckIds.stream().filter(item->item.equals(checkId)).findFirst();
		return optCheckId.isPresent();
	}

	@Override
	public List<MessageWRExtraConPubExport> getMessageWRExtraConByListID(List<String> listErrorAlarmID) {
		return errorAlarmConditionRepository.findMessageConByListErAlCheckId(listErrorAlarmID).stream()
				.map(c->convertToErAlCon(c)).collect(Collectors.toList());
	}

	private MessageWRExtraConPubExport convertToErAlCon(ErrorAlarmCondition errorAlarmCondition) {
		return new MessageWRExtraConPubExport(
				errorAlarmCondition.getErrorAlarmCheckID(),
				errorAlarmCondition.getDisplayMessage().v()
				);
	}
}
