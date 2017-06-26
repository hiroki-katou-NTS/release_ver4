package nts.uk.ctx.basic.dom.organization.shr;

import lombok.Getter;

@Getter
public class HierarchyLevelCd {

	private HierarchyLevelCode hierarchyCd01;

	private HierarchyLevelCode hierarchyCd02;

	private HierarchyLevelCode hierarchyCd03;

	private HierarchyLevelCode hierarchyCd04;

	private HierarchyLevelCode hierarchyCd05;

	private HierarchyLevelCode hierarchyCd06;

	private HierarchyLevelCode hierarchyCd07;

	private HierarchyLevelCode hierarchyCd08;

	private HierarchyLevelCode hierarchyCd09;

	private HierarchyLevelCode hierarchyCd10;

	public HierarchyLevelCd(HierarchyLevelCode hierarchyCd01, HierarchyLevelCode hierarchyCd02,
			HierarchyLevelCode hierarchyCd03, HierarchyLevelCode hierarchyCd04, HierarchyLevelCode hierarchyCd05,
			HierarchyLevelCode hierarchyCd06, HierarchyLevelCode hierarchyCd07, HierarchyLevelCode hierarchyCd08,
			HierarchyLevelCode hierarchyCd09, HierarchyLevelCode hierarchyCd10) {
		this.hierarchyCd01 = hierarchyCd01;
		this.hierarchyCd02 = hierarchyCd02;
		this.hierarchyCd03 = hierarchyCd03;
		this.hierarchyCd04 = hierarchyCd04;
		this.hierarchyCd05 = hierarchyCd05;
		this.hierarchyCd06 = hierarchyCd06;
		this.hierarchyCd07 = hierarchyCd07;
		this.hierarchyCd08 = hierarchyCd08;
		this.hierarchyCd09 = hierarchyCd09;
		this.hierarchyCd10 = hierarchyCd10;
	}

}
