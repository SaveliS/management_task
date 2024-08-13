function moveOptions(sourceId, destinationId) {
    var sourceSelect = document.getElementById(sourceId);
    var destinationSelect = document.getElementById(destinationId);

    Array.from(sourceSelect.selectedOptions).forEach(option => {
        destinationSelect.appendChild(option);
    });
}