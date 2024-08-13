document.addEventListener("DOMContentLoaded", function() {
    var notification = document.getElementById("notification");
    if (notification) {
        setTimeout(function() {
            notification.style.opacity = '0';
            setTimeout(function() {
                notification.style.display = 'none';
            }, 1000);
        }, 10000); // 10 seconds
    }
});