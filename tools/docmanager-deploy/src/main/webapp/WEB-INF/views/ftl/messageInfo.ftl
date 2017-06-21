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
<script id="messageInfoTpl" type="text/x-handlebars-template">
	<div class="sui-container">
		<h3>基本信息</h3>
		<table id="info" class="sui-table table-sideheader table-nobordered ">
			<tbody>
				<tr>
					<th>消息TOPIC</th>
					<td>{{topic}}</td>
				</tr>
				<tr>
					<th>消息类型</th>
					<td>{{messageType}}</td>
				</tr>
				<tr>
					<th>消息对象类型</th>
					<td>{{messageObjectName}}</td>
				</tr>
				<tr>
					<th>消息说明</th>
					<td>{{desc}}</td>
				</tr>
				<tr>
					<th>消息产生条件</th>
					<td>{{cause}}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="sui-container">
		<h3>消息对象详情</h3>
		<table id="messageObject" class="sui-table table-bordered treetable">
			<thead>
				<tr>
					<th>参数名</th>
					<th>java类</th>
					<th>说明</th>
				</tr>
			</thead>
			<tbody>
				{{#each messageObjects}}
					<tr data-tt-id="messageObject.{{id}}" class="{{isLeaf}} collapsed messageObject" data-tt-parent-id="messageObject.{{pid}}">
						<td>{{fieldName}}</td>
						<td>{{classType}}</td>
						<td>{{fieldComment}}</td>
					</tr>
				{{/each}}
			</tbody>
		</table>
	</div>
</script>

<!-- message详情-->
<script>
	var param_id;
	var loadMessage = function(messageId){
		$(".menu").find(".li_selected").click();
		var myTemplate = Handlebars.compile($("#messageInfoTpl").html());
		$(".sui-modal-backdrop").show();
		$.ajax({
            url: "/message/info",
            type: "POST",
            data: {id:messageId},
            dataType: "json",
            success: function(data){
                $(".sui-modal-backdrop").hide();
                if(data.success && data.stateCode.code == 0){
                	var messageObjects = [];
                	var messageObject = $.parseJSON(data.data.messageObjectJson);
                	if(!!messageObject){
                		messageObjects.push(messageObject);
                	}
                	param_id = 1;
                	data.data.messageObjects = loadParamList(messageObjects);
                	$("#infoBody").html(myTemplate(data.data));
                	$("#messageObject").treetable(
						{ 
							expandable: true,
							initialState:"expanded"
						}
					);
                }else {
                    $.alert({
                        width:'small',
                        body:data.stateCode.desc
                    });
                }
            }
        });
	};

	var loadParamList = function(paramList){
		var paramArr = new Array();
		for (var i = 0; i < paramList.length; i++) {
			loadParam(paramArr,paramList[i],0);
		}
    	return paramArr;
	};

	var loadParam = function(paramArr,param,pid){
		var field = {};
		field.fieldName = param.fieldName;
		field.classType = param.classType;
		field.fieldComment = param.fieldComment;
		field.id=param_id;
		var id = field.id;
		field.pid = pid;
		param_id++;
		paramArr.push(field);
		if(!!param.fieldObjs){
			for (var i = 0; i < param.fieldObjs.length; i++) {
				loadParam(paramArr,param.fieldObjs[i],id);
			}
		}else{
			field.isLeaf = "isLeaf";
		}
		
	}
</script>
<script>
(function ($) {
	<#if messageId?? >
		loadMessage(${messageId});
	</#if>	
})(jQuery)
</script>