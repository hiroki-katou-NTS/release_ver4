module cmm001.e.viewmodel {
    export class ScreenModel {
        
        dataSource: KnockoutObservableArray<model.CopyItem>;
        selectedCopyItems : KnockoutObservableArray<model.CopyItem>;
        switchOptions : KnockoutObservableArray<any>;
        isCheckedAll : KnockoutObservable<boolean>;
        switchHeader : KnockoutObservable<number>;
        
        constructor() {  
            let self = this;
            
            self.switchOptions = ko.observableArray([{ copyMethod: '0', text: nts.uk.resource.getText('CMM001_70') }, { copyMethod: '1', text: nts.uk.resource.getText('CMM001_71') }, { copyMethod: '2', text: nts.uk.resource.getText('CMM001_72') }]);
            self.isCheckedAll = ko.observable(false);
            self.isCheckedAll.subscribe((value) => {
               ko.utils.arrayForEach(self.dataSource(), function(item) {
               item.flag(value);
                });
            });
            self.switchHeader = ko.observable(0);
            self.switchHeader.subscribe((value) => {
                ko.utils.arrayForEach(self.dataSource(), function(item) {
                item.copyMethod(value);
                })
            });
            self.dataSource = ko.observableArray([]);
            self.selectedCopyItems = ko.observableArray([]);
            self.selectedCopyItems = ko.computed(function() {
                return self.dataSource().filter(function(item) {
                    return item.flag() === true;
                });
            });

        }
        
        start() : JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            var cid = nts.uk.ui.windows.getShared('companyId');
            
            service.getAllMasterCopyCategory().then(function(masterCopyCateList: Array<model.MasterCopyCategory>) {
                /* fake data */
                /*items = (function() {
                    var list = [];
                    for (var i = 0; i < 5; i++) {
                        list.push(new model.MasterCopyCategory('Common', 'ID' + i, 'Name', i));
                    }
                    for (var i = 5; i < 10; i++) {
                        list.push(new model.MasterCopyCategory('Salary', 'ID' + i, 'Name', i));
                    }
                    return list;
                })();
                masterCopyCateList = items;*/
                var copyItemList: model.CopyItem[];
                copyItemList = [];
                var preSystemType = '';
                var i : number;
                var num :number = masterCopyCateList.length;
                var itemData : model.MasterCopyCategory;
                var nextItem : model.MasterCopyCategory;
                for(itemData of masterCopyCateList) {
                    if (itemData.systemType === preSystemType) {
                        copyItemList.push(new model.CopyItem(itemData.systemType, itemData.masterCopyId, itemData.masterCopyCategory, itemData.order, true));
                    } else {
                        copyItemList.push(new model.CopyItem(itemData.systemType, itemData.masterCopyId, itemData.masterCopyCategory, itemData.order, false));
                    }
                    preSystemType = itemData.systemType;
                }
                self.dataSource(copyItemList);
                dfd.resolve();
                
            });
            return dfd.promise();
        }
        
        openFDialog() {
            var self = this;
            var selectedItems : model.CopyItem[];
            selectedItems = self.selectedCopyItems();
            if (selectedItems.length == 0) {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_1145" })
            }
            else {
                nts.uk.ui.dialog.confirm({ messageId: "Msg_1162" }).ifYes(() => {
                    var cid = nts.uk.ui.windows.getShared('companyId');
                    nts.uk.ui.windows.setShared('companyId', cid);
                    nts.uk.ui.windows.setShared('copyItemList', selectedItems);
                    nts.uk.ui.windows.sub.modal('/view/cmm/001/f/index.xhtml', { title: '', }).onClosed(function(): any {
                    });
                });
            }
        }
        
        closeDialog() {
            nts.uk.ui.windows.close();
        }
    }
    
    export module model {
        export class MasterCopyCategory {
            systemType: string;
            masterCopyId: string;
            masterCopyCategory: string;
            order: number;
            constructor(systemType: string, masterCopyId: string, masterCopyCategory: string, order: number) {
                this.systemType = systemType;
                this.masterCopyId = masterCopyId;
                this.masterCopyCategory = masterCopyCategory;
                this.order = order;
            }
        } 
        export class CopyItem {
            flag : KnockoutObservable<boolean>;
            copyMethod : KnockoutObservable<number>;
            systemType: string;
            masterCopyId: string;
            masterCopyCategory: string;
            order: number;
            isBorder : boolean;
            constructor(systemType: string, masterCopyId: string, masterCopyCategory: string, order: number, isBorder :boolean) {
                this.flag = ko.observable(false);
                this.copyMethod = ko.observable(0);
                this.systemType = systemType;
                this.masterCopyId = masterCopyId;
                this.masterCopyCategory = masterCopyCategory;
                this.order = order;
                this.isBorder = isBorder;
            }
            
        }
    }
}