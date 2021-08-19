package nts.uk.ctx.exio.infra.repository.input.importableitem.domain;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.exio.dom.input.domain.ImportingDomain;
import nts.uk.ctx.exio.dom.input.domain.ImportingDomainId;
import nts.uk.ctx.exio.dom.input.domain.ImportingDomainRepository;
import nts.uk.ctx.exio.infra.entity.input.importableitem.domain.XimctDomain;

@Stateless
@TransactionAttribute
public class JpaImportingDomainRepository extends JpaRepository implements ImportingDomainRepository {

	@Override
	public ImportingDomain find(ImportingDomainId groupId) {
		
		String sql = "select * from XIMCT_GROUP"
				+ " where GROUP_ID = @id";
		
		return this.jdbcProxy().query(sql)
				.paramInt("id", groupId.value)
				.getSingle(rec -> XimctDomain.MAPPER.toEntity(rec))
				.map(e -> e.toDomain())
				.get();
	}

}
