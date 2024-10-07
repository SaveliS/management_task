window.onload = function(){
    const messageElement = document.getElementById('messageFile');
    if(messageElement){
        setTimeout(() => {
            messageElement.style.display = 'none';
        },15000);
    }
}
document.getElementById('category').addEventListener('change', handleCategoryChange);
document.addEventListener('DOMContentLoaded', loadHiddenInput);
function loadHiddenInput () {
    var selectedGroupsInput = document.createElement('input');
    selectedGroupsInput.type = 'hidden';
    selectedGroupsInput.name = 'selectedGroups';
    selectedGroupsInput.id = 'selectedGroups';
    selectedGroupsInput.value = '';

    var selectedUsersInput = document.createElement('input');
    selectedUsersInput.type = 'hidden';
    selectedUsersInput.name = 'selectedUsers';
    selectedUsersInput.id = 'selectedUsers';
    selectedUsersInput.value = '';

    document.querySelector('form').appendChild(selectedGroupsInput);
    document.querySelector('form').appendChild(selectedUsersInput);
}

function updateResponsibleList(value){
    const ulElements = document.getElementById('list-responsible');

    if(ulElements){
        const liElements = ulElements.querySelectorAll('li');

        liElements.forEach(li => {
            const input = li.querySelector('input[type="hidden"]');

            if(input && input.value == value){
                return false;
            }else{
                return true;
            }
        });
    }
}

function handleCategoryChange() {
    var selected = document.getElementById('category').value;
    document.getElementById('groups_selected').style.display = (selected === 'groups') ? 'block' : 'none';
    document.getElementById('users_selected').style.display = (selected === 'users') ? 'block' : 'none';
}

function addGroup() {
    const groupSelect = document.getElementById('group');
    const selectedGroup = groupSelect.options[groupSelect.selectedIndex];
    if (selectedGroup.value !== "") {
        const ul = document.getElementById('list-responsible');
        const li = document.createElement('li');
        li.innerHTML = `${selectedGroup.text} <button onclick="this.parentElement.remove()">Удалить</button>`;
        ul.appendChild(li);

        addToHiddenInput('group', selectedGroup.value);
    }
}

function addUser() {
    const userSelect = document.getElementById('user');
    const selectedUser = userSelect.options[userSelect.selectedIndex];
    const isCorrectValue = updateResponsibleList(selectedUser.value);
    if(isCorrectValue == false){
        return;
    }
    if (selectedUser.value !== "") {
        const ul = document.getElementById('list-responsible');
        const li = document.createElement('li');
        li.innerHTML = `${selectedUser.text} <button onclick="this.parentElement.remove()">Удалить</button>`;
        ul.appendChild(li);

        addToHiddenInput('user', selectedUser.value);
    }
}

function addToHiddenInput(type, value) {
    var input = document.getElementById((type === 'group') ? 'selectedGroups' : 'selectedUsers');
    var currentValue = input.value;

    if (currentValue) {
        input.value = currentValue + ',' + value;
    } else {
        input.value = value;
    }
}

let selectedFiles = [];

function updateFileList() {
    const input = document.getElementById('fileUpload');
    const fileList = document.getElementById('file-list');

    selectedFiles = Array.from(input.files);

    fileList.innerHTML = ''; // Очищаем список перед добавлением новых файлов

    selectedFiles.forEach((file, index) => {
        const listItem = document.createElement('li');
        listItem.textContent = file.name;

        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Удалить';
        deleteButton.onclick = () => removeFile(index);

        listItem.appendChild(deleteButton);
        fileList.appendChild(listItem);
    });
}

function removeFile(index) {
    selectedFiles.splice(index, 1); // Удаляем файл из массива

    const input = document.getElementById('fileUpload');
    const dt = new DataTransfer();

    // Обновляем input.files, исключая удаленный файл
    selectedFiles.forEach(file => dt.items.add(file));
    input.files = dt.files;

    updateFileList(); // Обновляем список
}
function submitForm() {
    const selectElement = document.getElementById("task-template");
    const templateId = selectElement.value;
    const csrfToken = document.querySelector('input[name="_csrf"]').value;

    fetch('/tasks/new', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': csrfToken
        },
        body: `templateId=${templateId}`
    })
    .then(response => response.text())
    .then(data => {
        document.open();
        document.write(data);
        document.close();
        loadHiddenInput();
        attachCategoryEvent();
    })
    .catch(error => console.error('Error:', error));
}

function attachCategoryEvent() {
    const categorySelect = document.getElementById("category");
    if (categorySelect) {
        categorySelect.addEventListener("change", handleCategoryChange);
    }
}
// document.addEventListener("DOMContentLoaded", function() {
//     attachCategoryEvent();
// });
