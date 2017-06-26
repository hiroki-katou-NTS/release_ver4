module nts.uk.ui.wizard.viewmodel {
    
    export class ScreenModel {
        stepList: Array<NtsWizardStep>;
        stepSelected: KnockoutObservable<NtsWizardStep>;
        
        constructor() {
            var self = this;
            self.stepList = [
                {content: '.step-1'},
                {content: '.step-2'},
                {content: '.step-3'},
                {content: '.step-4'},
                {content: '.step-5'},
                {content: '.step-6'}
            ];
            self.stepSelected = ko.observable({content: '.step-1'});
        }
        
        begin() {
            $('#wizard').ntsWizard("begin");
        }
        end() {
            $('#wizard').ntsWizard("end");
        }
        next() {
            $('#wizard').ntsWizard("next");
        }
        previous() {
            $('#wizard').ntsWizard("prev");
        }
        getCurrentStep() {
            alert($('#wizard').ntsWizard("getCurrentStep"));
        }
        goto() {
            var index = this.stepList.indexOf(this.stepSelected());
            $('#wizard').ntsWizard("goto", index);
        }
    }
}
