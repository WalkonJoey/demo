/**
 * 
 * dynamic-table.js
 * 专用于动态增加或删除表格的行
 *  created by Aaron on 20140925
 * 
 * 支持easyui框架
 */

//添加1行
/**
 * autoTableId 表格id rowIndex 新增的行索引，最后一行直接为-1,如果为0，则在到处第二行新增 autoTableRowData
 * 行中的列组成的数组， 定义如：var autoTableRowData=new Array( '<input type="hidden"
 * name="productID" /><input type="text" placeholder="请选择"
 * onClick="selectSku(this);" name="skuNo" />', '<input type="text"
 * name="vendorModel" />', '<input type="text" name="productName" />', '<input
 * type="text" name="prodQty" />', '<input type="text" name="price" />', '<input
 * type="text" name="unit" />', '<input type="text" name="amount" />', '<input
 * type="text" name="deliverDt" class="easyui-datebox" />', '<a href="#"
 * onClick="delTableRow(this);return false;">删除</a>' );
 */
function addOneTableRow(autoTableId, rowIndex, autoTableRowData) {
	var tbobj = document.getElementById(autoTableId);
	var trobj, tdobj;
	if (rowIndex == -1) {
		trobj = tbobj.insertRow(-1);
	} else {
		var t = tbobj.rows.length;
		trobj = tbobj.insertRow(t - 1);
	}
	// trobj.className="Aaron";
	// trobj.onmouseover=new Function("this.className='Aaron';");
	// trobj.onmouseout=new Function("this.className='Aaron';");
	for (var i = 0; i < autoTableRowData.length; i++) {
		tdobj = trobj.insertCell(-1);
		tdobj.innerHTML = autoTableRowData[i];
	}
	// 这句很重要，通过JQuery重新解析
	$.parser.parse(trobj);
}

// 删除1行
/**
 * autoTableId 表格id obj 行中列内的某个元素，如input元素对象
 */
function delOneTableRow(autoTableId, obj) {
	// 这里查找行元素对象的索引时，要注意层级，如有变动直接修改
	var tbobj = document.getElementById(autoTableId);
	if (obj == null) {
		// 如果传入obj为空，表示删除除首末行外的所有行
		//console.log('总共行数：' + tbobj.rows.length);
		while (tbobj.rows.length != 2) {
			tbobj.deleteRow(1);
		}
		return;
	}
	var rowIndex = obj.parentNode.parentNode.rowIndex;
	if (rowIndex == -1) {
		if (tbobj.rows.length > 1) {
			tbobj.deleteRow(tbobj.rows.length - 1);
		}
	} else {
		tbobj.deleteRow(rowIndex);
	}
}

/**
 * 获取动态表格的所有名称值，json格式输出
 * 
 * @param autoTableId
 * @returns
 */
function getJson4Table(autoTableId, autoTableRowData) {
	var tbobj = document.getElementById(autoTableId);
	var json = '';
	for (var i = 1; i < tbobj.rows.length - 1; i++) {
		var row = tbobj.rows[i];
		json += '{';
		for (var j = 0; j < autoTableRowData.length - 1; j++) {
			var cell = row.cells[j];
			var name = cell.getElementsByTagName("input")[0]
					.getAttribute('name');
			var value = cell.getElementsByTagName("input")[0]
					.getAttribute('value');
			json += name + ':' + value;
			if (j < autoTableRowData.length - 2) {
			}
			json += ',';
		}
		json += '}';
	}
}
