function equGroupsCtrl($scope, $http, $rootScope) {
	var elem = document.createElement("script");
	elem.src = 'assets/js/jquery.easyui.min.js';
	document.body.appendChild(elem);
	$rootScope.edit_cmp=true;
	$scope.dataOnReady=false;
	$scope.dialog_status='A';
	$scope.loadCtl={
			addGrp:false
	}
	//获取设备分组列表
    $scope.getDevGroupsList = function () {
    	$http.post("service/getEquGroups").success(function(data) {
    		console.log(data);
    		$scope.tree=data.resultObj;
    		$scope.dataOnReady=true;
    		setTimeout(function () {
	    		//构建下拉树
	    		var treeFmt=angular.fromJson(angular.toJson($scope.tree));
	    		$('.easyui-combotree').combotree({
	    			data:treeFmt
	    		});
    		},400);
    	});
    };
    $scope.getDevGroupsList();
    
    //添加按钮
    $scope.addGrp = function() {
    	$scope.loadCtl.addGrp = true;
		$scope.addFrom.gId = $('#btn_easyui_combotree').combotree('getValue');
        $http.post("service/addGroup",$scope.addFrom).success(function(data) {
			    	if(data.resultObj == "errorMsg"){
			    		$("#close-add-equ-modal").click();
			    		swal(data.message, null, "error");
			        }else{
			        	//修改成功后
			        	$("#close-add-equ-modal").click();
			        	swal("添加成功", null, "success");
			        	$scope.getDevGroupsList();
			        }
			    	$scope.loadCtl.addGrp = false;
        });
		
    };
    //行按钮POST
    $scope.addGrpBtn = function() {
    	$scope.loadCtl.addGrp = true;
    	$scope.addBtnFrom.gId = $('#row_easyui_combotree').combotree('getValue');;
    	if($scope.dialog_status == 'A'){
    		$http.post("service/addGroup",$scope.addBtnFrom).success(function(data) {
    			if(data.resultObj == "errorMsg"){
    				$("#close-edt-btn").click();
    				swal(data.message, null, "error");
    			}else{
    				$("#close-edt-btn").click();
    				swal("添加成功", null, "success");
    				$scope.getDevGroupsList();
    			}
    			$scope.loadCtl.addGrp = false;
    		});
    	}else if($scope.dialog_status == 'E'){
    		$http.post("service/editGroup",$scope.addBtnFrom).success(function(data) {
    			if(data.resultObj == "errorMsg"){
    				$("#close-edt-btn").click();
    				swal(data.message, null, "error");
    			}else{
    				$("#close-edt-btn").click();
    				swal("修改成功", null, "success");
    				$scope.getDevGroupsList();
    			}
    			$scope.loadCtl.addGrp = false;
    		});
    	}
    	
    };
    
    //清空添加框
    $scope.closeAddGrpModal= function(){
    	$scope.addFrom=null;
    }
    //设置添加按钮默认值
    $scope.addGrpClick= function(){
    	$('#btn_easyui_combotree').combotree('setValue',1);
    }
    //行添加按钮
    $scope.addGroup = function ($item) {
    	$scope.dialog_status='A';
    	$scope.addBtnFrom={
    			name:null,
    			grpSort:null,
    			gId:$item.id
    	}
    	$("#dialog-equ-group").click();
    	$('#row_easyui_combotree').combotree('setValue',$item.id);
    };
    //行编辑按钮
    $scope.editGroup = function ($item) {
    	$scope.dialog_status='E';
    	$scope.addBtnFrom={
    			id:$item.id,
    			name:$item.name,
    			grpSort:$item.grpSort,
    			gId:$item.pid
    	}
    	$("#dialog-equ-group").click();
    	$('#row_easyui_combotree').combotree('setValue',$item.pid);
    };
    
    			
}