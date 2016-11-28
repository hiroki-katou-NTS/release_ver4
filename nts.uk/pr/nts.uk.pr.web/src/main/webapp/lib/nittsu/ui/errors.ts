module nts.uk.ui.errors {
    
    export class ErrorsViewModel {
        title: string;
        errors: KnockoutObservableArray<ErrorListItem>;
        option: any;
        
        constructor() {
            this.title = "エラー一覧"
            this.errors = ko.observableArray([]);
            this.option = ko.mapping.fromJS(new option.ErrorDialogOption());
        }
        
        closeButtonClicked() {
        }
        
        open() {
            this.option.show(true);
        }
        
        hide() {
            this.option.show(false);
        }
        
        addError(error: ErrorListItem) {
            // TODO: ignore duplicated item
            this.errors.push(error);
        }
        
        removeErrorByElement($element: JQuery) {
            var removeds = _.filter(this.errors(), e => e.$control.is($element));
            this.errors.removeAll(removeds);
        }
    }
    
    export interface ErrorListItem {
        tab?: string;
        location: string;
        message: string;
        $control?: JQuery;
    }
    
    export class ErrorHeader {
        name: string;
        text: string;
        width: number;
        visible: boolean;
        
        constructor(name:string, text: string, width: number, visible: boolean) {
            this.name = name;
            this.text = text;
            this.width = width;
            this.visible = visible;
        }
    }
    
    function errorsViewModel(): ErrorsViewModel {
        return nts.uk.ui._viewModel.kiban.errorDialogViewModel;
    }
    
    export function show() {
        errorsViewModel().open();
    }
    
    export function hide() {
        errorsViewModel().hide();
    }
    
    export function add(error: ErrorListItem) {
        errorsViewModel().addError(error);
    }
    
    export function removeByElement($control: JQuery) {
        errorsViewModel().removeErrorByElement($control);
    }
}