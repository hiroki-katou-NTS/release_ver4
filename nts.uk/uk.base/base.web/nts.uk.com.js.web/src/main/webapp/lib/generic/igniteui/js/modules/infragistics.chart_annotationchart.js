/*!@license
* Infragistics.Web.ClientUI infragistics.chart_annotationchart.js 16.1.20161.2145
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
"Point:bc", 
"Math:bd", 
"MulticastDelegate:bg", 
"IntPtr:bh", 
"Script:cc", 
"Action$1:ch", 
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
"Func$1:ib", 
"InterpolationUtil:jj", 
"Func$5:jk", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


$.ig.util.defType('InterpolationUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	interpolatePoints: function (interpolatedPoints, p, minPoints, maxPoints) {
		var q = 1 - p;
		if (interpolatedPoints == null) {
			interpolatedPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		}
		if (minPoints == null) {
			minPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		}
		var minCount = minPoints.count();
		var maxCount = maxPoints.count();
		var count = Math.max(minCount, maxCount);
		var interpolatedCount = interpolatedPoints.count();
		if (interpolatedCount < count) {
			interpolatedPoints.insertRange(interpolatedCount, new Array(count - interpolatedCount));
		}
		if (interpolatedCount > count) {
			interpolatedPoints.removeRange(count, interpolatedCount - count);
		}
		for (var i = 0; i < Math.min(minCount, maxCount); ++i) {
			interpolatedPoints.__inner[i] = { __x: (minPoints.__inner[i].__x * q) + (maxPoints.__inner[i].__x * p), __y: (minPoints.__inner[i].__y * q) + (maxPoints.__inner[i].__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (minCount < maxCount) {
			var mn = minCount > 0 ? minPoints.__inner[minCount - 1] : { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			for (var i1 = minCount; i1 < maxCount; ++i1) {
				interpolatedPoints.__inner[i1] = { __x: (mn.__x * q) + (maxPoints.__inner[i1].__x * p), __y: (mn.__y * q) + (maxPoints.__inner[i1].__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			}
		}
		if (minCount > maxCount) {
			var mx = maxCount > 0 ? maxPoints.__inner[maxCount - 1] : { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			for (var i2 = maxCount; i2 < minCount; ++i2) {
				interpolatedPoints.__inner[i2] = interpolatedPoints.__inner[i2] = { __x: (minPoints.__inner[i2].__x * q) + (mx.__x * p), __y: (minPoints.__inner[i2].__y * q) + (mx.__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			}
		}
		return interpolatedPoints;
	}
	,
	interpolateValues$1: function ($t, interpolatedValues, p, minValues, maxValues, createEmpty, interpolate) {
		var q = 1 - p;
		if (interpolatedValues == null) {
			interpolatedValues = new $.ig.List$1($t, 0);
		}
		if (minValues == null) {
			minValues = new $.ig.List$1($t, 0);
		}
		var minCount = minValues.count();
		var maxCount = maxValues.count();
		var count = Math.max(minCount, maxCount);
		if (interpolatedValues.count() < count) {
			var capacity = count - interpolatedValues.count();
			var range = new Array(capacity);
			interpolatedValues.insertRange(interpolatedValues.count(), range);
		}
		if (interpolatedValues.count() > count) {
			interpolatedValues.removeRange(count, interpolatedValues.count() - count);
		}
		for (var i = 0; i < Math.min(minCount, maxCount); ++i) {
			interpolatedValues.__inner[i] = interpolate(p, q, minValues.__inner[i], maxValues.__inner[i]);
		}
		if (minCount < maxCount) {
			var mn = minCount > 0 ? minValues.__inner[minCount - 1] : createEmpty();
			for (var i1 = minCount; i1 < maxCount; ++i1) {
				interpolatedValues.__inner[i1] = interpolate(p, q, mn, maxValues.__inner[i1]);
			}
		}
		if (minCount > maxCount) {
			var mx = maxCount > 0 ? maxValues.__inner[maxCount - 1] : createEmpty();
			for (var i2 = maxCount; i2 < minCount; ++i2) {
				interpolatedValues.__inner[i2] = interpolate(p, q, minValues.__inner[i2], mx);
			}
		}
		return interpolatedValues;
	}
	,
	$type: new $.ig.Type('InterpolationUtil', $.ig.Object.prototype.$type)
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
"CategoryToolTipLayer:w9", 
"CategoryToolTipLayerView:xa", 
"PointerTooltip:xb", 
"PointerTooltipView:xc", 
"PointerTooltipPointerLocation:xd", 
"CategoryTooltipLayerPosition:xe", 
"CategoryToolTipLayerFrame:xf", 
"CategoryTooltipSeriesInfo:xg", 
"ItemToolTipLayer:xi", 
"ItemToolTipLayerView:xj", 
"ItemTooltipLayerFrame:xk", 
"Func$5:xl", 
"InterpolationUtil:xm", 
"ItemTooltipCollisionInfo:xn", 
"CategoryHighlightLayer:x4", 
"CategoryHighlightLayerView:x5", 
"CategoryHighlightLayerFrame:x6", 
"CategoryHighlightFrameRect:x7", 
"CategoryItemHighlightLayer:x8", 
"CategoryItemHighlightLayerView:x9", 
"CategoryItemHighlightType:ya", 
"CategoryItemHighlightLayerFrame:yb", 
"ItemHighlightFrameRect:yc", 
"CrosshairLayer:yd", 
"CrosshairLayerView:ye", 
"CrosshairLayerFrame:yf", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('CategoryItemHighlightType', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Auto";
			case 1: return "Marker";
			case 2: return "Shape";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('CategoryItemHighlightType', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('PointerTooltipPointerLocation', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Auto";
			case 1: return "TopLeft";
			case 2: return "TopMiddle";
			case 3: return "TopRight";
			case 4: return "RightTop";
			case 5: return "RightMiddle";
			case 6: return "RightBottom";
			case 7: return "BottomRight";
			case 8: return "BottomMiddle";
			case 9: return "BottomLeft";
			case 10: return "LeftBottom";
			case 11: return "LeftMiddle";
			case 12: return "LeftTop";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('PointerTooltipPointerLocation', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('CategoryTooltipLayerPosition', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Auto";
			case 1: return "OutsideStart";
			case 2: return "InsideStart";
			case 3: return "InsideEnd";
			case 4: return "OutsideEnd";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('CategoryTooltipLayerPosition', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('AnnotationLayer', 'Series', {
	init: function () {
		this.__previousPoint = { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		this._currentPosition = { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		this.__shouldRenderAsOverlay = false;
		$.ig.Series.prototype.init.call(this);
		this.userCursorPosition({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	},
	createView: function () {
		return new $.ig.AnnotationLayerView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.Series.prototype.onViewCreated.call(this, view);
		this.annotationView(view);
	}
	,
	isSeriesValid: function (series) {
		if (series == null) {
			return false;
		}
		if (series.isAnnotationLayer()) {
			return false;
		}
		if (series == this) {
			return false;
		}
		return series.validateSeries(series.view().viewport(), series.view().windowRect(), series.view());
	}
	,
	_annotationView: null,
	annotationView: function (value) {
		if (arguments.length === 1) {
			this._annotationView = value;
			return value;
		} else {
			return this._annotationView;
		}
	}
	,
	useIndex: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnnotationLayer.prototype.useIndexProperty, value);
			return value;
		} else {
			return this.getValue($.ig.AnnotationLayer.prototype.useIndexProperty);
		}
	}
	,
	useLegend: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnnotationLayer.prototype.useLegendProperty, value);
			return value;
		} else {
			return this.getValue($.ig.AnnotationLayer.prototype.useLegendProperty);
		}
	}
	,
	cursorPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnnotationLayer.prototype.cursorPositionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.AnnotationLayer.prototype.cursorPositionProperty);
		}
	}
	,
	isDefaultCrosshairDisabled: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AnnotationLayer.prototype.isDefaultCrosshairDisabledProperty, value);
			return value;
		} else {
			return this.getValue($.ig.AnnotationLayer.prototype.isDefaultCrosshairDisabledProperty);
		}
	}
	,
	isDefaultCrosshairBehaviorDisabled: function () {
		return this.isDefaultCrosshairDisabled();
	}
	,
	isIndexed: function () {
		return this.useIndex() || this.useLegend();
	}
	,
	isUsableInLegend: function () {
		return this.useLegend();
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.Series.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.Series.prototype.seriesViewerPropertyName:
				var oldViewer = oldValue;
				var newViewer = newValue;
				if (oldViewer != null) {
					this.unregisterSeries(oldViewer.series());
					var $t = oldViewer.series();
					$t.collectionChanged = $.ig.Delegate.prototype.remove($t.collectionChanged, this.series_CollectionChanged.runOn(this));
					if ($.ig.util.cast($.ig.XamDataChart.prototype.$type, oldViewer) !== null) {
						var oldChart = oldViewer;
						this.unregisterAxes(oldChart.axes());
						var $t1 = oldChart.axes();
						$t1.collectionChanged = $.ig.Delegate.prototype.remove($t1.collectionChanged, this.axes_CollectionChanged.runOn(this));
					}
				}
				if (newViewer != null) {
					this.registerSeries(newViewer.series());
					var $t2 = newViewer.series();
					$t2.collectionChanged = $.ig.Delegate.prototype.combine($t2.collectionChanged, this.series_CollectionChanged.runOn(this));
					if ($.ig.util.cast($.ig.XamDataChart.prototype.$type, newViewer) !== null) {
						var newChart = newViewer;
						this.registerAxes(newChart.axes());
						var $t3 = newChart.axes();
						$t3.collectionChanged = $.ig.Delegate.prototype.combine($t3.collectionChanged, this.axes_CollectionChanged.runOn(this));
					}
				}
				break;
			case $.ig.AnnotationLayer.prototype.useLegendPropertyName:
			case $.ig.AnnotationLayer.prototype.useIndexPropertyName:
				if (this.isIndexed()) {
					if (this.index() == -1) {
						this.index($.ig.XamDataChart.prototype.findSeriesIndex(this));
					}
				} else {
					this.index(-1);
					this.actualBrush(null);
					this.actualOutline(null);
				}
				if (this.seriesViewer() != null) {
					this.seriesViewer().onLegendSortChanged();
				}
				this.renderSeries(true);
				break;
			case $.ig.Series.prototype.transitionProgressPropertyName:
				this.transitionFrame().interpolate3(this.transitionProgress(), this.previousFrame(), this.currentFrame());
				if (this.clearAndAbortIfInvalid1(this.view())) {
					return;
				}
				if (this.transitionProgress() == 1) {
					this.renderFrame(this.currentFrame(), this.view());
				} else {
					this.renderFrame(this.transitionFrame(), this.view());
				}
				break;
			case $.ig.AnnotationLayer.prototype.isDefaultCrosshairDisabledPropertyName:
				if (this.seriesViewer() != null) {
					this.seriesViewer().onHoverBehaviorOverridesChanged();
				}
				break;
			case $.ig.AnnotationLayer.prototype.cursorPositionPropertyName:
				this.userCursorPosition(this.cursorPosition());
				this.onCursorPointMoved(this.cursorPosition());
				break;
			case "ShouldRenderAsOverlay":
				if (this.seriesViewer() != null) {
					this.view().detachFromChart(this.seriesViewer());
					this.view().attachToChart(this.seriesViewer());
				}
				break;
		}
	}
	,
	_userCursorPosition: null,
	userCursorPosition: function (value) {
		if (arguments.length === 1) {
			this._userCursorPosition = value;
			return value;
		} else {
			return this._userCursorPosition;
		}
	}
	,
	unregisterSeries: function (seriesCollection) {
		var en = seriesCollection.getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			if (!(series == this)) {
				series.renderRequested = $.ig.Delegate.prototype.remove(series.renderRequested, this.series_RenderRequested.runOn(this));
			}
		}
		this.renderSeries(true);
	}
	,
	unregisterAxes: function (axisCollection) {
		var en = axisCollection.getEnumerator();
		while (en.moveNext()) {
			var axis = en.current();
			axis.renderRequested = $.ig.Delegate.prototype.remove(axis.renderRequested, this.axis_RenderRequested.runOn(this));
		}
		this.renderSeries(true);
	}
	,
	registerSeries: function (seriesCollection) {
		var en = seriesCollection.getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			if (!(series == this)) {
				series.renderRequested = $.ig.Delegate.prototype.combine(series.renderRequested, this.series_RenderRequested.runOn(this));
			}
		}
		this.renderSeries(true);
	}
	,
	registerAxes: function (axisCollection) {
		var en = axisCollection.getEnumerator();
		while (en.moveNext()) {
			var axis = en.current();
			axis.renderRequested = $.ig.Delegate.prototype.combine(axis.renderRequested, this.axis_RenderRequested.runOn(this));
		}
		this.renderSeries(true);
	}
	,
	axis_RenderRequested: function (sender, e) {
		this.onDependentAxisRender(sender, e.animate());
	}
	,
	series_RenderRequested: function (sender, e) {
		this.onDependentSeriesRender(sender, e.animate());
	}
	,
	onDependentAxisRender: function (axis, animate) {
	}
	,
	onDependentSeriesRender: function (series, animate) {
	}
	,
	series_CollectionChanged: function (sender, e) {
		this.onSeriesCollectionChanged(e);
	}
	,
	axes_CollectionChanged: function (sender, e) {
		this.onAxisCollectionChanged(e);
	}
	,
	getBrush: function () {
		if (this.actualBrush() != null) {
			return this.actualBrush();
		}
		return this.brush();
	}
	,
	getOutline: function () {
		if (this.actualOutline() != null) {
			return this.actualOutline();
		}
		return this.outline();
	}
	,
	onAxisCollectionChanged: function (e) {
		if (e.oldItems() != null) {
			var en = e.oldItems().getEnumerator();
			while (en.moveNext()) {
				var axis = en.current();
				axis.renderRequested = $.ig.Delegate.prototype.remove(axis.renderRequested, this.axis_RenderRequested.runOn(this));
			}
		}
		if (e.newItems() != null) {
			var en1 = e.newItems().getEnumerator();
			while (en1.moveNext()) {
				var axis1 = en1.current();
				axis1.renderRequested = $.ig.Delegate.prototype.combine(axis1.renderRequested, this.axis_RenderRequested.runOn(this));
			}
		}
		this.renderSeries(true);
	}
	,
	onSeriesCollectionChanged: function (e) {
		if (e.oldItems() != null) {
			var en = e.oldItems().getEnumerator();
			while (en.moveNext()) {
				var series = en.current();
				series.renderRequested = $.ig.Delegate.prototype.remove(series.renderRequested, this.series_RenderRequested.runOn(this));
			}
		}
		if (e.newItems() != null) {
			var en1 = e.newItems().getEnumerator();
			while (en1.moveNext()) {
				var series1 = en1.current();
				series1.renderRequested = $.ig.Delegate.prototype.combine(series1.renderRequested, this.series_RenderRequested.runOn(this));
			}
		}
		this.renderSeries(true);
	}
	,
	_previousFrame: null,
	previousFrame: function (value) {
		if (arguments.length === 1) {
			this._previousFrame = value;
			return value;
		} else {
			return this._previousFrame;
		}
	}
	,
	_currentFrame: null,
	currentFrame: function (value) {
		if (arguments.length === 1) {
			this._currentFrame = value;
			return value;
		} else {
			return this._currentFrame;
		}
	}
	,
	_transitionFrame: null,
	transitionFrame: function (value) {
		if (arguments.length === 1) {
			this._transitionFrame = value;
			return value;
		} else {
			return this._transitionFrame;
		}
	}
	,
	windowRectChangedOverride: function (oldWindowRect, newWindowRect) {
		this.renderSeries(false);
	}
	,
	viewportRectChangedOverride: function (oldWindowRect, newWindowRect) {
		this.renderSeries(true);
	}
	,
	renderSeriesOverride: function (animate) {
		$.ig.Series.prototype.renderSeriesOverride.call(this, animate);
		if (this.clearAndAbortIfInvalid1(this.view())) {
			return;
		}
		if (this.shouldAnimate(animate) && !this.skipPrepare()) {
			var previousFrame = this.previousFrame();
			if (this.animationActive()) {
				if (this.animator().needsFlush()) {
					this.animator().flush();
				}
				this.previousFrame(this.transitionFrame());
				this.transitionFrame(previousFrame);
			} else {
				this.previousFrame(this.currentFrame());
				this.currentFrame(previousFrame);
			}
			this.prepareFrame(this.currentFrame(), this.view());
			this.startAnimation();
		} else {
			if (!this.skipPrepare()) {
				this.prepareFrame(this.currentFrame(), this.view());
			}
			this.renderFrame(this.currentFrame(), this.view());
		}
	}
	,
	isAnnotationLayer: function () {
		return true;
	}
	,
	prepareFrame: function (frame, view) {
	}
	,
	renderFrame: function (frame, view) {
		this.annotationView().onRenderingFrame();
	}
	,
	__previousPoint: null,
	moveCursorPoint: function (point) {
		if (this.__previousPoint.__x != point.__x || this.__previousPoint.__y != point.__y) {
			this.__previousPoint = point;
			this.onCursorPointMoved(point);
		}
	}
	,
	getCategoryPosition: function (series) {
		var pos = { __x: this._currentPosition.__x, __y: this._currentPosition.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (series.isVertical()) {
			if ($.ig.util.isNaN(this._currentPosition.__y)) {
				return new $.ig.Tuple$2($.ig.Boolean.prototype.$type, $.ig.Point.prototype.$type, false, pos);
			}
			if ($.ig.util.isNaN(this._currentPosition.__x)) {
				pos.__x = 0;
				return new $.ig.Tuple$2($.ig.Boolean.prototype.$type, $.ig.Point.prototype.$type, true, pos);
			}
		} else {
			if ($.ig.util.isNaN(this._currentPosition.__x)) {
				return new $.ig.Tuple$2($.ig.Boolean.prototype.$type, $.ig.Point.prototype.$type, false, pos);
			}
			if ($.ig.util.isNaN(this._currentPosition.__y)) {
				pos.__y = 0;
				return new $.ig.Tuple$2($.ig.Boolean.prototype.$type, $.ig.Point.prototype.$type, true, pos);
			}
		}
		return new $.ig.Tuple$2($.ig.Boolean.prototype.$type, $.ig.Point.prototype.$type, true, pos);
	}
	,
	onCursorPointMoved: function (point) {
		if ($.ig.util.isNaN(this.userCursorPosition().__x) && $.ig.util.isNaN(this.userCursorPosition().__y)) {
			if ($.ig.util.isNaN(point.__x) && $.ig.util.isNaN(point.__y)) {
				this.annotationView().deferPositionClear();
				return;
			} else {
				this.annotationView().resetDeferredClear();
				this._currentPosition = point;
			}
		} else {
			this.annotationView().resetDeferredClear();
			this._currentPosition = this.userCursorPosition();
		}
		this.renderSeries(true);
	}
	,
	_currentPosition: null,
	validateSeries: function (viewportRect, windowRect, view) {
		var valid = $.ig.Series.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		if (!this.view().ready()) {
			return valid = false;
		}
		return valid;
	}
	,
	useOverlaySchdeduler: function () {
		return this.shouldRenderAsOverlay();
	}
	,
	__shouldRenderAsOverlay: false,
	shouldRenderAsOverlay: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__shouldRenderAsOverlay;
			this.__shouldRenderAsOverlay = value;
			this.raisePropertyChanged("ShouldRenderAsOverlay", oldValue, this.__shouldRenderAsOverlay);
			return value;
		} else {
			return this.__shouldRenderAsOverlay;
		}
	}
	,
	$type: new $.ig.Type('AnnotationLayer', $.ig.Series.prototype.$type)
}, true);

$.ig.util.defType('CategoryToolTipLayer', 'AnnotationLayer', {
	init: function () {
		$.ig.AnnotationLayer.prototype.init.call(this);
		var previousFrame = new $.ig.CategoryToolTipLayerFrame();
		var currentFrame = new $.ig.CategoryToolTipLayerFrame();
		var transitionFrame = new $.ig.CategoryToolTipLayerFrame();
		this.previousFrame(previousFrame);
		this.currentFrame(currentFrame);
		this.transitionFrame(transitionFrame);
		this.defaultStyleKey($.ig.CategoryToolTipLayer.prototype.$type);
	},
	createView: function () {
		return new $.ig.CategoryToolTipLayerView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AnnotationLayer.prototype.onViewCreated.call(this, view);
		this.categoryToolTipView(view);
	}
	,
	_categoryToolTipView: null,
	categoryToolTipView: function (value) {
		if (arguments.length === 1) {
			this._categoryToolTipView = value;
			return value;
		} else {
			return this._categoryToolTipView;
		}
	}
	,
	isDefaultTooltipBehaviorDisabled: function () {
		return true;
	}
	,
	targetAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryToolTipLayer.prototype.targetAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryToolTipLayer.prototype.targetAxisProperty);
		}
	}
	,
	useInterpolation: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryToolTipLayer.prototype.useInterpolationProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryToolTipLayer.prototype.useInterpolationProperty);
		}
	}
	,
	toolTipPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryToolTipLayer.prototype.toolTipPositionProperty, $.ig.CategoryTooltipLayerPosition.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.CategoryToolTipLayer.prototype.toolTipPositionProperty));
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.AnnotationLayer.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.CategoryToolTipLayer.prototype.targetAxisPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.CategoryToolTipLayer.prototype.useInterpolationPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.CategoryToolTipLayer.prototype.toolTipPositionPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.Series.prototype.seriesViewerPropertyName:
				this.categoryToolTipView().onSeriesViewerChanged();
				break;
		}
	}
	,
	onDependentAxisRender: function (axis, animate) {
		if (this.targetAxis() == axis) {
			if (!this.contentInfo().isDirty()) {
				this.renderSeries(animate);
			}
		}
	}
	,
	onDependentSeriesRender: function (series, animate) {
		if (!this.contentInfo().isDirty()) {
			this.renderSeries(animate);
		}
	}
	,
	prepareFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.prepareFrame.call(this, frame, view);
		var f = frame;
		var useInterpolation = this.useInterpolation();
		f.tooltipYCoord(NaN);
		f.tooltipXCoord(NaN);
		f.pointerPositionXCoord(NaN);
		f.pointerPositionYCoord(NaN);
		f.tooltipXCoord(NaN);
		f.tooltipYCoord(NaN);
		if ($.ig.util.isNaN(this._currentPosition.__x) && $.ig.util.isNaN(this._currentPosition.__y)) {
			this.categoryToolTipView().hideContainer(this.categoryToolTipView().getContainer());
			return;
		}
		if (this.targetAxis() != null) {
			var axis = this.targetAxis();
			if (axis == null || !axis.isCategory() || axis.isAngular()) {
				return;
			}
			if (!axis.isValid()) {
				return;
			}
			var catAxis = axis;
			var seriesInfo = this.getSeriesInfo(catAxis.series(), useInterpolation);
			this.prepareSeries(seriesInfo, f, useInterpolation);
		} else {
			if ($.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer()) !== null) {
				var chart = this.seriesViewer();
				var first = true;
				var processVertical = false;
				var processSeries = new $.ig.List$1($.ig.Series.prototype.$type, 0);
				var en = chart.axes().getEnumerator();
				while (en.moveNext()) {
					var axis1 = en.current();
					if (axis1.isCategory() && !axis1.isAngular()) {
						if (!axis1.isValid()) {
							continue;
						}
						if (first) {
							first = false;
							processVertical = axis1.isVertical();
						} else {
							if (axis1.isVertical() != processVertical) {
								continue;
							}
						}
						var en1 = axis1.series().getEnumerator();
						while (en1.moveNext()) {
							var series = en1.current();
							processSeries.add(series);
						}
					}
				}
				var seriesInfo1 = this.getSeriesInfo(processSeries, useInterpolation);
				this.prepareSeries(seriesInfo1, f, useInterpolation);
			}
		}
	}
	,
	getSeriesInfo: function (series, useInterpolation) {
		var minPos = 1.7976931348623157E+308;
		var maxPos = -1.7976931348623157E+308;
		var contexts = new $.ig.List$1($.ig.DataContext.prototype.$type, 0);
		var tooltipObjects = new $.ig.List$1($.ig.Object.prototype.$type, 0);
		var isVertical = false;
		var first = true;
		for (var i = 0; i < series.count(); i++) {
			var currSeries = series.item(i);
			if (!currSeries.isFinancial() && !currSeries.isCategory()) {
				continue;
			}
			if (currSeries.isStacked()) {
				continue;
			}
			var catSeries = currSeries;
			if (!catSeries.categoryAxis().isValid()) {
				continue;
			}
			if (first) {
				isVertical = currSeries.isVertical();
			} else {
				if (isVertical != currSeries.isVertical()) {
					continue;
				}
			}
			var res = this.getCategoryPosition(currSeries);
			if (!res.item1()) {
				continue;
			}
			var pos = currSeries.getSeriesValuePosition(res.item2(), useInterpolation, false);
			if (isVertical) {
				if (!$.ig.util.isNaN(pos.__y)) {
					minPos = Math.min(minPos, pos.__y);
					maxPos = Math.max(maxPos, pos.__y);
				}
			} else {
				if (!$.ig.util.isNaN(pos.__x)) {
					minPos = Math.min(minPos, pos.__x);
					maxPos = Math.max(maxPos, pos.__x);
				}
			}
			var obj = this.getTooltipObject(currSeries);
			if (obj == null) {
				continue;
			}
			var context = null;
			var item = currSeries.getItem(this._currentPosition);
			if (currSeries.isDefaultToolTipSelected() && item != null) {
				context = currSeries.getTooltipContext(item);
			} else {
				context = new $.ig.DataContext();
				context.item(item);
				context.series(currSeries);
			}
			contexts.add(context);
			tooltipObjects.add1(obj);
		}
		var posX = 0;
		var posY = 0;
		if (isVertical) {
			posY = (minPos + maxPos) / 2;
			posX = this.viewport().right();
			if (this.toolTipPosition() == $.ig.CategoryTooltipLayerPosition.prototype.insideStart || this.toolTipPosition() == $.ig.CategoryTooltipLayerPosition.prototype.outsideStart) {
				posX = this.viewport().left();
			}
			if (posY < 0 || posY > this.viewport().bottom()) {
				posY = NaN;
			}
		} else {
			posX = (minPos + maxPos) / 2;
			posY = this.viewport().top();
			if (this.toolTipPosition() == $.ig.CategoryTooltipLayerPosition.prototype.insideStart || this.toolTipPosition() == $.ig.CategoryTooltipLayerPosition.prototype.outsideStart) {
				posY = this.viewport().bottom();
			}
			if (posX < 0 || posX > this.viewport().right()) {
				posX = NaN;
			}
		}
		var ret = new $.ig.CategoryTooltipSeriesInfo();
		ret.position({ __x: posX, __y: posY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		ret.dataContexts(contexts);
		ret.tooltipObjects(tooltipObjects);
		return ret;
	}
	,
	prepareSeries: function (seriesInfo, f, useInterpolation) {
		var startPosition = seriesInfo.position();
		var contexts = seriesInfo.dataContexts();
		var tooltipObjects = seriesInfo.tooltipObjects();
		startPosition = this.categoryToolTipView().relativeToAbsolute(startPosition);
		var tooltipCount = 0;
		var viewport = this.view().viewport();
		var container = this.categoryToolTipView().getContainer();
		this.categoryToolTipView().clearTooltipContent();
		var isVertical = false;
		if (contexts.count() == 0) {
			this.categoryToolTipView().hideTooltip();
			return;
		}
		var anyTips = false;
		for (var i = 0; i < contexts.count(); i++) {
			var series = contexts.__inner[i].series();
			isVertical = series.isVertical();
			var context = contexts.__inner[i];
			var obj = tooltipObjects.__inner[i];
			if (this.categoryToolTipView().configureTooltip(series, obj, context)) {
				anyTips = true;
			}
			tooltipCount++;
		}
		if (!anyTips) {
			this.categoryToolTipView().hideContainer(container);
			return;
		}
		var size = this.categoryToolTipView().getTooltipSize(container);
		var pointerY = 0;
		var pointerX = 0;
		var posX = 0;
		var posY = 0;
		var atStart = false;
		var inside = false;
		var isAuto = false;
		var toolTipPosition = this.toolTipPosition();
		if (toolTipPosition == $.ig.CategoryTooltipLayerPosition.prototype.auto) {
			isAuto = true;
		}
		if (toolTipPosition == $.ig.CategoryTooltipLayerPosition.prototype.insideStart || toolTipPosition == $.ig.CategoryTooltipLayerPosition.prototype.insideEnd) {
			inside = true;
		}
		if (toolTipPosition == $.ig.CategoryTooltipLayerPosition.prototype.insideStart || toolTipPosition == $.ig.CategoryTooltipLayerPosition.prototype.outsideStart) {
			atStart = true;
		}
		var totalSize = this.categoryToolTipView().getValidAreaSize();
		if (isVertical) {
			if (!atStart && !inside && startPosition.__x + (size.width() + 10) > totalSize.width()) {
				inside = true;
			}
			if (atStart && !inside && startPosition.__x - (size.width() + 10) < 0) {
				inside = true;
			}
		} else {
			if (!atStart && !inside && startPosition.__y - (size.height() + 10) < 0) {
				inside = true;
			}
			if (atStart && !inside && startPosition.__y + (size.height() + 10) > totalSize.height()) {
				inside = true;
			}
		}
		var extraPointerOffset = 10;
		var startOffset = 5;
		if (!atStart) {
			if (!isVertical) {
				startOffset = size.height() + extraPointerOffset;
			}
			if (inside) {
				if (isVertical) {
					startOffset = startOffset + size.width() + extraPointerOffset;
				} else {
					startOffset = 0;
				}
			}
		} else {
			if (isVertical) {
				startOffset = size.width() + extraPointerOffset;
				if (inside) {
					startOffset = 0;
				}
			} else {
				startOffset = 5;
				if (inside) {
					startOffset = startOffset + size.height() + extraPointerOffset;
				}
			}
		}
		if (isVertical) {
			pointerX = 0 - (extraPointerOffset + 5);
			if (atStart) {
				pointerX = (pointerX * -1) + size.width();
			}
			pointerY = size.height() / 2;
			posY = startPosition.__y - size.height() / 2;
			posX = startPosition.__x - startOffset;
		} else {
			pointerY = size.height() + 10 + 5;
			if (atStart) {
				pointerY = 0 - (extraPointerOffset + 5);
			}
			pointerX = size.width() / 2;
			posX = startPosition.__x - size.width() / 2;
			posY = startPosition.__y - startOffset;
		}
		if (isVertical) {
			if (posY < 0) {
				pointerY -= 0 - posY;
				posY = 0;
			}
			if (posY + size.height() > totalSize.height()) {
				pointerY += (posY + size.height()) - totalSize.height();
				posY = totalSize.height() - size.height();
			}
		} else {
			if (posX < 0) {
				pointerX -= 0 - posX;
				posX = 0;
			}
			if (posX + size.width() > totalSize.width()) {
				pointerX += (posX + size.width()) - totalSize.width();
				posX = totalSize.width() - size.width();
			}
		}
		f.tooltipXCoord(posX);
		f.tooltipYCoord(posY);
		f.pointerPositionXCoord(pointerX);
		f.pointerPositionYCoord(pointerY);
		f.tooltipWidth(size.width());
		f.tooltipHeight(size.height());
	}
	,
	getTooltipObject: function (series) {
		return series.toolTip();
	}
	,
	renderFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.renderFrame.call(this, frame, view);
		var f = frame;
		var left = view.viewport().left();
		var right = view.viewport().right();
		var top = view.viewport().top();
		var bottom = view.viewport().bottom();
		var container = this.categoryToolTipView().getContainer();
		if ($.ig.util.isNaN(f.tooltipXCoord()) || $.ig.util.isNaN(f.tooltipYCoord()) || $.ig.util.isNaN(f.pointerPositionXCoord()) || $.ig.util.isNaN(f.pointerPositionYCoord())) {
			this.categoryToolTipView().hideContainer(container);
			return;
		}
		var displayX = f.tooltipXCoord();
		var displayY = f.tooltipYCoord();
		var pointerPosX = f.pointerPositionXCoord();
		var pointerPosY = f.pointerPositionYCoord();
		this.categoryToolTipView().moveTooltip(container, displayX, displayY, pointerPosX, pointerPosY);
	}
	,
	$type: new $.ig.Type('CategoryToolTipLayer', $.ig.AnnotationLayer.prototype.$type)
}, true);

$.ig.util.defType('CategoryToolTipLayerFrame', 'Frame', {
	__y: 0,
	tooltipYCoord: function (value) {
		if (arguments.length === 1) {
			this.__y = value;
			return value;
		} else {
			return this.__y;
		}
	}
	,
	_tooltipXCoord: 0,
	tooltipXCoord: function (value) {
		if (arguments.length === 1) {
			this._tooltipXCoord = value;
			return value;
		} else {
			return this._tooltipXCoord;
		}
	}
	,
	_pointerPositionYCoord: 0,
	pointerPositionYCoord: function (value) {
		if (arguments.length === 1) {
			this._pointerPositionYCoord = value;
			return value;
		} else {
			return this._pointerPositionYCoord;
		}
	}
	,
	_pointerPositionXCoord: 0,
	pointerPositionXCoord: function (value) {
		if (arguments.length === 1) {
			this._pointerPositionXCoord = value;
			return value;
		} else {
			return this._pointerPositionXCoord;
		}
	}
	,
	_tooltipWidth: 0,
	tooltipWidth: function (value) {
		if (arguments.length === 1) {
			this._tooltipWidth = value;
			return value;
		} else {
			return this._tooltipWidth;
		}
	}
	,
	_tooltipHeight: 0,
	tooltipHeight: function (value) {
		if (arguments.length === 1) {
			this._tooltipHeight = value;
			return value;
		} else {
			return this._tooltipHeight;
		}
	}
	,
	init: function () {
		$.ig.Frame.prototype.init.call(this);
		this.tooltipXCoord(NaN);
		this.tooltipYCoord(NaN);
		this.pointerPositionXCoord(NaN);
		this.pointerPositionYCoord(NaN);
		this.tooltipWidth(NaN);
		this.tooltipHeight(NaN);
	},
	interpolate3: function (p, min, max) {
		var mn = min;
		var mx = max;
		if ($.ig.util.isNaN(mn.tooltipXCoord())) {
			this.tooltipXCoord(mx.tooltipXCoord());
		} else {
			this.tooltipXCoord(mn.tooltipXCoord() + (mx.tooltipXCoord() - mn.tooltipXCoord()) * p);
		}
		if ($.ig.util.isNaN(mn.tooltipYCoord())) {
			this.tooltipYCoord(mx.tooltipYCoord());
		} else {
			this.tooltipYCoord(mn.tooltipYCoord() + (mx.tooltipYCoord() - mn.tooltipYCoord()) * p);
		}
		if ($.ig.util.isNaN(mn.pointerPositionXCoord())) {
			this.pointerPositionXCoord(mx.pointerPositionXCoord());
		} else {
			this.pointerPositionXCoord(mn.pointerPositionXCoord() + (mx.pointerPositionXCoord() - mn.pointerPositionXCoord()) * p);
		}
		if ($.ig.util.isNaN(mn.pointerPositionYCoord())) {
			this.pointerPositionYCoord(mx.pointerPositionYCoord());
		} else {
			this.pointerPositionYCoord(mn.pointerPositionYCoord() + (mx.pointerPositionYCoord() - mn.pointerPositionYCoord()) * p);
		}
		if ($.ig.util.isNaN(mn.tooltipWidth())) {
			this.tooltipWidth(mx.tooltipWidth());
		} else {
			this.tooltipWidth(mn.tooltipWidth() + (mx.tooltipWidth() - mn.tooltipWidth()) * p);
		}
		if ($.ig.util.isNaN(mn.tooltipHeight())) {
			this.tooltipHeight(mx.tooltipXCoord());
		} else {
			this.tooltipHeight(mn.tooltipHeight() + (mx.tooltipHeight() - mn.tooltipHeight()) * p);
		}
	}
	,
	$type: new $.ig.Type('CategoryToolTipLayerFrame', $.ig.Frame.prototype.$type)
}, true);

$.ig.util.defType('CategoryTooltipSeriesInfo', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_position: null,
	position: function (value) {
		if (arguments.length === 1) {
			this._position = value;
			return value;
		} else {
			return this._position;
		}
	}
	,
	_dataContexts: null,
	dataContexts: function (value) {
		if (arguments.length === 1) {
			this._dataContexts = value;
			return value;
		} else {
			return this._dataContexts;
		}
	}
	,
	_tooltipObjects: null,
	tooltipObjects: function (value) {
		if (arguments.length === 1) {
			this._tooltipObjects = value;
			return value;
		} else {
			return this._tooltipObjects;
		}
	}
	,
	$type: new $.ig.Type('CategoryTooltipSeriesInfo', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('AnnotationLayerView', 'SeriesView', {
	init: function (model) {
		this.__clearDeferred = false;
		this.__timerId = -1;
		$.ig.SeriesView.prototype.init.call(this, model);
		this.annotationModel(model);
	},
	_annotationModel: null,
	annotationModel: function (value) {
		if (arguments.length === 1) {
			this._annotationModel = value;
			return value;
		} else {
			return this._annotationModel;
		}
	}
	,
	__clearDeferred: false,
	__timerId: 0,
	tick: function () {
		if (this.__clearDeferred) {
			this.__clearDeferred = false;
			this.__timerId = -1;
			this.annotationModel()._currentPosition = { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			this.annotationModel().renderSeries(true);
		}
	}
	,
	deferPositionClear: function () {
		this.__clearDeferred = true;
		this.__timerId = window.setTimeout(this.tick.runOn(this), 300);
	}
	,
	resetDeferredClear: function () {
		if (this.__clearDeferred == true) {
			this.__clearDeferred = false;
			if (this.__timerId != -1) {
				window.clearTimeout(this.__timerId);
				this.__timerId = -1;
			}
		}
	}
	,
	onRenderingFrame: function () {
		this.makeDirty();
	}
	,
	$type: new $.ig.Type('AnnotationLayerView', $.ig.SeriesView.prototype.$type)
}, true);

$.ig.util.defType('CategoryToolTipLayerView', 'AnnotationLayerView', {
	init: function (model) {
		this.__container = null;
		this.__tooltips = null;
		this.__otherTooltips = null;
		this.__attachToContainer = false;
		$.ig.AnnotationLayerView.prototype.init.call(this, model);
		this.categoryToolTipModel(model);
		this.htmlTest(/^[^<]*(<[\w\W]+>)[^>]*$/);
	},
	_categoryToolTipModel: null,
	categoryToolTipModel: function (value) {
		if (arguments.length === 1) {
			this._categoryToolTipModel = value;
			return value;
		} else {
			return this._categoryToolTipModel;
		}
	}
	,
	__container: null,
	__tooltips: null,
	__otherTooltips: null,
	__attachToContainer: false,
	getContainer: function () {
		if (this.__container == null) {
			this.__container = new $.ig.PointerTooltip();
			this.__container.eventSink(this.model().seriesViewer().view().eventProxy());
			this.__tooltips = $("<div></div>");
			this.__otherTooltips = $("<div></div>");
			this.__container.__visibility = $.ig.Visibility.prototype.visible;
		}
		return this.__container;
	}
	,
	onInit: function () {
		$.ig.AnnotationLayerView.prototype.onInit.call(this);
		if (this.__container != null) {
			this.__container.__visibility = $.ig.Visibility.prototype.collapsed;
		}
	}
	,
	destroy: function () {
		$.ig.AnnotationLayerView.prototype.destroy.call(this);
		if (this.__container != null) {
			this.__container.destroy();
			this.__container = null;
		}
	}
	,
	_htmlTest: null,
	htmlTest: function (value) {
		if (arguments.length === 1) {
			this._htmlTest = value;
			return value;
		} else {
			return this._htmlTest;
		}
	}
	,
	configureTooltip: function (series, obj, context) {
		if (series.tooltipTemplate() == "default") {
			series.tooltipTemplate(series.view().getDefaultTooltipTemplate());
		}
		var template_ = series.tooltipTemplate();
		var context_ = context;
		var tmplExists = $.ig.tmpl !== null;
		if (!tmplExists) {
			return false;
		}
		if (series.flattenEventArgs() == null) {
			return false;
		}
		var args_ = series.flattenEventArgs()(context);
		var expanded_ = $.ig.tmpl(template_, args_).toString();
		if (!this.htmlTest().test(expanded_)) {
			expanded_ = "<span>" + expanded_ + "</span>";
		}
		var content = $(expanded_);
		this.__tooltips.append(content);
		return true;
	}
	,
	getTooltipSize: function (container) {
		var p = container;
		p.content(this.__tooltips);
		var availableSize = new $.ig.Size(1, Number.POSITIVE_INFINITY, Number.POSITIVE_INFINITY);
		p.pointerVisibility($.ig.Visibility.prototype.collapsed);
		var size = p.measureCore(new $.ig.Size(1, Number.POSITIVE_INFINITY, Number.POSITIVE_INFINITY));
		p.pointerVisibility($.ig.Visibility.prototype.visible);
		return size;
	}
	,
	relativeToAbsolute: function (pt) {
		var containerOffsetX = 0;
		var containerOffsetY = 0;
		if (this.model().seriesViewer() != null) {
			var offsets = this.model().seriesViewer().getContainerOffsets();
			containerOffsetX += offsets.__x;
			containerOffsetY += offsets.__y;
		}
		return { __x: containerOffsetX + pt.__x, __y: containerOffsetY + pt.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	moveTooltip: function (container, x, y, pointerPosX, pointerPosY) {
		var p = container;
		var cc = p.content();
		p.pointerPosition({ __x: pointerPosX, __y: pointerPosY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		p.__visibility = $.ig.Visibility.prototype.visible;
		p.canvasLeft(x);
		p.canvasTop(y);
	}
	,
	getValidAreaSize: function () {
		var x = $(window).width();
		var y = $(window).height();
		return new $.ig.Size(1, x, y);
	}
	,
	hideContainer: function (container) {
		this.__tooltips.children().remove();
		this.__otherTooltips.children().remove();
		this.__container.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	clearTooltipContent: function () {
		if (this.__tooltips != null) {
			var swap = this.__tooltips;
			this.__tooltips = this.__otherTooltips;
			this.__otherTooltips = swap;
			this.__tooltips.children().remove();
		}
	}
	,
	onSeriesViewerChanged: function () {
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnnotationLayerView.prototype.renderOverride.call(this, context, isHitContext);
		if (isHitContext) {
			return;
		}
		var style = null;
		if (this.model().seriesViewer() != null) {
			style = this.model().seriesViewer().view().viewManager().getPointerTooltipStyle();
		}
		var outerOffsetX = 0;
		var outerOffsetY = 0;
		var seriesViewer = this.model().seriesViewer();
		if (seriesViewer != null) {
			var offsets = seriesViewer.view().viewManager().getContainerOffsets();
			outerOffsetX += offsets.__x;
			outerOffsetY += offsets.__y;
		}
		if (this.__container != null) {
			this.__container.pointerTooltipStyle(style);
			this.__container.render(this.__container.canvasLeft(), this.__container.canvasTop());
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnnotationLayerView.prototype.exportViewShapes.call(this, svd);
		var t = this.__container;
		var containerOffsetX = 0;
		var containerOffsetY = 0;
		if (this.model().seriesViewer() != null) {
			var offsets = this.model().seriesViewer().getContainerOffsets();
			containerOffsetX += offsets.__x;
			containerOffsetY += offsets.__y;
		}
		var vd = t.exportVisualData();
		vd.offsetX(t.canvasLeft() - containerOffsetX);
		vd.offsetY(t.canvasTop() - containerOffsetY);
		vd.categoryNames(new $.ig.List$1(String, 0));
		var length = this.__tooltips.children().length;
		for (var index = 0; index < length; index++) {
			var indexliteral_ = index.toString();
			var child = $(this.__tooltips.children()[indexliteral_]).text();;
			var name = child.toString();
			vd.categoryNames().add(name);
		}
		svd.pointerTooltips().add(vd);
	}
	,
	$type: new $.ig.Type('CategoryToolTipLayerView', $.ig.AnnotationLayerView.prototype.$type)
}, true);

$.ig.util.defType('ItemToolTipLayer', 'AnnotationLayer', {
	init: function () {
		$.ig.AnnotationLayer.prototype.init.call(this);
		var previousFrame = new $.ig.ItemTooltipLayerFrame();
		var currentFrame = new $.ig.ItemTooltipLayerFrame();
		var transitionFrame = new $.ig.ItemTooltipLayerFrame();
		var animationRate = this.transitionDuration() / 1000;
		this.previousFrame(previousFrame);
		this.currentFrame(currentFrame);
		this.transitionFrame(transitionFrame);
		this.defaultStyleKey($.ig.ItemToolTipLayer.prototype.$type);
	},
	createView: function () {
		return new $.ig.ItemToolTipLayerView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AnnotationLayer.prototype.onViewCreated.call(this, view);
		this.itemTooltipView(view);
	}
	,
	_itemTooltipView: null,
	itemTooltipView: function (value) {
		if (arguments.length === 1) {
			this._itemTooltipView = value;
			return value;
		} else {
			return this._itemTooltipView;
		}
	}
	,
	isDefaultTooltipBehaviorDisabled: function () {
		return true;
	}
	,
	targetSeries: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ItemToolTipLayer.prototype.targetSeriesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ItemToolTipLayer.prototype.targetSeriesProperty);
		}
	}
	,
	useInterpolation: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ItemToolTipLayer.prototype.useInterpolationProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ItemToolTipLayer.prototype.useInterpolationProperty);
		}
	}
	,
	skipUnknownValues: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ItemToolTipLayer.prototype.skipUnknownValuesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ItemToolTipLayer.prototype.skipUnknownValuesProperty);
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.AnnotationLayer.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.ItemToolTipLayer.prototype.targetSeriesPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.ItemToolTipLayer.prototype.useInterpolationPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.ItemToolTipLayer.prototype.skipUnknownValuesPropertyName:
				this.renderSeries(true);
				break;
		}
	}
	,
	onDependentSeriesRender: function (series, animate) {
		if (this.targetSeries() == null || this.targetSeries() == series) {
			if (!this.contentInfo().isDirty()) {
				this.renderSeries(animate);
			}
		}
	}
	,
	isSeriesValid: function (series) {
		if (!$.ig.AnnotationLayer.prototype.isSeriesValid.call(this, series)) {
			return false;
		}
		var res = this.getCategoryPosition(series);
		if (!res.item1()) {
			return false;
		}
		return true;
	}
	,
	prepareFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.prepareFrame.call(this, frame, view);
		var f = frame;
		var containerPool = this.itemTooltipView().containerPool();
		var useInterpolation = this.useInterpolation();
		var skipUnknowns = this.skipUnknownValues();
		f.tooltipYCoords().clear();
		f.tooltipContainers().clear();
		f.tooltipXCoords().clear();
		f.actualTooltipYCoords().clear();
		f.actualTooltipXCoords().clear();
		f.tooltipObjects().clear();
		f.tooltipDataContexts().clear();
		f.tooltipWidths().clear();
		f.tooltipHeights().clear();
		f.leaderBrushes().clear();
		if ($.ig.util.isNaN(this._currentPosition.__x) && $.ig.util.isNaN(this._currentPosition.__y)) {
			containerPool.count(0);
			return;
		}
		var i = 0;
		if (this.isSeriesValid(this.targetSeries())) {
			this.prepareSeries(this.targetSeries(), f, useInterpolation, skipUnknowns, 0);
			i++;
		} else {
			var en = this.seriesViewer().series().getEnumerator();
			while (en.moveNext()) {
				var series = en.current();
				if (this.isSeriesValid(series)) {
					this.prepareSeries(series, f, useInterpolation, skipUnknowns, i);
					i++;
				}
			}
			this.antiCollide(f);
		}
		containerPool.count(i);
	}
	,
	antiCollide: function (f) {
		var items = new $.ig.List$1($.ig.ItemTooltipCollisionInfo.prototype.$type, 0);
		for (var i = 0; i < f.actualTooltipXCoords().count(); i++) {
			var info = new $.ig.ItemTooltipCollisionInfo();
			info.index(i);
			info.position({ __x: f.actualTooltipXCoords().__inner[i], __y: f.actualTooltipYCoords().__inner[i], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			info.width(f.tooltipWidths().__inner[i]);
			info.height(f.tooltipHeights().__inner[i]);
			info.order(f.tooltipYCoords().__inner[i]);
			if ($.ig.util.isNaN(info.position().__x) || $.ig.util.isNaN(info.position().__y)) {
				continue;
			}
			items.add(info);
		}
		items.sort2(function (i1, i2) {
			if (i1.position().__y < i2.position().__y) {
				return -1;
			}
			if (i1.position().__y > i2.position().__y) {
				return 1;
			}
			if (i1.order() < i2.order()) {
				return -1;
			}
			if (i1.order() > i2.order()) {
				return 1;
			}
			return 0;
		});
		var collisons = this.collisionsExist(items);
		if (!collisons) {
			return;
		}
		for (var i1 = 0; i1 < items.count() - 1; i1++) {
			var currItem = items.__inner[i1];
			var nextItem = items.__inner[i1 + 1];
			var currBounds = new $.ig.Rect(0, currItem.position().__x, currItem.position().__y, currItem.width(), currItem.height());
			var nextBounds = new $.ig.Rect(0, nextItem.position().__x, nextItem.position().__y, nextItem.width(), nextItem.height());
			if (currBounds.intersectsWith(nextBounds) || currBounds.top() > nextBounds.bottom()) {
				nextItem.position({ __x: nextItem.position().__x, __y: currBounds.bottom() + 1, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		}
		if (items.__inner[items.count() - 1].position().__y + items.__inner[items.count() - 1].height() > this.view().viewport().height()) {
			items.__inner[items.count() - 1].position({ __x: items.__inner[items.count() - 1].position().__x, __y: items.__inner[items.count() - 1].position().__y - ((items.__inner[items.count() - 1].position().__y + items.__inner[items.count() - 1].height()) - this.view().viewport().height()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		for (var i2 = items.count() - 1; i2 >= 1; i2--) {
			var currItem1 = items.__inner[i2];
			var nextItem1 = items.__inner[i2 - 1];
			var currBounds1 = new $.ig.Rect(0, currItem1.position().__x, currItem1.position().__y, currItem1.width(), currItem1.height());
			var nextBounds1 = new $.ig.Rect(0, nextItem1.position().__x, nextItem1.position().__y, nextItem1.width(), nextItem1.height());
			if (currBounds1.intersectsWith(nextBounds1) || currBounds1.top() < nextBounds1.bottom()) {
				nextItem1.position({ __x: nextItem1.position().__x, __y: currBounds1.top() - (nextBounds1.height() + 1), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		}
		for (var i3 = 0; i3 < items.count(); i3++) {
			var ind = items.__inner[i3].index();
			var item = items.__inner[i3];
			f.actualTooltipXCoords().__inner[ind] = item.position().__x;
			f.actualTooltipYCoords().__inner[ind] = item.position().__y;
		}
	}
	,
	collisionsExist: function (items) {
		if (items.count() <= 1) {
			return false;
		}
		var viewport = this.view().viewport();
		for (var i = 0; i < items.count() - 1; i++) {
			var currItem = items.__inner[i];
			var nextItem = items.__inner[i + 1];
			var currBounds = new $.ig.Rect(0, currItem.position().__x, currItem.position().__y, currItem.width(), currItem.height());
			var nextBounds = new $.ig.Rect(0, nextItem.position().__x, nextItem.position().__y, nextItem.width(), nextItem.height());
			if (currBounds.intersectsWith(nextBounds)) {
				return true;
			}
		}
		return false;
	}
	,
	prepareSeries: function (series, f, useInterpolation, skipUnknowns, i) {
		var res = this.getCategoryPosition(series);
		if (!res.item1()) {
			return;
		}
		var pos = series.getSeriesValuePosition(res.item2(), useInterpolation, skipUnknowns);
		var containerPool = this.itemTooltipView().containerPool();
		var obj = this.getTooltipObject(series);
		var toolPosition = this._currentPosition;
		if (!useInterpolation) {
			toolPosition = this.toWorldPosition(pos);
		}
		var item = series.getItem(toolPosition);
		var container = containerPool.item(i);
		var viewport = this.view().viewport();
		var context = null;
		if (series.isDefaultToolTipSelected() && item != null) {
			context = series.getTooltipContext(item);
		} else {
			context = new $.ig.DataContext();
			context.item(item);
			context.series(series);
		}
		if (!this.itemTooltipView().configureTooltip(series, container, obj, context)) {
			return;
		}
		var size = this.itemTooltipView().getTooltipSize(container, obj, context);
		f.tooltipContainers().add1(container);
		f.tooltipDataContexts().add1(context);
		f.tooltipObjects().add1(obj);
		var beforeX = pos.__x;
		var actualX = pos.__x;
		actualX = Math.max(actualX, viewport.left());
		actualX = Math.min(actualX, viewport.right());
		f.tooltipXCoords().add(actualX);
		var actualY = pos.__y;
		actualY = Math.max(actualY, viewport.top());
		actualY = Math.min(actualY, viewport.bottom());
		f.tooltipYCoords().add(actualY);
		var posX = pos.__x + 10;
		if (posX + size.width() > viewport.right()) {
			posX = (beforeX - size.width()) - 10;
		}
		if (posX < viewport.left()) {
			posX = viewport.left();
		}
		f.actualTooltipXCoords().add(posX);
		var posY = (pos.__y - size.height()) - 10;
		if (posY + size.height() > viewport.bottom()) {
			posY = (viewport.bottom() - size.height()) - 10;
		}
		if (posY < viewport.top()) {
			posY = viewport.top();
		}
		f.actualTooltipYCoords().add(posY);
		f.tooltipWidths().add(size.width());
		f.tooltipHeights().add(size.height());
		var brush = this.actualBrush();
		if (brush == null) {
			brush = this.brush();
		}
		if (brush == null) {
			brush = series.actualBrush();
		}
		f.leaderBrushes().add(brush);
	}
	,
	getTooltipObject: function (series) {
		return series.toolTip();
	}
	,
	renderFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.renderFrame.call(this, frame, view);
		var f = frame;
		var count = f.tooltipYCoords().count();
		var left = view.viewport().left();
		var right = view.viewport().right();
		var top = view.viewport().top();
		var bottom = view.viewport().bottom();
		for (var i = 0; i < count; i++) {
			if ($.ig.util.isNaN(f.tooltipXCoords().__inner[i]) || $.ig.util.isNaN(f.tooltipYCoords().__inner[i]) || $.ig.util.isNaN(f.actualTooltipXCoords().__inner[i]) || $.ig.util.isNaN(f.actualTooltipYCoords().__inner[i]) || f.tooltipObjects().__inner[i] == null || f.tooltipDataContexts().__inner[i] == null) {
				this.itemTooltipView().hideContainer(f.tooltipContainers().__inner[i]);
				continue;
			}
			var toolLeft = f.actualTooltipXCoords().__inner[i];
			var toolTop = f.actualTooltipYCoords().__inner[i];
			var width = f.tooltipWidths().__inner[i];
			var height = f.tooltipHeights().__inner[i];
			var hide = false;
			if (toolLeft < left && Math.abs(toolLeft - left) > 1) {
				hide = true;
			}
			if (toolTop < top && Math.abs(toolTop - top) > 1) {
				hide = true;
			}
			if ((toolLeft + width) > right && Math.abs((toolLeft + width) - right) > 1) {
				hide = true;
			}
			if ((toolTop + height) > bottom && Math.abs((toolTop + height) - bottom) > 1) {
				hide = true;
			}
			if (hide) {
				this.itemTooltipView().hideContainer(f.tooltipContainers().__inner[i]);
				continue;
			}
			var cont = f.tooltipContainers().__inner[i];
			var displayX = Math.min(f.actualTooltipXCoords().__inner[i], f.tooltipXCoords().__inner[i]);
			var displayY = Math.min(f.actualTooltipYCoords().__inner[i], f.tooltipYCoords().__inner[i]);
			var pointerPosX = f.tooltipXCoords().__inner[i] - f.actualTooltipXCoords().__inner[i];
			var pointerPosY = f.tooltipYCoords().__inner[i] - f.actualTooltipYCoords().__inner[i];
			this.itemTooltipView().moveTooltip(cont, displayX, displayY, pointerPosX, pointerPosY);
		}
	}
	,
	$type: new $.ig.Type('ItemToolTipLayer', $.ig.AnnotationLayer.prototype.$type)
}, true);

$.ig.util.defType('ItemTooltipCollisionInfo', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_position: null,
	position: function (value) {
		if (arguments.length === 1) {
			this._position = value;
			return value;
		} else {
			return this._position;
		}
	}
	,
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
	_width: 0,
	width: function (value) {
		if (arguments.length === 1) {
			this._width = value;
			return value;
		} else {
			return this._width;
		}
	}
	,
	_height: 0,
	height: function (value) {
		if (arguments.length === 1) {
			this._height = value;
			return value;
		} else {
			return this._height;
		}
	}
	,
	_order: 0,
	order: function (value) {
		if (arguments.length === 1) {
			this._order = value;
			return value;
		} else {
			return this._order;
		}
	}
	,
	$type: new $.ig.Type('ItemTooltipCollisionInfo', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ItemTooltipLayerFrame', 'Frame', {
	_tooltipYCoords: null,
	tooltipYCoords: function (value) {
		if (arguments.length === 1) {
			this._tooltipYCoords = value;
			return value;
		} else {
			return this._tooltipYCoords;
		}
	}
	,
	_tooltipXCoords: null,
	tooltipXCoords: function (value) {
		if (arguments.length === 1) {
			this._tooltipXCoords = value;
			return value;
		} else {
			return this._tooltipXCoords;
		}
	}
	,
	_actualTooltipYCoords: null,
	actualTooltipYCoords: function (value) {
		if (arguments.length === 1) {
			this._actualTooltipYCoords = value;
			return value;
		} else {
			return this._actualTooltipYCoords;
		}
	}
	,
	_actualTooltipXCoords: null,
	actualTooltipXCoords: function (value) {
		if (arguments.length === 1) {
			this._actualTooltipXCoords = value;
			return value;
		} else {
			return this._actualTooltipXCoords;
		}
	}
	,
	_tooltipObjects: null,
	tooltipObjects: function (value) {
		if (arguments.length === 1) {
			this._tooltipObjects = value;
			return value;
		} else {
			return this._tooltipObjects;
		}
	}
	,
	_tooltipDataContexts: null,
	tooltipDataContexts: function (value) {
		if (arguments.length === 1) {
			this._tooltipDataContexts = value;
			return value;
		} else {
			return this._tooltipDataContexts;
		}
	}
	,
	_tooltipContainers: null,
	tooltipContainers: function (value) {
		if (arguments.length === 1) {
			this._tooltipContainers = value;
			return value;
		} else {
			return this._tooltipContainers;
		}
	}
	,
	_tooltipWidths: null,
	tooltipWidths: function (value) {
		if (arguments.length === 1) {
			this._tooltipWidths = value;
			return value;
		} else {
			return this._tooltipWidths;
		}
	}
	,
	_tooltipHeights: null,
	tooltipHeights: function (value) {
		if (arguments.length === 1) {
			this._tooltipHeights = value;
			return value;
		} else {
			return this._tooltipHeights;
		}
	}
	,
	_leaderBrushes: null,
	leaderBrushes: function (value) {
		if (arguments.length === 1) {
			this._leaderBrushes = value;
			return value;
		} else {
			return this._leaderBrushes;
		}
	}
	,
	init: function () {
		$.ig.Frame.prototype.init.call(this);
		this.tooltipYCoords(new $.ig.List$1(Number, 0));
		this.tooltipXCoords(new $.ig.List$1(Number, 0));
		this.actualTooltipYCoords(new $.ig.List$1(Number, 0));
		this.actualTooltipXCoords(new $.ig.List$1(Number, 0));
		this.tooltipObjects(new $.ig.List$1($.ig.Object.prototype.$type, 0));
		this.tooltipDataContexts(new $.ig.List$1($.ig.Object.prototype.$type, 0));
		this.tooltipContainers(new $.ig.List$1($.ig.Object.prototype.$type, 0));
		this.tooltipWidths(new $.ig.List$1(Number, 0));
		this.tooltipHeights(new $.ig.List$1(Number, 0));
		this.leaderBrushes(new $.ig.List$1($.ig.Brush.prototype.$type, 0));
	},
	interpolate3: function (p, min, max) {
		var mn = min;
		var mx = max;
		$.ig.Frame.prototype.interpolate1(this.tooltipYCoords(), p, mn.tooltipYCoords(), mx.tooltipYCoords());
		$.ig.Frame.prototype.interpolate1(this.tooltipXCoords(), p, mn.tooltipXCoords(), mx.tooltipXCoords());
		$.ig.Frame.prototype.interpolate1(this.actualTooltipYCoords(), p, mn.actualTooltipYCoords(), mx.actualTooltipYCoords());
		$.ig.Frame.prototype.interpolate1(this.actualTooltipXCoords(), p, mn.actualTooltipXCoords(), mx.actualTooltipXCoords());
		$.ig.Frame.prototype.interpolate1(this.tooltipWidths(), p, mn.tooltipWidths(), mx.tooltipWidths());
		$.ig.Frame.prototype.interpolate1(this.tooltipHeights(), p, mn.tooltipHeights(), mx.tooltipHeights());
		$.ig.Frame.prototype.interpolate2(this.leaderBrushes(), p, mn.leaderBrushes(), mx.leaderBrushes(), $.ig.InterpolationMode.prototype.rGB);
		$.ig.InterpolationUtil.prototype.interpolateValues$1($.ig.Object.prototype.$type, this.tooltipObjects(), p, mn.tooltipObjects(), mx.tooltipObjects(), function () { return null; }, function (ip, iq, minVal, maxVal) {
			return maxVal;
		});
		$.ig.InterpolationUtil.prototype.interpolateValues$1($.ig.Object.prototype.$type, this.tooltipDataContexts(), p, mn.tooltipDataContexts(), mx.tooltipDataContexts(), function () { return null; }, function (ip, iq, minVal, maxVal) {
			return maxVal;
		});
		$.ig.InterpolationUtil.prototype.interpolateValues$1($.ig.Object.prototype.$type, this.tooltipContainers(), p, mn.tooltipContainers(), mx.tooltipContainers(), function () { return null; }, function (ip, iq, minVal, maxVal) {
			return maxVal;
		});
	}
	,
	$type: new $.ig.Type('ItemTooltipLayerFrame', $.ig.Frame.prototype.$type)
}, true);

$.ig.util.defType('ItemToolTipLayerView', 'AnnotationLayerView', {
	init: function (model) {
		$.ig.AnnotationLayerView.prototype.init.call(this, model);
		this.itemTooltipModel(model);
		this.visibleTooltips(new $.ig.List$1($.ig.PointerTooltip.prototype.$type, 0));
	},
	_itemTooltipModel: null,
	itemTooltipModel: function (value) {
		if (arguments.length === 1) {
			this._itemTooltipModel = value;
			return value;
		} else {
			return this._itemTooltipModel;
		}
	}
	,
	_containerPool: null,
	containerPool: function (value) {
		if (arguments.length === 1) {
			this._containerPool = value;
			return value;
		} else {
			return this._containerPool;
		}
	}
	,
	onInit: function () {
		var $self = this;
		$.ig.AnnotationLayerView.prototype.onInit.call(this);
		this.containerPool((function () {
			var $ret = new $.ig.Pool$1($.ig.Object.prototype.$type);
			$ret.create($self.containerCreate.runOn($self));
			$ret.activate($self.containerActivate.runOn($self));
			$ret.disactivate($self.containerDisactivate.runOn($self));
			$ret.destroy($self.containerDestroy.runOn($self));
			return $ret;
		}()));
		this.htmlTest(/^[^<]*(<[\w\W]+>)[^>]*$/);
	}
	,
	destroy: function () {
		$.ig.AnnotationLayerView.prototype.destroy.call(this);
		for (var i = 0; i < this.containerPool().active().count(); i++) {
			(this.containerPool().active().__inner[i]).destroy();
		}
		for (var i1 = 0; i1 < this.containerPool().inactive().count(); i1++) {
			(this.containerPool().inactive().__inner[i1]).destroy();
		}
		this.containerPool().clear();
	}
	,
	_visibleTooltips: null,
	visibleTooltips: function (value) {
		if (arguments.length === 1) {
			this._visibleTooltips = value;
			return value;
		} else {
			return this._visibleTooltips;
		}
	}
	,
	containerCreate: function () {
		var container = new $.ig.PointerTooltip();
		container.__visibility = $.ig.Visibility.prototype.collapsed;
		container.eventSink(this.model().seriesViewer().view().eventProxy());
		this.visibleTooltips().add(container);
		return container;
	}
	,
	containerActivate: function (container) {
		var p = container;
	}
	,
	containerDisactivate: function (container) {
		var p = container;
		p.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	containerDestroy: function (container) {
		var p = (container);
		p.unbind();
		p.content(null);
		this.visibleTooltips().remove1(container);
	}
	,
	_htmlTest: null,
	htmlTest: function (value) {
		if (arguments.length === 1) {
			this._htmlTest = value;
			return value;
		} else {
			return this._htmlTest;
		}
	}
	,
	configureTooltip: function (series, container, obj, context) {
		var p = container;
		if (series.tooltipTemplate() == "default") {
			series.tooltipTemplate(series.view().getDefaultTooltipTemplate());
		}
		var template_ = series.tooltipTemplate();
		var context_ = context;
		var tmplExists = $.ig.tmpl !== null;
		if (!tmplExists) {
			return false;
		}
		if (series.flattenEventArgs() == null) {
			return false;
		}
		var args_ = series.flattenEventArgs()(context);
		var expanded_ = $.ig.tmpl(template_, args_).toString();
		if (!this.htmlTest().test(expanded_)) {
			expanded_ = "<span>" + expanded_ + "</span>";
		}
		var content = $(expanded_);
		p.content(content);
		return true;
	}
	,
	getTooltipSize: function (container, obj, context) {
		var p = container;
		var cc = p.content();
		var availableSize = new $.ig.Size(1, Number.POSITIVE_INFINITY, Number.POSITIVE_INFINITY);
		p.pointerVisibility($.ig.Visibility.prototype.collapsed);
		var size = p.measureCore(new $.ig.Size(1, Number.POSITIVE_INFINITY, Number.POSITIVE_INFINITY));
		p.pointerVisibility($.ig.Visibility.prototype.visible);
		return size;
	}
	,
	moveTooltip: function (container, x, y, pointerPosX, pointerPosY) {
		var style = null;
		if (this.model().seriesViewer() != null) {
			style = this.model().seriesViewer().view().viewManager().getPointerTooltipStyle();
		}
		var p = container;
		p.pointerTooltipStyle(style);
		var cc = p.content();
		p.pointerPosition({ __x: pointerPosX, __y: pointerPosY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		p.__visibility = $.ig.Visibility.prototype.visible;
		p.canvasLeft(x);
		p.canvasTop(y);
	}
	,
	hideContainer: function (container) {
		if (container != null) {
			var p = container;
			p.__visibility = $.ig.Visibility.prototype.collapsed;
		}
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnnotationLayerView.prototype.renderOverride.call(this, context, isHitContext);
		if (isHitContext) {
			return;
		}
		var outerOffsetX = 0;
		var outerOffsetY = 0;
		var seriesViewer = this.model().seriesViewer();
		if (seriesViewer != null) {
			var offsets = seriesViewer.view().viewManager().getContainerOffsets();
			outerOffsetX += offsets.__x;
			outerOffsetY += offsets.__y;
		}
		for (var i = 0; i < this.visibleTooltips().count(); i++) {
			this.visibleTooltips().__inner[i].render(this.visibleTooltips().__inner[i].canvasLeft() + outerOffsetX, this.visibleTooltips().__inner[i].canvasTop() + outerOffsetY);
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnnotationLayerView.prototype.exportViewShapes.call(this, svd);
		var en = this.containerPool().active().getEnumerator();
		while (en.moveNext()) {
			var tooltip_ = en.current();
			if ((tooltip_).__visibility != $.ig.Visibility.prototype.visible) {
				continue;
			}
			var t = tooltip_;
			var vd = t.exportVisualData();
			vd.offsetX(t.canvasLeft());
			vd.offsetY(t.canvasTop());
			vd.categoryNames(new $.ig.List$1(String, 0));
			var child = tooltip_.content().text();
			var name = child.toString();
			vd.categoryNames().add(name);
			svd.pointerTooltips().add(vd);
		}
	}
	,
	$type: new $.ig.Type('ItemToolTipLayerView', $.ig.AnnotationLayerView.prototype.$type)
}, true);

$.ig.util.defType('PointerTooltip', 'ContentControl', {
	init: function () {
		var $self = this;
		this.__lastPosition = { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		this.__lastLocation = $.ig.PointerTooltipPointerLocation.prototype.auto;
		this.__lastWidthDelta = 0;
		this.__lastHeightDelta = 0;
		this.__bound = false;
		$.ig.ContentControl.prototype.init.call(this);
		this.defaultStyleKey($.ig.PointerTooltip.prototype.$type);
		this.propertyUpdated = $.ig.Delegate.prototype.combine(this.propertyUpdated, function (o, e) {
			$self.propertyUpdatedOverride(o, e.propertyName(), e.oldValue(), e.newValue());
		});
		this.view(new $.ig.PointerTooltipView(this));
	},
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
	pointerPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.pointerPositionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.pointerPositionProperty);
		}
	}
	,
	pointerInset: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.pointerInsetProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.pointerInsetProperty);
		}
	}
	,
	pointerWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.pointerWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.pointerWidthProperty);
		}
	}
	,
	pointerVisibility: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.pointerVisibilityProperty, $.ig.Visibility.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PointerTooltip.prototype.pointerVisibilityProperty));
		}
	}
	,
	pointerLocation: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.pointerLocationProperty, $.ig.PointerTooltipPointerLocation.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PointerTooltip.prototype.pointerLocationProperty));
		}
	}
	,
	actualPointerStartPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualPointerStartPositionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualPointerStartPositionProperty);
		}
	}
	,
	actualPointerFirstPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualPointerFirstPositionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualPointerFirstPositionProperty);
		}
	}
	,
	actualPointerSecondPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualPointerSecondPositionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualPointerSecondPositionProperty);
		}
	}
	,
	actualBoxTopLeftPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualBoxTopLeftPositionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualBoxTopLeftPositionProperty);
		}
	}
	,
	actualBoxTopRightPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualBoxTopRightPositionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualBoxTopRightPositionProperty);
		}
	}
	,
	actualBoxBottomLeftPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualBoxBottomLeftPositionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualBoxBottomLeftPositionProperty);
		}
	}
	,
	actualBoxBottomRightPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualBoxBottomRightPositionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualBoxBottomRightPositionProperty);
		}
	}
	,
	actualBoxWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualBoxWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualBoxWidthProperty);
		}
	}
	,
	actualBoxHeight: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualBoxHeightProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualBoxHeightProperty);
		}
	}
	,
	actualBoxFullHeight: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualBoxFullHeightProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualBoxFullHeightProperty);
		}
	}
	,
	actualBoxFullWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.actualBoxFullWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.actualBoxFullWidthProperty);
		}
	}
	,
	pointerPoints: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.pointerPointsProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.pointerPointsProperty);
		}
	}
	,
	pointerOutlinePoints: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PointerTooltip.prototype.pointerOutlinePointsProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PointerTooltip.prototype.pointerOutlinePointsProperty);
		}
	}
	,
	raisePropertyChanged: function (name, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(name));
		}
		if (this.propertyUpdated != null) {
			this.propertyUpdated(this, new $.ig.PropertyUpdatedEventArgs(name, oldValue, newValue));
		}
	}
	,
	propertyChanged: null,
	propertyUpdated: null,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.PointerTooltip.prototype.pointerPositionPropertyName:
				this.measureInvalid();
				break;
			case $.ig.PointerTooltip.prototype.pointerVisibilityPropertyName:
				this.measureInvalid();
				break;
		}
	}
	,
	_contentControl: null,
	contentControl: function (value) {
		if (arguments.length === 1) {
			this._contentControl = value;
			return value;
		} else {
			return this._contentControl;
		}
	}
	,
	_pointerShape: null,
	pointerShape: function (value) {
		if (arguments.length === 1) {
			this._pointerShape = value;
			return value;
		} else {
			return this._pointerShape;
		}
	}
	,
	_pointerOutlineShape: null,
	pointerOutlineShape: function (value) {
		if (arguments.length === 1) {
			this._pointerOutlineShape = value;
			return value;
		} else {
			return this._pointerOutlineShape;
		}
	}
	,
	_isMeasureInvalid: false,
	isMeasureInvalid: function (value) {
		if (arguments.length === 1) {
			this._isMeasureInvalid = value;
			return value;
		} else {
			return this._isMeasureInvalid;
		}
	}
	,
	checkContentSize: function () {
		if (this.view().contentSizeChanged(this.content())) {
			this.measureInvalid();
		}
	}
	,
	measureInvalid: function () {
		this.isMeasureInvalid(true);
	}
	,
	__lastPosition: null,
	__lastLocation: 0,
	__lastWidthDelta: 0,
	__lastHeightDelta: 0,
	closeEnough: function (value1, value2) {
		if (Math.abs(value1 - value2) < 1E-05) {
			return true;
		}
		return false;
	}
	,
	measureCore: function (availableSize) {
		this.isMeasureInvalid(false);
		var contentNeedsWidth = 0;
		var contentNeedsHeight = 0;
		var size = this.view().getDesiredContentSize(availableSize);
		contentNeedsWidth = size.width();
		contentNeedsHeight = size.height();
		if (this.__lastLocation == this.pointerLocation() && this.closeEnough(this.__lastPosition.__x, this.pointerPosition().__x) && this.closeEnough(this.__lastPosition.__y, this.pointerPosition().__y)) {
			this.actualBoxWidth(contentNeedsWidth);
			this.actualBoxHeight(contentNeedsHeight);
			if (this.pointerVisibility() == $.ig.Visibility.prototype.visible) {
				contentNeedsWidth += this.__lastWidthDelta;
				contentNeedsHeight += this.__lastHeightDelta;
				this.actualBoxFullWidth(this.actualBoxWidth() + this.__lastWidthDelta);
				this.actualBoxFullHeight(this.actualBoxHeight() + this.__lastHeightDelta);
			}
			return new $.ig.Size(1, contentNeedsWidth, contentNeedsHeight);
		}
		var boxTopLeft = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var boxTopMiddle = { __x: contentNeedsWidth * 0.5, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var boxTopRight = { __x: contentNeedsWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var boxBottomRight = { __x: contentNeedsWidth, __y: contentNeedsHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var boxBottomMiddle = { __x: contentNeedsWidth * 0.5, __y: contentNeedsHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var boxBottomLeft = { __x: 0, __y: contentNeedsHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var boxLeftMiddle = { __x: 0, __y: contentNeedsHeight * 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var boxRightMiddle = { __x: contentNeedsWidth, __y: contentNeedsHeight * 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var boxWidth = contentNeedsWidth;
		var boxHeight = contentNeedsHeight;
		var fullWidth = contentNeedsWidth;
		var fullHeight = contentNeedsHeight;
		var pointerConnectionFirst = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var pointerConnectionSecond = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var pointerOverlapConnectionFirst = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var pointerOverlapConnectionSecond = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var pointerInset = this.pointerInset();
		var pointerWidth = this.pointerWidth();
		var pointerLocation = this.pointerLocation();
		var pointerPosition = this.pointerPosition();
		var widthDelta = 0;
		var heightDelta = 0;
		var distTopLeft = Math.pow(boxTopLeft.__x - pointerPosition.__x, 2) + Math.pow(boxTopLeft.__y - pointerPosition.__y, 2);
		var distTopMiddle = Math.pow(boxTopMiddle.__x - pointerPosition.__x, 2) + Math.pow(boxTopMiddle.__y - pointerPosition.__y, 2);
		var distTopRight = Math.pow(boxTopRight.__x - pointerPosition.__x, 2) + Math.pow(boxTopRight.__y - pointerPosition.__y, 2);
		var distRightMiddle = Math.pow(boxRightMiddle.__x - pointerPosition.__x, 2) + Math.pow(boxRightMiddle.__y - pointerPosition.__y, 2);
		var distBottomRight = Math.pow(boxBottomRight.__x - pointerPosition.__x, 2) + Math.pow(boxBottomRight.__y - pointerPosition.__y, 2);
		var distBottomMiddle = Math.pow(boxBottomMiddle.__x - pointerPosition.__x, 2) + Math.pow(boxBottomMiddle.__y - pointerPosition.__y, 2);
		var distBottomLeft = Math.pow(boxBottomLeft.__x - pointerPosition.__x, 2) + Math.pow(boxBottomLeft.__y - pointerPosition.__y, 2);
		var distLeftMiddle = Math.pow(boxLeftMiddle.__x - pointerPosition.__x, 2) + Math.pow(boxLeftMiddle.__y - pointerPosition.__y, 2);
		var minDist = 1.7976931348623157E+308;
		minDist = Math.min(minDist, distTopLeft);
		minDist = Math.min(minDist, distTopMiddle);
		minDist = Math.min(minDist, distTopRight);
		minDist = Math.min(minDist, distRightMiddle);
		minDist = Math.min(minDist, distBottomRight);
		minDist = Math.min(minDist, distBottomMiddle);
		minDist = Math.min(minDist, distBottomLeft);
		minDist = Math.min(minDist, distLeftMiddle);
		if (pointerLocation == $.ig.PointerTooltipPointerLocation.prototype.auto) {
			if (minDist == distTopLeft) {
				pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.leftTop;
				if (pointerPosition.__x >= boxTopLeft.__x) {
					pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.topLeft;
				}
			}
			if (minDist == distLeftMiddle) {
				pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.leftMiddle;
			}
			if (minDist == distBottomLeft) {
				pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.leftBottom;
				if (pointerPosition.__x >= boxBottomLeft.__x) {
					pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.bottomLeft;
				}
			}
			if (minDist == distTopRight) {
				pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.rightTop;
				if (pointerPosition.__x <= boxTopRight.__x) {
					pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.topRight;
				}
			}
			if (minDist == distRightMiddle) {
				pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.rightMiddle;
			}
			if (minDist == distBottomRight) {
				pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.rightBottom;
				if (pointerPosition.__x <= boxBottomRight.__x) {
					pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.bottomRight;
				}
			}
			if (minDist == distTopMiddle) {
				pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.topMiddle;
			}
			if (minDist == distBottomMiddle) {
				pointerLocation = $.ig.PointerTooltipPointerLocation.prototype.bottomMiddle;
			}
		}
		var overlap = 3;
		switch (pointerLocation) {
			case $.ig.PointerTooltipPointerLocation.prototype.topLeft:
				pointerConnectionFirst = { __x: boxTopLeft.__x + pointerInset, __y: boxTopLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: boxTopLeft.__x + pointerInset + pointerWidth, __y: boxTopLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxTopLeft.__x + pointerInset, __y: boxTopLeft.__y + overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: boxTopLeft.__x + pointerInset + pointerWidth, __y: boxTopLeft.__y + overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.topMiddle:
				pointerConnectionFirst = { __x: ((boxTopLeft.__x + boxTopRight.__x) / 2) - (pointerWidth / 2), __y: boxTopLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: ((boxTopLeft.__x + boxTopRight.__x) / 2) + (pointerWidth / 2), __y: boxTopLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: ((boxTopLeft.__x + boxTopRight.__x) / 2) - (pointerWidth / 2), __y: boxTopLeft.__y + overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: ((boxTopLeft.__x + boxTopRight.__x) / 2) + (pointerWidth / 2), __y: boxTopLeft.__y + overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.topRight:
				pointerConnectionFirst = { __x: boxTopRight.__x - pointerInset, __y: boxTopLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: (boxTopRight.__x - pointerInset) - pointerWidth, __y: boxTopLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxTopRight.__x - pointerInset, __y: boxTopLeft.__y + overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: (boxTopRight.__x - pointerInset) - pointerWidth, __y: boxTopLeft.__y + overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.rightTop:
				pointerConnectionFirst = { __x: boxTopRight.__x, __y: boxTopRight.__y + pointerInset, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: boxTopRight.__x, __y: boxTopRight.__y + pointerInset + pointerWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxTopRight.__x - overlap, __y: boxTopRight.__y + pointerInset, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: boxTopRight.__x - overlap, __y: boxTopRight.__y + pointerInset + pointerWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.rightMiddle:
				pointerConnectionFirst = { __x: boxTopRight.__x, __y: ((boxTopRight.__y + boxBottomRight.__y) / 2) - (pointerWidth / 2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: boxTopRight.__x, __y: ((boxTopRight.__y + boxBottomRight.__y) / 2) + (pointerWidth / 2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxTopRight.__x - overlap, __y: ((boxTopRight.__y + boxBottomRight.__y) / 2) - (pointerWidth / 2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: boxTopRight.__x - overlap, __y: ((boxTopRight.__y + boxBottomRight.__y) / 2) + (pointerWidth / 2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.rightBottom:
				pointerConnectionFirst = { __x: boxBottomRight.__x, __y: boxBottomRight.__y - pointerInset, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: boxBottomRight.__x, __y: boxBottomRight.__y - pointerInset - pointerWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxBottomRight.__x - overlap, __y: boxBottomRight.__y - pointerInset, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: boxBottomRight.__x - overlap, __y: boxBottomRight.__y - pointerInset - pointerWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.bottomRight:
				pointerConnectionFirst = { __x: boxBottomRight.__x - pointerInset, __y: boxBottomRight.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: (boxBottomRight.__x - pointerInset) - pointerWidth, __y: boxBottomRight.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxBottomRight.__x - pointerInset, __y: boxBottomRight.__y - overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: (boxBottomRight.__x - pointerInset) - pointerWidth, __y: boxBottomRight.__y - overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.bottomMiddle:
				pointerConnectionFirst = { __x: ((boxBottomLeft.__x + boxBottomRight.__x) / 2) - (pointerWidth / 2), __y: boxBottomRight.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: ((boxBottomLeft.__x + boxBottomRight.__x) / 2) + (pointerWidth / 2), __y: boxBottomRight.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: ((boxBottomLeft.__x + boxBottomRight.__x) / 2) - (pointerWidth / 2), __y: boxBottomRight.__y - overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: ((boxBottomLeft.__x + boxBottomRight.__x) / 2) + (pointerWidth / 2), __y: boxBottomRight.__y - overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.bottomLeft:
				pointerConnectionFirst = { __x: boxBottomLeft.__x + pointerInset, __y: boxBottomLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: boxBottomLeft.__x + pointerInset + pointerWidth, __y: boxBottomLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxBottomLeft.__x + pointerInset, __y: boxBottomLeft.__y - overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: boxBottomLeft.__x + pointerInset + pointerWidth, __y: boxBottomLeft.__y - overlap, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.leftBottom:
				pointerConnectionFirst = { __x: boxTopLeft.__x, __y: boxBottomLeft.__y - pointerInset, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: boxTopLeft.__x, __y: (boxBottomLeft.__y - pointerInset) - pointerWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxTopLeft.__x + overlap, __y: boxBottomLeft.__y - pointerInset, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: boxTopLeft.__x + overlap, __y: (boxBottomLeft.__y - pointerInset) - pointerWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.leftMiddle:
				pointerConnectionFirst = { __x: boxTopLeft.__x, __y: ((boxTopLeft.__y + boxBottomLeft.__y) / 2) - (pointerWidth / 2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: boxTopLeft.__x, __y: ((boxTopLeft.__y + boxBottomLeft.__y) / 2) + (pointerWidth / 2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxTopLeft.__x + overlap, __y: ((boxTopLeft.__y + boxBottomLeft.__y) / 2) - (pointerWidth / 2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: boxTopLeft.__x + overlap, __y: ((boxTopLeft.__y + boxBottomLeft.__y) / 2) + (pointerWidth / 2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
			case $.ig.PointerTooltipPointerLocation.prototype.leftTop:
				pointerConnectionFirst = { __x: boxTopLeft.__x, __y: boxTopLeft.__y + pointerInset, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerConnectionSecond = { __x: boxTopLeft.__x, __y: boxTopLeft.__y + pointerInset + pointerWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionFirst = { __x: boxTopLeft.__x + overlap, __y: boxTopLeft.__y + pointerInset, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pointerOverlapConnectionSecond = { __x: boxTopLeft.__x + overlap, __y: boxTopLeft.__y + pointerInset + pointerWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				break;
		}
		var pointerStart = { __x: pointerPosition.__x, __y: pointerPosition.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (pointerStart.__y < boxTopLeft.__y) {
			var diff = boxTopLeft.__y - pointerStart.__y;
			pointerStart = { __x: pointerStart.__x, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			fullHeight += diff;
			boxTopLeft = { __x: boxTopLeft.__x, __y: boxTopLeft.__y + diff, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			boxTopRight = { __x: boxTopRight.__x, __y: boxTopRight.__y + diff, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			boxBottomLeft = { __x: boxBottomLeft.__x, __y: boxBottomLeft.__y + diff, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			boxBottomRight = { __x: boxBottomRight.__x, __y: boxBottomRight.__y + diff, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pointerConnectionFirst = { __x: pointerConnectionFirst.__x, __y: pointerConnectionFirst.__y + diff, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pointerConnectionSecond = { __x: pointerConnectionSecond.__x, __y: pointerConnectionSecond.__y + diff, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pointerOverlapConnectionFirst = { __x: pointerOverlapConnectionFirst.__x, __y: pointerOverlapConnectionFirst.__y + diff, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pointerOverlapConnectionSecond = { __x: pointerOverlapConnectionSecond.__x, __y: pointerOverlapConnectionSecond.__y + diff, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		} else {
			fullHeight = Math.max(pointerStart.__y, boxHeight);
		}
		if (pointerStart.__x < boxTopLeft.__x) {
			var diff1 = boxTopLeft.__x - pointerStart.__x;
			pointerStart = { __x: 0, __y: pointerStart.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			fullWidth += diff1;
			boxTopLeft = { __x: boxTopLeft.__x + diff1, __y: boxTopLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			boxTopRight = { __x: boxTopRight.__x + diff1, __y: boxTopRight.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			boxBottomLeft = { __x: boxBottomLeft.__x + diff1, __y: boxBottomLeft.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			boxBottomRight = { __x: boxBottomRight.__x + diff1, __y: boxBottomRight.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pointerConnectionFirst = { __x: pointerConnectionFirst.__x + diff1, __y: pointerConnectionFirst.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pointerConnectionSecond = { __x: pointerConnectionSecond.__x + diff1, __y: pointerConnectionSecond.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pointerOverlapConnectionFirst = { __x: pointerOverlapConnectionFirst.__x + diff1, __y: pointerOverlapConnectionFirst.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pointerOverlapConnectionSecond = { __x: pointerOverlapConnectionSecond.__x + diff1, __y: pointerOverlapConnectionSecond.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		} else {
			fullWidth = Math.max(pointerStart.__x, boxWidth);
		}
		widthDelta = fullWidth - boxWidth;
		heightDelta = fullHeight - boxHeight;
		this.__lastWidthDelta = widthDelta;
		this.__lastHeightDelta = heightDelta;
		this.__lastLocation = this.pointerLocation();
		this.__lastPosition = this.pointerPosition();
		this.actualPointerStartPosition(pointerStart);
		this.actualPointerFirstPosition(pointerConnectionFirst);
		this.actualPointerSecondPosition(pointerConnectionSecond);
		this.actualBoxWidth(boxWidth);
		this.actualBoxHeight(boxHeight);
		this.actualBoxFullWidth(fullWidth);
		this.actualBoxFullHeight(fullHeight);
		this.actualBoxTopLeftPosition(boxTopLeft);
		this.actualBoxTopRightPosition(boxTopRight);
		this.actualBoxBottomRightPosition(boxBottomRight);
		this.actualBoxBottomLeftPosition(boxBottomLeft);
		var pc = new $.ig.PointCollection(0);
		pc.add(pointerOverlapConnectionSecond);
		pc.add(this.actualPointerSecondPosition());
		pc.add(this.actualPointerStartPosition());
		pc.add(this.actualPointerFirstPosition());
		pc.add(pointerOverlapConnectionFirst);
		this.pointerPoints(pc);
		if (this.pointerShape() != null) {
			this.pointerShape().points(pc);
		}
		var pc2 = new $.ig.PointCollection(0);
		pc2.add(this.actualPointerSecondPosition());
		pc2.add(this.actualPointerStartPosition());
		pc2.add(this.actualPointerFirstPosition());
		this.pointerOutlinePoints(pc2);
		if (this.pointerOutlineShape() != null) {
			this.pointerOutlineShape().points(pc2);
		}
		if (this.pointerVisibility() == $.ig.Visibility.prototype.collapsed) {
			return new $.ig.Size(1, contentNeedsWidth, contentNeedsHeight);
		}
		return new $.ig.Size(1, fullWidth, fullHeight);
	}
	,
	destroy: function () {
		this.unbind();
		this.view().destroy();
	}
	,
	render: function (x, y) {
		if (this.isMeasureInvalid()) {
			this.measureCore(new $.ig.Size(1, Number.POSITIVE_INFINITY, Number.POSITIVE_INFINITY));
		}
		this.view().render(x, y);
	}
	,
	_eventSink: null,
	eventSink: function (value) {
		if (arguments.length === 1) {
			this._eventSink = value;
			return value;
		} else {
			return this._eventSink;
		}
	}
	,
	__bound: false,
	bind: function () {
		if (!this.__bound) {
			if (this.eventSink() != null && this.view().container() != null) {
				this.__bound = true;
				this.eventSink().bindToSource(this.view().container(), "pointerTooltip" + this.getHashCode());
			}
		}
	}
	,
	unbind: function () {
		if (this.__bound) {
			if (this.eventSink() != null && this.view().container() != null) {
				this.__bound = false;
				this.eventSink().unbindFromSource(this.view().container(), "pointerTooltip" + this.getHashCode());
			}
			this.eventSink(null);
		}
	}
	,
	_pointerTooltipStyle: null,
	pointerTooltipStyle: function (value) {
		if (arguments.length === 1) {
			this._pointerTooltipStyle = value;
			return value;
		} else {
			return this._pointerTooltipStyle;
		}
	}
	,
	exportVisualData: function () {
		return this.view().exportVisualData();
	}
	,
	$type: new $.ig.Type('PointerTooltip', $.ig.ContentControl.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('PointerTooltipView', 'Object', {
	init: function (model) {
		this.__shown = false;
		this.__lastCanvasWidth = 0;
		this.__lastCanvasHeight = 0;
		this.__currentWidth = -1;
		this.__currentHeight = -1;
		$.ig.Object.prototype.init.call(this);
		this.model(model);
		this.model().pointerShape(new $.ig.Polygon());
		this.model().pointerOutlineShape(new $.ig.Polyline());
		this.container(null);
	},
	_model: null,
	model: function (value) {
		if (arguments.length === 1) {
			this._model = value;
			return value;
		} else {
			return this._model;
		}
	}
	,
	getDesiredContentSize: function (availableSize) {
		var contentEle = $(this.model().content()).clone();
		var size = new $.ig.Size(1, 0, 0);
		var body = $("body");
		if (this.tempContainer() == null) {
			this.tempContainer($("<div class=\"ui-chart-pointer-tooltip-container\" style=\"position: absolute; visibility: hidden; top : 0; left : 0\"></div>"));
			this.tempRelativePanel($("<div style=\"position: relative\"></div>"));
			this.tempContentPanel($("<div class=\"ui-chart-pointer-tooltip-content-container\" style=\"position : absolute; top : 0; left : 0\" /></div>"));
			this.tempContainer().append(this.tempRelativePanel());
			this.tempRelativePanel().append(this.tempContentPanel());
		}
		this.tempContainer().remove();
		body.append(this.tempContainer());
		this.tempContentPanel().append(contentEle);
		size = new $.ig.Size(1, this.tempContentPanel().outerWidth(true), this.tempContentPanel().outerHeight(true));
		this.tempContainer().remove();
		contentEle.remove();
		return size;
	}
	,
	_tempContainer: null,
	tempContainer: function (value) {
		if (arguments.length === 1) {
			this._tempContainer = value;
			return value;
		} else {
			return this._tempContainer;
		}
	}
	,
	_tempRelativePanel: null,
	tempRelativePanel: function (value) {
		if (arguments.length === 1) {
			this._tempRelativePanel = value;
			return value;
		} else {
			return this._tempRelativePanel;
		}
	}
	,
	_tempContentPanel: null,
	tempContentPanel: function (value) {
		if (arguments.length === 1) {
			this._tempContentPanel = value;
			return value;
		} else {
			return this._tempContentPanel;
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
	_canvas: null,
	canvas: function (value) {
		if (arguments.length === 1) {
			this._canvas = value;
			return value;
		} else {
			return this._canvas;
		}
	}
	,
	_mainContext: null,
	mainContext: function (value) {
		if (arguments.length === 1) {
			this._mainContext = value;
			return value;
		} else {
			return this._mainContext;
		}
	}
	,
	_contentContainer: null,
	contentContainer: function (value) {
		if (arguments.length === 1) {
			this._contentContainer = value;
			return value;
		} else {
			return this._contentContainer;
		}
	}
	,
	__lastRectangle: null,
	__shown: false,
	__lastCanvasWidth: 0,
	__lastCanvasHeight: 0,
	render: function (x, y) {
		if (this.container() == null) {
			this.container($("<div class=\"ui-chart-pointer-tooltip-container\" style=\"position : absolute; top : 0; left : 0\"></div>"));
			var subContainer = $("<div></div>");
			subContainer.css("position", "relative");
			this.canvas($("<canvas style=\"position : absolute; top : 0; left : 0\" />"));
			this.contentContainer($("<div class=\"ui-chart-pointer-tooltip-content-container\" style=\"position : absolute; top : 0; left : 0\" /></div>"));
			subContainer.append(this.canvas());
			subContainer.append(this.contentContainer());
			this.container().append(subContainer);
			this.mainContext(new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), (this.canvas()[0]).getContext("2d")));
			this.container().hide();
			$("body").append(this.container());
		}
		this.model().bind();
		if (this.model().__visibility == $.ig.Visibility.prototype.collapsed && this.__shown) {
			this.container().hide();
			this.__shown = false;
			return;
		}
		var content = null;
		if (this.model().content() != null) {
			content = $(this.model().content());
			content.remove();
		}
		this.contentContainer().children().remove();
		if (content != null) {
			var cont_ = this.contentContainer()[0];
			var xPos_ = this.model().actualBoxTopLeftPosition().__x.toString() + "px";
			var yPos_ = this.model().actualBoxTopLeftPosition().__y.toString() + "px";
			cont_.style['left'] = xPos_;
			cont_.style['top'] = yPos_;
			this.contentContainer().append(content);
		}
		var style = this.model().pointerTooltipStyle();
		var thickness = 1;
		if (style != null) {
			thickness = !$.ig.util.isNaN(style.strokeThickness()) ? style.strokeThickness() : 1;
		}
		var newWidth = $.ig.truncate(Math.ceil(this.model().actualBoxFullWidth() + (thickness * 2)));
		var newHeight = $.ig.truncate(Math.ceil(this.model().actualBoxFullHeight() + (thickness * 2)));
		if (newWidth != this.__lastCanvasWidth || newHeight != this.__lastCanvasHeight) {
			this.canvas().attr("width", newWidth.toString());
			this.canvas().attr("height", newHeight.toString());
			this.__lastCanvasWidth = newWidth;
			this.__lastCanvasHeight = newHeight;
		} else {
			this.mainContext().clearRectangle(0, 0, this.__lastCanvasWidth, this.__lastCanvasHeight);
		}
		var rect = new $.ig.Rectangle();
		rect.width(this.model().actualBoxWidth());
		rect.height(this.model().actualBoxHeight());
		rect.canvasLeft(this.model().actualBoxTopLeftPosition().__x);
		rect.canvasTop(this.model().actualBoxTopLeftPosition().__y);
		if (style != null) {
			rect.__fill = style.fillColor() != null ? style.fillColor() : (function () {
				var $ret = new $.ig.Brush();
				$ret.fill("white");
				return $ret;
			}());
			rect.__stroke = style.strokeColor() != null ? style.strokeColor() : (function () {
				var $ret = new $.ig.Brush();
				$ret.fill("black");
				return $ret;
			}());
			rect.strokeThickness(!$.ig.util.isNaN(style.strokeThickness()) ? style.strokeThickness() : 1);
			rect.radiusX(!$.ig.util.isNaN(style.cornerRadius()) ? style.cornerRadius() : 0);
			rect.radiusY(!$.ig.util.isNaN(style.cornerRadius()) ? style.cornerRadius() : 0);
		} else {
			rect.__fill = (function () {
				var $ret = new $.ig.Brush();
				$ret.fill("white");
				return $ret;
			}());
			rect.__stroke = (function () {
				var $ret = new $.ig.Brush();
				$ret.fill("black");
				return $ret;
			}());
			rect.strokeThickness(1);
		}
		this.__lastRectangle = rect;
		var fillShape = this.model().pointerShape();
		var outlineShape = this.model().pointerOutlineShape();
		fillShape.__fill = rect.__fill;
		outlineShape.__stroke = rect.__stroke;
		this.mainContext().renderRectangle(rect);
		this.mainContext().renderPolygon(fillShape);
		this.mainContext().renderPolyline(outlineShape);
		var x_ = x + "px";
		var y_ = y + "px";
		var container_ = this.container();
		container_[0].style['left'] = x_; container_[0].style['top'] = y_;
		if (this.model().__visibility == $.ig.Visibility.prototype.visible && !this.__shown) {
			this.container().show();
			this.__shown = true;
		}
	}
	,
	exportVisualData: function () {
		var vd = new $.ig.PointerTooltipVisualData();
		vd.pointerFillShape(new $.ig.PolygonVisualData(1, "fillShape", this.model().pointerShape()));
		vd.pointerOutlineShape(new $.ig.PolyLineVisualData(1, "outlineShape", this.model().pointerOutlineShape()));
		vd.boxShape(new $.ig.RectangleVisualData(1, "boxShape", this.__lastRectangle));
		vd.viewport(new $.ig.Rect(0, 0, 0, this.model().actualBoxFullWidth(), this.model().actualBoxFullHeight()));
		return vd;
	}
	,
	__currentWidth: 0,
	__currentHeight: 0,
	contentSizeChanged: function (Content) {
		var oldWidth = this.__currentWidth;
		var oldHeight = this.__currentHeight;
		var size = this.getDesiredContentSize(new $.ig.Size(1, Number.POSITIVE_INFINITY, Number.POSITIVE_INFINITY));
		if (size.width() != oldWidth || size.height() != oldHeight) {
			return true;
		}
		return false;
	}
	,
	destroy: function () {
		this.container().hide();
		this.container().remove();
		this.container(null);
		this.tempContainer(null);
		this.tempRelativePanel(null);
		this.tempContentPanel(null);
		this.container(null);
		this.canvas(null);
		this.contentContainer(null);
	}
	,
	$type: new $.ig.Type('PointerTooltipView', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('CategoryHighlightLayer', 'AnnotationLayer', {
	init: function () {
		$.ig.AnnotationLayer.prototype.init.call(this);
		var previousFrame = new $.ig.CategoryHighlightLayerFrame();
		var currentFrame = new $.ig.CategoryHighlightLayerFrame();
		var transitionFrame = new $.ig.CategoryHighlightLayerFrame();
		var animationRate = this.transitionDuration() / 1000;
		this.previousFrame(previousFrame);
		this.currentFrame(currentFrame);
		this.transitionFrame(transitionFrame);
		this.defaultStyleKey($.ig.CategoryHighlightLayer.prototype.$type);
	},
	createView: function () {
		return new $.ig.CategoryHighlightLayerView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AnnotationLayer.prototype.onViewCreated.call(this, view);
		this.highlightView(view);
	}
	,
	_highlightView: null,
	highlightView: function (value) {
		if (arguments.length === 1) {
			this._highlightView = value;
			return value;
		} else {
			return this._highlightView;
		}
	}
	,
	targetAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryHighlightLayer.prototype.targetAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryHighlightLayer.prototype.targetAxisProperty);
		}
	}
	,
	useInterpolation: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryHighlightLayer.prototype.useInterpolationProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryHighlightLayer.prototype.useInterpolationProperty);
		}
	}
	,
	bandHighlightWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryHighlightLayer.prototype.bandHighlightWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryHighlightLayer.prototype.bandHighlightWidthProperty);
		}
	}
	,
	onDependentAxisRender: function (axis, animate) {
		if (this.targetAxis() == null || this.targetAxis() == axis) {
			if (!this.contentInfo().isDirty()) {
				this.renderSeries(animate);
			}
		}
	}
	,
	isAxisValid: function (axis) {
		return axis != null && axis.isValid();
	}
	,
	prepareFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.prepareFrame.call(this, frame, view);
		var f = frame;
		f.rects().clear();
		var useInterpolation = this.useInterpolation();
		if ($.ig.util.isNaN(this._currentPosition.__x) && $.ig.util.isNaN(this._currentPosition.__y)) {
			return;
		}
		if (this.targetAxis() != null) {
			if (this.isAxisValid(this.targetAxis())) {
				this.prepareSeries(this.targetAxis(), f, useInterpolation);
			}
		} else {
			if ($.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer()) !== null) {
				var chart = this.seriesViewer();
				var en = chart.axes().getEnumerator();
				while (en.moveNext()) {
					var axis = en.current();
					if ($.ig.util.cast($.ig.CategoryAxisBase.prototype.$type, axis) !== null && this.isAxisValid(axis)) {
						this.prepareSeries(axis, f, useInterpolation);
					}
				}
			}
		}
	}
	,
	prepareSeries: function (axis, f, useInterpolation) {
		if (this.view() == null || this.view().windowRect().isEmpty() || this.view().viewport().isEmpty()) {
			return;
		}
		var xPos = this._currentPosition.__x;
		if (axis.isVertical() && $.ig.util.isNaN(xPos)) {
			xPos = 0;
		}
		xPos = this.view().viewport().left() + this.view().viewport().width() * (xPos - this.view().windowRect().left()) / this.view().windowRect().width();
		var yPos = this._currentPosition.__y;
		if (!axis.isVertical() && $.ig.util.isNaN(yPos)) {
			yPos = 0;
		}
		yPos = this.view().viewport().top() + this.view().viewport().height() * (yPos - this.view().windowRect().top()) / this.view().windowRect().height();
		var rect = axis.getCategoryBoundingBox({ __x: xPos, __y: yPos, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, useInterpolation, this.bandHighlightWidth());
		this.prepareRect(rect, f, axis);
	}
	,
	prepareRect: function (rect, frame, target) {
		var n = new $.ig.CategoryHighlightFrameRect();
		n.left(rect.left());
		n.top(rect.top());
		n.right(rect.right());
		n.bottom(rect.bottom());
		n.brush(this.getBrush());
		n.outline(this.getOutline());
		if (n.brush() == null) {
			n.brush(this.highlightView().getLightenedTranslucentBrush(target.actualStroke()));
		}
		if (n.outline() == null) {
			n.outline(this.highlightView().getLightenedTranslucentBrush(target.actualStroke()));
		}
		n.strokeThickness(this.thickness());
		n.timeStamp(target.getHashCode());
		if (!rect.isEmpty()) {
			frame.rects().add(n.timeStamp(), n);
		}
	}
	,
	renderFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.renderFrame.call(this, frame, view);
		var rectPool = this.highlightView().rectPool();
		var f = frame;
		var i = 0;
		var en = f.rects().values().getEnumerator();
		while (en.moveNext()) {
			var rect = en.current();
			var rectVisual = rectPool.item(i);
			this.renderRect(rect, rectVisual);
			i++;
		}
		rectPool.count(i);
	}
	,
	renderRect: function (rect, rectVisual) {
		if ($.ig.util.isNaN(rect.left()) || $.ig.util.isNaN(rect.top()) || $.ig.util.isNaN(rect.bottom()) || $.ig.util.isNaN(rect.right()) || $.ig.util.isNaN(rect.strokeThickness())) {
			rectVisual.__visibility = $.ig.Visibility.prototype.collapsed;
			return;
		}
		this.highlightView().positionRectangle(rectVisual, rect.left(), rect.top(), rect.right() - rect.left(), rect.bottom() - rect.top());
		rectVisual.__fill = rect.brush();
		rectVisual.__stroke = rect.outline();
		rectVisual.strokeThickness(rect.strokeThickness());
		if (this.dashArray() != null) {
			rectVisual.strokeDashArray(this.dashArray());
		} else {
			rectVisual.strokeDashArray(null);
		}
		rectVisual.strokeDashCap(this.dashCap());
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.AnnotationLayer.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.CategoryHighlightLayer.prototype.targetAxisPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.CategoryHighlightLayer.prototype.useInterpolationPropertyName:
				this.renderSeries(true);
				break;
		}
	}
	,
	$type: new $.ig.Type('CategoryHighlightLayer', $.ig.AnnotationLayer.prototype.$type)
}, true);

$.ig.util.defType('CategoryHighlightLayerFrame', 'Frame', {
	init: function () {
		$.ig.Frame.prototype.init.call(this);
		this.rects(new $.ig.Dictionary$2($.ig.Number.prototype.$type, $.ig.CategoryHighlightFrameRect.prototype.$type, 0));
		this.__interpolator = new $.ig.DictInterpolator$3($.ig.Number.prototype.$type, $.ig.CategoryHighlightFrameRect.prototype.$type, $.ig.CategoryHighlightLayerFrame.prototype.$type, this.interpolateRect.runOn(this), function (r) { return r.timeStamp(); }, function (r) { return true; }, function () { return new $.ig.CategoryHighlightFrameRect(); });
	},
	_rects: null,
	rects: function (value) {
		if (arguments.length === 1) {
			this._rects = value;
			return value;
		} else {
			return this._rects;
		}
	}
	,
	__interpolator: null,
	interpolateRect: function (interpolated, p, min, max, minFrame, maxFrame) {
		if (max != null) {
			interpolated.timeStamp(max.timeStamp());
		} else if (min != null) {
			interpolated.timeStamp(min.timeStamp());
		} else {
			interpolated.timeStamp(0);
		}
		if (min == null || $.ig.util.isNaN(min.top())) {
			interpolated.top(max != null ? max.top() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.top())) {
			interpolated.top(NaN);
		} else {
			interpolated.top(min.top() + p * (max.top() - min.top()));
		}
		if (min == null || $.ig.util.isNaN(min.left())) {
			interpolated.left(max != null ? max.left() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.left())) {
			interpolated.left(NaN);
		} else {
			interpolated.left(min.left() + p * (max.left() - min.left()));
		}
		if (min == null || $.ig.util.isNaN(min.right())) {
			interpolated.right(max != null ? max.right() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.right())) {
			interpolated.right(NaN);
		} else {
			interpolated.right(min.right() + p * (max.right() - min.right()));
		}
		if (min == null || $.ig.util.isNaN(min.bottom())) {
			interpolated.bottom(max != null ? max.bottom() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.bottom())) {
			interpolated.bottom(NaN);
		} else {
			interpolated.bottom(min.bottom() + p * (max.bottom() - min.bottom()));
		}
		if (min == null || $.ig.util.isNaN(min.strokeThickness())) {
			interpolated.strokeThickness(max != null ? max.strokeThickness() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.strokeThickness())) {
			interpolated.strokeThickness(NaN);
		} else {
			interpolated.strokeThickness(min.strokeThickness() + p * (max.strokeThickness() - min.strokeThickness()));
		}
		if (min == null || min.brush() == null) {
			interpolated.brush(max != null ? max.brush() : null);
		} else if (max == null || max.brush() == null) {
			interpolated.brush(null);
		} else {
			interpolated.brush($.ig.BrushUtil.prototype.getInterpolation(min.brush(), p, max.brush(), $.ig.InterpolationMode.prototype.rGB));
		}
		if (min == null || min.outline() == null) {
			interpolated.outline(max != null ? max.outline() : null);
		} else if (max == null || max.outline() == null) {
			interpolated.outline(null);
		} else {
			interpolated.outline($.ig.BrushUtil.prototype.getInterpolation(min.outline(), p, max.outline(), $.ig.InterpolationMode.prototype.rGB));
		}
	}
	,
	interpolate3: function (p, min, max) {
		var mn = min;
		var mx = max;
		this.__interpolator.interpolate(this.rects(), p, mn.rects(), mx.rects(), mn, mx);
	}
	,
	$type: new $.ig.Type('CategoryHighlightLayerFrame', $.ig.Frame.prototype.$type)
}, true);

$.ig.util.defType('CategoryHighlightFrameRect', 'Object', {
	_timeStamp: 0,
	timeStamp: function (value) {
		if (arguments.length === 1) {
			this._timeStamp = value;
			return value;
		} else {
			return this._timeStamp;
		}
	}
	,
	_top: 0,
	top: function (value) {
		if (arguments.length === 1) {
			this._top = value;
			return value;
		} else {
			return this._top;
		}
	}
	,
	_left: 0,
	left: function (value) {
		if (arguments.length === 1) {
			this._left = value;
			return value;
		} else {
			return this._left;
		}
	}
	,
	_right: 0,
	right: function (value) {
		if (arguments.length === 1) {
			this._right = value;
			return value;
		} else {
			return this._right;
		}
	}
	,
	_bottom: 0,
	bottom: function (value) {
		if (arguments.length === 1) {
			this._bottom = value;
			return value;
		} else {
			return this._bottom;
		}
	}
	,
	_brush: null,
	brush: function (value) {
		if (arguments.length === 1) {
			this._brush = value;
			return value;
		} else {
			return this._brush;
		}
	}
	,
	_outline: null,
	outline: function (value) {
		if (arguments.length === 1) {
			this._outline = value;
			return value;
		} else {
			return this._outline;
		}
	}
	,
	_strokeThickness: 0,
	strokeThickness: function (value) {
		if (arguments.length === 1) {
			this._strokeThickness = value;
			return value;
		} else {
			return this._strokeThickness;
		}
	}
	,
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.timeStamp(0);
		this.top(NaN);
		this.left(NaN);
		this.right(NaN);
		this.left(NaN);
		this.bottom(NaN);
		this.strokeThickness(NaN);
	},
	$type: new $.ig.Type('CategoryHighlightFrameRect', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('CategoryItemHighlightLayer', 'AnnotationLayer', {
	init: function () {
		this.__useInterpolation = false;
		this.__skipUnknownValues = true;
		this.__mode0HighlightWidth = 10;
		$.ig.AnnotationLayer.prototype.init.call(this);
		var previousFrame = new $.ig.CategoryItemHighlightLayerFrame();
		var currentFrame = new $.ig.CategoryItemHighlightLayerFrame();
		var transitionFrame = new $.ig.CategoryItemHighlightLayerFrame();
		this.previousFrame(previousFrame);
		this.currentFrame(currentFrame);
		this.transitionFrame(transitionFrame);
		this.defaultStyleKey($.ig.CategoryItemHighlightLayer.prototype.$type);
	},
	createView: function () {
		return new $.ig.CategoryItemHighlightLayerView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AnnotationLayer.prototype.onViewCreated.call(this, view);
		this.itemView(view);
	}
	,
	_itemView: null,
	itemView: function (value) {
		if (arguments.length === 1) {
			this._itemView = value;
			return value;
		} else {
			return this._itemView;
		}
	}
	,
	targetSeries: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryItemHighlightLayer.prototype.targetSeriesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryItemHighlightLayer.prototype.targetSeriesProperty);
		}
	}
	,
	useInterpolation: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryItemHighlightLayer.prototype.useInterpolationProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryItemHighlightLayer.prototype.useInterpolationProperty);
		}
	}
	,
	highlightType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryItemHighlightLayer.prototype.highlightTypeProperty, $.ig.CategoryItemHighlightType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.CategoryItemHighlightLayer.prototype.highlightTypeProperty));
		}
	}
	,
	markerTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryItemHighlightLayer.prototype.markerTemplateProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryItemHighlightLayer.prototype.markerTemplateProperty);
		}
	}
	,
	bandHighlightWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryItemHighlightLayer.prototype.bandHighlightWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryItemHighlightLayer.prototype.bandHighlightWidthProperty);
		}
	}
	,
	skipUnknownValues: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryItemHighlightLayer.prototype.skipUnknownValuesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryItemHighlightLayer.prototype.skipUnknownValuesProperty);
		}
	}
	,
	onDependentSeriesRender: function (series, animate) {
		if (this.targetSeries() == null || this.targetSeries() == series) {
			if (!this.contentInfo().isDirty()) {
				this.renderSeries(animate);
			}
		}
	}
	,
	isSeriesValid: function (series) {
		if (!$.ig.AnnotationLayer.prototype.isSeriesValid.call(this, series)) {
			return false;
		}
		var res = this.getCategoryPosition(series);
		if (!res.item1()) {
			return false;
		}
		return true;
	}
	,
	prepareFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.prepareFrame.call(this, frame, view);
		var f = frame;
		var useInterpolation = this.useInterpolation();
		f.rects().clear();
		if ($.ig.util.isNaN(this._currentPosition.__x) && $.ig.util.isNaN(this._currentPosition.__y)) {
			return;
		}
		if (this.isSeriesValid(this.targetSeries())) {
			this.prepareSeries(this.targetSeries(), f, useInterpolation);
		} else {
			var en = this.seriesViewer().series().getEnumerator();
			while (en.moveNext()) {
				var series = en.current();
				if (this.isSeriesValid(series)) {
					this.prepareSeries(series, f, useInterpolation);
				}
			}
		}
	}
	,
	__useInterpolation: false,
	__skipUnknownValues: false,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.AnnotationLayer.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.CategoryItemHighlightLayer.prototype.targetSeriesPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.CategoryItemHighlightLayer.prototype.useInterpolationPropertyName:
				this.__useInterpolation = newValue;
				this.renderSeries(true);
				break;
			case $.ig.CategoryItemHighlightLayer.prototype.highlightTypePropertyName:
				this.renderSeries(true);
				break;
			case $.ig.CategoryItemHighlightLayer.prototype.bandHighlightWidthPropertyName:
				this.__mode0HighlightWidth = this.bandHighlightWidth();
				this.renderSeries(true);
				break;
			case $.ig.CategoryItemHighlightLayer.prototype.skipUnknownValuesPropertyName:
				this.__skipUnknownValues = newValue;
				this.renderSeries(true);
				break;
		}
	}
	,
	__mode0HighlightWidth: 0,
	prepareSeries: function (series, f, useInterpolation) {
		if (this.view() == null || this.view().windowRect().isEmpty() || this.view().viewport().isEmpty()) {
			return;
		}
		if (!series.isCategory() && !series.isFinancial()) {
			return;
		}
		var cat = series;
		if (cat.categoryAxis() == null) {
			return;
		}
		var xPos = this._currentPosition.__x;
		xPos = this.view().viewport().left() + this.view().viewport().width() * (xPos - this.view().windowRect().left()) / this.view().windowRect().width();
		var yPos = this._currentPosition.__y;
		yPos = this.view().viewport().top() + this.view().viewport().height() * (yPos - this.view().windowRect().top()) / this.view().windowRect().height();
		var skipUnknowns = this.skipUnknownValues();
		var isPointTypeCategorySeries = true;
		var modePref = $.ig.util.cast($.ig.IHasCategoryModePreference.prototype.$type, series);
		var preferredMode = $.ig.CategoryMode.prototype.mode0;
		if (modePref != null) {
			preferredMode = modePref.preferredCategoryMode(cat.categoryAxis());
			if (preferredMode == $.ig.CategoryMode.prototype.mode2) {
				isPointTypeCategorySeries = false;
			}
		}
		var usePointType = true;
		if (!isPointTypeCategorySeries || this.highlightType() == $.ig.CategoryItemHighlightType.prototype.shape) {
			usePointType = false;
		}
		if (this.highlightType() == $.ig.CategoryItemHighlightType.prototype.marker) {
			usePointType = true;
		}
		var res = this.getCategoryPosition(series);
		if (!res.item1()) {
			return;
		}
		if (usePointType) {
			var pt = series.getSeriesValuePosition(res.item2(), this.useInterpolation(), skipUnknowns);
			if (preferredMode != $.ig.CategoryMode.prototype.mode0 && !cat.categoryAxis().isSorting()) {
				var x = pt.__x;
				if (series.isFinancial()) {
					x -= (series).getOffsetValue();
					x += (series).getCategoryWidth() * 0.5;
					pt = { __x: x, __y: pt.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				} else if (series.isCategory()) {
					x -= (series).getOffsetValue();
					x += (series).getCategoryWidth() * 0.5;
					pt = { __x: x, __y: pt.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				}
			}
			if (!$.ig.util.isNaN(pt.__x) && !$.ig.util.isNaN(pt.__y)) {
				pt = this.toWorldPosition(pt);
			}
			this.preparePoint(pt, f, series);
		} else {
			var pt1 = series.getSeriesValuePosition(res.item2(), this.useInterpolation(), skipUnknowns);
			var rect;
			var effectiveViewport = this.getEffectiveViewport1(this.view());
			if (series.isVertical()) {
				var y = pt1.__y;
				var catAxis = cat.categoryAxis();
				var groupWidth = this.__mode0HighlightWidth;
				if (preferredMode != $.ig.CategoryMode.prototype.mode0) {
					groupWidth = catAxis.getGroupSize(this.view().windowRect(), this.view().viewport(), effectiveViewport);
				}
				if ($.ig.util.isNaN(groupWidth) || Number.isInfinity(groupWidth)) {
					return;
				}
				rect = new $.ig.Rect(0, this.viewport().left(), y - groupWidth / 2, this.viewport().width(), groupWidth);
			} else {
				var x1 = pt1.__x;
				var catAxis1 = cat.categoryAxis();
				var groupWidth1 = this.__mode0HighlightWidth;
				if (preferredMode != $.ig.CategoryMode.prototype.mode0) {
					groupWidth1 = catAxis1.getGroupSize(this.view().windowRect(), this.view().viewport(), effectiveViewport);
				}
				if ($.ig.util.isNaN(groupWidth1) || Number.isInfinity(groupWidth1)) {
					return;
				}
				rect = new $.ig.Rect(0, x1 - groupWidth1 / 2, this.viewport().top(), groupWidth1, this.viewport().height());
			}
			rect.intersect(this.view().viewport());
			this.prepareRect(rect, f, series);
		}
	}
	,
	preparePoint: function (pt, frame, target) {
		var n = new $.ig.ItemHighlightFrameRect();
		n.left(pt.__x);
		n.top(pt.__y);
		n.brush(this.getBrush());
		n.outline(this.getOutline());
		if (n.brush() == null) {
			if (target.hasMarkers()) {
				n.brush(this.itemView().getLightenedBrush(target.getActualMarkerBrush()));
			} else {
				n.brush(this.itemView().getLightenedBrush(target.actualBrush()));
			}
		}
		if (n.outline() == null) {
			if (target.hasMarkers()) {
				n.outline(this.itemView().getLightenedBrush(target.getActualMarkerOutlineBrush()));
			} else {
				n.outline(this.itemView().getLightenedBrush(target.actualOutline()));
			}
		}
		n.strokeThickness(this.thickness());
		n.timeStamp(target.getHashCode());
		n.dataItem(this.getDataItem(target, pt));
		var targetTemplate = ($.ig.util.cast($.ig.MarkerSeries.prototype.$type, target) !== null) ? (target).actualMarkerTemplate() : null;
		if (targetTemplate == null || targetTemplate == $.ig.MarkerSeries.prototype.nullMarkerTemplate()) {
			var markerType = $.ig.MarkerSeries.prototype.resolveMarkerType(target, $.ig.MarkerType.prototype.automatic);
			switch (markerType) {
				case $.ig.MarkerType.prototype.circle:
					targetTemplate = this.seriesViewer().circleMarkerTemplate();
					break;
				case $.ig.MarkerType.prototype.triangle:
					targetTemplate = this.seriesViewer().triangleMarkerTemplate();
					break;
				case $.ig.MarkerType.prototype.pyramid:
					targetTemplate = this.seriesViewer().pyramidMarkerTemplate();
					break;
				case $.ig.MarkerType.prototype.square:
					targetTemplate = this.seriesViewer().squareMarkerTemplate();
					break;
				case $.ig.MarkerType.prototype.diamond:
					targetTemplate = this.seriesViewer().diamondMarkerTemplate();
					break;
				case $.ig.MarkerType.prototype.pentagon:
					targetTemplate = this.seriesViewer().pentagonMarkerTemplate();
					break;
				case $.ig.MarkerType.prototype.hexagon:
					targetTemplate = this.seriesViewer().hexagonMarkerTemplate();
					break;
				case $.ig.MarkerType.prototype.tetragram:
					targetTemplate = this.seriesViewer().tetragramMarkerTemplate();
					break;
				case $.ig.MarkerType.prototype.pentagram:
					targetTemplate = this.seriesViewer().pentagramMarkerTemplate();
					break;
				case $.ig.MarkerType.prototype.hexagram:
					targetTemplate = this.seriesViewer().hexagramMarkerTemplate();
					break;
			}
		}
		n.markerTemplate(this.markerTemplate() != null ? this.markerTemplate() : targetTemplate);
		if (!$.ig.util.isNaN(n.left()) && !$.ig.util.isNaN(n.top())) {
			frame.rects().add(n.timeStamp(), n);
		}
	}
	,
	getDataItem: function (target, pt) {
		var item = target.getItem(pt);
		var exactIndex = target.getExactItemIndex(pt);
		if (this.useInterpolation() && exactIndex != Math.floor(exactIndex)) {
			item = null;
		}
		return (function () {
			var $ret = new $.ig.DataContext();
			$ret.series(target);
			$ret.item(item);
			return $ret;
		}());
	}
	,
	prepareRect: function (rect, frame, target) {
		var n = new $.ig.ItemHighlightFrameRect();
		n.left(rect.left());
		n.top(rect.top());
		n.right(rect.right());
		n.bottom(rect.bottom());
		n.brush(this.getBrush());
		n.outline(this.getOutline());
		if (n.brush() == null) {
			if (target != null) {
				n.brush(this.itemView().getLightenedTranslucentBrush(target.actualBrush()));
			}
		}
		if (n.outline() == null) {
			if (target != null) {
				n.outline(this.itemView().getLightenedTranslucentBrush(target.actualOutline()));
			}
		}
		n.strokeThickness(this.thickness());
		n.timeStamp(target.getHashCode());
		if (!rect.isEmpty()) {
			frame.rects().add(n.timeStamp(), n);
		}
	}
	,
	renderFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.renderFrame.call(this, frame, view);
		var rectPool = this.itemView().rectPool();
		var markerPool = this.itemView().markerPool();
		var rectCount = 0;
		var markerCount = 0;
		var f = frame;
		var en = f.rects().values().getEnumerator();
		while (en.moveNext()) {
			var rect = en.current();
			if (rect.markerTemplate() != null) {
				var markerVisual = markerPool.item(markerCount);
				this.renderPoint(rect, markerVisual);
				markerCount++;
			} else {
				var rectVisual = rectPool.item(rectCount);
				this.renderRect(rect, rectVisual);
				rectCount++;
			}
		}
		rectPool.count(rectCount);
		markerPool.count(markerCount);
	}
	,
	renderPoint: function (rect, markerVisual) {
		if ($.ig.util.isNaN(rect.left()) || $.ig.util.isNaN(rect.top()) || $.ig.util.isNaN(rect.strokeThickness()) || rect.dataItem() == null) {
			markerVisual.__visibility = $.ig.Visibility.prototype.collapsed;
			return;
		}
		var pt = { __x: rect.left(), __y: rect.top(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var dc = rect.dataItem();
		dc.itemBrush(rect.brush());
		var newPt = (dc.series()).getSeriesValuePosition(pt, this.animationActive() || this.__useInterpolation, this.__skipUnknownValues);
		if ($.ig.util.isNaN(newPt.__x) || $.ig.util.isNaN(newPt.__y)) {
			markerVisual.__visibility = $.ig.Visibility.prototype.collapsed;
			return;
		}
		this.itemView().positionMarker(markerVisual, newPt.__x, newPt.__y);
		markerVisual.contentTemplate(rect.markerTemplate());
		markerVisual.content(rect.dataItem());
	}
	,
	renderRect: function (rect, rectVisual) {
		if ($.ig.util.isNaN(rect.left()) || $.ig.util.isNaN(rect.top()) || $.ig.util.isNaN(rect.bottom()) || $.ig.util.isNaN(rect.right()) || $.ig.util.isNaN(rect.strokeThickness())) {
			rectVisual.__visibility = $.ig.Visibility.prototype.collapsed;
			return;
		}
		this.itemView().positionRectangle(rectVisual, rect.left(), rect.top(), rect.right() - rect.left(), rect.bottom() - rect.top());
		rectVisual.__fill = rect.brush();
		rectVisual.__stroke = rect.outline();
		rectVisual.strokeThickness(rect.strokeThickness());
		if (this.dashArray() != null) {
			rectVisual.strokeDashArray(this.dashArray());
		} else {
			rectVisual.strokeDashArray(null);
		}
		rectVisual.strokeDashCap(this.dashCap());
	}
	,
	$type: new $.ig.Type('CategoryItemHighlightLayer', $.ig.AnnotationLayer.prototype.$type)
}, true);

$.ig.util.defType('CategoryItemHighlightLayerFrame', 'Frame', {
	init: function () {
		$.ig.Frame.prototype.init.call(this);
		this.rects(new $.ig.Dictionary$2($.ig.Number.prototype.$type, $.ig.ItemHighlightFrameRect.prototype.$type, 0));
		this.__interpolator = new $.ig.DictInterpolator$3($.ig.Number.prototype.$type, $.ig.ItemHighlightFrameRect.prototype.$type, $.ig.CategoryItemHighlightLayerFrame.prototype.$type, this.interpolateRect.runOn(this), function (r) { return r.timeStamp(); }, function (r) { return true; }, function () { return new $.ig.ItemHighlightFrameRect(); });
	},
	_rects: null,
	rects: function (value) {
		if (arguments.length === 1) {
			this._rects = value;
			return value;
		} else {
			return this._rects;
		}
	}
	,
	__interpolator: null,
	interpolateRect: function (interpolated, p, min, max, minFrame, maxFrame) {
		if (max != null) {
			interpolated.timeStamp(max.timeStamp());
		} else if (min != null) {
			interpolated.timeStamp(min.timeStamp());
		} else {
			interpolated.timeStamp(0);
		}
		if (min == null || $.ig.util.isNaN(min.top())) {
			interpolated.top(max != null ? max.top() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.top())) {
			interpolated.top(NaN);
		} else {
			interpolated.top(min.top() + p * (max.top() - min.top()));
		}
		if (min == null || $.ig.util.isNaN(min.left())) {
			interpolated.left(max != null ? max.left() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.left())) {
			interpolated.left(NaN);
		} else {
			interpolated.left(min.left() + p * (max.left() - min.left()));
		}
		if (min == null || $.ig.util.isNaN(min.right())) {
			interpolated.right(max != null ? max.right() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.right())) {
			interpolated.right(NaN);
		} else {
			interpolated.right(min.right() + p * (max.right() - min.right()));
		}
		if (min == null || $.ig.util.isNaN(min.bottom())) {
			interpolated.bottom(max != null ? max.bottom() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.bottom())) {
			interpolated.bottom(NaN);
		} else {
			interpolated.bottom(min.bottom() + p * (max.bottom() - min.bottom()));
		}
		if (min == null || $.ig.util.isNaN(min.strokeThickness())) {
			interpolated.strokeThickness(max != null ? max.strokeThickness() : NaN);
		} else if (max == null || $.ig.util.isNaN(max.strokeThickness())) {
			interpolated.strokeThickness(NaN);
		} else {
			interpolated.strokeThickness(min.strokeThickness() + p * (max.strokeThickness() - min.strokeThickness()));
		}
		if (min == null || min.brush() == null) {
			interpolated.brush(max != null ? max.brush() : null);
		} else if (max == null || max.brush() == null) {
			interpolated.brush(null);
		} else {
			interpolated.brush($.ig.BrushUtil.prototype.getInterpolation(min.brush(), p, max.brush(), $.ig.InterpolationMode.prototype.rGB));
		}
		if (min == null || min.outline() == null) {
			interpolated.outline(max != null ? max.outline() : null);
		} else if (max == null || max.outline() == null) {
			interpolated.outline(null);
		} else {
			interpolated.outline($.ig.BrushUtil.prototype.getInterpolation(min.outline(), p, max.outline(), $.ig.InterpolationMode.prototype.rGB));
		}
		if (min == null || min.markerTemplate() == null) {
			interpolated.markerTemplate(max != null ? max.markerTemplate() : null);
		} else if (max == null || max.outline() == null) {
			interpolated.markerTemplate(null);
		} else {
			interpolated.markerTemplate(max.markerTemplate());
		}
		if (min == null || min.dataItem() == null) {
			interpolated.dataItem(max != null ? max.dataItem() : null);
		} else if (max == null || max.outline() == null) {
			interpolated.dataItem(null);
		} else {
			interpolated.dataItem(max.dataItem());
		}
	}
	,
	interpolate3: function (p, min, max) {
		var mn = min;
		var mx = max;
		this.__interpolator.interpolate(this.rects(), p, mn.rects(), mx.rects(), mn, mx);
	}
	,
	$type: new $.ig.Type('CategoryItemHighlightLayerFrame', $.ig.Frame.prototype.$type)
}, true);

$.ig.util.defType('ItemHighlightFrameRect', 'Object', {
	_timeStamp: 0,
	timeStamp: function (value) {
		if (arguments.length === 1) {
			this._timeStamp = value;
			return value;
		} else {
			return this._timeStamp;
		}
	}
	,
	_top: 0,
	top: function (value) {
		if (arguments.length === 1) {
			this._top = value;
			return value;
		} else {
			return this._top;
		}
	}
	,
	_left: 0,
	left: function (value) {
		if (arguments.length === 1) {
			this._left = value;
			return value;
		} else {
			return this._left;
		}
	}
	,
	_right: 0,
	right: function (value) {
		if (arguments.length === 1) {
			this._right = value;
			return value;
		} else {
			return this._right;
		}
	}
	,
	_bottom: 0,
	bottom: function (value) {
		if (arguments.length === 1) {
			this._bottom = value;
			return value;
		} else {
			return this._bottom;
		}
	}
	,
	_brush: null,
	brush: function (value) {
		if (arguments.length === 1) {
			this._brush = value;
			return value;
		} else {
			return this._brush;
		}
	}
	,
	_outline: null,
	outline: function (value) {
		if (arguments.length === 1) {
			this._outline = value;
			return value;
		} else {
			return this._outline;
		}
	}
	,
	_markerTemplate: null,
	markerTemplate: function (value) {
		if (arguments.length === 1) {
			this._markerTemplate = value;
			return value;
		} else {
			return this._markerTemplate;
		}
	}
	,
	_dataItem: null,
	dataItem: function (value) {
		if (arguments.length === 1) {
			this._dataItem = value;
			return value;
		} else {
			return this._dataItem;
		}
	}
	,
	_strokeThickness: 0,
	strokeThickness: function (value) {
		if (arguments.length === 1) {
			this._strokeThickness = value;
			return value;
		} else {
			return this._strokeThickness;
		}
	}
	,
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.timeStamp(0);
		this.top(NaN);
		this.left(NaN);
		this.right(NaN);
		this.left(NaN);
		this.bottom(NaN);
		this.strokeThickness(NaN);
	},
	$type: new $.ig.Type('ItemHighlightFrameRect', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('CrosshairLayer', 'AnnotationLayer', {
	init: function () {
		$.ig.AnnotationLayer.prototype.init.call(this);
		var previousFrame = new $.ig.CrosshairLayerFrame();
		var currentFrame = new $.ig.CrosshairLayerFrame();
		var transitionFrame = new $.ig.CrosshairLayerFrame();
		var animationRate = this.transitionDuration() / 1000;
		this.previousFrame(previousFrame);
		this.currentFrame(currentFrame);
		this.transitionFrame(transitionFrame);
		this.defaultStyleKey($.ig.CrosshairLayer.prototype.$type);
	},
	createView: function () {
		return new $.ig.CrosshairLayerView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AnnotationLayer.prototype.onViewCreated.call(this, view);
		this.crosshairView(view);
	}
	,
	_crosshairView: null,
	crosshairView: function (value) {
		if (arguments.length === 1) {
			this._crosshairView = value;
			return value;
		} else {
			return this._crosshairView;
		}
	}
	,
	targetSeries: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CrosshairLayer.prototype.targetSeriesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CrosshairLayer.prototype.targetSeriesProperty);
		}
	}
	,
	useInterpolation: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CrosshairLayer.prototype.useInterpolationProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CrosshairLayer.prototype.useInterpolationProperty);
		}
	}
	,
	verticalLineVisibility: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CrosshairLayer.prototype.verticalLineVisibilityProperty, $.ig.Visibility.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.CrosshairLayer.prototype.verticalLineVisibilityProperty));
		}
	}
	,
	horizontalLineVisibility: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CrosshairLayer.prototype.horizontalLineVisibilityProperty, $.ig.Visibility.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.CrosshairLayer.prototype.horizontalLineVisibilityProperty));
		}
	}
	,
	skipUnknownValues: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CrosshairLayer.prototype.skipUnknownValuesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CrosshairLayer.prototype.skipUnknownValuesProperty);
		}
	}
	,
	onDependentSeriesRender: function (series, animate) {
		if (this.targetSeries() == null || this.targetSeries() == series) {
			if (!this.contentInfo().isDirty()) {
				this.renderSeries(animate);
			}
		}
	}
	,
	prepareFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.prepareFrame.call(this, frame, view);
		var f = frame;
		var useInterpolation = this.useInterpolation();
		var skipUnknowns = this.skipUnknownValues();
		f.lineHeights().clear();
		f.lineWidths().clear();
		f.lineBrushes().clear();
		if (this.isSeriesValid(this.targetSeries())) {
			this.prepareSeries(this.targetSeries(), f, useInterpolation, skipUnknowns);
		} else {
			var en = this.seriesViewer().series().getEnumerator();
			while (en.moveNext()) {
				var series = en.current();
				if (this.isSeriesValid(series)) {
					this.prepareSeries(series, f, useInterpolation, skipUnknowns);
				}
			}
		}
	}
	,
	isSeriesValid: function (series) {
		if (!$.ig.AnnotationLayer.prototype.isSeriesValid.call(this, series)) {
			return false;
		}
		var res = this.getCategoryPosition(series);
		if (!res.item1()) {
			return false;
		}
		return true;
	}
	,
	prepareSeries: function (series, f, useInterpolation, skipUnknowns) {
		var res = this.getCategoryPosition(series);
		if (!res.item1()) {
			return;
		}
		var pos = res.item2();
		pos = series.getSeriesValuePosition(pos, useInterpolation, skipUnknowns);
		var brush = this.actualBrush();
		if (brush == null) {
			brush = this.brush();
		}
		if (brush == null) {
			brush = series.actualBrush();
			if (brush != null) {
				brush = this.crosshairView().getLightenedBrush(brush);
			}
		}
		f.lineBrushes().add(brush);
		f.lineHeights().add(pos.__y);
		f.lineWidths().add(pos.__x);
	}
	,
	renderFrame: function (frame, view) {
		$.ig.AnnotationLayer.prototype.renderFrame.call(this, frame, view);
		var linePool = this.crosshairView().linePool();
		var f = frame;
		var count = f.lineHeights().count();
		var lineCount = 0;
		var left = view.viewport().left();
		var right = view.viewport().right();
		var top = view.viewport().top();
		var bottom = view.viewport().bottom();
		var vertVisible = this.verticalLineVisibility() == $.ig.Visibility.prototype.visible;
		var horizVisible = this.horizontalLineVisibility() == $.ig.Visibility.prototype.visible;
		for (var i = 0; i < count; i++) {
			var yPos = Math.floor(f.lineHeights().__inner[i]);
			var xPos = Math.floor(f.lineWidths().__inner[i]);
			if ($.ig.util.isNaN(xPos) || $.ig.util.isNaN(yPos)) {
				continue;
			}
			var line = linePool.item(lineCount);
			lineCount++;
			line.__stroke = f.lineBrushes().__inner[i];
			line.strokeThickness(this.thickness());
			line.__fill = f.lineBrushes().__inner[i];
			line.strokeDashArray(this.dashArray());
			line.strokeDashCap(this.dashCap());
			var pg = new $.ig.PathGeometry();
			if (vertVisible) {
				var pf = new $.ig.PathFigure();
				pf.__startPoint = { __x: xPos, __y: top, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				var ls = new $.ig.LineSegment(1);
				ls.point({ __x: xPos, __y: bottom, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				pf.__segments.add(ls);
				pg.figures().add(pf);
			}
			if (horizVisible) {
				var pf1 = new $.ig.PathFigure();
				pf1.__startPoint = { __x: left, __y: yPos, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				var ls1 = new $.ig.LineSegment(1);
				ls1.point({ __x: right, __y: yPos, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				pf1.__segments.add(ls1);
				pg.figures().add(pf1);
			}
			line.data(pg);
		}
		linePool.count(lineCount);
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.AnnotationLayer.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.CrosshairLayer.prototype.horizontalLineVisibilityPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.CrosshairLayer.prototype.verticalLineVisibilityPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.CrosshairLayer.prototype.targetSeriesPropertyName:
				this.renderSeries(true);
				break;
			case $.ig.CrosshairLayer.prototype.useInterpolationPropertyName:
				this.renderSeries(true);
				break;
		}
	}
	,
	$type: new $.ig.Type('CrosshairLayer', $.ig.AnnotationLayer.prototype.$type)
}, true);

$.ig.util.defType('CrosshairLayerFrame', 'Frame', {
	_lineHeights: null,
	lineHeights: function (value) {
		if (arguments.length === 1) {
			this._lineHeights = value;
			return value;
		} else {
			return this._lineHeights;
		}
	}
	,
	_lineWidths: null,
	lineWidths: function (value) {
		if (arguments.length === 1) {
			this._lineWidths = value;
			return value;
		} else {
			return this._lineWidths;
		}
	}
	,
	_lineBrushes: null,
	lineBrushes: function (value) {
		if (arguments.length === 1) {
			this._lineBrushes = value;
			return value;
		} else {
			return this._lineBrushes;
		}
	}
	,
	init: function () {
		$.ig.Frame.prototype.init.call(this);
		this.lineHeights(new $.ig.List$1(Number, 0));
		this.lineWidths(new $.ig.List$1(Number, 0));
		this.lineBrushes(new $.ig.List$1($.ig.Brush.prototype.$type, 0));
	},
	interpolate3: function (p, min, max) {
		var mn = min;
		var mx = max;
		$.ig.Frame.prototype.interpolate1(this.lineHeights(), p, mn.lineHeights(), mx.lineHeights());
		$.ig.Frame.prototype.interpolate1(this.lineWidths(), p, mn.lineWidths(), mx.lineWidths());
		$.ig.Frame.prototype.interpolate2(this.lineBrushes(), p, mn.lineBrushes(), mx.lineBrushes(), $.ig.InterpolationMode.prototype.rGB);
	}
	,
	$type: new $.ig.Type('CrosshairLayerFrame', $.ig.Frame.prototype.$type)
}, true);

$.ig.util.defType('DictInterpolator$3', 'Object', {
	$tKey: null,
	$tValue: null,
	$tFrame: null,
	init: function ($tKey, $tValue, $tFrame, interpolatePointStrat, getKeyStrat, validPointStrat, createValueStrat) {
		this.$tKey = $tKey;
		this.$tValue = $tValue;
		this.$tFrame = $tFrame;
		this.$type = this.$type.specialize(this.$tKey, this.$tValue, this.$tFrame);
		$.ig.Object.prototype.init.call(this);
		this.interpolatePointStrat(interpolatePointStrat);
		this.getKeyStrat(getKeyStrat);
		this.validPointStrat(validPointStrat);
		this.createValueStrat(createValueStrat);
	},
	_interpolatePointStrat: null,
	interpolatePointStrat: function (value) {
		if (arguments.length === 1) {
			this._interpolatePointStrat = value;
			return value;
		} else {
			return this._interpolatePointStrat;
		}
	}
	,
	_getKeyStrat: null,
	getKeyStrat: function (value) {
		if (arguments.length === 1) {
			this._getKeyStrat = value;
			return value;
		} else {
			return this._getKeyStrat;
		}
	}
	,
	_validPointStrat: null,
	validPointStrat: function (value) {
		if (arguments.length === 1) {
			this._validPointStrat = value;
			return value;
		} else {
			return this._validPointStrat;
		}
	}
	,
	_createValueStrat: null,
	createValueStrat: function (value) {
		if (arguments.length === 1) {
			this._createValueStrat = value;
			return value;
		} else {
			return this._createValueStrat;
		}
	}
	,
	interpolate: function (target, p, min, max, minFrame, maxFrame) {
		var removeFromTarget = new $.ig.List$1(this.$tKey, 0);
		var en = target.keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			var minValue;
			var maxValue;
			var targetValue = target.item(key);
			var minContains = (function () { var $ret = min.tryGetValue(key, minValue); minValue = $ret.p1; return $ret.ret; }());
			var maxContains = (function () { var $ret = max.tryGetValue(key, maxValue); maxValue = $ret.p1; return $ret.ret; }());
			if (!minContains && !maxContains) {
				removeFromTarget.add(key);
			} else {
				this.interpolatePointStrat()(targetValue, p, minValue, maxValue, minFrame, maxFrame);
				if (!this.validPointStrat()(targetValue)) {
					removeFromTarget.add(key);
				}
			}
		}
		var en1 = removeFromTarget.getEnumerator();
		while (en1.moveNext()) {
			var key1 = en1.current();
			target.remove(key1);
		}
		var en2 = min.keys().getEnumerator();
		while (en2.moveNext()) {
			var key2 = en2.current();
			var minValue1 = min.item(key2);
			var maxValue1;
			var targetValue1;
			var $ret = max.tryGetValue(key2, maxValue1);
			maxValue1 = $ret.p1;
			var targetContains = (function () { var $ret = target.tryGetValue(key2, targetValue1); targetValue1 = $ret.p1; return $ret.ret; }());
			if (!targetContains) {
				targetValue1 = this.createValueStrat()();
				this.interpolatePointStrat()(targetValue1, p, minValue1, maxValue1, minFrame, maxFrame);
				if (!this.validPointStrat()(targetValue1)) {
					continue;
				}
				target.add(this.getKeyStrat()(targetValue1), targetValue1);
			}
		}
		var en3 = max.keys().getEnumerator();
		while (en3.moveNext()) {
			var key3 = en3.current();
			var maxValue2 = max.item(key3);
			var minValue2;
			var targetValue2;
			var minContains1 = (function () { var $ret = min.tryGetValue(key3, minValue2); minValue2 = $ret.p1; return $ret.ret; }());
			var targetContains1 = (function () { var $ret = target.tryGetValue(key3, targetValue2); targetValue2 = $ret.p1; return $ret.ret; }());
			if (!targetContains1 && !minContains1) {
				targetValue2 = this.createValueStrat()();
				this.interpolatePointStrat()(targetValue2, p, minValue2, maxValue2, minFrame, maxFrame);
				if (!this.validPointStrat()(targetValue2)) {
					continue;
				}
				target.add(this.getKeyStrat()(targetValue2), targetValue2);
			}
		}
	}
	,
	$type: new $.ig.Type('DictInterpolator$3', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('CategoryHighlightLayerView', 'AnnotationLayerView', {
	init: function (model) {
		$.ig.AnnotationLayerView.prototype.init.call(this, model);
		this.highlightModel(model);
		this.visibleRects(new $.ig.List$1($.ig.Rectangle.prototype.$type, 0));
	},
	_highlightModel: null,
	highlightModel: function (value) {
		if (arguments.length === 1) {
			this._highlightModel = value;
			return value;
		} else {
			return this._highlightModel;
		}
	}
	,
	_rectPool: null,
	rectPool: function (value) {
		if (arguments.length === 1) {
			this._rectPool = value;
			return value;
		} else {
			return this._rectPool;
		}
	}
	,
	onInit: function () {
		var $self = this;
		$.ig.AnnotationLayerView.prototype.onInit.call(this);
		this.rectPool((function () {
			var $ret = new $.ig.Pool$1($.ig.Rectangle.prototype.$type);
			$ret.create($self.rectCreate.runOn($self));
			$ret.activate($self.rectActivate.runOn($self));
			$ret.disactivate($self.rectDisactivate.runOn($self));
			$ret.destroy($self.rectDestroy.runOn($self));
			return $ret;
		}()));
	}
	,
	_visibleRects: null,
	visibleRects: function (value) {
		if (arguments.length === 1) {
			this._visibleRects = value;
			return value;
		} else {
			return this._visibleRects;
		}
	}
	,
	rectCreate: function () {
		var line = new $.ig.Rectangle();
		line.isHitTestVisible(false);
		this.visibleRects().add(line);
		return line;
	}
	,
	rectActivate: function (rect) {
		rect.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	rectDisactivate: function (rect) {
		rect.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	rectDestroy: function (rect) {
		this.visibleRects().remove(rect);
	}
	,
	getLightenedBrush: function (brush) {
		if (brush == null) {
			return brush;
		}
		return $.ig.BrushUtil.prototype.getLightened(brush, 0.1);
	}
	,
	getLightenedTranslucentBrush: function (brush) {
		if (brush != null) {
			var b = $.ig.BrushUtil.prototype.getLightened(brush, 0.3);
			if (b._isGradient) {
				var grad = b;
				if (grad._gradientStops != null) {
					for (var i = 0; i < grad._gradientStops.length; i++) {
						var stop = grad._gradientStops[i];
						stop.color($.ig.Color.prototype.fromArgb($.ig.truncate(Math.round((stop.color().a()) * 0.2)), stop.color().r(), stop.color().g(), stop.color().b()));
						stop.color(stop.color());
					}
				}
			} else {
				b.color($.ig.Color.prototype.fromArgb($.ig.truncate(Math.round((b.color().a()) * 0.2)), b.color().r(), b.color().g(), b.color().b()));
				b.color(b.color());
			}
			return b;
		} else {
			return null;
		}
	}
	,
	positionRectangle: function (rectVisual, left, top, width, height) {
		rectVisual.__visibility = $.ig.Visibility.prototype.visible;
		rectVisual.canvasLeft(left);
		rectVisual.canvasTop(top);
		rectVisual.width(width);
		rectVisual.height(height);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnnotationLayerView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender() && !isHitContext) {
			for (var i = 0; i < this.visibleRects().count(); i++) {
				var rect = this.visibleRects().__inner[i];
				if (rect.__visibility == $.ig.Visibility.prototype.visible) {
					context.renderRectangle(rect);
				}
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnnotationLayerView.prototype.exportViewShapes.call(this, svd);
		for (var i = 0; i < this.visibleRects().count(); i++) {
			var rectData = new $.ig.RectangleVisualData(1, "catItemRect", this.visibleRects().__inner[i]);
			rectData.tags().add("Main");
			rectData.tags().add("CategoryItem");
			svd.shapes().add(rectData);
		}
	}
	,
	$type: new $.ig.Type('CategoryHighlightLayerView', $.ig.AnnotationLayerView.prototype.$type)
}, true);

$.ig.util.defType('CategoryItemHighlightLayerView', 'AnnotationLayerView', {
	init: function (model) {
		$.ig.AnnotationLayerView.prototype.init.call(this, model);
		this.itemModel(model);
		this.visibleMarkers(new $.ig.List$1($.ig.ContentControl.prototype.$type, 0));
		this.visibleRects(new $.ig.List$1($.ig.Rectangle.prototype.$type, 0));
	},
	_itemModel: null,
	itemModel: function (value) {
		if (arguments.length === 1) {
			this._itemModel = value;
			return value;
		} else {
			return this._itemModel;
		}
	}
	,
	_rectPool: null,
	rectPool: function (value) {
		if (arguments.length === 1) {
			this._rectPool = value;
			return value;
		} else {
			return this._rectPool;
		}
	}
	,
	_markerPool: null,
	markerPool: function (value) {
		if (arguments.length === 1) {
			this._markerPool = value;
			return value;
		} else {
			return this._markerPool;
		}
	}
	,
	onInit: function () {
		var $self = this;
		$.ig.AnnotationLayerView.prototype.onInit.call(this);
		this.rectPool((function () {
			var $ret = new $.ig.Pool$1($.ig.Rectangle.prototype.$type);
			$ret.create($self.rectCreate.runOn($self));
			$ret.activate($self.rectActivate.runOn($self));
			$ret.disactivate($self.rectDisactivate.runOn($self));
			$ret.destroy($self.rectDestroy.runOn($self));
			return $ret;
		}()));
		this.markerPool((function () {
			var $ret = new $.ig.Pool$1($.ig.ContentControl.prototype.$type);
			$ret.create($self.markerCreate.runOn($self));
			$ret.activate($self.markerActivate.runOn($self));
			$ret.disactivate($self.markerDisactivate.runOn($self));
			$ret.destroy($self.markerDestroy.runOn($self));
			return $ret;
		}()));
	}
	,
	_visibleMarkers: null,
	visibleMarkers: function (value) {
		if (arguments.length === 1) {
			this._visibleMarkers = value;
			return value;
		} else {
			return this._visibleMarkers;
		}
	}
	,
	_visibleRects: null,
	visibleRects: function (value) {
		if (arguments.length === 1) {
			this._visibleRects = value;
			return value;
		} else {
			return this._visibleRects;
		}
	}
	,
	rectCreate: function () {
		var rect = new $.ig.Rectangle();
		rect.isHitTestVisible(false);
		this.visibleRects().add(rect);
		return rect;
	}
	,
	rectActivate: function (rect) {
		rect.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	rectDisactivate: function (rect) {
		rect.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	rectDestroy: function (rect) {
		this.visibleRects().remove(rect);
	}
	,
	markerCreate: function () {
		var marker = new $.ig.Marker();
		this.visibleMarkers().add(marker);
		return marker;
	}
	,
	markerActivate: function (marker) {
		marker.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	markerDisactivate: function (marker) {
		marker.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	markerDestroy: function (marker) {
		this.visibleMarkers().remove(marker);
	}
	,
	getLightenedBrush: function (brush) {
		if (brush == null) {
			return brush;
		}
		return $.ig.BrushUtil.prototype.getLightened(brush, 0.3);
	}
	,
	getLightenedTranslucentBrush: function (brush) {
		if (brush != null) {
			var b = $.ig.BrushUtil.prototype.getLightened(brush, 0.3);
			if (b._isGradient) {
				var grad = b;
				if (grad._gradientStops != null) {
					for (var i = 0; i < grad._gradientStops.length; i++) {
						var stop = grad._gradientStops[i];
						stop.color($.ig.Color.prototype.fromArgb($.ig.truncate(Math.round((stop.color().a()) * 0.3)), stop.color().r(), stop.color().g(), stop.color().b()));
						stop.color(stop.color());
					}
				}
			} else {
				b.color($.ig.Color.prototype.fromArgb($.ig.truncate(Math.round((b.color().a() & 255) * 0.3)), b.color().r(), b.color().g(), b.color().b()));
				b.color(b.color());
			}
			return b;
		} else {
			return null;
		}
	}
	,
	positionRectangle: function (rectVisual, left, top, width, height) {
		rectVisual.__visibility = $.ig.Visibility.prototype.visible;
		rectVisual.canvasLeft(left);
		rectVisual.canvasTop(top);
		rectVisual.width(width);
		rectVisual.height(height);
	}
	,
	positionMarker: function (markerVisual, x, y) {
		markerVisual.canvasLeft(x);
		markerVisual.canvasTop(y);
	}
	,
	setupMarkerAppearanceOverride: function (item, index) {
		$.ig.AnnotationLayerView.prototype.setupMarkerAppearanceOverride.call(this, item, index);
		var marker = item;
		var context = marker.content();
		if (context != null) {
			if (context.itemBrush() != null) {
				context.actualItemBrush(context.itemBrush());
			}
			var s = context.series();
			if (s.hasMarkers()) {
				context.outline(s.getActualMarkerOutlineBrush());
			}
			context.thickness(0.5);
		}
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnnotationLayerView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender() && !isHitContext) {
			for (var i = 0; i < this.visibleRects().count(); i++) {
				var rect = this.visibleRects().__inner[i];
				if (rect.__visibility == $.ig.Visibility.prototype.visible) {
					context.renderRectangle(rect);
				}
			}
			if (this.visibleMarkers().count() > 0) {
				var passInfo = new $.ig.DataTemplatePassInfo();
				passInfo.isHitTestRender = isHitContext;
				passInfo.context = context.getUnderlyingContext();
				passInfo.viewportTop = this.viewport().top();
				passInfo.viewportLeft = this.viewport().left();
				passInfo.viewportWidth = this.viewport().width();
				passInfo.viewportHeight = this.viewport().height();
				passInfo.passID = "ItemMarkers";
				var renderInfo = new $.ig.DataTemplateRenderInfo();
				renderInfo.isHitTestRender = isHitContext;
				renderInfo.passInfo = passInfo;
				var measureInfo = new $.ig.DataTemplateMeasureInfo();
				measureInfo.passInfo = passInfo;
				var isConstant = false;
				var cont = context.getUnderlyingContext();
				measureInfo.context = cont;
				renderInfo.context = cont;
				measureInfo.renderContext = context;
				renderInfo.renderContext = context;
				var constantWidth = 0;
				var constantHeight = 0;
				var _runningTemplates = new $.ig.Dictionary$2($.ig.DataTemplate.prototype.$type, $.ig.DataTemplate.prototype.$type, 0);
				for (var i1 = 0; i1 < this.visibleMarkers().count(); i1++) {
					var marker = this.visibleMarkers().__inner[i1];
					var template = marker.contentTemplate();
					if (!_runningTemplates.containsKey(template)) {
						_runningTemplates.add(template, template);
						if (template != null && template.passStarting() != null) {
							template.passStarting()(passInfo);
						}
					}
					if (marker.__visibility == $.ig.Visibility.prototype.collapsed) {
						continue;
					}
					this.setupMarkerAppearance(marker, i1, isHitContext);
					if (!isConstant) {
						measureInfo.data = marker.content();
						measureInfo.width = marker.width();
						measureInfo.height = marker.height();
						if (template.measure() != null) {
							measureInfo.data = marker.content();
							template.measure()(measureInfo);
							isConstant = measureInfo.isConstant;
							if (isConstant) {
								constantWidth = measureInfo.width;
								constantHeight = measureInfo.height;
							}
						}
						renderInfo.availableWidth = measureInfo.width;
						renderInfo.availableHeight = measureInfo.height;
					} else {
						renderInfo.availableWidth = constantWidth;
						renderInfo.availableHeight = constantHeight;
					}
					if (!$.ig.util.isNaN(marker.width()) && !Number.isInfinity(marker.width())) {
						renderInfo.availableWidth = marker.width();
					}
					if (!$.ig.util.isNaN(marker.height()) && !Number.isInfinity(marker.height())) {
						renderInfo.availableHeight = marker.height();
					}
					context.renderContentControl(renderInfo, marker);
					marker.actualWidth(renderInfo.availableWidth);
					marker.actualHeight(renderInfo.availableHeight);
				}
				var en = _runningTemplates.keys().getEnumerator();
				while (en.moveNext()) {
					var template1 = en.current();
					if (template1 != null && template1.passCompleted() != null) {
						template1.passCompleted()(passInfo);
					}
				}
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnnotationLayerView.prototype.exportViewShapes.call(this, svd);
		for (var i = 0; i < this.visibleRects().count(); i++) {
			var rectData = new $.ig.RectangleVisualData(1, "catItemRect", this.visibleRects().__inner[i]);
			rectData.tags().add("Main");
			rectData.tags().add("CategoryItem");
			svd.shapes().add(rectData);
		}
		for (var i1 = 0; i1 < this.visibleMarkers().count(); i1++) {
			svd.markerShapes().add(this.doToAllMarkers($.ig.util.cast($.ig.ContentControl.prototype.$type, this.visibleMarkers().__inner[i1])));
		}
	}
	,
	doToAllMarkers: function (m) {
		var mvd = new $.ig.MarkerVisualData();
		var appearance = new $.ig.PrimitiveAppearanceData();
		mvd.x(m.canvasLeft());
		mvd.y(m.canvasTop());
		mvd.bounds(new $.ig.Rect(0, m.canvasLeft() - (m.actualWidth() / 2), m.canvasTop() - (m.actualHeight() / 2), m.actualWidth(), m.actualHeight()));
		mvd.index(-1);
		mvd.contentTemplate(m.contentTemplate());
		if ($.ig.util.cast($.ig.DataContext.prototype.$type, m.content()) !== null) {
			var dataContext = m.content();
			appearance.fill($.ig.AppearanceHelper.prototype.fromBrush(dataContext.actualItemBrush()));
			appearance.stroke($.ig.AppearanceHelper.prototype.fromBrush(dataContext.outline()));
		}
		mvd.visibility(m.__visibility);
		mvd.markerAppearance(appearance);
		if (m.contentTemplate() == this.model().seriesViewer().circleMarkerTemplate()) {
			mvd.markerType("Circle");
		} else if (m.contentTemplate() == this.model().seriesViewer().diamondMarkerTemplate()) {
			mvd.markerType("Diamond");
		} else if (m.contentTemplate() == this.model().seriesViewer().hexagonMarkerTemplate()) {
			mvd.markerType("Hexagon");
		} else if (m.contentTemplate() == this.model().seriesViewer().hexagramMarkerTemplate()) {
			mvd.markerType("Hexagram");
		} else if (m.contentTemplate() == this.model().seriesViewer().pentagonMarkerTemplate()) {
			mvd.markerType("Pentagon");
		} else if (m.contentTemplate() == this.model().seriesViewer().pentagramMarkerTemplate()) {
			mvd.markerType("Pentagram");
		} else if (m.contentTemplate() == this.model().seriesViewer().pyramidMarkerTemplate()) {
			mvd.markerType("Pyramid");
		} else if (m.contentTemplate() == this.model().seriesViewer().squareMarkerTemplate()) {
			mvd.markerType("Square");
		} else if (m.contentTemplate() == this.model().seriesViewer().tetragramMarkerTemplate()) {
			mvd.markerType("Tetragram");
		} else if (m.contentTemplate() == this.model().seriesViewer().triangleMarkerTemplate()) {
			mvd.markerType("Triangle");
		} else {
			mvd.markerType("None");
		}
		return mvd;
	}
	,
	$type: new $.ig.Type('CategoryItemHighlightLayerView', $.ig.AnnotationLayerView.prototype.$type)
}, true);

$.ig.util.defType('CrosshairLayerView', 'AnnotationLayerView', {
	init: function (model) {
		$.ig.AnnotationLayerView.prototype.init.call(this, model);
		this.crosshairModel(model);
		this.visibleLines(new $.ig.List$1($.ig.Path.prototype.$type, 0));
	},
	_crosshairModel: null,
	crosshairModel: function (value) {
		if (arguments.length === 1) {
			this._crosshairModel = value;
			return value;
		} else {
			return this._crosshairModel;
		}
	}
	,
	_linePool: null,
	linePool: function (value) {
		if (arguments.length === 1) {
			this._linePool = value;
			return value;
		} else {
			return this._linePool;
		}
	}
	,
	_visibleLines: null,
	visibleLines: function (value) {
		if (arguments.length === 1) {
			this._visibleLines = value;
			return value;
		} else {
			return this._visibleLines;
		}
	}
	,
	onInit: function () {
		var $self = this;
		$.ig.AnnotationLayerView.prototype.onInit.call(this);
		this.linePool((function () {
			var $ret = new $.ig.Pool$1($.ig.Path.prototype.$type);
			$ret.create($self.lineCreate.runOn($self));
			$ret.activate($self.lineActivate.runOn($self));
			$ret.disactivate($self.lineDisactivate.runOn($self));
			$ret.destroy($self.lineDestroy.runOn($self));
			return $ret;
		}()));
	}
	,
	lineCreate: function () {
		var line = new $.ig.Path();
		line.renderTransform(new $.ig.TranslateTransform());
		this.visibleLines().add(line);
		return line;
	}
	,
	lineActivate: function (line) {
		line.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	lineDisactivate: function (line) {
		line.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	lineDestroy: function (line) {
		this.visibleLines().remove(line);
	}
	,
	setLineHeight: function (line, y, left, right) {
		if (line.data() == null || (line.data()).endPoint().__x != right || (line.data()).startPoint().__x != left) {
			var altGeo = (function () {
				var $ret = new $.ig.LineGeometry();
				$ret.startPoint({ __x: left, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.endPoint({ __x: right, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			line.data(altGeo);
		}
	}
	,
	getLightenedBrush: function (brush) {
		if (brush == null) {
			return brush;
		}
		return $.ig.BrushUtil.prototype.getLightened(brush, 0.1);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnnotationLayerView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender() && !isHitContext) {
			for (var i = 0; i < this.visibleLines().count(); i++) {
				var line = this.visibleLines().__inner[i];
				if (line.__visibility == $.ig.Visibility.prototype.visible) {
					context.renderPath(line);
				}
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnnotationLayerView.prototype.exportViewShapes.call(this, svd);
		for (var i = 0; i < this.visibleLines().count(); i++) {
			var lineData = new $.ig.PathVisualData(1, "crosshairLine", this.visibleLines().__inner[i]);
			lineData.tags().add("Main");
			lineData.tags().add("Crosshair");
			svd.shapes().add(lineData);
		}
	}
	,
	$type: new $.ig.Type('CrosshairLayerView', $.ig.AnnotationLayerView.prototype.$type)
}, true);

$.ig.CategoryItemHighlightType.prototype.auto = 0;
$.ig.CategoryItemHighlightType.prototype.marker = 1;
$.ig.CategoryItemHighlightType.prototype.shape = 2;

$.ig.PointerTooltipPointerLocation.prototype.auto = 0;
$.ig.PointerTooltipPointerLocation.prototype.topLeft = 1;
$.ig.PointerTooltipPointerLocation.prototype.topMiddle = 2;
$.ig.PointerTooltipPointerLocation.prototype.topRight = 3;
$.ig.PointerTooltipPointerLocation.prototype.rightTop = 4;
$.ig.PointerTooltipPointerLocation.prototype.rightMiddle = 5;
$.ig.PointerTooltipPointerLocation.prototype.rightBottom = 6;
$.ig.PointerTooltipPointerLocation.prototype.bottomRight = 7;
$.ig.PointerTooltipPointerLocation.prototype.bottomMiddle = 8;
$.ig.PointerTooltipPointerLocation.prototype.bottomLeft = 9;
$.ig.PointerTooltipPointerLocation.prototype.leftBottom = 10;
$.ig.PointerTooltipPointerLocation.prototype.leftMiddle = 11;
$.ig.PointerTooltipPointerLocation.prototype.leftTop = 12;

$.ig.CategoryTooltipLayerPosition.prototype.auto = 0;
$.ig.CategoryTooltipLayerPosition.prototype.outsideStart = 1;
$.ig.CategoryTooltipLayerPosition.prototype.insideStart = 2;
$.ig.CategoryTooltipLayerPosition.prototype.insideEnd = 3;
$.ig.CategoryTooltipLayerPosition.prototype.outsideEnd = 4;

$.ig.AnnotationLayer.prototype.useIndexPropertyName = "UseIndex";
$.ig.AnnotationLayer.prototype.useLegendPropertyName = "UseLegend";
$.ig.AnnotationLayer.prototype.cursorPositionPropertyName = "CursorPosition";
$.ig.AnnotationLayer.prototype.isDefaultCrosshairDisabledPropertyName = "IsDefaultCrosshairDisabled";
$.ig.AnnotationLayer.prototype.useIndexProperty = $.ig.DependencyProperty.prototype.register($.ig.AnnotationLayer.prototype.useIndexPropertyName, $.ig.Boolean.prototype.$type, $.ig.AnnotationLayer.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.AnnotationLayer.prototype.useIndexPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnnotationLayer.prototype.useLegendProperty = $.ig.DependencyProperty.prototype.register($.ig.AnnotationLayer.prototype.useLegendPropertyName, $.ig.Boolean.prototype.$type, $.ig.AnnotationLayer.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.AnnotationLayer.prototype.useLegendPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnnotationLayer.prototype.cursorPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.AnnotationLayer.prototype.cursorPositionPropertyName, $.ig.Point.prototype.$type, $.ig.AnnotationLayer.prototype.$type, new $.ig.PropertyMetadata(2, { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (sender, e) {
	(sender).raisePropertyChanged($.ig.AnnotationLayer.prototype.cursorPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.AnnotationLayer.prototype.isDefaultCrosshairDisabledProperty = $.ig.DependencyProperty.prototype.register($.ig.AnnotationLayer.prototype.isDefaultCrosshairDisabledPropertyName, $.ig.Boolean.prototype.$type, $.ig.AnnotationLayer.prototype.$type, new $.ig.PropertyMetadata(2, true, function (sender, e) {
	(sender).raisePropertyChanged($.ig.AnnotationLayer.prototype.isDefaultCrosshairDisabledPropertyName, e.oldValue(), e.newValue());
}));

$.ig.CategoryToolTipLayer.prototype.targetAxisPropertyName = "TargetAxis";
$.ig.CategoryToolTipLayer.prototype.useInterpolationPropertyName = "UseInterpolation";
$.ig.CategoryToolTipLayer.prototype.toolTipPositionPropertyName = "ToolTipPosition";
$.ig.CategoryToolTipLayer.prototype.targetAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryToolTipLayer.prototype.targetAxisPropertyName, $.ig.Axis.prototype.$type, $.ig.CategoryToolTipLayer.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryToolTipLayer.prototype.targetAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryToolTipLayer.prototype.useInterpolationProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryToolTipLayer.prototype.useInterpolationPropertyName, $.ig.Boolean.prototype.$type, $.ig.CategoryToolTipLayer.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryToolTipLayer.prototype.useInterpolationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryToolTipLayer.prototype.toolTipPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryToolTipLayer.prototype.toolTipPositionPropertyName, $.ig.CategoryTooltipLayerPosition.prototype.$type, $.ig.CategoryToolTipLayer.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.CategoryTooltipLayerPosition.prototype.getBox($.ig.CategoryTooltipLayerPosition.prototype.auto), function (sender, e) {
	($.ig.util.cast($.ig.CategoryToolTipLayer.prototype.$type, sender)).raisePropertyChanged($.ig.CategoryToolTipLayer.prototype.toolTipPositionPropertyName, e.oldValue(), e.newValue());
}));

$.ig.ItemToolTipLayer.prototype.targetSeriesPropertyName = "TargetSeries";
$.ig.ItemToolTipLayer.prototype.useInterpolationPropertyName = "UseInterpolation";
$.ig.ItemToolTipLayer.prototype.skipUnknownValuesPropertyName = "SkipUnknownValues";
$.ig.ItemToolTipLayer.prototype.targetSeriesProperty = $.ig.DependencyProperty.prototype.register($.ig.ItemToolTipLayer.prototype.targetSeriesPropertyName, $.ig.Series.prototype.$type, $.ig.ItemToolTipLayer.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ItemToolTipLayer.prototype.targetSeriesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ItemToolTipLayer.prototype.useInterpolationProperty = $.ig.DependencyProperty.prototype.register($.ig.ItemToolTipLayer.prototype.useInterpolationPropertyName, $.ig.Boolean.prototype.$type, $.ig.ItemToolTipLayer.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ItemToolTipLayer.prototype.useInterpolationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ItemToolTipLayer.prototype.skipUnknownValuesProperty = $.ig.DependencyProperty.prototype.register($.ig.ItemToolTipLayer.prototype.skipUnknownValuesPropertyName, $.ig.Boolean.prototype.$type, $.ig.ItemToolTipLayer.prototype.$type, new $.ig.PropertyMetadata(2, true, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ItemToolTipLayer.prototype.skipUnknownValuesPropertyName, e.oldValue(), e.newValue());
}));

$.ig.PointerTooltip.prototype.pointerPositionPropertyName = "PointerPosition";
$.ig.PointerTooltip.prototype.pointerInsetPropertyName = "PointerInset";
$.ig.PointerTooltip.prototype.pointerWidthPropertyName = "PointerWidth";
$.ig.PointerTooltip.prototype.pointerVisibilityPropertyName = "PointerVisibility";
$.ig.PointerTooltip.prototype.pointerLocationPropertyName = "PointerLocation";
$.ig.PointerTooltip.prototype.actualPointerStartPositionPropertyName = "ActualPointerStartPosition";
$.ig.PointerTooltip.prototype.actualPointerFirstPositionPropertyName = "ActualPointerFirstPosition";
$.ig.PointerTooltip.prototype.actualPointerSecondPositionPropertyName = "ActualPointerSecondPosition";
$.ig.PointerTooltip.prototype.actualBoxTopLeftPositionPropertyName = "ActualBoxTopLeftPosition";
$.ig.PointerTooltip.prototype.actualBoxTopRightPositionPropertyName = "ActualBoxTopRightPosition";
$.ig.PointerTooltip.prototype.actualBoxBottomLeftPositionPropertyName = "ActualBoxBottomLeftPosition";
$.ig.PointerTooltip.prototype.actualBoxBottomRightPositionPropertyName = "ActualBoxBottomRightPosition";
$.ig.PointerTooltip.prototype.actualBoxWidthPropertyName = "ActualBoxWidth";
$.ig.PointerTooltip.prototype.actualBoxHeightPropertyName = "ActualBoxHeight";
$.ig.PointerTooltip.prototype.actualBoxFullHeightPropertyName = "ActualBoxFullHeight";
$.ig.PointerTooltip.prototype.actualBoxFullWidthPropertyName = "ActualBoxFullWidth";
$.ig.PointerTooltip.prototype.pointerPointsPropertyName = "PointerPoints";
$.ig.PointerTooltip.prototype.pointerOutlinePointsPropertyName = "PointerOutlinePoints";
$.ig.PointerTooltip.prototype.pointerPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.pointerPositionPropertyName, $.ig.Point.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.pointerPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.pointerInsetProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.pointerInsetPropertyName, Number, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, 7.5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.pointerInsetPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.pointerWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.pointerWidthPropertyName, Number, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, 5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.pointerWidthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.pointerVisibilityProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.pointerVisibilityPropertyName, $.ig.Visibility.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.Visibility.prototype.getBox($.ig.Visibility.prototype.visible), function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.pointerVisibilityPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.pointerLocationProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.pointerLocationPropertyName, $.ig.PointerTooltipPointerLocation.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.PointerTooltipPointerLocation.prototype.getBox($.ig.PointerTooltipPointerLocation.prototype.auto), function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.pointerLocationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualPointerStartPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualPointerStartPositionPropertyName, $.ig.Point.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualPointerStartPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualPointerFirstPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualPointerFirstPositionPropertyName, $.ig.Point.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualPointerFirstPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualPointerSecondPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualPointerSecondPositionPropertyName, $.ig.Point.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualPointerSecondPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualBoxTopLeftPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualBoxTopLeftPositionPropertyName, $.ig.Point.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualBoxTopLeftPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualBoxTopRightPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualBoxTopRightPositionPropertyName, $.ig.Point.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualBoxTopRightPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualBoxBottomLeftPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualBoxBottomLeftPositionPropertyName, $.ig.Point.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualBoxBottomLeftPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualBoxBottomRightPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualBoxBottomRightPositionPropertyName, $.ig.Point.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualBoxBottomRightPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualBoxWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualBoxWidthPropertyName, Number, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualBoxWidthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualBoxHeightProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualBoxHeightPropertyName, Number, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualBoxHeightPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualBoxFullHeightProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualBoxFullHeightPropertyName, Number, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualBoxFullHeightPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.actualBoxFullWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.actualBoxFullWidthPropertyName, Number, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.actualBoxFullWidthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.pointerPointsProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.pointerPointsPropertyName, $.ig.PointCollection.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.pointerPointsPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PointerTooltip.prototype.pointerOutlinePointsProperty = $.ig.DependencyProperty.prototype.register($.ig.PointerTooltip.prototype.pointerOutlinePointsPropertyName, $.ig.PointCollection.prototype.$type, $.ig.PointerTooltip.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PointerTooltip.prototype.pointerOutlinePointsPropertyName, e.oldValue(), e.newValue());
}));

$.ig.CategoryHighlightLayer.prototype.targetAxisPropertyName = "TargetAxis";
$.ig.CategoryHighlightLayer.prototype.useInterpolationPropertyName = "UseInterpolation";
$.ig.CategoryHighlightLayer.prototype.bandHighlightWidthPropertyName = "BAndHighlightWidth";
$.ig.CategoryHighlightLayer.prototype.targetAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryHighlightLayer.prototype.targetAxisPropertyName, $.ig.CategoryAxisBase.prototype.$type, $.ig.CategoryHighlightLayer.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryHighlightLayer.prototype.targetAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryHighlightLayer.prototype.useInterpolationProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryHighlightLayer.prototype.useInterpolationPropertyName, $.ig.Boolean.prototype.$type, $.ig.CategoryHighlightLayer.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryHighlightLayer.prototype.useInterpolationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryHighlightLayer.prototype.bandHighlightWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryHighlightLayer.prototype.bandHighlightWidthPropertyName, Number, $.ig.CategoryHighlightLayer.prototype.$type, new $.ig.PropertyMetadata(2, 10, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryHighlightLayer.prototype.bandHighlightWidthPropertyName, e.oldValue(), e.newValue());
}));

$.ig.CategoryItemHighlightLayer.prototype.targetSeriesPropertyName = "TargetSeries";
$.ig.CategoryItemHighlightLayer.prototype.useInterpolationPropertyName = "UseInterpolation";
$.ig.CategoryItemHighlightLayer.prototype.highlightTypePropertyName = "HighlightType";
$.ig.CategoryItemHighlightLayer.prototype.markerTemplatePropertyName = "MarkerTemplate";
$.ig.CategoryItemHighlightLayer.prototype.bandHighlightWidthPropertyName = "BandHighlightWidth";
$.ig.CategoryItemHighlightLayer.prototype.skipUnknownValuesPropertyName = "SkipUnknownValues";
$.ig.CategoryItemHighlightLayer.prototype.targetSeriesProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryItemHighlightLayer.prototype.targetSeriesPropertyName, $.ig.Series.prototype.$type, $.ig.CategoryItemHighlightLayer.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryItemHighlightLayer.prototype.targetSeriesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryItemHighlightLayer.prototype.useInterpolationProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryItemHighlightLayer.prototype.useInterpolationPropertyName, $.ig.Boolean.prototype.$type, $.ig.CategoryItemHighlightLayer.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryItemHighlightLayer.prototype.useInterpolationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryItemHighlightLayer.prototype.highlightTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryItemHighlightLayer.prototype.highlightTypePropertyName, $.ig.CategoryItemHighlightType.prototype.$type, $.ig.CategoryItemHighlightLayer.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.CategoryItemHighlightType.prototype.getBox($.ig.CategoryItemHighlightType.prototype.auto), function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryItemHighlightLayer.prototype.highlightTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryItemHighlightLayer.prototype.markerTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryItemHighlightLayer.prototype.markerTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.CategoryItemHighlightLayer.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryItemHighlightLayer.prototype.markerTemplatePropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryItemHighlightLayer.prototype.bandHighlightWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryItemHighlightLayer.prototype.bandHighlightWidthPropertyName, Number, $.ig.CategoryItemHighlightLayer.prototype.$type, new $.ig.PropertyMetadata(2, 10, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryItemHighlightLayer.prototype.bandHighlightWidthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryItemHighlightLayer.prototype.skipUnknownValuesProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryItemHighlightLayer.prototype.skipUnknownValuesPropertyName, $.ig.Boolean.prototype.$type, $.ig.CategoryItemHighlightLayer.prototype.$type, new $.ig.PropertyMetadata(2, true, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryItemHighlightLayer.prototype.skipUnknownValuesPropertyName, e.oldValue(), e.newValue());
}));

$.ig.CrosshairLayer.prototype.targetSeriesPropertyName = "TargetSeries";
$.ig.CrosshairLayer.prototype.useInterpolationPropertyName = "UseInterpolation";
$.ig.CrosshairLayer.prototype.verticalLineVisibilityPropertyName = "VerticalLineVisibility";
$.ig.CrosshairLayer.prototype.horizontalLineVisibilityPropertyName = "HorizontalLineVisibility";
$.ig.CrosshairLayer.prototype.skipUnknownValuesPropertyName = "SkipUnknownValues";
$.ig.CrosshairLayer.prototype.targetSeriesProperty = $.ig.DependencyProperty.prototype.register($.ig.CrosshairLayer.prototype.targetSeriesPropertyName, $.ig.Series.prototype.$type, $.ig.CrosshairLayer.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CrosshairLayer.prototype.targetSeriesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CrosshairLayer.prototype.useInterpolationProperty = $.ig.DependencyProperty.prototype.register($.ig.CrosshairLayer.prototype.useInterpolationPropertyName, $.ig.Boolean.prototype.$type, $.ig.CrosshairLayer.prototype.$type, new $.ig.PropertyMetadata(2, true, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CrosshairLayer.prototype.useInterpolationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CrosshairLayer.prototype.verticalLineVisibilityProperty = $.ig.DependencyProperty.prototype.register($.ig.CrosshairLayer.prototype.verticalLineVisibilityPropertyName, $.ig.Visibility.prototype.$type, $.ig.CrosshairLayer.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.Visibility.prototype.getBox($.ig.Visibility.prototype.visible), function (sender, e) {
	(sender).raisePropertyChanged($.ig.CrosshairLayer.prototype.verticalLineVisibilityPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CrosshairLayer.prototype.horizontalLineVisibilityProperty = $.ig.DependencyProperty.prototype.register($.ig.CrosshairLayer.prototype.horizontalLineVisibilityPropertyName, $.ig.Visibility.prototype.$type, $.ig.CrosshairLayer.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.Visibility.prototype.getBox($.ig.Visibility.prototype.visible), function (sender, e) {
	(sender).raisePropertyChanged($.ig.CrosshairLayer.prototype.horizontalLineVisibilityPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CrosshairLayer.prototype.skipUnknownValuesProperty = $.ig.DependencyProperty.prototype.register($.ig.CrosshairLayer.prototype.skipUnknownValuesPropertyName, $.ig.Boolean.prototype.$type, $.ig.CrosshairLayer.prototype.$type, new $.ig.PropertyMetadata(2, true, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CrosshairLayer.prototype.skipUnknownValuesPropertyName, e.oldValue(), e.newValue());
}));

} (jQuery));


