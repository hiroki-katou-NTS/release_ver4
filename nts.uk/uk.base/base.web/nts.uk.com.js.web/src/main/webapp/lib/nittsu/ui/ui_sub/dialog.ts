/// <reference path="../../reference.ts"/>

module nts.uk.ui {
    
    let PS: any = window.parent;

    /**
     * Dialog Module
     * Using for display info or confirm dialog
     */
    export module dialog {
        interface DialogHeader {
            icon?: string;
            text?: string;
        }
        interface Message {
            d: string;
            messageParams?: any[];
        }
        export function getMaxZIndex() {
            let overlayElements = PS.$(".ui-widget-overlay");
            var max = 12000;
            if (overlayElements.length > 0) {
                let zIndexs = _.map(overlayElements, function(element) { return parseInt($(element).css("z-index")); });
                var temp = _.max(zIndexs);
                max = temp > max ? temp : max;
            }
            return max;
        }
        function createNoticeDialog(message, buttons, header?: DialogHeader) {
            var $control = $('<div/>').addClass('control').addClass("pre");
            let text;
            if (typeof message === "object") {
                //business exception
                if (message.message) {
                    text = message.message;
                    if (message.messageId) {
                        $control.append(message.messageId);
                    }
                } else {
                    text = nts.uk.resource.getMessage(message.messageId, message.messageParams);
                    $control.append(message.messageId);
                }

            } else {
                text = message;
            }
            text = text.replace(/\n/g, '<br />');

            var $this = PS.$('<div/>').addClass('notice-dialog')
                .append($('<div/>').addClass('text').append(text))
                .append($control)
                .appendTo('body')
                .dialog({
                    dialogClass: "no-close-btn",
                    width: 'auto',
                    modal: true,
                    minWidth: 300,
                    maxWidth: 800,
                    maxHeight: 400,
                    closeOnEscape: false,
                    buttons: buttons,
                    open: function() {
                        $(this).closest('.ui-dialog').css('z-index', getMaxZIndex() + 2);
                        $('.ui-widget-overlay').last().css('z-index', getMaxZIndex() + 1);
                        
                        let $buttons = $(this).parent().find('.ui-dialog-buttonset > button')
                            .removeClass('ui-button ui-corner-all ui-widget');
                        
                        if ($buttons.filter(".proceed").length === 1) {
                            $buttons.filter(".proceed").focus();
                        } else if ($buttons.filter(".danger").length === 1) {
                            $buttons.not(".danger").focus();
                        } else {
                            $buttons.eq(0).focus();
                        }
                        

                        //add header icon if it has
                        if (header && header.icon) {
                            let $headerContainer = $("<div'></div>").addClass("ui-dialog-titlebar-container");
                            $headerContainer.append($("<img>").attr("src", header.icon).addClass("ui-dialog-titlebar-icon"));
                            $headerContainer.append($(this).parent().find(".ui-dialog-title"));
                            $(this).parent().children(".ui-dialog-titlebar").prepend($headerContainer);
                        }
                        
                        
                    },
                    close: function(event) {
                        PS.$(this).dialog('destroy');
                        PS.$(event.target).remove();
                    }
                });
            $this.dialogPositionControl();
            //add header text if it has
            if (header && header.text) {
                $this.dialog("option", "title", header.text);
            }
            return $this;
        }
        
        export function version() {
            let versinText = "AP version: ...";
            
            let $this = PS.$('<div/>').addClass('version-dialog')
                .append($('<div/>').addClass('text').append(versinText))
                .appendTo('body')
                .dialog({
                });
        }
        
        function simpleDialog(message: any, option: any) {
            var then = $.noop;
            var $dialog = PS.$('<div/>').hide();
            $(function() {
                $dialog.appendTo('body').dialog({
                    autoOpen: false
                });
            })

            setTimeout(function() {
                var $this = createNoticeDialog(
                    message,
                    [{
                        text: toBeResource.close,
                        "class": "large",
                        click: function() {
                            $this.dialog('close');
                            then();
                        }
                    }], { text: option.title });
            }, 0);

            return {
                then: function(callback) {
                    then = callback;
                }
            };
            
        }

        export function info(message) {
            return simpleDialog(message, { title: toBeResource.info });
        }
        
        export function caution(message) {
            return simpleDialog(message, { title: toBeResource.warn });
        }
        
        export function error(message) {
            return simpleDialog(message, { title: toBeResource.error });
        }
        
        export function alertError(message) {
            return error(message);
        }
        
        export function alert(message) {
            return error(message);
        };

        export function confirm(message: any, option?: any) {
            var handleYes = $.noop;
            var handleNo = $.noop;
            var handleCancel = $.noop;
            var handleThen = $.noop;
            var hasNoButton = true;
            var hasCancelButton = false;
            
            var option = option || {
                buttonStyles: { yes: "danger" }
            };

            var handlers = {
                ifYes: function(handler) {
                    handleYes = handler;
                    return handlers;
                },
                ifCancel: function(handler) {
                    hasNoButton = false;
                    hasCancelButton = true;
                    handleCancel = handler;
                    return handlers;
                },
                ifNo: function(handler) {
                    hasNoButton = true;
                    handleNo = handler;
                    return handlers;
                },
                then: function(handler) {
                    handleThen = handler;
                    return handlers;
                }
            };

            setTimeout(function() {

                var buttons = [];
                // yes button
                buttons.push({
                    text: toBeResource.yes,
                    "class": "yes large " + (option.buttonStyles.yes || ""),
                    click: function() {
                        $this.dialog('close');
                        handleYes();
                        handleThen();
                    }
                });
                // no button
                if (hasNoButton) {
                    buttons.push({
                        text: toBeResource.no,
                        "class": "no large " + (option.buttonStyles.no || ""),
                        click: function() {
                            $this.dialog('close');
                            handleNo();
                            handleThen();
                        }
                    });
                }
                // cancel button
                if (hasCancelButton) {
                    buttons.push({
                        text: toBeResource.cancel,
                        "class": "cancel large",
                        click: function() {
                            $this.dialog('close');
                            handleCancel();
                            handleThen();
                        }
                    });
                }

                var $this = createNoticeDialog(message, buttons, { text: toBeResource.confirm });
            });

            return handlers;
        };
        
        export function confirmDanger(message: any) {
            return confirm(message);
        }
        
        export function confirmProceed(message: any) {
            return confirm(message, { buttonStyles: { yes: "proceed" } });
        }
        
        function addError(errorBody: JQuery, error: any, idx: number){
            let row = $("<tr/>");
            row.append("<td style='display: none;'>" + idx + "/td><td>" + error["message"] + "</td><td>" + error["messageId"] + "</td>");
            let nameId = error["supplements"]["NameID"];   
            if (!util.isNullOrUndefined(nameId)) {
                row.click(function(evt, ui){
                    let element = $("body").find('[NameID="' + nameId + '"]');
                    let tab = element.closest("[role='tabpanel']");
                    while(!util.isNullOrEmpty(tab)){
                        let tabId = tab.attr("id");
                        tab.siblings(":first").children("li[aria-controls='" + tabId + "']").children("a").click();
                        tab = tab.parent().closest("[role='tabpanel']");
                    }
                    element.focus();
                    let $dialogContainer = errorBody.closest(".bundled-errors-alert").closest("[role='dialog']");
                    let $self = nts.uk.ui.windows.getSelf();
                    let additonalTop = 0;
                    let additonalLeft = 0;
                    if(!$self.isRoot) {
                        let $currentDialog = $self.$dialog.closest("[role='dialog']");
                        let $currentHeadBar = $currentDialog.find(".ui-dialog-titlebar");
                        let currentDialogOffset = $currentDialog.offset();
                        additonalTop = currentDialogOffset.top+ $currentHeadBar.height();
                        additonalLeft = currentDialogOffset.left;
                    }
                    
                    let currentControlOffset = element.offset();
                    let top = additonalTop + currentControlOffset.top  + element.outerHeight() - window.scrollY;
                    let left = additonalLeft + currentControlOffset.left - window.scrollX;
                    let $errorDialogOffset = $dialogContainer.offset();
                    let maxLeft = $errorDialogOffset.left + $dialogContainer.width();
                    let maxTop = $errorDialogOffset.top + $dialogContainer.height();
                    if($errorDialogOffset.top < top && top < maxTop){
                        $dialogContainer.css("top", top + 15);
                    }
                    if (($errorDialogOffset.left < left && left < maxLeft) ){
                        $dialogContainer.css("left", left);
                    }
                });    
            }
            row.appendTo(errorBody);  
        }
        
        function getRoot(): JQuery {
            let self = nts.uk.ui.windows.getSelf();
            while(!self.isRoot){
                self = self.parent; 
            }
            return $(self.globalContext.document).find("body");
        }
        
        export function bundledErrors(errors) {
            var then = $.noop;
            let id = util.randomId();
            let container = $("<div id='" + id + "' class='bundled-errors-alert'/>"), 
                functionArea = $("<div id='functions-area-bottom'/>"),
                errorBoard = $(`<div id='error-board'>    <table> <thead> <tr>    <th style='width: auto;'>`
                     + toBeResource.errorContent + `</th><th style='display: none;'/>    <th style='width: 150px;'>`
                     + toBeResource.errorCode + `</th>   </tr>   </thead>    <tbody/>    </table> </div>`),
                closeButton = $("<button class='ntsButton ntsClose large'/>");
            
            let errorBody = errorBoard.find("tbody");
            if($.isArray(errors["errors"])) {
                 _.forEach(errors["errors"], function(error, idx: number){ 
                    addError(errorBody, error, idx + 1);  
                 });   
            } else {
                return alertError(errors);
            }
                       
            closeButton.appendTo(functionArea);
            functionArea.appendTo(container);
            errorBoard.appendTo(container);
            container.appendTo(getRoot()); 
            
            setTimeout(function() {
                container.dialog({ 
                    title: toBeResource.errorList,   
                    dialogClass: "no-close-btn",
                    modal: false,
                    resizable: false,
                    width: 450,
                    maxHeight: 500,
                    closeOnEscape: false,
                    open: function() {
                        errorBoard.css({"overflow": "auto", "max-height" : "300px", "margin-bottom": "65px"});
                        functionArea.css({"left": "0px"});
                        closeButton.text(toBeResource.close).click(function(evt){
                            container.dialog("destroy");  
                            container.remove();
                            then();
                        });
                        
                        container.closest("div[role='dialog']").position({ my: "center", at: "center", of: window.parent });
                    },
                    close: function(event) {
                    }
                }).dialogPositionControl();
            }, 0);
            
            return {
                then: function(callback) {
                    then = callback;
                }
            };
        };
    }
    
}