ptBossApp.controller('New_PacketSaleUserController', function($rootScope,$routeParams,$scope,$filter,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.userId;
	
	$scope.restriction;
	$scope.dateFormat;
	$scope.ptCurrency;
	$scope.dateTimeFormat;
	
	$scope.member;
	$scope.packetSales;
	
	$scope.infoSection=true;
	$scope.saleSection=false;
	
	$scope.userType;
	$scope.staffs;
	
	$scope.progType;
	
	$scope.btnPersonal="btn-primary";
	$scope.btnClass="btn-primary";
	$scope.btnMembership="btn-primary";
	
	$scope.programSelected=false;
	
	$scope.psf;
	
	
	$scope.infoSection=true;
	
	$scope.init=function(){
		
		$("#barOptions").attr("width",$("#cnvPanel").width())
		
		$scope.userId=$routeParams.userId;
		
		commonService.pageName=$translate.instant("packetProcess");
		commonService.pageComment=$translate.instant("packetSaleUserProcessComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			
			$scope.getMember($routeParams.userId);
			$scope.getMemberPacketSale($routeParams.userId);
		});
		
		commonService.getRestriction().then(function(restriction){
			$scope.restriction=restriction;
		});
		
		commonService.getUser().then(function(user){
			$scope.userType=user.userType;
		});
		
		
		$scope.psf=new Object();
		$scope.psf.saleId=0;
		$scope.psf.progType="";
		$scope.psf.staffId="0";
		$scope.psf.progId="0";
		$scope.psf.salesDate=new Date();
		$scope.psf.salesComment="";
		$scope.psf.bonusPayedFlag=0;
		$scope.psf.progCount=0;
		$scope.psf.saleStatu=0;
		$scope.psf.packetPrice=0;
		$scope.psf.userId=$scope.userId;
	}
	
	
	$scope.getMember=function(userId){
		$http({method:"POST", url:"/bein/member/findById/"+userId}).then(function(response){
			$scope.member=response.data.resultObj;
		}, function errorCallback(response) {
			$location.path("/login");
		});
	}
	
	$scope.getMemberPacketSale=function(userId){
		return $http({method:"POST", url:"/bein/packetsale/findUserBoughtPackets/"+userId}).then(function(response){
			$scope.packetSales=response.data;
			calculatePacket();
			getDataToGraph();
		}, function errorCallback(response) {
			$location.path("/login");
		});
	}
	
	
	$scope.saleIt=function(){
		
		if(controlSaleAttributes()){
			$http({method:"POST"
				   , url:"/bein/packetsale/sale"
				   ,data:angular.toJson($scope.psf)
				   }).then(function(response){
					   $scope.getMemberPacketSale($scope.userId);
			       }, function errorCallback(response) {
						$location.path("/login");
					});
		}
		
	}
	
	$scope.deletePS=function(packetSale){
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteStateComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: true,
            closeOnCancel: false },
        function (isConfirm) {
            if (isConfirm) {
            	$http({method:"POST"
            		, url:"/bein/packetsale/deletePacketSale"
            		,data:angular.toJson(packetSale)})
            		.then(function(response){
	        			
            			if(response.data.resultStatu==globals.RESULT_SUCCESS_STR){
            				toastr.success($translate.instant(response.data.resultMessage));
            				$scope.getMemberPacketSale($scope.userId);
            			}else{
            				toastr.error($translate.instant(response.data.resultMessage));
            			}
            		}, function errorCallback(response) {
        				$location.path("/login");
        			});
            } else {
                swal($translate.instant("deleteCanceled"), "");
            }
        });
	}
	
	$scope.editPS=function(packetSale){
		$scope.psf=packetSale;
	}
	
	$scope.paymentPS=function(packetSale){
		//$scope.psf=packetSale;
		$scope.$broadcast("payToPacket",packetSale);
		$scope.infoSection=false;
		$scope.saleSection=false;
	}
		
	
	function controlSaleAttributes(){
		if($scope.progId=="0"){
			toastr.error($translate.instant("noProgramSelectedForSale"));
			return false;
		}
		
		if($scope.progCount=="0" || $scope.progCount=="" ){
			toastr.error($translate.instant("participationMoreThanZero"));
			return false;
		}
		
		return true;
	}
	
	
	$scope.saleNewPacket=function(){
		findInstructors().then(function(staffs){
			$scope.staffs=staffs;
			
			$.each($scope.staffs,function(i,data){
				$scope.staffs[i].userId=""+data.userId;
			});
			$scope.infoSection=false;
			$scope.saleSection=true;
			findActiveProgram();
		});
	};
	
	
	function findActiveProgram(){
		
		if($scope.restriction.personalRestriction==1){
			$scope.progType="pp";
			$scope.psf.progType="psp";
			$scope.btnPersonal="btn-primary";
			$scope.btnClass="btn-default";
			$scope.btnMembership="btn-default";
		}else if($scope.restriction.groupRestriction==1){
			$scope.progType="pc";
			$scope.psf.progType="psc";
			$scope.btnPersonal="btn-default";
			$scope.btnClass="btn-primary";
			$scope.btnMembership="btn-default";
		}else if($scope.restriction.membershipRestriction==1){
			$scope.progType="pm";
			$scope.psf.progType="psm";
			$scope.btnPersonal="btn-default";
			$scope.btnClass="btn-default";
			$scope.btnMembership="btn-primary";
		}
		
		$http({method:"POST", url:"/bein/program/findPrograms/"+$scope.progType}).then(function(response){
			$scope.programs=response.data.resultObj;
			$.each($scope.programs,function(i,data){
				$scope.programs[i].progId=""+data.progId;
			});
			getDataToGraph()
		});
	}
	
	$scope.programChanged=function(){
		if($scope.psf.progId=="0"){
			$scope.programSelected=false;
		}else{
			
			$http({method:"POST", url:"/bein/program/findProgramById/"+$scope.progType+"/"+$scope.psf.progId}).then(function(response){
				$scope.program=response.data.resultObj;
				$scope.psf.progId=""+$scope.program.progId;
				$scope.psf.packetPrice=($scope.program.progPrice*$scope.program.progCount);
				$scope.psf.progCount=$scope.program.progCount;
				$scope.programSelected=true;
			});
			
		}
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
	

	 $scope.turnBackToInfo=function(){
		 $scope.infoSection=true;
		 $scope.saleSection=false;
		 
		 $scope.getMemberPacketSale($routeParams.userId).then(function(){
			 $("#barOptions").attr("width",$("#cnvPanel").width());
			 $("#barOptions").attr("height",240);
			 $("#barOptions").css({"width":$("#cnvPanel").width(),"height":"240"});
		 });
		 /*
		 setTimeout(function(){
			 
			 getDataToGraph();
		 },100);
		*/
	 };
	 
	 $scope.totalIncome=0;
	 $scope.totalDept=0;
	 
	 function calculatePacket(){
		 $scope.totalDept=0;
		 $scope.totalIncome=0;
		 $.each($scope.packetSales,function(i,data){
			 $scope.totalIncome=$scope.totalIncome+data.packetPrice;
			 if(data.packetPaymentFactory!=null){
			  $scope.totalDept=$scope.totalDept+data.packetPaymentFactory.payAmount;
			 }
		 });
		 
		 $scope.totalDept=$scope.totalIncome-$scope.totalDept;
	 }
	 
	 
	 
   function getDataToGraph(){
		
	   var dates=[];
	   var data1=[];
	   var data2=[];
	   
	   $.each($scope.packetSales,function(i,ps){
		   dates.push($filter('date')(ps.salesDate,$scope.dateFormat));
		   data1.push(ps.packetPrice);
		   if(ps.packetPaymentFactory!=null){
			   data2.push(ps.packetPaymentFactory.payAmount);
			 }else{
			   data2.push(0);
		     }
		});
	   
	   /**
        * Options for Bar chart
        */
       var barOptions = {
    		   showDatasetLabels : true,
           scaleBeginAtZero : true,
           scaleShowGridLines : true,
           scaleGridLineColor : "rgba(0,0,0,.05)",
           scaleGridLineWidth : 1,
           barShowStroke : true,
           barStrokeWidth : 1,
           barValueSpacing : 5,
           barDatasetSpacing : 1,
           responsive:true
       };
	   
	   /**
        * Data for Bar chart
        */
       var barData = {
           labels: dates,
           datasets: [
               {
                   label: $translate.instant('packetPrice'),
                   fillColor: "rgba(220,220,220,0.5)",
                   strokeColor: "rgba(220,220,220,0.8)",
                   highlightFill: "rgba(220,220,220,0.75)",
                   highlightStroke: "rgba(220,220,220,1)",
                   data: data1
               },
               {
                   label: $translate.instant("payAmount"),
                   fillColor: "rgba(98,203,49,0.5)",
                   strokeColor: "rgba(98,203,49,0.8)",
                   highlightFill: "rgba(98,203,49,0.75)",
                   highlightStroke: "rgba(98,203,49,1)",
                   data: data2
               }
           ]
       };

       var ctx = document.getElementById("barOptions").getContext("2d");
       var myNewChart = new Chart(ctx).Bar(barData, barOptions);
        
	}
	
	
});
