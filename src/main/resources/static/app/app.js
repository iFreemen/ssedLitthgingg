//定义主模块并注入依赖
var demoApp =angular.module("demoApp", ["ngRoute"]);

//路由配置
	demoApp.config(["$routeProvider","$httpProvider", function($routeProvider,$httpProvider) {
		$httpProvider.interceptors.push(HttpInterceptor);
		$routeProvider

	// 	.when("/player/list", {
	// 	templateUrl: "tmpl/player/list.html",
	// 	controller: playerListCtrl
	// })
	// 	.when("/player/view/:playerId/:playerName", {
	// 	templateUrl: "tmpl/player/view.html",
	// 	controller: playerViewCtrl
	// }).when("/player/add", {
	// 	templateUrl: "tmpl/player/add.html",
	// 	controller: playerAddCtrl
	// })
	.when("/module/demoChar/:ID", {
		templateUrl: "app/module/demoChar.html",
		controller: charCtrl
	}).when("/module/demoCharTest", {
		templateUrl: "app/module/demoCharTest.html",
		controller: demoCharTestCtrl
	}).when("/module/userList", {
		templateUrl: "app/module/userList.html",
		controller: userCtrl
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
		}).otherwise({
		templateUrl: "app/module/home.html",
		controller: homeCtrl
	});
}]);