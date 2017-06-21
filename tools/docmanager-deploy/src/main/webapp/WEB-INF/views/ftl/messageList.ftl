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
</body>
</html>
<!-- navbar -->
<script src="static/js/navbar.js"></script>
<!-- 模版 -->
<script id="messageListTpl" type="text/x-handlebars-template">
	<div class="page_con">
		<div class="tab_title">
	      <p>消息接口</p>
	      <div class="buttons">
	        <a id="edit" href="messageEdit?navId=${navId!}&navType=${navType}" class="sui-btn btn-success">编辑</a>
	      </div>
	    </div>
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
				{{#each data}}
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
</script>

<script>
	var loadMessage = function(appId){
		$(".menu").find(".li_selected").click();
		var myTemplate = Handlebars.compile($("#messageListTpl").html());
		$(".sui-modal-backdrop").show();
		$.ajax({
            url: "/message/list",
            type: "POST",
            data: {appId:appId},
            async:false,
            dataType: "json",
            success: function(data){
                $(".sui-modal-backdrop").hide();
                if(data.success && data.stateCode.code == 0){
                	$("#infoBody").html(myTemplate(data));
                }else {
                    $.alert({
                        width:'small',
                        body:data.stateCode.desc
                    });
                }
            }
        });
	};
</script>
<script>
(function ($) {
	<#if navId?? >
		loadMessage(${navId});
	</#if>
})(jQuery)
</script>