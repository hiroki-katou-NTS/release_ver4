package nts.uk.ctx.exio.infra.repository.exo.categoryitemdata;

import java.util.Optional;
import java.util.List;

import javax.ejb.Stateless;

import nts.uk.ctx.exio.infra.entity.exo.categoryitemdata.OiomtCtgItemData;
import nts.uk.ctx.exio.infra.entity.exo.categoryitemdata.OiomtCtgItemDataPk;
import nts.uk.ctx.exio.dom.exo.categoryitemdata.CtgItemDataRepository;
import nts.uk.ctx.exio.dom.exo.categoryitemdata.CtgItemData;
import nts.arc.layer.infra.data.JpaRepository;

@Stateless
public class JpaCtgItemDataRepository extends JpaRepository implements CtgItemDataRepository
{

    private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM OiomtCtgItemData f";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING +
    		" WHERE f.OiomtCtgItemDataPk.categoryId =:categoryId AND f.OiomtCtgItemDataPk.itemNo =:itemNo";
	private static final String SELECT_BY_CATEGORY_ID_AND_DISPLAY_CLS = SELECT_ALL_QUERY_STRING
			+ " WHERE f.ctgItemDataPk.categoryId =:categoryId AND f.displayClassfication = 1";
	private static final String SELECT_BY_KEY_AND_DISPLAY_CLS = SELECT_BY_CATEGORY_ID_AND_DISPLAY_CLS
			+ " AND f.ctgItemDataPk.itemNo =:itemNo";

    @Override
    public List<CtgItemData> getAllCtgItemData(){
        return this.queryProxy().query(SELECT_ALL_QUERY_STRING, OiomtCtgItemData.class)
                .getList(item -> item.toDomain());
    }


	@Override
	public List<CtgItemData> getAllByCategoryId(String categoryId) {
		return this.queryProxy().query(SELECT_BY_CATEGORY_ID_AND_DISPLAY_CLS, OiomtCtgItemData.class)
				.setParameter("categoryId", categoryId).getList(item -> item.toDomain());
	}

	@Override
	public List<CtgItemData> getAllByKey(String categoryId, String itemNo) {
		return this.queryProxy().query(SELECT_BY_KEY_AND_DISPLAY_CLS, OiomtCtgItemData.class)
				.setParameter("categoryId", categoryId).setParameter("itemNo", itemNo).getList(item -> item.toDomain());
	}
    
    @Override
    public Optional<CtgItemData> getCtgItemDataById(String categoryId, Integer itemNo){
        return this.queryProxy().query(SELECT_BY_KEY_STRING, OiomtCtgItemData.class)
        		.setParameter("categoryId", categoryId)
        		.setParameter("itemNo", itemNo)
        		.getSingle(c->c.toDomain());
    }

    @Override
    public void add(CtgItemData domain){
        this.commandProxy().insert(OiomtCtgItemData.toEntity(domain));
    }

    @Override
    public void update(CtgItemData domain){
        this.commandProxy().update(OiomtCtgItemData.toEntity(domain));
    }

    @Override
    public void remove(){
        this.commandProxy().remove(OiomtCtgItemData.class, new OiomtCtgItemDataPk()); 
    }

}
