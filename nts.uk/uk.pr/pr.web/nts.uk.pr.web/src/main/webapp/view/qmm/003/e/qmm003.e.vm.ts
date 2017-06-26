module qmm003.e.viewmodel {
    export class ScreenModel {
        treeLeft: KnockoutObservableArray<ResidentialTaxNode>;
        treeRight: KnockoutObservableArray<ResidentialTaxNode>;
        resiTaxCodeLeft: KnockoutObservableArray<string>;
        resiTaxCodeRight: KnockoutObservable<string>;
        filteredData: KnockoutObservableArray<ResidentialTaxNode> = ko.observableArray([]);
        nodeRegionPrefectures: KnockoutObservableArray<ResidentialTaxNode> = ko.observableArray([]);
        japanLocation: Array<qmm003.b.service.model.RegionObject> = [];
        precfecture: Array<ResidentialTaxNode> = [];
        itemPrefecture: KnockoutObservableArray<ResidentialTaxNode> = ko.observableArray([]);
        residentalTaxList: KnockoutObservableArray<qmm003.e.service.model.ResidentialTax> = ko.observableArray([]);
        selectedCode: KnockoutObservable<string> = ko.observable("");
        year: KnockoutObservable<number> = ko.observable(null);
        yearJapan: KnockoutObservable<string> = ko.observable("");
        yes: boolean = true;

        constructor() {
            let self = this;
            self.init();
            self.year.subscribe(function(value) {
                self.yearJapan("(" + nts.uk.time.yearInJapanEmpire(self.year()).toString() + ")");
            });

        }

        clickButton(): any {
            let self = this;
            self.yes = true;
            nts.uk.ui.windows.setShared('yes', self.yes, true);
            if (self.resiTaxCodeLeft() && self.resiTaxCodeRight() && self.year()) {
                service.updateAllReportCode(self.resiTaxCodeLeft(), self.resiTaxCodeRight(), self.year()).done(function(data: any) {
                    nts.uk.ui.windows.close();
                }).fail(function(res: any) {
                    nts.uk.ui.dialog.alert(res.message);
                });
            } else {
                if (nts.uk.text.isNullOrEmpty(self.year())) {
                    //error 07, 01
                    nts.uk.ui.dialog.alert("対象年度  が入力されていません。");
                } else if (self.resiTaxCodeLeft().length === 0 || nts.uk.text.isNullOrEmpty(self.resiTaxCodeRight())) {
                    //error 07
                    nts.uk.ui.dialog.alert("住民税納付先コード が選択されていません。");
                }

            }
        }

        cancelButton(): void {
            this.yes = false;
            nts.uk.ui.windows.setShared('yes', this.yes, true);
            nts.uk.ui.windows.close();
        }

        init(): void {
            let self = this;
            self.treeLeft = ko.observableArray([]);
            self.treeRight = ko.observableArray([]);
            self.resiTaxCodeLeft = ko.observableArray([]);
            self.resiTaxCodeRight = ko.observable("");
        }
        //11.初期データ取得処理 11. Initial data acquisition processing
        start(): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            let self = this;
            (qmm003.e.service.getResidentialTax()).done(function(data: Array<qmm003.e.service.model.ResidentialTax>) {
                if (data.length > 0) {
                    self.residentalTaxList(data);
                    (qmm003.e.service.getRegionPrefecture()).done(function(locationData: Array<service.model.RegionObject>) {
                        self.japanLocation = locationData;
                        self.itemPrefecture(self.precfecture);
                        self.buildResidentalTaxTree();
                        let node: Array<ResidentialTaxNode> = [];
                        node = nts.uk.util.flatArray(self.nodeRegionPrefectures(), "childs");
                        self.filteredData(node);
                        self.treeLeft(self.nodeRegionPrefectures());
                        self.treeRight(self.nodeRegionPrefectures());
                    });
                } else {
                    nts.uk.ui.dialog.alert("対象データがありません。");
                }
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
            });
            return dfd.promise();
        }

        buildResidentalTaxTree() {
            let self = this;
            var child = [];
            let i = 0;
            _.each(self.residentalTaxList(), function(objResi: qmm003.a.service.model.ResidentialTaxDetailDto) {
                _.each(self.japanLocation, function(objRegion: service.model.RegionObject) {
                    let isChild: boolean = false;
                    let isPrefecture: boolean = false;
                    _.each(objRegion.prefectures, function(objPrefecture: service.model.PrefectureObject) {
                        if (objPrefecture.prefectureCode === objResi.prefectureCode) {
                            _.each(self.nodeRegionPrefectures(), function(obj: ResidentialTaxNode) {
                                if (obj.code === objRegion.regionCode) {
                                    _.each(obj.childs, function(objChild: ResidentialTaxNode) {
                                        if (objChild.code === objPrefecture.prefectureCode) {
                                            objChild.childs.push(new ResidentialTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, []));
                                            isPrefecture = true;
                                        }
                                    });
                                    if (isPrefecture === false) {
                                        obj.childs.push(new ResidentialTaxNode(objPrefecture.prefectureCode, objPrefecture.prefectureName, [new ResidentialTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, [])]));
                                    }
                                    isChild = true;
                                }
                            });
                            if (isChild === false) {
                                let chi = [];
                                self.nodeRegionPrefectures.push(new ResidentialTaxNode(objRegion.regionCode, objRegion.regionName, [new ResidentialTaxNode(objPrefecture.prefectureCode, objPrefecture.prefectureName, [new ResidentialTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, [])])]));
                            }
                        }
                    });
                });

            });
        }
    }
    export class ResidentialTaxNode {
        code: string;
        name: string;
        nodeText: string;
        custom: string;
        childs: any;
        constructor(code: string, name: string, childs: Array<ResidentialTaxNode>) {
            let self = this;
            self.code = code;
            self.name = name;
            self.nodeText = self.code + ' ' + self.name;
            self.childs = childs;
        }
    }


};