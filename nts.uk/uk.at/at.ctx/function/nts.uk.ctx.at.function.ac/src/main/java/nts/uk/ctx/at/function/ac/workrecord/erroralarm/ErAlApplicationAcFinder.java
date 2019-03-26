package nts.uk.ctx.at.function.ac.workrecord.erroralarm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.ErAlApplicationAdapter;
import nts.uk.ctx.at.function.dom.adapter.ErAlApplicationAdapterDto;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.ErAlApplicationPub;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.ErAlApplicationPubExport;
@Stateless
public class ErAlApplicationAcFinder implements ErAlApplicationAdapter {
	
	@Inject
	private ErAlApplicationPub erAlApplicationpub;

	@Override
	public Optional<ErAlApplicationAdapterDto> getAllErAlAppByEralCode(String companyID, String errorAlarmCode) {
		return erAlApplicationpub.getAllErAlAppByEralCode(companyID, errorAlarmCode).map(c->convertToImport(c));
	}
	
	private ErAlApplicationAdapterDto convertToImport(ErAlApplicationPubExport export) {
		return new ErAlApplicationAdapterDto(export.getCompanyID(),
				export.getErrorAlarmCode(),
				export.getAppType()
				);
	}

	@Override
	public List<ErAlApplicationAdapterDto> getAllErAlAppByEralCode(String companyID, List<String> errorAlarmCode) {
		return erAlApplicationpub.getAllErAlAppByEralCode(companyID, errorAlarmCode).stream().map(c -> convertToImport(c)).collect(Collectors.toList());
	}

}
