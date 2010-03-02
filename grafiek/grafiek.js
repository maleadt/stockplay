$(function (){

    // grafiek opstellen
    function getData(x1, x2) {
        var d = [];
        for (var i = 0; i <= 25; ++i) {
            var x = x1 + i * (x2 - x1) / 50;
            d.push([x*10000000, (Math.sin(x+4)+2)*25]);
        }
                
        return [
            { label: "Beursverloop Tomtom", data: d, color: "lightblue" }
        ];
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
        yaxis: { ticks: 6 },
        selection: { mode: "x" },
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

    var startData = getData(2, 11);
    $.plot($("#plotTest div"), startData, options);
    
    var geschiedenis = new Array();
    var vorige;
    
    $("#plotTest div").bind("plotselected", function (event, ranges) {
        // limiet instellen
        if (ranges.xaxis.to - ranges.xaxis.from < 10000)
            ranges.xaxis.to = ranges.xaxis.from + 10000;
        if (ranges.yaxis.to - ranges.yaxis.from < 10000)
            ranges.yaxis.to = ranges.yaxis.from + 10000;
        
        // zoom data ophalen
        $.plot($("#plotTest div"), getData(ranges.xaxis.from/10000000, ranges.xaxis.to*2/10000000),
                      $.extend(true, {}, options, {
                          xaxis: { min: ranges.xaxis.from, max: ranges.xaxis.to }
                      }));
                      
        // geschiedenis
       	geschiedenis.push(vorige);
        vorige = [ranges.xaxis.from, ranges.xaxis.to];
        
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
        
});