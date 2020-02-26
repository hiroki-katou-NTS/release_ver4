/// <reference path="../reference.ts"/>

module nts.uk.ui {
     
    import option = nts.uk.ui.option;
    export var _viewModel: any;
    
    /** Event to notify document ready to initialize UI. */
    export var documentReady = $.Callbacks();
    
    /** Event to notify ViewModel built to bind. */
    export var viewModelBuilt = $.Callbacks();
    
    /** Event to notify ViewModel applied bindings. */
    export var viewModelApplied = $.Callbacks();

    
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
//                let pgName = this.programName();
//                if (pgName === "" || pgName === undefined || pgName === null) {
                return this.systemName();
//                }
                
//                return this.programName() + " - " + this.systemName();
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
            
            ko.applyBindings(_viewModel);
            
            viewModelApplied.fire(_viewModel);
            
            // off event reset for class reset-not-apply
            $(".reset-not-apply").find(".reset-element").off("reset");
            nts.uk.cookie.remove("startfrommenu", {path: "/"});
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
            _.defer(() => {
                if (cantCall()) {
                    loadEmployeeCodeConstraints().always(() => _start.call(__viewContext));
                } else {
                    _start.call(__viewContext);
                }
            });
            
            let onSamplePage = nts.uk.request.location.current.rawUrl.indexOf("/view/sample") >= 0;
            
            // Menu
            if (!onSamplePage) {
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
        }
        
        const noSessionWebScreens = [
            "/view/sample/",
            "/view/common/error/",
            "/view/spr/index.xhtml",
            "/view/ccg/007/",
            "/view/kdw/003/a/index.xhtml",
            "/view/ccg/033/index.xhtml"
        ];
        
        let cantCall = function() {
            return !_.some(noSessionWebScreens, w => request.location.current.rawUrl.indexOf(w) > -1)
                || request.location.current.rawUrl.indexOf("/view/sample/component/editor/text-editor.xhtml") > -1;
        };        
        
        let getEmployeeSetting = function() {
            let dfd = $.Deferred(),
                es = nts.uk.sessionStorage.getItem("nts.uk.session.EMPLOYEE_SETTING");
            if (es.isPresent()) {
                dfd.resolve(JSON.parse(es.get()));
            } else {
                request.ajax("com", "/bs/employee/setting/code/find").done(constraints => {
                    nts.uk.sessionStorage.setItemAsJson("nts.uk.session.EMPLOYEE_SETTING", constraints);
                    dfd.resolve(constraints);
                });
            }

            return dfd.promise();
        };
        
        let loadEmployeeCodeConstraints = function() {
            let self = this,
                dfd = $.Deferred();
        
            getEmployeeSetting().done(res => {
//                let res = __viewContext.empCodeSets;
                let formatOption: any = {
                    autofill: true
                };
        
                if (res.ceMethodAttr === 0) {
                    formatOption.filldirection = "left";
                    formatOption.fillcharacter = "0";
                } else if (res.ceMethodAttr === 1) {
                    formatOption.filldirection = "right";
                    formatOption.fillcharacter = "0";
                } else if (res.ceMethodAttr === 2) {
                    formatOption.filldirection = "left";
                    formatOption.fillcharacter = " ";
                } else {
                    formatOption.filldirection = "right";
                    formatOption.fillcharacter = " ";
                }
                
                // if not have primitive, create new
                if (!__viewContext.primitiveValueConstraints) {
                    __viewContext.primitiveValueConstraints = {
                        EmployeeCode: {
                            valueType: "String",
                            charType: "AlphaNumeric",
                            maxLength: res.numberOfDigits,
                            formatOption: formatOption
                        }
                    };
                } else {
                    // extend primitive constraint
                    _.extend(__viewContext.primitiveValueConstraints, {
                        EmployeeCode: {
                            valueType: "String",
                            charType: "AlphaNumeric",
                            maxLength: res.numberOfDigits,
                            formatOption: formatOption
                        }
                    });
                }
        
                dfd.resolve();
            }).fail(res => {
                dfd.reject();
            });
        
            return dfd.promise();
        };
        
        $(function () {
            
            __viewContext.noHeader = (__viewContext.noHeader === true) || $("body").hasClass("no-header");
            
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


        $(function () {
            let lastPause: any = new Date();
            $(window).keydown(e => {
                if (e.keyCode !== 19) return;
                let now: any = new Date();
                if (now - lastPause < 500) {
                    ui.dialog.version();
                }
                lastPause = new Date();
            });
        });
    }
}
