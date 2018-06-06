ptBossApp.controller('ZmsStockOutFindController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	
	$scope.zmsStockOuts;
	$scope.zmsProducts;
	$scope.zmsStockOut=new Object();
	$scope.zmsStockOut.productId="0";
	
	$scope.filterName="";
	$scope.filterSurname="";
	$scope.isSearch=true;
	
	
	$scope.init = function(){
     	$("[data-toggle=popover]").popover();
    		commonService.pageName=$translate.instant("SportTitle");
		commonService.pageComment=$translate.instant("defSportDefinitionComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
	
	
	};
	
	$scope.findKey=function(keyEvent){
		if (keyEvent.which === 13){
			$scope.find();
		}
	}
	
	$scope.find=function(){
		
		var userq=new Object();
		userq.userName=$scope.filterName+"%";
		userq.userSurname=$scope.filterSurname+"%";
		$http({
			  method: 'POST',
			  url: "/bein/zms/stockout/findByName",
			  data:angular.toJson(userq)
			}).then(function successCallback(response) {
				$scope.zmsStockOuts=response.data;
				$scope.isSearch=false;
				$scope.newStokOut=false;
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
	
	$scope.updateZmsStockOut =function(zmsStockOut){
		
		findAllZmsProduct().then(function(zmsProducts){
    		$scope.zmsProducts=zmsProducts;
			
        setTimeout(function(){
        	$scope.user=zmsStockOut.user;
    		$scope.zmsStockOut=zmsStockOut;
    		$scope.zmsStockOut.sellOutDate=new Date($scope.zmsStockOut.sellOutDate);
    		$scope.zmsStockOut.productId=""+$scope.zmsStockOut.productId;
    		$scope.zmsStockOut.sellStatu=""+$scope.zmsStockOut.sellStatu;
    		$scope.isSearch=false;
    		$scope.newStokOut=true;
        },500);
		
		
		});
	}
	
	$scope.createZmsStockOut =function(){
		
		$scope.zmsStockOut.userId=$scope.user.userId;
		
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
				
				
				$scope.willZmsStockInCreate=true;
				findAllZmsStock();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	
	$scope.deleteZmsStockOut =function(zmsStockOut){
		
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteStockOutComment"),
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
					  url: "/bein/zms/stockout/delete",
					  data:angular.toJson(zmsStockOut)
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
		
	
});