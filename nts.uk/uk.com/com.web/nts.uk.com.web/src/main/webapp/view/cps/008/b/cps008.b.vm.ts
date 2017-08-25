module cps008.b.vm {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import close = nts.uk.ui.windows.close;
    let __viewContext: any = window['__viewContext'] || {};

    export class ViewModel {
        layout: KnockoutObservable<Layout> = ko.observable(new Layout({ id: '', code: '', name: '' }));

        constructor() {
            let self = this,
                layout = self.layout();

            self.start();
        }

        start() {
            let self = this,
                layout = self.layout(),

                dto: any = getShared('CPS008B_PARAM');
            layout.id = dto.id;
            layout.code = dto.code;
            layout.name = dto.name;
            // lấy list items classification ra theo layoutid của maintainece layout truyền từ màn a lên
            // Không có thì gọi service dưới lấy list items classification của new layout rồi truyền vào layout ở view model
            service.getListCls(dto.id).done((x: any) => {
                if (x.listItemClsDto && x.listItemClsDto.length) {
                    layout.itemsClassification(x.listItemClsDto);
                } else {
                    service.getData().done((x: ILayout) => {
                        layout.itemsClassification(x.itemsClassification);

                    });
                }

            });

        }

        pushData() {
            let self = this,
                layout: ILayout = ko.toJS(self.layout);

            // check item tren man hinh
            if (layout.itemsClassification.length == 0) {
                nts.uk.ui.dialog.alert(nts.uk.resource.getText('Msg_203'));
                return;
            }

            let listItemIds = _(layout.itemsClassification).map(x => x.listItemDf).flatten().filter(x => !!x).map((m: IItemDefinition) => m.id).orderBy(m => m).value();


            // エラーメッセージ（#Msg_289#,２つ以上配置されている項目名）を表示する
            for (let i = 0; i < listItemIds.length - 2; i++) {
                if (listItemIds[i] === listItemIds[i + 1]) {
                    nts.uk.ui.dialog.alert(nts.uk.resource.getText('Msg_289'));
                    return;
                }
            }

            setShared("CPS008B_VALUE", layout.itemsClassification);

            close();

        }

        close() {
            setShared('CPS008B_VALUE', null);
            close();
        }
    }

    interface IItemClassification {
        layoutID?: string;
        dispOrder?: number;
        className?: string;
        personInfoCategoryID?: string;
        layoutItemType: number;
        listItemDf: Array<IItemDefinition>;
    }

    interface IItemDefinition {
        id: string;
        perInfoCtgId?: string;
        itemCode?: string;
        itemName: string;
    }

    interface ILayout {
        id: string;
        code: string;
        name: string;
        editable?: boolean;
        itemsClassification?: Array<IItemClassification>;
    }

    class Layout {
        id: KnockoutObservable<string> = ko.observable('');
        code: KnockoutObservable<string> = ko.observable('');
        name: KnockoutObservable<string> = ko.observable('');
        editable: KnockoutObservable<boolean> = ko.observable(true);
        itemsClassification: KnockoutObservableArray<IItemClassification> = ko.observableArray([]);

        constructor(param: ILayout) {
            let self = this;

            self.id(param.id);
            self.code(param.code);
            self.name(param.name);

            if (param.editable != undefined) {
                self.editable(param.editable);
            }

            // replace x by class that implement this interface
            self.itemsClassification(param.itemsClassification || []);
        }
    }
}