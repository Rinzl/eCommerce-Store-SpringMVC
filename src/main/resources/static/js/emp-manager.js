$(document).ready(function() {

    $('#example').DataTable({
        "columnDefs": [
            { "orderable": false, "targets": 4 }
        ],
        "paging":   false,
        "language": {
            "sProcessing":   "Đang xử lý...",
            "sLengthMenu":   "Xem _MENU_ mục",
            "sZeroRecords":  "Không tìm thấy dòng nào phù hợp",
            "sInfo":         "Đang xem _START_ đến _END_ trong tổng số _TOTAL_ mục",
            "sInfoEmpty":    "Đang xem 0 đến 0 trong tổng số 0 mục",
            "sInfoFiltered": "(được lọc từ _MAX_ mục)",
            "sInfoPostFix":  "",
            "sSearch":       "Tìm:",
            "sUrl":          "",
            "oPaginate": {
                "sFirst":    "Đầu",
                "sPrevious": "Trước",
                "sNext":     "Tiếp",
                "sLast":     "Cuối"
            }
        }
    });

    $('.btn-edit-emp').each(function () {
        $(this).on('click', function () {
            let modalSelector = $('#editEmpModal');
            modalSelector.modal();
            let selector = $(this).parent().parent();
            let id = selector.find('.text-center').attr('id');
            modalSelector.find('.btn-submit-edit').attr('id',id);
            let cateName = selector.find('.emp-name').html();
            let cateDesc = selector.find('.emp-pass').html();
            let status = selector.find('.emp-role').attr('value');
            modalSelector.find('input#emp-edit-name').val(cateName);
            modalSelector.find('input#emp-edit-pass').val(cateDesc);
            modalSelector.find('select#emp-edit-role').val(status);

        });
    });
    $('.btn-delete-emp').on('click', function () {
        let selector = $(this).parent().parent();
        let id = selector.find('.text-center').attr('id');
        let cateName = selector.find('.emp-name').html();
        let cateDesc = selector.find('.emp-pass').html();
        swal({
            title: "Bạn có muốn xóa " + cateName,
            icon: "warning",
            buttons: true,
            dangerMode: true,
        }).then((willDelete) => {
            if (willDelete) {
                $.ajax({
                    type : "POST",
                    contentType : "application/json",
                    url : "/admin/employee/delete",
                    data : JSON.stringify({
                        id : id,
                        cateName : cateName,
                        cateDesc : cateDesc
                    }),
                    dataType : 'json',
                    timeout : 100000,
                    success : function(data) {
                        var i = data.status;
                        if (data.status === 1) {
                            swal(name + " đã được xóa!", {
                                icon: "success",
                            }).then(() => {
                                window.location.reload(true);
                            });
                        } else {
                            swal("ERROR", "Xóa không thành công, có thể nhiều sản phẩm đang thuộc chuyên mục này", "error");

                        }
                    },
                    error : function(e) {
                        swal("ERROR", "Lỗi không xác định, vui lòng tải lại trang web", "error");
                    }
                });
            }
        });
    });

    $('#btn-add-emp').on('click', function () {
        let select = $('#form-add-emp');
        let formHolder = $('#form-add-emp-holder');
        let empName = select.find('input#emp-name').val();
        let empPass = select.find('input#emp-pass').val();
        let empRole = select.find('option:selected').val();

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/admin/employee/add",
            data : JSON.stringify({
                id : "1",
                name : empName,
                pass : empPass,
                role : empRole
            }),
            dataType : 'json',
            timeout : 100000,
            success : function(data) {
                var i = data.status;
                if (i===1) {
                    window.location.reload(true);
                }
                else if (i===2) {
                    formHolder.prepend("<div class=\"alert alert-danger\" role=\"alert\">" +
                        "                <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\" title=\"close\">×</a>" +
                        "                Tên user đã tồn tại </div>");
                }
            },
            error : function(e) {
                formHolder.prepend("<div class=\"alert alert-danger\" role=\"alert\">" +
                    "                <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\" title=\"close\">×</a>" +
                    "                Thêm User thất bại, vui lòng tải lại trang </div>");

                console.log("ERROR: ", e);
            }
        });
    });

    $('.btn-submit-edit').on('click', function () {
        let selector = $('#editEmpModal');
        let empName = selector.find('input#emp-edit-name').val();
        let empPass = selector.find('input#emp-edit-pass').val();
        let empRole = selector.find('option:selected').val();
        let formHolder = $('#form-edit-emp-holder');
        let id = $(this).attr('id');
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/admin/employee/edit",
            data : JSON.stringify({
                id : id,
                name : empName,
                pass : empPass,
                role : empRole
            }),
            dataType : 'json',
            timeout : 100000,
            success : function(data) {
                var i = data.status;
                if (i===1) {
                    window.location.reload(true);
                }
                else if (i === 2) {
                    formHolder.prepend("<div class=\"alert alert-danger\" role=\"alert\">" +
                        "                <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\" title=\"close\">×</a>" +
                        "                Username đã tồn tại </div>");
                }
            },
            error : function(e) {
                formHolder.prepend("<div class=\"alert alert-danger\" role=\"alert\">" +
                    "                <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\" title=\"close\">×</a>" +
                    "                Cập nhật User thất bại, vui lòng tải lại trang </div>");

                console.log("ERROR: ", e);
            }
        });
    });

    $('#exampleModal').on('hidden.bs.modal', function (e) {
        $('div.alert-danger').remove();
    });
    $('#editEmpModal').on('hidden.bs.modal', function (e) {
        $('div.alert-danger').remove();
    });
});
