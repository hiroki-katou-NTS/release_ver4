package nts.uk.ctx.at.record.pubimp.application.realitystatus;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.application.realitystatus.RealityStatusService;
import nts.uk.ctx.at.record.pub.application.realitystatus.RealityStatusPub;
import nts.uk.ctx.at.record.pub.application.realitystatus.UseSetingExport;

@Stateless
public class RealityStatusPubImpl implements RealityStatusPub{
	@Inject RealityStatusService realityStatusService;
	@Override
	public UseSetingExport getUseSetting(String cid) {
		realityStatusService.getUseSetting(cid);
		return null;
	}
}
