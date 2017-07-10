package nts.uk.ctx.sys.portal.infra.repository.placement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.portal.dom.placement.Placement;
import nts.uk.ctx.sys.portal.dom.placement.PlacementRepository;
import nts.uk.ctx.sys.portal.infra.entity.placement.CcgmtPlacement;
import nts.uk.ctx.sys.portal.infra.entity.placement.CcgmtPlacementPK;

/**
 * @author LamDT
 */
@Stateless
public class JpaPlacementRepository extends JpaRepository implements PlacementRepository {
	
	private final String SELECT_SINGLE = "SELECT c FROM CcgmtPlacement c WHERE c.ccgmtPlacementPK.placementID = :placementID";
	private final String SELECT_BY_LAYOUT = "SELECT c FROM CcgmtPlacement c WHERE c.layoutID = :layoutID";
	private final String SELECT_BY_TOPPAGEPART = "SELECT c FROM CcgmtPlacement c WHERE c.topPagePartID = :topPagePartID";

	@Override
	public Optional<Placement> find(String placementID) {
		return this.queryProxy().query(SELECT_SINGLE, CcgmtPlacement.class)
				.setParameter("placementID", placementID)
				.getSingle(c -> toDomain(c));
	}

	@Override
	public List<Placement> findByLayout(String layoutID) {
		return this.queryProxy().query(SELECT_BY_LAYOUT, CcgmtPlacement.class)
				.setParameter("layoutID", layoutID)
				.getList(c -> toDomain(c));
	}
	
	@Override
	public List<Placement> findByTopPagePart(String topPagePartID) {
		return this.queryProxy().query(SELECT_BY_TOPPAGEPART, CcgmtPlacement.class)
				.setParameter("topPagePartID", topPagePartID)
				.getList(c -> toDomain(c));
	}

	@Override
	public void remove(String companyID, String placementID) {
		this.commandProxy().remove(CcgmtPlacement.class, new CcgmtPlacementPK(companyID, placementID));
		this.getEntityManager().flush();
	}
	
	@Override
	public void removeAll(String companyID, List<String> placementIDs) {
		List<CcgmtPlacementPK> listCcgmtPlacementPK = new ArrayList<CcgmtPlacementPK>();
		for (String placementID : placementIDs) {
			listCcgmtPlacementPK.add(new CcgmtPlacementPK(companyID, placementID));
		}
		this.commandProxy().removeAll(CcgmtPlacement.class, listCcgmtPlacementPK);
		this.getEntityManager().flush();
	}

	@Override
	public void add(Placement placement) {
		this.commandProxy().insert(toEntity(placement));
	}

	@Override
	public void addAll(Collection<Placement> placements) {
		this.commandProxy().insertAll(toEntity(placements));
	}
	
	@Override
	public void update(Placement placement) {
		CcgmtPlacement newEntity = toEntity(placement);
		CcgmtPlacement updatedEntity = this.queryProxy().find(newEntity.ccgmtPlacementPK, CcgmtPlacement.class).get();
		updatedEntity.column = newEntity.column;
		updatedEntity.row = newEntity.row;
		updatedEntity.width = newEntity.width;
		updatedEntity.height = newEntity.height;
		updatedEntity.externalUrl = newEntity.externalUrl;
		updatedEntity.topPagePartID = newEntity.topPagePartID;
		this.commandProxy().update(updatedEntity);
	}

	/**
	 * Convert entity to domain
	 * 
	 * @param CcgmtPlacement entity
	 * @return Placement instance
	 */
	private Placement toDomain(CcgmtPlacement entity) {
		return Placement.createFromJavaType(
			entity.ccgmtPlacementPK.companyID, entity.ccgmtPlacementPK.placementID, entity.layoutID, entity.topPagePartID,
			entity.column, entity.row,
			entity.externalUrl, entity.width, entity.height);
	}

	/**
	 * Convert domain to entity
	 * 
	 * @param domain CcgmtPlacement
	 * @return CcgmtPlacement instance
	 */
	private CcgmtPlacement toEntity(Placement domain) {
		// External Url information
		Integer width = null, height = null;
		String externalUrl = null;
		if (domain.getExternalUrl().isPresent()) {
			width = domain.getExternalUrl().get().getWidth().v();
			height = domain.getExternalUrl().get().getHeight().v();
			externalUrl = domain.getExternalUrl().get().getUrl().v();
		}
		
		return new CcgmtPlacement(
			new CcgmtPlacementPK(domain.getCompanyID(), domain.getPlacementID()),
			domain.getLayoutID(), domain.getColumn().v(), domain.getRow().v(),
			width, height, externalUrl, domain.getToppagePartID());
	}

	/**
	 * Convert Collection domain to Collection entity
	 * 
	 * @param placements Collection CcgmtPlacement
	 * @return Collection CcgmtPlacement
	 */
	private Collection<CcgmtPlacement> toEntity(Collection<Placement> placements) {
		List<CcgmtPlacement> entities = new ArrayList<CcgmtPlacement>();
		for (Placement placement : placements) {
			entities.add(toEntity(placement));
		}
		return entities;
	}
}
