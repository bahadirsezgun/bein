ptBossApp.controller('IndexController', function($rootScope,$scope,commonService) {

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
});