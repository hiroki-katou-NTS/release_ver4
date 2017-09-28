/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workingtype;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

/**
 * @author danpv
 *
 */
@Getter
public class ChangeableWorktypeGroup {

	private int no;
	
	private ChangeableWorkTypeGroupName name;
	
	private Set<String> workTypeList;
	
	public ChangeableWorktypeGroup(int no, String name) {
		this.no = no;
		this.name = new ChangeableWorkTypeGroupName(name);
		this.workTypeList = new HashSet<>();
	}
}
