/*!@license
* Infragistics.Web.ClientUI infragistics.chart_radialchart.js 16.1.20161.2145
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
* Depends:
*     jquery-1.4.4.js
*     jquery.ui.core.js
*     jquery.ui.widget.js
*     infragistics.util.js
*/

(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["AbstractEnumerable:a", 
"Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Func$1:ba", 
"MulticastDelegate:bb", 
"IntPtr:bc", 
"AbstractEnumerator:bd", 
"Array:bo", 
"GenericEnumerable$1:cj", 
"GenericEnumerator$1:ck"]);


} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"Func$2:bf", 
"MulticastDelegate:bg", 
"IntPtr:bh", 
"Delegate:bj", 
"Interlocked:bk", 
"Script:cc", 
"Action$1:ch", 
"EventArgs:cl", 
"UIElement:cm", 
"DependencyObject:cn", 
"Dictionary:co", 
"DependencyProperty:cp", 
"PropertyMetadata:cq", 
"PropertyChangedCallback:cr", 
"DependencyPropertyChangedEventArgs:cs", 
"DependencyPropertiesCollection:ct", 
"UnsetValue:cu", 
"Binding:cv", 
"PropertyPath:cw", 
"Transform:cx", 
"List$1:cz", 
"IList$1:c0", 
"ICollection$1:c1", 
"IArray:c2", 
"IArrayList:c3", 
"Array:c4", 
"CompareCallback:c5", 
"Func$3:c6", 
"Comparer$1:c7", 
"IComparer:c8", 
"IComparer$1:c9", 
"DefaultComparer$1:da", 
"Comparison$1:db", 
"ReadOnlyCollection$1:dc", 
"Predicate$1:dd", 
"NotImplementedException:de", 
"NotifyCollectionChangedEventArgs:d1", 
"NotifyCollectionChangedAction:d2", 
"Dictionary$2:d8", 
"IDictionary$2:d9", 
"IDictionary:ea", 
"KeyValuePair$2:eb", 
"Enumerable:ec", 
"Thread:ed", 
"ThreadStart:ee", 
"IOrderedEnumerable$1:ef", 
"SortedList$1:eg", 
"ArgumentNullException:eh", 
"IEqualityComparer$1:ei", 
"EqualityComparer$1:ej", 
"IEqualityComparer:ek", 
"DefaultEqualityComparer$1:el", 
"InvalidOperationException:em", 
"ArgumentException:en", 
"FrameworkElement:eu", 
"Visibility:ev", 
"Style:ew", 
"Geometry:e1", 
"GeometryType:e2", 
"DataTemplateRenderInfo:e7", 
"DataTemplatePassInfo:e8", 
"ContentControl:e9", 
"Control:fa", 
"Thickness:fb", 
"HorizontalAlignment:fc", 
"VerticalAlignment:fd", 
"DataTemplate:fe", 
"DataTemplateRenderHandler:ff", 
"DataTemplateMeasureHandler:fg", 
"DataTemplateMeasureInfo:fh", 
"DataTemplatePassHandler:fi", 
"GeometryGroup:fx", 
"GeometryCollection:fy", 
"FillRule:fz", 
"PathGeometry:f0", 
"PathFigureCollection:f1", 
"PathFigure:f9", 
"PathSegmentCollection:ga", 
"INotifyPropertyChanged:gn", 
"PropertyChangedEventHandler:go", 
"PropertyChangedEventArgs:gp", 
"ObservableCollection$1:hb", 
"INotifyCollectionChanged:hc", 
"NotifyCollectionChangedEventHandler:hd", 
"Func$1:ib", 
"Extensions:jf", 
"Panel:jg", 
"UIElementCollection:jh", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


$.ig.util.defType('Extensions', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	reset1: function (geom) {
		geom.figures().clear();
		geom.figures().add(new $.ig.PathFigure());
		geom.figures().removeAt(0);
	}
	,
	reset: function (geom) {
		geom.children().clear();
		geom.children().add(new $.ig.PathGeometry());
		geom.children().removeAt(0);
	}
	,
	detach: function (child) {
		if (child == null) {
			return;
		}
		var parent = $.ig.util.cast($.ig.Panel.prototype.$type, child.parent());
		if (parent != null) {
			parent.children().remove(child);
			return;
		}
		var cont = $.ig.util.cast($.ig.ContentControl.prototype.$type, child.parent());
		if (cont != null) {
			cont.content(null);
			return;
		}
	}
	,
	transferChildrenTo: function (from, to) {
		var transfer = new $.ig.List$1($.ig.UIElement.prototype.$type, 0);
		var en = $.ig.Enumerable.prototype.ofType$1($.ig.UIElement.prototype.$type, from.children()).getEnumerator();
		while (en.moveNext()) {
			var child = en.current();
			transfer.add(child);
		}
		var en1 = transfer.getEnumerator();
		while (en1.moveNext()) {
			var child1 = en1.current();
			from.children().remove(child1);
			to.children().add(child1);
		}
	}
	,
	isPlottable: function (point) {
		return !$.ig.util.isNaN(point.__x) && !$.ig.util.isNaN(point.__y) && !Number.isInfinity(point.__x) && !Number.isInfinity(point.__y);
	}
	,
	isPlottable1: function (rect) {
		return !$.ig.util.isNaN(rect.left()) && !$.ig.util.isNaN(rect.right()) && !$.ig.util.isNaN(rect.top()) && !$.ig.util.isNaN(rect.bottom()) && !Number.isInfinity(rect.left()) && !Number.isInfinity(rect.right()) && !Number.isInfinity(rect.top()) && !Number.isInfinity(rect.bottom());
	}
	,
	$type: new $.ig.Type('Extensions', $.ig.Object.prototype.$type)
}, true);

} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["IProvidesViewport:a", 
"Void:b", 
"ValueType:c", 
"Object:d", 
"Type:e", 
"Boolean:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"Series:be", 
"Control:bf", 
"FrameworkElement:bg", 
"UIElement:bh", 
"DependencyObject:bi", 
"Dictionary:bj", 
"DependencyProperty:bk", 
"PropertyMetadata:bl", 
"PropertyChangedCallback:bm", 
"MulticastDelegate:bn", 
"IntPtr:bo", 
"DependencyPropertyChangedEventArgs:bp", 
"DependencyPropertiesCollection:bq", 
"UnsetValue:br", 
"Script:bs", 
"Binding:bt", 
"PropertyPath:bu", 
"Transform:bv", 
"Visibility:bw", 
"Style:bx", 
"Thickness:by", 
"HorizontalAlignment:bz", 
"VerticalAlignment:b0", 
"INotifyPropertyChanged:b1", 
"PropertyChangedEventHandler:b2", 
"PropertyChangedEventArgs:b3", 
"SeriesView:b4", 
"ISchedulableRender:b5", 
"SeriesViewer:b6", 
"SeriesViewerView:b7", 
"CanvasRenderScheduler:b8", 
"List$1:b9", 
"IList$1:ca", 
"ICollection$1:cb", 
"IArray:cc", 
"IArrayList:cd", 
"Array:ce", 
"CompareCallback:cf", 
"Func$3:cg", 
"Action$1:ch", 
"Comparer$1:ci", 
"IComparer:cj", 
"IComparer$1:ck", 
"DefaultComparer$1:cl", 
"Comparison$1:cm", 
"ReadOnlyCollection$1:cn", 
"Predicate$1:co", 
"NotImplementedException:cp", 
"Callback:cq", 
"window:cr", 
"RenderingContext:cs", 
"IRenderer:ct", 
"Rectangle:cu", 
"Shape:cv", 
"Brush:cw", 
"Color:cx", 
"ArgumentException:cy", 
"DoubleCollection:cz", 
"Path:c0", 
"Geometry:c1", 
"GeometryType:c2", 
"TextBlock:c3", 
"Polygon:c4", 
"PointCollection:c5", 
"Polyline:c6", 
"DataTemplateRenderInfo:c7", 
"DataTemplatePassInfo:c8", 
"ContentControl:c9", 
"DataTemplate:da", 
"DataTemplateRenderHandler:db", 
"DataTemplateMeasureHandler:dc", 
"DataTemplateMeasureInfo:dd", 
"DataTemplatePassHandler:de", 
"Line:df", 
"FontInfo:dg", 
"XamOverviewPlusDetailPane:dh", 
"XamOverviewPlusDetailPaneView:di", 
"XamOverviewPlusDetailPaneViewManager:dj", 
"JQueryObject:dk", 
"Element:dl", 
"ElementAttributeCollection:dm", 
"ElementCollection:dn", 
"WebStyle:dp", 
"ElementNodeType:dq", 
"Document:dr", 
"EventListener:ds", 
"IElementEventHandler:dt", 
"ElementEventHandler:du", 
"ElementAttribute:dv", 
"JQueryPosition:dw", 
"JQueryCallback:dx", 
"JQueryEvent:dy", 
"JQueryUICallback:dz", 
"EventProxy:d0", 
"ModifierKeys:d1", 
"Func$2:d2", 
"MouseWheelHandler:d3", 
"Delegate:d4", 
"Interlocked:d5", 
"GestureHandler:d6", 
"ZoomGestureHandler:d7", 
"FlingGestureHandler:d8", 
"ContactHandler:d9", 
"TouchHandler:ea", 
"MouseOverHandler:eb", 
"MouseHandler:ec", 
"KeyHandler:ed", 
"Key:ee", 
"JQuery:ef", 
"JQueryDeferred:eg", 
"JQueryPromise:eh", 
"Action:ei", 
"CanvasViewRenderer:ej", 
"CanvasContext2D:ek", 
"CanvasContext:el", 
"TextMetrics:em", 
"ImageData:en", 
"CanvasElement:eo", 
"Gradient:ep", 
"LinearGradientBrush:eq", 
"GradientStop:er", 
"GeometryGroup:es", 
"GeometryCollection:et", 
"FillRule:eu", 
"PathGeometry:ev", 
"PathFigureCollection:ew", 
"LineGeometry:ex", 
"RectangleGeometry:ey", 
"EllipseGeometry:ez", 
"ArcSegment:e0", 
"PathSegment:e1", 
"PathSegmentType:e2", 
"SweepDirection:e3", 
"PathFigure:e4", 
"PathSegmentCollection:e5", 
"LineSegment:e6", 
"PolyLineSegment:e7", 
"BezierSegment:e8", 
"PolyBezierSegment:e9", 
"GeometryUtil:fa", 
"Tuple$2:fb", 
"TransformGroup:fc", 
"TransformCollection:fd", 
"TranslateTransform:fe", 
"RotateTransform:ff", 
"ScaleTransform:fg", 
"DivElement:fh", 
"BaseDOMEventProxy:fi", 
"DOMEventProxy:fj", 
"MSGesture:fk", 
"MouseEventArgs:fl", 
"EventArgs:fm", 
"DoubleAnimator:fn", 
"EasingFunctionHandler:fo", 
"ImageElement:fp", 
"RectUtil:fq", 
"MathUtil:fr", 
"RuntimeHelpers:fs", 
"RuntimeFieldHandle:ft", 
"PropertyChangedEventArgs$1:fu", 
"InteractionState:fv", 
"OverviewPlusDetailPaneMode:fw", 
"IOverviewPlusDetailControl:fx", 
"EventHandler$1:fy", 
"ArgumentNullException:fz", 
"OverviewPlusDetailViewportHost:f0", 
"XamDataChart:f1", 
"GridMode:f2", 
"BrushCollection:f3", 
"ObservableCollection$1:f4", 
"INotifyCollectionChanged:f5", 
"NotifyCollectionChangedEventHandler:f6", 
"NotifyCollectionChangedEventArgs:f7", 
"NotifyCollectionChangedAction:f8", 
"InterpolationMode:f9", 
"Random:ga", 
"ColorUtil:gb", 
"AxisCollection:gc", 
"XamDataChartView:gd", 
"SeriesViewerViewManager:ge", 
"AxisTitlePosition:gf", 
"PointerTooltipStyle:gg", 
"Dictionary$2:gh", 
"IDictionary$2:gi", 
"IDictionary:gj", 
"KeyValuePair$2:gk", 
"Enumerable:gl", 
"Thread:gm", 
"ThreadStart:gn", 
"IOrderedEnumerable$1:go", 
"SortedList$1:gp", 
"IEqualityComparer$1:gq", 
"EqualityComparer$1:gr", 
"IEqualityComparer:gs", 
"DefaultEqualityComparer$1:gt", 
"InvalidOperationException:gu", 
"CanvasGestureDOMEventProxy:gv", 
"TouchPointInfo:gw", 
"DOMExecutionContext:gx", 
"IExecutionContext:gy", 
"ExecutionContextExecuteCallback:gz", 
"TouchGestureRecognizer:g0", 
"TouchGestureState:g1", 
"TouchVelocityTracker:g2", 
"TouchHistoryItem:g3", 
"TouchVelocityReading:g4", 
"TouchGestureEventHandler:g5", 
"TouchGestureEventArgs:g6", 
"CancelableTouchGestureEventHandler:g7", 
"CssHelper:g8", 
"CssGradientUtil:g9", 
"FontUtil:ha", 
"TileZoomTile:hb", 
"TileZoomTileInfo:hc", 
"TileZoomTileCache:hd", 
"TileZoomManager:he", 
"RectChangedEventHandler:hf", 
"RectChangedEventArgs:hg", 
"Debug:hh", 
"TileZoomInfo:hi", 
"LinkedList$1:hj", 
"LinkedListNode$1:hk", 
"RenderSurface:hl", 
"FragmentBase:hm", 
"HorizontalAnchoredCategorySeries:hn", 
"AnchoredCategorySeries:ho", 
"CategorySeries:hp", 
"MarkerSeries:hq", 
"MarkerSeriesView:hr", 
"Marker:hs", 
"DataContext:ht", 
"MarkerTemplates:hu", 
"HashPool$2:hv", 
"IHashPool$2:hw", 
"IPool$1:hx", 
"Func$1:hy", 
"Pool$1:hz", 
"IIndexedPool$1:h0", 
"MarkerType:h1", 
"SeriesVisualData:h2", 
"PrimitiveVisualDataList:h3", 
"IVisualData:h4", 
"PrimitiveVisualData:h5", 
"PrimitiveAppearanceData:h6", 
"BrushAppearanceData:h7", 
"StringBuilder:h8", 
"Environment:h9", 
"AppearanceHelper:ia", 
"LinearGradientBrushAppearanceData:ib", 
"GradientStopAppearanceData:ic", 
"SolidBrushAppearanceData:id", 
"GeometryData:ie", 
"GetPointsSettings:ig", 
"EllipseGeometryData:ih", 
"RectangleGeometryData:ii", 
"LineGeometryData:ij", 
"PathGeometryData:ik", 
"PathFigureData:il", 
"SegmentData:im", 
"LineSegmentData:io", 
"PolylineSegmentData:ip", 
"ArcSegmentData:iq", 
"PolyBezierSegmentData:ir", 
"BezierSegmentData:is", 
"LabelAppearanceData:it", 
"ShapeTags:iu", 
"PointerTooltipVisualDataList:iv", 
"MarkerVisualDataList:iw", 
"MarkerVisualData:ix", 
"PointerTooltipVisualData:iy", 
"RectangleVisualData:iz", 
"PolygonVisualData:i0", 
"PolyLineVisualData:i1", 
"IFastItemsSource:i2", 
"IFastItemColumn$1:i3", 
"IFastItemColumnPropertyName:i4", 
"FastItemsSourceEventArgs:i5", 
"FastItemsSourceEventAction:i6", 
"IHasCategoryModePreference:i7", 
"IHasCategoryAxis:i8", 
"CategoryAxisBase:i9", 
"Axis:ja", 
"AxisView:jb", 
"StackedSeriesBase:jc", 
"IIsCategoryBased:jd", 
"CategoryMode:je", 
"ICategoryScaler:jf", 
"IScaler:jg", 
"ScalerParams:jh", 
"IBucketizer:ji", 
"IDetectsCollisions:jj", 
"StackedSeriesView:jk", 
"CategorySeriesView:jl", 
"ISupportsMarkers:jm", 
"CategoryBucketCalculator:jn", 
"ISortingAxis:jo", 
"CategoryFrame:jp", 
"Frame:jq", 
"BrushUtil:jr", 
"Canvas:js", 
"Panel:jt", 
"UIElementCollection:ju", 
"StackedBucketCalculator:jv", 
"StackedSeriesManager:jw", 
"StackedSeriesCollection:jx", 
"StackedFragmentSeries:jy", 
"PenLineCap:jz", 
"PropertyUpdatedEventHandler:j0", 
"PropertyUpdatedEventArgs:j1", 
"StackedAreaSeries:j2", 
"HorizontalStackedSeriesBase:j3", 
"NumericYAxis:j4", 
"StraightNumericAxisBase:j5", 
"NumericAxisBase:j6", 
"NumericAxisBaseView:j7", 
"NumericAxisRenderer:j8", 
"AxisRendererBase:j9", 
"ShouldRenderHandler:ka", 
"ScaleValueHandler:kb", 
"AxisRenderingParametersBase:kc", 
"RangeInfo:kd", 
"TickmarkValues:ke", 
"TickmarkValuesInitializationParameters:kf", 
"Func$4:kg", 
"GetGroupCenterHandler:kh", 
"GetUnscaledGroupCenterHandler:ki", 
"PathRenderingInfo:kj", 
"RenderStripHandler:kk", 
"RenderLineHandler:kl", 
"ShouldRenderLinesHandler:km", 
"ShouldRenderContentHandler:kn", 
"RenderAxisLineHandler:ko", 
"DetermineCrossingValueHandler:kp", 
"ShouldRenderLabelHandler:kq", 
"GetLabelLocationHandler:kr", 
"LabelPosition:ks", 
"TransformToLabelValueHandler:kt", 
"AxisLabelManager:ku", 
"AxisLabelPanelBase:kv", 
"AxisLabelPanelBaseView:kw", 
"AxisLabelSettings:kx", 
"AxisLabelsLocation:ky", 
"TitleSettings:kz", 
"GetLabelForItemHandler:k0", 
"CreateRenderingParamsHandler:k1", 
"SnapMajorValueHandler:k2", 
"AdjustMajorValueHandler:k3", 
"CategoryAxisRenderingParameters:k4", 
"LogarithmicTickmarkValues:k5", 
"LogarithmicNumericSnapper:k6", 
"Snapper:k7", 
"LinearTickmarkValues:k8", 
"LinearNumericSnapper:k9", 
"AxisRangeChangedEventArgs:la", 
"AxisRange:lb", 
"AutoRangeCalculator:lc", 
"NumericRadiusAxis:ld", 
"NumericRadiusAxisView:le", 
"NumericAngleAxis:lf", 
"IAngleScaler:lg", 
"NumericAngleAxisView:lh", 
"PolarAxisRenderingManager:li", 
"ViewportUtils:lj", 
"PolarAxisRenderingParameters:lk", 
"NumericAxisRenderingParameters:ll", 
"IPolarRadialRenderingParameters:lm", 
"RadialAxisRenderingParameters:ln", 
"AxisOrientation:lo", 
"AngleAxisLabelPanel:lp", 
"AngleAxisLabelPanelView:lq", 
"Extensions:lr", 
"CategoryAngleAxis:ls", 
"CategoryAngleAxisView:lt", 
"CategoryAxisBaseView:lu", 
"CategoryAxisRenderer:lv", 
"LinearCategorySnapper:lw", 
"CategoryTickmarkValues:lx", 
"RadialAxisLabelPanel:ly", 
"HorizontalAxisLabelPanelBase:lz", 
"HorizontalAxisLabelPanelBaseView:l0", 
"RadialAxisLabelPanelView:l1", 
"NumericScaler:l2", 
"StraightNumericAxisBaseView:l3", 
"NumericScaleMode:l4", 
"LogarithmicScaler:l5", 
"NumericXAxis:l6", 
"NumericXAxisView:l7", 
"HorizontalSmartAxisLabelPanel:l8", 
"AxisExtentType:l9", 
"SmartAxisLabelDisplayType:ma", 
"HorizontalSmartAxisLabelPanelView:mb", 
"FontMappingInfo:mc", 
"CategoryDateTimeXAxis:md", 
"CategoryDateTimeXAxisView:me", 
"TimeAxisDisplayType:mf", 
"FastItemDateTimeColumn:mg", 
"IFastItemColumnInternal:mh", 
"FastItemColumn:mi", 
"FastReflectionHelper:mj", 
"HorizontalAxisLabelPanel:mk", 
"CoercionInfo:ml", 
"SortedListView$1:mm", 
"ArrayUtil:mn", 
"HorizontalLogarithmicScaler:mo", 
"HorizontalLinearScaler:mp", 
"LinearScaler:mq", 
"NumericYAxisView:mr", 
"VerticalAxisLabelPanel:ms", 
"VerticalAxisLabelPanelView:mt", 
"VerticalLogarithmicScaler:mu", 
"VerticalLinearScaler:mv", 
"CategoryFramePreparerBase:mw", 
"FramePreparer:mx", 
"ISupportsErrorBars:my", 
"DefaultSupportsMarkers:mz", 
"DefaultProvidesViewport:m0", 
"DefaultSupportsErrorBars:m1", 
"PreparationParams:m2", 
"CategoryYAxis:m3", 
"CategoryYAxisView:m4", 
"SyncSettings:m5", 
"ValuesHolder:m6", 
"LineSeries:m7", 
"LineSeriesView:m8", 
"AnchoredCategorySeriesView:m9", 
"CategoryTrendLineManagerBase:na", 
"TrendLineManagerBase$1:nb", 
"TrendLineType:nc", 
"Clipper:nd", 
"EdgeClipper:ne", 
"LeftClipper:nf", 
"BottomClipper:ng", 
"RightClipper:nh", 
"TopClipper:ni", 
"TrendResolutionParams:nj", 
"Flattener:nk", 
"Stack$1:nl", 
"ReverseArrayEnumerator$1:nm", 
"SpiralTodo:nn", 
"FlattenerSettings:no", 
"IPreparesCategoryTrendline:np", 
"SortingTrendLineManager:nq", 
"TrendFitCalculator:nr", 
"LeastSquaresFit:ns", 
"Numeric:nt", 
"TrendAverageCalculator:nu", 
"CategoryTrendLineManager:nv", 
"AnchoredCategoryBucketCalculator:nw", 
"UnknownValuePlotting:nx", 
"CategoryLineRasterizer:ny", 
"Action$5:nz", 
"PathVisualData:n0", 
"CategorySeriesRenderManager:n1", 
"AssigningCategoryStyleEventArgs:n2", 
"AssigningCategoryStyleEventArgsBase:n3", 
"GetCategoryItemsHandler:n4", 
"HighlightingInfo:n5", 
"HighlightingState:n6", 
"AssigningCategoryMarkerStyleEventArgs:n7", 
"HighlightingManager:n8", 
"SplineSeriesBase:n9", 
"SplineSeriesBaseView:oa", 
"SplineType:ob", 
"CollisionAvoider:oc", 
"SafeSortedReadOnlyDoubleCollection:od", 
"SafeReadOnlyDoubleCollection:oe", 
"SafeEnumerable:of", 
"AreaSeries:og", 
"AreaSeriesView:oh", 
"LegendTemplates:oi", 
"PieChartBase:oj", 
"PieChartBaseView:ok", 
"PieChartViewManager:ol", 
"PieChartVisualData:om", 
"PieSliceVisualDataList:on", 
"PieSliceVisualData:oo", 
"PieSliceDataContext:op", 
"Slice:oq", 
"SliceView:or", 
"PieLabel:os", 
"LabelsPosition:ot", 
"MouseButtonEventArgs:ou", 
"FastItemsSource:ov", 
"ColumnReference:ow", 
"FastItemObjectColumn:ox", 
"FastItemIntColumn:oy", 
"LeaderLineType:oz", 
"OthersCategoryType:o0", 
"IndexCollection:o1", 
"LegendBase:o2", 
"LegendBaseView:o3", 
"LegendBaseViewManager:o4", 
"GradientData:o5", 
"GradientStopData:o6", 
"DataChartLegendMouseButtonEventArgs:o7", 
"DataChartMouseButtonEventArgs:o8", 
"ChartLegendMouseEventArgs:o9", 
"ChartMouseEventArgs:pa", 
"DataChartLegendMouseButtonEventHandler:pb", 
"DataChartLegendMouseEventHandler:pc", 
"LegendVisualData:pd", 
"LegendVisualDataList:pe", 
"LegendItemVisualData:pf", 
"FunnelSliceDataContext:pg", 
"PieChartFormatLabelHandler:ph", 
"LabelClickEventHandler:pi", 
"LabelClickEventArgs:pj", 
"SliceClickEventHandler:pk", 
"SliceClickEventArgs:pl", 
"ItemLegend:pm", 
"ItemLegendView:pn", 
"LegendItemInfo:po", 
"BubbleSeries:pp", 
"ScatterBase:pq", 
"ScatterBaseView:pr", 
"MarkerManagerBase:ps", 
"OwnedPoint:pt", 
"MarkerManagerBucket:pu", 
"ScatterTrendLineManager:pv", 
"NumericMarkerManager:pw", 
"CollisionAvoidanceType:px", 
"SmartPlacer:py", 
"ISmartPlaceable:pz", 
"SmartPosition:p0", 
"SmartPlaceableWrapper$1:p1", 
"ScatterAxisInfoCache:p2", 
"ScatterErrorBarSettings:p3", 
"ErrorBarSettingsBase:p4", 
"EnableErrorBars:p5", 
"ErrorBarCalculatorReference:p6", 
"IErrorBarCalculator:p7", 
"ErrorBarCalculatorType:p8", 
"ScatterFrame:p9", 
"ScatterFrameBase$1:qa", 
"DictInterpolator$3:qb", 
"Action$6:qc", 
"SeriesHitTestMode:qd", 
"SyncLink:qe", 
"IFastItemsSourceProvider:qf", 
"ChartCollection:qg", 
"FastItemsSourceReference:qh", 
"SyncManager:qi", 
"SyncLinkManager:qj", 
"ErrorBarsHelper:qk", 
"BubbleSeriesView:ql", 
"BubbleMarkerManager:qm", 
"SizeScale:qn", 
"BrushScale:qo", 
"ScaleLegend:qp", 
"ScaleLegendView:qq", 
"CustomPaletteBrushScale:qr", 
"BrushSelectionMode:qs", 
"ValueBrushScale:qt", 
"RingSeriesBase:qu", 
"XamDoughnutChart:qv", 
"RingCollection:qw", 
"Ring:qx", 
"RingControl:qy", 
"RingControlView:qz", 
"Arc:q0", 
"ArcView:q1", 
"ArcItem:q2", 
"SliceItem:q3", 
"RingSeriesBaseView:q4", 
"Nullable$1:q5", 
"RingSeriesCollection:q6", 
"SliceCollection:q7", 
"XamDoughnutChartView:q8", 
"Action$2:q9", 
"DoughnutChartVisualData:ra", 
"RingSeriesVisualDataList:rb", 
"RingSeriesVisualData:rc", 
"RingVisualDataList:rd", 
"RingVisualData:re", 
"ArcVisualDataList:rf", 
"ArcVisualData:rg", 
"SliceVisualDataList:rh", 
"SliceVisualData:ri", 
"DoughnutChartLabelVisualData:rj", 
"HoleDimensionsChangedEventHandler:rk", 
"HoleDimensionsChangedEventArgs:rl", 
"XamFunnelChart:rm", 
"IItemProvider:rn", 
"MessageHandler:ro", 
"MessageHandlerEventHandler:rp", 
"Message:rq", 
"ServiceProvider:rr", 
"MessageChannel:rs", 
"MessageEventHandler:rt", 
"Queue$1:ru", 
"XamFunnelConnector:rv", 
"XamFunnelController:rw", 
"SliceInfoList:rx", 
"SliceInfo:ry", 
"SliceAppearance:rz", 
"PointList:r0", 
"FunnelSliceVisualData:r1", 
"PointData:r2", 
"SliceInfoUnaryComparison:r3", 
"Bezier:r4", 
"BezierPoint:r5", 
"BezierOp:r6", 
"BezierPointComparison:r7", 
"DoubleColumn:r8", 
"ObjectColumn:r9", 
"XamFunnelView:sa", 
"IOuterLabelWidthDecider:sb", 
"IFunnelLabelSizeDecider:sc", 
"MouseLeaveMessage:sd", 
"InteractionMessage:se", 
"MouseMoveMessage:sf", 
"MouseButtonMessage:sg", 
"MouseButtonAction:sh", 
"MouseButtonType:si", 
"SetAreaSizeMessage:sj", 
"RenderingMessage:sk", 
"RenderSliceMessage:sl", 
"RenderOuterLabelMessage:sm", 
"TooltipValueChangedMessage:sn", 
"TooltipUpdateMessage:so", 
"FunnelDataContext:sp", 
"PropertyChangedMessage:sq", 
"ConfigurationMessage:sr", 
"ClearMessage:ss", 
"ClearTooltipMessage:st", 
"ContainerSizeChangedMessage:su", 
"ViewportChangedMessage:sv", 
"ViewPropertyChangedMessage:sw", 
"OuterLabelAlignment:sx", 
"FunnelSliceDisplay:sy", 
"SliceSelectionManager:sz", 
"DataUpdatedMessage:s0", 
"ItemsSourceAction:s1", 
"FunnelFrame:s2", 
"UserSelectedItemsChangedMessage:s3", 
"LabelSizeChangedMessage:s4", 
"FrameRenderCompleteMessage:s5", 
"IntColumn:s6", 
"IntColumnComparison:s7", 
"Convert:s8", 
"SelectedItemsChangedMessage:s9", 
"ModelUpdateMessage:ta", 
"SliceClickedMessage:tb", 
"FunnelSliceClickedEventHandler:tc", 
"FunnelSliceClickedEventArgs:td", 
"FunnelChartVisualData:te", 
"FunnelSliceVisualDataList:tf", 
"RingSeries:tg", 
"WaterfallSeries:th", 
"WaterfallSeriesView:ti", 
"CategoryTransitionInMode:tj", 
"FinancialSeries:tk", 
"FinancialSeriesView:tl", 
"FinancialBucketCalculator:tm", 
"CategoryTransitionSourceFramePreparer:tn", 
"TransitionInSpeedType:to", 
"FinancialCalculationDataSource:tp", 
"CalculatedColumn:tq", 
"FinancialEventArgs:tr", 
"FinancialCalculationSupportingCalculations:ts", 
"ColumnSupportingCalculation:tt", 
"SupportingCalculation$1:tu", 
"SupportingCalculationStrategy:tv", 
"DataSourceSupportingCalculation:tw", 
"ProvideColumnValuesStrategy:tx", 
"AssigningCategoryStyleEventHandler:ty", 
"FinancialValueList:tz", 
"CategoryXAxis:t0", 
"CategoryXAxisView:t1", 
"FinancialEventHandler:t2", 
"StepLineSeries:t3", 
"StepLineSeriesView:t4", 
"StepAreaSeries:t5", 
"StepAreaSeriesView:t6", 
"RangeAreaSeries:t7", 
"HorizontalRangeCategorySeries:t8", 
"RangeCategorySeries:t9", 
"IHasHighLowValueCategory:ua", 
"RangeCategorySeriesView:ub", 
"RangeCategoryBucketCalculator:uc", 
"RangeCategoryFramePreparer:ud", 
"IHasCategoryTrendline:ue", 
"IHasTrendline:uf", 
"DefaultCategoryTrendlineHost:ug", 
"DefaultCategoryTrendlinePreparer:uh", 
"DefaultHighLowValueProvider:ui", 
"HighLowValuesHolder:uj", 
"CategoryMarkerManager:uk", 
"RangeValueList:ul", 
"RangeAreaSeriesView:um", 
"LineFragment:un", 
"LineFragmentView:uo", 
"LineFragmentBucketCalculator:up", 
"IStacked100Series:uq", 
"AreaFragment:ur", 
"AreaFragmentView:us", 
"AreaFragmentBucketCalculator:ut", 
"StackedSplineAreaSeries:uu", 
"SplineAreaFragment:uv", 
"SplineFragmentBase:uw", 
"SplineAreaFragmentView:ux", 
"StackedColumnSeries:uy", 
"StackedColumnSeriesView:uz", 
"StackedColumnBucketCalculator:u0", 
"ColumnFragment:u1", 
"ColumnFragmentView:u2", 
"StackedBarSeries:u3", 
"VerticalStackedSeriesBase:u4", 
"IBarSeries:u5", 
"StackedBarSeriesView:u6", 
"StackedBarBucketCalculator:u7", 
"BarFragment:u8", 
"StackedLineSeries:u9", 
"StackedSplineSeries:va", 
"SplineFragment:vb", 
"SplineFragmentView:vc", 
"SplineFragmentBucketCalculator:vd", 
"StackedSeriesFramePreparer:ve", 
"CategoryFramePreparer:vf", 
"IHasSingleValueCategory:vg", 
"DefaultSingleValueProvider:vh", 
"SingleValuesHolder:vi", 
"StackedSeriesCreatedEventHandler:vj", 
"StackedSeriesCreatedEventArgs:vk", 
"StackedSeriesVisualData:vl", 
"SeriesVisualDataList:vm", 
"AxisComponentsForView:vn", 
"AxisComponentsFromView:vo", 
"AxisFormatLabelHandler:vp", 
"VisualExportHelper:vq", 
"ContentInfo:vr", 
"LabelFontHeuristics:vs", 
"AxisRangeChangedEventHandler:vt", 
"ChartContentManager:vu", 
"ChartContentType:vv", 
"RenderRequestedEventArgs:vw", 
"ChartTitleVisualData:vx", 
"VisualDataSerializer:vy", 
"AxisVisualData:vz", 
"AxisLabelVisualDataList:v0", 
"AxisLabelVisualData:v1", 
"AssigningCategoryMarkerStyleEventHandler:v2", 
"SeriesComponentsForView:v3", 
"CategorySeriesMarkerCollisionAvoidance:v4", 
"NonCollisionAvoider:v5", 
"DataChartAxisRangeChangedEventHandler:v6", 
"ChartAxisRangeChangedEventArgs:v7", 
"ChartVisualData:v8", 
"AxisVisualDataList:v9", 
"RadialBase:wa", 
"RadialBaseView:wb", 
"RadialBucketCalculator:wc", 
"SeriesRenderer$2:wd", 
"SeriesRenderingArguments:we", 
"RadialFrame:wf", 
"RadialAxes:wg", 
"PolarBase:wh", 
"PolarBaseView:wi", 
"PolarTrendLineManager:wj", 
"PolarLinePlanner:wk", 
"AngleRadiusPair:wl", 
"PolarAxisInfoCache:wm", 
"PolarFrame:wn", 
"PolarAxes:wo", 
"SeriesCollection:wp", 
"SeriesViewerComponentsFromView:wq", 
"SeriesViewerSurfaceViewer:wr", 
"LabelPanelArranger:ws", 
"LabelPanelsArrangeState:wt", 
"ChartHitTestMode:wu", 
"WindowResponse:wv", 
"ViewerSurfaceUsage:ww", 
"SeriesViewerComponentsForView:wx", 
"DataChartCursorEventHandler:wy", 
"ChartCursorEventArgs:wz", 
"DataChartMouseButtonEventHandler:w0", 
"DataChartMouseEventHandler:w1", 
"AnnotationLayer:w2", 
"AnnotationLayerView:w3", 
"RefreshCompletedEventHandler:w4", 
"SeriesComponentsFromView:w5", 
"EasingFunctions:w6", 
"PolygonUtil:w7", 
"TrendCalculators:w8", 
"BarFramePreparer:yi", 
"BarTrendFitCalculator:yj", 
"BarTrendLineManager:yk", 
"VerticalAnchoredCategorySeries:yl", 
"BarSeries:ym", 
"BarSeriesView:yn", 
"BarBucketCalculator:yo", 
"RadialTrendLineManager:y1", 
"AnchoredRadialSeries:ab8", 
"AnchoredRadialSeriesView:ab9", 
"AnchoredRadialBucketCalculator:aca", 
"RadialAreaSeries:acb", 
"RadialAreaSeriesView:acc", 
"RadialColumnSeries:acd", 
"RadialColumnSeriesView:ace", 
"RadialLineSeries:acf", 
"RadialLineSeriesView:acg", 
"RadialPieSeries:ach", 
"RadialPieSeriesView:aci", 
"SliceCoords:acj", 
"Legend:ac1", 
"LegendView:ac2", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('AngleAxisLabelPanel', 'AxisLabelPanelBase', {
	createView: function () {
		return new $.ig.AngleAxisLabelPanelView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AxisLabelPanelBase.prototype.onViewCreated.call(this, view);
		this.angleView(view);
	}
	,
	_angleView: null,
	angleView: function (value) {
		if (arguments.length === 1) {
			this._angleView = value;
			return value;
		} else {
			return this._angleView;
		}
	}
	,
	init: function () {
		$.ig.AxisLabelPanelBase.prototype.init.call(this);
	},
	_clipLabelsToBounds: false,
	clipLabelsToBounds: function (value) {
		if (arguments.length === 1) {
			this._clipLabelsToBounds = value;
			return value;
		} else {
			return this._clipLabelsToBounds;
		}
	}
	,
	_getPoint: null,
	getPoint: function (value) {
		if (arguments.length === 1) {
			this._getPoint = value;
			return value;
		} else {
			return this._getPoint;
		}
	}
	,
	shouldDisplay: function (index, bounds) {
		if (!$.ig.Extensions.prototype.isPlottable1(bounds)) {
			return false;
		}
		if (this.axis() == null || this.axis().viewportRect().isEmpty()) {
			return $.ig.AxisLabelPanelBase.prototype.shouldDisplay.call(this, index, bounds);
		}
		return true;
	}
	,
	_largestWidth: 0,
	largestWidth: function (value) {
		if (arguments.length === 1) {
			this._largestWidth = value;
			return value;
		} else {
			return this._largestWidth;
		}
	}
	,
	_largestHeight: 0,
	largestHeight: function (value) {
		if (arguments.length === 1) {
			this._largestHeight = value;
			return value;
		} else {
			return this._largestHeight;
		}
	}
	,
	_largestRenderWidth: 0,
	largestRenderWidth: function (value) {
		if (arguments.length === 1) {
			this._largestRenderWidth = value;
			return value;
		} else {
			return this._largestRenderWidth;
		}
	}
	,
	_largestRenderHeight: 0,
	largestRenderHeight: function (value) {
		if (arguments.length === 1) {
			this._largestRenderHeight = value;
			return value;
		} else {
			return this._largestRenderHeight;
		}
	}
	,
	createBoundsRectangles: function () {
		var rectangles = new $.ig.List$1($.ig.Rect.prototype.$type, 0);
		if (this.textBlocks().count() != this.labelPositions().count()) {
			return rectangles;
		}
		this.angleView().determineLargestLabels(rectangles);
		var extentChanged = false;
		this.foundCollisions(this.detectCollisions(rectangles));
		if (!this.axis().hasUserExtent()) {
			extentChanged = true;
			this.extent(this.largestWidth() / 2);
			this.extent(this.extent() + this.otherExtentValues());
		} else {
			extentChanged = true;
			this.angleView().bindExtentToSettings();
		}
		if (extentChanged) {
			for (var i = 0; i < rectangles.count(); i++) {
				var currentTextBlock = this.textBlocks().__inner[i];
				var position = this.labelPositions().__inner[i];
				var currentRect = rectangles.__inner[i];
				var point = this.getPoint()(position.value());
				var x = point.__x - this.getDesiredWidth(currentTextBlock) / 2;
				var y = point.__y - this.getDesiredHeight(currentTextBlock) / 2;
				currentRect.x(x);
				currentRect.y(y);
				rectangles.__inner[i] = currentRect;
			}
		}
		return rectangles;
	}
	,
	otherExtentValues: function () {
		var $self = this;
		var owningChart;
		var axis;
		axis = this.axis();
		owningChart = null;
		if (axis != null) {
			owningChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, axis.seriesViewer());
		}
		if (owningChart == null || axis == null) {
			return 0;
		}
		var angleAxes = $.ig.Enumerable.prototype.toList$1($.ig.Axis.prototype.$type, ($.ig.Enumerable.prototype.where$1($.ig.Axis.prototype.$type, (owningChart.axes()), function (a) { return ($.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, a) !== null || $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, a) !== null) && $.ig.util.cast($.ig.AngleAxisLabelPanel.prototype.$type, a.labelPanel()) !== null && (a.labelPanel()).textBlocks().count() > 0 && (!a.hasUserExtent()) && !a.hasCrossingValue() && (a.crossingAxis() == null || $self.axis().crossingAxis() == null || (a.crossingAxis()).actualRadiusExtentScale() == (axis.crossingAxis()).actualRadiusExtentScale()); })));
		var index = angleAxes.indexOf(axis);
		if (index == -1) {
			return 0;
		}
		var extent = 0;
		var extentOffset = 5;
		if (index == 0) {
			extent += extentOffset;
		} else {
			extent += angleAxes.__inner[index - 1].labelPanel().extent();
			extent += angleAxes.__inner[index].labelPanel().extent() * 2 + extentOffset;
		}
		return extent;
	}
	,
	getDefaultLabelsLocation: function () {
		return $.ig.AxisLabelsLocation.prototype.insideTop;
	}
	,
	validLocation: function (location) {
		return location == $.ig.AxisLabelsLocation.prototype.insideTop || location == $.ig.AxisLabelsLocation.prototype.insideBottom;
	}
	,
	$type: new $.ig.Type('AngleAxisLabelPanel', $.ig.AxisLabelPanelBase.prototype.$type)
}, true);

$.ig.util.defType('CategoryAxisRenderer', 'AxisRendererBase', {
	init: function (labelManager) {
		$.ig.AxisRendererBase.prototype.init.call(this, labelManager);
	},
	getSnapperInfoInternal: function (renderingParams, interval, minorCount, first, last) {
		var catParams = $.ig.util.cast($.ig.CategoryAxisRenderingParameters.prototype.$type, renderingParams);
		var mode = $.ig.CategoryMode.prototype.mode0;
		if (catParams != null) {
			mode = catParams.categoryMode();
		}
		var snapper = new $.ig.LinearCategorySnapper(1, renderingParams.currentRangeInfo().visibleMinimum(), renderingParams.currentRangeInfo().visibleMaximum(), renderingParams.currentRangeInfo().resolution(), renderingParams.interval(), mode, renderingParams.heuristicResolution());
		interval = snapper.interval();
		if (renderingParams.currentRangeInfo().intervalOverride() != -1) {
			interval = renderingParams.currentRangeInfo().intervalOverride();
		}
		var firstValue = Math.floor((renderingParams.currentRangeInfo().visibleMinimum() - renderingParams.actualMinimumValue()) / interval);
		var lastValue = Math.ceil((renderingParams.currentRangeInfo().visibleMaximum() - renderingParams.actualMinimumValue()) / interval);
		first = $.ig.truncate(firstValue);
		last = $.ig.truncate(lastValue);
		minorCount = $.ig.truncate(snapper.minorCount());
		if (renderingParams.currentRangeInfo().minorCountOverride() != -1) {
			minorCount = renderingParams.currentRangeInfo().minorCountOverride();
		}
		return {
			p1: interval,
			p2: minorCount,
			p3: first,
			p4: last
		};
	}
	,
	getLabel: function (renderingParams, unscaledValue, index, interval) {
		var catParams = $.ig.util.cast($.ig.CategoryAxisRenderingParameters.prototype.$type, renderingParams);
		if (catParams == null) {
			return null;
		}
		var itemIndex = 0;
		if (interval >= 1) {
			itemIndex = index * $.ig.truncate(Math.floor(interval));
		} else {
			if ((index * interval) * 2 % 2 == 0) {
				itemIndex = $.ig.truncate(Math.floor(index * interval));
			} else {
				itemIndex = -1;
			}
		}
		var label = null;
		if ((catParams.count() > 0 && itemIndex < catParams.count() && itemIndex >= 0) || catParams.wrapAround()) {
			while (itemIndex >= catParams.count() && catParams.wrapAround()) {
				itemIndex -= catParams.count();
			}
			label = this.getLabelForItem()(itemIndex);
		}
		return label;
	}
	,
	renderMinorLines: function (renderingParams, interval, minorCount, majorValue, i, nextMajorValue) {
		var catParams = $.ig.util.cast($.ig.CategoryAxisRenderingParameters.prototype.$type, renderingParams);
		if (catParams.categoryMode() != $.ig.CategoryMode.prototype.mode0 && catParams.mode2GroupCount() != 0) {
			for (var categoryNumber = 0; categoryNumber < $.ig.truncate(interval); categoryNumber++) {
				for (var groupNumber = 0; groupNumber < catParams.mode2GroupCount(); groupNumber++) {
					var center = this.getGroupCenter()(groupNumber, renderingParams.windowRect(), renderingParams.viewportRect(), renderingParams.effectiveViewportRect());
					if (catParams.isInverted()) {
						center = -center;
					}
					var minorValue = this.scaling()(renderingParams, categoryNumber + i * interval) + center;
					renderingParams.currentRenderingInfo(renderingParams.minorRenderingInfo());
					this.line()(renderingParams, renderingParams.minor(), minorValue);
					renderingParams.currentRenderingInfo(null);
				}
			}
		}
	}
	,
	$type: new $.ig.Type('CategoryAxisRenderer', $.ig.AxisRendererBase.prototype.$type)
}, true);

$.ig.util.defType('IAngleScaler', 'Object', {
	$type: new $.ig.Type('IAngleScaler', null)
}, true);

$.ig.util.defType('CategoryAngleAxis', 'CategoryAxisBase', {
	createView: function () {
		return new $.ig.CategoryAngleAxisView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.CategoryAxisBase.prototype.onViewCreated.call(this, view);
		this.categoryAngleView(view);
	}
	,
	_categoryAngleView: null,
	categoryAngleView: function (value) {
		if (arguments.length === 1) {
			this._categoryAngleView = value;
			return value;
		} else {
			return this._categoryAngleView;
		}
	}
	,
	isAngular: function () {
		return true;
	}
	,
	_renderingManager: null,
	init: function () {
		this.__preventReentry = false;
		this.__lastCrossing = NaN;
		this.__startAngleOffsetRadians = 0;
		this.__actualMaximum = 1;
		$.ig.CategoryAxisBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.CategoryAngleAxis.prototype.$type);
		this.renderer(this.createRenderer());
		this._renderingManager = new $.ig.PolarAxisRenderingManager();
	},
	createLabelPanel: function () {
		var $self = this;
		var panel = new $.ig.AngleAxisLabelPanel();
		panel.getPoint(function (v) {
			var windowRect = $self.seriesViewer() != null ? $self.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
			var viewportRect = !windowRect.isEmpty() ? $self.viewportRect() : $.ig.Rect.prototype.empty();
			return $self.getLabelLocationPoint(v, { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, windowRect, viewportRect, $self.labelPanel().extent());
		});
		return panel;
	}
	,
	_renderer: null,
	renderer: function (value) {
		if (arguments.length === 1) {
			this._renderer = value;
			return value;
		} else {
			return this._renderer;
		}
	}
	,
	__preventReentry: false,
	round10: function (value) {
		return Math.round(value * Math.pow(10, 10)) / Math.pow(10, 10);
	}
	,
	__lastCrossing: 0,
	createRenderer: function () {
		var $self = this;
		var labelManager = (function () {
			var $ret = new $.ig.AxisLabelManager();
			$ret.axis($self);
			$ret.labelPositions($self.labelPositions());
			$ret.labelDataContext($self.labelDataContext());
			$ret.targetPanel($self.labelPanel());
			return $ret;
		}());
		if (this.labelSettings() != null) {
			this.labelSettings().registerAxis(this);
		}
		var renderer = new $.ig.CategoryAxisRenderer(labelManager);
		renderer.clear(function () {
			var axisGeometry = $self.view().getAxisLinesGeometry();
			var stripsGeometry = $self.view().getStripsGeometry();
			var majorGeometry = $self.view().getMajorLinesGeometry();
			var minorGeometry = $self.view().getMinorLinesGeometry();
			$self.updateLineVisibility();
			$self.clearMarks(axisGeometry);
			$self.clearMarks(stripsGeometry);
			$self.clearMarks(majorGeometry);
			$self.clearMarks(minorGeometry);
		});
		renderer.shouldRender(function (viewport, window) {
			return !window.isEmpty() && !viewport.isEmpty() && $self.radiusAxis() != null;
		});
		renderer.createRenderingParams(function (viewport, window, effectiveViewportRect, contentViewport) {
			return $self.createRenderingParams(viewport, window, effectiveViewportRect, contentViewport);
		});
		renderer.onRendering(function () {
			if (!$self.__preventReentry) {
				$self.__preventReentry = true;
				$self.radiusAxis().updateRange();
				$self.__preventReentry = false;
			}
		});
		renderer.getLabelForItem(function (item) {
			var index = $.ig.util.getValue(item);
			if (index > $self.fastItemsSource().count() - 1) {
				index -= $self.fastItemsSource().count();
			}
			var dataItem = $self.fastItemsSource().item(index);
			return $self.getLabel(dataItem);
		});
		renderer.labelManager().floatPanelAction(function (crossing) {
			if (($self.labelSettings() == null || $self.labelSettings().visibility() == $.ig.Visibility.prototype.visible) && $self.radiusAxis() != null && $self.__lastCrossing != crossing) {
				var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, $self.seriesViewer());
				if (dataChart == null) {
					return;
				}
				$self.__lastCrossing = crossing;
				$self.labelPanel().crossingValue(crossing);
				dataChart.invalidatePanels();
				var en = dataChart.axes().getEnumerator();
				while (en.moveNext()) {
					var axis = en.current();
					if (axis != $self && $.ig.util.cast($.ig.AngleAxisLabelPanel.prototype.$type, axis.labelPanel()) !== null) {
						axis.view().labelNeedRearrange();
					}
				}
			}
		});
		renderer.line(function (p, g, value) {
			var r2 = $.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.radialLine(g, value, p.viewportRect(), p.windowRect(), r2.minLength(), r2.maxLength(), r2.center());
		});
		renderer.strip(function (p, g, start, end) {
			var r3 = $.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.radialStrip(g, start, end, r3.viewportRect(), p.windowRect(), r3.minLength(), r3.maxLength(), r3.center());
		});
		renderer.scaling(function (p, unscaled) {
			return $self.getScaledAngle(unscaled);
		});
		renderer.shouldRenderLines(function (p, value) {
			if ($self.round10(value - $self.__startAngleOffsetRadians) < 0) {
				return false;
			}
			if ($self.round10(value - $self.__startAngleOffsetRadians - (2 * Math.PI)) > 0) {
				return false;
			}
			return true;
		});
		renderer.axisLine(function (p) {
			var r4 = $.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, p);
			if (r4.currentRangeInfo() == r4.rangeInfos().__inner[0]) {
				$self._renderingManager.concentricLine(p.axisGeometry(), p.crossingValue(), p.viewportRect(), p.windowRect(), r4.center(), r4.minAngle(), r4.maxAngle());
			}
		});
		renderer.determineCrossingValue(function (p) {
			p.crossingValue($self.getCrossingValue());
			p.relativeCrossingValue(p.crossingValue());
		});
		renderer.shouldRenderLabel(function (p, value, last) {
			var r5 = $.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, p);
			if (last) {
				return false;
			}
			var labelPoint = $self.getLabelLocationPoint(value, r5.center(), p.windowRect(), p.viewportRect(), 0);
			if (labelPoint.__x < p.viewportRect().right() && labelPoint.__x >= p.viewportRect().left() && labelPoint.__y < p.viewportRect().bottom() && labelPoint.__y >= p.viewportRect().top()) {
				return true;
			}
			return false;
		});
		renderer.adjustMajorValue(function (p, value, i, interval) {
			var effectiveViewportRect = p.effectiveViewportRect();
			var sParams = new $.ig.ScalerParams(0, p.windowRect(), p.viewportRect(), $self.isInverted(), effectiveViewportRect);
			var categoryValue = value;
			if ($self.categoryMode() != $.ig.CategoryMode.prototype.mode0) {
				var unscaledValue = (i * interval) + 1;
				unscaledValue = Math.min(unscaledValue, $self._cachedItemsCount);
				var nextCategoryValue = $self.getScaledValue(unscaledValue, sParams);
				categoryValue = (value + nextCategoryValue) / 2;
			}
			return categoryValue;
		});
		renderer.getGroupCenter(this.getGroupCenter.runOn(this));
		renderer.getUnscaledGroupCenter(this.getUnscaledGroupCenter.runOn(this));
		return renderer;
	}
	,
	getLabelLocationPoint: function (angleValue, center, windowRect, viewportRect, extent) {
		var crossingValue = this.getCrossingValue();
		var extentValue = $.ig.ViewportUtils.prototype.transformXFromViewportLength(extent, windowRect, viewportRect);
		if (this.labelSettings() != null && (this.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom || this.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.outsideBottom)) {
			extentValue *= -1;
		}
		var x = center.__x + (crossingValue + extentValue) * Math.cos(angleValue);
		var y = center.__y + (crossingValue + extentValue) * Math.sin(angleValue);
		x = $.ig.ViewportUtils.prototype.transformXToViewport(x, windowRect, viewportRect);
		y = $.ig.ViewportUtils.prototype.transformYToViewport(y, windowRect, viewportRect);
		return { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getCrossingValue: function () {
		if (this.radiusAxis() == null) {
			return 0;
		}
		if (!this.hasCrossingValue()) {
			return this.radiusAxis().getEffectiveMaximumLength();
		} else {
			return this.radiusAxis().getScaledValue3((this.crossingValue()));
		}
	}
	,
	createRenderingParams: function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
		if (this.fastItemsSource() == null) {
			return null;
		}
		var renderingParams = new $.ig.RadialAxisRenderingParameters();
		var max = this.fastItemsSource().count() - 1;
		var axisGeometry = this.view().getAxisLinesGeometry();
		var stripsGeometry = this.view().getStripsGeometry();
		var majorGeometry = this.view().getMajorLinesGeometry();
		var minorGeometry = this.view().getMinorLinesGeometry();
		renderingParams.axisGeometry(axisGeometry);
		renderingParams.strips(stripsGeometry);
		renderingParams.major(majorGeometry);
		renderingParams.minor(minorGeometry);
		renderingParams.actualMaximumValue(max);
		renderingParams.actualMinimumValue(0);
		renderingParams.hasUserMax(false);
		renderingParams.viewportRect(viewportRect);
		renderingParams.effectiveViewportRect(effectiveViewportRect);
		renderingParams.contentViewport(contentViewport);
		renderingParams.windowRect(windowRect);
		renderingParams.hasUserInterval(this.hasUserInterval());
		renderingParams.interval(this.interval());
		renderingParams.heuristicResolution(this.resolveHeuristicResolution());
		renderingParams.minorInterval(this.minorInterval());
		renderingParams.label(this.label());
		var closestRadius = this._renderingManager.getClosestRadiusValue(windowRect);
		var furthestRadius = this._renderingManager.getFurthestRadiusValue(windowRect);
		var maxRadius = 0.5 * this.radiusAxis().actualRadiusExtentScale();
		var minRadius = 0.5 * this.radiusAxis().actualInnerRadiusExtentScale();
		var minLen = closestRadius;
		var maxLen = furthestRadius;
		var effectiveMaximum = this.radiusAxis().getEffectiveMaximumLength();
		if ($.ig.util.isNaN(effectiveMaximum) || Number.isInfinity(effectiveMaximum)) {
			return null;
		}
		if (maxLen >= maxRadius) {
			maxLen = effectiveMaximum;
		}
		if (minLen < minRadius) {
			minLen = minRadius;
		}
		var resolution = viewportRect.width();
		this._renderingManager.determineView(windowRect, renderingParams, 0, this.fastItemsSource().count(), this.isInverted(), this.getUnscaledAngle.runOn(this), resolution);
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		renderingParams.center(center);
		renderingParams.maxLength(maxLen);
		renderingParams.minLength(minLen);
		renderingParams.effectiveMaximum(effectiveMaximum);
		renderingParams.count(this.fastItemsSource().count());
		renderingParams.categoryMode(this.categoryMode());
		renderingParams.wrapAround(true);
		renderingParams.isInverted(this.isInverted());
		renderingParams.mode2GroupCount(this.mode2GroupCount());
		renderingParams.tickmarkValues(new $.ig.CategoryTickmarkValues());
		renderingParams.shouldRenderMinorLines(this.shouldRenderMinorLines());
		return renderingParams;
	}
	,
	getMinMaxAngle: function (windowRect, visibleMinimum, visibleMaximum) {
		var $ret = this._renderingManager.getMinMaxAngle(windowRect, visibleMinimum, visibleMaximum);
		visibleMinimum = $ret.p1;
		visibleMaximum = $ret.p2;
		return {
			p1: visibleMinimum,
			p2: visibleMaximum
		};
	}
	,
	onApplyTemplate: function () {
		$.ig.CategoryAxisBase.prototype.onApplyTemplate.call(this);
		this.renderAxis1(false);
	}
	,
	__radiusAxis: null,
	radiusAxis: function (value) {
		if (arguments.length === 1) {
			this.__radiusAxis = value;
			return value;
		} else {
			if (this.__radiusAxis != null) {
				return this.__radiusAxis;
			}
			var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
			if (dataChart != null) {
				for (var i = 0; i < dataChart.axes().count(); i++) {
					if ($.ig.util.cast($.ig.NumericRadiusAxis.prototype.$type, dataChart.axes().__inner[i]) !== null) {
						return dataChart.axes().__inner[i];
					}
				}
			}
			return this.__radiusAxis;
		}
	}
	,
	startAngleOffset: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryAngleAxis.prototype.startAngleOffsetProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryAngleAxis.prototype.startAngleOffsetProperty);
		}
	}
	,
	__startAngleOffsetRadians: 0,
	getCategorySize: function (windowRect, viewportRect, effectiveViewportRect) {
		return 2 * Math.PI / this._cachedItemsCount;
	}
	,
	getGroupSize: function (windowRect, viewportRect, effectiveViewportRect) {
		var gap = !$.ig.util.isNaN(this.gap()) ? $.ig.MathUtil.prototype.clamp(this.gap(), 0, 1) : 0;
		var overlap = 0;
		if (!$.ig.util.isNaN(this.overlap())) {
			overlap = Math.min(this.overlap(), 1);
		}
		var categorySpace = 1 - 0.5 * gap;
		return this.getCategorySize(windowRect, viewportRect, effectiveViewportRect) * categorySpace / (this.mode2GroupCount() - (this.mode2GroupCount() - 1) * overlap);
	}
	,
	getGroupCenter: function (groupIndex, windowRect, viewportRect, effectiveViewportRect) {
		var groupCenter = 0.5;
		if (this.mode2GroupCount() > 1) {
			var gap = !$.ig.util.isNaN(this.gap()) ? $.ig.MathUtil.prototype.clamp(this.gap(), 0, 1) : 0;
			var overlap = 0;
			if (!$.ig.util.isNaN(this.overlap())) {
				overlap = Math.min(this.overlap(), 1);
			}
			var categorySpace = 1 - 0.5 * gap;
			var groupWidth = categorySpace / (this.mode2GroupCount() - (this.mode2GroupCount() - 1) * overlap);
			var groupSep = (categorySpace - groupWidth) / (this.mode2GroupCount() - 1);
			groupCenter = 0.25 * gap + 0.5 * groupWidth + groupIndex * groupSep;
		}
		return this.getCategorySize(windowRect, viewportRect, effectiveViewportRect) * groupCenter;
	}
	,
	getUnscaledGroupCenter: function (groupIndex) {
		var groupCenter = 0.5;
		if (this.mode2GroupCount() > 1) {
			var gap = !$.ig.util.isNaN(this.gap()) ? $.ig.MathUtil.prototype.clamp(this.gap(), 0, 1) : 0;
			var overlap = 0;
			if (!$.ig.util.isNaN(this.overlap())) {
				overlap = Math.min(this.overlap(), 1);
			}
			var categorySpace = 1 - 0.5 * gap;
			var groupWidth = categorySpace / (this.mode2GroupCount() - (this.mode2GroupCount() - 1) * overlap);
			var groupSep = (categorySpace - groupWidth) / (this.mode2GroupCount() - 1);
			groupCenter = 0.25 * gap + 0.5 * groupWidth + groupIndex * groupSep;
		}
		return groupCenter;
	}
	,
	renderAxisOverride: function (animate) {
		$.ig.CategoryAxisBase.prototype.renderAxisOverride.call(this, animate);
		var windowRect = this.seriesViewer() != null ? this.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = !windowRect.isEmpty() ? this.viewportRect() : $.ig.Rect.prototype.empty();
		var effectiveViewportRect = this.getCurrentEffectiveViewport();
		var contentViewport = this.getContentViewport();
		var renderParams = this.renderer().render(animate, viewportRect, windowRect, effectiveViewportRect, contentViewport);
		if (renderParams != null) {
			this.actualInterval(renderParams.tickmarkValues().interval());
			this.actualMinorInterval(renderParams.tickmarkValues().actualMinorInterval());
		}
	}
	,
	getMinimumViewable: function (viewportRect, windowRect) {
		var minAngle;
		var maxAngle;
		var $ret = this._renderingManager.getMinMaxAngle(windowRect, minAngle, maxAngle);
		minAngle = $ret.p1;
		maxAngle = $ret.p2;
		if (minAngle == 0) {
			if (this.isInverted()) {
				return this._cachedItemsCount;
			} else {
				return 0;
			}
		}
		var value = this.getUnscaledAngle(minAngle);
		if (value < 0 || value > this._cachedItemsCount) {
			value = this.getUnscaledAngle(minAngle + Math.PI * 2);
		}
		return value;
	}
	,
	getMaximumViewable: function (viewportRect, windowRect) {
		var minAngle;
		var maxAngle;
		var $ret = this._renderingManager.getMinMaxAngle(windowRect, minAngle, maxAngle);
		minAngle = $ret.p1;
		maxAngle = $ret.p2;
		if (maxAngle > Math.PI * 2) {
			maxAngle = maxAngle - Math.PI * 2;
		}
		if (maxAngle == Math.PI * 2) {
			if (this.isInverted()) {
				return 0;
			} else {
				return this._cachedItemsCount;
			}
		}
		var value = this.getUnscaledAngle(maxAngle);
		if (value < 0 || value > this._cachedItemsCount) {
			value = this.getUnscaledAngle(maxAngle + Math.PI * 2);
		}
		return value;
	}
	,
	getScaledAngle: function (unscaledAngle) {
		var itemsCount = this._cachedItemsCount;
		var scaledValue = itemsCount >= 2 ? (unscaledAngle) / (itemsCount) : itemsCount == 1 ? 0.5 : NaN;
		if (this.isInvertedCached()) {
			scaledValue = 1 - scaledValue;
		}
		return (scaledValue * 2 * Math.PI) + this.__startAngleOffsetRadians;
	}
	,
	getUnscaledAngle: function (scaledAngle) {
		if (scaledAngle < this.__startAngleOffsetRadians) {
			scaledAngle += 2 * Math.PI;
		}
		var unscaledValue = (scaledAngle - this.__startAngleOffsetRadians) / (2 * Math.PI);
		if (this.isInverted()) {
			unscaledValue = 1 - unscaledValue;
		}
		return unscaledValue * (this._cachedItemsCount);
	}
	,
	getScaledValue: function (unscaledValue, p) {
		return (this).getScaledAngle(unscaledValue);
	}
	,
	getUnscaledValue: function (scaledValue, p) {
		return (this).getUnscaledAngle(scaledValue);
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.CategoryAxisBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
		switch (propertyName) {
			case $.ig.Axis.prototype.crossingAxisPropertyName:
				var radiusAxis = $.ig.util.cast($.ig.NumericRadiusAxis.prototype.$type, newValue);
				this.onRadiusAxisChanged(radiusAxis);
				if (radiusAxis != null) {
					radiusAxis.onAngleAxisChanged(this);
				}
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				break;
			case $.ig.CategoryAngleAxis.prototype.startAngleOffsetPropertyName:
				this.__startAngleOffsetRadians = this.startAngleOffset();
				while (this.__startAngleOffsetRadians < 0) {
					this.__startAngleOffsetRadians += 360;
				}
				while (this.__startAngleOffsetRadians >= 360) {
					this.__startAngleOffsetRadians -= 360;
				}
				this.__startAngleOffsetRadians = (this.__startAngleOffsetRadians * Math.PI) / 180;
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				var en = this.series().getEnumerator();
				while (en.moveNext()) {
					var s = en.current();
					s.renderSeries(false);
					s.notifyThumbnailAppearanceChanged();
				}
				break;
			case $.ig.Axis.prototype.labelPropertyName:
				if (dataChart != null) {
					var en1 = dataChart.axes().getEnumerator();
					while (en1.moveNext()) {
						var axis = en1.current();
						axis.mustInvalidateLabels(true);
						axis.renderAxis();
					}
				}
				break;
			case $.ig.Axis.prototype.crossingValuePropertyName:
				if (dataChart != null) {
					var en2 = dataChart.axes().getEnumerator();
					while (en2.moveNext()) {
						var axis1 = en2.current();
						if ($.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, axis1) !== null || $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, axis1) !== null) {
							axis1.mustInvalidateLabels(true);
							axis1.renderAxis();
						}
					}
				}
				break;
			case $.ig.Axis.prototype.labelSettingsPropertyName:
				this.renderer(this.createRenderer());
				this.forcePanelRefloat();
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				break;
		}
	}
	,
	forcePanelRefloat: function () {
		this.__lastCrossing = NaN;
	}
	,
	onRadiusAxisChanged: function (radiusAxis) {
		this.radiusAxis(radiusAxis);
	}
	,
	interval: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryAngleAxis.prototype.intervalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryAngleAxis.prototype.intervalProperty);
		}
	}
	,
	_actualInterval: 0,
	actualInterval: function (value) {
		if (arguments.length === 1) {
			if (this.actualInterval() != value) {
				var oldValue = this._actualInterval;
				this._actualInterval = value;
				this.raisePropertyChanged($.ig.CategoryAngleAxis.prototype.actualIntervalPropertyName, oldValue, this.actualInterval());
			}
			return value;
		} else {
			return this._actualInterval;
		}
	}
	,
	minorInterval: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryAngleAxis.prototype.minorIntervalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryAngleAxis.prototype.minorIntervalProperty);
		}
	}
	,
	_actualMinorInterval: 0,
	actualMinorInterval: function (value) {
		if (arguments.length === 1) {
			if (this.actualMinorInterval() != value) {
				var oldValue = this._actualMinorInterval;
				this._actualMinorInterval = value;
				this.raisePropertyChanged($.ig.CategoryAngleAxis.prototype.actualMinorIntervalPropertyName, oldValue, this.actualMinorInterval());
			}
			return value;
		} else {
			return this._actualMinorInterval;
		}
	}
	,
	hasUserInterval: function () {
		return false;
	}
	,
	orientation: function () {
		return $.ig.AxisOrientation.prototype.angular;
	}
	,
	updateRangeOverride: function () {
		if (this.mustInvalidateLabels() && this.useEnhancedIntervalManagement()) {
			this.labelFontHeuristics(this.gatherLabelFontHeuristics());
		}
		if (this.fastItemsSource() == null) {
			return false;
		}
		var max = this.fastItemsSource().count();
		if (max != this.actualMaximum()) {
			var ea = new $.ig.AxisRangeChangedEventArgs(1, 1, this.actualMaximum(), max);
			this.actualMaximum(max);
			this.raiseRangeChanged(ea);
			return true;
		}
		return false;
	}
	,
	__actualMaximum: 0,
	actualMaximum: function (value) {
		if (arguments.length === 1) {
			this.__actualMaximum = value;
			return value;
		} else {
			return this.__actualMaximum;
		}
	}
	,
	$type: new $.ig.Type('CategoryAngleAxis', $.ig.CategoryAxisBase.prototype.$type, [$.ig.IAngleScaler.prototype.$type])
}, true);

$.ig.util.defType('NumericAngleAxis', 'NumericAxisBase', {
	createView: function () {
		return new $.ig.NumericAngleAxisView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.NumericAxisBase.prototype.onViewCreated.call(this, view);
		this.numericAngleView(view);
	}
	,
	_numericAngleView: null,
	numericAngleView: function (value) {
		if (arguments.length === 1) {
			this._numericAngleView = value;
			return value;
		} else {
			return this._numericAngleView;
		}
	}
	,
	_renderingManager: null,
	renderingManager: function (value) {
		if (arguments.length === 1) {
			this._renderingManager = value;
			return value;
		} else {
			return this._renderingManager;
		}
	}
	,
	isAngular: function () {
		return true;
	}
	,
	init: function () {
		this.__preventReentry = false;
		this.__lastCrossing = NaN;
		this.__startAngleOffsetRadians = 0;
		$.ig.NumericAxisBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.NumericAngleAxis.prototype.$type);
		this.renderingManager(new $.ig.PolarAxisRenderingManager());
		this.renderer(this.createRenderer());
	},
	createLabelPanel: function () {
		var $self = this;
		var panel = new $.ig.AngleAxisLabelPanel();
		panel.getPoint(function (v) {
			var windowRect = $self.seriesViewer() != null ? $self.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
			var viewportRect = !windowRect.isEmpty() ? $self.viewportRect() : $.ig.Rect.prototype.empty();
			return $self.getLabelLocationPoint(v, { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, windowRect, viewportRect, $self.labelPanel().extent());
		});
		return panel;
	}
	,
	getCrossingValue: function () {
		if (this.radiusAxis() == null) {
			return 0;
		}
		if (!this.hasCrossingValue()) {
			return this.radiusAxis().getEffectiveMaximumLength();
		} else {
			return this.radiusAxis().getScaledValue3((this.crossingValue()));
		}
	}
	,
	round10: function (value) {
		return Math.round(value * Math.pow(10, 10)) / Math.pow(10, 10);
	}
	,
	__preventReentry: false,
	__lastCrossing: 0,
	createRenderer: function () {
		var $self = this;
		var renderer = $.ig.NumericAxisBase.prototype.createRenderer.call(this);
		renderer.labelManager().floatPanelAction(function (crossing) {
			if (($self.labelSettings() == null || $self.labelSettings().visibility() == $.ig.Visibility.prototype.visible) && $self.radiusAxis() != null && $self.__lastCrossing != crossing) {
				var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, $self.seriesViewer());
				if (dataChart == null) {
					return;
				}
				$self.__lastCrossing = crossing;
				$self.labelPanel().crossingValue(crossing);
				dataChart.invalidatePanels();
				var en = dataChart.axes().getEnumerator();
				while (en.moveNext()) {
					var axis = en.current();
					if (axis != $self && $.ig.util.cast($.ig.AngleAxisLabelPanel.prototype.$type, axis.labelPanel()) !== null) {
						axis.view().labelNeedRearrange();
					}
				}
			}
		});
		renderer.determineCrossingValue(function (p) {
			p.crossingValue($self.getCrossingValue());
			p.relativeCrossingValue(p.crossingValue());
		});
		renderer.axisLine(function (p) {
			var r = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			if (r.currentRangeInfo() == r.rangeInfos().__inner[0]) {
				$self.renderingManager().concentricLine(p.axisGeometry(), p.crossingValue(), p.viewportRect(), p.windowRect(), r.center(), r.minAngle(), r.maxAngle());
			}
		});
		renderer.line(function (p, g, value) {
			var r2 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self.renderingManager().radialLine(g, value, p.viewportRect(), p.windowRect(), r2.minLength(), r2.maxLength(), r2.center());
		});
		renderer.strip(function (p, g, start, end) {
			var r3 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self.renderingManager().radialStrip(g, start, end, r3.viewportRect(), p.windowRect(), r3.minLength(), r3.maxLength(), r3.center());
		});
		renderer.createRenderingParams(function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
			var r4 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, $self.createRenderingParams(viewportRect, windowRect, effectiveViewportRect, contentViewport));
			return r4;
		});
		renderer.onRendering(function () {
			if (!$self.__preventReentry) {
				$self.__preventReentry = true;
				$self.radiusAxis().updateRange();
				$self.__preventReentry = false;
			}
		});
		renderer.scaling(function (p, unscaled) {
			return $self.getScaledAngle(unscaled);
		});
		renderer.shouldRender(function (viewport, window) {
			return !window.isEmpty() && !viewport.isEmpty() && $self.radiusAxis() != null;
		});
		renderer.shouldRenderLines(function (p, value) {
			if ($self.round10(value - $self.__startAngleOffsetRadians) < 0) {
				return false;
			}
			if ($self.round10(value - $self.__startAngleOffsetRadians - (2 * Math.PI)) > 0) {
				return false;
			}
			return true;
		});
		renderer.shouldRenderLabel(function (p, value, last) {
			var r5 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			var endPoint = $self.getLabelLocationPoint($self.getScaledAngle(r5.actualMaximumValue()), r5.center(), p.windowRect(), p.viewportRect(), 0);
			var labelPoint = $self.getLabelLocationPoint(value, r5.center(), p.windowRect(), p.viewportRect(), 0);
			if (last && $.ig.MathUtil.prototype.hypot(endPoint.__x - labelPoint.__x, endPoint.__y - labelPoint.__y) < 2) {
				return false;
			}
			if (labelPoint.__x < p.viewportRect().right() && labelPoint.__x >= p.viewportRect().left() && labelPoint.__y < p.viewportRect().bottom() && labelPoint.__y >= p.viewportRect().top()) {
				return true;
			}
			return false;
		});
		renderer.snapMajorValue(function (p, value, i, interval) {
			if (value < p.actualMinimumValue() && $.ig.util.cast($.ig.LogarithmicTickmarkValues.prototype.$type, p.tickmarkValues()) !== null) {
				return p.actualMinimumValue();
			} else if (value > p.actualMaximumValue() && ($.ig.util.cast($.ig.LogarithmicTickmarkValues.prototype.$type, p.tickmarkValues()) !== null || p.hasUserMax())) {
				return p.actualMaximumValue();
			}
			return value;
		});
		return renderer;
	}
	,
	getLabelLocationPoint: function (angleValue, center, windowRect, viewportRect, extent) {
		var crossingValue = this.getCrossingValue();
		var extentValue = $.ig.ViewportUtils.prototype.transformXFromViewportLength(extent, windowRect, viewportRect);
		if (this.labelSettings() != null && (this.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom || this.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.outsideBottom)) {
			extentValue *= -1;
		}
		var x = center.__x + (crossingValue + extentValue) * Math.cos(angleValue);
		var y = center.__y + (crossingValue + extentValue) * Math.sin(angleValue);
		x = $.ig.ViewportUtils.prototype.transformXToViewport(x, windowRect, viewportRect);
		y = $.ig.ViewportUtils.prototype.transformYToViewport(y, windowRect, viewportRect);
		return { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getScaledValue: function (unscaledValue, p) {
		return this.getScaledAngle(unscaledValue);
	}
	,
	getScaledAngle1: function (unscaledValue, isLogarithmic, isInverted) {
		var scaledValue = 0;
		if (isLogarithmic) {
			scaledValue = (Math.log(unscaledValue) - this.logActualMinimumValue()) / (this.logActualMaximumValue() - this.logActualMinimumValue());
		} else {
			scaledValue = (unscaledValue - this.actualMinimumValue()) / (this.actualMaximumValue() - this.actualMinimumValue());
		}
		if (isInverted) {
			scaledValue = 1 - scaledValue;
		}
		return (scaledValue * 2 * Math.PI) + this.__startAngleOffsetRadians;
	}
	,
	getScaledAngle: function (unscaledValue) {
		return this.getScaledAngle1(unscaledValue, this.isReallyLogarithmic(), this.isInvertedCached());
	}
	,
	getUnscaledValue: function (scaledValue, p) {
		return this.getUnscaledAngle(scaledValue);
	}
	,
	getUnscaledAngle: function (scaledValue) {
		var unscaledValue = (scaledValue - this.__startAngleOffsetRadians) / (2 * Math.PI);
		if (this.isInverted()) {
			unscaledValue = 1 - unscaledValue;
		}
		if (this.isReallyLogarithmic()) {
			return Math.exp(unscaledValue * (this.logActualMaximumValue() - this.logActualMinimumValue()) + this.logActualMinimumValue());
		} else {
			return this.actualMinimumValue() + unscaledValue * (this.actualMaximumValue() - this.actualMinimumValue());
		}
	}
	,
	startAngleOffset: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.NumericAngleAxis.prototype.startAngleOffsetProperty, value);
			return value;
		} else {
			return this.getValue($.ig.NumericAngleAxis.prototype.startAngleOffsetProperty);
		}
	}
	,
	__startAngleOffsetRadians: 0,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.NumericAxisBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
		switch (propertyName) {
			case $.ig.Axis.prototype.crossingAxisPropertyName:
				var radiusAxis = $.ig.util.cast($.ig.NumericRadiusAxis.prototype.$type, newValue);
				this.onRadiusAxisChanged(radiusAxis);
				if (radiusAxis != null) {
					radiusAxis.onAngleAxisChanged(this);
				}
				this.renderAxis1(false);
				break;
			case $.ig.NumericAngleAxis.prototype.startAngleOffsetPropertyName:
				this.__startAngleOffsetRadians = this.startAngleOffset();
				while (this.__startAngleOffsetRadians < 0) {
					this.__startAngleOffsetRadians += 360;
				}
				while (this.__startAngleOffsetRadians >= 360) {
					this.__startAngleOffsetRadians -= 360;
				}
				this.__startAngleOffsetRadians = (this.startAngleOffset() * Math.PI) / 180;
				this.renderAxis1(false);
				var en = this.directSeries().getEnumerator();
				while (en.moveNext()) {
					var series = en.current();
					series.renderSeries(false);
					series.notifyThumbnailAppearanceChanged();
				}
				break;
			case $.ig.Axis.prototype.labelPropertyName:
				if (dataChart != null) {
					var en1 = dataChart.axes().getEnumerator();
					while (en1.moveNext()) {
						var axis = en1.current();
						axis.renderAxis();
					}
				}
				break;
			case $.ig.Axis.prototype.crossingValuePropertyName:
				if (dataChart != null) {
					var en2 = dataChart.axes().getEnumerator();
					while (en2.moveNext()) {
						var axis1 = en2.current();
						if ($.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, axis1) !== null || $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, axis1) !== null) {
							axis1.renderAxis();
						}
					}
				}
				break;
			case $.ig.Axis.prototype.labelSettingsPropertyName:
				this.renderer(this.createRenderer());
				this.forcePanelRefloat();
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				break;
		}
	}
	,
	forcePanelRefloat: function () {
		this.__lastCrossing = NaN;
	}
	,
	createRenderingParamsInstance: function () {
		return new $.ig.PolarAxisRenderingParameters();
	}
	,
	createRenderingParams: function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
		var renderingParams = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, $.ig.NumericAxisBase.prototype.createRenderingParams.call(this, viewportRect, windowRect, effectiveViewportRect, contentViewport));
		var closestRadius = this.renderingManager().getClosestRadiusValue(windowRect);
		var furthestRadius = this.renderingManager().getFurthestRadiusValue(windowRect);
		var maxRadius = 0.5 * this.radiusAxis().actualRadiusExtentScale();
		var minRadius = 0.5 * this.radiusAxis().actualInnerRadiusExtentScale();
		var minLen = closestRadius;
		var maxLen = furthestRadius;
		var effectiveMaximum = this.radiusAxis().getEffectiveMaximumLength();
		if ($.ig.util.isNaN(effectiveMaximum) || Number.isInfinity(effectiveMaximum)) {
			return null;
		}
		if (maxLen >= maxRadius) {
			maxLen = effectiveMaximum;
		}
		if (minLen < minRadius) {
			minLen = minRadius;
		}
		var resolution = viewportRect.width();
		this.renderingManager().determineView(windowRect, renderingParams, this.actualMinimumValue(), this.actualMaximumValue(), this.isInverted(), this.getUnscaledAngle.runOn(this), resolution);
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		renderingParams.center(center);
		renderingParams.maxLength(maxLen);
		renderingParams.minLength(minLen);
		renderingParams.effectiveMaximum(effectiveMaximum);
		return renderingParams;
	}
	,
	getMinMaxAngle: function (windowRect, minAngle, maxAngle) {
		var $ret = this.renderingManager().getMinMaxAngle(windowRect, minAngle, maxAngle);
		minAngle = $ret.p1;
		maxAngle = $ret.p2;
		return {
			p1: minAngle,
			p2: maxAngle
		};
	}
	,
	renderAxisOverride: function (animate) {
		$.ig.NumericAxisBase.prototype.renderAxisOverride.call(this, animate);
		var windowRect = this.seriesViewer() != null ? this.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = !windowRect.isEmpty() ? this.viewportRect() : $.ig.Rect.prototype.empty();
		var effectiveViewportRect = this.getCurrentEffectiveViewport();
		var contentViewport = this.getContentViewport();
		var renderParams = this.renderer().render(animate, viewportRect, windowRect, effectiveViewportRect, contentViewport);
		if (renderParams != null) {
			this.actualInterval(renderParams.tickmarkValues().interval());
			this.actualMinorInterval(renderParams.tickmarkValues().actualMinorInterval());
		}
	}
	,
	__radiusAxis: null,
	radiusAxis: function (value) {
		if (arguments.length === 1) {
			this.__radiusAxis = value;
			return value;
		} else {
			if (this.__radiusAxis != null) {
				return this.__radiusAxis;
			}
			var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
			if (dataChart != null) {
				return $.ig.Enumerable.prototype.firstOrDefault$1($.ig.NumericRadiusAxis.prototype.$type, $.ig.Enumerable.prototype.ofType$1($.ig.NumericRadiusAxis.prototype.$type, dataChart.axes()));
			}
			return this.__radiusAxis;
		}
	}
	,
	onRadiusAxisChanged: function (numericRadiusAxis) {
		this.radiusAxis(numericRadiusAxis);
	}
	,
	viewportChangedOverride: function (oldRect, newRect) {
		$.ig.NumericAxisBase.prototype.viewportChangedOverride.call(this, oldRect, newRect);
		if (newRect.height() != oldRect.height() || newRect.width() != oldRect.width()) {
			this.updateRange();
		}
	}
	,
	orientation: function () {
		return $.ig.AxisOrientation.prototype.angular;
	}
	,
	$type: new $.ig.Type('NumericAngleAxis', $.ig.NumericAxisBase.prototype.$type, [$.ig.IAngleScaler.prototype.$type])
}, true);

$.ig.util.defType('NumericRadiusAxis', 'NumericAxisBase', {
	createView: function () {
		return new $.ig.NumericRadiusAxisView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.NumericAxisBase.prototype.onViewCreated.call(this, view);
		this.numericRadiusView(view);
	}
	,
	_numericRadiusView: null,
	numericRadiusView: function (value) {
		if (arguments.length === 1) {
			this._numericRadiusView = value;
			return value;
		} else {
			return this._numericRadiusView;
		}
	}
	,
	isRadial: function () {
		return true;
	}
	,
	_renderingManager: null,
	init: function () {
		this.__lastCrossing = NaN;
		$.ig.NumericAxisBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.NumericRadiusAxis.prototype.$type);
		this.actualRadiusExtentScale(this.radiusExtentScale());
		this.actualInnerRadiusExtentScale(this.innerRadiusExtentScale());
		this._renderingManager = new $.ig.PolarAxisRenderingManager();
		this.renderer(this.createRenderer());
	},
	createLabelPanel: function () {
		return new $.ig.RadialAxisLabelPanel();
	}
	,
	_suppress: false,
	suppress: function (value) {
		if (arguments.length === 1) {
			this._suppress = value;
			return value;
		} else {
			return this._suppress;
		}
	}
	,
	convertToDouble: function (x) {
		if (x == null) {
			return 0;
		}
		return x;
	}
	,
	__lastCrossing: 0,
	createRenderer: function () {
		var $self = this;
		var renderer = $.ig.NumericAxisBase.prototype.createRenderer.call(this);
		renderer.labelManager().floatPanelAction(function (crossing) {
			if (($self.labelSettings() == null || $self.labelSettings().visibility() == $.ig.Visibility.prototype.visible) && $self.angleAxis() != null) {
				if (($self.labelSettings() == null || ($self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop || $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom)) && $self.__lastCrossing != crossing) {
					$self.__lastCrossing = crossing;
					$self.labelPanel().crossingValue(crossing);
					$self.seriesViewer().invalidatePanels();
				}
			}
		});
		renderer.line(function (p, g, value) {
			var r = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.concentricLine(g, value, r.viewportRect(), r.windowRect(), r.center(), r.minAngle(), r.maxAngle());
		});
		renderer.strip(function (p, g, start, end) {
			if (start == end) {
				return;
			}
			var r2 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.concentricStrip(g, start, end, r2.viewportRect(), r2.windowRect(), r2.center(), r2.minAngle(), r2.maxAngle());
		});
		renderer.scaling(function (p, unscaled) {
			return $self.getScaledValue3(unscaled);
		});
		renderer.shouldRenderLines(function (p, value) {
			var r3 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			return value <= r3.effectiveMaximum();
		});
		renderer.shouldRenderContent(function (p, value) {
			var r4 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			return value <= r4.effectiveMaximum();
		});
		renderer.axisLine(function (p) {
			var r5 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.radialLine(p.axisGeometry(), r5.crossingAngleRadians(), p.viewportRect(), p.windowRect(), r5.minLength(), r5.maxLength(), r5.center());
		});
		renderer.determineCrossingValue(function (p) {
			p.crossingValue($self.labelSettings() == null || ($self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop || $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.outsideTop) ? p.viewportRect().top() : p.viewportRect().bottom());
			p.relativeCrossingValue(p.crossingValue());
			var r6 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			r6.crossingAngleRadians(($self.convertToDouble($self.crossingValue()) * Math.PI) / 180);
			if ($self.labelSettings() == null || ($self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop || $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom)) {
				r6.crossingValue($.ig.ViewportUtils.prototype.transformYToViewport(0.5, r6.windowRect(), r6.viewportRect()) - p.viewportRect().top());
				r6.relativeCrossingValue(r6.crossingValue());
				var panel = $.ig.util.cast($.ig.RadialAxisLabelPanel.prototype.$type, $self.labelPanel());
				if (panel != null) {
					var yVal = 0;
					if ($self.labelSettings() != null && $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop) {
						yVal = 1;
					}
					panel.rotationCenter({ __x: $.ig.ViewportUtils.prototype.transformXToViewport(0.5, r6.windowRect(), r6.viewportRect()), __y: yVal, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					panel.crossingAngle(r6.crossingAngleRadians());
				}
			}
		});
		renderer.shouldRenderLabel(function (p, v, last) {
			var r7 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			if ($self.angleAxis() == null) {
				return false;
			}
			if (v > r7.effectiveMaximum()) {
				return false;
			}
			var embedded = false;
			embedded = $self.labelSettings() == null || ($self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop || $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom);
			var panelAngle = 0;
			if (embedded) {
				panelAngle = $self.crossingValueRadians();
			}
			var x = r7.center().__x + v * Math.cos(panelAngle);
			var y = r7.center().__y + v * Math.sin(panelAngle);
			x = $.ig.ViewportUtils.prototype.transformXToViewport(x, r7.windowRect(), r7.viewportRect());
			y = $.ig.ViewportUtils.prototype.transformYToViewport(y, r7.windowRect(), r7.viewportRect());
			if (x <= p.viewportRect().right() && x >= p.viewportRect().left() && ((y <= p.viewportRect().bottom() && y >= p.viewportRect().top()) || !embedded)) {
				return true;
			}
			return false;
		});
		renderer.getLabelLocation(function (p, value) {
			var r8 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			return new $.ig.LabelPosition($.ig.ViewportUtils.prototype.transformXToViewport(r8.center().__x + value, r8.windowRect(), r8.viewportRect()));
		});
		renderer.snapMajorValue(function (p, value, i, interval) {
			if (value < p.actualMinimumValue()) {
				return p.actualMinimumValue();
			} else if (value > p.actualMaximumValue()) {
				return p.actualMaximumValue();
			}
			return value;
		});
		return renderer;
	}
	,
	getScaledValue: function (unscaledValue, p) {
		return this.getScaledValue3(unscaledValue);
	}
	,
	getScaledValue4: function (unscaledValue, isLogarithmic, isInverted, radiusExtentScale, innerRadiusExtentScale) {
		var scaledValue = 0;
		if (isLogarithmic) {
			if (unscaledValue <= 0) {
				scaledValue = (Math.log(this.actualMinimumValue()) - this.logActualMinimumValue()) / (this.logActualMaximumValue() - this.logActualMinimumValue());
			} else {
				scaledValue = (Math.log(unscaledValue) - this.logActualMinimumValue()) / (this.logActualMaximumValue() - this.logActualMinimumValue());
			}
		} else {
			scaledValue = (unscaledValue - this.actualMinimumValue()) / (this.actualMaximumValue() - this.actualMinimumValue());
		}
		if (isInverted) {
			scaledValue = 1 - scaledValue;
		}
		scaledValue = innerRadiusExtentScale + (scaledValue * (radiusExtentScale - innerRadiusExtentScale));
		scaledValue /= 2;
		return scaledValue;
	}
	,
	getScaledValue3: function (unscaledValue) {
		return this.getScaledValue4(unscaledValue, this.isReallyLogarithmic(), this.isInvertedCached(), this.actualRadiusExtentScale(), this.actualInnerRadiusExtentScale());
	}
	,
	getUnscaledValue3: function (scaledValue) {
		var unscaledValue = scaledValue * 2;
		unscaledValue = (unscaledValue - this.actualInnerRadiusExtentScale()) / (this.actualRadiusExtentScale() - this.actualInnerRadiusExtentScale());
		if (this.isInverted()) {
			unscaledValue = 1 - unscaledValue;
		}
		if (this.isReallyLogarithmic()) {
			return Math.exp(unscaledValue * (this.logActualMaximumValue() - this.logActualMinimumValue()) + this.logActualMinimumValue());
		} else {
			return this.actualMinimumValue() + unscaledValue * (this.actualMaximumValue() - this.actualMinimumValue());
		}
	}
	,
	getUnscaledValue: function (scaledValue, p) {
		return this.getUnscaledValue3(scaledValue);
	}
	,
	radiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.NumericRadiusAxis.prototype.radiusExtentScaleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.NumericRadiusAxis.prototype.radiusExtentScaleProperty);
		}
	}
	,
	_actualRadiusExtentScale: 0,
	actualRadiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._actualRadiusExtentScale = value;
			return value;
		} else {
			return this._actualRadiusExtentScale;
		}
	}
	,
	innerRadiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.NumericRadiusAxis.prototype.innerRadiusExtentScaleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.NumericRadiusAxis.prototype.innerRadiusExtentScaleProperty);
		}
	}
	,
	_actualInnerRadiusExtentScale: 0,
	actualInnerRadiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._actualInnerRadiusExtentScale = value;
			return value;
		} else {
			return this._actualInnerRadiusExtentScale;
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.Axis.prototype.crossingValuePropertyName:
				this.crossingValueRadians(this.convertToDouble(this.crossingValue()) * Math.PI / 180);
				break;
		}
		$.ig.NumericAxisBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.NumericRadiusAxis.prototype.radiusExtentScalePropertyName:
				this.actualRadiusExtentScale(this.radiusExtentScale());
				if (this.actualRadiusExtentScale() < 0) {
					this.actualRadiusExtentScale(0.1);
				}
				if (this.actualRadiusExtentScale() > 1) {
					this.actualRadiusExtentScale(1);
				}
				if (this.actualInnerRadiusExtentScale() >= this.actualRadiusExtentScale()) {
					this.actualInnerRadiusExtentScale(this.actualRadiusExtentScale() - 0.01);
					if (this.actualInnerRadiusExtentScale() < 0) {
						this.actualInnerRadiusExtentScale(0);
						this.actualRadiusExtentScale(0.01);
					}
				}
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				if (this.angleAxis() != null) {
					this.angleAxis().renderAxis();
				}
				var en = this.directSeries().getEnumerator();
				while (en.moveNext()) {
					var s = en.current();
					s.renderSeries(false);
				}
				break;
			case $.ig.NumericRadiusAxis.prototype.innerRadiusExtentScalePropertyName:
				this.actualInnerRadiusExtentScale(this.innerRadiusExtentScale());
				if (this.actualInnerRadiusExtentScale() < 0) {
					this.actualInnerRadiusExtentScale(0.1);
				}
				if (this.actualInnerRadiusExtentScale() > 1) {
					this.actualInnerRadiusExtentScale(1);
				}
				if (this.actualInnerRadiusExtentScale() >= this.actualRadiusExtentScale()) {
					this.actualInnerRadiusExtentScale(this.actualRadiusExtentScale() - 0.01);
					if (this.actualInnerRadiusExtentScale() < 0) {
						this.actualInnerRadiusExtentScale(0);
						this.actualRadiusExtentScale(0.01);
					}
				}
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				if (this.angleAxis() != null) {
					this.angleAxis().renderAxis();
				}
				var en1 = this.directSeries().getEnumerator();
				while (en1.moveNext()) {
					var s1 = en1.current();
					s1.thumbnailDirty(true);
					s1.renderSeries(false);
				}
				if (this.seriesViewer() != null) {
					this.seriesViewer().notifyThumbnailAppearanceChanged();
				}
				break;
			case $.ig.Axis.prototype.crossingAxisPropertyName:
				var angleAxis = $.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, newValue);
				var catAxis = $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, newValue);
				if (angleAxis == null && catAxis == null) {
					this.onAngleAxisChanged(null);
				}
				if (angleAxis != null) {
					this.onAngleAxisChanged(angleAxis);
					angleAxis.onRadiusAxisChanged(this);
				}
				if (catAxis != null) {
					this.onAngleAxisChanged(catAxis);
					catAxis.onRadiusAxisChanged(this);
				}
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				break;
			case $.ig.Axis.prototype.isInvertedPropertyName:
				if (this.angleAxis() != null) {
					this.angleAxis().mustInvalidateLabels(true);
					this.angleAxis().renderAxis1(false);
				}
				break;
		}
	}
	,
	_crossingValueRadians: 0,
	crossingValueRadians: function (value) {
		if (arguments.length === 1) {
			this._crossingValueRadians = value;
			return value;
		} else {
			return this._crossingValueRadians;
		}
	}
	,
	onAngleAxisChanged: function (angleAxis) {
		this.angleAxis(angleAxis);
	}
	,
	__angleAxis: null,
	angleAxis: function (value) {
		if (arguments.length === 1) {
			this.__angleAxis = value;
			return value;
		} else {
			if (this.__angleAxis != null) {
				return this.__angleAxis;
			}
			var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
			if (dataChart != null) {
				for (var i = 0; i < dataChart.axes().count(); i++) {
					if ($.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, dataChart.axes().__inner[i]) !== null || $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, dataChart.axes().__inner[i]) !== null) {
						return dataChart.axes().__inner[i];
					}
				}
			}
			return this.__angleAxis;
		}
	}
	,
	createRenderingParamsInstance: function () {
		return new $.ig.PolarAxisRenderingParameters();
	}
	,
	createRenderingParams: function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
		var renderingParams = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, $.ig.NumericAxisBase.prototype.createRenderingParams.call(this, viewportRect, windowRect, effectiveViewportRect, contentViewport));
		var closestRadius = this._renderingManager.getClosestRadiusValue(windowRect);
		var furthestRadius = this._renderingManager.getFurthestRadiusValue(windowRect);
		var maxRadius = 0.5 * this.actualRadiusExtentScale();
		var minRadius = 0.5 * this.actualInnerRadiusExtentScale();
		var visibleMinimum, visibleMaximum;
		if ($.ig.Rect.prototype.l_op_Equality(windowRect, $.ig.SeriesViewer.prototype.standardRect)) {
			visibleMaximum = this.actualMaximumValue();
			visibleMinimum = this.actualMinimumValue();
		} else {
			visibleMaximum = Math.min(furthestRadius, maxRadius);
			visibleMinimum = this.getUnscaledValue3(closestRadius);
			visibleMaximum = this.getUnscaledValue3(visibleMaximum);
			var $ret = this.snapVisibleExtents(viewportRect, windowRect, visibleMinimum, visibleMaximum);
			visibleMinimum = $ret.p2;
			visibleMaximum = $ret.p3;
		}
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var minLen = closestRadius;
		var maxLen = furthestRadius;
		var resolution = Math.min(viewportRect.width(), viewportRect.height()) * (this.actualRadiusExtentScale() - this.actualInnerRadiusExtentScale()) / 2;
		renderingParams.center(center);
		var trueMaxLen = Math.max(maxLen, minLen);
		var trueMinLen = Math.min(minLen, maxLen);
		renderingParams.maxLength(trueMaxLen);
		renderingParams.minLength(trueMinLen);
		var trueVisibleMinimum = Math.min(visibleMinimum, visibleMaximum);
		var trueVisibleMaximum = Math.max(visibleMinimum, visibleMaximum);
		if (trueVisibleMinimum < this.actualMinimumValue()) {
			trueVisibleMinimum = this.actualMinimumValue();
		}
		if (trueVisibleMaximum > this.actualMaximumValue()) {
			trueVisibleMaximum = this.actualMaximumValue();
		}
		this.actualVisibleMinimumValue(trueVisibleMinimum);
		this.actualVisibleMaximumValue(trueVisibleMaximum);
		renderingParams.rangeInfos().add((function () {
			var $ret = new $.ig.RangeInfo();
			$ret.visibleMinimum(trueVisibleMinimum);
			$ret.visibleMaximum(trueVisibleMaximum);
			$ret.resolution(resolution);
			return $ret;
		}()));
		var angleAxis = $.ig.util.cast($.ig.IAngleScaler.prototype.$type, this.angleAxis());
		if (angleAxis != null) {
			var minAngle;
			var maxAngle;
			var $ret1 = angleAxis.getMinMaxAngle(windowRect, minAngle, maxAngle);
			minAngle = $ret1.p1;
			maxAngle = $ret1.p2;
			renderingParams.minAngle(Math.min(minAngle, maxAngle));
			renderingParams.maxAngle(Math.max(minAngle, maxAngle));
		}
		var effectiveMaximum = this.getEffectiveMaximumLength();
		if (maxLen >= maxRadius) {
			maxLen = effectiveMaximum;
		}
		if (minLen < minRadius) {
			minLen = minRadius;
		}
		renderingParams.minLength(minLen);
		renderingParams.maxLength(maxLen);
		renderingParams.effectiveMaximum(effectiveMaximum);
		renderingParams.tickmarkValues(this.actualTickmarkValues());
		var linearTicks = $.ig.util.cast($.ig.LinearTickmarkValues.prototype.$type, renderingParams.tickmarkValues());
		if (linearTicks != null) {
			linearTicks.minTicks(this.getMinTicks(center, renderingParams.minLength(), renderingParams.maxLength(), windowRect, viewportRect));
		}
		return renderingParams;
	}
	,
	snapVisibleExtents: function (viewportRect, windowRect, visibleMinimum, visibleMaximum) {
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var extent = 0;
		if (this.isInverted()) {
			extent = this.getScaledValue3(this.actualMinimumValue());
		} else {
			extent = this.getScaledValue3(this.actualMaximumValue());
		}
		var crossingValue = 0;
		if (this.crossingValue() != null) {
			crossingValue = this.crossingValueRadians();
		}
		var x = center.__x + extent * Math.cos(crossingValue);
		var y = center.__y + extent * Math.sin(crossingValue);
		center.__x = $.ig.ViewportUtils.prototype.transformXToViewport(center.__x, windowRect, viewportRect);
		center.__y = $.ig.ViewportUtils.prototype.transformYToViewport(center.__y, windowRect, viewportRect);
		x = $.ig.ViewportUtils.prototype.transformXToViewport(x, windowRect, viewportRect);
		y = $.ig.ViewportUtils.prototype.transformYToViewport(y, windowRect, viewportRect);
		if (x >= viewportRect.left() && x <= viewportRect.right() && y >= viewportRect.top() && y <= viewportRect.bottom()) {
			if (this.isInverted()) {
				visibleMaximum = this.actualMinimumValue();
			} else {
				visibleMaximum = this.actualMaximumValue();
			}
		}
		if (center.__x >= viewportRect.left() && center.__x <= viewportRect.right() && center.__y >= viewportRect.top() && center.__y <= viewportRect.bottom()) {
			if (this.isInverted()) {
				visibleMinimum = this.actualMaximumValue();
			} else {
				visibleMinimum = this.actualMinimumValue();
			}
		}
		return {
			p2: visibleMinimum,
			p3: visibleMaximum
		};
	}
	,
	getMinTicks: function (center, minLen, maxLen, windowRect, viewportRect) {
		var radViewportLength = $.ig.ViewportUtils.prototype.transformXToViewportLength(maxLen - minLen, windowRect, viewportRect);
		var viewportRatio = radViewportLength / Math.min(viewportRect.width(), viewportRect.height());
		if (viewportRatio > 0.7) {
			return 10;
		}
		return 5;
	}
	,
	getEffectiveMaximumLength: function () {
		var value = 0;
		if (!this.isInverted()) {
			value = this.getScaledValue3(this.actualMaximumValue());
		} else {
			value = this.getScaledValue3(this.actualMinimumValue());
		}
		return value;
	}
	,
	onRangeChanged: function (ea) {
		if (this.angleAxis() != null) {
			this.angleAxis().renderAxis();
		}
	}
	,
	renderAxisOverride: function (animate) {
		$.ig.NumericAxisBase.prototype.renderAxisOverride.call(this, animate);
		var windowRect = this.seriesViewer() != null ? this.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = !windowRect.isEmpty() ? this.viewportRect() : $.ig.Rect.prototype.empty();
		var effectiveViewportRect = this.getCurrentEffectiveViewport();
		var contentViewport = this.getContentViewport();
		var renderParams = this.renderer().render(animate, viewportRect, windowRect, effectiveViewportRect, contentViewport);
		if (renderParams != null) {
			this.actualInterval(renderParams.tickmarkValues().interval());
			this.actualMinorInterval(renderParams.tickmarkValues().actualMinorInterval());
		}
	}
	,
	viewportChangedOverride: function (oldRect, newRect) {
		$.ig.NumericAxisBase.prototype.viewportChangedOverride.call(this, oldRect, newRect);
		if (newRect.height() != oldRect.height() || newRect.width() != oldRect.width()) {
			this.updateRange();
		}
	}
	,
	defineClipRegion: function (geom, viewportRect, windowRect, effectiveViewportRect, contentViewport) {
		var renderingParams = $.ig.util.cast($.ig.IPolarRadialRenderingParameters.prototype.$type, this.createRenderingParams(viewportRect, windowRect, effectiveViewportRect, contentViewport));
		if (renderingParams == null) {
			return;
		}
		this._renderingManager.concentricStrip(geom.children(), renderingParams.minLength(), renderingParams.maxLength(), viewportRect, windowRect, renderingParams.center(), renderingParams.minAngle(), renderingParams.maxAngle());
	}
	,
	orientation: function () {
		return $.ig.AxisOrientation.prototype.radial;
	}
	,
	$type: new $.ig.Type('NumericRadiusAxis', $.ig.NumericAxisBase.prototype.$type)
}, true);

$.ig.util.defType('PolarAxisRenderingManager', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	drawEllipse: function (radius, center, minAngle, maxAngle, windowRect, viewportRect) {
		var radiusX = $.ig.ViewportUtils.prototype.transformXToViewportLength(radius, windowRect, viewportRect);
		var radiusY = $.ig.ViewportUtils.prototype.transformYToViewportLength(radius, windowRect, viewportRect);
		var centerX = $.ig.ViewportUtils.prototype.transformXToViewport(center.__x, windowRect, viewportRect);
		var centerY = $.ig.ViewportUtils.prototype.transformYToViewport(center.__y, windowRect, viewportRect);
		if (maxAngle - minAngle < Math.PI && maxAngle - minAngle > 0) {
			var startPoint = { __x: $.ig.ViewportUtils.prototype.transformXToViewport(center.__x + radius * Math.cos(minAngle), windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(center.__y + radius * Math.sin(minAngle), windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var endPoint = { __x: $.ig.ViewportUtils.prototype.transformXToViewport(center.__x + radius * Math.cos(maxAngle), windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(center.__y + radius * Math.sin(maxAngle), windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var pf = new $.ig.PathFigure();
			pf.__startPoint = startPoint;
			pf.__isClosed = false;
			pf.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.isLargeArc(false);
				$ret.point(endPoint);
				$ret.size(new $.ig.Size(1, radiusX, radiusY));
				$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
				return $ret;
			}()));
			return pf;
		} else {
			var pf1 = new $.ig.PathFigure();
			pf1.__startPoint = { __x: centerX, __y: centerY - radiusY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pf1.__isClosed = true;
			pf1.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.isLargeArc(false);
				$ret.point({ __x: centerX, __y: centerY + radiusY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.size(new $.ig.Size(1, radiusX, radiusY));
				$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
				return $ret;
			}()));
			pf1.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.isLargeArc(false);
				$ret.point({ __x: centerX, __y: centerY - radiusY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.size(new $.ig.Size(1, radiusX, radiusY));
				$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
				return $ret;
			}()));
			return pf1;
		}
	}
	,
	concentricStrip: function (geometry, radius0, radius1, viewportRect, windowRect, center, minAngle, maxAngle) {
		var minRadius = Math.min(radius0, radius1);
		var maxRadius = Math.max(radius0, radius1);
		var strip = new $.ig.PathGeometry();
		var innerFigure = null;
		var connector1 = null;
		var outerFigure = null;
		var connector2 = null;
		if (minRadius > 0) {
			innerFigure = this.drawEllipse(minRadius, center, minAngle, maxAngle, windowRect, viewportRect);
			if (maxAngle - minAngle < Math.PI && maxAngle - minAngle > 0) {
				var seg = $.ig.util.cast($.ig.ArcSegment.prototype.$type, innerFigure.__segments.__inner[0]);
				if (seg != null) {
					var startPoint = { __x: $.ig.ViewportUtils.prototype.transformXToViewport(center.__x + maxRadius * Math.cos(maxAngle), windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(center.__y + maxRadius * Math.sin(maxAngle), windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
					connector1 = (function () {
						var $ret = new $.ig.LineSegment(1);
						$ret.point(startPoint);
						return $ret;
					}());
				}
			}
		}
		if (maxRadius > 0) {
			outerFigure = this.drawEllipse(maxRadius, center, minAngle, maxAngle, windowRect, viewportRect);
		}
		if (minRadius > 0) {
			if (maxAngle - minAngle < Math.PI && maxAngle - minAngle > 0) {
				var swap = outerFigure.__startPoint;
				var seg1 = $.ig.util.cast($.ig.ArcSegment.prototype.$type, outerFigure.__segments.__inner[0]);
				if (seg1 != null) {
					outerFigure.__startPoint = seg1.point();
					seg1.point(swap);
					seg1.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
					var startPoint1 = { __x: $.ig.ViewportUtils.prototype.transformXToViewport(center.__x + minRadius * Math.cos(minAngle), windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(center.__y + minRadius * Math.sin(minAngle), windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
					connector2 = (function () {
						var $ret = new $.ig.LineSegment(1);
						$ret.point(startPoint1);
						return $ret;
					}());
				}
			}
		}
		if (connector1 != null && connector2 != null) {
			innerFigure.__segments.add(connector1);
			var seg2 = outerFigure.__segments.__inner[0];
			outerFigure.__segments.remove(seg2);
			innerFigure.__segments.add(seg2);
			innerFigure.__segments.add(connector2);
			innerFigure.__isClosed = true;
			strip.figures().add(innerFigure);
		} else {
			if (innerFigure != null) {
				strip.figures().add(innerFigure);
				this.reverseArcFigure(outerFigure);
			}
			if (outerFigure != null) {
				strip.figures().add(outerFigure);
			}
		}
		geometry.add(strip);
	}
	,
	reverseArcFigure: function (figure) {
		if (figure.__segments.count() > 1) {
			var seg1 = $.ig.util.cast($.ig.ArcSegment.prototype.$type, figure.__segments.__inner[0]);
			var seg2 = $.ig.util.cast($.ig.ArcSegment.prototype.$type, figure.__segments.__inner[1]);
			figure.__segments.__inner[0] = seg2;
			figure.__segments.__inner[1] = seg1;
			var startPoint = seg2.point();
			var seg2Point = figure.__startPoint;
			var seg1Point = seg1.point();
			figure.__startPoint = startPoint;
			seg2.point(seg1Point);
			seg1.point(seg2Point);
			seg1.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
			seg2.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
		} else {
			var swap = figure.__startPoint;
			var seg = $.ig.util.cast($.ig.ArcSegment.prototype.$type, figure.__segments.__inner[0]);
			if (seg != null) {
				figure.__startPoint = seg.point();
				seg.point(swap);
				seg.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
			}
		}
	}
	,
	concentricLine: function (geometry, radius, viewportRect, windowRect, center, startAngle, endAngle) {
		if (radius > 0) {
			var line = new $.ig.PathGeometry();
			line.figures().add(this.drawEllipse(radius, center, startAngle, endAngle, windowRect, viewportRect));
			geometry.add(line);
		}
	}
	,
	radialStrip: function (geometry, startAngle, endAngle, viewportRect, windowRect, minLength, maxLength, center) {
		var angleMin = Math.min(startAngle, endAngle);
		var angleMax = Math.max(startAngle, endAngle);
		var isLargeArc = false;
		if (angleMax - angleMin > Math.PI) {
			isLargeArc = true;
		}
		var cosAngleMin = Math.cos(angleMin);
		var sinAngleMin = Math.sin(angleMin);
		var startXmin = center.__x + cosAngleMin * minLength;
		var startYmin = center.__y + sinAngleMin * minLength;
		var endXmin = center.__x + cosAngleMin * maxLength;
		var endYmin = center.__y + sinAngleMin * maxLength;
		var cosAngleMax = Math.cos(angleMax);
		var sinAngleMax = Math.sin(angleMax);
		var startXmax = center.__x + cosAngleMax * minLength;
		var startYmax = center.__y + sinAngleMax * minLength;
		var endXmax = center.__x + cosAngleMax * maxLength;
		var endYmax = center.__y + sinAngleMax * maxLength;
		startXmin = $.ig.ViewportUtils.prototype.transformXToViewport(startXmin, windowRect, viewportRect);
		startYmin = $.ig.ViewportUtils.prototype.transformYToViewport(startYmin, windowRect, viewportRect);
		endXmin = $.ig.ViewportUtils.prototype.transformXToViewport(endXmin, windowRect, viewportRect);
		endYmin = $.ig.ViewportUtils.prototype.transformYToViewport(endYmin, windowRect, viewportRect);
		startXmax = $.ig.ViewportUtils.prototype.transformXToViewport(startXmax, windowRect, viewportRect);
		startYmax = $.ig.ViewportUtils.prototype.transformYToViewport(startYmax, windowRect, viewportRect);
		endXmax = $.ig.ViewportUtils.prototype.transformXToViewport(endXmax, windowRect, viewportRect);
		endYmax = $.ig.ViewportUtils.prototype.transformYToViewport(endYmax, windowRect, viewportRect);
		var pf = new $.ig.PathFigure();
		pf.__startPoint = { __x: startXmin, __y: startYmin, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		pf.__isClosed = true;
		pf.__segments.add((function () {
			var $ret = new $.ig.LineSegment(1);
			$ret.point({ __x: endXmin, __y: endYmin, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point({ __x: endXmax, __y: endYmax, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			$ret.size(new $.ig.Size(1, $.ig.ViewportUtils.prototype.transformXToViewportLength(maxLength, windowRect, viewportRect), $.ig.ViewportUtils.prototype.transformYToViewportLength(maxLength, windowRect, viewportRect)));
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.isLargeArc(isLargeArc);
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.LineSegment(1);
			$ret.point({ __x: startXmax, __y: startYmax, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point({ __x: startXmin, __y: startYmin, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			$ret.size(new $.ig.Size(1, $.ig.ViewportUtils.prototype.transformXToViewportLength(minLength, windowRect, viewportRect), $.ig.ViewportUtils.prototype.transformYToViewportLength(minLength, windowRect, viewportRect)));
			$ret.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
			$ret.isLargeArc(isLargeArc);
			return $ret;
		}()));
		var pg = new $.ig.PathGeometry();
		pg.figures().add(pf);
		geometry.add(pg);
	}
	,
	radialLine: function (geometry, angle, viewportRect, windowRect, minLength, maxLength, center) {
		var radialLine = new $.ig.LineGeometry();
		var cosX = Math.cos(angle);
		var sinX = Math.sin(angle);
		var startX = center.__x + cosX * minLength;
		var startY = center.__y + sinX * minLength;
		var endX = center.__x + cosX * maxLength;
		var endY = center.__y + sinX * maxLength;
		startX = $.ig.ViewportUtils.prototype.transformXToViewport(startX, windowRect, viewportRect);
		startY = $.ig.ViewportUtils.prototype.transformYToViewport(startY, windowRect, viewportRect);
		endX = $.ig.ViewportUtils.prototype.transformXToViewport(endX, windowRect, viewportRect);
		endY = $.ig.ViewportUtils.prototype.transformYToViewport(endY, windowRect, viewportRect);
		radialLine.startPoint({ __x: startX, __y: startY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		radialLine.endPoint({ __x: endX, __y: endY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		geometry.add(radialLine);
	}
	,
	inXBand: function (center, bounds) {
		return center.__x >= bounds.left() && center.__x <= bounds.right();
	}
	,
	inYBand: function (center, bounds) {
		return center.__y >= bounds.top() && center.__y <= bounds.bottom();
	}
	,
	closestCorner: function (center, bounds) {
		var dist1 = Math.sqrt(Math.pow(center.__x - bounds.left(), 2) + Math.pow(center.__y - bounds.top(), 2));
		var dist2 = Math.sqrt(Math.pow(center.__x - bounds.right(), 2) + Math.pow(center.__y - bounds.top(), 2));
		var dist3 = Math.sqrt(Math.pow(center.__x - bounds.right(), 2) + Math.pow(center.__y - bounds.bottom(), 2));
		var dist4 = Math.sqrt(Math.pow(center.__x - bounds.left(), 2) + Math.pow(center.__y - bounds.bottom(), 2));
		return Math.min(dist1, Math.min(dist2, Math.min(dist3, dist4)));
	}
	,
	furthestCorner: function (center, bounds) {
		var dist1 = Math.sqrt(Math.pow(center.__x - bounds.left(), 2) + Math.pow(center.__y - bounds.top(), 2));
		var dist2 = Math.sqrt(Math.pow(center.__x - bounds.right(), 2) + Math.pow(center.__y - bounds.top(), 2));
		var dist3 = Math.sqrt(Math.pow(center.__x - bounds.right(), 2) + Math.pow(center.__y - bounds.bottom(), 2));
		var dist4 = Math.sqrt(Math.pow(center.__x - bounds.left(), 2) + Math.pow(center.__y - bounds.bottom(), 2));
		return Math.max(dist1, Math.max(dist2, Math.max(dist3, dist4)));
	}
	,
	getClosestRadiusValue: function (windowRect) {
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (this.inXBand(center, windowRect) && this.inYBand(center, windowRect)) {
			return 0;
		}
		if (this.inXBand(center, windowRect)) {
			if (center.__y < windowRect.top()) {
				return windowRect.top() - center.__y;
			} else {
				return center.__y - windowRect.bottom();
			}
		}
		if (this.inYBand(center, windowRect)) {
			if (center.__x < windowRect.left()) {
				return windowRect.left() - center.__x;
			} else {
				return center.__x - windowRect.right();
			}
		}
		return this.closestCorner(center, windowRect);
	}
	,
	getFurthestRadiusValue: function (windowRect) {
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		return this.furthestCorner(center, windowRect);
	}
	,
	getAngleTo: function (center, toPoint) {
		var radius = Math.sqrt(Math.pow(toPoint.__x - center.__x, 2) + Math.pow(toPoint.__y - center.__y, 2));
		var angle = Math.acos((toPoint.__x - center.__x) / radius);
		if ((toPoint.__y - center.__y) < 0) {
			angle = (2 * Math.PI) - angle;
		}
		return angle;
	}
	,
	lineCheck: function (maxValueRadiusPoint, rectPoint) {
		return ((maxValueRadiusPoint.__y - 0.5) * rectPoint.__x) + ((0.5 - maxValueRadiusPoint.__x) * rectPoint.__y) + ((maxValueRadiusPoint.__x * 0.5) - (0.5 * maxValueRadiusPoint.__y));
	}
	,
	getMinMaxAngle: function (windowRect, minAngle, maxAngle) {
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (this.inXBand(center, windowRect) && this.inYBand(center, windowRect)) {
			minAngle = 0;
			maxAngle = Math.PI * 2;
			return {
				p1: minAngle,
				p2: maxAngle
			};
		}
		var angle1 = this.getAngleTo(center, { __x: windowRect.left(), __y: windowRect.top(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		var angle2 = this.getAngleTo(center, { __x: windowRect.right(), __y: windowRect.top(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		var angle3 = this.getAngleTo(center, { __x: windowRect.right(), __y: windowRect.bottom(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		var angle4 = this.getAngleTo(center, { __x: windowRect.left(), __y: windowRect.bottom(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		if (this.inYBand(center, windowRect) && windowRect.left() > center.__x) {
			minAngle = angle1;
			maxAngle = 2 * Math.PI + angle4;
			return {
				p1: minAngle,
				p2: maxAngle
			};
		}
		minAngle = Math.min(angle1, Math.min(angle2, Math.min(angle3, angle4)));
		maxAngle = Math.max(angle1, Math.max(angle2, Math.max(angle3, angle4)));
		return {
			p1: minAngle,
			p2: maxAngle
		};
	}
	,
	determineView: function (windowRect, renderingParams, actualMinimumValue, actualMaximumValue, isInverted, getUnscaledAngle, resolution) {
		var minAngle;
		var maxAngle;
		var $ret = this.getMinMaxAngle(windowRect, minAngle, maxAngle);
		minAngle = $ret.p1;
		maxAngle = $ret.p2;
		var trueMinAngle = Math.min(minAngle, maxAngle);
		var trueMaxAngle = Math.max(minAngle, maxAngle);
		if ($.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, renderingParams) !== null) {
			(renderingParams).minAngle(trueMinAngle);
			(renderingParams).maxAngle(trueMaxAngle);
		} else if ($.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, renderingParams) !== null) {
			(renderingParams).minAngle(trueMinAngle);
			(renderingParams).maxAngle(trueMaxAngle);
		}
		if (minAngle == 0 && maxAngle == Math.PI * 2) {
			var visibleMinimum = actualMinimumValue;
			var visibleMaximum = actualMaximumValue;
			var trueVisibleMinimum = Math.min(visibleMinimum, visibleMaximum);
			var trueVisibleMaximum = Math.max(visibleMinimum, visibleMaximum);
			renderingParams.rangeInfos().add((function () {
				var $ret = new $.ig.RangeInfo();
				$ret.visibleMinimum(trueVisibleMinimum);
				$ret.visibleMaximum(trueVisibleMaximum);
				$ret.resolution(resolution);
				return $ret;
			}()));
			return;
		} else {
			if (maxAngle > Math.PI * 2) {
				maxAngle = maxAngle - Math.PI * 2;
			}
			var visibleMinimum1 = getUnscaledAngle(minAngle);
			var visibleMaximum1 = getUnscaledAngle(maxAngle);
			if (visibleMinimum1 < actualMinimumValue || visibleMinimum1 > actualMaximumValue) {
				visibleMinimum1 = getUnscaledAngle(minAngle + Math.PI * 2);
			}
			if (visibleMaximum1 < actualMinimumValue || visibleMaximum1 > actualMaximumValue) {
				visibleMaximum1 = getUnscaledAngle(maxAngle + Math.PI * 2);
			}
			var trueVisibleMinimum1 = Math.min(visibleMinimum1, visibleMaximum1);
			var trueVisibleMaximum1 = Math.max(visibleMinimum1, visibleMaximum1);
			if ((!isInverted && visibleMinimum1 > visibleMaximum1) || (isInverted && visibleMinimum1 < visibleMaximum1)) {
				var range1 = (actualMaximumValue - trueVisibleMaximum1);
				var range2 = (trueVisibleMinimum1 - actualMinimumValue);
				renderingParams.rangeInfos().add((function () {
					var $ret = new $.ig.RangeInfo();
					$ret.visibleMinimum(trueVisibleMaximum1);
					$ret.visibleMaximum(actualMaximumValue);
					$ret.resolution((range1 / (range1 + range2)) * resolution);
					return $ret;
				}()));
				renderingParams.rangeInfos().add((function () {
					var $ret = new $.ig.RangeInfo();
					$ret.visibleMinimum(actualMinimumValue);
					$ret.visibleMaximum(trueVisibleMinimum1);
					$ret.resolution((range2 / (range1 + range2)) * resolution);
					return $ret;
				}()));
			} else {
				renderingParams.rangeInfos().add((function () {
					var $ret = new $.ig.RangeInfo();
					$ret.visibleMinimum(trueVisibleMinimum1);
					$ret.visibleMaximum(trueVisibleMaximum1);
					$ret.resolution(resolution);
					return $ret;
				}()));
			}
		}
	}
	,
	$type: new $.ig.Type('PolarAxisRenderingManager', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('IPolarRadialRenderingParameters', 'Object', {
	$type: new $.ig.Type('IPolarRadialRenderingParameters', null)
}, true);

$.ig.util.defType('PolarAxisRenderingParameters', 'NumericAxisRenderingParameters', {
	init: function () {
		$.ig.NumericAxisRenderingParameters.prototype.init.call(this);
	},
	_minLength: 0,
	minLength: function (value) {
		if (arguments.length === 1) {
			this._minLength = value;
			return value;
		} else {
			return this._minLength;
		}
	}
	,
	_maxLength: 0,
	maxLength: function (value) {
		if (arguments.length === 1) {
			this._maxLength = value;
			return value;
		} else {
			return this._maxLength;
		}
	}
	,
	_center: null,
	center: function (value) {
		if (arguments.length === 1) {
			this._center = value;
			return value;
		} else {
			return this._center;
		}
	}
	,
	_minAngle: 0,
	minAngle: function (value) {
		if (arguments.length === 1) {
			this._minAngle = value;
			return value;
		} else {
			return this._minAngle;
		}
	}
	,
	_maxAngle: 0,
	maxAngle: function (value) {
		if (arguments.length === 1) {
			this._maxAngle = value;
			return value;
		} else {
			return this._maxAngle;
		}
	}
	,
	_crossingAngleRadians: 0,
	crossingAngleRadians: function (value) {
		if (arguments.length === 1) {
			this._crossingAngleRadians = value;
			return value;
		} else {
			return this._crossingAngleRadians;
		}
	}
	,
	_effectiveMaximum: 0,
	effectiveMaximum: function (value) {
		if (arguments.length === 1) {
			this._effectiveMaximum = value;
			return value;
		} else {
			return this._effectiveMaximum;
		}
	}
	,
	$type: new $.ig.Type('PolarAxisRenderingParameters', $.ig.NumericAxisRenderingParameters.prototype.$type, [$.ig.IPolarRadialRenderingParameters.prototype.$type])
}, true);

$.ig.util.defType('RadialAxisRenderingParameters', 'CategoryAxisRenderingParameters', {
	init: function () {
		$.ig.CategoryAxisRenderingParameters.prototype.init.call(this);
	},
	_minLength: 0,
	minLength: function (value) {
		if (arguments.length === 1) {
			this._minLength = value;
			return value;
		} else {
			return this._minLength;
		}
	}
	,
	_maxLength: 0,
	maxLength: function (value) {
		if (arguments.length === 1) {
			this._maxLength = value;
			return value;
		} else {
			return this._maxLength;
		}
	}
	,
	_center: null,
	center: function (value) {
		if (arguments.length === 1) {
			this._center = value;
			return value;
		} else {
			return this._center;
		}
	}
	,
	_crossingAngleRadians: 0,
	crossingAngleRadians: function (value) {
		if (arguments.length === 1) {
			this._crossingAngleRadians = value;
			return value;
		} else {
			return this._crossingAngleRadians;
		}
	}
	,
	_minAngle: 0,
	minAngle: function (value) {
		if (arguments.length === 1) {
			this._minAngle = value;
			return value;
		} else {
			return this._minAngle;
		}
	}
	,
	_maxAngle: 0,
	maxAngle: function (value) {
		if (arguments.length === 1) {
			this._maxAngle = value;
			return value;
		} else {
			return this._maxAngle;
		}
	}
	,
	_effectiveMaximum: 0,
	effectiveMaximum: function (value) {
		if (arguments.length === 1) {
			this._effectiveMaximum = value;
			return value;
		} else {
			return this._effectiveMaximum;
		}
	}
	,
	$type: new $.ig.Type('RadialAxisRenderingParameters', $.ig.CategoryAxisRenderingParameters.prototype.$type, [$.ig.IPolarRadialRenderingParameters.prototype.$type])
}, true);

$.ig.util.defType('RadialAxes', 'Object', {
	_radiusAxis: null,
	radiusAxis: function (value) {
		if (arguments.length === 1) {
			this._radiusAxis = value;
			return value;
		} else {
			return this._radiusAxis;
		}
	}
	,
	_angleAxis: null,
	angleAxis: function (value) {
		if (arguments.length === 1) {
			this._angleAxis = value;
			return value;
		} else {
			return this._angleAxis;
		}
	}
	,
	init: function (radiusAxis, angleAxis) {
		this._center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		$.ig.Object.prototype.init.call(this);
		this.radiusAxis(radiusAxis);
		this.angleAxis(angleAxis);
	},
	_center: null,
	getXValue: function (angle, radius, windowRect, viewportRect) {
		var X = this._center.__x + (radius * Math.cos(angle));
		return $.ig.ViewportUtils.prototype.transformXToViewport(X, windowRect, viewportRect);
	}
	,
	getScaledPoints: function (points, angleColumn, radiusColumn, windowRect, viewportRect, cosStrategy, sinStrategy) {
		var count = Math.min(angleColumn != null ? angleColumn.count() : 0, radiusColumn != null ? radiusColumn.count() : 0);
		var fillInPlace = false;
		if (points.count() == count) {
			fillInPlace = true;
		}
		var scaledAngle;
		var scaledRadius;
		var cX = this._center.__x;
		var cY = this._center.__y;
		var X;
		var Y;
		for (var i = 0; i < count; i++) {
			scaledAngle = this.angleAxis().getScaledAngle(angleColumn.item(i));
			scaledRadius = this.radiusAxis().getScaledValue3(radiusColumn.item(i));
			X = cX + (scaledRadius * cosStrategy(i, scaledAngle));
			Y = cY + (scaledRadius * sinStrategy(i, scaledAngle));
			X = viewportRect.left() + viewportRect.width() * (X - windowRect.left()) / windowRect.width();
			Y = viewportRect.top() + viewportRect.height() * (Y - windowRect.top()) / windowRect.height();
			if (!fillInPlace) {
				points.add({ __x: X, __y: Y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			} else {
				points.item(i, { __x: X, __y: Y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		}
	}
	,
	getUnscaledValues: function (scaledXValue, scaledYValue, windowRect, viewportRect, unscaledAngle, unscaledRadius) {
		var X = $.ig.ViewportUtils.prototype.transformXFromViewport(scaledXValue, windowRect, viewportRect);
		var Y = $.ig.ViewportUtils.prototype.transformYFromViewport(scaledYValue, windowRect, viewportRect);
		var scaledRadius = Math.sqrt(Math.pow(X - this._center.__x, 2) + Math.pow(Y - this._center.__y, 2));
		var scaledAngle = Math.acos((X - this._center.__x) / scaledRadius);
		if ((Y - this._center.__y) < 0) {
			scaledAngle = (2 * Math.PI) - scaledAngle;
		}
		unscaledAngle = this.angleAxis().getUnscaledAngle(scaledAngle);
		unscaledRadius = this.radiusAxis().getUnscaledValue3(scaledRadius);
		return {
			p4: unscaledAngle,
			p5: unscaledRadius
		};
	}
	,
	getYValue: function (angle, radius, windowRect, viewportRect) {
		var Y = this._center.__y + (radius * Math.sin(angle));
		return $.ig.ViewportUtils.prototype.transformYToViewport(Y, windowRect, viewportRect);
	}
	,
	getAngleTo: function (world) {
		var radius = Math.sqrt(Math.pow(world.__x - this._center.__x, 2) + Math.pow(world.__y - this._center.__y, 2));
		var angle = Math.acos((world.__x - this._center.__x) / radius);
		if ((world.__y - this._center.__y) < 0) {
			angle = (2 * Math.PI) - angle;
		}
		return angle;
	}
	,
	$type: new $.ig.Type('RadialAxes', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RadialAxisLabelPanel', 'HorizontalAxisLabelPanelBase', {
	init: function () {
		this.__toHide = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		$.ig.HorizontalAxisLabelPanelBase.prototype.init.call(this);
	},
	createView: function () {
		return new $.ig.RadialAxisLabelPanelView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalAxisLabelPanelBase.prototype.onViewCreated.call(this, view);
		this.radialView(view);
	}
	,
	_radialView: null,
	radialView: function (value) {
		if (arguments.length === 1) {
			this._radialView = value;
			return value;
		} else {
			return this._radialView;
		}
	}
	,
	_rotationCenter: null,
	rotationCenter: function (value) {
		if (arguments.length === 1) {
			this._rotationCenter = value;
			return value;
		} else {
			return this._rotationCenter;
		}
	}
	,
	_crossingAngle: 0,
	crossingAngle: function (value) {
		if (arguments.length === 1) {
			this._crossingAngle = value;
			return value;
		} else {
			return this._crossingAngle;
		}
	}
	,
	axisIsNotEmbedded: function () {
		return this.axis().labelSettings() != null && (this.axis().labelSettings().actualLocation() != $.ig.AxisLabelsLocation.prototype.insideBottom && this.axis().labelSettings().actualLocation() != $.ig.AxisLabelsLocation.prototype.insideTop);
	}
	,
	axisIsEmbedded: function () {
		return !this.axisIsNotEmbedded();
	}
	,
	shouldTryStagger: function () {
		return this.foundCollisions() && (!this.useRotation() || this.getEffectiveAngle() == -180);
	}
	,
	shouldVerticalAlign: function () {
		return !this.useStaggering();
	}
	,
	applyPanelRotation: function (finalSize) {
		$.ig.HorizontalAxisLabelPanelBase.prototype.applyPanelRotation.call(this, finalSize);
		if (this.crossingAngle() % 360 == 0 || this.axisIsNotEmbedded()) {
			this.radialView().clearPanelRotation();
		} else {
			this.radialView().applyPanelRotation(finalSize);
		}
	}
	,
	getEffectiveAngle: function () {
		var angle = $.ig.HorizontalAxisLabelPanelBase.prototype.getEffectiveAngle.call(this);
		if (this.axisIsEmbedded()) {
			angle -= (this.crossingAngle() * 180) / Math.PI;
		}
		return angle;
	}
	,
	shouldRotate: function () {
		return this.getEffectiveAngle() % 360 != 0;
	}
	,
	shouldClip: function () {
		if (this.axisIsNotEmbedded()) {
			return true;
		}
		var angle = this.crossingAngle() * 180 / Math.PI;
		if (angle < 30 || angle > 330 || (angle > 150 && angle < 210)) {
			return true;
		}
		return false;
	}
	,
	__toHide: null,
	setLabelRotationTransform: function (label, angle) {
		var angleRadians = angle * Math.PI / 180;
		var yFactor = Math.abs(Math.sin(angleRadians));
		if (this.axis().labelSettings() != null && this.axis().labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop) {
			yFactor = yFactor * -1;
		}
		this.radialView().setLabelRotationalTransform(label, angle, yFactor);
	}
	,
	minimumPosition: function () {
		var min = 1.7976931348623157E+308;
		var en = this.labelPositions().getEnumerator();
		while (en.moveNext()) {
			var pos = en.current();
			min = Math.min(pos.value(), min);
		}
		return min;
	}
	,
	maximumPosition: function () {
		var max = -1.7976931348623157E+308;
		var en = this.labelPositions().getEnumerator();
		while (en.moveNext()) {
			var pos = en.current();
			max = Math.max(pos.value(), max);
		}
		return max;
	}
	,
	hideOptionalLabels: function (rectangles) {
		var $self = this;
		var val;
		if (this.axis().isInverted()) {
			val = this.minimumPosition();
		} else {
			val = this.maximumPosition();
		}
		var toHide = $.ig.Enumerable.prototype.select$2($.ig.LabelPosition.prototype.$type, $.ig.Number.prototype.$type, ($.ig.Enumerable.prototype.where$1($.ig.LabelPosition.prototype.$type, (this.labelPositions()), function (pos) { return pos.value() == val; })), function (pos) { return $self.labelPositions().indexOf(pos); });
		this.__toHide = $.ig.Enumerable.prototype.toList$1($.ig.Number.prototype.$type, toHide);
		this.foundCollisions(this.detectCollisions($.ig.Enumerable.prototype.toList$1($.ig.Rect.prototype.$type, $.ig.Enumerable.prototype.where$11($.ig.Rect.prototype.$type, rectangles, function (rect, index) { return !$self.__toHide.contains(index); }))));
	}
	,
	shouldDisplay: function (index, bounds) {
		if (this.__toHide.contains(index)) {
			return false;
		} else {
			return $.ig.HorizontalAxisLabelPanelBase.prototype.shouldDisplay.call(this, index, bounds);
		}
	}
	,
	showOptionalLabels: function () {
		this.__toHide = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		$.ig.HorizontalAxisLabelPanelBase.prototype.showOptionalLabels.call(this);
	}
	,
	getDefaultLabelsLocation: function () {
		return $.ig.AxisLabelsLocation.prototype.insideBottom;
	}
	,
	validLocation: function (location) {
		return location == $.ig.AxisLabelsLocation.prototype.insideBottom || location == $.ig.AxisLabelsLocation.prototype.insideTop || location == $.ig.AxisLabelsLocation.prototype.outsideBottom || location == $.ig.AxisLabelsLocation.prototype.outsideTop;
	}
	,
	createTicks: function () {
	}
	,
	$type: new $.ig.Type('RadialAxisLabelPanel', $.ig.HorizontalAxisLabelPanelBase.prototype.$type)
}, true);

$.ig.util.defType('CategoryTickmarkValues', 'TickmarkValues', {
	init: function () {
		this.__majorValues = null;
		$.ig.TickmarkValues.prototype.init.call(this);
	},
	_mode: 0,
	mode: function (value) {
		if (arguments.length === 1) {
			this._mode = value;
			return value;
		} else {
			return this._mode;
		}
	}
	,
	_mode2GroupCount: 0,
	mode2GroupCount: function (value) {
		if (arguments.length === 1) {
			this._mode2GroupCount = value;
			return value;
		} else {
			return this._mode2GroupCount;
		}
	}
	,
	_viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this._viewport = value;
			return value;
		} else {
			return this._viewport;
		}
	}
	,
	_window: null,
	window: function (value) {
		if (arguments.length === 1) {
			this._window = value;
			return value;
		} else {
			return this._window;
		}
	}
	,
	_isInverted: false,
	isInverted: function (value) {
		if (arguments.length === 1) {
			this._isInverted = value;
			return value;
		} else {
			return this._isInverted;
		}
	}
	,
	_getUnscaledGroupCenter: null,
	getUnscaledGroupCenter: function (value) {
		if (arguments.length === 1) {
			this._getUnscaledGroupCenter = value;
			return value;
		} else {
			return this._getUnscaledGroupCenter;
		}
	}
	,
	initialize: function (initializationParameters) {
		$.ig.TickmarkValues.prototype.initialize.call(this, initializationParameters);
		var mode = initializationParameters.mode();
		this.mode(mode);
		this.mode2GroupCount(initializationParameters.mode2GroupCount());
		this.viewport(initializationParameters.viewport());
		this.window(initializationParameters.window());
		this.isInverted(initializationParameters.isInverted());
		this.getUnscaledGroupCenter(initializationParameters.getUnscaledGroupCenter());
		var snapper = new $.ig.LinearCategorySnapper(1, initializationParameters.visibleMinimum(), initializationParameters.visibleMaximum(), initializationParameters.resolution(), initializationParameters.userInterval(), mode, initializationParameters.heuristicResolution());
		var interval = snapper.interval();
		if (initializationParameters.intervalOverride() != -1) {
			interval = initializationParameters.intervalOverride();
		}
		var firstValue = Math.floor((initializationParameters.visibleMinimum() - initializationParameters.actualMinimum()) / interval);
		var lastValue = Math.ceil((initializationParameters.visibleMaximum() - initializationParameters.actualMinimum()) / interval);
		var first = $.ig.truncate(firstValue);
		var last = $.ig.truncate(lastValue);
		var minorCount = $.ig.truncate(snapper.minorCount());
		if (initializationParameters.minorCountOverride() != -1) {
			minorCount = initializationParameters.minorCountOverride();
		}
		this.interval(interval);
		this.minorInterval(initializationParameters.minorInterval());
		this.firstIndex(first);
		this.lastIndex(last);
		this.minorCount(minorCount);
		this.actualMinimum(initializationParameters.actualMinimum());
	}
	,
	_actualMinimum: 0,
	actualMinimum: function (value) {
		if (arguments.length === 1) {
			this._actualMinimum = value;
			return value;
		} else {
			return this._actualMinimum;
		}
	}
	,
	__majorValues: null,
	majorValuesArray: function () {
		var firstIndex = this.firstIndex();
		var count = this.lastIndex() - firstIndex + 1;
		if (count < 0) {
			count = 0;
		}
		if (this.__majorValues == null || this.__majorValues.length != count) {
			this.__majorValues = new Array(count);
		}
		var array = this.__majorValues;
		for (var i = 0; i < count; ++i) {
			var major = this.actualMinimum() + (i + firstIndex) * this.interval();
			array[i] = major;
		}
		return array;
	}
	,
	__minorValues: null,
	minorValuesArray: function () {
		var interval = this.interval();
		var lastIndex = this.lastIndex();
		var firstIndex = this.firstIndex();
		var mode2GroupCount = this.mode2GroupCount();
		var mode = this.mode();
		interval = Math.min(interval, 20);
		var count = 0;
		for (var i = firstIndex; i < lastIndex; ++i) {
			if (mode != $.ig.CategoryMode.prototype.mode0 && mode2GroupCount != 0) {
				for (var categoryNumber = 0; categoryNumber < $.ig.truncate(interval); categoryNumber++) {
					for (var groupNumber = 0; groupNumber < mode2GroupCount; groupNumber++) {
						count++;
					}
				}
			}
		}
		if (this.__minorValues == null || this.__minorValues.length != count) {
			this.__minorValues = new Array(count);
		}
		var array = this.__minorValues;
		var pos = 0;
		for (var i1 = firstIndex; i1 < lastIndex; ++i1) {
			if (mode != $.ig.CategoryMode.prototype.mode0 && mode2GroupCount != 0) {
				for (var categoryNumber1 = 0; categoryNumber1 < $.ig.truncate(interval); categoryNumber1++) {
					for (var groupNumber1 = 0; groupNumber1 < mode2GroupCount; groupNumber1++) {
						var center = this.getUnscaledGroupCenter()(groupNumber1);
						var minorValue = categoryNumber1 + (i1 * interval) + center;
						array[pos] = minorValue;
						pos++;
					}
				}
			}
		}
		return this.__minorValues;
	}
	,
	$type: new $.ig.Type('CategoryTickmarkValues', $.ig.TickmarkValues.prototype.$type)
}, true);

$.ig.util.defType('ViewportUtils', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	transformXToViewport: function (xValue, windowRect, viewportRect) {
		return viewportRect.left() + viewportRect.width() * (xValue - windowRect.left()) / windowRect.width();
	}
	,
	transformXFromViewport: function (xValue, windowRect, viewportRect) {
		return ((xValue - viewportRect.left()) * windowRect.width() / viewportRect.width()) + windowRect.left();
	}
	,
	transformYToViewport: function (yValue, windowRect, viewportRect) {
		return viewportRect.top() + viewportRect.height() * (yValue - windowRect.top()) / windowRect.height();
	}
	,
	transformYFromViewport: function (yValue, windowRect, viewportRect) {
		return ((yValue - viewportRect.top()) * windowRect.height() / viewportRect.height()) + windowRect.top();
	}
	,
	transformXToViewportLength: function (xValue, windowRect, viewportRect) {
		return viewportRect.width() * xValue / windowRect.width();
	}
	,
	transformXFromViewportLength: function (xValue, windowRect, viewportRect) {
		return windowRect.width() * xValue / viewportRect.width();
	}
	,
	transformYToViewportLength: function (yValue, windowRect, viewportRect) {
		return viewportRect.height() * yValue / windowRect.height();
	}
	,
	$type: new $.ig.Type('ViewportUtils', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SeriesRenderingArguments', 'Object', {
	init: function (series, viewport, window, animate, skipPrepare) {
		$.ig.Object.prototype.init.call(this);
		this.transitionDuration(series.transitionDuration());
		this.container(series);
		this.viewport(viewport);
		this.window(window);
		this.animate(animate);
		this.skipPrepare(skipPrepare);
	},
	_viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this._viewport = value;
			return value;
		} else {
			return this._viewport;
		}
	}
	,
	_window: null,
	window: function (value) {
		if (arguments.length === 1) {
			this._window = value;
			return value;
		} else {
			return this._window;
		}
	}
	,
	_transitionDuration: 0,
	transitionDuration: function (value) {
		if (arguments.length === 1) {
			this._transitionDuration = value;
			return value;
		} else {
			return this._transitionDuration;
		}
	}
	,
	_animate: false,
	animate: function (value) {
		if (arguments.length === 1) {
			this._animate = value;
			return value;
		} else {
			return this._animate;
		}
	}
	,
	_container: null,
	container: function (value) {
		if (arguments.length === 1) {
			this._container = value;
			return value;
		} else {
			return this._container;
		}
	}
	,
	_skipPrepare: false,
	skipPrepare: function (value) {
		if (arguments.length === 1) {
			this._skipPrepare = value;
			return value;
		} else {
			return this._skipPrepare;
		}
	}
	,
	$type: new $.ig.Type('SeriesRenderingArguments', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SeriesRenderer$2', 'Object', {
	$tFrame: null,
	$tView: null,
	_prepareFrame: null,
	prepareFrame: function (value) {
		if (arguments.length === 1) {
			this._prepareFrame = value;
			return value;
		} else {
			return this._prepareFrame;
		}
	}
	,
	_renderFrame: null,
	renderFrame: function (value) {
		if (arguments.length === 1) {
			this._renderFrame = value;
			return value;
		} else {
			return this._renderFrame;
		}
	}
	,
	_calculateBuckets: null,
	calculateBuckets: function (value) {
		if (arguments.length === 1) {
			this._calculateBuckets = value;
			return value;
		} else {
			return this._calculateBuckets;
		}
	}
	,
	_startupAnimation: null,
	startupAnimation: function (value) {
		if (arguments.length === 1) {
			this._startupAnimation = value;
			return value;
		} else {
			return this._startupAnimation;
		}
	}
	,
	_animationActive: null,
	animationActive: function (value) {
		if (arguments.length === 1) {
			this._animationActive = value;
			return value;
		} else {
			return this._animationActive;
		}
	}
	,
	_checkFlush: null,
	checkFlush: function (value) {
		if (arguments.length === 1) {
			this._checkFlush = value;
			return value;
		} else {
			return this._checkFlush;
		}
	}
	,
	init: function ($tFrame, $tView, initNumber, prepare, render, animationActive, startupAnimation, checkFlush) {
		this.$tFrame = $tFrame;
		this.$tView = $tView;
		this.$type = this.$type.specialize(this.$tFrame, this.$tView);
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.Object.prototype.init.call(this);
		this.prepareFrame(prepare);
		this.renderFrame(render);
		this.calculateBuckets(function (f) {
		});
		this.animationActive(animationActive);
		this.startupAnimation(startupAnimation);
		this.checkFlush(checkFlush);
	},
	init1: function ($tFrame, $tView, initNumber, prepare, render, animationActive, startupAnimation, checkFlush, calculateBuckets) {
		this.$tFrame = $tFrame;
		this.$tView = $tView;
		this.$type = this.$type.specialize(this.$tFrame, this.$tView);
		$.ig.Object.prototype.init.call(this);
		this.prepareFrame(prepare);
		this.renderFrame(render);
		this.calculateBuckets(calculateBuckets);
		this.animationActive(animationActive);
		this.startupAnimation(startupAnimation);
		this.checkFlush(checkFlush);
	},
	render: function (arguments, previousFrame, currentFrame, transitionFrame, view) {
		var totalMilliseconds = 0;
		totalMilliseconds = arguments.transitionDuration();
		if (arguments.animate() && totalMilliseconds > 0 && !arguments.skipPrepare()) {
			var prevFrame = previousFrame;
			if (this.animationActive()()) {
				this.checkFlush()();
				previousFrame = transitionFrame;
				transitionFrame = prevFrame;
			} else {
				previousFrame = currentFrame;
				currentFrame = prevFrame;
			}
			this.calculateBuckets()(currentFrame);
			this.prepareFrame()(currentFrame, view);
			this.startupAnimation()();
		} else {
			if (!arguments.skipPrepare()) {
				this.calculateBuckets()(currentFrame);
				this.prepareFrame()(currentFrame, view);
			}
			this.renderFrame()(currentFrame, view);
		}
		return {
			p1: previousFrame,
			p2: currentFrame,
			p3: transitionFrame
		};
	}
	,
	$type: new $.ig.Type('SeriesRenderer$2', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RadialTrendLineManager', 'TrendLineManagerBase$1', {
	init: function () {
		$.ig.TrendLineManagerBase$1.prototype.init.call(this, Number);
	},
	prepareLine: function (flattenedPoints, trendLineType, valueColumn, period, GetScaledAngleValue, GetScaledRadiusValue, trendResolutionParams, clipper) {
		var xmin = trendResolutionParams.firstBucket() * trendResolutionParams.bucketSize();
		var xmax = trendResolutionParams.lastBucket() * trendResolutionParams.bucketSize();
		var trend = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		if (!trendResolutionParams.window().isEmpty() && !trendResolutionParams.viewport().isEmpty()) {
			if (trendLineType == $.ig.TrendLineType.prototype.none) {
				this.trendCoefficients(null);
				this.trendColumn().clear();
				return;
			}
			if (this.isFit(trendLineType)) {
				this.trendColumn().clear();
				this.trendCoefficients($.ig.TrendFitCalculator.prototype.calculateFit(trend, trendLineType, trendResolutionParams, this.trendCoefficients(), valueColumn.count(), function (i) { return (i + 1); }, function (i) { return valueColumn.item(i); }, function (x) { return GetScaledAngleValue(x - 1); }, GetScaledRadiusValue, xmin + 1, xmax + 1));
			}
			if (this.isAverage(trendLineType)) {
				this.trendCoefficients(null);
				$.ig.TrendAverageCalculator.prototype.calculateSingleValueAverage(trendLineType, this.trendColumn(), valueColumn, period);
				for (var i = trendResolutionParams.firstBucket(); i <= trendResolutionParams.lastBucket(); i += 1) {
					var itemIndex = (i % valueColumn.count()) * trendResolutionParams.bucketSize();
					if (itemIndex >= 0 && itemIndex < this.trendColumn().count()) {
						var xi = GetScaledAngleValue(itemIndex);
						var yi = GetScaledRadiusValue(this.trendColumn().__inner[itemIndex]);
						if (!$.ig.util.isNaN(xi) && !$.ig.util.isNaN(yi)) {
							trend.add({ __x: xi + trendResolutionParams.offset(), __y: yi, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						}
					}
				}
			}
			if (trend.count() > 0) {
				this.flattenTrendLine1(trend, trendResolutionParams, flattenedPoints, clipper);
			}
		}
	}
	,
	_radiusExtentScale: 0,
	radiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._radiusExtentScale = value;
			return value;
		} else {
			return this._radiusExtentScale;
		}
	}
	,
	_innerRadiusExtentScale: 0,
	innerRadiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._innerRadiusExtentScale = value;
			return value;
		} else {
			return this._innerRadiusExtentScale;
		}
	}
	,
	_projectX: null,
	projectX: function (value) {
		if (arguments.length === 1) {
			this._projectX = value;
			return value;
		} else {
			return this._projectX;
		}
	}
	,
	_projectY: null,
	projectY: function (value) {
		if (arguments.length === 1) {
			this._projectY = value;
			return value;
		} else {
			return this._projectY;
		}
	}
	,
	flattenTrendLine: function (trend, trendResolutionParams, flattenedPoints) {
		this.flattenTrendLine1(trend, trendResolutionParams, flattenedPoints, null);
	}
	,
	flattenTrendLine1: function (trend, trendResolutionParams, flattenedPoints, clipper) {
		var $self = this;
		if (clipper != null) {
			clipper.target(flattenedPoints);
		}
		var planner = (function () {
			var $ret = new $.ig.PolarLinePlanner();
			$ret.angleProvider(function (i) { return trend.item(i).__x; });
			$ret.radiusProvider(function (i2) { return trend.item(i2).__y; });
			$ret.clipper(clipper);
			$ret.count(trend.count());
			$ret.resolution(trendResolutionParams.resolution());
			$ret.transformedXProvider(function (i3) { return $self.projectX()(trend.item(i3).__x, trend.item(i3).__y); });
			$ret.transformedYProvider(function (i4) { return $self.projectY()(trend.item(i4).__x, trend.item(i4).__y); });
			$ret.useCartesianInterpolation(true);
			$ret.viewport(trendResolutionParams.viewport());
			$ret.window(trendResolutionParams.window());
			return $ret;
		}());
		planner.prepareLine1(null, null);
	}
	,
	$type: new $.ig.Type('RadialTrendLineManager', $.ig.TrendLineManagerBase$1.prototype.$type.specialize(Number))
}, true);

$.ig.util.defType('AngleRadiusPair', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this._index = value;
			return value;
		} else {
			return this._index;
		}
	}
	,
	_angle: 0,
	angle: function (value) {
		if (arguments.length === 1) {
			this._angle = value;
			return value;
		} else {
			return this._angle;
		}
	}
	,
	_radius: 0,
	radius: function (value) {
		if (arguments.length === 1) {
			this._radius = value;
			return value;
		} else {
			return this._radius;
		}
	}
	,
	$type: new $.ig.Type('AngleRadiusPair', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PolarLinePlanner', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_useCartesianInterpolation: false,
	useCartesianInterpolation: function (value) {
		if (arguments.length === 1) {
			this._useCartesianInterpolation = value;
			return value;
		} else {
			return this._useCartesianInterpolation;
		}
	}
	,
	_unknownValuePlotting: 0,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this._unknownValuePlotting = value;
			return value;
		} else {
			return this._unknownValuePlotting;
		}
	}
	,
	_angleProvider: null,
	angleProvider: function (value) {
		if (arguments.length === 1) {
			this._angleProvider = value;
			return value;
		} else {
			return this._angleProvider;
		}
	}
	,
	_radiusProvider: null,
	radiusProvider: function (value) {
		if (arguments.length === 1) {
			this._radiusProvider = value;
			return value;
		} else {
			return this._radiusProvider;
		}
	}
	,
	_transformedXProvider: null,
	transformedXProvider: function (value) {
		if (arguments.length === 1) {
			this._transformedXProvider = value;
			return value;
		} else {
			return this._transformedXProvider;
		}
	}
	,
	_transformedYProvider: null,
	transformedYProvider: function (value) {
		if (arguments.length === 1) {
			this._transformedYProvider = value;
			return value;
		} else {
			return this._transformedYProvider;
		}
	}
	,
	_resolution: 0,
	resolution: function (value) {
		if (arguments.length === 1) {
			this._resolution = value;
			return value;
		} else {
			return this._resolution;
		}
	}
	,
	_count: 0,
	count: function (value) {
		if (arguments.length === 1) {
			this._count = value;
			return value;
		} else {
			return this._count;
		}
	}
	,
	__viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this.__viewport = value;
			return value;
		} else {
			return this.__viewport;
		}
	}
	,
	__window: null,
	window: function (value) {
		if (arguments.length === 1) {
			this.__window = value;
			return value;
		} else {
			return this.__window;
		}
	}
	,
	_clipper: null,
	clipper: function (value) {
		if (arguments.length === 1) {
			this._clipper = value;
			return value;
		} else {
			return this._clipper;
		}
	}
	,
	_clippingDisabled: false,
	clippingDisabled: function (value) {
		if (arguments.length === 1) {
			this._clippingDisabled = value;
			return value;
		} else {
			return this._clippingDisabled;
		}
	}
	,
	_isClosed: false,
	isClosed: function (value) {
		if (arguments.length === 1) {
			this._isClosed = value;
			return value;
		} else {
			return this._isClosed;
		}
	}
	,
	_target: null,
	target: function (value) {
		if (arguments.length === 1) {
			this._target = value;
			return value;
		} else {
			return this._target;
		}
	}
	,
	valid: function () {
		if (this.angleProvider() == null || this.radiusProvider() == null || this.transformedXProvider() == null || this.transformedYProvider() == null || $.ig.Rect.prototype.l_op_Equality(this.viewport(), $.ig.Rect.prototype.empty()) || $.ig.Rect.prototype.l_op_Equality(this.window(), $.ig.Rect.prototype.empty())) {
			return false;
		}
		return true;
	}
	,
	measure: function (X, Y, a, b) {
		var x = X(b) - X(a);
		var y = Y(b) - Y(a);
		return x * x + y * y;
	}
	,
	reduce: function (viableIndices) {
		var list = new $.ig.List$1($.ig.AngleRadiusPair.prototype.$type, 0);
		var measure = this.resolution() * this.resolution();
		var indices;
		if (viableIndices != null) {
			indices = viableIndices;
		} else {
			indices = $.ig.Enumerable.prototype.range(0, this.count());
		}
		var indicesEnumerator = indices.getEnumerator();
		var notDone = true;
		notDone = indicesEnumerator.moveNext();
		var i = 0;
		if (notDone) {
			i = indicesEnumerator.current();
		}
		while (notDone) {
			var j = i;
			notDone = indicesEnumerator.moveNext();
			i = indicesEnumerator.current();
			while (notDone && this.measure(this.transformedXProvider(), this.transformedYProvider(), j, i) < measure) {
				notDone = indicesEnumerator.moveNext();
				i = indicesEnumerator.current();
			}
			var pair = new $.ig.AngleRadiusPair();
			pair.index(j);
			if (!this.useCartesianInterpolation()) {
				pair.angle(this.angleProvider()(j));
				pair.radius(this.radiusProvider()(j));
			}
			list.add(pair);
		}
		return list;
	}
	,
	prepareCartesian: function (viableIndices) {
		var en = this.reduce(viableIndices).getEnumerator();
		while (en.moveNext()) {
			var pair = en.current();
			if ($.ig.util.isNaN(pair.angle()) || Number.isInfinity(pair.angle()) || $.ig.util.isNaN(pair.radius()) || Number.isInfinity(pair.radius())) {
				this.addPoint({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				continue;
			}
			this.addPoint({ __x: this.transformedXProvider()(pair.index()), __y: this.transformedYProvider()(pair.index()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	getErrorTolerance: function () {
		return Math.pow(this.resolution() / (Math.max(this.viewport().width() / this.window().width(), this.viewport().height() / this.window().height())), 2);
	}
	,
	addFromPolar: function (angle, radius) {
		var x = 0.5 + radius * Math.cos(angle);
		var y = 0.5 + radius * Math.sin(angle);
		x = this.__viewport.left() + this.__viewport.width() * (x - this.__window.left()) / this.__window.width();
		y = this.__viewport.top() + this.__viewport.height() * (y - this.__window.top()) / this.__window.height();
		this.addPoint({ __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	preparePolar: function (viableIndices) {
		var error = this.getErrorTolerance();
		var pairs = this.reduce(viableIndices);
		var a0 = pairs.item(0).angle();
		var r0 = pairs.item(0).radius();
		var i0 = pairs.item(0).index();
		for (var i = 1; i < pairs.count(); i++) {
			var ai = pairs.item(i).angle();
			var ri = pairs.item(i).radius();
			var ii = pairs.item(i).index();
			if ($.ig.util.isNaN(ai) || Number.isInfinity(ai) || $.ig.util.isNaN(ri) || Number.isInfinity(ri)) {
				this.addPoint({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				if (this.unknownValuePlotting() != $.ig.UnknownValuePlotting.prototype.linearInterpolate) {
					i++;
					if (i < pairs.count()) {
						a0 = pairs.item(i).angle();
						r0 = pairs.item(i).radius();
					}
				}
				continue;
			}
			this.createSpiralPoints(ai, ri, a0, r0, i, error, i0 > ii);
			a0 = ai;
			r0 = ri;
			i0 = ii;
		}
	}
	,
	createSpiralPoints: function (ai, ri, a0, r0, index, error, wrapAround) {
		var swapped = false;
		if ((ai < a0 && !wrapAround) || (ai > a0 && wrapAround)) {
			swapped = true;
			var swap = ai;
			ai = a0;
			a0 = swap;
			swap = ri;
			ri = r0;
			r0 = swap;
		}
		var ps = $.ig.Flattener.prototype.spiral(a0, r0, ai, ri, error);
		if (swapped) {
			ps = $.ig.Enumerable.prototype.reverse$1(Number, ps);
		}
		var en = ps.getEnumerator();
		while (en.moveNext()) {
			var p = en.current();
			var a = a0 + p * (ai - a0);
			var r = r0 + p * (ri - r0);
			this.addFromPolar(a, r);
		}
	}
	,
	ensureClipper: function (target) {
		var $self = this;
		var top = this.viewport().top() - 10;
		var bottom = this.viewport().bottom() + 10;
		var left = this.viewport().left() - 10;
		var right = this.viewport().right() + 10;
		if (this.clipper() == null) {
			this.clipper((function () {
				var $ret = new $.ig.Clipper(1, left, bottom, right, top, $self.isClosed());
				$ret.target(target);
				return $ret;
			}()));
		}
	}
	,
	isPlottable: function (point) {
		return !$.ig.util.isNaN(point.__x) && !$.ig.util.isNaN(point.__y) && !Number.isInfinity(point.__x) && !Number.isInfinity(point.__y);
	}
	,
	addPoint: function (point) {
		if (this.unknownValuePlotting() == $.ig.UnknownValuePlotting.prototype.linearInterpolate && !this.isPlottable(point)) {
			return;
		}
		if (this.clippingDisabled()) {
			this.target().add(point);
		} else {
			this.clipper().add(point);
		}
	}
	,
	prepareLine: function (viableIndices) {
		this.prepareLine1(null, null);
	}
	,
	prepareLine1: function (target, viableIndices) {
		this.target(target);
		if (!this.valid()) {
			return;
		}
		if (this.count() > 1) {
			this.ensureClipper(target);
			if (this.useCartesianInterpolation()) {
				this.prepareCartesian(viableIndices);
			} else {
				this.preparePolar(viableIndices);
			}
			this.clipper().target(null);
		}
	}
	,
	$type: new $.ig.Type('PolarLinePlanner', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RadialBase', 'MarkerSeries', {
	onViewCreated: function (view) {
		$.ig.MarkerSeries.prototype.onViewCreated.call(this, view);
		this.radialView(view);
	}
	,
	_radialView: null,
	radialView: function (value) {
		if (arguments.length === 1) {
			this._radialView = value;
			return value;
		} else {
			return this._radialView;
		}
	}
	,
	init: function () {
		var $self = this;
		this._previousFrame = new $.ig.RadialFrame(3);
		this._transitionFrame = new $.ig.RadialFrame(3);
		this._currentFrame = new $.ig.RadialFrame(3);
		$.ig.MarkerSeries.prototype.init.call(this);
		this.seriesRenderer(new $.ig.SeriesRenderer$2($.ig.RadialFrame.prototype.$type, $.ig.RadialBaseView.prototype.$type, 1, this.prepareFrame.runOn(this), this.renderFrame.runOn(this), this.animationActive.runOn(this), this.startAnimation.runOn(this), this.checkFlush.runOn(this), function (f) { $self.radialView().bucketCalculator().calculateBuckets($self.resolution()); }));
	},
	checkFlush: function () {
		if (this.animator().needsFlush()) {
			this.animator().flush();
		}
	}
	,
	categoryAxis: function () {
		return this.angleAxis();
	}
	,
	_seriesRenderer: null,
	seriesRenderer: function (value) {
		if (arguments.length === 1) {
			this._seriesRenderer = value;
			return value;
		} else {
			return this._seriesRenderer;
		}
	}
	,
	isRadial: function () {
		return true;
	}
	,
	angleAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RadialBase.prototype.angleAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RadialBase.prototype.angleAxisProperty);
		}
	}
	,
	valueAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RadialBase.prototype.valueAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RadialBase.prototype.valueAxisProperty);
		}
	}
	,
	clipSeriesToBounds: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RadialBase.prototype.clipSeriesToBoundsProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RadialBase.prototype.clipSeriesToBoundsProperty);
		}
	}
	,
	windowRectChangedOverride: function (oldWindowRect, newWindowRect) {
		this.radialView().bucketCalculator().calculateBuckets(this.resolution());
		this.renderSeries(false);
	}
	,
	viewportRectChangedOverride: function (oldViewportRect, newViewportRect) {
		this.radialView().bucketCalculator().calculateBuckets(this.resolution());
		this.renderSeries(false);
	}
	,
	preferredCategoryMode: function (axis) {
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.MarkerSeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.Series.prototype.seriesViewerPropertyName:
				if (oldValue != null && newValue == null) {
					if (this.angleAxis() != null) {
						this.angleAxis().deregisterSeries(this);
					}
					if (this.valueAxis() != null) {
						this.valueAxis().deregisterSeries(this);
					}
				}
				if (oldValue == null && newValue != null) {
					if (this.angleAxis() != null) {
						this.angleAxis().registerSeries(this);
					}
					if (this.valueAxis() != null) {
						this.valueAxis().registerSeries(this);
					}
				}
				this.radialView().bucketCalculator().calculateBuckets(this.resolution());
				this.renderSeries(false);
				break;
			case $.ig.RadialBase.prototype.angleAxisPropertyName:
				if (this.angleAxis() != null && this.valueAxis() != null) {
					this.__axes = new $.ig.RadialAxes(this.valueAxis(), this.angleAxis());
				}
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				this.radialView().bucketCalculator().calculateBuckets(this.resolution());
				this.renderSeries(false);
				break;
			case $.ig.RadialBase.prototype.valueAxisPropertyName:
				if (this.angleAxis() != null && this.valueAxis() != null) {
					this.__axes = new $.ig.RadialAxes(this.valueAxis(), this.angleAxis());
				}
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				this.radialView().bucketCalculator().calculateBuckets(this.resolution());
				if (this.valueAxis() == null || !this.valueAxis().updateRange()) {
					this.renderSeries(false);
				}
				break;
			case $.ig.Series.prototype.syncLinkPropertyName:
				if (this.syncLink() != null && this.seriesViewer() != null) {
					this.radialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.Series.prototype.transitionProgressPropertyName:
				this._transitionFrame.interpolate3(this.transitionProgress(), this._previousFrame, this._currentFrame);
				if (this.clearAndAbortIfInvalid1(this.view())) {
					return;
				}
				if (this.transitionProgress() == 1) {
					this.renderFrame(this._currentFrame, this.radialView());
				} else {
					this.renderFrame(this._transitionFrame, this.radialView());
				}
				break;
			case $.ig.RadialBase.prototype.clipSeriesToBoundsPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.Series.prototype.visibilityProxyPropertyName:
				if ($.ig.util.getEnumValue(oldValue) != $.ig.Visibility.prototype.visible && $.ig.util.getEnumValue(newValue) == $.ig.Visibility.prototype.visible) {
					this.radialView().bucketCalculator().calculateBuckets(this.resolution());
				}
				break;
		}
	}
	,
	canUseAsValueAxis: function (axis) {
		if ($.ig.util.cast($.ig.NumericRadiusAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	canUseAsAngleAxis: function (axis) {
		if ($.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	getItem: function (world) {
		var index = this.getItemIndex(world);
		return index >= 0 && this.fastItemsSource() != null && index < this.fastItemsSource().count() ? this.fastItemsSource().item(index) : null;
	}
	,
	getAngleFromWorld: function (world) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		if (this.angleAxis() != null && !windowRect.isEmpty() && !viewportRect.isEmpty() && this.__axes != null) {
			var angle = this.__axes.getAngleTo(world);
			if (angle < 0) {
				angle += Math.PI * 2;
			}
			if (angle > Math.PI * 2) {
				angle -= Math.PI * 2;
			}
			return angle;
		}
		return NaN;
	}
	,
	getExactItemIndex: function (world) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var rowIndex = -1;
		if (this.angleAxis() != null && !windowRect.isEmpty() && !viewportRect.isEmpty() && this.__axes != null) {
			var angle = this.__axes.getAngleTo(world);
			if (angle < 0) {
				angle += Math.PI * 2;
			}
			if (angle > Math.PI * 2) {
				angle -= Math.PI * 2;
			}
			var unscaled = this.angleAxis().getUnscaledAngle(angle);
			if (this.angleAxis().categoryMode() != $.ig.CategoryMode.prototype.mode0) {
				unscaled -= 0.5;
			}
			rowIndex = unscaled;
		}
		return rowIndex;
	}
	,
	getItemIndex: function (world) {
		var index = $.ig.truncate(Math.round(this.getExactItemIndex(world)));
		if (this.fastItemsSource() != null && index == this.fastItemsSource().count()) {
			index = 0;
		}
		return index;
	}
	,
	scrollIntoView: function (item) {
		return false;
	}
	,
	_previousFrame: null,
	_transitionFrame: null,
	_currentFrame: null,
	prepareFrame: function (radialFrame, view) {
	}
	,
	renderFrame: function (radialFrame, view) {
	}
	,
	__axes: null,
	invalidateAxes: function () {
		$.ig.MarkerSeries.prototype.invalidateAxes.call(this);
		if (this.angleAxis() != null) {
			this.angleAxis().renderAxis1(false);
		}
		if (this.valueAxis() != null) {
			this.valueAxis().renderAxis1(false);
		}
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = true;
		var radialView = view;
		if (!$.ig.MarkerSeries.prototype.validateSeries.call(this, viewportRect, windowRect, view) || !view.hasSurface() || windowRect.isEmpty() || viewportRect.isEmpty() || this.angleAxis() == null || this.angleAxis().itemsSource() == null || this.valueAxis() == null || this.__axes == null || this.fastItemsSource() == null || this.angleAxis().seriesViewer() == null || this.valueAxis().seriesViewer() == null || this.valueAxis().actualMinimumValue() == this.valueAxis().actualMaximumValue()) {
			radialView.bucketCalculator().bucketSize(0);
			isValid = false;
		}
		return isValid;
	}
	,
	renderSeriesOverride: function (animate) {
		var windowRect;
		var viewportRect;
		var $ret = this.getViewInfo(viewportRect, windowRect);
		viewportRect = $ret.p0;
		windowRect = $ret.p1;
		if (!this.validateSeries(viewportRect, windowRect, this.view())) {
			this.clearRendering(true, this.view());
			return;
		}
		var args = new $.ig.SeriesRenderingArguments(this, viewportRect, windowRect, animate, this.skipPrepare());
		var $ret1 = this.seriesRenderer().render(args, this._previousFrame, this._currentFrame, this._transitionFrame, this.radialView());
		this._previousFrame = $ret1.p1;
		this._currentFrame = $ret1.p2;
		this._transitionFrame = $ret1.p3;
	}
	,
	getMode2Index: function () {
		var result = 0;
		var en = this.seriesViewer().series().getEnumerator();
		while (en.moveNext()) {
			var currentSeries = en.current();
			if (currentSeries == this) {
				return result;
			}
			var currentRadialSeries = $.ig.util.cast($.ig.RadialBase.prototype.$type, currentSeries);
			if (currentRadialSeries != null && currentRadialSeries.angleAxis() == this.angleAxis() && currentRadialSeries.preferredCategoryMode(currentRadialSeries.angleAxis()) == $.ig.CategoryMode.prototype.mode2) {
				result++;
			}
		}
		$.ig.Debug.prototype.assert1(false, "RadialBase.GetMode2Index failed to find series");
		return -1;
	}
	,
	$type: new $.ig.Type('RadialBase', $.ig.MarkerSeries.prototype.$type, [$.ig.IHasCategoryModePreference.prototype.$type])
}, true);

$.ig.util.defType('AnchoredRadialSeries', 'RadialBase', {
	createView: function () {
		return new $.ig.AnchoredRadialSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.RadialBase.prototype.onViewCreated.call(this, view);
		this.anchoredRadialView(view);
	}
	,
	_anchoredRadialView: null,
	anchoredRadialView: function (value) {
		if (arguments.length === 1) {
			this._anchoredRadialView = value;
			return value;
		} else {
			return this._anchoredRadialView;
		}
	}
	,
	init: function () {
		this._terminationPoint = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		$.ig.RadialBase.prototype.init.call(this);
		this.thumbnailFrame(new $.ig.RadialFrame(3));
		this.lineRasterizer(new $.ig.CategoryLineRasterizer());
	},
	_lineRasterizer: null,
	lineRasterizer: function (value) {
		if (arguments.length === 1) {
			this._lineRasterizer = value;
			return value;
		} else {
			return this._lineRasterizer;
		}
	}
	,
	getPreviousOrExactIndex: function (world, skipUnknowns) {
		return this.getPreviousOrExactIndexHelper(world, skipUnknowns, this.angleAxis(), null, this.valueColumn());
	}
	,
	getNextOrExactIndex: function (world, skipUnknowns) {
		return this.getNextOrExactIndexHelper(world, skipUnknowns, this.angleAxis(), null, this.valueColumn());
	}
	,
	getDistanceToIndex: function (world, index, axis, p, offset) {
		if (this.valueColumn() == null) {
			return Number.POSITIVE_INFINITY;
		}
		return this.getDistanceToIndexHelper(world, index, this.angleAxis(), p, offset, this.valueColumn().count(), null);
	}
	,
	getSeriesValue: function (world, useInterpolation, skipUnknowns) {
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var offset = this.getOffset(this.angleAxis(), this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
		var angleParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.angleAxis().isInverted(), effectiveViewportRect);
		angleParams._effectiveViewportRect = this.seriesViewer().viewportRect();
		return this.getSeriesValueHelper(this.valueColumn(), world, this.angleAxis(), angleParams, offset, null, useInterpolation, skipUnknowns);
	}
	,
	getSeriesValuePosition: function (world, useInterpolation, skipUnknowns) {
		if (this.valueAxis() == null || this.angleAxis() == null || this.view() == null || this.view().windowRect().isEmpty() || this.view().viewport().isEmpty()) {
			return { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var valueParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.valueAxis().isInverted(), effectiveViewportRect);
		valueParams._effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var value = this.getSeriesValue(world, useInterpolation, skipUnknowns);
		value = this.valueAxis().getScaledValue(value, valueParams);
		var angle = this.__axes.getAngleTo(world);
		if (!useInterpolation && this.angleAxis() != null) {
			var prevItem = this.getPreviousOrExactIndex(world, skipUnknowns);
			var nextItem = this.getNextOrExactIndex(world, skipUnknowns);
			var offset = this.getOffset(this.angleAxis(), this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
			var xParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.angleAxis().isInverted(), effectiveViewportRect);
			xParams._effectiveViewportRect = this.seriesViewer().viewportRect();
			var distToPrev = this.getDistanceToIndex(world, prevItem, this.angleAxis(), xParams, offset);
			var distToNext = this.getDistanceToIndex(world, nextItem, this.angleAxis(), xParams, offset);
			if (distToNext <= distToPrev) {
				angle = this.angleAxis().getScaledValue(nextItem, xParams) + offset;
			} else {
				angle = this.angleAxis().getScaledValue(prevItem, xParams) + offset;
			}
		}
		var xPos = this.__axes.getXValue(angle, value, this.view().windowRect(), this.view().viewport());
		var yPos = this.__axes.getYValue(angle, value, this.view().windowRect(), this.view().viewport());
		return { __x: xPos, __y: yPos, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	valueMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnchoredRadialSeries.prototype.valueMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.AnchoredRadialSeries.prototype.valueMemberPathProperty);
		}
	}
	,
	valueColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__valueColumn != value) {
				var oldValueColumn = this.__valueColumn;
				this.__valueColumn = value;
				this.raisePropertyChanged($.ig.AnchoredRadialSeries.prototype.valueColumnPropertyName, oldValueColumn, this.__valueColumn);
			}
			return value;
		} else {
			return this.__valueColumn;
		}
	}
	,
	__valueColumn: null,
	trendLineType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnchoredRadialSeries.prototype.trendLineTypeProperty, $.ig.TrendLineType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.AnchoredRadialSeries.prototype.trendLineTypeProperty));
		}
	}
	,
	trendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnchoredRadialSeries.prototype.trendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.AnchoredRadialSeries.prototype.trendLineBrushProperty);
		}
	}
	,
	actualTrendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnchoredRadialSeries.prototype.actualTrendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.AnchoredRadialSeries.prototype.actualTrendLineBrushProperty);
		}
	}
	,
	trendLineThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnchoredRadialSeries.prototype.trendLineThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.AnchoredRadialSeries.prototype.trendLineThicknessProperty);
		}
	}
	,
	trendLineDashCap: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnchoredRadialSeries.prototype.trendLineDashCapProperty, $.ig.PenLineCap.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.AnchoredRadialSeries.prototype.trendLineDashCapProperty));
		}
	}
	,
	trendLineDashArray: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnchoredRadialSeries.prototype.trendLineDashArrayProperty, value);
			return value;
		} else {
			return this.getValue($.ig.AnchoredRadialSeries.prototype.trendLineDashArrayProperty);
		}
	}
	,
	trendLinePeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnchoredRadialSeries.prototype.trendLinePeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.AnchoredRadialSeries.prototype.trendLinePeriodProperty));
		}
	}
	,
	trendLineZIndex: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnchoredRadialSeries.prototype.trendLineZIndexProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.AnchoredRadialSeries.prototype.trendLineZIndexProperty));
		}
	}
	,
	getRange: function (axis) {
		if (axis != null && axis == this.angleAxis() && this.valueColumn() != null && this.valueColumn().count() > 0) {
			return new $.ig.AxisRange(0, this.valueColumn().count() - 1);
		}
		if (axis != null && axis == this.valueAxis() && this.valueColumn() != null && this.valueColumn().count() > 0) {
			return new $.ig.AxisRange(this.valueColumn().minimum(), this.valueColumn().maximum());
		}
		return null;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.RadialBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		if (this.anchoredRadialView().trendLineManager().propertyUpdated(sender, propertyName, oldValue, newValue)) {
			this.renderSeries(false);
			this.notifyThumbnailAppearanceChanged();
		}
		var valueAxis = $.ig.util.cast($.ig.NumericAxisBase.prototype.$type, this.valueAxis());
		switch (propertyName) {
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue) !== null) {
					(oldValue).deregisterColumn(this.valueColumn());
					this.valueColumn(null);
				}
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue) !== null) {
					this.valueColumn(this.registerDoubleColumn(this.valueMemberPath()));
					this.anchoredRadialView().bucketCalculator().calculateBuckets(this.resolution());
				}
				if (valueAxis != null && !valueAxis.updateRange()) {
					this.anchoredRadialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.AnchoredRadialSeries.prototype.valueMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.valueColumn());
					this.valueColumn(this.registerDoubleColumn(this.valueMemberPath()));
				}
				break;
			case $.ig.AnchoredRadialSeries.prototype.valueColumnPropertyName:
				if (valueAxis != null && !valueAxis.updateRange()) {
					this.anchoredRadialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.Series.prototype.trendLineBrushPropertyName:
				this.updateIndexedProperties();
				break;
			case $.ig.Series.prototype.trendLineTypePropertyName:
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		switch (action) {
			case $.ig.FastItemsSourceEventAction.prototype.reset:
			case $.ig.FastItemsSourceEventAction.prototype.insert:
			case $.ig.FastItemsSourceEventAction.prototype.remove:
				this.anchoredRadialView().bucketCalculator().calculateBuckets(this.resolution());
				break;
		}
		this.anchoredRadialView().trendLineManager().dataUpdated(action, position, count, propertyName);
		switch (action) {
			case $.ig.FastItemsSourceEventAction.prototype.reset:
				if (this.valueAxis() != null && !this.valueAxis().updateRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.insert:
				if (this.valueAxis() != null && !this.valueAxis().updateRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.remove:
				if (this.valueAxis() != null && !this.valueAxis().updateRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.replace:
				if (this.valueMemberPath() != null && this.anchoredRadialView().bucketCalculator().bucketSize() > 0 && this.valueAxis() != null && !this.valueAxis().updateRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.change:
				if (propertyName == this.valueMemberPath()) {
					if (this.valueAxis() != null && !this.valueAxis().updateRange()) {
						this.renderSeries(true);
					}
				}
				break;
		}
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = $.ig.RadialBase.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		var anchoredView = view;
		if (this.fastItemsSource() == null || this.fastItemsSource().count() == 0 || this.angleAxis() == null || this.valueColumn() == null || this.angleAxis()._cachedItemsCount == 0 || this.valueAxis() == null || Number.isInfinity(this.valueAxis().actualMinimumValue()) || Number.isInfinity(this.valueAxis().actualMaximumValue()) || anchoredView.bucketCalculator().bucketSize() < 1) {
			isValid = false;
		}
		return isValid;
	}
	,
	getOffsetValue: function () {
		return this.getOffset(this.angleAxis(), this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	getCategoryWidth: function () {
		return this.angleAxis().getCategorySize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	isClosed: function () {
		return false;
	}
	,
	getOffset: function (angleAxis, windowRect, viewportRect, effectiveViewportRect) {
		var offset = 0;
		var categoryMode = this.preferredCategoryMode(angleAxis);
		if (categoryMode == $.ig.CategoryMode.prototype.mode0 && angleAxis.categoryMode() != $.ig.CategoryMode.prototype.mode0) {
			categoryMode = $.ig.CategoryMode.prototype.mode1;
		}
		switch (categoryMode) {
			case $.ig.CategoryMode.prototype.mode0:
				offset = 0;
				break;
			case $.ig.CategoryMode.prototype.mode1:
				offset = 0.5 * angleAxis.getCategorySize(windowRect, viewportRect, effectiveViewportRect);
				break;
			case $.ig.CategoryMode.prototype.mode2:
				offset = angleAxis.getGroupCenter(this.getMode2Index(), windowRect, viewportRect, effectiveViewportRect);
				break;
		}
		if (angleAxis.isInverted()) {
			offset = -offset;
		}
		return offset;
	}
	,
	prepareFrame: function (frame, view) {
		var $self = this;
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var angleAxis = this.angleAxis();
		var valueAxis = this.valueAxis();
		var minValue = valueAxis.actualMinimumValue();
		var maxValue = valueAxis.actualMaximumValue();
		frame._buckets.clear();
		frame._markers.clear();
		frame._trend.clear();
		var markers = this.shouldDisplayMarkers();
		var markerCount = 0;
		var anchoredRadialView = view;
		var collisionAvoider = new $.ig.CollisionAvoider();
		var offset = 0;
		var lastBucket = view.bucketCalculator().lastBucket();
		offset = this.getOffset(angleAxis, windowRect, viewportRect, effectiveViewportRect);
		anchoredRadialView.trendLineManager().radiusExtentScale(this.valueAxis().actualRadiusExtentScale());
		anchoredRadialView.trendLineManager().innerRadiusExtentScale(this.valueAxis().actualInnerRadiusExtentScale());
		anchoredRadialView.trendLineManager().projectX(function (angle, radius) { return $self.projectX(angle, radius, windowRect, viewportRect); });
		anchoredRadialView.trendLineManager().projectY(function (angle, radius) { return $self.projectY(angle, radius, windowRect, viewportRect); });
		var clipper = (function () {
			var $ret = new $.ig.Clipper(0, viewportRect, false);
			$ret.target(frame._trend);
			return $ret;
		}());
		var resParams = (function () {
			var $ret = new $.ig.TrendResolutionParams();
			$ret.bucketSize(view.bucketCalculator().bucketSize());
			$ret.firstBucket(view.bucketCalculator().firstBucket());
			$ret.lastBucket(lastBucket);
			$ret.offset(offset);
			$ret.resolution($self.resolution());
			$ret.viewport(viewportRect);
			$ret.window(windowRect);
			return $ret;
		}());
		anchoredRadialView.trendLineManager().prepareLine(frame._trend, this.trendLineType(), this.valueColumn(), this.trendLinePeriod(), function (a) { return $self.angleAxis().getScaledAngle(a); }, function (r) { return $self.valueAxis().getScaledValue3(r); }, resParams, clipper);
		var inNans = true;
		if (this.repeatExists(view)) {
			lastBucket--;
		}
		for (var i = view.bucketCalculator().firstBucket(); i <= lastBucket; ++i) {
			var index = i;
			if (index * view.bucketCalculator().bucketSize() >= this.angleAxis()._cachedItemsCount) {
				index -= $.ig.intDivide((this.angleAxis()._cachedItemsCount), view.bucketCalculator().bucketSize());
			}
			var bucket = view.bucketCalculator().getBucket(index);
			if (!isNaN(bucket[0])) {
				bucket[0] = (angleAxis.getScaledAngle(bucket[0]) + offset);
				if (bucket[1] < minValue || bucket[1] > maxValue) {
					continue;
				}
				bucket[1] = valueAxis.getScaledValue3(bucket[1]);
				if (view.bucketCalculator().bucketSize() > 1) {
					if (bucket[2] < minValue || bucket[2] > maxValue) {
						continue;
					}
					bucket[2] = valueAxis.getScaledValue3(bucket[2]);
				} else {
					bucket[2] = bucket[1];
				}
				if ((isNaN(bucket[1]) || isNaN(bucket[2])) && inNans && this.isClosed() && this.centerVisible()) {
					lastBucket++;
				} else {
					inNans = false;
				}
				frame._buckets.add(bucket);
				if (markers) {
					var j = Math.min(index * view.bucketCalculator().bucketSize(), this.fastItemsSource().count() - 1);
					var x = this.__axes.getXValue(bucket[0], bucket[1], windowRect, viewportRect);
					var y = this.__axes.getYValue(bucket[0], bucket[1], windowRect, viewportRect);
					var markerRect = new $.ig.Rect(0, x - 5, y - 5, 11, 11);
					if (!$.ig.util.isNaN(x) && !$.ig.util.isNaN(y) && !Number.isInfinity(x) && !Number.isInfinity(y) && collisionAvoider.tryAdd(markerRect)) {
						frame._markers.add({ __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						var marker = view.markers().item(markerCount);
						(marker.content()).item(this.fastItemsSource().item(j));
						++markerCount;
					}
				}
			} else {
				if (inNans && this.isClosed() && this.centerVisible()) {
					lastBucket++;
				}
			}
		}
		view.markers().count(markerCount);
		return;
	}
	,
	projectX: function (angle, radius, windowRect, viewportRect) {
		return this.__axes.getXValue(angle, radius, windowRect, viewportRect);
	}
	,
	projectY: function (angle, radius, windowRect, viewportRect) {
		return this.__axes.getYValue(angle, radius, windowRect, viewportRect);
	}
	,
	repeatExists: function (view) {
		var anchoredView = view;
		return !this.isClosed() && anchoredView.bucketCalculator().firstBucket() == 0 && anchoredView.bucketCalculator().lastBucket() == this.angleAxis()._cachedItemsCount;
	}
	,
	getLineClipper: function (buckets, endIndex, view) {
		var clipper = null;
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		if (endIndex > -1 && !windowRect.isEmpty() && !viewportRect.isEmpty()) {
			var left = buckets.__inner[0][0] < viewportRect.left() - 2000 ? viewportRect.left() - 10 : NaN;
			var bottom = viewportRect.bottom() + 10;
			var right = buckets.__inner[endIndex][0] > viewportRect.right() + 2000 ? viewportRect.right() + 10 : NaN;
			var top = viewportRect.top() - 10;
			clipper = new $.ig.Clipper(1, left, bottom, right, top, false);
		}
		return clipper;
	}
	,
	_terminationPoint: null,
	terminatePolygon: function (polygon0, line0, polygon01, line1, finished) {
		if (polygon0.count() > 0 && line1.count() > 0) {
			if (!finished || this.centerNotVisible()) {
				polygon0.add(this._terminationPoint);
				polygon0.add(polygon0.__inner[0]);
				line1.add(this._terminationPoint);
				line1.add(line1.__inner[0]);
			}
		}
	}
	,
	centerVisible: function () {
		return !this.centerNotVisible();
	}
	,
	centerNotVisible: function () {
		var window = this.view().windowRect();
		return !window.containsPoint({ __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.RadialBase.prototype.clearRendering.call(this, wipeClean, view);
		var radView = view;
		if (wipeClean) {
			radView.markers().clear();
		}
		radView.trendLineManager().clearPoints();
	}
	,
	renderFrame: function (frame, view) {
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var contentViewport = this.getContentViewport(view);
		var anchoredView = view;
		anchoredView.trendLineManager().rasterizeTrendLine(frame._trend);
		$.ig.CategoryMarkerManager.prototype.rasterizeMarkers(this, frame._markers, anchoredView.markers(), this.useLightweightMarkers());
		anchoredView.renderMarkers();
		this.applyClipping(viewportRect, windowRect, effectiveViewportRect, contentViewport, anchoredView);
	}
	,
	applyClipping: function (viewportRect, windowRect, effectiveViewportRect, contentViewport, view) {
		view.applyClipping(viewportRect, windowRect, effectiveViewportRect, contentViewport);
	}
	,
	updateIndexedProperties: function () {
		$.ig.RadialBase.prototype.updateIndexedProperties.call(this);
		if (this.index() < 0) {
			return;
		}
		this.anchoredRadialView().updateTrendlineBrush();
	}
	,
	item: function (sender, point) {
		if (sender == this.anchoredRadialView().trendLineManager().trendPolyline()) {
			return null;
		}
		return $.ig.RadialBase.prototype.item.call(this, sender, point);
	}
	,
	scrollIntoView: function (item) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var index = !windowRect.isEmpty() && !viewportRect.isEmpty() && this.fastItemsSource() != null ? this.fastItemsSource().indexOf(item) : -1;
		if (this.angleAxis() == null || this.valueColumn() == null || this.valueAxis() == null) {
			return false;
		}
		if (index < 0 || index > this.valueColumn().count() - 1) {
			return false;
		}
		var scaledAngle = this.angleAxis().getScaledAngle(index);
		var scaledRadius = this.valueAxis().getScaledValue3(this.valueColumn().item(index));
		if ($.ig.util.isNaN(scaledRadius)) {
			scaledRadius = (this.valueAxis().actualInnerRadiusExtentScale() + this.valueAxis().actualRadiusExtentScale()) / 2;
		}
		var cx = 0.5 + (Math.cos(scaledAngle) * scaledRadius);
		var cy = 0.5 + (Math.sin(scaledAngle) * scaledRadius);
		if (!$.ig.util.isNaN(cx)) {
			if (cx < windowRect.left() + 0.1 * windowRect.width()) {
				cx = cx + 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
			if (cx > windowRect.right() - 0.1 * windowRect.width()) {
				cx = cx - 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
		}
		if (!$.ig.util.isNaN(cy)) {
			if (cy < windowRect.top() + 0.1 * windowRect.height()) {
				cy = cy + 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
			if (cy > windowRect.bottom() - 0.1 * windowRect.height()) {
				cy = cy - 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
		}
		if (this.syncLink() != null) {
			this.syncLink().windowNotify(this.seriesViewer(), windowRect);
		}
		return index >= 0;
	}
	,
	_thumbnailFrame: null,
	thumbnailFrame: function (value) {
		if (arguments.length === 1) {
			this._thumbnailFrame = value;
			return value;
		} else {
			return this._thumbnailFrame;
		}
	}
	,
	renderThumbnail: function (viewportRect, surface) {
		$.ig.RadialBase.prototype.renderThumbnail.call(this, viewportRect, surface);
		if (!this.thumbnailDirty()) {
			this.view().prepSurface(surface);
			return;
		}
		this.view().prepSurface(surface);
		var thumbnailView = $.ig.util.cast($.ig.AnchoredRadialSeriesView.prototype.$type, this.thumbnailView());
		thumbnailView.bucketCalculator().calculateBuckets(this.resolution());
		if (this.clearAndAbortIfInvalid1(this.thumbnailView())) {
			return;
		}
		if (!this.skipThumbnailPrepare()) {
			this.thumbnailFrame(new $.ig.RadialFrame(3));
			this.prepareFrame(this.thumbnailFrame(), thumbnailView);
		}
		this.skipThumbnailPrepare(false);
		this.renderFrame(this.thumbnailFrame(), thumbnailView);
		this.thumbnailDirty(false);
	}
	,
	_alternateFrame: null,
	alternateFrame: function (value) {
		if (arguments.length === 1) {
			this._alternateFrame = value;
			return value;
		} else {
			return this._alternateFrame;
		}
	}
	,
	renderAlternateView: function (viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio) {
		$.ig.RadialBase.prototype.renderAlternateView.call(this, viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio);
		var view = this.alternateViews().item(viewIdentifier);
		var radialSeriesView = view;
		radialSeriesView.bucketCalculator().calculateBuckets(this.resolution());
		view.prepAltSurface(surface);
		if (this.clearAndAbortIfInvalid1(radialSeriesView)) {
			return;
		}
		if (this.alternateFrame() == null) {
			this.alternateFrame(new $.ig.RadialFrame(3));
		}
		this.alternateFrame().clearFrame();
		this.prepareFrame(this.alternateFrame(), radialSeriesView);
		this.renderFrame(this.alternateFrame(), radialSeriesView);
	}
	,
	exportVisualDataOverride: function (svd) {
		$.ig.RadialBase.prototype.exportVisualDataOverride.call(this, svd);
		var trendShape = new $.ig.PolyLineVisualData(1, "trendLine", this.anchoredRadialView().trendLineManager().trendPolyline());
		trendShape.tags().add("Trend");
		svd.shapes().add(trendShape);
	}
	,
	$type: new $.ig.Type('AnchoredRadialSeries', $.ig.RadialBase.prototype.$type)
}, true);

$.ig.util.defType('RadialBucketCalculator', 'Object', {
	_view: null,
	view: function (value) {
		if (arguments.length === 1) {
			this._view = value;
			return value;
		} else {
			return this._view;
		}
	}
	,
	init: function (view) {
		$.ig.Object.prototype.init.call(this);
		this.view(view);
	},
	_firstBucket: 0,
	firstBucket: function (value) {
		if (arguments.length === 1) {
			this._firstBucket = value;
			return value;
		} else {
			return this._firstBucket;
		}
	}
	,
	_lastBucket: 0,
	lastBucket: function (value) {
		if (arguments.length === 1) {
			this._lastBucket = value;
			return value;
		} else {
			return this._lastBucket;
		}
	}
	,
	_bucketSize: 0,
	bucketSize: function (value) {
		if (arguments.length === 1) {
			this._bucketSize = value;
			return value;
		} else {
			return this._bucketSize;
		}
	}
	,
	getBucket: function (index) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	getErrorBucket: function (index, column) {
		return NaN;
	}
	,
	getBucketInfo: function (firstBucket, lastBucket, bucketSize, resolution) {
		firstBucket = this.firstBucket();
		lastBucket = this.lastBucket();
		bucketSize = this.bucketSize();
		resolution = this.view().radialModel().resolution();
		return {
			p0: firstBucket,
			p1: lastBucket,
			p2: bucketSize,
			p3: resolution
		};
	}
	,
	calculateBuckets: function (resolution) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var angleAxis = this.view().radialModel().angleAxis();
		if (windowRect.isEmpty() || viewportRect.isEmpty() || angleAxis == null || this.view().radialModel().fastItemsSource() == null || this.view().radialModel().fastItemsSource().count() == 0) {
			this.bucketSize(0);
			return;
		}
		var x0 = Math.floor(angleAxis.getMinimumViewable(viewportRect, windowRect));
		var x1 = Math.ceil(angleAxis.getMaximumViewable(viewportRect, windowRect));
		if (angleAxis.isInverted()) {
			x1 = Math.ceil(angleAxis.getMinimumViewable(viewportRect, windowRect));
			x0 = Math.floor(angleAxis.getMaximumViewable(viewportRect, windowRect));
		}
		if (x1 < x0) {
			x1 = angleAxis._cachedItemsCount + x1;
		}
		var valueAxis = this.view().radialModel().valueAxis();
		var extentScale = valueAxis != null ? valueAxis.actualRadiusExtentScale() : 0.75;
		var circum = Math.min(viewportRect.width(), viewportRect.height()) * 0.5 * (extentScale) * 2 * Math.PI;
		var c = Math.floor((x1 - x0 + 1) * resolution / circum);
		this.bucketSize($.ig.truncate(Math.max(1, c)));
		this.firstBucket($.ig.truncate(Math.max(0, Math.floor(x0 / this.bucketSize()) - 1)));
		this.lastBucket($.ig.truncate(Math.ceil(x1 / this.bucketSize())));
	}
	,
	cacheValues: function () {
	}
	,
	unCacheValues: function () {
	}
	,
	$type: new $.ig.Type('RadialBucketCalculator', $.ig.Object.prototype.$type, [$.ig.IBucketizer.prototype.$type])
}, true);

$.ig.util.defType('AnchoredRadialBucketCalculator', 'RadialBucketCalculator', {
	_anchoredView: null,
	anchoredView: function (value) {
		if (arguments.length === 1) {
			this._anchoredView = value;
			return value;
		} else {
			return this._anchoredView;
		}
	}
	,
	init: function (view) {
		$.ig.RadialBucketCalculator.prototype.init.call(this, view);
		this.anchoredView(view);
	},
	getBucket: function (bucket) {
		var valueColumn = this.anchoredView().anchoredRadialModel().valueColumn();
		var i0 = Math.min(bucket * this.bucketSize(), valueColumn.count() - 1);
		var i1 = Math.min(i0 + this.bucketSize() - 1, valueColumn.count() - 1);
		var min = NaN;
		var max = NaN;
		for (var i = i0; i <= i1; ++i) {
			var y = valueColumn.item(i);
			if (!$.ig.util.isNaN(min)) {
				if (!$.ig.util.isNaN(y)) {
					min = Math.min(min, y);
					max = Math.max(max, y);
				}
			} else {
				min = y;
				max = y;
			}
		}
		if (!$.ig.util.isNaN(min)) {
			return [ (0.5 * (i0 + i1)), min, max ];
		}
		return [ (0.5 * (i0 + i1)), NaN, NaN ];
	}
	,
	$type: new $.ig.Type('AnchoredRadialBucketCalculator', $.ig.RadialBucketCalculator.prototype.$type)
}, true);

$.ig.util.defType('RadialAreaSeries', 'AnchoredRadialSeries', {
	createView: function () {
		return new $.ig.RadialAreaSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AnchoredRadialSeries.prototype.onViewCreated.call(this, view);
		this.radialAreaView(view);
	}
	,
	_radialAreaView: null,
	radialAreaView: function (value) {
		if (arguments.length === 1) {
			this._radialAreaView = value;
			return value;
		} else {
			return this._radialAreaView;
		}
	}
	,
	init: function () {
		$.ig.AnchoredRadialSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.RadialAreaSeries.prototype.$type);
	},
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode0;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.AnchoredRadialSeries.prototype.clearRendering.call(this, wipeClean, view);
		this.radialAreaView().clearRadialArea();
	}
	,
	renderFrame: function (frame, view) {
		$.ig.AnchoredRadialSeries.prototype.renderFrame.call(this, frame, view);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var areaView = view;
		if (view.hasSurface() && !windowRect.isEmpty() && !viewportRect.isEmpty() && this.angleAxis() != null && this.valueAxis() != null) {
			var buckets = frame._buckets;
			var count = buckets.count();
			var procesedBuckets = new $.ig.List$1(Array, 2, count);
			var radius = 0;
			for (var i = 0; i < count; i++) {
				var values = new Array(4);
				procesedBuckets.add(values);
				values[0] = this.__axes.getXValue(frame._buckets.__inner[i][0], frame._buckets.__inner[i][1], windowRect, viewportRect);
				values[1] = this.__axes.getYValue(frame._buckets.__inner[i][0], frame._buckets.__inner[i][1], windowRect, viewportRect);
				values[2] = this.__axes.getXValue(frame._buckets.__inner[i][0], frame._buckets.__inner[i][2], windowRect, viewportRect);
				var y1 = frame._buckets.__inner[i][2];
				values[3] = this.__axes.getYValue(frame._buckets.__inner[i][0], y1, windowRect, viewportRect);
				if (y1 > radius) {
					radius = y1;
				}
			}
			var centerX = $.ig.ViewportUtils.prototype.transformXToViewport(0.5, windowRect, viewportRect);
			var centerY = $.ig.ViewportUtils.prototype.transformYToViewport(0.5, windowRect, viewportRect);
			this._terminationPoint = { __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			areaView.rasterizeArea(frame._buckets.count(), procesedBuckets, false, this.radialAreaView().bucketCalculator().bucketSize(), this.resolution(), this.terminatePolygon.runOn(this), this.unknownValuePlotting());
			areaView._polygon0.__opacity = this.actualAreaFillOpacity();
			areaView._polygon1.__opacity = this.actualAreaFillOpacity() * 0.5;
		}
	}
	,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RadialAreaSeries.prototype.unknownValuePlottingProperty, $.ig.UnknownValuePlotting.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.RadialAreaSeries.prototype.unknownValuePlottingProperty));
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.AnchoredRadialSeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.RadialAreaSeries.prototype._unknownValuePlottingPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	isClosed: function () {
		return true;
	}
	,
	$type: new $.ig.Type('RadialAreaSeries', $.ig.AnchoredRadialSeries.prototype.$type)
}, true);

$.ig.util.defType('RadialFrame', 'CategoryFrame', {
	init: function (count) {
		$.ig.CategoryFrame.prototype.init.call(this, count);
	},
	$type: new $.ig.Type('RadialFrame', $.ig.CategoryFrame.prototype.$type)
}, true);

$.ig.util.defType('RadialColumnSeries', 'AnchoredRadialSeries', {
	createView: function () {
		return new $.ig.RadialColumnSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AnchoredRadialSeries.prototype.onViewCreated.call(this, view);
		this.radialColumnView(view);
	}
	,
	_radialColumnView: null,
	radialColumnView: function (value) {
		if (arguments.length === 1) {
			this._radialColumnView = value;
			return value;
		} else {
			return this._radialColumnView;
		}
	}
	,
	init: function () {
		$.ig.AnchoredRadialSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.RadialColumnSeries.prototype.$type);
	},
	onApplyTemplate: function () {
		$.ig.AnchoredRadialSeries.prototype.onApplyTemplate.call(this);
		this.renderSeries(false);
	}
	,
	radiusX: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RadialColumnSeries.prototype.radiusXProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RadialColumnSeries.prototype.radiusXProperty);
		}
	}
	,
	radiusY: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RadialColumnSeries.prototype.radiusYProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RadialColumnSeries.prototype.radiusYProperty);
		}
	}
	,
	preferredCategoryMode: function (axis) {
		return axis != null && axis == this.angleAxis() ? $.ig.CategoryMode.prototype.mode2 : $.ig.CategoryMode.prototype.mode0;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.AnchoredRadialSeries.prototype.clearRendering.call(this, wipeClean, view);
		var radialColumnView = view;
		if (wipeClean) {
			radialColumnView._columns.count(0);
		}
	}
	,
	getItemSpan: function () {
		return this.angleAxis().getGroupSize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	renderFrame: function (frame, view) {
		$.ig.AnchoredRadialSeries.prototype.renderFrame.call(this, frame, view);
		var buckets = frame._buckets;
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var contentViewport = this.getContentViewport(view);
		var p = this.valueAxis().createRenderingParams(viewportRect, windowRect, effectiveViewportRect, contentViewport);
		var columnView = view;
		var rscale = this.valueAxis();
		var refValue = Math.max(0, 0.5 * rscale.actualInnerRadiusExtentScale());
		refValue = Math.max(refValue, p.minLength() * 0.9);
		var zero = $.ig.ViewportUtils.prototype.transformXToViewportLength(refValue, windowRect, viewportRect);
		var groupWidthRadians = this.angleAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect);
		var widthAtRadius = Math.max(0.1 * this.valueAxis().actualRadiusExtentScale(), refValue);
		var x0 = this.__axes.getXValue(0, widthAtRadius, windowRect, viewportRect);
		var y0 = this.__axes.getYValue(0, widthAtRadius, windowRect, viewportRect);
		var x1 = this.__axes.getXValue(groupWidthRadians, widthAtRadius, windowRect, viewportRect);
		var y1 = this.__axes.getYValue(groupWidthRadians, widthAtRadius, windowRect, viewportRect);
		var groupWidth = Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
		var center = { __x: $.ig.ViewportUtils.prototype.transformXToViewport(0.5, windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(0.5, windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		for (var i = 0; i < buckets.count(); ++i) {
			var length = Math.min(frame._buckets.__inner[i][2], p.maxLength() * 1.1);
			var x = this.__axes.getXValue(frame._buckets.__inner[i][0], length, windowRect, viewportRect);
			var y = this.__axes.getYValue(frame._buckets.__inner[i][0], length, windowRect, viewportRect);
			var radius = Math.sqrt((x - center.__x) * (x - center.__x) + (y - center.__y) * (y - center.__y));
			var top = radius;
			var bottom = zero;
			var angle = frame._buckets.__inner[i][0] - (Math.PI / 2);
			var column = columnView._columns.item(i);
			var leftX = 0 - (groupWidth / 2);
			var rightX = (groupWidth / 2);
			var topY = Math.max(bottom, top);
			var bottomY = Math.min(bottom, top);
			var ca = Math.cos(angle);
			var sa = Math.sin(angle);
			var p1 = { __x: center.__x + (leftX * ca - topY * sa), __y: center.__y + (leftX * sa + topY * ca), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var p2 = { __x: center.__x + (rightX * ca - topY * sa), __y: center.__y + (rightX * sa + topY * ca), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var p3 = { __x: center.__x + (rightX * ca - bottomY * sa), __y: center.__y + (rightX * sa + bottomY * ca), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var p4 = { __x: center.__x + (leftX * ca - bottomY * sa), __y: center.__y + (leftX * sa + bottomY * ca), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var pg = new $.ig.PathGeometry();
			var pf = new $.ig.PathFigure();
			pf.__startPoint = p1;
			pf.__isClosed = true;
			var pl = new $.ig.PolyLineSegment();
			pl.__points.add(p2);
			pl.__points.add(p3);
			pl.__points.add(p4);
			pf.__segments.add(pl);
			pg.figures().add(pf);
			column.data(pg);
		}
		columnView._columns.count(buckets.count());
		this.radialColumnView().finalizeColumns();
	}
	,
	$type: new $.ig.Type('RadialColumnSeries', $.ig.AnchoredRadialSeries.prototype.$type)
}, true);

$.ig.util.defType('RadialLineSeries', 'AnchoredRadialSeries', {
	createView: function () {
		return new $.ig.RadialLineSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AnchoredRadialSeries.prototype.onViewCreated.call(this, view);
		this.radialLineView(view);
	}
	,
	_radialLineView: null,
	radialLineView: function (value) {
		if (arguments.length === 1) {
			this._radialLineView = value;
			return value;
		} else {
			return this._radialLineView;
		}
	}
	,
	init: function () {
		$.ig.AnchoredRadialSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.RadialLineSeries.prototype.$type);
	},
	onApplyTemplate: function () {
		$.ig.AnchoredRadialSeries.prototype.onApplyTemplate.call(this);
	}
	,
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode0;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.AnchoredRadialSeries.prototype.clearRendering.call(this, wipeClean, view);
		var radialLineView = view;
		radialLineView.clearRadialLine();
	}
	,
	renderFrame: function (frame, view) {
		$.ig.AnchoredRadialSeries.prototype.renderFrame.call(this, frame, view);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var buckets = frame._buckets;
		var count = buckets.count();
		var procesedBuckets = new $.ig.List$1(Array, 2, count);
		var radius = 0;
		for (var i = 0; i < count; i++) {
			var values = new Array(4);
			procesedBuckets.add(values);
			values[0] = this.__axes.getXValue(frame._buckets.__inner[i][0], frame._buckets.__inner[i][1], windowRect, viewportRect);
			values[1] = this.__axes.getYValue(frame._buckets.__inner[i][0], frame._buckets.__inner[i][1], windowRect, viewportRect);
			values[2] = this.__axes.getXValue(frame._buckets.__inner[i][0], frame._buckets.__inner[i][2], windowRect, viewportRect);
			var y1 = frame._buckets.__inner[i][2];
			values[3] = this.__axes.getYValue(frame._buckets.__inner[i][0], y1, windowRect, viewportRect);
			if (y1 > radius) {
				radius = y1;
			}
		}
		var lineView = view;
		lineView.renderRadialLine(count, procesedBuckets, false, this.unknownValuePlotting(), this.getLineClipper(procesedBuckets, count - 1, view), lineView.bucketCalculator().bucketSize(), this.resolution());
	}
	,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RadialLineSeries.prototype.unknownValuePlottingProperty, $.ig.UnknownValuePlotting.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.RadialLineSeries.prototype.unknownValuePlottingProperty));
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.AnchoredRadialSeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.RadialLineSeries.prototype._unknownValuePlottingPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	isClosed: function () {
		return true;
	}
	,
	$type: new $.ig.Type('RadialLineSeries', $.ig.AnchoredRadialSeries.prototype.$type)
}, true);

$.ig.util.defType('RadialPieSeries', 'AnchoredRadialSeries', {
	createView: function () {
		return new $.ig.RadialPieSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AnchoredRadialSeries.prototype.onViewCreated.call(this, view);
		this.radialPieView(view);
	}
	,
	_radialPieView: null,
	radialPieView: function (value) {
		if (arguments.length === 1) {
			this._radialPieView = value;
			return value;
		} else {
			return this._radialPieView;
		}
	}
	,
	init: function () {
		$.ig.AnchoredRadialSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.RadialPieSeries.prototype.$type);
	},
	radiusX: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RadialPieSeries.prototype.radiusXProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RadialPieSeries.prototype.radiusXProperty);
		}
	}
	,
	radiusY: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RadialPieSeries.prototype.radiusYProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RadialPieSeries.prototype.radiusYProperty);
		}
	}
	,
	preferredCategoryMode: function (axis) {
		return axis != null && axis == this.angleAxis() ? $.ig.CategoryMode.prototype.mode2 : $.ig.CategoryMode.prototype.mode0;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.AnchoredRadialSeries.prototype.clearRendering.call(this, wipeClean, view);
		var radialPieView = view;
		if (wipeClean && radialPieView._slices != null) {
			radialPieView._slices.count(0);
		}
	}
	,
	getItemSpan: function () {
		return this.angleAxis().getGroupSize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	renderFrame: function (frame, view) {
		$.ig.AnchoredRadialSeries.prototype.renderFrame.call(this, frame, view);
		var pieView = view;
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var contentViewport = this.getContentViewport(view);
		var p = this.valueAxis().createRenderingParams(viewportRect, windowRect, effectiveViewportRect, contentViewport);
		var buckets = frame._buckets;
		var rscale = this.valueAxis();
		var refValue = Math.max(0, 0.5 * rscale.actualInnerRadiusExtentScale());
		var zero = refValue;
		zero = Math.max(zero, p.minLength());
		var groupWidthRadians = this.angleAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect);
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var radiusX = this.radiusX();
		var radiusY = this.radiusY();
		var doRounded = (radiusX > 0 && radiusY > 0);
		var radius = 0;
		for (var i = 0; i < buckets.count(); ++i) {
			var slice = pieView._slices.item(i);
			var angleRadians = buckets.__inner[i][0];
			var length = Math.min(buckets.__inner[i][2], p.maxLength());
			var pg = null;
			if (doRounded) {
				pg = this.drawRoundedSlice(windowRect, viewportRect, angleRadians - (groupWidthRadians * 0.5), angleRadians + (groupWidthRadians * 0.5), zero, length, center, radiusX, radiusY);
			} else {
				pg = this.drawSlice(windowRect, viewportRect, angleRadians - (groupWidthRadians * 0.5), angleRadians + (groupWidthRadians * 0.5), zero, length, center);
			}
			slice.data(pg);
			var sliceRadius = buckets.__inner[i][2];
			if (sliceRadius > radius) {
				radius = sliceRadius;
			}
		}
		pieView.radius($.ig.ViewportUtils.prototype.transformXToViewportLength(radius, windowRect, viewportRect));
		pieView.center({ __x: $.ig.ViewportUtils.prototype.transformXToViewport(center.__x, windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(center.__y, windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		pieView._slices.count(buckets.count());
		pieView.slicesUpdated();
	}
	,
	drawSlice: function (windowRect, viewportRect, startAngle, endAngle, zero, radius, center) {
		var sc = $.ig.SliceCoords.prototype.getSliceCoords(windowRect, viewportRect, startAngle, endAngle, zero, radius, center);
		var pf = new $.ig.PathFigure();
		pf.__startPoint = sc.a();
		pf.__isClosed = true;
		pf.__segments.add((function () {
			var $ret = new $.ig.LineSegment(1);
			$ret.point(sc.b());
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point(sc.c());
			$ret.size(sc.outerSize());
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.isLargeArc(sc.isLargeArc());
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.LineSegment(1);
			$ret.point(sc.d());
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point(sc.a());
			$ret.size(sc.innerSize());
			$ret.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
			$ret.isLargeArc(sc.isLargeArc());
			return $ret;
		}()));
		var pg = new $.ig.PathGeometry();
		pg.figures().add(pf);
		return pg;
	}
	,
	drawRoundedSlice: function (windowRect, viewportRect, startAngle, endAngle, zero, radius, center, radiusX, radiusY) {
		var sc = $.ig.SliceCoords.prototype.getRoundedSliceCoords(windowRect, viewportRect, startAngle, endAngle, zero, radius, center, radiusX, radiusY);
		if (sc == null) {
			return this.drawSlice(windowRect, viewportRect, startAngle, endAngle, zero, radius, center);
		}
		var pf = new $.ig.PathFigure();
		pf.__startPoint = sc.a();
		pf.__isClosed = true;
		var rotationAngle = ((startAngle + ((endAngle - startAngle) * 0.5)) * 180 / Math.PI) + 90;
		var lowerCornerSize = new $.ig.Size(1, sc.cornerSize().width() * (zero / radius), sc.cornerSize().height() * (zero / radius));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point(sc.a2());
			$ret.size(lowerCornerSize);
			$ret.rotationAngle(rotationAngle);
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.isLargeArc(false);
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.LineSegment(1);
			$ret.point(sc.b());
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point(sc.b2());
			$ret.size(sc.cornerSize());
			$ret.rotationAngle(rotationAngle);
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.isLargeArc(false);
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point(sc.c());
			$ret.size(sc.outerSize());
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.isLargeArc(sc.isLargeArc());
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point(sc.c2());
			$ret.size(sc.cornerSize());
			$ret.rotationAngle(rotationAngle);
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.isLargeArc(false);
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.LineSegment(1);
			$ret.point(sc.d());
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point(sc.d2());
			$ret.size(lowerCornerSize);
			$ret.rotationAngle(rotationAngle);
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.isLargeArc(false);
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point(sc.a());
			$ret.size(sc.innerSize());
			$ret.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
			$ret.isLargeArc(sc.isLargeArc());
			return $ret;
		}()));
		var pg = new $.ig.PathGeometry();
		pg.figures().add(pf);
		return pg;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.AnchoredRadialSeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.RadialPieSeries.prototype.radiusXPropertyName:
			case $.ig.RadialPieSeries.prototype.radiusYPropertyName:
				this.renderSeries(false);
				break;
		}
	}
	,
	$type: new $.ig.Type('RadialPieSeries', $.ig.AnchoredRadialSeries.prototype.$type)
}, true);

$.ig.util.defType('SliceCoords', 'Object', {
	init: function () {
		this._outerSize = new $.ig.Size();
		this._innerSize = new $.ig.Size();
		this._cornerSize = new $.ig.Size();
		$.ig.Object.prototype.init.call(this);
	},
	_a: null,
	a: function (value) {
		if (arguments.length === 1) {
			this._a = value;
			return value;
		} else {
			return this._a;
		}
	}
	,
	_b: null,
	b: function (value) {
		if (arguments.length === 1) {
			this._b = value;
			return value;
		} else {
			return this._b;
		}
	}
	,
	_c: null,
	c: function (value) {
		if (arguments.length === 1) {
			this._c = value;
			return value;
		} else {
			return this._c;
		}
	}
	,
	_d: null,
	d: function (value) {
		if (arguments.length === 1) {
			this._d = value;
			return value;
		} else {
			return this._d;
		}
	}
	,
	_a2: null,
	a2: function (value) {
		if (arguments.length === 1) {
			this._a2 = value;
			return value;
		} else {
			return this._a2;
		}
	}
	,
	_b2: null,
	b2: function (value) {
		if (arguments.length === 1) {
			this._b2 = value;
			return value;
		} else {
			return this._b2;
		}
	}
	,
	_c2: null,
	c2: function (value) {
		if (arguments.length === 1) {
			this._c2 = value;
			return value;
		} else {
			return this._c2;
		}
	}
	,
	_d2: null,
	d2: function (value) {
		if (arguments.length === 1) {
			this._d2 = value;
			return value;
		} else {
			return this._d2;
		}
	}
	,
	_isLargeArc: false,
	isLargeArc: function (value) {
		if (arguments.length === 1) {
			this._isLargeArc = value;
			return value;
		} else {
			return this._isLargeArc;
		}
	}
	,
	_outerSize: null,
	outerSize: function (value) {
		if (arguments.length === 1) {
			this._outerSize = value;
			return value;
		} else {
			return this._outerSize;
		}
	}
	,
	_innerSize: null,
	innerSize: function (value) {
		if (arguments.length === 1) {
			this._innerSize = value;
			return value;
		} else {
			return this._innerSize;
		}
	}
	,
	_cornerSize: null,
	cornerSize: function (value) {
		if (arguments.length === 1) {
			this._cornerSize = value;
			return value;
		} else {
			return this._cornerSize;
		}
	}
	,
	getSliceCoords: function (windowRect, viewportRect, startAngle, endAngle, zero, radius, center) {
		var angleMin = Math.min(startAngle, endAngle);
		var angleMax = Math.max(startAngle, endAngle);
		var cosAngleMin = Math.cos(angleMin);
		var sinAngleMin = Math.sin(angleMin);
		var minLength = Math.max(0, zero);
		var maxLength = radius;
		var startXmin = center.__x + cosAngleMin * minLength;
		var startYmin = center.__y + sinAngleMin * minLength;
		var endXmin = center.__x + cosAngleMin * maxLength;
		var endYmin = center.__y + sinAngleMin * maxLength;
		var cosAngleMax = Math.cos(angleMax);
		var sinAngleMax = Math.sin(angleMax);
		var startXmax = center.__x + cosAngleMax * minLength;
		var startYmax = center.__y + sinAngleMax * minLength;
		var endXmax = center.__x + cosAngleMax * maxLength;
		var endYmax = center.__y + sinAngleMax * maxLength;
		startXmin = $.ig.ViewportUtils.prototype.transformXToViewport(startXmin, windowRect, viewportRect);
		startYmin = $.ig.ViewportUtils.prototype.transformYToViewport(startYmin, windowRect, viewportRect);
		endXmin = $.ig.ViewportUtils.prototype.transformXToViewport(endXmin, windowRect, viewportRect);
		endYmin = $.ig.ViewportUtils.prototype.transformYToViewport(endYmin, windowRect, viewportRect);
		startXmax = $.ig.ViewportUtils.prototype.transformXToViewport(startXmax, windowRect, viewportRect);
		startYmax = $.ig.ViewportUtils.prototype.transformYToViewport(startYmax, windowRect, viewportRect);
		endXmax = $.ig.ViewportUtils.prototype.transformXToViewport(endXmax, windowRect, viewportRect);
		endYmax = $.ig.ViewportUtils.prototype.transformYToViewport(endYmax, windowRect, viewportRect);
		var a = { __x: startXmin, __y: startYmin, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var b = { __x: endXmin, __y: endYmin, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var c = { __x: endXmax, __y: endYmax, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var d = { __x: startXmax, __y: startYmax, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var maxLenX = $.ig.ViewportUtils.prototype.transformXToViewportLength(maxLength, windowRect, viewportRect);
		var maxLenY = $.ig.ViewportUtils.prototype.transformYToViewportLength(maxLength, windowRect, viewportRect);
		var minLenX = $.ig.ViewportUtils.prototype.transformXToViewportLength(minLength, windowRect, viewportRect);
		var minLenY = $.ig.ViewportUtils.prototype.transformYToViewportLength(minLength, windowRect, viewportRect);
		return (function () {
			var $ret = new $.ig.SliceCoords();
			$ret.a(a);
			$ret.b(b);
			$ret.c(c);
			$ret.d(d);
			$ret.isLargeArc(((angleMax - angleMin) > Math.PI));
			$ret.outerSize(new $.ig.Size(1, maxLenX, maxLenY));
			$ret.innerSize(new $.ig.Size(1, minLenX, minLenY));
			return $ret;
		}());
	}
	,
	getRoundedSliceCoords: function (windowRect, viewportRect, startAngle, endAngle, zero, radius, center, radiusX, radiusY) {
		var radDiff = $.ig.ViewportUtils.prototype.transformXFromViewportLength(radiusY, windowRect, viewportRect);
		var radLength = radius - zero;
		if (radLength < 0) {
			return null;
		}
		if ($.ig.ViewportUtils.prototype.transformXToViewportLength(radLength, windowRect, viewportRect) < 2) {
			return null;
		}
		if (radDiff * 2 > radLength) {
			radDiff = radLength / 2;
			radiusY = $.ig.ViewportUtils.prototype.transformXToViewportLength(radDiff, windowRect, viewportRect);
		}
		var xDiff = $.ig.ViewportUtils.prototype.transformXFromViewportLength(radiusX, windowRect, viewportRect);
		var oppOverAdj = xDiff / (radius - radDiff);
		var angleDiff = Math.atan(oppOverAdj);
		if (angleDiff * 2 > Math.abs(endAngle - startAngle)) {
			angleDiff = Math.abs(endAngle - startAngle) / 2;
			radiusX = $.ig.ViewportUtils.prototype.transformXToViewportLength(Math.tan(angleDiff) * (radius - radDiff), windowRect, viewportRect);
		}
		var sc = $.ig.SliceCoords.prototype.getSliceCoords(windowRect, viewportRect, startAngle + angleDiff, endAngle - angleDiff, zero, radius, center);
		var sc2 = $.ig.SliceCoords.prototype.getSliceCoords(windowRect, viewportRect, startAngle, endAngle, zero + (radDiff * (zero / radius)), radius - radDiff, center);
		sc.b2(sc.b());
		sc.d2(sc.d());
		sc.a2(sc2.a());
		sc.b(sc2.b());
		sc.c2(sc2.c());
		sc.d(sc2.d());
		sc.cornerSize(new $.ig.Size(1, radiusX, radiusY));
		return sc;
	}
	,
	$type: new $.ig.Type('SliceCoords', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('Legend', 'LegendBase', {
	createView: function () {
		return new $.ig.LegendView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.LegendBase.prototype.onViewCreated.call(this, view);
		this.legendView(view);
	}
	,
	_legendView: null,
	legendView: function (value) {
		if (arguments.length === 1) {
			this._legendView = value;
			return value;
		} else {
			return this._legendView;
		}
	}
	,
	init: function () {
		var $self = this;
		$.ig.LegendBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.Legend.prototype.$type);
		var $t = this.children();
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, function (o, e) {
			if (e.oldItems() != null) {
				var en = e.oldItems().getEnumerator();
				while (en.moveNext()) {
					var item = en.current();
					$self.legendView().removeItemVisual(item);
				}
			}
			if (e.newItems() != null) {
				var en1 = e.newItems().getEnumerator();
				while (en1.moveNext()) {
					var item1 = en1.current();
					$self.legendView().addItemVisual(item1);
				}
			}
		});
	},
	getSeriesIndex: function (series) {
		if ($.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null || $.ig.util.cast($.ig.SplineFragmentBase.prototype.$type, series) !== null) {
			var parentSeries = $.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null ? (series).parentSeries() : (series).parentSeries();
			if (parentSeries.reverseLegendOrder()) {
				return -1 + parentSeries.index() + parentSeries.actualSeries().count() - parentSeries.stackedSeriesManager().seriesVisual().indexOf($.ig.util.cast($.ig.AnchoredCategorySeries.prototype.$type, series));
			}
		}
		return series.index();
	}
	,
	addChildInOrder: function (legendItem, series) {
		if ($.ig.util.cast($.ig.StackedSeriesBase.prototype.$type, series) !== null) {
			return;
		}
		if (!series.isUsableInLegend()) {
			return;
		}
		var index = 0;
		var en = this.children().getEnumerator();
		while (en.moveNext()) {
			var item = en.current();
			var itemChart;
			var itemSeries;
			var itemItem;
			var $ret = this.view().fetchLegendEnvironment(item, itemChart, itemSeries, itemItem);
			itemChart = $ret.p1;
			itemSeries = $ret.p2;
			itemItem = $ret.p3;
			if (series.seriesViewer() != null && itemChart != null && (this.getSortOrder(series.seriesViewer()) < this.getSortOrder(itemChart) || (this.getSortOrder(series.seriesViewer()) == -1 && this.getSortOrder(itemChart) == -1 && series.seriesViewer().getHashCode() < itemChart.getHashCode()))) {
				break;
			}
			if (series.seriesViewer() != null && itemChart != null && series.seriesViewer() == itemChart && itemSeries != null) {
				var indexOfSeries = this.getSeriesIndex(series);
				var indexOfItemSeries = this.getSeriesIndex(itemSeries);
				var sortOrderSeries = this.getSortOrder(series);
				var sortOrderItemSeries = this.getSortOrder(itemSeries);
				if ($.ig.util.cast($.ig.BarSeries.prototype.$type, itemSeries) !== null) {
					if (sortOrderItemSeries == -1 && sortOrderSeries == -1) {
						index = 0;
						break;
					}
					if (sortOrderSeries < sortOrderItemSeries || sortOrderItemSeries == -1) {
						break;
					}
				}
				if (indexOfSeries <= indexOfItemSeries) {
					break;
				}
			}
			index++;
		}
		this.children().insert(index, legendItem);
		var info = new $.ig.LegendItemInfo();
		info.legendItem(legendItem);
		info.series(series);
		var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, legendItem);
		if (contentControl != null && contentControl.content() != null) {
			var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
			if (context != null) {
				info.dataContext(context);
				info.text(context.itemLabel());
			}
		}
	}
	,
	getSortOrder: function (target) {
		return -1;
	}
	,
	$type: new $.ig.Type('Legend', $.ig.LegendBase.prototype.$type)
}, true);

$.ig.util.defType('AngleAxisLabelPanelView', 'AxisLabelPanelBaseView', {
	_angleModel: null,
	angleModel: function (value) {
		if (arguments.length === 1) {
			this._angleModel = value;
			return value;
		} else {
			return this._angleModel;
		}
	}
	,
	init: function (model) {
		$.ig.AxisLabelPanelBaseView.prototype.init.call(this, model);
		this.angleModel(model);
	},
	onInit: function () {
		$.ig.AxisLabelPanelBaseView.prototype.onInit.call(this);
		this.angleModel().clipLabelsToBounds(true);
	}
	,
	determineLargestLabels: function (rectangles) {
		this.angleModel().largestWidth(-1.7976931348623157E+308);
		this.angleModel().largestHeight(-1.7976931348623157E+308);
		this.angleModel().largestRenderWidth(-1.7976931348623157E+308);
		this.angleModel().largestRenderHeight(-1.7976931348623157E+308);
		for (var i = 0; i < this.model().textBlocks().count(); i++) {
			var currentTextBlock = this.model().textBlocks().__inner[i];
			var position = this.model().labelPositions().__inner[i];
			var point = this.angleModel().getPoint()(position.value());
			var width = this.getDesiredWidth(currentTextBlock);
			var height = this.getDesiredHeight(currentTextBlock);
			var fullWidth = width + this.getLabelLeftMargin() + this.getLabelRightMargin();
			var fullHeight = height + this.getLabelTopMargin() + this.getLabelBottomMargin();
			var x = point.__x - fullWidth / 2;
			var y = point.__y - fullHeight / 2;
			this.angleModel().largestRenderWidth(Math.max(width, this.angleModel().largestRenderWidth()));
			this.angleModel().largestRenderHeight(Math.max(height, this.angleModel().largestRenderHeight()));
			this.angleModel().largestWidth(Math.max(fullWidth, this.angleModel().largestWidth()));
			this.angleModel().largestHeight(Math.max(fullHeight, this.angleModel().largestHeight()));
			var rect = new $.ig.Rect(0, x, y, fullWidth, fullHeight);
			rectangles.add(rect);
		}
	}
	,
	getLabelBottomMargin: function () {
		return 0;
	}
	,
	getLabelLeftMargin: function () {
		return 0;
	}
	,
	getLabelRightMargin: function () {
		return 0;
	}
	,
	getLabelTopMargin: function () {
		return 0;
	}
	,
	$type: new $.ig.Type('AngleAxisLabelPanelView', $.ig.AxisLabelPanelBaseView.prototype.$type)
}, true);

$.ig.util.defType('CategoryAngleAxisView', 'CategoryAxisBaseView', {
	_categoryAngleModel: null,
	categoryAngleModel: function (value) {
		if (arguments.length === 1) {
			this._categoryAngleModel = value;
			return value;
		} else {
			return this._categoryAngleModel;
		}
	}
	,
	init: function (model) {
		$.ig.CategoryAxisBaseView.prototype.init.call(this, model);
		this.categoryAngleModel(model);
	},
	$type: new $.ig.Type('CategoryAngleAxisView', $.ig.CategoryAxisBaseView.prototype.$type)
}, true);

$.ig.util.defType('NumericAngleAxisView', 'NumericAxisBaseView', {
	_numericAngleModel: null,
	numericAngleModel: function (value) {
		if (arguments.length === 1) {
			this._numericAngleModel = value;
			return value;
		} else {
			return this._numericAngleModel;
		}
	}
	,
	init: function (model) {
		$.ig.NumericAxisBaseView.prototype.init.call(this, model);
		this.numericAngleModel(model);
	},
	$type: new $.ig.Type('NumericAngleAxisView', $.ig.NumericAxisBaseView.prototype.$type)
}, true);

$.ig.util.defType('NumericRadiusAxisView', 'NumericAxisBaseView', {
	_numericRadiusModel: null,
	numericRadiusModel: function (value) {
		if (arguments.length === 1) {
			this._numericRadiusModel = value;
			return value;
		} else {
			return this._numericRadiusModel;
		}
	}
	,
	init: function (model) {
		$.ig.NumericAxisBaseView.prototype.init.call(this, model);
		this.numericRadiusModel(model);
	},
	$type: new $.ig.Type('NumericRadiusAxisView', $.ig.NumericAxisBaseView.prototype.$type)
}, true);

$.ig.util.defType('RadialAxisLabelPanelView', 'HorizontalAxisLabelPanelBaseView', {
	_radialModel: null,
	radialModel: function (value) {
		if (arguments.length === 1) {
			this._radialModel = value;
			return value;
		} else {
			return this._radialModel;
		}
	}
	,
	init: function (model) {
		$.ig.HorizontalAxisLabelPanelBaseView.prototype.init.call(this, model);
		this.radialModel(model);
	},
	setLabelRotationalTransform: function (label, angle, yFactor) {
		var width = this.getDesiredWidth(label);
		var height = this.getDesiredHeight(label);
		var g = new $.ig.TransformGroup();
		g.children().add((function () {
			var $ret = new $.ig.RotateTransform();
			$ret.angle(angle);
			$ret.centerX(width / 2);
			$ret.centerY(height / 2);
			return $ret;
		}()));
		g.children().add((function () {
			var $ret = new $.ig.TranslateTransform();
			$ret.x(width / -2);
			$ret.y(width / 2 * yFactor);
			return $ret;
		}()));
		label.renderTransform(g);
	}
	,
	clearPanelRotation: function () {
		this.model().renderTransform(null);
	}
	,
	applyPanelRotation: function (finalSize) {
		var $self = this;
		var panelTop = $.ig.ViewportUtils.prototype.transformYToViewport(0.5, this.model().windowRect(), this.model().viewportRect());
		this.model().renderTransform((function () {
			var $ret = new $.ig.RotateTransform();
			$ret.angle(($self.radialModel().crossingAngle() * 180) / Math.PI);
			$ret.centerX($self.radialModel().rotationCenter().__x);
			$ret.centerY(($self.radialModel().rotationCenter().__y * finalSize.height()) + panelTop);
			return $ret;
		}()));
	}
	,
	onExtentChangedAfterArrange: function () {
		$.ig.HorizontalAxisLabelPanelBaseView.prototype.onExtentChangedAfterArrange.call(this);
		this.model().arrangeLabels(new $.ig.Size(1, this.model().labelViewport().width(), this.model().labelViewport().height()));
	}
	,
	$type: new $.ig.Type('RadialAxisLabelPanelView', $.ig.HorizontalAxisLabelPanelBaseView.prototype.$type)
}, true);

$.ig.util.defType('LegendView', 'LegendBaseView', {
	init: function (model) {
		$.ig.LegendBaseView.prototype.init.call(this, model);
		this.legendModel(model);
	},
	_legendModel: null,
	legendModel: function (value) {
		if (arguments.length === 1) {
			this._legendModel = value;
			return value;
		} else {
			return this._legendModel;
		}
	}
	,
	onInit: function () {
		$.ig.LegendBaseView.prototype.onInit.call(this);
	}
	,
	$type: new $.ig.Type('LegendView', $.ig.LegendBaseView.prototype.$type)
}, true);

$.ig.util.defType('RadialBaseView', 'MarkerSeriesView', {
	_bucketCalculator: null,
	bucketCalculator: function (value) {
		if (arguments.length === 1) {
			this._bucketCalculator = value;
			return value;
		} else {
			return this._bucketCalculator;
		}
	}
	,
	_radialModel: null,
	radialModel: function (value) {
		if (arguments.length === 1) {
			this._radialModel = value;
			return value;
		} else {
			return this._radialModel;
		}
	}
	,
	init: function (model) {
		$.ig.MarkerSeriesView.prototype.init.call(this, model);
		this.radialModel(model);
		this.markers(new $.ig.Pool$1($.ig.Marker.prototype.$type));
		this.initMarkers1(this.markers());
	},
	_markers: null,
	markers: function (value) {
		if (arguments.length === 1) {
			this._markers = value;
			return value;
		} else {
			return this._markers;
		}
	}
	,
	doToAllMarkers: function (action) {
		this.markers().doToAll(action);
	}
	,
	_radius: 0,
	radius: function (value) {
		if (arguments.length === 1) {
			this._radius = value;
			return value;
		} else {
			return this._radius;
		}
	}
	,
	_center: null,
	center: function (value) {
		if (arguments.length === 1) {
			this._center = value;
			return value;
		} else {
			return this._center;
		}
	}
	,
	$type: new $.ig.Type('RadialBaseView', $.ig.MarkerSeriesView.prototype.$type)
}, true);

$.ig.util.defType('AnchoredRadialSeriesView', 'RadialBaseView', {
	_anchoredRadialModel: null,
	anchoredRadialModel: function (value) {
		if (arguments.length === 1) {
			this._anchoredRadialModel = value;
			return value;
		} else {
			return this._anchoredRadialModel;
		}
	}
	,
	init: function (model) {
		$.ig.RadialBaseView.prototype.init.call(this, model);
		this.bucketCalculator(new $.ig.AnchoredRadialBucketCalculator(this));
		this.anchoredRadialModel(model);
		this.trendLineManager(new $.ig.RadialTrendLineManager());
	},
	_trendLineManager: null,
	trendLineManager: function (value) {
		if (arguments.length === 1) {
			this._trendLineManager = value;
			return value;
		} else {
			return this._trendLineManager;
		}
	}
	,
	applyClipping: function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
	}
	,
	updateTrendlineBrush: function () {
		this.anchoredRadialModel().actualTrendLineBrush(null);
		if (this.anchoredRadialModel().trendLineBrush() != null) {
			this.anchoredRadialModel().actualTrendLineBrush(this.anchoredRadialModel().trendLineBrush());
		} else {
			this.anchoredRadialModel().actualTrendLineBrush(this.anchoredRadialModel().actualBrush());
		}
	}
	,
	renderMarkersOverride: function (context, isHitContext) {
		if (context.shouldRender()) {
			if (!isHitContext) {
			}
			if (this.anchoredRadialModel().trendLineType() != $.ig.TrendLineType.prototype.none && !isHitContext) {
				var polyline = this.trendLineManager().trendPolyline();
				polyline.strokeThickness(this.anchoredRadialModel().trendLineThickness());
				polyline.__stroke = this.anchoredRadialModel().actualTrendLineBrush();
				polyline.strokeDashArray(this.anchoredRadialModel().trendLineDashArray());
				polyline.strokeDashCap(this.anchoredRadialModel().trendLineDashCap());
				context.renderPolyline(polyline);
			}
		}
		$.ig.RadialBaseView.prototype.renderMarkersOverride.call(this, context, isHitContext);
	}
	,
	getDefaultTooltipTemplate: function () {
		var tooltipTemplate = "<div class='ui-chart-default-tooltip-content'>";
		if (this.anchoredRadialModel().angleAxis().label() != null) {
			tooltipTemplate += "<span>${item." + this.anchoredRadialModel().angleAxis().label() + "}</span><br/>";
		}
		tooltipTemplate += "<span";
		var nonTransparentOutline = this.model().actualOutline() != null && $.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.model().actualOutline().color()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) && this.model().actualOutline().color().a() > 0;
		if (nonTransparentOutline) {
			tooltipTemplate += " style='color:" + this.model().actualOutline().__fill + "'";
		}
		tooltipTemplate += ">" + this.anchoredRadialModel().title() + ": </span><span class='ui-priority-primary'>" + "${item." + this.anchoredRadialModel().valueMemberPath() + "}</span></div>";
		return tooltipTemplate;
	}
	,
	$type: new $.ig.Type('AnchoredRadialSeriesView', $.ig.RadialBaseView.prototype.$type)
}, true);

$.ig.util.defType('RadialAreaSeriesView', 'AnchoredRadialSeriesView', {
	_radialAreaModel: null,
	radialAreaModel: function (value) {
		if (arguments.length === 1) {
			this._radialAreaModel = value;
			return value;
		} else {
			return this._radialAreaModel;
		}
	}
	,
	init: function (model) {
		this._polygon0 = new $.ig.Path();
		this._polyline0 = new $.ig.Path();
		this._polygon1 = new $.ig.Path();
		this._polyline1 = new $.ig.Path();
		$.ig.AnchoredRadialSeriesView.prototype.init.call(this, model);
		this.radialAreaModel(model);
	},
	onInit: function () {
		$.ig.AnchoredRadialSeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().resolution(4);
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_polygon0: null,
	_polyline0: null,
	_polygon1: null,
	_polyline1: null,
	clearRadialArea: function () {
		this._polygon0.data(null);
		this._polyline0.data(null);
		this._polygon1.data(null);
		this._polyline1.data(null);
		this.makeDirty();
	}
	,
	rasterizeArea: function (count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting) {
		this.anchoredRadialModel().lineRasterizer().rasterizePolygonPaths(this._polygon0, this._polyline0, this._polygon1, this._polyline1, count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting);
		this.makeDirty();
	}
	,
	setupAppearanceOverride: function () {
		$.ig.AnchoredRadialSeriesView.prototype.setupAppearanceOverride.call(this);
		this._polygon0.__fill = this.model().actualBrush();
		this._polygon0.__opacity = this.model().actualAreaFillOpacity();
		this._polygon1.__fill = this.model().actualBrush();
		this._polygon1.__opacity = this.model().actualAreaFillOpacity() * 0.5;
		this._polyline0.__stroke = this.model().actualOutline();
		this._polyline0.strokeThickness(this.model().thickness());
		this._polyline0.strokeDashArray(this.model().dashArray());
		this._polyline0.strokeDashCap(this.model().dashCap());
		this._polyline1.__stroke = this.model().actualOutline();
		this._polyline1.strokeThickness(this.model().thickness());
		this._polyline1.strokeDashArray(this.model().dashArray());
		this._polyline1.strokeDashCap(this.model().dashCap());
	}
	,
	setupHitAppearanceOverride: function () {
		$.ig.AnchoredRadialSeriesView.prototype.setupHitAppearanceOverride.call(this);
		var hitBrush = this.getHitBrush();
		this._polyline0.__stroke = hitBrush;
		this._polyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this._polyline1.__stroke = hitBrush;
		this._polyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this._polygon0.__fill = hitBrush;
		this._polygon0.__opacity = 1;
		this._polygon1.__fill = hitBrush;
		this._polygon1.__opacity = 1;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredRadialSeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			context.renderPath(this._polygon0);
			context.renderPath(this._polygon1);
			context.renderPath(this._polyline0);
			context.renderPath(this._polyline1);
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredRadialSeriesView.prototype.exportViewShapes.call(this, svd);
		var lowerShape = new $.ig.PathVisualData(1, "lowerShape", this._polyline0);
		lowerShape.tags().add("Lower");
		var upperShape = new $.ig.PathVisualData(1, "upperShape", this._polyline1);
		upperShape.tags().add("Upper");
		upperShape.tags().add("Main");
		var translucent = new $.ig.PathVisualData(1, "translucentShape", this._polygon1);
		translucent.tags().add("Translucent");
		var fill = new $.ig.PathVisualData(1, "fillShape", this._polygon0);
		fill.tags().add("Fill");
		svd.shapes().add(lowerShape);
		svd.shapes().add(upperShape);
		svd.shapes().add(translucent);
		svd.shapes().add(fill);
	}
	,
	$type: new $.ig.Type('RadialAreaSeriesView', $.ig.AnchoredRadialSeriesView.prototype.$type)
}, true);

$.ig.util.defType('RadialColumnSeriesView', 'AnchoredRadialSeriesView', {
	_radialColumnModel: null,
	radialColumnModel: function (value) {
		if (arguments.length === 1) {
			this._radialColumnModel = value;
			return value;
		} else {
			return this._radialColumnModel;
		}
	}
	,
	init: function (model) {
		var $self = this;
		$.ig.AnchoredRadialSeriesView.prototype.init.call(this, model);
		this.radialColumnModel(model);
		this._columns = (function () {
			var $ret = new $.ig.Pool$1($.ig.Path.prototype.$type);
			$ret.create($self.columnCreate.runOn($self));
			$ret.activate($self.columnActivate.runOn($self));
			$ret.disactivate($self.columnDisactivate.runOn($self));
			$ret.destroy($self.columnDestroy.runOn($self));
			return $ret;
		}());
	},
	_columns: null,
	onInit: function () {
		$.ig.AnchoredRadialSeriesView.prototype.onInit.call(this);
		this.visibleColumns(new $.ig.List$1($.ig.Path.prototype.$type, 0));
		if (!this.isThumbnailView()) {
			this.model().resolution(4);
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_visibleColumns: null,
	visibleColumns: function (value) {
		if (arguments.length === 1) {
			this._visibleColumns = value;
			return value;
		} else {
			return this._visibleColumns;
		}
	}
	,
	columnCreate: function () {
		var column = new $.ig.Path();
		this.visibleColumns().add(column);
		column.__visibility = $.ig.Visibility.prototype.collapsed;
		return column;
	}
	,
	columnActivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	columnDisactivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	columnDestroy: function (column) {
		this.visibleColumns().remove(column);
	}
	,
	positionRectangle: function (column, xpos, ypos, angle, centerX, centerY) {
		this.makeDirty();
	}
	,
	setupItemAppearanceOverride: function (item, index) {
		$.ig.AnchoredRadialSeriesView.prototype.setupItemAppearanceOverride.call(this, item, index);
		var column = item;
		column.__fill = this.model().actualBrush();
		column.__stroke = this.model().actualOutline();
		column.strokeThickness(this.model().thickness());
		column.strokeDashArray(this.model().dashArray());
		column.strokeDashCap(this.model().dashCap());
	}
	,
	setupItemHitAppearanceOverride: function (item, index) {
		$.ig.AnchoredRadialSeriesView.prototype.setupItemHitAppearanceOverride.call(this, item, index);
		var column = item;
		var hitBrush = this.getHitBrush1(index);
		column.__fill = hitBrush;
		column.__stroke = hitBrush;
		column.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredRadialSeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			for (var i = 0; i < this.visibleColumns().count(); i++) {
				var column = this.visibleColumns().__inner[i];
				this.setupItemAppearance(column, i, isHitContext);
				context.renderPath(column);
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredRadialSeriesView.prototype.exportViewShapes.call(this, svd);
		var i = 0;
		var en = this._columns.active().getEnumerator();
		while (en.moveNext()) {
			var column = en.current();
			var rvd = new $.ig.PathVisualData(1, "column" + i, column);
			rvd.tags().add("Main");
			rvd.tags().add("Fill");
			svd.shapes().add(rvd);
			i++;
		}
	}
	,
	finalizeColumns: function () {
		this.makeDirty();
	}
	,
	$type: new $.ig.Type('RadialColumnSeriesView', $.ig.AnchoredRadialSeriesView.prototype.$type)
}, true);

$.ig.util.defType('RadialLineSeriesView', 'AnchoredRadialSeriesView', {
	_radialLineModel: null,
	radialLineModel: function (value) {
		if (arguments.length === 1) {
			this._radialLineModel = value;
			return value;
		} else {
			return this._radialLineModel;
		}
	}
	,
	init: function (model) {
		this._polyline0 = new $.ig.Path();
		this._polygon01 = new $.ig.Path();
		this._polyline1 = new $.ig.Path();
		$.ig.AnchoredRadialSeriesView.prototype.init.call(this, model);
		this.radialLineModel(model);
	},
	onInit: function () {
		$.ig.AnchoredRadialSeriesView.prototype.onInit.call(this);
	}
	,
	_polyline0: null,
	_polygon01: null,
	_polyline1: null,
	clearRadialLine: function () {
		this._polyline0.data(null);
		this._polygon01.data(null);
		this._polyline1.data(null);
		this.makeDirty();
	}
	,
	renderRadialLine: function (count, buckets, useX0AsX1, unknownValuePlotting, clipper, bucketSize, resolution) {
		this.anchoredRadialModel().lineRasterizer().rasterizePolylinePaths(this._polyline0, this._polygon01, this._polyline1, count, buckets, useX0AsX1, unknownValuePlotting, clipper, bucketSize, resolution);
		this.makeDirty();
	}
	,
	setupAppearanceOverride: function () {
		$.ig.AnchoredRadialSeriesView.prototype.setupAppearanceOverride.call(this);
		this._polyline0.__stroke = this.model().actualBrush();
		this._polyline0.strokeThickness(this.model().thickness());
		this._polyline0.strokeDashArray(this.model().dashArray());
		this._polyline0.strokeDashCap(this.model().dashCap());
		this._polyline1.__stroke = this.model().actualBrush();
		this._polyline1.strokeThickness(this.model().thickness());
		this._polyline1.strokeDashArray(this.model().dashArray());
		this._polyline1.strokeDashCap(this.model().dashCap());
		this._polygon01.__fill = this.model().actualBrush();
		this._polygon01.__opacity = 0.75;
	}
	,
	setupHitAppearanceOverride: function () {
		$.ig.AnchoredRadialSeriesView.prototype.setupHitAppearanceOverride.call(this);
		var hitBrush = this.getHitBrush();
		this._polyline0.__stroke = hitBrush;
		this._polyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this._polyline1.__stroke = hitBrush;
		this._polyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this._polygon01.__fill = hitBrush;
		this._polygon01.__opacity = 1;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredRadialSeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			context.renderPath(this._polygon01);
			context.renderPath(this._polyline0);
			context.renderPath(this._polyline1);
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredRadialSeriesView.prototype.exportViewShapes.call(this, svd);
		var lowerShape = new $.ig.PathVisualData(1, "lowerShape", this._polyline0);
		lowerShape.tags().add("Lower");
		lowerShape.tags().add("Main");
		var upperShape = new $.ig.PathVisualData(1, "upperShape", this._polyline1);
		upperShape.tags().add("Upper");
		var translucent = new $.ig.PathVisualData(1, "translucentShape", this._polygon01);
		translucent.tags().add("Translucent");
		svd.shapes().add(lowerShape);
		svd.shapes().add(upperShape);
		svd.shapes().add(translucent);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(1);
		this.model().shadowOffsetY(4);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('RadialLineSeriesView', $.ig.AnchoredRadialSeriesView.prototype.$type)
}, true);

$.ig.util.defType('RadialPieSeriesView', 'AnchoredRadialSeriesView', {
	_radialPieModel: null,
	radialPieModel: function (value) {
		if (arguments.length === 1) {
			this._radialPieModel = value;
			return value;
		} else {
			return this._radialPieModel;
		}
	}
	,
	init: function (model) {
		var $self = this;
		$.ig.AnchoredRadialSeriesView.prototype.init.call(this, model);
		this.radialPieModel(model);
		this._slices = (function () {
			var $ret = new $.ig.Pool$1($.ig.Path.prototype.$type);
			$ret.create($self.pieCreate.runOn($self));
			$ret.activate($self.pieActivate.runOn($self));
			$ret.disactivate($self.pieDisactivate.runOn($self));
			$ret.destroy($self.pieDestroy.runOn($self));
			return $ret;
		}());
	},
	_slices: null,
	onInit: function () {
		$.ig.AnchoredRadialSeriesView.prototype.onInit.call(this);
		this.visibleSlices(new $.ig.List$1($.ig.Path.prototype.$type, 0));
		if (!this.isThumbnailView()) {
			this.model().resolution(4);
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_visibleSlices: null,
	visibleSlices: function (value) {
		if (arguments.length === 1) {
			this._visibleSlices = value;
			return value;
		} else {
			return this._visibleSlices;
		}
	}
	,
	pieCreate: function () {
		var $self = this;
		var slice = (function () {
			var $ret = new $.ig.Path();
			$ret.dataContext((function () {
				var $ret = new $.ig.DataContext();
				$ret.series($self.model());
				return $ret;
			}()));
			return $ret;
		}());
		this.visibleSlices().add(slice);
		slice.__visibility = $.ig.Visibility.prototype.collapsed;
		return slice;
	}
	,
	pieActivate: function (slice) {
		slice.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	pieDisactivate: function (slice) {
		slice.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	pieDestroy: function (slice) {
		this.visibleSlices().remove(slice);
	}
	,
	setupItemAppearanceOverride: function (item, index) {
		$.ig.AnchoredRadialSeriesView.prototype.setupItemAppearanceOverride.call(this, item, index);
		var slice = item;
		slice.__stroke = this.model().actualOutline();
		slice.strokeThickness(this.model().thickness());
		slice.__fill = this.model().actualBrush();
		slice.strokeDashArray(this.model().dashArray());
		slice.strokeDashCap(this.model().dashCap());
	}
	,
	setupItemHitAppearanceOverride: function (item, index) {
		$.ig.AnchoredRadialSeriesView.prototype.setupItemHitAppearanceOverride.call(this, item, index);
		var slice = item;
		var hitBrush = this.getHitBrush1(index);
		slice.__stroke = hitBrush;
		slice.strokeThickness(this.model().thickness());
		slice.__fill = hitBrush;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredRadialSeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			for (var i = 0; i < this.visibleSlices().count(); i++) {
				var slice = this.visibleSlices().__inner[i];
				this.setupItemAppearance(slice, i, isHitContext);
				context.renderPath(slice);
			}
		}
	}
	,
	slicesUpdated: function () {
		this.makeDirty();
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredRadialSeriesView.prototype.exportViewShapes.call(this, svd);
		var i = 0;
		var en = this._slices.active().getEnumerator();
		while (en.moveNext()) {
			var slice = en.current();
			var pvd = new $.ig.PathVisualData(1, "slice" + i, slice);
			pvd.tags().add("Main");
			pvd.tags().add("Fill");
			svd.shapes().add(pvd);
			i++;
		}
	}
	,
	$type: new $.ig.Type('RadialPieSeriesView', $.ig.AnchoredRadialSeriesView.prototype.$type)
}, true);

$.ig.CategoryAngleAxis.prototype.startAngleOffsetPropertyName = "StartAngleOffset";
$.ig.CategoryAngleAxis.prototype._intervalPropertyName = "Interval";
$.ig.CategoryAngleAxis.prototype.actualIntervalPropertyName = "ActualInterval";
$.ig.CategoryAngleAxis.prototype._minorIntervalPropertyName = "MinorInterval";
$.ig.CategoryAngleAxis.prototype.actualMinorIntervalPropertyName = "ActualMinorInterval";
$.ig.CategoryAngleAxis.prototype.startAngleOffsetProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryAngleAxis.prototype.startAngleOffsetPropertyName, Number, $.ig.CategoryAngleAxis.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryAngleAxis.prototype.startAngleOffsetPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryAngleAxis.prototype.intervalProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryAngleAxis.prototype._intervalPropertyName, Number, $.ig.CategoryAngleAxis.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryAngleAxis.prototype._intervalPropertyName, e.oldValue(), e.newValue());
	(sender).renderAxis1(false);
}));
$.ig.CategoryAngleAxis.prototype.minorIntervalProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryAngleAxis.prototype._minorIntervalPropertyName, Number, $.ig.CategoryAngleAxis.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (sender, e) {
	($.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, sender)).raisePropertyChanged($.ig.CategoryAngleAxis.prototype._minorIntervalPropertyName, e.oldValue(), e.newValue());
	($.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, sender)).renderAxis1(false);
}));

$.ig.NumericAngleAxis.prototype.startAngleOffsetPropertyName = "StartAngleOffset";
$.ig.NumericAngleAxis.prototype.startAngleOffsetProperty = $.ig.DependencyProperty.prototype.register($.ig.NumericAngleAxis.prototype.startAngleOffsetPropertyName, Number, $.ig.NumericAngleAxis.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.NumericAngleAxis.prototype.startAngleOffsetPropertyName, e.oldValue(), e.newValue());
}));

$.ig.NumericRadiusAxis.prototype.radiusExtentScalePropertyName = "RadiusExtentScale";
$.ig.NumericRadiusAxis.prototype.innerRadiusExtentScalePropertyName = "InnerRadiusExtentScale";
$.ig.NumericRadiusAxis.prototype.radiusExtentScaleProperty = $.ig.DependencyProperty.prototype.register($.ig.NumericRadiusAxis.prototype.radiusExtentScalePropertyName, Number, $.ig.NumericRadiusAxis.prototype.$type, new $.ig.PropertyMetadata(2, 0.75, function (sender, e) {
	(sender).raisePropertyChanged($.ig.NumericRadiusAxis.prototype.radiusExtentScalePropertyName, e.oldValue(), e.newValue());
}));
$.ig.NumericRadiusAxis.prototype.innerRadiusExtentScaleProperty = $.ig.DependencyProperty.prototype.register($.ig.NumericRadiusAxis.prototype.innerRadiusExtentScalePropertyName, Number, $.ig.NumericRadiusAxis.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.NumericRadiusAxis.prototype.innerRadiusExtentScalePropertyName, e.oldValue(), e.newValue());
}));

$.ig.RadialBase.prototype.angleAxisPropertyName = "AngleAxis";
$.ig.RadialBase.prototype.valueAxisPropertyName = "ValueAxis";
$.ig.RadialBase.prototype.clipSeriesToBoundsPropertyName = "ClipSeriesToBounds";
$.ig.RadialBase.prototype.angleAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.RadialBase.prototype.angleAxisPropertyName, $.ig.CategoryAngleAxis.prototype.$type, $.ig.RadialBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RadialBase.prototype.angleAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RadialBase.prototype.valueAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.RadialBase.prototype.valueAxisPropertyName, $.ig.NumericRadiusAxis.prototype.$type, $.ig.RadialBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RadialBase.prototype.valueAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RadialBase.prototype.clipSeriesToBoundsProperty = $.ig.DependencyProperty.prototype.register($.ig.RadialBase.prototype.clipSeriesToBoundsPropertyName, $.ig.Boolean.prototype.$type, $.ig.RadialBase.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RadialBase.prototype.clipSeriesToBoundsPropertyName, e.oldValue(), e.newValue());
}));

$.ig.AnchoredRadialSeries.prototype.valueMemberPathPropertyName = "ValueMemberPath";
$.ig.AnchoredRadialSeries.prototype.valueColumnPropertyName = "ValueColumn";
$.ig.AnchoredRadialSeries.prototype.valueMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.AnchoredRadialSeries.prototype.valueMemberPathPropertyName, String, $.ig.AnchoredRadialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.AnchoredRadialSeries.prototype.valueMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnchoredRadialSeries.prototype.trendLineTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineTypePropertyName, $.ig.TrendLineType.prototype.$type, $.ig.AnchoredRadialSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.TrendLineType.prototype.getBox($.ig.TrendLineType.prototype.none), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnchoredRadialSeries.prototype.trendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.AnchoredRadialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnchoredRadialSeries.prototype.actualTrendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineActualBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.AnchoredRadialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineActualBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnchoredRadialSeries.prototype.trendLineThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineThicknessPropertyName, Number, $.ig.AnchoredRadialSeries.prototype.$type, new $.ig.PropertyMetadata(2, 1.5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnchoredRadialSeries.prototype.trendLineDashCapProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashCapPropertyName, $.ig.PenLineCap.prototype.$type, $.ig.AnchoredRadialSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.PenLineCap.prototype.getBox($.ig.PenLineCap.prototype.flat), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashCapPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnchoredRadialSeries.prototype.trendLineDashArrayProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashArrayPropertyName, $.ig.DoubleCollection.prototype.$type, $.ig.AnchoredRadialSeries.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashArrayPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnchoredRadialSeries.prototype.trendLinePeriodProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLinePeriodPropertyName, $.ig.Number.prototype.$type, $.ig.AnchoredRadialSeries.prototype.$type, new $.ig.PropertyMetadata(2, 7, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLinePeriodPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnchoredRadialSeries.prototype.trendLineZIndexProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineZIndexPropertyName, $.ig.Number.prototype.$type, $.ig.AnchoredRadialSeries.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineZIndexPropertyName, e.oldValue(), e.newValue());
}));

$.ig.RadialAreaSeries.prototype._unknownValuePlottingPropertyName = "UnknownValuePlotting";
$.ig.RadialAreaSeries.prototype.unknownValuePlottingProperty = $.ig.DependencyProperty.prototype.register($.ig.RadialAreaSeries.prototype._unknownValuePlottingPropertyName, $.ig.UnknownValuePlotting.prototype.$type, $.ig.RadialAreaSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.UnknownValuePlotting.prototype.getBox($.ig.UnknownValuePlotting.prototype.dontPlot), function (sender, e) {
	(sender).raisePropertyChanged($.ig.RadialAreaSeries.prototype._unknownValuePlottingPropertyName, e.oldValue(), e.newValue());
}));

$.ig.RadialColumnSeries.prototype.radiusXPropertyName = "RadiusX";
$.ig.RadialColumnSeries.prototype.radiusYPropertyName = "RadiusY";
$.ig.RadialColumnSeries.prototype.radiusXProperty = $.ig.DependencyProperty.prototype.register($.ig.RadialColumnSeries.prototype.radiusXPropertyName, Number, $.ig.RadialColumnSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RadialColumnSeries.prototype.radiusXPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RadialColumnSeries.prototype.radiusYProperty = $.ig.DependencyProperty.prototype.register($.ig.RadialColumnSeries.prototype.radiusYPropertyName, Number, $.ig.RadialColumnSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RadialColumnSeries.prototype.radiusYPropertyName, e.oldValue(), e.newValue());
}));

$.ig.RadialLineSeries.prototype._unknownValuePlottingPropertyName = "UnknownValuePlotting";
$.ig.RadialLineSeries.prototype.unknownValuePlottingProperty = $.ig.DependencyProperty.prototype.register($.ig.RadialLineSeries.prototype._unknownValuePlottingPropertyName, $.ig.UnknownValuePlotting.prototype.$type, $.ig.RadialLineSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.UnknownValuePlotting.prototype.getBox($.ig.UnknownValuePlotting.prototype.dontPlot), function (sender, e) {
	(sender).raisePropertyChanged($.ig.RadialLineSeries.prototype._unknownValuePlottingPropertyName, e.oldValue(), e.newValue());
}));

$.ig.RadialPieSeries.prototype.radiusXPropertyName = "RadiusX";
$.ig.RadialPieSeries.prototype.radiusYPropertyName = "RadiusY";
$.ig.RadialPieSeries.prototype.radiusXProperty = $.ig.DependencyProperty.prototype.register($.ig.RadialPieSeries.prototype.radiusXPropertyName, Number, $.ig.RadialPieSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RadialPieSeries.prototype.radiusXPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RadialPieSeries.prototype.radiusYProperty = $.ig.DependencyProperty.prototype.register($.ig.RadialPieSeries.prototype.radiusYPropertyName, Number, $.ig.RadialPieSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RadialPieSeries.prototype.radiusYPropertyName, e.oldValue(), e.newValue());
}));

$.ig.Legend.prototype.orientationPropertyName = "Orientation";

} (jQuery));


