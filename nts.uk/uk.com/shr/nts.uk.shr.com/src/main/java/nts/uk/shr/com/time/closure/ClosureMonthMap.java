package nts.uk.shr.com.time.closure;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.gul.collection.ListHashMap;

public class ClosureMonthMap {
	
	private final ListHashMap<String, ClosureMonth> map = new ListHashMap<>();
	
	public void add(String employeeId, List<ClosureMonth> closureMonths) {
		map.put(employeeId, closureMonths);
	}
	
	public void add(String employeeId, ClosureMonth closureMonth) {
		map.addElement(employeeId, closureMonth);
	}

	public Optional<ClosureMonth> get(String employeeId, GeneralDate date) {
		return Optional.ofNullable(map.get(employeeId))
				.flatMap(list -> list.stream()
						.filter(cm -> cm.period().contains(date))
						.findFirst());
	}
	
	public List<ClosureMonth> get(String employeeId) {
		return map.getOrDefault(employeeId, Collections.emptyList());
	}
}
