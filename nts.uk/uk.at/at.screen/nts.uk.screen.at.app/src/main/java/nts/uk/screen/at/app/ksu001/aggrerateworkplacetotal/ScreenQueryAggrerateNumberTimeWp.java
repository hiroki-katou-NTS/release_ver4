package nts.uk.screen.at.app.ksu001.aggrerateworkplacetotal;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.aggregationprocess.TotalTimesCounterService;
import nts.uk.ctx.at.shared.app.find.scherec.totaltimes.dto.TotalTimesDetailDto;
import nts.uk.ctx.at.shared.dom.common.EmployeeId;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.service.AttendanceItemConvertFactory;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimes;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesRepository;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.UseAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.screen.at.app.kml002.screenG.CountInfoDto;
import nts.uk.screen.at.app.kml002.screenG.CountInfoProcessor;
import nts.uk.screen.at.app.kml002.screenG.NumberOfTimeTotalDto;
import nts.uk.screen.at.app.kml002.screenG.RequestPrams;
import nts.uk.shr.com.context.AppContexts;

/**
 * 回数集計を集計する
 * @author hoangnd
 *
 */
@Stateless
public class ScreenQueryAggrerateNumberTimeWp {
	
	@Inject
	private CountInfoProcessor countInfoProcessor;
	
	@Inject
	private TotalTimesRepository totalTimeRepository;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private AttendanceItemConvertFactory converterFactory;
	
	
	public Map<String, Map<String, BigDecimal>> aggrerate(
			List<IntegrationOfDaily> aggrerateintegrationOfDaily
			) {
		
		Require require = new Require(totalTimeRepository, workTypeRepository, converterFactory); 
		//1: 取得する(回数集計種類)
		Optional<CountInfoDto> countInfoOp = Optional.ofNullable(countInfoProcessor.getInfo(new RequestPrams(0)));
		
		// 2: Optional<回数集計選択>.isPresent
		if (countInfoOp.isPresent()) {
			

			// 2.1: 社員別に集計する(Require, List<回数集計NO>, List<日別勤怠(Work)>)
			Map<EmployeeId, Map<Integer, BigDecimal>> countTotalTime = 
					TotalTimesCounterService.countingNumberOfTotalTimeByEmployee(
								require,
								CollectionUtil.isEmpty(countInfoOp.get().getNumberOfTimeTotalDtos()) ? Collections.emptyList() : 
									Arrays.asList(new Integer[] {countInfoOp.get().getNumberOfTimeTotalDtos().get(0).getNumber()})  // 集計対象の回数集計 = 1で取得した「回数集計選択」．選択した項目リスト
									  .stream()
									  .collect(Collectors.toList()), 
								aggrerateintegrationOfDaily // 日別勤怠リスト = Input．List<日別勤怠(Work)>
								);
			
			// 2.2: 
			List<TotalTimesDetailDto> totalTimes = 
					totalTimeRepository.getTotalTimesDetailByListNo(AppContexts.user().companyId(),
						countInfoOp.get()
						   .getNumberOfTimeTotalDtos()
						   .stream()
						   .map(NumberOfTimeTotalDto::getNumber)
						   .collect(Collectors.toList())
						)
						.stream()
						.filter(x -> x.getUseAtr() == UseAtr.Use)
						.map(x -> {
							TotalTimesDetailDto totalTimesDetailDto = new TotalTimesDetailDto();
							totalTimesDetailDto.setTotalCountNo(x.getTotalCountNo());
							totalTimesDetailDto.setTotalTimesName(x.getTotalTimesName());
							return totalTimesDetailDto;
						})
						.collect(Collectors.toList());
			
			return countTotalTime.entrySet()
					      .stream()
					      .collect(Collectors.toMap(
					    		  e -> e.getKey().v(),
					    		  e -> e.getValue().entrySet()
					    		  				   .stream()
					    		  				   .collect(Collectors.toMap(
					    		  						   x -> totalTimes.stream()
					    		  						   				  .filter(y -> y.getTotalCountNo() == x.getKey())
					    		  						   				  .findFirst().map(z -> z.getTotalTimesName()).orElse(null),
					    		  						   x -> x.getValue()
			    		  						   ))
					    		  
			    		  ));
			
		}
		
		
		return new HashMap<String, Map<String, BigDecimal>>();
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	private static class Require implements TotalTimesCounterService.Require {
		
		@Inject
		private TotalTimesRepository totalTimesRepository;
		
		@Inject
		private WorkTypeRepository workTypeRepository;
		
		@Inject
		private AttendanceItemConvertFactory converterFactory;
		
		@Override
		public DailyRecordToAttendanceItemConverter createDailyConverter() {
			return converterFactory.createDailyConverter();
		}

		@Override
		public Optional<WorkType> workType(String companyId, String workTypeCd) {
			return workTypeRepository.findByPK(companyId, workTypeCd);
		}

		@Override
		public List<TotalTimes> getTotalTimesList(List<Integer> totalTimeNos) {
			return totalTimesRepository.getTotalTimesDetailByListNo(AppContexts.user().companyId(), totalTimeNos);
		}
		
	}
}
