package nts.uk.ctx.office.app.command.favorite;

import java.util.List;

import lombok.Data;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.office.dom.favorite.FavoriteSpecify;

/*
 * お気に入りの指定 Command
 */
@Data
public class FavoriteSpecifyCommand implements FavoriteSpecify.MementoGetter {
	// お気に入り名
	private String favoriteName;

	// 作��D
	private String creatorId;

	// 入力日
	private GeneralDateTime inputDate;

	// 対象選�
	private Integer targetSelection;

	// 職場ID
	private List<String> workplaceId;

	// 頺
	private Integer order;

}
