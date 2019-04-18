/**
 * 5:42:15 PM Aug 23, 2017
 */
package nts.uk.screen.at.app.monthlyperformance.correction.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.Data;
import nts.uk.ctx.at.record.dom.daily.dailyperformance.classification.EnumCodeName;

@Data
public class MPControlDisplayItem {
	// sheet dtos
	private List<MPSheetDto> lstSheet;
	// header dtos
	private List<MPHeaderDto> lstHeader;
	
	private List<MPAttendanceItem> lstAttendanceItem;
	
	private Set<String> formatCode;
	
	private List<ColumnSetting> columnSettings;
	
	private boolean settingUnit;
	
	private List<EnumCodeName> comboItemCalc;
	
	private List<EnumCodeName> comboItemDoWork;
	
	private List<EnumCodeName> comboItemReason;
	
	private List<Integer> itemIds;

	public MPControlDisplayItem() {
		super();
		this.lstSheet = new ArrayList<>();
		this.lstHeader = new ArrayList<>();
		this.columnSettings = new ArrayList<>();
		this.itemIds = new ArrayList<>();
	}
	
	private boolean isExistHeader(MPHeaderDto header) {
		for(MPHeaderDto exstHeader : this.lstHeader){
			if(exstHeader.getKey().equals(header.getKey())){
				return true;
			}
		}
		return false;
	}
	
	private boolean isExistSheet(MPSheetDto sheet){
		for(MPSheetDto exstSheet : this.lstSheet){
			if(exstSheet.getName().equals(sheet.getName())){
				return true;
			}
		}
		return false;
	}
	
	public void setLstHeader(List<MPHeaderDto> lstHeader) {
		for(MPHeaderDto header : lstHeader){
			if(!isExistHeader(header)){
				this.lstHeader.add(header);
			}
		}
	}

	public void createSheets(List<MPSheetDto> lstSheet) {
		for (MPSheetDto sheet : lstSheet) {
			if(!isExistSheet(sheet)){
				this.lstSheet.add(sheet);
			}
		}
		if(!this.lstSheet.isEmpty()) {
			this.lstSheet.sort((e1,e2) -> new BigDecimal(e1.getName()).compareTo(new BigDecimal(e2.getName())));
		}
	}

	public void setHeaderText(List<MPAttendanceItem> lstAttendanceItem) {
		lstAttendanceItem.stream().forEach(i -> {
			Optional<MPHeaderDto> header = this.getLstHeader().stream()
					.filter(h -> h.getKey().equals("A"+String.valueOf(i.getId()))).findFirst();
			if (header.isPresent()) {
				header.get().setHeaderText(i);
			}
		});
	}

	public void setHeaderColor(List<MPAttendanceItemControl> lstAttendanceItemControl) {
		lstAttendanceItemControl.stream().forEach(i -> {
			Optional<MPHeaderDto> header = this.getLstHeader().stream()
					.filter(h -> h.getKey().equals(String.valueOf("A"+i.getAttendanceItemId()))).findFirst();
			if (header.isPresent()) {
				header.get().setHeaderColor(i);
				if(!header.get().getGroup().isEmpty()){
					header.get().getGroup().get(0).setHeaderColor(i);	
					header.get().getGroup().get(1).setHeaderColor(i);	
				}
			}
		});
	}

	public void setColumnsAccessModifier(List<MPBusinessTypeControl> lstDPBusinessTypeControl) {
		lstDPBusinessTypeControl.stream().forEach(i -> {
			Optional<MPHeaderDto> header = this.getLstHeader().stream()
					.filter(h -> h.getKey().substring(1, h.getKey().length()).equals(String.valueOf(i.getAttendanceItemId()))).findFirst();
			if (header.isPresent()) {
				header.get().setChangedByOther(i.isChangedByOther());
				header.get().setChangedByYou(i.isChangedByYou());
			}
		});
	}

}
