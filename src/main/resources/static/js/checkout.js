$(document).ready(function(){
    var wizard = $('#smartwizard');
    wizard.smartWizard({
        selected: 0,  // Initial selected step, 0 = first step
        keyNavigation:true, // Enable/Disable keyboard navigation(left and right keys are used if enabled)
        autoAdjustHeight:true, // Automatically adjust content height
        cycleSteps: false, // Allows to cycle the navigation of steps
        backButtonSupport: true, // Enable the back button support
        useURLhash: false, // Enable selection of the step based on url hash
        lang: {  // Language variables
            next: 'Tiếp tục',
            previous: 'Quay lại'
        },
        toolbarSettings: {
            toolbarPosition: 'bottom', // none, top, bottom, both
            toolbarButtonPosition: 'right', // left, right
            showNextButton: true, // show/hide a Next button
            showPreviousButton: true, // show/hide a Previous button
            toolbarExtraButtons: [
                $('<button></button>')
                    .text('Đặt hàng')
                    .attr("id","btnOrder")
                    .addClass('btn btn-info')
                    .on('click', function(){
                        let name = $('#fullname_invoice').val();
                        let address = $('#street_invoice').val();
                        let phone = $('#phonenumber_invoice').val();
                        $.ajax({
                            type : "POST",
                            contentType : "application/json",
                            url : "/checkout/order",
                            data : JSON.stringify({
                                name : name,
                                address : address,
                                phone : phone
                            }),
                            dataType : 'json',
                            timeout : 100000,
                            success : function(data) {
                                var i = data.status;
                                if (i===1) {
                                    swal("Đơn hàng", "Đơn hàng đã được đặt thành công !", "success");
                                }
                                else swal("ERROR", "Có lỗi bất thường xẩy ra, vui lòng reload lại trang web", "error");
                            },
                            error : function(e) {
                                swal("ERROR", "Có lỗi bất thường xẩy ra, vui lòng tải lại trang web", "error");
                                console.log("ERROR: ", e);
                            }
                        });
                    })
            ]
        },
        anchorSettings: {
            anchorClickable: true, // Enable/Disable anchor navigation
            enableAllAnchors: false, // Activates all anchors clickable all times
            markDoneStep: true, // add done css
            enableAnchorOnDoneStep: true // Enable/Disable the done steps navigation
        },
        contentURL: null, // content url, Enables Ajax content loading. can set as data data-content-url on anchor
        disabledSteps: [],    // Array Steps disabled
        errorSteps: [],    // Highlight step with errors
        theme: 'dots',
        transitionEffect: 'slide', // Effect on navigation, none/slide/fade
        transitionSpeed: '400'
    });
    $('.sw-btn-group-extra').hide();
    wizard.on("showStep", function(e, anchorObject, stepNumber, stepDirection) {
        if($('button.sw-btn-next').hasClass('disabled')){
            $('.sw-btn-group-extra').show(); // show the button extra only in the last page
        }else{
            $('.sw-btn-group-extra').hide();
        }

    });
});
