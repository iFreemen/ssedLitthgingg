function equCtrl($scope, $http, $rootScope) {
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
	$scope.loadCtl={
    		search:false,	
    		addEnq:false
    };
	$scope.chkCmp = $rootScope.user.competence;
    $scope.init=function(){
    	$scope.loadCtl.search = true;
    	$http.post("service/getEquipments",$scope.quereyData).success(function(data) {
    		$scope.equipments = data.resultObj.list;
    		$scope.pages=data.resultObj.pages;
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
    
    $scope.addEqu = function() {
    	$scope.loadCtl.addEnq = true;
        $http.post("service/addEqu",
        		{eid:$scope.eid,
    			eType:$scope.eType,
    			amount:$scope.amount,
    			eRange:$scope.eRange,
    			eTotal:$scope.eTotal,
    			alarms:$scope.alarms,
    			eRemark:$scope.eRemark,
    			seleCompany:$scope.seleCompany
    			}).success(function(data) {
			    	if(data.resultObj == "errorMsg"){
			    		$("#close-add-equ-modal").click();
			    		swal(data.message, null, "error");
			        }else{
			        	//修改成功后
			        	$scope.closeAddEquModal();
			        	swal("新增成功", null, "success");
			        	$scope.init();
			        }
			    	$scope.loadCtl.addEnq = false;
        });
		
    };
    
    $scope.seleCompetenceChg = function(){
    	$http.get("service/getCompanySeleList").success(function(data) {
    		$scope.selCompanys = data.resultObj;

    	});
	};
    
    $scope.closeAddEquModal = function(){
		$scope.eid = null;
		$scope.eType = null;
		$scope.amount = null;
		$scope.eRange = null;
		$scope.eTotal = null;
		$scope.alarms = null;
		$scope.eRemark = null;
		$scope.seleCompany = null;
		$("#close-add-equ-modal").click();
	};
    
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
}