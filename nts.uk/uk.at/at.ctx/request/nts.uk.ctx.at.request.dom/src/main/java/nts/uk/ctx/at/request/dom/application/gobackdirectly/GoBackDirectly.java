package nts.uk.ctx.at.request.dom.application.gobackdirectly;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumConstant;
import nts.uk.ctx.at.request.dom.application.Application;


@Getter
@Setter
//直行直帰申請
public class GoBackDirectly extends Application {

	// 直帰区分
	private EnumConstant straightDistinction;
	// 直行区分
	private EnumConstant straightLine;
	// 勤務を変更する
	private Optional<EnumConstant> isChangedWork = Optional.empty();
	// 勤務情報
	private Optional<DataWork> dataWork = Optional.empty();

	public GoBackDirectly() {
		
	}

	// 申請内容
	public String getContent() {
		return null;
	}

}
