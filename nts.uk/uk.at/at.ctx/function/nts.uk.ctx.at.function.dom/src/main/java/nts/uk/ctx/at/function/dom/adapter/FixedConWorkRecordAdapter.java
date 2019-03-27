package nts.uk.ctx.at.function.dom.adapter;

import java.util.List;



public interface FixedConWorkRecordAdapter {

	/**
	 * get all fixed condition work record by list dailyAlarmConID
	 * @return
	 */
	List<FixedConWorkRecordAdapterDto> getAllFixedConWorkRecordByID(String dailyAlarmConID);
	
	List<FixedConWorkRecordAdapterDto> getAllFixedConWorkRecordByID(List<String> dailyAlarmConID);

	/**
	 * get fixed condition work record by dailyAlarmConID and fixConWorkRecordNo
	 * @param dailyAlarmConID
	 * @param fixConWorkRecordNo
	 * @return
	 */
	FixedConWorkRecordAdapterDto getFixedConWRByCode(String dailyAlarmConID,int fixConWorkRecordNo);
	
	void addFixedConWorkRecordPub(FixedConWorkRecordAdapterDto fixedConWorkRecordAdapterDto);
	
	void updateFixedConWorkRecordPub(FixedConWorkRecordAdapterDto fixedConWorkRecordAdapterDto);
	
	void deleteFixedConWorkRecordPub(String dailyAlarmConID);
}
