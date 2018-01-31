ptBossApp.controller('LeftPaymentController', function($rootScope,$http,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.ptCurrency;
	$scope.dateFormat;
	$scope.leftPacketPayment;
	
	$scope.paymentShow=false;
	$scope.packetSale;
	$scope.packetPaymentType;
	
	$scope.initLPC=function(){
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			$scope.leftPaymentDetail();
		});
		
		commonService.pageName=$translate.instant("packetLeftPaymentProcess");
		commonService.pageComment=$translate.instant("packetLeftPaymentProcessComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
	}
	
	
	$scope.editPayment=function(lpp){
		
		$location.path('/packetsale/saletouser/'+lpp.user.userId);
		
	}
	
	
	
	
	$scope.leftPaymentDetail=function(){
		 
		
		$http({
			  method:'POST',
			  url: "/bein/packetsale/leftPayments",
			}).then(function successCallback(response) {
				$scope.paymentShow=false;
				$scope.userPacketPaymentPage="";
				$scope.leftPacketPayment=response.data;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
});