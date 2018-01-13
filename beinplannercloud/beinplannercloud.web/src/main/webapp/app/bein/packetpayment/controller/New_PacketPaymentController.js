ptBossApp.controller('New_PacketPaymentController', function($rootScope,$scope,$http,$filter,$translate,parameterService,$location,homerService,commonService,globals) {

	
	
	
	
	$scope.ppf=new Object();
	$scope.ppf.payDate=new Date();
	$scope.ppf.packetPaymentDetailFactories=new Array();
	
	$scope.$on('payToPacket', function(event, packetSale) {
		
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
		
		if(packetSale.packetPaymentFactory!=null){
			$scope.ppf=packetSale.packetPaymentFactory;
			$scope.ppf.payDate=new Date(packetSale.packetPaymentFactory.payDate);
			$.each($scope.ppf.packetPaymentDetailFactories,function(i,data){
				$scope.ppf.packetPaymentDetailFactories[i].payDate=new Date(data.payDate);
			});
			
			
			$scope.ppf.payDate=new Date(packetSale.packetPaymentFactory.payDate);
			
		}else{
			$scope.payId=0;
			$scope.payAmount=packetSale.packetPrice;
		}
		
		
		
		setTimeout(function(){
			 $("#singleBarOptions").attr("width",($("#cnvSPanel").width()-20));
			 $("#singleBarOptions").attr("height",140);
			 //$("#singleBarOptions").css({"width":$("#cnvSPanel").width(),"height":"140"});
			 getGraphPayment(packetSale);
		 },1000);
		
		
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