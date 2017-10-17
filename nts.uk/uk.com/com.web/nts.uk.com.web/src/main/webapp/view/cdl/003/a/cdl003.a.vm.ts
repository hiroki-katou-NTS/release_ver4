module nts.uk.com.view.cdl003.a {

    import ListType = kcp.share.list.ListType;
    import SelectType = kcp.share.list.SelectType;
    import UnitModel = kcp.share.list.UnitModel;
    import ComponentOption = kcp.share.list.ComponentOption;

    export module viewmodel {
        /**
        * Screen Model.
        */
        export class ScreenModel {
            selectedMulClassification: KnockoutObservableArray<string>;
            selectedSelClassification: KnockoutObservable<string>;
            classifications: ComponentOption;
            isMultiple: boolean;
            isShowNoSelectRow: boolean;
            constructor(){
                var self = this;
                self.selectedMulClassification = ko.observableArray([]);
                self.selectedSelClassification = ko.observable('');
                self.isMultiple = false;
                self.isShowNoSelectRow = false;
                var inputCDL003 = nts.uk.ui.windows.getShared('inputCDL003');
                if(inputCDL003){
                    self.isMultiple = inputCDL003.isMultiple;
                    self.isShowNoSelectRow = inputCDL003.showNoSelection;
                    if (self.isMultiple) {
                        self.selectedMulClassification(inputCDL003.canSelected);
                    }   
                    else {
                        self.selectedSelClassification(inputCDL003.canSelected);
                    } 
                }
                
                self.classifications = {
                    isShowAlreadySet: false,
                    isMultiSelect: self.isMultiple,
                    listType: ListType.Classification,
                    selectType: SelectType.SELECT_BY_SELECTED_CODE,
                    isShowNoSelectRow:  self.isShowNoSelectRow,
                    selectedCode: null,
                    isDialog: true,
                    maxRows: 10,
                    tabindex: 1
                }
                if (self.isMultiple) {
                    self.classifications.selectedCode = self.selectedMulClassification;
                }
                else {
                    self.classifications.selectedCode = self.selectedSelClassification;
                }
            }
            
            /**
             * function on click button selected classification
             */
            private selectedClassification() :void {
                var self = this;
                var dataList: UnitModel[] = $("#classification").getDataList();
                if(self.isMultiple){
                    var selectedCodes: string[] = self.getSelectByMul(self.selectedMulClassification(), dataList);
                    if(!selectedCodes || selectedCodes.length == 0){
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_641" }).then(() => nts.uk.ui.windows.close());
                        return;
                    }
                    nts.uk.ui.windows.setShared('outputCDL003', { selectedCode: selectedCodes });
                    nts.uk.ui.windows.close();    
                }else {
                    var selectedCode: string = self.getSelectBySel(self.selectedSelClassification(), dataList);
                    var isNoSelectRowSelected = $("#classification").isNoSelectRowSelected();
                    if(!selectedCode && !isNoSelectRowSelected){
                        // Check if selected No select Row.
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_641" }).then(() => nts.uk.ui.windows.close());
                        return;
                    }
                    nts.uk.ui.windows.setShared('outputCDL003', { selectedCode: isNoSelectRowSelected ? null : selectedCode});
                    nts.uk.ui.windows.close();    
                }
                
            }
            
            /**
             * check selected code
             */
            private getSelectBySel(selected: string, selectedCodes: UnitModel[]): string {
                let a = _.find(selectedCodes, x => {
                    return x.code === selected
                });
                if (a) {
                    return a.code;
                }
                return undefined;
            }
            /**
             * check selected array code
             */
            private getSelectByMul(selected: string[], selectedCodes: UnitModel[]): string[] {
                var resSeleted: string[] = [];
                for (var selectedCode of selected) {
                    if (this.getSelectBySel(selectedCode, selectedCodes)) {
                        resSeleted.push(selectedCode);
                    }
                }

                return resSeleted;
            }
            /**
             * close windows
             */
            private closeWindows(): void{
                nts.uk.ui.windows.close();  
            }
        }
    }
}