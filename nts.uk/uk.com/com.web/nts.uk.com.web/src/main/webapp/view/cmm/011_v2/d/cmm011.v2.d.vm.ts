module nts.uk.com.view.cmm011.v2.d.viewmodel {
    // import getText = nts.uk.resource.getText;
    // import modal = nts.uk.ui.windows.sub.modal;
    // import info = nts.uk.ui.dialog.info;
    // import block = nts.uk.ui.block;
    // import alertError = nts.uk.ui.dialog.alertError;
    // import setShared = nts.uk.ui.windows.setShared;
    // import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {
        // itemList: KnockoutObservableArray<any>;
        // createMethod: KnockoutObservable<number>;

        // id: KnockoutObservable<string> = ko.observable(null);
        // code: KnockoutObservable<string> = ko.observable(null);
        // name: KnockoutObservable<string> = ko.observable(null);
        // displayName: KnockoutObservable<string> = ko.observable(null);
        // genericName: KnockoutObservable<string> = ko.observable(null);
        // externalCode: KnockoutObservable<string> = ko.observable(null);
        // hierarchyCode: KnockoutObservable<string> = ko.observable(null);

        // selectedHistoryId: string;
        // selectedCode: KnockoutObservable<string> = ko.observable(null);
        // selectedName: KnockoutObservable<string> = ko.observable(null);
        // selectedHierarchyCode: KnockoutObservable<string> = ko.observable(null);
        // itemsCount: KnockoutObservable<number> = ko.observable(0);
        // screenMode: number;
        // hierarchySiblings: number = 999;
        // hierarchyLevel: number = 10;
        // items: Array<any> = [];
        // listHierarchyChange: Array<any> = [];
        // listHierarchyChangeBak: Array<any> = [];
        // updateMode = false;

        // constructor() {
        //     block.invisible();
        //     let self = this, params = nts.uk.ui.windows.getShared("CMM011AParams");
        //     let currentScreen = nts.uk.ui.windows.getSelf();
        //     if (params) {
        //         self.screenMode = params.initMode;
        //         self.selectedCode(params.selectedCode);
        //         self.selectedName(params.selectedName);
        //         self.selectedHierarchyCode(params.selectedHierarchyCode);
        //         self.items = params.items;
        //         self.itemsCount(self.items.length);
        //         self.hierarchyLevel = params.selectedHierarchyCode.length / 3;
        //         self.hierarchySiblings = self.getSiblings(params.items, params.selectedHierarchyCode, self.hierarchyLevel).length;
        //         self.selectedHistoryId = params.history;
        //         self.listHierarchyChangeBak = params.listHierarchyChange;
        //     }
        //     if (self.itemsCount() == 0) {
        //         currentScreen.setHeight(400);
        //     }
        //     if (self.screenMode == SCREEN_MODE.DEPARTMENT) {
        //         currentScreen.setTitle(getText("CMM011_303"));
        //     }
        //     self.itemList = ko.observableArray([
        //         new BoxModel(CreationType.CREATE_ON_TOP,
        //             self.screenMode == SCREEN_MODE.WORKPLACE ? getText('CMM011_211') : getText('CMM011_311'),
        //             self.hierarchySiblings < 999
        //         ),
        //         new BoxModel(CreationType.CREATE_BELOW,
        //             self.screenMode == SCREEN_MODE.WORKPLACE ? getText('CMM011_212') : getText('CMM011_312'),
        //             self.hierarchySiblings < 999
        //         ),
        //         new BoxModel(CreationType.CREATE_TO_CHILD,
        //             self.screenMode == SCREEN_MODE.WORKPLACE ? getText('CMM011_213') : getText('CMM011_313'),
        //             self.hierarchyLevel < 10
        //         )
        //     ]);
        //     self.createMethod = ko.observable(self.hierarchySiblings < 999 ? CreationType.CREATE_BELOW : CreationType.CREATE_TO_CHILD);
        //     self.code.subscribe(value => {
        //         self.id(null);
        //         if (value && !_.isEmpty(self.selectedHistoryId) && !$("#A5_2").ntsError("hasError"))
        //             self.checkInputCode(value);
        //     });
        //     self.name.subscribe(value => {
        //         if (_.isEmpty(value) || $("#A6_2").ntsError("hasError")) 
        //             return;
        //         if (_.isEmpty(self.displayName())) {
        //             self.displayName(value);
        //         }
        //         if (_.isEmpty(self.genericName())) {
        //             self.genericName(self.getGenericName(value));
        //         }
        //         $(".nts-input").trigger("validate");
        //         $("#A5_2").trigger("keyup");
        //     });
        //     self.createMethod.subscribe(value => {
        //         let items = _.cloneDeep(self.items);
        //         self.listHierarchyChange = [];
        //         if (items.length == 0) {
        //             self.hierarchyCode("001");
        //         } else {
        //             let currentHierarchyCode = self.selectedHierarchyCode();
        //             if (self.createMethod() == CreationType.CREATE_TO_CHILD) {
        //                 items = self.getSiblings(items, currentHierarchyCode, self.hierarchyLevel);
        //                 let currNode = _.find(items, i => i.hierarchyCode == currentHierarchyCode);
        //                 let newHCode = currentHierarchyCode;
        //                 if (_.isEmpty(currNode.children))
        //                     newHCode = newHCode + "001";
        //                 else {
        //                     let childLevel = currNode.children[0].hierarchyCode.length / 3;
        //                     let lastChildNumber = Number(currNode.children[currNode.children.length - 1].hierarchyCode.substring(3 * (childLevel - 1)));
        //                     let newChildNumber = (lastChildNumber + 1) + "";
        //                     if (newChildNumber.length == 1) newChildNumber = "00"+ newChildNumber;
        //                     if (newChildNumber.length == 2) newChildNumber = "0"+ newChildNumber;
        //                     newHCode = newHCode + newChildNumber;
        //                 }
        //                 self.hierarchyCode(newHCode);
        //             } else {
        //                 let parentCode = currentHierarchyCode.substring(0, (self.hierarchyLevel - 1) * 3);
        //                 let newItems = [];
        //                 items = self.getSiblings(items, currentHierarchyCode, self.hierarchyLevel);
        //                 let currIndex = _.findIndex(items, i => i.hierarchyCode == currentHierarchyCode);
        //                 switch (self.createMethod()) {
        //                     case CreationType.CREATE_ON_TOP:
        //                         items.forEach((item, index) => {
        //                             if (index == currIndex) {
        //                                 newItems.push({ hierarchyCode: item.hierarchyCode, children: [] });
        //                             }
        //                             newItems.push(item);
        //                         });
        //                         break;
        //                     case CreationType.CREATE_BELOW:
        //                         currIndex += 1;
        //                         if (currIndex == items.length) {
        //                             newItems = items;
        //                             newItems.push({ hierarchyCode: "", children: [] });
        //                         } else {
        //                             items.forEach((item, index) => {
        //                                 if (index == currIndex) {
        //                                     newItems.push({ hierarchyCode: item.hierarchyCode, children: [] });
        //                                 }
        //                                 newItems.push(item);
        //                             });
        //                         }
        //                         break;
        //                     default:
        //                         break;
        //                 }
        //                 self.generateHierarchyCode(newItems, parentCode);
        //                 self.hierarchyCode(newItems[currIndex].hierarchyCode);
        //             }
        //         }
        //     });
        //     self.createMethod.valueHasMutated();
        //     block.clear();
        // }

        // getSiblings(items: Array<any>, currentHierarchyCode: string, hierarchyLevel: number): Array<any> {
        //     for (let i = 1; i < hierarchyLevel; i++) {
        //         let hCode = currentHierarchyCode.substring(0, 3 * i);
        //         let node = _.find(items, i => i.hierarchyCode == hCode);
        //         items = node.children;
        //     }
        //     return items;
        // }
        
        // checkInputCode(inputCode: string) {
        //     let self = this;
        //     block.invisible();
        //     service.checkCode(self.screenMode, self.selectedHistoryId, inputCode).done(checkResult => {
        //         if (checkResult.usedInThePast) {
        //             let params = {
        //                 initMode: self.screenMode,
        //                 listDuplicate: checkResult.listDuplicatePast
        //             };
        //             setShared("CMM011DParams", params);
        //             modal("/view/cmm/011_v2/c/index.xhtml").onClosed(() => {
        //                 let result = getShared("CMM011CParams");
        //                 if (result) {
        //                     if (result.targetId && result.historyId) {
        //                         self.id(result.targetId);
        //                         self.updateMode = true;
        //                         block.invisible();
        //                         service.getWkpDepInforById(self.screenMode, result.historyId, result.targetId).done(res => {
        //                             self.displayName(res.dispName);
        //                             self.genericName(res.genericName);
        //                             self.externalCode(res.externalCode);
        //                             self.name(res.name);
        //                             nts.uk.ui.errors.clearAll();
        //                         }).fail((error) => {
        //                             alertError(error);
        //                         }).always(() => {
        //                             block.clear();
        //                         });
        //                     } else {
        //                         self.updateMode = false;
        //                     }
        //                 } else {
        //                     self.code(null);
        //                     self.updateMode = false;
        //                 }
        //             });
        //         } else {
        //             self.updateMode = false;
        //         }
        //         block.clear();
        //     }).fail(error => {
        //         block.clear();
        //         alertError(error).then(() => {
        //             if (error.messageId == "Msg_3") {
        //                 self.code(null);
        //             }
        //         });
        //     });
        // }
        
        // register() {
        //     let self = this;
        //     $(".nts-input").trigger("validate");
        //     $("#A5_2").trigger("keyup");
        //     if (nts.uk.ui.errors.hasError()) 
        //         return;
        //     block.invisible();
        //     self.listHierarchyChange.forEach(i => {
        //         self.listHierarchyChangeBak = self.listHierarchyChangeBak.filter(b => b.id != i.id);
        //         self.listHierarchyChangeBak.push(i);
        //     });
        //     let command = {
        //         initMode: self.screenMode,
        //         historyId: self.selectedHistoryId,
        //         id: self.id(),
        //         code: self.code(),
        //         name: self.name(),
        //         dispName: self.displayName(),
        //         genericName: self.genericName(),
        //         externalCode: self.externalCode(),
        //         hierarchyCode: self.hierarchyCode(), 
        //         listHierarchyChange: self.listHierarchyChangeBak,
        //         updateMode: self.updateMode
        //     };
        //     service.registerWkpDepInfor(command).done((id) => {
        //         info({ messageId: "Msg_15" }).then(() => {
        //             setShared("CreatedWorkplace", { idToSelect: id });
        //             nts.uk.ui.windows.close();
        //         });
        //     }).fail(error => {
        //         alertError(error);
        //     }).always(() => {
        //         block.clear();
        //     });
        // }

        // cancel() {
        //     nts.uk.ui.windows.close();
        // }
        
        // generateHierarchyCode(items: Array<any>, parentHierarchyCode: string) {
        //     let self = this;
        //     items.forEach((node, index) => {
        //         let hCode = ++index + "";
        //         if (hCode.length == 1) hCode = "00"+ hCode;
        //         if (hCode.length == 2) hCode = "0"+ hCode;
        //         let newHierarchyCode = parentHierarchyCode + hCode;
        //         if (node.hierarchyCode != newHierarchyCode) {
        //             node.hierarchyCode = newHierarchyCode;
        //             self.listHierarchyChange = self.listHierarchyChange.filter(i => i.id != node.id);
        //             self.listHierarchyChange.push({ id: node.id, hierarchyCode: node.hierarchyCode });
        //         }
        //         self.generateHierarchyCode(node.children, node.hierarchyCode);
        //     })
        // }
        
        // getGenericName(value: string): string {
        //     let self = this, result = "";
        //     let currentHierarchyCode = self.hierarchyCode();
        //     if (currentHierarchyCode) {
        //         let level = Math.floor(currentHierarchyCode.length/3);
        //         let items = _.cloneDeep(self.items);
        //         for (let i = 1; i < level; i++) {
        //             let hCode = currentHierarchyCode.substring(0, 3*i);
        //             let node = _.find(items, i => i.hierarchyCode == hCode);
        //             result = result + node.name + " ";
        //             items = node.children;
        //         }
        //     }
        //     return result + value;
        // }
        
    }

    enum SCREEN_MODE {
        WORKPLACE = 0,
        DEPARTMENT = 1
    }

    enum CreationType {
        CREATE_ON_TOP = 1,
        CREATE_BELOW = 2,
        CREATE_TO_CHILD = 3
    }

    class BoxModel {
        id: number;
        name: string;
        enable: KnockoutObservable<boolean>;

        constructor(id, name, enable) {
            var self = this;
            self.id = id;
            self.name = name;
            self.enable = ko.observable(enable);
        }
    }

    class RegisterInfor {
        id: string;
        code: string;
        name: string;
        displayName: string;
        genericName: string;
        externalCode: string;
        hierarchyCode: string;

        constructor(id: string, code: string, name: string, displayName: string, genericName: string, externalCode: string, hierarchyCode: string) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.displayName = displayName;
            this.genericName = genericName;
            this.externalCode = externalCode;
            this.hierarchyCode = hierarchyCode;
        }
    }
}