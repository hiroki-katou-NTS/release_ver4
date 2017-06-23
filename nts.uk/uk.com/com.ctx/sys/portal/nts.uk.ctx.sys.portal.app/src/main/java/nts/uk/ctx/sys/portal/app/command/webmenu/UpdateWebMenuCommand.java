package nts.uk.ctx.sys.portal.app.command.webmenu;


import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdateWebMenuCommand {

	private String webMenuCode;

	private String webMenuName;

	private int defaultMenu;

	private List<MenuBarCommand> menuBars;
}
