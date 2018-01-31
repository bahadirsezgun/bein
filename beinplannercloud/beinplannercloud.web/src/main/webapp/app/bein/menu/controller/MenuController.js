ptBossApp.controller('MenuController', function($rootScope,$scope,$translate,commonService,homerService,globals) {
	
	$scope.profileUrl="/homerlib/images/profile.png";
	$scope.user;
	$scope.userTypeStr;
	$scope.userCommentStr;
	
	$scope.menuList="";
	$scope.menuTop="";
	
	$scope.searchBox;
	$scope.searchBoxPH;
	
	$scope.$on("searchBoxPH",function(){
		$scope.searchBoxPH=commonService.searchBoxPH;
	});
	
	
	
	$scope.searchBoxChange=function(){
		commonService.search=$scope.searchBox;
		commonService.searchItem();
	}
	
	
	function initHomer(){
		 
		 $('.animate-panel').animatePanel();
		
		 $('.closebox').unbind('click').bind('click', function (event) {
		        event.preventDefault();
		        var hpanel = $(this).closest('div.hpanel');
		        hpanel.remove();
		        if($('body').hasClass('fullscreen-panel-mode')) { $('body').removeClass('fullscreen-panel-mode');}
		    });
		 
		 $('.showhide').unbind('click').bind('click', function (event) {
		        event.preventDefault();
		        var hpanel = $(this).closest('div.hpanel');
		        var icon = $(this).find('i:first');
		        var body = hpanel.find('div.panel-body');
		        var footer = hpanel.find('div.panel-footer');
		        body.slideToggle(300);
		        footer.slideToggle(200);

		        icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
		        hpanel.toggleClass('').toggleClass('panel-collapse');
		        setTimeout(function () {
		            hpanel.resize();
		            hpanel.find('[id^=map-]').resize();
		        }, 50);
		    });
		 
		 	$('.hide-menu').unbind('click').bind('click', function(event){
		        event.preventDefault();
		        if ($(window).width() < 769) {
		            $("body").toggleClass("show-sidebar");
		        } else {
		            $("body").toggleClass("hide-sidebar");
		        }
		    });

		    
		 	
		 	
		    $('.fullscreen').unbind('click').bind('click', function() {
		        var hpanel = $(this).closest('div.hpanel');
		        var icon = $(this).find('i:first');
		        $('body').toggleClass('fullscreen-panel-mode');
		        icon.toggleClass('fa-expand').toggleClass('fa-compress');
		        hpanel.toggleClass('fullscreen');
		        setTimeout(function() {
		            $(window).trigger('resize');
		        }, 100);
		    });

		  
		
		    
		    $('.small-header-action').on('click', function(event){
		        event.preventDefault();
		        var icon = $(this).find('i:first');
		        var breadcrumb  = $(this).parent().find('#hbreadcrumb');
		        $(this).parent().parent().parent().toggleClass('small-header');
		        breadcrumb.toggleClass('m-t-lg');
		        icon.toggleClass('fa-arrow-up').toggleClass('fa-arrow-down');
		    });
		    
		
		    
		    
	};
	
	
	$scope.initMenu=function(){
		
		
		commonService.getUser().then(function(res){
			$scope.user=res;
		});
		
		commonService.getPtGlobal().then(function(res){
				$scope.ptTz=res.ptTz;
				$scope.ptCurrency=res.ptCurrency;
				$scope.ptStaticIp=res.ptStaticIp;
				$scope.ptLang=(res.ptLang).substring(0,2);
				$scope.ptDateFormat=res.ptScrDateFormat;
				
				$translate.use($scope.ptLang);
				$translate.refresh;
				
				$scope.initMenuLeft();
		});
		
		
	}
	
	
	
	$scope.initMenuLeft=function(){
			$.ajax({
						  type:'POST',
						  url: "/bein/menu/getMenuLeft",
						  contentType: "application/json; charset=utf-8",				    
						  dataType: 'json', 
						  cache:false
						}).done(function(res) {
							var menuHtml="";
							menuHtml+="<li class='active'>";
							menuHtml+="		<a href='#dashboard'> <span class='nav-label'>"+$translate.instant('main_page')+"</span> <span class='label label-success pull-right'>v.1</span> </a>";
							menuHtml+="</li>";
							
							$.each(res.resultObj,function(i,menu){
									menuHtml+="<li>";
									   menuHtml+="    <a href='"+menu.menuLink+"'> <span class='nav-label'>"+$translate.instant(menu.menuName)+"</span> " ;
									   if(menu.menuSubTbls!=null){
										   menuHtml+=" <span class='fa arrow'></span>";
									   }
									   menuHtml+="</a>";
									
									   if(menu.menuSubTbls!=null){
										   menuHtml+="    <ul class='nav nav-second-level'>";
											   $.each(menu.menuSubTbls,function(i,menuLevel2){
												   menuHtml+=" <li><a class='submenulink' href='"+menuLevel2.menuLink+"'>"+$translate.instant(menuLevel2.menuName)+"</a></li>";
												});
										   menuHtml+="    </ul>";
									   }	   
									   
								    menuHtml+="</li>";
							});
							
							$scope.menuList=menuHtml;
							$scope.$apply();
							
							
							$('#side-menu').metisMenu();
							menuEvents();
				           
							
							
						});
	  		//});
	};
	
 
	function menuEvents(){
		$('.submenulink').bind("click",function(){
			$("body").toggleClass("hide-sidebar");
			$("body").toggleClass("show-sidebar");
		});
		
	}
	
	
});
