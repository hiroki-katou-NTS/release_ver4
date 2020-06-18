package nts.uk.ctx.workflow.dom.resultrecord.status.internal;

import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.gul.collection.ListHashMap;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;

public class RouteInstancesMap {

	private final Map<String, List<AppRootInstance>> mapSubject;
	private final Map<String, List<AppRootInstance>> mapRepresent;
	
	@Getter
	private final List<String> actualTargetEmployeeIds;
	
	public RouteInstancesMap(List<AppRootInstance> instancesSubject, List<AppRootInstance> instancesRepresent) {
		
		// 主体社員が承認者となっているインスタンス
		mapSubject = ListHashMap.create(instancesSubject, i -> i.getEmployeeID());
		
		// システム日付時点で主体社員に代行依頼している承認者達のインスタンス
		mapRepresent = ListHashMap.create(instancesRepresent, i -> i.getEmployeeID());
		
		// 指定されたtargetEmployeeIdsのうち、実際に承認対象となっている社員
		actualTargetEmployeeIds = Stream.concat(
				instancesSubject.stream().map(i -> i.getEmployeeID()),
				instancesRepresent.stream().map(i -> i.getEmployeeID()))
				.collect(toList());
	}
	
	public Optional<AppRootInstance> getInstance(String employeeId, GeneralDate date) {
		
		// 承認者自身のものがあればそれを返す
		Optional<AppRootInstance> result = mapSubject.getOrDefault(employeeId, Collections.emptyList()).stream()
				.filter(i -> i.getDatePeriod().contains(date))
				.findFirst();
		if (result.isPresent()) {
			return result;
		}
		
		// 無ければ代行依頼者のものを探す
		return mapRepresent.getOrDefault(employeeId, Collections.emptyList()).stream()
				.filter(i -> i.getDatePeriod().contains(date))
				.findFirst();
	}
	
}
