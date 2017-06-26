module qmm003.a.viewmodel {
    export class ScreenModel {
        // data of items list - tree grid
        redensitalTaxNodeList: KnockoutObservableArray<RedensitalTaxNode>;
        singleSelectedCode: KnockoutObservable<string>;
        firstSelectedCode: KnockoutObservable<string> = ko.observable("");
        enableBTN009: KnockoutObservable<boolean>;
        enableBTN005: KnockoutObservable<boolean> = ko.observable(null);
        enableBTN006: KnockoutObservable<boolean> = ko.observable(null);
        isNew: KnockoutObservable<boolean> = ko.observable(true); // mode update , new mode if true
        filteredData: KnockoutObservableArray<RedensitalTaxNode> = ko.observableArray([]);
        nodeRegionPrefectures: KnockoutObservableArray<RedensitalTaxNode> = ko.observableArray([]);
        japanLocation: Array<service.model.RegionObject> = [];
        precfecture: Array<RedensitalTaxNode> = [];
        itemPrefecture: KnockoutObservableArray<RedensitalTaxNode> = ko.observableArray([]);
        //companyCode != 0000
        residentalTaxList: KnockoutObservableArray<service.model.ResidentialTaxDto> = ko.observableArray([]);
        //companyCode = 0000
        residentalTaxList000: KnockoutObservableArray<service.model.ResidentialTaxDto> = ko.observableArray([]);
        currentResidential: KnockoutObservable<ResidentialTax> = ko.observable(null);
        dirtyObject: nts.uk.ui.DirtyChecker;
        previousCurrentCode: string = null; //lưu giá trị của currentCode trước khi nó bị thay đổi
        numberOfResidentialTax: KnockoutObservable<string> = ko.observable('');
        constructor() {
            let self = this;
            self.init();
            self.singleSelectedCode.subscribe(function(newValue) {
                if (newValue.length === 1) {
                    let index: number;
                    index = _.findIndex(self.redensitalTaxNodeList(), function(obj: RedensitalTaxNode) {
                        return obj.code === newValue;

                    })
                    self.processWhenCurrentCodeChange(self.redensitalTaxNodeList()[index].childs[0].childs[0].code);
                    return;
                }
                if (newValue.length === 2) {
                    let prefecture = [];
                    prefecture = self.findIndex(self.redensitalTaxNodeList(), newValue);
                    self.processWhenCurrentCodeChange(self.redensitalTaxNodeList()[prefecture[0]].childs[prefecture[1]].childs[0].code);
                    return;
                }

                if (!nts.uk.text.isNullOrEmpty(newValue) && (self.singleSelectedCode() !== self.previousCurrentCode)) {
                    if (self.dirtyObject.isDirty()) {
                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\nよろしいですか。?").ifYes(function() {
                            self.processWhenCurrentCodeChange(newValue);
                        }).ifCancel(function() {

                            if (nts.uk.text.isNullOrEmpty(self.previousCurrentCode)) {
                                self.singleSelectedCode('');
                                self.isNew(true);
                                self.enableBTN005(false);
                            } else {
                                self.singleSelectedCode(self.previousCurrentCode);
                            }
                        })

                    } else {
                        self.processWhenCurrentCodeChange(newValue);
                    }
                }
            });
        }

        // tìm index để khi chọn root thì ra hiển thị ra thằng đầu tiên của 1 thằng root
        findIndex(items: Array<RedensitalTaxNode>, newValue: string): any {
            let index: number;
            let count: number = -1;
            let prefectureArray = [];
            _.each(items, function(obj: RedensitalTaxNode) {
                count++;
                index = _.findIndex(obj.childs, function(obj1: RedensitalTaxNode) {
                    return obj1.code === newValue;
                });
                if (index > -1) {
                    prefectureArray.push(count, index);
                }

            });

            return prefectureArray;
        }

        // get Data khi currentCode thay đổi
        processWhenCurrentCodeChange(newValue: string) {
            let self = this;
            service.getResidentialTaxDetail(newValue).done(function(data: service.model.ResidentialTaxDetailDto) {
                if (data) {
                    if ($('.nts-editor').ntsError("hasError")) {
                        $('.save-error').ntsError('clear');
                    }
                    if (data.resiTaxReportCode) {
                        let residential: service.model.ResidentialTaxDto;
                        _.each(self.residentalTaxList000(), function(objResi: service.model.ResidentialTaxDto) {
                            if (objResi.resiTaxCode === data.resiTaxReportCode) {
                                residential = objResi;
                            }
                        });
                        if (!residential) {
                            _.each(self.residentalTaxList(), function(objResi: service.model.ResidentialTaxDto) {
                                if (objResi.resiTaxCode === data.resiTaxReportCode) {
                                    residential = objResi;
                                }
                            });
                        }
                        data.resiTaxReportCode = residential.resiTaxCode + " " + residential.resiTaxAutonomy;
                    }
                    self.isNew(false);
                    self.enableBTN005(true);
                    self.currentResidential().setData(data);
                    self.previousCurrentCode = newValue;
                    self.dirtyObject.reset();
                } else {
                    self.resetData();
                }
            });
        }

        // create array prefecture from japan location
        buildPrefectureArray(): void {
            let self = this;
            _.map(self.japanLocation, function(region: service.model.RegionObject) {
                _.each(region.prefectures, function(objPrefecture: service.model.PrefectureObject) {
                    return self.precfecture.push(new RedensitalTaxNode(objPrefecture.prefectureCode, objPrefecture.prefectureName, []));
                });
            });

        }

        // reset Data
        resetData(): void {
            let self = this;
            if ($('.nts-editor').ntsError("hasError")) {
                $('.save-error').ntsError('clear');
            }
            self.currentResidential().enableBTN009(true);
            self.isNew(true);
            self.enableBTN005(false);
            self.currentResidential().resiTaxCode('');
            self.currentResidential().resiTaxAutonomy('');
            self.currentResidential().resiTaxAutonomyKana('');
            self.currentResidential().prefectureCode('');
            self.currentResidential().resiTaxReportCode('');
            self.currentResidential().registeredName('');
            self.currentResidential().companyAccountNo('');
            self.currentResidential().companySpecifiedNo('');
            self.currentResidential().cordinatePostalCode('');
            self.currentResidential().cordinatePostOffice('');
            self.currentResidential().memo('');
            self.previousCurrentCode = '';
            self.singleSelectedCode("");
            self.dirtyObject.reset();
            $("#A_INP_002").focus();
        }

        // init menu
        init(): void {
            let self = this;
            // data of treegrid
            self.redensitalTaxNodeList = ko.observableArray([]);
            self.singleSelectedCode = ko.observable("");
            self.currentResidential = ko.observable(new ResidentialTax({
                resiTaxCode: '',
                resiTaxAutonomy: '',
                resiTaxAutonomyKana: '',
                prefectureCode: '',
                registeredName: '',
                companyAccountNo: '',
                companySpecifiedNo: ''
            }));
        }

        // khi click vào btn006 mở ra màn hình D
        openDDialog() {
            let self = this;
            let yes: any;
            nts.uk.ui.windows.sub.modeless("/view/qmm/003/d/index.xhtml", { title: '住民税納付先の登録　＞　一括削除', dialogClass: "no-close" }).onClosed(function(): any {
                yes = nts.uk.ui.windows.getShared("yes");
                if (yes) {
                    self.redensitalTaxNodeList([]);
                    self.nodeRegionPrefectures([]);
                    self.reload(undefined);
                }
            });

        }

        // xóa một đối tượng 
        deleteAResidential(): void {
            let self = this;
            let objResidential: ResidentialTax;
            objResidential = (ko.toJS(self.currentResidential()));
            let resiTaxCodes = [];
            resiTaxCodes.push(objResidential.resiTaxCode);
            nts.uk.ui.dialog.confirm("データを削除します。\r\n よろしいですか？").ifYes(function() {
                if (resiTaxCodes) {
                    qmm003.a.service.deleteResidential(resiTaxCodes).done(function(data) {
                        self.redensitalTaxNodeList([]);
                        self.nodeRegionPrefectures([]);
                        self.reload(self.firstSelectedCode());
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alert(res.message);
                    });
                } else {
                    nts.uk.ui.dialog.alert("住民税納付先コード が選択されていません。す。");
                }
            })

        }

        // khi click btn004 mở ra màn hình E - chức năng hợp nhất nơi đăng kí thuế cư trú
        openEDialog() {
            let self = this;
            let yes: any;
            if (self.redensitalTaxNodeList().length > 0) {
                nts.uk.ui.windows.sub.modeless("/view/qmm/003/e/index.xhtml", { title: '住民税納付先の登録＞納付先の統合', dialogClass: "no-close" }).onClosed(function(): any {
                    yes = nts.uk.ui.windows.getShared('yes');
                    if (yes) {
                        self.redensitalTaxNodeList([]);
                        self.nodeRegionPrefectures([]);
                        self.reload(undefined);
                    }
                });
            }
        }

        //11.初期データ取得処理 11. Initial data acquisition processing
        start(currentSelectedCode: string): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            let self = this;
            service.getResidentialTaxCCD().done(function(data: Array<service.model.ResidentialTaxDto>) {
                self.residentalTaxList000(data);
            });
            (qmm003.a.service.getResidentialTax()).done(function(data: Array<service.model.ResidentialTaxDto>) {
                if (data.length > 0) {
                    self.isNew(false);
                    self.enableBTN005(true);
                    self.enableBTN006(true);
                    self.residentalTaxList(data);
                    self.numberOfResidentialTax(" 【登録件数】" + data.length + "  件");
                    self.getJapanLocation(currentSelectedCode);
                } else {
                    self.settingNewMode();
                    self.enableBTN006(false);
                    self.currentResidential().enableBTN009(false);
                }

                dfd.resolve();

            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            });

            return dfd.promise();
        }

        // Khi xóa dữ liệu xong rồi  mà list bên trái  = [] 
        //thì sẽ gọi hàm reload ra để reset các giá trị ở màn hình bên phải
        reload(currentSelectedCode: string): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            let self = this;

            (qmm003.a.service.getResidentialTax()).done(function(data: Array<service.model.ResidentialTaxDto>) {
                if (data.length > 0) {
                    self.currentResidential().editMode = true; // true, update mode 
                    self.residentalTaxList(data);
                    self.numberOfResidentialTax(" 【登録件数】" + data.length + "  件");
                    self.getJapanLocation(currentSelectedCode);
                    self.isNew(false);
                    self.enableBTN005(true);
                    self.enableBTN006(true);
                } else {
                    self.resetData();
                    self.numberOfResidentialTax(" 【登録件数】" + data.length + "  件");
                    self.settingNewMode();
                    self.currentResidential().enableBTN009(false);
                    self.enableBTN006(false);
                }

                dfd.resolve();

            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            });

            return dfd.promise();
        }

        // lấy dữ liệu japan location
        getJapanLocation(currentSelectedCode: string) {
            let self = this;
            (qmm003.a.service.getRegionPrefecture()).done(function(locationData: Array<service.model.RegionObject>) {
                self.japanLocation = locationData;
                self.buildPrefectureArray();
                self.itemPrefecture(self.precfecture);
                self.buildResidentalTaxTree();
                let node: Array<RedensitalTaxNode> = [];
                node = nts.uk.util.flatArray(self.nodeRegionPrefectures(), "childs");
                self.filteredData(node);
                self.redensitalTaxNodeList(self.nodeRegionPrefectures());
                self.dirtyObject = new nts.uk.ui.DirtyChecker(self.currentResidential);
                if (currentSelectedCode === undefined) {
                    self.singleSelectedCode(self.firstSelectedCode());
                } else {
                    self.singleSelectedCode(currentSelectedCode);
                }
                self.currentResidential().enableBTN009(true);
                self.isNew(false);
                self.enableBTN005(true);
            });
        }

        // setting chế độ thêm mới (trường hợp get List data = 0)
        settingNewMode() {
            let self = this;
            (qmm003.a.service.getRegionPrefecture()).done(function(locationData: Array<service.model.RegionObject>) {
                self.japanLocation = locationData;
                self.buildPrefectureArray();
                self.itemPrefecture(self.precfecture);
            });
            self.numberOfResidentialTax(" 【登録件数】 0  件");
            self.isNew(true);
            self.enableBTN005(false);
            $("#A_INP_002").focus();
        }

        // hàm xây dựng tree
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
                            _.each(self.nodeRegionPrefectures(), function(obj: RedensitalTaxNode) {
                                if (obj.code === objRegion.regionCode) {
                                    _.each(obj.childs, function(objChild: RedensitalTaxNode) {
                                        if (objChild.code === objPrefecture.prefectureCode) {
                                            objChild.childs.push(new RedensitalTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, []));
                                            isPrefecture = true;
                                        }
                                    });
                                    if (isPrefecture === false) {
                                        obj.childs.push(
                                            new RedensitalTaxNode(objPrefecture.prefectureCode, objPrefecture.prefectureName,
                                                [new RedensitalTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, [])]));
                                    }
                                    isChild = true;
                                }
                            });
                            if (isChild === false) {
                                let child = [];
                                self.nodeRegionPrefectures.push(
                                    new RedensitalTaxNode(objRegion.regionCode, objRegion.regionName,
                                        [new RedensitalTaxNode(objPrefecture.prefectureCode, objPrefecture.prefectureName,
                                            [new RedensitalTaxNode(objResi.resiTaxCode, objResi.resiTaxAutonomy, [])])]));
                            }
                        }
                    });
                });

            });
            self.firstSelectedCode(self.nodeRegionPrefectures()[0].childs[0].childs[0].code);
        }

        validateData(): boolean {
            $(".nts-editor").ntsEditor("validate");
            $("#A_INP_002").ntsEditor("validate");
            $("#A_INP_003").ntsEditor("validate");
            $("#A_INP_004").ntsEditor("validate");
            $("#A_INP_005").ntsEditor("validate");
            $("#A_INP_006").ntsEditor("validate");
            $("#A_INP_007").ntsEditor("validate");
            $("#A_INP_008").ntsEditor("validate");
            $("#A_SEL_001").ntsEditor("validate");
            let errorA: boolean = false;
            errorA = $("#A_INP_002").ntsError('hasError') || $("#A_INP_003").ntsError('hasError')
                || $("#A_INP_004").ntsError('hasError') || $("#A_INP_005").ntsError('hasError')
                || $("#A_INP_006").ntsError('hasError') || $("#A_INP_007").ntsError('hasError')
                || $("#A_INP_008").ntsError('hasError') || $("#A_SEL_001").ntsError('hasError');
            if ($(".nts-editor").ntsError('hasError') || errorA) {
                return false;
            }
            return true;
        }

        // add va update doi tuong residentialTax
        clickRegister(): void {
            let self = this;
            let objResi = new service.model.ResidentialTaxDetailDto();
            let node = new RedensitalTaxNode('', '', []);
            if (!self.validateData()) {
                return;
            }
            objResi = ko.toJS(self.currentResidential());
            if (!objResi.resiTaxReportCode) {
                objResi.resiTaxReportCode = objResi.resiTaxReportCode;
            } else {
                objResi.resiTaxReportCode = objResi.resiTaxReportCode.substring(0, 6);
            }
            if (self.isNew()) {
                qmm003.a.service.addResidential(objResi).done(function() {
                    self.redensitalTaxNodeList([]);
                    self.nodeRegionPrefectures([]);
                    self.start(objResi.resiTaxCode);

                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
            } else {
                qmm003.a.service.updateData(objResi).done(function() {
                    self.redensitalTaxNodeList([]);
                    self.nodeRegionPrefectures([]);
                    self.start(objResi.resiTaxCode);
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
            }
        }
    }

    //insert an update  ResidentialTax Object
    export class ResidentialTax {
        editMode: boolean = null; // true là mode thêm mới, false là mode update 
        enableBTN009: KnockoutObservable<boolean> = ko.observable(null);
        resiTaxCode: KnockoutObservable<string>;
        resiTaxAutonomy: KnockoutObservable<string>;
        resiTaxAutonomyKana: KnockoutObservable<string>;
        prefectureCode: KnockoutObservable<string>;
        resiTaxReportCode: KnockoutObservable<string>;
        registeredName: KnockoutObservable<string>;
        companyAccountNo: KnockoutObservable<string>;
        companySpecifiedNo: KnockoutObservable<string>;
        cordinatePostalCode: KnockoutObservable<string>;
        cordinatePostOffice: KnockoutObservable<string>;
        memo: KnockoutObservable<string>;
        constructor(param: ResidentialTaxBase) {
            this.init(param);
        }

        setData(param: any) {
            let self = this;
            self.resiTaxCode(param.resiTaxCode);
            self.resiTaxAutonomy(param.resiTaxAutonomy);
            self.resiTaxAutonomyKana(param.resiTaxAutonomyKana);
            self.prefectureCode(param.prefectureCode);
            self.resiTaxReportCode(param.resiTaxReportCode);
            self.registeredName(param.registeredName);
            self.companyAccountNo(param.companyAccountNo);
            self.companySpecifiedNo(param.companySpecifiedNo);
            self.cordinatePostalCode(param.cordinatePostalCode);
            self.cordinatePostOffice(param.cordinatePostOffice);
            self.memo(param.memo);
        }

        init(param: any) {
            this.resiTaxCode = ko.observable(param.resiTaxCode);
            this.resiTaxAutonomy = ko.observable(param.resiTaxAutonomy);
            this.resiTaxAutonomyKana = ko.observable(param.resiTaxAutonomyKana);
            this.prefectureCode = ko.observable(param.prefectureCode);
            this.resiTaxReportCode = ko.observable(param.resiTaxReportCode);
            this.registeredName = ko.observable(param.registeredName);
            this.companyAccountNo = ko.observable(param.companyAccountNo);
            this.companySpecifiedNo = ko.observable(param.companySpecifiedNo);
            this.cordinatePostalCode = ko.observable(param.cordinatePostalCode);
            this.cordinatePostOffice = ko.observable(param.cordinatePostOffice);
            this.memo = ko.observable(param.memo);
        }
        // khi click btn007 mo ra man hinh B
        openBDialog() {
            let self = this;
            let currentNode: service.model.ResidentialTaxDetailDto;
            let yes: boolean;
            nts.uk.ui.windows.sub.modeless('/view/qmm/003/b/index.xhtml', { title: '住民税納付先の登録＞住民税納付先一覧', dialogClass: "no-close" }).onClosed(function(): any {
                currentNode = nts.uk.ui.windows.getShared("currentNode");
                yes = nts.uk.ui.windows.getShared("yes");
                if (yes) {
                    self.resiTaxCode(currentNode.resiTaxCode);
                    self.resiTaxAutonomy(currentNode.resiTaxAutonomy);
                    self.resiTaxAutonomyKana(currentNode.resiTaxAutonomyKana);
                    self.prefectureCode(currentNode.prefectureCode);
                    $('#A_INP_002').ntsError('clear');
                    $('#A_INP_003').ntsError('clear');
                }
            });
        }

        // khi click btn009 mo ra man hinh C 
        openCDialog() {
            let self = this;
            let currentNode: any;
            let yes: boolean;
            nts.uk.ui.windows.sub.modeless("/view/qmm/003/c/index.xhtml", { title: '住民税納付先の登録＞住民税報告先一覧', dialogClass: "no-close" }).onClosed(function(): any {
                currentNode = nts.uk.ui.windows.getShared('currentNode');
                yes = nts.uk.ui.windows.getShared('yes');
                if (yes) {
                    self.resiTaxReportCode(currentNode.resiTaxCode + " " + currentNode.resiTaxAutonomy);
                }
            });
        }
    }

    interface ResidentialTaxBase {
        resiTaxCode: string;
        resiTaxAutonomy: string;
        resiTaxAutonomyKana: string;
        prefectureCode: string;
        resiTaxReportCode?: string;
        registeredName: string;
        companyAccountNo: string;
        companySpecifiedNo: string;
        cordinatePostalCode?: string;
        cordinatePostOffice?: string;
        memo?: string;
    }

    // class xay dung tree grid
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

};
