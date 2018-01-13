ptBossApp.controller('New_PacketPaymentController', function($rootScope,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	
	
	
	
	$scope.ppf=new Object();
	$scope.ppf.payDate=new Date();
	$scope.ppf.packetPaymentDetailFactories=new Array();
	
	$scope.$on('payToPacket', function(event, mass) {
		
		$scope.ppf=new Object();
		$scope.ppf.payDate=new Date();
		$scope.ppf.payConfirm=0;
		$scope.ppf.payType="0";
		$scope.ppf.packetPaymentDetails;
		$scope.ppf.payId=0;
		$scope.ppf.payComment="";
		$scope.ppf.saleId=$scope.psf.saleId
		$scope.ppf.type;
		
		
		setPaymentType();
		
		if($scope.psf.packetPaymentFactory!=null){
			$scope.ppf=$scope.psf.packetPaymentFactory;
		}else{
			$scope.payId=0;
			$scope.payAmount=$scope.psf.packetPrice;
		}
		
		getGraphPayment();
		//findPaymentDetail($scope.psf.saleId);
		
	});
	
	
	
	function setPaymentType(){
		if($scope.psf.progType=="psp"){
			$scope.ppf.type="ppp"
		}else if($scope.psf.progType=="psc"){
			$scope.ppf.type="ppc"
		}else if($scope.psf.progType=="psm"){
			$scope.ppf.type="ppm"
		}
	}
	
	
	var findPaymentDetail=function(saleId){
		$http({
			method:'POST',
			  url: "/bein/packetpayment/findPacketPaymentBySaleId/"+saleId+"/"+$scope.ppf.type
			}).then(function successCallback(response) {
				var res=response.data;
				if(res!=null){
					$scope.ppf=res;
					
					getGraphPayment();
				}
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
	
	$scope.createPacketPayment=function(){
		
		if($scope.payType=="0"){
			toastr.error($translate.instant('noPayTypeSelected'));
			return;
		}
		
		$scope.onProgress=true;
		
		
		$.ajax({
			  type:'POST',
			  url: "/bein/packetpayment/save",
			  contentType: "application/json; charset=utf-8",				    
			  data: angular.toJson($scope.ppf),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.resultStatu==1){
					findPaymentDetail($scope.ppf.saleId);
					toastr.success($translate.instant("success"));
				}else{
					toastr.success($translate.instant("fail"));
				}
				
			}).done(function() {
				$scope.onProgress=false;
				$scope.$apply();
			});
	};
	
	
	
	$scope.packetPaymentDetailDelete=function(payDetId,payId,payAmount){
		
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
            	$.ajax({
					  type:'POST',
					  url: "../pt/packetpayment/deletePacketPaymentDetail/"+payDetId+"/"+payId+"/"+$scope.packetPaymentType,
					  contentType: "application/json; charset=utf-8",				    
					  dataType: 'json', 
					  cache:false
					}).done(function(res) {
						
						if(res.resultStatu=="2"){
							toastr.error($translate.instant(res.resultMessage));
						}else{
							$scope.packetSale.leftPrice=$scope.packetSale.leftPrice+payAmount;
							findPaymentDetail($scope.saleId);
							toastr.success($translate.instant("success"));
						}
					});
             }
      });
		
	};
	
	
	
	function getGraphPayment(){
		
		var data1=[];
		var data1Label=[];
		var data1Color=[];
		
		var data1ColorTemplate=["#62cb31","#80dd55","#a3e186","#acb89e","#c2f0c2","#adebad","#99e699","#85e085","#70db70","#5cd65c","#47d147","#33cc33","#2eb82e","#29a329","#248f24"];
		
		
		if($scope.ppf.packetPaymentDetailFactories!=null){
		  var leftPrice= $scope.psf.packetPrice-$scope.ppf.payAmount;	
		  data1.push(leftPrice);
		}else{
			 data1.push($scope.psf.packetPrice);
		}
		
		data1Label.push($translate.instant("arrear"));
		data1Color.push("#da98a1");
		
		
		$.each($scope.ppf.packetPaymentDetailFactories,function(i,ppfd){
			data1.push(ppfd.payAmount);
			data1Label.push(i+"."+$translate.instant("payAmount"));
			if(i<15){
				data1Color.push(data1ColorTemplate[i]);
			}else{
				data1Color.push(data1ColorTemplate[0]);
			}
			
		});
		
		var legend=$translate.instant("payGraph");
		
		   var polarData = {
	            datasets: [{
	                data: data1,
	                backgroundColor: data1Color,
	                label: legend // for legend
	            }],
	            labels:data1Label
	        }

	        var polarOptions = {
	            responsive: false

	        };

	        var myctx = document.getElementById("polarOptions").getContext("2d");
	        var myNewPChart =new Chart(myctx, {type: 'polarArea', data: polarData, options:polarOptions});
	}
	
	
});