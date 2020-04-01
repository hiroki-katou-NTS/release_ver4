module nts.uk.com.view.cmm011.v2.a.viewmodel {
    // import block = nts.uk.ui.block;
    // import getText = nts.uk.resource.getText;
    // import confirm = nts.uk.ui.dialog.confirm;
    // import alertError = nts.uk.ui.dialog.alertError;
    // import info = nts.uk.ui.dialog.info;
    // import modal = nts.uk.ui.windows.sub.modal;
    // import setShared = nts.uk.ui.windows.setShared;
    // import getShared = nts.uk.ui.windows.getShared;
    // import queryString = nts.uk.request.location.current.queryString;

    // const LENGTH_HIERARCHY_ORIGIN = 3;

    export class ScreenModel {

//         initMode: number = SCREEN_MODE.WORKPLACE;
//         isUpdateMode: boolean = false;
//         configuration: KnockoutObservable<WkpDepConfig>;
//         items: KnockoutObservableArray<WkpDepNode>;
//         selectedId: KnockoutObservable<string>;
//         selectedCode: KnockoutObservable<string> = ko.observable(null);
//         selectedName: KnockoutObservable<string> = ko.observable(null);
//         selectedDispName: KnockoutObservable<string> = ko.observable(null);
//         selectedGenericName: KnockoutObservable<string> = ko.observable(null);
//         selectedHierarchyCode: KnockoutObservable<string> = ko.observable("");
//         selectedExternalCode: KnockoutObservable<string> = ko.observable(null);
//         isSynchronized: KnockoutObservable<boolean> = ko.observable(false);
//         listHierarchyChange: Array<any> = [];
//         needRegenerateHierarchyCode: boolean = false;
//         backupCode: string = null;

//         constructor() {
//             let self = this;
//             if (!_.isEmpty(queryString.items)) {
//                 self.initMode = Number(queryString.items["initmode"]);
//             }
//             self.configuration = ko.observable(new WkpDepConfig(null, null, null));
//             self.items = ko.observableArray([]);
//             self.selectedId = ko.observable(null);
//             self.selectedId.subscribe(value => {
//                 if (_.isEmpty(value)) {
//                     self.backupCode = null;
//                     self.selectedCode(null);
//                     self.selectedDispName(null);
//                     self.selectedGenericName(null);
//                     self.selectedHierarchyCode("");
//                     self.selectedExternalCode(null);
//                     self.selectedName(null);
//                     nts.uk.ui.errors.clearAll();
//                 } else {
//                     block.invisible();
//                     service.getWkpDepInforById(self.initMode, self.configuration().historyId(), value).done(res => {
//                         self.backupCode = res.code;
//                         self.selectedCode(res.code);
//                         self.selectedDispName(res.dispName);
//                         self.selectedGenericName(res.genericName);
//                         self.selectedHierarchyCode(res.hierarchyCode);
//                         self.selectedExternalCode(res.externalCode);
//                         self.selectedName(res.name);
//                         nts.uk.ui.errors.clearAll();
//                         $("#A5_2").focus();
//                     }).fail((error) => {
//                         alertError(error);
//                     }).always(() => {
//                         block.clear();
//                     });
//                 }
//             });
//             self.selectedName.subscribe(value => {
//                 if (_.isEmpty(value) || $("#A6_2").ntsError("hasError")) 
//                     return;
//                 if (_.isEmpty(self.selectedDispName())) {
//                     self.selectedDispName(value);
//                 }
//                 if (_.isEmpty(self.selectedGenericName())) {
//                     self.selectedGenericName(self.getGenericName(value));
//                 }
//                 $(".nts-input").trigger("validate");
//                 $("#A5_2").trigger("keyup");
//             });
//             self.selectedCode.subscribe(value => {
//                 if (!_.isEmpty(self.configuration().historyId()) && value && value != self.backupCode && !$("#A5_2").ntsError("hasError")) {
//                     service.checkCode(self.initMode, self.configuration().historyId(), value).done(checkResult => {
                    
//                     }).fail(error => {
//                         alertError(error).then(() => {
//                             if (error.messageId == "Msg_3") {
//                                 self.selectedCode(self.backupCode);
//                             }
//                         });
//                     });
//                 }
//             });
//             $(document).delegate("#A4_1", "igtreedragstop", function(evt, ui) {
//                 let newDataSource = $("#A4_1").igTree('option', 'dataSource')._rootds._data;
//                 self.items(_.map(newDataSource, i => { return new WkpDepNode(i); }));
//                 self.needRegenerateHierarchyCode = true;
//             });
//         }

//         startPage(): JQueryPromise<any> {
//             let self = this, dfd = $.Deferred();
//             block.invisible();
//             service.getOperationRule().done(res => {
//                 self.isSynchronized(res.synchWkpDep);
//                 service.getConfiguration(self.initMode).done((configuration) => {
//                     if (configuration) {
//                         self.configuration(new WkpDepConfig(configuration.historyId, configuration.startDate, configuration.endDate));
//                         self.getAllWkpDepInfor().done(() => {
//                             dfd.resolve();
//                         }).fail((error) => {
//                             dfd.reject();
//                         }).always(() => {
//                             block.clear()
//                         });
//                     } else {
//                         dfd.resolve();
//                         self.openConfigDialog();
//                     }
//                 }).fail((error) => {
//                     dfd.reject();
//                     alertError(error);
//                     block.clear();
//                 });
//             }).fail((error) => {
//                 dfd.reject();
//                 alertError(error);
//                 block.clear();
//             });
//             return dfd.promise();
//         }

//         getAllWkpDepInfor(idToSelect?: string): JQueryPromise<any> {
//             let self = this, dfd = $.Deferred();
//             self.items([]);
//             self.selectedId(null);
//             service.getAllWkpDepInforTree(self.initMode, self.configuration().historyId()).done((data) => {
//                 if (_.isEmpty(data)) {
//                     dfd.resolve();
//                     if (self.initMode == SCREEN_MODE.WORKPLACE) {
//                         info({ messageId: "Msg_373" }).then(() => {
//                             self.openWkpDepCreateDialog();
//                         });
//                     } else {
//                         info({ messageId: "Msg_1503" }).then(() => {
//                             self.openWkpDepCreateDialog();
//                         });
//                     }
//                 } else {
//                     let listNode = _.map(data, i => {
//                         return new WkpDepNode(i);
//                     });
//                     self.items(listNode);
//                     if (idToSelect)
//                         self.selectedId(idToSelect);
//                     else if (self.items().length > 0)
//                         self.selectedId(self.items()[0].id);
//                     dfd.resolve();
//                 }
//                 self.listHierarchyChange = [];
//                 self.needRegenerateHierarchyCode = false;
//             }).fail((error) => {
//                 dfd.reject();
//                 alertError(error);
//             }).always(() => {
//                 block.clear()
//             });
//             return dfd.promise();
//         }
        
//         registerMaster() {
//             let self = this;
//             if (_.isEmpty(self.configuration().historyId()) || _.isEmpty(self.items()))
//                 return;
//             $(".nts-input").trigger("validate");
//             $("#A5_2").trigger("keyup");
//             if (nts.uk.ui.errors.hasError()) 
//                 return;
//             block.invisible();
// //            if (self.needRegenerateHierarchyCode) {
//                 self.generateHierarchyCode(self.items(), "");
// //            }
//             let command = {
//                 initMode: self.initMode,
//                 historyId: self.configuration().historyId(),
//                 id: self.selectedId(),
//                 code: self.selectedCode(),
//                 name: self.selectedName(),
//                 dispName: self.selectedDispName(),
//                 genericName: self.selectedGenericName(),
//                 externalCode: self.selectedExternalCode(),
//                 hierarchyCode: self.selectedHierarchyCode(), 
//                 listHierarchyChange: self.listHierarchyChange,
//                 updateMode: true
//             };
//             service.registerWkpDepInfor(command).done((id) => {
//                 info({ messageId: "Msg_15" }).then(() => {
//                     self.getAllWkpDepInfor(id);
//                 });
//             }).fail(error => {
//                 alertError(error);
//             }).always(() => {
//                 block.clear();
//             });
//         }
        
//         deleteMaster() {
//             let self = this;
//             confirm({ messageId: self.initMode == SCREEN_MODE.WORKPLACE ? "Msg_1505" : "Msg_1506" }).ifYes(() => {
//                 block.invisible();
//                 if (self.needRegenerateHierarchyCode) {
//                     self.generateHierarchyCode(self.items(), "");
//                 } 
//                 let currentHierarchyCode = self.selectedHierarchyCode();
//                 let level = currentHierarchyCode.length / 3;
//                 let parentCode = currentHierarchyCode.substring(0, (level - 1) * 3);
//                 let items = _.cloneDeep(self.items()), newItems = [];
//                 for (let i = 1; i < level; i++) {
//                     let hCode = currentHierarchyCode.substring(0, 3 * i);
//                     let node = _.find(items, i => i.hierarchyCode == hCode);
//                     items = node.children;
//                 }
//                 let currIndex = _.findIndex(items, i => i.id == self.selectedId());
//                 items.forEach((item, index) => {
//                     if (index != currIndex) {
//                         newItems.push(item);
//                     }                    
//                 });
//                 self.generateHierarchyCode(newItems, parentCode);
//                 let data = {
//                     initMode: self.initMode,
//                     historyId: self.configuration().historyId(),
//                     selectedWkpDepId: self.selectedId(),
//                     listHierarchyChange: self.listHierarchyChange
//                 };
//                 service.deleteWkpDepInfor(data).done(() => {
//                     info({ messageId: "Msg_16" }).then(() => {
//                         block.invisible();
//                         self.getAllWkpDepInfor().done(() => {
                            
//                         }).always(() => {
//                             block.clear()
//                         });
//                     });
//                 }).fail(error => {
//                     alertError(error);
//                 }).always(() => {
//                     block.clear();
//                 });
//             }).ifNo(() => {
//             });
//         }

//         openConfigDialog() {
//             let self = this,
//                 params = {
//                     initMode: self.initMode,
//                     historyId: self.configuration() ? self.configuration().historyId() : null
//                 };
//             setShared("CMM011AParams", params);
//             modal("/view/cmm/011_v2/b/index.xhtml").onClosed(() => {
//                 let params = getShared("CMM011BParams");
//                 if (params) {
//                     self.configuration().historyId(params.historyId);
//                     self.configuration().startDate(params.startDate);
//                     self.configuration().endDate(params.endDate);
//                     block.invisible();
//                     self.getAllWkpDepInfor().done(() => {
                        
//                     }).always(() => {
//                         block.clear()
//                     });
//                 }
//             });
//         }

//         openWkpDepCreateDialog() {
//             let self = this;
//             block.invisible();
//             service.checkTotalWkpDepInfor(self.initMode, self.configuration().historyId()).done(() => {
//                 if (self.needRegenerateHierarchyCode) {
//                     self.generateHierarchyCode(self.items(), "");
//                 }
//                 let node = $("#A4_1").ntsTreeDrag("getSelected");
//                 let params = {
//                     initMode: self.initMode,
//                     selectedCode: node ? node.data.code : self.selectedCode(),
//                     selectedName: node ? node.data.name : self.selectedName(),
//                     selectedHierarchyCode: self.selectedHierarchyCode(),
//                     history: self.configuration().historyId(),
//                     items: self.items(),
//                     listHierarchyChange: self.listHierarchyChange
//                 };
//                 setShared("CMM011AParams", params);
//                 modal("/view/cmm/011_v2/d/index.xhtml").onClosed(() => {
//                     let result = getShared("CreatedWorkplace");
//                     if (result) {
//                         block.invisible();
//                         self.getAllWkpDepInfor(result.idToSelect).always(() => {
//                             block.clear();
//                         });
//                     }
//                 });
//             }).fail((error) => {
//                 block.clear();
//                 alertError(error);
//             });
//         }

//         moveLeft() {
//             let self = this;
//             let node = $("#A4_1").ntsTreeDrag("getSelected");
//             let target = $("#A4_1").ntsTreeDrag("getParent", node.data.id)
//             if (target) {
//                 $("#A4_1").ntsTreeDrag("moveNext", target.data.id, node.data.id);
//                 self.needRegenerateHierarchyCode = true;
//             }
//         }

//         moveRight() {
//             let self = this;
//             let node = $("#A4_1").ntsTreeDrag("getSelected");
//             let target = $("#A4_1").ntsTreeDrag("getPrevious", node.data.id)
//             if (target) {
//                 $("#A4_1").ntsTreeDrag("moveInto", target.data.id, node.data.id);
//                 self.needRegenerateHierarchyCode = true;
//             }
//         }

//         moveUp() {
//             let self = this;
//             let node = $("#A4_1").ntsTreeDrag("getSelected");
//             $("#A4_1").ntsTreeDrag("moveUp", node.id);
//             self.needRegenerateHierarchyCode = true;
//         }

//         moveDown() {
//             let self = this;
//             let node = $("#A4_1").ntsTreeDrag("getSelected");
//             $("#A4_1").ntsTreeDrag("moveDown", node.id);
//             self.needRegenerateHierarchyCode = true;
//         }
        
//         getGenericName(value: string): string {
//             let self = this, result = "";
//             let currentHierarchyCode = self.selectedHierarchyCode();
//             if (currentHierarchyCode) {
//                 let level = Math.floor(currentHierarchyCode.length/3);
//                 let items = self.items();
//                 for (let i = 1; i < level; i++) {
//                     let hCode = currentHierarchyCode.substring(0, 3*i);
//                     let node = _.find(items, i => i.hierarchyCode == hCode);
//                     result = result + node.name + " ";
//                     items = node.children;
//                 }
//             }
//             return result + value;
//         }
        
//         generateHierarchyCode(items: Array<WkpDepNode>, parentHierarchyCode: string) {
//             let self = this;
//             items.forEach((node, index) => {
//                 let hCode = ++index + "";
//                 if (hCode.length == 1) hCode = "00"+ hCode;
//                 if (hCode.length == 2) hCode = "0"+ hCode;
//                 let newHierarchyCode = parentHierarchyCode + hCode;
//                 if (node.hierarchyCode != newHierarchyCode) {
//                     node.hierarchyCode = newHierarchyCode;
//                     self.listHierarchyChange = self.listHierarchyChange.filter(i => i.id != node.id);
//                     self.listHierarchyChange.push({ id: node.id, hierarchyCode: node.hierarchyCode });
//                 }
//                 if (node.id == self.selectedId()) {
//                     self.selectedHierarchyCode(newHierarchyCode);
//                 }
//                 self.generateHierarchyCode(node.children, node.hierarchyCode);
//             })
//         }

    }

    // enum SCREEN_MODE {
    //     WORKPLACE = 0,
    //     DEPARTMENT = 1
    // }
    
    // enum CreationType {
    //     CREATE_ON_TOP = 1,
    //     CREATE_BELOW = 2,
    //     CREATE_TO_CHILD = 3
    // }

    // class WkpDepNode {
    //     id: string;
    //     code: string;
    //     name: string;
    //     nodeText: string;
    //     hierarchyCode: string;
    //     children: Array<WkpDepNode>;

    //     constructor(param) {
    //         if (param) {
    //             this.id = param.id;
    //             this.code = param.code;
    //             this.name = param.name;
    //             this.nodeText = _.escape(param.code + ' ' + param.name);
    //             this.hierarchyCode = param.hierarchyCode;
    //             this.children = _.isEmpty(param.children) ? [] : _.map(param.children, i => {return new WkpDepNode(i)});
    //         }
    //     }
    // }

    // class WkpDepConfig {
    //     historyId: KnockoutObservable<string>;
    //     startDate: KnockoutObservable<string>;
    //     endDate: KnockoutObservable<string>;

    //     constructor(histId: string, startDate: string, endDate: string) {
    //         this.historyId = ko.observable(histId);
    //         this.startDate = ko.observable(startDate);
    //         this.endDate = ko.observable(endDate);
    //     }

    // }

    // class WkpDepInformation {
    //     id: KnockoutObservable<string> = ko.observable(null);
    //     code: KnockoutObservable<string> = ko.observable(null);
    //     name: KnockoutObservable<string> = ko.observable(null);
    //     dispName: KnockoutObservable<string> = ko.observable(null);
    //     genericName: KnockoutObservable<string> = ko.observable(null);
    //     hierarchyCode: KnockoutObservable<string> = ko.observable(null);
    //     externalCode: KnockoutObservable<string> = ko.observable(null);

    //     constructor(params: any) {
    //         if (params) {
    //             this.id(params.id);
    //             this.code(params.code);
    //             this.name(params.name);
    //             this.dispName(params.dispName);
    //             this.genericName(params.genericName);
    //             this.hierarchyCode(params.hierarchyCode);
    //             this.externalCode(params.externalCode);
    //         }
    //     }
    // }

}
