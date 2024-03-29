ptBossApp.controller('ZmsProductController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.zmsProducts;
	$scope.zmsProduct=new Object();
	$scope.zmsProduct.productUnit="count";
	
	toastr.options = {
	        "debug": false,
	        "newestOnTop": false,
	        "positionClass": "toast-top-center",
	        "closeButton": true,
	        "toastClass": "animated fadeInDown",
	    };
	
	$scope.init = function(){
     	$("[data-toggle=popover]").popover();
    		commonService.pageName=$translate.instant("zmsProductScreen");
		commonService.pageComment=$translate.instant("zmsProductScreenComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		findAllZmsProduct();
		
    };
    
    $scope.addNewZmsProduct =function(){
		$scope.zmsProduct=new Object();
		$scope.zmsProduct.productId="0";
		$scope.zmsProduct.productName="";
		$scope.zmsProduct.productComment="";
		$scope.zmsProduct.productUnit="count";
		
		$scope.willZmsProductCreate=true;
	};
    
	
	
	
	$scope.deleteZmsProduct =function(zmsProduct){
		
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
			  url: "/bein/zms/product/delete",
			  data:angular.toJson(zmsProduct)
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
	
	
	$scope.createZmsProduct =function(){
		$http({
			  method: 'POST',
			  url: "/bein/zms/product/create",
			  data:angular.toJson($scope.zmsProduct)
			}).then(function successCallback(response) {
				toastr.success($translate.instant("success"));
				$scope.willZmsProductCreate=true;
				findAllZmsProduct();
			}, function errorCallback(response) {
				toastr.success($translate.instant("fail"));
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
		
	
	
	$scope.showZmsProduct =function(productId){
		$http({
			  method: 'POST',
			  url: "/bein/zms/product/findById/"+productId
			}).then(function successCallback(response) {
				$scope.zmsProduct=response.data;
				$scope.willZmsProductCreate=true;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		
	};
	
    function findAllZmsProduct(){
		$http({
		  method: 'POST',
		  url: "/bein/zms/product/findAll"
		}).then(function successCallback(response) {
			$scope.zmsProducts=response.data;
			if($scope.zmsProducts.length!=0){
				$scope.noProgram=false;
			}else{
				$scope.noProgram=true;
			}
			
		}, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		});
    }
	
});