/// <reference path="../reference.ts"/>

module nts.uk.ui {
     
    import option = nts.uk.ui.option;
    export var _viewModel: any;
    
    /** Event to notify document ready to initialize UI. */
    export var documentReady = $.Callbacks();
    
    /** Event to notify ViewModel built to bind. */
    export var viewModelBuilt = $.Callbacks();

    
    // Kiban ViewModel
    export class KibanViewModel {
        title: KnockoutObservable<string>;
        errorDialogViewModel: errors.ErrorsViewModel;
        
        constructor(){
            this.title = ko.observable('');
            this.errorDialogViewModel = new nts.uk.ui.errors.ErrorsViewModel();
        }
    }
    
    module init {
        
        var _start: any;
        
        __viewContext.ready = function (callback: () => void) {
            _start = callback;
        };
        
        __viewContext.bind = function (contentViewModel: any) {
            var kiban = new KibanViewModel();
            
            _viewModel = {
                content: contentViewModel,
                kiban: kiban,
                errors: {
                    isEmpty: ko.computed(() => !kiban.errorDialogViewModel.occurs())
                }
            };
            
            kiban.title(__viewContext.title || 'THIS IS TITLE');
            
            viewModelBuilt.fire(_viewModel);
            
            ko.applyBindings(_viewModel);
            
            // off event reset for class reset-not-apply
            $(".reset-not-apply").find(".reset-element").off("reset");
        }
        
        $(function () {
            documentReady.fire();
            
            __viewContext.transferred = uk.sessionStorage.getItemAndRemove(uk.request.STORAGE_KEY_TRANSFER_DATA)
                .map(v => JSON.parse(v));
            
            _.defer(() => _start.call(__viewContext));
        });
    }
}