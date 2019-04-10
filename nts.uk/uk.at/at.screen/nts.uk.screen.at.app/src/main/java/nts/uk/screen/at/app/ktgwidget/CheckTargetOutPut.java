package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 
 * @author tutk
 *
 */

@Getter
@Setter
@NoArgsConstructor
public class CheckTargetOutPut {
	List<CheckTargetItem> listCheckTargetItem = new ArrayList<>();

	public CheckTargetOutPut(List<CheckTargetItem> listCheckTargetItem) {
		super();
		this.listCheckTargetItem = listCheckTargetItem;
	}
	
	
}
