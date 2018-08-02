<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%
	pageContext.setAttribute("currentHeader", "domap");
%>
<%
	pageContext.setAttribute("currentMenu", "domap");
%>
<%
	pageContext.setAttribute("currentMenuId", "719");
%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="dev.map-group-save.title" text="列表"/></title>
    <%@include file="/common/s3.jsp"%>
    <script type="text/javascript">
var config = {
    id: 'cms-articleGrid',
    pageNo: ${page.pageNo},
    pageSize: ${page.pageSize},
    totalCount: ${page.totalCount},
    resultSize: ${page.resultSize},
    pageCount: ${page.pageCount},
    orderBy: '${page.orderBy == null ? "" : page.orderBy}',
    asc: ${page.asc},
    params: {
        'filter_EQS_startCurrentTime': '${param.filter_EQS_startCurrentTime}',
        'filter_EQS_endCurrentTime': '${param.filter_EQS_endCurrentTime}'
    },
	selectedItemClass: 'selectedItem',
	gridFormId: 'domap-infoGridForm',
	//exportUrl: 'domap-article-export.do'
};

var table;

$(function() {
	table = new Table(config);
    table.configPagination('.m-pagination');
    table.configPageInfo('.m-page-info');
    table.configPageSize('.m-page-size');
});
$(function(){
	$(".del").click(function(){
		return confirm("是否确认删除");
		if(confirm("是否确认删除")){
			submit_function();
		}else{
			return false;
		}
	})
})
    </script>
  </head>


   <body class="page-body">
     <%@include file="/header/mainHeader.jsp" %>
    
 <div class="panel panel-default">
  <div class="panel-heading">
	<i class="glyphicon glyphicon-list"></i>
    查询
	<div class="pull-right ctrl">
	  <a class="btn btn-default btn-xs"><i id="cms-articleSearchIcon" class="glyphicon glyphicon-chevron-up"></i></a>
    </div>
  </div>
  <div class="panel-body">

		  	<form name="domap-userInfo" method="post" action="map-mapHolidays-list.do" class="form-inline">
		  		  
		  	<div class="form-group">
                    <label for="domap-domapInfo-startCurrentTime" style="margin-left:40px">开始日期:</label>
                    <div class="input-group">
	    			<input id="filter_EQS_startCurrentTime" type="text" name="filter_EQS_startCurrentTime" 
	    			<fmt:parseDate value="${param.filter_EQS_startCurrentTime}" pattern="yyyy-MM-dd" var="dateStart"/>
	    			value="<fmt:formatDate value='${dateStart}'/>" readonly="readonly" style="background-color:white;cursor:default;"
	    			class="form-control required" 
	    			onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
	    			<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
	  		</div>	
	  		</div>
	  		<div class="form-group">
                    <label for="domap-domapInfo-startCurrentTime" style="margin-left:40px">结束日期:</label>
                    <div class="input-group">
	    			<input id="filter_EQS_startCurrentTime" type="text" name="filter_EQS_endCurrentTime" 
	    			<fmt:parseDate value="${param.filter_EQS_endCurrentTime}" pattern="yyyy-MM-dd" var="dateEnd"/>
	    			value="<fmt:formatDate value='${dateEnd}'/>" readonly="readonly" style="background-color:white;cursor:default;"
	    			class="form-control required" 
	    			onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
	    			<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
	  		</div>
            </div>
		    
		  		   <!--  <label>&nbsp;合同类型:</label>
                    <div class="form-group">
                     <tags:getcodeList selectName="filter_EQS_type" selectId="contract-ptys_type" selectValue="${param.filter_EQS_type}" selectCalss="form-control" code="hetongtype"  ></tags:getcodeList>

                    </div> -->
		   <!-- <label for="domap-domapInfo-groupName"><spring:message code='domap-domapInfo.domap-domapInfo-groupName' text='考勤组'/>:</label>
		    <tags:getcodeList selectName="filter_LIKES_groupName" selectId="domap-domapInfo-groupName" selectValue="${param.filter_LIKES_groupName}" selectCalss="form-control" code="cms"  ></tags:getcodeList>
		  		   &nbsp; -->	
		  <button class="btn btn-primary" style="margin-bottom: 2px" onclick="document.cms-articleForm.submit()">						
				<i class="fa-search">&nbsp;&nbsp;查&nbsp;询</i>
		 </button>
		  
		  </form>

		</div>
	  </div>

      <div>
	   

		<div class="pull-right">
		  每页显示
		  <select class="m-page-size form-control" style="display:inline;width:auto;">
		    <option value="10">10</option>
		    <option value="20">20</option>
		    <option value="50">50</option>
		  </select>
		  条
        </div>

	    <div class="clearfix"></div>
	  </div>
      <div>
	    <div class="pull-left btn-group" role="group">
		  <button class="btn btn-success btn-sm" onclick="location.href='map-mapHolidays-input.do'"><i class="fa-plus">&nbsp;&nbsp;新&nbsp;建</i> </button>
		  
		  <!--
		<button class="btn btn-default a-export" onclick="table.exportExcel()">导出</button>
		  -->
		</div>

	    <div class="clearfix"></div>
	  </div>
<form id="cms-articleGridForm" name="cms-articleGridForm" method='post' action="cms-article-remove.do" class="m-form-blank">
      <div class="panel panel-default">
        <div class="panel-heading">
		  <i class="glyphicon glyphicon-list"></i>
		  <spring:message code="scope-info.scope-info.list.title" text="列表"/>
		</div>


  <table id="cmsArticleGrid" class="table table-striped table-hover table-bordered dataTable"> 
    <thead>
      <tr>
     <!--    <th width="10" class="table-check"><input type="checkbox" name="checkAll" onchange="toggleSelectedItems(this.checked)"></th> -->
        <th class="sorting"  width="10%">时间</th>
        <th class="sorting" width="20%">状态</th>
        <th class="sorting" width="50%">备注</th>
        <th width="20%">&nbsp;</th>
        
      </tr>
    </thead>

    <tbody>
      <c:forEach items="${page.result}" var="item">
      <tr>
		<td><fmt:formatDate value='${item.mapTime}' pattern='yyyy-MM-dd'/></td>
		<c:if test="${item.status == 0}">
		<td>休息</td>
		</c:if>
		<c:if test="${item.status == 1}">
		<td>工作</td>
		</c:if>
		<td>${item.mapNotes}</td>
        <td>
          <a href="map-mapHolidaysInfo-update.do?mapId=${item.mapId}">编辑</a>
          &nbsp;
          <a class="del" href="map-mapHolidays-delete.do?mapId=${item.mapId}">删除</a> 
          <%-- <a href="cms-article-image.do?id=${item.id}" class="a-update"><spring:message code="core.list.edit" text="编辑"/></a>--%>
        </td>
      </tr>
      </c:forEach>
    </tbody>
  </table>


      </div>
</form>

	  <div>
	    <div class="m-page-info pull-left">
		  共100条记录 显示1到10条记录
		</div>

		<div class="btn-group m-pagination pull-right">
		  <button class="btn btn-default">&lt;</button>
		  <button class="btn btn-default">1</button>
		  <button class="btn btn-default">&gt;</button>
		</div>

	    <div class="clearfix"></div>
      </div>

      <div class="m-spacer"></div>

<%@include file="/header/mainFooter.jsp" %>

  </body>

</html>

