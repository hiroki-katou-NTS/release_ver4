package nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult;

import java.util.List;
import java.util.Optional;

public interface AlarmListExtractResultRepo {

	List<AlarmListExtractResult> findBy(String compId, String executeEmpId, ExtractExecuteType type);
	
	Optional<AlarmListExtractResult> findByLimited(String compId, String executeEmpId, ExtractExecuteType type, String executeId, int limitedWith);
	
	Optional<AlarmListExtractResult> findBy(String compId, String executeEmpId, ExtractExecuteType type, String executeId);
	
	Optional<AlarmListExtractResult> findResultEmpInfo(String compId, String executeEmpId, ExtractExecuteType type, String executeId);
	
	void insert(List<AlarmListExtractResult> result);
}
