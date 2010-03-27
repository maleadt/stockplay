// Default options

var Options = function() {
	this.init();
}

$.extend(Options.prototype, {

	// object variables
	legend: {
		show: true,
		position: "sw"
	},
	
	series: {
		lines: {
			show: true,
			fill: true,
			lineWidth: 2
		},
		points: { show: false }
	},
	
	xaxis: {
		ticks: 9,
		mode: "time"
	},
	
	x2axis: {
		ticks: 9,
		mode: "time"
	},
	
	yaxis: {
		ticks: 6,
		min: 0,
		max: 80
	},
	
	//selection: { mode: "x" },
	
	pan: {
		interactive: true
	},
	
	zoom: {
		interactive: true
	},
	
	grid: {
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
	},

	init: function() {
	},
	
    // Option changers
	addSelection: function() {
		this.selection = {
			mode: "x"
		};
	},

	addPanning: function() {
		this.pan.interactive = true;
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