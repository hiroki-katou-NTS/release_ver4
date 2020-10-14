package nts.uk.ctx.sys.portal.app.find.generalsearch;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import nts.uk.ctx.sys.portal.dom.generalsearch.GeneralSearchRepository;

/**
 * The Class GeneralSearchHistoryFinder.
 */
public class GeneralSearchHistoryFinder {

	/** The repo. */
	@Inject
	private GeneralSearchRepository repo;
	
	/**
	 * Gets the.
	 *
	 * @param userID the user ID
	 * @param companyID the company ID
	 * @param searchCategory the search category
	 * @return the list
	 */
	public List<GeneralSearchHistoryDto> get(String userID, String companyID, int searchCategory) {
		return this.repo.get(userID, companyID, searchCategory).stream()
				.map(item -> GeneralSearchHistoryDto.builder()
						.companyID(item.getCompanyID())
						.searchCategory(item.getSearchCategory().value)
						.searchDate(item.getSearchDate())
						.userID(item.getUserID())
						.contents(item.getContents().toString())
						.build())
				.collect(Collectors.toList());
	}
	
	/**
	 * Gets the last 10 used searches.
	 * 最近10使った検索を取得する
	 * @param userID the user ID
	 * @param companyID the company ID
	 * @param searchCategory the search category
	 * @return the last 10 used searches
	 */
	public List<GeneralSearchHistoryDto> getLast10UsedSearches(String userID, String companyID, int searchCategory) {
		return this.repo.getLast10UsedSearches(userID, companyID, searchCategory).stream()
				.map(item -> GeneralSearchHistoryDto.builder()
						.companyID(item.getCompanyID())
						.searchCategory(item.getSearchCategory().value)
						.searchDate(item.getSearchDate())
						.userID(item.getUserID())
						.contents(item.getContents().toString())
						.build())
				.collect(Collectors.toList());
	}
	

	/**
	 * Gets the by contents.
	 *
	 * @param userID the user ID
	 * @param companyID the company ID
	 * @param searchCategory the search category
	 * @param searchContent the search content
	 * @return the by contents
	 */
	public List<GeneralSearchHistoryDto> getByContents(String userID, String companyID, int searchCategory, String searchContent) {
		return this.repo.getByContents(userID, companyID, searchCategory, searchContent).stream()
				.map(item -> GeneralSearchHistoryDto.builder()
						.companyID(item.getCompanyID())
						.searchCategory(item.getSearchCategory().value)
						.searchDate(item.getSearchDate())
						.userID(item.getUserID())
						.contents(item.getContents().toString())
						.build())
				.collect(Collectors.toList());
	}
}
