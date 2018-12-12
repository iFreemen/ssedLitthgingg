function equCtrl($scope, $http,$location, $rootScope) {
	$scope.equStatus={
	        "N":"在线", 
	        "F":"故障", 
	        "B":"离线", 
	    };
	//为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:defaultSize, //每页数据量 defaultSize全局变量
    };
	$scope.pages=0;
	$scope.total=0;
	$scope.loadCtl={
    		search:false,	
    		addEnq:false
    };
	$scope.seleItem={};
	$scope.edit_cmp=$rootScope.edit_cmp;
    $scope.init=function(){
    	$scope.loadCtl.search = true;
    	$http.post("service/getEquipments",$scope.quereyData).success(function(data) {
    		console.log(data.resultObj.list);
    		$scope.equipments = data.resultObj.list;
    		$scope.pages=data.resultObj.pages;
    		$scope.total=data.resultObj.total;
    		$scope.pageArr=data.resultObj.navigatepageNums;
    		$scope.quereyData.page=data.resultObj.pageNum;
    		$scope.loadCtl.search = false;
    	});
    }
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
    $scope.selEquInfo=function(equ){
    	$scope.seleItem=equ;
    }
    //关闭详情框
    $scope.closeEquModal=function(){
    	$scope.seleItem={};
    }
    
    
	$scope.delEqu = function(eid){
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
	 $scope.gotoEdit=function(devId,id){
         $location.path("/module/equEdit/"+devId+"/"+id);
     }
	$scope.delEquById = function(eid){
		$scope.currDel = eid;
		$http.post("service/delEqu",
				{eid:$scope.currDel,}).success(function(data) {
				if(data.resultObj == "errorMsg"){
					swal(data.message, null, "error");
				}else{
					swal("删除成功", null, "success");
					$scope.init();
				}
			});
	}
	//获取上下线图片
    $scope.getStatusImg = function (status) {
        if(status == 0){
            return "assets/img/offline.png";
        } else if(status == 1){
            return "assets/img/online.png";
        }else if(status == 2){
            return "assets/img/alarmline.png";
        }
    };
    $scope.exportLora=function () {
        $rootScope.downLoadFile("/service/exprLora" );
    };
    $scope.exportNbiot=function () {
    	$rootScope.downLoadFile("/service/exprNbiot" );
    };
}