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
<script id="diffTpl" type="text/x-handlebars-template">
	<div class="sui-container">
		<h1>
			对比概要({{appName}} : {{version1}} -> {{version2}})
			<a  href="javascript:selectText('infoBody');" class="sui-btn btn-small btn-info">全选</a>
			<a  href="/api/pom/createDiffExcel?appId=${navId}&version1=${version1}&version2=${version2}" class="sui-btn btn-small btn-info">导出</a>
		</h1>
		<div class="sui-container">
			<table class="sui-table table-bordered diffTable">
				<tbody>
					<tr>
						<th rowspan={{showAddSize}} style="width:125px;">增加的API(共:{{addSize}})</th>
					</tr>		
					{{#each addApiList}}
					    <tr>
							<td>
								<div><a href="${"#"}{{serviceName}}.{{apiName}}">{{serviceName}}.{{apiName}}</a></div>
							</td>
							<td>
								<div><span>{{desc2}}</span></div>
							</td>
						</tr>
					{{/each}}
					<tr>
						<th rowspan={{showChangeSize}} style="width:125px;">修改的API(共:{{changeSize}})</th>
					</tr>		
					{{#each changeApiList}}
					    <tr>
							<td>
								<div><a href="${"#"}{{serviceName}}.{{apiName}}">{{serviceName}}.{{apiName}}</a></div>
							</td>
							<td>
								<div><span>{{desc2}}</span></div>
							</td>
						</tr>
					{{/each}}
					<tr>
						<th rowspan={{showDeleteSize}} style="width:125px;">删除的API(共:{{deleteSize}})</th>
					</tr>		
					{{#each deleteApiList}}
					    <tr>
							<td>
								<div><a href="${"#"}{{serviceName}}.{{apiName}}">{{serviceName}}.{{apiName}}</a></div>
							</td>
							<td>
								<div><span>{{desc1}}</span></div>
							</td>
						</tr>
					{{/each}}
				</tbody>
			</table>
		</div>
	</div>
	<div class="sui-container">
		<h1>增加的API</h1>
		<hr/>
		<div class="sui-container">
			{{#each addApiList}}
				<h3 id="{{serviceName}}.{{apiName}}">{{serviceName}}.{{apiName}}({{desc2}})</h3>
				<table class="sui-table table-bordered treetable diffTable">
					<caption>入参</caption>
					<thead>
						<tr style="text-align:center;">
							<th colspan="3" class="addTr versionTh">{{../version2}}({{desc2}})</th>
						</tr>
						<tr>
							<th>参数名</th>
							<th>java类</th>
							<th>说明</th>
						</tr>
					</thead>
					<tbody>
						{{#each paramIn}}
							<tr data-tt-id="diffParamIn.{{id}}" class="{{isLeaf}} collapsed addTr" data-tt-parent-id="diffParamIn.{{pid}}" >
								<td>{{name}}</td>
								<td>{{type2}}</td>
								<td>{{desc2}}</td>
							</tr>
						{{/each}}
					</tbody>
				</table>
				<table class="sui-table table-bordered treetable diffTable">
					<caption>出参</caption>
					<thead>
						<tr style="text-align:center;">
							<th colspan="3" class="addTr versionTh">{{../version2}}({{desc2}})</th>
						</tr>
						<tr>
							<th>参数名</th>
							<th>java类</th>
							<th>说明</th>
						</tr>
					</thead>
					<tbody>
						{{#each paramOut}}
							<tr data-tt-id="diffParamOut.{{id}}" class="{{isLeaf}} collapsed addTr" data-tt-parent-id="diffParamOut.{{pid}}" >
								<td>{{name}}</td>
								<td>{{type2}}</td>
								<td>{{desc2}}</td>
							</tr>
						{{/each}}
					</tbody>
				</table>
				<hr/>
			{{/each}}
		</div>
	</div>
	<div class="sui-container">
		<h1>修改的API</h1>
		<hr/>
		<div class="sui-container">
			{{#each changeApiList}}
				<h3 id="{{serviceName}}.{{apiName}}">{{serviceName}}.{{apiName}}({{desc2}})</h3>
				<table class="sui-table table-bordered treetable diffTable changeApiTable">
					<caption>入参</caption>
					<thead>
						<tr class="changeApiTr">
							<th></th>
							<th colspan="2" class="versionTh">{{../version1}}(<span class="diffDescTh">{{desc1}}</span>)</th>
							<th colspan="2" class="versionTh">{{../version2}}(<span class="diffDescTh">{{desc2}}</span>)</th>
						</tr>
						<tr>
							<th>参数名</th>
							<th>java类</th>
							<th>说明</th>
							<th>java类</th>
							<th>说明</th>
						</tr>
					</thead>
					<tbody>
						{{#each paramIn}}
							<tr data-tt-id="diffParamIn.{{id}}" class="{{isLeaf}} collapsed changeApiTr" data-tt-parent-id="diffParamIn.{{pid}}">
								<td class="diffNameTd">{{name}}</td>
								<td class="diffTypeTd">{{type1}}</td>
								<td class="diffDescTd">{{desc1}}</td>
								<td class="diffTypeTd">{{type2}}</td>
								<td class="diffDescTd">{{desc2}}</td>
							</tr>
						{{/each}}
					</tbody>
				</table>
				<table class="sui-table table-bordered treetable diffTable changeApiTable">
					<caption>出参</caption>
					<thead>
						<tr class="changeApiTr">
							<th></th>
							<th colspan="2" class="versionTh">{{../version1}}(<span class="diffDescTh">{{desc1}}</span>)</th>
							<th colspan="2" class="versionTh">{{../version2}}(<span class="diffDescTh">{{desc2}}</span>)</th>
						</tr>
						<tr>
							<th>参数名</th>
							<th>java类</th>
							<th>说明</th>
							<th>java类</th>
							<th>说明</th>
						</tr>
					</thead>
					<tbody>
						{{#each paramOut}}
							<tr data-tt-id="diffParamOut.{{id}}" class="{{isLeaf}} collapsed changeApiTr" data-tt-parent-id="diffParamOut.{{pid}}">
								<td class="diffNameTd">{{name}}</td>
								<td class="diffTypeTd">{{type1}}</td>
								<td class="diffDescTd">{{desc1}}</td>
								<td class="diffTypeTd">{{type2}}</td>
								<td class="diffDescTd">{{desc2}}</td>
							</tr>
						{{/each}}
					</tbody>
				</table>
				<hr/>
			{{/each}}
		</div>
	</div>
	<div class="sui-container">
		<h1>删除的API</h1>
		<hr/>
		<div class="sui-container">
			{{#each deleteApiList}}
				<h3 id="{{serviceName}}.{{apiName}}">{{serviceName}}.{{apiName}}({{desc1}})</h3>
				<table class="sui-table table-bordered treetable diffTable" >
					<caption>入参</caption>
					<thead>
						<tr style="text-align:center;">
							<th colspan="3" class="deleteTr versionTh">{{../version1}}({{desc1}})</th>
						</tr>
						<tr>
							<th>参数名</th>
							<th>java类</th>
							<th>说明</th>
						</tr>
					</thead>
					<tbody>
						{{#each paramIn}}
							<tr data-tt-id="diffParamIn.{{id}}" class="{{isLeaf}} collapsed deleteTr" data-tt-parent-id="diffParamIn.{{pid}}" >
								<td>{{name}}</td>
								<td>{{type1}}</td>
								<td>{{desc1}}</td>
							</tr>
						{{/each}}
					</tbody>
				</table>
				<table class="sui-table table-bordered treetable diffTable" > 
					<caption>出参</caption>
					<thead>
						<tr style="text-align:center;">
							<th colspan="3" class="deleteTr versionTh">{{../version1}}({{desc1}})</th>
						</tr>
						<tr>
							<th>参数名</th>
							<th>java类</th>
							<th>说明</th>
						</tr>
					</thead>
					<tbody>
						{{#each paramOut}}
							<tr data-tt-id="diffParamOut.{{id}}" class="{{isLeaf}} collapsed deleteTr" data-tt-parent-id="diffParamOut.{{pid}}" >
								<td>{{name}}</td>
								<td>{{type1}}</td>
								<td>{{desc1}}</td>
							</tr>
						{{/each}}
					</tbody>
				</table>
				<hr/>
			{{/each}}
		</div>
	</div>
</script>

<!-- 对比-->
<script>
	var diff_param_id;
	var loadDiff = function(appId,version1,version2){
		$(".menu").find(".li_selected").click();
		var myTemplate = Handlebars.compile($("#diffTpl").html());
		$(".sui-modal-backdrop").show();
		$.ajax({
            url: "/api/pom/diff",
            type: "POST",
            data: {appId:appId,version1:version1,version2:version2},
            dataType: "json",
            success: function(data){
                $(".sui-modal-backdrop").hide();
                if(data.success && data.stateCode.code == 0){
                	var addApiList = data.data.addApiList;
                	var changeApiList = data.data.changeApiList;
                	var deleteApiList = data.data.deleteApiList;

                	loadDiffApiList(addApiList);
                	loadDiffApiList(changeApiList);
                	loadDiffApiList(deleteApiList);
                	data.data.addSize=addApiList.length;
                	data.data.showAddSize = addApiList.length+1;
                	data.data.changeSize=changeApiList.length;
                	data.data.showChangeSize=changeApiList.length+1;
                	data.data.deleteSize=deleteApiList.length;
                	data.data.showDeleteSize=deleteApiList.length+1;

                	$("#infoBody").html(myTemplate(data.data));
                	$(".diffTable").treetable(
						{ 
							expandable: true,
							initialState:"expanded"
						}
					);
					$(".changeApiTable").each(function(){
					  	var changeApiTrs = $(this).find(".changeApiTr");
					  	changeApiTrs.each(function(){
					  		var diffNameTd  = $(this).find(".diffNameTd");
					  		var diffTypeTds = $(this).find(".diffTypeTd");
					  		var diffDescTds = $(this).find(".diffDescTd");
					  		var diffDescThs =  $(this).find(".diffDescTh");
					  		if(diffTypeTds.size() == 2){
					  			if(diffTypeTds[0].innerHTML == diffTypeTds[1].innerHTML){
					  				diffTypeTds.addClass("defaultColor");
					  				diffNameTd.addClass("defaultColor");
					  			}else if(diffTypeTds[0].innerHTML == ""){
					  				diffTypeTds.addClass("addTr");
					  				diffNameTd.addClass("addTr");
					  			}else if(diffTypeTds[1].innerHTML == ""){
									diffTypeTds.addClass("deleteTr");
									diffNameTd.addClass("deleteTr");
					  			}else{
					  				diffTypeTds.addClass("changeTr");
					  				diffNameTd.addClass("changeTr");
					  			}
					  		}
					  		if(diffDescTds.size() == 2){
					  			if(diffDescTds[0].innerHTML == diffDescTds[1].innerHTML){
					  				diffDescTds.addClass("defaultColor");
					  			}else if(diffDescTds[0].innerHTML == ""){
					  				diffDescTds.addClass("addTr");
					  			}else if(diffDescTds[1].innerHTML == ""){
									diffDescTds.addClass("deleteTr");
					  			}else{
					  				diffDescTds.addClass("changeTr");
					  			}
					  		}
					  		if(diffDescThs.size() == 2){
					  			if(diffDescThs[0].innerHTML == diffDescThs[1].innerHTML){
					  				diffDescThs.addClass("defaultColor");
					  			}else if(diffDescThs[0].innerHTML == ""){
					  				diffDescThs.addClass("addTr");
					  			}else if(diffDescThs[1].innerHTML == ""){
									diffDescThs.addClass("deleteTr");
					  			}else{
					  				diffDescThs.addClass("changeTr");
					  			}
					  		}
					  	});
					});
					
                }else {
                    $.alert({
                        width:'small',
                        body:data.stateCode.desc
                    });
                }

            }
        });
	};

	var loadDiffApiList = function(apiList){
		for (var i = 0; i < apiList.length; i++) {
    		var paramIn = apiList[i].paramIn;
    		diff_param_id = 1;
        	apiList[i].paramIn = loadDiffParamList(paramIn);
        	var paramOut = [];
        	var paramOutObj = apiList[i].paramOut;
        	if(!!paramOutObj){
        		paramOut.push(paramOutObj);
        	}
        	diff_param_id = 1;
        	apiList[i].paramOut = loadDiffParamList(paramOut);
    	}
	};

	var loadDiffParamList = function(paramList){
		var paramArr = new Array();
		for (var i = 0; i < paramList.length; i++) {
			loadDiffParam(paramArr,paramList[i],0);
		}
    	return paramArr;
	};

	var loadDiffParam = function(paramArr,param,pid){
		var field = {};
		field.name = param.name;
		field.type1 = param.type1;
		field.type2 = param.type2;
		field.desc1 = param.desc1;
		field.desc2 = param.desc2;
		if(param.diffType == 1){
			field.add = true;
		}else if(param.diffType == 2){
			field.delete = true;
		}else if(param.diffType == 3){
			field.update = true;
		}else{
			field.same = true;
		}
		field.id=diff_param_id;
		var id = field.id;
		field.pid = pid;
		diff_param_id++;
		paramArr.push(field);
		if(!!param.childList){
			for (var i = 0; i < param.childList.length; i++) {
				loadDiffParam(paramArr,param.childList[i],id);
			}
		}else{
			field.isLeaf = "isLeaf";
		}
		
	};
	function selectText(element) {
	    var text = document.getElementById(element);
	    if (document.body.createTextRange) {
	        var range = document.body.createTextRange();
	        range.moveToElementText(text);
	        range.select();
	    } else if (window.getSelection) {
	        var selection = window.getSelection();
	        var range = document.createRange();
	        range.selectNodeContents(text);
	        selection.removeAllRanges();
	        selection.addRange(range);
	    }
	  }
</script>
<script>
(function ($) {
	<#if navId?? && version1?? && version2?? >
		loadDiff(${navId},"${version1}","${version2}");
	</#if>	
	
})(jQuery)
</script>