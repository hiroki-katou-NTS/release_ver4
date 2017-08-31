module nts.uk.com.view.cps006.a.viewmodel {
    import error = nts.uk.ui.errors;
    import text = nts.uk.resource.getText;
    import close = nts.uk.ui.windows.close;
    import dialog = nts.uk.ui.dialog;
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    import block = nts.uk.ui.block;

    export class ScreenModel {
        categoryList: KnockoutObservableArray<any> = ko.observableArray([]);
        currentCategory: KnockoutObservable<CategoryInfoDetail> = ko.observable((new CategoryInfoDetail({
            id: '', categoryNameDefault: '',
            categoryName: '', categoryType: 4, isAbolition: "", itemList: []
        })));
        // nếu sử dụng thì bằng true và ngược lại
        isAbolished: KnockoutObservable<boolean> = ko.observable(false);

        constructor() {
            let self = this;
            self.start(undefined);
            self.currentCategory().id.subscribe(function(value) {
                self.getDetailCategory(value);
            });
            self.isAbolished.subscribe(function(value) {
                self.categoryList.removeAll();
                if (value) {
                    service.getAllCategory().done(function(data: Array<any>) {
                        if (data.length > 0) {
                            self.categoryList(_.map(data, x => new CategoryInfo({
                                id: x.id,
                                categoryCode: x.categoryCode,
                                categoryName: x.categoryName,
                                categoryType: x.categoryType,
                                isAbolition: x.isAbolition == 1 ? "<i  style=\"margin-left: 10px\" class=\"icon icon-close\"></i>" : ""
                            })));
                            $("#category_grid").igGrid("option", "dataSource", self.categoryList());
                        }
                    });

                } else {

                    service.getAllCategory().done(function(data: Array<any>) {
                        if (data.length > 0) {
                            self.categoryList(_.map(_.filter(data, x => { return x.isAbolition == 0 }), x => new CategoryInfo({
                                id: x.id,
                                categoryCode: x.categoryCode,
                                categoryName: x.categoryName,
                                categoryType: x.categoryType,
                                isAbolition: ""
                            })));
                            let category = _.find(self.categoryList(), x => { return x.id == self.currentCategory().id() });
                            if (category === undefined) {
                                self.currentCategory().id(self.categoryList()[0].id);
                            }
                            $("#category_grid").igGrid("option", "dataSource", self.categoryList());
                        }
                    });

                }

            });

        }

        getDetailCategory(id: string) {
            let self = this;
            service.getDetailCtgInfo(id).done(function(data: any) {
                if (data) {
                    self.currentCategory().setData({
                        categoryNameDefault: data.categoryNameDefault, categoryName: data.categoryName,
                        categoryType: data.categoryType, isAbolition: data.abolition, itemList: data.itemLst
                    }, data.systemRequired);
                    if (data.itemLst.length > 0) {
                        self.currentCategory().currentItemId(data.itemLst[0].id);
                    } else {
                        self.currentCategory().currentItemId('');
                    }
                    self.currentCategory.valueHasMutated();
                }
            });
        }

        start(id: string): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            self.categoryList.removeAll();
            if (self.isAbolished()) {
                service.getAllCategory().done(function(data: Array<any>) {
                    if (data.length > 0) {
                        self.categoryList(_.map(data, x => new CategoryInfo({
                            id: x.id,
                            categoryCode: x.categoryCode,
                            categoryName: x.categoryName,
                            categoryType: x.categoryType,
                            isAbolition: x.isAbolition == 1 ? "<i  style=\"margin-left: 10px\" class=\"icon icon-close\"></i>" : "",
                        })));
                        if (id === undefined) {
                            self.currentCategory().id(self.categoryList()[0].id);
                        } else {
                            self.currentCategory().id(id);
                        }

                    }
                    dfd.resolve();
                });
            } else {
                service.getAllCategory().done(function(data: Array<any>) {
                    if (data.length > 0) {
                        self.categoryList(_.map(_.filter(data, x => { return x.isAbolition == 0 }), x => new CategoryInfo({
                            id: x.id,
                            categoryCode: x.categoryCode,
                            categoryName: x.categoryName,
                            categoryType: x.categoryType,
                            isAbolition: ""
                        })));
                        if (id === undefined) {
                            self.currentCategory().id(self.categoryList()[0].id);
                        } else {
                            self.currentCategory().id(id);
                        }

                    }
                    dfd.resolve();
                });

            }


            return dfd.promise();
        }

        openBModal() {

            let self = this;
            setShared('categoryInfo', self.currentCategory());
            block.invisible();
            nts.uk.ui.windows.sub.modal('/view/cps/006/b/index.xhtml', { title: '' }).onClosed(function(): any {
                self.start(self.currentCategory().id()).done(() => {
                    block.clear();
                });
            });
        }

        openCDL022Modal() {
            let self = this,
                cats = _.map(ko.toJS(self.categoryList), (x: any) => { return { id: x.id, name: x.categoryName }; });

            setShared('CDL020_PARAMS', cats);
            nts.uk.ui.windows.sub.modal('/view/cdl/022/a/index.xhtml', { title: '' }).onClosed(function(): any {
                let CTGlist: Array<any> = getShared('CDL020_VALUES'),
                    i: number = 0,
                    CTGsorrList = _.map(CTGlist, x => {
                        return {
                            id: x.id,
                            order: i++
                        }
                    });
                service.updateCtgOrder(CTGsorrList).done(function(data: Array<any>) {
                    self.start(undefined);
                })
            });
        }

        registerCategoryInfo() {
            let self = this,
                cat = ko.toJS(self.currentCategory),
                command = {
                    categoryId: cat.id,
                    categoryName: cat.categoryName,
                    isAbolition: cat.isAbolition

                };

            service.updateCtgInfo(command).done(function(data) {
                dialog.info({ messageId: "Msg_15" }).then(function() {
                    self.start(command.categoryId);
                });
            }).fail(function(res: any) {
                dialog.alert(res.message);
            })

        }


    }
    export interface ICategoryInfo {
        id: string;
        categoryName: string;
        categoryCode: string;
        categoryType: number;
        isAbolition: string;
    }

    export class CategoryInfo {
        id: string;
        categoryCode: string;
        categoryName: string;
        categoryType: number;
        isAbolition: string;
        constructor(params: ICategoryInfo) {
            this.id = params.id;
            this.categoryName = params.categoryName;
            this.categoryCode = params.categoryCode;
            this.categoryType = params.categoryType;
            this.isAbolition = params.isAbolition;
        }

    }

    export interface IItemInfo {
        id: string;
        perInfoCtgId: string;
        itemName: string;
        systemRequired: number;
        isAbolition: string;
    }

    export class ItemInfo {
        id: string;
        perInfoCtgId: string;
        itemName: string;
        systemRequired: number;
        isAbolition: string;
        constructor(params: IItemInfo) {
            this.id = params.id;
            this.perInfoCtgId = params.perInfoCtgId;
            this.itemName = params.itemName;
            this.systemRequired = params.systemRequired;
            this.isAbolition = params.isAbolition;
        }
    }

    export interface ICategoryInfoDetail {
        id: string;
        categoryNameDefault: string;
        categoryName: string;
        categoryType: number;
        isAbolition: string;
        itemList?: Array<any>;
    }

    export class CategoryInfoDetail {
        id: KnockoutObservable<string>;
        categoryNameDefault: string;
        categoryName: KnockoutObservable<string>;
        categoryType: number;
        isAbolition: KnockoutObservable<boolean>;
        displayIsAbolished: number = 0;
        itemList: KnockoutObservableArray<any>;
        currentItemId: KnockoutObservable<string> = ko.observable('');
        itemColums: KnockoutObservableArray<any> = ko.observableArray([
            { headerText: 'id', key: 'id', width: 100, hidden: true },
            { headerText: text('CPS006_16'), key: 'itemName', width: 250 },
            { headerText: text('CPS006_17'), key: 'isAbolition', width: 50, formatter: makeIcon }
        ]);
        ctgColums: KnockoutObservableArray<any> = ko.observableArray([
            { headerText: 'id', key: 'id', width: 100, hidden: true },
            { headerText: text('CPS006_6'), key: 'categoryName', width: 230 },
            { headerText: text('CPS006_7'), key: 'isAbolition', width: 50 }
        ]);
        constructor(params: ICategoryInfoDetail) {
            this.id = ko.observable("");
            this.categoryNameDefault = params.categoryNameDefault;
            this.categoryName = ko.observable(params.categoryName);
            this.categoryType = params.categoryType;
            this.isAbolition = ko.observable(false);
            this.itemList = ko.observableArray(params.itemList || []);
        }

        setData(params: any, displayIsAbolished: number) {
            this.categoryNameDefault = params.categoryNameDefault;
            this.categoryName(params.categoryName);
            this.categoryType = params.categoryType;
            this.isAbolition(params.isAbolition);
            this.displayIsAbolished = displayIsAbolished;
            this.itemList(params.itemList);
        }
    }
    function makeIcon(value, row) {
        if (value == '1')
            return "<i  style=\"margin-left: 10px\" class=\"icon icon-close\"></i>";
        return '';
    }
}