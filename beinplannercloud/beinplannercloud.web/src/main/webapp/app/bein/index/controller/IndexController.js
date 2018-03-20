ptBossApp.controller('IndexController', function($rootScope,$scope,commonService, Idle, Keepalive,$location) {

	$scope.headerPage="";
	
	$scope.init=function(){
		findGlobals();
	}
	
	function findGlobals(){
		$.ajax({
  		  type:'POST',
  		  url: "/bein/global/getGlobals",
  		  contentType: "application/json; charset=utf-8",				    
  		  dataType: 'json', 
  		  cache:false
  		}).done(function(result) {
  			var res=result.resultObj;
  			commonService.setPtGlobal(result.resultObj);
  			
  			
  			
  			
  			$scope.$apply();
  			
  		});
	 }
	
	
	
	
	
	
	
	
	
	
	

    $scope.$on('IdleStart', function() {
      console.log("idle start "+new Date());

      
    });

    $scope.$on('IdleEnd', function() {
    	console.log("idle end "+new Date())
    });

    $scope.$on('IdleTimeout', function() {
    	console.log("idle timeout "+new Date())
    	$(location).attr("href","/login");
    });

    Idle.watch();
	
	
	
	
	
});