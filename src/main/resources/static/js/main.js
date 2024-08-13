document.addEventListener('DOMContentLoaded', function () {
    var rows = document.querySelectorAll('.table-row');
    rows.forEach(function (row) {
        row.addEventListener('click', function () {
            var url = row.getAttribute('data-url');
            window.location.href = url;
        });
    });
});