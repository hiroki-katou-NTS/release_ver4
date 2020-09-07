/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.optitem.calculation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.dailyattdcal.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.monthlyprocess.aggr.converter.MonthlyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.optitem.PerformanceAtr;

/**
 * The Class CalculationItemSelection.
 */
// 計算式設定
@Getter
public class ItemSelection extends DomainObject {

	/** The minus segment. */
	// マイナス区分
	private MinusSegment minusSegment;

	/** The selected attendance items. */
	// 選択勤怠項目
	private List<SelectedAttendanceItem> selectedAttendanceItems;

	/**
	 * Instantiates a new item selection.
	 *
	 * @param memento the memento
	 */
	public ItemSelection(ItemSelectionGetMemento memento) {
		this.minusSegment = memento.getMinusSegment();
		this.selectedAttendanceItems = memento.getListSelectedAttendanceItem();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(ItemSelectionSetMemento memento) {
		memento.setMinusSegment(this.minusSegment);
		memento.setListSelectedAttendanceItem(this.selectedAttendanceItems);
	}
	
	
	/**
	 * 項目選択による計算
	 * @param dailyRecordDto
	 * @return
	 */
	public BigDecimal calculationByItemSelection(PerformanceAtr performanceAtr,Optional<DailyRecordToAttendanceItemConverter> dailyRecordDto,Optional<MonthlyRecordToAttendanceItemConverter> monthlyRecordDto) {
		//計算値
		BigDecimal result = BigDecimal.ZERO;
		if(performanceAtr.isDailyPerformance()&&dailyRecordDto.isPresent()) {//実績区分が日別実績の場合
			for(SelectedAttendanceItem selectedAttendanceItem:this.selectedAttendanceItems) {//選択勤怠項目分ループ
				//該当する勤怠項目を取得
				Optional<ItemValue> itemValue = dailyRecordDto.get().convert(selectedAttendanceItem.getAttendanceItemId());
				if(itemValue.isPresent()) {
					result = selectedAttendanceItem.calc(itemValue.get(), result);
				}
			}
			//マイナスかどうか
			if(result.compareTo(BigDecimal.ZERO) < 0) {
				if(this.minusSegment.isTreatedAsZero()) {
					return BigDecimal.ZERO;
				}
			}
			return result;
		}
		if(performanceAtr.isMonthlyPerformance()&&monthlyRecordDto.isPresent()){//実績区分が月別実績の場合
			for(SelectedAttendanceItem selectedAttendanceItem:this.selectedAttendanceItems) {//選択勤怠項目分ループ
				//該当する勤怠項目を取得
				Optional<ItemValue> itemValue = monthlyRecordDto.get().convert(selectedAttendanceItem.getAttendanceItemId());
				if(itemValue.isPresent()) {
					result = selectedAttendanceItem.calc(itemValue.get(), result);
				}
			}
			//マイナスかどうか
			if(result.compareTo(BigDecimal.ZERO) < 0) {
				if(this.minusSegment.isTreatedAsZero()) {
					return BigDecimal.ZERO;
				}
			}
			return result;
		}
		return result;
	}
	
	
}
