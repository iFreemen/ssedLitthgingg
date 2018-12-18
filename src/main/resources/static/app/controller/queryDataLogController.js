
function queryDataLogCtrl($scope, $http, $rootScope) {


    //初始化数据
    $scope.init=function(){
        $http.get("/service/queryDataLog").success(function(data) {
            $scope.data = data.resultObj;
        });
    }




    //初始化
    $scope.init();

}