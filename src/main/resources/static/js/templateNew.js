document.addEventListener('DOMContentLoaded', function() {
    let previousElement = null;
    // Обработчик события изменения выбора
    document.getElementById('category').addEventListener('change', function() {
        // Получение выбранного значения
        var selectedValue = this.value + "_selected";
        console.log('Выбранное значение:', selectedValue);
        var showElement = document.getElementById(selectedValue);
        // Скрытие предыдущего элемента, если он существует
        if (previousElement && previousElement !== showElement) {
            previousElement.style.display = "none";
        }
        // Отображение текущего выбранного элемента
        if (showElement) {
            showElement.style.display = "";
        }
        // Запомнить текущий элемент как предыдущий
        previousElement = showElement;
    });
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
});

let selectedGroupListId = [];
let selectedUserListId = [];

function addGroup() {
    var groupSelect = document.getElementById('group');
    var selectedGroup = groupSelect.options[groupSelect.selectedIndex];
    if(!selectedGroupListId.includes(selectedGroup.value)){
        updateResponsibleList('group', selectedGroup);
        selectedGroupListId.push(selectedGroup.value);
    }
}

function addUser() {
    var userSelect = document.getElementById('user');
    var selectedUser = userSelect.options[userSelect.selectedIndex];
    if(!selectedUserListId.includes(selectedUser.value)){
        updateResponsibleList('user', selectedUser);
        selectedUserListId.push(selectedUser.value);
    }
}

function updateResponsibleList(type, selectedResponsible) {
    if (selectedResponsible.value) {
        var groupList = document.getElementById('list-responsible');
        var listItem = document.createElement('li');
        listItem.textContent = selectedResponsible.text;
        listItem.dataset.value = selectedResponsible.value;
       // Кнопка удаления
        var removeButton = document.createElement('button');
        removeButton.textContent = 'Удалить';
        removeButton.onclick = function() {
            groupList.removeChild(listItem);
            removeFromHiddenInput(type, selectedResponsible.value);
        };

        listItem.appendChild(removeButton);
        groupList.appendChild(listItem);

       // Добавление значения в скрытый input
        addToHiddenInput(type, selectedResponsible.value);
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
function removeFromHiddenInput(type, value) {
    var input = document.getElementById((type === 'group') ? 'selectedGroups' : 'selectedUsers');
    var currentValue = input.value.split(',');
    if(type === 'group'){
        var groupIndex = selectedGroupListId.indexOf(value);
        if(groupIndex > -1){
            selectedGroupListId.splice(groupIndex, 1);
        }
    }else{
        var userIndex = selectedUserListId.indexOf(value);
        if(userIndex > -1){
            selectedUserListId.splice(userIndex, 1);
        }
    }
    // Убираем значение
    var index = currentValue.indexOf(value);
    if (index > -1) {
        currentValue.splice(index, 1);
    }

    // Обновляем значение input
    input.value = currentValue.join(',');
}
function addAttributeTask() {
    const container = document.getElementById('group-container-attribute'); // выбор списка ul для добавления элементов
    const index = container.querySelectorAll('li').length; // рассчитываем количество уже существующих li

    const newElement = `
        <li class="group-attribute-li">
            <label for="attribute_name_${index}">Название:</label>
            <input type="text" name="attributeTask[${index}].attributeTaskKey.attribute_name" id="attribute_name_${index}" />
            <label for="attribute_value_${index}">Значение:</label>
            <input type="text" name="attributeTask[${index}].attribute_value" id="attribute_value_${index}" />
            <button type="button" class="delete-button" onclick="removeAttributeTask(this)">Удалить</button>
        </li>
    `;
    
    container.insertAdjacentHTML('beforeend', newElement); // добавляем новый элемент в список ul
}

function removeAttributeTask(button) {
    const listItem = button.closest('li'); // находим ближайший элемент li
    listItem.remove(); // удаляем его
}

// function addAttributeTask() {
//     const container = document.getElementById('group-container-attribute'); // выбор контейнера с ID 'group-container-attribute'
//     const index = document.querySelectorAll('.group-attribute').length; // находим количество существующих элементов

//     const newElement = `
//         <div class="group-attribute">
//             <hr>
//             <div class="form-group">
//                 <label for="attribute_name_${index}">Название атрибута:</label>
//                 <input type="text" name="attributeTask[${index}].attributeTaskKey.attribute_name" id="attribute_name_${index}" />
//                 <label for="attribute_value_${index}">Значение атрибута:</label>
//                 <input type="text" name="attributeTask[${index}].attribute_value" id="attribute_value_${index}" />
//             </div>
//             <button type="button" class="delete-button" onclick="removeAttributeTask(this)">Удалить атрибут</button>
//         </div>
//     `;
    
//     container.insertAdjacentHTML('beforeend', newElement); // добавляем новый элемент в нужный контейнер
// }

// function removeAttributeTask(button) {
//     const taskElement = button.closest('.group-attribute'); // находим родительский элемент
//     taskElement.remove(); // удаляем родительский элемент
// }