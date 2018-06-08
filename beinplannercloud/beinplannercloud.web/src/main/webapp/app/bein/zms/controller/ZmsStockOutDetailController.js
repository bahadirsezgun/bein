ptBossApp.controller('ZmsStockOutDetailController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	
	$scope.showDetail=true;
	$scope.showUpdate=false;
	$scope.showPayment=false;
	
	$scope.zmsStockOutDetail;
	
	$scope.init=function(){
		
		if($scope.zmsProduct==null){
			
		}else{
			$scope.searchStockOut();
		}
		
		findAllZmsProduct().then(function(zmsProducts){
    		$scope.zmsProducts=zmsProducts;
		});
	}
	
	
	$scope.zmsPaymentPage="";
	
	$scope.payZmsStockOut=function(stockOut){
		$scope.zmsStockOutDetail=stockOut;
		$scope.paymentDetailPage="/bein/zms/zmsPayment.html";
		$scope.showPayment=true;
		$scope.showDetail=false;
		$scope.showUpdate=false;
	
	}
	
	$scope.user;
	
	$scope.updateZmsStockOut =function(zmsStockOut){
		
    		
    		$scope.zmsStockOut=zmsStockOut;
        	$scope.user=$scope.zmsStockOut.user;
    		
    		$scope.zmsStockOut.sellOutDate=new Date($scope.zmsStockOut.sellOutDate);
    		$scope.zmsStockOut.productId=""+$scope.zmsStockOut.productId;
    		$scope.zmsStockOut.sellStatu=""+$scope.zmsStockOut.sellStatu;
    		$scope.showPayment=false;
    		$scope.showDetail=false;
    		$scope.showUpdate=true;
    	
		
		
	}
	
	
	$scope.createZmsStockOutInDetail =function(){
		
		$scope.zmsStockOut.userId=$scope.user.userId;
		
		if($scope.zmsStockOut.productId=="0"){
			toastr.error($translate.instant("pleaseSelectProduct"));
			return;
		}
		
		
		if($scope.zmsStockOut.stockCount==""){
			toastr.error($translate.instant("pleaseSelectStockCount"));
			return;
		}
		
		$http({
			  method: 'POST',
			  url: "/bein/zms/stockout/create",
			  data:angular.toJson($scope.zmsStockOut)
			}).then(function successCallback(response) {
				$scope.zmsProduct=response.data.resultObj;
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	function findAllZmsProduct(){
		return $http({
		  method: 'POST',
		  url: "/bein/zms/product/findAll"
		}).then(function successCallback(response) {
			return response.data;
		}, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		});
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