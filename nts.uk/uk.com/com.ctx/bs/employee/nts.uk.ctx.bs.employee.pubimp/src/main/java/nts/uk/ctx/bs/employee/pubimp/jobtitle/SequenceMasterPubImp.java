package nts.uk.ctx.bs.employee.pubimp.jobtitle;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.jobtitle.sequence.SequenceMasterRepository;
import nts.uk.ctx.bs.employee.pub.jobtitle.SequenceMasterExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SequenceMasterPub;

@Stateless
public class SequenceMasterPubImp implements SequenceMasterPub{

	@Inject
	private SequenceMasterRepository repo;
	
	@Override
	public List<SequenceMasterExport> findAll(String companyId, String sequenceCode) {
		// TODO Auto-generated method stub
		return this.repo.findAll(companyId, sequenceCode).stream()
				.map(x -> {
			return new SequenceMasterExport(x.getCompanyId().v(), x.getOrder(), x.getSequenceCode().v(), x.getSequenceName().v());
		}).collect(Collectors.toList());
	}

}
