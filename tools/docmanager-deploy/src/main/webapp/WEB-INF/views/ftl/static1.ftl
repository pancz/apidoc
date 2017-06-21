<!DOCTYPE html>
<html lang="en">
<#include "/include/head.ftl"/>
<style type="text/css">
    .word_manager img{display:block;width: 80%;margin: auto;}
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
          <div class="page_con">
            <div class="word_manager">
			    <h2>文档管理系统，会根据java注解自动生成文档，也为了其它开发者从jar包中的注解一目了然的知道接口的意思，请各位开发自觉按下面规范写注解。</h2>
			    <h3>详情请下载附件<a href="resource/interfaceRule.zip">接口规范.zip</a></h3>
			    <h3>一、接口注解</h3>
			    <img src="static/img/pic1.png">
			    <h3>二、RO注解</h3>
			    <img src="static/img/pic2.png">
			    <h3>三、Eclipse配置注解模板</h3>
			    <img src="static/img/pic3.png">
			    <img src="static/img/pic4.png">
			    <h3>四、Idea配置注解模板</h3>
			    <h4>1.注解模板配置（EclipseCodeFormatter插件）</h4>
			    <p>1）下载插件文件<br>http://plugins.jetbrains.com/plugin/?idea&id=6546中下载 Eclipse Code Formatter插件（http://plugins.jetbrains.com/files/6546/27170/EclipseFormatter.zip）<br>2）idea中安装插件</p>
			    <img src="static/img/pic5.png">
			    <p>3）导入配置文件</p>
			    <img src="static/img/pic6.png">
			    <h4>2.注解格式化模板（Jindent插件）</h4>
			    <p>2.1 安装Jindent插件</p>
			    <img src="static/img/pic7.png">
			    <p>2.2 导入配置文件</p>
			    <img src="static/img/pic8.png">
			    <p>2.3 手动添加注解</p>
			    <img src="static/img/pic9.png">
			    <h4>3.javadoc忽略校验自定义tag配置</h4>
			    <img src="static/img/pic10.png">
			  </div>
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
    
</script>