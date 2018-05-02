package nts.uk.ctx.sys.assist.dom.category;

import java.util.Optional;
import java.util.List;

/**
* カテゴリ
*/
public interface CategoryRepository
{

    List<Category> getAllCategory();

    Optional<Category> getCategoryById();

    void add(Category domain);

    void update(Category domain);

    void remove();

	Optional<Category> getCategoryById(String categoryId);

	void remove(String categoryId);
	
	List<Category> findByAttendanceSystem();
	List<Category> findByPaymentAvailability();
	List<Category> findByPossibilitySystem();
	List<Category> findBySchelperSystem();
	List<Category> findByAttendanceSystemAndCodeName(String keySearch,List<String> categoriesIgnore);
	List<Category> findByPaymentAvailabilityAndCodeName(String keySearch,List<String> categoriesIgnore);
	List<Category> findByPossibilitySystemAndCodeName(String keySearch,List<String> categoriesIgnore);
	List<Category> findBySchelperSystemAndCodeName(String keySearch,List<String> categoriesIgnore);
}
