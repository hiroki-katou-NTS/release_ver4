package nts.uk.ctx.at.record.infra.repository.jobmanagement.favoritetask.favoritetaskitem;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.jobmanagement.favoritetask.favoritetaskitem.FavoriteDisplayOrder;
import nts.uk.ctx.at.record.dom.jobmanagement.favoritetask.favoritetaskitem.FavoriteTaskDisplayOrder;
import nts.uk.ctx.at.record.dom.jobmanagement.favoritetask.favoritetaskitem.FavoriteTaskDisplayOrderRepository;
import nts.uk.ctx.at.record.infra.entity.jobmanagement.favoritetask.favoritetaskitem.KrcdtTaskFavFrameSet;
import nts.uk.ctx.at.record.infra.entity.jobmanagement.favoritetask.favoritetaskitem.KrcdtTaskFavFrameSetDisporder;

/**
 * 
 * @author tutt
 *
 */
@Stateless
public class JpaFavoriteTaskDisplayOrderRepository extends JpaRepository implements FavoriteTaskDisplayOrderRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT o FROM KrcdtTaskFavFrameSetDisporder o";
	private static final String SELECT_BY_SID = SELECT_ALL_QUERY_STRING + " WHERE s.sId = :sId";

	@Override
	public void insert(FavoriteTaskDisplayOrder order) {
		for (FavoriteDisplayOrder o : order.getDisplayOrders()) {
			this.commandProxy().insert(new KrcdtTaskFavFrameSetDisporder(order.getEmployeeId(), o));
		}
	}

	@Override
	public void update(FavoriteTaskDisplayOrder order) {
		for (FavoriteDisplayOrder o : order.getDisplayOrders()) {
			this.commandProxy().update(new KrcdtTaskFavFrameSetDisporder(order.getEmployeeId(), o));
		}
	}

	@Override
	public void delete(String sId) {
		KrcdtTaskFavFrameSetDisporder entity = this.queryProxy()
				.query(SELECT_BY_SID, KrcdtTaskFavFrameSetDisporder.class).setParameter("sId", sId).getSingleOrNull();
		this.commandProxy().remove(entity);
	}

	@Override
	public Optional<FavoriteTaskDisplayOrder> get(String sId) {
		Optional<KrcdtTaskFavFrameSetDisporder> entity = this.queryProxy().query(SELECT_BY_SID, KrcdtTaskFavFrameSetDisporder.class).setParameter("sId", sId)
				.getSingle();
		//TODO: mapping với FavoriteTaskDisplayOrder
		 return null;
	}

}
