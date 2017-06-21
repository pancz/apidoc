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
          <div class="page_con">
              <h3>一、  命名原则</h3>
			  <h4>1.  拼写要准确</h4>
			  <p>接口函数一旦发布就不能改了，要保持兼容性，拼写错误也不能改了，所以要仔细检查拼写，否则会被同行嘲笑很多年。
			  著名悲剧：unix 的 creat</p>
			
			  <h4>2.  不仅是英文单词不要拼错，时态、单复数也不要错。</h4>
			  <p>比如：
			  表示状态的变量或者函数要注意时态，比如 onXxxxChanged 表示xxx已经变化了，isConnecting表示正在连接。
			  正确的时态可以给使用者传递更丰富的信息。</p>
			
			  <h4>3.  函数最好是动宾结构</h4>
			  <p>动宾结构就是 doSomething，这样的函数命名含义明确<br>
			  比如： openFile, allocBuffer, setName<br>
			  如果这个函数的动词宾语就是这个对象本身，那么可以省略掉宾语</p>
			
			  <h4>4.  属性命名最好是定语+名词</h4>
			  <p>比如 fileName, maxSize, textColor</p>
			
			  <h4>5.  不要用生僻单词，这不是秀英语的地方，也不要用汉语拼音</h4>
			  <p>比如：rendezvous，估计大多数人要去查词典才知道什么意思，这个词源自法语，是约会的意思。<br>
			  Symbian OS里有个用它命名的函数，开发Symbian的是英国人，也许人家觉得很平常吧，反正我是查了词典才知道的。</p>
			
			  <h4>6.  不要自己发明缩写</h4>
			  <p>除非是约定俗成已经被广泛使用的缩写，否则老老实实用完整拼写。<br>
			  坏例子: count->cnt, manager->mngr password->pw button->btn，名字长一点没关系的，可读性更重要。</p>
			
			  <h4>7.  保持方法的对称性，有些方法一旦出现就应该是成对的，</h4>
			  <p>比如 有open就要有close，有alloc就要有free，有add就要有remove，这些单词基本是固定搭配的，使用者就很容易理解。<br>
			  如果 open对应clear就有点让人困惑了。</p>
			
			  <h4>8.  站在使用者的角度去思考，API设计也要讲究用户体验。</h4>
			  <p>好的API设计应该是符合直觉，能望文生义的，让使用者能用尽量简洁的代码完成调用。</p>
			
			  <h3>二、  接口命名规范</h3>
			  <h4>前台接口命名： </h4>
			  <p>com.zhaoyun.[应用名].façade.service.[模块名].XxxFacde</p>
			
			  <h4>后台管理接口命名： </h4>
			  <p>com.zhaoyun.[应用名].facade.manager.[模块名].XxxMamagerFacde<br>
			  com.zhaoyun.[应用名].facade.ro.[模块名].XxxRO<br>
			  com.zhaoyun.[应用名].facade.ro.query.[模块名].XxxQueryRO</p>
			
			  <h3>三、  项目中常用的方法命名示例</h3>
			  <table class='sui-table table-bordered'>
			    <thead>
			      <tr>
			        <th>读写方式</th>
			        <th>类型</th>
			        <th>规范</th>
			        <th>示例</th>
			      </tr>
			    </thead>
			    <tbody>
			      <tr>
			        <td rowspan='4'>读接口</td>
			        <td>条件固定的单个查询</td>
			        <td>返回单个对象，采用get前缀，条件前用by，方法名区分出查询条件。RO getXxxByXxx()</td>
			        <td>UserRO getUserById(Long id), UserRO getUserByName(String name)</td>
			      </tr>
			      <tr>
			        <td>条件固定的批量查询</td>
			        <td>返回对象列表，采用get前缀，条件前用by，方法名区分出查询条件，并在方法名中用 S或List体现复数。List&lt;RO&gt; getXxxsByXxx()List&lt;RO&gt; getXxxListByXxx ()</td>
			        <td>List&lt;ProductRO&gt; getStoreProductsByStoreId(Integer storeID)<br>List&lt;ProductRO&gt; getStoreProductListByStoreId(Integer storeID)</td>
			      </tr>
			      <tr>
			        <td>可变条件列表查询</td>
			        <td>返回对象列表，采用query前缀，查询条件写在QueryRo中。List&lt;RO&gt; queryXxx （XxxQueryRO xxxQueryRO） </td>
			        <td>List&lt;RO&gt;  queryUserInfo（UserQueryRO userQueryRO）</td>
			      </tr>
			      <tr>
			        <td>可变条件分页查询</td>
			        <td>返回对象的分页列表（包含页码和总页数），采用pagedQuery前缀，查询条件写在QueryRo（包含页大小，和当前页号）中。PagedDataRO&lt;RO&gt; queryXxx （XxxQueryRO xxxQueryRO）</td>
			        <td>PagedDataRO&lt;RO&gt; pagedQueryUserInfo（UserQueryRO userQueryRO）</td>
			      </tr>
			      <tr>
			        <td rowspan='8'>写接口</td>
			        <td>新加单条数据</td>
			        <td>直接使用add前缀。保存的值放到RO中，返回对应的业务Id。如果出现重复添加应报错。Long addXxx (RO)</td>
			        <td>Long addUser (UserRO userRO)<br>Long addBaseInfoUser (UserBaseInfoRO userBaseInfoRO)</td>
			      </tr>
			      <tr>
			        <td>修改单条数据</td>
			        <td>返回更新是否成功，用update前缀，条件的作件放到By后面。如果记录不存在应该返回false<br>Boolean updateXxxByXxx(RO)</td>
			        <td>Boolean updateUserById(UserRO userRO)</td>
			      </tr>
			      <tr>
			        <td>新加或修改单条数据<br>(不推荐）</td>
			        <td>使用addOrUpdate前缀。保存的值放到RO中，返回对应的业务Id。如果出现重复则根据业务Id修改数据<br>Long addOrUpdateXxxByXxx(RO)</td>
			        <td>Long addOrUpdateUserById (UserRO userRO)</td>
			      </tr>
			      <tr>
			        <td>删除单条数据</td>
			        <td>根据条件删除数据，使用delete前缀，返回是否成功，删除条件放到By后面。如果记录不存在返回true。<br>Boolean deleteXxxByXxx()</td>
			        <td>Boolean deleteUserById(Long userId)</td>
			      </tr>
			      <tr>
			        <td>批量新加多条数据</td>
			        <td>使用batchAdd前缀。保存的值放到List&lt;RO&gt;中，返回是否成功或部分成功的数据。实现内部需要做成事务。<br>全部事务操作，只返回是否成功Boolean batchAddXxx (List<RO>)如果支持部分成功，则返回对象，返回哪些成功，哪些失败 RO batchAddXxx (List<RO>)<br>推荐全部批量操作<br></td>
			        <td>Boolean batchAddUser (List&lt;UserRO&gt; userROs)<br>RO batchAddUser (List<UserRO> userROs)</td>
			      </tr>
			      <tr>
			        <td>批量修改多条数据</td>
			        <td>使用batchUpdate做前缀。保存的值放到List&lt;RO&gt;中，更新条件写到By后面，返回是否成功或部分成功的数据。实现内部需要做成事务。<br>Boolean batchUpdateXxxByXxx(List&lt;RO&gt;)</td>
			        <td>Boolean batchUpdateUserById(List&lt;UserRO&gt; userROs)<br>RO batchUpdateUserById(List<UserRO> userROs)</td>
			      </tr>
			      <tr>
			        <td>批量新加或修改<br>（不推荐）</td>
			        <td>使用batchAddOrUpdate做前缀。保存的值放到List&lt;RO&gt;中，返回是否成功或部分成功的数据。如果数据存在根据业务Id对数据做更新，实现内部需要做成事务。<br>Boolean batchAddOrUpdateXxxByXxx(List&lt;RO&gt;)</td>
			        <td>Boolean batchAddOrUpdateUserById(List&lt;UserRO&gt; userROs)<br>RO batchAddOrUpdateUserById(List<UserRO> userROs)</td>
			      </tr>
			      <tr>
			        <td>批量删除多条数据</td>
			        <td>使用batchDelete做前缀。删除的条件写到By后面。删除的条件值放到参数List&lt;RO&gt;中，返回是否成功或部分成功的数据。实现内部需要做成事务。<br>Boolean batchDeleteXxxByXxx(List)</td>
			        <td>Boolean batchDeleteUserById(List< Long > userIds)<br>RO batchDeleteUserById(List< Long > userIds)</td>
			      </tr>
			    </tbody>
			  </table>
			  <h3>四、  消息命名规范</h3>
			  <table class='sui-table table-bordered'>
			    <thead>
			      <tr>
			        <th>Topic</th>
			        <th>MesageType</th>
			        <th>描述</th>
			        <th>消费方</th>
			      </tr>
			    </thead>
			    <tbody>
			      <tr>
			        <td>TOPIC_ORDER<应用名>_业务/模块/场景均大写，以'_'隔开单词</td>
			        <td>ORDER<模块>_CREATED<具体操作></td>
			        <td>订单生成</td>
			        <td>OMS</td>
			      </tr>
			    </tbody>
			  </table>
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