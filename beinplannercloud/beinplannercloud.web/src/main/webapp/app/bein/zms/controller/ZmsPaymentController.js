ptBossApp.controller('ZmsPaymentController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.dateFormat;
	$scope.ptCurrency;
	
	$scope.zmsPayment=new Object();
	$scope.zmsPaymentDetailFactories=new Array();
	$scope.zmsPayment.payDate=new Date();
	$scope.zmsPayment.payAmount=$scope.zmsStockOutDetail.sellAmount;
	
	
	toastr.options = {
	        "debug": false,
	        "newestOnTop": false,
	        "positionClass": "toast-top-center",
	        "closeButton": true,
	        "toastClass": "animated fadeInDown",
	    };
	
	
	$scope.initZP = function(){
     	$("[data-toggle=popover]").popover();
    		commonService.pageName=$translate.instant("zmsStockInScreen");
		commonService.pageComment=$translate.instant("zmsStockInScreenComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
	
	  commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			$(".splash").css("display",'');
			findZmsPayment();
		});
		
	};
    
	function findZmsPayment(){
		$http({
			  method: 'POST',
			  url: "/bein/zms/payment/findPayment/"+$scope.zmsStockOutDetail.stkIdx,
			}).then(function successCallback(response) {
				$scope.zmsPayment=response.data;
				if($scope.zmsPayment!=null && $scope.zmsPayment!="" ){
					$scope.zmsPaymentDetails=$scope.zmsPayment.zmsPaymentDetails;
					$scope.zmsPayment.payAmount=$scope.zmsStockOutDetail.sellPrice-$scope.zmsPayment.payAmount;
					$scope.zmsPayment.payType=""+$scope.zmsPayment.payType;
					
				}else{
					$scope.zmsPayment=new Object();
					$scope.zmsPayment.stkIdx=$scope.zmsStockOutDetail.stkIdx;
					$scope.zmsPayment.payDate=new Date();
					$scope.zmsPayment.payAmount=$scope.zmsStockOutDetail.sellPrice;
					$scope.zmsPayment.payComment="";
					$scope.zmsPayment.payType="0";
				}
				$(".splash").css("display",'none');
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
				$(".splash").css("display",'none');
			});
	}
	
	
	$scope.createZmsPayment=function(){
		
		$http({
			  method: 'POST',
			  url: "/bein/zms/payment/create",
			  data:angular.toJson($scope.zmsPayment)
			}).then(function successCallback(response) {
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					findZmsPayment();
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		
	}
	
	$scope.deleteZmsPaymentDetail=function(zmsPaymentDetail){
		
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deletePacketPaymentDetailComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	
            
		$http({
			  method: 'POST',
			  url: "/bein/zms/payment/create",
			  data:angular.toJson(zmsPaymentDetail)
			}).then(function successCallback(response) {
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					findZmsPayment();
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
            }
        });   
	}
    
    
});