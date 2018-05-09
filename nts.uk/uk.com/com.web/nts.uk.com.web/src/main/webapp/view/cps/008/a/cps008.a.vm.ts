module cps008.a.viewmodel {
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import showDialog = nts.uk.ui.dialog;
    import Text = nts.uk.resource.getText;
    import lv = nts.layout.validate;

    let __viewContext: any = window['__viewContext'] || {},
        block = window["nts"]["uk"]["ui"]["block"]["grayout"],
        unblock = window["nts"]["uk"]["ui"]["block"]["clear"],
        invisible = window["nts"]["uk"]["ui"]["block"]["invisible"];


    export class ViewModel {
        layouts: KnockoutObservableArray<ILayout> = ko.observableArray([]);
        layout: KnockoutObservable<Layout> = ko.observable(new Layout({ id: '', code: null, name: null }));

        constructor() {
            let self = this,
                layout: Layout = self.layout(),
                layouts = self.layouts;


            self.start();

            layout.id.subscribe(id => {
                if (id) {

                    // Gọi service tải dữ liệu ra layout
                    block();
                    service.getDetails(id).done((data: any) => {
                        if (data) {
                            layout.code(data.layoutCode);
                            layout.name(data.layoutName);

                            // remove all sibling sperators
                            lv.removeDoubleLine(data.itemsClassification);

                            layout.classifications(data.listItemClsDto || []);
                            layout.action(LAYOUT_ACTION.UPDATE);
                            $("#A_INP_NAME").focus();
                            unblock();
                        }
                    });
                }
            });
        }

        start(code?: string): JQueryPromise<any> {
            let self = this,
                layout: Layout = self.layout(),
                layouts = self.layouts,
                dfd = $.Deferred();
            // get all layout
            layouts.removeAll();
            service.getAll().done((data: Array<any>) => {
                if (data && data.length) {
                    let _data: Array<ILayout> = _.map(data, x => {
                        return {
                            id: x.maintenanceLayoutID,
                            name: x.layoutName,
                            code: x.layoutCode
                        }
                    });
                    _.each(_data, d => layouts.push(d));
                    if (!code) {
                        layout.id(_data[0].id);
                    }
                    else {
                        let _item: ILayout = _.find(ko.toJS(layouts), (x: ILayout) => x.code == code);
                        if (_item) {
                            layout.id(_item.id);
                        } else {
                            layout.id(_data[0].id);
                        }
                    }
                    layout.id.valueHasMutated();

                } else {
                    self.createNewLayout();
                }
                dfd.resolve();
            });
            return dfd.promise();
        }

        createNewLayout() {
            let self = this,
                layout: Layout = self.layout(),
                layouts = self.layouts;

            layout.id(undefined);
            layout.code(null);
            layout.name(null);
            layout.classifications([]);
            layout.action(LAYOUT_ACTION.INSERT);
            $("#A_INP_CODE").focus();
        }

        saveDataLayout() {
            let self = this,
                data: ILayout = ko.toJS(self.layout),
                command: any = {
                    id: data.id,
                    code: data.code,
                    name: data.name,
                    action: data.action,
                    classifications: data.outData
                };

            // validate
            $("#A_INP_CODE").trigger("validate");
            $("#A_INP_NAME").trigger("validate");

            if (nts.uk.ui.errors.hasError()) {
                return;
            }

            // call service savedata
            block();
            service.saveData(command).done((_data: any) => {
                unblock();
                showDialog.info({ messageId: "Msg_15" }).then(function() {
                    $("#A_INP_NAME").focus();
                });

                self.start(data.code);


            }).fail((error: any) => {
                unblock();
                if (error.message == 'Msg_3') {
                    showDialog.alert({ messageId: "Msg_3" }).then(function() {
                        $("#A_INP_CODE").focus();
                    });
                }


            });
        }

        copyDataLayout() {
            let self = this,
                data: ILayout = ko.toJS(self.layout),
                layouts: Array<ILayout> = ko.toJS(self.layouts);

            setShared('CPS008_PARAM', data);
            modal('../c/index.xhtml').onClosed(() => {
                let _data = getShared('CPS008C_RESPONE');
                if (_data != undefined) {
                    self.start(_data);
                } 
            });
        }

        removeDataLayout() {
            let self = this,
                data: ILayout = ko.toJS(self.layout),
                layouts: Array<ILayout> = ko.toJS(self.layouts);

            data.action = LAYOUT_ACTION.REMOVE;
            let indexItemDelete = _.findIndex(ko.toJS(self.layouts), function(item: any) { return item.id == data.id; });

            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                var command: any = {
                    id: data.id,
                    code: data.code,
                    name: data.name,
                    action: data.action,
                    classifications: data.outData
                };

                // call service remove
                invisible();
                let itemListLength = self.layouts().length;
                service.saveData(command).done((data: any) => {


                    showDialog.info({ messageId: "Msg_16" }).then(function() {
                        if (itemListLength === 1) {
                            self.start().done(() => {
                                unblock();
                            });
                        } else if (itemListLength - 1 === indexItemDelete) {
                            self.start(layouts[indexItemDelete - 1].code).done(() => {
                                unblock();
                            });
                        } else if (itemListLength - 1 > indexItemDelete) {
                            self.start(layouts[indexItemDelete + 1].code).done(() => {
                                unblock();
                            });
                        }
                    });
                    unblock();
                }).fail((error: any) => {
                    unblock();
                });

            }).ifCancel(() => {
            });
        }

        showDialogB() {
            let self = this,
                layout: Layout = self.layout(),
                data: ILayout = ko.toJS(self.layout);
            setShared('CPS008B_PARAM', data);
            modal('../b/index.xhtml').onClosed(() => {
                let dto: Array<any> = getShared('CPS008B_VALUE');

                if (dto && dto.length) {
                    layout.classifications(_.map(dto, x => _.omit(x, ["items"])));
                    layout.action(LAYOUT_ACTION.UPDATE);
                }
            });
        }
    }

    interface IItemClassification {
        layoutID?: string;
        dispOrder?: number;
        className?: string;
        personInfoCategoryID?: string;
        layoutItemType: IT_CLA_TYPE;
        listItemDf: Array<IItemDefinition>;
    }

    interface IItemDefinition {
        id: string;
        perInfoCtgId?: string;
        itemCode?: string;
        itemName: string;
        dispOrder: number;
    }


    interface ILayout {
        id: string;
        code: string;
        name: string;
        classifications?: Array<IItemClassification>;
        action?: number;
        outData?: Array<any>;
    }

    class Layout {
        id: KnockoutObservable<string> = ko.observable(null);
        code: KnockoutObservable<string> = ko.observable(null);
        name: KnockoutObservable<string> = ko.observable(null);
        classifications: KnockoutObservableArray<any> = ko.observableArray([]);
        action: KnockoutObservable<LAYOUT_ACTION> = ko.observable(LAYOUT_ACTION.INSERT);
        outData: KnockoutObservableArray<any> = ko.observableArray([]);

        constructor(param: ILayout) {
            let self = this;

            if (param) {
                self.id(param.id || null);
                self.code(param.code || null);
                self.name(param.name || null);

                self.classifications(param.classifications || []);
            }
        }
    }

    enum LAYOUT_ACTION {
        INSERT = 0,
        UPDATE = 1,
        COPY = 2,
        OVERRIDE = 3,
        REMOVE = 4
    }

    // define ITEM_CLASSIFICATION_TYPE
    enum IT_CLA_TYPE {
        ITEM = <any>"ITEM", // single item
        LIST = <any>"LIST", // list item
        SPER = <any>"SeparatorLine" // line item
    }
}