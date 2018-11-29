$(document).ready(function() {
	listFloor.init();
})

var listFloor = {
	init : function() {
		listFloor.initEvent();
	},

	initEvent : function() {
		$('#type1').change(listFloor.changeEcomCode);
		$('.btn-reset').on('click', listFloor.searchReset);
		$('.btn-add').on('click', listFloor.intoAdd);
		$('.btn-edit').on('click', listFloor.intoEdit);
		$('.btn-delete').on('click', listFloor.deleteFloor);
		$('.btn-close').on('click', listFloor.searchReset);
		$('.btn-add-banner').on('click', listFloor.intoFloorEgoodsList);
	},
	initTip : function(intoType) {
		var ttip_validator = $('.form_validation_tip').validate({
			onkeyup : false,
			errorClass : 'error',
			validClass : 'valid',
			highlight : function(element) {
				$(element).closest('div').addClass("f_error");
			},
			unhighlight : function(element) {
				$(element).closest('div').removeClass("f_error");
			},
			invalidHandler : function(form, validator) {
			},
			rules : {
				title1 : {
					required : true
				},
				type1 : {
					required : true
				},
				is_display : {
					required : true
				},
				sort : {
					required : true
				}
			},
			messages : {
				title1 : {
					required : "请输入楼层标题"
				},
				type1 : {
					required : "请输入类型"
				},
				is_display : {
					required : "请输入显示标识"
				},
				sort : {
					required : "请输入排序"
				}
			},
			errorPlacement : function(error, element) {
				var elem = $(element);
				if (!error.is(':empty')) {
					if ((elem.is(':checkbox')) || (elem.is(':radio'))) {
						elem.filter(':not(.valid)').parent('label').parent('div').find('.error_placement').qtip({
							overwrite : false,
							content : error,
							position : {
								my : 'left center',
								at : 'center right',
								viewport : $(window),
								adjust : {
									x : 6
								}
							},
							show : {
								event : false,
								ready : true
							},
							hide : false,
							style : {
								classes : 'ui-tooltip-red ui-tooltip-rounded' 
							}
						}).qtip('option','content.text', error);
					} else {
						var xPoint = 5;
						elem.filter(':not(.valid)').qtip({
							overwrite : false,
							content : error,
							position : {
								my : 'left center',
								at : 'center right',
								viewport : $(window),
								adjust : {
									x : xPoint,
									y : 0
								}
							},
							show : {
								event : false,
								ready : true
							},
							hide : false,
							style : {
								classes : 'ui-tooltip-red ui-tooltip-rounded'
							}
						}).qtip('option','content.text', error);
					}
					;
				} else {
					if ((elem.is(':checkbox')) || (elem.is(':radio'))) {
						elem.parent('label').parent('div').find('.error_placement').qtip('destroy');
					} else {
						elem.qtip('destroy');
					}
				}
			},
			submitHandler : function(form) {
//				$(".btn-submit").attr('disabled',"true");
				if (intoType == 1) {
					listFloor.addFloor();
				} else if (intoType == 2) {
					listFloor.editFloor();
				}
				return false;
			},
			success : $.noop
		});
	},
	intoAdd : function() {
		listFloor.loadModal(1, $(this).attr('floorId'));
		listFloor.initTip(1);
	},
	intoEdit : function() {
		listFloor.loadModal(2, $(this).attr('floorId'));
		listFloor.initTip(2);
	},
	addFloor : function() {
		var type = $("#type1").val();
		var ecomCode = $("#ecom_code").val();
		if(type == '1' && ecomCode == '0'){
			Helper.alert("请选择电商名称");
			return false;
		}
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/floor/addFloor',
			data : {
				"title" : $("#title1").val(),
				"type" : type,
				"ecomCode" :ecomCode,
				"logo" : $("#logo").val(),
				"isDisplay" : $("#is_display").val(),
				"sort" : $("#sort").val(),
				"remarks" : $("#remarks").val()
			},
			cache : false,
			dataType : 'json',
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('新增楼层信息成功', function() {
						listFloor.searchReset();
					});
				} else {
					Helper.alert(data.msg);
					return false;
				}
			},
			error : function() {
				Helper.alert(data.msg);
			}
		});
	},
	editFloor : function() {
		var type = $("#type1").val();
		var ecomCode = $("#ecom_code").val();
		if(type == '1' && ecomCode == '0'){
			Helper.alert("请选择电商名称");
			return false;
		}
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/floor/editFloor',
			data : {
				"floorId": $("#floor_id").val(),
				"title" : $("#title1").val(),
				"type" : type,
				"ecomCode" : ecomCode,
				"logo" : $("#logo").val(),
				"isDisplay" : $("#is_display").val(),
				"sort" : $("#sort").val(),
				"remarks" : $("#remarks").val()
			},
			cache : false,
			dataType : 'json',
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('编辑楼层信息成功', function() {
						listFloor.searchReset();
					});
				} else {
					Helper.alert(data.msg);
					return false;
				}
			},
			error : function() {
				Helper.alert(data.msg);
			}
		});
	},
	deleteFloor : function() {
		var floorId = $(this).attr('floorId');
		if (floorId == null || floorId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定删除该商城？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/floor/deleteFloor',
				type : 'post',
				dataType : "json",
				data : {
					"floorId" : floorId
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('删除楼层信息成功', function() {
							listFloor.searchReset();
						});
					}  else {
						Helper.alert(data.msg);
						return false;
					}
				},
				error : function() {
					Helper.alert(data.msg);
				}
			});
		});
	},
	searchReset : function() {
		Helper.post('/floor/listFloors');
	},
	changeEcomCode: function(){
		var floorType = $("#type1").val();
		if(floorType == '1'){
			$("#ecomCodeId").show();
			$('#ecom_code').removeAttr("disabled");
		}else{
			$("#ecomCodeId").hide();
			$('#ecom_code').attr('disabled',true);
			$('#ecom_code').val('0');
		}
	},
	loadModal : function(type, floorId) {
		$('#modal').modal({
			backdrop : "static"
		});
		if (type == 1) {
			$('#modal_h').html("添加电商楼层");
			listFloor.changeEcomCode();
			return;
		} else if (type == 2) {
			$('#modal_h').html("编辑电商楼层");
		}

		$.ajax({
			url : Helper.getRootPath() + '/floor/getFloor',
			type : 'post',
			dataType : "json",
			data : {
				"floorId" : floorId
			},
			success : function(data) {
				$('#floor_id').val(data.floorId);
				$('#title1').val(data.title);
				$("#parent_id").val(data.parentId);
				$('#type1').val(data.type);
				$('#ecom_code').val(data.ecomCode);
				$('#logo').val(data.logo);
				$('#is_display').val(data.isDisplay);
				$('#sort').val(data.sort);
				$('#remarks').val(data.remarks);
				listFloor.changeEcomCode();
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});
		$("#modal").on("hidden.bs.modal", function(e) {
			$(".btn-submit").removeAttr('disabled');
		});
	},
	intoFloorEgoodsList: function(){
		var floorId = $(this).attr('floorId');
		Helper.post('/floor/intoFloorEgoodsList?floorId='+floorId);
	}
	
}
