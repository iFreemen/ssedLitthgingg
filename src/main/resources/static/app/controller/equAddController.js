function equAddCtrl($scope, $http) {
	$scope.loadCtl={
    		addEnq:false
    };
	$scope.addFrom = {
			modelId: '1',
			typeCd : 'L'
	}
	$scope.addEqu = function() {
    	/*$scope.loadCtl.addEnq = true;
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
        });*/
		console.log($scope.typeCd,$scope.fromTypeCd,$scope.state,$scope.selectState);
		
    };
    
    $scope.seleCompetenceChg = function(){
    	$http.get("service/getCompanySeleList").success(function(data) {
    		$scope.selCompanys = data.resultObj;

    	});
	};
	
	$scope.selEquType = function(type){
		$scope.addFrom.typeCd = type;
	}
}