package nts.uk.ctx.at.function.dom.adapter.worklocation;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;

public interface RecordWorkInfoFunAdapter {
	public Optional<RecordWorkInfoFunAdapterDto>  getInfoCheckNotRegister(String employeeId, GeneralDate ymd);
	
	public List<RecordWorkInfoFunAdapterDto> findByEmpAndPeriod(String employeeId, DatePeriod datePeriod);
	
	public List<WorkInfoOfDailyPerFnImport> findByPeriodOrderByYmd(String employeeId);
	/**
	 * 日別実績の勤務情報を取得する
	 * @param employeeIds
	 * @param datePeriod
	 * @return
	 */
	public List<RecordWorkInfoFunAdapterDto>  findByPeriodOrderByYmdAndEmps(List<String> employeeIds, DatePeriod datePeriod);
	
	
	public Optional<String> getWorkTypeCode(String employeeId, GeneralDate ymd);
}
