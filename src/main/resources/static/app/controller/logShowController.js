/**
 * Created by heqichao on 2018-11-29.
 */

function logShowCtrl($scope, $http, $rootScope,$routeParams,$location) { $scope.pages=0;
    $scope.param={};
    $scope.param.devId="";
    $scope.param.attrKey="";
    if($routeParams){
        $scope.param.devId=$routeParams.devId;
        $scope.param.attrKey=$routeParams.dataName;
        //初始化选择
        //只有第一次从列表跳入时初始化
        $scope.param.initOption='TRUE';
    }



    $scope.fmtDate = function(date){
        var y = 1900+date.getYear();
        var m = "0"+(date.getMonth()+1);
        var d = "0"+date.getDate();
        return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
    }
    //设备用户名
    $scope.equipUserName ="";
    $scope.devList=new Array();
    $scope.attrList=new Array();
    //图型数据
    $scope.plotDownloads =new Array();
    $scope.attrType="";

    $scope.param.end =$scope.fmtDate(new Date());
    var date = new Date();//获取当前时间
    date.setDate(date.getDate()-30);//设置天数 -30天
    $scope.param.start=$scope.fmtDate(date);

    //设备信息
    $scope.equip={};
    //记录
    $scope.log=new Array();

    $scope.unit="";

    //时间组件
    $("#datepickerStrat"). datepicker().on('changeDate', function () {
        $scope.param.start=$("#datepickerStrat").val();
        $scope.init();
    });
    $("#datepickerEnd"). datepicker().on('changeDate', function (e) {
        $scope.param.end =$("#datepickerEnd").val();
        $scope.init();
    });

    //初始化数据
    $scope.init=function(){
        $http.post("/service/queryEquAttrLog",$scope.param).success(function(data) {
            $scope.param.initOption='FALSE';
            if(data.resultObj.devList){
                $scope.devList = data.resultObj.devList;
            }
           if(data.resultObj.attrList){
               $scope.attrList= data.resultObj.attrList;
           }
            if(data.resultObj.dataType){
                $scope.attrType=data.resultObj.dataType;
            }
            if(data.resultObj.unit){
                $scope.unit=data.resultObj.unit;
            }
            $scope.param.devId=data.resultObj.devId;
            $scope.param.attrKey=data.resultObj.attrKey;

            if(data.resultObj.log){
                $scope.log=data.resultObj.log;
                $scope.showChart();
            }
            if(data.resultObj.equip){
                $scope.equip=data.resultObj.equip;
            }
            if(data.resultObj.equipUserName) {
                //设备用户名
                $scope.equipUserName = data.resultObj.equipUserName
            }
        });
    };

    //初始化
    $scope.init();

    $scope.changeDevId=function () {
        $scope.param.attrKey="";
        $scope.init();
    }

    $scope.changeAttr=function(){
        for(var i=0;i<$scope.attrList.length;i++){
            if($scope.param.attrKey == $scope.attrList[i].attrName){
                $scope.attrType=$scope.attrList[i].dataType;
                $scope.unit=$scope.attrList[i].unit;
                $scope.init();
                break;
            }
        }
    }



    $scope.showChart=function(){
        $scope.plotDownloads =[];
        //非雷击波形
        if( $scope.attrType != "WAVE_TYPE"){
            for(var j=0;j<$scope.log.length;j++){
                var obj =$scope.log[j];
                var value =obj.dataValue;
                $scope.plotDownloads.push([Date.parse(new Date(obj.addDate)),value]);
            }
        }else{
            //默认显示第一个波形
            $scope.showWave($scope.log[0]);
        }

        $.Dashboard.init();
    }

    $scope.showWave=function (obj) {
        $scope.plotDownloads=[];
        if(obj && $scope.attrType == "WAVE_TYPE"){
            var values=obj.dataValue;
            var arr = values.split(",");
            for(var i=0;i<arr.length;i++){
                $scope.plotDownloads.push([i,arr[i]]);
            }
            $.Dashboard.init();
        }
    }

    var Dashboard = function() {
        this.$body = $("body")
        this.$realData = []
    };
    $scope.sum=0;
    $scope.max=1;

    var pcolors = '#2b9ac9';
    var borderColor = '#fff';
    var bgColor = '#fff';

    Dashboard.prototype.createPlotGraph = function(selector, data1,  labels, colors, borderColor, bgColor) {
        //shows tooltip
        function showTooltip(x, y, contents) {
            $('<div id="tooltip" class="tooltipflot">' + contents + '</div>').css( {
                position: 'absolute',
                top: y + 5,
                left: x + 5
            }).appendTo("body").fadeIn(200);
        }

        $.plot($(selector),
            [ { data: data1,
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
                    shadowSize: 0,
                    /*dataLabels:{
                     enabled:false
                     }*/
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
                    //min: 0,
                    //max: $scope.max,
                    autoscaleMargin:'1',
                   // tickDecimals:'2',
                    color: 'rgba(0,0,0,0)',
                    tickFormatter: function (val, axis) {
                        if($scope.unit){
                            return val+$scope.unit ; //单位
                        }else{
                            return val;
                        }

                    },
                },
                xaxis: {
                    //autoscaleMargin:'1',
                    color: 'rgba(0,0,0,0)',
                    tickFormatter: function (val, axis) {
                        //非雷击波形
                        if( $scope.attrType != "WAVE_TYPE"){
                            return formatDateTime(val);
                        }else{
                            return val;
                        }

                    },
                    labelWidth:5
                },
                tooltip: true,
                tooltipOpts: {
                    content: '%y (%x) ',
                    shifts: {
                        x: -60,
                        y: 25
                    },
                    defaultTheme: false
                }
            });
    },
        //end plot graph

        //initializing various charts and componentszuo
        Dashboard.prototype.init = function() {
            this.createPlotGraph("#website-stats2",  $scope.plotDownloads,  $scope.param.attrKey, pcolors, borderColor, bgColor);
        },

        $.Dashboard = new Dashboard, $.Dashboard.Constructor = Dashboard;

    function formatDateTime(inputTime) {
        var date = new Date(inputTime);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = date.getMinutes();
        var second = date.getSeconds();
        minute = minute < 10 ? ('0' + minute) : minute;
        second = second < 10 ? ('0' + second) : second;
        var time= y + '-' + m + '-' + d+' \n '+h+':'+minute+':'+second;
        // time =time.replace(" "," \n ");
        return time;
    };
}