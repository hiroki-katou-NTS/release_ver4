package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;


public interface AfterOvertimeReflectProcess {
	/**
	 * 予定勤種・就時の反映
	 * @param employeeId
	 * @param baseDate
	 * @param scheAndRecordSameChangeFlg
	 * @param workTimeCode
	 * @return
	 */
	public boolean checkScheReflect(OvertimeParameter overtimePara);
	
	

}
