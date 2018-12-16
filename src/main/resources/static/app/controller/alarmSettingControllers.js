function alarmSettingCtrl($scope, $http,$location, $rootScope) {
	//为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:defaultSize, //每页数据量 defaultSize全局变量
    };
	$scope.pages=0;
	$scope.total=0;
	$scope.loadCtl={
    		search:false,	
    		editEnq:false,	
    		addEnq:false
    };
	$scope.data= null;
	$scope.seleItem={};
	$scope.editFrom={};
	$scope.edit_cmp=$rootScope.edit_cmp;
    $scope.init=function(){
    	$scope.loadCtl.search = true;
    	$http.post("service/getAlarmSettings",$scope.quereyData).success(function(data) {
    		$scope.alarmSettings = data.resultObj.list;
    		$scope.pages=data.resultObj.pages;
    		$scope.total=data.resultObj.total;
    		$scope.pageArr=data.resultObj.navigatepageNums;
    		$scope.quereyData.page=data.resultObj.pageNum;
    		$scope.seleUserModelLst();
    		$scope.loadCtl.search = false;
    	});
    }
    //模板列表
    $scope.seleUserModelLst = function(){
    	$http.get("service/queryUserModel").success(function(data) {
    		if(data.resultObj == "errorMsg"){
    			swal(data.message, null, "error");
    		}else{
    			$scope.selModels = data.resultObj;
    		}
    	});
    };
    //数据点列表
    $scope.seleUserAttrLst = function(mid){
    	$http.post("service/queryUserAttr",{mid:mid}).success(function(data) {
    		if(data.resultObj == "errorMsg"){
    			swal(data.message, null, "error");
    		}else{
    			$scope.selAttrs = data.resultObj;
    		}
    	});
    };
  //初始化
    $scope.init();

    //翻页
    $scope.changePage=function(page){
        $scope.quereyData.page=page;
        $scope.init();
    }
    $scope.resetSearch=function(){
        $scope.quereyData.eid=null;
        $scope.quereyData.type=null;
        $scope.quereyData.seleStatus=null;
        $scope.quereyData.page=1;
        $scope.pages=0;
        $scope.init();
    }
    //选中行
    $scope.seleEdit=function(alarm){
    	$scope.seledModel(alarm.modelId);
    	angular.copy(alarm,$scope.editFrom);
    }
    //根据模板获取数据点列表
    $scope.seledModel=function(mid){
    	$scope.seleUserAttrLst(mid)
    }
    //关闭详情框
    $scope.closeEquModal=function(){
    	$scope.seleItem={};
    }
    //关闭添加框
    $scope.clearAddModal=function(){
    	$scope.addFrom={};
    }
    //关闭编辑框
    $scope.clearEditModal=function(){
    	$scope.editFrom={};
    }
    $scope.addAlarm = function() {
    	$scope.loadCtl.addEnq = true;
        $http.post("service/addAlarmSetting",$scope.addFrom).success(function(data) {
			    	if(data.resultObj == "errorMsg"){
			    		swal(data.message, null, "error");
			        }else{
			        	//修改成功后
			        	swal("新增成功", null, "success");
			        	$scope.init();
			        }
			    	$("#close-add-alarm-modal").click();
			    	$scope.loadCtl.addEnq = false;
        });
		
    };
    $scope.editAlarm = function() {
    	$scope.loadCtl.editEnq = true;
    	$http.post("service/editAlarmSetting",$scope.editFrom).success(function(data) {
    		if(data.resultObj == "errorMsg"){
    			swal(data.message, null, "error");
    		}else{
    			//修改成功后
    			swal("修改成功", null, "success");
    			$scope.init();
    		}
    		$("#close-edit-alarm-modal").click();
    		$scope.loadCtl.editEnq = false;
    	});
    	
    };
    
    
	$scope.delEqu = function(eid){
		console.log(eid)
		swal({   
            title: "是否确定删除该设备？",   
            type: "warning",   
            showCancelButton: true,   
            confirmButtonColor: "#DD6B55",   
            confirmButtonText: "确定删除",   
            cancelButtonText: "取消", 
            closeOnConfirm: false,   
            closeOnCancel: false 
        }, function(isConfirm){   
            if (isConfirm) {     
            	$scope.delEquById(eid);
            }  else {     
                swal("操作取消", null, "error");   
            } 
        });
		
	}
	 
	$scope.delEquById = function(aid){
		$http.post("service/delAlarmSetting",{aid:aid}).success(function(data) {
				if(data.resultObj == "errorMsg"){
					swal(data.message, null, "error");
				}else{
					swal("删除成功", null, "success");
					$scope.init();
				}
			});
	}
}