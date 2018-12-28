package nts.uk.ctx.sys.assist.infra.repository.categoryfieldmtfordelete;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.assist.dom.categoryfieldmt.CategoryFieldMt;
import nts.uk.ctx.sys.assist.dom.categoryfieldmt.CategoryFieldMtRepository;
import nts.uk.ctx.sys.assist.dom.categoryfieldmtfordelete.CategoryFieldMtForDelRepository;
import nts.uk.ctx.sys.assist.dom.categoryfieldmtfordelete.CategoryFieldMtForDelete;
import nts.uk.ctx.sys.assist.infra.entity.categoryfieldmt.SspmtCategoryFieldMt;
import nts.uk.ctx.sys.assist.infra.entity.categoryfieldmtfordelete.SspmtCategoryFieldMtForDelete;

@Stateless
public class JpaCategoryFieldMtForDelRepository extends JpaRepository implements CategoryFieldMtForDelRepository
{

    private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM SspmtCategoryFieldMtForDelete f";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE ";
    private static final String SELECT_BY_LIST_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE  f.categoryFieldMtPk.categoryId IN :lstCategoryId ";
    

    
    @Override
    public void remove(){
    }




	@Override
	public List<CategoryFieldMtForDelete> getAllCategoryFieldMt() {
		return null;
	}




	@Override
	public Optional<CategoryFieldMtForDelete> getCategoryFieldMtById() {
		return null;
	}




	@Override
	public void add(CategoryFieldMtForDelete domain) {
	}




	@Override
	public void update(CategoryFieldMtForDelete domain) {
		
	}




	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.assist.dom.categoryFieldMaster.CategoryFieldMtRepository#getCategoryFieldMtByListId(java.util.List)
	 */
	@Override
	public List<CategoryFieldMtForDelete> getCategoryFieldMtByListId(List<String> categoryIds) {
		List<SspmtCategoryFieldMtForDelete> entities = new ArrayList<>();
		CollectionUtil.split(categoryIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			entities.addAll(this.queryProxy().query(SELECT_BY_LIST_KEY_STRING, SspmtCategoryFieldMtForDelete.class)
				.setParameter("lstCategoryId", subList)
		        .getList());
		});
		return entities.stream().map(c->c.toDomain()).collect(Collectors.toList());
	}
}
