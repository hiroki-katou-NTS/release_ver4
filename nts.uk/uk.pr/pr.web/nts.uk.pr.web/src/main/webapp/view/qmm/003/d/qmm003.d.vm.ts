module qmm003.d.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<RedensitalTaxNode>;
        selectedCode: KnockoutObservableArray<string>;
        filteredData: KnockoutObservableArray<RedensitalTaxNode> = ko.observableArray([]);
        filteredData1: KnockoutObservableArray<RedensitalTaxNode> = ko.observableArray([]);
        filteredData2: KnockoutObservableArray<RedensitalTaxNode> = ko.observableArray([]);
        filteredData3: KnockoutObservableArray<RedensitalTaxNode> = ko.observableArray([]);
        arrayNode: KnockoutObservableArray<string> = ko.observableArray([]);
        nodeRegionPrefectures: KnockoutObservableArray<RedensitalTaxNode> = ko.observableArray([]);
        japanLocation: Array<qmm003.d.service.model.RegionObject> = [];
        precfecture: Array<RedensitalTaxNode> = [];
        itemPrefecture: KnockoutObservableArray<RedensitalTaxNode> = ko.observableArray([]);
        residentalTaxList: KnockoutObservableArray<qmm003.d.service.model.ResidentialTax> = ko.observableArray([]);
        currentResidential: KnockoutObservable<service.model.ResidentialTax> = ko.observable(null);
        indexOfRoot = [];
        indexOfPrefecture = [];
        yes: boolean = null; // cancel or register

        constructor() {
            let self = this;
            self.init();
            self.selectedCode.subscribe(function(newValue) {
                self.arrayNode(newValue);
            });
        }

        init(): void {
            let self = this;
            self.items = ko.observableArray([]);
            self.selectedCode = ko.observableArray([]);
        }

        clickButton(): void {
            let self = this;
            let resiTaxCodes = [];
            let resiTax = [];
            self.yes = true;
            for (let i = 0; i < self.arrayNode().length; i++) {
                resiTaxCodes.push(self.arrayNode()[i]);
            }
            nts.uk.ui.windows.setShared('yes', self.yes, true);
            if (resiTaxCodes.length > 0) {
                qmm003.d.service.deleteResidential(resiTaxCodes).done(function(data) {
                    self.items([]);
                    self.nodeRegionPrefectures([]);
                    nts.uk.ui.windows.close();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
            } else {
                nts.uk.ui.dialog.alert("住民税納付先コード が選択されていません。");
            }
        }
        cancelButton(): void {
            this.yes = false;
            nts.uk.ui.windows.setShared('items', this.items(), true);
            nts.uk.ui.windows.setShared('yes', this.yes, true);
            nts.uk.ui.windows.close();
        }
        //11.初期データ取得処理 11. Initial data acquisition processing
        start(): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            let self = this;
            (qmm003.d.service.getResidentialTax()).done(function(data: Array<qmm003.d.service.model.ResidentialTax>) {
                if (data.length > 0) {
                    self.residentalTaxList(data);
                    (qmm003.d.service.getRegionPrefecture()).done(function(locationData: Array<service.model.RegionObject>) {
                        self.japanLocation = locationData;
                        self.buildResidentalTaxTree();
                        let node: Array<RedensitalTaxNode> = [];
                        node = nts.uk.util.flatArray(self.nodeRegionPrefectures(), "childs");
                        let node1: Array<RedensitalTaxNode> = [];
                        node1 = nts.uk.util.flatArray(self.nodeRegionPrefectures(), "childs");
                        let node2: Array<RedensitalTaxNode> = [];
                        node2 = nts.uk.util.flatArray(self.nodeRegionPrefectures(), "childs");
                        let node3: Array<RedensitalTaxNode> = [];
                        node3 = nts.uk.util.flatArray(self.nodeRegionPrefectures(), "childs");
                        self.filteredData(node);
                        self.filteredData1(node1);
                        self.filteredData2(node2);
                        self.filteredData3(node3);
                        self.items(self.nodeRegionPrefectures());
                    });
                } else {
                    nts.uk.ui.dialog.alert("対象データがありません。");
                }
                dfd.resolve();

            }).fail(function(res) {

            });

            return dfd.promise();
        }

        buildResidentalTaxTree() {
            let self = this;
            var child = [];
            let i = 0;
            _.each(self.residentalTaxList(), function(objResi: qmm003.d.service.model.ResidentialTax) {
                _.each(self.japanLocation, function(objRegion: service.model.RegionObject) {
                    let isChild: boolean = false;
                    let isPrefecture: boolean = false;
                    _.each(objRegion.prefectures, function(objPrefecture: service.model.PrefectureObject) {
                        if (objPrefecture.prefectureCode === objResi.prefectureCode) {
                            _.each(self.nodeRegionPrefectures(), function(obj: RedensitalTaxNode) {
                                if (obj.code === objRegion.regionCode) {
                                    _.each(obj.childs, function(objChild: RedensitalTaxNode) {
                                        if (objChild.code === objPrefecture.prefectureCode) {
                                            objChild.childs.push(new RedensitalTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, []));
                                            isPrefecture = true;
                                        }
                                    });
                                    if (isPrefecture === false) {
                                        obj.childs.push(new RedensitalTaxNode(objPrefecture.prefectureCode, objPrefecture.prefectureName, [new RedensitalTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, [])]));
                                    }
                                    isChild = true;
                                }
                            });
                            if (isChild === false) {
                                let chi = [];
                                self.nodeRegionPrefectures.push(new RedensitalTaxNode(objRegion.regionCode, objRegion.regionName, [new RedensitalTaxNode(objPrefecture.prefectureCode, objPrefecture.prefectureName, [new RedensitalTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, [])])]));
                            }
                        }
                    });
                });

            });
        }
    }
    export class RedensitalTaxNode {
        code: string;
        name: string;
        nodeText: string;
        childs: any;
        constructor(code: string, name: string, childs: Array<RedensitalTaxNode>) {
            let self = this;
            self.code = code;
            self.name = name;
            self.nodeText = self.code + ' ' + self.name;
            self.childs = childs;

        }
    }

}