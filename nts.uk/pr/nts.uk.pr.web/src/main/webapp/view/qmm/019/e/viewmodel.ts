module qmm019.e.viewmodel {
    
    export class ScreenModel {
        selectLayout: KnockoutObservable<service.model.LayoutMasterDto>;
        selectedLayoutAtr: KnockoutObservable<number>;
        selectLayoutCode: KnockoutObservable<string>;
        selectLayoutName: KnockoutObservable<string>;
        selectLayoutStartYm: KnockoutObservable<string>;
        selectLayoutEndYm: KnockoutObservable<string>;
        /**
         * Init screen model.
         */
        constructor() {
            var self = this;
            self.selectedLayoutAtr = ko.observable(null);
            self.selectLayoutCode = ko.observable(null);
            self.selectLayoutName = ko.observable(null);    
            self.selectLayoutStartYm = ko.observable(null);
            self.selectLayoutEndYm = ko.observable(null);
            self.selectLayout = ko.observable(null);
        }
        
         // start function
        start(): JQueryPromise<any> {
            var self = this;
             service.getLayout("01", 201606).done(function(layout){
                 self.selectLayout(layout);
                 self.startDiaglog();                 
                 
             }).fail(function(res){
                alert(res);    
             })
            
            var dfd = $.Deferred<any>();
            dfd.resolve();
            
            
            // Return.
            return dfd.promise();    
        }
        
        startDiaglog(): any{
            var self = this;
            var layout = self.selectLayout();
            var code = layout.stmtCode.trim();
            if(code.length < 2){
               code = "0" + code;
            }
            self.selectLayoutCode(code);
            self.selectLayoutName(layout.stmtName);
            self.selectLayoutStartYm(nts.uk.time.formatYearMonth(layout.startYm));
            self.selectLayoutEndYm(nts.uk.time.formatYearMonth(layout.endYm));
        }
        
        layoutProcess(): any{
            var self = this;
            //履歴の編集-削除処理
            if($("#layoutDetele").is(":checked")){
                self.dataDelete();
            }else{
                self.dataUpdate();    
            }
        }
        
        dataDelete():any{
            var self = this;
            service.deleteLayout(self.selectLayout()).done(function(){
                alert("履歴を削除する。");
            }).fail(function(res){
                alert(res);    
            })
        }
        
        dataUpdate(): any{
            var self = this;
            var layoutInfor = self.selectLayout();
            layoutInfor.startYm = $("#INP_001").val().replace('/','');
            service.updateLayout(layoutInfor).done(function(){
                alert("履歴を修正する。")    
            }).fail(function(res){
                alert(res);    
            })
        }
    }
}