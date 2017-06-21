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
          <div class="page_con" style="display:none">
            <p style="font-size: 16px;line-height: 36px;">这是一个文档自动化系统，为公司广大的java开发服务。<br>
            我们定时从maven仓库拉取各系统最新上传的facade包，通过类似javadoc的方式生成文档并入库。<br>
            如果各位开发按照<a style="font-size: 16px;line-height: 36px;" href="static1?navType=STATIC_1">注解规范</a>为facade提供清晰的注释，我们也将提供更加清晰的文档。<br>
            这种反向生成文档的方式将有利于提高开发效率。<br>
            通过关系型数据库，我们可以很容易的对文档进行管理、统计，并提供文档的全文搜索和版本对比功能。</p>
            <h4>目前已经开发好的功能:</h4>
            <ul style="font-size: 16px;margin-left: 2%;list-style-type: decimal;">
              <li style="line-height: 30px;">maven同步</li>
              <li style="line-height: 30px;">文档自动生成</li>
              <li style="line-height: 30px;">文档版本对比</li>
              <li style="line-height: 30px;">应用下MQ消息管理</li>
              <li style="line-height: 30px;">应用过滤</li>
            </ul>
            
            <h4>待开发的功能:</h4>
            <ul style="font-size: 16px;margin-left: 2%;list-style-type: decimal;">
              <li style="line-height: 30px;">文档全文搜索</li>
              <li style="line-height: 30px;">文档收藏</li>
            </ul>
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
<script>
    $("body").click();
</script>