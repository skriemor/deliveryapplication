$(document).ready(function () {
    $('#saleForm').on('keydown', 'input', function (event) {
        if (event.which == 13) {
            event.preventDefault();
            var $this = $(event.target);
            var index = parseFloat($this.attr('tabindex'));
            if (index == 13) {
                $('[tabindex=14]').click();
                $('[tabindex=15]').click().focus().select();
            }
            $('[tabindex="' + (index + 1).toString() + '"]').focus().select();
        }
    });

    highlightOverduePayments();
});

function funcForWindowPopup() {
    var popup = null;
    popup = window.open(
        "buyers_no_menu.xhtml",
        "popup",
        "toolbar=no,menubar=no,scrollbars=yes,location=no,left=350,top=50,width=1200,height=768"
    );
    popup.openerFormId = "vendorForm";
    popup.focus();
}

function highlightOverduePayments() {
    $('#saleForm\\:saleList tbody tr').each(function () {
        var $row = $(this);
        var deadlineText = $row.find('.deadlineColumn').text().trim();
        var completionText = $row.find('.completionDateColumn').text().trim();
        if (deadlineText !== '') {
            var parts = deadlineText.split(".");
            var deadlineDate = new Date(parts[0], parts[1] - 1, parts[2]);
            var today = new Date();
            today.setHours(0, 0, 0, 0);
            if (deadlineDate < today && completionText === '') {
                $row.find('.deadlineColumn').css('background-color', 'red');
            }
        }
    });
}
