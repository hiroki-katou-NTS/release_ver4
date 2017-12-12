module cps008.a.viewmodel {
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import showDialog = nts.uk.ui.dialog;
    import Text = nts.uk.resource.getText;

    let __viewContext: any = window['__viewContext'] || {},
        block = window["nts"]["uk"]["ui"]["block"]["grayout"],
        unblock = window["nts"]["uk"]["ui"]["block"]["clear"],
        invisible = window["nts"]["uk"]["ui"]["block"]["invisible"];


    export class ViewModel {
        layouts: KnockoutObservableArray<ILayout> = ko.observableArray([]);
        layout: KnockoutObservable<Layout> = ko.observable(new Layout({ id: '', code: '', name: '' }));

        constructor() {
            let self = this,
                layout: Layout = self.layout(),
                layouts = self.layouts;


            self.start();
            layout.id.subscribe(id => {
                if (id) {

                    // Gọi service tải dữ liệu ra layout
                    service.getDetails(id).done((data: any) => {
                        if (data) {
                            layout.code(data.layoutCode);
                            layout.name(data.layoutName);

                            // remove all sibling sperators
                            let maps = _(data.listItemClsDto)
                                .map((x, i) => (x.layoutItemType == IT_CLA_TYPE.SPER) ? i : -1)
                                .filter(x => x != -1).value();

                            _.each(maps, (t, i) => {
                                if (maps[i + 1] == t + 1) {
                                    _.remove(data.listItemClsDto, (m: IItemClassification) => {
                                        let item: IItemClassification = ko.unwrap(data.listItemClsDto)[maps[i + 1]];
                                        return item && item.layoutItemType == IT_CLA_TYPE.SPER && item.layoutID == m.layoutID;
                                    });
                                }
                            });

                            layout.classifications(data.listItemClsDto || []);
                            layout.action(LAYOUT_ACTION.UPDATE);
                            $("#A_INP_NAME").focus();
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
            layout.code('');
            layout.name('');
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
                    classifications: _(data.classifications || []).map((item, i) => {
                        return {
                            dispOrder: i + 1,
                            personInfoCategoryID: item.personInfoCategoryID,
                            layoutItemType: _(IT_CLA_TYPE).map(x => x).indexOf(item.layoutItemType),
                            listItemClsDf: _(item.listItemDf || []).map((def, j) => {
                                return {
                                    dispOrder: j + 1,
                                    personInfoItemDefinitionID: def.id
                                };
                            }).value()
                        };
                    }).value()
                };

            // check input
            if (data.code == '' || data.name == '') {
                if (data.code == '') {
                    $("#A_INP_CODE").focus();
                } else {
                    $("#A_INP_NAME").focus();
                }
                return;
            }

            // call service savedata
            invisible();
            service.saveData(command).done((_data: any) => {

                showDialog.info({ messageId: "Msg_15" }).then(function() {
                    unblock();
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
                if (_data) {
                    var command: any = {
                        id: data.id,
                        code: _data.code,
                        name: _data.name,
                        classifications: (data.classifications || []).map((item, i) => {
                            return {
                                dispOrder: i + 1,
                                personInfoCategoryID: item.personInfoCategoryID,
                                layoutItemType: _(IT_CLA_TYPE).map(x => x).indexOf(item.layoutItemType),
                                listItemClsDf: (item.listItemDf || []).map((def, j) => {
                                    return {
                                        dispOrder: j + 1,
                                        personInfoItemDefinitionID: def.id
                                    };
                                })
                            };
                        })
                    };

                    if (_data.action) {
                        command.action = LAYOUT_ACTION.OVERRIDE;
                    } else {
                        command.action = LAYOUT_ACTION.COPY;
                    }


                    // call saveData service
                    invisible();
                    service.saveData(command).done((data: any) => {

                        showDialog.info({ messageId: "Msg_20" }).then(function() {
                            unblock();
                            self.start(_data.code);
                        });

                    }).fail((error: any) => {
                        if (error.message == 'Msg_3') {
                            showDialog.alert({ messageId: "Msg_3" }).then(function() {
                                unblock();
                                self.start(data.code);
                            });
                        }


                    });

                } else {
                    $("#A_INP_NAME").focus();
                }
            });
        }

        removeDataLayout() {
            let self = this,
                data: ILayout = ko.toJS(self.layout),
                layouts: Array<ILayout> = ko.toJS(self.layouts);

            data.action = LAYOUT_ACTION.REMOVE;
            let indexItemDelete = _.findIndex(ko.toJS(self.layouts), function(item: any) { return item.id == data.id; });
            debugger;
            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {

                // call service remove
                invisible();
                let itemListLength = self.layouts().length;
                service.saveData(data).done((data: any) => {

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

                    showDialog.info({ messageId: "Msg_16" }).then(function() {
                        unblock();
                    });
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
                    layout.classifications.removeAll();
                    _.each(dto, x => layout.classifications.push(x));
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
    }

    class Layout {
        id: KnockoutObservable<string> = ko.observable('');
        code: KnockoutObservable<string> = ko.observable('');
        name: KnockoutObservable<string> = ko.observable('');
        classifications: KnockoutObservableArray<any> = ko.observableArray([]);
        action: KnockoutObservable<LAYOUT_ACTION> = ko.observable(LAYOUT_ACTION.INSERT);

        constructor(param: ILayout) {
            let self = this;

            if (param) {
                self.id(param.id || '');
                self.code(param.code || '');
                self.name(param.name || '');

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