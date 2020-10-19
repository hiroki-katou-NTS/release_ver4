package nts.uk.ctx.sys.portal.infra.repository.generalsearch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.portal.dom.generalsearch.GeneralSearchHistory;
import nts.uk.ctx.sys.portal.dom.generalsearch.GeneralSearchRepository;
import nts.uk.ctx.sys.portal.infra.entity.generalsearch.SptdtGenericSearchHist;
import nts.uk.ctx.sys.portal.infra.entity.generalsearch.SptdtGenericSearchHistPK;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class JpaGeneralSearchHistoryRepository.
 */
@Stateless
public class JpaGeneralSearchHistoryRepository extends JpaRepository implements GeneralSearchRepository {

	/** The Constant QUERY_SELECT_ALL. */
	private static final String QUERY_SELECT_ALL = "SELECT f FROM SptdtGenericSearchHist f";
	
	/** The Constant QUERY_SELECT_LIST_ALL. */
	private static final String QUERY_SELECT_LIST_ALL = QUERY_SELECT_ALL
			+ " WHERE f.pk.companyID = :companyID"
			+ " AND f.pk.userID = :userID"
			+ " AND f.pk.searchCategory = :searchCategory"
			+ " ORDER BY f.searchDate DESC";
	
	/** The Constant QUERY_SELECT_LAST_10_RESULTS. */
	private static final String QUERY_SELECT_LAST_10_RESULTS = "SELECT TOP 10 * FROM SPTDT_GENERIC_SEARCH_HIST"
			+ " WHERE CID = 'companyID'"
			+ " AND USER_ID = 'userID'"
			+ " AND SEARCH_ATR = 'searchCategory'"
			+ " ORDER BY SEARCH_DATE DESC";
	
	/** The Constant QUERY_SELECT_BY_CONTENT. */
	private static final String QUERY_SELECT_BY_CONTENT = QUERY_SELECT_ALL
			+ " WHERE f.pk.companyID = :companyID"
			+ " AND f.pk.userID = :userID"
			+ " AND f.pk.searchCategory = :searchCategory"
			+ " AND f.pk.contents = :contents"
			+ " ORDER BY f.searchDate DESC";
	
	/**
	 * Insert.
	 *
	 * @param domain the domain
	 */
	@Override
	public void insert(GeneralSearchHistory domain) {
		this.commandProxy().insert(this.toEntity(domain));
	}

	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the object
	 */
	private Object toEntity(GeneralSearchHistory domain) {
		SptdtGenericSearchHist entity = new SptdtGenericSearchHist();
		domain.setMemento(entity);
		entity.setContractCd(AppContexts.user().contractCode());
		entity.setSearchDate(GeneralDateTime.now());
		return entity;
	}

	/**
	 * Update.
	 *
	 * @param domain the domain
	 */
	@Override
	public void update(GeneralSearchHistory domain) {
		this.commandProxy().update(this.toEntity(domain));
	}

	/**
	 * Delete.
	 *
	 * @param domain the domain
	 */
	@Override
	public void delete(GeneralSearchHistory domain) {
		SptdtGenericSearchHistPK pk = new SptdtGenericSearchHistPK(
				AppContexts.user().companyId(), 
				AppContexts.user().userId(), 
				domain.getSearchCategory().value, 
				domain.getContents().v());
		this.commandProxy().remove(SptdtGenericSearchHist.class, pk);
	}

	/**
	 * Gets the.
	 * [4] 取得する
	 * @param userID the user ID
	 * @param companyID the company ID
	 * @param searchCategory the search category
	 * @return the list
	 */
	@Override
	public List<GeneralSearchHistory> get(String userID, String companyID, int searchCategory) {
		return this.queryProxy()
				.query(QUERY_SELECT_LIST_ALL, SptdtGenericSearchHist.class)
				.setParameter("companyID", companyID)
				.setParameter("userID", userID)
				.setParameter("searchCategory", searchCategory)
				.getList(GeneralSearchHistory::createFromMemento);
	}

	/**
	 * Gets the last 10 used searches.
	 * [5]最近１０使った検索を 取得する
	 * @param userID the user ID
	 * @param companyID the company ID
	 * @param searchCategory the search category
	 * @return the last 10 used searches
	 */
	@Override
	@SneakyThrows
	public List<GeneralSearchHistory> getLast10UsedSearches(String userID, String companyID,
			int searchCategory) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String query = QUERY_SELECT_LAST_10_RESULTS;
		query = query.replaceAll("companyID", companyID);
		query = query.replaceAll("userID", userID);
		query = query.replaceAll("searchCategory", String.valueOf(searchCategory));
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			ResultSet rs = pstatement.executeQuery();
			List<GeneralSearchHistory> listResult = new NtsResultSet(rs)
					.getList(r -> {
						SptdtGenericSearchHist entity = new SptdtGenericSearchHist(
							SptdtGenericSearchHistPK.builder()
							.companyID(r.getString("CID"))
							.contents(r.getString("SEARCH_CONTENT"))
							.userID(r.getString("USER_ID"))
							.searchCategory(r.getInt("SEARCH_ATR"))
							.build(),
							r.getGeneralDateTime("SEARCH_DATE"));
					return GeneralSearchHistory.createFromMemento(entity);
					});
			if (listResult.isEmpty()) {
				return Collections.emptyList();
			} else {
				return listResult;
			}
		}
	}
	
	/**
	 * Gets the by contents.
	 * [6] 内容で取得する
	 * @param userID the user ID
	 * @param companyID the company ID
	 * @param searchCategory the search category
	 * @param searchContent the search content
	 * @return the by contents
	 */
	@Override
	public List<GeneralSearchHistory> getByContents(String userID, String companyID, int searchCategory,
			String searchContent) {
		return this.queryProxy()
				.query(QUERY_SELECT_BY_CONTENT, SptdtGenericSearchHist.class)
				.setParameter("companyID", companyID)
				.setParameter("userID", userID)
				.setParameter("searchCategory", searchCategory)
				.setParameter("contents", searchContent)
				.getList(GeneralSearchHistory::createFromMemento);
	}

}
