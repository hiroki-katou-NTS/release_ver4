package nts.uk.ctx.at.shared.dom.dailyattdcal.converter;

// 日別のコンバーターを作成する
public interface DailyRecordConverter {
	DailyRecordToAttendanceItemConverter createDailyConverter();
}
