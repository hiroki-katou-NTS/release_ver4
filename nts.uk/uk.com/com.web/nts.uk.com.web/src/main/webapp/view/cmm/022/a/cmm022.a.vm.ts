module nts.uk.com.view.cmm022.a {

    import setShared = nts.uk.ui.windows.setShared;
    import block = nts.uk.ui.block;
    import alert = nts.uk.ui.dialog.alert;
    import info = nts.uk.ui.dialog.info;
    import confirm = nts.uk.ui.dialog.confirm;

    export module viewmodel {
        export class ScreenModel {
            commonMasters: KnockoutObservableArray<ICommonMaster> = ko.observableArray([]);
            commonMasterId: KnockoutObservable<String> = ko.observable();
            selectedCommonMaster: KnockoutObservable<ICommonMaster> = ko.observable(new CommonMaster());
            
            commonMasterItems: KnockoutObservableArray<ICommonMaster> = ko.observableArray([]);
            commonMasterItemId: KnockoutObservable<String> = ko.observable();
            selectedCommonMasterItem: KnockoutObservable<MasterItem> = ko.observable(new MasterItem(this.defaultItem));
            defaultItem = {
                commonMasterItemId: null,
                commonMasterItemCode: "",
                commonMasterItemName: "",
                displayNumber: null,
                usageStartDate: moment(new Date()).format("YYYY/MM/DD"),
                usageEndDate: "9999/12/31"
            }
            
            newMode: KnockoutObservable<boolean> = ko.observable(false);

            constructor() {
                let self = this;

                self.commonMasterId.subscribe((id) => {

                    self.selectedCommonMaster(new CommonMaster(_.filter(self.commonMasters(), ['commonMasterId', id])[0]));

                    if (!id) {
                        self.commonMasterItems([]);
                        self.commonMasterItemId("");
                        return;
                    }

                    block.grayout();

                    service.getItems(id).done((data: Array<IMasterItem>) => {

                        self.commonMasterItems(data);
                        self.commonMasterItemId(data.length ? data[0].commonMasterItemId : null);

                    }).fail(function(res) {
                        
                        alert(res);

                    }).always(() => {

                        nts.uk.ui.errors.clearAll();
                        block.clear();
                    });

                });


                self.commonMasterItemId.subscribe((id) => {

                    if (id == null) {
                        return;
                    }
                    let selectedItem = _.filter(self.commonMasterItems(), ['commonMasterItemId', id])[0];
                    self.selectedCommonMasterItem().updateData(selectedItem ? selectedItem : self.defaultItem);
                    if (id) {
                        self.newMode(false);
                    }

                });

                self.commonMasters.subscribe((data) => {

                    self.commonMasterId(data.length ? data[0].commonMasterId : null);

                });

                self.selectedCommonMasterItem.subscribe((item) => {

                    nts.uk.ui.errors.clearAll();
                    if (item.commonMasterItemCode()) {
                        self.newMode(false);
                    } else {
                        self.newMode(true);
                    }

                });
                
                
                setTimeout(() => {
                    $(window).resize(function() {
                        $("#master-item-list").igGrid("option", "height", (window.innerHeight - 283) + "px");
                        $("#master-list").igGrid("option", "height", (window.innerHeight - 306) + "px");
                    });
                }, 100); 
            }

            /**
             * start page
             */
            public start_page(): JQueryPromise<any> {

                let self = this, dfd = $.Deferred(),

                    param = { confirmed: null };

                block.grayout();

                service.startPage(param)
                .done((data: Array<ICommonMaster>) => {

                    self.commonMasters(data);


                }).fail(function(res) {


                    if (res.messageId == "Msg_1589") {

                        confirm({ messageId: res.messageId }).ifYes(() => {
                            
                            param.confirmed = true;
                            block.grayout();
                            
                            service.startPage(param).done((data: Array<ICommonMaster>) => {

                                self.commonMasters(data);

                            }).always(() => {
                                
                                block.clear();

                            });
                        }).ifNo(() => {
                            
                            alert("Msg_1590");
                            
                        });
                    } else {
                        alert(res.messageId);
                    }

                    block.clear();


                }).always(() => {
                    
                    setTimeout(() => {
                        
                        $("#master-item-list").igGrid("option", "height", (window.innerHeight - 283) + "px");
                        $("#master-list").igGrid("option", "height", (window.innerHeight - 306) + "px");
                        
                    }, 100); 
                    nts.uk.ui.errors.clearAll();
                    block.clear();
                    dfd.resolve();

                });

                return dfd.promise();
            }

            public saveData() {

                let self = this
                    , param = ko.mapping.toJS(self);
                
                param.selectedCommonMasterItem.usageStartDate =
                    moment(param.selectedCommonMasterItem.usageStartDate).format("YYYY/MM/DD");
                
                param.selectedCommonMasterItem.usageEndDate =
                    moment(param.selectedCommonMasterItem.usageEndDate).format("YYYY/MM/DD");

                block.grayout();

                service.saveItems(param).done(() => {

                    service.getItems(self.commonMasterId()).done((data: Array<IMasterItem>) => {

                        self.commonMasterItems(data);
                        
                        if (self.newMode()) {
                            self.commonMasterItemId(_.maxBy(data, 'displayNumber').commonMasterItemId);
                        }

                    }).always(() => {

                        block.clear();
                    });


                    info({ messageId: "Msg_15" });


                }).fail(function(res) {

                    alert(res);

                }).always(() => {

                    block.clear();
                });
            }
            
            public newItem() {

                let self = this;
                self.commonMasterItemId(null);
                self.selectedCommonMasterItem().updateData(self.defaultItem);
                self.newMode(true);
            }
            
            public exportExcel() {
                let self = this;

                block.grayout();

                service.outPutFileExcel().done(() => {
                    block.clear();
                })
            }

            public openDialogB() {
                let self = this;
                setShared('listMasterToB', self.commonMasters());
                nts.uk.ui.windows.sub.modal('/view/cmm/022/b/index.xhtml').onClosed(function(): any {

                });
            }

            public openDialogC() {
                let self = this;
                setShared('listMasterToC', self.commonMasters());
                nts.uk.ui.windows.sub.modal('/view/cmm/022/c/index.xhtml').onClosed(function(): any {
                });
            }

        }


    }

    export interface ICommonMaster {
        commonMasterId: string;
        commonMasterCode: string;
        commonMasterName: string;
        commonMasterMemo: string;
    }

    export class CommonMaster {
        commonMasterId: KnockoutObservable<String> = ko.observable();
        commonMasterCode: KnockoutObservable<String> = ko.observable();
        commonMasterName: KnockoutObservable<String> = ko.observable();
        commonMasterMemo: KnockoutObservable<String> = ko.observable();
        constructor(data?: ICommonMaster) {
            let self = this;
            if (data) {
                self.commonMasterId(data.commonMasterId);
                self.commonMasterCode(data.commonMasterCode);
                self.commonMasterName(data.commonMasterName);
                self.commonMasterMemo(data.commonMasterMemo);
            }
        }
    }

    export interface IMasterItem {
        commonMasterItemId: string;
        commonMasterItemCode: string;
        commonMasterItemName: string;
        displayNumber: number;
        usageStartDate: string;
        usageEndDate: string;
    }

    export class MasterItem {
        commonMasterItemId: KnockoutObservable<String> = ko.observable();
        commonMasterItemCode: KnockoutObservable<String> = ko.observable();
        commonMasterItemName: KnockoutObservable<String> = ko.observable();
        displayNumber: KnockoutObservable<number> = ko.observable();
        usageStartDate: KnockoutObservable<String> = ko.observable(moment(new Date()).format("YYYY/MM/DD"));
        usageEndDate: KnockoutObservable<String> = ko.observable("9999/12/31");

        constructor(data: IMasterItem) {
            let self = this;
            if (data) {
                self.commonMasterItemId(data.commonMasterItemId);
                self.commonMasterItemCode(data.commonMasterItemCode);
                self.commonMasterItemName(data.commonMasterItemName);
                self.displayNumber(data.displayNumber);
                self.usageStartDate(data.usageStartDate);
                self.usageEndDate(data.usageEndDate);
            }
        }
        
        updateData(data: IMasterItem) {
            let self = this;
            self.commonMasterItemId(data.commonMasterItemId);
            self.commonMasterItemCode(data.commonMasterItemCode);
            self.commonMasterItemName(data.commonMasterItemName);
            self.displayNumber(data.displayNumber);
            self.usageStartDate(data.usageStartDate);
            self.usageEndDate(data.usageEndDate);
            nts.uk.ui.errors.clearAll();
        }
    }
}