/**
 * Easy Tree 简易的jquery树插件，将一个无序列表转化成树
 * 支持单选、新增、编辑、删除
 * @Copyright yuez.me 2014
 * @Author yuez
 * @Version 0.1
 */
(function ($) {
	function addCookie(name, value, options) {
	      if (arguments.length > 1 && name != null) {
	        if (options == null) {
	          options = {};
	        }
	        if (value == null) {
	          options.expires = -1;
	        }
	        if (typeof options.expires == "number") {
	          var time = options.expires;
	          var expires = options.expires = new Date();
	          expires.setTime(expires.getTime() + time * 1000);
	        }
	        if (options.path == null) {
	          options.path = "/";
	        }
	        if (options.domain == null) {
	          options.domain = location.hostname;
	        }
	        // document.cookie = encodeURIComponent(String(name)) + "=" + encodeURIComponent(String(value)) + (options.expires != null ? "; expires=" + options.expires.toUTCString() : "") + ("; path=/") + ("; domain=.zhaoyun.com") + (options.secure != null ? "; secure" : "");
	        document.cookie =encodeURIComponent(String(name)) + "=" + encodeURIComponent(String(value)) + (options.expires && options.expires.toUTCString() != null ? "; expires=" + options.expires.toUTCString() : "") + ("; path=/") + ("; domain="+options.domain);
	      }
	    }

	    function getCookie(name) {
	      if (name != null) {
	        var value = new RegExp("(?:^|; )" + encodeURIComponent(String(name)) + "=([^;]*)").exec(document.cookie);
	        return value ? decodeURIComponent(value[1]) : null;
	      }
	    }

	    function removeCookie(name, options) {
	      addCookie(name, null, options);
	    }

	    var splitStr = function(str){
	        if(str != undefined){
	            str = str.split(',');
	            var returnStr = [];
	            for(var i = 0; i < str.length; i++){
	                returnStr = returnStr.concat(str[i].split('，'));
	            }
	            return returnStr;
	        }
	    }

	    var filterFun =function(str){
	        if(str == undefined) {
	            str = getCookie("filterName");
	            $('input.filter').val(str);
	        }else addCookie("filterName",str,{expires:7*24*36000});
	        str = splitStr(str);
	        $('.parent_li.filter').find('ul:first li').each(function(){
	            if($(this).parents('li').length == 1){
	                if(str == null) $(this).show();
	                else{
	                    var lock = false;
	                    for(var i =0;i<str.length;i++){
	                        if($(this).find(' > span > a:first').text().toUpperCase().indexOf(str[i].toUpperCase()) != -1){
	                            lock = true;
	                        }
	                    }
	                    if(lock){
	                        $(this).show()
	                    }else{
	                        $(this).hide();
	                    }
	                }
	            }
	        });
	        if(!$('input.filter').closest('li').hasClass('delegate')){
	            $('input.filter').closest('li').click();
	        }
	    };
	    
    $.fn.EasyTree = function (options) {
        var defaults = {
            selectable: true,
            deletable: false,
            editable: false,
            addable: false,
            i18n: {
                deleteNull: '请选择要删除的项。',
                deleteConfirmation: '您确认要执行删除操作吗？',
                confirmButtonLabel: '确认',
                editNull: '请选择要编辑的项。',
                editMultiple: '一次只能编辑一项',
                addMultiple: '请选择一项添加',
                collapseTip: '收起分支',
                expandTip: '展开分支',
                selectTip: '选择',
                unselectTip: '取消选择',
                editTip: '编辑',
                addTip: '添加',
                deleteTip: '删除',
                cancelButtonLabel: '取消'
            },
            open: false,
            callback: null
        };

        var warningAlert = $('<div class="alert alert-warning alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><strong></strong><span class="alert-content"></span> </div> ');
        var dangerAlert = $('<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><strong></strong><span class="alert-content"></span> </div> ');

        var createInput = $('<div class="input-group"><input type="text" class="form-control"><span class="input-group-btn"><button type="button" class="btn btn-default btn-success confirm"></button> </span><span class="input-group-btn"><button type="button" class="btn btn-default cancel"></button> </span> </div> ');

        options = $.extend(defaults, options);
        
        this.each(function () {
            var easyTree = $(this);
            var outEasyTree = $('.easy-tree');
            $.each($(easyTree).find('ul > li'), function() {
                var text;
                var len = $(this).parents('li').length * 20 + 10;
                var aWidth = 200 - len;
                if($(this).is('li:has(ul)')|| $(this).hasClass('parent_li')) {
                    var children = $(this).find(' > ul');
                    $(children).remove();
                    text = $(this).text();
                    $(this).html('<span style="padding-left:'+ len +'px"><span class="glyphicon"></span><a style="max-width:' + aWidth + 'px" href="javascript: void(0);"></a> </span>');
                    $(this).find(' > span > span').addClass('glyphicon-folder-close');
                    $(this).find(' > span > a').text(text);
                    $(this).find(' > span > a').attr('title',text);
                    $(this).append(children);
                }
                else {
                    text = $(this).text();
                    $(this).html('<span style="padding-left:'+ len +'px"><span class="glyphicon"></span><a style="max-width:' + aWidth + 'px" href="javascript: void(0);"></a> </span>');
                    $(this).find(' > span > span').addClass('glyphicon-file');
                    $(this).find(' > span > a').text(text);
                    $(this).find(' > span > a').attr('title',text);
                }
                
                if($(this).hasClass('filter')){
                    $(this).find(' > span > a').append('<i class="sui-icon icon-tb-filter"></i><div class="filter_out"><div class="addPadding"><input placeholder="查询多个用,分隔" class="filter" type="text" ></div></div>');
                }
                
            });
            easyTree.show();
//            $(easyTree).find('li:has(ul)').addClass('parent_li').find(' > span').attr('title', options.i18n.collapseTip);

            var delegateShow = function($this,e,callback){
                $this.toggleClass("delegate");
                var children = $this.children('ul');
                if (!$this.hasClass('delegate')) {
                    children.hide('fast');
                    $this.find('span.glyphicon:first').addClass('glyphicon-folder-close');
                    $this.find('span.glyphicon:first').removeClass('glyphicon-folder-open');
                } else {
                    children.show('fast');
                    $this.find('span.glyphicon:first').addClass('glyphicon-folder-open');
                    $this.find('span.glyphicon:first').removeClass('glyphicon-folder-close');
                }
                e.stopPropagation();
            }

            var findLiOpen = function(){
                $.each($(outEasyTree).find('ul > li'), function() {
                    if($(this).hasClass('li_selected')){
                        $(this).show();
                        $(this).siblings('li').show();
                        $(this).parents('li').addClass('delegate').find('.glyphicon:first').removeClass('glyphicon-folder-close').addClass('glyphicon-folder-open');
                        $(this).parents('ul').show();
                        $(this).parents('li').siblings('li.parent_li').find('.glyphicon:first').removeClass('glyphicon-folder-open').addClass('glyphicon-folder-close');
                    }
                });
                
                if(easyTree.className ==  outEasyTree.className) filterFun();
            }

            //是否展开
            if(!options.open){
                $(easyTree).find('ul').hide();
                $(easyTree).find('ul:first').show();
                //$(easyTree).find('ul:first').find('li:first').siblings('li').show();
                findLiOpen();
            }



            // selectable, only single select
            if (options.selectable) {
                $(easyTree).delegate("li","click",function(e){
                    var li = $(this);
                    if(li.hasClass('parent_li')){
                        delegateShow(li,e); 
                     }
                    if (li.hasClass('li_selected')) {
                        e.stopPropagation();
                        return false;
                    }
                    else {
                        $(outEasyTree).find('li.li_selected').removeClass('li_selected');
                        $(li).addClass('li_selected');
                        options.callback(li);
                    }
                    e.stopPropagation();
                });
            }

            $(easyTree).delegate('.icon-tb-filter','click',function(e){
                $(this).siblings('.filter_out').toggle();
                $(this).siblings('.filter_out').find('input.filter').focus();
                e.stopPropagation();
            });

            $(easyTree).delegate('input.filter','click',function(e){
                //$(this).closest('li').append('<input class="filter" type="text" >')
                e.stopPropagation();
            });

            $("body").keydown(function() {
                if (event.keyCode == "13") {//keyCode=13是回车键
                    var str = $('input.filter').val();
                    filterFun(str);
                }
            });

            $("html").bind("click",function(e){
                $('.filter_out').hide();
                var str = $('input.filter').val();
                filterFun(str);
            });
            
            // Get selected items
            var getSelectedItems = function () {
                return $(easyTree).find('li.li_selected');
            };
        });
    };
})(jQuery);
