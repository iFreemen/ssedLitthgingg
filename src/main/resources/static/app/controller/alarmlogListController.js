
function alarmLogListCtrl($scope, $http, $rootScope,$filter,$location,fileUpload) { $scope.pages=0;
    $scope.fmtDate = function(date){
        var y = 1900+date.getYear();
        var m = "0"+(date.getMonth()+1);
        var d = "0"+date.getDate();
        return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
    }
    $scope.alarmStatus='A';
    $scope.record='';
    $scope.select={};
    //为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:defaultSize, //每页数据量 defaultSize全局变量
        devId:"",
        attrId:"",
        status:"A"  //默认查报警
    };
    $scope.pageArr=[1];//页码数组
    $scope.pages= $scope.pageArr.length; //总页数
    $scope.total=0;

    /*$scope.quereyData.end =$scope.fmtDate(new Date());
    var date = new Date();//获取当前时间
    date.setDate(date.getDate()-30);//设置天数 -30天
    $scope.quereyData.start=$scope.fmtDate(date);*/

    //时间组件
    $("#datepickerStrat"). datepicker().on('changeDate', function () {
        $scope.quereyData.start=$("#datepickerStrat").val();
        $scope.init();
    });
    $("#datepickerEnd"). datepicker().on('changeDate', function (e) {
        $scope.quereyData.end =$("#datepickerEnd").val();
        $scope.init();
    });

    $scope.attrList=new Array();
    $scope.devList=new Array();

    $scope.closeAlarm=function () {
        $scope.alarmStatus='A';
        $scope.record='';
        //关闭
        $("#updateAlarm").click();
    }
    $scope.selectAlarm=function (entity) {
        $scope.select=entity;
        $scope.alarmStatus=entity.data_status;
        $scope.record=entity.record;
    }
    $scope.updateAlarm=function () {
        $http.post("/service/updateAlarm",{"status":$scope.alarmStatus,"record":$scope.record,"id":$scope.select.id}).success(function(data) {
            //初始化
            $scope.init();
            $scope.closeAlarm();
        });
    }

    //初始化数据
    $scope.init=function(){
        $http.post("/service/queryAlarmLog",$scope.quereyData).success(function(data) {
            $scope.data = data.resultObj.data.list;
            $scope.pages=data.resultObj.data.pages;
            $scope.total=data.resultObj.data.total;
            $scope.quereyData.page=data.resultObj.data.pageNum;
            if(data.resultObj.devList){
                $scope.devList=data.resultObj.devList;
            }
            if(data.resultObj.attrList){
                $scope.attrList=data.resultObj.attrList;
            }
            if(data.resultObj.devId){
                $scope.quereyData.devId=data.resultObj.devId;
            }
            if(data.resultObj.attrId){
                $scope.quereyData.attrId=data.resultObj.attrId;
            }
        });
    }

    //初始化
    $scope.init();
    //翻页
    $scope.changePage=function(page){
        $scope.quereyData.page=page;
        $scope.init();
    }
    $scope.changeDevId=function () {
        $scope.quereyData.attrId="";
        $scope.init();
    }

    $scope.changeAttr=function(){
        $scope.init();
    }


}