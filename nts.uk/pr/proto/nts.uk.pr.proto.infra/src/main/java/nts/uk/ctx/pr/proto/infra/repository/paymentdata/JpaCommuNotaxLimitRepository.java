package nts.uk.ctx.pr.proto.infra.repository.paymentdata;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.proto.dom.paymentdata.CommuNotaxLimit;
import nts.uk.ctx.pr.proto.dom.paymentdata.repository.CommuNotaxLimitRepository;
import nts.uk.ctx.pr.proto.infra.entity.paymentdata.QtxmtCommuNotaxLimit;
import nts.uk.ctx.pr.proto.infra.entity.paymentdata.QtxmtCommuNotaxLimitPK;

@Stateless
public class JpaCommuNotaxLimitRepository extends JpaRepository implements CommuNotaxLimitRepository {

	private final String SELECT_ITEM = " SELECT c FROM QtxmtCommuNotaxLimit c "
									+ " WHERE c.ccd = :CCD" + " AND c.commuNotaxLimitCd = :COMMU_NOTAX_LIMIT_CD";
	
	@Override
	public Optional<CommuNotaxLimit> find(String ccd, String commuNotaxLimitCode) {
		
		return this.queryProxy().find(new QtxmtCommuNotaxLimitPK(ccd, commuNotaxLimitCode), QtxmtCommuNotaxLimit.class).map(c -> toDomain(c));
	}
	
	private static CommuNotaxLimit toDomain(QtxmtCommuNotaxLimit entity) {
		CommuNotaxLimit domain = CommuNotaxLimit.createFromJavaType(entity.qtxmtCommuNotaxLimitPK.companyCode,
																	entity.exclusVer, 
																	entity.qtxmtCommuNotaxLimitPK.commuNotaxLimitCd, 
																	entity.commuNotaxLimitName,
																	entity.commuNotaxLimitValue);
		return domain;
	}

}
