var loadAppList = function(li){
	var myTemplate = Handlebars.compile($("#appTpl").html());
	$(".sui-modal-backdrop").show();
	$.ajax({
        url: "/app/all",
        type: "POST",
        async: false,
        data: {sortColumn:"name",sortType:"asc"},
        dataType: "json",
        success: function(data){
        	$(".sui-modal-backdrop").hide();
            if(data.success && data.stateCode.code === 0){	
                var len = li.parents('.parent_li').length * 20 + 30;
                data.len = len;
                var aWidth = 200 - len;
                data.aWidth = aWidth;
                li.append(myTemplate(data));
            }else {
                $.alert({
                    width:'small',
                    body:data.stateCode.desc
                });
            }
        }
    });
};

var loadAppSubList = function(li){
	var myTemplate = Handlebars.compile($("#appSubTpl").html());
	var appId = li.attr("navId");
	var data={};
    var len = li.parents('.parent_li').length * 20 + 30;
    data.len = len;
    var aWidth = 200 - len;
    data.aWidth = aWidth;
    data.id = appId;
    li.append(myTemplate(data));
           
}

var loadServiceList = function(li){
	var appId = li.attr("navId");
	var myTemplate = Handlebars.compile($("#serviceTpl").html());
	$(".sui-modal-backdrop").show();
	$.ajax({
        url: "/service/list",
        type: "POST",
        data: '{"appId":'+appId+',"sortColumn":"full_name","sortType":"asc","isDeprecated":0}',
        dataType: "json",
        async: false,
        contentType:"application/json",
        success: function(data){
            $(".sui-modal-backdrop").hide();
            if(data.success && data.stateCode.code === 0){
                var len = li.parents('.parent_li').length * 20 + 30;
                data.len = len;
                var aWidth = 200 - len;
                data.aWidth = aWidth;
                li.append(myTemplate(data));
            }else {
                $.alert({
                    width:'small',
                    body:data.stateCode.desc
                });
            }
        }
    });
	$(".sui-modal-backdrop").show();
	myTemplate = Handlebars.compile($("#dptServiceTpl").html());
	$.ajax({
        url: "/service/list",
        type: "POST",
        data: '{"appId":'+appId+',"sortColumn":"full_name","sortType":"asc","isDeprecated":1}',
        dataType: "json",
        async: false,
        contentType:"application/json",
        success: function(data){
            $(".sui-modal-backdrop").hide();
            if(data.success && data.stateCode.code === 0){
            	if(data.data.length > 0){
            		var len = li.parents('.parent_li').length * 20 + 30;
                    data.len = len;
                    data.appId = appId;
                    var aWidth = 200 - len;
                    data.aWidth = aWidth;
                    li.find("ul").append(myTemplate(data));
            	}
            }else {
                $.alert({
                    width:'small',
                    body:data.stateCode.desc
                });
            }
        }
    });
};

var loadDptServiceList = function(li){
	var appId = li.attr("navId");
	var myTemplate = Handlebars.compile($("#dptServiceSubTpl").html());
	$(".sui-modal-backdrop").show();
	$.ajax({
        url: "/service/list",
        type: "POST",
        data: '{"appId":'+appId+',"sortColumn":"full_name","sortType":"asc","isDeprecated":1}',
        dataType: "json",
        contentType:"application/json",
        success: function(data){
            $(".sui-modal-backdrop").hide();
            if(data.success && data.stateCode.code === 0){
            	if(data.data.length > 0){
            		var len = li.parents('.parent_li').length * 20 + 30;
                    data.len = len;
                    var aWidth = 200 - len;
                    data.aWidth = aWidth;
                    li.append(myTemplate(data));
            	}
            }else {
                $.alert({
                    width:'small',
                    body:data.stateCode.desc
                });
            }
        }
    });
};

var loadApiList = function(li){
	var serviceId = li.attr("navId");
	var myTemplate = Handlebars.compile($("#apiTpl").html());
	$(".sui-modal-backdrop").show();
	$.ajax({
        url: "/api/list/base",
        type: "POST",
        data: '{"serviceId":'+serviceId+',"sortColumn":"name","sortType":"asc","isDeprecated":0}',
        dataType: "json",
        async: false,
        contentType:"application/json",
        success: function(data){
            $(".sui-modal-backdrop").hide();
            if(data.success && data.stateCode.code === 0){
            	var len = li.parents('.parent_li').length * 20 + 30;
                data.len = len;
                var aWidth = 200 - len;
                data.aWidth = aWidth;
                li.append(myTemplate(data));
            }else {
                $.alert({
                    width:'small',
                    body:data.stateCode.desc
                });
            }

        }
    });
	$(".sui-modal-backdrop").show();
	myTemplate = Handlebars.compile($("#dptApiTpl").html());
	$.ajax({
        url: "/api/list/base",
        type: "POST",
        data: '{"serviceId":'+serviceId+',"sortColumn":"name","sortType":"asc","isDeprecated":1}',
        dataType: "json",
        async: false,
        contentType:"application/json",
        success: function(data){
            $(".sui-modal-backdrop").hide();
            if(data.success && data.stateCode.code === 0){
            	if(data.data.length > 0){
            		var len = li.parents('.parent_li').length * 20 + 30;
                    data.len = len;
                    var aWidth = 200 - len;
                    data.aWidth = aWidth;
                    data.serviceId = serviceId;
                    li.find("ul").append(myTemplate(data));
            	}
            }else {
                $.alert({
                    width:'small',
                    body:data.stateCode.desc
                });
            }

        }
    });
};

var loadDptApiList = function(li){
	var serviceId = li.attr("navId");
	var myTemplate = Handlebars.compile($("#dptApiSubTpl").html());
	$(".sui-modal-backdrop").show();
	$.ajax({
        url: "/api/list/base",
        type: "POST",
        data: '{"serviceId":'+serviceId+',"sortColumn":"name","sortType":"asc","isDeprecated":1}',
        dataType: "json",
        contentType:"application/json",
        success: function(data){
            $(".sui-modal-backdrop").hide();
            if(data.success && data.stateCode.code === 0){
        		var len = li.parents('.parent_li').length * 20 + 30;
                data.len = len;
                var aWidth = 200 - len;
                data.aWidth = aWidth;
                li.append(myTemplate(data));
            }else {
                $.alert({
                    width:'small',
                    body:data.stateCode.desc
                });
            }

        }
    });
};

var loadServiceDptApiList = function(li){
	var serviceId = li.attr("navId");
	var myTemplate = Handlebars.compile($("#serviceDptApiSubTpl").html());
	$(".sui-modal-backdrop").show();
	$.ajax({
        url: "/api/list/base",
        type: "POST",
        data: '{"serviceId":'+serviceId+',"sortColumn":"name","sortType":"asc","isDeprecated":1}',
        dataType: "json",
        contentType:"application/json",
        success: function(data){
            $(".sui-modal-backdrop").hide();
            if(data.success && data.stateCode.code === 0){
        		var len = li.parents('.parent_li').length * 20 + 30;
                data.len = len;
                var aWidth = 200 - len;
                data.aWidth = aWidth;
                li.append(myTemplate(data));
            }else {
                $.alert({
                    width:'small',
                    body:data.stateCode.desc
                });
            }

        }
    });
};
function easyTreeCall(li){
	var navType = li.attr("navType");
	var navId = li.attr("navId");
	
	if(li.attr("navType") === "APP"){
		window.location.href = "appInfo?navId="+navId+"&navType="+navType;
	}else if(li.attr("navType") === "API" || li.attr("navType") === "DEPRECATED_API" || li.attr("navType") === "DEPRECATED_SERVICE_API"){
		window.location.href = "apiInfo?navId="+navId+"&navType="+navType;
	}else if(li.attr("navType") === "MESSAGE"){
		window.location.href = "messageList?navId="+navId+"&navType="+navType;
	}else if(li.attr("navType") === "STATIC_0"){
		window.location.href = "static0?navType="+navType;
	}else if(li.attr("navType") === "STATIC_1"){
		window.location.href = "static1?navType="+navType;
	}
	
	if(!li.is('li:has(ul)')){
		if(li.attr("navType") === "ROOT_APP"){
    		loadAppList(li);
    	}else if(li.attr("navType") === "ROOT_API"){
    		loadServiceList(li);
    	}else if(li.attr("navType") === "DEPRECATED_SERVICE_LIST"){
    		loadDptServiceList(li);
    	}else if(li.attr("navType") === "SERVICE"){
    		loadApiList(li);
    	}else if(li.attr("navType") === "DEPRECATED_API_LIST" ){
    		loadDptApiList(li);
    	}else if(li.attr("navType") === "DEPRECATED_SERVICE"){
    		loadServiceDptApiList(li);
    	}
	}
	li.EasyTree({callback:easyTreeCall});
}
(function ($) {
    function init() {
        $('.easy-tree').EasyTree({
            callback: easyTreeCall
          });
    }
    window.onload = init();
    var $top = $("#goTop");
    $top.click(function() {
      $("body, html").animate({scrollTop: 0});
    });
})(jQuery)