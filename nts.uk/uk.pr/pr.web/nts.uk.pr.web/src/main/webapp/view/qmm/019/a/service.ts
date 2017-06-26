module qmm019.a {
    export module service {
        var paths: any = {
            getAllLayoutHead: "pr/proto/layout/findalllayoutHead",
            getAllLayoutHist: "pr/proto/layout/findalllayoutHist",
//            getLayoutInfor : "/pr/proto/layout/findlayout/{0}/{1}",
//            getLayoutsWithMaxStartYm: "pr/proto/layout/findlayoutwithmaxstartym",
            getCategoryFull: "pr/proto/layout/findCategoies/full",
            registerLayout: "pr/proto/layout/register"
        }


        /**
         * Get list payment date processing.
         */
        export function getAllLayoutHead(): JQueryPromise<Array<model.LayoutHeadDto>> {
            var dfd = $.Deferred<Array<any>>();
            nts.uk.request.ajax(paths.getAllLayoutHead)
                .done(function(res: Array<any>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        
        /**
         * Get list payment date processing.
         */
        export function getAllLayoutHist(): JQueryPromise<Array<model.LayoutMasterDto>> {
            var dfd = $.Deferred<Array<any>>();
            nts.uk.request.ajax(paths.getAllLayoutHist)
                .done(function(res: Array<any>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        
//        /**
//         * Get layout master 
//         */
//        export function getLayout(stmtCode: string, historyId: string): JQueryPromise<model.LayoutMasterDto> {
//            var dfd = $.Deferred<any>();
//            var _path = nts.uk.text.format(paths.getLayoutInfor, stmtCode, historyId);
//            nts.uk.request.ajax(_path)
//                .done(function(res: any){
//                    dfd.resolve(res);
//                })
//                .fail(function(res) {
//                    dfd.reject(res);
//                })
//            return dfd.promise(); 
//        }
        /**
         * Get list payment date processing.
         */
        export function getLayoutsWithMaxStartYm(): JQueryPromise<Array<model.LayoutMasterDto>> {
            var dfd = $.Deferred<Array<any>>();
            nts.uk.request.ajax(paths.getLayoutsWithMaxStartYm)
                .done(function(res: Array<any>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        /**
         * Get list getCategoryFull.
         */
        export function getCategoryFull(layoutCode, historyId, startYm): JQueryPromise<Array<model.Category>> {
            var dfd = $.Deferred<Array<model.Category>>();
            nts.uk.request.ajax(paths.getCategoryFull + "/" + layoutCode + "/" + historyId + "/" + startYm)
                .done(function(res: Array<model.Category>) {
                    var result = _.map(res, function(category: any) {
                        return new model.Category(category.lines, category.categoryAtr);
                    });
                    dfd.resolve(result);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        
        /**
         * Register Layout
         */
        export function registerLayout(layout: model.LayoutMasterDto, categories: Array<model.Category>): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            var categoryCommand = [], lineCommand = [], detailCommand = [];
            var listCategoryAtrDeleted = [], listAutoLineIdDeleted = [], listItemCodeDeleted = [];
            var categoryPosition = 1;
            for (let category of categories) {
                if (category.isRemoved === true) {
                    // Truong hop remove category thi remove luon line va detail
                    listCategoryAtrDeleted.push(category.categoryAtr);
                } else {
                    categoryCommand.push({categoryAtr: category.categoryAtr, categoryPosition: categoryPosition});
                    let linePosition = 1;
                    let sortedLines = $("#" + category.categoryAtr).sortable("toArray");
                    for (let itemLine of sortedLines) {
                        let line : model.Line = _.find(category.lines(), function(lineDetail){
                            return lineDetail.rowId === itemLine.toString();
                        });
                        if (line.isRemoved !== true) {
                            lineCommand.push({categoryAtr: category.categoryAtr, 
                                                autoLineId: line.autoLineId, 
                                                linePosition: linePosition, 
                                                lineDisplayAtr: line.lineDispayAtr});
                        } else {
                            if (_.includes(line.autoLineId, "lineIdTemp-") === false) {
                                listAutoLineIdDeleted.push({categoryAtr: category.categoryAtr, autoLineId: line.autoLineId});
                                continue;
                            }
                        }    
                        linePosition++;
                        let itemPosColumn = 1;
                        let sortedItemCodes = $("#" + line.rowId).sortable("toArray");
                        // Vì item mà required thì ko được sortable nên cần kiểm tra để thêm item này vào còn save.
                        if (line.hasRequiredItem) {
                            let detailRequired = _.find(line.details, function(requireItem) {
                                return requireItem.isRequired();    
                            });
                            sortedItemCodes.push(detailRequired.itemCode());
                        }
                        for (let item of sortedItemCodes) {
                            let detail : model.ItemDetail = _.find(line.details, function(itemDetail){
                                return itemDetail.itemCode() === item.toString();
                            });
                            if (detail.isRemoved !== true) {
                                detailCommand.push({
                                    categoryAtr: category.categoryAtr, 
                                    itemCode: detail.itemCode(),
                                    updateItemCode: detail.updateItemCode(),
                                    added: detail.added(),
                                    autoLineId: detail.autoLineId(),
                                    itemPosColumn: itemPosColumn,
                                    calculationMethod: detail.calculationMethod(),
                                    displayAtr: line.lineDispayAtr,
                                    sumScopeAtr: detail.sumScopeAtr(),
                                    setOffItemCode: detail.setOffItemCode(),
                                    commuteAtr: detail.commuteAtr(),
                                    personalWageCode: detail.personalWageCode(),
                                    distributeWay: detail.distributeWay(),
                                    distributeSet: detail.distributeSet(),
                                    isErrorUseHigh: detail.isUseHighError(),
                                    errorRangeHigh: detail.errRangeHigh(),
                                    isErrorUserLow: detail.isUseLowError(),
                                    errorRangeLow: detail.errRangeLow(),
                                    isAlamUseHigh: detail.isUseHighAlam(),
                                    alamRangeHigh: detail.alamRangeHigh(),
                                    isAlamUseLow: detail.isUseLowAlam(),
                                    alamRangeLow: detail.alamRangeLow()});
                            } else if (!detail.added()) {
                                //Chỉ đưa vào mảng những itemCode đã đc lưu trước đó 
                                listItemCodeDeleted.push({categoryAtr: category.categoryAtr, itemCode: detail.itemCode()});
                            }
                            itemPosColumn++;
                        }
                    }
                }
                categoryPosition++;
            }
            var command = {
                layoutCommand: {
                    stmtCode: layout.stmtCode,
                    startYm: layout.startYm,
                    stmtName: layout.stmtName,
                    endYm: layout.endYm,
                    historyId: layout.historyId
                },
                categoryCommand: categoryCommand,
                lineCommand: lineCommand,
                detailCommand: detailCommand,
                listCategoryAtrDeleted: listCategoryAtrDeleted,
                listAutoLineIdDeleted: listAutoLineIdDeleted,
                listItemCodeDeleted: listItemCodeDeleted
            }
            nts.uk.request.ajax(paths.registerLayout, command)
                .done(function(res: Array<any>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        /**
           * Model namespace.
        */
        export module model {
            
            // layout head
            export class LayoutHeadDto {
                companyCode: string;
                stmtCode: string;
                stmtName: string;
                constructor() {
                }
            }
            
            // layout
            export class LayoutMasterDto {
                companyCode: string;
                stmtCode: string;
                startYm: number;
                stmtName: string;
                endYm: number;
                layoutAtr: number;
                historyId: string;
                constructor() {
                }
            }
                    
            export class Category {
                lines: KnockoutObservableArray<Line>;
                categoryAtr: number;
                categoryName: string;
                hasSetting: boolean = false;
                isRemoved: boolean = false;
                totalGrayLine: number = 0;
                
                constructor(lines: Array<Line>, categoryAtr: number) {
                    this.lines = ko.observableArray([]);
                    this.lines(_.map(lines, function(line: model.Line) {
                        var details = 
                            _.map(line.details, function(detail: model.ItemDetail){
                                return new model.ItemDetail(detail);  
                            });
                        return new model.Line(line.categoryAtr, details, line.autoLineId, line.lineDispayAtr, line.linePosition);
                    }));
                    this.categoryAtr = categoryAtr;
                    switch (categoryAtr){
                        case 0:
                            this.categoryName = "支給";
                            break;
                        case 1:
                            this.categoryName = "控除";
                            break;
                        case 2:
                            this.categoryName = "勤怠";
                            this.hasSetting = true;
                            break;
                        case 3:
                            this.categoryName = "記事";
                            this.hasSetting = true;
                            break;
                        default:
                            this.categoryName = "その他";
                            this.hasSetting = true;
                            break;        
                    }
                }
                categoryClick(data, event) {
                    var self = this;
                    nts.uk.ui.windows.sub.modal('/view/qmm/019/k/index.xhtml',{title: '明細レイアウトの作成＞カテゴリの設定'}).onClosed(() => {
                        var selectedCode = nts.uk.ui.windows.getShared('selectedCode');
                        if (selectedCode === "1") {
                            // cho phep print all row
                            for (let line of self.lines()){
                                line.setPrint(true);
                            }
                        } else if (selectedCode === "2") {
                            // Gray - Khong cho print all row
                            for (let line of self.lines()){
                                line.setPrint(false);
                            }                            
                        } else if (selectedCode === "3") {
                            // Xoa category
                            $("#group-" + data.categoryAtr).addClass("removed");
                            self.isRemoved = true;
                            if (data.categoryAtr === 2)
                                screenQmm019().notHasKintai(true);
                            if (data.categoryAtr === 3)
                                screenQmm019().notHasKiji(true);
                        }
                        screenQmm019().calculateLine();
                        return this;
                    });
                }
                addLine(){
                    var self = this;
                    //if (screenQmm019().totalNormalLineNumber() + screenQmm019().totalGrayLineNumber() === 10) {return this;}
                    nts.uk.ui.windows.setShared('totalNormalLineNumber', screenQmm019().totalNormalLineNumber());
                    nts.uk.ui.windows.setShared('totalGrayLineNumber', self.totalGrayLine);
                    
                    nts.uk.ui.windows.sub.modal('/view/qmm/019/i/index.xhtml',{title: '明細レイアウトの作成＞＋行追加'}).onClosed(() => {
                        var selectedCode = nts.uk.ui.windows.getShared('selectedCode');
                        if (selectedCode === undefined) return this;
                        
                        let autoLineId : string = "lineIdTemp-" + self.lines().length;
                        let listItemDetail: Array<ItemDetail> = new Array;
                        for(let i: number = 1; i <= 9; i++) {
                            listItemDetail.push(new ItemDetail(
                                {itemCode: "itemTemp-" + i, itemAbName: "+", isRequired: false, itemPosColumn: i,
                                categoryAtr: self.categoryAtr, autoLineId: autoLineId, sumScopeAtr: 0, 
                                setOffItemCode: "", commuteAtr: 0, calculationMethod: 0,
                                distributeSet: 0, distributeWay: 0, personalWageCode: "", isUseHighError: 0,
                                errRangeHigh: 0, isUseLowError: 0, errRangeLow: 0, isUseHighAlam: 0,
                                alamRangeHigh: 0, isUseLowAlam: 0, alamRangeLow: 0}            
                                ));
                        }
                        let line: Line = new Line(self.categoryAtr, listItemDetail, autoLineId, 1, self.lines.length);
                        if (selectedCode === "1") {
                            // cho phep print
                            line.setPrint(true);
                        } else if (selectedCode === "2") {
                            // Gray - Khong cho print
                            line.setPrint(false);                           
                        }
                        self.lines.push(line);
                        screenQmm019().calculateLine();
                        
                        screenQmm019().bindSortable();
                        screenQmm019().destroySortable();
                        screenQmm019().bindSortable();
    
                        return this;
                    });
                }
            }
        
            export class Line {
                categoryAtr: number;
                autoLineId: string;
                rowId: string;
                details: Array<ItemDetail>;
                lineDispayAtr: number;
                linePosition: number;
                isDisplayOnPrint: boolean;
                hasRequiredItem: boolean = false;
                isRemoved: boolean = false;
                
                constructor(categoryAtr: number, itemDetails: Array<ItemDetail>, 
                    autoLineId: string, lineDispayAtr: number, linePosition: number) {
                    this.details = itemDetails;
                    this.autoLineId = autoLineId;
                    this.rowId = categoryAtr + autoLineId;
                    this.lineDispayAtr = lineDispayAtr;
                    if(lineDispayAtr === 0) {
                        this.isDisplayOnPrint = false;
                    } 
                    else {
                        this.isDisplayOnPrint = true;
                    }
                    var checkRequired = _.find(itemDetails, function(findItem) {
                        return findItem.isRequired() === true;    
                    });
                    if(checkRequired !== undefined) {
                        this.hasRequiredItem = true;    
                    }
                    
                    this.linePosition = linePosition;
                    this.categoryAtr = categoryAtr;
                }
                lineClick(data: Line, event) {
                    var self = this;
                    nts.uk.ui.windows.sub.modal('/view/qmm/019/j/index.xhtml', {title: '明細レイアウトの作成＞行の設定'}).onClosed(() => {
                        var selectedCode = nts.uk.ui.windows.getShared('selectedCode');
                        if (selectedCode === "1") {
                            // cho phep print
                            self.setPrint(true);
                        } else if (selectedCode === "2") {
                            // Gray - Khong cho print
                            self.setPrint(false);                            
                        } else if (selectedCode === "3") {
                            // Xoa line
                            if (data.hasRequiredItem === false) {
                                $("#" + data.rowId).addClass("removed");
                                self.isRemoved = true;
                            }
                        }
                        screenQmm019().calculateLine();
                        return this;
                    });
                }
                setPrint(allowPrint: boolean) {
                    var self = this;
                    if (allowPrint === true) {
                        // cho phep print
                        $("#" + self.rowId).removeClass("ground-gray");
                        self.isDisplayOnPrint = true;
                        self.lineDispayAtr = 1;
                    } else {
                        // Gray - Khong cho print
                        $("#" + self.rowId).addClass("ground-gray");
                        self.isDisplayOnPrint = false;
                        self.lineDispayAtr = 0;                            
                    }    
                }
            }
        
            export class ItemDetail {
                itemCode: KnockoutObservable<string>;
                updateItemCode:  KnockoutObservable<string> = ko.observable("");
                added: KnockoutObservable<boolean> = ko.observable(false);
                itemAbName: KnockoutObservable<string>;
                isRequired: KnockoutObservable<boolean> = ko.observable(false);
                itemPosColumn: KnockoutObservable<number>;
                categoryAtr: KnockoutObservable<number>;
                autoLineId: KnockoutObservable<string>;
                sumScopeAtr: KnockoutObservable<number>;
                setOffItemCode: KnockoutObservable<string>;
                commuteAtr: KnockoutObservable<number>;
                calculationMethod: KnockoutObservable<number>;
                distributeSet: KnockoutObservable<number>;
                distributeWay: KnockoutObservable<number>;
                personalWageCode: KnockoutObservable<string>;
                isUseHighError: KnockoutObservable<number>;
                errRangeHigh: KnockoutObservable<number>;
                isUseLowError: KnockoutObservable<number>;
                errRangeLow: KnockoutObservable<number>;
                isUseHighAlam: KnockoutObservable<number>;
                alamRangeHigh: KnockoutObservable<number>;
                isUseLowAlam: KnockoutObservable<number>;
                alamRangeLow: KnockoutObservable<number>;
                isRemoved: boolean = false;
                //TODO-LamVT: bỏ rem khi đồng bộ sang develop
                //contextMenu : nts.uk.ui.contextmenu.ContextMenu;// context menu cho từng item
                contextMenuClassId : string = "";
                
                constructor(itemObject: any) {
                    var self = this;
                    self.itemCode = ko.observable(itemObject.itemCode);
                    self.itemAbName = ko.observable(itemObject.itemAbName);
                    if (itemObject.categoryAtr === 0 && 
                            (itemObject.itemCode === "F001" || itemObject.itemCode === "F002" || itemObject.itemCode === "F003")){
                            self.isRequired = ko.observable(true);
                    }
                    if (itemObject.categoryAtr === 1 && 
                            (itemObject.itemCode === "F114")){
                            self.isRequired = ko.observable(true);
                    }
                    self.itemPosColumn = ko.observable(itemObject.itemPosColumn);
                    self.categoryAtr = ko.observable(itemObject.categoryAtr);
                    self.autoLineId = ko.observable(itemObject.autoLineId);
                    self.sumScopeAtr = ko.observable(itemObject.sumScopeAtr);
                    self.setOffItemCode = ko.observable(itemObject.setOffItemCode);
                    self.commuteAtr = ko.observable(itemObject.commuteAtr);
                    self.calculationMethod = ko.observable(itemObject.calculationMethod);
                    self.distributeSet = ko.observable(itemObject.distributeSet);
                    self.distributeWay = ko.observable(itemObject.distributeWay);
                    self.personalWageCode = ko.observable(itemObject.personalWageCode);
                    self.isUseHighError = ko.observable(itemObject.isUseHighError);
                    self.errRangeHigh = ko.observable(itemObject.errRangeHigh);
                    self.isUseLowError = ko.observable(itemObject.isUseLowError);
                    self.errRangeLow = ko.observable(itemObject.errRangeLow);
                    self.isUseHighAlam = ko.observable(itemObject.isUseHighAlam);
                    self.alamRangeHigh = ko.observable(itemObject.alamRangeHigh);
                    self.isUseLowAlam = ko.observable(itemObject.isUseLowAlam);
                    self.alamRangeLow = ko.observable(itemObject.alamRangeLow);
                    
                    self.initContextMenu();
                }
                
                initContextMenu() {
                    var self = this;
                    self.contextMenuClassId = "context-menu-" + self.itemCode();
                    //Chỉ cho phép xóa những item khác dấu "+" và không phải là item required
                    if (!_.includes(self.contextMenuClassId, "itemTemp-") && !self.isRequired()) {
                        //Setup context menu for item:
                        //TODO-LamVT: bỏ rem khi đồng bộ sang develop
//                        self.contextMenu = new nts.uk.ui.contextmenu.ContextMenu("." + self.contextMenuClassId, [
//                            new nts.uk.ui.contextmenu.ContextMenuItem("delete", "削除", (ui) => {
//                                    self.setDelete(true);
//                                }, "", true),
//                            new nts.uk.ui.contextmenu.ContextMenuItem("undoDelete", "戻す", (ui) => {
//                                    self.setDelete(false);
//                                }, "", false)
//                        ]);
                    }    
                }
                setDelete(isDelete : boolean) {
                    var self = this;
                    self.isRemoved = isDelete;
                    //TODO-LamVT: bỏ rem khi đồng bộ sang develop
//                    self.contextMenu.setVisibleItem(!isDelete, "delete");
//                    self.contextMenu.setVisibleItem(isDelete, "undoDelete");
                    if (isDelete) {
                        $("#" + self.itemCode()).addClass("item-isDeleting");    
                    } else {
                        $("#" + self.itemCode()).removeClass("item-isDeleting");    
                    }                 
                }
                itemClick(data, event) {
                    var self = this;
                    // Nếu đang bị delete thì ko cho bật dialog detail
                    if (self.isRemoved) return this;
                    
                    var param = {
                        categoryId: data.categoryAtr(),
                        itemCode: data.itemCode(),
                        isUpdate: data.itemAbName() === "+" ? false : true,
                        startYm: screenQmm019().layoutMaster().startYm,
                        stmtCode: screenQmm019().layoutMaster().stmtCode,
                        isNotYetSave: data.added(),
                        objectNotYetSave: data
                    };    
                    nts.uk.ui.windows.setShared('param', param);
                    nts.uk.ui.windows.sub.modal('/view/qmm/019/f/index.xhtml',{title: '項目の選択・設定', width: 1200, height: 670}).onClosed(() => {
                        var itemResult: qmm019.f.service.model.ItemDetailModel = nts.uk.ui.windows.getShared('itemResult');
                        
                        if (itemResult === undefined) return this;
                        
                        if (data.itemAbName() === "+") {
                            // Them moi
                            self.itemCode(itemResult.itemCode);
                            self.added(true);
                            self.initContextMenu();
                        } else {
                            if (self.added()) {
                                // Sửa một detail đang được Thêm mới
                                self.itemCode(itemResult.itemCode);    
                            } else if (itemResult.itemCode !== self.itemCode()){
                                // Update
                                self.updateItemCode(itemResult.itemCode);    
                            }
                        }
                        
                        self.itemAbName(itemResult.itemAbName);
                        self.sumScopeAtr(itemResult.sumScopeAtr);
                        //self.setOffItemCode(itemResult.setOffItemCode);
                        self.commuteAtr(itemResult.commuteAtr);
                        self.calculationMethod(itemResult.calculationMethod);
                        self.distributeSet(itemResult.distributeSet);
                        self.distributeWay(itemResult.distributeWay);
                        self.personalWageCode(itemResult.personalWageCode);
                        self.isUseHighError(itemResult.isUseHighError ? 1 : 0);
                        self.errRangeHigh(itemResult.errRangeHigh === null ? 0 : itemResult.errRangeHigh);
                        self.isUseLowError(itemResult.isUseLowError ? 1 : 0);
                        self.errRangeLow(itemResult.errRangeLow === null ? 0 : itemResult.errRangeLow);
                        self.isUseHighAlam(itemResult.isUseHighAlam ? 1 : 0);
                        self.alamRangeHigh(itemResult.alamRangeHigh === null ? 0 : itemResult.alamRangeHigh);
                        self.isUseLowAlam(itemResult.isUseLowAlam ? 1 : 0);
                        self.alamRangeLow(itemResult.alamRangeLow === null ? 0 : itemResult.alamRangeLow);
                        
                        return this;
                    });
                }
            }
        }
    }

}



