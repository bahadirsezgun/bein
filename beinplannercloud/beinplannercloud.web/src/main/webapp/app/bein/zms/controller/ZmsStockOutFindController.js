ptBossApp.controller('ZmsStockOutFindController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	
	$scope.zmsStockOuts;
	$scope.zmsProducts;
	$scope.zmsStockOut=new Object();
	$scope.zmsStockOut.productId="0";
	
	$scope.filterName="";
	$scope.filterSurname="";
	$scope.showSearch=true;
	$scope.showPayment=false;
	$scope.showDetail=false;
	$scope.showPayment=false;
	
	$scope.year;
	
	$scope.init = function(){
     	$("[data-toggle=popover]").popover();
    		commonService.pageName=$translate.instant("zmsStockOutFindScreen");
		commonService.pageComment=$translate.instant("zmsStockOutFindScreenComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
	
		findAllZmsProduct().then(function(zmsProducts){
    		$scope.zmsProducts=zmsProducts;

		});
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
				$scope.showSearch=false;
				$scope.showDetail=true;
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
	
	
$scope.createNewUser=function(){
		
		$scope.createUserPage="/bein/member/createfast.html";
		$scope.showSearch=false;
		$scope.showMember=true;
		
	}
	
	$scope.updateZmsStockOut =function(zmsStockOut){
		
        	$scope.user=zmsStockOut.user;
    		$scope.zmsStockOut=zmsStockOut;
    		$scope.zmsStockOut.sellOutDate=new Date($scope.zmsStockOut.sellOutDate);
    		$scope.zmsStockOut.productId=""+$scope.zmsStockOut.productId;
    		$scope.zmsStockOut.sellStatu=""+$scope.zmsStockOut.sellStatu;
    		$scope.showUpdate=true;
			$scope.showDetail=false;
        
	}
	
	
	$scope.zmsPaymentPage="";
	
	$scope.payZmsStockOut=function(stockOut){
		$scope.zmsStockOutDetail=stockOut;
		$scope.paymentDetailPage="/bein/zms/zmsPayment.html";
		$scope.showPayment=true;
		$scope.showDetail=false;
		$scope.showUpdate=false;
	
	}
	
	$scope.createZmsStockOut =function(){
		
		if($scope.zmsStockOut.productId=="0"){
			toastr.error($translate.instant("pleaseSelectProduct"));
			return;
		}
		
		
		if($scope.zmsStockOut.stockCount==""){
			toastr.error($translate.instant("pleaseSelectStockCount"));
			return;
		}
		
		$scope.zmsStockOut.userId=$scope.user.userId;
		
		var d = new Date($scope.zmsStockOut.sellOutDate);
		$scope.year = d.getFullYear();
		
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