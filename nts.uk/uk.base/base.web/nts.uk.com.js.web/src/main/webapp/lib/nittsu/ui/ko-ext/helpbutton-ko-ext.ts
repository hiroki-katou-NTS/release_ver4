/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * HelpButton binding handler
     */
    class NtsHelpButtonBindingHandler implements KnockoutBindingHandler {
        
        constructor() { }
        
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext) {
            // Get data
            var data = valueAccessor();
            var image: string = ko.unwrap(data.image);
            var textId: string = ko.unwrap(data.textId);
            var textParams: string = ko.unwrap(data.textParams);
            var enable: boolean = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
            var position: string = ko.unwrap(data.position);
            
            var isImage = !util.isNullOrUndefined(image);

            //Position
            var myPositions: Array<string> = position.replace(/[^a-zA-Z ]/gmi, "").split(" ");
            var atPositions: Array<string> = position.split(" ");
            var operator: number = 1;
            var marginDirection: string = "";
            var caretDirection: string = "";
            var caretPosition: string = "";
            if (myPositions[0].search(/(top|left)/i) !== -1) {
                operator = -1;
            }
            if (myPositions[0].search(/(left|right)/i) === -1) {
                atPositions[0] = atPositions.splice(1, 1, atPositions[0])[0];
                myPositions[0] = myPositions.splice(1, 1, myPositions[0])[0];
                caretDirection = myPositions[1] = text.reverseDirection(myPositions[1]);
                caretPosition = "left";
                marginDirection = "margin-top";
            }
            else {
                caretDirection = myPositions[0] = text.reverseDirection(myPositions[0]);
                caretPosition = "top";
                marginDirection = "margin-left";
            }

            // Container
            $(element).on("click", function() {
                if ($popup.is(":visible")) {
                    $popup.hide();
                }
                else {
                    let CARET_WIDTH = parseFloat($caret.css("font-size")) * 2;
                    $popup.show()
                        .css(marginDirection, 0)
                        .position({
                            my: myPositions[0] + " " + myPositions[1],
                            at: atPositions[0] + " " + atPositions[1],
                            of: $(element),
                            collision: "none"
                        })
                        .css(marginDirection, CARET_WIDTH * operator);
                    $caret.css(caretPosition, parseFloat($popup.css(caretPosition)) * -1);
                }
            }).wrap($("<div class='ntsControl ntsHelpButton'></div>"));
            
            var $container = $(element).closest(".ntsHelpButton");
            var $content;
            if (isImage) {
                $content = $("<img src='" + request.resolvePath(image) + "' />");
            } else {
                $content = $("<span>").text(resource.getText(textId, textParams));
                $content.css('white-space', 'pre-line');
            }
            
            var $caret = $("<span class='caret-helpbutton caret-" + caretDirection + "'></span>");
            var $popup = $("<div class='nts-help-button-image'></div>")
                .append($caret)
                .append($content)
                .appendTo($container).hide();
            if (!isImage) {
                let CHARACTER_DEFAULT_WIDTH = 7;
                let DEFAULT_SPACE = 5;
                let textLengths = _.map($content.text().split(/\r\n/g), function(o) { return nts.uk.text.countHalf(o); });
                let WIDTH_SHOULD_NEED = CHARACTER_DEFAULT_WIDTH * _.max(textLengths) + DEFAULT_SPACE;
                $popup.width(WIDTH_SHOULD_NEED > 300 ? 300 : WIDTH_SHOULD_NEED);
            }
            // Click outside event
            $("html").on("click", function(event) {
                if (!$container.is(event.target) && $container.has(event.target).length === 0) {
                    $container.find(".nts-help-button-image").hide();
                }
            });
        }

        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data
            var data = valueAccessor();
            var enable: boolean = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;

            // Enable
            (enable === true) ? $(element).removeAttr("disabled") : $(element).attr("disabled", "disabled");

        }
    }
    
    ko.bindingHandlers['ntsHelpButton'] = new NtsHelpButtonBindingHandler();
}
