ptBossApp.controller('New_PacketPaymentController', function($rootScope,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	
	
	
	
	$scope.ppf;
	
	$scope.$on('payToPacket', function(event, mass) {
		
		$scope.ppf=new Object();
		$scope.ppf.payDate=new Date();
		$scope.ppf.payConfirm=0;
		$scope.ppf.payType="0";
		$scope.ppf.packetPaymentDetails;
		$scope.ppf.payId=0;
		$scope.ppf.payComment="";
		$scope.ppf.type;
		
		setPaymentType();
		
		if($scope.psf.packetPaymentFactory!=null){
			$scope.ppf=$scope.psf.packetPaymentFactory;
		}else{
			$scope.payId=0;
			$scope.payAmount=$scope.psf.packetPrice;
		}
		findPaymentDetail($scope.psf.saleId);
		
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
			  url: "/bein/packetpayment/findPacketPaymentBySaleId/"+$scope.saleId+"/"+$scope.packetPaymentType,
			}).then(function(response) {
				var res=response.data.resultObj;
				if(res!=null){
					$scope.ppf=res;
				}
			});
	}
	
	$scope.createPacketPayment=function(){
		
		if($scope.payType=="0"){
			toastr.error($translate.instant('noPayTypeSelected'));
			return;
		}
		
		$scope.onProgress=true;
		
		var frmDatum={	"saleId":$scope.saleId
						,"payId":$scope.payId
						,"type":$scope.packetPaymentType
						,"payAmount":$scope.payAmount
						,"payDateStr":$scope.payDateStr
						,"payConfirm":$scope.payConfirm
						,"payType":$scope.payType
						,"payComment":$scope.payComment
						,'mailSubject':$translate.instant("packetPaymentSubject")
				   		,'mailContent':$translate.instant("packetPaymentContent")
				   		,'userName':$scope.packetSale.userName
				   		,'userSurname':$scope.packetSale.userSurname
				   		,'progName':$scope.packetSale.progName
				   		  
					 }
		$.ajax({
			  type:'POST',
			  url: "../pt/packetpayment/createPacketPayment",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.resultStatu==1){
					$scope.payId=res.resultMessage;
					$scope.packetSale.leftPrice=parseFloat($scope.packetSale.leftPrice)-parseFloat($scope.payAmount);
					
					findPaymentDetail($scope.saleId);
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
		
	}
	
	
});