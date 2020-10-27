package nts.uk.ctx.sys.portal.infra.entity.webmenu;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SPTMT_MENU_BAR")
public class SptmtMenuBar extends ContractUkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    public SptmtMenuBarPK sptmtMenuBarPK;
		
	@Column(name = "MENU_BAR_NAME")
	public String  menuBarName;
	
	@Column(name = "SELECTED_ATR")
	public int selectedAtr;
	
	@Column(name = "SYSTEM")
	public int system;
	
	@Column(name = "MENU_CLS")
	public int menuCls;
	
	@Column(name = "CODE")
	public String code;
	
	@Column(name = "BACKGROUND_COLOR")
	public String  backgroundColor;
	
	@Column(name = "TEXT_COLOR")
	public String textColor;
	
	@Column(name = "DISPLAY_ORDER")
	public int displayOrder;
	
	
	@ManyToOne
	@JoinColumns( {
        @JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
        @JoinColumn(name = "WEB_MENU_CD", referencedColumnName = "WEB_MENU_CD", insertable = false, updatable = false)
    })
	public SptmtWebMenu webMenu;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="menuBar", orphanRemoval = true)
	public List<SptmtTitleBar> titleMenus;
	
	@Override
	protected Object getKey() {
		return sptmtMenuBarPK;
	}

	public SptmtMenuBar(SptmtMenuBarPK sptmtMenuBarPK, String menuBarName, int selectedAtr,
			int system, int menuCls, String code, String backgroundColor, String textColor, int displayOrder,
			List<SptmtTitleBar> titleMenus) {
		super();
		this.sptmtMenuBarPK = sptmtMenuBarPK;
		this.menuBarName = menuBarName;
		this.selectedAtr = selectedAtr;
		this.system = system;
		this.menuCls = menuCls;
		this.code = code;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
		this.displayOrder = displayOrder;
		this.titleMenus = titleMenus;
	}

}
