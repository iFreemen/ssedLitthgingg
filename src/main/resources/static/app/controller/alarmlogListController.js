
function alarmLogListCtrl($scope, $http, $rootScope,$filter,$location,fileUpload) { $scope.pages=0;


    //为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:defaultSize, //每页数据量 defaultSize全局变量
        devId:"",
        attrId:""
    };
    $scope.pageArr=[1];//页码数组
    $scope.pages= $scope.pageArr.length; //总页数
    $scope.total=0;

    $scope.attrList=new Array();
    $scope.devList=new Array();
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