// Default options

var options = function() { };
var lineOptions = function() { };
var barOptions = function() { };

labelFormatter = function(label, series) {
    if (series.id == 0)
        return label;
    return label + ' <a href="' + series.id + '">x</a>';
}

$.extend(options.prototype, {

    init: function() {
        this.legend = {
            show: true,
            position: "sw",
            labelFormatter: labelFormatter,
            backgroundOpacity: 0.5
        };

        this.x2axis = {
            ticks: 9,
            mode: "time"
        };

        this.yaxis = {
            ticks: 6,
            min: 0,
            max: 80
        };

        this.grid = {
            borderWidth: 1,
            borderColor: "#d8d8d8",
            labelMargin: 8,
            color: "#444",
            backgroundColor: "#fafafa",
            borderColor: "#fff",
            tickColor: "#ddd",
            borderWidth: 0,
            hoverable: true,
            autoHighlight: true,
            mouseActiveRadius: 50
        };

        this.xaxis = {
            ticks: 9,
            mode: "time"
        };

        this._init();
    },

    // Option changers
    setSelectionMode: function() {
        this.selection = {
            mode: "x"
        };

        this.pan = {
            interactive: false
        };
    },

    setPanningMode: function() {
        this.selection = {};

        this.pan = {
            interactive: true
        };
    },

    setXRange: function(min, max) {
        this.xaxis.min = min;
        this.xaxis.max = max;
    },

    setYRange: function(min, max) {
        this.yaxis.min = min;
        this.yaxis.max = max;
    }

});

$.extend(lineOptions.prototype, {

    _init: function() {

        this.series = {
            lines: {
                show: true,
                fill: false,
                lineWidth: 1
            },
            points: { show: false }
        };

        this.zoom = {
            interactive: true
        };

        this.pan = {
            interactive: true
        };

    }
});

$.extend(barOptions.prototype, {

    _init: function() {

        this.legend = {
            show: false
        };

        this.x2axis = {
            show: false
        };

        this.xaxis = {
            show: false,
            ticks: []
        };

        this.yaxis = {
            show: false
        };

        this.y2axis = {
            show: false
        };

    }
});

var LineOptions = function() {
    this.init();
}

var BarOptions = function() {
    this.init();
}

$.extend(true, LineOptions.prototype, options.prototype, lineOptions.prototype);
$.extend(true, BarOptions.prototype, options.prototype, barOptions.prototype);