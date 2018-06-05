package nts.uk.ctx.sys.auth.infra.repository.grant.rolesetjob;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.auth.dom.grant.rolesetjob.RoleSetGrantedJobTitle;
import nts.uk.ctx.sys.auth.dom.grant.rolesetjob.RoleSetGrantedJobTitleRepository;
import nts.uk.ctx.sys.auth.infra.entity.grant.rolesetjob.SacmtRoleSetGrantedJobTitle;
import nts.uk.ctx.sys.auth.infra.entity.grant.rolesetjob.SacmtRoleSetGrantedJobTitleDetail;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
@Transactional
public class JpaRoleSetGrantedJobTitleRepository extends JpaRepository implements RoleSetGrantedJobTitleRepository {

	private static final String GET_All_BY_COMPANY_ID = "SELECT rs FROM SacmtRoleSetGrantedJobTitle rs WHERE rs.companyId = :companyId ";

	private static final String SELECT_BY_JOBTITLECD = "SELECT c FROM SacmtRoleSetGrantedJobTitleDetail c "
			+ " WHERE c.roleSetGrantedJobTitleDetailPK.companyId = :companyId"
			+ " AND c.roleSetGrantedJobTitleDetailPK.jobTitleId = :jobTitleId";
	
	@Override
	public List<RoleSetGrantedJobTitle> getAllByCompanyId(String companyId) {
		return this.queryProxy().query(GET_All_BY_COMPANY_ID, SacmtRoleSetGrantedJobTitle.class)
				.setParameter("companyId", companyId).getList(d -> SacmtRoleSetGrantedJobTitle.toDomain(d));
	}

	@Override
	public Optional<RoleSetGrantedJobTitle> getOneByCompanyId(String companyId) {
		SacmtRoleSetGrantedJobTitle entity = this.queryProxy()
				.query(GET_All_BY_COMPANY_ID, SacmtRoleSetGrantedJobTitle.class).setParameter("companyId", companyId)
				.getSingleOrNull();
		if (entity == null) {
			return Optional.empty();
		} else {
			return Optional.of(SacmtRoleSetGrantedJobTitle.toDomain(entity));
		}
	}

	@Override
	public void insert(RoleSetGrantedJobTitle domain) {
		this.commandProxy().insert(SacmtRoleSetGrantedJobTitle.toEntity(domain));
	}

	@Override
	public void update(RoleSetGrantedJobTitle domain) {
		SacmtRoleSetGrantedJobTitle entity = this.queryProxy()
				.find(domain.getCompanyId(), SacmtRoleSetGrantedJobTitle.class).get();
		entity.applyToConcurrentPerson = domain.isApplyToConcurrentPerson();
		List<SacmtRoleSetGrantedJobTitleDetail> oldDetails = entity.details;
		List<SacmtRoleSetGrantedJobTitleDetail> newDetails = domain.getDetails().stream()
				.map(item -> new SacmtRoleSetGrantedJobTitleDetail(item.getRoleSetCd().v(), item.getJobTitleId(),
						item.getCompanyId()))
				.collect(Collectors.toList());
		
		for (SacmtRoleSetGrantedJobTitleDetail newDetail : newDetails){
			for (SacmtRoleSetGrantedJobTitleDetail oldDetail : oldDetails){
				if (oldDetail.roleSetGrantedJobTitleDetailPK.equals(newDetail.roleSetGrantedJobTitleDetailPK)){
					newDetails.set(newDetails.indexOf(newDetail), oldDetail);
					break;
				}
			}
		} 
		
		entity.details = newDetails;
		this.commandProxy().update(entity);
	}

	@Override
	public void delete(String companyId) {
		this.commandProxy().remove(SacmtRoleSetGrantedJobTitle.class, companyId);

	}

	@Override
	public boolean checkRoleSetCdExist(String roleSetCd, String companyId) {
		SacmtRoleSetGrantedJobTitle entity = this.queryProxy()
				.query(GET_All_BY_COMPANY_ID, SacmtRoleSetGrantedJobTitle.class).setParameter("companyId", companyId)
				.getSingleOrNull();
		if (entity == null) {
			return false;
		} else {
			return SacmtRoleSetGrantedJobTitle.toDomain(entity).isRoleSetCdExist(roleSetCd);
		}
	}

	@Override
	public Optional<String> getRoleSetCd(String companyId, String jobTitleId) {
		return this.queryProxy().query(SELECT_BY_JOBTITLECD ,SacmtRoleSetGrantedJobTitleDetail.class )
				.setParameter("companyId", companyId)
				.setParameter("jobTitleId", jobTitleId)
				.getSingle( c -> c.roleSetCd);
		
	}

}
