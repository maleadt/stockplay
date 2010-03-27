$(function (){

    function getMax(from, to) {
    	var max = null;
 		for(i=0;i<startData[0]['data'].length;i++)
        	if (startData[0]['data'][i][0] <= to && startData[0]['data'][i][0] >= from) {
	        	if (max == null)
	        		max = startData[0]['data'][i][1];
	        	if (startData[0]['data'][i][1] > max )
    	    		max =  startData[0]['data'][i][1];
    	    }
    	return max;
    }

    function getMin(from, to) {
    	var min = null;
 		for(i=0;i<startData[0]['data'].length;i++)
        	if (startData[0]['data'][i][0] <= to && startData[0]['data'][i][0] >= from) {
	        	if (min == null)
	        		min = startData[0]['data'][i][1];
	        	if (startData[0]['data'][i][1] < min )
	        		min =  startData[0]['data'][i][1];
	       	}
    	return min;
    }
    
    var options = {
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
  		}
    };


    // grafiek opstellen
    function getData(x1, x2) {
    	//alert("data opvragen");
        var d = [];
        for (var i = 0; i <= 250; ++i) {
            var x = x1 + i * (x2 - x1) / 500;
            d.push([x*10000000, (Math.sin(x+4)+2)*25]);
        }
        
        return [
            { label: "Beursverloop Tomtom", data: d, color: "lightblue" }
        ];
    }

    var startData = getData(2, 11);

	//alert(getMax(20000000, 20000000+10000000));
	//alert(getMax());
	

    $.plot($("#plotTest div"), startData, options);

    var geschiedenis = new Array();
    var vorige;
    
    function selectie() {
	    $.plot($("#plotTest div"), startData, addSelection(options) );
	};    

    function addSelection(options) {
	    return $.extend(true, {}, options, {
			selection: { mode: "x" }
        });
	};    

    function addPanning(options) {
	    return $.extend(true, {}, options, {
			pan: { interactive: true }
        });
	};    

    function setXRange(options, min, max) {
	    return $.extend(true, {}, options, {
			xaxis: { min: min, max: max }
        });
	};    

    function setYRange(options, min, max) {
	    return $.extend(true, {}, options, {
			yaxis: { min: min, max: max }
        });
	};    

	$("#plotTest div").bind('plotpan', function (event, plot) {
        
        var axes = plot.getAxes();

        return;

        $.plot($("#plotTest div"), getData(axes.xaxis.min/10000000, axes.xaxis.max*2/10000000),
                      $.extend(true, {}, options, {
                          xaxis: { min: axes.xaxis.min, max: axes.xaxis.max }
        }));        
        
        return;
        
        var axes = plot.getAxes();
        // zoom data ophalen
        
        //alert(axes.xaxis.min);
        
        $.plot($("#plotTest div"), getData(axes.xaxis.min/10000000, axes.xaxis.max*2/10000000), options,
                      $.extend(true, {}, options, {
                          //xaxis: { min: axes.xaxis.min, max: axes.xaxis.max }
        }));
                      
    });
    
    $("#plotTest div").bind("plotselected", function (event, ranges) {
    
        // limiet instellen
        if (ranges.xaxis.to - ranges.xaxis.from < 10000)
            ranges.xaxis.to = ranges.xaxis.from + 10000;
        if (ranges.yaxis.to - ranges.yaxis.from < 10000)
            ranges.yaxis.to = ranges.yaxis.from + 10000;
  
  
          // zoom data ophalen
        $.plot($("#plotTest div"), startData, addPanning(  setXRange( options, ranges.xaxis.from,  ranges.xaxis.to   )      ) );

        
        // zoom data ophalen
//        $.plot($("#plotTest div"), getData(ranges.xaxis.from/10000000, ranges.xaxis.to*2/10000000),
//                      $.extend(true, {}, options, {
//                          xaxis: { min: ranges.xaxis.from, max: ranges.xaxis.to }
//                      }));
                      
        // geschiedenis
       	geschiedenis.push(vorige);
        vorige = [ranges.xaxis.from, ranges.xaxis.to];
        
    });
    
    $("#plotTest div").bind('plotzoom', function (event, plot) {
    

        var ranges = plot.getAxes();

//        console.log(ranges.xaxis.max);

		//console.log(getMin(ranges.xaxis.min, ranges.xaxis.max));
		
		//for (x in ranges.xaxis)
			//console.log(x);
			
		//console.log(ranges.xaxis.max);

        // zoom data ophalen
        $.plot($("#plotTest div"), startData, setXRange(  setYRange( options, getMin(ranges.xaxis.min, ranges.xaxis.max),  getMax(ranges.xaxis.min, ranges.xaxis.max)  )   , ranges.xaxis.min  , ranges.xaxis.max  )  );

//    alert(getMax());

        return;

        
        // limiet instellen
        if (ranges.xaxis.to - ranges.xaxis.min < 10000)
            ranges.xaxis.to = ranges.xaxis.min + 10000;
        if (ranges.yaxis.to - ranges.yaxis.min < 10000)
            ranges.yaxis.to = ranges.yaxis.min + 10000;
        
        // zoom data ophalen
        $.plot($("#plotTest div"), getData(ranges.xaxis.min/10000000, ranges.xaxis.max*2/10000000),
                      $.extend(true, {}, options, {
                          xaxis: { min: ranges.xaxis.min, max: ranges.xaxis.max }
                      }));
                      
        // geschiedenis
       	geschiedenis.push(vorige);
        vorige = [ranges.xaxis.min, ranges.xaxis.max];
    });
        
    $("#plotTest li.hand.zoomOut").bind('click', function(e){
    
			if (geschiedenis.length <= 1) {
				$.plot($("#plotTest div"), startData, options);
				geschiedenis.length = 0;
				return;
			}
			
    		var bereik = geschiedenis.pop();
    		   
			$.plot($("#plotTest div"), getData(bereik[0]/10000000, bereik[1]*2/10000000),
                      $.extend(true, {}, options, {
                          xaxis: { min: bereik[0], max: bereik[1] }
            		  }));
                      
       		return false;
    });

    $("#plotTest li.hand.reset").bind('click', function(e){
		$.plot($("#plotTest div"), startData, options);
		geschiedenis.length = 0;
		return false;
    });

    $("#plotTest li.hand.selection").bind('click', function(e){
		selectie();
		return false;
    });
        
});