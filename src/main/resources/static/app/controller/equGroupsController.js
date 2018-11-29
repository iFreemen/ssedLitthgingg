function equGroupsCtrl($scope, $http) {
	//获取设备分组列表
    $scope.getDevGroupsList = function () {
    	$http.post("service/getEquGroups").success(function(data) {
    		console.log(data);
    		$('$easyuiTree').combotree({
                data:data.resultObj
            });
            $('$easyuiTree').combotree('setValue',0);
    	});
    };
    $scope.getDevGroupsList();
/*
	"use strict";

	$http.post("service/getEquGroups").success(function(data) {
		console.log(data);
		
	});
    var Nestable = function() {};

    Nestable.prototype.updateOutput = function (e) {
        var list = e.length ? e : $(e.target),
            output = list.data('output');
        if (window.JSON) {
            output.val(window.JSON.stringify(list.nestable('serialize'))); //, null, 2));
        } else {
            output.val('JSON browser support required for this demo.');
        }
    },
    //init
    Nestable.prototype.init = function() {

        $('#nestable_list_menu').on('click', function (e) {
            var target = $(e.target),
                action = target.data('action');
            if (action === 'expand-all') {
                $('.dd').nestable('expandAll');
            }
            if (action === 'collapse-all') {
                $('.dd').nestable('collapseAll');
            }
        });

        $('#nestable_list_3').nestable();
    },
    //init
    $.Nestable = new Nestable, $.Nestable.Constructor = Nestable;
    $.Nestable.init();*/
	
	
	 
    
}