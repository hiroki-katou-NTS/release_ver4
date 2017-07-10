 module nts.uk.com.view.ccg008.a.viewmodel {  
  import commonModel = ccg.model;
     export class ScreenModel {
        tabs: KnockoutObservableArray<any>;
        selectedTab: KnockoutObservable<string>;
        flowmenu: KnockoutObservable<model.Placement>;
        placements: KnockoutObservableArray<model.Placement>;
        visibleMyPage: KnockoutObservable<boolean>;
        dataSource: KnockoutObservable<model.LayoutAllDto>;
        displayButton: boolean;
        topPageCode: KnockoutObservable<string>; 
        constructor() {
            var self = this;
            self.topPageCode = ko.observable('');
            self.displayButton = true;
            self.dataSource = ko.observable(null);
            self.visibleMyPage = ko.observable(true);
            self.flowmenu = ko.observable(null);
            self.placements = ko.observableArray([]);
            self.tabs = ko.observableArray([
                {id: 'tab-1', title: nts.uk.resource.getText("CCG008_1"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true)},
                {id: 'tab-2', title: nts.uk.resource.getText("CCG008_4"), content: '.tab-content-2', enable: ko.observable(true), visible: self.visibleMyPage}
            ]);
            self.selectedTab = ko.observable(null);
            self.selectedTab.subscribe(function(codeChange){
                if(codeChange=='tab-1'){//display data top page
                    self.placements([]);
                    self.showToppage(self.dataSource().topPage);
                }
                if(codeChange=='tab-2'){//display data my page
                    self.placements([]);
                 self.showMypage(self.dataSource().myPage);   
                }
            });
        }
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            var transferData = __viewContext.transferred.value;
            var code = transferData && transferData.topPageCode ? transferData.topPageCode : "";
            self.topPageCode(code);
            service.getTopPageByCode( self.topPageCode()).done((data: model.LayoutAllDto) => {
                console.log(data);
                self.dataSource(data);
                if(data.topPage!=null && data.topPage.standardMenuUrl!=null){//hien thi standardmenu
                    location.href = data.topPage.standardMenuUrl;
                }
                if(data.checkMyPage == false ){//k hien thi my page
                    self.visibleMyPage(false);
                }
                if(data.check==true){//hien thi top page truoc
                    self.selectedTab('tab-1');
                }else{
                    self.selectedTab('tab-2');
                }
                if(data.checkMyPage == false && data.checkTopPage == false){
                    self.displayButton = false;    
                }
                dfd.resolve();
            });
            return dfd.promise();
        }
 
        //display top page
        showToppage(data: model.LayoutForTopPageDto){
            var self = this;
            if (data != null) {
                let listPlacement: Array<model.Placement> = _.map(data.placements, (item) => {
                     var html = '<iframe src="'+ item.placementPartDto.externalUrl +'"/>';
                    return new model.Placement(item.placementID, item.placementPartDto.name,
                        item.row, item.column,
                        item.placementPartDto.width, item.placementPartDto.height, item.placementPartDto.externalUrl,
                        item.placementPartDto.topPagePartID, item.placementPartDto.type,html);
                });
                if(data.flowMenu != null){
                    _.map(data.flowMenu, (items) => {
                        var html = '<img style="width:'+ ((items.widthSize * 150)-13) +'px;height:'+ ((items.heightSize * 150)-50) +'px" src="'+ nts.uk.request.liveView(items.fileID) +'"/>';
                        listPlacement.push( new model.Placement(items.fileID, items.fileName,
                        items.row, items.column,
                        items.widthSize, items.heightSize, null,
                        items.toppagePartID, 2,html));
                    });
                }
                listPlacement = _.orderBy(listPlacement, ['row', 'column'], ['asc', 'asc']);
                if (listPlacement !== undefined)
                    self.placements(listPlacement);
                _.defer(() => { self.setupPositionAndSizeAll('toppage'); });
            }
        }
        //display my page
        showMypage(data: model.LayoutForMyPageDto){
            var self = this;
            if (data != null) {
                let listPlacement: Array<model.Placement> = _.map(data.placements, (item) => {
                    var html = '<iframe src="'+ item.placementPartDto.externalUrl +'"/>';
                    return new model.Placement(item.placementID, item.placementPartDto.name,
                        item.row, item.column,
                        item.placementPartDto.width, item.placementPartDto.height, item.placementPartDto.externalUrl,
                        item.placementPartDto.topPagePartID, item.placementPartDto.type, html);
                });
                if(data.flowMenu != null){
                    _.map(data.flowMenu, (items) => {
                        var html = '<img style="width:'+ ((items.widthSize * 150)-13) +'px;height:'+ ((items.heightSize * 150)-50) +'px" src="'+ nts.uk.request.liveView(items.fileID) +'"/>';
                        listPlacement.push( new model.Placement(items.fileID, items.fileName,
                        items.row, items.column,
                        items.widthSize, items.heightSize, null,
                        items.toppagePartID, 2, html));
                    });
                }
                listPlacement = _.orderBy(listPlacement, ['row', 'column'], ['asc', 'asc']);
                if (listPlacement !== undefined)
                self.placements(listPlacement); 
                    self.placements(listPlacement);
                _.defer(() => { self.setupPositionAndSizeAll('mypage'); });
            }
        }
            
        //for setting dialog
        openDialogB(){
            var self = this;
            let dialogTitle = nts.uk.resource.getText("CCG008_2");
            nts.uk.ui.windows.setShared('checkTopPage', self.dataSource().checkTopPage, true);
            nts.uk.ui.windows.setShared('checkMyPage', self.dataSource().checkMyPage, true);
            if(self.dataSource().myPage==null){
                var transferData: commonModel.TransferLayoutInfo = {parentCode: '', layoutID: '', pgType: 2};
            }else{
                var transferData: commonModel.TransferLayoutInfo = {parentCode: self.dataSource().myPage.employeeID, layoutID: self.dataSource().myPage.layoutID, pgType: 2};
            }
            
            nts.uk.ui.windows.setShared('CCG008_layout', transferData);
            nts.uk.ui.windows.sub.modal("/view/ccg/008/b/index.xhtml", {title: dialogTitle});
        }
        /** Setup position and size for all Placements */
        private setupPositionAndSizeAll(name: string): void {
            var self = this;
            _.forEach(self.placements(), (placement, index) => {
                self.setupPositionAndSize(name, placement, index);
            });
        }
 
        /** Setup position and size for a Placement */
        private setupPositionAndSize(name: string, placement: model.Placement, index: number): void {
            var $placement = $("#" + name + "_" + placement.placementID + "_" + index);
            $placement.css({
                top: ((placement.row - 1) * 150) + ((placement.row - 1) * 10),
                left: ((placement.column - 1) * 150) + ((placement.column - 1) * 10),
                width: (placement.width * 150) + ((placement.width - 1) * 10),
                height: (placement.height * 150) + ((placement.height - 1) * 10)
            });
        }
    }  
     export module model {
         /** Client Placement class */
        export class Placement {
            // Required
            placementID: string;
            name: string;
            row: number;
            column: number;
            width: number;
            height: number;
            // External Url info
            isExternalUrl: boolean;
            url: string;
            // TopPagePart info
            topPagePartID: string;
            partType: number;
            html: string;
            constructor(placementID: string, name: string, row: number, column: number, width: number, height: number, url?: string, topPagePartID?: string, partType?: number, html: string) {
                // Non Agruments
                this.isExternalUrl = (nts.uk.util.isNullOrEmpty(url)) ? false : true;
                this.placementID = placementID;
                this.name = (this.isExternalUrl) ? "外部URL" : name;
                this.row = nts.uk.ntsNumber.getDecimal(row, 0);
                this.column = nts.uk.ntsNumber.getDecimal(column, 0);
                this.width = nts.uk.ntsNumber.getDecimal(width, 0);
                this.height = nts.uk.ntsNumber.getDecimal(height, 0);
                this.url = url;
                this.topPagePartID = topPagePartID;
                this.partType = partType;
                this.html = html;
            }
        }    
         /** Server LayoutDto */
        export interface LayoutDto {
            companyID: string;
            layoutID: string;
            pgType: number;
            placements: Array<PlacementDto>;
        }
         /** Server PlacementDto */
        export interface PlacementDto {
            companyID: string,
            placementID: string;
            layoutID: string;
            column: number;
            row: number;
            placementPartDto: PlacementPartDto;
        }
         /** Server PlacementPartDto */
        export interface PlacementPartDto {
            companyID: string;
            width: number;
            height: number;
            topPagePartID?: string;
            code?: string;
            name?: string;
            "type"?: number;
            externalUrl?: string;
        }
         export interface LayoutForMyPageDto{
            employeeID: string;
            layoutID: string;
            pgType: number;
            flowMenu: Array<FlowMenuPlusDto>;
            placements: Array<PlacementDto>;
         }
         export interface LayoutForTopPageDto{
            companyID: string;
            layoutID: string;
            pgType: number;
            flowMenu: Array<FlowMenuPlusDto>;
            placements: Array<PlacementDto>;
            standardMenuUrl: string;
         }
         export interface FlowMenuPlusDto{
            widthSize: number;
            heightSize: number;
            toppagePartID: string;
            fileID: string;
            fileName: string;
            fileType: string;
            mimeType: string;
            originalSize: number;
            storedAt: string;
            row: number;
            column: number;
         }
         export interface LayoutAllDto{
            /**my page*/
            myPage: LayoutForMyPageDto;
            /**top page*/
            topPage: LayoutForTopPageDto;
            /**check xem hien thi toppage hay mypage truoc*/
            check: boolean;//check = true (hien thi top page truoc)||check = false (hien thi my page truoc)
            /**check my page co duoc hien khong*/
            checkMyPage: boolean;
            //check top page co duoc setting khong
            checkTopPage: boolean;
        }
    }
}