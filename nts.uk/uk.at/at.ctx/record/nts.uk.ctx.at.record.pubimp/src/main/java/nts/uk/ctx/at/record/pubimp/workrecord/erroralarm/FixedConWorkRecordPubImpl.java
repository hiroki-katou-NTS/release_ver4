package nts.uk.ctx.at.record.pubimp.workrecord.erroralarm;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
//import javax.persistence.EnumType;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.FixedConditionWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.FixedConditionWorkRecordName;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.FixedConditionWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.WorkRecordFixedCheckItem;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.FixedConWorkRecordPub;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.FixedConWorkRecordPubExport;

@Stateless
public class FixedConWorkRecordPubImpl implements FixedConWorkRecordPub {

	@Inject
	private FixedConditionWorkRecordRepository repo;
	
	@Override
	public List<FixedConWorkRecordPubExport> getAllFixedConWorkRecordByID(String dailyAlarmConID) {
		List<FixedConWorkRecordPubExport> data = repo.getAllFixedConWorkRecordByID(dailyAlarmConID)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
		if(data.isEmpty())
			return Collections.emptyList();
		return data;
	}
	
	@Override
	public List<FixedConWorkRecordPubExport> getAllFixedConWorkRecordByID(List<String> dailyAlarmConID) {
		List<FixedConWorkRecordPubExport> data = repo.getAllFixedConWorkRecordByID(dailyAlarmConID)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
		if(data.isEmpty())
			return Collections.emptyList();
		return data;
	}

	@Override
	public FixedConWorkRecordPubExport getFixedConWRByCode(String dailyAlarmConID,int fixConWorkRecordNo) {
		Optional<FixedConWorkRecordPubExport> data = repo.getFixedConWRByCode(dailyAlarmConID,fixConWorkRecordNo).map(c->convertToExport(c));
		if(data.isPresent())
			return data.get();
		return null;
	}
	
	public FixedConWorkRecordPubExport convertToExport(FixedConditionWorkRecord domain) {
		return new FixedConWorkRecordPubExport(
				domain.getDailyAlarmConID(),
				domain.getFixConWorkRecordNo().value,
				domain.getMessage().v(),
				domain.isUseAtr()
				);
		
	}
	
	public FixedConditionWorkRecord convertToDomain(FixedConWorkRecordPubExport dto) {
		return new FixedConditionWorkRecord(
				dto.getDailyAlarmConID(),
				EnumAdaptor.valueOf(dto.getFixConWorkRecordNo(), WorkRecordFixedCheckItem.class),
				new FixedConditionWorkRecordName(dto.getMessage()),
				dto.isUseAtr()
				);
		
	}
	

	@Override
	public void addFixedConWorkRecordPub(FixedConWorkRecordPubExport fixedConWorkRecordPubExport) {
		this.repo.addFixedConWorkRecord(convertToDomain(fixedConWorkRecordPubExport));
	}

	@Override
	public void updateFixedConWorkRecordPub(FixedConWorkRecordPubExport fixedConWorkRecordPubExport) {
		this.repo.updateFixedConWorkRecord(convertToDomain(fixedConWorkRecordPubExport));
		
	}

	@Override
	public void deleteFixedConWorkRecordPub(String dailyAlarmConID) {
		this.repo.deleteFixedConWorkRecord(dailyAlarmConID);
	}

}
