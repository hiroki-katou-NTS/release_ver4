package nts.uk.ctx.sys.assist.ac.favoritespecify;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.pub.jobtitle.SequenceMasterExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SequenceMasterPub;
import nts.uk.ctx.sys.assist.dom.favorite.adapter.RankOfPositionAdapter;
import nts.uk.ctx.sys.assist.dom.favorite.adapter.SequenceMasterImport;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class RankOfPositionAdapterImpl implements RankOfPositionAdapter {
	@Inject
	public SequenceMasterPub pub;

	@Override
	public  List<SequenceMasterImport> getRankOfPosition() {
		String cId = AppContexts.user().companyId();
		List<SequenceMasterExport> lstExport = this.pub.findByCid(cId);
		if (!lstExport.isEmpty()) {
			return lstExport.stream().map(x -> {
				return new SequenceMasterImport(x.getCompanyId(), x.getOrder(), x.getSequenceCode(),
						x.getSequenceName());
			}).collect(Collectors.toList());
		}
		return new ArrayList<SequenceMasterImport>();
	}

}
