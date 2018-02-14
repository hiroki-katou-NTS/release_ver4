/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * Dialog binding handler
     */
    class NtsDialogBindingHandler implements KnockoutBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {

        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            var option: any = ko.unwrap(data.option);
            var title: string = ko.unwrap(data.title);
            var message: string = ko.unwrap(data.message);
            var modal: boolean = ko.unwrap(option.modal);
            var show: boolean = ko.unwrap(option.show);
            var buttons: any = ko.unwrap(option.buttons);

            var $dialog = $("<div id='ntsDialog'></div>");
            if (show == true) {
                $('body').append($dialog);
                // Create Buttons
                var dialogbuttons = [];
                for (let button of buttons) {
                    dialogbuttons.push({
                        text: ko.unwrap(button.text),
                        "class": ko.unwrap(button.class) + ko.unwrap(button.size) + " " + ko.unwrap(button.color),
                        click: function() { button.click(bindingContext.$data, $dialog) }
                    });
                }
                // Create dialog
                $dialog.dialog({
                    title: title,
                    modal: modal,
                    closeOnEscape: false,
                    buttons: dialogbuttons,
                    dialogClass: "no-close",
                    open: function() {
                        $(this).parent().find('.ui-dialog-buttonset > button.yes').focus();
                        $(this).parent().find('.ui-dialog-buttonset > button').removeClass('ui-button ui-corner-all ui-widget');
                        $('.ui-widget-overlay').last().css('z-index', 120000);
                    },
                    close: function(event) {
                        bindingContext.$data.option.show(false);
                    }
                }).text(message);
            }
            else {
                // Destroy dialog
                if ($('#ntsDialog').dialog("instance") != null)
                    $('#ntsDialog').dialog("destroy");
                $('#ntsDialog').remove();
            }
        }
    }

    /**
     * Error Dialog binding handler
     */
    class NtsErrorDialogBindingHandler implements KnockoutBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            var option: any = ko.unwrap(data.option);
            var title: string = ko.unwrap(data.title);
            var headers: Array<any> = ko.unwrap(option.headers);
            var modal: boolean = ko.unwrap(option.modal);
            var show: boolean = ko.unwrap(option.show);
            var buttons: any = ko.unwrap(option.buttons);

            var $dialog = $("<div id='ntsErrorDialog'></div>");

            parent.$('body').append($dialog);
            // Create Buttons
            var dialogbuttons = [];
            for (let button of buttons) {
                dialogbuttons.push({
                    text: ko.unwrap(button.text),
                    "class": ko.unwrap(button.class) + ko.unwrap(button.size) + " " + ko.unwrap(button.color),
                    click: function() { button.click(bindingContext.$data, $dialog) }
                });
            }
            // Calculate width
            var dialogWidth: number = 40 + 35 + 17;
            headers.forEach(function(header, index) {
                if (ko.unwrap(header.visible)) {
                    if (typeof ko.unwrap(header.width) === "number") {
                        dialogWidth += ko.unwrap(header.width);
                    } else {
                        dialogWidth += 200;
                    }

                }
            });
            // Create dialog
            $dialog.dialog({
                title: title,
                modal: modal,
                autoOpen:false,
                closeOnEscape: false,
                width: dialogWidth,
                maxHeight: 500,
                buttons: dialogbuttons,
                dialogClass: "no-close",
                open: function() {
                    $(this).parent().find('.ui-dialog-buttonset > button.yes').focus();
                    $(this).parent().find('.ui-dialog-buttonset > button').removeClass('ui-button ui-corner-all ui-widget');
                    $('.ui-widget-overlay').last().css('z-index', nts.uk.ui.dialog.getMaxZIndex());
                },
                close: function(event) {
                    bindingContext.$data.option().show(false);
                }
            }).dialogPositionControl();
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            var option: any = ko.unwrap(data.option);
            var title: string = ko.unwrap(data.title);
            var errors: Array<any> = ko.unwrap(data.errors);
            var headers: Array<any> = ko.unwrap(option.headers);
            var displayrows: number = ko.unwrap(option.displayrows);
            //var maxrows: number = ko.unwrap(option.maxrows);
            var autoclose: boolean = ko.unwrap(option.autoclose);
            var show: boolean = ko.unwrap(option.show);

            var $dialog = parent.$("#ntsErrorDialog");

            if (show == true) {
                
                // Create Error Table
                var $errorboard = $("<div id='error-board'></div>");
                var $errortable = $("<table></table>");
                // Header
                var $header = $("<thead></thead>");
                let $headerRow = $("<tr></tr>");
                $headerRow.append("<th style='display:none;'></th>");

                headers.forEach(function(header, index) {
                    if (ko.unwrap(header.visible)) {
                        let $headerElement = $("<th>" + ko.unwrap(header.text) + "</th>").width(ko.unwrap(header.width));
                        $headerRow.append($headerElement);
                    }
                });

                $header.append($headerRow);
                $errortable.append($header);

                // Body
                var $body = $("<tbody></tbody>");
                errors.forEach(function(error, index) {
                    // Row
                    let $row = $("<tr></tr>");
                    $row.click(function(){
                        error.$control.eq(0).exposeOnTabPanel().focus();    
                        let $dialogContainer = $dialog.closest("[role='dialog']");
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
                        
                        let currentControlOffset = error.$control.offset();
                        let top = additonalTop + currentControlOffset.top  + error.$control.outerHeight() - window.scrollY;
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
                    $row.append("<td style='display:none;'>" + (index + 1) + "</td>");
                    headers.forEach(function(header) {
                        if (ko.unwrap(header.visible))
                            if (error.hasOwnProperty(ko.unwrap(header.name))) {
                                // TD
                                let $column = $("<td>" + error[ko.unwrap(header.name)] + "</td>");

                                $row.append($column);
                            }
                    });
                    $body.append($row);
                });
                $errortable.append($body);
                $errorboard.append($errortable);
                // Errors over maxrows message
                var $message = $("<div></div>");
                $dialog.html("");
                $dialog.append($errorboard).append($message);

//                $dialog.on("dialogresizestop dialogopen", function() {
                $dialog.on("dialogopen", function() {
                    var maxrowsHeight = 0;
                    var index = 0;
                    $(this).find("table tbody tr").each(function() {
                        if (index < displayrows) {
                            index++;
                            maxrowsHeight += $(this).height();
                        }
                    });
                    maxrowsHeight = maxrowsHeight + 33 + 20 + 20 + 55 +4 + $(this).find("table thead").height();
                    if (maxrowsHeight > $dialog.dialog("option", "maxHeight")) {
                        maxrowsHeight = $dialog.dialog("option", "maxHeight");
                    }
                    $dialog.dialog("option", "height", maxrowsHeight);
                });
                
//                if($dialog.dialog("isOpen")){
                    $dialog.dialog("open");    
//                } else {
                    $dialog.closest("[role='dialog']").show();
//                }
            }
            else {
                $dialog.closest("[role='dialog']").hide();
//                $dialog.dialog("close");
            }
        }
    }

    ko.bindingHandlers['ntsDialog'] = new NtsDialogBindingHandler();
    ko.bindingHandlers['ntsErrorDialog'] = new NtsErrorDialogBindingHandler();
}