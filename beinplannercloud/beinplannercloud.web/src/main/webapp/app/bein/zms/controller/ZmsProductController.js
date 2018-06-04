ptBossApp.controller('ZmsProductController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.zmsProducts;
	$scope.zmsProduct;
	
	
	toastr.options = {
	        "debug": false,
	        "newestOnTop": false,
	        "positionClass": "toast-top-center",
	        "closeButton": true,
	        "toastClass": "animated fadeInDown",
	    };
	
	$scope.init = function(){
     	$("[data-toggle=popover]").popover();
    		commonService.pageName=$translate.instant("SportTitle");
		commonService.pageComment=$translate.instant("defSportDefinitionComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		findAllZmsProduct();
		
    };
    
    $scope.addNewZmsProduct =function(){
		$scope.zmsProduct=new Object();
		$scope.zmsProduct.productId="0";
		$scope.zmsProduct.productName="";
		$scope.zmsProduct.productComment="";
		
		
		$scope.willZmsProductCreate=true;
	};
    
	
	
	
	$scope.deleteZmsProduct =function(zmsProduct){
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
	};
	
	
	$scope.createZmsProduct =function(){
		$http({
			  method: 'POST',
			  url: "/bein/zms/product/create",
			  data:angular.toJson($scope.zmsProduct)
			}).then(function successCallback(response) {
				$scope.zmsProduct=response.data.resultObj;
				
				$scope.willZmsProductCreate=true;
				findAllZmsProduct();
			}, function errorCallback(response) {
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