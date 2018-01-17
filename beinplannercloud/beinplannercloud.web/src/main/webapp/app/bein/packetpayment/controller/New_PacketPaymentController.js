ptBossApp.controller('New_PacketPaymentController', function($rootScope,$scope,$http,$filter,$translate,parameterService,$location,homerService,commonService,globals) {

	
	
	
	
	
	$scope.packetSale;
	
	$scope.ppf=new Object();
	$scope.ppf.payDate=new Date();
	$scope.ppf.payType="0";
	$scope.packetPaymentDetailFactories=new Array();
	
	$scope.$on('payToPacket', function(event, packetSale) {
		
		$scope.packetSale=packetSale;
		
		$scope.ppf=new Object();
		$scope.ppf.payDate=new Date();
		$scope.ppf.payConfirm=0;
		$scope.ppf.payType="0";
		$scope.ppf.packetPaymentDetails;
		$scope.ppf.payId=0;
		$scope.ppf.payComment="";
		$scope.ppf.saleId=packetSale.saleId
		$scope.ppf.type;
		
		
		setPaymentType(packetSale);
		
		
		findPaymentDetail(packetSale.saleId).then(function(){
			
		});
		
		
	});
	
	
	
	function setPaymentType(packetSale){
		if(packetSale.progType=="psp"){
			$scope.ppf.type="ppp"
		}else if(packetSale.progType=="psc"){
			$scope.ppf.type="ppc"
		}else if(packetSale.progType=="psm"){
			$scope.ppf.type="ppm"
		}
	}
	
	
	var findPaymentDetail=function(saleId){
		return $http({
			method:'POST',
			  url: "/bein/packetpayment/findPacketPaymentBySaleId/"+saleId+"/"+$scope.ppf.type
			}).then(function successCallback(response) {
				var res=response.data;
				$scope.packetPaymentDetailFactories=new Array();
				if(res!=""){
					$scope.ppf=response.data;
					$scope.ppf.payType=""+$scope.ppf.payType;
					$scope.ppf.payDate=new Date($scope.ppf.payDate);
					$scope.ppf.payAmount=$scope.packetSale.packetPrice-$scope.ppf.payAmount;
					
					$scope.packetPaymentDetailFactories=$scope.ppf.packetPaymentDetailFactories;
					$scope.ppf.payComment="";
					$.each($scope.ppf.packetPaymentDetailFactories,function(i,data){
						$scope.packetPaymentDetailFactories[i].payDate=new Date(data.payDate);
					});
					
					
				}else{
					 $scope.ppf=new Object();
					 $scope.ppf.payAmount=$scope.packetSale.packetPrice;
					 $scope.ppf.payDate=new Date();
					 $scope.ppf.payType="0";
					 $scope.ppf.payComment="";
					 $scope.ppf.saleId=$scope.packetSale.saleId;
					 setPaymentType($scope.packetSale);
				}
				
				getGraphPayment($scope.packetSale);
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	$scope.createPacketPayment=function(){
		
		if($scope.ppf.payType=="0"){
			toastr.error($translate.instant('noPayTypeSelected'));
			return;
		}
		
		
		
		$scope.onProgress=true;
		
		
		$http({
			method:'POST',
			  url: "/bein/packetpayment/save",
			  data: angular.toJson($scope.ppf)
			}).then(function successCallback(response) {
				
				if(response.data.resultStatu=="success"){
					
					findPaymentDetail(response.data.resultObj.saleId);		
					toastr.success($translate.instant(response.data.resultMessage));
				}else{
					toastr.success($translate.instant(response.data.resultMessage));
				}
				
				$scope.onProgress=false;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	};
	
	
	
	$scope.packetPaymentDetailDelete=function(packetPaymentDetailFactory){
		
		
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
            		method:'POST',
					  url: "/bein/packetpayment/deleteDetail",
					  data: angular.toJson(packetPaymentDetailFactory),				    
					}).then(function successCallback(response) {
						if(response.data.resultStatu=="fail"){
							toastr.error($translate.instant(response.data.resultMessage));
						}else{
							findPaymentDetail($scope.packetSale.saleId);
							toastr.success($translate.instant(response.data.resultMessage));
						}
					}, function errorCallback(response) {
						$location.path("/login");
					});
             }
      });
		
	};
	
	
	
	function getGraphPayment(psf){
		
		var data1=[];
		var dates=[];
		var fc="rgba(98,203,49,0.5)";
		
		
		if($scope.ppf.packetPaymentDetailFactories==null){
			data1.push(parseInt(psf.packetPrice));
			dates.push($filter('date')(psf.salesDate,$scope.dateFormat));
			fc="rgba(255,0,0,0.5)";
		}else{
			$.each($scope.ppf.packetPaymentDetailFactories,function(i,ppfd){
				data1.push(parseInt(ppfd.payAmount));
				dates.push($filter('date')(ppfd.payDate,$scope.dateFormat));
			});
		}
		
		
		var singleBarOptions = {
	            /*scaleBeginAtZero : true,
	            scaleShowGridLines : true,
	            scaleGridLineColor : "rgba(0,0,0,.05)",
	            scaleGridLineWidth : 1,
	            */barShowStroke : true,
	            //barStrokeWidth : 1,
	            responsive:true,
	            //stepSize:50,
	            autoSkip: false
	        };

	        /**
	         * Data for Bar chart
	         */
	        var singleBarData = {
	            labels: dates,
	            datasets: [
	                {
	                    label: "My Second dataset",
	                    fillColor: fc,
	                    strokeColor: "rgba(98,203,49,0.8)",
	                    highlightFill: "rgba(98,203,49,0.75)",
	                    highlightStroke: "rgba(98,203,49,1)",
	                    data: data1
	                }
	            ]
	        };

	        var ctx = document.getElementById("singleBarOptions").getContext("2d");
	        var myNewChart = new Chart(ctx).Bar(singleBarData, singleBarOptions);

	}
	
	
});