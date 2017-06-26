module qmm020.d.viewmodel {
    export class ScreenModel {
        listTreeColumns: Array<any> = [];

        model: KnockoutObservable<ItemModel> = ko.observable(undefined);
        dirty: nts.uk.ui.DirtyChecker = new nts.uk.ui.DirtyChecker(this.model);


        constructor() {
            var self = this;

            self.listTreeColumns = [{ headerText: '', key: 'code', dataType: 'string', hidden: true },
                { headerText: 'コード/名称', key: 'name', dataType: 'string', hidden: false, width: '450px', template: '<span>${code} ${name}</span>' },
                { headerText: '給与明細書', key: 'bonus', dataType: 'object', hidden: false, width: '250px', template: '<button class="d-btn-001" onclick="__viewContext.viewModel.viewmodelD.openMDialog()">選択</button><span>${bonus.code} ${bonus.name}</span>' },
                { headerText: '賞与明細書', key: 'payment', dataType: 'object', hidden: false, width: '250px', template: '<button class="d-btn-002" onclick="__viewContext.viewModel.viewmodelD.openMDialog()">選択</button><span>${payment.code} ${payment.name}</span>' }];

            self.start();
        }

        start() {
            let self = this;

            self.model(new ItemModel({ ItemTrees: [], ItemLists: [] }));

            // replace below code by service            
            self.model().ItemLists([
                new ItemList({ code: '1', name: '2016/04 ~ 9999/12' }),
                new ItemList({ code: '2', name: '2015/04 ~ 2016/03' }),
                new ItemList({ code: '3', name: '2014/04 ~ 2015/03' })
            ]);
            self.model().ItemListSelected('1');

            self.model().ItemTrees([
                new ItemTree({
                    code: '001', name: 'Name 0001',
                    bonus: new ItemList({ code: '01', name: 'Bonus 01' }),
                    payment: new ItemList({ code: '50', name: 'Payment 50' }),
                    childs: [{
                        code: '001001', name: 'Child 001',
                        bonus: new ItemList({ code: '01', name: 'Bonus 01' }),
                        payment: new ItemList({ code: '50', name: 'Payment 50' })
                    },
                        {
                            code: '001002', name: 'Child 002',
                            bonus: new ItemList({ code: '01', name: 'Bonus 01' }),
                            payment: new ItemList({ code: '50', name: 'Payment 50' })
                        }]
                }),
                new ItemTree({
                    code: '002', name: 'Name 0002',
                    bonus: new ItemList({ code: '01', name: 'Bonus Name 01' }),
                    payment: new ItemList({ code: '50', name: 'Payment Name 50' })
                }),
                new ItemTree({
                    code: '003', name: 'Name 0003',
                    bonus: new ItemList({ code: '01', name: 'Bonus Name 01' }),
                    payment: new ItemList({ code: '50', name: 'Payment Name 50' })
                })
            ]);

            self.model().ItemTreeSelected('002');

            // reset dirty check
            self.dirty.reset();
        }

        openJDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('J_MODE', 2);
            nts.uk.ui.windows.setShared("J_BASEDATE", 201701);
            nts.uk.ui.windows.sub.modal('/view/qmm/020/j/index.xhtml', { width: 485, height: 550, title: '履歴の追加', dialogClass: "no-close" })
                .onClosed(function() {
                    let value = nts.uk.ui.windows.getShared('J_RETURN');
                    // set data into gridview
                });
        }

        openKDialog() {
            let self = this;

            nts.uk.ui.windows.sub.modal('/view/qmm/020/k/index.xhtml', { width: 485, height: 550, title: '履歴の編集', dialogClass: "no-close" });
        }

        openMDialog() {
            let self = this;
            let currentItemTree = self.model().currentItemTree();
            if (!!currentItemTree) {
                currentItemTree.dialog(true);
            }

            nts.uk.ui.windows.setShared('M_BASEYM', 201707);
            nts.uk.ui.windows.sub.modal('/view/qmm/020/m/index.xhtml', { width: 485, height: 550, title: '明細書の選択', dialogClass: "no-close" })
                .onClosed(function() {
                    currentItemTree.dialog(false);
                    let value = nts.uk.ui.windows.getShared('M_RETURN');
                    // set data into gridview
                });
        }
    }

    interface IItemModel {
        ItemTrees: Array<IItemTree>;
        ItemLists: Array<IItemList>;
    }

    class ItemModel {
        ItemTrees: KnockoutObservableArray<ItemTree> = ko.observableArray([]);
        ItemTreeSelected: KnockoutObservable<string> = ko.observable(undefined);
        ItemLists: KnockoutObservableArray<ItemList> = ko.observableArray([]);
        ItemListSelected: KnockoutObservable<string> = ko.observable(undefined);

        constructor(param: IItemModel) {
            let self = this;
            self.ItemTrees(param.ItemTrees.map((m) => { return new ItemTree(m); }));
            self.ItemLists(param.ItemLists.map((m) => { return new ItemList(m); }));
        }

        currentItemTree(): ItemTree {
            let self = this;
            return _.find(self.ItemTrees(), function(m: ItemTree) { return m.code == self.ItemTreeSelected(); });
        }

        currentItemList(): ItemList {
            let self = this;
            return _.find(self.ItemLists(), function(m: ItemList) { return m.code == self.ItemListSelected(); });
        }
    }

    interface IItemTree {
        code: string;
        name: string;
        bonus?: IItemList;
        payment?: IItemList;
        childs?: Array<IItemTree>;
    }

    class ItemTree {
        code: string;
        name: string;

        bonus: ItemList;
        payment: ItemList;
        childs: Array<ItemTree>;
        dialog: KnockoutObservable<boolean> = ko.observable(false);
        constructor(param: IItemTree) {
            this.code = param.code;
            this.name = param.name;
            this.bonus = param.bonus;
            this.payment = param.payment;
            this.childs = (param.childs || []).map((c) => { return new ItemTree(c); });
        }
    }

    interface IItemList {
        code: string;
        name: string;
    }

    class ItemList {
        code: string;
        name: string;
        constructor(param: IItemList) {
            this.code = param.code;
            this.name = param.name;
        }
    }
}