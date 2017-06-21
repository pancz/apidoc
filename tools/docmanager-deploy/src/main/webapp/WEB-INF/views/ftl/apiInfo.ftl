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
<script id="apiInfoTpl" type="text/x-handlebars-template">
	<div class="sui-container">
		<h3>基本信息</h3>
		<table id="info" class="sui-table table-sideheader table-nobordered ">
			<tbody>
				<tr>
					<th>名称</th>
					<td>{{name}}{{#if chName}}({{chName}}){{/if}}</td>
				</tr>
				<tr>
					<th>全名</th>
					<td>{{fullName}}</td>
				</tr>
				<tr>
					<th>作者</th>
					<td>{{author}}</td>
				</tr>
				<tr>
					<th>时间</th>
					<td>{{date}}</td>
				</tr>
				<tr >
					<th>说明</th>
					<td>{{desc}}</td>
				</tr>
                <tr style="display:none;">
                    <th>调用量信息</th>
                    <td> 月：{{chainInfoVo.monthVisits}} 次&nbsp;&nbsp;周：{{chainInfoVo.weekVisits}} 次&nbsp;&nbsp;日：{{chainInfoVo.dayVisits}} 次</td>
                </tr>
                <tr style="display:none;">
                <th>调用平均响应时间</th>
                <td> 月：{{chainInfoVo.monthVisitsAvgTime}} ms,&nbsp;&nbsp;周：{{chainInfoVo.weekVisitsAvgTime}} ms&nbsp;&nbsp;日：{{chainInfoVo.dayVisitsAvgTime}} ms</td>
                </tr>
			</tbody>
		</table>
	</div>
	<div class="sui-container">
		<h3>入参</h3>
		<table id="paramin" class="sui-table table-bordered treetable">
			<thead>
				<tr>
					<th>参数名</th>
					<th>java类</th>
					<th>说明</th>
				</tr>
			</thead>
			<tbody>
				{{#each paramIn}}
					<tr data-tt-id="paramIn.{{id}}" class="{{isLeaf}} collapsed paramIn" data-tt-parent-id="paramIn.{{pid}}">
						<td>{{fieldName}}</td>
						<td>{{classType}}</td>
						<td>{{fieldComment}}</td>
					</tr>
				{{/each}}
			</tbody>
		</table>
	</div>
	<div class="sui-container">
		<h3>出参</h3>
		<table id="paramout" class="sui-table table-bordered treetable">
			<thead>
				<tr>
					<th>参数名</th>
					<th>java类</th>
					<th>说明</th>
				</tr>
			</thead>
			<tbody>
				{{#each paramOut}}
					<tr data-tt-id="paramOut.{{id}}" class="{{isLeaf}} collapsed paramOut" data-tt-parent-id="paramOut.{{pid}}">
						<td>{{fieldName}}</td>
						<td>{{classType}}</td>
						<td>{{fieldComment}}</td>
					</tr>
				{{/each}}
			</tbody>
		</table>
	</div>
</script>

<!-- api详情-->
<script>
	var param_id;
	var loadApi = function(apiId){
		var myTemplate = Handlebars.compile($("#apiInfoTpl").html());
		$(".sui-modal-backdrop").show();
		$.ajax({
            url: "/api/info",
            type: "POST",
            data: {id:apiId},
            dataType: "json",
            success: function(data){
                $(".sui-modal-backdrop").hide();
                if(data.success && data.stateCode.code == 0){
                	var paramIn = $.parseJSON(data.data.paramIn);
                	param_id = 1;
                	data.data.paramIn = loadParamList(paramIn);
                	var paramOut = [];
                	var paramOutObj = $.parseJSON(data.data.paramOut);
                	if(!!paramOutObj){
                		paramOut.push(paramOutObj);
                	}
                	param_id = 1;
                	data.data.paramOut = loadParamList(paramOut);
                	$("#infoBody").html(myTemplate(data.data));
                	$("#paramin").treetable(
						{ 
							expandable: true,
							initialState:"expanded"
						}
					);
					$("#paramout").treetable(
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
	<#if navId?? >
		loadApi(${navId});
	</#if>	
})(jQuery)
</script>