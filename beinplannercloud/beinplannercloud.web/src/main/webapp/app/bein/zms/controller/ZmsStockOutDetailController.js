ptBossApp.controller('ZmsStockOutDetailController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	
	
	$scope.isSearch=true;
	$scope.showPayment=false;
	
	$scope.zmsStockOutDetail;
	
	$scope.init=function(){
		
		if($scope.zmsStockOutDetails!=null){
			$scope.isSearch=false;
		}
		
		
	}
	
	
	$scope.zmsPaymentPage="";
	
	$scope.payZmsStockOut=function(stockOut){
		$scope.zmsStockOutDetail=stockOut;
		$scope.paymentDetailPage="/bein/zms/zmsPayment.html";
		$scope.showPayment=true;
		
	}
	
	
	$scope.searchStockOut=function(){
		
		
		$http({
			  method: 'POST',
			  url: "/bein/zms/stockout/findAllStockOutsByProduct/"+$scope.year+"/"+$scope.zmsProduct.productId,
			}).then(function successCallback(response) {
				$scope.zmsStockOutDetails=response.data;
				$scope.isSearch=false;
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
})