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
        systemName: KnockoutObservable<string>;
        programName: KnockoutObservable<string>;
        title: KnockoutComputed<string>;
        errorDialogViewModel: errors.ErrorsViewModel;
        
        constructor(dialogOptions?: any){
            this.systemName = ko.observable("");
            this.programName = ko.observable("");
            this.title = ko.computed(() => {
                let pgName = this.programName();
                if (pgName === "" || pgName === undefined || pgName === null) {
                    return this.systemName();
                }
                
                return this.programName() + " - " + this.systemName();
            });
            this.errorDialogViewModel = new nts.uk.ui.errors.ErrorsViewModel(dialogOptions);
        }
    }
    
    module init {
        
        var _start: any;
        
        __viewContext.ready = function (callback: () => void) {
            _start = callback;
        };
        
        __viewContext.bind = function (contentViewModel: any, dialogOptions?: any) {
            var kiban = new KibanViewModel(dialogOptions);
            
            _viewModel = {
                content: contentViewModel,
                kiban: kiban,
                errors: {
                    isEmpty: ko.computed(() => !kiban.errorDialogViewModel.occurs())
                }
            };
            
            kiban.title.subscribe(newTitle => {
                document.title = newTitle;
            });
            
            kiban.systemName(__viewContext.env.systemName);
            
            viewModelBuilt.fire(_viewModel);
            
            if($(".html-loading").length > 0) {
                let dfd = [];
                _.forEach($(".html-loading"), function(e){
                    let $container = $(e);
                    let dX = $.Deferred(); 
                    $container.load($container.attr("link"), function(){
                        dX.resolve();
                    });
                    dfd.push(dX);
                    dX.promise();
                })
                $.when(...dfd).then(function( data, textStatus, jqXHR ) {
                    $('.html-loading').contents().unwrap();
                    binding(_viewModel);
                });    
            } else {
                binding(_viewModel);
            }
        }
        
        var binding = function(_viewModel: any){
            ko.applyBindings(_viewModel);
            
            // off event reset for class reset-not-apply
            $(".reset-not-apply").find(".reset-element").off("reset");
            
            //avoid page content overlap header and function area
            var content_height=20;
            if ($("#header").length != 0) {
                content_height += $("#header").outerHeight();//header height+ content area botton padding,top padding
            }
            if ($("#functions-area").length != 0) {
                content_height += $("#functions-area").outerHeight();//top function area height
            }
            if ($("#functions-area-bottom").length != 0) {
                content_height += $("#functions-area-bottom").outerHeight();//bottom function area height
            }
            $("#contents-area").css("height", "calc(100vh - " + content_height + "px)");
            //            if($("#functions-area-bottom").length!=0){
            //            }    
        }
        
        var startP = function(){
            _.defer(() => _start.call(__viewContext));
                
            // Menu
            if ($(document).find("#header").length > 0) {
                menu.request();
            } else if (!util.isInFrame() && !__viewContext.noHeader) {
                let header = "<div id='header'><div id='menu-header'>" 
                                + "<div id='logo-area' class='cf'>" 
                                + "<div id='logo'>勤次郎</div>"
                                + "<div id='user-info' class='cf'>"
                                + "<div id='company' class='cf' />"
                                + "<div id='user' class='cf' />"    
                                + "</div></div>"                            
                                + "<div id='nav-area' class='cf' />"
                                + "<div id='pg-area' class='cf' />"
                                + "</div></div>";
                $("#master-wrapper").prepend(header);
                menu.request();
            }    
        }
        
        $(function () {
            console.log("call");
            documentReady.fire();
            
            __viewContext.transferred = uk.sessionStorage.getItem(uk.request.STORAGE_KEY_TRANSFER_DATA)
                .map(v => JSON.parse(v));
            
            if($(".html-loading").length <= 0){
                startP();
                return;
            }
            let dfd = [];
            _.forEach($(".html-loading"), function(e){
                let $container = $(e);
                let dX = $.Deferred(); 
                $container.load($container.attr("link"), function(){
                    dX.resolve();
                });
                dfd.push(dX);
                dX.promise();
            })
            $.when(...dfd).then(function( data, textStatus, jqXHR ) {
                $('.html-loading').contents().unwrap();
                startP();
            });  
        });
    }
}
