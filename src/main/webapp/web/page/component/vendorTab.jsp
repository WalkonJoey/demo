<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<!-- 此页面用于TAB选项卡，展示相关联的供应商信息 -->
</head>
<body>
		<c:choose>
			<c:when test="${vendor_tab!=null}">
				<table class="tab_view_table">
					<tr>
						<th><label>供应商代码:</label></th>
						<td>${vendor_tab.vendorCode}</td>
						<th><label>供应商名称:</label></th>
						<td>${vendor_tab.vendorName}</td>
						<th>跟单人:</th>
						<td>${vendor_tab.documentaryName}
						</td>
					</tr>
					<tr>
						<th>邮编:</th>
						<td>${vendor_tab.postCode}</td>
						<th><label>供应商地址:</label></th>
						<td colspan="3">${vendor_tab.address}</td>
					</tr>
					<tr>
						<th>采购工程师:</th>
						<td>
							${vendor_tab.pEngineerName}
						</td>
						<th>结算周期:</th>
						<td>
							<select class="i_input" name="payPeriod">
								<option value="1" <c:if test="${vendor_tab.payPeriod==1}">selected="selected"</c:if>>现结</option>
								<option value="2" <c:if test="${vendor_tab.payPeriod==2}">selected="selected"</c:if>>周结</option>
								<option value="3" <c:if test="${vendor_tab.payPeriod==3}">selected="selected"</c:if>>旬结</option>
								<option value="4" <c:if test="${vendor_tab.payPeriod==4}">selected="selected"</c:if>>两周结</option>
								<option value="5" <c:if test="${vendor_tab.payPeriod==5}">selected="selected"</c:if>>月结</option>
								<option value="6" <c:if test="${vendor_tab.payPeriod==6}">selected="selected"</c:if>>季结</option>
								<option value="7" <c:if test="${vendor_tab.payPeriod==7}">selected="selected"</c:if>>半年结</option>
								<option value="8" <c:if test="${vendor_tab.payPeriod==8}">selected="selected"</c:if>>年结</option>
							</select>
						</td>
						<th>付款约定:</th>
						<td>
							<select class="i_input" name="payAppoint">
								<option value="1" <c:if test="${vendor_tab.payAppoint==1}">selected="selected"</c:if>>T+1</option>
								<option value="2" <c:if test="${vendor_tab.payAppoint==2}">selected="selected"</c:if>>T+2</option>
								<option value="3" <c:if test="${vendor_tab.payAppoint==3}">selected="selected"</c:if>>T+3</option>
								<option value="4" <c:if test="${vendor_tab.payAppoint==4}">selected="selected"</c:if>>T+4</option>
								<option value="5" <c:if test="${vendor_tab.payAppoint==5}">selected="selected"</c:if>>T+5</option>
								<option value="6" <c:if test="${vendor_tab.payAppoint==6}">selected="selected"</c:if>>T+6</option>
								<option value="7" <c:if test="${vendor_tab.payAppoint==7}">selected="selected"</c:if>>T+7</option>
								<option value="8" <c:if test="${vendor_tab.payAppoint==8}">selected="selected"</c:if>>T+14</option>
								<option value="9" <c:if test="${vendor_tab.payAppoint==9}">selected="selected"</c:if>>T+30</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>币种:</th>
						<td>
							<input name="currencyID" type="hidden" value="${vendor_tab.currencyID}"/>
							<input name="currencyName" class="i_input" id="clo-currency-name"  value="${vendor_tab.currencyName}" placeholder="选择币种" onClick="selectCurrency(this);" required="true"/>
						</td>
						<th>结算方式:</th>
						<td>
							<select class="i_input" name="payType">
								<option value="1" <c:if test="${vendor_tab.payType==1}">selected="selected"</c:if>>银行转账</option>
								<option value="2" <c:if test="${vendor_tab.payType==2}">selected="selected"</c:if>>现金结算</option>
							</select>
						</td>
						<th>票据类型:</th>
						<td>
							<select class="i_input" name="invoiceType">
								<option value="1" <c:if test="${vendor_tab.invoiceType==1}">selected="selected"</c:if>>普通发票</option>
								<option value="2" <c:if test="${vendor_tab.invoiceType==2}">selected="selected"</c:if>>增值税专用发票</option>
								<option value="3" <c:if test="${vendor_tab.invoiceType==3}">selected="selected"</c:if>>增值税普通发票</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>规格参数分隔符:</th>
						<td>
							<select class="i_input" name="separatorAscii">
								<option value="47" <c:if test="${vendor_tab.separatorAscii==47}">selected="selected"</c:if>>斜杠'/'</option>
								<option value="20" <c:if test="${vendor_tab.separatorAscii==20}">selected="selected"</c:if>>空格</option>
								<option value="13" <c:if test="${vendor_tab.separatorAscii==13}">selected="selected"</c:if>>换行</option>
								<option value="42" <c:if test="${vendor_tab.separatorAscii==42}">selected="selected"</c:if>>星号'*'</option>
							</select>
						</td>
						<th><label>备注:</label></th>
						<td colspan="3">${vendor_tab.comment}</td>
					</tr>
				</table>
				<table class="view_table">
					<tr>
						<th>姓名:</th>
						<td>
							${vendor_tab.csmName}
						</td>
						<th>固定电话:</th>
						<td>
							${vendor_tab.csmTelephone}
						</td>
						<th>移动电话:</th>
						<td>
							${vendor_tab.csmMobilePhone}
						</td>
					</tr>
					<tr>
						<th>传真</th>
						<td>
							${vendor_tab.csmFax}
						</td>
						<th>邮箱:</th>
						<td>
							${vendor_tab.csmEmail}
						</td>
						
						<th>QQ:</th>
						<td>
							${vendor_tab.csmQQ}
						</td>
					</tr>
					<tr>
						<th>姓名:</th>
						<td>
							${vendor_tab.clerkName}
						</td>
						<th>固定电话:</th>
						<td>
							${vendor_tab.clerkTelephone}
						</td>
						<th>移动电话:</th>
						<td>
							${vendor_tab.clerkMobilePhone}
						</td>
					</tr>
					<tr>
						<th>传真</th>
						<td>
							${vendor_tab.csmFax}
						</td>
						<th>邮箱:</th>
						<td>
							${vendor_tab.clerkEmail}
						</td>
						
						<th>QQ:</th>
						<td>
							${vendor_tab.clerkQQ}
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<p>没有相关的供应商信息
			</c:otherwise>
		</c:choose>
</body>
</html>