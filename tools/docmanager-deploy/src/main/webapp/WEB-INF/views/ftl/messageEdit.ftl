<!DOCTYPE html>
<html lang="en">
<#include "/include/head.ftl"/>
<style>
   .input-error{
   		border-color: #ea4a36 !important;
   	}
   .help-inline{
   		display: inline-block;
	    margin-bottom: 0;
	    vertical-align: middle;
	    color: #ea4a36 !important;
   }
</style>
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
<script id="messageEditTpl" type="text/x-handlebars-template">
	<div class="tab_title">
      <p>消息接口</p>
      <div class="buttons">
        <button id="save" class="sui-btn btn-success">保存</button>
      </div>
    </div>
    <div class="special_table">
      <table class="sui-table table-zebra">
        <thead>
          <tr>
            <th>消息TOPIC</th>
			<th>消息类型</th>
			<th>消息对象类型</th>
			<th>消息说明</th>
			<th>消息产生条件</th>
            <th><i class="sui-icon icon-tb-add"></i></th>
          </tr>
        </thead>
        <tbody id="messageList">
        	{{#each data}}
				<tr data-id="{{id}}" appId="{{appId}}">
					<td>
						<select>
							{{#each ../mqMessage.topicList}}
								<option value="{{this}}" {{#equal ../topic this}} selected {{/equal}}>{{this}}</option>
							{{/each}}
						</select>
					</td>
	                <td>
	                	<select>
	                		{{#each ../mqMessage.messageTypeList}}
								<option value="{{this}}" {{#equal ../messageType this}} selected {{/equal}}>{{this}}</option>
							{{/each}}
	                	</select>
	                </td>
	                <td>
	                	<input class="classTypeInput" type="text" value="{{messageObjectName}}" data-rules="validateClassName">
	                	<span class="help-inline" style="display:none;">请填写该应用下的类名</span>
	                </td>
	                <td><input type="text" value="{{desc}}"></td>
	                <td><input type="text" value="{{cause}}"></td>
	                <td><i class="sui-icon icon-tb-close"></i><input class="jsonstr" type="hidden" value="{{messageObjectJson}}"></td>
				</tr>
			{{/each}}
        </tbody>
      </table>
    </div>
</script>
<script id="messageTrTpl" type="text/x-handlebars-template">
	<tr appId="{{appId}}">
		<td>
			<select>
				{{#each mqMessage.topicList}}
					<option value="{{this}}" >{{this}}</option>
				{{/each}}
			</select>
		</td>
        <td>
        	<select>
        		{{#each mqMessage.messageTypeList}}
					<option value="{{this}}" >{{this}}</option>
				{{/each}}
        	</select>
        </td>
        <td>
        	<input class="classTypeInput input-error" type="text" data-rules="validateClassName">
        	<span class="help-inline" style="display:none;">请填写该应用下的类名</span>
        </td>
        <td><input type="text" value=""></td>
        <td><input type="text" value=""></td>
        <td><i class="sui-icon icon-tb-close"></i><input class="jsonstr" type="hidden"></td>
	</tr>
</script>
<!-- 特殊表格 -->
<script>
	
	<#if navId?? >
		var appId = ${navId};
	</#if>
	<#if navType?? >
		var navType = "${navType}";
	</#if>
	var trTemplate = Handlebars.compile($("#messageTrTpl").html());
	var trData = {};
	trData.appId = appId;
	
	Handlebars.registerHelper('equal',function(v1 , v2,options){  
       if(v1 == v2){  
           return options.fn(this);  
       }  
       return options.inverse(this);  
   });
   
	//特殊表格处理
    var specialTabelFun = {
      deleteArr: [],//删除的行的id数组
      saveArr: [],
      init: function(){
        //删除行
        $('.special_table').delegate('.icon-tb-close','click',function(){
          var thisId = $(this).closest('tr').attr('data-id');
          if(!!thisId){
          	specialTabelFun.deleteArr.push(thisId);
          }
          $(this).closest('tr').remove();
          console.log(specialTabelFun.deleteArr);
        });
        //增加tr
        $('.special_table').delegate('.icon-tb-add','click',function(){
          $("#messageList").append(trTemplate(trData));
          $(".classTypeSelect").change();
          $("select").select2({
			width:"200px"
		  });
		  $('.classTypeInput').autocomplete({
			  lookup: trData.messageClassTypeList,
			  minChars: 0,
			  onSelect: function (suggestion) {
		         $(this).change();
		      }
		  });
        });
      },
      mergeData: function(){//整理成object方法
      	specialTabelFun.saveArr = [];
        $('.special_table tbody tr').each(function(ind){
          specialTabelFun.saveArr[ind] = {};
          if(!!$(this).attr('data-id')){
	          specialTabelFun.saveArr[ind].id = parseInt($(this).attr('data-id'));
          }
          specialTabelFun.saveArr[ind].appId = parseInt($(this).attr('appId'));
          specialTabelFun.saveArr[ind].topic = $(this).find('td').eq(0).find('select option:selected').text();
          specialTabelFun.saveArr[ind].messageType = $(this).find('td').eq(1).find('select option:selected').text();
          specialTabelFun.saveArr[ind].messageObjectName = $(this).find('td').eq(2).find('input').val();
          specialTabelFun.saveArr[ind].desc = $(this).find('td').eq(3).find('input').val();
          specialTabelFun.saveArr[ind].cause = $(this).find('td').eq(4).find('input').val();
          specialTabelFun.saveArr[ind].messageObjectJson = $(this).find('td').eq(5).find('input').val();
        });
        return specialTabelFun.saveArr;
      }
    };
</script>
<script>
	var loadMqMessage = function(){
		$.ajax({
            url: "/message/mqMessage",
            type: "POST",
            async:false,
            dataType: "json",
            success: function(data){
                if(data.success && data.stateCode.code == 0){
                	trData.mqMessage = data.data;
                	trData.messageEntityMap = {};
                	trData.messageClassTypeList = [];
                	for(var i = 0 ; i < trData.mqMessage.messageEntityList.length; i++){
                		trData.messageEntityMap[trData.mqMessage.messageEntityList[i].classType]=trData.mqMessage.messageEntityList[i];
                		trData.messageClassTypeList.push(trData.mqMessage.messageEntityList[i].classType);
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
	
	var loadMessageEdit = function(appId){
		$(".menu").find(".li_selected").click();
		var myTemplate = Handlebars.compile($("#messageEditTpl").html());
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
                	data.mqMessage = trData.mqMessage;
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
		var appId = ${navId};
		loadMqMessage();
		loadMessageEdit(${navId});
	</#if>
	
	specialTabelFun.init();
	
	$("#infoBody").on("click","#save",function(){
		var appMessageSaveParam = {};
		appMessageSaveParam.updateList = specialTabelFun.mergeData();
		appMessageSaveParam.deleteIds = specialTabelFun.deleteArr;
		$(".sui-modal-backdrop").show();
		$.ajax({
            url: "/message/save",
            type: "POST",
            data: JSON.stringify(appMessageSaveParam),
            async:false,
            dataType: "json",
            contentType:"application/json",
            success: function(data){
                $(".sui-modal-backdrop").hide();
                if(data.success && data.stateCode.code == 0){
                	window.location.href = "messageList?navId="+appId+"&navType="+navType;
                }else {
                    $.alert({
                        width:'small',
                        body:data.stateCode.desc
                    });
                }
            }
        });
	});
	
	$("#infoBody").on("change",".classTypeInput",function(){
		var className = $(this).val();
		var entity = trData.messageEntityMap[className];
		var hiddenInput = $(this).closest('tr').find('.jsonstr');
		var nowInput = $(this);
		var nowSpan = $(this).closest('td').find('span');
		if(!entity){
			$.ajax({
	            url: "/message/parseClass",
	            type: "POST",
	            data: {appId:appId,className:className},
	            dataType: "json",
	            success: function(data){
	                $(".sui-modal-backdrop").hide();
	                if(data.success && data.stateCode.code == 0){
	                	nowInput.removeClass("input-error");
	                	nowSpan.hide();
	                	hiddenInput.val(JSON.stringify(data.data));
	                }else {
	                	nowInput.addClass("input-error");
	                	nowSpan.show();
	                	hiddenInput.val("");
	                }
	            }
	        });
		}else{
			nowInput.removeClass("input-error");
        	nowSpan.hide();
			hiddenInput.val(JSON.stringify(entity));
		}
	});
	
	$("select").select2({
		width:"200px"
	});
	
	$('.classTypeInput').autocomplete({
	    lookup: trData.messageClassTypeList,
	    minChars: 0,
	    onSelect: function (suggestion) {
          $(this).change();
      }
    });
	
})(jQuery)
</script>