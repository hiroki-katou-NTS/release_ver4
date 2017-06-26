/*!@license
* Infragistics.Web.ClientUI infragistics.chart_stackedchart.js 16.1.20161.2145
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
"MulticastDelegate:bg", 
"IntPtr:bh", 
"Array:c4", 
"Func$1:ib", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


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
"Stacked100BarBucketCalculator:acq", 
"Stacked100ColumnBucketCalculator:acr", 
"Stacked100BarSeries:acs", 
"Stacked100BarSeriesView:act", 
"Stacked100AreaSeries:acu", 
"Stacked100ColumnSeries:acv", 
"Stacked100ColumnSeriesView:acw", 
"Stacked100SplineAreaSeries:acx", 
"Stacked100SplineSeries:acy", 
"Stacked100LineSeries:acz", 
"Legend:ac1", 
"LegendView:ac2", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('Stacked100BarBucketCalculator', 'StackedBarBucketCalculator', {
	init: function (view) {
		$.ig.StackedBarBucketCalculator.prototype.init.call(this, view);
	},
	getBucket: function (index) {
		return $.ig.StackedBarBucketCalculator.prototype.getBucket.call(this, index);
	}
	,
	getBucket1: function (series, index, sortingIndex, windowRect, viewportRect, currentFrame) {
		var barSeries = $.ig.util.cast($.ig.StackedBarSeries.prototype.$type, this.view().categoryModel());
		var bucket = [ NaN, NaN, NaN ];
		var fragment = $.ig.util.cast($.ig.BarFragment.prototype.$type, series);
		if (fragment == null || fragment.logicalSeriesLink() == null) {
			return bucket;
		}
		var value = series.valueColumn().item(sortingIndex);
		var total = 0;
		var zero = 0;
		var min = NaN;
		var max = NaN;
		var high = Number.NEGATIVE_INFINITY;
		var low = Number.POSITIVE_INFINITY;
		var effectiveViewportRect = barSeries.getEffectiveViewport1(this.view());
		var count = Math.min(barSeries.lows() != null ? barSeries.lows().length : 0, barSeries.highs() != null ? barSeries.highs().length : 0);
		var i0 = sortingIndex * this._bucketSize;
		var i1 = Math.min(i0 + this._bucketSize - 1, count - 1);
		for (var i = i0; i <= i1; ++i) {
			value = series.valueColumn().item(i);
			total = Math.abs(barSeries.lows()[i]) + barSeries.highs()[i];
			if (value < zero) {
				low = Math.min(low, (fragment.logicalSeriesLink().lowValues().__inner[i] + value) / total * 100);
				high = Math.max(high, fragment.logicalSeriesLink().lowValues().__inner[i] / total * 100);
			} else {
				low = Math.min(low, fragment.logicalSeriesLink().highValues().__inner[i] / total * 100);
				high = Math.max(high, (fragment.logicalSeriesLink().highValues().__inner[i] + value) / total * 100);
			}
			if (!$.ig.util.isNaN(min)) {
				if (!$.ig.util.isNaN(low)) {
					min = Math.min(min, low);
					max = Math.max(max, low);
				}
				if (!$.ig.util.isNaN(high)) {
					min = Math.min(min, high);
					max = Math.max(max, high);
				}
			} else {
				min = low;
				max = high;
			}
		}
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, barSeries.xAxis().isInverted(), effectiveViewportRect);
		bucket = [ currentFrame._buckets.__inner[index - this._firstBucket][0], barSeries.xAxis().getScaledValue(max, xParams), barSeries.xAxis().getScaledValue(min, xParams) ];
		return bucket;
	}
	,
	$type: new $.ig.Type('Stacked100BarBucketCalculator', $.ig.StackedBarBucketCalculator.prototype.$type)
}, true);

$.ig.util.defType('Stacked100ColumnBucketCalculator', 'StackedColumnBucketCalculator', {
	init: function (view) {
		$.ig.StackedColumnBucketCalculator.prototype.init.call(this, view);
	},
	getBucket: function (index) {
		return $.ig.StackedColumnBucketCalculator.prototype.getBucket.call(this, index);
	}
	,
	getBucket1: function (series, index, sortingIndex, windowRect, viewportRect, currentFrame) {
		var stackedSeries = $.ig.util.cast($.ig.StackedSeriesBase.prototype.$type, this.view().categoryModel());
		var bucket = [ NaN, NaN, NaN ];
		var fragment = $.ig.util.cast($.ig.ColumnFragment.prototype.$type, series);
		if (fragment == null || fragment.logicalSeriesLink() == null) {
			return bucket;
		}
		var effectiveViewportRect = stackedSeries.getEffectiveViewport1(this.view());
		var value = series.valueColumn().item(sortingIndex);
		var zero = 0;
		var min = NaN;
		var max = NaN;
		var high = Number.NEGATIVE_INFINITY;
		var low = Number.POSITIVE_INFINITY;
		var total = 0;
		var count = Math.min(stackedSeries.lows() != null ? stackedSeries.lows().length : 0, stackedSeries.highs() != null ? stackedSeries.highs().length : 0);
		var i0 = sortingIndex * this._bucketSize;
		var i1 = Math.min(i0 + this._bucketSize - 1, count - 1);
		for (var i = i0; i <= i1; ++i) {
			value = series.valueColumn().item(i);
			total = Math.abs(stackedSeries.lows()[i]) + stackedSeries.highs()[i];
			if (value < zero) {
				low = Math.min(low, (fragment.logicalSeriesLink().lowValues().__inner[i] + value) / total * 100);
				high = Math.max(high, fragment.logicalSeriesLink().lowValues().__inner[i] / total * 100);
			} else {
				low = Math.min(low, fragment.logicalSeriesLink().highValues().__inner[i] / total * 100);
				high = Math.max(high, (fragment.logicalSeriesLink().highValues().__inner[i] + value) / total * 100);
			}
			if (!$.ig.util.isNaN(min)) {
				if (!$.ig.util.isNaN(low)) {
					min = Math.min(min, low);
					max = Math.max(max, low);
				}
				if (!$.ig.util.isNaN(high)) {
					min = Math.min(min, high);
					max = Math.max(max, high);
				}
			} else {
				min = low;
				max = high;
			}
		}
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, fragment.yAxis().isInverted(), effectiveViewportRect);
		bucket = [ currentFrame._buckets.__inner[index - this._firstBucket][0], fragment.yAxis().getScaledValue(max, yParams), fragment.yAxis().getScaledValue(min, yParams) ];
		return bucket;
	}
	,
	$type: new $.ig.Type('Stacked100ColumnBucketCalculator', $.ig.StackedColumnBucketCalculator.prototype.$type)
}, true);

$.ig.util.defType('Stacked100BarSeries', 'StackedBarSeries', {
	init: function () {
		$.ig.StackedBarSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.Stacked100BarSeries.prototype.$type);
	},
	onApplyTemplate: function () {
		$.ig.StackedBarSeries.prototype.onApplyTemplate.call(this);
		this.renderSeries(false);
	}
	,
	createView: function () {
		return new $.ig.Stacked100BarSeriesView(this);
	}
	,
	_stacked100BarView: null,
	stacked100BarView: function (value) {
		if (arguments.length === 1) {
			this._stacked100BarView = value;
			return value;
		} else {
			return this._stacked100BarView;
		}
	}
	,
	onViewCreated: function (view) {
		$.ig.StackedBarSeries.prototype.onViewCreated.call(this, view);
		this.stacked100BarView(view);
	}
	,
	getSeriesView: function () {
		return this.stacked100BarView();
	}
	,
	prepareData: function () {
		$.ig.StackedBarSeries.prototype.prepareData.call(this);
		if (this.fastItemsSource() == null) {
			return;
		}
		var min = Number.POSITIVE_INFINITY;
		var max = Number.NEGATIVE_INFINITY;
		for (var i = 0; i < this.fastItemsSource().count(); i++) {
			var total = Math.abs(this.lows()[i]) + this.highs()[i];
			if (total == 0) {
				min = Math.min(min, 0);
				max = Math.max(max, 0);
				continue;
			}
			min = Math.min(min, this.lows()[i] / total * 100);
			max = Math.max(max, this.highs()[i] / total * 100);
		}
		this.minimum(min);
		this.maximum(max);
	}
	,
	$type: new $.ig.Type('Stacked100BarSeries', $.ig.StackedBarSeries.prototype.$type, [$.ig.IStacked100Series.prototype.$type])
}, true);

$.ig.util.defType('Stacked100AreaSeries', 'StackedAreaSeries', {
	init: function () {
		$.ig.StackedAreaSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.Stacked100AreaSeries.prototype.$type);
	},
	onApplyTemplate: function () {
		$.ig.StackedAreaSeries.prototype.onApplyTemplate.call(this);
		this.renderSeries(false);
	}
	,
	prepareData: function () {
		$.ig.StackedAreaSeries.prototype.prepareData.call(this);
		if (this.fastItemsSource() == null) {
			return;
		}
		var min = Number.POSITIVE_INFINITY;
		var max = Number.NEGATIVE_INFINITY;
		for (var i = 0; i < this.fastItemsSource().count(); i++) {
			var total = Math.abs(this.lows()[i]) + this.highs()[i];
			if (total == 0) {
				min = Math.min(min, 0);
				max = Math.max(max, 0);
				continue;
			}
			min = Math.min(min, this.lows()[i] / total * 100);
			max = Math.max(max, this.highs()[i] / total * 100);
		}
		this.minimum(min);
		this.maximum(max);
	}
	,
	$type: new $.ig.Type('Stacked100AreaSeries', $.ig.StackedAreaSeries.prototype.$type, [$.ig.IStacked100Series.prototype.$type])
}, true);

$.ig.util.defType('Stacked100ColumnSeries', 'StackedColumnSeries', {
	init: function () {
		$.ig.StackedColumnSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.Stacked100ColumnSeries.prototype.$type);
	},
	onApplyTemplate: function () {
		$.ig.StackedColumnSeries.prototype.onApplyTemplate.call(this);
		this.renderSeries(false);
	}
	,
	createView: function () {
		return new $.ig.Stacked100ColumnSeriesView(this);
	}
	,
	_stacked100ColumnView: null,
	stacked100ColumnView: function (value) {
		if (arguments.length === 1) {
			this._stacked100ColumnView = value;
			return value;
		} else {
			return this._stacked100ColumnView;
		}
	}
	,
	onViewCreated: function (view) {
		$.ig.StackedColumnSeries.prototype.onViewCreated.call(this, view);
		this.stacked100ColumnView(view);
	}
	,
	getSeriesView: function () {
		return this.stacked100ColumnView();
	}
	,
	prepareData: function () {
		$.ig.StackedColumnSeries.prototype.prepareData.call(this);
		if (this.fastItemsSource() == null) {
			return;
		}
		var min = Number.POSITIVE_INFINITY;
		var max = Number.NEGATIVE_INFINITY;
		for (var i = 0; i < this.fastItemsSource().count(); i++) {
			var total = Math.abs(this.lows()[i]) + this.highs()[i];
			if (total == 0) {
				min = Math.min(min, 0);
				max = Math.max(max, 0);
				continue;
			}
			min = Math.min(min, this.lows()[i] / total * 100);
			max = Math.max(max, this.highs()[i] / total * 100);
		}
		this.minimum(min);
		this.maximum(max);
	}
	,
	$type: new $.ig.Type('Stacked100ColumnSeries', $.ig.StackedColumnSeries.prototype.$type, [$.ig.IStacked100Series.prototype.$type])
}, true);

$.ig.util.defType('Stacked100SplineAreaSeries', 'StackedSplineAreaSeries', {
	init: function () {
		$.ig.StackedSplineAreaSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.Stacked100SplineAreaSeries.prototype.$type);
	},
	onApplyTemplate: function () {
		$.ig.StackedSplineAreaSeries.prototype.onApplyTemplate.call(this);
		this.renderSeries(false);
	}
	,
	prepareData: function () {
		$.ig.StackedSplineAreaSeries.prototype.prepareData.call(this);
		if (this.fastItemsSource() == null) {
			return;
		}
		var min = Number.POSITIVE_INFINITY;
		var max = Number.NEGATIVE_INFINITY;
		for (var i = 0; i < this.fastItemsSource().count(); i++) {
			var total = Math.abs(this.lows()[i]) + this.highs()[i];
			if (total == 0) {
				min = Math.min(min, 0);
				max = Math.max(max, 0);
				continue;
			}
			min = Math.min(min, this.lows()[i] / total * 100);
			max = Math.max(max, this.highs()[i] / total * 100);
		}
		this.minimum(min);
		this.maximum(max);
	}
	,
	$type: new $.ig.Type('Stacked100SplineAreaSeries', $.ig.StackedSplineAreaSeries.prototype.$type, [$.ig.IStacked100Series.prototype.$type])
}, true);

$.ig.util.defType('Stacked100SplineSeries', 'StackedSplineSeries', {
	init: function () {
		$.ig.StackedSplineSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.Stacked100SplineSeries.prototype.$type);
	},
	onApplyTemplate: function () {
		$.ig.StackedSplineSeries.prototype.onApplyTemplate.call(this);
		this.renderSeries(false);
	}
	,
	prepareData: function () {
		$.ig.StackedSplineSeries.prototype.prepareData.call(this);
		if (this.fastItemsSource() == null) {
			return;
		}
		var min = Number.POSITIVE_INFINITY;
		var max = Number.NEGATIVE_INFINITY;
		for (var i = 0; i < this.fastItemsSource().count(); i++) {
			var total = Math.abs(this.lows()[i]) + this.highs()[i];
			if (total == 0) {
				min = Math.min(min, 0);
				max = Math.max(max, 0);
				continue;
			}
			min = Math.min(min, this.lows()[i] / total * 100);
			max = Math.max(max, this.highs()[i] / total * 100);
		}
		this.minimum(min);
		this.maximum(max);
	}
	,
	$type: new $.ig.Type('Stacked100SplineSeries', $.ig.StackedSplineSeries.prototype.$type, [$.ig.IStacked100Series.prototype.$type])
}, true);

$.ig.util.defType('Stacked100LineSeries', 'StackedLineSeries', {
	init: function () {
		$.ig.StackedLineSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.Stacked100LineSeries.prototype.$type);
	},
	onApplyTemplate: function () {
		$.ig.StackedLineSeries.prototype.onApplyTemplate.call(this);
		this.renderSeries(false);
	}
	,
	prepareData: function () {
		$.ig.StackedLineSeries.prototype.prepareData.call(this);
		if (this.fastItemsSource() == null) {
			return;
		}
		var min = Number.POSITIVE_INFINITY;
		var max = Number.NEGATIVE_INFINITY;
		for (var i = 0; i < this.fastItemsSource().count(); i++) {
			var total = Math.abs(this.lows()[i]) + this.highs()[i];
			if (total == 0) {
				min = Math.min(min, 0);
				max = Math.max(max, 0);
				continue;
			}
			min = Math.min(min, this.lows()[i] / total * 100);
			max = Math.max(max, this.highs()[i] / total * 100);
		}
		this.minimum(min);
		this.maximum(max);
	}
	,
	$type: new $.ig.Type('Stacked100LineSeries', $.ig.StackedLineSeries.prototype.$type, [$.ig.IStacked100Series.prototype.$type])
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

$.ig.util.defType('Stacked100BarSeriesView', 'StackedBarSeriesView', {
	_stacked100BarModel: null,
	stacked100BarModel: function (value) {
		if (arguments.length === 1) {
			this._stacked100BarModel = value;
			return value;
		} else {
			return this._stacked100BarModel;
		}
	}
	,
	init: function (model) {
		$.ig.StackedBarSeriesView.prototype.init.call(this, model);
		this.stacked100BarModel(model);
	},
	createBucketCalculator: function () {
		return new $.ig.Stacked100BarBucketCalculator(this);
	}
	,
	$type: new $.ig.Type('Stacked100BarSeriesView', $.ig.StackedBarSeriesView.prototype.$type)
}, true);

$.ig.util.defType('Stacked100ColumnSeriesView', 'StackedColumnSeriesView', {
	_stacked100ColumnModel: null,
	stacked100ColumnModel: function (value) {
		if (arguments.length === 1) {
			this._stacked100ColumnModel = value;
			return value;
		} else {
			return this._stacked100ColumnModel;
		}
	}
	,
	init: function (model) {
		$.ig.StackedColumnSeriesView.prototype.init.call(this, model);
		this.stacked100ColumnModel(model);
	},
	createBucketCalculator: function () {
		return new $.ig.Stacked100ColumnBucketCalculator(this);
	}
	,
	$type: new $.ig.Type('Stacked100ColumnSeriesView', $.ig.StackedColumnSeriesView.prototype.$type)
}, true);

$.ig.Legend.prototype.orientationPropertyName = "Orientation";

} (jQuery));


