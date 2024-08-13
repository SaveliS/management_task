window.onload = function(){
    const messageElement = document.getElementById('messageFile');
    if(messageElement){
        setTimeout(() => {
            messageElement.style.display = 'none';
        },15000);
    }
}