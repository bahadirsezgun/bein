ptBossApp.controller('ZmsStockInController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	
	$scope.zmsProducts;
	$scope.zmsProduct;
	
	$scope.zmsStocks;
	
	$scope.zmsStockIns;
	$scope.zmsStockIn=new Object();
	$scope.zmsStockIn.stokInDate=new Date();
	$scope.zmsStockIn.productId="0";
	
	$scope.productIn=true;
	$scope.newStokIn=false;
	
	$scope.years=new Array();
	
	toastr.options = {
	        "debug": false,
	        "newestOnTop": false,
	        "positionClass": "toast-top-center",
	        "closeButton": true,
	        "toastClass": "animated fadeInDown",
	    };
	
	$scope.init = function(){
     	$("[data-toggle=popover]").popover();
    		commonService.pageName=$translate.instant("zmsStockInScreen");
		commonService.pageComment=$translate.instant("zmsStockInScreenComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		var date=new Date();
		var year=date.getFullYear();
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		$scope.year=year;
		
		findAllZmsStock();
		
    };
    
    $scope.addNewZmsStockIn =function(){
		
    	findAllZmsProduct().then(function(zmsProducts){
    		$scope.zmsProducts=zmsProducts;
    		$scope.zmsStockIn=new Object();
			$scope.zmsStockIn.stockInDate=new Date();
			$scope.zmsStockIn.productName="";
			$scope.zmsStockIn.productComment="";
			$scope.zmsStockIn.productId="0";
			
			$scope.productIn=false;
			$scope.newStokIn=true;
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
	
	$scope.deleteZmsStockIn =function(zmsStockIn){
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
			  url: "/bein/zms/stockin/delete",
			  data:angular.toJson(zmsStockIn)
			}).then(function successCallback(response) {
				toastr.success($translate.instant(response.data.resultMessage))
				findAllDefSport();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
            }
            });
	};
	
	
	$scope.createZmsStockIn =function(){
		
		if($scope.zmsStockIn.productId=="0"){
			toastr.error($translate.instant("pleaseSelectProduct"));
			return;
		}
		
		if($scope.zmsStockIn.stockCount==""){
			toastr.error($translate.instant("pleaseSelectStockCount"));
			return;
		}
		
		$http({
			  method: 'POST',
			  url: "/bein/zms/stockin/create",
			  data:angular.toJson($scope.zmsStockIn)
			}).then(function successCallback(response) {
				$scope.zmsProduct=response.data.resultObj;
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				
				$scope.willZmsStockInCreate=true;
				findAllZmsStock();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
		
	
	$scope.showStock =function(){
		findAllZmsStock();
		$scope.productIn=true;
		$scope.newStokIn=false;
	}
	
	$scope.showZmsStockIn =function(productId){
		$http({
			  method: 'POST',
			  url: "/bein/zms/stockin/findById/"+productId
			}).then(function successCallback(response) {
				$scope.zmsProduct=response.data;
				$scope.willZmsStockInCreate=true;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		
	};
	
    function findAllZmsStock(){
		$http({
		  method: 'POST',
		  url: "/bein/zms/stock/findAllZmsStock/"+$scope.year
		}).then(function successCallback(response) {
			$scope.zmsStocks=response.data;
			if($scope.zmsStocks.length!=0){
				$scope.noStok=false;
			}else{
				$scope.noStok=true;
			}
			
		}, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		});
    }
	
});