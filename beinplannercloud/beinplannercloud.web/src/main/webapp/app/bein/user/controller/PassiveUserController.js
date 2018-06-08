ptBossApp.controller('PassiveUserController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	
	$scope.members;
	$scope.dateFormat;
	$scope.dateTimeFormat;
	
	$scope.init = function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			find();
		});
	};
	
	$scope.packetSale=function(member){
		$location.path('/packetsale/saletouser/'+member.userId);
	}
	
	function find(){
		$http({
			  method: 'POST',
			  url: "/bein/passive/booking/findPassive",
			}).then(function successCallback(response) {
				$scope.members=response.data;
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
	
	
	
});