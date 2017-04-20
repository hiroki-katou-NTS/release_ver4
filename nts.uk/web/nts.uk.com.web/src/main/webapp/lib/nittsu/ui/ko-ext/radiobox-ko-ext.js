/// <reference path="../../reference.ts"/>
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                /**
                 * RadioBoxGroup binding handler
                 */
                class NtsRadioBoxGroupBindingHandler {
                    constructor() {
                    }
                    /**
                     * Init
                     */
                    init(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        $(element).addClass("ntsControl");
                        let enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        $(element).data("enable", _.clone(enable));
                    }
                    /**
                     * Update
                     */
                    update(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        // Get data
                        var data = valueAccessor();
                        var options = data.options === undefined ? [] : JSON.parse(ko.toJSON(data.options));
                        var optionValue = ko.unwrap(data.optionsValue);
                        var optionText = ko.unwrap(data.optionsText);
                        var selectedValue = data.value;
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        // Container
                        var container = $(element);
                        var getOptionValue = (item) => {
                            return (optionValue === undefined) ? item : item[optionValue];
                        };
                        // Render
                        if (!_.isEqual(container.data("options"), options)) {
                            var radioName = uk.util.randomId();
                            container.empty();
                            _.forEach(options, (option) => {
                                var radioBoxLabel = $("<label class='ntsRadioBox'></label>");
                                var radioBox = $('<input type="radio">').attr("name", radioName).data("value", getOptionValue(option)).on("change", function () {
                                    var self = this;
                                    if ($(self).is(":checked"))
                                        selectedValue($(self).data("value"));
                                });
                                let disableOption = option["enable"];
                                if (!nts.uk.util.isNullOrUndefined(disableOption) && (disableOption === false)) {
                                    radioBox.attr("disabled", "disabled");
                                }
                                radioBox.appendTo(radioBoxLabel);
                                var box = $("<span class='box'></span>").appendTo(radioBoxLabel);
                                if (option[optionText] && option[optionText].length > 0)
                                    var label = $("<span class='label'></span>").text(option[optionText]).appendTo(radioBoxLabel);
                                radioBoxLabel.appendTo(container);
                            });
                            // Save a clone
                            container.data("options", _.cloneDeep(options));
                        }
                        // Checked
                        var checkedRadio = _.find(container.find("input[type='radio']"), (item) => {
                            return _.isEqual($(item).data("value"), selectedValue());
                        });
                        if (checkedRadio !== undefined)
                            $(checkedRadio).prop("checked", true);
                        // Enable
                        if (!_.isEqual(container.data("enable"), enable)) {
                            container.data("enable", _.clone(enable));
                            (enable === true) ? container.find("input[type='radio']").removeAttr("disabled") : container.find("input[type='radio']").attr("disabled", "disabled");
                            _.forEach(data.options(), (option) => {
                                if (typeof option["enable"] === "function") {
                                    option["enable"](enable);
                                }
                                else {
                                    option["enable"] = (enable);
                                }
                            });
                        }
                    }
                }
                ko.bindingHandlers['ntsRadioBoxGroup'] = new NtsRadioBoxGroupBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
