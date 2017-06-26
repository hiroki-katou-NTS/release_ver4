module qmm020.m.viewmodel {
    export class ScreenModel {
        listItemSelected: KnockoutObservable<string> = ko.observable('');
        listItemDataSources: KnockoutObservableArray<ItemModel> = ko.observableArray([]);
        listItemColumns: KnockoutObservableArray<any> = ko.observableArray([
            { headerText: 'コード', prop: 'code', width: 50 },
            { headerText: '名称', prop: 'name', width: 175 },
            { headerText: '有効期間', prop: 'time', width: 175 }
        ]);

        constructor() {
            let self = this, currentBaseYM = parseInt(nts.uk.ui.windows.getShared('M_BASEYM')) || 190701;
            if (!!currentBaseYM) {
                service.getData(currentBaseYM)
                    .done((resp: Array<any>) => {
                        if (resp.length == 2) {
                            let _items: Array<ItemModel> = [];
                            _.forEach(resp[0], (item, i) => {
                                _items.push(new ItemModel({ code: item.stmtCode, name: resp[1][i], time: item.startYm + '~' + item.endYm }));
                            });
                            self.listItemDataSources(_items);
                        }
                    }).fail((res) => {
                        alert(res);
                    });
            }
        }

        //event when click to 選択 Button
        selectStmtCode() {
            let self = this;
            nts.uk.ui.windows.setShared('M_RETURN', self.listItemSelected());
            self.closeDialog();
        }

        //event when close dialog
        closeDialog() {
            nts.uk.ui.windows.close();
        };
    }

    interface IItemModel {
        code: string;
        name: string;
        time: string;
    }

    class ItemModel {
        code: string;
        name: string;
        time: string;
        constructor(param: IItemModel) {
            this.code = param.code;
            this.name = param.name;
            this.time = param.time;
        }
    }
}