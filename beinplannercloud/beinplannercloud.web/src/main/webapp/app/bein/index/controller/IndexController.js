ptBossApp.controller('IndexController', function($rootScope,$http,$scope,commonService, Idle, Keepalive,$location) {

	$scope.headerPage="";
	
	$scope.init=function(){
		findGlobals();
	}
	
	function findGlobals(){
		commonService.getPtGlobal().then(function(result) {
			$scope.dateFormat=commonService.ptGlobal.ptDateFormat;
			$scope.dateTimeFormat=commonService.ptGlobal.ptDateTimeFormat;
			
		});
	 }
	
	
	
	
	
	
	
	
	
	
	

    $scope.$on('IdleStart', function() {
     // console.log("idle start "+new Date());

      
    });

    $scope.$on('IdleEnd', function() {
    //	console.log("idle end "+new Date())
    });

    $scope.$on('IdleTimeout', function() {
    //	console.log("idle timeout "+new Date())
    	$(location).attr("href","/login");
    });

    Idle.watch();
	
	
	
	
	
});