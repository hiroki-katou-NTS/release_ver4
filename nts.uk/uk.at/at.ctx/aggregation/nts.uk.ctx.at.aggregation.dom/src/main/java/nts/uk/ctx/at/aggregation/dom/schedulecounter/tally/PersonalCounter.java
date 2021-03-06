package nts.uk.ctx.at.aggregation.dom.schedulecounter.tally;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.objecttype.DomainAggregate;

/**
 * 個人計
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.予実集計.スケジュール集計.職場計・個人計.個人計
 * @author dan_pv
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PersonalCounter implements DomainAggregate {

	/**
	 * 利用カテゴリ一覧
	 */
	private List<PersonalCounterCategory> useCategories = new ArrayList<>();

	/**
	 * @param useCategories
	 * @return
	 */
	public static PersonalCounter create(List<PersonalCounterCategory> useCategories) {

		if ( useCategories.size() != new HashSet<>(useCategories).size() ) {
			throw new RuntimeException("the list is duplicated");
		}

		return new PersonalCounter(useCategories);
	}

	/**
	 * 利用されているか
	 * @param category　チェックしたい個人計カテゴリ
	 * @return
	 */
	public boolean isUsed(PersonalCounterCategory category) {

		return this.useCategories.contains(category);
	}


}
