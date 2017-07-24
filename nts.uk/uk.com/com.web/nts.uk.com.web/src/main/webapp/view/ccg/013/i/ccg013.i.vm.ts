 module nts.uk.com.view.ccg013.i.viewmodel {  
    export class ScreenModel {    
        textColor: KnockoutObservable<string>;
        bgColor: KnockoutObservable<string>;
        nameMenuBar: KnockoutObservable<string>;
        textOption:KnockoutObservable<nts.uk.ui.option.TextEditorOption>;
        
        constructor() {
            var self = this;
            
            self.textColor = ko.observable('');
            self.bgColor = ko.observable('');
            self.nameMenuBar = ko.observable('');
            self.textOption = ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                width: "160px"
            })); 
            
            //Get data and fill to popup
            var menuBar = nts.uk.ui.windows.getShared("CCG013I_MENU_BAR1");           
            if(menuBar != undefined){
                self.nameMenuBar(menuBar.menuBarName);
                self.textColor(menuBar.textColor);
                self.bgColor(menuBar.backgroundColor);                
            }
        }
        
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            dfd.resolve();   
            return dfd.promise();
        }
        
        /**
         * Pass data to main screen
         * Close the popup
         */
        submit() {
            var self = this;
            $(".ntsColorPicker_Container").trigger("validate");
             var hasError = nts.uk.ui.errors.hasError() || nts.uk.text.isNullOrEmpty(self.nameMenuBar().trim());
            if (hasError) {
                return;
            }
            
            //Set data
            var menuBar = {
                menuBarName: self.nameMenuBar(),
                textColor: self.textColor(),
                backgroundColor: self.bgColor()    
            }
            
            nts.uk.ui.windows.setShared("CCG013I_MENU_BAR", menuBar);            
            self.closeDialog();
        }
        
        /**
         * Click on button i1_9
         * Close the popup
         */
        closeDialog() {
            nts.uk.ui.windows.close();
        }
    }
 }