
var screenQmm019: KnockoutObservable<qmm019.a.ScreenModel>;

module qmm019.a {
    
    export class ScreenModel {
        //Khai bao bien
        itemList: KnockoutObservableArray<NodeTest>;
        itemListFull: Array<NodeTest> = [];
        itemListSearch: Array<NodeTest> = [];
        queueSearchResult: Array<NodeTest> = [];
        textSearch: string = "";
        singleSelectedCode: KnockoutObservable<string>;
        layouts: KnockoutObservableArray<service.model.LayoutMasterDto>;
        layoutsMax: KnockoutObservableArray<service.model.LayoutMasterDto>;
        layoutMaster: KnockoutObservable<service.model.LayoutMasterDto>;
        categories: KnockoutObservableArray<service.model.Category>;
        notHasKintai: KnockoutObservable<boolean> = ko.observable(false);
        notHasKiji: KnockoutObservable<boolean> = ko.observable(false);
        startYm: KnockoutObservable<string> = ko.observable("");
        endYm: KnockoutObservable<string> = ko.observable("");
        totalNormalLine: KnockoutObservable<string> = ko.observable("0行");
        totalNormalLineNumber: KnockoutObservable<number> = ko.observable(0);
        totalGrayLine: KnockoutObservable<string> = ko.observable("（+非表示0行）");
        totalGrayLineNumber: KnockoutObservable<number> = ko.observable(0);
        allowClick: KnockoutObservable<boolean> = ko.observable(true);
        firstLayoutCode: string = ""; //Dùng cho select item đầu tiên.
        previousItemPosition : number = 0; //Vị trí của item trước khi bị move giữa các row.
        
        constructor() {
            var self = this;
            screenQmm019 = ko.observable(self);
            self.itemList = ko.observableArray([]);
            self.singleSelectedCode = ko.observable(null);
            self.layouts = ko.observableArray([]);
            self.layoutsMax = ko.observableArray([]);
            self.layoutMaster = ko.observable(new service.model.LayoutMasterDto());
            self.categories = ko.observableArray([new service.model.Category([], 0)]);

            self.singleSelectedCode.subscribe(function(codeChanged) {
                var layoutFind = _.find(self.layouts(), function(layout) {
                    return layout.stmtCode === codeChanged.split(';')[0] && layout.startYm === parseInt(codeChanged.split(';')[1]);
                });
                if (layoutFind !== undefined){
                    service.getLayout(layoutFind.stmtCode, layoutFind.historyId).done(function(layout: service.model.LayoutMasterDto){
                        layoutFind.stmtName = layout.stmtName;
                        self.layoutMaster(layoutFind);  
                        self.startYm(nts.uk.time.formatYearMonth(self.layoutMaster().startYm));
                        self.endYm(nts.uk.time.formatYearMonth(self.layoutMaster().endYm));
                        service.getCategoryFull(layoutFind.stmtCode, layoutFind.startYm)
                            .done(function(listResult : Array<service.model.Category>){
                                self.categories(listResult);
                                self.calculateLine();
                                self.checkKintaiKiji();
                                self.bindSortable();
                        });
                    });
                }
            });
            
        }
        
        searchLayout() {
            var self = this;
            var textSearch: string = $("#A_INP_001").val().trim();
            
            if (textSearch.length === 0){
                nts.uk.ui.dialog.alert("コード/名称が入力されていません。");
            } else {
                if (self.textSearch !== textSearch) {
                    self.itemListSearch = _.filter(self.itemListFull, function(item) {
                        return _.includes(item.code, textSearch) || _.includes(item.name, textSearch);
                    });
                    self.queueSearchResult = [];
                    for (let item of self.itemListSearch) {
                        for (let child of item.childs) {
                            self.queueSearchResult.push(child);    
                        }
                    }
                    self.textSearch = textSearch;
                }
                if (self.itemListSearch.length === 0){
                    nts.uk.ui.dialog.alert("対象データがありません。");
                } else {
                    let firstResult: NodeTest = _.first(self.queueSearchResult);
                    self.singleSelectedCode(firstResult.code);
                    self.queueSearchResult.shift();
                    self.queueSearchResult.push(firstResult);
                }
            }
        }
        calculateLine() {
            var self = this;
            self.totalNormalLineNumber(0);
            self.totalGrayLineNumber(0);
            for (let category of self.categories()) {
                category.totalGrayLine = 0;
                for (let line of category.lines()){
                    if (line.isRemoved || category.isRemoved) continue;
                    if (!line.isDisplayOnPrint) {
                        self.totalGrayLineNumber(self.totalGrayLineNumber() + 1);
                        category.totalGrayLine += 1;
                    } else { 
                        self.totalNormalLineNumber(self.totalNormalLineNumber() + 1);
                    }    
                }
            }
            self.totalNormalLine(self.totalNormalLineNumber() + "行");
            self.totalGrayLine("（+非表示" + self.totalGrayLineNumber() + "行）");    
        }        
        checkKintaiKiji(){
            var self = this;
            var findKintai = _.find(self.categories(), function(category){
               return category.categoryAtr === 2; 
            });
            self.notHasKintai(findKintai === undefined);
            
            var findKiji = _.find(self.categories(), function(category){
               return category.categoryAtr === 3; 
            });
            self.notHasKiji(findKiji === undefined);    
        }
        bindSortable() {
            var self = this;
            
            _.forEach(self.categories(), function(category) {
                _.forEach(category.lines(), function(line) {
                    $("#" + line.rowId).sortable({
                        items: "span:not(.ui-state-disabled)",
                        connectWith: ".categoryAtr" + line.categoryAtr,
                        receive: function(event, ui){
                            if (ui.sender !== null) {
                                //Trả ra vị trí index của item vừa được move đến trong array span
                                let getItemIndex = $(this).find(".one-line").find("span").index(ui.item);
                                //get item ở sau item vừa được move đến (get by index) -> di chuyển nó đến vị trí cũ của item vừa bị move
                                let itemWillBeMoved = $(this).find(".one-line").find("span")[getItemIndex + 1];
                                if ($(itemWillBeMoved).hasClass("fixed-button") || itemWillBeMoved === undefined) {
                                    //nếu item sẽ dùng đề move bù lại thằng vừa kéo đi -> mà là dạng fixed-button
                                    //hoặc là undefined thì nhảy lên lấy thằng index trước đó
                                    itemWillBeMoved = $(this).find(".one-line").find("span")[getItemIndex - 1];
                                }
                                //thực hiện move item mới vào chỗ của thằng vừa kéo đi
                                if (screenQmm019().previousItemPosition === 8) {
                                    //Nếu vị trí để insert vào là số 8 thì dùng cái này ↓
                                    $(itemWillBeMoved).insertAfter($(ui.sender[0]).find(".one-line").find("span")[7]);
                                } else {
                                    $(itemWillBeMoved).insertBefore($(ui.sender[0]).find(".one-line").find("span")[screenQmm019().previousItemPosition]);  
                                }
                                
                                let currentCategoryId = $(this).parent().parent().attr("id");
                                let thisLineId = $(this).attr("id");
                                let senderLineId = $(ui.sender).attr("id");
                                let comeInItem : service.model.ItemDetail, returnItem : service.model.ItemDetail;
                                
                                let categoryFind = _.find(screenQmm019().categories(), function(categoryItem) {
                                    return categoryItem.categoryAtr === Number(currentCategoryId);
                                });
                                if (categoryFind !== undefined) {
                                    let lineFind = _.find(categoryFind.lines(), function(lineItem) {
                                        return lineItem.rowId === thisLineId;
                                    });
                                    let senderLineFind = _.find(categoryFind.lines(), function(lineItem) {
                                        return lineItem.rowId === senderLineId;
                                    });
                                    
                                    if (lineFind !== undefined) {
                                        returnItem = _.find(lineFind.details, function(item) {
                                            return item.itemCode() === $(itemWillBeMoved).attr("id");
                                        }); 
                                        //update autolineId mới
                                        returnItem.autoLineId(senderLineFind.autoLineId);
                                        
                                        _.remove(lineFind.details, function(item) {
                                            return item.itemCode() === $(itemWillBeMoved).attr("id");
                                        }); 
                                    }
                                    if (senderLineFind !== undefined) {
                                        comeInItem = _.find(senderLineFind.details, function(item) {
                                            return item.itemCode() === $(ui.item).attr("id");
                                        });
                                        //update autolineId mới 
                                        comeInItem.autoLineId(lineFind.autoLineId);
                                        
                                        _.remove(senderLineFind.details, function(item) {
                                            return item.itemCode() === $(ui.item).attr("id");
                                        }); 
                                    }
                                    let comeInItemFound = true, returnItemFound = true;
                                    //Tạo id mới cho comeInItem và returnItem
                                    for(let i: number = 1; i <= 9; i++) {
                                        //Tìm xem nếu id mới chưa có thì set
                                        if (_.includes(comeInItem.itemCode(), "itemTemp-") && comeInItemFound ) {
                                            let comeInItemFind = _.find(lineFind.details, function(item) {
                                                return item.itemCode() === "itemTemp-" + i;
                                            }); 
                                            if (comeInItemFind === undefined) {
                                                comeInItemFound = false;
                                                comeInItem.itemCode("itemTemp-" + i);
                                            }
                                        }
                                        if (_.includes(returnItem.itemCode(), "itemTemp-") && returnItemFound ) { 
                                            let returnItemFind = _.find(senderLineFind.details, function(item) {
                                                return item.itemCode() === "itemTemp-" + i;
                                            }); 
                                            if (returnItemFind === undefined) {
                                                returnItemFound = false;
                                                returnItem.itemCode("itemTemp-" + i);
                                            }
                                        }
                                        if (!comeInItemFound && !returnItemFound) {
                                            break;    
                                        }
                                    }
                                    
                                    //update datasource
                                    lineFind.details.push(comeInItem);
                                    senderLineFind.details.push(returnItem);
                                    
                                }
                                
                            }
                        },
                        start: function( event, ui ) {
                            self.previousItemPosition = $(this).find(".one-line").find("span").index(ui.item);
                        }
                    });
                })
            })
            
            //Setting sortable giữa các dòng với nhau
            $(".all-line").sortable({
                items: ".row"
            });
        }
        
        destroySortable() {
            $(".row").sortable("destroy");
            $(".all-line").sortable("destroy");    
        }
        
        // start function
        start(currentLayoutSelectedCode: string): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            service.getAllLayout().done(function(layouts: Array<service.model.LayoutMasterDto>) {
                if (layouts.length > 0) {
                
                    self.layouts(layouts);
                    service.getLayoutsWithMaxStartYm().done(function(layoutsMax: Array<service.model.LayoutMasterDto>) {
                        self.layoutsMax(layoutsMax);
                        self.buildTreeDataSource();
                        //let firstLayout: service.model.LayoutMasterDto = _.first(self.layouts());
                        if (currentLayoutSelectedCode === undefined) {
                            self.singleSelectedCode(self.firstLayoutCode);
                        } else {
                            self.singleSelectedCode(currentLayoutSelectedCode);
                        }
                        dfd.resolve();
                    });
                    
                } else {
                    self.allowClick(false);
                    dfd.resolve();    
                }
            }).fail(function(res) {
                // Alert message
                alert(res);
            });
            // Return.
            return dfd.promise();
        }

        buildTreeDataSource(): any {
            var self = this;
            var items = [];
            _.forEach(self.layoutsMax(), function(layoutMax) {
                var children = [];

                var childLayouts = _.filter(self.layouts(), function(layout) {
                    return layout.stmtCode === layoutMax.stmtCode;
                });
                _.forEach(childLayouts, function(child) {
                    if (self.firstLayoutCode === "") self.firstLayoutCode = child.stmtCode + ";" + child.startYm;
                    
                    children.push(new NodeTest(child.stmtCode + ";" + child.startYm, child.stmtName, [], 
                            nts.uk.time.formatYearMonth(child.startYm) + " ~ " + nts.uk.time.formatYearMonth(child.endYm)));
                });
                items.push(new NodeTest(layoutMax.stmtCode, layoutMax.stmtName, children, layoutMax.stmtCode + " " + layoutMax.stmtName));
            });
            self.itemList(items);
            self.itemListFull = items;
        }
        
        registerLayout() {
            var self = this;
            if (self.validateOnRegister()) {
                service.registerLayout(self.layoutMaster(), self.categories()).done(function (res) {
                    service.getCategoryFull(self.layoutMaster().stmtCode, self.layoutMaster().startYm)
                        .done(function(listResult : Array<service.model.Category>){
                            self.categories(listResult);
                            self.checkKintaiKiji();
                            self.bindSortable();
                    });
                }).fail(function(err){
                    alert(err);    
                });
            }
        }
        
        validateOnRegister() {
            let self = this;
            if (self.layoutMaster().stmtName.length === 0) {
                nts.uk.ui.dialog.alert("明細書名が入力されていません。");
                return false;
            }
            
            return true;
        }
        
        addKintaiCategory() {
            var self = this;
            let category: service.model.Category = new service.model.Category([], 2);
            self.categories.push(category);
            self.notHasKintai(false);
            self.bindSortable();
        }
        
        addKijiCategory() {
            var self = this;
            let category: service.model.Category = new service.model.Category([], 3);
            self.categories.push(category);
            self.notHasKiji(false);
            self.bindSortable();
        }
        
        openADialog() {
            var self = this;
            if(self.singleSelectedCode() == null)
                return false;
            var singleSelectedCode = self.singleSelectedCode().split(';');
            nts.uk.ui.windows.setShared('stmtCode', singleSelectedCode[0]);
            nts.uk.ui.windows.sub.modal('/view/qmm/019/d/index.xhtml', {title: '明細レイアウトの作成＞履歴追加'}).onClosed(function(): any {
                self.start(self.singleSelectedCode());
            });
        }
        openEDialog(){
            var self = this;
            if(self.singleSelectedCode() == null)
                return false;
            var singleSelectedCode = self.singleSelectedCode().split(';');
            if(singleSelectedCode[0] === undefined
                || singleSelectedCode[1] === undefined
                || self.layoutMaster().historyId === undefined)
                return false;
            nts.uk.ui.windows.setShared('stmtCode', singleSelectedCode[0]);
            nts.uk.ui.windows.setShared('startYm', singleSelectedCode[1]);
            nts.uk.ui.windows.setShared('historyId', self.layoutMaster().historyId);
            nts.uk.ui.windows.sub.modal('/view/qmm/019/e/index.xhtml', {title: '明細レイアウトの作成＞履歴の編集' }).onClosed(function(): any  {
                self.start(self.singleSelectedCode());
            });
        }
        openGDialog(){
            var self = this;
            nts.uk.ui.windows.sub.modal('/view/qmm/019/g/index.xhtml', {title: '明細レイアウトの作成＞新規登録'}).onClosed(function(): any  {
                self.start(undefined);
            });
        }
    }
    export class NodeTest {
        code: string;
        name: string;
        childs: Array<NodeTest>;
        nodeText: any;
        constructor(code: string, name: string, children: Array<NodeTest>, nodeText: string) {
            this.code = code;
            this.name = name;
            this.childs = children;
            this.nodeText = nodeText;
        }
    }

};
