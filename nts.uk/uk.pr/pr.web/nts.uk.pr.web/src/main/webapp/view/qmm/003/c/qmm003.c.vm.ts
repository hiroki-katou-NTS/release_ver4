module qmm003.c.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<ResidentalTaxNode>;
        singleSelectedCode: KnockoutObservable<string>;
        filteredData: KnockoutObservableArray<ResidentalTaxNode> = ko.observableArray([]);
        currentNode: service.model.ResidentialTaxDto = null;
        nodeRegionPrefectures: KnockoutObservableArray<ResidentalTaxNode> = ko.observableArray([]);
        japanLocation: Array<service.model.RegionObject> = [];
        precfecture: Array<ResidentalTaxNode> = [];
        itemPrefecture: KnockoutObservableArray<ResidentalTaxNode> = ko.observableArray([]);
        residentalTaxList: KnockoutObservableArray<service.model.ResidentialTaxDto> = ko.observableArray([]);
        yes: boolean = null;
        constructor() {
            let self = this;
            self.init();
            self.singleSelectedCode.subscribe(function(newValue) {
                if (newValue.length > 2) {
                    self.currentNode = _.find(self.residentalTaxList(), function(obj: service.model.ResidentialTaxDto) {
                        return obj.resiTaxCode = newValue;
                    });
                }
            });

        }
        clickButton(): any {
            let self = this;
            self.yes = true;
            nts.uk.ui.windows.setShared('yes', self.yes, true);
            if (self.currentNode) {
                nts.uk.ui.windows.setShared('currentNode', self.currentNode, true);
                nts.uk.ui.windows.close();
            } else {
                nts.uk.ui.dialog.alert("住民税納付先コード が選択されていません。");
            }

        }
        cancelButton(): void {
            let self = this;
            self.yes = false;
            nts.uk.ui.windows.setShared('yes', self.yes, true);
            nts.uk.ui.windows.close();
        }
        init(): void {
            let self = this;
            self.items = ko.observableArray([]);
            self.singleSelectedCode = ko.observable("");
        }
        //11.初期データ取得処理 11. Initial data acquisition processing
        start(): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            let self = this;
            (qmm003.c.service.getResidentialTax()).done(function(data: Array<qmm003.c.service.model.ResidentialTaxDto>) {
                if (data.length > 0) {
                    self.residentalTaxList(data);
                    console.log(data);
                    (qmm003.c.service.getRegionPrefecture()).done(function(locationData: Array<service.model.RegionObject>) {
                        self.japanLocation = locationData;
                        self.buildResidentalTaxTree();
                        let node: Array<ResidentalTaxNode> = [];
                        node = nts.uk.util.flatArray(self.nodeRegionPrefectures(), "childs");
                        self.filteredData(node);
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
            _.each(self.residentalTaxList(), function(objResi: service.model.ResidentialTax) {
                _.each(self.japanLocation, function(objRegion: service.model.RegionObject) {
                    let isChild: boolean = false;
                    let isPrefecture: boolean = false;
                    _.each(objRegion.prefectures, function(objPrefecture: service.model.PrefectureObject) {
                        if (objPrefecture.prefectureCode === objResi.prefectureCode) {
                            _.each(self.nodeRegionPrefectures(), function(obj: ResidentalTaxNode) {
                                if (obj.code === objRegion.regionCode) {
                                    _.each(obj.childs, function(objChild: ResidentalTaxNode) {
                                        if (objChild.code === objPrefecture.prefectureCode) {
                                            objChild.childs.push(new ResidentalTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, []));
                                            isPrefecture = true;
                                        }
                                    });
                                    if (isPrefecture === false) {
                                        obj.childs.push(new ResidentalTaxNode(objPrefecture.prefectureCode, objPrefecture.prefectureName, [new ResidentalTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, [])]));
                                    }
                                    isChild = true;
                                }
                            });
                            if (isChild === false) {
                                let chi = [];
                                self.nodeRegionPrefectures.push(new ResidentalTaxNode(objRegion.regionCode, objRegion.regionName, [new ResidentalTaxNode(objPrefecture.prefectureCode, objPrefecture.prefectureName, [new ResidentalTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, [])])]));
                            }
                        }
                    });
                });

            });
        }

    }
    export class ResidentalTaxNode {
        code: string;
        name: string;
        nodeText: string;
        custom: string;
        childs: any;
        constructor(code: string, name: string, childs: Array<ResidentalTaxNode>) {
            let self = this;
            self.code = code;
            self.name = name;
            self.nodeText = self.code + ' ' + self.name;
            self.childs = childs;
        }
    }


};