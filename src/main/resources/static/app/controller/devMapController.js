function devMapCtrl($scope, $http, $rootScope,$routeParams,$timeout) {

	$timeout(function () {
//		$scope.getDevList = function () {
//            $scope.getAlarmHistory();
            $scope.devListLoadFinished = false;
            var params = {
//                "groupId": $('.easyui-combotree').combotree('getValue'),
//                "search_param": $scope.devidOrName,
//                "sortByWeight": 'up'
            };
//            initialize();

            $scope.map = new BMap.Map("map", {enableMapClick: false});//关闭地图点击事件
            $scope.map.addControl(new BMap.NavigationControl()); //添加标准地图控件(左上角的放大缩小左右拖拽控件)
            $scope.map.addControl(new BMap.MapTypeControl()); //添加地图类型控件
            $scope.map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

            
            $http.get("service/getEquipments").success(function(data) {
                $scope.devListLoadFinished = true;
                $scope.dataItemsTotal = data.resultObj.total;
                $scope.dataItems = data.resultObj.list;
                try {
                    var markers = [];
                    var points = [];
                    $scope.markers = [];
                    $scope.devidMap = [];
                    for (var key in data.resultObj.list) {
                        var position = $scope.dataItems[key].site;
                        if(data.resultObj.list[key].online==1) {
                            var statusIco = 'assets/img/online.png';
                        }else {
                            var statusIco = 'assets/img/offline.png';
                        }
                        if (position && position !== '-1') {
                            var myIcon = new BMap.Icon(statusIco, new BMap.Size(20, 20),{anchor:new BMap.Size(10, 10)});
                            var content = data.resultObj.list[key].devId;
                            var marker = new BMap.Marker(new BMap.Point(data.resultObj.list[key].site.split(',')[0], data.resultObj.list[key].site.split(',')[1]), {
                                icon: myIcon,
                                title: data.resultObj.list[key].name+'('+data.resultObj.list[key].devId+')'
                            });
                            $scope.map.addOverlay(marker);
                            $scope.markers.push(marker);
                            points.push(new BMap.Point(data.resultObj.list[key].site.split(',')[0], data.resultObj.list[key].site.split(',')[1]));
//                            addClickEvent(content, marker);
                        }
                    }
//                    $scope.devidMap = function (devid, position) {
//                        httpRequest.request('getDevice', {'deviceId': devid}, true, 'post', function (res) {
//                            var point = new BMap.Point(position.split(',')[0], position.split(',')[1]);
//                            var devidState = (res.data.data.device.onlineStatus == 0) ? '<img src="assets/img/offline.png" />' : '<img src="assets/img/online.png" />';
//                            var deviceImg = res.data.data.device.img ? '<img src="/uploads/' + res.data.data.device.img + '" style="width:120px;max-height: 100%;display: block;" />' : '<img src="/uploads/device/nopic.png" style="width:120px;max-height: 100%;display: block;" />';
//                            $scope.map.centerAndZoom(point, 18);
//                            var devidManage;
//                            var devidDataHistory;
//                            var devidAlarmHistory;
//                            if($rootScope.ums_device_manage){
//                                devidManage='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="' + $scope.host + '/main.html#/device/deviceDatail/' + devid + '">' + $translate.instant(200003) + '</a>';
//                            }else {
//                                devidManage='';
//                            }
//                            if($rootScope.ums_dataHistory_list){
//                                devidDataHistory='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="' + $scope.host + '/main.html#/data/dataHistoryList/devId/' + devid + '/slaveIndex//dataId/">' + $translate.instant(200402) + '</a>';
//                            }else {
//                                devidDataHistory='';
//                            }
//                            if($rootScope.ums_alarm_list){
//                                devidAlarmHistory='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="' + $scope.host + '/main.html#/trigger/alarmHistoryList/devId/' + devid + '/slaveIndex//dataId/">' + $translate.instant(200503) + '</a>';
//                            }else {
//                                devidAlarmHistory='';
//                            }
//                            var devidContent = '<div style="margin:0;line-height:20px;padding:2px;position:relative;font-size:12px;">' +
//                                '<div style="position:absolute;width:120px;height: 100%;display: list-item;">' + deviceImg + '</div>' +
//                                '<div style="padding-left:140px;line-height: 24px;color:#333;">' +
//                                '<b>' + $translate.instant(400035) + '：</b>' + devidState + '<br />' +
//                                '<b>' + $translate.instant(400094) + '：</b>' + res.data.data.device.name + '<br />' +
//                                '<b>' + $translate.instant(400084) + '：</b>' + res.data.data.device.deviceId + '<br />' +
//                                '<b>' + $translate.instant(400030) + '：</b>' + res.data.data.device.account + '<br />' +
//                                '<b>' + $translate.instant(800009) + '：</b>' + res.data.data.device.address +
//                                '</div></div><div style="margin:20px auto;text-align: center;font-size:12px;">' +
//                                devidManage +
//                                devidDataHistory +
//                                devidAlarmHistory +
//                                '</div>';
//                            $scope.map.openInfoWindow(new BMap.InfoWindow(devidContent, {
//                                width: 370,     // 信息窗口宽度
//                                //title: "设备信息", // 信息窗口标题
//                                enableMessage: true//设置允许信息窗发送短息
//                            }), point);
//                        });
//                    };
                    if (points.length === 1) {
                        $scope.map.centerAndZoom(points[0], 12);
                    } else if (points.length > 1) {
                        var view = $scope.map.getViewport(eval(points));
                        var mapZoom = view.zoom;
                        var centerPoint = view.center;
                        $scope.map.centerAndZoom(centerPoint, mapZoom);
                    } else {
                        $scope.map.centerAndZoom(new BMap.Point(114.070855, 22.551052), 10);
                    }

                } catch (e){}
//                $scope.devList(1);
            })

//            function addClickEvent(content, marker) {
//                marker.addEventListener("click", function (e) {
//                    httpRequest.request('getDevice', {'deviceId': content}, true, 'post', function (res) {
//                        var point = new BMap.Point(e.target.getPosition().lng, e.target.getPosition().lat);
//                        var devidState = (res.data.data.device.onlineStatus == 0) ? '<img src="assets/img/offline.png" />' : '<img src="assets/img/online.png" />';
//                        var deviceImg = res.data.data.device.img ? '<img src="/uploads/' + res.data.data.device.img + '" style="width:120px;max-height: 100%;display: block;" />' : '<img src="/uploads/device/nopic.png" style="width:120px;max-height: 100%;display: block;" />';
//                        $scope.map.centerAndZoom(point, 18);
//                        var devidManage;
//                        var devidDataHistory;
//                        var devidAlarmHistory;
//                        if($rootScope.ums_device_manage){
//                            devidManage='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="' + $scope.host + '/main.html#/device/deviceDatail/' + content + '">' + $translate.instant(200003) + '</a>';
//                        }else {
//                            devidManage='';
//                        }
//                        if($rootScope.ums_dataHistory_list){
//                            devidDataHistory='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="' + $scope.host + '/main.html#/data/dataHistoryList/devId/' + content + '/slaveIndex//dataId/">' + $translate.instant(200402) + '</a>';
//                        }else {
//                            devidDataHistory='';
//                        }
//                        if($rootScope.ums_alarm_list){
//                            devidAlarmHistory='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="' + $scope.host + '/main.html#/trigger/alarmHistoryList/devId/' + content + '/slaveIndex//dataId/">' + $translate.instant(200503) + '</a>';
//                        }else {
//                            devidAlarmHistory='';
//                        }
//                        var devidContent = '<div style="margin:0;line-height:20px;padding:2px;position:relative;font-size:12px;">' +
//                            '<div style="position:absolute;width:120px;height: 100%;display: list-item;">' + deviceImg + '</div>' +
//                            '<div style="padding-left:140px;line-height: 24px;color:#333;">' +
//                            '<b>' + $translate.instant(400035) + '：</b>' + devidState + '<br />' +
//                            '<b>' + $translate.instant(400094) + '：</b>' + res.data.data.device.name + '<br />' +
//                            '<b>' + $translate.instant(400084) + '：</b>' + res.data.data.device.deviceId + '<br />' +
//                            '<b>' + $translate.instant(400030) + '：</b>' + res.data.data.device.account + '<br />' +
//                            '<b>' + $translate.instant(800009) + '：</b>' + res.data.data.device.address +
//                            '</div></div><div style="margin:20px auto;text-align: center;font-size:12px;">' +
//                            devidManage +
//                            devidDataHistory +
//                            devidAlarmHistory +
//                            '</div>';
//                        $scope.map.openInfoWindow(new BMap.InfoWindow(devidContent, {
//                            width: 370,     // 信息窗口宽度
//                            //title: "设备信息", // 信息窗口标题
//                            enableMessage: true//设置允许信息窗发送短息
//                        }), point);
//                    });
//                });
//            }


//        };
    },10);
}