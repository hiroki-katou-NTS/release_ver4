package nts.uk.ctx.sys.assist.infra.repository.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.assist.dom.category.Category;
import nts.uk.ctx.sys.assist.dom.category.CategoryRepository;
import nts.uk.ctx.sys.assist.dom.category.SystemUsability;
import nts.uk.ctx.sys.assist.dom.category.TimeStore;
import nts.uk.ctx.sys.assist.infra.entity.category.SspmtCategory;

@Stateless
public class JpaCategoryRepository extends JpaRepository implements CategoryRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM SspmtCategory f";

	private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE  f.categoryId =:categoryId ";
	private static final String SELECT_BY_ATTENDANCE_SYSTEM = SELECT_ALL_QUERY_STRING
			+ " WHERE f.attendanceSystem =:attendanceSystem";
	private static final String SELECT_BY_PAYMENT_AVAIABILITY = SELECT_ALL_QUERY_STRING
			+ " WHERE f.paymentAvailability =:paymentAvailability";
	private static final String SELECT_BY_POSSIBILITY_SYSTEM = SELECT_ALL_QUERY_STRING
			+ " WHERE f.possibilitySystem =:possibilitySystem";
	private static final String SELECT_BY_SCHELPER_SYSTEM = SELECT_ALL_QUERY_STRING
			+ " WHERE f.schelperSystem =:schelperSystem";
	private static final String SELECT_BY_ATTENDANCE_SYSTEM_AND_CODENAME = SELECT_ALL_QUERY_STRING
			+ " WHERE ( f.categoryId like :keySearch OR f.categoryName LIKE :keySearch ) and f.timeStore <> :timeStore and f.categoryId NOT IN :categoriesIgnore and f.attendanceSystem = :attendanceSystem ORDER BY f.attendanceSystem,f.categoryId";
	private static final String SELECT_BY_PAYMENT_AVAIABILITY_AND_CODENAME = SELECT_ALL_QUERY_STRING
			+ " WHERE ( f.categoryId like :keySearch OR f.categoryName LIKE :keySearch ) and f.timeStore <> :timeStore and f.categoryId NOT IN :categoriesIgnore and f.paymentAvailability = :paymentAvailability ORDER BY f.paymentAvailability,f.categoryId";
	private static final String SELECT_BY_POSSIBILITY_SYSTEM_AND_CODENAME = SELECT_ALL_QUERY_STRING
			+ " WHERE ( f.categoryId like :keySearch OR f.categoryName LIKE :keySearch ) and f.timeStore <> :timeStore and f.categoryId NOT IN :categoriesIgnore and f.possibilitySystem = :possibilitySystem ORDER BY f.possibilitySystem,f.categoryId";
	private static final String SELECT_BY_SCHELPER_SYSTEM_AND_CODENAME = SELECT_ALL_QUERY_STRING
			+ " WHERE ( f.categoryId like :keySearch OR f.categoryName LIKE :keySearch ) and f.timeStore <> :timeStore and f.categoryId NOT IN :categoriesIgnore and f.schelperSystem = :schelperSystem ORDER BY f.possibilitySystem,f.categoryId";

	private static final String SELECT_BY_ATTENDANCE_SYSTEM_AND_CODENAME_CATEIGNORE = SELECT_ALL_QUERY_STRING
			+ " WHERE ( f.categoryId like :keySearch OR f.categoryName LIKE :keySearch ) and f.timeStore <> :timeStore  and f.attendanceSystem = :attendanceSystem ORDER BY f.attendanceSystem,f.categoryId";
	private static final String SELECT_BY_PAYMENT_AVAIABILITY_AND_CODENAME_CATEIGNORE = SELECT_ALL_QUERY_STRING
			+ " WHERE ( f.categoryId like :keySearch OR f.categoryName LIKE :keySearch ) and f.timeStore <> :timeStore  and f.paymentAvailability = :paymentAvailability ORDER BY f.paymentAvailability,f.categoryId";
	private static final String SELECT_BY_POSSIBILITY_SYSTEM_AND_CODENAME_CATEIGNORE = SELECT_ALL_QUERY_STRING
			+ " WHERE ( f.categoryId like :keySearch OR f.categoryName LIKE :keySearch ) and f.timeStore <> :timeStore  and f.possibilitySystem = :possibilitySystem ORDER BY f.possibilitySystem,f.categoryId";
	private static final String SELECT_BY_SCHELPER_SYSTEM_AND_CODENAME_CATEIGNORE = SELECT_ALL_QUERY_STRING
			+ " WHERE ( f.categoryId like :keySearch OR f.categoryName LIKE :keySearch ) and f.timeStore <> :timeStore  and f.schelperSystem = :schelperSystem ORDER BY f.possibilitySystem,f.categoryId";

	private static final String SELECT_BY_LIST_KEY_STRING = SELECT_ALL_QUERY_STRING
			+ " WHERE  f.categoryId IN :lstCID ";
	
	private static final String SELECT_BY_ID = "SELECT f FROM SspmtCategory f WHERE f.categoryId IN ( SELECT t.tableListPk.categoryId FROM SspmtTableList t WHERE  t.dataRecoveryProcessId =:storeProcessingId AND t.selectionTargetForRes =:selectionTargetForRes )";
	
	private static final String GET_LIST_CATEGORYID = "SELECT t.tableListPk.categoryId FROM SspmtTableList t WHERE  t.dataRecoveryProcessId =:storeProcessingId AND t.selectionTargetForRes =:selectionTargetForRes ";

	@Override
	public List<Category> getAllCategory() {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SspmtCategory.class).getList(item -> item.toDomain());
	}

	@Override
	public Optional<Category> getCategoryById(String categoryId) {
		return this.queryProxy().query(SELECT_BY_KEY_STRING, SspmtCategory.class).setParameter("categoryId", categoryId)
				.getSingle(c -> c.toDomain());
	}

	@Override
	public void add(Category domain) {
		this.commandProxy().insert(SspmtCategory.toEntity(domain));
	}

	@Override
	public void update(Category domain) {
		this.commandProxy().update(SspmtCategory.toEntity(domain));
	}

	@Override
	public void remove(String categoryId) {
		this.commandProxy().remove(SspmtCategory.class, categoryId);
	}

	@Override
	public Optional<Category> getCategoryById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Category> findByAttendanceSystem() {
		return this.queryProxy().query(SELECT_BY_ATTENDANCE_SYSTEM, SspmtCategory.class)
				.setParameter("attendanceSystem", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain());
	}

	@Override
	public List<Category> findByPaymentAvailability() {
		return this.queryProxy().query(SELECT_BY_PAYMENT_AVAIABILITY, SspmtCategory.class)
				.setParameter("paymentAvailability", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain());
	}

	@Override
	public List<Category> findByPossibilitySystem() {
		return this.queryProxy().query(SELECT_BY_POSSIBILITY_SYSTEM, SspmtCategory.class)
				.setParameter("possibilitySystem", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain());
	}

	@Override
	public List<Category> findBySchelperSystem() {
		return this.queryProxy().query(SELECT_BY_SCHELPER_SYSTEM, SspmtCategory.class)
				.setParameter("schelperSystem", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain());
	}

	@Override
	public List<Category> findByAttendanceSystemAndCodeName(String keySearch, List<String> categoriesIgnore) {
		List<Category> resultList = new ArrayList<>();
		if (categoriesIgnore == null || categoriesIgnore.isEmpty()) {
			resultList.addAll(this.queryProxy().query(SELECT_BY_ATTENDANCE_SYSTEM_AND_CODENAME_CATEIGNORE, SspmtCategory.class)
					.setParameter("keySearch", "%" + keySearch + "%")
					.setParameter("timeStore", TimeStore.FULL_TIME.value)
					.setParameter("attendanceSystem", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain()));
		} else {
			CollectionUtil.split(categoriesIgnore, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
				resultList.addAll(this.queryProxy().query(SELECT_BY_ATTENDANCE_SYSTEM_AND_CODENAME, SspmtCategory.class)
					.setParameter("keySearch", "%" + keySearch + "%").setParameter("categoriesIgnore", subList)
					.setParameter("timeStore", TimeStore.FULL_TIME.value)
					.setParameter("attendanceSystem", SystemUsability.AVAILABLE.value)
					.getList(c -> c.toDomain()));
			});
			resultList.sort((o1, o2) -> {
				int tmp = o1.getAttendanceSystem().value - o2.getAttendanceSystem().value;
				if (tmp != 0) return tmp;
				return o1.getCategoryId().compareTo(o2.getCategoryId());
			});
		}
		return resultList;
	}

	@Override
	public List<Category> findByPaymentAvailabilityAndCodeName(String keySearch, List<String> categoriesIgnore) {
		List<Category> resultList = new ArrayList<>();
		if (categoriesIgnore == null || categoriesIgnore.isEmpty()) {
			resultList.addAll(this.queryProxy().query(SELECT_BY_PAYMENT_AVAIABILITY_AND_CODENAME_CATEIGNORE, SspmtCategory.class)
					.setParameter("keySearch", "%" + keySearch + "%")
					.setParameter("timeStore", TimeStore.FULL_TIME.value)
					.setParameter("paymentAvailability", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain()));
		} else {
			CollectionUtil.split(categoriesIgnore, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
				resultList.addAll(this.queryProxy().query(SELECT_BY_PAYMENT_AVAIABILITY_AND_CODENAME, SspmtCategory.class)
					.setParameter("keySearch", "%" + keySearch + "%").setParameter("categoriesIgnore", subList)
					.setParameter("timeStore", TimeStore.FULL_TIME.value)
					.setParameter("paymentAvailability", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain()));
			});
			resultList.sort((o1, o2) -> {
				int tmp = o1.getPaymentAvailability().value - o2.getPaymentAvailability().value;
				if (tmp != 0) return tmp;
				return o1.getCategoryId().compareTo(o2.getCategoryId());
			});
		}
		return resultList;
	}

	@Override
	public List<Category> findByPossibilitySystemAndCodeName(String keySearch, List<String> categoriesIgnore) {
		List<Category> resultList = new ArrayList<>();
		if (categoriesIgnore == null || categoriesIgnore.isEmpty()) {
			resultList.addAll(this.queryProxy().query(SELECT_BY_POSSIBILITY_SYSTEM_AND_CODENAME_CATEIGNORE, SspmtCategory.class)
					.setParameter("keySearch", "%" + keySearch + "%")
					.setParameter("timeStore", TimeStore.FULL_TIME.value)
					.setParameter("possibilitySystem", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain()));
		} else {
			CollectionUtil.split(categoriesIgnore, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
				resultList.addAll(this.queryProxy().query(SELECT_BY_POSSIBILITY_SYSTEM_AND_CODENAME, SspmtCategory.class)
						.setParameter("keySearch", "%" + keySearch + "%").setParameter("categoriesIgnore", subList)
						.setParameter("timeStore", TimeStore.FULL_TIME.value)
						.setParameter("possibilitySystem", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain()));
			});
			resultList.sort((o1, o2) -> {
				int tmp = o1.getPossibilitySystem().value - o2.getPossibilitySystem().value;
				if (tmp != 0) return tmp;
				return o1.getCategoryId().compareTo(o2.getCategoryId());
			});
		}
		return resultList;
	}

	@Override
	public List<Category> findBySchelperSystemAndCodeName(String keySearch, List<String> categoriesIgnore) {
		List<Category> resultList = new ArrayList<>();
		if (categoriesIgnore == null || categoriesIgnore.isEmpty()) {
			resultList.addAll(this.queryProxy().query(SELECT_BY_SCHELPER_SYSTEM_AND_CODENAME_CATEIGNORE, SspmtCategory.class)
					.setParameter("keySearch", "%" + keySearch + "%")
					.setParameter("timeStore", TimeStore.FULL_TIME.value)
					.setParameter("schelperSystem", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain()));
		} else {
			CollectionUtil.split(categoriesIgnore, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
				resultList.addAll(this.queryProxy().query(SELECT_BY_SCHELPER_SYSTEM_AND_CODENAME, SspmtCategory.class)
					.setParameter("keySearch", "%" + keySearch + "%").setParameter("categoriesIgnore", subList)
					.setParameter("timeStore", TimeStore.FULL_TIME.value)
					.setParameter("schelperSystem", SystemUsability.AVAILABLE.value).getList(c -> c.toDomain()));
			});
			resultList.sort((o1, o2) -> {
				int tmp = o1.getPossibilitySystem().value - o2.getPossibilitySystem().value;
				if (tmp != 0) return tmp;
				return o1.getCategoryId().compareTo(o2.getCategoryId());
			});
		}
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.assist.dom.category.CategoryRepository#getCategoryByListId
	 * (java.util.List)
	 */
	@Override
	public List<Category> getCategoryByListId(List<String> categoryIds) {

		// check exist input
		if (CollectionUtil.isEmpty(categoryIds)) {
			return new ArrayList<>();
		}

		List<Category> lstCategory = new ArrayList<>();
		CollectionUtil.split(categoryIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
			lstCategory.addAll(this.queryProxy().query(SELECT_BY_LIST_KEY_STRING, SspmtCategory.class)
					.setParameter("lstCID", subIdList).getList(f -> f.toDomain()));
		});
				
		return lstCategory;

		// return this.queryProxy().query(SELECT_BY_LIST_KEY_STRING,
		// SspmtCategory.class)
		// .setParameter("lstCID", categoryIds).getList(c->c.toDomain());
	}

	@Override
	public List<Category> findById(String storeProcessingId, int selectionTargetForRes) {
		return this.queryProxy().query(SELECT_BY_ID, SspmtCategory.class)
				.setParameter("storeProcessingId", storeProcessingId)
				.setParameter("selectionTargetForRes", selectionTargetForRes)
				.getList(c -> c.toDomain());
	}
	
	@Override
	public Set<String> getListCategoryId(String storeProcessingId, int selectionTargetForRes) {
		List<Object> objects =  this.queryProxy().query(GET_LIST_CATEGORYID, Object.class)
				.setParameter("storeProcessingId", storeProcessingId)
				.setParameter("selectionTargetForRes", selectionTargetForRes)
				.getQuery().getResultList();
		 
		return objects.stream().map(x -> {
			Object values = (Object)x;
			String ctgId = String.valueOf(values);
			return ctgId;
		}).collect(Collectors.toSet());
	}

}
