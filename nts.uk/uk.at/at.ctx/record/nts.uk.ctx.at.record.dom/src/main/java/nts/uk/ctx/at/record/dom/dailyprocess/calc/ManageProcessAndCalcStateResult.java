package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;

/**
 * 計算ステータス、中断ステータス、実績を管理するクラス
 * @author keisuke_hoshina
 *
 */
@AllArgsConstructor
@Getter
public class ManageProcessAndCalcStateResult {
		//中断ステータス
		ProcessState ps;
		//計算ステータス&実績
		List<ManageCalcStateAndResult> lst;
		
		protected List<IntegrationOfDaily> getIntegrationList() {
			return this.lst.stream().map(tg -> tg.integrationOfDaily).collect(Collectors.toList());
		}
}
