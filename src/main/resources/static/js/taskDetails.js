function showSection(sectionId) {
    var sections = document.querySelectorAll('.section');
    sections.forEach(function(section) {
        section.classList.remove('active');
    });
    document.getElementById(sectionId).classList.add('active');
}


let lastClickedBlock = null;

function toggleSelection(listItem) {
    const currentBlock = listItem.closest('.attachment');
    
    // Сбросить выбор, если блок изменился
    if (lastClickedBlock && lastClickedBlock !== currentBlock) {
        resetSelection(lastClickedBlock);
    }

    // Обновить последний кликнутый блок
    lastClickedBlock = currentBlock;
    
    // Переключение выбора элемента
    const checkbox = listItem.querySelector('.select-checkbox');
    checkbox.checked = !checkbox.checked;
    if (checkbox.checked) {
        listItem.classList.add('selected');
    } else {
        listItem.classList.remove('selected');
    }
}

function resetSelection(block) {
    const checkboxes = block.querySelectorAll('.select-checkbox');
    const listItems = block.querySelectorAll('li');

    checkboxes.forEach(checkbox => {
        checkbox.checked = false;
    });

    listItems.forEach(listItem => {
        listItem.classList.remove('selected');
    });
}

function deleteSelectedSubTasks () {
    const urlBase = window.location.origin + window.location.pathname + '/removeSubTask';
    const selectedSubTask = Array.from(document.querySelectorAll('li.selected')).map(li => li.dataset.fileId);

    if(selectedSubTask.length === 0){
        alert('Не выбрано задач для удаления');
        return;
    }

    const data = {
        taskId : selectedSubTask
    };
    const csrfToken = document.querySelector('input[name="_csrf"]').value;

    fetch(urlBase, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if(!response.ok){
            throw new Error('Ошибка ответа сервера')
        }

        window.location.reload();
    });
}


function deleteSelectedTasks() {

    const urlBase = window.location.origin + window.location.pathname + '/remove';
    const selectedTask = Array.from(document.querySelectorAll('li.selected')).map(li => li.dataset.fileId);
    if (selectedTask.length === 0) {
        alert('Не выбрано вложений для удаления');
        return;
    }

    const data = {
        taskIds: selectedTask
    };
    const csrfToken = document.querySelector('input[name="_csrf"]').value;

    fetch(urlBase, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if(!response.ok){
            throw new Error('Ошибка ответа сервера')
        }

        window.location.reload();
    });
}
document.getElementById('uploadLink').addEventListener('click', function(event) {
    event.preventDefault();
    document.getElementById('fileInput').click();
});

document.getElementById('fileInput').addEventListener('change', function(event) {
    const urlBase = window.location.origin + window.location.pathname + '/upload';
    const files = event.target.files;
    console.log('Выбрано файлов:', files);

    // Создаем объект FormData и добавляем выбранные файлы
    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
    }

    // Отправка запроса на сервер
    fetch(urlBase, {
        method: 'POST',
        body: formData,
        headers: {
            'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]').value  // Добавление CSRF-токена в заголовок
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Запрос не отправлен');
        }
        window.location.reload();
    });
});
