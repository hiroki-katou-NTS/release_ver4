package nts.uk.ctx.at.request.infra.repository.overtimeinstruct;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.overtimeinstruct.OverTimeInstruct;
import nts.uk.ctx.at.request.dom.overtimeinstruct.OvertimeInstructRepository;
import nts.uk.ctx.at.request.infra.entity.overtimeinstruct.KrqdtOvertimeInstruct;

@Stateless
public class JpaOvertimeInstructRepository extends JpaRepository implements OvertimeInstructRepository {
	private static final String FIND_ALL;
	private static final String FIND_FOR_TARGET_PERSON;
	private static final String FIND_ALL_BY_TARGET_PERSON;
	
	static{
		StringBuilder query = new StringBuilder();
		query.append("Select o from KrqdtOvertimeInstruct o");
		FIND_ALL = query.toString();
		
		query = new StringBuilder();
		query.append(FIND_ALL);
		query.append(" WHERE o.krqdtOvertimeInstructPK.instructDate = :instructDate");
		query.append(" AND o.krqdtOvertimeInstructPK.targetPerson = :targetPerson");
		FIND_FOR_TARGET_PERSON = query.toString();
		
		query = new StringBuilder();
		query.append(FIND_ALL);
		query.append(" AND o.krqdtOvertimeInstructPK.targetPerson = :targetPerson");
		FIND_ALL_BY_TARGET_PERSON = query.toString();
		
	}

	@Override
	public OverTimeInstruct getOvertimeInstruct(GeneralDate instructDate, String targetPerson) {
		
		List<OverTimeInstruct> overTimeInstructs = this.queryProxy().query(FIND_FOR_TARGET_PERSON,KrqdtOvertimeInstruct.class)
		.setParameter("instructDate", instructDate.date())
		.setParameter("targetPerson", targetPerson).getList(c -> convertToDomain(c));
		if(overTimeInstructs != null){
			if(overTimeInstructs.size() > 0){
				return overTimeInstructs.get(0);
			}
		}
		return null;
	}
	private OverTimeInstruct convertToDomain(KrqdtOvertimeInstruct krqdtOvertimeInstruct){
		 return OverTimeInstruct.createSimpleFromJavaType(krqdtOvertimeInstruct.getKrqdtOvertimeInstructPK().getCid(),
				krqdtOvertimeInstruct.getWorkContent(),
				krqdtOvertimeInstruct.getInputDate(),
				krqdtOvertimeInstruct.getKrqdtOvertimeInstructPK().getTargetPerson(),
				krqdtOvertimeInstruct.getKrqdtOvertimeInstructPK().getInstructDate(),
				krqdtOvertimeInstruct.getInstructor(),
				krqdtOvertimeInstruct.getOvertimeInstructReason(),
				krqdtOvertimeInstruct.getOvertimeHour(),
				krqdtOvertimeInstruct.getStartClock(),
				krqdtOvertimeInstruct.getEndClock());
		
		
	}
	
	/**
	 * For request list 230
	 */
	@Override
	public List<OverTimeInstruct> getAllOverTimeInstructBySId(String sId) {
		List<OverTimeInstruct> overTimeInstructs = this.queryProxy().query(FIND_ALL_BY_TARGET_PERSON, KrqdtOvertimeInstruct.class)
				.setParameter("targetPerson", sId).getList(c -> convertToDomain(c));
		
		if(overTimeInstructs != null){
			if(overTimeInstructs.size() > 0){
				return overTimeInstructs;
			}
		}
		
		return null;
	}

}
