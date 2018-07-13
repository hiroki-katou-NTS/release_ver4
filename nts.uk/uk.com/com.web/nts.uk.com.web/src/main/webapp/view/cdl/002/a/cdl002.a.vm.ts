module nts.uk.com.view.cdl002.a {
    import close = nts.uk.ui.windows.close;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import SelectType = kcp.share.list.SelectType;
    export module viewmodel {
        export class ScreenModel {
            selectedMulEmployment: KnockoutObservableArray<string>;
            selectedSelEmployment: KnockoutObservable<string>;
            isMultiSelect: KnockoutObservable<boolean>;
            isDisplayUnselect: KnockoutObservable<boolean>;
            isShowWorkClosure: KnockoutObservable<boolean>;
            isMultipleUse: boolean;
            
            listComponentOption: any;
            
            constructor() {
                let self = this;
                var params = getShared('CDL002Params');
                self.selectedMulEmployment = ko.observableArray([]);
                self.selectedSelEmployment = ko.observable('');
                self.isMultiSelect = ko.observable(params.isMultiple);
                self.isShowWorkClosure = ko.observable(false);
                 self.isMultipleUse = false;
                if (self.isMultiSelect()) {
                    self.selectedMulEmployment(params.selectedCodes ? params.selectedCodes : []);
                }
                else {
                    self.selectedSelEmployment(params.selectedCodes);
                }
                
                // If Selection Mode is Multiple Then not show Unselected Row
                self.isDisplayUnselect = ko.observable(self.isMultiSelect() ? false : params.showNoSelection);
                
                // Set value for Multiple Use by isShowWorkClosure 
                if(_.isNil(params.isShowWorkClosure)){
                    self.isMultipleUse = true;                             
                } else{
                    self.isMultipleUse = params.isShowWorkClosure ? true : false;    
                }
                
                
                // Initial listComponentOption
                self.listComponentOption = {
                    isMultiSelect: self.isMultiSelect(),
                    isDisplayClosureSelection: self.isMultipleUse,
                    listType: ListType.EMPLOYMENT,
                    selectType: SelectType.SELECT_BY_SELECTED_CODE,
                    selectedCode: null,
                    isDialog: true,
                    isShowNoSelectRow: self.isDisplayUnselect(),
                    maxRows: 10,
                    tabindex: 1
                };
                if (self.isMultiSelect()) {
                    self.listComponentOption.selectedCode = self.selectedMulEmployment;
                }
                else {
                    self.listComponentOption.selectedCode = self.selectedSelEmployment;
                }
            }

            /**
             * Close dialog.
             */
            closeDialog(): void {
                setShared('CDL002Cancel', true);
                nts.uk.ui.windows.close();
            }

            /**
             * Decide Employment
             */
            decideData = () => {
                let self = this;
                if(self.isMultiSelect() && self.selectedMulEmployment().length == 0) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_640" }).then(() => nts.uk.ui.windows.close());
                    return;
                }
                var isNoSelectRowSelected = $("#jobtitle").isNoSelectRowSelected();
                if (!self.isMultiSelect() && !self.selectedSelEmployment() && !isNoSelectRowSelected) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_640" }).then(() => nts.uk.ui.windows.close());
                    return;
                }
                setShared('CDL002Output', self.isMultiSelect() ? self.selectedMulEmployment() : self.selectedSelEmployment());
                close();
            }
            
            /**
             * function check employment
             */
            public checkExistEmployment(code: string, data: UnitModel[]): boolean {
                for (var item of data) {
                    if (code === item.code) {
                        return true;
                    }
                }
                return false;
            }
        }
        
        /**
        * List Type
        */
        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }
        
    }
}