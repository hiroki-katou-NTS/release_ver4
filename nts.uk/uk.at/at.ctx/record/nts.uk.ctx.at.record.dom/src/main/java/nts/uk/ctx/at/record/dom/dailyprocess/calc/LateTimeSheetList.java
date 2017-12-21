package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.val;


/**
 * 遅刻時間帯のリストを管理するクラス
 * EA上には表れていない内部処理用のクラスです
 * @author ken_takasu
 *
 */
public class LateTimeSheetList {

	private List<LateTimeSheet> timeSheets = new ArrayList<>();
	
	public LateTimeSheetList(List<LateTimeSheet> lateTimeSheets) {
		this.timeSheets.addAll(lateTimeSheets);
	}
	
	/**
	 * 遅刻時間帯Listを結合し、遅刻時間帯を返す
	 * @author ken_takasu
	 * @return
	 */
	public Optional<LateTimeSheet> join() {
		if (timeSheets.isEmpty()) {
			return Optional.empty();
		}
		
		//勤務No
		val workNo = this.timeSheets.stream().findAny().get().getWorkNo();
		if (!this.timeSheets.stream().allMatch(s -> s.getWorkNo().equals(workNo))) {
			throw new RuntimeException("不正な勤務Noです");
		}
		
		//計上用時間帯
		val recordsTimeSheet = this.joinRecords();
		
		//控除用時間帯
		val deductionsTimeSheet = this.joinDeductions();
		
		return Optional.of(LateTimeSheet.createAsLate(recordsTimeSheet, deductionsTimeSheet, workNo));
		
	}
	
	/**
	 * 計上用時間帯（List）を1つの計上用時間帯にまとめる
	 * @author ken_takasu
	 * @return
	 */
	private LateLeaveEarlyTimeSheet joinRecords() {
		List<LateLeaveEarlyTimeSheet> list = timeSheets.stream()
				.map(s -> s.getForRecordTimeSheet().get())
				.collect(Collectors.toList());
		return LateLeaveEarlyTimeSheet.joinedLateLeaveEarlyTimeSheet(list);
	}
	
	/**
	 * 控除用時間帯（List）を1つの控除用時間帯にまとめる
	 * @author ken_takasu
	 * @return
	 */
	private LateLeaveEarlyTimeSheet joinDeductions() {
		List<LateLeaveEarlyTimeSheet> list = timeSheets.stream()
				.map(s -> s.getForDeducationTimeSheet().get())
				.collect(Collectors.toList());
		return LateLeaveEarlyTimeSheet.joinedLateLeaveEarlyTimeSheet(list);
	}
}
