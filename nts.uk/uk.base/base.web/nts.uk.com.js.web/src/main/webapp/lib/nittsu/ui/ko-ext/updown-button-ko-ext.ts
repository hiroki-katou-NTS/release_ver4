/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * UpDownButton binding handler
     */
    class NtsUpDownBindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: HTMLElement, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any,
            bindingContext: KnockoutBindingContext): void {
            var $upDown = $(element);

            if ($upDown.prop("tagName").toLowerCase() !== "div") {
                throw new Error('The element must be a div');
            }
            var data = valueAccessor();

            var elementId = $upDown.attr('id');
            var comId = ko.unwrap(data.comId);
            var childField = ko.unwrap(data.childDataKey);
            var primaryKey = ko.unwrap(data.primaryKey);
            var height = ko.unwrap(data.height);
            var targetType = ko.unwrap(data.type);
            var swapTarget = ko.unwrap(data.swapTarget);

            if (nts.uk.util.isNullOrUndefined(elementId)) {
                throw new Error('The element NtsSwapList must have id attribute.');
            }
            if (nts.uk.util.isNullOrUndefined(comId)) {
                throw new Error('The target element of NtsUpDown is required.');
            }

            $upDown.addClass("ntsComponent ntsUpDown").append("<div class='upDown-container'/>");
            $upDown.find(".upDown-container")
                .append("<button class = 'ntsUpButton ntsButton ntsUpDownButton auto-height' id= '" + elementId + "-up'/>")
                .append("<button class = 'ntsDownButton ntsButton ntsUpDownButton auto-height' id= '" + elementId + "-down'/>");

            var $target = $(comId);

            if (height !== undefined) {
                $upDown.height(height);
                $upDown.find(".upDown-container").height(height);
            } else {
                var targetHeight = $(comId + "_container").height();
                if (targetHeight === undefined) {
                    var h = _.find($(comId).attr("data-bind").split(","), function(attr) {
                        return attr.indexOf("height") >= 0;
                    });
                    if (h !== undefined) {
                        targetHeight = parseFloat(h.split(":")[1]);
                    }
                }
                $upDown.height(targetHeight);
                $upDown.find(".upDown-container").height(targetHeight);
            }

            var $up = $upDown.find(".ntsUpButton");
            var $down = $upDown.find(".ntsDownButton");

            $up.append("<i class='icon icon-button-arrow-top'/>");
            $down.append("<i class='icon icon-button-arrow-bottom'/>");

            var move = function(upDown, $targetElement) {
                var multySelectedRaw = $targetElement.igGrid("selectedRows");
                var singleSelectedRaw = $targetElement.igGrid("selectedRow");
                var selected = [];
                if (multySelectedRaw !== null) {
                    selected = _.filter(multySelectedRaw, function(item) {
                        return item["index"] >= 0;
                    });
                } else if (singleSelectedRaw !== null) {
                    selected.push(singleSelectedRaw);
                } else {
                    return;
                }

                var source = _.cloneDeep($targetElement.igGrid("option", "dataSource"));
                var group = 1;
                var grouped = { "group1": [] };
                if (selected.length > 0) {
                    var size = selected.length;
                    selected = _.sortBy(selected, "index");
                    _.forEach(selected, function(item, idx) {
                        grouped["group" + group].push(item);
                        if (idx !== size - 1 && item["index"] + 1 !== selected[idx + 1]["index"]) {
                            group++;
                            grouped["group" + group] = [];
                        }
                    });
                    var moved = false;
                    _.forEach(_.valuesIn(grouped), function(items: Array<any>) {
                        var firstIndex = items[0].index;
                        var lastIndex = items[items.length - 1].index;
                        if (upDown < 0) {
                            var canMove = firstIndex > 0;
                        } else {
                            var canMove = lastIndex < source.length - 1;
                        }
                        if (canMove) {
                            var removed = source.splice(firstIndex, items.length);
                            _.forEach(removed, function(item, idx) {
                                source.splice(firstIndex + upDown + idx, 0, item);
                            });
                            moved = true;
                        }
                    });
                    if (moved) {
                        $targetElement.igGrid("virtualScrollTo", 0);
                        data.targetSource(source);
                        //                        $targetElement.igGrid("option", "dataSource", source);
                        //                        $targetElement.igGrid("dataBind");
                        var index = upDown + grouped["group1"][0].index;
                        //                        var index = $targetElement.igGrid("selectedRows")[0].index;
                        $targetElement.igGrid("virtualScrollTo", index);
                    }
                }
            }

            var moveTree = function(upDown, $targetElement) {
                var multiSelectedRaw = $targetElement.igTreeGrid("selectedRows");
                var singleSelectedRaw = $targetElement.igTreeGrid("selectedRow");
                //                var targetSource = ko.unwrap(data.targetSource);
                var selected;
                if (multiSelectedRaw !== null) {
                    if (multiSelectedRaw.length !== 1) {
                        return;
                    }
                    selected = multiSelectedRaw[0];
                } else if (singleSelectedRaw !== null) {
                    selected.push(singleSelectedRaw);
                } else {
                    return;
                }

                if (selected["index"] < 0) {
                    return;
                }
                //                var targetSource = ko.unwrap(data.targetSource);
                var source = _.cloneDeep($targetElement.igTreeGrid("option", "dataSource"));

                var result = findChild(upDown, selected["id"], source, false, false);
                var moved = result.moved;
                var changed = result.changed;
                source = result.source;

                if (moved && changed) {
                    data.targetSource(source);
                    //                    $targetElement.igTreeGrid("option", "dataSource", source);
                    //                    $targetElement.igTreeGrid("dataBind");
                    //                    data.targetSource(source);
                    var index = $targetElement.igTreeGrid("selectedRows")[0].index;
                    if (index !== selected["index"]) {
                        var scrollTo = _.sumBy(_.filter($target.igTreeGrid("allRows"), function(row) {
                            return $(row).attr("data-row-idx") < index;
                        }), function(row) {
                            return $(row).height();
                        });
                        $targetElement.igTreeGrid("scrollContainer").scrollTop(scrollTo);
                    }
                }
            }

            var findChild = function(upDown, key, children, moved, changed) {
                var index = -1;
                if (children !== undefined && children !== null && children.length > 0 && !moved && !changed) {
                    _.forEach(children, function(child, idx: number) {
                        if (!moved) {
                            if (child[primaryKey] === key) {
                                index = idx;
                                return false;
                            } else {
                                var result = findChild(upDown, key, child[childField], moved, changed);
                                child[childField] = result.source;
                                moved = result.moved;
                                changed = result.changed;
                            }
                        } else {
                            return false;
                        }
                    });
                    if (index >= 0) {
                        if (upDown < 0) {
                            var canMove = index > 0;
                        } else {
                            var canMove = index < children.length - 1;
                        }
                        if (canMove) {
                            var removed = children.splice(index, 1);
                            children.splice(index + upDown, 0, removed[0]);
                            changed = true;
                        }
                        moved = true;
                    }
                    return {
                        source: children,
                        moved: moved,
                        changed: changed
                    };
                }
                return {
                    source: children,
                    moved: moved,
                    changed: changed
                };
            }

            $up.click(function(event, ui) {
                if (targetType === "tree") {
                    moveTree(-1, $target);
                } else if (targetType === "grid") {
                    move(-1, $target);
                } else if (targetType === "swap") {
                    var swapTargetGrid = swapTarget.toLocaleLowerCase() === "left" ? "-grid1" : "-grid2";
                    move(-1, $(comId + swapTargetGrid));
                }
            });

            $down.click(function(event, ui) {
                if (targetType === "tree") {
                    moveTree(1, $target);
                } else if (targetType === "grid") {
                    move(1, $target);
                } else if (targetType === "swap") {
                    var swapTargetGrid = swapTarget.toLocaleLowerCase() === "left" ? "-grid1" : "-grid2";
                    move(1, $(comId + swapTargetGrid));
                }
            });


        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any,
            bindingContext: KnockoutBindingContext): void {
            var $upDown = $(element);
            var elementId = $upDown.attr('id');
            if (nts.uk.util.isNullOrUndefined(elementId)) {
                throw new Error('the element NtsSwapList must have id attribute.');
            }
            var data = valueAccessor();
            var enable = ko.unwrap(data.enable);
            if(enable === false){
                $upDown.find(".ntsUpDownButton").prop('disabled', true);        
            } else {
                $upDown.find(".ntsUpDownButton").prop('disabled', false);    
            }
        }
    }

    ko.bindingHandlers['ntsUpDown'] = new NtsUpDownBindingHandler();
}