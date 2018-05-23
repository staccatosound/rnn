sl = { series: {}};

sl.series.ohlc = function () {

var xScale = d3.scaleTime(),
    yScale = d3.scaleLinear();

var ohlc = function (selection) {
    selection.each(function (data) {
        // Generate ohlc bars here.
    });
};

ohlc.xScale = function (value) {
    if (!arguments.length) {
        return xScale;
    }
    xScale = value;
    return ohlc;
};

ohlc.yScale = function (value) {
    // Similar to xScale above.
};

return ohlc;
};


// Create series and bind x and y scales
var series = sl.series.ohlc()
            .xScale(xScale)
            .yScale(yScale);

// Bind data to a selection and call the series.
d3.select('.series')
    .datum(data)
    .call(series);

    console.log("here");


var ohlc = function (selection) {
    selection.each(function (data) {
        // Generate ohlc bars here.
        series = d3.select(this).selectAll('.ohlc-series').data([data]);
        series.enter().append('g').classed('ohlc-series', true);
        //... 
    });
};    

//... 
bars = series.selectAll('.bar')
    .data(data, function (d) {
        return d.date;
    });

bars.enter()
    .append('g')
    .classed('bar', true);

bars.classed({
    'up-day': function(d) {
        return d.close > d.open;
        },
    'down-day': function (d) {
        return d.close <= d.open;
        }
    });

bars.exit().remove();

var line = d3.svg.line()
            .x(function (d) { return d.x; })
            .y(function (d) { return d.y; });

var highLowLines = function (bars) {

var paths = bars
    .selectAll('.high-low-line')
    .data(function (d) {
        return [d];
    });

    paths.enter().append('path');

    paths.classed('high-low-line', true)
    .attr('d', function (d) {
        return line([
            { x: xScale(d.date), y: yScale(d.high) },
            { x: xScale(d.date), y: yScale(d.low) }
        ]);
    });
};

var openCloseTicks = function (bars) {
    var open,
        close,
        tickWidth = 5;

    open = bars.selectAll('.open-tick').data(function (d) {
        return [d];
    });

    close = bars.selectAll('.close-tick').data(function (d) {
        return [d];
    });

    open.enter().append('path');
    close.enter().append('path');

    open.classed('open-tick', true)
        .attr('d', function (d) {
            return line([
                { x: xScale(d.date) - tickWidth, y: yScale(d.open) },
                { x: xScale(d.date), y: yScale(d.open) }
            ]);
        });

    close.classed('close-tick', true)
        .attr('d', function (d) {
            return line([
                { x: xScale(d.date), y: yScale(d.close) },
                { x: xScale(d.date) + tickWidth, y: yScale(d.close) }
            ]);
        });

};  
