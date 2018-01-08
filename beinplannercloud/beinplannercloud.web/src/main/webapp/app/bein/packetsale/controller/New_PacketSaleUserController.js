ptBossApp.controller('New_PacketSaleUserController', function($rootScope,$routeParams,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.userId;
	
	$scope.restriction;
	$scope.dateFormat;
	$scope.ptCurrency;
	$scope.dateTimeFormat;
	$scope.member;
	
	$scope.init=function(){
		$scope.userId=$routeParams.userId;
		
		commonService.pageName=$translate.instant("packetProcess");
		commonService.pageComment=$translate.instant("packetSaleUserProcessComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			
			$scope.getMember($routeParams.userId);
			
		});
		
		commonService.getRestriction().then(function(restriction){
			$scope.restriction=restriction;
		});
	}
	
	
	$scope.getMember=function(userId){
		$http({method:"POST", url:"/bein/member/findById/"+userId}).then(function(response){
			$scope.member=response.data.resultObj;
		});
	}
	
	
});
