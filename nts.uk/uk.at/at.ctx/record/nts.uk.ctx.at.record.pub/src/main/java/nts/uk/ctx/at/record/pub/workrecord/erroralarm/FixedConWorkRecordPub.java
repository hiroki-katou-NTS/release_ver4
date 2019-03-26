package nts.uk.ctx.at.record.pub.workrecord.erroralarm;

import java.util.List;


public interface FixedConWorkRecordPub {

	/**
	 * get all fixed condition work record by list errorAlarmID
	 * @return
	 */
	List<FixedConWorkRecordPubExport> getAllFixedConWorkRecordByID(String dailyAlarmConID);
	
	List<FixedConWorkRecordPubExport> getAllFixedConWorkRecordByID(List<String> dailyAlarmConID);

	/**
	 * get fixed condition work record by dailyAlarmConID and fixConWorkRecordNo
	 * @param errorAlarmID
	 * @param fixConWorkRecordNo
	 * @return
	 */
	FixedConWorkRecordPubExport getFixedConWRByCode(String dailyAlarmConID,int fixConWorkRecordNo);
	
	
	void addFixedConWorkRecordPub(FixedConWorkRecordPubExport fixedConWorkRecordPubExport);
	
	void updateFixedConWorkRecordPub(FixedConWorkRecordPubExport fixedConWorkRecordPubExport);
	
	void deleteFixedConWorkRecordPub(String dailyAlarmConID);
}
