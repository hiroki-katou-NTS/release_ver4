module kdl002.a.viewmodel {
    export class ScreenModel {
        isShowNoSelectRow:boolean;
        isAcceptSelectNone:boolean;
        isMulti: boolean;
        items: KnockoutObservableArray<model.WorkTypeInfor>;
        columns: KnockoutObservableArray<any>;
        currentCodeList: KnockoutObservableArray<any>;
        posibleItems: Array<string>;
        dataSoure: Array<model.WorkTypeInfor>;

        constructor() {
            var self = this;
            self.isShowNoSelectRow = false;
            self.isAcceptSelectNone = false;
            self.isMulti = true;
            self.items = ko.observableArray([]);
            //header
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText("KDL002_3"), prop: 'workTypeCode', width: 70 },
                { headerText: nts.uk.resource.getText("KDL002_4"), prop: 'name', width: 200 ,formatter: _.escape},
                { headerText: nts.uk.resource.getText("KDL002_5"), prop: 'memo', width: 210 ,formatter: _.escape}
            ]);
            self.currentCodeList = ko.observableArray([]);
            self.posibleItems = [];
            self.start();
        }
        //load data
        start() {
            var self = this;
            
            self.isShowNoSelectRow = nts.uk.ui.windows.getShared('KDL002_isShowNoSelectRow');
            self.isAcceptSelectNone = nts.uk.ui.windows.getShared('KDL002_isAcceptSelectNone');
            self.isMulti = nts.uk.ui.windows.getShared('KDL002_Multiple');
            //all possible items
            self.posibleItems = nts.uk.ui.windows.getShared('KDL002_AllItemObj');
            //selected items
            var selectCode = nts.uk.ui.windows.getShared('KDL002_SelectedItemId');
            
            //set source
            if(self.posibleItems == null || self.posibleItems === undefined){
                self.items();
                return;
            }
            if (self.posibleItems.length > 0) {
                service.getItemSelected(self.posibleItems).done(function(lstItem: Array<model.WorkTypeInfor>) {
                    $("input").focus();
                    let lstItemMapping =  _.map(lstItem , item => {
                        return new model.WorkTypeInfor(item.workTypeCode, item.name, item.memo, item.dispOrder);
                    });
                    self.initNotSelectItem(!self.isMulti, lstItemMapping);
                    self.items(lstItemMapping);
                    self.currentCodeList(selectCode);
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
            }
        }

        initNotSelectItem(isSingle: boolean, data: any) {
            let self = this;
            if (!isSingle) return;
            let noSelectItem = {
                memo: '',
                name: nts.uk.resource.getText('KDL002_9'),
                workTypeCode: ''
            };

            // Check is show no select row.
            if (self.isShowNoSelectRow) {
                data.unshift(noSelectItem);
            }
        }
        //event When click to 決定 ボタン
        register() {
            var self = this;
            if(self.isMulti == true){
                let lstObj : any[] = [];
                for (let i =0, length = self.currentCodeList().length; i< length ;i++) {
                    let objectNew = self.findItem(self.currentCodeList()[i]);
                    if(objectNew != undefined && objectNew != null){
                        lstObj.push({"code": objectNew.workTypeCode, "name":objectNew.name});
                    }
                }
               if (!self.isAcceptSelectNone && lstObj.length == 0) {
                   nts.uk.ui.dialog.alertError({ messageId: "Msg_10"});
                   return;
               }
                let lstItem2 = _.orderBy(lstObj,['code'],['asc']);
                nts.uk.ui.windows.setShared('KDL002_SelectedNewItem', lstItem2);
            }else{
                let lstObj2 : any[] = [];
                let objectNew2 = self.findItem(self.currentCodeList());
                if(objectNew2 != undefined && objectNew2 != null){
                    lstObj2.push({ "code": objectNew2.workTypeCode, "name":objectNew2.name});
                }else{
                   nts.uk.ui.dialog.alertError({ messageId: "Msg_10"});
                   return;
                }    
                nts.uk.ui.windows.setShared('KDL002_SelectedNewItem', lstObj2);
            }
            nts.uk.ui.windows.setShared('KDL002_IsCancel', false);
            nts.uk.ui.windows.close();
        }
        /**
         * find item is selected
         */
        findItem(value: any): model.WorkTypeInfor {
            var self = this;
            return _.find(self.items(), function(obj: model.WorkTypeInfor) {
                return obj.workTypeCode == value;
            })
        }
        //Close Dialog
        close() {
            var self = this;
            let selectCode = nts.uk.ui.windows.getShared('KDL002_SelectedItemId');
            if(self.isMulti == true){
                let lstObj : any[] = [];
                for (let i =0, length = selectCode.length; i< length ;i++) {
                    let objectNew = self.findItem(selectCode[i]);
                    if(objectNew != undefined && objectNew != null){
                        lstObj.push({"code": objectNew.workTypeCode, "name":objectNew.name});
                    }
                }
                let lstItem2 = _.orderBy(lstObj,['code'],['asc']);
                nts.uk.ui.windows.setShared('KDL002_SelectedNewItem', lstItem2,true);
            }else{
                let lstCancel : any[] = [];
                if(selectCode != null && selectCode !== undefined){
                    let objectNew2 = self.findItem(selectCode);
                    if(objectNew2 != undefined && objectNew2 != null){
                        lstCancel.push({ "code": objectNew2.workTypeCode, "name":objectNew2.name});
                    }
                }
                nts.uk.ui.windows.setShared('KDL002_SelectedNewItem', lstCancel,true);
            }
            nts.uk.ui.windows.setShared('KDL002_IsCancel', true);
            nts.uk.ui.windows.close();
        }
    }
    export module model {
        export class WorkTypeInfor {
            workTypeCode: string;
            name: string;
            memo: string;
            dispOrder: number;
            constructor(workTypeCode: string, name: string, memo: string, dispOrder: number) {
                this.workTypeCode = workTypeCode;
                this.name = name;
                this.memo = memo;
                this.dispOrder = dispOrder;
            }
        }
    
    }
}