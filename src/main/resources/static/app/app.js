//定义主模块并注入依赖
var demoApp =angular.module("demoApp", ["ngRoute"]);

//路由配置
	demoApp.config(["$routeProvider","$httpProvider", function($routeProvider,$httpProvider) {
		$httpProvider.interceptors.push(HttpInterceptor);
		$routeProvider
	.when("/module/devPage", {
		templateUrl: "app/module/devPage.html",
		controller: devPage
	}).when("/module/userList", {
		templateUrl: "app/module/userList.html",
		controller: userCtrl
	}).when("/module/equGroups", {
		templateUrl: "app/module/equGroups.html",
		controller: equGroupsCtrl
	}).when("/module/equAdd", {
		templateUrl: "app/module/equAdd.html",
		controller: equAddCtrl
	}).when("/module/equView", {
		templateUrl: "app/module/equipmentView.html",
		controller: equCtrl
	}).when("/module/lightLog", {
			templateUrl: "app/module/lightLog.html",
			controller: lightLogCtrl
	}).when("/module/nbLightLog", {
		templateUrl: "app/module/nbLightLog.html",
		controller: nbLightLogCtrl
	}).when("/module/liteEqu", {
		templateUrl: "app/module/liteEqu.html",
		controller: liteEquCtrl
	}).when("/module/liteApp", {
		templateUrl: "app/module/liteApp.html",
		controller: liteAppCtrl
	}).when("/module/liteLog", {
		templateUrl: "app/module/liteLog.html",
		controller: liteLogCtrl
	}).when("/module/warningLog", {
			templateUrl: "app/module/warningLog.html",
			controller: warningLogCtrl
	}).when("/module/equMap", {
		templateUrl: "app/module/equMap.html",
		controller: equMapCtrl
	}).when("/module/devChart/:devId/:logId", {
			templateUrl: "app/module/devChart.html",
			controller: devChartCtrl
	}).when("/module/modelEdit/:modelId", {
			templateUrl: "app/module/modelEdit.html",
			controller: modelEditCtrl
	}).when("/module/modelList", {
			templateUrl: "app/module/modelList.html",
			controller: modelListCtrl
	}).when("/module/logShow", {
			templateUrl: "app/module/logShow.html",
			controller: logShowCtrl
		}).otherwise({
		templateUrl: "app/module/home.html",
			controller: homeCtrl
		/*templateUrl: "app/module/devPage.html",
		controller: devPage*/
	});
}]);