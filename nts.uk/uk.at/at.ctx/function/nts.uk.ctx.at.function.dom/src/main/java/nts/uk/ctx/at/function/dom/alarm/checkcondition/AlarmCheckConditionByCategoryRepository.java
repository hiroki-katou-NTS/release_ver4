package nts.uk.ctx.at.function.dom.alarm.checkcondition;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author HungTT
 *
 */
public interface AlarmCheckConditionByCategoryRepository {
	
	public Optional<AlarmCheckConditionByCategory> find(String companyId, int category, String code);

	public List<AlarmCheckConditionByCategory> findAll(String companyId);

	public List<AlarmCheckConditionByCategory> findByCategory(String companyId, int category);

	public void add(AlarmCheckConditionByCategory domain);

	public void update(AlarmCheckConditionByCategory domain);

	public void delete(String companyId, int category, String alarmConditionCode);
	
	public boolean isCodeExist(String companyId, int category, String code);
	
	public List<AlarmCheckConditionByCategory> findByCategoryAndCode(String companyId, int category, List<String> codes);
	
	public List<AlarmCheckConditionByCategory> findByCategoryAndCode(String companyId, List<Integer> categories, List<String> codes);
	
}
