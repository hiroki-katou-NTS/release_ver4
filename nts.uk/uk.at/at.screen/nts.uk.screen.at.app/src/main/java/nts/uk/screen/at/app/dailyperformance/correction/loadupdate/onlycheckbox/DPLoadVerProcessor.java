package nts.uk.screen.at.app.dailyperformance.correction.loadupdate.onlycheckbox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQueryProcessor;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyResult;
import nts.uk.screen.at.app.dailyperformance.correction.GetDataDaily;
import nts.uk.screen.at.app.dailyperformance.correction.dto.EmpAndDate;

@Stateless
public class DPLoadVerProcessor {

	@Inject
	private DailyModifyQueryProcessor dailyModifyQueryProcessor;

	public LoadVerDataResult loadVerAfterCheckbox(LoadVerData loadVerData) {
		LoadVerDataResult result = new LoadVerDataResult();
		List<EmpAndDate> lstDataChange = loadVerData.getLstDataChange();

		Map<String, List<GeneralDate>> mapDate = lstDataChange.stream()
				.collect(Collectors.groupingBy(x -> x.getEmployeeId(), Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().map(c -> c.getDate()).collect(Collectors.toList()))));
		
		Pair<List<DailyModifyResult>, List<DailyRecordDto>> resultPair = new GetDataDaily(mapDate,
				dailyModifyQueryProcessor).getDataRow();
		// List<DailyRecordDto> dailyRow = resultPair.getRight();
		Map<Pair<String, GeneralDate>, DailyRecordDto> mapDtoChange = resultPair.getRight().stream()
				.collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> x));
		List<Pair<String, GeneralDate>> lstPairChange = lstDataChange.stream().map(x -> Pair.of(x.getEmployeeId(), x.getDate())).collect(Collectors.toList());
		result.setLstDomainOld(loadVerData.getLstDomainOld().stream().map(x -> {
			return lstPairChange.contains(Pair.of(x.getEmployeeId(), x.getDate()))
					? mapDtoChange.getOrDefault(Pair.of(x.getEmployeeId(), x.getDate()), x)
					: x;
		}).collect(Collectors.toList()));
		return result;
	}
}
