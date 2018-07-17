package nts.uk.ctx.exio.app.find.exo.awdataformatsetting;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.exio.dom.exo.dataformat.AwDataFormatSet;
import nts.uk.ctx.exio.dom.exo.dataformat.AwDataFormatSetRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AWDataScreenService {

	@Inject
	AwDataFormatSetRepository awDataFormatSetRepository;

	public Optional<AwDataFormatDTO> getAWData(){
		String cid = AppContexts.user().companyId();
		Optional<AwDataFormatSet> awDataFormatSet = awDataFormatSetRepository
				.getAwDataFormatSetById(cid);
		if (awDataFormatSet.isPresent()){
			return Optional.of(AwDataFormatDTO.fromDomain(awDataFormatSet.get()));
		}else return Optional.empty();
	}
}
