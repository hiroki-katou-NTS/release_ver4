var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_1) {
            var koExtentions;
            (function (koExtentions) {
                var NtsGridListBindingHandler = (function () {
                    function NtsGridListBindingHandler() {
                    }
                    NtsGridListBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var HEADER_HEIGHT = 27;
                        var $grid = $(element);
                        var gridId = $grid.attr('id');
                        if (nts.uk.util.isNullOrUndefined(gridId)) {
                            throw new Error('the element NtsGridList must have id attribute.');
                        }
                        var data = valueAccessor();
                        var optionsValue = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
                        var options = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
                        var deleteOptions = ko.unwrap(data.deleteOptions);
                        var observableColumns = ko.unwrap(data.columns);
                        var showNumbering = ko.unwrap(data.showNumbering) === true ? true : false;
                        var features = [];
                        features.push({ name: 'Selection', multipleSelection: data.multiple });
                        features.push({ name: 'Sorting', type: 'local' });
                        if (data.multiple) {
                            features.push({ name: 'RowSelectors', enableCheckBoxes: data.multiple, enableRowNumbering: showNumbering });
                        }
                        var gridFeatures = ko.unwrap(data.features);
                        var iggridColumns = _.map(observableColumns, function (c) {
                            c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                            c["dataType"] = 'string';
                            if (c["controlType"] === "switch") {
                                var switchF = _.find(gridFeatures, function (s) {
                                    return s["name"] === "Switch";
                                });
                                if (!uk.util.isNullOrUndefined(switchF)) {
                                    features.push({ name: 'Updating', enableAddRow: false, enableDeleteRow: false, editMode: 'none' });
                                    var switchOptions_1 = ko.unwrap(switchF.options);
                                    var switchValue_1 = switchF.optionsValue;
                                    var switchText_1 = switchF.optionsText;
                                    c["formatter"] = function createButton(val, row) {
                                        var result = $('<div class="ntsControl"/>');
                                        _.forEach(switchOptions_1, function (opt) {
                                            var value = opt[switchValue_1];
                                            var text = opt[switchText_1];
                                            var btn = $('<button>').text(text)
                                                .addClass('nts-switch-button');
                                            btn.attr('data-value', value);
                                            if (val == value) {
                                                btn.addClass('selected');
                                            }
                                            btn.appendTo(result);
                                        }, result.attr("data-value", val));
                                        return result[0].outerHTML;
                                    };
                                    $grid.on("click", ".nts-switch-button", function (evt, ui) {
                                        var $element = $(this);
                                        var selectedValue = $element.attr('data-value');
                                        var $parent = $element.parent();
                                        var currentSelect = $parent.attr('data-value');
                                        if (selectedValue !== currentSelect) {
                                            var $tr = $parent.closest("tr");
                                            var rowKey = $tr.attr("data-id");
                                            $parent.find("nts-switch-button").removeClass("selected");
                                            $element.addClass('selected');
                                            var $scroll = $("#" + gridId + "_scrollContainer");
                                            var currentPosition = $scroll[0].scrollTop;
                                            $grid.igGridUpdating("setCellValue", rowKey, c["key"], selectedValue);
                                            $grid.igGrid("commit");
                                            if ($grid.igGrid("hasVerticalScrollbar")) {
                                                $scroll[0].scrollTop = currentPosition;
                                            }
                                        }
                                    });
                                }
                            }
                            return c;
                        });
                        $grid.igGrid({
                            width: data.width,
                            height: (data.height) + "px",
                            primaryKey: optionsValue,
                            columns: iggridColumns,
                            virtualization: true,
                            virtualizationMode: 'continuous',
                            features: features
                        });
                        if (!uk.util.isNullOrUndefined(deleteOptions) && !uk.util.isNullOrUndefined(deleteOptions.deleteField)
                            && deleteOptions.visible === true) {
                            var sources = (data.dataSource !== undefined ? data.dataSource : data.options);
                            $grid.ntsGridList("setupDeleteButton", {
                                deleteField: deleteOptions.deleteField,
                                sourceTarget: sources
                            });
                        }
                        $grid.ntsGridList('setupSelecting');
                        $grid.bind('selectionchanged', function () {
                            if (data.multiple) {
                                var selected = $grid.ntsGridList('getSelected');
                                if (selected) {
                                    data.value(_.map(selected, function (s) { return s.id; }));
                                }
                                else {
                                    data.value([]);
                                }
                            }
                            else {
                                var selected = $grid.ntsGridList('getSelected');
                                if (selected) {
                                    data.value(selected.id);
                                }
                                else {
                                    data.value('');
                                }
                            }
                        });
                        var gridId = $grid.attr('id');
                        $grid.setupSearchScroll("igGrid", true);
                    };
                    NtsGridListBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var $grid = $(element);
                        var data = valueAccessor();
                        var optionsValue = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
                        var currentSource = $grid.igGrid('option', 'dataSource');
                        var sources = (data.dataSource !== undefined ? data.dataSource() : data.options());
                        if (!_.isEqual(currentSource, sources)) {
                            var currentSources = sources.slice();
                            var observableColumns = _.filter(ko.unwrap(data.columns), function (c) {
                                c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                                return c["isDateColumn"] !== undefined && c["isDateColumn"] !== null && c["isDateColumn"] === true;
                            });
                            _.forEach(currentSources, function (s) {
                                _.forEach(observableColumns, function (c) {
                                    var key = c["key"] === undefined ? c["prop"] : c["key"];
                                    s[key] = moment(s[key]).format(c["format"]);
                                });
                            });
                            $grid.igGrid('option', 'dataSource', currentSources);
                            $grid.igGrid("dataBind");
                        }
                        var currentSelectedItems = $grid.ntsGridList('getSelected');
                        var isEqual = _.isEqualWith(currentSelectedItems, data.value(), function (current, newVal) {
                            if ((current === undefined && newVal === undefined) || (current !== undefined && current.id === newVal)) {
                                return true;
                            }
                        });
                        if (!isEqual) {
                            $grid.ntsGridList('setSelected', data.value());
                        }
                        $grid.closest('.ui-iggrid').addClass('nts-gridlist').height(data.height);
                    };
                    return NtsGridListBindingHandler;
                }());
                ko.bindingHandlers['ntsGridList'] = new NtsGridListBindingHandler();
            })(koExtentions = ui_1.koExtentions || (ui_1.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
