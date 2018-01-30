ptBossApp.controller('PaymentConfirmController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.confirmed=0;
	$scope.unConfirmed=0;
	
	$scope.confirms;
	$scope.showQuery=true;
	$scope.filterName="";
	$scope.filterSurname="";
	
	$scope.showDetail=false;
	$scope.ptCurrency;
	$scope.dateFormat;
	
	
	$scope.init=function(){
		$('.i-checks').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			
		});
		
	}
	
	$('#confirmChk').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.confirmed=1;
		}else{
			$scope.confirmed=0;
		}
		$scope.$apply();
    });
	
	$('#unConfirmChk').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.unConfirmed=1;
		}else{
			$scope.unConfirmed=0;
		}
		$scope.$apply();
    });
	

	
	
	$scope.payConfirm =function(puc){
		
		if(puc.payConfirm==1){
			puc.payConfirm=0;
		}else{
			puc.payConfirm=1;
		}
		
		$http({
			  method:'POST',
			  url: "/bein/packetpayment/updatePaymentToConfirm",
			  data: angular.toJson(puc),
			}).then(function successCallback(response) {
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	$scope.find =function(){
		   
		  var frmDatum = {"userName":$scope.filterName
			,"userSurname":$scope.filterSurname
			,"confirmed":$scope.confirmed
			,"unConfirmed":$scope.unConfirmed
			}; 
		  
		   $.ajax({
			   method:'POST',
			  url: "/bein/packetpayment/findPaymentsToConfirm",
			  data: angular.toJson(frmDatum),
			}).then(function successCallback(response) {
					$scope.confirms=response.data;
				    $scope.showQuery=false;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	};
	
	$scope.packetPaymentDetails;
	
	$scope.closeDetail =function(){
		$scope.showDetail=false;
		$scope.packetPaymentDetails=new Array();
		$scope.packetPayment="";
	};
	
	$scope.packetPayment;
	
	$scope.findDetail =function(packetPayment){
		$scope.packetPayment=packetPayment;
		$.ajax({
			  method:'POST',
			  url: "/bein/packetpayment/findPacketPaymentByPayId/"+packetPayment.payId+"/"+packetPayment.type,
			}).then(function successCallback(response) {
				$scope.packetPaymentDetails=response.data.packetPaymentDetailFactories;
				$scope.showDetail=true;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	
	
	$scope.query =function(){
		if($scope.showDetail){
			 $scope.showDetail=false;
			 $scope.packetPayment="";
		}else{
			$scope.showQuery=true;
			 $scope.showDetail=false;
			 $scope.packetPayment="";
		}
		 
	}
	
	
	
});