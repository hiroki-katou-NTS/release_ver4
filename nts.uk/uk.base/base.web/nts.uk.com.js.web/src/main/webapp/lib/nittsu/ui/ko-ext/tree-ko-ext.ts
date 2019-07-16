/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * Tree binding handler
     */
    class NtsTreeDragAndDropBindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: HTMLElement, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let ROW_HEIGHT = 30;
            let HEADER_HEIGHT = 24;
            // Get data. 
            let data = valueAccessor();
            let options: Array<any> = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
            let optionsValue = ko.unwrap(data.primaryKey !== undefined ? data.primaryKey : data.optionsValue);
            let optionsText = ko.unwrap(data.primaryText !== undefined ? data.primaryText : data.optionsText);
            let optionsChild = ko.unwrap(data.childDataKey !== undefined ? data.childDataKey : data.optionsChild);
            
            let multiple = data.multiple != undefined ? ko.unwrap(data.multiple) : false;
            let selectedValues: Array<any> = ko.unwrap(data.selectedValues);
            let singleValue = ko.unwrap(data.value);
            let rows = ko.unwrap(data.rows);
            
            let selectOnParent = data.selectOnParent != undefined ? ko.unwrap(data.selectOnParent) : false;
            let allowOtherTreeNode = data.receiveOtherTreeNode !== undefined ? ko.unwrap(data.receiveOtherTreeNode) : false;
            let enable = data.enable !== undefined ? ko.unwrap(data.enable) : true;

            let height = ko.unwrap(data.height !== undefined ? data.height : 0); 
            let width = ko.unwrap(data.width !== undefined ? data.width : 0);
            
            let maxDeepLeaf = ko.unwrap(data.maxDeepLeaf !== undefined ? data.maxDeepLeaf : 10);
            let maxChildInNode = ko.unwrap(data.maxChildInNode !== undefined ? data.maxChildInNode : 999);
            let customValidate: Function = data.customValidate;
            
            if (!nts.uk.util.isNullOrEmpty(rows)) {
                height = rows* ROW_HEIGHT;    
            }
            $(element).addClass("ig-tree-background");
            
            let $tree = $(element);
            
//            let template = "{{if ${"+optionsValue+"}.indexOf('1') >= 0}} <img src='http://igniteui.com/images/samples/tree/book.png'>" + 
//                " {{elseif ${"+optionsValue+"}.indexOf('2') >= 0}}<img src='http://igniteui.com/images/samples/tree/coins.png'>" +
//                "{{else}}<img src='http://igniteui.com/images/samples/tree/documents-folder.png'/>{{/if}}${"+optionsValue+"}"
            
            // Init ig grid.
            $tree.igTree({
                width: width,
                height: height,
                dataSource: _.cloneDeep(options),
                initialExpandDepth: 0, 
                tabIndex: -1,
                checkboxMode : !multiple ? "off" : selectOnParent ? "triState" : "biState",
                singleBranchExpand: false,
                pathSeparator: '_',
                bindings: {
                    textKey: optionsText,
                    valueKey: optionsValue,
                    childDataProperty: optionsChild
//                    ,nodeContentTemplate: template
                },
                dragAndDrop: true,
                dragAndDropSettings: {
                    allowDrop: allowOtherTreeNode,
                    customDropValidation: function (element) {
                        let dragInfor = $tree.data("dragInfor");
                        let mousePosition = $tree.data("mousePosition");
                        // Validates the drop target
                        let droppableNode = $(this);
                        if(droppableNode.prop("tagName").toLowerCase() !== "li"){
                            droppableNode = droppableNode.closest("li");    
                        }
                        
                        let isOutTarget = mousePosition.top < droppableNode.offset().top 
                            || mousePosition.top > droppableNode.offset().top + droppableNode.height();
                        
                        let dragParent = $tree.igTree("parentNode", element);
                        let targetParent: any = $tree.igTree("parentNode", droppableNode);
                        
                        let targetNode: any = $tree.igTree("nodeFromElement", droppableNode);
                        if(!isOutTarget){
                            if(!nts.uk.util.isNullOrEmpty(targetNode.path)){
                                let targetDeep: number = (targetNode.path.match(/_/g) || []).length;
                                if(targetDeep + 1 >= maxDeepLeaf){
                                    return false;        
                                }     
                            } else {
                                return false;    
                            }
                            
                            let targetNodeChildren = $tree.igTree("children", droppableNode);
                            if(targetNodeChildren.length >= maxChildInNode){
                                return false        
                            }          
                        } else if (targetParent !== null && !targetParent.is(dragParent)){
                            targetNode = $tree.igTree("nodeFromElement", targetParent);
                            if(!nts.uk.util.isNullOrEmpty(targetNode.path)){
                                let targetDeep: number = (targetNode.path.match(/_/g) || []).length;
                                if(targetDeep + 1 >= maxDeepLeaf){
                                    return false;        
                                }     
                            } else {
                                return false;    
                            }
                            
                            let targetNodeChildren = $tree.igTree("children", targetParent);
                            if(targetNodeChildren.length >= maxChildInNode){
                                return false        
                            }     
                        } 
                        
                        let customValidateResult = nts.uk.util.isNullOrUndefined(customValidate) ? true: customValidate();
                        
                        if(customValidateResult === false){
                            return false;        
                        }
                        
                        return true;
                    }
                },
                dragStart: function(evt, ui) {
                    $tree.data("dragInfor", {
                        helper:  ui.helper,  
                        targetNodePath: ui.path,
                        mousePosition: ui.position
                    });
                },
                selectionChanged: function(evt, ui) {
                    if (ko.unwrap(data.multiple)) {
                        if (ko.isObservable(data.selectedValues)) {
                            let selectedNodes = $tree.igTree("checkedNodes");
                            let checkedNodes = _.map(selectedNodes, function(s){
                                return s.data[optionsValue];    
                            });
                            if(ui.selectedNodes.length > 0){
                                checkedNodes.push(ui.selectedNodes[0].data[optionsValue]);            
                            }
                            data.selectedValues(_.uniq(checkedNodes));
                        }
                    } else {
                        if (ko.isObservable(data.value)) {
                            var selectedRows: Array<any> = ui.selectedNodes;
                            data.value(selectedRows.length <= 0 ? undefined : selectedRows[0].data[optionsValue]);
                        }
                    }
                },
                nodeCheckstateChanged: function(evt, ui) {
                    if (ko.isObservable(data.selectedValues)) {
                        if (ko.isObservable(data.selectedValues)) { 
                            let selectedNodes = $tree.igTree("checkedNodes");
                            data.selectedValues(_.map(selectedNodes, function(s){
                                return s.data[optionsValue];    
                            }));
                        }
                    }
                }
            });
            $tree.mousemove(function( event ) {
                var pageCoords = {top: event.pageY, left: event.pageX};
                $tree.data("mousePosition", pageCoords );
            });
            $tree.setupSearchScroll("igTree");
            
            $tree.bind("sourcechanging", function(evt){
                let source = $tree.igTree("option", "dataSource").__ds;
                if(_.isNil(data.dataSource)){
                    data.options(source);
                } else {
                    data.dataSource(source);    
                }
            });
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            let data = valueAccessor();
            let options: Array<any> = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
            let selectedValues: Array<any> = ko.unwrap(data.selectedValues);
            let singleValue = ko.unwrap(data.value);
            let $tree = $(element);
            let multiple = data.multiple != undefined ? ko.unwrap(data.multiple) : false;
            
            // Update datasource.
            let originalSource = $tree.igTree('option', 'dataSource').__ds;
            if (!_.isEqual(originalSource, options)) {
                $tree.igTree("option", "dataSource", _.cloneDeep(options));
                $tree.igTree("dataBind");
            }

            // Clear selection.
            if (nts.uk.util.isNullOrEmpty(selectedValues) && nts.uk.util.isNullOrUndefined(singleValue)) {
                $tree.ntsTreeDrag("deselectAll");
                $tree.find("a").removeClass("ui-state-active");
            } else {
                let getOffset = function($node){
                    var offset = 0, siblings = $node.prevAll(), parent = $node;
//                    var offset = $node[0].offsetTop, parent = $node[0].offsetParent;
                    while (true) {   
                        siblings.each(function(idx, el) {
                            offset += $(el).height();
                        });
                        parent = $tree.igTree("parentNode", parent);
                        if(_.isNil(parent)){
                            return offset;
                        }
                        siblings = parent.prevAll();
                    }   
                }
                if (multiple) {
                    $tree.find("a").removeClass("ui-state-active");
                    selectedValues.forEach(function(val) {
                        let $node = $tree.igTree("nodesByValue", val);
                        if($node.length > 0){
                            $node.find("a:first").addClass("ui-state-active");
                            let $checkbox = $node.find("span[data-role=checkbox]:first").find(".ui-icon-check");
                            if($node.length > 0 && $tree.igTree("checkState", $node) === "off"){
                                $tree.igTree("toggleCheckstate", $node);
                            }  
                            $tree.igTree("expandToNode", $node);
                        }
                    });
                    if (selectedValues.length > 0) {
                        var lastV = $tree.data("values");
                        if(!_.isNil(lastV)) {
                            var newV = _.difference(selectedValues, lastV),
                                scrollTo = newV.length === 0 ? selectedValues[0] : newV[0],
                                $selectingNode = $tree.igTree("nodesByValue", scrollTo);
                            
                            if ($selectingNode.length > 0) {
                                $tree[0].scrollTop  = (getOffset($selectingNode));
                            }
                        }
                        $tree.data("values", selectedValues);
                        
                    }
                } else {
                    $tree.igTree("clearSelection");
                    var $selectingNode = $tree.igTree("nodesByValue", singleValue);
                    if ($selectingNode.length > 0) {
                        $tree.igTree("select", $selectingNode);
                        $tree.igTree("expandToNode", $selectingNode);
                        $tree[0].scrollTop  = getOffset($selectingNode);
                    }
                }
            }
        }
    }
    
    ko.bindingHandlers['ntsTreeDragAndDrop'] = new NtsTreeDragAndDropBindingHandler();
}
