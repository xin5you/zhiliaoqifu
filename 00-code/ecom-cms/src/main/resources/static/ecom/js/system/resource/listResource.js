$(document).ready(function() {
	listResource.init();
})

var listResource = {
	init : function() {
		listResource.initEvent();
	},

	initEvent : function() {
		$('.btn-reset').on('click', listResource.searchReset);
		$('.btn-add').on('click', listResource.intoAdd);
		$('.btn-edit').on('click', listResource.intoEdit);
		$('.btn-delete').on('click', listResource.deleteResource);
		$('.btn-close').on('click', listResource.searchReset);
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
			rules : {
				name_ : {
					required : true
				},
				resourceType_ : {
					required : true
				},
				url_ : {
					required : true
				}
			},
			messages : {
				name_ : {
					required : "请输入资源名称"
				},
				resourceType_ : {
					required : "请输入资源类型"
				},
				url_ : {
					required : "请输入资源路径"
				}
			},
			invalidHandler : function(form, validator) {
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
								classes : 'ui-tooltip-red ui-tooltip-rounded' // Make
							// it
							// red...
							// the
							// classic
							// error
							// colour!
							}
						}).qtip('option','content.text', error);
					} else {
						var xPoint = 5;
						/*
						 * if (elem.attr('name') ==
						 * 'authCode'){//特殊处理验证码 xPoint = 115; }
						 */
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
				$(".btn-submit").attr('disabled',"true");
				if (intoType == 1) {
					listResource.addResource();
				} else if (intoType == 2) {
					listResource.editResource();
				}
				return false;
			},
			success : $.noop
		});
	},
	intoAdd : function() {
		listResource.loadModal(1, $(this).attr('resourceId'));
		listResource.initTip(1);
	},
	intoEdit : function() {
		listResource.loadModal(2, $(this).attr('resourceId'));
		listResource.initTip(2);
	},
	addResource : function() {
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/system/resource/addResource',
			data : {
				"name" : $("#name_").val(),
				"resourceType" : $("#resourceType_").val(),
				"url" : $("#url_").val(),
				"description" : $("#description_").val()
			},
			cache : false,
			dataType : 'json',
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('新增资源成功', function() {
						listResource.searchReset();
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
	editResource : function() {
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/system/resource/editResource',
			data : {
				"id" : $("#id_").val(),
				"name" : $("#name_").val(),
				"resourceType" : $("#resourceType_").val(),
				"url" : $("#url_").val(),
				"description" : $("#description_").val()
			},
			cache : false,
			dataType : 'json',
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('编辑资源成功', function() {
						listResource.searchReset();
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
	deleteResource : function() {
		var id = $(this).attr('resourceId');
		if (id == null || id == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定删除该资源？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/system/resource/deleteResource',
				type : 'post',
				dataType : "json",
				data : {
					"id" : id
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('删除资源成功', function() {
							listResource.searchReset();
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
		});
	},
	searchReset : function() {
//		location = Helper.getRootPath() + '/system/resource/listResource';
		Helper.post('/system/resource/listResource');
	},
	loadModal : function(type, resourceId) {
		$('#modal').modal({
			backdrop : "static"
		});
		if (type == 1) {
			$('#modal_h').html("添加资源");
			return;
		} else if (type == 2) {
			$('#modal_h').html("编辑资源");
		}

		$.ajax({
			url : Helper.getRootPath() + '/system/resource/getResource',
			type : 'post',
			dataType : "json",
			data : {
				"id" : resourceId
			},
			success : function(data) {
				$('#id_').val(data.id);
				$('#name_').val(data.name);
				$('#resourceType_').val(data.resourceType);
				$('#url_').val(data.url);
				$('#description_').val(data.description);
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});

		$("#modal").on("hidden.bs.modal", function(e) {
			$("#name_").removeAttr('readonly');
			$("#resourceType_").removeAttr('readonly');
			$("#url_").removeAttr('readonly');
			$("#description_").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	}
}
