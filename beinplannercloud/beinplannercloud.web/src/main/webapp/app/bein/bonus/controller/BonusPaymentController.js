ptBossApp.controller('BonusPaymentController', function($scope,$translate,homerService,commonService,globals,$http ) {

	$scope.firms;
	$scope.firmId;
	$scope.staffs=new Array();
	$scope.staffId="0";
	
	$scope.ptCurrency;
	
	
	$scope.startDate=new Date();
	$scope.endDate=new Date();
	$scope.month="0";
	$scope.year;
	
	$scope.monthly=true;
	$scope.queryType=1;
	$scope.monthlyPayment=true;
	$scope.queryTypePayment=1;
	
	$scope.leftPaymentClass="hbgred";
	
	$scope.filter=true;
	$scope.payment=true;
	
	$scope.filterClass="";
	$scope.userBonusObj;
	$scope.rateLabel="";
	$scope.rateSuffix;
	
	$scope.payedAmount=0;
	
	$scope.bonusPayment=new Object();
	$scope.bonusPayment.bonId;
	$scope.bonusPayment.userId;
	$scope.bonusPayment.bonPaymentDate=new Date();
	$scope.bonusPayment.bonAmount;
	$scope.bonusPayment.bonComment;
	$scope.bonusPayment.bonMonth;
	$scope.bonusPayment.bonYear;
	$scope.bonusPayment.bonStartDate=new Date();
	$scope.bonusPayment.bonEndDate=new Date();
	$scope.bonusPayment.bonType="0";
	$scope.bonusPayment.bonQueryType=1;
	
	
	$scope.paymentDetailPage="";
	$scope.paymentDetail=false;
	
	$scope.bonusPaymentRule;
	
	$scope.bonusPayments=new Array();
	
	
	$scope.months=new Array({value:"0",name:$translate.instant("pleaseSelect")}
		    ,{value:"1",name:$translate.instant("january")}
		   ,{value:"2",name:$translate.instant("february")}
		   ,{value:"3",name:$translate.instant("march")}
		   ,{value:"4",name:$translate.instant("april")}
		   ,{value:"5",name:$translate.instant("may")}
		   ,{value:"6",name:$translate.instant("june")}
		   ,{value:"7",name:$translate.instant("july")}
		   ,{value:"8",name:$translate.instant("august")}
		   ,{value:"9",name:$translate.instant("september")}
		   ,{value:"10",name:$translate.instant("october")}
		   ,{value:"11",name:$translate.instant("november")}
		   ,{value:"12",name:$translate.instant("december")}
		   );
		
		
	$scope.years=new Array();
		
		
	$scope.init=function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		var date=new Date();
		var year=date.getFullYear();
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		$scope.year=year;
		
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptScrDateFormat+" HH:mm";
			$scope.ptCurrency=global.ptCurrency;
			
			
			findInstructors().then(function(response){
				
				$scope.staffs=response;
				$scope.staffId="0";
			})
			
		});
		
		commonService.getRestriction().then(function(restriction){
			$scope.restriction=restriction;
		});
		
		
		$('#inQueryCheck').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		$('#inPaymentCheck').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
    	
	}
	
	
	
	$('#inQueryCheck').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.monthly=true;
			$scope.queryType=1;
		}else{
			$scope.monthly=false;
			$scope.queryType=2;
		}
		$scope.$apply();
    });
    
    
    $('#inPaymentCheck').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.monthlyPayment=true;
			$scope.bonusPayment.bonQueryType=1;
		}else{
			$scope.monthlyPayment=false;
			$scope.bonusPayment.bonQueryType=2;
		}
		$scope.$apply();
    });
	
    
    $scope.scheduleFactories;
    
    $scope.bonusValue=0;
    $scope.staffPayment=0;
    $scope.progressPayment=0;
    
    $scope.creditCardCommissionRate;
    $scope.creditCardCommissionRule;
    
    $scope.userBonusDetailObj;
    
    $scope.editBonusDetail=function(userBonusDetailObj){
    	
    	 $scope.userBonusDetailObj=userBonusDetailObj;
    	console.log(userBonusDetailObj);
    	
    	$scope.scheduleFactories=userBonusDetailObj.scheduleFactories;
    	$scope.bonusValue==userBonusDetailObj.bonusValue;
    	$scope.paymentDetail=true;
    	
    	
    	
    	
    };
    
    
	
    
    
	$scope.showFilter=function(){
		
		if($scope.paymentDetail){
			$scope.paymentDetail=false;
		}else{
			if($scope.payment){
				$scope.filterClass="animated fadeInDown";
				$scope.filter=true;
			}else{
				$scope.payment=true;
			}
		}
	}
	
	$scope.leftPayment=0;
	$scope.staff;
	
	$scope.queryBonusPayment=function(){
		
		$(".splash").css("display",'');
		
		if($scope.bonusPayment.bonType=="0"){
			$(".splash").css("display",'none');
			toastr.error($translate.instant('chooseProgramType'));
		  return;
		}
		
		if($scope.staffId=="0"){
			$(".splash").css("display",'none');
			toastr.error($translate.instant('chooseInstructor'));
		  return;
		}
		
		if($scope.monthly && $scope.month=="0"){
			$(".splash").css("display",'none');
			toastr.error($translate.instant('chooseMonth'));
		  return;
		}
		
		  $.each($scope.staffs,function(i,data){
			  if(data.userId==$scope.staffId){
				  $scope.staff=data;
			  }
		  });
		
		  $scope.monthlyPayment=$scope.monthly;
		
		  var searchObj=new Object();
		  searchObj.queryType=$scope.queryType;
		  searchObj.schStaffId=$scope.staffId;
		  searchObj.month=$scope.month;
		  searchObj.year=$scope.year;
		  searchObj.startDate=$scope.startDate;
		  searchObj.endDate=$scope.endDate;
		  
		  
		    $scope.bonusPayment.userId=""+$scope.staffId;
	    	$scope.bonusPayment.bonStartDate=$scope.startDate;
	    	$scope.bonusPayment.bonEndDate=$scope.endDate;
	    	$scope.bonusPayment.bonMonth=""+$scope.month;
	    	$scope.bonusPayment.bonYear=$scope.year;
		  
		 $http({
			  method:'POST',
			  url: "/bein/bonus/calculate/findStaffBonus/"+$scope.bonusPayment.bonType,
			  data:angular.toJson(searchObj)
			}).then(function(response) {
				var res=response.data;
				
				$scope.userBonusObj=res;
				$scope.payedAmount=$scope.userBonusObj.payedAmount;
				
				$scope.bonusPayments=$scope.userBonusObj.userBonusPaymentFactories;
				$scope.leftPayment=$scope.userBonusObj.willPayAmount-$scope.payedAmount;
				
				if($scope.leftPayment>0){
					$scope.leftPaymentClass="hbgred";
				}else{
					$scope.leftPaymentClass="hbggreen";
				}
				
				
				$scope.bonusPaymentRule=$scope.userBonusObj.bonusPaymentRule;
				
				  $scope.creditCardCommissionRate=$scope.userBonusObj.creditCardCommissionRate;
				  $scope.creditCardCommissionRule=$scope.userBonusObj.creditCardCommissionRule;
				  
				
				if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_RATE){
					$scope.rateLabel=$translate.instant("bonusRate");
					$scope.rateSuffix="%"
				}else if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_STATIC){
					$scope.rateLabel=$translate.instant("bonusStatic");
					$scope.rateSuffix=$scope.ptCurrency;
				}else if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_STATIC_RATE){
					$scope.rateLabel=$translate.instant("bonusRateStatic");
					$scope.rateSuffix="%"
				}
				
				
				$scope.filter=false;
		
				$(".splash").css("display",'none');
			});
	};
	
	
	
   $scope.searchBonusPayment=function(){
	   $scope.payment=false;
	   $scope.bonusPayment.userId=""+$scope.staffId;
   	$scope.bonStartDate=$scope.startDate;
   	$scope.bonEndDate=$scope.endDate;
   	$scope.bonMonth=""+$scope.month;
   	$scope.bonYear=""+$scope.year;
	};
	
	
	
	 $scope.saveBonusPayment=function(){
		 $http({
			  method:'POST',
			  url: "/bein/bonus/payment/saveBonusPayment/"+$scope.bonusPayment.bonType,
			  data: angular.toJson($scope.bonusPayment),
			}).then(function(res) {
				
				if(res.resultStatu==1){
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
				 $scope.findBonusPayment();
			});
	}
    

	 $scope.deleteBonusPayment=function(bonusPayment){
		 
	
		 bonusPayment.bonType=$scope.bonusPayment.bonType;
		 
		 $http({
			  method:'POST',
			  url: "/bein/bonus/payment/deleteBonusPayment/"+$scope.bonusPayment.bonType,
			  data: angular.toJson(bonusPayment)
			}).then(function(res) {
				 $scope.findBonusPayment();
			});
	}
    
	 $scope.findBonusPayment=function(){
		
		 
		 var frmDatum=new Object();
		  frmDatum.queryType=$scope.bonusPayment.bonQueryType;
		  frmDatum.schStaffId=$scope.bonusPayment.userId;
		  frmDatum.startDate=$scope.bonusPayment.bonStartDate;
		  frmDatum.endDate=$scope.bonusPayment.bonEndDate;
		  frmDatum.month=$scope.bonusPayment.bonMonth;
		  frmDatum.year=$scope.bonusPayment.bonYear;
			
		
		 
		 $http({
			  method:'POST',
			  url: "/bein/bonus/payment/findStaffBonusPayment/"+$scope.bonusPayment.bonType,
			  data: angular.toJson(frmDatum),
			}).then(function(response) {
				var res=response.data;
				
				var totalPayment=0;
				if(res!=null){
					$.each(res,function(i,data){
						totalPayment+=data.bonAmount;
					});
					$scope.payedAmount=totalPayment;
					
					$scope.leftPayment=$scope.userBonusObj.willPayAmount-$scope.payedAmount;
					
					if($scope.leftPayment>0){
						$scope.leftPaymentClass="hbgred";
					}else{
						$scope.leftPaymentClass="hbggreen";
					}
					
				}
				$scope.bonusPayments=res;
				
			});
	 }
	 
	 
	
	
	
	 function findInstructors(){
		   return  $http({
			  method: 'POST',
			  url: "/bein/staff/findAllSchedulerStaff"
			}).then(function successCallback(response) {
				return response.data.resultObj;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	  }
	
});