
function modelEditCtrl($scope, $http, $rootScope,$routeParams,$location) {
    $scope.modelId=$routeParams.modelId;
    if("add"==$scope.modelId){
        $scope.modelId="";
    }
    $scope.list=new Array();
    $scope.list.push({"isEdit":true});
    $scope.types=new Array();
    $scope.subTypes=new Array();
    $scope.modelName="";
    $scope.loading=false;
    //获取数据类型和数值类型数据
/*    $http.post("/service/queryModelType").success(function(data) {
        $scope.types = data.resultObj.TYPE;
        $scope.subTypes=data.resultObj.SUBTYPE;
    });*/



    $scope.init=function(){
        if( $scope.modelId && "add" != $scope.modelId){
            $http.post("/service/queryAllByModelId",{"modelId":$scope.modelId}).success(function(data) {
                $scope.list = data.resultObj.attrs;
                $scope.modelName=data.resultObj.model.modelName;
            });
        }
    };

    $scope.edit=function (entity) {
        entity.isEdit = true;
    };
    
    $scope.deleteItem=function (entity) {
        $scope.list.splice($scope.list.indexOf(entity), 1);
        entity.isEdit = false;
      /*  swal({
            title: "是否确定删除该模板属性？",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定删除",
            cancelButtonText: "取消",
            closeOnConfirm: false,
            closeOnCancel: false
        }, function(isConfirm){
            if (isConfirm) {
                $http.post("service/deleteModelAttr",{"id":entity.id}).success(function(data) {
                 if(data.success ){
                 swal("删除成功", null, "success");

                 }else{
                 swal(data.message, null, "error");
                 }
                 });

                swal("删除成功", null, "success");
            }  else {
                swal("操作取消", null, "error");
            }
        });*/
    };

    $scope.addList=function () {
        $scope.list.push({"isEdit":true});
    };

    $scope.changepType=function (entity) {

        if("INT_TYPE"==entity.dataType){
            entity.valueTypeDisable=false;
            //设置默认
            entity.valueType="TWO_UNSIGNED";
        }else{
            entity.valueType="";
            entity.valueTypeDisable=true;
            //非数值型禁止设置数值类型、小数位数等
            entity.numberFormat="";
            entity.unit="";
            entity.expression="";
        }
    }

    $scope.save=function(){
        var mapObj = new Map();
        //检验属性名称和类型
        if(!$scope.modelName){
            swal("请填写模板名称！", null, "error");
            return;
        }
        if($scope.list && $scope.list.length>0){
            for(var i = 0; i < $scope.list.length; i++){
                if(!$scope.list[i].attrName){
                    swal("请填写属性名称！", null, "error");
                    return;
                }
                if(!$scope.list[i].dataType){
                    swal("请选择数据类型！", null, "error");
                    return;
                }
                if(mapObj.get($scope.list[i].attrName)){
                    swal("属性名称不允许重复！", null, "error");
                    return;
                }else{
                    mapObj.set($scope.list[i].attrName,$scope.list[i].dataType);
                }
                //控制波形类型一定要在最后
                if("WAVE_TYPE"== $scope.list[i].dataType){
                    if(i != $scope.list.length-1){
                        swal("雷击波形只能设置在最后！", null, "error");
                        return;
                    }
                }
            }
        }
        $scope.loading=true;
        $http.post("service/saveOrUpdateModel",{"id":$scope.modelId,"modelName":$scope.modelName,"list":JSON.stringify($scope.list)}).success(function(data) {
            if(data.success ){
                $scope.loading=false;
                $scope.modelId =data.resultObj;
                swal("保存成功", null, "success");
                $location.path("/module/modelList");
            }else{
                swal(data.message, null, "error");
                $scope.loading=false;
            }
        });
    }


    //初始化
    $scope.init();


}