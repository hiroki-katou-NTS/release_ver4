module nts.uk.at.view.ktg028.a.viewmodel {
    import NtsGridListColumn = nts.uk.ui.NtsGridListColumn;
    import block = nts.uk.ui.block;
    export class ScreenModel {
        texteditorA3_2: any;
        texteditorA4_2: any;
        texteditorA5_4: any;
        items_A2: KnockoutObservableArray<any>;
        columns_A2: KnockoutObservableArray<any>;
        currentCode_A2: KnockoutObservable<any>;
        items_A7: KnockoutObservableArray<any>;
        columns_A7: KnockoutObservableArray<NtsGridListColumn>;
        currentCodeList_A7: KnockoutObservableArray<any>;
        isEnable: KnockoutObservable<boolean>;
        isCreated: KnockoutObservable<boolean>;
        index: KnockoutObservable<number>;
        allData: Array<any>;

        constructor() {
            let self = this;
            self.allData = [];
            self.isEnable = ko.observable(true);
            self.isCreated = ko.observable(false);
            self.index = ko.observable(0);
            self.items_A2 = ko.observableArray([]);
            self.currentCode_A2 = ko.observable('');
            self.columns_A2 = ko.observableArray([
                { headerText: '', key: 'serialNumber', width: 40 },
                { headerText: nts.uk.resource.getText('KTG028_6'), key: 'topPageCode', width: 80 },
                { headerText: nts.uk.resource.getText('KTG028_7'), key: 'topPageName', width: 150, formatter: _.escape }
            ]);
            self.items_A7 = ko.observableArray([]);
            self.currentCodeList_A7 = ko.observableArray([]);
            self.columns_A7 = ko.observableArray([
                { headerText: '', key: 'value', width: 100, hidden: true },
                { headerText: nts.uk.resource.getText('KTG028_44'), key: 'name', width: 350 }
            ]);
            self.texteditorA3_2 = {
                value: ko.observable(''),
                constraint: 'TopPagePartCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "",
                    width: "43px",
                    textalign: "left"
                })),
                required: ko.observable(true),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
            self.texteditorA4_2 = {
                value: ko.observable(''),
                constraint: 'TopPagePartName',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "",
                    width: "210px",
                    textalign: "left"
                })),
                required: ko.observable(true),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
            self.texteditorA5_4 = {
                value: ko.observable(''),
                constraint: 'Height',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "",
                    width: "20px",
                    textalign: "left"
                })),
                required: ko.observable(true),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
            $("#fixed-table").ntsFixedTable({ height: 300, width: 600 });
            self.currentCode_A2.subscribe(function(newValue) {
                _.defer(() => nts.uk.ui.errors.clearAll());
                let currentItem = _.find(self.items_A2(), { 'topPageCode': newValue });
                if (newValue) {
                    self.texteditorA3_2.enable(false);
                    self.isCreated(false);
                    self.index(_.findIndex(self.items_A2(), ['topPageCode', newValue]));
                    self.texteditorA3_2.value(currentItem.topPageCode);
                    self.texteditorA4_2.value(currentItem.topPageName);
                    self.texteditorA5_4.value(currentItem.height());
                    let listType = []
                    for(let x of _.orderBy(currentItem.listType())){
                        //remove 「子の看護休残数」 và 「看護休残数」,計画年休残数
                        if(x != 22 && x != 23 && x != 18){
                            listType.push(x.toString());
                        }    
                    }
                    self.currentCodeList_A7(listType);
                    $("#name").focus();
                } else {
                    self.isCreated(true);
                    self.texteditorA3_2.enable(true);
                    $("#code").focus();
                }
            });

        }

        /**
         * startPage
         */
        public startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            var listWidgets = _.remove(__viewContext.enums.WidgetDisplayItemType, function(n){
               //remove 「子の看護休残数」 và 「看護休残数」, 計画年休残数
              /*・残業指示件数（A7_1）
                                    ・休出指示件数（A7_2）
                                    ・勤務実績参照（A7_9）
                                    ・半日年休残数（A7_16）
                                    ・時間年休使用可上限（A7_17）
                                    ・公休残数（A7_21）
                                    ・60H超休残数（A7_25）
                bug 107377*/
                return ( n.value != 0 && n.value != 1 && n.value != 8 && n.value != 15 &&  n.value != 16 && n.value != 21 && n.value != 25 && n.value != 22 && n.value != 23 && n.value != 18); 
                });
            var widgets = []; 
            listWidgets.forEach(function (value) {
                widgets.push(new ItemEnum(value.value.toString(),value.name));
            });
            self.items_A7(widgets);
            self.findAll().done(() => {
                if (self.items_A2().length > 0) {
                    self.currentCode_A2(self.items_A2()[0].topPageCode);
                }else{
                    self.isCreated(true);    
                }
                dfd.resolve();
            });
            return dfd.promise();
        }
        findAll(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            block.invisible();
            service.findAll().done((data: any) => {
                self.allData = _.sortBy(data, 'topPageCode');
                let items = []
                _.forEach(self.allData, (element, index) => {
                    items.push(new ItemA2(parseInt(index)+1, element.topPagePartID, element.topPageCode, element.topPageName
                        , element.width, element.height, _.map(_.filter(element.displayItemTypes, ['notUseAtr', 1]), 'displayItemType')));
                });
                self.items_A2(items);
                dfd.resolve();
            }).always(function(){
                block.clear();
            });

            return dfd.promise();
        }
        /**
    * clean form
    */
        cleanForm(): void {
            var self = this;
            nts.uk.ui.errors.clearAll();
            self.texteditorA3_2.enable(true);
            self.texteditorA3_2.value("");
            self.texteditorA4_2.value("");
            self.currentCode_A2("");
            self.texteditorA5_4.value("");
            self.currentCodeList_A7([]);
            $("#code").focus();
        }
        saveData(): void {
            let self = this;
            $("#code").trigger("validate");
            $("#name").trigger("validate");
            $("#height").trigger("validate");
            if (!nts.uk.ui.errors.hasError()) {
                block.invisible();
                let optionalWidget = _.find(self.allData, ['topPageCode', self.currentCode_A2()]);
                let displayItemTypes: Array<any> = [];
                let values = _.map(self.items_A7(), 'value');
                _.forEach(values, (x => {
                    let selectedList = self.currentCodeList_A7();
                    if (_.includes(selectedList, x)) {
                        displayItemTypes.push({
                            'displayItemType': x,
                            'notUseAtr': 1
                        });
                    } else {
                        displayItemTypes.push({
                            'displayItemType': x,
                            'notUseAtr': 0
                        });
                    }
                }));
                displayItemTypes.push({'displayItemType': '18', 'notUseAtr': 0},{'displayItemType': '22', 'notUseAtr': 0},{'displayItemType': '23', 'notUseAtr': 0});
                if (optionalWidget) {
                    let data: any = {};
                    data.topPagePartID = optionalWidget.topPagePartID;
                    data.topPageCode = optionalWidget.topPageCode;
                    data.topPageName = self.texteditorA4_2.value();
                    data.width = 2;
                    data.height = parseInt(self.texteditorA5_4.value());
                    data.displayItemTypes = displayItemTypes;
                    service.update(data).done(function() {
                        self.isCreated(false);
                        self.findAll().done(function() {
                            self.currentCode_A2(data.topPageCode);
                        });
                        nts.uk.ui.dialog.info({messageId: 'Msg_15'}).then(() => {
                            $("#name").focus();
                        });
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alertError({messageId: res.messageId }).then(() => {
                            $("#name").focus();
                        });
                        self.findAll();
                    })
                } else {
                    let data: any = {};
                    data.topPageCode = self.texteditorA3_2.value();
                    data.topPageName = self.texteditorA4_2.value();
                    data.width = 2;
                    data.height = parseInt(self.texteditorA5_4.value());
                    data.displayItemTypes = displayItemTypes;
                    service.add(data).done(function() {
                        self.isCreated(false);
                        self.findAll().done(function() {
                            self.currentCode_A2(data.topPageCode);
                        });
                        nts.uk.ui.dialog.info({messageId: 'Msg_15'}).then(() => {
                            $("#name").focus();
                        });
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alertError({messageId: res.messageId }).then(() => {
                            $("#code").focus();
                        });
                        self.findAll();
                    });
                }
            }
        }
        removeData(): any {
            let self = this;
            let optionalWidget = _.find(self.allData, ['topPageCode', self.currentCode_A2()]);
            let data = {
                topPagePartID: optionalWidget.topPagePartID,
                displayItemTypes: optionalWidget.displayItemTypes
            }
            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                block.invisible();
                service.remove(data).done(function() {
                    nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                    self.findAll().done(function() {
                            if (self.items_A2().length == 0) {
                                self.cleanForm();
                            } else if (self.index() == self.items_A2().length) {
                                self.currentCode_A2(self.items_A2()[self.index() - 1].topPageCode);
                            } else {
                                self.currentCode_A2(self.items_A2()[self.index()].topPageCode);
                            }
                        });
                }).fail(function(error) {
                    self.isCreated(false);
                    nts.uk.ui.dialog.alertError(error.messageId);
                    block.clear();
                });
            });
        }

    }
    class ItemA2 {
        serialNumber: number;
        topPagePartID: string;
        topPageCode: string;
        topPageName: string;
        width: KnockoutObservable<number>;
        height: KnockoutObservable<number>;
        listType: KnockoutObservableArray<any>;
        constructor(index: number, topPagePartID: string, topPageCode: string, topPageName: string, width: number, height: number, listType: Array<any>) {
            this.serialNumber = index;
            this.topPagePartID = topPagePartID;
            this.topPageCode = topPageCode;
            this.topPageName = topPageName;
            this.width = ko.observable(width);
            this.height = ko.observable(height);
            this.listType = ko.observableArray(listType);
        }
    }
    class ItemEnum {
        value: string;
        name: string;
        constructor(value: string, name: string) {
            this.value = value;
            this.name = name;
        }
    }
}