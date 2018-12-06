function homeCtrl($scope, $http, $rootScope,$timeout) {
	$scope.cmp = !$rootScope.user ? true : ($rootScope.user.competence == 2 ? false : true) ;
	var Dashboard = function() {
        this.$body = $("body")
        this.$realData = []
    };
    $scope.sum=20;
    $scope.max= 100;
    $scope.home={
        'equAll':33
    }

    var plabels =  "雷击次数";
    var pcolors = '#2b9ac9';
    var plotDownloads = [[1, 0], [2, 4], [3,6], [4, 20], [5, 17], [6, 10], [7, 6], [8, 5], [9, 0], [10, 14], [11,10], [12,4]];
    var borderColor = '#fff';
    var bgColor = '#fff';
    var pielabels = ["GPRS","LORA","NBIOT"];
    var pieColors = ['#7e57c2', '#2b9ac9', "#58c9c7"];
	var pieDatas = [10,20,30];
    //creates plot graph
    Dashboard.prototype.createPlotGraph = function(selector,  data2, labels, colors, borderColor, bgColor) {
      //shows tooltip
      function showTooltip(x, y, contents) {
        $('<div id="tooltip" class="tooltipflot">' + contents + '</div>').css( {
          position: 'absolute',
          top: y + 5,
          left: x + 5
        }).appendTo("body").fadeIn(200);
      }

      $.plot($(selector),
          [
          { data: data2,
            label: labels,
            color: colors
          }
        ],
        {
            series: {
               lines: {
              show: true,
              fill: true,
              lineWidth: 2,
              fillColor: {
                colors: [ { opacity: 0.0 },
                          { opacity: 0.0 }
                        ]
              }
            },
            points: {
              show: true
            },
            shadowSize: 0
            },
            legend: {
            position: 'nw'
          },
          grid: {
            hoverable: true,
            clickable: true,
            borderColor: borderColor,
            borderWidth: 0,
            labelMargin: 10,
            backgroundColor: bgColor
          },
          yaxis: {
            min: 0,
            max: $scope.max,
            color: 'rgba(0,0,0,0)'
          },
          xaxis: {
            color: 'rgba(0,0,0,0)',
              tickFormatter: function (val, axis) {
                  return val+"月";
              },
          },
          tooltip: true,
          tooltipOpts: {
              content: '%x雷击%y次',
              shifts: {
                  x: -60,
                  y: 25
              },
              defaultTheme: false
          }
      });
    },
    //end plot graph

    //creates Pie Chart
    Dashboard.prototype.createPieGraph = function(selector, labels, datas, colors) {
        var data = [{
            label: labels[0],
            data: datas[0]
        }, {
            label: labels[1],
            data: datas[1]
        }, {
            label: labels[2],
            data: datas[2]
        }];
        var options = {
            series: {
                pie: {
                    show: true
                }
            },
            legend : {
              show : false
            },
            grid : {
              hoverable : true,
              clickable : true
            },
            colors : colors,
            tooltip : true,
            tooltipOpts : {
              content : function(label, xval, yval, flotItem){
            	  return label+"设备:"+yval+"台";
              }
//            	  content : "%s, %p.0%"
            }
        };

        $.plot($(selector), data, options);
    },

    

        //initializing various charts and components
        Dashboard.prototype.init = function() {
    	
          //plot graph data
          this.createPlotGraph("#website-stats",  plotDownloads, plabels, pcolors, borderColor, bgColor);

            //Pie graph data
            this.createPieGraph("#pie-chart #pie-chart-container", pielabels , pieDatas, pieColors);

        },

    //init Dashboard
    $.Dashboard = new Dashboard, $.Dashboard.Constructor = Dashboard;
    $.Dashboard.init();

//    console.log(datas);
//	$timeout(function(){$.Dashboard.init(); }, 1000);
}