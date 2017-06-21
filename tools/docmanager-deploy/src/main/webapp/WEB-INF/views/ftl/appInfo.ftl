<!DOCTYPE html>
<html lang="en">
<#include "/include/head.ftl"/>
<body>
   <#include "/include/topbar.ftl"/>
  <div class="sui-container">
    <table class="content">
      <tr>
        <td class="menu" valign="top">
          <#include "/include/navbar.ftl"/>
        </td>
        <td class="con" valign="top">
          <div class="page_con" id="infoBody">

          </div>
        </td>
      </tr>
    </table>
  </div>
  <div id="goTop" class="goTop" style="display: block;"><a href="javascript:;">&nbsp;</a></div>
  <div class="sui-modal-backdrop fade in" style="z-index: 9999; display: none; background: rgb(0, 0, 0);">
	<div class="sui-loading loading-large center"> <i class="sui-icon icon-pc-loading"></i></div>
  </div>
</div>
<!-- 导出调用链接弹出框 start -->
<div id="importConsumer" role="dialog" data-hasfoot="false" class="sui-modal hide fade">
   <div class="modal-dialog">
	   <div class="modal-content">
		   <div class="modal-header">
			   <button type="button" data-dismiss="modal" aria-hidden="true" class="sui-close">×</button>
			   <h4 id="myModalLabel" class="modal-title">导出调用链信息</h4>
		   </div>
		   <div class="modal-body">
			   <form class="sui-form form-horizontal sui-validate">
                   <div id="datepick" class="control-group input-daterange">
                       <label class="control-label">时间选择：</label>
                       <div class="controls">
                           <input type="text"  id="beginDate" class="input-medium input-date"><span>-</span>
                           <input type="text"  id="endDate" class="input-medium input-date">
                       </div>
                   </div>
				   <div class="control-group">
					   <label class="control-label"></label>
					   <div class="controls">
						   <button type="button" data-ok="modal" class="sui-btn btn-primary btn-large">确定</button>
					   </div>
				   </div>
			   </form>
		   </div>
	   </div>
   </div>
</div>
<!-- 导出调用链接弹出框end  -->
<!-- 刷新弹出框 start -->
<div id="refreshModal" role="dialog" data-hasfoot="false" class="sui-modal hide fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" data-dismiss="modal" aria-hidden="true" class="sui-close">×</button>
        <h4 id="myModalLabel" class="modal-title">指定版本并刷新</h4>
      </div>
      <div class="modal-body">
      	<form class="sui-form form-horizontal sui-validate">
		  <div class="control-group">
		    <label for="appVersionSelect" class="control-label">指定版本：</label>
		    <div class="controls">
		      <select id="appVersionSelect" data-rules='required' >
			  </select>
		      <div class="sui-msg msg-naked msg-info"><i class="msg-icon"></i>
			      <div class="msg-con">必填</div>
			  </div>
		    </div>
		  </div>
		  <div class="control-group">
		    <label class="control-label"></label>
		    <div class="controls">
		     	<button type="button" data-ok="modal" class="sui-btn btn-primary btn-large">确定</button>
		    </div>
		  </div>
		</form>
      </div>
    </div>
  </div>
</div>
<!-- 刷新弹出框end  -->
<!-- 对比弹出框 start -->
<div id="diffModal" role="dialog" data-hasfoot="false" class="sui-modal hide fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" data-dismiss="modal" aria-hidden="true" class="sui-close">×</button>
        <h4 id="myModalLabel" class="modal-title">指定高低版本并对比</h4>
      </div>
      <div class="modal-body">
      	<form class="sui-form form-horizontal sui-validate">
		  <div class="control-group">
		    <label for="appVersionSelect1" class="control-label">低版本：</label>
		    <div class="controls">
		      <select id="appVersionSelect1" data-rules='required' >
			  </select>
		      <div class="sui-msg msg-naked msg-info"><i class="msg-icon"></i>
			      <div class="msg-con">必填</div>
			  </div>
		    </div>
		  </div>
		  <div class="control-group">
		    <label for="appVersionSelect2" class="control-label">高版本：</label>
		    <div class="controls">
		      <select id="appVersionSelect2" data-rules='required' >
			  </select>
		      <div class="sui-msg msg-naked msg-info"><i class="msg-icon"></i>
			      <div class="msg-con">必填</div>
			  </div>
		    </div>
		  </div>
		  <div class="control-group">
		    <label class="control-label"></label>
		    <div class="controls">
		     	<button type="button" data-ok="modal" class="sui-btn btn-primary btn-large">确定</button>
		    </div>
		  </div>
		</form>
      </div>
    </div>
  </div>
</div>
<!-- 对比弹出框end  -->
</body>
</html>
<!-- navbar -->
<script src="static/js/navbar.js"></script>
<!-- 模版 -->
<script id="appInfoTpl" type="text/x-handlebars-template">
	<div class="page_con">
		<div class="tab_title">
	      <p>{{name}} 当前版本:{{defaultVersionVo.version}}</p>
	      <div class="buttons">
			<!-- <button  data-toggle="modal" data-target="#importConsumer" data-keyboard="false" class="sui-btn btn-success">导出调用连</button> -->
	        <button  data-toggle="modal" data-target="#refreshModal" data-keyboard="false" class="sui-btn btn-success">刷新</button>
	        <button  data-toggle="modal" data-target="#diffModal" data-keyboard="false" class="sui-btn btn-success">对比</button>
	      </div>
	    </div>
	    <div class="tab">
	      <span class="bagcolor">api接口</span>
	      <!-- <span>消息接口</span> -->
	    </div>
	    <div class="tab_con">
	      <div>
			<table id="apilist" class="sui-table table-bordered">
				<thead>
					<tr>
						<th>服务名</th>
						<th><input id="check-all" type="checkbox"/>&nbsp;&nbsp;接口名(共{{totalApis}}个)</th>
						<th>接口说明</th>
					</tr>
				</thead>
				<tbody>
					{{#each defaultVersionVo.docServiceVos}}
						{{#each docApiVos}}
							<tr {{#equal ../isDeprecated 1}} style="text-decoration: line-through;" {{/equal}} >
								{{#equal @index 0}}
                    				<td onclick="clickCheckBox('{{../name}}')" rowspan="{{../apiCount}}" >{{../name}}<br/>{{../desc}}</td>
								{{/equal}}
								{{#equal ../isDeprecated 1}}
									<td  style="text-decoration: line-through;" >{{addOne @index}}&nbsp;<input type="checkbox" name="apiIds" class="{{../name}}" value="{{id}}">&nbsp;&nbsp;<a href="apiInfo?navId={{id}}&navType=DEPRECATED_SERVICE_API">{{name}}</a></td>
								{{else}}
									{{#equal isDeprecated 1}}
										<td  style="text-decoration: line-through;" >{{addOne @index}}&nbsp;<input type="checkbox" name="apiIds" class="{{../name}}" value="{{id}}">&nbsp;&nbsp;<a href="apiInfo?navId={{id}}&navType=DEPRECATED_API">{{name}}</a></td>
									{{else}}
										<td>{{addOne @index}}&nbsp;<input type="checkbox" name="apiIds" class="{{../name}}" value="{{id}}">&nbsp;&nbsp;<a href="apiInfo?navId={{id}}&navType=API">{{name}}</a></td>
									{{/equal}}
								{{/equal}}
								<td {{#equal isDeprecated 1}} style="text-decoration: line-through;" {{/equal}} >{{showName}}</td>
							</tr>
						{{/each}}
					{{/each}}
				</tbody>
			</table>
		  </div>
	      <div style="display: none;">
	      	<table id="messagelist" class="sui-table table-bordered">
				<thead>
					<tr>
						<th>消息TOPIC</th>
						<th>消息类型</th>
						<th>消息对象类型</th>
						<th>消息说明</th>
						<th>消息产生条件</th>
					</tr>
				</thead>
				<tbody>
					{{#each docAppMessageVos}}
						<tr >
							<td>{{topic}}</td>
							<td>{{messageType}}</td>
							<td><a href="messageInfo?navId=${navId!}&navType=${navType!}&messageId={{id}}">{{messageObjectName}}</a></td>
							<td>{{desc}}</td>
							<td>{{cause}}</td>
						</tr>
					{{/each}}
				</tbody>
			</table>
	      </div>
	    </div>
    </div>
</script>
<script id="versionSelectTpl" type="text/x-handlebars-template">
	{{#each data}}
		<option value="{{this}}" {{#equal ../defaultVersion this}} selected {{/equal}} >{{this}}</option>
	{{/each}}
</script>

<!-- api详情-->
<script>
	var navType = "APP";
	<#if navType?? >
		navType = "${navType}";
	</#if>
	var appId;
	var appName;
	var defaultVersion;
	var artifactId;
	var groupId;

	Handlebars.registerHelper('equal',function(v1 , v2,options){
       if(v1 == v2){
           return options.fn(this);
       }
       return options.inverse(this);
   });

    Handlebars.registerHelper("addOne",function(index){
	 //返回+1之后的结果
	 return index+1;
   });

	var loadApp = function(id){
		appId = id;
		$(".menu").find(".li_selected").click();
		var myTemplate = Handlebars.compile($("#appInfoTpl").html());
		$(".sui-modal-backdrop").show();
		$.ajax({
            url: "/app/info",
            type: "POST",
            data: {id:appId},
            async:false,
            dataType: "json",
            success: function(data){
                $(".sui-modal-backdrop").hide();
                if(data.success && data.stateCode.code == 0){
                	$("#infoBody").html(myTemplate(data.data));
                	artifactId = data.data.artifactId;
                	groupId = data.data.groupId;
                	appName = data.data.name;
                	if(!!data.data.defaultVersionVo){
	                	defaultVersion = data.data.defaultVersionVo.version;
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
    var clickCheckBox = function(cname){
        var element = "input[type=checkbox]."+cname;
		console.log("input[type=checkbox]."+cname)
            $(element).each(function () {
				if(this.checked){
                    this.checked = false;
				}else{
                    this.checked = true;
				}

            });
    };
</script>
<script>
(function ($) {
	<#if navId?? >
		loadApp(${navId});
	</#if>
	$('.page_con .tab').delegate('span','click',function(){
      if($(this).hasClass('bagcolor')) return false;
      var ind = $(this).index();
      $(this).addClass('bagcolor').siblings('span').removeClass('bagcolor');
      $('.page_con .tab_con').find('div').eq(ind).show().siblings('div').hide();
    });
	//定义时间格式化函数
    Date.prototype.format = function(fmt)
    {
        var o = {
            "M+" : this.getMonth()+1,         //月份
            "d+" : this.getDate(),          //日
            "h+" : this.getHours(),          //小时
            "m+" : this.getMinutes(),         //分
            "s+" : this.getSeconds(),         //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S" : this.getMilliseconds()       //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    }
    $('#importConsumer').on('shown', function(e){
        // 计算昨天的日期
        var yda = new Date().format("yyyy-MM-dd"), tda = new Date().format("yyyy-MM-dd");
        $("#beginDate").attr("value",yda);
        $("#endDate").attr("value",tda);

    });
    $('#importConsumer').on('okHide', function(e){
		var beginDate = $("#beginDate").val();
		var endDate = $("#endDate").val();
        var apiIds = [];
        $('input[name="apiIds"]:checked').each(function () {
            apiIds.push($(this).val());
        })
        window.location.href = "/app/importConsumer?appId="+appId+"&beginDate="+beginDate+"&endDate="+endDate+"&apiIds="+apiIds;
    });
    //全选
    $("#check-all").click(function () {
        if ($(this).is(':checked')) {
            $("input[type='checkbox']").each(function () {
                this.checked = true;
            });
        } else {
            $("input[type='checkbox']").each(function () {
                this.checked = false;
            });
        }
    });

    $('#refreshModal').on('shown', function(e){
		var myTemplate = Handlebars.compile($("#versionSelectTpl").html());
		$.ajax({
            url: "/version/all",
            type: "POST",
            data: {groupId:groupId,artifactId:artifactId},
            success: function(data){
            	if(data.success && data.stateCode.code == 0){
            		data.defaultVersion = defaultVersion;
                	$("#appVersionSelect").empty().append(myTemplate(data));
                	$("#appVersionSelect").change();
                }else {
                    $.alert({
                        width:'small',
                        body:data.stateCode.desc
                    });
                }
            }
        });
	});

	$("#refreshModal").on("okHide",function(){
			var appVersionSelect = $("#appVersionSelect").val();
			if(appVersionSelect == ''){
				return false;
			}
			$(".sui-modal-backdrop").show();
			$.ajax({
	            url: "/app/refresh",
	            type: "POST",
	            data: {appId:appId,appName:appName,version:appVersionSelect},
	            dataType: "json",
	            success: function(data){
	                $(".sui-modal-backdrop").hide();
	                if(data.success && data.stateCode.code == 0){
	                	window.location.reload();
	                }else {
	                    $.alert({
	                        width:'small',
	                        body:data.stateCode.desc
	                    });
	                }
	            }
	        });
		});


		$('#diffModal').on('shown', function(e){
			var myTemplate = Handlebars.compile($("#versionSelectTpl").html());
			$.ajax({
	            url: "/version/all/db",
	            type: "POST",
	            data: {appId:appId},
	            success: function(data){
	            	if(data.success && data.stateCode.code == 0){
	            		data.defaultVersion = defaultVersion;
	                	$("#appVersionSelect2").empty().append(myTemplate(data));
	                	$("#appVersionSelect2").change();
	                }else {
	                    $.alert({
	                        width:'small',
	                        body:data.stateCode.desc
	                    });
	                }
	            }
	        });
		});

		$("#appVersionSelect2").on("change",function(){
			var appVersionSelect = $("#appVersionSelect2").val();
			var myTemplate = Handlebars.compile($("#versionSelectTpl").html());
			$.ajax({
	            url: "/version/all/db",
	            type: "POST",
	            data: {appId:appId,maxVersion:appVersionSelect},
	            success: function(data){
	            	if(data.success && data.stateCode.code == 0){
	                	$("#appVersionSelect1").empty().append(myTemplate(data));
	                	$("#appVersionSelect1").change();
	                }else {
	                    $.alert({
	                        width:'small',
	                        body:data.stateCode.desc
	                    });
	                }
	            }
	        });
		});

		$('#diffModal').on('okHide', function(e){
			var version1 = $("#appVersionSelect1").val();
			if(version1 == ''){
				return false;
			}
			var version2 = $("#appVersionSelect2").val();
			if(version2 == ''){
				return false;
			}
			window.location.href="versiondiff?navId="+appId+"&navType="+navType+"&version1="+version1+"&version2="+version2;
		});

		$("select").select2({
			width:"250px"
		});
    	var format = "YYYY-MM-DD";
		$('#datepick.input-daterange').datepicker({
		});
})(jQuery)
</script>