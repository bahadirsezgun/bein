ptBossApp.controller('ZmsStockOutController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {


	$scope.zmsProducts;
	$scope.zmsProduct;
	
	$scope.zmsStocks;
	$scope.userDefined=false;
	
	
	
	$scope.zmsStockOuts;
	$scope.zmsStockOut=new Object();
	$scope.zmsStockOut.sellOutDate=new Date();
	$scope.zmsStockOut.productId="0";
	$scope.zmsStockOut.sellComment="";
	$scope.zmsStockOut.sellStatu="1";
	
	$scope.productOut=true;
	$scope.newStokOut=false;
	
	$scope.showStockDetail=false;
	
	$scope.user;
	
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
    		commonService.pageName=$translate.instant("zmsStockOutScreen");
		commonService.pageComment=$translate.instant("zmsStockOutScreenComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		findAllZmsStock();
		
		
		var date=new Date();
		var year=date.getFullYear();
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		$scope.year=year;
		
		
    };
    
    $scope.addNewZmsStockOut =function(){
		
    	findAllZmsProduct().then(function(zmsProducts){
    		$scope.zmsProducts=zmsProducts;
    		$scope.zmsStockOut=new Object();
			$scope.zmsStockOut.sellOutDate=new Date();
			$scope.zmsStockOut.productName="";
			$scope.zmsStockOut.productComment="";
			$scope.zmsStockOut.productId="0";
			$scope.zmsStockOut.userId=0;
			
			$scope.productOut=false;
			$scope.newStokOut=true;
    	});
	};
    
	$scope.zmsStockOutDetails=null;
	$scope.stockDetailPage="";
	
	 
    $scope.findNoPaymentZmsStockOut=function(){
    	 $http({
   		  method: 'POST',
   		  url: "/bein/zms/stockout/findStockOutForDeptors"
   		}).then(function successCallback(response) {
   			
   			$scope.zmsStockOutDetails=response.data;
   			
   			$scope.stockDetailPage="/bein/zms/zmsStockOutDetail.html";
   			$scope.showStockDetail=true;
   			$scope.productOut=false;
   			$scope.newStokOut=false;
   			
   		}, function errorCallback(response) {
   		    // called asynchronously if an error occurs
   		    // or server returns response with an error status.
   		});
    	 
    	
    }
	
	
	$scope.showStockOutDetails =function(stock){
		
		$scope.zmsProduct=stock.zmsProduct;
		$scope.stockDetailPage="/bein/zms/zmsStockOutDetail.html";
		$scope.showStockDetail=true;
		$scope.productOut=false;
		$scope.newStokOut=false;
	}

	
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
	
	$scope.deleteZmsStockOut =function(zmsStockOut){
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
		
	
	$scope.showStock =function(){
		findAllZmsStock();
		$scope.productOut=true;
		$scope.newStokOut=false;
		$scope.showStockDetail=false;
		$scope.stockDetailPage="";
		$scope.zmsStockOutDetails=null;
	}
	
	$scope.showZmsStockOut =function(productId){
		$http({
			  method: 'POST',
			  url: "/bein/zms/stockout/findById/"+productId
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
		  url: "/bein/zms/stock/findAllZmsStock"
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
    
    
    
    
   
    
    
    
    
    $scope.userList=new Array();
	$scope.selectedUserList=new Array();
	$scope.searchUsername="";
	
	
	
	$scope.searchUserBy=function(){
		
		var user=new Object();
		user.userName=$scope.searchUsername;
		user.userSurname="";
			$http({
				  method:'POST',
				  url: "/bein/member/findByUsernameAndUsersurname",
				  data:angular.toJson(user)
				}).then(function successCallback(response) {
					$scope.userList=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
	}
	
	$scope.searchUser=function(event){
		
		$scope.addUserReady=false;
		$scope.selectedUser=new Object();
		$scope.selectedUser.userId=0;
		
	}
	
	$scope.addUser=function(){
		$scope.userDefined=true;
	}

	$scope.selectedUser=null;
	
	$scope.selectUser=function(user){
		$scope.searchUsername=user.userName+" "+user.userSurname;
		$scope.user=user;
		$scope.addUserReady=true;
		 $scope.userList=new Array();
	}
    
    
	
});