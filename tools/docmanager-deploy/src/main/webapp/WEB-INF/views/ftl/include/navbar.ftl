<div class="easy-tree" style="display: none;">
	<ul>
	   <!-- <li  navType="ROOT_STATIC" class="parent_li <#if (navbar.navType)! == "ROOT_STATIC" >li_selected</#if>" >文档说明
	    	<ul>
	    		<li  navType="STATIC_0" <#if (navbar.navType)! == "STATIC_0" >class="li_selected"</#if> >接口规范</li>
	    		<li  navType="STATIC_1" <#if (navbar.navType)! == "STATIC_1" >class="li_selected"</#if> >注解规范</li>
	    	</ul>
	    </li> -->
	    <li  navType="ROOT_APP" class="parent_li filter <#if (navbar.navType)! == "ROOT_APP" >li_selected</#if>" >应用
	    	<#if (navbar.apps)?? >
		    	<ul>
			    	<#list (navbar.apps)! as app>
			    		<#if (navbar.appId)! == (app.id)! >
			    			<li navType="APP" navId="${(app.id)!}" class="parent_li <#if (navbar.navType)! == "APP" >li_selected</#if>" >${(app.name)!}
								<ul>
						    		<li  navType="ROOT_API" navId="${(app.id)!}"  class="parent_li <#if (navbar.navType)! == "ROOT_API" >li_selected</#if>" >API接口
						    			<#if (navbar.services)?? || (navbar.deprecatedServices)??>
						    				<ul>
			    								<#list (navbar.services)! as service>
			    									<#if (navbar.serviceId)! == (service.id)! >
				    									<li navType="SERVICE" navId="${(service.id)!}"  class="parent_li <#if (navbar.navType)! == "SERVICE" >li_selected</#if>" >${(service.name)!}
			    											<#if (navbar.apis)?? || (navbar.deprecatedApis)??>
			    												<ul>
				    												<#list (navbar.apis)! as api>
				    													<li navType="API" navId="${(api.id)!}" class="api <#if (navbar.navType)! == "API" && (navbar.apiId)! == (api.id)! >li_selected</#if>" >${(api.name)!}
							    										</li>
				    												</#list>
				    												<#if (navbar.deprecatedApis)??>
								    									<li navType="DEPRECATED_API_LIST" navId="${(service.id)!}"  class="parent_li <#if (navbar.navType)! == "DEPRECATED_API_LIST" >li_selected</#if>" >过期的API
									    									<ul>
										    									<#list (navbar.deprecatedApis)! as dpdapi>
										    										<li navType="DEPRECATED_API" navId="${(dpdapi.id)!}" class="api <#if (navbar.navType)! == "DEPRECATED_API" && (navbar.apiId)! == (dpdapi.id)! >li_selected</#if>" >${(dpdapi.name)!}
								    												</li>
										    									</#list>
									    									</ul>
									    								</li>
								    								</#if>
							    								</ul>
			    											</#if>
				    									</li>
			    									<#else>
														<li navType="SERVICE"  class="parent_li" navId="${(service.id)!}" >${(service.name)!}</li>
										    		</#if>
			    								</#list>
			    								
			    								<#if (navbar.deprecatedServices)??>
			    									<li navType="DEPRECATED_SERVICE_LIST" navId="${(app.id)!}"  class="parent_li <#if (navbar.navType)! == "DEPRECATED_SERVICE_LIST" >li_selected</#if>" >过期的服务
				    									<ul>
					    									<#list (navbar.deprecatedServices)! as dpdService>
					    										<#if (navbar.serviceId)! == (dpdService.id)! >
						    										<li navType="DEPRECATED_SERVICE" navId="${(dpdService.id)!}"  class="parent_li <#if (navbar.navType)! == "DEPRECATED_SERVICE" >li_selected</#if>" >${(dpdService.name)!}
						    											<#if (navbar.deprecatedApis)??>
									    									<ul>
										    									<#list (navbar.deprecatedApis)! as dpdapi>
										    										<li navType="DEPRECATED_SERVICE_API" navId="${(dpdapi.id)!}" class="api <#if (navbar.navType)! == "DEPRECATED_SERVICE_API" && (navbar.apiId)! == (dpdapi.id)! >li_selected</#if>" >${(dpdapi.name)!}
								    												</li>
										    									</#list>
									    									</ul>
									    								</#if>
				    												</li>
			    												<#else>
																	<li navType="DEPRECATED_SERVICE"  class="parent_li" navId="${(dpdService.id)!}" >${(dpdService.name)!}</li>
													    		</#if>
					    									</#list>
				    									</ul>
				    								</li>
			    								</#if>
			    							</ul>
						    			</#if>
						    		</li>
						    		<li navType="MESSAGE" navId="${(app.id)!}" <#if (navbar.navType)! == "MESSAGE" >class="li_selected"</#if> style="display:none" >消息接口</li>
						    	</ul>
							</li>
						<#else>
							<li navType="APP" class="parent_li" navId="${(app.id)!}" >${(app.name)!}
								
							</li>
			    		</#if>
						
					</#list>
				</ul>
	    	</#if>
	    </li>
	</ul>
</div>
<script id="appTpl" type="text/x-handlebars-template">
	<ul>
	{{#each data}}
		<li class="parent_li" navType="APP" navId="{{id}}"><span style="padding-left:{{../len}}px"><span class="glyphicon"></span><a style="max-width:{{../aWidth}}px" href="javascript: void(0);" title="选择">{{name}}</a></span>
			
		</li>
	{{/each}}
	</ul>
</script>
<script id="appSubTpl" type="text/x-handlebars-template">
	<ul>
		<li  class="parent_li" navType="ROOT_API" navId="{{id}}"><span style="padding-left:{{len}}px"><span class="glyphicon glyphicon-folder-close"></span><a style="max-width:{{aWidth}}px" href="javascript: void(0);" title="选择">API接口</a></span></li>
		<li  class="parent_li" navType="MESSAGE" navId="{{id}}"><span style="padding-left:{{len}}px"><span class="glyphicon"></span><a style="max-width:{{aWidth}}px" href="javascript: void(0);" title="选择">消息接口</a></span></li>
	</ul>	
</script>
<script id="serviceTpl" type="text/x-handlebars-template">
	<ul>
	{{#each data}}
		<li class="parent_li" navType="SERVICE" navId="{{id}}"><span style="padding-left:{{../len}}px"><span class="glyphicon glyphicon-folder-close"></span><a style="max-width:{{../aWidth}}px" href="javascript: void(0);" title="选择">{{name}}</a></span>
		</li>
	{{/each}}
	</ul>
</script>
<script id="dptServiceTpl" type="text/x-handlebars-template">
	<li  class="parent_li" navType="DEPRECATED_SERVICE_LIST" navId="{{appId}}"><span style="padding-left:{{len}}px"><span class="glyphicon glyphicon-folder-close"></span><a style="max-width:{{aWidth}}px" href="javascript: void(0);" title="选择">过期的服务</a></span></li>
</script>
<script id="dptServiceSubTpl" type="text/x-handlebars-template">
	<ul>
	{{#each data}}
		<li class="parent_li" navType="DEPRECATED_SERVICE" navId="{{id}}"><span style="padding-left:{{../len}}px"><span class="glyphicon glyphicon-folder-close"></span><a style="max-width:{{../aWidth}}px" href="javascript: void(0);" title="选择">{{name}}</a></span>
		</li>
	{{/each}}
	</ul>
</script>
<script id="apiTpl" type="text/x-handlebars-template">
	<ul>
	{{#each data}}
		<li  navType="API" navId="{{id}}"><span style="padding-left:{{../len}}px"><a style="max-width:{{../aWidth}}px" href="javascript: void(0);" title="选择">{{name}}</a></span>
		</li>
	{{/each}}
	</ul>
</script>
<script id="dptApiTpl" type="text/x-handlebars-template">
	<li  class="parent_li" navType="DEPRECATED_API_LIST" navId="{{serviceId}}"><span style="padding-left:{{len}}px"><span class="glyphicon glyphicon-folder-close"></span><a style="max-width:{{aWidth}}px" href="javascript: void(0);" title="选择">过期的API</a></span></li>
</script>
<script id="dptApiSubTpl" type="text/x-handlebars-template">
	<ul>
	{{#each data}}
		<li  navType="DEPRECATED_API" navId="{{id}}"><span style="padding-left:{{../len}}px"><a style="max-width:{{../aWidth}}px" href="javascript: void(0);" title="选择">{{name}}</a></span>
		</li>
	{{/each}}
	</ul>
</script>
<script id="serviceDptApiSubTpl" type="text/x-handlebars-template">
	<ul>
	{{#each data}}
		<li  navType="DEPRECATED_SERVICE_API" navId="{{id}}"><span style="padding-left:{{../len}}px"><a style="max-width:{{../aWidth}}px" href="javascript: void(0);" title="选择">{{name}}</a></span>
		</li>
	{{/each}}
	</ul>
</script>