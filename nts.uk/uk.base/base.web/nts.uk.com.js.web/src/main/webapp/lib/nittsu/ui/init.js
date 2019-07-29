/// <reference path="../reference.ts"/>
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            /** Event to notify document ready to initialize UI. */
            ui.documentReady = $.Callbacks();
            /** Event to notify ViewModel built to bind. */
            ui.viewModelBuilt = $.Callbacks();
            /** Event to notify ViewModel applied bindings. */
            ui.viewModelApplied = $.Callbacks();
            // Kiban ViewModel
            var KibanViewModel = (function () {
                function KibanViewModel(dialogOptions) {
                    var _this = this;
                    this.systemName = ko.observable("");
                    this.programName = ko.observable("");
                    this.title = ko.computed(function () {
                        //                let pgName = this.programName();
                        //                if (pgName === "" || pgName === undefined || pgName === null) {
                        return _this.systemName();
                        //                }
                        //                return this.programName() + " - " + this.systemName();
                    });
                    this.errorDialogViewModel = new nts.uk.ui.errors.ErrorsViewModel(dialogOptions);
                }
                return KibanViewModel;
            }());
            ui.KibanViewModel = KibanViewModel;
            var init;
            (function (init) {
                var _start;
                __viewContext.ready = function (callback) {
                    _start = callback;
                };
                __viewContext.bind = function (contentViewModel, dialogOptions) {
                    var kiban = new KibanViewModel(dialogOptions);
                    ui._viewModel = {
                        content: contentViewModel,
                        kiban: kiban,
                        errors: {
                            isEmpty: ko.computed(function () { return !kiban.errorDialogViewModel.occurs(); })
                        }
                    };
                    kiban.title.subscribe(function (newTitle) {
                        document.title = newTitle;
                    });
                    kiban.systemName(__viewContext.env.systemName);
                    ui.viewModelBuilt.fire(ui._viewModel);
                    ko.applyBindings(ui._viewModel);
                    ui.viewModelApplied.fire(ui._viewModel);
                    // off event reset for class reset-not-apply
                    $(".reset-not-apply").find(".reset-element").off("reset");
                    nts.uk.cookie.remove("startfrommenu", { path: "/" });
                    //avoid page content overlap header and function area
                    var content_height = 20;
                    if ($("#header").length != 0) {
                        content_height += $("#header").outerHeight(); //header height+ content area botton padding,top padding
                    }
                    if ($("#functions-area").length != 0) {
                        content_height += $("#functions-area").outerHeight(); //top function area height
                    }
                    if ($("#functions-area-bottom").length != 0) {
                        content_height += $("#functions-area-bottom").outerHeight(); //bottom function area height
                    }
                    $("#contents-area").css("height", "calc(100vh - " + content_height + "px)");
                    //            if($("#functions-area-bottom").length!=0){
                    //            } 
                };
                var startP = function () {
                    _.defer(function () {
                        if (cantCall()) {
                            loadEmployeeCodeConstraints().always(function () { return _start.call(__viewContext); });
                        }
                        else {
                            _start.call(__viewContext);
                        }
                    });
                    var onSamplePage = nts.uk.request.location.current.rawUrl.indexOf("/view/sample") >= 0;
                    // Menu
                    if (!onSamplePage) {
                        if ($(document).find("#header").length > 0) {
                            ui.menu.request();
                        }
                        else if (!uk.util.isInFrame() && !__viewContext.noHeader) {
                            var header = "<div id='header'><div id='menu-header'>"
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
                            ui.menu.request();
                        }
                    }
                };
                var noSessionWebScreens = [
                    "/view/sample/",
                    "/view/common/error/",
                    "/view/spr/index.xhtml",
                    "/view/ccg/007/",
                    "/view/kdw/003/a/index.xhtml",
                    "/view/ccg/033/index.xhtml"
                ];
                var cantCall = function () {
                    return !_.some(noSessionWebScreens, function (w) { return uk.request.location.current.rawUrl.indexOf(w) > -1; })
                        || uk.request.location.current.rawUrl.indexOf("/view/sample/component/editor/text-editor.xhtml") > -1;
                };
                var loadEmployeeCodeConstraints = function () {
                    var self = this, dfd = $.Deferred();
                    uk.request.ajax("com", "/bs/employee/setting/code/find").done(function (res) {
                        var formatOption = {
                            autofill: true
                        };
                        if (res.ceMethodAttr === 0) {
                            formatOption.filldirection = "left";
                            formatOption.fillcharacter = "0";
                        }
                        else if (res.ceMethodAttr === 1) {
                            formatOption.filldirection = "right";
                            formatOption.fillcharacter = "0";
                        }
                        else if (res.ceMethodAttr === 2) {
                            formatOption.filldirection = "left";
                            formatOption.fillcharacter = " ";
                        }
                        else {
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
                        }
                        else {
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
                    }).fail(function (res) {
                        dfd.reject();
                    });
                    return dfd.promise();
                };
                $(function () {
                    __viewContext.noHeader = (__viewContext.noHeader === true) || $("body").hasClass("no-header");
                    console.log("call");
                    ui.documentReady.fire();
                    __viewContext.transferred = uk.sessionStorage.getItem(uk.request.STORAGE_KEY_TRANSFER_DATA)
                        .map(function (v) { return JSON.parse(v); });
                    if ($(".html-loading").length <= 0) {
                        startP();
                        return;
                    }
                    var dfd = [];
                    _.forEach($(".html-loading"), function (e) {
                        var $container = $(e);
                        var dX = $.Deferred();
                        $container.load($container.attr("link"), function () {
                            dX.resolve();
                        });
                        dfd.push(dX);
                        dX.promise();
                    });
                    $.when.apply($, dfd).then(function (data, textStatus, jqXHR) {
                        $('.html-loading').contents().unwrap();
                        startP();
                    });
                });
                $(function () {
                    var lastPause = new Date();
                    $(window).keydown(function (e) {
                        if (e.keyCode !== 19)
                            return;
                        var now = new Date();
                        if (now - lastPause < 500) {
                            ui.dialog.version();
                        }
                        lastPause = new Date();
                    });
                });
            })(init || (init = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
